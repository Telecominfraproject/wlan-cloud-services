package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author yongli
 *
 */
public class ApMeshPortalConfiguration extends ApMeshConfiguration {
    /**
     * 
     */
    private static final long serialVersionUID = 9190278082893305233L;
    /**
     * Enable Ethernet protected.
     * <ul>
     * <li>true - enabled
     * <li>false - disabled
     * <li>null - default
     * </ul>
     */
    private Boolean ethernetProtection;

    /**
     * Extender nodes, map of equipment Id to base mac address
     */
    private Map<Long, MeshPointInformation> meshPointMap;

    public ApMeshPortalConfiguration(Long groupId, final Collection<MeshPointInformation> meshPoints,
            Boolean ethernetProtection) {
        this(groupId, meshPoints, ethernetProtection, 0L);
    }

    /**
     * Constructor with points, properties.
     * 
     * @param groupId
     * @param meshPoints
     * @param ethernetProtection
     * @param lastModifiedTimestamp
     */
    public ApMeshPortalConfiguration(Long groupId, final Collection<MeshPointInformation> meshPoints,
            Boolean ethernetProtection, long lastModifiedTimestamp) {
        this(groupId, lastModifiedTimestamp);
        setMeshPoints(meshPoints);
        this.ethernetProtection = ethernetProtection;
    }

    protected ApMeshPortalConfiguration() {
        this(null, 0L);
    }

    private ApMeshPortalConfiguration(Long groupId, long lastModifiedTimestamp) {
        super(ApMeshMode.MESH_PORTAL, groupId, lastModifiedTimestamp);
    }

    @Override
    public ApMeshPortalConfiguration clone() {
        ApMeshPortalConfiguration result = (ApMeshPortalConfiguration) super.clone();
        if (null != this.meshPointMap) {
            result.meshPointMap = null;
            result.setMeshPoints(this.meshPointMap.values());
        }
        return result;
    }

    public Boolean getEthernetProtection() {
        return ethernetProtection;
    }

    @JsonIgnore
    public Map<Long, MeshPointInformation> getMeshPointMap() {
        return meshPointMap;
    }

    public Collection<MeshPointInformation> getMeshPoints() {
        if (null == meshPointMap) {
            return Collections.emptyList();
        }
        return meshPointMap.values();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (BaseJsonModel.hasUnsupportedValue(this.meshPointMap)) {
            return true;
        }
        return false;
    }

    public void setEthernetProtection(Boolean ethernetProtection) {
        this.ethernetProtection = ethernetProtection;
    }

    @JsonIgnore
    public void setMeshPointMap(Map<Long, MeshPointInformation> extenderNodes) {
        this.meshPointMap = extenderNodes;
    }

    public void setMeshPoints(Collection<MeshPointInformation> meshPoints) {
        if (null == meshPoints) {
            this.meshPointMap = null;
        } else {
            if (null == this.meshPointMap) {
                this.meshPointMap = new HashMap<>();
            } else {
                this.meshPointMap.clear();
            }
            for (MeshPointInformation mp : meshPoints) {
                if (null != this.meshPointMap.put(mp.getEquipmentId(), mp)) {
                    throw new DsDataValidationException("duplicated mesh point with id " + mp.getEquipmentId());
                }
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(ethernetProtection, meshPointMap);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ApMeshPortalConfiguration)) {
            return false;
        }
        ApMeshPortalConfiguration other = (ApMeshPortalConfiguration) obj;
        return Objects.equals(ethernetProtection, other.ethernetProtection)
                && Objects.equals(meshPointMap, other.meshPointMap);
    }

}
