package com.telecominfraproject.wlan.status.equipment.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * VLAN Status Data
 * 
 * @author yongli
 *
 */
public class VLANStatusData extends BaseJsonModel {
    private static final long serialVersionUID = 7673951775459216328L;
    
    private InetAddress ipBase;
    private InetAddress subnetMask;
    private InetAddress gateway;
    private InetAddress dhcpServer;
    private InetAddress dnsServer1;
    private InetAddress dnsServer2;
    private InetAddress dnsServer3;

    public VLANStatusData() {
        super();
    }

    public VLANStatusData(InetAddress ip, InetAddress subnetMask, InetAddress gateway, InetAddress dhcpServer,
            InetAddress dnsServer1, InetAddress dnsServer2) {
        super();
        this.ipBase = ip;
        this.subnetMask = subnetMask;
        this.gateway = gateway;
        this.dhcpServer = dhcpServer;
        this.dnsServer1 = dnsServer1;
        this.dnsServer2 = dnsServer2;
    }

    public VLANStatusData(InetAddress ip, InetAddress subnetMask, InetAddress gateway, InetAddress dhcpServer,
            InetAddress dnsServer1, InetAddress dnsServer2, InetAddress dnsServer3) {
        super();
        this.ipBase = ip;
        this.subnetMask = subnetMask;
        this.gateway = gateway;
        this.dhcpServer = dhcpServer;
        this.dnsServer1 = dnsServer1;
        this.dnsServer2 = dnsServer2;
        this.dnsServer3 = dnsServer3;
    }

    public VLANStatusData clone() {
        return (VLANStatusData) super.clone();
    }

    /* (non-Javadoc)
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
        if (!(obj instanceof VLANStatusData)) {
            return false;
        }
        VLANStatusData other = (VLANStatusData) obj;
        if (dhcpServer == null) {
            if (other.dhcpServer != null) {
                return false;
            }
        } else if (!dhcpServer.equals(other.dhcpServer)) {
            return false;
        }
        if (dnsServer1 == null) {
            if (other.dnsServer1 != null) {
                return false;
            }
        } else if (!dnsServer1.equals(other.dnsServer1)) {
            return false;
        }
        if (dnsServer2 == null) {
            if (other.dnsServer2 != null) {
                return false;
            }
        } else if (!dnsServer2.equals(other.dnsServer2)) {
            return false;
        }
        if (dnsServer3 == null) {
            if (other.dnsServer3 != null) {
                return false;
            }
        } else if (!dnsServer3.equals(other.dnsServer3)) {
            return false;
        }
        if (gateway == null) {
            if (other.gateway != null) {
                return false;
            }
        } else if (!gateway.equals(other.gateway)) {
            return false;
        }
        if (ipBase == null) {
            if (other.ipBase != null) {
                return false;
            }
        } else if (!ipBase.equals(other.ipBase)) {
            return false;
        }
        if (subnetMask == null) {
            if (other.subnetMask != null) {
                return false;
            }
        } else if (!subnetMask.equals(other.subnetMask)) {
            return false;
        }
        return true;
    }

    public InetAddress getDhcpServer() {
        return dhcpServer;
    }

    public InetAddress getDnsServer1() {
        return dnsServer1;
    }

    public InetAddress getDnsServer2() {
        return dnsServer2;
    }

    public InetAddress getDnsServer3() {
        return dnsServer3;
    }

    public void setDnsServer3(InetAddress dnsServer3) {
        this.dnsServer3 = dnsServer3;
    }

    public InetAddress getGateway() {
        return gateway;
    }

    public InetAddress getIpBase() {
        return ipBase;
    }

    public InetAddress getSubnetMask() {
        return subnetMask;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dhcpServer == null) ? 0 : dhcpServer.hashCode());
        result = prime * result + ((dnsServer1 == null) ? 0 : dnsServer1.hashCode());
        result = prime * result + ((dnsServer2 == null) ? 0 : dnsServer2.hashCode());
        result = prime * result + ((dnsServer3 == null) ? 0 : dnsServer3.hashCode());
        result = prime * result + ((gateway == null) ? 0 : gateway.hashCode());
        result = prime * result + ((ipBase == null) ? 0 : ipBase.hashCode());
        result = prime * result + ((subnetMask == null) ? 0 : subnetMask.hashCode());
        return result;
    }

    public void setDhcpServer(InetAddress dhcpServer) {
        this.dhcpServer = dhcpServer;
    }

    public void setDnsServer1(InetAddress dnsServer1) {
        this.dnsServer1 = dnsServer1;
    }

    public void setDnsServer2(InetAddress dnsServer2) {
        this.dnsServer2 = dnsServer2;
    }

    public void setGateway(InetAddress gateway) {
        this.gateway = gateway;
    }

    public void setIpBase(InetAddress ipBase) {
        this.ipBase = ipBase;
    }

    public void setSubnetMask(InetAddress subnetMask) {
        this.subnetMask = subnetMask;
    }
}
