package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


/**
 * @author ekeddy
 *
 */
public class RtlsSettings extends BaseJsonModel {
    private static final long serialVersionUID = -5968111668614985285L;
    private boolean enabled;
    private InetAddress srvHostIp;
    private int srvHostPort;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public InetAddress getSrvHostIp() {
        return srvHostIp;
    }

    public void setSrvHostIp(InetAddress srvHostIp) {
        this.srvHostIp = srvHostIp;
    }

    public int getSrvHostPort() {
        return srvHostPort;
    }

    public void setSrvHostPort(int srvHostPort) {
        this.srvHostPort = srvHostPort;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((srvHostIp == null) ? 0 : srvHostIp.hashCode());
        result = prime * result + srvHostPort;
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
        if (!(obj instanceof RtlsSettings)) {
            return false;
        }
        RtlsSettings other = (RtlsSettings) obj;
        if (enabled != other.enabled) {
            return false;
        }
        if (srvHostIp == null) {
            if (other.srvHostIp != null) {
                return false;
            }
        } else if (!srvHostIp.equals(other.srvHostIp)) {
            return false;
        }
        if (srvHostPort != other.srvHostPort) {
            return false;
        }
        return true;
    }

    @Override
    public RtlsSettings clone() {
        return (RtlsSettings) super.clone();
    }

}
