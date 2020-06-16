package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.scheduler.ScheduleSetting;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;

public class FirmwareTrackRecordRowMapper implements RowMapper<FirmwareTrackRecord> {

    @Override
    public FirmwareTrackRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        FirmwareTrackRecord ret = new FirmwareTrackRecord();
        int colIdx=1;
        ret.setRecordId(rs.getLong(colIdx++));
        ret.setTrackName(rs.getString(colIdx++));
        
        String jsonString = rs.getString(colIdx++);
        if (null != jsonString) {
            ret.setMaintenanceWindow(BaseJsonModel.fromString(jsonString, ScheduleSetting.class));
        }
        ret.setCreatedTimestamp(rs.getLong(colIdx++));
        ret.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return ret;
    }

}
