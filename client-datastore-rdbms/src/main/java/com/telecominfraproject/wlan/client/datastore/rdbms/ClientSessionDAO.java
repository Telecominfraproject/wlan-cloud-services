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

import com.telecominfraproject.wlan.client.session.models.ClientSession;
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
public class ClientSessionDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDatastoreRdbms.class);
    
    private static final String COL_ID = "macAddress";
    
    private static final String[] GENERATED_KEY_COLS = { };
    
    private static final String[] ALL_COLUMNS_LIST = {        
        COL_ID,
        
        //TODO: add colums from properties ClientSession in here
        "customerId",
        "equipmentId",
        "locationId",
        "details",
        //make sure the order of properties matches this list and list in ClientSessionRowMapper and list in create/update methods
        
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "customerId", "equipmentId"));
    
    private static final String TABLE_NAME = "client_session";
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
        " where customerId = ? and equipmentId = ? and " + COL_ID + " = ?";
    
    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

    private static final String SQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where customerId = ? and equipmentId = ? and  " + COL_ID + " = ?";

    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
        "delete from "+TABLE_NAME+" where customerId = ? and equipmentId = ? and  " + COL_ID + " = ? ";

    private static final String SQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where  customerId = ? and equipmentId = ? and " + COL_ID + " = ? "
        + " and ( lastModifiedTimestamp = ? or ? = true) " //last parameter will allow us to skip check for concurrent modification, if necessary
        ;

    private static final String SQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where  customerId = ? and "+ COL_ID +" in ";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";


    private static final RowMapper<ClientSession> clientSessionRowMapper = new ClientSessionRowMapper();


    @Autowired(required=false)
    public void setDataSource(ClientDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    @PostConstruct
    void afterPropertiesSet(){
    	setSkipCheckForConcurrentUpdates(true);
    }

    public ClientSession create(final ClientSession clientSession) {
        
        final long ts = System.currentTimeMillis();

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from Client here 
                        ps.setLong(colIdx++, clientSession.getMacAddress().getAddressAsLong());
                        ps.setInt(colIdx++, clientSession.getCustomerId());
                        ps.setLong(colIdx++, clientSession.getEquipmentId());
                        ps.setLong(colIdx++, clientSession.getLocationId());
                      	ps.setBytes(colIdx++, (clientSession.getDetails()!=null)?clientSession.getDetails().toZippedBytes():null);
                        
                        ps.setLong(colIdx++, ts);
                        
                        return ps;
                    }
                });
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        
        clientSession.setLastModifiedTimestamp(ts);


        LOG.debug("Stored Client session {}", clientSession);
        
        return clientSession.clone();
    }

    
    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac) {
        LOG.debug("Looking up Client session for id {} {} {}", customerId, equipmentId, clientMac);

        try{
            ClientSession client = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    clientSessionRowMapper, customerId, equipmentId, clientMac.getAddressAsLong());
            
            LOG.debug("Found Client session {}", client);
            
            return client;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find Client for id {} {}", customerId, clientMac);
            return null;
        }
    }

    public ClientSession updateSession(ClientSession clientSession) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = clientSession.getLastModifiedTimestamp();
        
        int updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 
                //TODO: add remaining properties from Client session here
                clientSession.getLocationId(),
                (clientSession.getDetails()!=null)?clientSession.getDetails().toZippedBytes():null ,
                newLastModifiedTs,
                
                // use id for update operation
        		clientSession.getCustomerId(),
        		clientSession.getEquipmentId(),
                clientSession.getMacAddress().getAddressAsLong(),
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
                    clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession.getMacAddress().getAddressAsLong()
                    );
                
                if(!isSkipCheckForConcurrentUpdates()){
	                LOG.debug("Concurrent modification detected for Client session with id {}:{}:{} expected version is {} but version in db was {}", 
	                		clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession.getMacAddress(),
	                        incomingLastModifiedTs,
	                        recordTimestamp
	                        );
	                throw new DsConcurrentModificationException("Concurrent modification detected for Client session with id " 
	                        + clientSession.getCustomerId() + ":" + clientSession.getEquipmentId() + ":" + clientSession.getMacAddress()
	                        +" expected version is " + incomingLastModifiedTs
	                        +" but version in db was " + recordTimestamp
	                        );
                }
                
            }catch (EmptyResultDataAccessException e) {
                LOG.debug("Could not find Client for id {}:{}:{}", clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession.getMacAddress());

                if(isSkipCheckForConcurrentUpdates()){
                    //in this case we did not request protection against concurrent updates,
                    //so the updateCount is 0 because record in db was not found
                	//we'll create a new record 
                	return create(clientSession);
                } else {
					throw new DsEntityNotFoundException("Client session not found " + clientSession.getCustomerId() + ":"
							+ clientSession.getEquipmentId() + ":" + clientSession.getMacAddress());
                }
            }
        }

        //make a copy so that we don't accidentally update caller's version by reference
        ClientSession clientSessionCopy = clientSession.clone();
        clientSessionCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Client session {}", clientSessionCopy);
        
        return clientSessionCopy;
    }

	public List<ClientSession> updateSessions(List<ClientSession> sessionList) {
		List<ClientSession> ret = new ArrayList<>();
		if (sessionList != null) {
			sessionList.forEach(s -> ret.add(updateSession(s)));
		}
		return ret;
	}

    public ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac) {
        ClientSession client = getSessionOrNull(customerId, equipmentId, clientMac);
        if(client!=null) {
            this.jdbcTemplate.update(SQL_DELETE, customerId, equipmentId, clientMac.getAddressAsLong());
        } else {
        	throw new DsEntityNotFoundException("Cannot find Client for id " + customerId + " " + equipmentId+ " " + clientMac);
        }

        LOG.debug("Deleted Client session {} {} {}", customerId, equipmentId, clientMac);

        return client;
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
        
        String query = SQL_GET_ALL_IN_SET + set;
        ArrayList<Object> bindVars = new ArrayList<>();
        bindVars.add(customerId);
        clientMacSet.forEach(m -> bindVars.add(m.getAddressAsLong()) );
        
        List<ClientSession> results = this.jdbcTemplate.query(query, bindVars.toArray(), clientSessionRowMapper);

        LOG.debug("getSessions({}, {}) returns {} record(s)", customerId, clientMacSet, results.size());
        return results;
    }


	public PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds, List<ColumnAndSort> sortBy,
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
        
        //add locationId filters
        if (locationIds != null && !locationIds.isEmpty()) {
            queryArgs.addAll(locationIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append("and locationId in (");
            for (int i = 0; i < locationIds.size(); i++) {
                strb.append("?");
                if (i < locationIds.size() - 1) {
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
        List<ClientSession> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                clientSessionRowMapper);

        if (pageItems == null) {
            LOG.debug("Cannot find Client sessions for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Client sessions for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }
}
