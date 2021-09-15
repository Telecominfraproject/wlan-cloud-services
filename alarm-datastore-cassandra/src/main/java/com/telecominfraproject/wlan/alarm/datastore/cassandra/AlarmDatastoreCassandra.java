package com.telecominfraproject.wlan.alarm.datastore.cassandra;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils;
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

        private static final String CQL_GET_BY_CUSTOMER_ID = 
        		"select " + ALL_COLUMNS +
        		" from " + TABLE_NAME + " " + 
        		" where customerId = ? ";

        private static final String CQL_GET_ALL = 
                "select " + ALL_COLUMNS +
                " from " + TABLE_NAME + " ";

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
    
        private static final String CQL_INSERT_INTO_BY_ACKNOWLEDGED_TABLE = "insert into alarm_by_acknowledged(customerId, equipmentId, alarmCode, createdTimestamp, acknowledged) values ( ?, ?, ?, ?, ?) ";
        private static final String CQL_DELETE_FROM_BY_ACKNOWLEDGED_TABLE = "delete from alarm_by_acknowledged where customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ? and acknowledged = ? ";

        private static final String CQL_INSERT_INTO_BY_ACKNOWLEDGED_EQUIPMENTID_TABLE = "insert into alarm_by_acknowledged_equipmentId(customerId, equipmentId, alarmCode, createdTimestamp, acknowledged) values ( ?, ?, ?, ?, ?) ";
        private static final String CQL_DELETE_FROM_BY_ACKNOWLEDGED_EQUIPMENTID_TABLE = "delete from alarm_by_acknowledged_equipmentId where customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ? and acknowledged = ? ";

        private static final String CQL_INSERT_INTO_BY_ACKNOWLEDGED_ALARMCODE_TABLE = "insert into alarm_by_acknowledged_alarmCode(customerId, equipmentId, alarmCode, createdTimestamp, acknowledged) values ( ?, ?, ?, ?, ?) ";
        private static final String CQL_DELETE_FROM_BY_ACKNOWLEDGED_ALARMCODE_TABLE = "delete from alarm_by_acknowledged_alarmCode where customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ? and acknowledged = ? ";
        
        private static final String CQL_INSERT_INTO_BY_ACKNOWLEDGED_TIMESTAMP_TABLE = "insert into alarm_by_acknowledged_timestamp(customerId, equipmentId, alarmCode, createdTimestamp, acknowledged) values ( ?, ?, ?, ?, ?) ";
        private static final String CQL_DELETE_FROM_BY_ACKNOWLEDGED_TIMESTAMP_TABLE = "delete from alarm_by_acknowledged_timestamp where customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ? and acknowledged = ? ";

        //Cassandra has a difficulty running this:
        // message="Group by currently only support groups of columns following their declared order in the PRIMARY KEY"
        //private static final String CQL_COUNTS_BY_ALARM_CODE_HEADER = "select alarmCode, count(1) from alarm where customerId = ? ";
        //private static final String CQL_COUNTS_BY_ALARM_CODE_FOOTER = " group by alarmCode";
        
        // CQL statements for counts
        private static final String CQL_GET_CUSTOMER_ALARM_COUNT_BY_CUSTOMER_ID = "select equipmentId, alarmCode from alarm where customerId = ? ";
        
        private static final String CQL_GET_CUSTOMER_ACKNOWLEDGED_ALARM_COUNT_BY_CUSTOMER_ID = "select equipmentId, alarmCode from alarm_by_acknowledged where customerId = ? and acknowledged = ? ";
        

    private static final RowMapper<Alarm> alarmRowMapper = new AlarmRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_getOrNull;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_getLastmod;
	private PreparedStatement preparedStmt_delete;
	private PreparedStatement preparedStmt_deleteByEquipment;
    
    // filter by acknowledged statements
    private PreparedStatement preparedStmt_insertIntoAlarmByAcknowledged;
	private PreparedStatement preparedStmt_deleteFromAlarmByAcknowledged;
	private PreparedStatement preparedStmt_insertIntoAlarmByAcknowledgedEquipmentId;
	private PreparedStatement preparedStmt_deleteFromAlarmByAcknowledgedEquipmentId;
	private PreparedStatement preparedStmt_insertIntoAlarmByAcknowledgedAlarmCode;
	private PreparedStatement preparedStmt_deleteFromAlarmByAcknowledgedAlarmCode;
	private PreparedStatement preparedStmt_insertIntoAlarmByAcknowledgedTimestamp;
	private PreparedStatement preparedStmt_deleteFromAlarmByAcknowledgedTimestamp;

	@PostConstruct
	private void postConstruct(){
		preparedStmt_getOrNull = cqlSession.prepare(CQL_GET_BY_ID);
		preparedStmt_create = cqlSession.prepare(CQL_INSERT);
		preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
		preparedStmt_getLastmod = cqlSession.prepare(CQL_GET_LASTMOD_BY_ID);
		preparedStmt_delete = cqlSession.prepare(CQL_DELETE);
		preparedStmt_deleteByEquipment = cqlSession.prepare(CQL_DELETE_BY_EQUIPMENT);
		
		preparedStmt_insertIntoAlarmByAcknowledged = cqlSession.prepare(CQL_INSERT_INTO_BY_ACKNOWLEDGED_TABLE);
		preparedStmt_deleteFromAlarmByAcknowledged = cqlSession.prepare(CQL_DELETE_FROM_BY_ACKNOWLEDGED_TABLE);
		preparedStmt_insertIntoAlarmByAcknowledgedEquipmentId = cqlSession.prepare(CQL_INSERT_INTO_BY_ACKNOWLEDGED_EQUIPMENTID_TABLE);
		preparedStmt_deleteFromAlarmByAcknowledgedEquipmentId = cqlSession.prepare(CQL_DELETE_FROM_BY_ACKNOWLEDGED_EQUIPMENTID_TABLE);
		preparedStmt_insertIntoAlarmByAcknowledgedAlarmCode = cqlSession.prepare(CQL_INSERT_INTO_BY_ACKNOWLEDGED_ALARMCODE_TABLE);
		preparedStmt_deleteFromAlarmByAcknowledgedAlarmCode = cqlSession.prepare(CQL_DELETE_FROM_BY_ACKNOWLEDGED_ALARMCODE_TABLE);
		preparedStmt_insertIntoAlarmByAcknowledgedTimestamp = cqlSession.prepare(CQL_INSERT_INTO_BY_ACKNOWLEDGED_TIMESTAMP_TABLE);
		preparedStmt_deleteFromAlarmByAcknowledgedTimestamp = cqlSession.prepare(CQL_DELETE_FROM_BY_ACKNOWLEDGED_TIMESTAMP_TABLE);

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
		
		//insert entry into acknowledged tables
		cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledged.bind(
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                alarm.isAcknowledged()
                ));
		
		cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledgedEquipmentId.bind(
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                alarm.isAcknowledged()
                ));
		
		cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledgedAlarmCode.bind(
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                alarm.isAcknowledged()
                ));
		
		cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledgedTimestamp.bind(
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                alarm.isAcknowledged()
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
	    Alarm original = getOrNull(alarm.getCustomerId(), alarm.getEquipmentId(), alarm.getAlarmCode(), alarm.getCreatedTimestamp());
	    
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
        
        // if the acknowledged boolean value has been updated (typically false -> true, but opposite could happen too)
        // then we need to update the values in the supporting acknowledged tables as well. 
        // Since they are part of the primary key to properly filter, we cannot simply update the acknowledged value in these tables.
        // We need to delete those rows (in acknowledged tables) and recreate them with the new acknowledged value (in alarm)
        if (original.isAcknowledged() != alarm.isAcknowledged()) {
            //delete entry into acknowledged tables
            cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledged.bind(
                    original.getCustomerId(),
                    original.getEquipmentId(),
                    original.getAlarmCode().getId(),
                    original.getCreatedTimestamp(),
                    original.isAcknowledged()
                    ));
            
            cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedEquipmentId.bind(
                    original.getCustomerId(),
                    original.getEquipmentId(),
                    original.getAlarmCode().getId(),
                    original.getCreatedTimestamp(),
                    original.isAcknowledged()
                    ));
            
            cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedAlarmCode.bind(
                    original.getCustomerId(),
                    original.getEquipmentId(),
                    original.getAlarmCode().getId(),
                    original.getCreatedTimestamp(),
                    original.isAcknowledged()
                    ));
            
            cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedTimestamp.bind(
                    original.getCustomerId(),
                    original.getEquipmentId(),
                    original.getAlarmCode().getId(),
                    original.getCreatedTimestamp(),
                    original.isAcknowledged()
                    ));
            
            // recreate rows in supporting acknowledged tables with the new acknowledged value
            cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledged.bind(
                    alarm.getCustomerId(),
                    alarm.getEquipmentId(),
                    alarm.getAlarmCode().getId(),
                    alarm.getCreatedTimestamp(),
                    alarm.isAcknowledged()
                    ));
            
            cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledgedEquipmentId.bind(
                    alarm.getCustomerId(),
                    alarm.getEquipmentId(),
                    alarm.getAlarmCode().getId(),
                    alarm.getCreatedTimestamp(),
                    alarm.isAcknowledged()
                    ));
            
            cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledgedAlarmCode.bind(
                    alarm.getCustomerId(),
                    alarm.getEquipmentId(),
                    alarm.getAlarmCode().getId(),
                    alarm.getCreatedTimestamp(),
                    alarm.isAcknowledged()
                    ));
            
            cqlSession.execute(preparedStmt_insertIntoAlarmByAcknowledgedTimestamp.bind(
                    alarm.getCustomerId(),
                    alarm.getEquipmentId(),
                    alarm.getAlarmCode().getId(),
                    alarm.getCreatedTimestamp(),
                    alarm.isAcknowledged()
                    ));
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
		
		//delete entry into acknowledged tables
		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledged.bind(
                ret.getCustomerId(),
                ret.getEquipmentId(),
                ret.getAlarmCode().getId(),
                ret.getCreatedTimestamp(),
                ret.isAcknowledged()
                ));
		
		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedEquipmentId.bind(
                ret.getCustomerId(),
                ret.getEquipmentId(),
                ret.getAlarmCode().getId(),
                ret.getCreatedTimestamp(),
                ret.isAcknowledged()
                ));
		
		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedAlarmCode.bind(
                ret.getCustomerId(),
                ret.getEquipmentId(),
                ret.getAlarmCode().getId(),
                ret.getCreatedTimestamp(),
                ret.isAcknowledged()
                ));
		
		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedTimestamp.bind(
                ret.getCustomerId(),
                ret.getEquipmentId(),
                ret.getAlarmCode().getId(),
                ret.getCreatedTimestamp(),
                ret.isAcknowledged()
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
    		//delete entry into acknowledged tables
    		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledged.bind(
                    al.getCustomerId(),
                    al.getEquipmentId(),
                    al.getAlarmCode().getId(),
                    al.getCreatedTimestamp(),
                    al.isAcknowledged()
                    ));
    		
    		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedEquipmentId.bind(
                    al.getCustomerId(),
                    al.getEquipmentId(),
                    al.getAlarmCode().getId(),
                    al.getCreatedTimestamp(),
                    al.isAcknowledged()
                    ));
    		
    		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedAlarmCode.bind(
    				al.getCustomerId(),
    				al.getEquipmentId(),
    				al.getAlarmCode().getId(),
    				al.getCreatedTimestamp(),
    				al.isAcknowledged()
                    ));
    		
    		cqlSession.execute(preparedStmt_deleteFromAlarmByAcknowledgedTimestamp.bind(
    				al.getCustomerId(),
    				al.getEquipmentId(),
    				al.getAlarmCode().getId(),
    				al.getCreatedTimestamp(),
    				al.isAcknowledged()
                    ));

        });
        
        return ret;
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
	
    private static enum FilterOptions{ customer_only, customer_and_equipment, customer_and_alarmCode, customer_and_timestamp, customer_and_acknowledged }

	@Override
	public PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIds,
			Set<AlarmCode> alarmCodes, long createdAfterTimestamp, Boolean acknowledged, List<ColumnAndSort> sortBy,
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

        String query_head = CQL_GET_BY_CUSTOMER_ID;
        String query = "";

    	if((alarmCodes==null || alarmCodes.isEmpty()) && createdAfterTimestamp>0) {
    		//if alarm codes not specified (means all) - explicitly list all of them, otherwise the following exception if thrown:
    		//com.datastax.oss.driver.api.core.servererrors.InvalidQueryException: 
    		//	PRIMARY KEY column "createdtimestamp" cannot be restricted as preceding column "alarmcode" is not restricted
    		alarmCodes = new HashSet<>(Arrays.asList(AlarmCode.validValues()));
    	}
    	
    	FilterOptions filterOptions = FilterOptions.customer_only;
    	
        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);

            query += " and equipmentId in" + CassandraUtils.getBindPlaceholders(equipmentIds);

            filterOptions = FilterOptions.customer_and_equipment;
        }
        
        //add alarmCodes filters
        if (alarmCodes != null && !alarmCodes.isEmpty()) {
        	alarmCodes.forEach(ac -> queryArgs.add(ac.getId()));

        	query += " and alarmCode in" + CassandraUtils.getBindPlaceholders(alarmCodes);

        	filterOptions = FilterOptions.customer_and_alarmCode;
        }        

        if(createdAfterTimestamp > 0) {
        	query += " and createdTimestamp > ?" ;
        	queryArgs.add(createdAfterTimestamp);
        	filterOptions = FilterOptions.customer_and_timestamp;
        }
        
        if (acknowledged != null) {
        	queryArgs.clear();
        	queryArgs.add(customerId);
            queryArgs.add(acknowledged);
            query_head = "select customerId, equipmentId, alarmCode, createdTimestamp from alarm_by_acknowledged where customerId = ? ";
            
            if (equipmentIds != null && !equipmentIds.isEmpty()) {
            	query_head = "select customerId, equipmentId, alarmCode, createdTimestamp from alarm_by_acknowledged_equipmentId where customerId = ? ";
                queryArgs.addAll(equipmentIds);
            }
            if (alarmCodes != null && !alarmCodes.isEmpty()) {
            	query_head = "select customerId, equipmentId, alarmCode, createdTimestamp from alarm_by_acknowledged_alarmCode where customerId = ? ";
            	alarmCodes.forEach(ac -> queryArgs.add(ac.getId()));
            }
            if (createdAfterTimestamp > 0) {
            	query_head = "select customerId, equipmentId, alarmCode, createdTimestamp from alarm_by_acknowledged_timestamp where customerId = ? ";
                queryArgs.add(createdAfterTimestamp);
                
                if (equipmentIds != null && !equipmentIds.isEmpty()) {
                	query_head = "select customerId, equipmentId, alarmCode, createdTimestamp from alarm_by_acknowledged where customerId = ? ";
                }
            }
        	
        	query = " and acknowledged = ? " + query;
        	
        	filterOptions = FilterOptions.customer_and_acknowledged;
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
        	preparedStmt_getPageForCustomer = cqlSession.prepare(query_head + query);
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
		
		switch(filterOptions) {
		case customer_only:
		case customer_and_equipment:
		case customer_and_alarmCode:
		case customer_and_timestamp:
			// iterate through the current page
			while (rs.getAvailableWithoutFetching() > 0) {
			  pageItems.add(alarmRowMapper.mapRow(rs.one()));
			}
			break;
		case customer_and_acknowledged:
			while (rs.getAvailableWithoutFetching() > 0) {
				Row row = rs.one();
				long equipmentIdPostQuery = row.getLong("equipmentId");
				int alarmCodePostQuery = row.getInt("alarmCode");
				long createdTimestampPostQuery = row.getLong("createdTimestamp");
				Alarm alarmToAdd = getOrNull(customerId, equipmentIdPostQuery, AlarmCode.getById(alarmCodePostQuery), createdTimestampPostQuery);
                if (alarmToAdd != null) {
                    pageItems.add(alarmToAdd);
                }
			}
			break;
		default:
			LOG.warn("Unknown filter option:", filterOptions);
			throw new IllegalArgumentException("Unknown filter option " + filterOptions);
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
	public AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIds, Set<AlarmCode> alarmCodes, Boolean acknowledged) {
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

		//build the query
		
		StringBuilder query = new StringBuilder();

		if (acknowledged == null) {
        	query.append(CQL_GET_CUSTOMER_ALARM_COUNT_BY_CUSTOMER_ID);
		} else {
		    queryArgs.add(acknowledged);
            query.append(CQL_GET_CUSTOMER_ACKNOWLEDGED_ALARM_COUNT_BY_CUSTOMER_ID);
		}
        

		
        //add alarmCodes filters
		//we will do the client-side filtering for the AlarmCodes, see below

        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);
            
            if (equipmentIds.size() == 1) {
                query.append("and equipmentId = ? ");
            } else {
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
        }

        
		AlarmCounts alarmCounts = new AlarmCounts();
		alarmCounts.setCustomerId(customerId);
		alarmCounts.setAcknowledged(acknowledged);

        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfAlarmCodes
        PreparedStatement preparedStmt_getCounts;
        try {
        	preparedStmt_getCounts = cqlSession.prepare(query.toString());
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query.toString(), e);
        	throw e;
        }
        
		ResultSet rs = cqlSession.execute(preparedStmt_getCounts.bind(queryArgs.toArray()));
		
		
		int equipmentIdColIdx = 0;
		int alarmCodeColIdx = 1;
		
		rs.forEach(row -> {
			//we will do the client-side filtering for the AlarmCodes, because querying for it as part of CQL does not seem to work
			//we can afford it because there are not that many different alarm codes in total, and during normal operations 
			//the amount of distinct alarm codes per equipment is very small
	        AlarmCode ac = AlarmCode.getById(row.getInt(alarmCodeColIdx));
	        
	        if (alarmCodes != null && !alarmCodes.isEmpty() && alarmCodes.contains(ac) || alarmCodes == null || alarmCodes.isEmpty() ) 
	        {
	    		if(equipmentIds == null || equipmentIds.isEmpty()) {
	    			alarmCounts.addToCounter(0, ac, 1);
	    		} else {
	    			alarmCounts.addToCounter(row.getLong(equipmentIdColIdx), ac, 1);
	    		}
	        }
		});

		return alarmCounts;		
	}

    @Override
    public List<Alarm> get(Set<AlarmCode> alarmCodes, long createdAfterTimestamp) {

        if (alarmCodes == null || alarmCodes.isEmpty()) {
            throw new IllegalArgumentException("alarmCodes must be provided");
        }

        LOG.debug("Looking up Alarms for alarmCodes {} createdAfter {}", alarmCodes, createdAfterTimestamp);

        String query = CQL_GET_ALL;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();

        // add alarmCodes filters
        alarmCodes.forEach(ac -> queryArgs.add(ac.getId()));

        StringBuilder strb = new StringBuilder(100);
        strb.append("where alarmCode in (");
        for (int i = 0; i < alarmCodes.size(); i++) {
            strb.append("?");
            if (i < alarmCodes.size() - 1) {
                strb.append(",");
            }
        }
        strb.append(") ");

        if (createdAfterTimestamp > 0) {
            strb.append(" and createdTimestamp > ?");
            queryArgs.add(createdAfterTimestamp);
        }
        strb.append(" allow filtering");
        query += strb.toString();

        List<Alarm> ret = new ArrayList<>();

        PreparedStatement preparedStmt_getListForCustomer = cqlSession.prepare(query);

        ResultSet rs = cqlSession.execute(preparedStmt_getListForCustomer.bind(queryArgs.toArray()));

        rs.forEach(row -> ret.add(alarmRowMapper.mapRow(row)));

        LOG.debug("Found {} Alarms for alarmCodes {} createdAfter {}", ret.size(), alarmCodes, createdAfterTimestamp);

        return ret;
    }

}
