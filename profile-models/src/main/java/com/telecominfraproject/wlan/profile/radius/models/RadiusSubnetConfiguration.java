package com.telecominfraproject.wlan.profile.radius.models;

/**
 * Configuration for Radius for a given management subnet.
 * 
 * @author yongli
 *
 */
public class RadiusSubnetConfiguration extends BaseRadiusSubnetConfiguration {

    private static final long serialVersionUID = -6480957006396528964L;

    public RadiusSubnetConfiguration() {

    }

    public RadiusSubnetConfiguration clone() {
        RadiusSubnetConfiguration result = (RadiusSubnetConfiguration) super.clone();
        return result;
    }


    /**
     * Validate the required value is set. Throws ConfigurationException if it's
     * not valid.
     */
    @Override
    public void validateConfig() {
        super.validateConfig();
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
    
    
    
    
    
}
