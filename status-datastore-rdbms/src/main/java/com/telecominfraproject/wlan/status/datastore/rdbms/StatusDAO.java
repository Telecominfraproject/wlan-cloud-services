package com.telecominfraproject.wlan.status.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class StatusDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(StatusDatastoreRdbms.class);
    
    private static final String[] GENERATED_KEY_COLS = { };
    
    private static final String[] ALL_COLUMNS_LIST = {        
        
        //TODO: add colums from properties Status in here
        "customerId",
        "equipmentId",
        "statusDataType",
        "details",
        //make sure the order of properties matches this list and list in StatusRowMapper and list in create/update methods
        
        "createdTimestamp",
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList("createdTimestamp"));
    
    private static final String TABLE_NAME = "status";
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
        " where customerId = ? and equipmentId = ? and statusDataType = ? ";
    
    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

    private static final String SQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where customerId = ? and equipmentId = ? and statusDataType = ? ";

    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
        "delete from "+TABLE_NAME+" where customerId = ? and equipmentId = ? ";

    private static final String SQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where customerId = ? and equipmentId = ? and statusDataType = ?  "
        + " and ( lastModifiedTimestamp = ? or ? = true) " //last parameter will allow us to skip check for concurrent modification, if necessary
        ;

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";


    private static final RowMapper<Status> statusRowMapper = new StatusRowMapper();


    @Autowired(required=false)
    public void setDataSource(StatusDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }

    @PostConstruct
    void afterPropertiesSet(){
    	setSkipCheckForConcurrentUpdates(true);
    }
    
    public Status create(final Status status) {
        
        final long ts = System.currentTimeMillis();

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from Status here 
                        ps.setInt(colIdx++, status.getCustomerId());
                        ps.setLong(colIdx++, status.getEquipmentId());
                        ps.setInt(colIdx++, status.getStatusDataType().getId());
                      	ps.setBytes(colIdx++, (status.getDetails()!=null)?status.getDetails().toZippedBytes():null);
                        
                        ps.setLong(colIdx++, ts);
                        ps.setLong(colIdx++, ts);
                        
                        return ps;
                    }
                });
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        
        status.setCreatedTimestamp(ts);
        status.setLastModifiedTimestamp(ts);


        LOG.debug("Stored Status {}", status);
        
        return status.clone();
    }

    
    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType) {
        LOG.debug("Looking up Status for id {}:{}:{}", customerId, equipmentId, statusDataType);

        try{
			Status status = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID, statusRowMapper, 
					customerId, 
					equipmentId,
					statusDataType != null ? statusDataType.getId() : -1
				);
            
            LOG.debug("Found Status {}", status);
            
            return status;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find Status for id {}:{}:{}", customerId, equipmentId, statusDataType);
            return null;
        }
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Status update(Status status) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = status.getLastModifiedTimestamp();
        
        int updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 

                //TODO: add remaining properties from Status here
        		status.getCustomerId(),
                status.getEquipmentId(),
                status.getStatusDataType().getId(),
                (status.getDetails()!=null)?status.getDetails().toZippedBytes():null ,
                                
                //status.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,
                
                // use id for update operation
        		status.getCustomerId(),
                status.getEquipmentId(),
                status.getStatusDataType().getId(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs,
                isSkipCheckForConcurrentUpdates()
        });
        
        if(updateCount==0){
            
            try{
                
                //find out if record could not be updated because it does not exist or because it was modified concurrently
                long recordTimestamp = this.jdbcTemplate.queryForObject(
                    SQL_GET_LASTMOD_BY_ID,
                    Long.class,
                    status.getCustomerId(), status.getEquipmentId(), status.getStatusDataType().getId()
                    );
                
                if(!isSkipCheckForConcurrentUpdates()){
	                LOG.debug("Concurrent modification detected for Status with id {}:{}:{} expected version is {} but version in db was {}", 
	                		status.getCustomerId(), status.getEquipmentId(), status.getStatusDataType(),
	                        incomingLastModifiedTs,
	                        recordTimestamp
	                        );
	                throw new DsConcurrentModificationException("Concurrent modification detected for Status with id " 
	                        + status.getCustomerId() + ":" + status.getEquipmentId() + ":" + status.getStatusDataType()
	                        +" expected version is " + incomingLastModifiedTs
	                        +" but version in db was " + recordTimestamp
	                        );
                }
                
            }catch (EmptyResultDataAccessException e) {
                LOG.debug("Could not find Status for id {}:{}:{}", status.getCustomerId(), status.getEquipmentId(), status.getStatusDataType());

                if(isSkipCheckForConcurrentUpdates()){
                    //in this case we did not request protection against concurrent updates,
                    //so the updateCount is 0 because record in db was not found
                	//we'll create a new record 
                	return create(status);
                } else {
					throw new DsEntityNotFoundException("Status not found " + status.getCustomerId() + ":"
							+ status.getEquipmentId() + ":" + status.getStatusDataType());
                }
            }
        }

        //make a copy so that we don't accidentally update caller's version by reference
        Status statusCopy = status.clone();
        statusCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Status {}", statusCopy);
        
        return statusCopy;
    }

    public List<Status> update(List<Status> statusList) {
    	 List<Status>ret = new ArrayList<>();
    	 if(statusList!=null) {
    		 statusList.forEach(s -> ret.add(update(s)) ); 
    	 }
    	 return ret;
    }
    
    public List<Status> delete(int customerId, long equipmentId) {
    	List<Status> ret = get(customerId, equipmentId);
        
        this.jdbcTemplate.update(SQL_DELETE, customerId, equipmentId);
                
        LOG.debug("Deleted Statuses {}", ret);
        
        return ret;
    }

    public List<Status> getAllForCustomer(int customerId) {
        LOG.debug("Looking up Statuss for customer {}", customerId);

        List<Status> ret = this.jdbcTemplate.query(SQL_GET_BY_CUSTOMER_ID,
                statusRowMapper, customerId);

        if (ret == null) {
            LOG.debug("Cannot find Statuss for customer {}", customerId);
        } else {
            LOG.debug("Found Statuss for customer {} : {}", customerId, ret);
        }

        return ret;
    }

    public List<Status> get(int customerId, long equipmentId) {
        LOG.debug("calling get({},{})", customerId, equipmentId);

        String query = SQL_GET_BY_CUSTOMER_ID + " and equipmentId = ? ";
        List<Status> results = this.jdbcTemplate.query(query, new Object[] {customerId, equipmentId} , statusRowMapper);

        LOG.debug("get({},{}) returns {} record(s)", customerId, equipmentId, (null == results) ? 0 : results.size());
        return results;
    }


	public PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Status> context) {

        PaginationResponse<Status> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Statuses for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Statuss for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        String query = SQL_GET_BY_CUSTOMER_ID;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

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
            strbSort.append("equipmentId");
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
        List<Status> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                statusRowMapper);

        if (pageItems == null) {
            LOG.debug("Cannot find Statuss for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Statuss for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }
	
    public PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds,
    		Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy, PaginationContext<Status> context) {
        PaginationResponse<Status> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Statuses for customer {} equipment {} types {} with last returned page number {}",
                    customerId, equipmentIds, statusDataTypes, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Statuses for customer {} equipment {} types {} with last returned page number {}", 
                customerId, equipmentIds, statusDataTypes, context.getLastReturnedPageNumber());

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
        
        //add statusDataType filters
        if (statusDataTypes != null && !statusDataTypes.isEmpty()) {
        	statusDataTypes.forEach(sdt -> queryArgs.add(sdt.getId()));

            StringBuilder strb = new StringBuilder(100);
            strb.append("and statusDataType in (");
            for (int i = 0; i < statusDataTypes.size(); i++) {
                strb.append("?");
                if (i < statusDataTypes.size() - 1) {
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
            // no sort order was specified - sort by id to have consistent
            // paging
            strbSort.append("equipmentId");
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
        List<Status> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                statusRowMapper);

        if (pageItems == null) {
            LOG.debug("Cannot find Statuses for customer {} equipment {} types {} with last returned page number {}",
                    customerId, equipmentIds, statusDataTypes, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Statuses for customer {} equipment {} types {} with last returned page number {}",
                    pageItems.size(), customerId, equipmentIds, statusDataTypes, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;    	
    }

	public List<Status> getForEquipment(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes) {
		
    	if(equipmentIds==null || equipmentIds.isEmpty()) {
    		throw new IllegalArgumentException("equipmentIds parameter must be provided");
    	}
    	
        LOG.debug("Looking up Statuses for customer {} equipment {} types {}", 
                customerId, equipmentIds, statusDataTypes);

        String query = SQL_GET_BY_CUSTOMER_ID;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        
        //add equipmentId filters
        {
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
        
        //add statusDataType filters
        if (statusDataTypes != null && !statusDataTypes.isEmpty()) {
        	statusDataTypes.forEach(sdt -> queryArgs.add(sdt.getId()));

            StringBuilder strb = new StringBuilder(100);
            strb.append("and statusDataType in (");
            for (int i = 0; i < statusDataTypes.size(); i++) {
                strb.append("?");
                if (i < statusDataTypes.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
        }        

        query += " order by equipmentId, statusDataType";

        List<Status> ret = this.jdbcTemplate.query(query, queryArgs.toArray(),
                statusRowMapper);

        if (ret == null) {
            LOG.debug("Cannot find Statuses for customer {} equipment {} types {} ",
                    customerId, equipmentIds, statusDataTypes);
        } else {
            LOG.debug("Found {} Statuses for customer {} equipment {} types {}",
                    ret.size(), customerId, equipmentIds, statusDataTypes);
        }
        
        return ret;

	}
}
