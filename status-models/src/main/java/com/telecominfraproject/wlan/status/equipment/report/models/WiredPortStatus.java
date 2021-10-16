package com.telecominfraproject.wlan.status.equipment.report.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import java.util.List;
import java.util.Objects;

/**
 * @author rsharma
 */
public class WiredPortStatus extends BaseJsonModel {

    private static final long serialVersionUID = 5525006111579379607L;

    private String name; // eth_port: eth0 or eth1
    private String originalIfName; // ifName when the AP came up (lan, wan etc.)
    private String currentIfName; // Current ifName (e.g. lan, wan etc.)
    private String ifType; // bridge/Nat/eth/vlan
    private int speed; // in MBPS
    private String duplex; // full, half
    private String operationalState; // Operational State
    private int vlanId; // tagged VlanId
    private boolean trunkEnabled; // trunk enabled or disabled
    private List<Integer> allowedVlanIds; // allowed vlanIds

    public WiredPortStatus() {
    	
    }

    public WiredPortStatus(String name, String originalIfName, String currentIfName, String ifType, int speed, String duplex,
                           String operationalState, int vlanId, boolean trunkEnabled, List<Integer> allowedVlanIds) {
        this.name = name;
        this.originalIfName = originalIfName;
        this.currentIfName = currentIfName;
        this.ifType = ifType;
        this.speed = speed;
        this.duplex = duplex;
        this.operationalState = operationalState;
        this.vlanId = vlanId;
        this.trunkEnabled = trunkEnabled;
        this.allowedVlanIds = allowedVlanIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WiredPortStatus)) {
            return false;
        }
        WiredPortStatus that = (WiredPortStatus) o;
        return getSpeed() == that.getSpeed() && Objects.equals(getName(), that.getName())
                && Objects.equals(getOriginalIfName(), that.getOriginalIfName())
                && Objects.equals(getCurrentIfName(), that.getCurrentIfName())
                && Objects.equals(getIfType(), that.getIfType())
                && Objects.equals(getDuplex(), that.getDuplex())
                && Objects.equals(getOperationalState(), that.getOperationalState())
                && getVlanId() == that.getVlanId()
                && Objects.equals(getAllowedVlanIds(), that.getAllowedVlanIds())
                && Objects.equals(isTrunkEnabled(), that.isTrunkEnabled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getOriginalIfName(), getCurrentIfName(), getIfType(), getSpeed(), getDuplex(),
                getOperationalState(), getVlanId(), getAllowedVlanIds(), isTrunkEnabled());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalIfName() {
        return originalIfName;
    }

    public void setOriginalIfName(String originalIfName) {
        this.originalIfName = originalIfName;
    }

    public String getCurrentIfName() {
        return currentIfName;
    }

    public void setCurrentIfName(String currentIfName) {
        this.currentIfName = currentIfName;
    }

    public String getIfType() {
        return ifType;
    }

    public void setIfType(String ifType) {
        this.ifType = ifType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getDuplex() {
        return duplex;
    }

    public void setDuplex(String duplex) {
        this.duplex = duplex;
    }

    public String getOperationalState() {
        return operationalState;
    }

    public void setOperationalState(String operationalState) {
        this.operationalState = operationalState;
    }

    public int getVlanId() {
        return vlanId;
    }

    public void setVlanId(int vlanId) {
        this.vlanId = vlanId;
    }

    public List<Integer> getAllowedVlanIds() {
        return allowedVlanIds;
    }

    public void setAllowedVlanIds(List<Integer> allowedVlanIds) {
        this.allowedVlanIds = allowedVlanIds;
    }

    public boolean isTrunkEnabled() {
        return trunkEnabled;
    }

    public void setTrunkEnabled(boolean trunkEnabled) {
        this.trunkEnabled = trunkEnabled;
    }
}
