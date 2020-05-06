package com.telecominfraproject.wlan.profile.ssid.models;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;
import com.telecominfraproject.wlan.equipment.models.NetworkForwardMode;
import com.telecominfraproject.wlan.equipment.models.StateSetting;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;

/**
 * @author dtoptygin
 *
 */
public class SsidConfiguration extends ProfileDetails implements PushableConfiguration<SsidConfiguration>
{

    private static final long serialVersionUID = 980346612233615236L;

    private static final Logger LOG = LoggerFactory.getLogger(SsidConfiguration.class);
    
    /* Defaults */
    final static Integer DEFAULT_KEY_REFRESH = 0;
    final static Integer DEFAULT_VLAN = null;
    final static StateSetting DEFAULT_BROADCAST_SSID = StateSetting.enabled;
    final static Integer BANDWIDTH_LIMIT_NO_LIMIT = 0;
    final static Integer BANDWIDTH_LIMIT_MAX = 800;
    final static String  DEFAULT_SSID_NAME = "Default-SSID";
    
    private String ssid;
    private Set<RadioType> appliedRadios = new HashSet<>();
    private StateSetting ssidAdminState;
    private SecureMode secureMode;
    private Integer vlanId = DEFAULT_VLAN;
    private String keyStr;
    private StateSetting broadcastSsid = DEFAULT_BROADCAST_SSID;
    private Integer keyRefresh = DEFAULT_KEY_REFRESH;

    private Boolean noLocalSubnets;
    private String radiusServiceName;
    private Long captivePortalId;
    
    private Integer bandwidthLimitDown;
    private Integer bandwidthLimitUp;
    private Boolean videoTrafficOnly;
    
    private Map<RadioType, RadioBasedSsidConfiguration> radioBasedConfigs;
    private Long bonjourGatewayProfileId;
    
    private Boolean enable80211w;
    
    private WepConfiguration wepConfig;
    
    private NetworkForwardMode forwardMode;

    /**
     * @return the noLocalSubnets
     */
    public Boolean getNoLocalSubnets() {
        return noLocalSubnets;
    }

    /**
     * @param noLocalSubnets the noLocalSubnets to set
     */
    public void setNoLocalSubnets(Boolean noLocalSubnets) {
        this.noLocalSubnets = noLocalSubnets;
    }

    private SsidConfiguration()
    {
        /**
         * 
         * BATTLESTAR GALLACTICA WAS A HORRIBLE SERIES (now I have your attention)
         * 
         * SUPER IMPORTANT NOTICE:  
         * 
         * If you modify these values, make sure you update the timestamp in DefaultableConfiguration otherwise
         * they won't be downloaded when an AP reconnects.
         * 
         */
        long timestamp = System.currentTimeMillis();
        setSsid(DEFAULT_SSID_NAME + "-" + timestamp);
        getAppliedRadios().add(RadioType.is2dot4GHz);
        getAppliedRadios().add(RadioType.is5GHz);
        setBroadcastSsid(DEFAULT_BROADCAST_SSID);
        setVlanId(1);
        setSecureMode(SecureMode.open);
        setSsidAdminState(StateSetting.enabled);
        setNoLocalSubnets(false);
        setBandwidthLimitDown(BANDWIDTH_LIMIT_NO_LIMIT);
        setBandwidthLimitUp(BANDWIDTH_LIMIT_NO_LIMIT);
        setForwardMode(forwardMode);
        radioBasedConfigs = initRadioBasedConfig();
        setVideoTrafficOnly(false);
    }
        
    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Set<RadioType> getAppliedRadios() {
		return appliedRadios;
	}

	public void setAppliedRadios(Set<RadioType> appliedRadios) {
		this.appliedRadios = appliedRadios;
	}

	public StateSetting getSsidAdminState() {
        return ssidAdminState;
    }

    public void setSsidAdminState(StateSetting ssidAdminState) {
        this.ssidAdminState = ssidAdminState;
    }

    public SecureMode getSecureMode() {
        return secureMode;
    }

    public void setSecureMode(SecureMode secureMode) {
        this.secureMode = secureMode;
    }

    public Integer getKeyRefresh()
    {
       return this.keyRefresh;
    }
    
    public void setKeyRefresh(Integer refresh)
    {
       this.keyRefresh = refresh;
    }
    
    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public String getKeyStr() {
        return keyStr;
    }

    public void setKeyStr(String keyStr) {
        this.keyStr = keyStr;
    }

    public StateSetting getBroadcastSsid()
    {
       return this.broadcastSsid;
    }
    
