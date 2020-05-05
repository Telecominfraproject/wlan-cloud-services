package com.telecominfraproject.wlan.profile.radius.models;

import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * Configuration for Radius for a given management subnet.
 * 
 * @author yongli
 *
 */
public class RadiusSubnetConfiguration extends BaseRadiusSubnetConfiguration {

    private static final long serialVersionUID = -6480957006396528964L;

    /**
     * RADIUS service region. Required.
     */
    private String serviceRegionName;

    public RadiusSubnetConfiguration() {

    }

    public RadiusSubnetConfiguration clone() {
        RadiusSubnetConfiguration result = (RadiusSubnetConfiguration) super.clone();
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
        if (!(obj instanceof RadiusSubnetConfiguration)) {
            return false;
        }
        RadiusSubnetConfiguration other = (RadiusSubnetConfiguration) obj;
        if (getServiceRegionName() == null) {
            if (other.getServiceRegionName() != null) {
                return false;
            }
        } else if (!getServiceRegionName().equals(other.getServiceRegionName())) {
            return false;
        }
        return true;
    }

    public String getServiceRegionName() {
        return serviceRegionName;
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
        result = prime * result + ((getServiceRegionName() == null) ? 0 : getServiceRegionName().hashCode());
        return result;
    }

    public void setServiceRegion(String regionName) {
        this.serviceRegionName = regionName;
    }

    /**
     * Validate the required value is set. Throws ConfigurationException if it's
     * not valid.
     */
    @Override
    public void validateConfig() {
        super.validateConfig();
        if ((null == this.getServiceRegionName()) || (this.getServiceRegionName().isEmpty())) {
            throw new ConfigurationException("missing service region name");
        }
    }

    /**
     * Return printable subnet address
     * 
     * @return printable string with name[a.b.c.d/z]
     */
    public String showSubnetString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getSubnetName());
        sb.append('[');
        if (null != this.getSubnetAddress()) {
            sb.append(this.getSubnetAddress().getHostAddress());
        }
        else {
            sb.append(this.getSubnetAddress());
        }
        sb.append('/');
        sb.append(this.getSubnetCidrPrefix());
        sb.append(']');
        return sb.toString();
    }
}
