package com.telecominfraproject.wlan.location.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
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

import com.telecominfraproject.wlan.core.model.utils.JsonPatchException;
import com.telecominfraproject.wlan.core.model.utils.JsonPatchUtil;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.server.exceptions.SerializationException;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class LocationDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(LocationDatastoreRdbms.class);

    private static final LocationDetails DEFAULT_DETAILS = LocationDetails.createWithDefaults();

    private static final String COL_ID = "id";

    private static final String[] ALL_COLUMNS_LIST = { COL_ID,

            // add columns from properties of Location in here
            "locationType", "customerId", "name", "parentId", "details",
            // make sure the order of properties matches this list and list in
            // LocationRowMapper and list in create/update
            // methods

            "createdTimestamp", "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));

    private static final String TABLE_NAME = "equipment_location";
    private static final String TABLE_PREFIX = "s.";
    private static final String ALL_COLUMNS;

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

    private static final String SQL_GET_TOP_LEVEL_CITY = "WITH RECURSIVE recursetree("+ALL_COLUMNS+") AS ("
            + " SELECT "+ALL_COLUMNS+" FROM "+TABLE_NAME+" WHERE id = ?"
            + " UNION "
            + " SELECT " + ALL_COLUMNS_WITH_PREFIX
            + " FROM "+TABLE_NAME+" s" + " JOIN recursetree rt ON rt.parentid = s.id)"
            + " SELECT * FROM recursetree where parentid = 0";

    private static final String SQL_GET_ALL_CITIES = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " "
            + " where parentid = 0";

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
                    ps.setLong(colIdx++, location.getParentId());
                    ps.setString(colIdx++, generatePatch(location.getDetails()));
                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);
                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        // keyHolder.getKey() now contains the generated key
        location.setId(((Long) keyHolder.getKeys().get(COL_ID)).intValue());
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
                location.getLocationType().getId(), location.getCustomerId(),
                location.getName(), location.getParentId(),
                generatePatch(location.getDetails()),
                // location.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,

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

        if (ret == null) {
            LOG.debug("Cannot find Locations for customer {}", customerId);
        } else {
            LOG.debug("Found Locations for customer {} : {}", customerId, ret);
        }

        return ret;
    }

    public List<Location> getAllTopLocations() {
        LOG.debug("getAllTopLocations()");

        List<Location> ret = this.jdbcTemplate.query(SQL_GET_ALL_CITIES,
                locationRowMapper);

        if (ret == null) {
            LOG.error("Cannot find any top-level locations in the DB. This can't be good");
        } else {
            LOG.debug("Found all top-level locations: {}", ret);
        }

        return ret;
    }

    public List<Location> getAllDescendants(long locationId) {
        LOG.debug("Looking up all children Locations for parent {}", locationId);

        List<Location> ret = this.jdbcTemplate.query(SQL_GET_ALL_CHILDREN,
                locationRowMapper, locationId);

        if (ret == null) {
            LOG.debug("Cannot find Locations that have parent {}", locationId);
        } else {
            LOG.debug("Found children Locations for parent {} : {}", locationId, ret);
        }

        return ret;
    }

    public Location getTopLevelLocation(long locationId) {
        LOG.debug("Looking up top-level Location for child {}", locationId);
        Location ret = null;

        try {
            ret = this.jdbcTemplate.queryForObject(SQL_GET_TOP_LEVEL_CITY, locationRowMapper,
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

    private static String generatePatch(LocationDetails details) {
        try {
            if (details != null) {
                return JsonPatchUtil.generatePatch(DEFAULT_DETAILS, details);
            } else {
                return null;
            }
        } catch (JsonPatchException e) {
            throw new SerializationException(e);
        }
    }

    static LocationDetails generateDetails(String patch) {
        try {
            if (patch != null) {
                return JsonPatchUtil.apply(DEFAULT_DETAILS, patch, LocationDetails.class);
            } else {
                return DEFAULT_DETAILS;
            }
        } catch (JsonPatchException e) {
            throw new SerializationException(e);
        }
    }

}
