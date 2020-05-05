package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * Information for a network and equipment deployment
 * 
 * @author yongli
 *
 */
public abstract class DeploymentInformation extends BaseJsonModel implements DeploymentValue {
    private static final long serialVersionUID = 4788632432821036957L;
    private long id;
    private int customerId;
    private final NetworkDeploymentType deploymentType;
    private Map<String, DeploymentSsidInfo> ssidMap;
    private Map<Long, DeploymentEquipmentInfo> equipmentMap;
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    private Boolean equipmentDiscovery;

    protected DeploymentInformation(int customerId, NetworkDeploymentType deploymentType) {
        this.customerId = customerId;
        this.deploymentType = deploymentType;
    }

    /**
     * Add an equipment information
     * 
     * @param equipmentInfo
     */
    public void addEquipmentInfo(final DeploymentEquipmentInfo equipmentInfo) {
        if (null == this.equipmentMap) {
            this.equipmentMap = new HashMap<>();
        }
        this.equipmentMap.put(equipmentInfo.getEquipmentId(), equipmentInfo);
    }

    /**
     * add a ssid information
     * 
     * @param ssidInfo
     */
    public void addSsidInfo(final DeploymentSsidInfo ssidInfo) {
        if (null == this.ssidMap) {
            ssidMap = new HashMap<>();
        }
        this.ssidMap.put(ssidInfo.getSsidName(), ssidInfo);
    }

