package com.telecominfraproject.wlan.routing.datastore.cassandra;

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
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.utils.TimeBasedId;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtop
 *
 */
@Component
public class RoutingDAO {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingDAO.class);

    private static final String[] ALL_COLUMNS_LIST = {        
    		"id",
    		
            //TODO: add colums from properties EquipmentRoutingRecord in here
            "equipmentId",
            "customerId",
            "gatewayId",
            //make sure the order of properties matches this list and list in RoutingRowMapper and list in create/update methods
            
            "createdTimestamp",
            "lastModifiedTimestamp"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
        private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList( 
        		"id",
                "createdTimestamp"));
        
        private static final String TABLE_NAME = "equipment_routing";
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
        
        public static final String CQL_GET_ALL = "select " + ALL_COLUMNS + " from "+TABLE_NAME ;
        
        private static final String CQL_GET_BY_ID =
        		CQL_GET_ALL + 
                " where id = ?";
        
        private static final String CQL_GET_BY_CUSTOMER_ID = 
        		CQL_GET_ALL + " where customerId = ? ";
        
        private static final String CQL_GET_BY_EQUIPMENT_ID =
        		CQL_GET_ALL + " where equipmentId = ? ";

        private static final String CQL_GET_LASTMOD_BY_ID =
                "select lastModifiedTimestamp " +
                " from "+TABLE_NAME+" " +
                " where id= ?";

        private static final String CQL_INSERT =
            "insert into "+TABLE_NAME+" ( " 
            + ALL_COLUMNS_FOR_INSERT
            + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

        private static final String CQL_DELETE =
                "delete from "+TABLE_NAME+" where id = ? ";

        private static final String CQL_UPDATE =
            "update "+TABLE_NAME+" set "
            + ALL_COLUMNS_UPDATE +
            " where id = ? "
            + " IF lastModifiedTimestamp = ? " 
            ;
    

    private static final RowMapper<EquipmentRoutingRecord> routingRowMapper = new RoutingRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_getOrNull;
	private PreparedStatement preparedStmt_getLastmod;
	private PreparedStatement preparedStmt_getByEquipmentId;
	private PreparedStatement preparedStmt_getPageForCustomer;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_delete;

	@PostConstruct
	private void postConstruct(){

		preparedStmt_getOrNull = cqlSession.prepare(CQL_GET_BY_ID);
		preparedStmt_getLastmod = cqlSession.prepare(CQL_GET_LASTMOD_BY_ID);
		preparedStmt_getByEquipmentId = cqlSession.prepare(CQL_GET_BY_EQUIPMENT_ID);
    	preparedStmt_getPageForCustomer = cqlSession.prepare(CQL_GET_BY_CUSTOMER_ID);
		preparedStmt_create = cqlSession.prepare(CQL_INSERT);
		preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
		preparedStmt_delete = cqlSession.prepare(CQL_DELETE);
	}
	
	public EquipmentRoutingRecord create(EquipmentRoutingRecord routing) {
		
        final long ts = System.currentTimeMillis();
        if(routing.getCreatedTimestamp()<=0) {
        	routing.setCreatedTimestamp(ts);
        }
        
        routing.setLastModifiedTimestamp(ts);
        
        routing.setId(TimeBasedId.generateIdFromTimeNanos());

		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from EquipmentRoutingRecord here
				routing.getId(),
                routing.getEquipmentId(),
                routing.getCustomerId(),
                routing.getGatewayId(),
                
                routing.getCreatedTimestamp(),
                routing.getLastModifiedTimestamp()
				
				));
		
        LOG.debug("Stored EquipmentRoutingRecord {}", routing);

        return routing.clone();
	}

		
	public EquipmentRoutingRecord getOrNull(long routingId) {
        LOG.debug("Looking up EquipmentRoutingRecord for id {}", routingId);

		ResultSet rs = cqlSession.execute(preparedStmt_getOrNull.bind(routingId));
		
		Row row = rs.one();

		if(row!=null) {
			EquipmentRoutingRecord routingRecord = routingRowMapper.mapRow(row);
            LOG.debug("Found EquipmentRoutingRecord {}", routingRecord);
            
            return routingRecord;
		} else {
            LOG.debug("Could not find EquipmentRoutingRecord for id {}", routingId);
            return null;			
		}

	}
	
	public EquipmentRoutingRecord get(long routingId) {
		EquipmentRoutingRecord ret = getOrNull(routingId);
		if(ret == null) {
			throw new DsEntityNotFoundException("Cannot find EquipmentRoutingRecord " + routingId);
		}
		
		return ret;
	}
	

	public EquipmentRoutingRecord update(EquipmentRoutingRecord routing) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = routing.getLastModifiedTimestamp();
        
		ResultSet rs = cqlSession.execute(preparedStmt_update.bind(

                //TODO: add remaining properties from EquipmentRoutingRecord here
                routing.getEquipmentId(),
                routing.getCustomerId(),
                routing.getGatewayId(),
                                
                newLastModifiedTs,
                
                // use id for update operation
                routing.getId(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs
                
        ));
        
		
        if(!rs.wasApplied()){
            
                //find out if record could not be updated because it does not exist or because it was modified concurrently
            	rs = cqlSession.execute(preparedStmt_getLastmod.bind(routing.getId()) );
            	Row row = rs.one();
            	
            	if(row!=null) {
	                long recordTimestamp = row.getLong(0);
	                
	                LOG.debug("Concurrent modification detected for EquipmentRoutingRecord with id {} expected version is {} but version in db was {}", 
	                		routing.getId(),
	                        incomingLastModifiedTs,
	                        recordTimestamp
	                        );
	                throw new DsConcurrentModificationException("Concurrent modification detected for EquipmentRoutingRecord with id " + 
	                		routing.getId()
	                        +" expected version is " + incomingLastModifiedTs
	                        +" but version in db was " + recordTimestamp
	                        );
            } else {
                LOG.debug("Cannot find EquipmentRoutingRecord for {} ", routing.getId());
                throw new DsEntityNotFoundException("EquipmentRoutingRecord not found " +  
                		routing.getId());
            }
        }
        

        //make a copy so that we don't accidentally update caller's version by reference
        EquipmentRoutingRecord routingCopy = routing.clone();
        routingCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated EquipmentRoutingRecord {}", routingCopy);
        
        return routingCopy;
    }
	
    public EquipmentRoutingRecord delete(long routingId) {
    	EquipmentRoutingRecord ret = get(routingId);
        
        cqlSession.execute(preparedStmt_delete.bind(routingId));
                
        LOG.debug("Deleted EquipmentRoutingRecord {}", ret);
        
        return ret;
    }

	public List<EquipmentRoutingRecord> get(Set<Long> routingIdSet) {
		
    	if(routingIdSet == null || routingIdSet.isEmpty()) {
    		throw new IllegalArgumentException("routingIdSet must be provided");
    	}

        LOG.debug("Looking up EquipmentRoutingRecords {}", routingIdSet);

        String query = CQL_GET_ALL;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        
        //add routingIdSet filters
        {
            queryArgs.addAll(routingIdSet);

            StringBuilder strb = new StringBuilder(100);
            strb.append(" where id in (");
            for (int i = 0; i < routingIdSet.size(); i++) {
                strb.append("?");
                if (i < routingIdSet.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query += strb.toString();
        }
        
        List<EquipmentRoutingRecord> ret = new ArrayList<>();
        
        //TODO: create a cache of these prepared statements, keyed by the numberOfRoutingIds
        PreparedStatement preparedStmt_getListForIds = cqlSession.prepare(query);
        
		ResultSet rs = cqlSession.execute(preparedStmt_getListForIds.bind(queryArgs.toArray() ));
		
		rs.forEach( row -> ret.add(routingRowMapper.mapRow(row)) );
		
        LOG.debug("Found {} EquipmentRoutingRecords {}",
        		ret.size(),
        		routingIdSet);

        return ret;
        
	}

	public List<EquipmentRoutingRecord> getRegisteredRouteList(long equipmentId) {
        LOG.debug("calling getRegisteredGatewayRecordList({})", equipmentId);

		ResultSet rs = cqlSession.execute(preparedStmt_getByEquipmentId.bind(equipmentId ));
        List<EquipmentRoutingRecord> ret = new ArrayList<>();

		rs.forEach( row -> ret.add(routingRowMapper.mapRow(row)) );

        LOG.debug("getRegisteredGatewayRecordList({}) returns {} record(s)", equipmentId, ret.size());
        return ret;
	}



    public PaginationResponse<EquipmentRoutingRecord> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<EquipmentRoutingRecord> context) {

    	PaginationResponse<EquipmentRoutingRecord> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up EquipmentRoutingRecords for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up EquipmentRoutingRecords for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

       
        // add sorting options for the query
        // Cassandra allows very limited support for ordering results
        // See https://cassandra.apache.org/doc/latest/cql/dml.html#select
        // In here allowed orderings are the order induced by the clustering columns and the reverse of that one.
        // also, order by with secondary indexes is not supported
        // ***** We will ignore the order supplied by the caller for this datastore

        BoundStatement boundStmt = preparedStmt_getPageForCustomer.bind(customerId );
        //have to do it this way - setPageSize creates new object
        boundStmt = boundStmt.setPageSize(context.getMaxItemsPerPage());
        
        if(context.getThirdPartyPagingState()!=null) {
        	ByteBuffer currentPagingState = ByteBuffer.wrap(context.getThirdPartyPagingState());
        	//have to do it this way - setPagingState creates new object
        	boundStmt = boundStmt.setPagingState(currentPagingState);
        }
        
		ResultSet rs = cqlSession.execute(boundStmt);
		ByteBuffer nextPagingState = rs.getExecutionInfo().getPagingState();

		List<EquipmentRoutingRecord> pageItems = new ArrayList<>();
		
		// iterate through the current page
		while (rs.getAvailableWithoutFetching() > 0) {
		  pageItems.add(routingRowMapper.mapRow(rs.one()));
		}

        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find EquipmentRoutingRecords for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} EquipmentRoutingRecords for customer {} with last returned page number {}",
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
