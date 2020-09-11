package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
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

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author ekeddy
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class FirmwareVersionDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareDatastoreRdbms.class);

    private static final String COL_ID = "id";

    private static final String[] GENERATED_KEY_COLS = { COL_ID };

    private static final String[] ALL_COLUMNS_LIST = { COL_ID,

            // add colums from properties FirmwareVersion in here
            "equipmentType", "modelId", "versionName", "commitTag", "description", "filename", "validationMethod",
            "validationCode", "releaseDate",

            // make sure the order of properties matches this list and list in
            // FirmwareVersionRowMapper and list in create/update methods

            "createdTimestamp", "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));

    static final String TABLE_NAME = "firmware_version";
    private static final String TABLE_PREFIX = "s.";
    private static final String ALL_COLUMNS;

    static final String ALL_COLUMNS_WITH_PREFIX;

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
            + " where versionName = ?";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from " + TABLE_NAME + " "
            + " where id = ?";

    private static final String SQL_INSERT = "insert into " + TABLE_NAME + " ( " + ALL_COLUMNS_FOR_INSERT
            + " ) values ( " + BIND_VARS_FOR_INSERT + " ) ";

    private static final String SQL_DELETE = "delete from " + TABLE_NAME + " where id = ? ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update " + TABLE_NAME + " set " + ALL_COLUMNS_UPDATE + " where id = ? "
            + " and ( lastModifiedTimestamp = ? or ? = true) ";

    private static final String SQL_GET_ALL = "select " + ALL_COLUMNS + " from " + TABLE_NAME;
    
    private static final String SQL_GET_BY_EQUIPMENT_TYPE = SQL_GET_ALL + " where equipmentType = ? order by createdTimestamp desc";

    private static final String SQL_GET_BY_EQUIPMENT_TYPE_AND_MODEL = SQL_GET_ALL + " where equipmentType = ?  and modelId = ? order by createdTimestamp desc";

    private static final String SQL_GET_MODEL_IDS_BY_EQUIPMENT_TYPE = "select distinct(modelId) from " + TABLE_NAME + " where equipmentType = ? ";

    
    private static final RowMapper<FirmwareVersion> firmwareVersionRowMapper = new FirmwareVersionRowMapper();

    @Autowired(required = false)
    public void setDataSource(FirmwareDataSourceInterface dataSource) {
        setDataSource((DataSource) dataSource);
    }

    public FirmwareVersion create(final FirmwareVersion firmwareVersion) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                            keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS));
                    int colIdx = 1;

                    // add properties from FirmwareVersion here
                    ps.setLong(colIdx++, firmwareVersion.getEquipmentType().getId());
                    ps.setString(colIdx++, firmwareVersion.getModelId());
                    ps.setString(colIdx++, firmwareVersion.getVersionName());
                    ps.setString(colIdx++, firmwareVersion.getCommit());
                    ps.setString(colIdx++, firmwareVersion.getDescription());
                    ps.setString(colIdx++, firmwareVersion.getFilename());
                    ps.setLong(colIdx++, firmwareVersion.getValidationMethod().getId());
                    ps.setString(colIdx++, firmwareVersion.getValidationCode());
                    ps.setLong(colIdx++, firmwareVersion.getReleaseDate());

                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        // keyHolder.getKey() now contains the generated key
        firmwareVersion.setId(((Long) keyHolder.getKeys().get(COL_ID)));
        firmwareVersion.setCreatedTimestamp(ts);
        firmwareVersion.setLastModifiedTimestamp(ts);

        LOG.debug("Stored FirmwareVersion {}", firmwareVersion);

        return firmwareVersion.clone();
    }

    public FirmwareVersion get(long firmwareVersionId) {
        LOG.debug("Looking up FirmwareVersion for id {}", firmwareVersionId);

        try {
            FirmwareVersion firmwareVersion = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID, firmwareVersionRowMapper,
                    firmwareVersionId);

            LOG.debug("Found FirmwareVersion {}", firmwareVersion);

            return firmwareVersion;
        } catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    public FirmwareVersion getByName(String versionName) {
        FirmwareVersion ret = getByNameOrNull(versionName);
        if (ret == null) {
            throw new DsEntityNotFoundException("FirmwareVersion not found " + versionName);
        }
        return ret;
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public FirmwareVersion getByNameOrNull(String versionName) {
        LOG.debug("Looking up FirmwareVersion for name {}", versionName);

        try {
            FirmwareVersion firmwareVersion = this.jdbcTemplate.queryForObject(SQL_GET_BY_NAME,
                    firmwareVersionRowMapper, versionName);

            LOG.debug("Found FirmwareVersion {}", firmwareVersion);

            return firmwareVersion;
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("Cannot find FirmwareVersion {}", versionName);
            return null;
        }
    }

    public FirmwareVersion update(FirmwareVersion firmwareVersion) {

        long incomingLastModifiedTs = firmwareVersion.getLastModifiedTimestamp();
        long newLastModifiedTs = getNewLastModTs(incomingLastModifiedTs);

        int updateCount = this.jdbcTemplate.update(SQL_UPDATE,
                // firmwareVersion.getId(), - not updating this one

                // add properties from FirmwareVersion here
                firmwareVersion.getEquipmentType().getId(), firmwareVersion.getModelId(),
                firmwareVersion.getVersionName(), firmwareVersion.getCommit(), firmwareVersion.getDescription(),
                firmwareVersion.getFilename(), firmwareVersion.getValidationMethod().getId(),
                firmwareVersion.getValidationCode(), firmwareVersion.getReleaseDate(),

                // firmwareVersion.getCreatedTimestamp(), - not updating this
                // one
                newLastModifiedTs,

                // use id for update operation
                firmwareVersion.getId(),
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
                        firmwareVersion.getId());

                LOG.debug(
                        "Concurrent modification detected for FirmwareVersion with id {} expected version is {} but version in db was {}",
                        firmwareVersion.getId(), incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for FirmwareVersion with id " + firmwareVersion.getId()
                                + " expected version is " + incomingLastModifiedTs + " but version in db was "
                                + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find FirmwareVersion for {}", firmwareVersion.getId());
                throw new DsEntityNotFoundException("FirmwareVersion not found " + firmwareVersion.getId());
            }
        }

        // make a copy so that we don't accidentally update caller's version by
        // reference
        FirmwareVersion firmwareVersionCopy = firmwareVersion.clone();
        firmwareVersionCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated FirmwareVersion {}", firmwareVersionCopy);

        return firmwareVersionCopy;
    }

    public FirmwareVersion delete(long firmwareVersionId) {
        FirmwareVersion ret = get(firmwareVersionId);
        this.jdbcTemplate.update(SQL_DELETE, firmwareVersionId);
        LOG.debug("Deleted FirmwareVersion {}", ret);
        return ret;
    }

    public Map<EquipmentType, List<FirmwareVersion>> getAllGroupedByEquipmentType() {
        List<FirmwareVersion> results = this.jdbcTemplate.query(SQL_GET_ALL, firmwareVersionRowMapper);
        Map<EquipmentType, List<FirmwareVersion>> resultMap = new HashMap<>();
        for (EquipmentType et : EquipmentType.values()) {
            resultMap.put(et, new ArrayList<>());
        }
        
        for (FirmwareVersion v : results) {
            resultMap.get(v.getEquipmentType()).add(v);
        }
        
        LOG.debug("Found {} FirmwareVersions", results.size());
        return resultMap;
    }

	public List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType, String modelId) {
		List<FirmwareVersion> results;
		if(modelId == null) {
			results = this.jdbcTemplate.query(SQL_GET_BY_EQUIPMENT_TYPE, firmwareVersionRowMapper, equipmentType.getId());
		} else {
			results = this.jdbcTemplate.query(SQL_GET_BY_EQUIPMENT_TYPE_AND_MODEL, firmwareVersionRowMapper, equipmentType.getId(), modelId);
		}
        
        LOG.debug("Found {} FirmwareVersions", results.size());
        return results;
	}

	public List<String> getAllFirmwareModelIdsByEquipmentType(EquipmentType equipmentType) {
		List<String> results = this.jdbcTemplate.queryForList(SQL_GET_MODEL_IDS_BY_EQUIPMENT_TYPE, String.class, equipmentType.getId());
        
        LOG.debug("Found {} model ids for equipment {}", results.size(), equipmentType);
        return results;
	}

}
