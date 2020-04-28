package com.telecominfraproject.wlan.equipment.datastore.rdbms;

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

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

import com.telecominfraproject.wlan.equipment.models.Equipment;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class EquipmentDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDatastoreRdbms.class);
    
    private static final String COL_ID = "id";
    
    private static final String[] GENERATED_KEY_COLS = { COL_ID };
    
    private static final String[] ALL_COLUMNS_LIST = {        
        COL_ID,
        
        //TODO: add colums from properties Equipment in here
        "customerId",
        "profileId",
        "locationId",
        "equipmentType",
        "inventoryId",

        "name",
        "details",
        "latitude",
        "longitude", 
        "serial",
        //make sure the order of properties matches this list and list in EquipmentRowMapper and list in create/update methods
        
        "createdTimestamp",
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));
    
    private static final String TABLE_NAME = "equipment";
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
    
    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";

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

    private static final String SQL_GET_BY_CUSTOMER_ID_AND_EQUIPMENT_TYPE = "select " + ALL_COLUMNS + " from "
            + TABLE_NAME + " " + " where customerId = ? and equipmentType = ? ";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";


    private static final RowMapper<Equipment> equipmentRowMapper = new EquipmentRowMapper();


    @Autowired(required=false)
    public void setDataSource(EquipmentDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    public Equipment create(final Equipment equipment) {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT, keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS) );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from Equipment here 
                        ps.setInt(colIdx++, equipment.getCustomerId());
                        ps.setLong(colIdx++, equipment.getProfileId());
                        ps.setLong(colIdx++, equipment.getLocationId());
                        ps.setInt(colIdx++, equipment.getEquipmentType().getId());
                        ps.setString(colIdx++, equipment.getInventoryId());
                        
                        ps.setString(colIdx++, equipment.getName());
                      	ps.setBytes(colIdx++, (equipment.getDetails()!=null)?equipment.getDetails().toZippedBytes():null);
                      	ps.setString(colIdx++, equipment.getLatitude());
                      	ps.setString(colIdx++, equipment.getLongitude());
                      	ps.setString(colIdx++, equipment.getSerial());
                        
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
        equipment.setId(((Long)keyHolder.getKeys().get(COL_ID)));
        equipment.setCreatedTimestamp(ts);
        equipment.setLastModifiedTimestamp(ts);


        LOG.debug("Stored Equipment {}", equipment);
        
        return equipment.clone();
    }

    
    public Equipment get(long equipmentId) {
        LOG.debug("Looking up Equipment for id {}", equipmentId);

        try{
            Equipment equipment = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    equipmentRowMapper, equipmentId);
            
            LOG.debug("Found Equipment {}", equipment);
            
            return equipment;
        }catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Equipment getOrNull(long equipmentId) {
        LOG.debug("Looking up Equipment for id {}", equipmentId);

        try{
            Equipment equipment = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    equipmentRowMapper, equipmentId);
            
            LOG.debug("Found Equipment {}", equipment);
            
            return equipment;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find Equipment for id {}", equipmentId);
            return null;
        }
    }

    public Equipment update(Equipment equipment) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = equipment.getLastModifiedTimestamp();
        
        int updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 
                //equipment.getId(), - not updating this one

                //TODO: add remaining properties from Equipment here
        		equipment.getCustomerId(),
        		equipment.getProfileId(),
        		equipment.getLocationId(),
        		equipment.getEquipmentType().getId(),
        		equipment.getInventoryId(),
                equipment.getName(),
                (equipment.getDetails()!=null)?equipment.getDetails().toZippedBytes():null ,
                equipment.getLatitude(),
                equipment.getLongitude(),
                equipment.getSerial(),
                                
                //equipment.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,
                
                // use id for update operation
                equipment.getId(),
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
                    equipment.getId()
                    );
                
                LOG.debug("Concurrent modification detected for Equipment with id {} expected version is {} but version in db was {}", 
                        equipment.getId(),
                        incomingLastModifiedTs,
                        recordTimestamp
                        );
                throw new DsConcurrentModificationException("Concurrent modification detected for Equipment with id " + equipment.getId()
                        +" expected version is " + incomingLastModifiedTs
                        +" but version in db was " + recordTimestamp
                        );
                
            }catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find Equipment for {}", equipment.getId());
                throw new DsEntityNotFoundException("Equipment not found " + equipment.getId());
            }
        }

        //make a copy so that we don't accidentally update caller's version by reference
        Equipment equipmentCopy = equipment.clone();
        equipmentCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Equipment {}", equipmentCopy);
        
        return equipmentCopy;
    }

    
    public Equipment delete(long equipmentId) {
        Equipment ret = get(equipmentId);
        
        this.jdbcTemplate.update(SQL_DELETE, equipmentId);
                
        LOG.debug("Deleted Equipment {}", ret);
        
        return ret;
    }

    public List<Equipment> getAllForCustomer(int customerId) {
        LOG.debug("Looking up Equipments for customer {}", customerId);

        List<Equipment> ret = this.jdbcTemplate.query(SQL_GET_BY_CUSTOMER_ID,
                equipmentRowMapper, customerId);

        if (ret == null) {
            LOG.debug("Cannot find Equipments for customer {}", customerId);
        } else {
            LOG.debug("Found Equipments for customer {} : {}", customerId, ret);
        }

        return ret;
    }

    public List<Equipment> get(Set<Long> equipmentIdSet) {
        LOG.debug("calling get({})", equipmentIdSet);

        if (equipmentIdSet == null || equipmentIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder set = new StringBuilder(256);
        set.append("(");
        for(int i =0; i< equipmentIdSet.size(); i++) {
        		set.append("?,");
        }
        //remove last comma
        set.deleteCharAt(set.length()-1);
        set.append(")");
        
        String query = SQL_GET_ALL_IN_SET + set;
        List<Equipment> results = this.jdbcTemplate.query(query, equipmentIdSet.toArray(), equipmentRowMapper);

        LOG.debug("get({}) returns {} record(s)", equipmentIdSet, (null == results) ? 0 : results.size());
        return results;
    }


	public PaginationResponse<Equipment> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Equipment> context) {

        PaginationResponse<Equipment> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Equipments for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Equipments for customer {} with last returned page number {}", 
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
        List<Equipment> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                equipmentRowMapper);

        if (pageItems == null) {
            LOG.debug("Cannot find Equipments for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} Equipments for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }
	
    public PaginationResponse<Equipment> getForCustomer(int customerId, EquipmentType equipmentType,
            Set<Long> locationIds, List<ColumnAndSort> sortBy, PaginationContext<Equipment> context) {

        PaginationResponse<Equipment> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up equipment {} for customer {} locations {} last returned page number {}",
                    equipmentType, customerId, locationIds, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up equipment {} for customer {} locations {} last returned page number {}", equipmentType,
                customerId, locationIds, context.getLastReturnedPageNumber());

        String query = SQL_GET_BY_CUSTOMER_ID_AND_EQUIPMENT_TYPE;

        // add filters for the query
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.add(equipmentType.getId());

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
            strbSort.append(" id ");
        }

        query += strbSort.toString();

        // add pagination parameters for the query
        query += SQL_PAGING_SUFFIX;

        queryArgs.add(context.getMaxItemsPerPage());
        queryArgs.add(context.getTotalItemsReturned());

        /*
         * https://www.citusdata.com/blog/2016/03/30/five-ways-to-paginate/
         * Choosing offset=1000 makes cost about 19 and has a 0.609 ms execution
         * time. Once offset=5,000,000 the cost goes up to 92734 and execution
         * time is 758.484 ms. - DT: still acceptable for our use case
         */
        List<Equipment> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                equipmentRowMapper);

        if (pageItems == null) {
            LOG.debug("Cannot find equipment {} for customer {} locations {} last returned page number {}",
                    equipmentType, customerId, locationIds, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} equipment {} for customer {} locations {} last returned page number {}",
                    pageItems.size(), equipmentType, customerId, locationIds, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }
	
}
