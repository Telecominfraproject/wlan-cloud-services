package com.telecominfraproject.wlan.profile.radius.models;

import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * Region with same set of Radius Service Configurations
 * 
 * @author yongli
 *
 */
public class RadiusServiceRegion extends BaseRadiusServerCfgGroup {
    /**
     * 
     */
    private static final long serialVersionUID = 3246854477987581637L;

    public static final String DEFALUT_REGION_NAME = "Default";

    /**
     * Named server mapping
     */
    private String regionName;

    public RadiusServiceRegion clone() {
        RadiusServiceRegion result = (RadiusServiceRegion) super.clone();
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RadiusServiceRegion)) {
            return false;
        }
        RadiusServiceRegion other = (RadiusServiceRegion) obj;
        if (regionName == null) {
            if (other.regionName != null) {
                return false;
            }
        } else if (!regionName.equals(other.regionName)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((regionName == null) ? 0 : regionName.hashCode());
        return result;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String grpName) {
        this.regionName = grpName;
    }

    /**
     * Test if the configuration is valid.
     * 
     * @throws ConfigurationException
     *             if it's invalid.
     */
    public void validateConfig() {
        if ((null == regionName) || regionName.isEmpty()) {
            // empty name
            throw new ConfigurationException("missing service zone name");
        }
        try {
            super.validateConfig();
        } catch (ConfigurationException e) {
            throw new ConfigurationException(
                    "invalid service zone configuration " + regionName + ", " + e.getLocalizedMessage(), e);
        }
    }
}