    public void setBroadcastSsid(StateSetting bSsid)
    {
       this.broadcastSsid = bSsid;
    }
        
    public Long getCaptivePortalId() {
        return captivePortalId;
    }

    public void setCaptivePortalId(Long captivePortalId) {
        this.captivePortalId = captivePortalId;
    }

    public Integer getBandwidthLimitDown() {
        return bandwidthLimitDown;
    }

    public void setBandwidthLimitDown(Integer bandwidthLimitDown) {
        if (bandwidthLimitDown < BANDWIDTH_LIMIT_NO_LIMIT) {
            LOG.debug("Unable to set bandwidth limit down less than {}. Using default value of NO LIMIT.", BANDWIDTH_LIMIT_NO_LIMIT);
            this.bandwidthLimitDown = BANDWIDTH_LIMIT_NO_LIMIT;
            return;
        }
        if (bandwidthLimitDown > BANDWIDTH_LIMIT_MAX) {
            LOG.debug("Unable to set bandwidth limit down gresater than {}. Using max value of {}Mbps.", BANDWIDTH_LIMIT_MAX);
            this.bandwidthLimitDown = BANDWIDTH_LIMIT_MAX;
            return;
        }
        
        this.bandwidthLimitDown = bandwidthLimitDown;
    }

    public Integer getBandwidthLimitUp() {
        return bandwidthLimitUp;
    }

    public void setBandwidthLimitUp(Integer bandwidthLimitUp) {
        if (bandwidthLimitUp < BANDWIDTH_LIMIT_NO_LIMIT) {
            LOG.debug("Unable to set bandwidth limit up less than {}. Using default value of NO LIMIT.", BANDWIDTH_LIMIT_NO_LIMIT);
            this.bandwidthLimitUp = BANDWIDTH_LIMIT_NO_LIMIT;
            return;
        }
        if (bandwidthLimitUp > BANDWIDTH_LIMIT_MAX) {
            LOG.debug("Unable to set bandwidth limit up gresater than {}. Using max value of {}Mbps.", BANDWIDTH_LIMIT_MAX);
            this.bandwidthLimitUp = BANDWIDTH_LIMIT_MAX;
            return;
        }
        this.bandwidthLimitUp = bandwidthLimitUp;
    }

    public Map<RadioType, RadioBasedSsidConfiguration> getRadioBasedConfigs() 
    {
        // This should never trigger. But I get so scared sometimes.
        if(radioBasedConfigs == null)
        {
            radioBasedConfigs = initRadioBasedConfig();
        }
        
        return radioBasedConfigs;
    }

    public void setRadioBasedConfigs(Map<RadioType, RadioBasedSsidConfiguration> radioBasedConfigs) {
        this.radioBasedConfigs = radioBasedConfigs;
    }

    public Boolean getEnable80211w() {
        return enable80211w;
    }

    public void setEnable80211w(Boolean enable80211w) {
        this.enable80211w = enable80211w;
    }

    public static SsidConfiguration createWithDefaults()
    {
       SsidConfiguration returnValue = new SsidConfiguration();
       return returnValue;
    }
    
    
    @Override
    public boolean needsToBeUpdatedOnDevice(SsidConfiguration obj)
    {
        return !sameStuffWillBePushedToDevice(obj);
    }
    
    
    /**
     * This is a renamed "equals" which ONLY contains the fields that would have triggered 
     * a push to the device (ie: when we change a profile name, we don't want to push).
     * 
     * @param obj
     * @return
     */
    public boolean sameStuffWillBePushedToDevice(SsidConfiguration obj) 
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SsidConfiguration other = (SsidConfiguration) obj;

        if (appliedRadios == null) {
            if (other.appliedRadios != null) {
                return false;
            }
        } else if (!appliedRadios.equals(other.appliedRadios)) {
            return false;
        }
        
