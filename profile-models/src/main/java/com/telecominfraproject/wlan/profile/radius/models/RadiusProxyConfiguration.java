package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
public class RadiusProxyConfiguration extends BaseJsonModel {
    private static final long serialVersionUID = 6946909953535655324L;

    /**
     * Proxy server floating IP address
     */
    private InetAddress floatingIpAddress;

    /**
     * Optional subnet CIDR prefix for floating IP address. Required if
     * different floating IP address is outside of the the management subnet.
     */
    private Integer floatingIfCidrPrefix;

    /**
     * Optional Gateway IP Address for the floating IP interface
     */
    private InetAddress floatingIfGwAddress;

    /**
     * Optional VLAN id for the floating IP interface
     */
    private Integer floatingIfVlan;

    /**
     * Shared secret used to communicate with the proxy server
     */
    private String sharedSecret;

    @Override
    public RadiusProxyConfiguration clone() {
        return (RadiusProxyConfiguration) super.clone();
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
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RadiusProxyConfiguration)) {
            return false;
        }
        RadiusProxyConfiguration other = (RadiusProxyConfiguration) obj;
        if (floatingIfCidrPrefix == null) {
            if (other.floatingIfCidrPrefix != null) {
                return false;
            }
        } else if (!floatingIfCidrPrefix.equals(other.floatingIfCidrPrefix)) {
            return false;
        }
        if (getFloatingIfGwAddress() == null) {
            if (other.getFloatingIfGwAddress() != null) {
                return false;
            }
        } else if (!getFloatingIfGwAddress().equals(other.getFloatingIfGwAddress())) {
            return false;
        }
        if (floatingIfVlan == null) {
            if (other.floatingIfVlan != null) {
                return false;
            }
        } else if (!floatingIfVlan.equals(other.floatingIfVlan)) {
            return false;
        }
        if (floatingIpAddress == null) {
            if (other.floatingIpAddress != null) {
                return false;
            }
        } else if (!floatingIpAddress.equals(other.floatingIpAddress)) {
            return false;
        }
        if (sharedSecret == null) {
            if (other.sharedSecret != null) {
                return false;
            }
        } else if (!sharedSecret.equals(other.sharedSecret)) {
            return false;
        }
        return true;
    }

    public Integer getFloatingIfCidrPrefix() {
        return floatingIfCidrPrefix;
    }

    public InetAddress getFloatingIfGwAddress() {
        return floatingIfGwAddress;
    }

    public Integer getFloatingIfVlan() {
        return floatingIfVlan;
    }

    public InetAddress getFloatingIpAddress() {
        return floatingIpAddress;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((floatingIfCidrPrefix == null) ? 0 : floatingIfCidrPrefix.hashCode());
        result = prime * result + ((floatingIfGwAddress == null) ? 0 : floatingIfGwAddress.hashCode());
        result = prime * result + ((floatingIfVlan == null) ? 0 : floatingIfVlan.hashCode());
        result = prime * result + ((floatingIpAddress == null) ? 0 : floatingIpAddress.hashCode());
        result = prime * result + ((sharedSecret == null) ? 0 : sharedSecret.hashCode());
        return result;
    }

    public void setFloatingIfCidrPrefix(Integer floatingIfCidrPrefix) {
        this.floatingIfCidrPrefix = floatingIfCidrPrefix;
    }

    public void setFloatingIfGwAddress(InetAddress floatingIfGwAddress) {
        this.floatingIfGwAddress = floatingIfGwAddress;
    }

    public void setFloatingIfVlan(Integer floatingIfVlan) {
        this.floatingIfVlan = floatingIfVlan;
    }

    public void setFloatingIpAddress(InetAddress floatingIpAddress) {
        this.floatingIpAddress = floatingIpAddress;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
}
