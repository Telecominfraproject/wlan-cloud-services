/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

import java.net.InetAddress;
import java.util.Objects;

public class DhcpAckEvent extends BaseDhcpEvent {

    private static final long serialVersionUID = -1664218986243927669L;
    private InetAddress subnetMask;
    private InetAddress primaryDns;
    private InetAddress secondaryDns;
    private Integer leaseTime;
    private Integer renewalTime;
    private Integer rebindingTime;
    private Integer timeOffset;
    private InetAddress gatewayIp;
    /**
     * Offer provided by internal server
     */
    private boolean fromInternal = false;

    public DhcpAckEvent() {
        super(0, 0, 0, null);
    }

    public DhcpAckEvent(int customerId, long equipmentId, long eventTimestamp, Long sessionId) {
        super(customerId, equipmentId, eventTimestamp, sessionId);
    }

    public InetAddress getGatewayIp() {
        return gatewayIp;
    }

    public Integer getLeaseTime() {
        return leaseTime;
    }

    public InetAddress getPrimaryDns() {
        return primaryDns;
    }

    public Integer getRebindingTime() {
        return rebindingTime;
    }

    public Integer getRenewalTime() {
        return renewalTime;
    }

    public InetAddress getSecondaryDns() {
        return secondaryDns;
    }

    public InetAddress getSubnetMask() {
        return subnetMask;
    }

    public Integer getTimeOffset() {
        return timeOffset;
    }

    /**
     * @return the fromInternal
     */
    public boolean isFromInternal() {
        return fromInternal;
    }

    /**
     * @param fromInternal
     *            the fromInternal to set
     */
    public void setFromInternal(boolean fromInternal) {
        this.fromInternal = fromInternal;
    }

    public void setGatewayIp(InetAddress gatewayIp) {
        this.gatewayIp = gatewayIp;
    }

    public void setLeaseTime(Integer leaseTime) {
        this.leaseTime = leaseTime;
    }

    public void setPrimaryDns(InetAddress primaryDns) {
        this.primaryDns = primaryDns;
    }

    public void setRebindingTime(Integer rebindingTime) {
        this.rebindingTime = rebindingTime;
    }

    public void setRenewalTime(Integer renewalTime) {
        this.renewalTime = renewalTime;
    }

    public void setSecondaryDns(InetAddress secondaryDns) {
        this.secondaryDns = secondaryDns;
    }

    public void setSubnetMask(InetAddress subnetMask) {
        this.subnetMask = subnetMask;
    }

    public void setTimeOffset(Integer timeOffset) {
        this.timeOffset = timeOffset;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(fromInternal, gatewayIp, leaseTime, primaryDns, rebindingTime,
                renewalTime, secondaryDns, subnetMask, timeOffset);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DhcpAckEvent other = (DhcpAckEvent) obj;
        return fromInternal == other.fromInternal && Objects.equals(gatewayIp, other.gatewayIp)
                && Objects.equals(leaseTime, other.leaseTime) && Objects.equals(primaryDns, other.primaryDns)
                && Objects.equals(rebindingTime, other.rebindingTime) && Objects.equals(renewalTime, other.renewalTime)
                && Objects.equals(secondaryDns, other.secondaryDns) && Objects.equals(subnetMask, other.subnetMask)
                && Objects.equals(timeOffset, other.timeOffset);
    }

    @Override
    public DhcpAckEvent clone() {
        DhcpAckEvent ret = (DhcpAckEvent) super.clone();

        return ret;
    }
}
