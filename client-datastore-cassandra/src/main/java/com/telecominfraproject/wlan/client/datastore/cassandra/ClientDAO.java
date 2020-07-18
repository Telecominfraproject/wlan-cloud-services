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
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtoptygin
 *
 */
@Component
public class ClientDAO {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDatastoreCassandra.class);
    
    private static final String COL_ID = "macAddress";
    
    private static final String[] ALL_COLUMNS_LIST = {        
        
        //TODO: add colums from properties Client in here
        "customerId",
        COL_ID,
        "details",
        //make sure the order of properties matches this list and list in ClientRowMapper and list in create/update methods
        
        "createdTimestamp",
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp", "customerId"));
    
    private static final String TABLE_NAME = "client";
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
        " where customerId = ? and " + COL_ID + " = ?";
    
    private static final String CQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

    private static final String CQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where customerId = ? and  " + COL_ID + " = ?";

    private static final String CQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String CQL_DELETE =
        "delete from "+TABLE_NAME+" where customerId = ? and  " + COL_ID + " = ? ";

    private static final String CQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where  customerId = ? and " + COL_ID + " = ? "
        + " IF lastModifiedTimestamp = ? " 
        ;

    private static final String CQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where  customerId = ? and "+ COL_ID +" in ";


    private static final RowMapper<Client> clientRowMapper = new ClientRowMapper();

	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_getByIdOrNull;
	private PreparedStatement preparedStmt_getLastmod;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_delete;
	private PreparedStatement preparedStmt_getPageForCustomer;

	@PostConstruct
	private void postConstruct(){

		try {
			preparedStmt_getByIdOrNull = cqlSession.prepare(CQL_GET_BY_ID);
			preparedStmt_getLastmod = cqlSession.prepare(CQL_GET_LASTMOD_BY_ID);
			preparedStmt_create = cqlSession.prepare(CQL_INSERT);
			preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
			preparedStmt_delete = cqlSession.prepare(CQL_DELETE);
        	preparedStmt_getPageForCustomer = cqlSession.prepare(CQL_GET_BY_CUSTOMER_ID);
		} catch (InvalidQueryException e) {
			LOG.error("Cannot prepare query", e);
			throw e;
		}
	}

	public Client create(Client client) {
		
        final long ts = System.currentTimeMillis();
        if(client.getCreatedTimestamp()<=0) {
        	client.setCreatedTimestamp(ts);
        }
        
        client.setLastModifiedTimestamp(ts);
        
		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from Client here
				client.getCustomerId(),
                client.getMacAddress().getAddressAsLong(),
                (client.getDetails()!=null) ? ByteBuffer.wrap(client.getDetails().toZippedBytes()) : null,
                
                client.getCreatedTimestamp(),
                client.getLastModifiedTimestamp()
				
				));
		
        LOG.debug("Stored Client {}", client);

        return client.clone();
	}

	public Client getOrNull(int customerId, MacAddress clientMac) {
        LOG.debug("Looking up Client for id {} {}", customerId, clientMac);

		ResultSet rs = cqlSession.execute(preparedStmt_getByIdOrNull.bind(customerId, clientMac.getAddressAsLong()));
		
		Row row = rs.one();

		if(row!=null) {
			Client client = clientRowMapper.mapRow(row);
            LOG.debug("Found Client {}", client);
            
            return client;
		} else {
            LOG.debug("Could not find Client for id {} {}", customerId, clientMac);
            return null;			
		}

	}
	
	public Client update(Client client) {
        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = client.getLastModifiedTimestamp();
        
		ResultSet rs = cqlSession.execute(preparedStmt_update.bind(

                //TODO: add remaining properties from Client here
                (client.getDetails()!=null) ? ByteBuffer.wrap(client.getDetails().toZippedBytes()) : null ,
                                
                newLastModifiedTs,
                
                // use id for update operation
                client.getCustomerId(),
                client.getMacAddress().getAddressAsLong(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs
        ));
        
		
        if(!rs.wasApplied()){
            
                //find out if record could not be updated because it does not exist or because it was modified concurrently
            	rs = cqlSession.execute(preparedStmt_getLastmod.bind(
                        client.getCustomerId(),
                        client.getMacAddress().getAddressAsLong()
                        ) );
            	Row row = rs.one();
            	
            	if(row!=null) {
	                long recordTimestamp = row.getLong(0);
	                
	                LOG.debug("Concurrent modification detected for Client with id {} {} expected version is {} but version in db was {}", 
	                		client.getCustomerId(),
	                		client.getMacAddress().getAddressAsLong(),
	                        incomingLastModifiedTs,
	                        recordTimestamp
	                        );
	                throw new DsConcurrentModificationException("Concurrent modification detected for Client with id " + 
	                        client.getCustomerId() + " " +
	                        client.getMacAddress().getAddressAsLong() + " " +
	                        " expected version is " + incomingLastModifiedTs
	                        +" but version in db was " + recordTimestamp
	                        );
            } else {
                LOG.debug("Cannot find Client for {} {} ", client.getCustomerId(),  
                		client.getMacAddress().getAddressAsLong());
                throw new DsEntityNotFoundException("Client not found " +  
                        client.getCustomerId() + " " +
                        client.getMacAddress().getAddressAsLong());
            }
        }
        

        //make a copy so that we don't accidentally update caller's version by reference
        Client clientCopy = client.clone();
        clientCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Client {}", clientCopy);
        
        return clientCopy;
    }
	
    
    public Client delete(int customerId, MacAddress clientMac) {
        Client client = getOrNull(customerId, clientMac);
        if(client!=null) {
        	cqlSession.execute(preparedStmt_delete.bind(customerId, clientMac.getAddressAsLong()));
        } else {
        	throw new DsEntityNotFoundException("Cannot find Client for id " + customerId + " " + clientMac);
        }

        LOG.debug("Deleted Client {} {}", customerId, clientMac);

        return client;
    }

    public List<Client> get(int customerId, Set<MacAddress> clientMacSet) {
        LOG.debug("calling get({}, {})", customerId, clientMacSet);

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
        
        List<Client> results = new ArrayList<>();
        
        //TODO: create a cache of these prepared statements, keyed by the numberOfClientMacs
        PreparedStatement preparedStmt_getListForCustomer = cqlSession.prepare(query);
        
		ResultSet rs = cqlSession.execute(preparedStmt_getListForCustomer.bind(bindVars.toArray() ));
		
		rs.forEach( row -> results.add(clientRowMapper.mapRow(row)) );


        LOG.debug("get({}, {}) returns {} record(s)", customerId, clientMacSet, results.size());
        return results;
    }


	public PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Client> context) {

        PaginationResponse<Client> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Clients for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Clients for customer {} with last returned page number {}", 
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

		List<Client> pageItems = new ArrayList<>();
		
		// iterate through the current page
		while (rs.getAvailableWithoutFetching() > 0) {
		  pageItems.add(clientRowMapper.mapRow(rs.one()));
		}

        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find Clients for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Clients for customer {} with last returned page number {}",
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

        return ret;	
    }
	
}
