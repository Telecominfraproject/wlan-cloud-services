package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;


public class RadiusServer extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = -5254789636729173278L;

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authPort == null) ? 0 : authPort.hashCode());
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        result = prime * result + ((secret == null) ? 0 : secret.hashCode());
        result = prime * result + ((timeout == null) ? 0 : timeout.hashCode());
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
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RadiusServer)) {
            return false;
        }
        RadiusServer other = (RadiusServer) obj;
        if (authPort == null) {
            if (other.authPort != null) {
                return false;
            }
        } else if (!authPort.equals(other.authPort)) {
            return false;
        }
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        if (secret == null) {
            if (other.secret != null) {
                return false;
            }
        } else if (!secret.equals(other.secret)) {
            return false;
        }
        if (timeout == null) {
            if (other.timeout != null) {
                return false;
            }
        } else if (!timeout.equals(other.timeout)) {
            return false;
        }
        return true;
    }

    private InetAddress ipAddress;
    private String secret;
    private Integer authPort;
    private Integer timeout;

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getAuthPort() {
        return authPort;
    }

    public void setAuthPort(Integer authPort) {
        this.authPort = authPort;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public RadiusServer clone() {
        return (RadiusServer) super.clone();
    }

    /**
     * Validate the configuration
     * 
     * @throws ConfigurationException
     *             if it's invalid.
     */
    public void validateConfig() {
        if ((null == ipAddress) || ipAddress.isAnyLocalAddress() || ipAddress.isLoopbackAddress()
                || ipAddress.isMulticastAddress()) {
            // invalid IP address
            throw new ConfigurationException("invalid radius server ip address " + ipAddress);
        }
        if ((null == secret) || secret.isEmpty()) {
            throw new ConfigurationException("missing shared secret");
        }
    }
}