        if (broadcastSsid != other.broadcastSsid) {
            return false;
        }
        if (captivePortalId == null) {
            if (other.captivePortalId != null) {
                return false;
            }
        } else if (!captivePortalId.equals(other.captivePortalId)) {
            return false;
        }
        if (bonjourGatewayProfileId == null) {
            if (other.bonjourGatewayProfileId != null) {
                return false;
            }
        } else if (!bonjourGatewayProfileId.equals(other.bonjourGatewayProfileId)) {
            return false;
        }
        if (keyRefresh == null) {
            if (other.keyRefresh != null) {
                return false;
            }
        } else if (!keyRefresh.equals(other.keyRefresh)) {
            return false;
        }
        if (keyStr == null) {
            if (other.keyStr != null) {
                return false;
            }
        } else if (!keyStr.equals(other.keyStr)) {
            return false;
        }
        if (noLocalSubnets == null) {
            if (other.noLocalSubnets != null) {
                return false;
            }
        } else if (!noLocalSubnets.equals(other.noLocalSubnets)) {
            return false;
        }
        if (radiusServiceName == null) {
            if (other.radiusServiceName != null) {
                return false;
            }
        } else if (!radiusServiceName.equals(other.radiusServiceName)) {
            return false;
        }
        if (secureMode != other.secureMode) {
            return false;
        }
        if (ssid == null) {
            if (other.ssid != null) {
                return false;
            }
        } else if (!ssid.equals(other.ssid)) {
            return false;
        }
        if (ssidAdminState != other.ssidAdminState) {
            return false;
        }
        if (vlanId == null) {
            if (other.vlanId != null) {
                return false;
            }
        } else if (!vlanId.equals(other.vlanId)) {
            return false;
        }
        if (bandwidthLimitDown == null) {
            if (other.bandwidthLimitDown != null) {
                return false;
            }
        } else if (!bandwidthLimitDown.equals(other.bandwidthLimitDown)) {
            return false;
        }
        if (bandwidthLimitUp == null) {
            if (other.bandwidthLimitUp != null) {
                return false;
            }
        } else if (!bandwidthLimitUp.equals(other.bandwidthLimitUp)) {
            return false;
        }
        else if(!Objects.equals(this.radioBasedConfigs, other.radioBasedConfigs))
        {
            return false;
        }
        else if(!Objects.equals(this.enable80211w, other.enable80211w))
        {
            return false;
        }
        if (!Objects.equals(this.wepConfig, other.wepConfig)) {
            return false;
        }
        if (!Objects.equals(this.forwardMode, other.forwardMode)) {
            return false;
        }
        if(!Objects.equals(this.videoTrafficOnly, other.videoTrafficOnly))
        {
            return false;
        }
        