    @Override
    public DeploymentInformation clone() {
        DeploymentInformation result = (DeploymentInformation) super.clone();
        if (null != result.ssidMap) {
            result.ssidMap = null;
            for (DeploymentSsidInfo entry : this.ssidMap.values()) {
                result.addSsidInfo(entry.clone());
            }
        }
        if (null != result.equipmentMap) {
            result.equipmentMap = null;
            for (DeploymentEquipmentInfo entry : this.equipmentMap.values()) {
                result.addEquipmentInfo(entry);
            }
        }
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
        if (!(obj instanceof DeploymentInformation)) {
            return false;
        }
        DeploymentInformation other = (DeploymentInformation) obj;
        if (this.createdTimestamp != other.createdTimestamp) {
            return false;
        }
        if (this.customerId != other.customerId) {
            return false;
        }
        if (this.deploymentType != other.deploymentType) {
            return false;
        }
        if (this.equipmentMap == null) {
            if (other.equipmentMap != null) {
                return false;
            }
        } else if (!this.equipmentMap.equals(other.equipmentMap)) {
            return false;
        }
        if (this.equipmentDiscovery == null) {
            if (other.equipmentDiscovery != null) {
                return false;
            }
        } else if (!this.equipmentDiscovery.equals(other.equipmentDiscovery)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        if (this.lastModifiedTimestamp != other.lastModifiedTimestamp) {
            return false;
        }
        if (this.ssidMap == null) {
            if (other.ssidMap != null) {
                return false;
            }
        } else if (!this.ssidMap.equals(other.ssidMap)) {
            return false;
        }
        return true;
    }

    /**
     * Find an equipment information
     * 
     * @param equipmentId
     * @return
     */
    public DeploymentEquipmentInfo findEquipmentInfo(long equipmentId) {
        if (null != this.equipmentMap) {
            return this.equipmentMap.get(equipmentId);
        }
        return null;
    }

    /**
     * find a ssid information by name
     * 
     * @param ssidName
     * @return
     */
    public DeploymentSsidInfo findSsidInfo(String ssidName) {
        if (null != this.ssidMap) {
            return ssidMap.get(ssidName);
        }
        return null;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public int getCustomerId() {
        return customerId;
    }

    public NetworkDeploymentType getDeploymentType() {
        return deploymentType;
    }

    /**
     * Use for JSON serialization only
     * 
     * @return
     */
    public Collection<DeploymentEquipmentInfo> getEquipment() {
        if (null == this.equipmentMap) {
            return null;
        }
        return equipmentMap.values();
    }

    @JsonIgnore
    public Map<Long, DeploymentEquipmentInfo> getEquipmentMap() {
        return equipmentMap;
    }

    public Boolean getEquipmentDiscovery() {
        return equipmentDiscovery;
    }

    public long getId() {
        return id;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    @JsonIgnore
    public Map<String, DeploymentSsidInfo> getSsidMap() {
        return ssidMap;
    }

    /**
     * Use for JSON serialization only
     * 
     * @return
     */
    public Collection<DeploymentSsidInfo> getSsids() {
        if (null == this.ssidMap) {
            return null;
        }
        return this.ssidMap.values();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.createdTimestamp ^ (this.createdTimestamp >>> 32));
        result = prime * result + this.customerId;
        result = prime * result + ((this.deploymentType == null) ? 0 : this.deploymentType.hashCode());
        result = prime * result + ((this.equipmentMap == null) ? 0 : this.equipmentMap.hashCode());
        result = prime * result + ((this.equipmentDiscovery == null) ? 0 : this.equipmentDiscovery.hashCode());
        result = prime * result + (int) (this.id ^ (this.id >>> 32));
        result = prime * result + (int) (this.lastModifiedTimestamp ^ (this.lastModifiedTimestamp >>> 32));
        result = prime * result + ((this.ssidMap == null) ? 0 : this.ssidMap.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (NetworkDeploymentType.isUnsupported(this.deploymentType)) {
            return true;
        }
        if (BaseJsonModel.hasUnsupportedValue(this.ssidMap)) {
            return true;
        }
        if (BaseJsonModel.hasUnsupportedValue(this.equipmentMap)) {
            return true;
        }
        return false;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Use for JSON serialization only
     * 
     * @param equipment
     */
    public void setEquipment(Collection<DeploymentEquipmentInfo> equipment) {
        if (null == equipment) {
            this.equipmentMap = null;
        } else {
            this.equipmentMap = new HashMap<>();
            for (DeploymentEquipmentInfo eq : equipment) {
                if (null != this.equipmentMap.put(eq.getEquipmentId(), eq)) {
                    throw new DsDataValidationException("duplicated equipment with id " + eq.getEquipmentId());
                }
            }
        }
    }

    @JsonIgnore
    public void setEquipmentMap(Map<Long, DeploymentEquipmentInfo> equipment) {
        this.equipmentMap = equipment;
    }

    public void setEquipmentDiscovery(Boolean gatewayDiscovery) {
        this.equipmentDiscovery = gatewayDiscovery;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    @JsonIgnore
    public void setSsidMap(Map<String, DeploymentSsidInfo> ssids) {
        this.ssidMap = ssids;
    }

    /**
     * Use for JSON serialization only
     * 
     * @param ssids
     */
    public void setSsids(Collection<DeploymentSsidInfo> ssids) {
        if (null == ssids) {
            this.ssidMap = null;
        } else {
            this.ssidMap = new HashMap<>();
            for (DeploymentSsidInfo ssid : ssids) {
                if (null == ssid.getSsidName()) {
                    throw new DsDataValidationException("ssid name is null");
                }
                if (null != this.ssidMap.put(ssid.getSsidName(), ssid)) {
                    throw new DsDataValidationException("mutliple ssid with same name " + ssid.getSsidName());
                }
            }
        }
    }

    @Override
    public void validateValue() {
        if (0 >= customerId) {
            throw new DsDataValidationException("invalid customerId");
        }
        if (hasUnsupportedValue()) {
            throw new DsDataValidationException("unsupported value");
        }
        if (null == this.ssidMap || this.ssidMap.isEmpty()) {
            throw new DsDataValidationException("missing ssids");
        }
        if (null == this.equipmentMap || this.equipmentMap.isEmpty()) {
            throw new DsDataValidationException("missing equipment");
        }
    }
}
