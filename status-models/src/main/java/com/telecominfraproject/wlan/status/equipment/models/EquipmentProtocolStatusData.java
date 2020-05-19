package com.telecominfraproject.wlan.status.equipment.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EquipmentProtocolStatusData)) {
            return false;
        }
        EquipmentProtocolStatusData other = (EquipmentProtocolStatusData) obj;
        if (this.bandPlan == null) {
            if (other.bandPlan != null) {
                return false;
            }
        } else if (!this.bandPlan.equals(other.bandPlan)) {
            return false;
        }
        if (this.baseMacAddress == null) {
            if (other.baseMacAddress != null) {
                return false;
            }
        } else if (!this.baseMacAddress.equals(other.baseMacAddress)) {
            return false;
        }
        if (this.cloudCfgDataVersion == null) {
            if (other.cloudCfgDataVersion != null) {
                return false;
            }
        } else if (!this.cloudCfgDataVersion.equals(other.cloudCfgDataVersion)) {
            return false;
        }
        if (this.cloudProtocolVersion == null) {
            if (other.cloudProtocolVersion != null) {
                return false;
            }
        } else if (!this.cloudProtocolVersion.equals(other.cloudProtocolVersion)) {
            return false;
        }
        if (this.countryCode == null) {
            if (other.countryCode != null) {
                return false;
            }
        } else if (!this.countryCode.equals(other.countryCode)) {
            return false;
        }
        if (this.getDataThrottleCfgModeChanged() == null) {
            if (other.getDataThrottleCfgModeChanged() != null) {
                return false;
            }
        } else if (!this.getDataThrottleCfgModeChanged().equals(other.getDataThrottleCfgModeChanged())) {
            return false;
        }
        if (this.ipBasedConfiguration == null) {
            if (other.ipBasedConfiguration != null) {
                return false;
            }
        } else if (!this.ipBasedConfiguration.equals(other.ipBasedConfiguration)) {
            return false;
        }
        if (this.isApcConnected == null) {
            if (other.isApcConnected != null) {
                return false;
            }
        } else if (!this.isApcConnected.equals(other.isApcConnected)) {
            return false;
        }
        if (this.lastApcUpdate == null) {
            if (other.lastApcUpdate != null) {
                return false;
            }
        } else if (!this.lastApcUpdate.equals(other.lastApcUpdate)) {
            return false;
        }
        if (this.poweredOn != other.poweredOn) {
            return false;
        }
        if (this.protocolState != other.protocolState) {
            return false;
        }
        if (this.radiusProxyAddress == null) {
            if (other.radiusProxyAddress != null) {
                return false;
            }
        } else if (!this.radiusProxyAddress.equals(other.radiusProxyAddress)) {
            return false;
        }
        if (this.reportedApcAddress == null) {
            if (other.reportedApcAddress != null) {
                return false;
            }
        } else if (!this.reportedApcAddress.equals(other.reportedApcAddress)) {
            return false;
        }
        if (this.reportedCC != other.reportedCC) {
            return false;
        }
        if (this.reportedCfgDataVersion == null) {
            if (other.reportedCfgDataVersion != null) {
                return false;
            }
        } else if (!this.reportedCfgDataVersion.equals(other.reportedCfgDataVersion)) {
            return false;
        }
        if (this.reportedHwVersion == null) {
            if (other.reportedHwVersion != null) {
                return false;
            }
        } else if (!this.reportedHwVersion.equals(other.reportedHwVersion)) {
            return false;
        }
        if (this.reportedIpV4Addr == null) {
            if (other.reportedIpV4Addr != null) {
                return false;
            }
        } else if (!this.reportedIpV4Addr.equals(other.reportedIpV4Addr)) {
            return false;
        }
        if (this.reportedIpV6Addr == null) {
            if (other.reportedIpV6Addr != null) {
                return false;
            }
        } else if (!this.reportedIpV6Addr.equals(other.reportedIpV6Addr)) {
            return false;
        }

        if (this.reportedMacAddr == null) {
            if (other.reportedMacAddr != null) {
                return false;
            }
        } else if (!this.reportedMacAddr.equals(other.reportedMacAddr)) {
            return false;
        }        
        
        if (this.reportedSku == null) {
            if (other.reportedSku != null) {
                return false;
            }
        } else if (!this.reportedSku.equals(other.reportedSku)) {
            return false;
        }
        if (this.reportedSwAltVersion == null) {
            if (other.reportedSwAltVersion != null) {
                return false;
            }
        } else if (!this.reportedSwAltVersion.equals(other.reportedSwAltVersion)) {
            return false;
        }
        if (this.reportedSwVersion == null) {
            if (other.reportedSwVersion != null) {
                return false;
            }
        } else if (!this.reportedSwVersion.equals(other.reportedSwVersion)) {
            return false;
        }
        if (this.serialNumber == null) {
            if (other.serialNumber != null) {
                return false;
            }
        } else if (!this.serialNumber.equals(other.serialNumber)) {
            return false;
        }
        if (this.systemContact == null) {
            if (other.systemContact != null) {
                return false;
            }
        } else if (!this.systemContact.equals(other.systemContact)) {
            return false;
        }
        if (this.systemLocation == null) {
            if (other.systemLocation != null) {
                return false;
            }
        } else if (!this.systemLocation.equals(other.systemLocation)) {
            return false;
        }
        if (this.systemName == null) {
            if (other.systemName != null) {
                return false;
            }
        } else if (!this.systemName.equals(other.systemName)) {
            return false;
        }
        if (this.useTroubleshotThrottleConfig == null) {
            if (other.useTroubleshotThrottleConfig != null) {
                return false;
            }
        } else if (!this.useTroubleshotThrottleConfig.equals(other.useTroubleshotThrottleConfig)) {
            return false;
        }
        return true;
    }

    public String getBandPlan() {
        return bandPlan;
    }

    public MacAddress getBaseMacAddress() {
        return baseMacAddress;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.bandPlan == null) ? 0 : this.bandPlan.hashCode());
        result = prime * result + ((this.baseMacAddress == null) ? 0 : this.baseMacAddress.hashCode());
        result = prime * result + ((this.cloudCfgDataVersion == null) ? 0 : this.cloudCfgDataVersion.hashCode());
        result = prime * result + ((this.cloudProtocolVersion == null) ? 0 : this.cloudProtocolVersion.hashCode());
        result = prime * result + ((this.countryCode == null) ? 0 : this.countryCode.hashCode());
        result = prime * result
                + ((this.getDataThrottleCfgModeChanged() == null) ? 0 : this.getDataThrottleCfgModeChanged().hashCode());
        result = prime * result + ((this.ipBasedConfiguration == null) ? 0 : this.ipBasedConfiguration.hashCode());
        result = prime * result + ((this.isApcConnected == null) ? 0 : this.isApcConnected.hashCode());
        result = prime * result + ((this.lastApcUpdate == null) ? 0 : this.lastApcUpdate.hashCode());
        result = prime * result + (this.poweredOn ? 1231 : 1237);
        result = prime * result + ((this.protocolState == null) ? 0 : this.protocolState.hashCode());
        result = prime * result + ((this.radiusProxyAddress == null) ? 0 : this.radiusProxyAddress.hashCode());
        result = prime * result + ((this.reportedApcAddress == null) ? 0 : this.reportedApcAddress.hashCode());
        result = prime * result + ((this.reportedCC == null) ? 0 : this.reportedCC.hashCode());
        result = prime * result + ((this.reportedCfgDataVersion == null) ? 0 : this.reportedCfgDataVersion.hashCode());
        result = prime * result + ((this.reportedHwVersion == null) ? 0 : this.reportedHwVersion.hashCode());
        result = prime * result + ((this.reportedIpV4Addr == null) ? 0 : this.reportedIpV4Addr.hashCode());
        result = prime * result + ((this.reportedIpV6Addr == null) ? 0 : this.reportedIpV6Addr.hashCode());
        result = prime * result + ((this.reportedMacAddr == null) ? 0 : this.reportedMacAddr.hashCode());
        result = prime * result + ((this.reportedSku == null) ? 0 : this.reportedSku.hashCode());
        result = prime * result + ((this.reportedSwAltVersion == null) ? 0 : this.reportedSwAltVersion.hashCode());
        result = prime * result + ((this.reportedSwVersion == null) ? 0 : this.reportedSwVersion.hashCode());
        result = prime * result + ((this.serialNumber == null) ? 0 : this.serialNumber.hashCode());
        result = prime * result + ((this.systemContact == null) ? 0 : this.systemContact.hashCode());
        result = prime * result + ((this.systemLocation == null) ? 0 : this.systemLocation.hashCode());
        result = prime * result + ((this.systemName == null) ? 0 : this.systemName.hashCode());
        result = prime * result
                + ((this.useTroubleshotThrottleConfig == null) ? 0 : this.useTroubleshotThrottleConfig.hashCode());
        return result;
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
}
