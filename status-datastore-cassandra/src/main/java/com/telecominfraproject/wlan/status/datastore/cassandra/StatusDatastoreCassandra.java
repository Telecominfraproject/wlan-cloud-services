package com.telecominfraproject.wlan.status.datastore.cassandra;

import static com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils.getBindPlaceholders;
import static com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils.isPresent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.status.datastore.StatusDatastore;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtop
 *
 */
@Component
public class StatusDatastoreCassandra implements StatusDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(StatusDatastoreCassandra.class);

    private static final String[] ALL_COLUMNS_LIST = {        
            
            //TODO: add columns from properties of Status in here
            "customerId",
            "equipmentId",
            "statusDataType",
            "details",
            
            "createdTimestamp",
            "lastModifiedTimestamp"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
        private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList( 
        		"customerId",
                "equipmentId",
                "statusDataType",
                "createdTimestamp"));
        
        private static final String TABLE_NAME = "status";
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
            " where  customerId = ? and equipmentId = ? and statusDataType = ? ";
        
        private static final String CQL_GET_BY_CUSTOMER_ID = 
        		"select " + ALL_COLUMNS +
        		" from " + TABLE_NAME + " " + 
        		" where customerId = ? ";

        private static final String CQL_GET_BY_CUSTOMER_AND_EQUIPMENT_ID =
        		CQL_GET_BY_CUSTOMER_ID +
        		" and equipmentId = ?";

        private static final String CQL_INSERT =
            "insert into "+TABLE_NAME+" ( " 
            + ALL_COLUMNS_FOR_INSERT
            + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

        private static final String CQL_DELETE =
            "delete from "+TABLE_NAME+
            " where  customerId = ? and equipmentId = ? ";

        private static final String CQL_UPDATE =
            "update "+TABLE_NAME+" set "
            + ALL_COLUMNS_UPDATE +
            " where  customerId = ? and equipmentId = ? and statusDataType = ? "
            ;

        private static final String CQL_GET_KEYS_FOR_CUSTOMER_AND_EQUIPMENT =
                "select customerId, equipmentId, statusDataType from status where customerId = ? and equipmentId = ?";

        private static final String CQL_INSERT_BY_DATATYPE =
                "insert into status_by_datatype (customerId, equipmentId, statusDataType) values ( ?, ?, ? )";

        private static final String CQL_DELETE_BY_DATATYPE_HEADER =
                "delete from status_by_datatype where customerId = ? and equipmentId = ? ";

        private static final String CQL_DELETE_BY_DATATYPE = CQL_DELETE_BY_DATATYPE_HEADER + " and statusDataType = ?";

    private static final RowMapper<Status> statusRowMapper = new StatusRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_getOrNull;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_delete;
	private PreparedStatement preparedStmt_getByCustomerAndEquipment;
	private PreparedStatement preparedStmt_insertByDataType;
	private PreparedStatement preparedStmt_deleteByDataType;
	private PreparedStatement preparedStmt_getKeysByCustomerAndEquipment;

	@PostConstruct
	private void postConstruct(){

		preparedStmt_getOrNull = cqlSession.prepare(CQL_GET_BY_ID);
		preparedStmt_create = cqlSession.prepare(CQL_INSERT);
		preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
		preparedStmt_delete = cqlSession.prepare(CQL_DELETE);
		preparedStmt_getByCustomerAndEquipment = cqlSession.prepare(CQL_GET_BY_CUSTOMER_AND_EQUIPMENT_ID);
		
		preparedStmt_insertByDataType = cqlSession.prepare(CQL_INSERT_BY_DATATYPE);
		preparedStmt_deleteByDataType = cqlSession.prepare(CQL_DELETE_BY_DATATYPE);
		preparedStmt_getKeysByCustomerAndEquipment = cqlSession.prepare(CQL_GET_KEYS_FOR_CUSTOMER_AND_EQUIPMENT);

	}
	
	public Status create(Status status) {
		
        final long ts = System.currentTimeMillis();
        if(status.getCreatedTimestamp()<=0) {
        	status.setCreatedTimestamp(ts);
        }
        
        status.setLastModifiedTimestamp(ts);

		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from Status here 
                status.getCustomerId(),
                status.getEquipmentId(),
                status.getStatusDataType().getId(),
                
              	(status.getDetails()!=null) ? ByteBuffer.wrap(status.getDetails().toZippedBytes()) : null,
              			
                status.getCreatedTimestamp(),
                status.getLastModifiedTimestamp()
				
				));

		cqlSession.execute(preparedStmt_insertByDataType.bind(
                status.getCustomerId(),
                status.getEquipmentId(),
                status.getStatusDataType().getId()
                ));

        LOG.debug("Stored Status {}", status);

        return status.clone();
	}

		
	@Override
	public Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType) {
        LOG.debug("Looking up Status for id {} {} {}", customerId, equipmentId, statusDataType);

		ResultSet rs = cqlSession.execute(preparedStmt_getOrNull.bind(
				customerId, equipmentId, statusDataType!=null?statusDataType.getId():-1
				));
		
		Row row = rs.one();

		if(row!=null) {
			Status status = statusRowMapper.mapRow(row);
            LOG.debug("Found Status {}", status);
            
            return status;
		} else {
            LOG.debug("Could not find Status for id {} {} {}", customerId, equipmentId, statusDataType);
            return null;			
		}

	}

	@Override
	public Status update(Status status) {

		//This DAO does not enforce check for concurrent updates. Last one always wins.

        long newLastModifiedTs = System.currentTimeMillis();
        
		cqlSession.execute(preparedStmt_update.bind(

                //TODO: add remaining properties from Status here
                (status.getDetails()!=null) ? ByteBuffer.wrap(status.getDetails().toZippedBytes()) : null ,
                                
                newLastModifiedTs,
                
                // use id for update operation
        		status.getCustomerId(),
                status.getEquipmentId(),
                status.getStatusDataType().getId()
        ));

		cqlSession.execute(preparedStmt_insertByDataType.bind(
                status.getCustomerId(),
                status.getEquipmentId(),
                status.getStatusDataType().getId()
                ));


        //make a copy so that we don't accidentally update caller's version by reference
        Status statusCopy = status.clone();
        statusCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Status {}", statusCopy);
        
        return statusCopy;
    }
	

	@Override
	public List<Status> delete(int customerId, long equipmentId) {
        List<Status> ret = get(customerId, equipmentId);
        
        cqlSession.execute(preparedStmt_delete.bind(customerId, equipmentId));
        
        //clear the index table
		ResultSet rs = cqlSession.execute(preparedStmt_getKeysByCustomerAndEquipment.bind(customerId, equipmentId ));
		
		rs.forEach( row -> 
				cqlSession.execute(preparedStmt_deleteByDataType.bind(
		                row.getInt("customerId"),
		                row.getLong("equipmentId"),
		                row.getInt("statusDataType")
                ))
				 );

        LOG.debug("Deleted Statuses {}", ret);
                
        return ret;
	}
	
	@Override
	public List<Status> delete(int customerId, long equipmentId, Set<StatusDataType> statusDataTypes) {
	    
	    if(statusDataTypes == null || statusDataTypes.isEmpty()) {
	        return delete(customerId, equipmentId);
	    }
	    
        List<Status> ret = getForEquipment(customerId, Set.of(equipmentId), statusDataTypes);

        //
        // Delete from the main table
        //

        String query = CQL_DELETE;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.add(equipmentId);

        //add statusDataType filters
        if (statusDataTypes != null && !statusDataTypes.isEmpty()) {
            statusDataTypes.forEach(sdt -> queryArgs.add(sdt.getId()));

            StringBuilder strb = new StringBuilder(100);
            strb.append(" and statusDataType in (");
            strb.append("?,".repeat(statusDataTypes.size()));
            // remove last ','
            strb.deleteCharAt(strb.length() - 1);
            strb.append(") ");

            query += strb.toString();
        }     
        
        PreparedStatement preparedStmt = cqlSession.prepare(query); 
        cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));

        //
        // Clear the index table
        //
        query = CQL_DELETE_BY_DATATYPE_HEADER;

        // add filters for the query
        queryArgs.clear();
        queryArgs.add(customerId);
        queryArgs.add(equipmentId);

        //add statusDataType filters
        if (statusDataTypes != null && !statusDataTypes.isEmpty()) {
            statusDataTypes.forEach(sdt -> queryArgs.add(sdt.getId()));

            StringBuilder strb = new StringBuilder(100);
            strb.append(" and statusDataType in (");
            strb.append("?,".repeat(statusDataTypes.size()));
            // remove last ','
            strb.deleteCharAt(strb.length() - 1);
            strb.append(") ");

            query += strb.toString();
        }     
        
        preparedStmt = cqlSession.prepare(query); 
        cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));

        LOG.debug("Deleted Statuses {}", ret);
        
        return ret;
	}

    public List<Status> get(int customerId, long equipmentId) {
        LOG.debug("calling get({},{})", customerId, equipmentId);

		ResultSet rs = cqlSession.execute(preparedStmt_getByCustomerAndEquipment.bind(customerId, equipmentId ));
		
		List<Status> ret = new ArrayList<>();
		rs.forEach( row -> ret.add(statusRowMapper.mapRow(row)) );

        LOG.debug("get({},{}) returns {} record(s)", customerId, equipmentId, ret.size());
        return ret;
    }

    
	@Override
	public List<Status> getForEquipment(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes) {
		
    	if(equipmentIds == null || equipmentIds.isEmpty()) {
    		throw new IllegalArgumentException("equipmentIds must be provided");
    	}

        LOG.debug("Looking up Statuses for customer {} equipment {} data types {}", 
        		customerId, equipmentIds, statusDataTypes);

        String query = CQL_GET_BY_CUSTOMER_ID;

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

        List<Status> ret = new ArrayList<>();
        
        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfStatusDataTypes
        PreparedStatement preparedStmt_getListForCustomer = cqlSession.prepare(query);
        
		ResultSet rs = cqlSession.execute(preparedStmt_getListForCustomer.bind(queryArgs.toArray() ));
		
		rs.forEach( row -> ret.add(statusRowMapper.mapRow(row)) );
		
        LOG.debug("Found {} Statuses for customer {} equipment {} data types {}",
        		ret.size(),
        		customerId, equipmentIds, statusDataTypes);

        return ret;
        
	}

	private List<Status> getPage(int customerId, Set<Long> equipmentIds, Set<Integer>  statusDataTypes) {
		//select * from status where customerId = 1 and equipmentId in (1,2) and statusDataType in(1,2) ;
		List<Status> ret = new ArrayList<>();
		
		ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.addAll(equipmentIds);
        queryArgs.addAll(statusDataTypes);

		PreparedStatement preparedStmt = cqlSession.prepare("select * from status where customerId = ? "
				+ "and equipmentId  in "+getBindPlaceholders(equipmentIds)
				+" and statusDataType in " + getBindPlaceholders(statusDataTypes));	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
		
		Row row;
		while((row = rs.one()) !=null) {
			ret.add(statusRowMapper.mapRow(row));
		}

		return ret;
	}
	
	private static enum FilterOptions {
		/**
		 * All filters are provided and not empty - customerId, equipmentIds and dataTypes. This is the most efficient way to query, does not use index tables, goes through the status table directly
		 */
		all_filters, 
		/**
		 * Only customerId is provided. Uses idx_status_customerId index. Goes through the status table directly.
		 */
		customer,   
		/**
		 * Only customerId and equipmentIds are provided.  Goes through the status table directly. 
		 */
		equipment,   
		/**
		 * Only customerId and dataTypes are provided. First iterate through the status_by_datatype table, collect a page of keys, then use getPage() method to read from the status table.
		 */
		customer_dataType,  
		/**
		 * Note: this case is covered by the all_filters element above. Keeping it here only in case we need to extend ways of filtering. May remove it and the corresponding index table in the future. Only customerId, equipmentIds and dataTypes are provided. 
		 */
		equipment_dataType,  
	}

	@Override
	public PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds,
			Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy,
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

        LOG.debug("Looking up Statuses for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        //select the index table based on provided inputs, create a query and bind variables
        FilterOptions filterOptions = null;
        String query = null;
        ArrayList<Object> queryArgs = new ArrayList<>();

        if(isPresent(equipmentIds) && isPresent(statusDataTypes)) {
        	filterOptions = FilterOptions.all_filters;
			query = "select * from status where customerId = ? "
					+ " and equipmentId in "+getBindPlaceholders(equipmentIds)
					+" and statusDataType in "+getBindPlaceholders(statusDataTypes) ;
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            statusDataTypes.forEach(dt -> queryArgs.add(dt.getId()));
        } else if(!isPresent(equipmentIds) && !isPresent(statusDataTypes)) {
        	filterOptions = FilterOptions.customer;
        	query = "select * from status where customerId = ? ";
            queryArgs.add(customerId);
        } else if(isPresent(equipmentIds) && !isPresent(statusDataTypes)) {
        	filterOptions = FilterOptions.equipment;
        	query = "select * from status where customerId = ? "
        			+ " and equipmentId in "+getBindPlaceholders(equipmentIds);
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
        } else if(!isPresent(equipmentIds) && isPresent(statusDataTypes)) {
        	filterOptions = FilterOptions.customer_dataType;
        	query = "select * from status_by_datatype where customerId = ? "
        			+ " and statusDataType in "+getBindPlaceholders(statusDataTypes);
            queryArgs.add(customerId);
            statusDataTypes.forEach(dt -> queryArgs.add(dt.getId()));
        } else if(isPresent(equipmentIds) && isPresent(statusDataTypes)) {
        	filterOptions = FilterOptions.equipment_dataType;
			query = "select * from status where customerId = ? "
					+ " and equipmentId in "+getBindPlaceholders(equipmentIds)
					+" and statusDataType in "+getBindPlaceholders(statusDataTypes) ;
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            statusDataTypes.forEach(dt -> queryArgs.add(dt.getId()));
        } 
        
        //this is to protect the future code changes - when new filter operations are added
        if(filterOptions == null){
        	throw new IllegalStateException("Unknown combination of query filters");
        }

        
        // add sorting options for the query
        // Cassandra allows very limited support for ordering results
        // See https://cassandra.apache.org/doc/latest/cql/dml.html#select
        // In here allowed orderings are the order induced by the clustering columns and the reverse of that one.
        // also, order by with secondary indexes is not supported
        // ***** We will ignore the order supplied by the caller for this datastore

        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfStatusDataTypes
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

		List<Status> pageItems = new ArrayList<>();
		
		switch(filterOptions) {
		case all_filters:
		case customer:
		case equipment:
		case equipment_dataType:
			// iterate through the current page directly
			while (rs.getAvailableWithoutFetching() > 0) {
			  pageItems.add(statusRowMapper.mapRow(rs.one()));
			}
			break;		
		case customer_dataType:
			//the query was against the index table 
			//find all the keys for the page, then retrieve records for them from system_event table
			Set<Long> equipmentIdsIdx = new HashSet<>();
			Set<Integer> dataTypesIdx = new HashSet<>();
			Row row;
			while (rs.getAvailableWithoutFetching() > 0) {
				row = rs.one();
				equipmentIdsIdx.add(row.getLong("equipmentId"));
				dataTypesIdx.add(row.getInt("statusDataType"));
			}

			List<Status> pageOfStatuses = getPage(customerId, equipmentIdsIdx, dataTypesIdx);
			pageItems.addAll(pageOfStatuses);			
			break;
		default:
			LOG.warn("Unknown filter option:", filterOptions);
			throw new IllegalArgumentException("Unknown filter option " + filterOptions);
		}
		
        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find Statuses for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Statuses for customer {} with last returned page number {}",
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


}
