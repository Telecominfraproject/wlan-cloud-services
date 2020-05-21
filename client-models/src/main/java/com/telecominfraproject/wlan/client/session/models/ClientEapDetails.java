package com.telecominfraproject.wlan.client.session.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class ClientEapDetails extends BaseJsonModel {
    private static final long serialVersionUID = 1L;
    
    private Long eapKey1Timestamp;
    private Long eapKey2Timestamp;
    private Long eapKey3Timestamp;
    private Long eapKey4Timestamp;
    private Long requestIdentityTimestamp;
    private Long eapNegotiationStartTimestamp;
    private Long eapSuccessTimestamp;

    public Long getEapKey1Timestamp() {
        return eapKey1Timestamp;
    }
    public void setEapKey1Timestamp(Long eapKey1Timestamp) {
        this.eapKey1Timestamp = eapKey1Timestamp;
    }
    public Long getEapKey2Timestamp() {
        return eapKey2Timestamp;
    }
    public void setEapKey2Timestamp(Long eapKey2Timestamp) {
        this.eapKey2Timestamp = eapKey2Timestamp;
    }
    public Long getEapKey3Timestamp() {
        return eapKey3Timestamp;
    }
    public void setEapKey3Timestamp(Long eapKey3Timestamp) {
        this.eapKey3Timestamp = eapKey3Timestamp;
    }
    public Long getEapKey4Timestamp() {
        return eapKey4Timestamp;
    }
    public void setEapKey4Timestamp(Long eapKey4Timestamp) {
        this.eapKey4Timestamp = eapKey4Timestamp;
    }
    public Long getRequestIdentityTimestamp() {
        return requestIdentityTimestamp;
    }
    public void setRequestIdentityTimestamp(Long requestIdentityTimestamp) {
        this.requestIdentityTimestamp = requestIdentityTimestamp;
    }
    public Long getEapNegotiationStartTimestamp() {
        return eapNegotiationStartTimestamp;
    }
    public void setEapNegotiationStartTimestamp(Long eapNegotiationStartTimestamp) {
        this.eapNegotiationStartTimestamp = eapNegotiationStartTimestamp;
    }
    
    public Long getEapSuccessTimestamp() {
        return eapSuccessTimestamp;
    }
    public void setEapSuccessTimestamp(Long eapSuccessTimestamp) {
        this.eapSuccessTimestamp = eapSuccessTimestamp;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eapKey1Timestamp == null) ? 0 : eapKey1Timestamp.hashCode());
        result = prime * result + ((eapKey2Timestamp == null) ? 0 : eapKey2Timestamp.hashCode());
        result = prime * result + ((eapKey3Timestamp == null) ? 0 : eapKey3Timestamp.hashCode());
        result = prime * result + ((eapKey4Timestamp == null) ? 0 : eapKey4Timestamp.hashCode());
        result = prime * result
                + ((eapNegotiationStartTimestamp == null) ? 0 : eapNegotiationStartTimestamp.hashCode());
        result = prime * result + ((eapSuccessTimestamp == null) ? 0 : eapSuccessTimestamp.hashCode());
        result = prime * result + ((requestIdentityTimestamp == null) ? 0 : requestIdentityTimestamp.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ClientEapDetails))
            return false;
        ClientEapDetails other = (ClientEapDetails) obj;
        if (eapKey1Timestamp == null) {
            if (other.eapKey1Timestamp != null)
                return false;
        } else if (!eapKey1Timestamp.equals(other.eapKey1Timestamp))
            return false;
        if (eapKey2Timestamp == null) {
            if (other.eapKey2Timestamp != null)
                return false;
        } else if (!eapKey2Timestamp.equals(other.eapKey2Timestamp))
            return false;
        if (eapKey3Timestamp == null) {
            if (other.eapKey3Timestamp != null)
                return false;
        } else if (!eapKey3Timestamp.equals(other.eapKey3Timestamp))
            return false;
        if (eapKey4Timestamp == null) {
            if (other.eapKey4Timestamp != null)
                return false;
        } else if (!eapKey4Timestamp.equals(other.eapKey4Timestamp))
            return false;
        if (eapNegotiationStartTimestamp == null) {
            if (other.eapNegotiationStartTimestamp != null)
                return false;
        } else if (!eapNegotiationStartTimestamp.equals(other.eapNegotiationStartTimestamp))
            return false;
        if (eapSuccessTimestamp == null) {
            if (other.eapSuccessTimestamp != null)
                return false;
        } else if (!eapSuccessTimestamp.equals(other.eapSuccessTimestamp))
            return false;
        if (requestIdentityTimestamp == null) {
            if (other.requestIdentityTimestamp != null)
                return false;
        } else if (!requestIdentityTimestamp.equals(other.requestIdentityTimestamp))
            return false;
        return true;
    }
    
    
    public void mergeDetails(ClientEapDetails latest) {
        if(latest.eapKey1Timestamp != null) {
            this.eapKey1Timestamp = latest.eapKey1Timestamp;
        }
        if(latest.eapKey2Timestamp != null) {
            this.eapKey2Timestamp = latest.eapKey2Timestamp;
        }
        if(latest.eapKey3Timestamp != null) {
            this.eapKey3Timestamp = latest.eapKey3Timestamp;
        }
        if(latest.eapKey4Timestamp != null) {
            this.eapKey4Timestamp = latest.eapKey4Timestamp;
        }
        if(latest.eapNegotiationStartTimestamp != null && (this.eapNegotiationStartTimestamp == null || this.eapNegotiationStartTimestamp > latest.eapNegotiationStartTimestamp)) {
            this.eapNegotiationStartTimestamp = latest.eapNegotiationStartTimestamp;
        }
        if(latest.requestIdentityTimestamp != null && (this.requestIdentityTimestamp == null || this.requestIdentityTimestamp > latest.requestIdentityTimestamp)) {
            this.requestIdentityTimestamp = latest.requestIdentityTimestamp;
        }
        if(latest.eapSuccessTimestamp != null && (this.eapSuccessTimestamp == null || this.eapSuccessTimestamp > latest.eapSuccessTimestamp)) {
            this.eapSuccessTimestamp = latest.eapSuccessTimestamp;
        }
    }

    @Override
    public ClientEapDetails clone() {
        ClientEapDetails ret = (ClientEapDetails) super.clone();
        return ret;
    }

    
}
