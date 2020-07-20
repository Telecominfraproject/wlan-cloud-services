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
     * @param equipmentType - filter firmware versions by equipment type
     * @param modelId - optional filter by equipment model, if null - then firmware versions for all the equipment models are returned
     * @return list of firmware versions that satisfy the supplied filters
     */
    List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType, String modelId);

	/**
	 * @param equipmentType
	 * @return list of equipment models taken from the all known firmware versions 
	 */
	List<String> getAllFirmwareModelIdsByEquipmentType(EquipmentType equipmentType);
	
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
