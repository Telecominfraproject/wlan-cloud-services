package com.telecominfraproject.wlan.client.session.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class ClientFailureDetails extends BaseJsonModel {
    private static final long serialVersionUID = 468876079715114163L;
    private Long failureTimestamp;
    private Integer reasonCode;
    private String reason;
    
    public Long getFailureTimestamp() {
        return failureTimestamp;
    }
    public void setFailureTimestamp(Long failureTimestamp) {
        this.failureTimestamp = failureTimestamp;
    }
    public Integer getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(Integer reasonCode) {
        this.reasonCode = reasonCode;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((failureTimestamp == null) ? 0 : failureTimestamp.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((reasonCode == null) ? 0 : reasonCode.hashCode());
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
        if (!(obj instanceof ClientFailureDetails)) {
            return false;
        }
        ClientFailureDetails other = (ClientFailureDetails) obj;
        if (failureTimestamp == null) {
            if (other.failureTimestamp != null) {
                return false;
            }
        } else if (!failureTimestamp.equals(other.failureTimestamp)) {
            return false;
        }
        if (reason == null) {
            if (other.reason != null) {
                return false;
            }
        } else if (!reason.equals(other.reason)) {
            return false;
        }
        if (reasonCode == null) {
            if (other.reasonCode != null) {
                return false;
            }
        } else if (!reasonCode.equals(other.reasonCode)) {
            return false;
        }
        return true;
    }

    @Override
    public ClientFailureDetails clone() {
        ClientFailureDetails ret = (ClientFailureDetails) super.clone();
        return ret;
    }

}
