package com.telecominfraproject.wlan.client.session.models;

import java.net.InetAddress;
import java.util.Objects;

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
    private String associationId;
    
    public ClientDhcpDetails(String sessionId) {
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
    
    public String getAssociationId() {
        return associationId;
    }
    public void setAssociationId(String associationId) {
        this.associationId = associationId;
    }
    
    public Long getNakTimestamp() {
        return nakTimestamp;
    }

    public void setNakTimestamp(Long nakTimestamp) {
        this.nakTimestamp = nakTimestamp;
    }

    @Override
    public ClientDhcpDetails clone() {
        ClientDhcpDetails ret = (ClientDhcpDetails) super.clone();
        return ret;
    }
    
    public void mergeDetails(ClientDhcpDetails other) {
        if(other == null) return;

        if(!Objects.equals(this.associationId, other.associationId)) {
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

    @Override
    public int hashCode() {
        return Objects.hash(associationId, dhcpServerIp, firstDiscoverTimestamp, firstOfferTimestamp, firstRequestTimestamp, fromInternal, gatewayIp,
                leaseStartTimestamp, leaseTimeInSeconds, nakTimestamp, primaryDns, secondaryDns, subnetMask);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientDhcpDetails other = (ClientDhcpDetails) obj;
        return Objects.equals(associationId, other.associationId) && Objects.equals(dhcpServerIp, other.dhcpServerIp)
                && Objects.equals(firstDiscoverTimestamp, other.firstDiscoverTimestamp) && Objects.equals(firstOfferTimestamp, other.firstOfferTimestamp)
                && Objects.equals(firstRequestTimestamp, other.firstRequestTimestamp) && fromInternal == other.fromInternal
                && Objects.equals(gatewayIp, other.gatewayIp) && Objects.equals(leaseStartTimestamp, other.leaseStartTimestamp)
                && Objects.equals(leaseTimeInSeconds, other.leaseTimeInSeconds) && Objects.equals(nakTimestamp, other.nakTimestamp)
                && Objects.equals(primaryDns, other.primaryDns) && Objects.equals(secondaryDns, other.secondaryDns)
                && Objects.equals(subnetMask, other.subnetMask);
    }

}
