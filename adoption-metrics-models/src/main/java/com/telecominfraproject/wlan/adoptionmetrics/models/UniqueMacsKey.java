package com.telecominfraproject.wlan.adoptionmetrics.models;

import java.util.Objects;

public class UniqueMacsKey {

    private long timestampMs;

    private int customerId;
    private long locationId;
    private long equipmentId;

    public UniqueMacsKey() {
    }

    public UniqueMacsKey( long timestampMs, int customerId, long locationId, long equipmentId ) {
        this.timestampMs = timestampMs;
        this.customerId = customerId;
        this.locationId = locationId;
        this.equipmentId = equipmentId;
    }

    public long getTimestampMs() {
        return timestampMs;
    }
    public void setTimestampMs(long timestampMs) {
        this.timestampMs = timestampMs;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public long getLocationId() {
        return locationId;
    }
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }
    public long getEquipmentId() {
        return equipmentId;
    }
    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, equipmentId, locationId, timestampMs);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UniqueMacsKey)) {
            return false;
        }
        UniqueMacsKey other = (UniqueMacsKey) obj;
        return customerId == other.customerId && equipmentId == other.equipmentId
                && locationId == other.locationId 
                && timestampMs == other.timestampMs;
    }

    
}
