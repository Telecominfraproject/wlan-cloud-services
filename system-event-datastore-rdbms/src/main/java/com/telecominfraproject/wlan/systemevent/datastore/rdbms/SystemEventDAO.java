package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class SystemEventDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventDatastoreRdbms.class);
    
    private static final String[] GENERATED_KEY_COLS = {};
    
    private static final String[] ALL_COLUMNS_LIST = {        
        
        //TODO: add colums from properties SystemEvent in here
        "customerId",
        "equipmentId",
        "dataType",
        "eventTimestamp",
        "details"
        //make sure the order of properties matches this list and list in SystemEventRowMapper and list in create/update methods
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(
    		"customerId",
            "equipmentId",
            "dataType",
            "eventTimestamp"));
    
    private static final String TABLE_NAME = "system_event";
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
    

    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? and eventTimestamp >= ? and eventTimestamp <= ? ";


    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
            "delete from "+TABLE_NAME+" where customerId = ? and equipmentId = ? and eventTimestamp < ?";

        private static final String SQL_PURGE_OLD_RECORDS =
                "delete from "+TABLE_NAME+" where eventTimestamp < ?";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";


    private static final RowMapper<SystemEventRecord> systemEventRecordRowMapper = new SystemEventRowMapper();


    @Autowired(required=false)
    public void setDataSource(SystemEventDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    public SystemEventRecord create(final SystemEventRecord systemEventRecord) {
        
        final long ts = System.currentTimeMillis();
        
        if(systemEventRecord.getEventTimestamp() == 0) {
        	systemEventRecord.setEventTimestamp(ts);
        }

        try{

            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from SystemEventRecord here 
                        ps.setInt(colIdx++, systemEventRecord.getCustomerId());
                        ps.setLong(colIdx++, systemEventRecord.getEquipmentId());
                        ps.setString(colIdx++, systemEventRecord.getDataType());
                        ps.setLong(colIdx++, systemEventRecord.getEventTimestamp());
                        
                      	ps.setBytes(colIdx++, (systemEventRecord.getDetails()!=null)?systemEventRecord.getDetails().toZippedBytes():null);
                                                
                        return ps;
                    }
                });
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        

        LOG.debug("Stored SystemEventRecord {}", systemEventRecord);
        
        return systemEventRecord.clone();
    }
    
    public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
        
        this.jdbcTemplate.update(SQL_DELETE, customerId, equipmentId, createdBeforeTimestamp);
                
        LOG.debug("Deleted SystemEventRecords for {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
    }

	public void create(List<SystemEventRecord> systemEventRecords) {
		if(systemEventRecords == null || systemEventRecords.isEmpty()) {
			return;
		}
		
        final long ts = System.currentTimeMillis();
        
		systemEventRecords.forEach(m -> {
			if (m.getEventTimestamp() == 0) {
				m.setEventTimestamp(ts);
			}
		});

        try{
        	
            this.jdbcTemplate.batchUpdate(SQL_INSERT, systemEventRecords, 100,
                    new ParameterizedPreparedStatementSetter<SystemEventRecord>() {
                        @Override
                        public void setValues(PreparedStatement ps, SystemEventRecord systemEventRecord) throws SQLException {

                            int colIdx = 1;
                            
                            //TODO: add remaining properties from SystemEventRecord here 
                            ps.setInt(colIdx++, systemEventRecord.getCustomerId());
                            ps.setLong(colIdx++, systemEventRecord.getEquipmentId());
                            ps.setString(colIdx++, systemEventRecord.getDataType());
                            ps.setLong(colIdx++, systemEventRecord.getEventTimestamp());
                            
                          	ps.setBytes(colIdx++, (systemEventRecord.getDetails()!=null)?systemEventRecord.getDetails().toZippedBytes():null);
                        }
                    });

        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        

        LOG.debug("Stored {} SystemEventRecord", systemEventRecords.size());
		
	}


	public void delete(long createdBeforeTimestamp) {
        this.jdbcTemplate.update(SQL_PURGE_OLD_RECORDS, createdBeforeTimestamp);
        
        LOG.debug("Deleted SystemEventRecords created before {}", createdBeforeTimestamp);
        
	}
	

    public PaginationResponse<SystemEventRecord> getForCustomer(long fromTime, long toTime, int customerId,
    		Set<Long> equipmentIds, Set<String> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<SystemEventRecord> context) {

        PaginationResponse<SystemEventRecord> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up SystemEventRecords for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up SystemEventRecords for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        String query = SQL_GET_BY_CUSTOMER_ID;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.add(fromTime);
        queryArgs.add(toTime);

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
      

        //add DataType filters
        if (dataTypes != null && !dataTypes.isEmpty()) {
        	dataTypes.forEach(sdt -> queryArgs.add(sdt));

            StringBuilder strb = new StringBuilder(100);
            strb.append("and dataType in (");
            for (int i = 0; i < dataTypes.size(); i++) {
                strb.append("?");
                if (i < dataTypes.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
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
            // no sort order was specified - sort by the eventTimestamp to have consistent
            // paging
            strbSort.append("eventTimestamp");
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
        List<SystemEventRecord> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                systemEventRecordRowMapper);

        LOG.debug("Found {} SystemEventRecord for customer {} with last returned page number {}",
                pageItems.size(), customerId, context.getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }
    
}
