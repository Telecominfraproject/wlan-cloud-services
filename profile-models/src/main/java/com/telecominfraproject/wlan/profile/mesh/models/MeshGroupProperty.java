package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author yongli
 *
 */
public class MeshGroupProperty extends BaseJsonModel implements PushableConfiguration<MeshGroupProperty> {
    /**
     * 
     */
    private static final long serialVersionUID = 1271944573077996284L;

    private static final int UNKNOWN_LOCATION = -1;

    private String name;

    private int locationId = UNKNOWN_LOCATION;

    /**
     * Enable Ethernet protected.
     * <ul>
     * <li>true - enabled
     * <li>false - disabled
     * <li>null - default
     * </ul>
     */
    private Boolean ethernetProtection;

    @Override
    public MeshGroupProperty clone() {
        return (MeshGroupProperty) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MeshGroupProperty)) {
            return false;
        }
        MeshGroupProperty other = (MeshGroupProperty) obj;
        if (this.ethernetProtection == null) {
            if (other.ethernetProtection != null) {
                return false;
            }
        } else if (!this.ethernetProtection.equals(other.ethernetProtection)) {
            return false;
        }
        if (this.locationId != other.locationId) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public Boolean getEthernetProtection() {
        return ethernetProtection;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.ethernetProtection == null) ? 0 : this.ethernetProtection.hashCode());
        result = prime * result + this.locationId;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    public void setEthernetProtection(Boolean ethernetProtection) {
        this.ethernetProtection = ethernetProtection;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Only Ethernet protection property requires configuration push on mesh
     * portal
     */
    @Override
    public boolean needsToBeUpdatedOnDevice(MeshGroupProperty previousVersion) {
        return !Objects.equals(ethernetProtection, previousVersion.getEthernetProtection());
    }

    public void validateData() {
        if ((null == this.name) || this.name.isEmpty()) {
            throw new DsDataValidationException("Missing name for the mesh group");
        }
        if (UNKNOWN_LOCATION == this.locationId) {
            throw new DsDataValidationException("Missing location for the mesh group");
        }
    }
}
