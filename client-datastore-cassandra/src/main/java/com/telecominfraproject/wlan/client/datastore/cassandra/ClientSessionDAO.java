package com.telecominfraproject.wlan.client.datastore.cassandra;

import java.nio.ByteBuffer;
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

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtoptygin
 *
 */
@Component
public class ClientSessionDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDatastoreCassandra.class);
    
    private static final String[] ALL_COLUMNS_LIST = {        
        
        //TODO: add columns from properties ClientSession in here
        "customerId",
        "equipmentId",
        "macAddress",
        "locationId",
        "details",
        
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList( "macAddress", "customerId", "equipmentId"));
    
    private static final String TABLE_NAME = "client_session";
    private static final String ALL_COLUMNS;

    private static final Set<String> ALL_COLUMNS_LOWERCASE = new HashSet<>();

    private static final String ALL_COLUMNS_FOR_INSERT; 
    private static final String BIND_VARS_FOR_INSERT;
    private static final String ALL_COLUMNS_UPDATE;
    
    static{
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        StringBuilder strbColumnsForUpdate = new StringBuilder(512);
        for(String colName: ALL_COLUMNS_LIST){

            ALL_COLUMNS_LOWERCASE.add(colName.toLowerCase());

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
        " where customerId = ? and equipmentId = ? and macAddress = ?";
    
    private static final String CQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

    private static final String CQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where customerId = ? and equipmentId = ? and  macAddress = ?";

    private static final String CQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String CQL_DELETE =
        "delete from "+TABLE_NAME+" where customerId = ? and equipmentId = ? and  macAddress = ? ";

    private static final String CQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where  customerId = ? and equipmentId = ? and macAddress = ? "
        + " IF locationId = ? " //last parameter will allow us to detect changes to locationId and update the indexes if needed
        ;

    private static final String CQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where  customerId = ? and macAddress in ";

    private static final String CQL_INSERT_INTO_BY_EQUIPMENT_TABLE = "insert into client_session_by_equipment(customerId, equipmentId, macAddress) values ( ?, ?, ?) ";
    private static final String CQL_DELETE_FROM_BY_EQUIPMENT_TABLE = "delete from client_session_by_equipment where customerId = ? and equipmentId = ? and macAddress = ? ";

    private static final String CQL_INSERT_INTO_BY_LOCATION_TABLE = "insert into client_session_by_location(customerId, locationId, equipmentId, macAddress) values ( ?, ?, ?, ?) ";
    private static final String CQL_DELETE_FROM_BY_LOCATION_TABLE = "delete from client_session_by_location where locationId = ? and equipmentId = ? and macAddress = ? ";
        
    private static final String CQL_INSERT_INTO_CLIENT_SESSION_MAC = "insert into client_session_by_mac (customerId, locationId, equipmentId, macAddress, macAddressString) values (?, ?, ?, ?, ?)";
    private static final String CQL_DELETE_FROM_CLIENT_SESSION_MAC = "delete from client_session_by_mac where locationId = ? and equipmentId = ? and macAddress = ?";
    
    private static final String CQL_INSERT_INTO_CLIENT_SESSION_MAC_AND_EQUIPMENT = "insert into client_session_by_mac_and_equipment (customerId, locationId, equipmentId, macAddress, macAddressString) values (?, ?, ?, ?, ?)";
    private static final String CQL_DELETE_FROM_CLIENT_SESSION_MAC_AND_EQUIPMENT = "delete from client_session_by_mac_and_equipment where locationId = ? and equipmentId = ? and macAddress = ?";

    
    private static final RowMapper<ClientSession> clientSessionRowMapper = new ClientSessionRowMapper();

	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_getByIdOrNull;
	private PreparedStatement preparedStmt_getLastmod;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_delete;
	private PreparedStatement preparedStmt_getPageForCustomer;
	private PreparedStatement preparedStmt_createByEquipment;
	private PreparedStatement preparedStmt_deleteByEquipment;
	private PreparedStatement preparedStmt_createByLocation;
	private PreparedStatement preparedStmt_deleteByLocation;
    private PreparedStatement preparedStmt_createByMac;
    private PreparedStatement preparedStmt_deleteByMac;
    private PreparedStatement preparedStmt_createByMacAndEquipment;
    private PreparedStatement preparedStmt_deleteByMacAndEquipment;


	@PostConstruct
	private void postConstruct(){

		try {
			preparedStmt_getByIdOrNull = cqlSession.prepare(CQL_GET_BY_ID);
			preparedStmt_getLastmod = cqlSession.prepare(CQL_GET_LASTMOD_BY_ID);
			preparedStmt_create = cqlSession.prepare(CQL_INSERT);
			preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
			preparedStmt_delete = cqlSession.prepare(CQL_DELETE);
        	preparedStmt_getPageForCustomer = cqlSession.prepare(CQL_GET_BY_CUSTOMER_ID);
        	preparedStmt_createByEquipment = cqlSession.prepare(CQL_INSERT_INTO_BY_EQUIPMENT_TABLE);
        	preparedStmt_deleteByEquipment = cqlSession.prepare(CQL_DELETE_FROM_BY_EQUIPMENT_TABLE);
        	preparedStmt_createByLocation = cqlSession.prepare(CQL_INSERT_INTO_BY_LOCATION_TABLE);
        	preparedStmt_deleteByLocation = cqlSession.prepare(CQL_DELETE_FROM_BY_LOCATION_TABLE);
        	preparedStmt_createByMac = cqlSession.prepare(CQL_INSERT_INTO_CLIENT_SESSION_MAC);
        	preparedStmt_deleteByMac = cqlSession.prepare(CQL_DELETE_FROM_CLIENT_SESSION_MAC);
        	preparedStmt_createByMacAndEquipment = cqlSession.prepare(CQL_INSERT_INTO_CLIENT_SESSION_MAC_AND_EQUIPMENT);
        	preparedStmt_deleteByMacAndEquipment = cqlSession.prepare(CQL_DELETE_FROM_CLIENT_SESSION_MAC_AND_EQUIPMENT);
        	
		} catch (InvalidQueryException e) {
			LOG.error("Cannot prepare query", e);
			throw e;
		}
	}

    public ClientSession create(final ClientSession clientSession) {
        
        final long ts = System.currentTimeMillis();
        
        clientSession.setLastModifiedTimestamp(ts);
        
		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from clientSession here
				clientSession.getCustomerId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong(),
				
				clientSession.getLocationId(),
                (clientSession.getDetails()!=null) ? ByteBuffer.wrap(clientSession.getDetails().toZippedBytes()) : null ,
   				clientSession.getLastModifiedTimestamp()
				
				));


		//insert index records
		cqlSession.execute(preparedStmt_createByEquipment.bind(
				clientSession.getCustomerId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong()));
		
		cqlSession.execute(preparedStmt_createByLocation.bind(
				clientSession.getCustomerId(),
				clientSession.getLocationId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong()));
		
		cqlSession.execute(preparedStmt_createByMac.bind(
				clientSession.getCustomerId(),
				clientSession.getLocationId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong(),
				clientSession.getMacAddress().getAddressAsString()
				));
		
		cqlSession.execute(preparedStmt_createByMacAndEquipment.bind(
				clientSession.getCustomerId(),
				clientSession.getLocationId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong(),
				clientSession.getMacAddress().getAddressAsString()
				));

        LOG.debug("Stored Client session {}", clientSession);
        
        return clientSession.clone();
    }

    
    public ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac) {
        LOG.debug("Looking up Client session for id {} {} {}", customerId, equipmentId, clientMac);

		ResultSet rs = cqlSession.execute(preparedStmt_getByIdOrNull.bind(customerId, equipmentId, clientMac.getAddressAsLong()));
		
		Row row = rs.one();

		if(row!=null) {
			ClientSession clientSession = clientSessionRowMapper.mapRow(row);
            LOG.debug("Found ClientSession {}", clientSession);
            
            return clientSession;
		} else {
            LOG.debug("Could not find ClientSession for id {} {} {}", customerId, equipmentId, clientMac);
            return null;			
		}

    }

    public ClientSession updateSession(ClientSession clientSession) {

        long newLastModifiedTs = System.currentTimeMillis();
        
		ResultSet rs = cqlSession.execute(preparedStmt_update.bind(
                //TODO: add remaining properties from Client session here
                clientSession.getLocationId(),
                (clientSession.getDetails()!=null) ? ByteBuffer.wrap(clientSession.getDetails().toZippedBytes()) : null ,
                		
                newLastModifiedTs,
                
                // use id for update operation
        		clientSession.getCustomerId(),
        		clientSession.getEquipmentId(),
                clientSession.getMacAddress().getAddressAsLong(),
                // use locationId to determine if it has changed
                clientSession.getLocationId()
        ));
        
		if(!rs.wasApplied()){
			//update did not go through
            
			Row row = rs.one();
			
			if(row!=null && row.getColumnDefinitions().contains("locationId") && !row.isNull("locationId")) {
				//locationId has changed
				long oldLocationId = row.getLong("locationId");
				//delete record from the index table client_session_by_location for the old location - it will be re-created in the call to create() below
				cqlSession.execute(preparedStmt_deleteByLocation.bind(
						oldLocationId,
						clientSession.getEquipmentId(),
						clientSession.getMacAddress().getAddressAsLong()));

			}
			
			return create(clientSession);
        }

        //make a copy so that we don't accidentally update caller's version by reference
        ClientSession clientSessionCopy = clientSession.clone();
        clientSessionCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Client session {}", clientSessionCopy);
        
        return clientSessionCopy;
    }


    public ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac) {
        ClientSession clientSession = getSessionOrNull(customerId, equipmentId, clientMac);
        if(clientSession!=null) {
        	cqlSession.execute(preparedStmt_delete.bind(customerId, equipmentId, clientMac.getAddressAsLong()));
        } else {
        	throw new DsEntityNotFoundException("Cannot find Client for id " + customerId + " " + equipmentId+ " " + clientMac);
        }

        LOG.debug("Deleted Client session {} {} {}", customerId, equipmentId, clientMac);
        
		//delete index records
		cqlSession.execute(preparedStmt_deleteByEquipment.bind(
				clientSession.getCustomerId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong()));
		
		cqlSession.execute(preparedStmt_deleteByLocation.bind(
				clientSession.getLocationId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong()));
		
		cqlSession.execute(preparedStmt_deleteByMac.bind(
				clientSession.getLocationId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong()));
		
		cqlSession.execute(preparedStmt_deleteByMacAndEquipment.bind(
				clientSession.getLocationId(),
				clientSession.getEquipmentId(),
				clientSession.getMacAddress().getAddressAsLong()));


        return clientSession;
    }

    public List<ClientSession> getSessions(int customerId, Set<MacAddress> clientMacSet) {
        LOG.debug("calling getSessions({}, {})", customerId, clientMacSet);

        if (clientMacSet == null || clientMacSet.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder set = new StringBuilder(256);
        set.append("(");
        for(int i =0; i< clientMacSet.size(); i++) {
        		set.append("?,");
        }
        //remove last comma
        set.deleteCharAt(set.length()-1);
        set.append(")");
        
        String query = CQL_GET_ALL_IN_SET + set;
        ArrayList<Object> bindVars = new ArrayList<>();
        bindVars.add(customerId);
        clientMacSet.forEach(m -> bindVars.add(m.getAddressAsLong()) );
        
        List<ClientSession> results = new ArrayList<>();

        //TODO: create a cache of these prepared statements, keyed by the numberOfClientMacs
        PreparedStatement preparedStmt_getListForCustomer = cqlSession.prepare(query);
        
		ResultSet rs = cqlSession.execute(preparedStmt_getListForCustomer.bind(bindVars.toArray() ));
		
		rs.forEach( row -> results.add(clientSessionRowMapper.mapRow(row)) );

        LOG.debug("getSessions({}, {}) returns {} record(s)", customerId, clientMacSet, results.size());
        return results;
    }

    private static enum FilterOptions{ customer_only, customer_and_equipment, customer_and_location, customer_and_macAddress}

	public PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds, String macSubstring, List<ColumnAndSort> sortBy,
			PaginationContext<ClientSession> context) {

        PaginationResponse<ClientSession> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Client sessions for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Client sessions for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        String query_head = CQL_GET_BY_CUSTOMER_ID;
        String query = "";
        FilterOptions filterOptions = FilterOptions.customer_only;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);

        //add equipmentId filters
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            queryArgs.addAll(equipmentIds);

            query += "and equipmentId in " + CassandraUtils.getBindPlaceholders(equipmentIds);
            query_head = "select macAddress, equipmentId from client_session_by_equipment where customerId = ? ";
            
            filterOptions = FilterOptions.customer_and_equipment;
        }
        
        //add locationId filters
        if (locationIds != null && !locationIds.isEmpty()) {
        	//need to rebuild query args
        	queryArgs.clear();
            queryArgs.addAll(locationIds);
            if (equipmentIds != null && !equipmentIds.isEmpty()) {
                queryArgs.addAll(equipmentIds);
            }

            query = " locationId in " + CassandraUtils.getBindPlaceholders(locationIds) + query;
            query_head = "select macAddress, equipmentId from client_session_by_location where ";
            
            filterOptions = FilterOptions.customer_and_location;
        }
        
        //add macSubstring filters
        if (macSubstring != null && !macSubstring.isEmpty()) {
        	queryArgs.clear();
        	queryArgs.add(customerId);
        	queryArgs.add("%" + macSubstring.toLowerCase() + "%");
        	query_head = "select macAddress, equipmentId from client_session_by_mac where customerId = ? ";
        	
        	if (locationIds != null && !locationIds.isEmpty()) {
        		queryArgs.addAll(locationIds);
        		query = "and " + query;
        	}
        	if (equipmentIds != null && !equipmentIds.isEmpty()) {
        		queryArgs.addAll(equipmentIds);
            	query_head = "select macAddress, equipmentId from client_session_by_mac_and_equipment where customerId = ? ";
        	}
        	
        	query = " and macAddressString like ? " + query + " allow filtering";
        	
        	filterOptions = FilterOptions.customer_and_macAddress;
        }

        // add sorting options for the query
        // Cassandra allows very limited support for ordering results
        // See https://cassandra.apache.org/doc/latest/cql/dml.html#select
        // In here allowed orderings are the order induced by the clustering columns and the reverse of that one.
        // also, order by with secondary indexes is not supported
        // ***** We will ignore the order supplied by the caller for this datastore

        //TODO: create a cache of these prepared statements, keyed by the numberOfEquipmentIds_numberOfLocationIds
        PreparedStatement preparedStmt_getPageForCustomer;
        try {
        	preparedStmt_getPageForCustomer = cqlSession.prepare(query_head + query);
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query, e);
        	throw e;
        }

        // add pagination parameters for the query
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

		//populate page items
		List<ClientSession> pageItems = new ArrayList<>();
		
		switch(filterOptions) {
		case customer_only:
			// iterate through the current page directly
			while (rs.getAvailableWithoutFetching() > 0) {
			  pageItems.add(clientSessionRowMapper.mapRow(rs.one()));
			}
			break;		
		case customer_and_equipment:
		case customer_and_location:
		case customer_and_macAddress:
			//the query was against client_session_by_equipment, client_session_by_location, or client_session_by_mac(_and_equipment) table
			//find all the macAddresses and equipmentId for the page, then retrieve records for them from client_session table
			while (rs.getAvailableWithoutFetching() > 0) {
			    Row row = rs.one();
				MacAddress macAddr = new MacAddress(row.getLong("macAddress"));
				long eqId = row.getLong("equipmentId");
				// get a single client session per customerId, returned equipmentId, and returned macAddress from filter tables
				ClientSession retSession = getSessionOrNull(customerId, eqId, macAddr);
				
				if (retSession != null 
				        && (locationIds == null || locationIds.isEmpty() || locationIds.contains(retSession.getLocationId()))
                        && (equipmentIds == null || equipmentIds.isEmpty() || equipmentIds.contains(retSession.getEquipmentId()))
                        && (macSubstring == null || macSubstring.isEmpty() || retSession.getMacAddress().getAddressAsString().toLowerCase().contains(macSubstring.toLowerCase()))
                        ) 
                {
                        pageItems.add(retSession);
                }
			}

			break;
		default:
			LOG.warn("Unknown filter option:", filterOptions);
			throw new IllegalArgumentException("Unknown filter option " + filterOptions);
		}

        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find ClientSessions for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} ClientSessions for customer {} with last returned page number {}",
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