        return true;
    }


    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SsidConfiguration)) {
            return false;
        }
        SsidConfiguration other = (SsidConfiguration) obj;
        if (appliedRadios == null) {
            if (other.appliedRadios != null) {
                return false;
            }
        } else if (!appliedRadios.equals(other.appliedRadios)) {
            return false;
        }

        if (broadcastSsid != other.broadcastSsid) {
            return false;
        }
        if (keyRefresh == null) {
            if (other.keyRefresh != null) {
                return false;
            }
        } else if (!keyRefresh.equals(other.keyRefresh)) {
            return false;
        }
        if (keyStr == null) {
            if (other.keyStr != null) {
                return false;
            }
        } else if (!keyStr.equals(other.keyStr)) {
            return false;
        }
        if (noLocalSubnets == null) {
            if (other.noLocalSubnets != null) {
                return false;
            }
        } else if (!noLocalSubnets.equals(other.noLocalSubnets)) {
            return false;
        }
        if (secureMode != other.secureMode) {
            return false;
        }
        if (ssid == null) {
            if (other.ssid != null) {
                return false;
            }
        } else if (!ssid.equals(other.ssid)) {
            return false;
        }
        if (ssidAdminState != other.ssidAdminState) {
            return false;
        }
        if (vlanId == null) {
            if (other.vlanId != null) {
                return false;
            }
        } else if (!vlanId.equals(other.vlanId)) {
            return false;
        }
        if (radiusServiceName == null) {
            if (other.radiusServiceName != null) {
                return false;
            }
        } else if (!radiusServiceName.equals(other.radiusServiceName)) {
            return false;
        }
        if (captivePortalId == null) {
            if (other.captivePortalId != null) {
                return false;
            }
        } else if (!captivePortalId.equals(other.captivePortalId)) {
            return false;
        }
        if (bandwidthLimitDown == null) {
            if (other.bandwidthLimitDown != null) {
                return false;
            }
        } else if (!bandwidthLimitDown.equals(other.bandwidthLimitDown)) {
            return false;
        }
        if (bandwidthLimitUp == null) {
            if (other.bandwidthLimitUp != null) {
                return false;
            }
        } else if (!bandwidthLimitUp.equals(other.bandwidthLimitUp)) {
            return false;
        } 
        else if(!Objects.equals(this.radioBasedConfigs, other.radioBasedConfigs))
        {
            return false;
        }
        
        if (!Objects.equals(this.wepConfig, other.wepConfig)) {
            return false;
        }
        
        if(!Objects.equals(this.videoTrafficOnly, other.videoTrafficOnly))
        {
            return false;
        }
        
        return true;
    }
    
    @Override
    public SsidConfiguration clone() {
        SsidConfiguration returnValue = (SsidConfiguration) super.clone();
        
        if (this.wepConfig != null) {
            returnValue.setWepConfig(this.getWepConfig().clone());
        }
        
        return returnValue;
    }
    
    public String getRadiusServiceName() {
        return radiusServiceName;
    }

    public void setRadiusServiceName(String radiusServiceName) {
        this.radiusServiceName = radiusServiceName;
    }

    public static enum SecureMode {

        open(0L),
        wpaPSK(1L),
        wpa2PSK(2L),
        wpaRadius(3L),
        wpa2Radius(4L),
        wpa2OnlyPSK(5L),
        wpa2OnlyRadius(6L),
        wep(7L),
        
        UNSUPPORTED(-1L);

        private final long id;
        private static final Map<Long, SecureMode> ELEMENTS = new HashMap<>();

        private SecureMode(long id) {
            this.id = id;
        }

        public long getId() {
            return this.id;
        }

        public static SecureMode getById(long enumId) {
            if (ELEMENTS.isEmpty()) {
                synchronized (ELEMENTS) {
                    if (ELEMENTS.isEmpty()) {
                        //initialize elements map
                        for(SecureMode met : SecureMode.values()) {
                            ELEMENTS.put(met.getId(), met);
                        }
                    }
                }
            }
            return ELEMENTS.get(enumId);
        }
        
        @JsonCreator
        public static SecureMode getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, SecureMode.class, UNSUPPORTED);
        }
        
        public static boolean isUnsupported(Object value) {
            return UNSUPPORTED.equals(value);
        }
        
        public static boolean isWPA2_Enterprise_or_Personal(SecureMode mode)
        {
            return mode == wpa2OnlyPSK || mode == wpa2OnlyRadius; 
        }
        
        
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        
        if (appliedRadios==null || appliedRadios.isEmpty() || StateSetting.isUnsupported(ssidAdminState)
                || SecureMode.isUnsupported(secureMode) || StateSetting.isUnsupported(broadcastSsid)
                || NetworkForwardMode.isUnsupported(this.forwardMode) 
                || hasUnsupportedValue(wepConfig)) {
            return true;
        }
        
        boolean hasUnsupported = false;
        for(RadioType rt: appliedRadios) {
        	if(RadioType.isUnsupported(rt)){ 
        		hasUnsupported = true;
        		break;
        	} 
        }
        
        return hasUnsupported;
    }

    private static Map<RadioType, RadioBasedSsidConfiguration> initRadioBasedConfig() {
        Map<RadioType, RadioBasedSsidConfiguration> returnValue = new EnumMap<>(RadioType.class);

        for (RadioType radioType : RadioType.validValues()) {
            returnValue.put(radioType, RadioBasedSsidConfiguration.generateDefault(radioType));
        }

        return returnValue;
    }

    /**
     * @return the bonjourGatewayProfileId
     */
    public Long getBonjourGatewayProfileId() {
        return bonjourGatewayProfileId;
    }

    /**
     * @param bonjourGatewayProfileId the bonjourGatewayProfileId to set
     */
    public void setBonjourGatewayProfileId(Long bonjourGatewayProfileId) {
        this.bonjourGatewayProfileId = bonjourGatewayProfileId;
    }

    public WepConfiguration getWepConfig() {
        return wepConfig;
    }

    public void setWepConfig(WepConfiguration wepConfig) {
        this.wepConfig = wepConfig;
    }

    /**
     * @return the forwardMode
     */
    public NetworkForwardMode getForwardMode() {
        return forwardMode;
    }

    /**
     * @param forwardMode the forwardMode to set
     */
    public void setForwardMode(NetworkForwardMode forwardMode) {
        this.forwardMode = forwardMode;
    }

    public Boolean getVideoTrafficOnly() {
        return videoTrafficOnly;
    }

    public void setVideoTrafficOnly(Boolean videoTrafficOnly) {
        this.videoTrafficOnly = videoTrafficOnly;
    }

    public void disableAllRadioConfigSettings() {
        setEnable80211w(false);
        if (getRadioBasedConfigs() != null && getRadioBasedConfigs().size() != 0) {
            for (RadioBasedSsidConfiguration radioConfig : getRadioBasedConfigs().values()) {
                radioConfig.setEnable80211k(false);
                radioConfig.setEnable80211r(false);
                radioConfig.setEnable80211v(false);
            }
        }
    }
}
