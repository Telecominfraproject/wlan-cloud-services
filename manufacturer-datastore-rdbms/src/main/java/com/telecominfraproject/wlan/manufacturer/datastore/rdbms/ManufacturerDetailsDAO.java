package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;

/**
 * @author mpreston
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ManufacturerDetailsDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerDetailsDAO.class);

    private static final String COL_ID = "id";

    private static final String[] GENERATED_KEY_COLS = { COL_ID };

    private static final int MAX_RESULTS = 400;

    private static final String[] ALL_COLUMNS_LIST = {
            // bigint
            COL_ID,
            // add colums from properties ClientManufacturerDetailsRecord in
            // here

            // string
            "manufacturerName",
            // string
            "manufacturerAlias",
            // big int
            "createdTimestamp",
            // bigint
            "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));

    private static final String TABLE_NAME = "manufacturer_details";
    private static final String ALL_COLUMNS;
    private static final String ALL_COLUMNS_FOR_INSERT;
    private static final String BIND_VARS_FOR_INSERT;
    private static final String ALL_COLUMNS_UPDATE;

    static {
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        StringBuilder strbColumnsForUpdate = new StringBuilder(512);
        for (String colName : ALL_COLUMNS_LIST) {

            strbAllColumns.append(colName).append(",");

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
        strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
        strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
        strbColumnsForUpdate.deleteCharAt(strbColumnsForUpdate.length() - 1);

        ALL_COLUMNS = strbAllColumns.toString();
        ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
        BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
        ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();

    }

    private static final String SQL_INSERT = "insert into " + TABLE_NAME + " ( " + ALL_COLUMNS_FOR_INSERT
            + " ) values ( " + BIND_VARS_FOR_INSERT + " ) ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + ALL_COLUMNS_UPDATE + " where id = ? "
            + " and ( lastModifiedTimestamp = ? or ? = true) ";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from " + TABLE_NAME + " "
            + " where id = ?";

    private static final String SQL_DELETE = "delete from " + TABLE_NAME + " where id = ? ";

    private static final String SQL_GET_BY_ID = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " " + " where id = ?";

    private static final String SQL_GET_BY_MANUFACTURER = "select " + ALL_COLUMNS + " from " + TABLE_NAME
            + " where lower(manufacturerName) like lower(?) or lower(manufacturerAlias) like lower(?) limit "
            + MAX_RESULTS;

    private static final String SQL_GET_ALIAS_BEGINS_WITH = "select manufacturerAlias " + " from " + TABLE_NAME
            + " where lower(manufacturerAlias) like lower(?) limit ?";

    private static final RowMapper<ManufacturerDetailsRecord> ManufacturerDetailsRowMapper = new ManufacturerDetailsRowMapper();

    @Transactional(noRollbackFor = { DuplicateKeyException.class })
    public ManufacturerDetailsRecord create(final ManufacturerDetailsRecord manufacturerDetails) {
        LOG.debug("create({})", manufacturerDetails);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                            keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS));
                    int colIdx = 1;

                    ps.setString(colIdx++, manufacturerDetails.getManufacturerName());
                    ps.setString(colIdx++, manufacturerDetails.getManufacturerAlias());

                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            DsDuplicateEntityException exp = new DsDuplicateEntityException(e);
            LOG.debug("create({}) throws {}", manufacturerDetails, exp.getLocalizedMessage(), exp);
            throw exp;
        }

        // keyHolder.getKey() now contains the generated key
        ManufacturerDetailsRecord result = manufacturerDetails.clone();
        result.setId(((Long) keyHolder.getKeys().get(COL_ID)));
        result.setCreatedTimestamp(ts);
        result.setLastModifiedTimestamp(ts);

        LOG.debug("create() returns {}", result);

        return result;
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public List<ManufacturerDetailsRecord> getByManufacturer(String manufacturer, boolean exactMatch) {
        LOG.debug("getByManufacturer('{}', {})", manufacturer, exactMatch);

        List<ManufacturerDetailsRecord> result = new ArrayList<>();

        try {
            String searchString = (exactMatch) ? manufacturer : (manufacturer + "%");
            result = this.jdbcTemplate.query(SQL_GET_BY_MANUFACTURER, ManufacturerDetailsRowMapper, searchString,
                    searchString);

        } catch (EmptyResultDataAccessException e) {
            // ignore
        }
        LOG.debug("getByManufacturer('{}', {}) returns {}", manufacturer, exactMatch, result.size());
        return result;
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public ManufacturerDetailsRecord getById(long id) {
        LOG.debug("getById({})", id);

        try {
            ManufacturerDetailsRecord manufacturerDetails = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID,
                    ManufacturerDetailsRowMapper, id);

            LOG.debug("getById({}) returns {}", id, manufacturerDetails);
            return manufacturerDetails;
        } catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    public ManufacturerDetailsRecord update(ManufacturerDetailsRecord manufacturerDetails) {
        LOG.debug("update({})", manufacturerDetails);
        long incomingLastModifiedTs = manufacturerDetails.getLastModifiedTimestamp();
        long newLastModifiedTs = getNewLastModTs(incomingLastModifiedTs);

        int updateCount = this.jdbcTemplate.update(SQL_UPDATE,
                // manufacturerDetails.getId(), - not updating this one

                // Add remaining properties from ClientInfoRecord here
                manufacturerDetails.getManufacturerName(), manufacturerDetails.getManufacturerAlias(),

                // clientInfo.getCreatedTimestamp(), - not updating this one
                newLastModifiedTs,

                // use id for update operation
                manufacturerDetails.getId(),
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
                        manufacturerDetails.getId());

                LOG.debug(
                        "Concurrent modification detected for ManufacturerDetailsRecord with id {} expected version is {} but version in db was {}",
                        manufacturerDetails.getId(), incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for ManufacturerDetailsRecord with id "
                                + manufacturerDetails.getId() + " expected version is " + incomingLastModifiedTs
                                + " but version in db was " + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find ManufacturerDetailsRecord for {}", manufacturerDetails.getId());
                throw new DsEntityNotFoundException(
                        "ManufacturerDetailsRecord not found " + manufacturerDetails.getId());
            }
        }

        // make a copy so that we don't accidentally update caller's version by
        // reference
        ManufacturerDetailsRecord manufacturerDetailsCopy = manufacturerDetails.clone();
        manufacturerDetailsCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("update() returns {}", manufacturerDetailsCopy);

        return manufacturerDetailsCopy;
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public ManufacturerDetailsRecord delete(long id) {
        LOG.debug("delete({})", id);
        try {
            ManufacturerDetailsRecord ret = getById(id);
            if (null != ret) {
                this.jdbcTemplate.update(SQL_DELETE, ret.getId());
            }
            LOG.debug("delete({}) returns {}", id, ret);

            return ret;
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("Cannot find ManufacturerDetailsRecord for {} to delete", id);
            throw new DsEntityNotFoundException("ManufacturerDetailsRecord not found " + id);
        }
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public List<String> getStoresAliasValuesThatBeginWith(String prefix, int maxResults) {
        LOG.debug("Looking up alias names that begin with {}", prefix);

        List<String> result = new ArrayList<>();

        if (maxResults < 0 || maxResults > 1000) {
            maxResults = 1000;
        }

        try {
            result = this.jdbcTemplate.queryForList(SQL_GET_ALIAS_BEGINS_WITH, String.class, prefix + "%", maxResults);

            LOG.debug("Found {} aliases", result.size());
            return result;
        } catch (EmptyResultDataAccessException e) {
            return result;
        }
    }
}
