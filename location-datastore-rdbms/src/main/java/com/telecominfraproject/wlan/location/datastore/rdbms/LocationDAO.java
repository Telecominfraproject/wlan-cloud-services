package com.telecominfraproject.wlan.location.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
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
import com.telecominfraproject.wlan.core.model.utils.JsonPatchException;
import com.telecominfraproject.wlan.core.model.utils.JsonPatchUtil;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.server.exceptions.SerializationException;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class LocationDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(LocationDatastoreRdbms.class);

    private static final String COL_ID = "id";

    // skip filling the details; instead fill in detailsBin
    private static final String DETAILS_ID = "details";

    private static final String[] ALL_COLUMNS_LIST = { COL_ID,

            // add columns from properties of Location in here
            "locationType", "customerId", "name", "parentId", DETAILS_ID,
            // make sure the order of properties matches this list and list in
            // LocationRowMapper and list in create/update
            // methods

            "createdTimestamp", "lastModifiedTimestamp" , "detailsBin"};

    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID, DETAILS_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, DETAILS_ID, "createdTimestamp"));

    private static final String TABLE_NAME = "equipment_location";
    private static final String TABLE_PREFIX = "s.";
    private static final String ALL_COLUMNS;
    
    private static final Set<String> ALL_COLUMNS_LOWERCASE = new HashSet<>();

    @SuppressWarnings("unused")
    // use this for queries where multiple tables are involved
    private static final String ALL_COLUMNS_WITH_PREFIX;

    private static final String ALL_COLUMNS_FOR_INSERT;
    private static final String BIND_VARS_FOR_INSERT;
    private static final String ALL_COLUMNS_UPDATE;

    static {
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsWithPrefix = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        StringBuilder strbColumnsForUpdate = new StringBuilder(512);
        for (String colName : ALL_COLUMNS_LIST) {

            ALL_COLUMNS_LOWERCASE.add(colName.toLowerCase());

            strbAllColumns.append(colName).append(",");
            strbAllColumnsWithPrefix.append(TABLE_PREFIX).append(colName).append(",");

            if (!columnsToSkipForInsert.contains(colName)) {
                strbAllColumnsForInsert.append(colName).append(",");
                strbBindVarsForInsert.append("?,");
            }

            if (!columnsToSkipForUpdate.contains(colName)) {
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

    private static final String SQL_GET_BY_ID = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " " + " where id = ?";

    private static final String SQL_GET_BY_CUSTOMER_ID = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " "
            + " where customerId = ?";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from " + TABLE_NAME + " "
            + " where id = ?";

    private static final String SQL_INSERT = "insert into " + TABLE_NAME + " ( " + ALL_COLUMNS_FOR_INSERT
            + " ) values ( " + BIND_VARS_FOR_INSERT + " ) ";

    private static final String SQL_DELETE = "delete from " + TABLE_NAME + " where id = ? ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + ALL_COLUMNS_UPDATE + " where id = ? "
            + " and ( lastModifiedTimestamp = ? or ? = true) ";

    private static final String SQL_GET_ALL_CHILDREN = "WITH RECURSIVE recursetree("+ALL_COLUMNS+") AS ("
            + " SELECT "+ALL_COLUMNS+" FROM "+TABLE_NAME+" WHERE parentid = ?"
            + " UNION "
            + " SELECT "+ALL_COLUMNS_WITH_PREFIX 
            + " FROM "+TABLE_NAME+" s" + " JOIN recursetree rt ON rt.id = s.parentid)"
            + " SELECT * FROM recursetree";

    private static final String SQL_GET_TOP_LEVEL_LOCATION = "WITH RECURSIVE recursetree("+ALL_COLUMNS+") AS ("
            + " SELECT "+ALL_COLUMNS+" FROM "+TABLE_NAME+" WHERE id = ?"
            + " UNION "
            + " SELECT " + ALL_COLUMNS_WITH_PREFIX
            + " FROM "+TABLE_NAME+" s" + " JOIN recursetree rt ON rt.parentid = s.id)"
            + " SELECT * FROM recursetree where parentid is null";

    private static final String SQL_GET_ALL_TOP_LEVEL = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " "
            + " where parentid is null";

    private static final String SQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where "+ COL_ID +" in ";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";

    private static final RowMapper<Location> locationRowMapper = new LocationRowMapper();

    protected static final String[] GENERATED_KEY_COLS = { COL_ID };

    @Autowired(required = false)
    public void setDataSource(LocationDataSourceInterface dataSource) {
        setDataSource((DataSource) dataSource);
    }

    public Location create(final Location location) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                            keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS));
                    int colIdx = 1;

                    // add properties from Location here
                    ps.setInt(colIdx++, location.getLocationType().getId());
                    ps.setInt(colIdx++, location.getCustomerId());
                    ps.setString(colIdx++, location.getName());
                    if(location.getParentId()>0) {
	                    ps.setLong(colIdx++, location.getParentId());
	                }else {
	                	ps.setNull(colIdx++, Types.BIGINT);
	                }
                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);
                    ps.setBytes(colIdx++, (location.getDetails() != null) ? location.getDetails().toZippedBytes() : null);
                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        // keyHolder.getKey() now contains the generated key
        location.setId((Long) keyHolder.getKeys().get(COL_ID));
        location.setCreatedTimestamp(ts);
        location.setLastModifiedTimestamp(ts);

        LOG.debug("Stored Location {}", location);

        return location.clone();
    }

    public Location get(long locationId) {
        LOG.debug("Looking up Location for id {}", locationId);

        try {
            Location location = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID,
                    locationRowMapper, locationId);

            LOG.debug("Found Location {}", location);

            return location;
        } catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    public Location update(Location location) {
        long incomingLastModifiedTs = location.getLastModifiedTimestamp();
        long newLastModifiedTs = getNewLastModTs(incomingLastModifiedTs);

        int updateCount = this.jdbcTemplate.update(SQL_UPDATE,
                // location.getId(), - not updating this one
                // Add remaining properties from Location here
                location.getLocationType().getId(), 
                location.getCustomerId(),
                location.getName(), 
                (location.getParentId() > 0) ? location.getParentId() : null,
                // location.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,
                (location.getDetails() != null) ? location.getDetails().toZippedBytes() : null,
                // use id for update operation
                location.getId(),
                // use lastModifiedTimestamp for data protection against
                // concurrent modifications
                incomingLastModifiedTs, isSkipCheckForConcurrentUpdates());

        if (updateCount == 0) {

            try {

                if (isSkipCheckForConcurrentUpdates()) {
                    // in this case we did not request protection against
                    // concurrent updates,
                    // so the updateCount is 0 because record in db was not
                    // found
                    throw new EmptyResultDataAccessException(1);
                }

                // find out if record could not be updated because it does not
                // exist or because it was modified concurrently
                long recordTimestamp = this.jdbcTemplate.queryForObject(SQL_GET_LASTMOD_BY_ID, Long.class,
                        location.getId());

                LOG.debug(
                        "Concurrent modification detected for Location {} expected version is {} but version in db was {}",
                        location.getId(), incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for Location "
                                + location.getId() + " expected version is " + incomingLastModifiedTs
                                + " but version in db was " + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find Location {}", location.getId());
                throw new DsEntityNotFoundException(
                        "Location not found " + location.getId());
            }
        }

        // make a copy so that we don't accidentally update caller's version by
        // reference
        Location locationCopy = location.clone();
        locationCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated Location {}", locationCopy);

        return locationCopy;
    }

    @Transactional(noRollbackFor = { DsConcurrentModificationException.class })
    public Location updateWithNoRollback(Location location) {
        return update(location);
    }

    public Location delete(long locationId) {
        Location ret = get(locationId);

        this.jdbcTemplate.update(SQL_DELETE, locationId);

        LOG.debug("Deleted Location {}", ret);

        return ret;
    }

    public List<Location> getAllLocationsForCustomer(int customerId) {
        LOG.debug("Looking up Locations for customer {}", customerId);

        List<Location> ret = this.jdbcTemplate.query(SQL_GET_BY_CUSTOMER_ID,
                locationRowMapper, customerId);

        LOG.debug("Found Locations for customer {} : {}", customerId, ret);

        return ret;
    }

    public List<Location> getAllTopLocations() {
        LOG.debug("getAllTopLocations()");

        List<Location> ret = this.jdbcTemplate.query(SQL_GET_ALL_TOP_LEVEL,
                locationRowMapper);

        LOG.debug("Found all top-level locations: {}", ret);

        return ret;
    }

    public List<Location> getAllDescendants(long locationId) {
        LOG.debug("Looking up all children Locations for parent {}", locationId);

        List<Location> ret = this.jdbcTemplate.query(SQL_GET_ALL_CHILDREN,
                locationRowMapper, locationId);

        LOG.debug("Found children Locations for parent {} : {}", locationId, ret);

        return ret;
    }

    public Location getTopLevelLocation(long locationId) {
        LOG.debug("Looking up top-level Location for child {}", locationId);
        Location ret = null;

        try {
            ret = this.jdbcTemplate.queryForObject(SQL_GET_TOP_LEVEL_LOCATION, locationRowMapper,
                    locationId);
        } catch (EmptyResultDataAccessException e) {
            // we'll log below.
        }

        if (ret == null) {
            LOG.warn("Cannot find the Location parent for child {}. Everyone should have a parent! ",
                    locationId);
        } else {
            LOG.debug("Found top-level Location for child {} : {}", locationId, ret);
        }

        return ret;
    }

    public List<Location> get(Set<Long> locationIdSet) {
        LOG.debug("calling get({})", locationIdSet);

        if (locationIdSet == null || locationIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder set = new StringBuilder(256);
        set.append("(");
        for(int i =0; i< locationIdSet.size(); i++) {
        		set.append("?,");
        }
        //remove last comma
        set.deleteCharAt(set.length()-1);
        set.append(")");
        
        String query = SQL_GET_ALL_IN_SET + set;
        List<Location> results = this.jdbcTemplate.query(query, locationIdSet.toArray(), locationRowMapper);

        LOG.debug("get({}) returns {} record(s)", locationIdSet, results.size());
        return results;
    }


	public PaginationResponse<Location> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Location> context) {

        PaginationResponse<Location> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Locations for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Locations for customer {} with last returned page number {}", 
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
        List<Location> pageItems = this.jdbcTemplate.query(query, queryArgs.toArray(),
                locationRowMapper);

        LOG.debug("Found {} Locations for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }

}
