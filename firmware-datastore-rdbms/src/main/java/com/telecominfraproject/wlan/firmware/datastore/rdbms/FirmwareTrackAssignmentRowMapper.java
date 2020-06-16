package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;

/**
 * @author ekeddy
 *
 */
public class FirmwareTrackAssignmentRowMapper implements RowMapper<FirmwareTrackAssignmentRecord> {

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public FirmwareTrackAssignmentRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        FirmwareTrackAssignmentRecord ret = new FirmwareTrackAssignmentRecord();
        int colIdx=1;
        ret.setTrackRecordId(rs.getLong(colIdx++));
        ret.setFirmwareVersionRecordId(rs.getLong(colIdx++));
        ret.setDefaultRevisionForTrack(rs.getBoolean(colIdx++));
        ret.setDeprecated(rs.getBoolean(colIdx++));
        ret.setCreatedTimestamp(rs.getLong(colIdx++));
        ret.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return ret;    }

}
