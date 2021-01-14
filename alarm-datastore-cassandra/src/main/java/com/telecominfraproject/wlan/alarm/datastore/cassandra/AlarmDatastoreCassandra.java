package com.telecominfraproject.wlan.alarm.datastore.cassandra;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;
import com.telecominfraproject.wlan.alarm.datastore.AlarmDatastore;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtop
 *
 */
@Component
public class AlarmDatastoreCassandra implements AlarmDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmDatastoreCassandra.class);

    private static final String[] ALL_COLUMNS_LIST = {        
            
            //TODO: add colums from properties Alarm in here
            "customerId",
            "equipmentId",
            "alarmCode",
            "createdTimestamp",
            "originatorType",
            "severity",
            "scopeType",
            "scopeId",
            "details",
            "acknowledged",
            
            "lastModifiedTimestamp"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
        private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList( 
        		"customerId",
                "equipmentId",
                "alarmCode",
                "createdTimestamp"));
        
        private static final String TABLE_NAME = "alarm";
        private static final String ALL_COLUMNS;

        private static final String ALL_COLUMNS_FOR_INSERT; 
        private static final String BIND_VARS_FOR_INSERT;
        private static final String ALL_COLUMNS_UPDATE;
        
        static{
            StringBuilder strbAllColumns = new StringBuilder(1024);
            StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
            StringBuilder strbBindVarsForInsert = new StringBuilder(128);
            StringBuilder strbColumnsForUpdate = new StringBuilder(512);
            for(String colName: ALL_COLUMNS_LIST){

                strbAllColumns.append(colName).append(","); 
                
                if(!columnsToSkipForInsert.contains(colName)){
                    strbAllColumnsForInsert.append(colName).append(",");
                    strbBindVarsForInsert.append("?,");
                }
                
                if(!columnsToSkipForUpdate.contains(colName)){
                    strbColumnsForUpdate.append(colName).append("=?,");
                }
            }

            // remove trailing ','
            strbAllColumns.deleteCharAt(strbAllColumns.length() - 1);
            strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
            strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
            strbColumnsForUpdate.deleteCharAt(strbColumnsForUpdate.length() - 1);

            ALL_COLUMNS = strbAllColumns.toString();
            ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
            BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
            ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();
            
        }
        

        private static final String CQL_GET_BY_ID =
            "select " + ALL_COLUMNS +
            " from "+TABLE_NAME+" " +
            " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?";

        private static final String CQL_GET_ALL_NO_DETAILS = 
                "select customerId, equipmentId, alarmCode, createdTimestamp from alarm " ; 

        private static final String CQL_GET_BY_CUSTOMER_ID = 
        		"select " + ALL_COLUMNS +
        		" from " + TABLE_NAME + " " + 
        		" where customerId = ? ";

        private static final String CQL_GET_LASTMOD_BY_ID =
            "select lastModifiedTimestamp " +
            " from "+TABLE_NAME+" " +
            " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?";

        private static final String CQL_INSERT =
            "insert into "+TABLE_NAME+" ( " 
            + ALL_COLUMNS_FOR_INSERT
            + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

        private static final String CQL_DELETE =
            "delete from "+TABLE_NAME+
            " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?";

        private static final String CQL_DELETE_BY_EQUIPMENT =
                "delete from "+TABLE_NAME+
                " where  customerId = ? and equipmentId = ? ";

        private static final String CQL_UPDATE =
            "update "+TABLE_NAME+" set "
            + ALL_COLUMNS_UPDATE +
            " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?"
            + " IF lastModifiedTimestamp = ? " 
            ;
    
        private static final String CQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_HEADER = "select equipmentId, alarmCode, count(1) from alarm where customerId = ? ";
        private static final String CQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_FOOTER = " group by equipmentId, alarmCode";

        //Cassandra has a difficulty running this:
        // message="Group by currently only support groups of columns following their declared order in the PRIMARY KEY"
        //private static final String CQL_COUNTS_BY_ALARM_CODE_HEADER = "select alarmCode, count(1) from alarm where customerId = ? ";
        //private static final String CQL_COUNTS_BY_ALARM_CODE_FOOTER = " group by alarmCode";
        
        private static final String CQL_INCREMENT_EQUIPMENT_ALARM_COUNT = "update alarm_counts_by_equipment set alarmCount = alarmCount + 1 where  customerId = ? and equipmentId = ? and alarmCode = ? ";
        private static final String CQL_DECREMENT_EQUIPMENT_ALARM_COUNT = "update alarm_counts_by_equipment set alarmCount = alarmCount - 1 where  customerId = ? and equipmentId = ? and alarmCode = ? ";
        
        private static final String CQL_GET_EQUIPMENT_ALARM_COUNT_BY_CUSTOMER_ID = "select equipmentId, alarmCode, alarmCount from alarm_counts_by_equipment where customerId = ? ";

        private static final String CQL_INCREMENT_CUSTOMER_ALARM_COUNT = "update alarm_counts_by_customer set alarmCount = alarmCount + 1 where  customerId = ? and alarmCode = ? ";
        private static final String CQL_DECREMENT_CUSTOMER_ALARM_COUNT = "update alarm_counts_by_customer set alarmCount = alarmCount - 1 where  customerId = ?  and alarmCode = ? ";
        
        private static final String CQL_GET_CUSTOMER_ALARM_COUNT_BY_CUSTOMER_ID = "select alarmCode, alarmCount from alarm_counts_by_customer where customerId = ? ";
        
        private static final String CQL_UPDATE_CUSTOMER_ALARM_COUNT = "update alarm_counts_by_customer set alarmCount = alarmCount + ? where  customerId = ? and alarmCode = ? ";
        private static final String CQL_UPDATE_EQUIPMENT_ALARM_COUNT = "update alarm_counts_by_equipment set alarmCount = alarmCount + ? where  customerId = ? and equipmentId = ? and alarmCode = ? ";


    private static final RowMapper<Alarm> alarmRowMapper = new AlarmRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_getOrNull;
    private PreparedStatement preparedStmt_getAllNoDetails;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_getLastmod;
	private PreparedStatement preparedStmt_delete;
	private PreparedStatement preparedStmt_deleteByEquipment;
	private PreparedStatement preparedStmt_incrementAlarmCountByEquipment;
	private PreparedStatement preparedStmt_decrementAlarmCountByEquipment;

	private PreparedStatement preparedStmt_incrementAlarmCountByCustomer;
	private PreparedStatement preparedStmt_decrementAlarmCountByCustomer;
	
    private PreparedStatement preparedStmt_updateAlarmCountByEquipment;
    private PreparedStatement preparedStmt_updateAlarmCountByCustomer;
	

	@PostConstruct
	private void postConstruct(){
		preparedStmt_getOrNull = cqlSession.prepare(CQL_GET_BY_ID);
        preparedStmt_getAllNoDetails = cqlSession.prepare(CQL_GET_ALL_NO_DETAILS);
		preparedStmt_create = cqlSession.prepare(CQL_INSERT);
		preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
		preparedStmt_getLastmod = cqlSession.prepare(CQL_GET_LASTMOD_BY_ID);
		preparedStmt_delete = cqlSession.prepare(CQL_DELETE);
		preparedStmt_deleteByEquipment = cqlSession.prepare(CQL_DELETE_BY_EQUIPMENT);
		preparedStmt_incrementAlarmCountByEquipment = cqlSession.prepare(CQL_INCREMENT_EQUIPMENT_ALARM_COUNT);
		preparedStmt_decrementAlarmCountByEquipment = cqlSession.prepare(CQL_DECREMENT_EQUIPMENT_ALARM_COUNT);
		preparedStmt_incrementAlarmCountByCustomer= cqlSession.prepare(CQL_INCREMENT_CUSTOMER_ALARM_COUNT);
		preparedStmt_decrementAlarmCountByCustomer = cqlSession.prepare(CQL_DECREMENT_CUSTOMER_ALARM_COUNT);

		preparedStmt_updateAlarmCountByEquipment = cqlSession.prepare(CQL_UPDATE_EQUIPMENT_ALARM_COUNT);
		preparedStmt_updateAlarmCountByCustomer= cqlSession.prepare(CQL_UPDATE_CUSTOMER_ALARM_COUNT);

	}
	
	@Override
	public Alarm create(Alarm alarm) {
		
        final long ts = System.currentTimeMillis();
        if(alarm.getCreatedTimestamp()<=0) {
        	alarm.setCreatedTimestamp(ts);
        }
        
        alarm.setLastModifiedTimestamp(ts);

		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from Alarm here 
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                
                alarm.getOriginatorType().getId(),
                alarm.getSeverity().getId(),
                alarm.getScopeType().getId(),                        
                alarm.getScopeId(),
                
              	(alarm.getDetails()!=null) ? ByteBuffer.wrap(alarm.getDetails().toZippedBytes()) : null,
              	
              	alarm.isAcknowledged(),
                alarm.getLastModifiedTimestamp()
				
				));
		
        LOG.debug("Stored Alarm {}", alarm);

        //update alarm count
		cqlSession.execute(preparedStmt_incrementAlarmCountByEquipment.bind(
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId()
                ));

		cqlSession.execute(preparedStmt_incrementAlarmCountByCustomer.bind(
                alarm.getCustomerId(),
                alarm.getAlarmCode().getId()
                ));

        return alarm.clone();
	}

	
	@SuppressWarnings("unused")
	private void example_execute_cql_script() {
        String fileContentStr;

		try {
	        Object fileContent = ResourceUtils.getURL("classpath:schema-cassandra.cql").getContent();
			
			if (fileContent instanceof String) {
				fileContentStr = (String) fileContent;
			} else if (fileContent instanceof InputStream) {
				fileContentStr = StreamUtils.copyToString((InputStream) fileContent, StandardCharsets.UTF_8);
			} else {
				throw new IllegalStateException("Cannot read schema-cassandra.cql - expected either String or InputStream, instead got "+ fileContent);
			}
		}catch(IOException e) {
			throw new IllegalStateException("Cannot read schema-cassandra.cql");
		}
		
		String[] statements = fileContentStr.split(";");
		
		Arrays.stream(statements).map(statement -> { return statement.replace("\n", "") + ";"; }).forEach(cqlSession::execute);
	}
	
	
	@Override
	public Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        LOG.debug("Looking up Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);

		ResultSet rs = cqlSession.execute(preparedStmt_getOrNull.bind(
				customerId, equipmentId, alarmCode.getId(), createdTimestamp
				));
		
		Row row = rs.one();

		if(row!=null) {
			Alarm alarm = alarmRowMapper.mapRow(row);
            LOG.debug("Found Alarm {}", alarm);
            
            return alarm;
		} else {
            LOG.debug("Could not find Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);
            return null;			
		}

	}

	@Override
	public Alarm update(Alarm alarm) {
        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = alarm.getLastModifiedTimestamp();
        
		ResultSet rs = cqlSession.execute(preparedStmt_update.bind(

                //TODO: add remaining properties from Alarm here
        		alarm.getOriginatorType().getId(),
                alarm.getSeverity().getId(),
                alarm.getScopeType().getId(),
                alarm.getScopeId(),
                (alarm.getDetails()!=null) ? ByteBuffer.wrap(alarm.getDetails().toZippedBytes()) : null ,
                alarm.isAcknowledged(),
                                
                newLastModifiedTs,
                
                // use id for update operation
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs
                
        ));
        
		
        if(!rs.wasApplied()){
            
                //find out if record could not be updated because it does not exist or because it was modified concurrently
            	rs = cqlSession.execute(preparedStmt_getLastmod.bind(
                        alarm.getCustomerId(),
                        alarm.getEquipmentId(),
                        alarm.getAlarmCode().getId(),
                        alarm.getCreatedTimestamp()
                        ) );
            	Row row = rs.one();
            	
            	if(row!=null) {
	                long recordTimestamp = row.getLong(0);
	                
	                LOG.debug("Concurrent modification detected for Alarm with id {} {} {} {} expected version is {} but version in db was {}", 
	                		alarm.getCustomerId(),
	                        alarm.getEquipmentId(),
	                        alarm.getAlarmCode().getId(),
	                        alarm.getCreatedTimestamp(),
	                        incomingLastModifiedTs,
	                        recordTimestamp
	                        );
	                throw new DsConcurrentModificationException("Concurrent modification detected for Alarm with id " + 
	                        alarm.getCustomerId() + " " +
	                        alarm.getEquipmentId() + " " +
	                        alarm.getAlarmCode().getId() + " " +
	                        alarm.getCreatedTimestamp() + " " 
	                        +" expected version is " + incomingLastModifiedTs
	                        +" but version in db was " + recordTimestamp
	                        );
            } else {
                LOG.debug("Cannot find Alarm for {} {} {} {} ", alarm.getCustomerId(),
                        alarm.getEquipmentId(),
                        alarm.getAlarmCode().getId(),
                        alarm.getCreatedTimestamp());
                throw new DsEntityNotFoundException("Alarm not found " +  
                        alarm.getCustomerId() + " " +
                        alarm.getEquipmentId() + " " +
                        alarm.getAlarmCode().getId() + " " +
                        alarm.getCreatedTimestamp());
            }
        }
        

        //make a copy so that we don't accidentally update caller's version by reference
        Alarm alarmCopy = alarm.clone();
        alarmCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Alarm {}", alarmCopy);
        
        return alarmCopy;
    }

	@Override
	public Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        Alarm ret = getOrNull(customerId, equipmentId, alarmCode, createdTimestamp);
        
        if(ret==null) {
        	throw new DsEntityNotFoundException("Cannot find alarm to be deleted");
        }
        
        cqlSession.execute(preparedStmt_delete.bind(customerId, equipmentId, alarmCode.getId(), createdTimestamp));
        LOG.debug("Deleted Alarm {}", ret);
        
        //update alarm count
		cqlSession.execute(preparedStmt_decrementAlarmCountByEquipment.bind(
                ret.getCustomerId(),
                ret.getEquipmentId(),
                ret.getAlarmCode().getId()
                ));

		cqlSession.execute(preparedStmt_decrementAlarmCountByCustomer.bind(
                ret.getCustomerId(),
                ret.getAlarmCode().getId()
                ));

        return ret;
	}

	@Override
	//used mostly in unit tests to clean up data
	public List<Alarm> delete(int customerId, long equipmentId) {
        List<Alarm> ret = get(customerId, Collections.singleton(equipmentId), null, -1);
        
        cqlSession.execute(preparedStmt_deleteByEquipment.bind(customerId, equipmentId));

        LOG.debug("Deleted Alarms {}", ret);
        
        ret.forEach(al -> {
        	//TODO: replace with one update of a counter
            //update alarm count
    		cqlSession.execute(preparedStmt_decrementAlarmCountByEquipment.bind(
                    al.getCustomerId(),
                    al.getEquipmentId(),
                    al.getAlarmCode().getId()
                    ));
    		
    		cqlSession.execute(preparedStmt_decrementAlarmCountByCustomer.bind(
                    al.getCustomerId(),
                    al.getAlarmCode().getId()
                    ));        	

        });
        
        return ret;
	}
	
	@Override
	public void resetAlarmCounters() {
        LOG.debug("Resetting Alarm counters ");
	    
        //count real alarms per-customer and per equipment
        Map<Integer, AlarmCounts> alarmCountsPerCustomerMap = new HashMap<>();

        //select customerId, equipmentId, alarmCode, createdTimestamp from alarm
        ResultSet rs = cqlSession.execute(preparedStmt_getAllNoDetails.bind());
        
        rs.forEach(row -> {
            int customerId = row.getInt(0);
            long equipmentId = row.getLong(1);
            AlarmCode alarmCode = AlarmCode.getById(row.getInt(2));

            AlarmCounts alarmCounts = alarmCountsPerCustomerMap.get(customerId);
            if(alarmCounts == null) {
                alarmCounts = new AlarmCounts();
                alarmCounts.setCustomerId(customerId);
                alarmCountsPerCustomerMap.put(customerId, alarmCounts);
            }

            alarmCounts.addToCounter(equipmentId, alarmCode, 1);
            
        });
        
        //read existing counters alarms per-customer
        Map<Integer, AlarmCounts> existingCustomerCountsPerCustomerMap = new HashMap<>();
        rs = cqlSession.execute(cqlSession.prepare("select customerid, alarmcode, alarmcount from alarm_counts_by_customer ").bind());
        
        rs.forEach(row -> {
            int customerId = row.getInt(0);
            AlarmCode alarmCode = AlarmCode.getById(row.getInt(1));
            long count = row.getLong(2);

            AlarmCounts alarmCounts = existingCustomerCountsPerCustomerMap.get(customerId);
            if(alarmCounts == null) {
                alarmCounts = new AlarmCounts();
                alarmCounts.setCustomerId(customerId);
                existingCustomerCountsPerCustomerMap.put(customerId, alarmCounts);
            }

            alarmCounts.addToCounter(0, alarmCode, (int) count);
            
        });

        //read existing counters alarms per equipment
        Map<Integer, AlarmCounts> existingEquipmentCountsPerCustomerMap = new HashMap<>();
        rs = cqlSession.execute(cqlSession.prepare("select customerid, equipmentid, alarmcode, alarmcount from alarm_counts_by_equipment ").bind());
        
        rs.forEach(row -> {
            int customerId = row.getInt(0);
            long equipmentId = row.getLong(1);
            AlarmCode alarmCode = AlarmCode.getById(row.getInt(2));
            long count = row.getLong(3);

            AlarmCounts alarmCounts = existingEquipmentCountsPerCustomerMap.get(customerId);
            if(alarmCounts == null) {
                alarmCounts = new AlarmCounts();
                alarmCounts.setCustomerId(customerId);
                existingEquipmentCountsPerCustomerMap.put(customerId, alarmCounts);
            }

            alarmCounts.addToCounter(equipmentId, alarmCode, (int) count);
            
        });

        //set existing counters per customer - first to 0, then to new computed values
        existingCustomerCountsPerCustomerMap.values().forEach(customerCounts -> {
            AlarmCounts realAlarmCounts = alarmCountsPerCustomerMap.get(customerCounts.getCustomerId());
            customerCounts.getTotalCountsPerAlarmCodeMap().forEach((alarmCode, existingCount) -> {
                int realCount = realAlarmCounts != null ? realAlarmCounts.getTotalCountsPerAlarmCodeMap().getOrDefault(alarmCode, new AtomicInteger(0)).get(): 0;
                //update alarm_counts_by_customer set alarmCount = alarmCount + ? where  customerId = ? and alarmCode = ? 
                cqlSession.execute(preparedStmt_updateAlarmCountByCustomer.bind( (long) (0L - existingCount.get() + realCount), customerCounts.getCustomerId(), alarmCode.getId()));
            });            
        });

        //set existing counters per equipment - first to 0, then to new computed values
        existingEquipmentCountsPerCustomerMap.values().forEach(customerCounts -> {
            int customerId = customerCounts.getCustomerId();
            AlarmCounts realAlarmCounts = alarmCountsPerCustomerMap.get(customerId);
            
            customerCounts.getCountsPerEquipmentIdMap().forEach((eqId, perAlarmCodeMap) -> {
                
                perAlarmCodeMap.forEach((alarmCode, existingCount) -> {
                    int realCount = 0;
                    if(realAlarmCounts != null && realAlarmCounts.getCountsPerEquipmentIdMap().get(eqId)!=null ) {
                        realCount  = realAlarmCounts.getCountsPerEquipmentIdMap().get(eqId).getOrDefault(alarmCode, new AtomicInteger(0)).get();
                    }
                    //update alarm_counts_by_equipment set alarmCount = alarmCount + ? where  customerId = ? and equipmentId = ? and alarmCode = ?  
                    cqlSession.execute(preparedStmt_updateAlarmCountByEquipment.bind( (long) ( 0L - existingCount.get() + realCount), customerId, eqId, alarmCode.getId()));
                });            
                
            });
            
        });
        
        //process new customer counts that are not present in existing counts
        alarmCountsPerCustomerMap.values().forEach(customerCounts -> {
            AlarmCounts existingAlarmCounts = existingCustomerCountsPerCustomerMap.get(customerCounts.getCustomerId());
            if(existingAlarmCounts == null) {
                customerCounts.getTotalCountsPerAlarmCodeMap().forEach((alarmCode, newCount) -> {
                    //update alarm_counts_by_customer set alarmCount = alarmCount + ? where  customerId = ? and alarmCode = ? 
                    cqlSession.execute(preparedStmt_updateAlarmCountByCustomer.bind((long) newCount.get(), customerCounts.getCustomerId(), alarmCode.getId()));
                });
            }
        });
        

        //process new equipment counts that are not present in existing counts
        alarmCountsPerCustomerMap.values().forEach(customerCounts -> {
            int customerId = customerCounts.getCustomerId();
            AlarmCounts existingAlarmCounts = existingEquipmentCountsPerCustomerMap.get(customerId);
            
            customerCounts.getCountsPerEquipmentIdMap().forEach((eqId, perAlarmCodeMap) -> {
                if(existingAlarmCounts == null || existingAlarmCounts.getCountsPerEquipmentIdMap().get(eqId) == null) {                
                    perAlarmCodeMap.forEach((alarmCode, existingCount) -> {
                        //update alarm_counts_by_equipment set alarmCount = alarmCount + ? where  customerId = ? and equipmentId = ? and alarmCode = ?  
                        cqlSession.execute(preparedStmt_updateAlarmCountByEquipment.bind((long) existingCount.get(), customerId, eqId, alarmCode.getId()));
                    });
                }
                
            });
            
        });

        
        LOG.debug("Completed resetting Alarm counters ");
	}

	@Override
	public List<Alarm> get(int customerId, Set<Long> equipmentIds, Set<AlarmCode> alarmCodes,
			long createdAfterTimestamp) {
		
    	if(equipmentIds == null || equipmentIds.isEmpty()) {
    		throw new IllegalArgumentException("equipmentIds must be provided");
    	}

    	if(alarmCodes==null || alarmCodes.isEmpty()) {
    		//if alarm codes not specified (means all) - explicitly list all of them, otherwise the following exception if thrown:
    		//com.datastax.oss.driver.api.core.servererrors.InvalidQueryException: 
    		//	PRIMARY KEY column "createdtimestamp" cannot be restricted as preceding column "alarmcode" is not restricted
    		alarmCodes = new HashSet<>(Arrays.asList(AlarmCode.validValues()));
    	}
    	
        LOG.debug("Looking up Alarms for customer {} equipment {} codes {} createdAfter {}", 
        		customerId, equipmentIds, alarmCodes, createdAfterTimestamp);

        String query = CQL_GET_BY_CUSTOMER_ID;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        
        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append("and equipmentId in (");
            for (int i = 0; i < equipmentIds.size(); i++) {
                strb.append("?");
                if (i < equipmentIds.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
        }
        
        //add alarmCodes filters
        if (alarmCodes != null && !alarmCodes.isEmpty()) {
        	alarmCodes.forEach(ac -> queryArgs.add(ac.getId()));

            StringBuilder strb = new StringBuilder(100);
            strb.append("and alarmCode in (");
            for (int i = 0; i < alarmCodes.size(); i++) {
                strb.append("?");
                if (i < alarmCodes.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
        }        

        if(createdAfterTimestamp > 0) {
        	query += " and createdTimestamp > ?" ;
        	queryArgs.add(createdAfterTimestamp);
        }

        List<Alarm> ret = new ArrayList<>();
        
        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfAlarmCodes
        PreparedStatement preparedStmt_getListForCustomer = cqlSession.prepare(query);
        
		ResultSet rs = cqlSession.execute(preparedStmt_getListForCustomer.bind(queryArgs.toArray() ));
		
		rs.forEach( row -> ret.add(alarmRowMapper.mapRow(row)) );
		
        LOG.debug("Found {} Alarms for customer {} equipment {} codes {} createdAfter {}",
        		ret.size(),
        		customerId, equipmentIds, alarmCodes, createdAfterTimestamp);

        return ret;
        
	}


	@Override
	public PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIds,
			Set<AlarmCode> alarmCodes, long createdAfterTimestamp, List<ColumnAndSort> sortBy,
			PaginationContext<Alarm> context) {
        PaginationResponse<Alarm> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Alarms for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Alarms for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        String query = CQL_GET_BY_CUSTOMER_ID;

    	if((alarmCodes==null || alarmCodes.isEmpty()) && createdAfterTimestamp>0) {
    		//if alarm codes not specified (means all) - explicitly list all of them, otherwise the following exception if thrown:
    		//com.datastax.oss.driver.api.core.servererrors.InvalidQueryException: 
    		//	PRIMARY KEY column "createdtimestamp" cannot be restricted as preceding column "alarmcode" is not restricted
    		alarmCodes = new HashSet<>(Arrays.asList(AlarmCode.validValues()));
    	}
    	
        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append("and equipmentId in (");
            for (int i = 0; i < equipmentIds.size(); i++) {
                strb.append("?");
                if (i < equipmentIds.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
        }
        
        //add alarmCodes filters
        if (alarmCodes != null && !alarmCodes.isEmpty()) {
        	alarmCodes.forEach(ac -> queryArgs.add(ac.getId()));

            StringBuilder strb = new StringBuilder(100);
            strb.append("and alarmCode in (");
            for (int i = 0; i < alarmCodes.size(); i++) {
                strb.append("?");
                if (i < alarmCodes.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
        }        

        if(createdAfterTimestamp > 0) {
        	query += " and createdTimestamp > ?" ;
        	queryArgs.add(createdAfterTimestamp);
        }
        
        // add sorting options for the query
        // Cassandra allows very limited support for ordering results
        // See https://cassandra.apache.org/doc/latest/cql/dml.html#select
        // In here allowed orderings are the order induced by the clustering columns and the reverse of that one.
        // also, order by with secondary indexes is not supported
        // ***** We will ignore the order supplied by the caller for this datastore


        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfAlarmCodes
        PreparedStatement preparedStmt_getPageForCustomer;
        try {
        	preparedStmt_getPageForCustomer = cqlSession.prepare(query);
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query, e);
        	throw e;
        }
                
        BoundStatement boundStmt = preparedStmt_getPageForCustomer.bind(queryArgs.toArray() );
        //have to do it this way - setPageSize creates new object
        boundStmt = boundStmt.setPageSize(context.getMaxItemsPerPage());
        
        if(context.getThirdPartyPagingState()!=null) {
        	ByteBuffer currentPagingState = ByteBuffer.wrap(context.getThirdPartyPagingState());
        	//have to do it this way - setPagingState creates new object
        	boundStmt = boundStmt.setPagingState(currentPagingState);
        }
        
		ResultSet rs = cqlSession.execute(boundStmt);
		ByteBuffer nextPagingState = rs.getExecutionInfo().getPagingState();

		List<Alarm> pageItems = new ArrayList<>();
		
		// iterate through the current page
		while (rs.getAvailableWithoutFetching() > 0) {
		  pageItems.add(alarmRowMapper.mapRow(rs.one()));
		}

        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find Alarms for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Alarms for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(nextPagingState!=null) {
        	ret.getContext().setThirdPartyPagingState(nextPagingState.array());
        } else {
        	ret.getContext().setThirdPartyPagingState(null);
        }
        
        // startAfterItem is not used in Cassandra datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        //in cassandra we will rely only on nextPagingState to set the lastPage indicator
        ret.getContext().setLastPage(false);

        if(nextPagingState == null) {
            //in cassandra, if there are no more pages available, the pagingState is returned as null by the driver
            //this overrides all other heuristics related to guessing the indication of the last page
            ret.getContext().setLastPage(true);
        }

        return ret;		
	}

	/**
	 * This method does not generate any performance warnings by the server side
	 * The downside is that the alarm counters may get out of sync once in a while.
	 * Maybe we can recalculate them periodically (or on demand) by executing getAlarmCounts_raw()
	 * TODO: this needs to be discussed with the team
	 * 
	 */
	@Override
	public AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIds, Set<AlarmCode> alarmCodes) {
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

		//build the query
		
		StringBuilder query = new StringBuilder();

        if (equipmentIds != null && !equipmentIds.isEmpty()) {
        	query.append(CQL_GET_EQUIPMENT_ALARM_COUNT_BY_CUSTOMER_ID);
        } else {
        	query.append(CQL_GET_CUSTOMER_ALARM_COUNT_BY_CUSTOMER_ID);
        }

		
        //add alarmCodes filters
		//we will do the client-side filtering for the AlarmCodes, see below

        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append("and equipmentId in (");
            for (int i = 0; i < equipmentIds.size(); i++) {
                strb.append("?");
                if (i < equipmentIds.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query.append(strb);
        }

        
		AlarmCounts alarmCounts = new AlarmCounts();
		alarmCounts.setCustomerId(customerId);

        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfAlarmCodes
        PreparedStatement preparedStmt_getCounts;
        try {
        	preparedStmt_getCounts = cqlSession.prepare(query.toString());
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query.toString(), e);
        	throw e;
        }
        
		ResultSet rs = cqlSession.execute(preparedStmt_getCounts.bind(queryArgs.toArray() ));
		
		int alarmCodeColIdx;
		int equipmentIdColIdx;
		int countColIdx;
		
		if (equipmentIds == null || equipmentIds.isEmpty()) {
			alarmCodeColIdx = 0;
			equipmentIdColIdx = 0;
			countColIdx = 1;
		} else {
			alarmCodeColIdx = 1;
			equipmentIdColIdx = 0;
			countColIdx = 2;
		}
		
		rs.forEach(row -> {
			//we will do the client-side filtering for the AlarmCodes, because querying for it as part of CQL does not seem to work
			//we can afford it because there are not that many different alarm codes in total, and during normal operations 
			//the amount of distinct alarm codes per equipment is very small
	        AlarmCode ac = AlarmCode.getById(row.getInt(alarmCodeColIdx));
	        
	        if (alarmCodes != null && !alarmCodes.isEmpty() && alarmCodes.contains(ac) 
	        		|| alarmCodes == null || alarmCodes.isEmpty() ) 
	        {
	    		if(equipmentIds == null || equipmentIds.isEmpty()) {
	    			alarmCounts.addToCounter(0, ac, (int) row.getLong(countColIdx));
	    		} else {
	    			alarmCounts.addToCounter(row.getLong(equipmentIdColIdx), ac, (int) row.getLong(countColIdx));
	    		}
	        }
		});

		return alarmCounts;		
	}
	
	public AlarmCounts getAlarmCounts_raw(int customerId, Set<Long> equipmentIds, Set<AlarmCode> alarmCodes) {
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

		//build the query
		
		StringBuilder query = new StringBuilder();

		query.append(CQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_HEADER);

		
        //add alarmCodes filters
		//we will do the client-side filtering for the AlarmCodes, see below

        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append("and equipmentId in (");
            for (int i = 0; i < equipmentIds.size(); i++) {
                strb.append("?");
                if (i < equipmentIds.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query.append(strb);
        }

        
		query.append(CQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_FOOTER);

		AlarmCounts alarmCounts = new AlarmCounts();
		alarmCounts.setCustomerId(customerId);

        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfAlarmCodes
        PreparedStatement preparedStmt_getCounts;
        try {
        	preparedStmt_getCounts = cqlSession.prepare(query.toString());
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query.toString(), e);
        	throw e;
        }
        
		ResultSet rs = cqlSession.execute(preparedStmt_getCounts.bind(queryArgs.toArray() ));
		
		rs.forEach(row -> {
			//we will do the client-side filtering for the AlarmCodes, because querying for it as part of CQL does not seem to work
			//we can afford it because there are not that many different alarm codes in total, and during normal operations 
			//the amount of distinct alarm codes per equipment is very small
	        AlarmCode ac = AlarmCode.getById(row.getInt(1));
	        
	        if (alarmCodes != null && !alarmCodes.isEmpty() && alarmCodes.contains(ac) 
	        		|| alarmCodes == null || alarmCodes.isEmpty() ) 
	        {
	    		if(equipmentIds == null || equipmentIds.isEmpty()) {
	    			alarmCounts.addToCounter(0, ac, (int) row.getLong(2));
	    		} else {
	    			alarmCounts.addToCounter(row.getLong(0), ac, (int) row.getLong(2));
	    		}
	        }
		});

		return alarmCounts;
	}

}
