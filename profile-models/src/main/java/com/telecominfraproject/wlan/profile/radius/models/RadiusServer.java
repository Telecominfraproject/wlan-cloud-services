package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;


public class RadiusServer extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = -5254789636729173278L;

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

    @Override
    public int hashCode() {
        return Objects.hash(authPort, ipAddress, secret, timeout);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadiusServer other = (RadiusServer) obj;
        return Objects.equals(authPort, other.authPort) && Objects.equals(ipAddress, other.ipAddress)
                && Objects.equals(secret, other.secret) && Objects.equals(timeout, other.timeout);
    }
    
    
}
