package com.telecominfraproject.wlan.manufacturer.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author mpreston
 * 
 */
public class ManufacturerDetailsRecord extends BaseJsonModel {

    private static final long serialVersionUID = -1094384189381503822L;
    
    private long id;
    private String manufacturerName;
    private String manufacturerAlias;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
    public ManufacturerDetailsRecord() {}
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerAlias() {
        return manufacturerAlias;
    }

    public void setManufacturerAlias(String manufacturerAlias) {
        this.manufacturerAlias = manufacturerAlias;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((manufacturerAlias == null) ? 0 : manufacturerAlias.hashCode());
        result = prime * result + ((manufacturerName == null) ? 0 : manufacturerName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManufacturerDetailsRecord other = (ManufacturerDetailsRecord) obj;
        if (manufacturerAlias == null) {
            if (other.manufacturerAlias != null)
                return false;
        } else if (!manufacturerAlias.equals(other.manufacturerAlias))
            return false;
        if (manufacturerName == null) {
            if (other.manufacturerName != null)
                return false;
        } else if (!manufacturerName.equals(other.manufacturerName))
            return false;
        return true;
    }

    @Override
    public ManufacturerDetailsRecord clone() {
        ManufacturerDetailsRecord ret = (ManufacturerDetailsRecord) super.clone();
        return ret;
    }
}
