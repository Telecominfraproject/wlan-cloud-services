package com.telecominfraproject.wlan.status.equipment.models;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.ApcElectionEvent.ApcMode;

/**
 * Equipment protocol status. Updated by the Equipment Gateway when
 * protocol state changes.
 * 
 * @author yongli
 *
 */
public class EquipmentProtocolStatusData extends StatusDetails {
    private static final long serialVersionUID = 1193955685155400122L;

    /**
     * Powered On
     */
    private boolean poweredOn;

    /**
     * Protocol state
     */
    private EquipmentProtocolState protocolState;

    /**
     * H/W revision
     */
    private String reportedHwVersion;
    /**
     * Current software version
     */
    private String reportedSwVersion;
    /**
     * Software version in alternative bank
     */
    private String reportedSwAltVersion;
    /**
     * CAMI protocol version
     */
    private String cloudProtocolVersion;
    /**
     * IPv4 address
     */
    private InetAddress reportedIpV4Addr;
    /**
     * IPv6 address
     */
    private InetAddress reportedIpV6Addr;
    /**
     * MAC address
     */
    private MacAddress reportedMacAddr;
    /**
     * Country code
     */
    private String countryCode;
    /**
     * System name
     */
    private String systemName;
    /**
     * System contact name
     */
    private String systemContact;
    /**
     * System location
     */
    private String systemLocation;
    /**
     * System band plan
     */
    private String bandPlan;
    /**
     * System serial number
     */
    private String serialNumber;
    /**
     * Base MAC address
     */
    private MacAddress baseMacAddress;

    /**
     * IP address of the APC
     */
    private InetAddress apcDesignatedRouterIpAddress;

    /**
     * Backup IP address of the APC
     */
    private InetAddress apcBackupDesignatedRouterIpAddress;
    
    /**
     * APC mode of the Equipment
     */
    private ApcMode apcMode;
    
    /**
     * The last reported ApcAddress. 
     */
    private InetAddress reportedApcAddress;

    /**
     * Timestamp for the last APC status report
     */
    private Long lastApcUpdate;

    /**
     * Flag indicate the current APC connection state.
     * true means election is finished.
     */
    private Boolean isApcConnected;

    /**
     * Management IP address used to generate the configuration. Only set if
     * configuration is generated based on management ip.
     */
    private InetAddress ipBasedConfiguration;

    /**
     * Reported SKU
     */
    private String reportedSku;

    /**
     * Reported Country Code
     */
    private CountryCode reportedCC;

    /**
     * RADIUS proxy server floating IP address. If this is elected as APC. value
     * of 0.0.0.0 or null means RADIUS proxy is not enabled. Other value means
     * it's acting as RADIUS proxy server at the IP address.
     */
    private InetAddress radiusProxyAddress;

    /**
     * Last equipment reported configure data version
     */
    private Long reportedCfgDataVersion;

    /**
     * Last cloud resolved configuration data version
     */
    private Long cloudCfgDataVersion;
    
    /**
     * Use troubleshooting throttle configuration
     */
    private Boolean useTroubleshotThrottleConfig;
    
    /**
     * Last time {@link #useTroubleshotThrottleConfig} changed
     */
    private Long dataThrottleCfgModeChanged;

