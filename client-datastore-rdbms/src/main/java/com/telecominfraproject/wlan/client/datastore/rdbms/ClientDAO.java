package com.telecominfraproject.wlan.client.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
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
public class ClientDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDatastoreRdbms.class);
    
    private static final String COL_ID = "macAddress";
    
    private static final String[] GENERATED_KEY_COLS = { };
    
    private static final String[] ALL_COLUMNS_LIST = {        
        COL_ID,
        "macAddressString", 
        
        //TODO: add colums from properties Client in here
        "customerId",
        "details",
        //make sure the order of properties matches this list and list in ClientRowMapper and list in create/update methods
        
        "createdTimestamp",
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "macAddressString", "createdTimestamp", "customerId"));
    
    private static final String TABLE_NAME = "client";
    private static final String TABLE_PREFIX = "c.";
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
        " where customerId = ? and " + COL_ID + " = ?";
    
    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

    private static final String SQL_GET_BLOCKED_CLIENTS = "select " + ALL_COLUMNS_WITH_PREFIX +
    		" from " + TABLE_NAME + " c ,  client_blocklist cb " + 
    		" where cb.customerId = ?  and c.customerId = cb.customerId and c.macAddress = cb.macAddress ";

    
    private static final String SQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where customerId = ? and  " + COL_ID + " = ?";

    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
        "delete from "+TABLE_NAME+" where customerId = ? and  " + COL_ID + " = ? ";

    private static final String SQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where  customerId = ? and " + COL_ID + " = ? "
        + " and ( lastModifiedTimestamp = ? or ? = true) " //last parameter will allow us to skip check for concurrent modification, if necessary
        ;

    private static final String SQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where  customerId = ? and "+ COL_ID +" in ";
    
    private static final String SQL_APPEND_SEARCH_MAC_SUBSTRING = 
    		"and macAddressString like ? ";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";

	private static final String SQL_INSERT_BLOCK_LIST = "insert into client_blocklist (customerId, macAddress) values (?, ?) ";
	private static final String SQL_DELETE_BLOCK_LIST = "delete from client_blocklist where customerId = ? and macAddress = ? ";


    private static final RowMapper<Client> clientRowMapper = new ClientRowMapper();


    @Autowired(required=false)
    public void setDataSource(ClientDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    public Client create(final Client client) {
        
        final long ts = System.currentTimeMillis();

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from Client here 
                        ps.setLong(colIdx++, client.getMacAddress().getAddressAsLong());
                        ps.setString(colIdx++, client.getMacAddress().getAddressAsString());
                        ps.setInt(colIdx++, client.getCustomerId());
                      	ps.setBytes(colIdx++, (client.getDetails()!=null)?client.getDetails().toZippedBytes():null);
                        
                        ps.setLong(colIdx++, ts);
                        ps.setLong(colIdx++, ts);
                        
                        return ps;
                    }
                });
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        //update blocked_client table, if needed
		if((client.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails().isEnabled() 
				) {

			this.jdbcTemplate.update( SQL_INSERT_BLOCK_LIST, 
					client.getCustomerId(), client.getMacAddress().getAddressAsLong());
			
			client.setNeedToUpdateBlocklist(true);
		}
        
        
        client.setCreatedTimestamp(ts);
        client.setLastModifiedTimestamp(ts);


        LOG.debug("Stored Client {}", client);
        
        return client.clone();
    }

    
    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Client getOrNull(int customerId, MacAddress clientMac) {
        LOG.debug("Looking up Client for id {} {}", customerId, clientMac);

        try{
            Client client = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    clientRowMapper, customerId, clientMac.getAddressAsLong());
            
            LOG.debug("Found Client {}", client);
            
            return client;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find Client for id {} {}", customerId, clientMac);
            return null;
        }
    }

    public Client update(Client client) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = client.getLastModifiedTimestamp();
        
        Client existingClient = getOrNull(client.getCustomerId(), client.getMacAddress()); 

        if(existingClient==null) {
            LOG.debug("Cannot find Client for {} {}", client.getCustomerId(), client.getMacAddress());
            throw new DsEntityNotFoundException("Client not found " + client.getCustomerId() + " " + client.getMacAddress());
        }

        int updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 
                //TODO: add remaining properties from Client here
                (client.getDetails()!=null)?client.getDetails().toZippedBytes():null ,
                                
                //client.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,
                
                // use id for update operation
        		client.getCustomerId(),
                client.getMacAddress().getAddressAsLong(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs,
                isSkipCheckForConcurrentUpdates()
        });
        
        if(updateCount==0){
            
                if(isSkipCheckForConcurrentUpdates()){
                    //in this case we did not request protection against concurrent updates,
                    //so the updateCount is 0 because record in db was not found
                    throw new EmptyResultDataAccessException(1);
                }
                
                long recordTimestamp = existingClient.getLastModifiedTimestamp();
                
                LOG.debug("Concurrent modification detected for Client with id {} {} expected version is {} but version in db was {}", 
                        client.getCustomerId(),
                        client.getMacAddress().getAddressAsLong(),
                        incomingLastModifiedTs,
                        recordTimestamp
                        );
                throw new DsConcurrentModificationException("Concurrent modification detected for Client with id " 
                        + client.getCustomerId() + " " + client.getMacAddress()
                        +" expected version is " + incomingLastModifiedTs
                        +" but version in db was " + recordTimestamp
                        );
                
        }

        //update client_blocklist table, if the blocking state of the client has changed
        boolean existingClientBlocked = (existingClient.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)existingClient.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)existingClient.getDetails()).getBlocklistDetails().isEnabled();

        boolean updatedClientBlocked = (client.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails().isEnabled();
        
        if(existingClientBlocked != updatedClientBlocked) {
        	if(updatedClientBlocked) {
        		//insert record into client_blocklist table
    			this.jdbcTemplate.update( SQL_INSERT_BLOCK_LIST, 
    					client.getCustomerId(), client.getMacAddress().getAddressAsLong());
        	} else {
        		//delete record from client_blocklist table
    			this.jdbcTemplate.update( SQL_DELETE_BLOCK_LIST, 
    					client.getCustomerId(), client.getMacAddress().getAddressAsLong());
        	}

        	//notify the caller that block list needs to be updated
			client.setNeedToUpdateBlocklist(true);

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
            this.jdbcTemplate.update(SQL_DELETE, customerId, clientMac.getAddressAsLong());
        } else {
        	throw new DsEntityNotFoundException("Cannot find Client for id " + customerId + " " + clientMac);
        }

        //delete from client_blocklist table happens by foreign key cascade
        //but we still need to tell the caller if the blocklist need to be updated
        if((client.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails().isEnabled() 
				) {
			client.setNeedToUpdateBlocklist(true);
        }
        
        LOG.debug("Deleted Client {} {}", customerId, clientMac);

        return client;
    }

    public List<Client> getAllForCustomer(int customerId) {
        LOG.debug("Looking up Clients for customer {}", customerId);

        List<Client> ret = this.jdbcTemplate.query(SQL_GET_BY_CUSTOMER_ID,
                clientRowMapper, customerId);

        LOG.debug("Found Clients for customer {} : {}", customerId, ret);

        return ret;
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
        
        String query = SQL_GET_ALL_IN_SET + set;
        ArrayList<Object> bindVars = new ArrayList<>();
        bindVars.add(customerId);
        clientMacSet.forEach(m -> bindVars.add(m.getAddressAsLong()) );
        
        List<Client> results = this.jdbcTemplate.query(query, bindVars.toArray(), clientRowMapper);

        LOG.debug("get({}, {}) returns {} record(s)", customerId, clientMacSet, results.size());
        return results;
    }
    
    public PaginationResponse<Client> searchByMacAddress(int customerId, String macSubstring, 
    		List<ColumnAndSort> sortBy, PaginationContext<Client> context) {
        LOG.debug("calling searchByMacAddress({}, {})", customerId, macSubstring);
        
        PaginationResponse<Client> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Clients for customer {} and macSubstring {} with last returned page number {}",
                    customerId, macSubstring, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Clients for customer {} and macSubstring {} with last returned page number {}", 
                customerId, macSubstring, context.getLastReturnedPageNumber());
        
        String query = SQL_GET_BY_CUSTOMER_ID;
        ArrayList<Object> queryArgs = new ArrayList<>();
        
        // add filters for the query
        queryArgs.add(customerId);
        
        if (macSubstring != null) {
	        query += SQL_APPEND_SEARCH_MAC_SUBSTRING;
	
	        queryArgs.add("%" + macSubstring.toLowerCase() + "%");
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
            strbSort.append(COL_ID);
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
        List<Client> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                clientRowMapper);

        LOG.debug("Found {} Clients for customer {} and macSubstring {} with last returned page number {}",
                    pageItems.size(), customerId, macSubstring, context.getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
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
            strbSort.append(COL_ID);
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
        List<Client> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                clientRowMapper);

        LOG.debug("Found {} Clients for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }


	public List<Client> getBlockedClients(int customerId) {
        LOG.debug("calling getBlockedClients({})", customerId);
        
        List<Client> results = this.jdbcTemplate.query(SQL_GET_BLOCKED_CLIENTS, clientRowMapper, customerId);

        LOG.debug("getBlockedClients({}) returns {} record(s)", customerId, results.size());
        return results;
    }

}
