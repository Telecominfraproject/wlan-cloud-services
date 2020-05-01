package com.telecominfraproject.wlan.equipment.models;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.DeploymentType;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;

/**
 * @author dtoptygin 
 */
public abstract class CommonElementConfiguration extends EquipmentDetails implements PushableConfiguration {

    private static final long serialVersionUID = 8677752036977274097L;
    private String elementConfigVersion;
    private EquipmentType equipmentType;

    private DeviceMode deviceMode;
    private GettingIP gettingIP;
    private InetAddress staticIP;
    private Integer staticIpMaskCidr;
    private InetAddress staticIpGw;
    private GettingDNS gettingDNS;
    private InetAddress staticDnsIp1;
    private InetAddress staticDnsIp2;
    private List<PeerInfo> peerInfoList = new ArrayList<>();
    private String deviceName;
    private String locationData;
    private int locallyConfiguredMgmtVlan;
    private boolean locallyConfigured;
    private DeploymentType deploymentType;
    private Boolean syntheticClientEnabled;
    private Boolean frameReportThrottleEnabled;
    private AntennaType antennaType;
    private Boolean costSavingEventsEnabled;
    private NetworkForwardMode forwardMode;

    /**
     * this constructor is used for CAMI only
     */
    public CommonElementConfiguration() {
        this.deviceName = "Default Device Name";
        this.deploymentType = DeploymentType.CEILING;
        this.antennaType = AntennaType.OMNI;
        this.forwardMode = NetworkForwardMode.BRIDGE;
    }

    protected CommonElementConfiguration(EquipmentType equipmentType) {
        this();
        this.equipmentType = equipmentType;
    }

    @Override
    public CommonElementConfiguration clone() {
        CommonElementConfiguration ret = (CommonElementConfiguration) super.clone();

        if (peerInfoList != null) {
            ret.peerInfoList = new ArrayList<>(peerInfoList.size());
            for (PeerInfo pi : peerInfoList) {
                ret.peerInfoList.add(pi.clone());
            }
        }

        return ret;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (!super.equals(obj)) {
            return false;
        }
        
        if (!(obj instanceof CommonElementConfiguration)) {
            return false;
        }
        CommonElementConfiguration other = (CommonElementConfiguration) obj;
        return this.antennaType == other.antennaType
                && Objects.equals(costSavingEventsEnabled, other.costSavingEventsEnabled)
                && this.deploymentType == other.deploymentType && this.deviceMode == other.deviceMode
                && Objects.equals(deviceName, other.deviceName)
                && Objects.equals(elementConfigVersion, other.elementConfigVersion)
                && this.equipmentType == other.equipmentType && this.forwardMode == other.forwardMode
                && Objects.equals(frameReportThrottleEnabled, other.frameReportThrottleEnabled)
                && this.gettingDNS == other.gettingDNS && this.gettingIP == other.gettingIP
                && this.locallyConfigured == other.locallyConfigured
                && this.locallyConfiguredMgmtVlan == other.locallyConfiguredMgmtVlan
                && Objects.equals(locationData, other.locationData) && Objects.equals(peerInfoList, other.peerInfoList)
                && Objects.equals(staticDnsIp1, other.staticDnsIp1) && Objects.equals(staticDnsIp2, other.staticDnsIp2)
                && Objects.equals(staticIP, other.staticIP) && Objects.equals(staticIpGw, other.staticIpGw)
                && Objects.equals(staticIpMaskCidr, other.staticIpMaskCidr)
                && Objects.equals(syntheticClientEnabled, other.syntheticClientEnabled);
    }

    public AntennaType getAntennaType() {
        return antennaType;
    }

