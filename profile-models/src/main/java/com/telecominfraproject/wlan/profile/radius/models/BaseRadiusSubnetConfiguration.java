package com.telecominfraproject.wlan.profile.radius.models;

import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * Configuration for Radius for a given management subnet.
 * 
 * @author yongli
 *
 */
public class BaseRadiusSubnetConfiguration extends BaseSubnetConfiguration {
    private static final long serialVersionUID = 8948068661293002098L;
    
    /**
     * Minimum value to which the probeInterval value may be set.
     */
    private static final int MINIMUM_PROBE_INTERVAL = 60;
    /**
     * Maximum value to which the probeInterval value may be set.
     */
    private static final int MAXIMUM_PROBE_INTERVAL = 600;
    
    /**
     * Value which when set as the probeInterval value will disable 
     * the probe feature for this Subnet.
     */
    private static final int PROBE_DISABLED = 0;
    
    /**
     * name for this subnet
     */
    private String subnetName;

    /**
     * RADIUS proxy configuration, optional
     */
    private RadiusProxyConfiguration proxyConfig;

    /**
     * Synthentic client probing internval (seconds). 
     * Use 0 or null for disable.
     */
    private Integer probeInterval;
    
    public BaseRadiusSubnetConfiguration(BaseRadiusSubnetConfiguration subnetConfig) {
        super(subnetConfig);
        this.subnetName = subnetConfig.subnetName;
        this.proxyConfig = subnetConfig.proxyConfig;
        this.probeInterval = subnetConfig.probeInterval;
    }

    /**
     * Hide constructor
     */
    protected BaseRadiusSubnetConfiguration() {

    }

    @Override
    public BaseRadiusSubnetConfiguration clone() {
        BaseRadiusSubnetConfiguration result = (BaseRadiusSubnetConfiguration) super.clone();
        if (null != this.proxyConfig) {
            result.proxyConfig = this.proxyConfig.clone();
        }
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
        if (!(obj instanceof BaseRadiusSubnetConfiguration)) {
            return false;
        }
        BaseRadiusSubnetConfiguration other = (BaseRadiusSubnetConfiguration) obj;
        if (probeInterval == null) {
            if (other.probeInterval != null) {
                return false;
            }
        } else if (!probeInterval.equals(other.probeInterval)) {
            return false;
        }
        if (proxyConfig == null) {
            if (other.proxyConfig != null) {
                return false;
            }
        } else if (!proxyConfig.equals(other.proxyConfig)) {
            return false;
        }
        if (subnetName == null) {
            if (other.subnetName != null) {
                return false;
            }
        } else if (!subnetName.equals(other.subnetName)) {
            return false;
        }
        return true;
    }

    /**
     * @return the probeInterval
     */
    public Integer getProbeInterval() {
        return probeInterval;
    }

    /**
     * @return the proxyConfig
     */
    public RadiusProxyConfiguration getProxyConfig() {
        return proxyConfig;
    }

    public String getSubnetName() {
        return subnetName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((probeInterval == null) ? 0 : probeInterval.hashCode());
        result = prime * result + ((proxyConfig == null) ? 0 : proxyConfig.hashCode());
        result = prime * result + ((subnetName == null) ? 0 : subnetName.hashCode());
        return result;
    }

    /**
     * @param probeInterval the probeInterval to set
     */
    public void setProbeInterval(Integer probeInterval) {
        this.probeInterval = probeInterval;
    }

    /**
     * @param proxyConfig
     *            the proxyConfig to set
     */
    public void setProxyConfig(RadiusProxyConfiguration proxyConfig) {
        this.proxyConfig = proxyConfig;
    }
    

    public void setSubnetName(String subnetName) {
        this.subnetName = subnetName;
    }

    /**
     * Validate the required value is set. Throws ConfigurationException if it's
     * not valid.
     */
    public void validateConfig() {
        if (null == this.subnetName || this.subnetName.isEmpty()) {
            throw new ConfigurationException("missing subnet name");
        }
        if (this.probeInterval != null && this.probeInterval != PROBE_DISABLED && 
                (this.probeInterval < MINIMUM_PROBE_INTERVAL || this.probeInterval > MAXIMUM_PROBE_INTERVAL)) {
            throw new ConfigurationException("Invalid probe interval. Probe interval must be either " + 
                PROBE_DISABLED + " to disable probes or a value between " + MINIMUM_PROBE_INTERVAL +
                " and " + MAXIMUM_PROBE_INTERVAL + ".");
        }
        super.validateConfig();
    }
}
