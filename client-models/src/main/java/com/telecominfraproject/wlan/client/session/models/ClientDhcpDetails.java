package com.telecominfraproject.wlan.client.session.models;

import java.net.InetAddress;

import com.telecominfraproject.wlan.core.model.equipment.WiFiSessionUtility;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class ClientDhcpDetails extends BaseJsonModel {
    private static final long serialVersionUID = 7550639617951991499L;

    private InetAddress dhcpServerIp;
    private InetAddress primaryDns;
    private InetAddress secondaryDns;
    private InetAddress subnetMask;
    private InetAddress gatewayIp;
    private Long leaseStartTimestamp;
    private Integer leaseTimeInSeconds;
    private Long firstRequestTimestamp;
    private Long firstOfferTimestamp;
    private Long firstDiscoverTimestamp;
    private Long nakTimestamp;
    private boolean fromInternal = false;

    
    /**
     * Identifies the association where DHCP last occurred.
     */
    private long associationId;
    

    public ClientDhcpDetails(long sessionId) {
        this.associationId = sessionId;
    }
    
    protected ClientDhcpDetails() {
        super();
    }
    public InetAddress getDhcpServerIp() {
        return dhcpServerIp;
    }
    public void setDhcpServerIp(InetAddress dhcpServerIp) {
        this.dhcpServerIp = dhcpServerIp;
    }
    public InetAddress getPrimaryDns() {
        return primaryDns;
    }
    public void setPrimaryDns(InetAddress primaryDns) {
        this.primaryDns = primaryDns;
    }
    public InetAddress getSecondaryDns() {
        return secondaryDns;
    }
    public void setSecondaryDns(InetAddress secondaryDns) {
        this.secondaryDns = secondaryDns;
    }
    public InetAddress getSubnetMask() {
        return subnetMask;
    }
    public void setSubnetMask(InetAddress subnetMask) {
        this.subnetMask = subnetMask;
    }
    public InetAddress getGatewayIp() {
        return gatewayIp;
    }
    public void setGatewayIp(InetAddress gatewayIp) {
        this.gatewayIp = gatewayIp;
    }
    public Long getLeaseStartTimestamp() {
        return leaseStartTimestamp;
    }
    public void setLeaseStartTimestamp(Long leaseStartTimestamp) {
        this.leaseStartTimestamp = leaseStartTimestamp;
    }
    public Integer getLeaseTimeInSeconds() {
        return leaseTimeInSeconds;
    }
    public void setLeaseTimeInSeconds(Integer leaseTimeInSeconds) {
        this.leaseTimeInSeconds = leaseTimeInSeconds;
    }
    
    public Long getFirstRequestTimestamp() {
        return firstRequestTimestamp;
    }
    public void setFirstRequestTimestamp(Long firstRequestTimestamp) {
        this.firstRequestTimestamp = firstRequestTimestamp;
    }
    public Long getFirstOfferTimestamp() {
        return firstOfferTimestamp;
    }
    public void setFirstOfferTimestamp(Long firstOfferTimestamp) {
        this.firstOfferTimestamp = firstOfferTimestamp;
    }
    public Long getFirstDiscoverTimestamp() {
        return firstDiscoverTimestamp;
    }
    public void setFirstDiscoverTimestamp(Long firstDiscoverTimestamp) {
        this.firstDiscoverTimestamp = firstDiscoverTimestamp;
    }
    
    public long getAssociationId() {
        return associationId;
    }
    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }
    
    public Long getNakTimestamp() {
        return nakTimestamp;
    }

    public void setNakTimestamp(Long nakTimestamp) {
        this.nakTimestamp = nakTimestamp;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (associationId ^ (associationId >>> 32));
        result = prime * result + ((dhcpServerIp == null) ? 0 : dhcpServerIp.hashCode());
        result = prime * result + ((firstDiscoverTimestamp == null) ? 0 : firstDiscoverTimestamp.hashCode());
        result = prime * result + ((firstOfferTimestamp == null) ? 0 : firstOfferTimestamp.hashCode());
        result = prime * result + ((firstRequestTimestamp == null) ? 0 : firstRequestTimestamp.hashCode());
        result = prime * result + (fromInternal ? 1231 : 1237);
        result = prime * result + ((gatewayIp == null) ? 0 : gatewayIp.hashCode());
        result = prime * result + ((leaseStartTimestamp == null) ? 0 : leaseStartTimestamp.hashCode());
        result = prime * result + ((leaseTimeInSeconds == null) ? 0 : leaseTimeInSeconds.hashCode());
        result = prime * result + ((nakTimestamp == null) ? 0 : nakTimestamp.hashCode());
        result = prime * result + ((primaryDns == null) ? 0 : primaryDns.hashCode());
        result = prime * result + ((secondaryDns == null) ? 0 : secondaryDns.hashCode());
        result = prime * result + ((subnetMask == null) ? 0 : subnetMask.hashCode());
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
        if (!(obj instanceof ClientDhcpDetails)) {
            return false;
        }
        ClientDhcpDetails other = (ClientDhcpDetails) obj;
        if (associationId != other.associationId) {
            return false;
        }
        if (dhcpServerIp == null) {
            if (other.dhcpServerIp != null) {
                return false;
            }
        } else if (!dhcpServerIp.equals(other.dhcpServerIp)) {
            return false;
        }
        if (firstDiscoverTimestamp == null) {
            if (other.firstDiscoverTimestamp != null) {
                return false;
            }
        } else if (!firstDiscoverTimestamp.equals(other.firstDiscoverTimestamp)) {
            return false;
        }
        if (firstOfferTimestamp == null) {
            if (other.firstOfferTimestamp != null) {
                return false;
            }
        } else if (!firstOfferTimestamp.equals(other.firstOfferTimestamp)) {
            return false;
        }
        if (firstRequestTimestamp == null) {
            if (other.firstRequestTimestamp != null) {
                return false;
            }
        } else if (!firstRequestTimestamp.equals(other.firstRequestTimestamp)) {
            return false;
        }
        if (fromInternal != other.fromInternal) {
            return false;
        }
        if (gatewayIp == null) {
            if (other.gatewayIp != null) {
                return false;
            }
        } else if (!gatewayIp.equals(other.gatewayIp)) {
            return false;
        }
        if (leaseStartTimestamp == null) {
            if (other.leaseStartTimestamp != null) {
                return false;
            }
        } else if (!leaseStartTimestamp.equals(other.leaseStartTimestamp)) {
            return false;
        }
        if (leaseTimeInSeconds == null) {
            if (other.leaseTimeInSeconds != null) {
                return false;
            }
        } else if (!leaseTimeInSeconds.equals(other.leaseTimeInSeconds)) {
            return false;
        }
        if (nakTimestamp == null) {
            if (other.nakTimestamp != null) {
                return false;
            }
        } else if (!nakTimestamp.equals(other.nakTimestamp)) {
            return false;
        }
        if (primaryDns == null) {
            if (other.primaryDns != null) {
                return false;
            }
        } else if (!primaryDns.equals(other.primaryDns)) {
            return false;
        }
        if (secondaryDns == null) {
            if (other.secondaryDns != null) {
                return false;
            }
        } else if (!secondaryDns.equals(other.secondaryDns)) {
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
    
    @Override
    public ClientDhcpDetails clone() {
        ClientDhcpDetails ret = (ClientDhcpDetails) super.clone();
        return ret;
    }
    
    public void mergeDetails(ClientDhcpDetails other) {
        if(other == null) return;
        
        if(WiFiSessionUtility.decodeWiFiAssociationId(other.associationId)>WiFiSessionUtility.decodeWiFiAssociationId(associationId)) {
            // The other dhcp details are from a newer session and so everything must be reset.
            this.dhcpServerIp = null;
            this.firstDiscoverTimestamp = null;
            this.firstOfferTimestamp = null;
            this.firstRequestTimestamp = null;
            this.gatewayIp = null;
            this.leaseStartTimestamp = null;
            this.leaseTimeInSeconds = null;
            this.primaryDns = null;
            this.secondaryDns = null;
            this.subnetMask = null;
            // set the session Id to the newer session
            this.associationId = other.associationId;
            this.fromInternal = false;
        }
        else if(other.associationId != associationId) {
            // other is older, ignore it
            return;
        }
        dhcpServerIp = (InetAddress) assignOtherIfOtherNotNull(dhcpServerIp, other.dhcpServerIp);
        firstDiscoverTimestamp = assignOtherIfOtherIsLess(firstDiscoverTimestamp, other.firstDiscoverTimestamp);
        firstOfferTimestamp = assignOtherIfOtherIsLess(firstOfferTimestamp, other.firstOfferTimestamp);
        firstRequestTimestamp = assignOtherIfOtherIsLess(firstRequestTimestamp, other.firstRequestTimestamp);
        gatewayIp = (InetAddress) assignOtherIfOtherNotNull(gatewayIp, other.gatewayIp);
        leaseStartTimestamp = assignOtherIfOtherIsLess(leaseStartTimestamp, other.leaseStartTimestamp);
        primaryDns = (InetAddress) assignOtherIfOtherNotNull(primaryDns, other.primaryDns);
        secondaryDns = (InetAddress) assignOtherIfOtherNotNull(secondaryDns, other.secondaryDns);
        subnetMask = (InetAddress) assignOtherIfOtherNotNull(subnetMask, other.subnetMask);
        leaseTimeInSeconds = (Integer) assignOtherIfOtherNotNull(leaseTimeInSeconds, other.leaseTimeInSeconds);
        nakTimestamp = assignOtherIfOtherIsLess(nakTimestamp, other.nakTimestamp);
        // toggle if any of the detail has it set
        if (!this.fromInternal) {
            this.fromInternal = other.fromInternal;
        }
    }

    private static Long assignOtherIfOtherIsLess(Long curValue, Long otherValue) {
        
        if(otherValue != null && (curValue == null || curValue > otherValue)) {
            return otherValue;
        }
        return curValue;
    }

    private static Object assignOtherIfOtherNotNull(Object curValue, Object otherValue) {
       return  otherValue == null? curValue: otherValue;
    }

    /**
     * @return the fromInternal
     */
    public boolean isFromInternal() {
        return fromInternal;
    }

    /**
     * @param fromInternal the fromInternal to set
     */
    public void setFromInternal(boolean fromInternal) {
        this.fromInternal = fromInternal;
    }

}
