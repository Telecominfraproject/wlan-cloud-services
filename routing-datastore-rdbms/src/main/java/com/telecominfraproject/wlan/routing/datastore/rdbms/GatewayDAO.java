package com.telecominfraproject.wlan.routing.datastore.rdbms;

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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class GatewayDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingDatastoreRdbms.class);
    
    private static final String COL_ID = "id";
    
    private static final String[] GENERATED_KEY_COLS = { COL_ID };
    
    private static final String[] ALL_COLUMNS_LIST = {        
        COL_ID,
        
        //TODO: add colums from properties EquipmentGatewayRecord in here
        "hostname",
        "ipAddr",
        "port",
        "gatewayType",
        //make sure the order of properties matches this list and list in GatewayRowMapper and list in create/update methods
        
        "createdTimestamp",
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));
    
    private static final String TABLE_NAME = "equipment_gateway";
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
        " where " + COL_ID + " = ?";

    private static final String SQL_GET_ALL =
            "select " + ALL_COLUMNS +
            " from "+TABLE_NAME;

    private static final String SQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where " + COL_ID + " = ?";

    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
        "delete from "+TABLE_NAME+" where " + COL_ID + " = ? ";

    private static final String SQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where " + COL_ID + " = ? "
        + " and ( lastModifiedTimestamp = ? or ? = true) " //last parameter will allow us to skip check for concurrent modification, if necessary
        ;

    private static final String SQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where "+ COL_ID +" in ";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";


    private static final RowMapper<EquipmentGatewayRecord> gatewayRowMapper = new GatewayRowMapper();


    @Autowired(required=false)
    public void setDataSource(RoutingDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    public EquipmentGatewayRecord create(final EquipmentGatewayRecord gatewayRecord) {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT, keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS) );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from EquipmentGatewayRecord here 
                        ps.setString(colIdx++, gatewayRecord.getHostname());
                        ps.setString(colIdx++, gatewayRecord.getIpAddr());
                        ps.setInt(colIdx++, gatewayRecord.getPort());
                        ps.setInt(colIdx++, gatewayRecord.getGatewayType().getId());
                        
                        ps.setLong(colIdx++, ts);
                        ps.setLong(colIdx++, ts);
                        
                        return ps;
                    }
                },
                keyHolder);
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        
        // keyHolder.getKey() now contains the generated key   
        gatewayRecord.setId(((Long)keyHolder.getKeys().get(COL_ID)));
        gatewayRecord.setCreatedTimestamp(ts);
        gatewayRecord.setLastModifiedTimestamp(ts);


        LOG.debug("Stored EquipmentGatewayRecord {}", gatewayRecord);
        
        return gatewayRecord.clone();
    }

    
    public EquipmentGatewayRecord get(long gatewayId) {
        LOG.debug("Looking up EquipmentGatewayRecord for id {}", gatewayId);

        try{
            EquipmentGatewayRecord gatewayRecord = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    gatewayRowMapper, gatewayId);
            
            LOG.debug("Found EquipmentGatewayRecord {}", gatewayRecord);
            
            return gatewayRecord;
        }catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public EquipmentGatewayRecord getOrNull(long gatewayId) {
        LOG.debug("Looking up EquipmentGatewayRecord for id {}", gatewayId);

        try{
            EquipmentGatewayRecord gatewayRecord = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    gatewayRowMapper, gatewayId);
            
            LOG.debug("Found EquipmentGatewayRecord {}", gatewayRecord);
            
            return gatewayRecord;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find EquipmentGatewayRecord for id {}", gatewayId);
            return null;
        }
    }

    public EquipmentGatewayRecord update(EquipmentGatewayRecord gatewayRecord) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = gatewayRecord.getLastModifiedTimestamp();
        
        int updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 
                //gatewayRecord.getId(), - not updating this one

                //TODO: add remaining properties from EquipmentGatewayRecord here
        		gatewayRecord.getHostname(),
        		gatewayRecord.getIpAddr(),
        		gatewayRecord.getPort(),
                gatewayRecord.getGatewayType().getId(),
                                
                //gatewayRecord.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,
                
                // use id for update operation
                gatewayRecord.getId(),
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
                    gatewayRecord.getId()
                    );
                
                LOG.debug("Concurrent modification detected for EquipmentGatewayRecord with id {} expected version is {} but version in db was {}", 
                        gatewayRecord.getId(),
                        incomingLastModifiedTs,
                        recordTimestamp
                        );
                throw new DsConcurrentModificationException("Concurrent modification detected for EquipmentGatewayRecord with id " + gatewayRecord.getId()
                        +" expected version is " + incomingLastModifiedTs
                        +" but version in db was " + recordTimestamp
                        );
                
            }catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find EquipmentGatewayRecord for {}", gatewayRecord.getId());
                throw new DsEntityNotFoundException("EquipmentGatewayRecord not found " + gatewayRecord.getId());
            }
        }

        //make a copy so that we don't accidentally update caller's version by reference
        EquipmentGatewayRecord gatewayRecordCopy = gatewayRecord.clone();
        gatewayRecordCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated EquipmentGatewayRecord {}", gatewayRecordCopy);
        
        return gatewayRecordCopy;
    }

    
    public EquipmentGatewayRecord delete(long gatewayId) {
        EquipmentGatewayRecord ret = get(gatewayId);
        
        this.jdbcTemplate.update(SQL_DELETE, gatewayId);
                
        LOG.debug("Deleted EquipmentGatewayRecord {}", ret);
        
        return ret;
    }

    public List<EquipmentGatewayRecord> get(Set<Long> gatewayRecordIdSet) {
        LOG.debug("calling get({})", gatewayRecordIdSet);

        if (gatewayRecordIdSet == null || gatewayRecordIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder set = new StringBuilder(256);
        set.append("(");
        for(int i =0; i< gatewayRecordIdSet.size(); i++) {
        		set.append("?,");
        }
        //remove last comma
        set.deleteCharAt(set.length()-1);
        set.append(")");
        
        String query = SQL_GET_ALL_IN_SET + set;
        List<EquipmentGatewayRecord> results = this.jdbcTemplate.query(query, gatewayRecordIdSet.toArray(), gatewayRowMapper);

        LOG.debug("get({}) returns {} record(s)", gatewayRecordIdSet, results.size());
        return results;
    }


	public PaginationResponse<EquipmentGatewayRecord> getAll(List<ColumnAndSort> sortBy,
			PaginationContext<EquipmentGatewayRecord> context) {

        PaginationResponse<EquipmentGatewayRecord> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up EquipmentGateways with last returned page number {}",
                    context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up EquipmentGateways with last returned page number {}", 
                context.getLastReturnedPageNumber());

        String query = SQL_GET_ALL;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();

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
        List<EquipmentGatewayRecord> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                gatewayRowMapper);

        LOG.debug("Found {} EquipmentGateways with last returned page number {}",
                    pageItems.size(), context.getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }


	public List<EquipmentGatewayRecord> getGateway(String hostname) {
        LOG.debug("calling getGateway({})", hostname);

        if (hostname == null || hostname.isEmpty()) {
            return Collections.emptyList();
        }

        String query = SQL_GET_ALL + " where hostname = ? ";
        List<EquipmentGatewayRecord> results = this.jdbcTemplate.query(query, new Object[] {hostname}, gatewayRowMapper);

        LOG.debug("getGateway({}) returns {} record(s)", hostname, results.size());
        return results;
	}


	public List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType) {
        LOG.debug("calling getGateway({})", gatewayType);

        List<EquipmentGatewayRecord> results;
        String query = SQL_GET_ALL;
        if (gatewayType == null ) {
            results = this.jdbcTemplate.query(query, new Object[] {}, gatewayRowMapper);
        } else {
            query += " where gatewayType = ? ";
            results = this.jdbcTemplate.query(query, new Object[] {gatewayType.getId()}, gatewayRowMapper);        	
        }


        LOG.debug("getGateway({}) returns {} record(s)", gatewayType, results.size());
        return results;
	}


	public List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId) {
        LOG.debug("calling getRegisteredGatewayRecordList({})", equipmentId);

        String query = SQL_GET_ALL + ", equipment_routing r where r.gatewayId = equipment_gateway.id AND r.equipmentId = ? ";
        List<EquipmentGatewayRecord> results = this.jdbcTemplate.query(query, new Object[] {equipmentId}, gatewayRowMapper);

        LOG.debug("getRegisteredGatewayRecordList({}) returns {} record(s)", equipmentId, results.size());
        return results;
	}


	public List<EquipmentGatewayRecord> deleteGateway(String hostname) {
        LOG.debug("calling deleteGateway({})", hostname);

        List<EquipmentGatewayRecord> results = getGateway(hostname);
        
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }

        String query = "delete from "+TABLE_NAME+ " where hostname = ? ";
        this.jdbcTemplate.update(query, new Object[] {hostname});

        LOG.debug("deleteGateway({}) returns {} record(s)", hostname, results.size());
        return results;
	}
}
