package com.telecominfraproject.wlan.profile.ethernetport.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author rsharma
 */
public class WiredPort extends BaseJsonModel {

    private static final long serialVersionUID = 6595665507927422135L;

    private String name; //eth0 or eth1
    private String displayName; // LAN-1, WAN-0 etc
    private String ifName; // lan, wan etc
    private String ifType; // bridge/Nat
    private int vlanId; // native vlanId/tagged vlanId
    private boolean trunkEnabled; // trunk enabled or disabled
    private List<Integer> allowedVlanIds; // allowed vlanIds

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public String getIfType() {
        return ifType;
    }

    public void setIfType(String ifType) {
        this.ifType = ifType;
    }

    public int getVlanId() {
        return vlanId;
    }

    public void setVlanId(int vlanId) {
        this.vlanId = vlanId;
    }

    public boolean isTrunkEnabled() {
        return trunkEnabled;
    }

    public void setTrunkEnabled(boolean trunkEnabled) {
        this.trunkEnabled = trunkEnabled;
    }

    public List<Integer> getAllowedVlanIds() {
        return allowedVlanIds;
    }

    public void setAllowedVlanIds(List<Integer> allowedVlanIds) {
        this.allowedVlanIds = allowedVlanIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WiredPort)) {
            return false;
        }
        WiredPort wiredPort = (WiredPort) o;
        return getVlanId() == wiredPort.getVlanId() && isTrunkEnabled() == wiredPort.isTrunkEnabled()
                && Objects.equals(getName(), wiredPort.getName())
                && Objects.equals(getDisplayName(), wiredPort.getDisplayName())
                && Objects.equals(getIfName(), wiredPort.getIfName())
                && Objects.equals(getIfType(), wiredPort.getIfType())
                && Objects.equals(getAllowedVlanIds(), wiredPort.getAllowedVlanIds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDisplayName(), getIfName(), getIfType(), getVlanId(), isTrunkEnabled(), getAllowedVlanIds());
    }

    @Override
    public WiredPort clone() {
        WiredPort port = (WiredPort) super.clone();

        if (getAllowedVlanIds() != null) {
            port.allowedVlanIds = new ArrayList<>(getAllowedVlanIds());
        }

        return port;
    }
}
