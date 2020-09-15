package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class StreamingVideoServerRecord extends BaseJsonModel {

    /**
     * 
     */
    private static final long serialVersionUID = 6467756215663743578L;
    private long id;
    private int customerId;
    private long equipmentId; // originating equipment

    private InetAddress ipAddr;
    private StreamingVideoType type;

    private long createdTimestamp;
    private long lastModifiedTimestamp;

    public StreamingVideoServerRecord() {
        // serial
    }

    public StreamingVideoServerRecord(InetAddress ipAddr, StreamingVideoType type) {
        this.ipAddr = ipAddr;
        this.type = type;
    }

    public InetAddress getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }

    public StreamingVideoType getType() {
        return type;
    }

    public void setType(StreamingVideoType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue() || ((type != null) && StreamingVideoType.isUnsupported(type))) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + customerId;
        result = (prime * result) + (int) (equipmentId ^ (equipmentId >>> 32));
        result = (prime * result) + ((ipAddr == null) ? 0 : ipAddr.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        StreamingVideoServerRecord other = (StreamingVideoServerRecord) obj;
        if (customerId != other.customerId) {
            return false;
        }
        if (equipmentId != other.equipmentId) {
            return false;
        }
        if (ipAddr == null) {
            if (other.ipAddr != null) {
                return false;
            }
        } else if (!ipAddr.equals(other.ipAddr)) {
            return false;
        }
        return true;
    }

}
