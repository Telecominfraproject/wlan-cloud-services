package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.util.CollectionUtils;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ManufacturerOuiDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerOuiDAO.class);

    private static final String COL_ID = "oui";
    
    private static final int MAX_RESULTS = 400;
    
    private static final String[] ALL_COLUMNS_LIST = {
            // string
            COL_ID,
            
            // add columns from properties ClientOuiDetails in here
            "manufacturerDetails"
            
    };

    private static final Set<String> columnsToSkipForInsert = Collections.emptySet();
    
    private static final String TABLE_NAME = "manufacturer_oui";
    private static final String MANUFACTURER_DETAILS_TABLE_NAME = "manufacturer_details";
    private static final String ALL_COLUMNS; 
    private static final String ALL_COLUMNS_FOR_INSERT; 
    private static final String BIND_VARS_FOR_INSERT;
    
    static{
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        for(String colName: ALL_COLUMNS_LIST){
            
            strbAllColumns.append(colName).append(",");            
            
            if(!columnsToSkipForInsert.contains(colName)){
                strbAllColumnsForInsert.append(colName).append(",");
                strbBindVarsForInsert.append("?,");
            }
        }
        
        //remove trailing ','
        strbAllColumns.deleteCharAt(strbAllColumns.length()-1);
        strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length()-1);
        strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length()-1);
        
        ALL_COLUMNS = strbAllColumns.toString();
        ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
        BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
        
    }
    
    private static final String SQL_INSERT =
            "insert into "+TABLE_NAME+" ( " 
            + ALL_COLUMNS_FOR_INSERT
            + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";
    
    private static final String SQL_DELETE =
            "delete from "+TABLE_NAME+" where oui = ? ";
    
    private static final String SQL_GET_BY_OUI =
            "select oui, manufacturerName, manufacturerAlias" + 
            " from "+TABLE_NAME+", " +MANUFACTURER_DETAILS_TABLE_NAME+
            " where " + COL_ID + " = ? and manufacturerDetails = id";
    
    private static final String SQL_GET_OUD_LIST_BY_MANUFACTURER =
            "select " + COL_ID +
            " from "+TABLE_NAME+" " +
            " where manufacturerDetails in " +
            "(select id from " + MANUFACTURER_DETAILS_TABLE_NAME+
            " where manufacturerName like ? or manufacturerAlias like ?)" +
            " limit " + MAX_RESULTS;
    
    private static final String SQL_GET_ALL_MANUFACTURER_DATA = 
            "select oui, manufacturerName, manufacturerAlias" + 
            " from "+TABLE_NAME+", " +MANUFACTURER_DETAILS_TABLE_NAME+
            " where manufacturerDetails = id";
    
    private static final String SQL_GET_MANUFACTURER_DATA_FOR_OUI_LIST = 
            "select oui, manufacturerName, manufacturerAlias" + 
            " from "+TABLE_NAME+", " +MANUFACTURER_DETAILS_TABLE_NAME+
            " where oui in (%s) and manufacturerDetails = id";
    
    private static final String SQL_GET_DETAILS_ID =
            "select manufacturerDetails " + 
            " from "+TABLE_NAME+" " +
            " where oui = ?";
    
    private static final RowMapper<ManufacturerOuiDetails> ManufacturerOuiRowMapper = new ManufacturerOuiRowMapper();

    @Transactional(noRollbackFor={DuplicateKeyException.class})
    public ManufacturerOuiDetails create(final ManufacturerOuiDetails ouiDetails, final long manufacturerDetailsId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT);
                    int colIdx = 1;

                    ps.setString(colIdx++, ouiDetails.getOui().toLowerCase());
                    ps.setLong(colIdx++, manufacturerDetailsId);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            // Don't worry about it, there is already a record:
            LOG.debug("Already stored entry for OUI {}", ouiDetails);
            throw(e);
        }

        LOG.debug("Stored ManufacturerOuiDetails {}", ouiDetails);

        return ouiDetails.clone();
    }

    @Transactional(noRollbackFor={EmptyResultDataAccessException.class})
    public List<String> getOuiListForManufacturer(String  manufacturer, boolean exactMatch) {
        LOG.debug("Looking up OUI values for {}", manufacturer);

        try {
            String searchString = (exactMatch) ? manufacturer : (manufacturer + "%");
            List<String> resultList = this.jdbcTemplate.queryForList(SQL_GET_OUD_LIST_BY_MANUFACTURER, String.class,
                    searchString, searchString);

            LOG.debug("Found {} for {}", resultList, manufacturer);
            
            return resultList;
        }catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    @Transactional(noRollbackFor={EmptyResultDataAccessException.class})
    public ManufacturerOuiDetails getByOui(String oui) {
        LOG.debug("Looking up ManufacturerOuiDetails record with oui {}", oui);

        try {
            ManufacturerOuiDetails clientOuiDetails = this.jdbcTemplate.queryForObject(SQL_GET_BY_OUI,
                    ManufacturerOuiRowMapper, oui.toLowerCase());

            LOG.debug("Found ManufacturerOuiDetails {}", clientOuiDetails);
            return clientOuiDetails;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("No Manufacturer Details currently stored for oui {}", oui);
            return null;
        }
    }

    @Transactional(noRollbackFor={EmptyResultDataAccessException.class})
    public ManufacturerOuiDetails delete(String oui) {
        ManufacturerOuiDetails ret = null;
        try {
            ret = getByOui(oui);

            this.jdbcTemplate.update(SQL_DELETE, ret.getOui());

            LOG.debug("Deleted ManufacturerOuiDetails {}", ret);

            return ret;
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("Cannot find ManufacturerOuiDetails for {} to delete", oui);
            throw new DsEntityNotFoundException("ManufacturerOuiDetails not found " + oui);
        }
    }

    public List<ManufacturerOuiDetails> getAllManufacturerData() {
        LOG.debug("Retrieving all Manufacturer data ");

        try {
            List<ManufacturerOuiDetails> resultList = this.jdbcTemplate.query(SQL_GET_ALL_MANUFACTURER_DATA,
                    ManufacturerOuiRowMapper);
            LOG.debug("Resulting Manufacturer data has {} entries", resultList.size());
            return resultList;
        } catch (Exception e) {
            LOG.debug("Error retrieving all Manufacturer data: {}", e);
            return Collections.emptyList();
        }
    }

    public Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiList(List<String> ouiList) {
        Map<String, ManufacturerOuiDetails> result = new HashMap<>();
        
        if (CollectionUtils.isEmpty(ouiList)) {
            return result;
        }
        
        StringBuilder ouiListSB = new StringBuilder();
        for (String oui : ouiList) {
            ouiListSB.append("'");
            ouiListSB.append(oui.toLowerCase());
            ouiListSB.append("'");
            ouiListSB.append(",");
        }
        ouiListSB.setLength(ouiListSB.length()-1);
        
        
        List<ManufacturerOuiDetails> resultList = new ArrayList<>();
        try {
            // Create our own SQL prepared statement as variable number of
            // arguments causes weird behaviour.
            resultList = this.jdbcTemplate.query(
                    String.format(SQL_GET_MANUFACTURER_DATA_FOR_OUI_LIST, ouiListSB.toString()),
                    ManufacturerOuiRowMapper);

            LOG.debug("Resulting Manufacturer data has {} entries", resultList.size());
            for (ManufacturerOuiDetails details : resultList) {
                result.put(details.getOui(), details);
            }

            return result;
        }catch (Exception  e) {
            LOG.debug("Error retrieving Manufacturer data for OUI list: {}" , e);
            return result;
        }
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public long getManufacturerDetialsId(String oui) {
        LOG.debug("Looking up manufacturer details id for oui {}", oui);

        try {
            long mfrDetailsId = this.jdbcTemplate.queryForObject(SQL_GET_DETAILS_ID, Long.class, oui.toLowerCase());

            LOG.debug("Found details record id {} for oui {}", mfrDetailsId, oui);
            return mfrDetailsId;
        } catch (EmptyResultDataAccessException e) {
            LOG.debug("No Manufacturer Details record currently stored for oui {}", oui);
            return -1;
        }
    }
}
