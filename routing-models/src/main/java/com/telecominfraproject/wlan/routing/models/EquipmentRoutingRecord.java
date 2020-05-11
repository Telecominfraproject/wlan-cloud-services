package com.telecominfraproject.wlan.routing.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 *
 */
public class EquipmentRoutingRecord extends BaseJsonModel  implements HasCustomerId {
    
    private static final long serialVersionUID = 1726739755068718320L;

    private long id;

    /** Unique identifier for a hardware device. */
    private long equipmentId;
    
    /** Unique identifier of a customer. */
    private int customerId;
    
    /** Unique identifier for a EquipmentGatewayRecord id. */
    private long gatewayId;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public long getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(long gatewayId) {
        this.gatewayId = gatewayId;
    }

    @Override
    public EquipmentRoutingRecord clone() {
        return (EquipmentRoutingRecord) super.clone();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
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
        if (!(obj instanceof EquipmentRoutingRecord)) {
            return false;
        }
        EquipmentRoutingRecord other = (EquipmentRoutingRecord) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
    
}
