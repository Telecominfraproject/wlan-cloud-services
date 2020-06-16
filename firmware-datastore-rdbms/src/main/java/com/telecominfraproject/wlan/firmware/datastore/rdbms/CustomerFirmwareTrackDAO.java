package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
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

import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.firmware.datastore.CustomerFirmwareTrackDaoInterface;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;

/**
 * @author ekeddy
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class CustomerFirmwareTrackDAO extends BaseJdbcDao implements CustomerFirmwareTrackDaoInterface {
    private static final Logger LOG = LoggerFactory.getLogger(FirmwareDatastoreRdbms.class);

    private static final String[] ALL_COLUMNS_LIST = {

            // add colums from properties FirmwareTrackRecord in here
            "trackId", "customerId", "settings",
            // make sure the order of properties matches this list and list in
            // FirmwareTrackRecordRowMapper and list in create/update methods
            "createdTimestamp", "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = Collections.emptySet();
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(
            Arrays.asList("createdTimestamp", "trackId", "customerId"));

    private static final String TABLE_NAME = "customer_track_assignment";
    private static final String TABLE_PREFIX = "a.";
    private static final String ALL_COLUMNS;

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
        ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
        BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
        ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();

    }

    private static final String SQL_GET_BY_CUSTOMER_ID = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " "
            + " where customerId = ?";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from " + TABLE_NAME + " "
            + " where customerId = ?";

    private static final String SQL_INSERT = "insert into " + TABLE_NAME + " ( " + ALL_COLUMNS_FOR_INSERT
            + " ) values ( " + BIND_VARS_FOR_INSERT + " ) ";

    private static final String SQL_DELETE = "delete from " + TABLE_NAME + " where customerId = ? ";

    private static final String SQL_DELETE_FOR_TRACKID = "delete from " + TABLE_NAME + " where trackId = ? ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + ALL_COLUMNS_UPDATE
            + " where customerId = ? " + " and ( lastModifiedTimestamp = ? or ? = true) ";

    private static final RowMapper<CustomerFirmwareTrackRecord> rowMapper = new CustomerTrackAssignmentRowMapper();

    @Autowired(required = false)
    public void setDataSource(FirmwareDataSourceInterface dataSource) {
        setDataSource((DataSource) dataSource);
    }

    public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(final CustomerFirmwareTrackRecord record) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
                    int colIdx = 1;

                    ps.setLong(colIdx++, record.getTrackRecordId());
                    ps.setLong(colIdx++, record.getCustomerId());
                    ps.setString(colIdx++, record.getSettings() == null ? null : record.getSettings().toString());
                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        CustomerFirmwareTrackRecord ret = record.clone();
        ret.setCreatedTimestamp(ts);
        ret.setLastModifiedTimestamp(ts);

        LOG.debug("Stored CustomerFirmwareTrackRecord {}", ret);

        return ret;
    }

    public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
        long incomingLastModifiedTs = record.getLastModifiedTimestamp();
        long newLastModifiedTs = getNewLastModTs(incomingLastModifiedTs);

        int updateCount = this.jdbcTemplate.update(SQL_UPDATE,

                // add properties from FirmwareTrackRecord here
                record.getSettings() == null ? null : record.getSettings().toString(),

                newLastModifiedTs,

                // use id for update operation
                record.getCustomerId(),

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
                        record.getCustomerId());

                LOG.debug(
                        "Concurrent modification detected for FirmwareTrackAssignmentRecord with customerId {} expected version is {} but version in db was {}",
                        record.getCustomerId(), incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for FirmwareTrackAssignmentRecord with customerId "
                                + record.getCustomerId() + " expected version is " + incomingLastModifiedTs
                                + " but version in db was " + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find CustomerFirmwareTrackRecord for customerId {}", record.getCustomerId());
                throw new DsEntityNotFoundException(
                        "CustomerFirmwareTrackRecord not found for customerId  " + record.getCustomerId());
            }
        }
        // make a copy so that we don't accidentally update caller's version by
        // reference
        CustomerFirmwareTrackRecord copy = record.clone();
        copy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated CustomerFirmwareTrackRecord {}", copy);

        return copy;
    }

    public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId) {
        CustomerFirmwareTrackRecord ret = null;
        try {
            ret = this.jdbcTemplate.queryForObject(SQL_GET_BY_CUSTOMER_ID, rowMapper, customerId);

            LOG.debug("Found CustomerFirmwareTrackRecord {}", ret);

        } catch (EmptyResultDataAccessException e) {
            LOG.debug("CustomerFirmwareTrackRecord not found for customerId {}", customerId);
        }
        return ret;
    }

    public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId) {
        CustomerFirmwareTrackRecord ret = getCustomerFirmwareTrackRecord(customerId);
        this.jdbcTemplate.update(SQL_DELETE, customerId);
        LOG.debug("Deleted CustomerFirmwareTrackRecord {}", ret);
        return ret;
    }

	public void deleteCustomerFirmwareTrackRecords(long firmwareTrackRecordId) {
        this.jdbcTemplate.update(SQL_DELETE_FOR_TRACKID, firmwareTrackRecordId);
        LOG.debug("Deleted CustomerFirmwareTrackRecords for track {}", firmwareTrackRecordId);
	}

}
