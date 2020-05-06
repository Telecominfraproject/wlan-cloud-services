package com.telecominfraproject.wlan.profile.network.models;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualString;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((equipmentType == null) ? 0 : equipmentType.hashCode());
        result = prime * result + ((ledControlEnabled == null) ? 0 : ledControlEnabled.hashCode());
        result = prime * result + ((networkConfigVersion == null) ? 0 : networkConfigVersion.hashCode());
        result = prime * result + ((ntpServer == null) ? 0 : ntpServer.hashCode());
        result = prime * result + ((rtlsSettings == null) ? 0 : rtlsSettings.hashCode());
        result = prime * result + ((syntheticClientEnabled == null) ? 0 : syntheticClientEnabled.hashCode());
        result = prime * result + ((syslogRelay == null) ? 0 : syslogRelay.hashCode());
        result = prime * result + vlan;
        result = prime * result + ((vlanNative == null) ? 0 : vlanNative.hashCode());
        result = prime * result + ((equipmentDiscovery == null) ? 0 : equipmentDiscovery.hashCode());

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
        if (!(obj instanceof CommonNetworkConfiguration)) {
            return false;
        }
        CommonNetworkConfiguration other = (CommonNetworkConfiguration) obj;
        if (equipmentType != other.equipmentType) {
            return false;
        }
        if (ledControlEnabled == null) {
            if (other.ledControlEnabled != null) {
                return false;
            }
        } else if (!ledControlEnabled.equals(other.ledControlEnabled)) {
            return false;
        }
        if (networkConfigVersion == null) {
            if (other.networkConfigVersion != null) {
                return false;
            }
        } else if (!networkConfigVersion.equals(other.networkConfigVersion)) {
            return false;
        }
        if (ntpServer == null) {
            if (other.ntpServer != null) {
                return false;
            }
        } else if (!ntpServer.equals(other.ntpServer)) {
            return false;
        }
        if (rtlsSettings == null) {
            if (other.rtlsSettings != null) {
                return false;
            }
        } else if (!rtlsSettings.equals(other.rtlsSettings)) {
            return false;
        }
        if (syntheticClientEnabled == null) {
            if (other.syntheticClientEnabled != null) {
                return false;
            }
        } else if (!syntheticClientEnabled.equals(other.syntheticClientEnabled)) {
            return false;
        }
        if (syslogRelay == null) {
            if (other.syslogRelay != null) {
                return false;
            }
        } else if (!syslogRelay.equals(other.syslogRelay)) {
            return false;
        }
        if (vlan != other.vlan) {
            return false;
        }
        if (vlanNative == null) {
            if (other.vlanNative != null) {
                return false;
            }
        } else if (!vlanNative.equals(other.vlanNative)) {
            return false;
        }
        if (equipmentDiscovery == null) {
            if (other.equipmentDiscovery != null) {
                return false;
            }
        } else if (!equipmentDiscovery.equals(other.equipmentDiscovery)) {
            return false;
        }
        return true;
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
}
