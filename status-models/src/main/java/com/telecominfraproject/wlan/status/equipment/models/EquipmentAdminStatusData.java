package com.telecominfraproject.wlan.status.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * Administrative Status for Customer Equipment
 * 
 * @author yongli
 *
 */
public class EquipmentAdminStatusData extends StatusDetails {
    /**
     * Serial number
     */
    private static final long serialVersionUID = 5282800813124689810L;

    /**
     * Status Code for the equipment
     */
    private StatusCode statusCode;

    /**
     * Human readable message for the status
     */
    private String statusMessage;


    private Map<String,Long> alarmTimestamps;
    
    public EquipmentAdminStatusData() {

    }

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.EQUIPMENT_ADMIN;
    }
    
    public EquipmentAdminStatusData(EquipmentAdminStatusData data) {
        this.statusCode = data.statusCode;
        this.statusMessage = data.statusMessage;
        this.alarmTimestamps = data.alarmTimestamps==null?null:new HashMap<>(data.alarmTimestamps);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alarmTimestamps == null) ? 0 : alarmTimestamps.hashCode());
        result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
        result = prime * result + ((statusMessage == null) ? 0 : statusMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EquipmentAdminStatusData)) {
            return false;
        }
        EquipmentAdminStatusData other = (EquipmentAdminStatusData) obj;
        if (alarmTimestamps == null) {
            if (other.alarmTimestamps != null) {
                return false;
            }
        } else if (!alarmTimestamps.equals(other.alarmTimestamps)) {
            return false;
        }
        if (statusCode != other.statusCode) {
            return false;
        }
        if (statusMessage == null) {
            if (other.statusMessage != null) {
                return false;
            }
        } else if (!statusMessage.equals(other.statusMessage)) {
            return false;
        }
        return true;
    }

    @Override
    public EquipmentAdminStatusData clone() {
        EquipmentAdminStatusData res = (EquipmentAdminStatusData) super.clone();
        if(this.alarmTimestamps != null) {
            res.setAlarmTimestamps(new HashMap<>(this.alarmTimestamps));
        }
        return res;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, Long> getAlarmTimestamps() {
        return alarmTimestamps;
    }

    public void setAlarmTimestamps(Map<String, Long> alarmTimestamps) {
        this.alarmTimestamps = alarmTimestamps;
    }
    
    public long findAlarmTimeOrZero(String alarmKey) {
        return alarmTimestamps==null?0:alarmTimestamps.getOrDefault(alarmKey, 0l);
    }

    public void putAlarmTimestamp(String alarmKey, long value) {
        if(alarmTimestamps == null) {
            alarmTimestamps = new HashMap<>();
        }
        alarmTimestamps.put(alarmKey, value);
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StatusCode.isUnsupported(statusCode) ) {
            return true;
        }
        return false;
    }
}
