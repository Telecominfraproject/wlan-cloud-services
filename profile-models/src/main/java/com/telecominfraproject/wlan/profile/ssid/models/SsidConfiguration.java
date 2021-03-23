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
import com.telecominfraproject.wlan.profile.models.ProfileType;

/**
 * @author dtoptygin
 *
 */
public class SsidConfiguration extends ProfileDetails implements PushableConfiguration<SsidConfiguration> {

    private static final long serialVersionUID = -6375665507927422135L;

    private static final Logger LOG = LoggerFactory.getLogger(SsidConfiguration.class);

    /* Defaults */
    final static Integer DEFAULT_KEY_REFRESH = 0;
    final static Integer DEFAULT_VLAN = null;
    final static StateSetting DEFAULT_BROADCAST_SSID = StateSetting.enabled;
    final static Integer BANDWIDTH_LIMIT_NO_LIMIT = 0;
    final static String DEFAULT_SSID_NAME = "Default-SSID";
    
    final static Integer BANDWIDTH_LIMIT_MAX = Integer.getInteger("tip.wlan.ssid.bandwidthLimitMax", 800);
    final static Integer RADIUS_ACCOUNTING_SERVICE_INTERVAL_MIN = Integer.getInteger("tip.wlan.ssid.radiusAccountingServiceIntervalMin", 60);
    final static Integer RADIUS_ACCOUNTING_SERVICE_INTERVAL_MAX = Integer.getInteger("tip.wlan.ssid.radiusAccountingServiceIntervalMax", 600);
    public static final int MAX_SSID_LENGTH = Integer.getInteger("tip.wlan.ssid.maxSsidLength", 32);

    private String ssid;
    private Set<RadioType> appliedRadios = new HashSet<>();
    private StateSetting ssidAdminState;
    private SecureMode secureMode;
    private Integer vlanId = DEFAULT_VLAN;
    private DynamicVlanMode dynamicVlan = DynamicVlanMode.disabled;
    private String keyStr;
    private StateSetting broadcastSsid = DEFAULT_BROADCAST_SSID;
    private Integer keyRefresh = DEFAULT_KEY_REFRESH;

    private Boolean noLocalSubnets;
    private long radiusServiceId;
    private int radiusAcountingServiceInterval;

    private Long captivePortalId;

    private Integer bandwidthLimitDown;
    private Integer bandwidthLimitUp;
    private Integer clientBandwidthLimitDown;
    private Integer clientBandwidthLimitUp;
    private Boolean videoTrafficOnly;

    private Map<RadioType, RadioBasedSsidConfiguration> radioBasedConfigs;
    private Long bonjourGatewayProfileId;

    private Boolean enable80211w;

    private WepConfiguration wepConfig;

    private NetworkForwardMode forwardMode;

    private RadiusNasConfiguration radiusNasConfiguration;

    /**
     * @return the noLocalSubnets
     */
    public Boolean getNoLocalSubnets() {
        return noLocalSubnets;
    }

    /**
     * @param noLocalSubnets
     *            the noLocalSubnets to set
     */
    public void setNoLocalSubnets(Boolean noLocalSubnets) {
        this.noLocalSubnets = noLocalSubnets;
    }

