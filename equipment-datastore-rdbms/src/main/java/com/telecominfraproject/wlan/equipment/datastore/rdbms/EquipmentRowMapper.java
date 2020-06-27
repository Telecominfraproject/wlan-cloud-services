package com.telecominfraproject.wlan.equipment.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;

/**
 * @author dtoptygin
 *
 */

public class EquipmentRowMapper implements RowMapper<Equipment> {
    
    private static final Logger LOG = LoggerFactory.getLogger(EquipmentRowMapper.class);

    public Equipment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Equipment equipment = new Equipment();
        int colIdx=1;
        equipment.setId(rs.getLong(colIdx++));

        //add columns from properties Equipment in here. 
        //make sure order of fields is the same as defined in Equipment
        equipment.setCustomerId(rs.getInt(colIdx++));
        equipment.setProfileId(rs.getLong(colIdx++));
        equipment.setLocationId(rs.getLong(colIdx++));
        equipment.setEquipmentType(EquipmentType.getById(rs.getInt(colIdx++)));
        equipment.setInventoryId(rs.getString(colIdx++));
        
        equipment.setName(rs.getString(colIdx++));
        
        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	EquipmentDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, EquipmentDetails.class);
                equipment.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode EquipmentDetails from database for id = {}", equipment.getId());
            }
        }

        equipment.setLatitude(rs.getString(colIdx++));
        equipment.setLongitude(rs.getString(colIdx++));
        
        String macStr = rs.getString(colIdx++);        
        if(macStr!=null && !macStr.isEmpty()) {
	        try {
	            equipment.setBaseMacAddress(MacAddress.valueOf(macStr));
	        } catch (RuntimeException exp) {
	            LOG.warn("Failed to decode baseMacAddress {} from database for id = {}", macStr, equipment.getId());            	
	        }
        }
        //skipping the next column - manufacturerOui - as it is derived from base mac. we only store that column to allow counting equipment by OUIs.
        colIdx++;
        
        equipment.setSerial(rs.getString(colIdx++));
        
        equipment.setCreatedTimestamp(rs.getLong(colIdx++));
        equipment.setLastModifiedTimestamp(rs.getLong(colIdx++));
        
        return equipment;
    }
}