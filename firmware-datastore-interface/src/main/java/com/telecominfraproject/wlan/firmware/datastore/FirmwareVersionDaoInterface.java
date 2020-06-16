package com.telecominfraproject.wlan.firmware.datastore;

import java.util.List;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author ekeddy
 * @author dtoptygin
 *
 */
public interface FirmwareVersionDaoInterface {

    /**
     * Creates a firmware version entry in the DB.
     * @param firmwareVersion
     * @return
     */
    FirmwareVersion create(FirmwareVersion firmwareVersion);
    
    /**
     * Retrieves a firmware version entry from the DB using the primary key.
     * @param firmwareVersionId
     * @return
     */
    FirmwareVersion get(long firmwareVersionId);
    
    /**
     * Update a firmware version entry.
     * @param firmwareVersion
     * @return
     */
    FirmwareVersion update(FirmwareVersion firmwareVersion);
    
    /**
     * Delete a firmware version entry specified by the primary key.
     * @param firmwareVersionId
     * @return
     */
    FirmwareVersion delete(long firmwareVersionId);
    
    /**
     * Retrieve all firmware revisions and group the results by equipment type.
     * @return
     */
	Map<EquipmentType, List<FirmwareVersion>> getAllGroupedByEquipmentType();
	
	/**
	 * Retrieve the firmware version entry by name.
	 * @param targetSwVersion
	 * @return
	 */
	FirmwareVersion getByName(String targetSwVersion);
	
	
    /**
     * Retrieve the firmware version entry by name.
     * Does not throw exception if entry not found.
     * @param targetSwVersion
     * @return
     */
	FirmwareVersion getByNameOrNull(String targetSwVersion);
	
}
