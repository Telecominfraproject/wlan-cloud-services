package com.telecominfraproject.wlan.client.models.events;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SecurityType;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientConnectSuccessEvent extends RealTimeEvent {
    /**
     * 
     */
    private static final long serialVersionUID = -6082134146801575193L;
    private MacAddress macAddress;
    private long sessionId;
    private RadioType radioType;
    private Boolean isReassociation;
    private String ssid;
    private SecurityType securityType;
    private Boolean fbtUsed;
    private InetAddress ipAddr;
    private String cltId;

    private Long authTs;
    private Long assocTs;
    private Long eapolTs;
    private Long portEnabledTs;
    private Long firstDataRxTs;
    private Long firstDataTxTs;

    private Boolean using11k;
    private Boolean using11r;
    private Boolean using11v;

    private Long ipAcquisitionTs;
    private Integer assocRSSI;

    /**
     * Adding this such that we can skip DHCP event decoding
     */
    private String hostName;

    public ClientConnectSuccessEvent() {
        super(RealTimeEventType.ClientConnectedSuccess, 0L);
    }

    public ClientConnectSuccessEvent(long timestamp) {
        super(RealTimeEventType.ClientConnectedSuccess, timestamp);
    }

    public MacAddress getDeviceMacAddress() {
        return this.macAddress;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.macAddress = deviceMacAddress;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public Boolean getIsReassociation() {
        return isReassociation;
    }

    public void setIsReassociation(Boolean isReassociation) {
        this.isReassociation = isReassociation;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public Boolean getFbtUsed() {
        return fbtUsed;
    }

    public void setFbtUsed(Boolean fbtUsed) {
        this.fbtUsed = fbtUsed;
    }

    public InetAddress getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getCltId() {
        return cltId;
    }

    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    public Long getAuthTs() {
        return authTs;
    }

    public void setAuthTs(Long authTs) {
        this.authTs = authTs;
    }

    public Long getAssocTs() {
        return assocTs;
    }

    public void setAssocTs(Long assocTs) {
        this.assocTs = assocTs;
    }

    public Long getEapolTs() {
        return eapolTs;
    }

    public void setEapolTs(Long eapolTs) {
        this.eapolTs = eapolTs;
    }

    public Long getPortEnabledTs() {
        return portEnabledTs;
    }

    public void setPortEnabledTs(Long portEnabledTs) {
        this.portEnabledTs = portEnabledTs;
    }

    public Long getFirstDataRxTs() {
        return firstDataRxTs;
    }

    public void setFirstDataRxTs(Long firstDataRxTs) {
        this.firstDataRxTs = firstDataRxTs;
    }

    public Long getFirstDataTxTs() {
        return firstDataTxTs;
    }

    public void setFirstDataTxTs(Long firstDataTxTs) {
        this.firstDataTxTs = firstDataTxTs;
    }

    public Boolean getUsing11k() {
        return using11k;
    }

    public void setUsing11k(Boolean using11k) {
        this.using11k = using11k;
    }

    public Boolean getUsing11r() {
        return using11r;
    }

    public void setUsing11r(Boolean using11r) {
        this.using11r = using11r;
    }

    public Boolean getUsing11v() {
        return using11v;
    }

    public void setUsing11v(Boolean using11v) {
        this.using11v = using11v;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(assocRSSI, assocTs, authTs, cltId, eapolTs, fbtUsed, firstDataRxTs,
                firstDataTxTs, hostName, ipAcquisitionTs, ipAddr, isReassociation, macAddress, portEnabledTs, radioType,
                securityType, sessionId, ssid, using11k, using11r, using11v);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientConnectSuccessEvent)) {
            return false;
        }
        ClientConnectSuccessEvent other = (ClientConnectSuccessEvent) obj;
        return Objects.equals(assocRSSI, other.assocRSSI) && Objects.equals(assocTs, other.assocTs)
                && Objects.equals(authTs, other.authTs) && Objects.equals(cltId, other.cltId)
                && Objects.equals(eapolTs, other.eapolTs) && Objects.equals(fbtUsed, other.fbtUsed)
                && Objects.equals(firstDataRxTs, other.firstDataRxTs)
                && Objects.equals(firstDataTxTs, other.firstDataTxTs) && Objects.equals(hostName, other.hostName)
                && Objects.equals(ipAcquisitionTs, other.ipAcquisitionTs) && Objects.equals(ipAddr, other.ipAddr)
                && Objects.equals(isReassociation, other.isReassociation)
                && Objects.equals(macAddress, other.macAddress) && Objects.equals(portEnabledTs, other.portEnabledTs)
                && this.radioType == other.radioType && this.securityType == other.securityType
                && this.sessionId == other.sessionId && Objects.equals(ssid, other.ssid)
                && Objects.equals(using11k, other.using11k) && Objects.equals(using11r, other.using11r)
                && Objects.equals(using11v, other.using11v);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (RadioType.isUnsupported(radioType) || SecurityType.isUnsupported(securityType)) {
            return true;
        }

        return false;
    }

    @Override
    public ClientConnectSuccessEvent clone() {
        return (ClientConnectSuccessEvent) super.clone();
    }

    public Long getIpAcquisitionTs() {
        return ipAcquisitionTs;
    }

    public void setIpAcquisitionTs(Long ipAcquisitionTs) {
        this.ipAcquisitionTs = ipAcquisitionTs;
    }

    public Integer getAssocRSSI() {
        return assocRSSI;
    }

    public void setAssocRSSI(Integer assocRSSI) {
        this.assocRSSI = assocRSSI;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

}