    public EquipmentProtocolStatusData() {
    }

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.PROTOCOL;
    }
    
    public EquipmentProtocolStatusData(EquipmentProtocolStatusData other) {
        // super(other);
        this.poweredOn = other.poweredOn;
        this.protocolState = other.protocolState;
        this.reportedHwVersion = other.reportedHwVersion;
        this.reportedSwVersion = other.reportedSwVersion;
        this.reportedSwAltVersion = other.reportedSwAltVersion;
        this.cloudProtocolVersion = other.cloudProtocolVersion;
        this.reportedIpV4Addr = other.reportedIpV4Addr;
        this.reportedIpV6Addr = other.reportedIpV6Addr;
        this.reportedMacAddr = other.reportedMacAddr;
        this.countryCode = other.countryCode;
        this.systemName = other.systemName;
        this.systemContact = other.systemContact;
        this.systemLocation = other.systemLocation;
        this.bandPlan = other.bandPlan;
        this.serialNumber = other.serialNumber;
        this.baseMacAddress = other.baseMacAddress == null ? null : other.baseMacAddress.clone();
        this.reportedApcAddress = other.reportedApcAddress;
        this.lastApcUpdate = other.lastApcUpdate;
        this.isApcConnected = other.isApcConnected;
        this.ipBasedConfiguration = other.ipBasedConfiguration;
        this.reportedSku = other.reportedSku;
        this.reportedCC = other.reportedCC;
        this.radiusProxyAddress = other.radiusProxyAddress;
        this.reportedCfgDataVersion = other.reportedCfgDataVersion;
        this.cloudCfgDataVersion = other.cloudCfgDataVersion;
        this.useTroubleshotThrottleConfig = other.useTroubleshotThrottleConfig;
        this.setDataThrottleCfgModeChanged(other.getDataThrottleCfgModeChanged());
    }

    public EquipmentProtocolStatusData clone() {
        EquipmentProtocolStatusData result = (EquipmentProtocolStatusData) super.clone();
        if (reportedMacAddr != null) {
            result.reportedMacAddr = reportedMacAddr.clone();
        }
        if (baseMacAddress != null) {
            result.baseMacAddress = baseMacAddress.clone();
        }
        return result;
    }

    public String getBandPlan() {
        return bandPlan;
    }

    public MacAddress getBaseMacAddress() {
        return baseMacAddress;
    }

    public InetAddress getApcDesignatedRouterIpAddress() {
        return apcDesignatedRouterIpAddress;
    }

    public InetAddress getApcBackupDesignatedRouterIpAddress() {
        return apcBackupDesignatedRouterIpAddress;
    }

    public ApcMode getApcMode() {
        return apcMode;
    }

    public Long getCloudCfgDataVersion() {
        return cloudCfgDataVersion;
    }

    public String getCloudProtocolVersion() {
        return cloudProtocolVersion;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public InetAddress getIpBasedConfiguration() {
        return ipBasedConfiguration;
    }

    public Boolean getIsApcConnected() {
        return isApcConnected;
    }

    public Long getLastApcUpdate() {
        return lastApcUpdate;
    }

    public EquipmentProtocolState getProtocolState() {
        return protocolState;
    }

    public InetAddress getRadiusProxyAddress() {
        return radiusProxyAddress;
    }

    public InetAddress getReportedApcAddress() {
        return reportedApcAddress;
    }

    public CountryCode getReportedCC() {
        return reportedCC;
    }

    public Long getReportedCfgDataVersion() {
        return reportedCfgDataVersion;
    }

    public String getReportedHwVersion() {
        return reportedHwVersion;
    }

    public InetAddress getReportedIpV4Addr() {
        return reportedIpV4Addr;
    }

    public InetAddress getReportedIpV6Addr() {
        return reportedIpV6Addr;
    }

    public MacAddress getReportedMacAddr() {
        return reportedMacAddr;
    }

    public String getReportedSku() {
        return reportedSku;
    }

    public String getReportedSwAltVersion() {
        return reportedSwAltVersion;
    }

    public String getReportedSwVersion() {
        return reportedSwVersion;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSystemContact() {
        return systemContact;
    }

    public String getSystemLocation() {
        return systemLocation;
    }

    public String getSystemName() {
        return systemName;
    }

    public boolean isPoweredOn() {
        return poweredOn;
    }

    public void setBandPlan(String bandPlan) {
        this.bandPlan = bandPlan;
    }

    public void setBaseMacAddress(MacAddress baseMacAddress) {
        this.baseMacAddress = baseMacAddress;
    }

    public void setApcDesignatedRouterIpAddress(InetAddress apcDesignatedRouterIpAddress) {
        this.apcDesignatedRouterIpAddress = apcDesignatedRouterIpAddress;
    }

    public void setApcBackupDesignatedRouterIpAddress(InetAddress apcBackupDesignatedRouterIpAddress) {
        this.apcBackupDesignatedRouterIpAddress = apcBackupDesignatedRouterIpAddress;
    }

    public void setApcMode(ApcMode apcMode) {
        this.apcMode = apcMode;
    }

    public void setCloudCfgDataVersion(Long cloudCfgDataVersion) {
        this.cloudCfgDataVersion = cloudCfgDataVersion;
    }

    public void setCloudProtocolVersion(String cloudProtocolVersion) {
        this.cloudProtocolVersion = cloudProtocolVersion;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setIpBasedConfiguration(InetAddress ipBasedConfiguration) {
        this.ipBasedConfiguration = ipBasedConfiguration;
    }

    public void setIsApcConnected(Boolean isApcConnected) {
        this.isApcConnected = isApcConnected;
    }

    public void setLastApcUpdate(Long lastApcUpdate) {
        this.lastApcUpdate = lastApcUpdate;
    }

    public void setPoweredOn(boolean poweredOn) {
        this.poweredOn = poweredOn;
    }

    public void setProtocolState(EquipmentProtocolState protocolState) {
        this.protocolState = protocolState;
    }

    public void setRadiusProxyAddress(InetAddress radiusProxyAddress) {
        this.radiusProxyAddress = radiusProxyAddress;
    }

    public void setReportedApcAddress(InetAddress reportedApcAddress) {
        this.reportedApcAddress = reportedApcAddress;
    }

    public void setReportedCC(CountryCode reportedCC) {
        this.reportedCC = reportedCC;
    }

    public void setReportedCfgDataVersion(Long reportedCfgDataVersion) {
        this.reportedCfgDataVersion = reportedCfgDataVersion;
    }

    public void setReportedHwVersion(String reportedHwVersion) {
        this.reportedHwVersion = reportedHwVersion;
    }

    public void setReportedIpV4Addr(InetAddress reportedIpV4Addr) {
        this.reportedIpV4Addr = reportedIpV4Addr;
    }

    public void setReportedIpV6Addr(InetAddress reportedIpV6Addr) {
        this.reportedIpV6Addr = reportedIpV6Addr;
    }

    public void setReportedMacAddr(MacAddress reportedMacAddr) {
        this.reportedMacAddr = reportedMacAddr;
    }

    public void setReportedSku(String reportedSku) {
        this.reportedSku = reportedSku;
    }

    public void setReportedSwAltVersion(String reportedSwAltVersion) {
        this.reportedSwAltVersion = reportedSwAltVersion;
    }

    public void setReportedSwVersion(String reportedSwVersion) {
        this.reportedSwVersion = reportedSwVersion;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setSystemContact(String systemContact) {
        this.systemContact = systemContact;
    }

    public void setSystemLocation(String systemLocation) {
        this.systemLocation = systemLocation;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (EquipmentProtocolState.isUnsupported(protocolState) || CountryCode.isUnsupported(reportedCC)
                || hasUnsupportedValue(baseMacAddress)) {
            return true;
        }
        return false;
    }
    
    public boolean hasActiveRadiusProxyIp() {
        return (this.radiusProxyAddress != null && !this.radiusProxyAddress.isAnyLocalAddress());
    }

    public static boolean hasActiveRadiusProxyIp(EquipmentProtocolStatusData data) {
        return (data.radiusProxyAddress != null && !data.radiusProxyAddress.isAnyLocalAddress());
    }

    public Boolean getUseTroubleshotThrottleConfig() {
        return useTroubleshotThrottleConfig;
    }

    public void setUseTroubleshotThrottleConfig(Boolean useTroubleshotThrottleConfig) {
        this.useTroubleshotThrottleConfig = useTroubleshotThrottleConfig;
    }

    public Long getDataThrottleCfgModeChanged() {
        return dataThrottleCfgModeChanged;
    }

    public void setDataThrottleCfgModeChanged(Long dataThrottleCfgModeChanged) {
        this.dataThrottleCfgModeChanged = dataThrottleCfgModeChanged;
    }

    @Override
    public int hashCode() {
        return Objects.hash(apcBackupDesignatedRouterIpAddress, apcDesignatedRouterIpAddress, apcMode, bandPlan, baseMacAddress, cloudCfgDataVersion,
                cloudProtocolVersion, countryCode, dataThrottleCfgModeChanged, ipBasedConfiguration, isApcConnected, lastApcUpdate, poweredOn, protocolState,
                radiusProxyAddress, reportedApcAddress, reportedCC, reportedCfgDataVersion, reportedHwVersion, reportedIpV4Addr, reportedIpV6Addr,
                reportedMacAddr, reportedSku, reportedSwAltVersion, reportedSwVersion, serialNumber, systemContact, systemLocation, systemName,
                useTroubleshotThrottleConfig);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EquipmentProtocolStatusData other = (EquipmentProtocolStatusData) obj;
        return Objects.equals(apcBackupDesignatedRouterIpAddress, other.apcBackupDesignatedRouterIpAddress)
                && Objects.equals(apcDesignatedRouterIpAddress, other.apcDesignatedRouterIpAddress) && apcMode == other.apcMode
                && Objects.equals(bandPlan, other.bandPlan) && Objects.equals(baseMacAddress, other.baseMacAddress)
                && Objects.equals(cloudCfgDataVersion, other.cloudCfgDataVersion) && Objects.equals(cloudProtocolVersion, other.cloudProtocolVersion)
                && Objects.equals(countryCode, other.countryCode) && Objects.equals(dataThrottleCfgModeChanged, other.dataThrottleCfgModeChanged)
                && Objects.equals(ipBasedConfiguration, other.ipBasedConfiguration) && Objects.equals(isApcConnected, other.isApcConnected)
                && Objects.equals(lastApcUpdate, other.lastApcUpdate) && poweredOn == other.poweredOn && protocolState == other.protocolState
                && Objects.equals(radiusProxyAddress, other.radiusProxyAddress) && Objects.equals(reportedApcAddress, other.reportedApcAddress)
                && Objects.equals(reportedCC, other.reportedCC) && Objects.equals(reportedCfgDataVersion, other.reportedCfgDataVersion)
                && Objects.equals(reportedHwVersion, other.reportedHwVersion) && Objects.equals(reportedIpV4Addr, other.reportedIpV4Addr)
                && Objects.equals(reportedIpV6Addr, other.reportedIpV6Addr) && Objects.equals(reportedMacAddr, other.reportedMacAddr)
                && Objects.equals(reportedSku, other.reportedSku) && Objects.equals(reportedSwAltVersion, other.reportedSwAltVersion)
                && Objects.equals(reportedSwVersion, other.reportedSwVersion) && Objects.equals(serialNumber, other.serialNumber)
                && Objects.equals(systemContact, other.systemContact) && Objects.equals(systemLocation, other.systemLocation)
                && Objects.equals(systemName, other.systemName) && Objects.equals(useTroubleshotThrottleConfig, other.useTroubleshotThrottleConfig);
    }
    
    
}