    public Boolean getCostSavingEventsEnabled() {
        return costSavingEventsEnabled;
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

    public DeviceMode getDeviceMode() {
        return deviceMode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getElementConfigVersion() {
        return elementConfigVersion;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public NetworkForwardMode getForwardMode() {
        return forwardMode;
    }

    /**
     * @return the frameReportThrottleEnabled
     */
    public Boolean getFrameReportThrottleEnabled() {
        return frameReportThrottleEnabled;
    }

    public GettingDNS getGettingDNS() {
        return gettingDNS;
    }

    public GettingIP getGettingIP() {
        return gettingIP;
    }

    public int getLocallyConfiguredMgmtVlan() {
        return this.locallyConfiguredMgmtVlan;
    }

    public String getLocationData() {
        return locationData;
    }

    public List<PeerInfo> getPeerInfoList() {
        // TODO: refactor this - callers should be recognizing nulls in here
        if (peerInfoList == null) {
            peerInfoList = new ArrayList<>();
        }
        return peerInfoList;
    }

    public InetAddress getStaticDnsIp1() {
        return staticDnsIp1;
    }

    public InetAddress getStaticDnsIp2() {
        return staticDnsIp2;
    }

    public InetAddress getStaticIP() {
        return staticIP;
    }

    public InetAddress getStaticIpGw() {
        return staticIpGw;
    }

    public Integer getStaticIpMaskCidr() {
        return staticIpMaskCidr;
    }

    public Boolean getSyntheticClientEnabled() {
        return syntheticClientEnabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(antennaType, costSavingEventsEnabled, deploymentType, deviceMode, deviceName,
                elementConfigVersion, equipmentType, forwardMode, frameReportThrottleEnabled, gettingDNS, gettingIP,
                locallyConfigured, locallyConfiguredMgmtVlan, locationData, peerInfoList, staticDnsIp1, staticDnsIp2,
                staticIP, staticIpGw, staticIpMaskCidr, syntheticClientEnabled);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (EquipmentType.isUnsupported(equipmentType) || DeviceMode.isUnsupported(deviceMode)
                || GettingIP.isUnsupported(gettingIP) || GettingDNS.isUnsupported(gettingDNS)
                || hasUnsupportedValue(peerInfoList) || DeploymentType.isUnsupported(deploymentType)
                || AntennaType.isUnsupported(antennaType) || NetworkForwardMode.isUnsupported(this.forwardMode)) {
            return true;
        }
        return false;
    }

    public boolean isLocallyConfigured() {
        return this.locallyConfigured;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(Object previousVersion) {
        if (previousVersion instanceof CommonElementConfiguration) {
            return !Objects.equals(this, previousVersion);
        }

        return true;
    }

    public void setAntennaType(AntennaType antennaType) {
        this.antennaType = antennaType;
    }

    public void setCostSavingEventsEnabled(Boolean costSavingEventsEnabled) {
        this.costSavingEventsEnabled = costSavingEventsEnabled;
    }

    public void setDeploymentType(DeploymentType deploymentType) {
        this.deploymentType = deploymentType;
    }

    public void setDeviceMode(DeviceMode deviceMode) {
        this.deviceMode = deviceMode;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setElementConfigVersion(String elementConfigVersion) {
        this.elementConfigVersion = elementConfigVersion;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setForwardMode(NetworkForwardMode forwardMode) {
        this.forwardMode = forwardMode;
    }

    /**
     * @param frameReportThrottleEnabled
     *            the frameReportThrottleEnabled to set
     */
    public void setFrameReportThrottleEnabled(Boolean frameReportThrottleEnabled) {
        this.frameReportThrottleEnabled = frameReportThrottleEnabled;
    }

    public void setGettingDNS(GettingDNS gettingDNS) {
        this.gettingDNS = gettingDNS;
    }

    public void setGettingIP(GettingIP gettingIP) {
        this.gettingIP = gettingIP;
    }

    public void setLocallyConfigured(boolean configed) {
        this.locallyConfigured = configed;
    }

    public void setLocallyConfiguredMgmtVlan(int mgmtVlan) {
        this.locallyConfiguredMgmtVlan = mgmtVlan;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    public void setPeerInfoList(List<PeerInfo> peerInfoList) {
        this.peerInfoList = peerInfoList;
    }

    public void setStaticDnsIp1(InetAddress staticDnsIp1) {
        this.staticDnsIp1 = staticDnsIp1;
    }

    public void setStaticDnsIp2(InetAddress staticDnsIp2) {
        this.staticDnsIp2 = staticDnsIp2;
    }

    public void setStaticIP(InetAddress staticIP) {
        this.staticIP = staticIP;
    }

    public void setStaticIpGw(InetAddress staticIpGw) {
        this.staticIpGw = staticIpGw;
    }

    public void setStaticIpMaskCidr(Integer staticIpMaskCidr) {
        this.staticIpMaskCidr = staticIpMaskCidr;
    }

    public void setSyntheticClientEnabled(Boolean syntheticClientEnabled) {
        this.syntheticClientEnabled = syntheticClientEnabled;
    }
}
