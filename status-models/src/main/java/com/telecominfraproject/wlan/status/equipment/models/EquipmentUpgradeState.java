package com.telecominfraproject.wlan.status.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;


/**
 * @author ekeddy
 *
 */
public enum EquipmentUpgradeState {
	
	undefined(0),
	
	// States of the upgrade
    download_initiated(1),
    downloading(2),
    download_failed(3),
    download_complete(4),  //firmware downloaded into reserve area but not flashed to bank
    
    apply_initiated(100),
    applying(101),
    apply_failed(102), 
    apply_complete(103),
    
    reboot_initiated(200),
    rebooting(201),
    out_of_date(202),
    up_to_date(203), 
    reboot_failed(204),
    
    UNSUPPORTED(-1);
    
    // Reasons for failure.
    public enum FailureReason {
    	downloadRequestRejected,
    	validationFailed,
    	unreachableUrl,
    	downloadFailed, 
    	applyRequestRejected, 
    	applyFailed, 
    	rebootRequestRejected, 
    	invalidVersion,
    	rebootWithWrongVersion, 
    	maxRetries,
    	rebootTimedout,
    	downloadRequestFailedFlashFull,
    	UNSUPPORTED;
    	
        @JsonCreator
        public static FailureReason getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, FailureReason.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(FailureReason value) {
            return UNSUPPORTED.equals(value);
        }
    }
    
    private final int id;
    private static final Map<Integer, EquipmentUpgradeState> ELEMENTS = new HashMap<>();

    private EquipmentUpgradeState(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static EquipmentUpgradeState getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(EquipmentUpgradeState met : EquipmentUpgradeState.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static EquipmentUpgradeState getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EquipmentUpgradeState.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(EquipmentUpgradeState value) {
        return UNSUPPORTED.equals(value);
    }

}
