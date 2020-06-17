package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author ekeddy
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class FirmwareTrackAssignmentDAO extends BaseJdbcDao {
    private static final Logger LOG = LoggerFactory.getLogger(FirmwareDatastoreRdbms.class);

    private static final String[] ALL_COLUMNS_LIST = {

            // add colums from properties FirmwareTrackRecord in here
            "trackId", "firmwareId", "defaultForTrack", "deprecated",
            // make sure the order of properties matches this list and list in
            // FirmwareTrackRecordRowMapper and list in create/update methods

            "createdTimestamp", "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = Collections.emptySet();
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(
            Arrays.asList("createdTimestamp", "trackId", "firmwareId"));

    private static final String TABLE_NAME = "firmware_track_assignment";
    private static final String TABLE_PREFIX = "a.";
    private static final String ALL_COLUMNS;

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

    private static final String SQL_GET_BY_TRACK_ID_AND_VERSION_ID = "select " + ALL_COLUMNS + " from " + TABLE_NAME
            + " " + " where trackId = ? and firmwareId = ?";

    private static final String SQL_GET_BY_TRACK_ID = "select " + ALL_COLUMNS + " from " + TABLE_NAME + " "
            + " where trackId = ?";

    private static final String SQL_GET_FIRMWARE_VERSIONS_BY_TRACK_ID = "select "
            + FirmwareVersionDAO.ALL_COLUMNS_WITH_PREFIX + ", " + ALL_COLUMNS_WITH_PREFIX + " from "
            + FirmwareVersionDAO.TABLE_NAME + " s, " + TABLE_NAME + " a"
            + " where a.trackId = ? and s.id = a.firmwareId";

    private static final String SQL_GET_DEFAULT_FIRMWARE_VERSIONS_BY_TRACK_ID = "select "
            + FirmwareVersionDAO.ALL_COLUMNS_WITH_PREFIX + ", " + ALL_COLUMNS_WITH_PREFIX + " from "
            + FirmwareVersionDAO.TABLE_NAME + " s, " + TABLE_NAME + " a"
            + " where a.trackId = ? and a.defaultfortrack = true and s.id = a.firmwareId";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from " + TABLE_NAME + " "
            + " where trackId = ? and firmwareId = ?";

    private static final String SQL_INSERT = "insert into " + TABLE_NAME + " ( " + ALL_COLUMNS_FOR_INSERT
            + " ) values ( " + BIND_VARS_FOR_INSERT + " ) ";

    private static final String SQL_DELETE = "delete from " + TABLE_NAME + " where trackId = ? and firmwareId = ? ";

    private static final String SQL_DELETE_FOR_TRACKID = "delete from " + TABLE_NAME + " where trackId = ? ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + ALL_COLUMNS_UPDATE
            + " where trackId = ? and firmwareId = ? " + " and ( lastModifiedTimestamp = ? or ? = true) ";

    private static final RowMapper<FirmwareTrackAssignmentRecord> rowMapper = new FirmwareTrackAssignmentRowMapper();
    private static final RowMapper<FirmwareTrackAssignmentDetails> firmwareVersionRowMapper = new FirmwareTrackAssignmentDetailsRowMapper();

	@Autowired 
	private FirmwareVersionDAO firmwareVersionDatastore;
	
	@Autowired 
	private FirmwareTrackDAO firmwareTrackDatastore;

    @Autowired(required = false)
    public void setDataSource(FirmwareDataSourceInterface dataSource) {
        setDataSource((DataSource) dataSource);
    }

    public FirmwareTrackAssignmentRecord createOrUpdateFirmwareTrackAssignment(
            FirmwareTrackAssignmentRecord assignmentRecord) {
        FirmwareTrackAssignmentRecord existing = getFirmwareTrackAssignmentOrNull(assignmentRecord.getTrackRecordId(),
                assignmentRecord.getFirmwareVersionRecordId());
        if (existing == null) {
            return create(assignmentRecord);
        } else {
            return update(assignmentRecord);
        }
    }

    public FirmwareTrackAssignmentRecord create(final FirmwareTrackAssignmentRecord assignmentRecord) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
                    int colIdx = 1;

                    ps.setLong(colIdx++, assignmentRecord.getTrackRecordId());
                    ps.setLong(colIdx++, assignmentRecord.getFirmwareVersionRecordId());
                    ps.setBoolean(colIdx++, assignmentRecord.isDefaultRevisionForTrack());
                    ps.setBoolean(colIdx++, assignmentRecord.isDeprecated());
                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        FirmwareTrackAssignmentRecord ret = assignmentRecord.clone();
        ret.setCreatedTimestamp(ts);
        ret.setLastModifiedTimestamp(ts);

        LOG.debug("Stored FirmwareTrackAssignmentRecord {}", ret);

        return ret;
    }

    public FirmwareTrackAssignmentRecord update(FirmwareTrackAssignmentRecord assignmentRecord) {
        long incomingLastModifiedTs = assignmentRecord.getLastModifiedTimestamp();
        long newLastModifiedTs = getNewLastModTs(incomingLastModifiedTs);

        int updateCount = this.jdbcTemplate.update(SQL_UPDATE,

                // add properties from FirmwareTrackRecord here
                assignmentRecord.isDefaultRevisionForTrack(), assignmentRecord.isDeprecated(),

                newLastModifiedTs,

                // use id for update operation
                assignmentRecord.getTrackRecordId(), assignmentRecord.getFirmwareVersionRecordId(),

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
                        assignmentRecord.getTrackRecordId(), assignmentRecord.getFirmwareVersionRecordId());

                LOG.debug(
                        "Concurrent modification detected for FirmwareTrackAssignmentRecord with trackId {} and versionId {} expected version is {} but version in db was {}",
                        assignmentRecord.getTrackRecordId(), assignmentRecord.getFirmwareVersionRecordId(),
                        incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for FirmwareTrackAssignmentRecord with trackId "
                                + assignmentRecord.getTrackRecordId() + " and versionId "
                                + assignmentRecord.getFirmwareVersionRecordId() + " expected version is "
                                + incomingLastModifiedTs + " but version in db was " + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find FirmwareTrackAssignmentRecord for trackId {} and versionId {}",
                        assignmentRecord.getTrackRecordId(), assignmentRecord.getFirmwareVersionRecordId());
                throw new DsEntityNotFoundException(
                        "FirmwareTrackAssignmentRecord not found for trackId  " + assignmentRecord.getTrackRecordId()
                                + " and versionId " + assignmentRecord.getFirmwareVersionRecordId());
            }
        }
        // make a copy so that we don't accidentally update caller's version by
        // reference
        FirmwareTrackAssignmentRecord copy = assignmentRecord.clone();
        copy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated FirmwareTrackAssignmentRecord {}", copy);

        return copy;
    }

    public FirmwareTrackAssignmentRecord getFirmwareTrackAssignmentOrNull(long firmwareTrackRecordId,
            long firmwareVersionRecordId) {
        FirmwareTrackAssignmentRecord ret = null;
        try {
            ret = this.jdbcTemplate.queryForObject(SQL_GET_BY_TRACK_ID_AND_VERSION_ID, rowMapper, firmwareTrackRecordId,
                    firmwareVersionRecordId);

            LOG.debug("Found FirmwareTrackAssignmentRecord {}", ret);

        } catch (EmptyResultDataAccessException e) {
            LOG.debug("FirmwareTrackAssignmentRecord not found for trackId {} and versionId {}", firmwareTrackRecordId,
                    firmwareVersionRecordId);
        }
        return ret;
    }

    public FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId,
            long firmwareVersionRecordId) {
        FirmwareTrackAssignmentRecord ret = getFirmwareTrackAssignmentOrNull(firmwareTrackRecordId,
                firmwareVersionRecordId);

        this.jdbcTemplate.update(SQL_DELETE, firmwareTrackRecordId, firmwareVersionRecordId);

        LOG.debug("Deleted FirmwareTrackAssignmentRecord {}", ret);

        return ret;
    }

    public List<FirmwareTrackAssignmentDetails> getAllFirmwareVersionsByTrackId(long trackId) {
        List<FirmwareTrackAssignmentDetails> results = this.jdbcTemplate.query(SQL_GET_FIRMWARE_VERSIONS_BY_TRACK_ID,
                firmwareVersionRowMapper, trackId);
        if (results == null || results.isEmpty()) {
            LOG.debug("No FirmwareVersions found for trackId {}", trackId);
        } else {
            LOG.debug("Found {} FirmwareVersions for trackId {}", results.size(), trackId);
        }
        return results;
    }

    public Map<String, FirmwareTrackAssignmentDetails> getAllDefaultFirmwareTrackAssignmentDetails(long trackId) {
        List<FirmwareTrackAssignmentDetails> defaultAssignments = this.jdbcTemplate
                .query(SQL_GET_DEFAULT_FIRMWARE_VERSIONS_BY_TRACK_ID, firmwareVersionRowMapper, trackId);
        Map<String, FirmwareTrackAssignmentDetails> results = new HashMap<>();
        if (defaultAssignments == null || defaultAssignments.isEmpty()) {
            LOG.debug("No FirmwareVersions found for trackId {}", trackId);
        } else {
            LOG.debug("Found {} FirmwareVersions for trackId {}", results.size(), trackId);
            for (FirmwareTrackAssignmentDetails entry : defaultAssignments) {
                results.put(entry.getModelId(), entry);
            }
        }
        return results;
    }

    public List<FirmwareTrackAssignmentRecord> getFirmwareTrackAssignmentsForTrackId(long trackId) {
        List<FirmwareTrackAssignmentRecord> results = this.jdbcTemplate.query(SQL_GET_BY_TRACK_ID, rowMapper, trackId);
        if (results == null || results.isEmpty()) {
            LOG.debug("No FirmwareTrackAssignmentRecords found for trackId {}", trackId);
        } else {
            LOG.debug("Found {} FirmwareTrackAssignmentRecords for trackId {}", results.size(), trackId);
        }
        return results;
    }

	public void deleteFirmwareTrackAssignments(long firmwareTrackRecordId) {
        this.jdbcTemplate.update(SQL_DELETE_FOR_TRACKID, firmwareTrackRecordId);

        LOG.debug("Deleted FirmwareTrackAssignmentRecords for {}", firmwareTrackRecordId);
		
	}

	public FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(
			long firmwareTrackRecordId, String equipmentModel) {

		if (equipmentModel == null || equipmentModel.length() == 0) {
            return null;
        }
        Map<String, FirmwareTrackAssignmentDetails> listOfVersions = getAllDefaultFirmwareTrackAssignmentDetails(
                firmwareTrackRecordId);

        return listOfVersions.get(equipmentModel);
        
	}
	
	public FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(String trackName,
			String equipmentModel) {
		
        FirmwareTrackRecord trackRecord = firmwareTrackDatastore.getFirmwareTrackByNameOrNull(trackName);
        if(trackRecord != null) {
            return getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(trackRecord.getRecordId(), equipmentModel);
        }

        return null;
	}
	
	public FirmwareTrackAssignmentDetails getFirmwareTrackAssignmentDetails(long firmwareTrackRecordId,
			long firmwareVersionRecordId) {
		
        FirmwareTrackAssignmentRecord assignmentRecord = getFirmwareTrackAssignmentOrNull(firmwareTrackRecordId, firmwareVersionRecordId);
        if(assignmentRecord != null) {
            FirmwareVersion version = firmwareVersionDatastore.get(firmwareVersionRecordId);
            if(version != null) {
                return new FirmwareTrackAssignmentDetails(assignmentRecord, version);
            }
        }
        return null;
	}
	
	public List<FirmwareTrackAssignmentDetails> getFirmwareTrackDetails(String firmwareTrackName) {
        FirmwareTrackRecord trackRecord = firmwareTrackDatastore.getFirmwareTrackByName(firmwareTrackName);
        List<FirmwareTrackAssignmentDetails> versions = getAllFirmwareVersionsByTrackId(trackRecord.getRecordId());
        return versions;
	}
	
}
