package com.telecominfraproject.wlan.client.models.events.realtime;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SecurityType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientConnectSuccessEvent extends RealTimeEvent implements HasClientMac {

    private static final long serialVersionUID = -6082134146801575193L;
    private MacAddress clientMacAddress;
    private String sessionId;
    private RadioType radioType;
    private boolean isReassociation;
    private String ssid;
    private SecurityType securityType;
    private boolean fbtUsed;
    private InetAddress ipAddr;
    private String cltId;

    private long authTs;
    private long assocTs;
    private long eapolTs;
    private long portEnabledTs;
    private long firstDataRxTs;
    private long firstDataTxTs;

    private boolean using11k;
    private boolean using11r;
    private boolean using11v;

    private long ipAcquisitionTs;
    private int assocRSSI;

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

    /**
     * @param clientMacAddress
     *            the clientMacAddress to set
     */
    public void setClientMacAddress(MacAddress macAddress) {
        this.clientMacAddress = macAddress;
    }

    /**
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     *            the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the radioType
     */
    public RadioType getRadioType() {
        return radioType;
    }

    /**
     * @param radioType
     *            the radioType to set
     */
    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    /**
     * @return the isReassociation
     */
    public boolean isReassociation() {
        return isReassociation;
    }

    /**
     * @param isReassociation
     *            the isReassociation to set
     */
    public void setReassociation(boolean isReassociation) {
        this.isReassociation = isReassociation;
    }

    /**
     * @return the ssid
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * @param ssid
     *            the ssid to set
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * @return the securityType
     */
    public SecurityType getSecurityType() {
        return securityType;
    }

    /**
     * @param securityType
     *            the securityType to set
     */
    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    /**
     * @return the fbtUsed
     */
    public boolean isFbtUsed() {
        return fbtUsed;
    }

    /**
     * @param fbtUsed
     *            the fbtUsed to set
     */
    public void setFbtUsed(boolean fbtUsed) {
        this.fbtUsed = fbtUsed;
    }

    /**
     * @return the ipAddr
     */
    public InetAddress getIpAddr() {
        return ipAddr;
    }

    /**
     * @param ipAddr
     *            the ipAddr to set
     */
    public void setIpAddr(InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }

    /**
     * @return the cltId
     */
    public String getCltId() {
        return cltId;
    }

    /**
     * @param cltId
     *            the cltId to set
     */
    public void setCltId(String cltId) {
        this.cltId = cltId;
    }

    /**
     * @return the authTs
     */
    public long getAuthTs() {
        return authTs;
    }

    /**
     * @param authTs
     *            the authTs to set
     */
    public void setAuthTs(long authTs) {
        this.authTs = authTs;
    }

    /**
     * @return the assocTs
     */
    public long getAssocTs() {
        return assocTs;
    }

    /**
     * @param assocTs
     *            the assocTs to set
     */
    public void setAssocTs(long assocTs) {
        this.assocTs = assocTs;
    }

    /**
     * @return the eapolTs
     */
    public long getEapolTs() {
        return eapolTs;
    }

    /**
     * @param eapolTs
     *            the eapolTs to set
     */
    public void setEapolTs(long eapolTs) {
        this.eapolTs = eapolTs;
    }

    /**
     * @return the portEnabledTs
     */
    public long getPortEnabledTs() {
        return portEnabledTs;
    }

    /**
     * @param portEnabledTs
     *            the portEnabledTs to set
     */
    public void setPortEnabledTs(long portEnabledTs) {
        this.portEnabledTs = portEnabledTs;
    }

    /**
     * @return the firstDataRxTs
     */
    public long getFirstDataRxTs() {
        return firstDataRxTs;
    }

    /**
     * @param firstDataRxTs
     *            the firstDataRxTs to set
     */
    public void setFirstDataRxTs(long firstDataRxTs) {
        this.firstDataRxTs = firstDataRxTs;
    }

    /**
     * @return the firstDataTxTs
     */
    public long getFirstDataTxTs() {
        return firstDataTxTs;
    }

    /**
     * @param firstDataTxTs
     *            the firstDataTxTs to set
     */
    public void setFirstDataTxTs(long firstDataTxTs) {
        this.firstDataTxTs = firstDataTxTs;
    }

    /**
     * @return the using11k
     */
    public boolean isUsing11k() {
        return using11k;
    }

    /**
     * @param using11k
     *            the using11k to set
     */
    public void setUsing11k(boolean using11k) {
        this.using11k = using11k;
    }

    /**
     * @return the using11r
     */
    public boolean isUsing11r() {
        return using11r;
    }

    /**
     * @param using11r
     *            the using11r to set
     */
    public void setUsing11r(boolean using11r) {
        this.using11r = using11r;
    }

    /**
     * @return the using11v
     */
    public boolean isUsing11v() {
        return using11v;
    }

    /**
     * @param using11v
     *            the using11v to set
     */
    public void setUsing11v(boolean using11v) {
        this.using11v = using11v;
    }

    /**
     * @return the ipAcquisitionTs
     */
    public long getIpAcquisitionTs() {
        return ipAcquisitionTs;
    }

    /**
     * @param ipAcquisitionTs
     *            the ipAcquisitionTs to set
     */
    public void setIpAcquisitionTs(long ipAcquisitionTs) {
        this.ipAcquisitionTs = ipAcquisitionTs;
    }

    /**
     * @return the assocRSSI
     */
    public int getAssocRSSI() {
        return assocRSSI;
    }

    /**
     * @param assocRSSI
     *            the assocRSSI to set
     */
    public void setAssocRSSI(int assocRSSI) {
        this.assocRSSI = assocRSSI;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    
    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(assocRSSI, assocTs, authTs, cltId, eapolTs, fbtUsed, firstDataRxTs,
                firstDataTxTs, hostName, ipAcquisitionTs, ipAddr, isReassociation, clientMacAddress, portEnabledTs, radioType,
                securityType, sessionId, ssid, using11k, using11r, using11v);
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
        ClientConnectSuccessEvent other = (ClientConnectSuccessEvent) obj;
        return assocRSSI == other.assocRSSI && assocTs == other.assocTs && authTs == other.authTs
                && Objects.equals(cltId, other.cltId) && eapolTs == other.eapolTs && fbtUsed == other.fbtUsed
                && firstDataRxTs == other.firstDataRxTs && firstDataTxTs == other.firstDataTxTs
                && Objects.equals(hostName, other.hostName) && ipAcquisitionTs == other.ipAcquisitionTs
                && Objects.equals(ipAddr, other.ipAddr) && isReassociation == other.isReassociation
                && Objects.equals(clientMacAddress, other.clientMacAddress) && portEnabledTs == other.portEnabledTs
                && radioType == other.radioType && securityType == other.securityType && sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid) && using11k == other.using11k && using11r == other.using11r
                && using11v == other.using11v;
    }

}
