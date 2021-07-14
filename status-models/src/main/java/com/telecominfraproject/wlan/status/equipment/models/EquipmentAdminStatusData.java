
package com.telecominfraproject.wlan.status.equipment.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private Map<String, Long> alarmTimestamps;

    private LedStatus ledStatus;

    public EquipmentAdminStatusData() {

    }

    @Override
    public StatusDataType getStatusDataType() {
        return StatusDataType.EQUIPMENT_ADMIN;
    }

    public EquipmentAdminStatusData(EquipmentAdminStatusData data) {
        this.statusCode = data.statusCode;
        this.statusMessage = data.statusMessage;
        this.alarmTimestamps = data.alarmTimestamps == null ? null : new HashMap<>(data.alarmTimestamps);
    }

    @Override
    public EquipmentAdminStatusData clone() {
        EquipmentAdminStatusData res = (EquipmentAdminStatusData) super.clone();
        if (this.alarmTimestamps != null) {
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
        return alarmTimestamps == null ? 0 : alarmTimestamps.getOrDefault(alarmKey, 0l);
    }

    public void putAlarmTimestamp(String alarmKey, long value) {
        if (alarmTimestamps == null) {
            alarmTimestamps = new HashMap<>();
        }
        alarmTimestamps.put(alarmKey, value);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StatusCode.isUnsupported(statusCode)) {
            return true;
        }
        return false;
    }

    public LedStatus getLedStatus() {
        return ledStatus;
    }

    public void setLedStatus(LedStatus ledStatus) {
        this.ledStatus = ledStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alarmTimestamps, ledStatus, statusCode, statusMessage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EquipmentAdminStatusData other = (EquipmentAdminStatusData) obj;
        return Objects.equals(alarmTimestamps, other.alarmTimestamps) && ledStatus == other.ledStatus && statusCode == other.statusCode
                && Objects.equals(statusMessage, other.statusMessage);
    }
    
    
}
