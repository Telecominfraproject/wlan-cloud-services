package com.telecominfraproject.wlan.client.session.models;

import java.net.InetAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SecurityType;
import com.telecominfraproject.wlan.core.model.equipment.SteerType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class ClientSessionDetails extends BaseJsonModel {

    /*
     * 5 minutes for a session to go stale on cloud side
     */
    private static final long CLOUD_SESSION_TIMEOUT = TimeUnit.MINUTES.toMillis(5);

    private static final long serialVersionUID = -7714023056859882994L;

    private String sessionId;
    private Long authTimestamp;
    private Long assocTimestamp;
    private Integer assocInternalSC;
    private Long ipTimestamp;
    private Long disconnectByApTimestamp;
    private Long disconnectByClientTimestamp;
    private Long timeoutTimestamp;
    private Long firstDataSentTimestamp;
    private Long firstDataRcvdTimestamp;
    private InetAddress ipAddress;
    private String radiusUsername;
    private String ssid;
    private RadioType radioType;
    private Long lastEventTimestamp;
    private String hostname;
    private String apFingerprint;
    private String userAgentStr;
    private Long lastRxTimestamp;
    private Long lastTxTimestamp;
    private String cpUsername;
    private ClientDhcpDetails dhcpDetails;
    private ClientEapDetails eapDetails;
    private ClientSessionMetricDetails metricDetails;
    private Boolean isReassociation;
    private Integer disconnectByApReasonCode;
    private Integer disconnectByClientReasonCode;
    private Integer disconnectByApInternalReasonCode;
    private Integer disconnectByClientInternalReasonCode;
    private Long portEnabledTimestamp;
    private Boolean is11RUsed;
    private Boolean is11KUsed;
    private Boolean is11VUsed;
    private SecurityType securityType;
    private SteerType steerType;
    private String previousValidSessionId;
    private ClientFailureDetails lastFailureDetails;
    private ClientFailureDetails firstFailureDetails;
    private Integer associationStatus;
    private Integer dynamicVlan;
    private Integer assocRssi;
    private String priorSessionId;
    private Long priorEquipmentId;
    private String classificationName;
    private AssociationState associationState;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getAuthTimestamp() {
        return authTimestamp;
    }

    public void setAuthTimestamp(Long authTimestamp) {
        this.authTimestamp = authTimestamp;
    }

    public Long getAssocTimestamp() {
        return assocTimestamp;
    }

    public void setAssocTimestamp(Long assocTimestamp) {
        this.assocTimestamp = assocTimestamp;
    }

    public Integer getAssocInternalSC() {
        return assocInternalSC;
    }

    public void setAssocInternalSC(Integer assocInternalSC) {
        this.assocInternalSC = assocInternalSC;
    }

    public Long getTimeoutTimestamp() {
        return timeoutTimestamp;
    }

    public void setTimeoutTimestamp(Long timeoutTimestamp) {
        this.timeoutTimestamp = timeoutTimestamp;
    }

    public Long getFirstDataSentTimestamp() {
        return firstDataSentTimestamp;
    }

    public void setFirstDataSentTimestamp(Long firstDataSentTimestamp) {
        this.firstDataSentTimestamp = firstDataSentTimestamp;
    }

    public Long getFirstDataRcvdTimestamp() {
        return firstDataRcvdTimestamp;
    }

    public void setFirstDataRcvdTimestamp(Long firstDataRcvdTimestamp) {
        this.firstDataRcvdTimestamp = firstDataRcvdTimestamp;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRadiusUsername() {
        return radiusUsername;
    }

    public void setRadiusUsername(String radiusUsername) {
        this.radiusUsername = radiusUsername;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public Long getLastEventTimestamp() {
        if (getMetricDetails() != null) {
            if (lastEventTimestamp != null) {
                return Math.max(getMetricDetails().getLastMetricTimestamp(), lastEventTimestamp);
            }
            return getMetricDetails().getLastMetricTimestamp();
        }
        return lastEventTimestamp;
    }

    public void setLastEventTimestamp(Long lastEventTimestamp) {
        this.lastEventTimestamp = lastEventTimestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Long getIpTimestamp() {
        return ipTimestamp;
    }

    public void setIpTimestamp(Long ipTimestamp) {
        this.ipTimestamp = ipTimestamp;
    }

    public String getApFingerprint() {
        return apFingerprint;
    }

    public void setApFingerprint(String apFingerprint) {
        this.apFingerprint = apFingerprint;
    }

    public Long getLastRxTimestamp() {
        if (lastRxTimestamp != null && getMetricDetails() != null && getMetricDetails().getLastRxTimestamp() != null) {
            return Math.max(lastRxTimestamp, getMetricDetails().getLastRxTimestamp());
        } else if (lastRxTimestamp != null) {
            return lastRxTimestamp;
        }

        return getMetricDetails() == null ? null : getMetricDetails().getLastRxTimestamp();
    }

    public void setLastRxTimestamp(Long lastRxTimestamp) {
        this.lastRxTimestamp = lastRxTimestamp;
    }

    public Long getLastTxTimestamp() {
        if (lastTxTimestamp != null && getMetricDetails() != null && getMetricDetails().getLastTxTimestamp() != null) {
            return Math.max(lastTxTimestamp, getMetricDetails().getLastTxTimestamp());
        } else if (lastTxTimestamp != null) {
            return lastTxTimestamp;
        }
        return getMetricDetails() == null ? null : getMetricDetails().getLastTxTimestamp();
    }

    public void setLastTxTimestamp(Long lastTxTimestamp) {
        this.lastTxTimestamp = lastTxTimestamp;
    }

    /**
     * @return the cpUsername
     */
    public String getCpUsername() {
        return cpUsername;
    }

    /**
     * @param cpUsername
     *            the cpUsername to set
     */
    public void setCpUsername(String cpUsername) {
        this.cpUsername = cpUsername;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public ClientDhcpDetails getDhcpDetails() {
        return dhcpDetails;
    }

    public void setDhcpDetails(ClientDhcpDetails dhcpDetails) {
        this.dhcpDetails = dhcpDetails;
    }

    public ClientEapDetails getEapDetails() {
        return eapDetails;
    }

    public void setEapDetails(ClientEapDetails eapDetails) {
        this.eapDetails = eapDetails;
    }

    public ClientSessionMetricDetails getMetricDetails() {
        return metricDetails;
    }

    public void setMetricDetails(ClientSessionMetricDetails metricDetails) {
        this.metricDetails = metricDetails;
    }

    /**
     * RADIUS 802.1x EAP_Success timestamp
     * 
     * @return
     */
    public Long getEapSuccessTimestamp() {
        return this.eapDetails == null ? null : eapDetails.getEapSuccessTimestamp();
    }

    @Deprecated
    public void setEapSuccessTimestamp(Long eapSuccessTimestamp) {
        if (eapSuccessTimestamp != null) {
            createEapDetailsIfNull();
            this.eapDetails.setEapSuccessTimestamp(eapSuccessTimestamp);
        }
    }

    public Long getEapKey4Timestamp() {
        return this.eapDetails == null ? null : eapDetails.getEapKey4Timestamp();
    }

    @Deprecated
    public void setEapKey4Timestamp(Long eapKey4Timestamp) {
        if (eapKey4Timestamp != null) {
            createEapDetailsIfNull();
            this.eapDetails.setEapKey4Timestamp(eapKey4Timestamp);
        }
    }

    public Boolean getIsReassociation() {
        return isReassociation;
    }

    public void setIsReassociation(Boolean isReassociation) {
        this.isReassociation = isReassociation;
    }

    @JsonIgnore
    public Boolean getDisconnectedByClient() {
        return disconnectByClientReasonCode != null;
    }

    public Long getDisconnectByApTimestamp() {
        return disconnectByApTimestamp;
    }

    public void setDisconnectByApTimestamp(Long disconnectByApTimestamp) {
        this.disconnectByApTimestamp = disconnectByApTimestamp;
    }

    public Long getDisconnectByClientTimestamp() {
        return disconnectByClientTimestamp;
    }

    public void setDisconnectByClientTimestamp(Long disconnectByClientTimestamp) {
        this.disconnectByClientTimestamp = disconnectByClientTimestamp;
    }

    @JsonIgnore
    public Long getDisconnectTimestamp() {
        if (disconnectByClientTimestamp == null) {
            return disconnectByApTimestamp;
        } else if (disconnectByApTimestamp == null) {
            return disconnectByClientTimestamp;
        }
        return Math.min(disconnectByClientTimestamp, disconnectByApTimestamp);
    }

    public Integer getDisconnectByApReasonCode() {
        return disconnectByApReasonCode;
    }

    public void setDisconnectByApReasonCode(Integer disconnectByApReasonCode) {
        this.disconnectByApReasonCode = disconnectByApReasonCode;
    }

    public Integer getDisconnectByClientReasonCode() {
        return disconnectByClientReasonCode;
    }

    public void setDisconnectByClientReasonCode(Integer disconnectByClientReasonCode) {
        this.disconnectByClientReasonCode = disconnectByClientReasonCode;
    }

    public Integer getDisconnectByApInternalReasonCode() {
        return disconnectByApInternalReasonCode;
    }

    public void setDisconnectByApInternalReasonCode(Integer disconnectByApInternalReasonCode) {
        this.disconnectByApInternalReasonCode = disconnectByApInternalReasonCode;
    }

    public Integer getDisconnectByClientInternalReasonCode() {
        return disconnectByClientInternalReasonCode;
    }

    public void setDisconnectByClientInternalReasonCode(Integer disconnectByClientInternalReasonCode) {
        this.disconnectByClientInternalReasonCode = disconnectByClientInternalReasonCode;
    }

    public Long getPortEnabledTimestamp() {
        return portEnabledTimestamp;
    }

    public void setPortEnabledTimestamp(Long portEnabledTimestamp) {
        this.portEnabledTimestamp = portEnabledTimestamp;
    }

    public Boolean getIs11RUsed() {
        return is11RUsed;
    }

    public void setIs11RUsed(Boolean is11rUsed) {
        is11RUsed = is11rUsed;
    }

    public Boolean getIs11KUsed() {
        return is11KUsed;
    }

    public void setIs11KUsed(Boolean is11kUsed) {
        is11KUsed = is11kUsed;
    }

    public Boolean getIs11VUsed() {
        return is11VUsed;
    }

    public void setIs11VUsed(Boolean is11vUsed) {
        is11VUsed = is11vUsed;
    }

    public SecurityType getSecurityType() {
        return securityType;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType;
    }

    public SteerType getSteerType() {
        return steerType;
    }

    public void setSteerType(SteerType steerType) {
        this.steerType = steerType;
    }

    public String getPreviousValidSessionId() {
        return previousValidSessionId;
    }

    public void setPreviousValidSessionId(String previousValidSessionId) {
        this.previousValidSessionId = previousValidSessionId;
    }

    public ClientFailureDetails getLastFailureDetails() {
        return lastFailureDetails;
    }

    public void setLastFailureDetails(ClientFailureDetails lastFailureDetails) {
        this.lastFailureDetails = lastFailureDetails;
    }

    public ClientFailureDetails getFirstFailureDetails() {
        return firstFailureDetails;
    }

    public void setFirstFailureDetails(ClientFailureDetails firstFailureDetails) {
        this.firstFailureDetails = firstFailureDetails;
    }

    @JsonIgnore
    public boolean isTerminated() {
        return this.getDisconnectTimestamp() != null || this.timeoutTimestamp != null;
    }

    @JsonIgnore
    public Long getSessionStart() {
        Long ret = authTimestamp;
        if (ret == null) {
            ret = assocTimestamp;
        }
        if (ret == null) {
            ret = ipTimestamp;
        }

        if (ret == null) {
            if (firstDataRcvdTimestamp == null) {
                ret = firstDataSentTimestamp;
            } else if (firstDataSentTimestamp == null) {
                ret = firstDataRcvdTimestamp;
            } else
                ret = Math.min(firstDataRcvdTimestamp, firstDataSentTimestamp);
        }
        return ret;
    }

    /**
     * Returns the most recent timestamp for data sent or received for the
     * client. Can be used to determined idle time.
     * 
     * @return
     */
    @JsonIgnore
    public Long getLastDataRxTxTimestamp() {
        if (lastRxTimestamp == null) {
            return lastTxTimestamp;
        }
        if (lastTxTimestamp == null) {
            return lastRxTimestamp;
        }
        return Math.max(lastRxTimestamp, lastTxTimestamp);
    }

    public void setAssociationState(AssociationState associationState) {
        this.associationState = associationState;
    }

    /**
     * Returns an association state which is based on event timestamps.
     * 
     * @return
     */

    public AssociationState getAssociationState() {
        return associationState;
    }

    @JsonIgnore
    public AssociationState calculateAssociationState() {

        if (timeoutTimestamp != null) {
            return AssociationState.AP_Timeout;
        }
        if (getDisconnectTimestamp() != null && (getDisconnectByApInternalReasonCode() == null
                || getDisconnectByApInternalReasonCode().intValue() != 53)) {
            return AssociationState.Disconnected;
        }

        if (getLastEventTimestamp() != null
                && getLastEventTimestamp() < System.currentTimeMillis() - CLOUD_SESSION_TIMEOUT) {
            // No event seen for a while, assume they are gone.
            if (getDisconnectTimestamp() != null) {
                return AssociationState.Disconnected;
            }
            return AssociationState.Cloud_Timeout;

        }
        if (firstDataRcvdTimestamp != null || firstDataSentTimestamp != null
                || (getMetricDetails() != null && (getMetricDetails().getLastRxTimestamp() != null
                        || getMetricDetails().getLastTxTimestamp() != null))) {
            return AssociationState.Active_Data;
        }
        if (assocTimestamp != null) {
            return AssociationState._802_11_Associated;
        } else
            return AssociationState._802_11_Authenticated;

    }

    @Override
    public ClientSessionDetails clone() {
        ClientSessionDetails ret = (ClientSessionDetails) super.clone();
        if (this.dhcpDetails != null) {
            ret.setDhcpDetails(this.dhcpDetails.clone());
        }
        if (this.eapDetails != null) {
            ret.setEapDetails(this.eapDetails.clone());
        }
        if (this.metricDetails != null) {
            ret.setMetricDetails(this.metricDetails.clone());
        }
        if (this.lastFailureDetails != null) {
            ret.setLastFailureDetails(this.lastFailureDetails.clone());
        }
        if (this.firstFailureDetails != null) {
            ret.setFirstFailureDetails(this.firstFailureDetails.clone());
        }
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(apFingerprint, assocInternalSC, assocRssi, assocTimestamp, associationStatus, authTimestamp,
                classificationName, cpUsername, dhcpDetails, disconnectByApInternalReasonCode, disconnectByApReasonCode,
                disconnectByApTimestamp, disconnectByClientInternalReasonCode, disconnectByClientReasonCode,
                disconnectByClientTimestamp, dynamicVlan, eapDetails, firstDataRcvdTimestamp, firstDataSentTimestamp,
                firstFailureDetails, hostname, ipAddress, ipTimestamp, is11KUsed, is11RUsed, is11VUsed, isReassociation,
                lastEventTimestamp, lastFailureDetails, lastRxTimestamp, lastTxTimestamp, metricDetails,
                portEnabledTimestamp, previousValidSessionId, priorEquipmentId, priorSessionId, radioType,
                radiusUsername, securityType, sessionId, ssid, steerType, timeoutTimestamp, userAgentStr,
                associationState);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ClientSessionDetails)) {
            return false;
        }
        ClientSessionDetails other = (ClientSessionDetails) obj;
        return Objects.equals(apFingerprint, other.apFingerprint)
                && Objects.equals(assocInternalSC, other.assocInternalSC) && Objects.equals(assocRssi, other.assocRssi)
                && Objects.equals(assocTimestamp, other.assocTimestamp)
                && Objects.equals(associationStatus, other.associationStatus)
                && Objects.equals(authTimestamp, other.authTimestamp)
                && Objects.equals(classificationName, other.classificationName)
                && Objects.equals(cpUsername, other.cpUsername) && Objects.equals(dhcpDetails, other.dhcpDetails)
                && Objects.equals(disconnectByApInternalReasonCode, other.disconnectByApInternalReasonCode)
                && Objects.equals(disconnectByApReasonCode, other.disconnectByApReasonCode)
                && Objects.equals(disconnectByApTimestamp, other.disconnectByApTimestamp)
                && Objects.equals(disconnectByClientInternalReasonCode, other.disconnectByClientInternalReasonCode)
                && Objects.equals(disconnectByClientReasonCode, other.disconnectByClientReasonCode)
                && Objects.equals(disconnectByClientTimestamp, other.disconnectByClientTimestamp)
                && Objects.equals(dynamicVlan, other.dynamicVlan) && Objects.equals(eapDetails, other.eapDetails)
                && Objects.equals(firstDataRcvdTimestamp, other.firstDataRcvdTimestamp)
                && Objects.equals(firstDataSentTimestamp, other.firstDataSentTimestamp)
                && Objects.equals(firstFailureDetails, other.firstFailureDetails)
                && Objects.equals(hostname, other.hostname) && Objects.equals(ipAddress, other.ipAddress)
                && Objects.equals(ipTimestamp, other.ipTimestamp) && Objects.equals(is11KUsed, other.is11KUsed)
                && Objects.equals(is11RUsed, other.is11RUsed) && Objects.equals(is11VUsed, other.is11VUsed)
                && Objects.equals(isReassociation, other.isReassociation)
                && Objects.equals(lastEventTimestamp, other.lastEventTimestamp)
                && Objects.equals(lastFailureDetails, other.lastFailureDetails)
                && Objects.equals(lastRxTimestamp, other.lastRxTimestamp)
                && Objects.equals(lastTxTimestamp, other.lastTxTimestamp)
                && Objects.equals(metricDetails, other.metricDetails)
                && Objects.equals(portEnabledTimestamp, other.portEnabledTimestamp)
                && Objects.equals(previousValidSessionId, other.previousValidSessionId)
                && Objects.equals(priorEquipmentId, other.priorEquipmentId)
                && Objects.equals(priorSessionId, other.priorSessionId) && radioType == other.radioType
                && Objects.equals(radiusUsername, other.radiusUsername) && securityType == other.securityType
                && sessionId == other.sessionId && Objects.equals(ssid, other.ssid) && steerType == other.steerType
                && Objects.equals(timeoutTimestamp, other.timeoutTimestamp)
                && Objects.equals(userAgentStr, other.userAgentStr)
                && Objects.equals(associationState, other.associationState);

    }

    /**
     * Merge the updated session information
     * 
     * @param latest
     */
    public void mergeSession(ClientSessionDetails latest) {
        if (null != latest.authTimestamp) {
            this.authTimestamp = latest.authTimestamp;
        }
        if (null != latest.assocTimestamp) {
            this.assocTimestamp = latest.assocTimestamp;
        }
        if (null != latest.assocInternalSC) {
            this.assocInternalSC = latest.assocInternalSC;
        }
        if (null != latest.ipTimestamp) {
            this.ipTimestamp = latest.ipTimestamp;
        }
        if (null != latest.timeoutTimestamp) {
            this.timeoutTimestamp = latest.timeoutTimestamp;
        }
        if (null != latest.firstDataSentTimestamp) {
            this.firstDataSentTimestamp = latest.firstDataSentTimestamp;
        }
        if (null != latest.firstDataRcvdTimestamp) {
            this.firstDataRcvdTimestamp = latest.firstDataRcvdTimestamp;
        }
        if (null != latest.ipAddress) {
            this.ipAddress = latest.ipAddress;
        }
        if (null != latest.radiusUsername) {
            this.radiusUsername = latest.radiusUsername;
        }
        if (null != latest.ssid) {
            this.ssid = latest.ssid;
        }
        if (null != latest.radioType) {
            this.radioType = latest.radioType;
        }
        if (null != latest.lastEventTimestamp) {
            this.lastEventTimestamp = latest.lastEventTimestamp;
        }
        if (null != latest.hostname) {
            this.hostname = latest.hostname;
        }
        if (null != latest.apFingerprint) {
            this.apFingerprint = latest.apFingerprint;
        }
        if (null != latest.lastRxTimestamp) {
            this.lastRxTimestamp = latest.lastRxTimestamp;
        }
        if (null != latest.lastTxTimestamp) {
            this.lastTxTimestamp = latest.lastTxTimestamp;
        }
        if (null != latest.cpUsername) {
            this.cpUsername = latest.cpUsername;
        }
        if (null != latest.classificationName) {
            this.classificationName = latest.classificationName;
        }
        if (null != latest.dhcpDetails && null == this.dhcpDetails) {
            this.dhcpDetails = latest.dhcpDetails;
        } else if (latest.dhcpDetails != null) {
            this.dhcpDetails.mergeDetails(latest.dhcpDetails);
        }
        if (null != latest.eapDetails && null == this.eapDetails) {
            this.eapDetails = latest.eapDetails;
        } else if (latest.eapDetails != null) {
            this.eapDetails.mergeDetails(latest.eapDetails);
        }
        if (null != latest.metricDetails) {
            this.metricDetails = latest.metricDetails;
        }

        if (null != latest.getIsReassociation()) {
            this.isReassociation = latest.getIsReassociation();
        }

        if (null != latest.getDynamicVlan()) {
            this.dynamicVlan = latest.getDynamicVlan();
        }
        if (null != latest.getAssocRssi()) {
            this.assocRssi = latest.getAssocRssi();
        }
        if (null != latest.getPriorSessionId()) {
            this.priorSessionId = latest.getPriorSessionId();
        }
        if (null != latest.getPriorEquipmentId()) {
            this.priorEquipmentId = latest.getPriorEquipmentId();
        }

        if (null != latest.getIs11KUsed()) {
            this.is11KUsed = latest.getIs11KUsed();
        }
        if (null != latest.getIs11RUsed()) {
            this.is11RUsed = latest.getIs11RUsed();
        }
        if (null != latest.getIs11VUsed()) {
            this.is11VUsed = latest.getIs11VUsed();
        }

        if (latestTimestampIsFirst(latest.disconnectByClientTimestamp, this.disconnectByClientTimestamp)) {
            this.disconnectByClientTimestamp = latest.disconnectByClientTimestamp;
            this.disconnectByClientReasonCode = latest.disconnectByClientReasonCode;
            this.disconnectByClientInternalReasonCode = latest.disconnectByClientInternalReasonCode;
        }

        if (latestTimestampIsFirst(latest.disconnectByApTimestamp, this.disconnectByApTimestamp)) {
            this.disconnectByApTimestamp = latest.disconnectByApTimestamp;
            this.disconnectByApReasonCode = latest.disconnectByApReasonCode;
            this.disconnectByApInternalReasonCode = latest.disconnectByApInternalReasonCode;
        }

        if (null != latest.getPortEnabledTimestamp()) {
            // Take the latest settings.
            if (this.portEnabledTimestamp == null || latest.getPortEnabledTimestamp() > this.portEnabledTimestamp) {
                this.portEnabledTimestamp = latest.portEnabledTimestamp;
                this.is11RUsed = latest.is11RUsed;
            }
        }
        if (null != latest.getSecurityType()) {
            this.securityType = latest.securityType;
        }
        if (null != latest.getSteerType()) {
            this.steerType = latest.getSteerType();
        }

        if (null != latest.getPreviousValidSessionId()) {
            this.previousValidSessionId = latest.getPreviousValidSessionId();
        }

        if (null != latest.lastFailureDetails && (this.lastFailureDetails == null
                || this.lastFailureDetails.getFailureTimestamp() < latest.lastFailureDetails.getFailureTimestamp())) {
            this.lastFailureDetails = latest.lastFailureDetails;
        }

        if (null != latest.firstFailureDetails && (this.firstFailureDetails == null
                || this.firstFailureDetails.getFailureTimestamp() > latest.firstFailureDetails.getFailureTimestamp())) {
            this.firstFailureDetails = latest.firstFailureDetails;
        }

        if (null != latest.associationStatus) {
            this.associationStatus = latest.associationStatus;
        }

        if (null != latest.associationState) {
            this.associationState = latest.associationState;
        }

        if (null != latest.getUserAgentStr()) {
            this.userAgentStr = latest.getUserAgentStr();
        }

    }

    private void createEapDetailsIfNull() {
        if (eapDetails == null)
            eapDetails = new ClientEapDetails();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (SecurityType.isUnsupported(securityType) || SteerType.isUnsupported(steerType)
                || RadioType.isUnsupported(radioType) || hasUnsupportedValue(dhcpDetails)
                || hasUnsupportedValue(eapDetails) || hasUnsupportedValue(lastFailureDetails)
                || hasUnsupportedValue(firstFailureDetails)) {
            return true;
        }
        return false;
    }

    private static boolean latestTimestampIsFirst(Long latest, Long existing) {
        if (latest == null) {
            return false;
        }
        return (existing == null) || (latest > existing);
    }

    public Integer getAssociationStatus() {
        return associationStatus;
    }

    public void setAssociationStatus(Integer associationStatus) {
        this.associationStatus = associationStatus;
    }

    public Integer getDynamicVlan() {
        return dynamicVlan;
    }

    public void setDynamicVlan(Integer dynamicVlan) {
        this.dynamicVlan = dynamicVlan;
    }

    public String getUserAgentStr() {
        return userAgentStr;
    }

    public void setUserAgentStr(String userAgentStr) {
        this.userAgentStr = userAgentStr;
    }

    public Integer getAssocRssi() {
        return assocRssi;
    }

    public void setAssocRssi(Integer assocRssi) {
        this.assocRssi = assocRssi;
    }

    public String getPriorSessionId() {
        return priorSessionId;
    }

    public void setPriorSessionId(String priorSessionId) {
        this.priorSessionId = priorSessionId;
    }

    public Long getPriorEquipmentId() {
        return priorEquipmentId;
    }

    public void setPriorEquipmentId(Long priorEquipmentId) {
        this.priorEquipmentId = priorEquipmentId;
    }

    @JsonIgnore
    public String getModel() {
        String userAgentModel = UserAgentOsMapper.getPlatform(this.userAgentStr, this.apFingerprint);

        if (userAgentModel == null) {
            return this.apFingerprint;
        } else {
            return userAgentModel;
        }
    }

}
