package com.telecominfraproject.wlan.alarm.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AlarmDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmDatastoreRdbms.class);
    
    private static final String[] GENERATED_KEY_COLS = { };
    
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
        //make sure the order of properties matches this list and list in AlarmRowMapper and list in create/update methods
        
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList( 
    		"customerId",
            "equipmentId",
            "alarmCode",
            "createdTimestamp"));
    
    private static final String TABLE_NAME = "alarm";
    private static final String TABLE_PREFIX = "s.";
    private static final String ALL_COLUMNS;

    private static final Set<String> ALL_COLUMNS_LOWERCASE = new HashSet<>();

    @SuppressWarnings("unused")
    //use this for queries where multiple tables are involved
    private static final String ALL_COLUMNS_WITH_PREFIX;
    
    private static final String ALL_COLUMNS_FOR_INSERT; 
    private static final String BIND_VARS_FOR_INSERT;
    private static final String ALL_COLUMNS_UPDATE;
    
    static{
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsWithPrefix = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        StringBuilder strbColumnsForUpdate = new StringBuilder(512);
        for(String colName: ALL_COLUMNS_LIST){

            ALL_COLUMNS_LOWERCASE.add(colName.toLowerCase());

            strbAllColumns.append(colName).append(","); 
            strbAllColumnsWithPrefix.append(TABLE_PREFIX).append(colName).append(",");
            
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
        strbAllColumnsWithPrefix.deleteCharAt(strbAllColumnsWithPrefix.length() - 1);
        strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
        strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
        strbColumnsForUpdate.deleteCharAt(strbColumnsForUpdate.length() - 1);

        ALL_COLUMNS = strbAllColumns.toString();
        ALL_COLUMNS_WITH_PREFIX = strbAllColumnsWithPrefix.toString();
        ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
        BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
        ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();
        
    }
    

    private static final String SQL_GET_BY_ID =
        "select " + ALL_COLUMNS +
        " from "+TABLE_NAME+" " +
        " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?";
    
    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

    private static final String SQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?";

    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
        "delete from "+TABLE_NAME+
        " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?";

    private static final String SQL_DELETE_BY_EQUIPMENT =
            "delete from "+TABLE_NAME+
            " where  customerId = ? and equipmentId = ? ";

    private static final String SQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where  customerId = ? and equipmentId = ? and alarmCode = ? and createdTimestamp = ?"
        + " and ( lastModifiedTimestamp = ? or ? = true) " //last parameter will allow us to skip check for concurrent modification, if necessary
        ;

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";

    private static final String SQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_HEADER = "select equipmentId, alarmCode, count(1) from alarm where customerId = ? ";
    private static final String SQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_FOOTER = " group by equipmentId, alarmCode";

    private static final String SQL_COUNTS_BY_ALARM_CODE_HEADER = "select alarmCode, count(1) from alarm where customerId = ? ";
    private static final String SQL_COUNTS_BY_ALARM_CODE_FOOTER = " group by alarmCode";

    private static final RowMapper<Alarm> alarmRowMapper = new AlarmRowMapper();


    @Autowired(required=false)
    public void setDataSource(AlarmDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    public Alarm create(final Alarm alarm) {
        
        final long ts = System.currentTimeMillis();
        if(alarm.getCreatedTimestamp()<=0) {
        	alarm.setCreatedTimestamp(ts);
        }
        
        alarm.setLastModifiedTimestamp(ts);

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from Alarm here 
                        ps.setInt(colIdx++, alarm.getCustomerId());
                        ps.setLong(colIdx++, alarm.getEquipmentId());
                        ps.setInt(colIdx++, alarm.getAlarmCode().getId());
                        ps.setLong(colIdx++, alarm.getCreatedTimestamp());
                        
                        ps.setInt(colIdx++, alarm.getOriginatorType().getId());
                        ps.setInt(colIdx++, alarm.getSeverity().getId());
                        ps.setInt(colIdx++, alarm.getScopeType().getId());                        
                        ps.setString(colIdx++, alarm.getScopeId());
                        
                      	ps.setBytes(colIdx++, (alarm.getDetails()!=null)?alarm.getDetails().toZippedBytes():null);
                      	
                      	ps.setBoolean(colIdx++, alarm.isAcknowledged());                        
                        ps.setLong(colIdx++, alarm.getLastModifiedTimestamp());
                        
                        return ps;
                    }
                });
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }        

        LOG.debug("Stored Alarm {}", alarm);
        
        return alarm.clone();
    }

    public Alarm get(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        LOG.debug("Looking up Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);

        try {
	        Alarm alarm = this.jdbcTemplate.queryForObject(
	                SQL_GET_BY_ID,
	                alarmRowMapper, customerId, equipmentId, alarmCode.getId(), createdTimestamp);
	        
	        LOG.debug("Found Alarm {}", alarm);
	        
	        return alarm;
        }catch (EmptyResultDataAccessException e) {
        	throw new DsEntityNotFoundException(e);
        }
    }

    
    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        LOG.debug("Looking up Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);

        try{
            Alarm alarm = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    alarmRowMapper, customerId, equipmentId, alarmCode.getId(), createdTimestamp);
            
            LOG.debug("Found Alarm {}", alarm);
            
            return alarm;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);
            return null;
        }
    }

    public Alarm update(Alarm alarm) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = alarm.getLastModifiedTimestamp();
        
        int updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 

                //TODO: add remaining properties from Alarm here
        		alarm.getOriginatorType().getId(),
                alarm.getSeverity().getId(),
                alarm.getScopeType().getId(),
                alarm.getScopeId(),
                (alarm.getDetails()!=null)?alarm.getDetails().toZippedBytes():null ,
                alarm.isAcknowledged(),
                                
                newLastModifiedTs,
                
                // use id for update operation
                alarm.getCustomerId(),
                alarm.getEquipmentId(),
                alarm.getAlarmCode().getId(),
                alarm.getCreatedTimestamp(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs,
                isSkipCheckForConcurrentUpdates()
        });
        
        if(updateCount==0){
            
            try{
                
                if(isSkipCheckForConcurrentUpdates()){
                    //in this case we did not request protection against concurrent updates,
                    //so the updateCount is 0 because record in db was not found
                    throw new EmptyResultDataAccessException(1);
                }
                
                //find out if record could not be updated because it does not exist or because it was modified concurrently
                long recordTimestamp = this.jdbcTemplate.queryForObject(
                    SQL_GET_LASTMOD_BY_ID,
                    Long.class,
                    alarm.getCustomerId(),
                    alarm.getEquipmentId(),
                    alarm.getAlarmCode().getId(),
                    alarm.getCreatedTimestamp()
                    );
                
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
                
            }catch (EmptyResultDataAccessException e) {
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

    
    public Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        Alarm ret = get(customerId, equipmentId, alarmCode, createdTimestamp);
        
        this.jdbcTemplate.update(SQL_DELETE, customerId, equipmentId, alarmCode.getId(), createdTimestamp);
                
        LOG.debug("Deleted Alarm {}", ret);
        
        return ret;
    }

    public List<Alarm> delete(int customerId, long equipmentId) {
        List<Alarm> ret = get(customerId, Collections.singleton(equipmentId), null, -1);
                
        this.jdbcTemplate.update(SQL_DELETE_BY_EQUIPMENT, customerId, equipmentId);
                
        LOG.debug("Deleted Alarms {}", ret);
        
        return ret;
    }

	public List<Alarm> get(int customerId, Set<Long> equipmentIds, Set<AlarmCode> alarmCodes,
			long createdAfterTimestamp) {

    	if(equipmentIds == null || equipmentIds.isEmpty()) {
    		throw new IllegalArgumentException("equipmentIds must be provided");
    	}

        LOG.debug("Looking up Alarms for customer {} equipment {} codes {} createdAfter {}", 
        		customerId, equipmentIds, alarmCodes, createdAfterTimestamp);

        String query = SQL_GET_BY_CUSTOMER_ID;

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

        List<Alarm> ret = this.jdbcTemplate.query(query, queryArgs.toArray(),
                alarmRowMapper);
        
        LOG.debug("Found {} Alarms for customer {} equipment {} codes {} createdAfter {}",
        		ret.size(),
        		customerId, equipmentIds, alarmCodes, createdAfterTimestamp);

        return ret;
	}
	
    public List<Alarm> getAllForCustomer(int customerId) {
        LOG.debug("Looking up Alarms for customer {}", customerId);

        List<Alarm> ret = this.jdbcTemplate.query(SQL_GET_BY_CUSTOMER_ID,
                alarmRowMapper, customerId);

        LOG.debug("Found Alarms for customer {} : {}", customerId, ret);

        return ret;
    }



	public PaginationResponse<Alarm> getForCustomer(int customerId, 
			Set<Long> equipmentIds, Set<AlarmCode> alarmCodes, long createdAfterTimestamp, Boolean acknowledged,
			List<ColumnAndSort> sortBy, PaginationContext<Alarm> context) {

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

        String query = SQL_GET_BY_CUSTOMER_ID;

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
        
        if (acknowledged != null) {
        	query += " and acknowledged = ? ";
        	queryArgs.add(acknowledged);
        }
        
        // add sorting options for the query
        StringBuilder strbSort = new StringBuilder(100);
        strbSort.append(" order by ");

        if (sortBy != null && !sortBy.isEmpty()) {

            // use supplied sorting options
            for (ColumnAndSort column : sortBy) {
                if (!ALL_COLUMNS_LOWERCASE.contains(column.getColumnName().toLowerCase())) {
                    // unknown column, skip it
                    continue;
                }

                strbSort.append(column.getColumnName());

                if (column.getSortOrder() == SortOrder.desc) {
                    strbSort.append(" desc");
                }

                strbSort.append(",");
            }

            // remove last ','
            strbSort.deleteCharAt(strbSort.length() - 1);

        } else {
            // no sort order was specified - sort by id to have consistent
            // paging
            strbSort.append(" equipmentId, createdTimestamp");
        }

        query += strbSort.toString();

        // add pagination parameters for the query
        query += SQL_PAGING_SUFFIX ;

        queryArgs.add(context.getMaxItemsPerPage());
        queryArgs.add(context.getTotalItemsReturned());

        /*
         * https://www.citusdata.com/blog/2016/03/30/five-ways-to-paginate/
         * Choosing offset=1000 makes cost about 19 and has a 0.609 ms execution
         * time. Once offset=5,000,000 the cost goes up to 92734 and execution
         * time is 758.484 ms. - DT: still acceptable for our use case
         */
        List<Alarm> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                alarmRowMapper);

        LOG.debug("Found {} Alarms for customer {} with last returned page number {}",
                pageItems.size(), customerId, context.getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }


	public AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIds, Set<AlarmCode> alarmCodes) {

        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

		//build the query
		
		StringBuilder query = new StringBuilder();
		if(equipmentIds == null || equipmentIds.isEmpty()) {
			query.append(SQL_COUNTS_BY_ALARM_CODE_HEADER);
		} else {
			query.append(SQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_HEADER);
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

            query.append(strb);
        }        

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

        
		if(equipmentIds == null || equipmentIds.isEmpty()) {
			query.append(SQL_COUNTS_BY_ALARM_CODE_FOOTER);
		} else {
			query.append(SQL_COUNTS_BY_EQUIPMENT_AND_ALARM_CODE_FOOTER);
		}

		AlarmCounts alarmCounts = new AlarmCounts();
		alarmCounts.setCustomerId(customerId);
		
        this.jdbcTemplate.query(query.toString(), queryArgs.toArray(),
                new RowCallbackHandler() {
        	@Override
        	public void processRow(ResultSet rs) throws SQLException {
        		if(equipmentIds == null || equipmentIds.isEmpty()) {
        			alarmCounts.addToCounter(0, AlarmCode.getById(rs.getInt(1)), rs.getInt(2));
        		} else {
        			alarmCounts.addToCounter(rs.getLong(1), AlarmCode.getById(rs.getInt(2)), rs.getInt(3));
        		}

        	}
        });

		return alarmCounts;
	}
}
