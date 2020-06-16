package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
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

import com.telecominfraproject.wlan.core.model.scheduler.ScheduleSetting;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;

/**
 * @author ekeddy
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class FirmwareTrackDAO extends BaseJdbcDao {
    private static final Logger LOG = LoggerFactory.getLogger(FirmwareDatastoreRdbms.class);

    private static final String COL_ID = "id";

    private static final String[] GENERATED_KEY_COLS = { COL_ID };

    private static final String[] ALL_COLUMNS_LIST = { COL_ID,
            // add colums from properties FirmwareTrackRecord in here
            "trackName",
            // make sure the order of properties matches this list and list in
            // FirmwareTrackRecordRowMapper and list in create/update methods
            "maintenanceWindow", "createdTimestamp", "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));

    private static final String TABLE_NAME = "firmware_track";
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

    private static final String SQL_GET_BY_NAME = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " "
            + " where trackName = ?";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from " + TABLE_NAME + " "
            + " where id = ?";

    private static final String SQL_INSERT = "insert into " + TABLE_NAME + " ( " + ALL_COLUMNS_FOR_INSERT
            + " ) values ( " + BIND_VARS_FOR_INSERT + " ) ";

    private static final String SQL_DELETE = "delete from " + TABLE_NAME + " where id = ? ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + ALL_COLUMNS_UPDATE + " where id = ? "
            + " and ( lastModifiedTimestamp = ? or ? = true) ";

    private static final RowMapper<FirmwareTrackRecord> rowMapper = new FirmwareTrackRecordRowMapper();

    @Autowired(required = false)
    public void setDataSource(FirmwareDataSourceInterface dataSource) {
        setDataSource((DataSource) dataSource);
    }

    @Transactional(noRollbackFor = { DuplicateKeyException.class })
    public FirmwareTrackRecord createFirmwareTrack(final FirmwareTrackRecord trackRecord) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                            keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS));
                    int colIdx = 1;

                    ps.setString(colIdx++, trackRecord.getTrackName());
                    ScheduleSetting setting = trackRecord.getMaintenanceWindow();
                    if (null == setting) {
                        ps.setNull(colIdx++, java.sql.Types.VARCHAR);
                    } else {
                        ps.setString(colIdx++, setting.toString());
                    }
                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        FirmwareTrackRecord ret = trackRecord.clone();
        ret.setRecordId(((Long) keyHolder.getKeys().get(COL_ID)));
        ret.setCreatedTimestamp(ts);
        ret.setLastModifiedTimestamp(ts);

        LOG.debug("Stored FirmwareTrackRecord {}", ret);

        return ret;
    }

    public FirmwareTrackRecord getFirmwareTrackByName(String versionName) {
        FirmwareTrackRecord ret = getFirmwareTrackByNameOrNull(versionName);
        if (ret == null) {
            throw new DsEntityNotFoundException("FirmwareTrackRecord not found " + versionName);
        }
        return ret;
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public FirmwareTrackRecord getFirmwareTrackByNameOrNull(String firmwareTrackName) {
        LOG.debug("Looking up FirmwareTrackRecord for name {}", firmwareTrackName);

        try {
            FirmwareTrackRecord ret = this.jdbcTemplate.queryForObject(SQL_GET_BY_NAME, rowMapper, firmwareTrackName);
            LOG.debug("Found FirmwareTrackRecord {}", ret);
            return ret;
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("Cannot find FirmwareTrackRecord {}", firmwareTrackName);
            return null;
        }
    }

    public FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackRecordId) {
        LOG.debug("Looking up FirmwareTrackRecord for id {}", firmwareTrackRecordId);

        try {
            FirmwareTrackRecord ret = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID, rowMapper, firmwareTrackRecordId);
            LOG.debug("Found FirmwareTrackRecord {}", ret);
            return ret;
        } catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    public FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord) {
        long incomingLastModifiedTs = firmwareTrackRecord.getLastModifiedTimestamp();
        long newLastModifiedTs = getNewLastModTs(incomingLastModifiedTs);

        String windowSetting = null;
        if (null != firmwareTrackRecord.getMaintenanceWindow()) {
            windowSetting = firmwareTrackRecord.getMaintenanceWindow().toString();
        }
        int updateCount = this.jdbcTemplate.update(SQL_UPDATE,
                // add properties from FirmwareTrackRecord here
                firmwareTrackRecord.getTrackName(), windowSetting, newLastModifiedTs,
                // use id for update operation
                firmwareTrackRecord.getRecordId(),
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
                long recordTimestamp = this.jdbcTemplate.queryForObject(SQL_GET_LASTMOD_BY_ID,
                        new Object[] { firmwareTrackRecord.getRecordId() }, Long.class);

                LOG.debug(
                        "Concurrent modification detected for FirmwareTrackRecord with id {} expected version is {} but version in db was {}",
                        firmwareTrackRecord.getRecordId(), incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for FirmwareTrackRecord with id "
                                + firmwareTrackRecord.getRecordId() + " expected version is " + incomingLastModifiedTs
                                + " but version in db was " + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find FirmwareTrackRecord for {}", firmwareTrackRecord.getRecordId());
                throw new DsEntityNotFoundException(
                        "FirmwareTrackRecord not found " + firmwareTrackRecord.getRecordId());
            }
        }
        // make a copy so that we don't accidentally update caller's version by
        // reference
        FirmwareTrackRecord copy = firmwareTrackRecord.clone();
        copy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated FirmwareTrackRecord {}", copy);

        return copy;
    }

    public FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackRecordId) {
        FirmwareTrackRecord ret = getFirmwareTrackById(firmwareTrackRecordId);
        this.jdbcTemplate.update(SQL_DELETE, firmwareTrackRecordId);
        LOG.debug("Deleted FirmwareTrackRecord {}", ret);
        return ret;
    }

}
