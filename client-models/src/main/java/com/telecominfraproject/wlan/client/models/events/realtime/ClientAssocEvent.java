package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.client.models.events.utils.WlanStatusCode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientAssocEvent extends RealTimeEvent implements HasClientMac {
    private static final long serialVersionUID = 7015822981315570338L;

    private long sessionId;
    private String ssid;
    private MacAddress clientMacAddress;
    private RadioType radioType;
    private boolean isReassociation;
    private WlanStatusCode status;
    private int rssi;
    private int internalSC;
    private boolean using11k;
    private boolean using11r;
    private boolean using11v;

    public ClientAssocEvent() {
        // serialization
        
    }

    public ClientAssocEvent(int customerId, long locationId, long equipmentId, long timestamp, long sessionId, String ssid,
            MacAddress clientMacAddress, RadioType radioType, boolean isReassociation, WlanStatusCode status,
            Integer internalSC, Integer rssi) {
        super(RealTimeEventType.STA_Client_Assoc, customerId, locationId, equipmentId, timestamp);
        this.sessionId = sessionId;
        this.ssid = ssid;
        setClientMacAddress(clientMacAddress);
        this.radioType = radioType;
        this.isReassociation = isReassociation;
        this.status = status;
        this.internalSC = internalSC;
        this.rssi = rssi;
    }

    public ClientAssocEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Assoc, timestamp);
    }

    /**
     * @return the sessionId
     */
    public long getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     *            the sessionId to set
     */
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
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

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    /**
     * @param clientMacAddress
     *            the clientMacAddress to set
     */
    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
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
     * @return the status
     */
    public WlanStatusCode getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(WlanStatusCode status) {
        this.status = status;
    }

    /**
     * @return the rssi
     */
    public int getRssi() {
        return rssi;
    }

    /**
     * @param rssi
     *            the rssi to set
     */
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    /**
     * @return the internalSC
     */
    public int getInternalSC() {
        return internalSC;
    }

    /**
     * @param internalSC
     *            the internalSC to set
     */
    public void setInternalSC(int internalSC) {
        this.internalSC = internalSC;
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

    @Override
    public ClientAssocEvent clone() {
        return (ClientAssocEvent) super.clone();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(clientMacAddress, internalSC, isReassociation, radioType, rssi,
                sessionId, ssid, status, using11k, using11r, using11v);
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
        ClientAssocEvent other = (ClientAssocEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && internalSC == other.internalSC
                && isReassociation == other.isReassociation && radioType == other.radioType && rssi == other.rssi
                && sessionId == other.sessionId && Objects.equals(ssid, other.ssid)
                && Objects.equals(status, other.status) && using11k == other.using11k && using11r == other.using11r
                && using11v == other.using11v;
    }

   

}