    private SsidConfiguration() {
        /**
         * 
         * BATTLESTAR GALLACTICA WAS A HORRIBLE SERIES (now I have your
         * attention)
         * 
         * SUPER IMPORTANT NOTICE:
         * 
         * If you modify these values, make sure you update the timestamp in
         * DefaultableConfiguration otherwise they won't be downloaded when an
         * AP reconnects.
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
        setClientBandwidthLimitDown(BANDWIDTH_LIMIT_NO_LIMIT);
        setBandwidthLimitUp(BANDWIDTH_LIMIT_NO_LIMIT);
        setClientBandwidthLimitUp(BANDWIDTH_LIMIT_NO_LIMIT);
        setRadiusServiceId(0L);
        setForwardMode(forwardMode);
        radioBasedConfigs = initRadioBasedConfig();
        setVideoTrafficOnly(false);
        radiusNasConfiguration = RadiusNasConfiguration.createWithDefaults();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.ssid;
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

    public Integer getKeyRefresh() {
        return this.keyRefresh;
    }

    public void setKeyRefresh(Integer refresh) {
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

    public StateSetting getBroadcastSsid() {
        return this.broadcastSsid;
    }

    public void setBroadcastSsid(StateSetting bSsid) {
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
            LOG.debug("Unable to set bandwidth limit down less than {}. Using default value of NO LIMIT.",
                    BANDWIDTH_LIMIT_NO_LIMIT);
            this.bandwidthLimitDown = BANDWIDTH_LIMIT_NO_LIMIT;
            return;
        }
        if (bandwidthLimitDown > BANDWIDTH_LIMIT_MAX) {
            LOG.debug("Unable to set bandwidth limit down greater than {}. Using max value of {} Mbps.",
                    BANDWIDTH_LIMIT_MAX, BANDWIDTH_LIMIT_MAX);
            this.bandwidthLimitDown = BANDWIDTH_LIMIT_MAX;
            return;
        }

        this.bandwidthLimitDown = bandwidthLimitDown;
    }

    public Integer getClientBandwidthLimitDown() {
        return clientBandwidthLimitDown;
    }

    public void setClientBandwidthLimitDown(Integer clientBandwidthLimitDown) {

        if (clientBandwidthLimitDown < BANDWIDTH_LIMIT_NO_LIMIT) {
            LOG.debug("Unable to set client download bandwidth to less than {}. Using default value of NO LIMIT.",
                    BANDWIDTH_LIMIT_NO_LIMIT);
            this.clientBandwidthLimitDown = BANDWIDTH_LIMIT_NO_LIMIT;
            return;
        }

        if (clientBandwidthLimitDown > getBandwidthLimitDown()) {
            LOG.debug(
                    "Unable to set client download bandwidth greater than the Profile's download bandwith limit {}. Using max value of {} Mbps.",
                    getBandwidthLimitDown(), getBandwidthLimitDown());
            this.clientBandwidthLimitDown = getBandwidthLimitDown();
            return;
        }

        this.clientBandwidthLimitDown = clientBandwidthLimitDown;
    }

    public Integer getBandwidthLimitUp() {
        return bandwidthLimitUp;
    }

    public void setBandwidthLimitUp(Integer bandwidthLimitUp) {
        if (bandwidthLimitUp < BANDWIDTH_LIMIT_NO_LIMIT) {
            LOG.debug("Unable to set bandwidth limit up less than {}. Using default value of NO LIMIT.",
                    BANDWIDTH_LIMIT_NO_LIMIT);
            this.bandwidthLimitUp = BANDWIDTH_LIMIT_NO_LIMIT;
            return;
        }
        if (bandwidthLimitUp > BANDWIDTH_LIMIT_MAX) {
            LOG.debug("Unable to set bandwidth limit up greater than {}. Using max value of {} Mbps.",
                    BANDWIDTH_LIMIT_MAX, BANDWIDTH_LIMIT_MAX);
            this.bandwidthLimitUp = BANDWIDTH_LIMIT_MAX;
            return;
        }
        this.bandwidthLimitUp = bandwidthLimitUp;
    }

    public Integer getClientBandwidthLimitUp() {
        return clientBandwidthLimitUp;
    }

    public void setClientBandwidthLimitUp(Integer clientBandwidthLimitUp) {
        if (clientBandwidthLimitUp < BANDWIDTH_LIMIT_NO_LIMIT) {
            LOG.debug("Unable to set client upload bandwidth to less than {}. Using default value of NO LIMIT.",
                    BANDWIDTH_LIMIT_NO_LIMIT);
            this.clientBandwidthLimitUp = BANDWIDTH_LIMIT_NO_LIMIT;
            return;
        }

        if (clientBandwidthLimitUp > getBandwidthLimitUp()) {
            LOG.debug(
                    "Unable to set client upload bandwidth greater than the Profile's upload bandwidth limit {}. Using max value of {} Mbps.",
                    getBandwidthLimitUp(), getBandwidthLimitUp());
            this.clientBandwidthLimitUp = getBandwidthLimitUp();
            return;
        }

        this.clientBandwidthLimitUp = clientBandwidthLimitUp;
    }

    public Map<RadioType, RadioBasedSsidConfiguration> getRadioBasedConfigs() {
        // This should never trigger. But I get so scared sometimes.
        if (radioBasedConfigs == null) {
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

    public static SsidConfiguration createWithDefaults() {
        SsidConfiguration returnValue = new SsidConfiguration();
        return returnValue;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(SsidConfiguration obj) {
        return !Objects.equals(this, obj);
    }

    public long getRadiusServiceId() {
        return radiusServiceId;
    }

    public void setRadiusServiceId(long radiusServiceId) {
        this.radiusServiceId = radiusServiceId;
    }

    public Integer getRadiusAcountingServiceInterval() {
        return radiusAcountingServiceInterval;
    }

    public void setRadiusAcountingServiceInterval(int radiusAcountingServiceInterval) {
        if (radiusAcountingServiceInterval == 0) {
            // toggles 'off' and lets Radius set this value
            this.radiusAcountingServiceInterval = 0;
        } else if (radiusAcountingServiceInterval > RADIUS_ACCOUNTING_SERVICE_INTERVAL_MAX) {
            LOG.info("Unable to set radius accounting service interval to greater than {}. Using max value of {}.",
                    RADIUS_ACCOUNTING_SERVICE_INTERVAL_MAX, RADIUS_ACCOUNTING_SERVICE_INTERVAL_MAX);
            this.radiusAcountingServiceInterval = RADIUS_ACCOUNTING_SERVICE_INTERVAL_MAX;
        } else if (radiusAcountingServiceInterval < RADIUS_ACCOUNTING_SERVICE_INTERVAL_MIN) {
            LOG.info("Unable to set radius accounting service interval to less than {}. Using min value of {}.",
                    RADIUS_ACCOUNTING_SERVICE_INTERVAL_MIN, RADIUS_ACCOUNTING_SERVICE_INTERVAL_MIN);
            this.radiusAcountingServiceInterval = RADIUS_ACCOUNTING_SERVICE_INTERVAL_MIN;
        } else {
            this.radiusAcountingServiceInterval = radiusAcountingServiceInterval;
        }
    }

    /**
     * @return the radiusClientConfiguration
     */
    public RadiusNasConfiguration getRadiusClientConfiguration() {
        return radiusNasConfiguration;
    }

    /**
     * @param radiusClientConfiguration
     *            the radiusClientConfiguration to set
     */
    public void setRadiusClientConfiguration(RadiusNasConfiguration radiusClientConfiguration) {
        this.radiusNasConfiguration = radiusClientConfiguration;
    }

    public static enum SecureMode {

        open(0L), wpaPSK(1L), wpa2PSK(2L), wpaRadius(3L), wpa2Radius(4L), wpa2OnlyPSK(5L), wpa2OnlyRadius(6L), wep(
                7L), wpaEAP(8L), wpa2EAP(
                        9L), wpa2OnlyEAP(10L), wpa3OnlySAE(11L), wpa3MixedSAE(12L), wpa3OnlyEAP(13L), wpa3MixedEAP(14L),

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
                        // initialize elements map
                        for (SecureMode met : SecureMode.values()) {
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

        public static boolean isWPA2_Enterprise_or_Personal(SecureMode mode) {
            return mode == wpa2OnlyPSK || mode == wpa2OnlyRadius;
        }

        public static boolean isWPA3_Enterprise_or_Personal(SecureMode mode) {
            return mode == wpa3OnlySAE || mode == wpa3OnlyEAP;
        }

    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (appliedRadios == null || appliedRadios.isEmpty() || StateSetting.isUnsupported(ssidAdminState)
                || SecureMode.isUnsupported(secureMode) || StateSetting.isUnsupported(broadcastSsid)
                || NetworkForwardMode.isUnsupported(this.forwardMode) || hasUnsupportedValue(wepConfig)) {
            return true;
        }

        boolean hasUnsupported = false;
        for (RadioType rt : appliedRadios) {
            if (RadioType.isUnsupported(rt)) {
                hasUnsupported = true;
                break;
            }
        }
        
        if (ssid != null  && ssid.length() > MAX_SSID_LENGTH)
        {
            String msg = String.format("given SSID: %s of length: %2d exceeds the maximum character limit of %2d.", ssid, ssid.length(), MAX_SSID_LENGTH);
            LOG.error(msg);
            throw new SsidExceedsMaxLengthException(msg);
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
     * @param bonjourGatewayProfileId
     *            the bonjourGatewayProfileId to set
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
     * @param forwardMode
     *            the forwardMode to set
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

    public DynamicVlanMode getDynamicVlan() {
        return dynamicVlan;
    }

    public void setDynamicVlan(DynamicVlanMode dynamicVlan) {
        this.dynamicVlan = dynamicVlan;
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

    @Override
    public SsidConfiguration clone() {
        SsidConfiguration returnValue = (SsidConfiguration) super.clone();

        if (this.wepConfig != null) {
            returnValue.setWepConfig(this.getWepConfig().clone());
        }

        if (this.radioBasedConfigs != null) {
            returnValue.setRadioBasedConfigs(new HashMap<>(this.getRadioBasedConfigs()));
        }

        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appliedRadios, bandwidthLimitDown, bandwidthLimitUp, bonjourGatewayProfileId, broadcastSsid,
                captivePortalId, clientBandwidthLimitDown, clientBandwidthLimitUp, enable80211w, forwardMode,
                keyRefresh, keyStr, noLocalSubnets, radioBasedConfigs, 
                radiusAcountingServiceInterval, radiusNasConfiguration, radiusServiceId, secureMode, ssid,
                ssidAdminState, videoTrafficOnly, vlanId, wepConfig);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SsidConfiguration other = (SsidConfiguration) obj;
        return Objects.equals(appliedRadios, other.appliedRadios)
                && Objects.equals(bandwidthLimitDown, other.bandwidthLimitDown)
                && Objects.equals(bandwidthLimitUp, other.bandwidthLimitUp)
                && Objects.equals(bonjourGatewayProfileId, other.bonjourGatewayProfileId)
                && broadcastSsid == other.broadcastSsid && Objects.equals(captivePortalId, other.captivePortalId)
                && Objects.equals(clientBandwidthLimitDown, other.clientBandwidthLimitDown)
                && Objects.equals(clientBandwidthLimitUp, other.clientBandwidthLimitUp)
                && Objects.equals(enable80211w, other.enable80211w) && forwardMode == other.forwardMode
                && Objects.equals(keyRefresh, other.keyRefresh) && Objects.equals(keyStr, other.keyStr)
                && Objects.equals(noLocalSubnets, other.noLocalSubnets)
                && Objects.equals(radioBasedConfigs, other.radioBasedConfigs)
                && radiusAcountingServiceInterval == other.radiusAcountingServiceInterval
                && Objects.equals(radiusNasConfiguration, other.radiusNasConfiguration)
                && radiusServiceId == other.radiusServiceId && secureMode == other.secureMode
                && Objects.equals(ssid, other.ssid) && ssidAdminState == other.ssidAdminState
                && Objects.equals(videoTrafficOnly, other.videoTrafficOnly) && Objects.equals(vlanId, other.vlanId)
                && Objects.equals(wepConfig, other.wepConfig);
    }  

    
}
