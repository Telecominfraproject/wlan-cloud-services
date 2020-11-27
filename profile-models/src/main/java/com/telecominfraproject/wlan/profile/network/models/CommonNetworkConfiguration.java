package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualString;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;


/**
 * @author dtoptygin
 */
public abstract class CommonNetworkConfiguration extends ProfileDetails {
    private static final long serialVersionUID = -1304499821472994219L;
    private String networkConfigVersion;
    private EquipmentType equipmentType;
    private Boolean vlanNative;  
    private int vlan;
    private AutoOrManualString ntpServer;
    private SyslogRelay syslogRelay;
    private RtlsSettings rtlsSettings;

    private Boolean syntheticClientEnabled;
    private Boolean ledControlEnabled;
    private Boolean equipmentDiscovery;
    
    private String greTunnelName;
    private String greParentIfName;
    private InetAddress greLocalInetAddr;
    private InetAddress greRemoteInetAddr;
    private MacAddress greRemoteMacAddr;

    public CommonNetworkConfiguration() {
    }

    public CommonNetworkConfiguration(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getNetworkConfigVersion() {
        return networkConfigVersion;
    }

    public void setNetworkConfigVersion(String networkConfigVersion) {
        this.networkConfigVersion = networkConfigVersion;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getVlan()
    {
        return this.vlan;
    }

    public void setVlan(int vlan)
    {
        this.vlan = vlan; 
    }

    public Boolean isVlanNative()
    {
        return this.vlanNative;
    }

    public void setVlanNative(Boolean bool)
    {
        this.vlanNative = bool;
    }


    public AutoOrManualString getNtpServer() {
        return ntpServer;
    }

    public void setNtpServer(AutoOrManualString ntpServer) {
        this.ntpServer = ntpServer;
    }

    public SyslogRelay getSyslogRelay() {
        return syslogRelay;
    }

    public void setSyslogRelay(SyslogRelay syslogRelay) {
        this.syslogRelay = syslogRelay;
    }

    public Boolean getSyntheticClientEnabled() {
        return syntheticClientEnabled;
    }

    public void setSyntheticClientEnabled(Boolean syntheticClientEnabled) {
        this.syntheticClientEnabled = syntheticClientEnabled;
    }


    public Boolean getLedControlEnabled() {
        return ledControlEnabled;
    }

    public void setLedControlEnabled(Boolean ledControlEnabled) {
        this.ledControlEnabled = ledControlEnabled;
    }
    
    public RtlsSettings getRtlsSettings() {
        return rtlsSettings;
    }

    public void setRtlsSettings(RtlsSettings rtlsSettings) {
        this.rtlsSettings = rtlsSettings;
    }

    @Override
    public CommonNetworkConfiguration clone() {

        CommonNetworkConfiguration ret = (CommonNetworkConfiguration) super.clone();

        if (syslogRelay != null) {
            ret.setSyslogRelay(syslogRelay.clone());
        }
        if( rtlsSettings != null) {
            ret.setRtlsSettings(rtlsSettings.clone());
        }
        
        ret.setVlan(getVlan());
        ret.setVlanNative(isVlanNative());
        ret.setSyntheticClientEnabled(getSyntheticClientEnabled());


        return ret;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (EquipmentType.isUnsupported(equipmentType)) {
            return true;
        }
        if (syslogRelay != null && hasUnsupportedValue(syslogRelay)) {
            return true;
        }

        return false;
    }

    public Boolean getEquipmentDiscovery() {
        return equipmentDiscovery;
    }

    public void setEquipmentDiscovery(Boolean equipmentDiscovery) {
        this.equipmentDiscovery = equipmentDiscovery;
    }

    
    public String getGreTunnelName() {
        return greTunnelName;
    }

    
    public void setGreTunnelName(String greTunnelName) {
        this.greTunnelName = greTunnelName;
    }

    
    public String getGreParentIfName() {
        return greParentIfName;
    }

    
    public void setGreParentIfName(String greParentIfName) {
        this.greParentIfName = greParentIfName;
    }

    
    public InetAddress getGreLocalInetAddr() {
        return greLocalInetAddr;
    }

    
    public void setGreLocalInetAddr(InetAddress greLocalInetAddr) {
        this.greLocalInetAddr = greLocalInetAddr;
    }

    
    public InetAddress getGreRemoteInetAddr() {
        return greRemoteInetAddr;
    }

    
    public void setGreRemoteInetAddr(InetAddress greRemoteInetAddr) {
        this.greRemoteInetAddr = greRemoteInetAddr;
    }

    
    public MacAddress getGreRemoteMacAddr() {
        return greRemoteMacAddr;
    }

    
    public void setGreRemoteMacAddr(MacAddress greRemoteMacAddr) {
        this.greRemoteMacAddr = greRemoteMacAddr;
    }

    
    public Boolean getVlanNative() {
        return vlanNative;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentDiscovery, equipmentType, greLocalInetAddr, greParentIfName, greRemoteInetAddr,
                greRemoteMacAddr, greTunnelName, ledControlEnabled, networkConfigVersion, ntpServer, rtlsSettings,
                syntheticClientEnabled, syslogRelay, vlan, vlanNative);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CommonNetworkConfiguration other = (CommonNetworkConfiguration) obj;
        return Objects.equals(equipmentDiscovery, other.equipmentDiscovery)
                && Objects.equals(equipmentType, other.equipmentType)
                && Objects.equals(greLocalInetAddr, other.greLocalInetAddr)
                && Objects.equals(greParentIfName, other.greParentIfName)
                && Objects.equals(greRemoteInetAddr, other.greRemoteInetAddr)
                && Objects.equals(greRemoteMacAddr, other.greRemoteMacAddr)
                && Objects.equals(greTunnelName, other.greTunnelName)
                && Objects.equals(ledControlEnabled, other.ledControlEnabled)
                && Objects.equals(networkConfigVersion, other.networkConfigVersion)
                && Objects.equals(ntpServer, other.ntpServer) && Objects.equals(rtlsSettings, other.rtlsSettings)
                && Objects.equals(syntheticClientEnabled, other.syntheticClientEnabled)
                && Objects.equals(syslogRelay, other.syslogRelay) && vlan == other.vlan
                && Objects.equals(vlanNative, other.vlanNative);
    }
    
    
}
