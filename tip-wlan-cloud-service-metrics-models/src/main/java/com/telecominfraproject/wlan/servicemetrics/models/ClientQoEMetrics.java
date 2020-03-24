package com.telecominfraproject.wlan.servicemetrics.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * QoE related metrics which is independent of RadioType
 * 
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientQoEMetrics extends BaseClientMetrics {

    private static final long serialVersionUID = 5242617221447159480L;

    // Connectivity QoE stats.
    private Long qoeEventualSuccessTimeTaken;
    private Long qoeNumOfAttempts;
    private Long qoeNumOfSuccess;
    private Long qoeAttemptDuration;
    private Long qoeAssociatedDuration;
    private Long qoeDeltaDuration;
    private Long qoeNumRepeatedAttempts;
    private Long qoeUserError;

    public ClientQoEMetrics() {
    }

    @Override
    public ClientQoEMetrics clone() {
        return (ClientQoEMetrics) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientQoEMetrics)) {
            return false;
        }
        ClientQoEMetrics other = (ClientQoEMetrics) obj;
        if (this.qoeAssociatedDuration == null) {
            if (other.qoeAssociatedDuration != null) {
                return false;
            }
        } else if (!this.qoeAssociatedDuration.equals(other.qoeAssociatedDuration)) {
            return false;
        }
        if (this.qoeAttemptDuration == null) {
            if (other.qoeAttemptDuration != null) {
                return false;
            }
        } else if (!this.qoeAttemptDuration.equals(other.qoeAttemptDuration)) {
            return false;
        }
        if (this.qoeDeltaDuration == null) {
            if (other.qoeDeltaDuration != null) {
                return false;
            }
        } else if (!this.qoeDeltaDuration.equals(other.qoeDeltaDuration)) {
            return false;
        }
        if (this.qoeEventualSuccessTimeTaken == null) {
            if (other.qoeEventualSuccessTimeTaken != null) {
                return false;
            }
        } else if (!this.qoeEventualSuccessTimeTaken.equals(other.qoeEventualSuccessTimeTaken)) {
            return false;
        }
        if (this.qoeNumOfAttempts == null) {
            if (other.qoeNumOfAttempts != null) {
                return false;
            }
        } else if (!this.qoeNumOfAttempts.equals(other.qoeNumOfAttempts)) {
            return false;
        }
        if (this.qoeNumOfSuccess == null) {
            if (other.qoeNumOfSuccess != null) {
                return false;
            }
        } else if (!this.qoeNumOfSuccess.equals(other.qoeNumOfSuccess)) {
            return false;
        }
        if (this.qoeNumRepeatedAttempts == null) {
            if (other.qoeNumRepeatedAttempts != null) {
                return false;
            }
        } else if (!this.qoeNumRepeatedAttempts.equals(other.qoeNumRepeatedAttempts)) {
            return false;
        }
        if (this.qoeUserError == null) {
            if (other.qoeUserError != null) {
                return false;
            }
        } else if (!this.qoeUserError.equals(other.qoeUserError)) {
            return false;
        }
        return true;
    }

    public Long getQoeAssociatedDuration() {
        return qoeAssociatedDuration;
    }

    public Long getQoeAttemptDuration() {
        return qoeAttemptDuration;
    }

    public Long getQoeDeltaDuration() {
        return qoeDeltaDuration;
    }

    public Long getQoeEventualSuccessTimeTaken() {
        return qoeEventualSuccessTimeTaken;
    }

    public Long getQoeNumOfAttempts() {
        return qoeNumOfAttempts;
    }

    public Long getQoeNumOfSuccess() {
        return qoeNumOfSuccess;
    }

    public Long getQoeNumRepeatedAttempts() {
        return qoeNumRepeatedAttempts;
    }

    public Long getQoeUserError() {
        return qoeUserError;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.qoeAssociatedDuration == null) ? 0 : this.qoeAssociatedDuration.hashCode());
        result = prime * result + ((this.qoeAttemptDuration == null) ? 0 : this.qoeAttemptDuration.hashCode());
        result = prime * result + ((this.qoeDeltaDuration == null) ? 0 : this.qoeDeltaDuration.hashCode());
        result = prime * result
                + ((this.qoeEventualSuccessTimeTaken == null) ? 0 : this.qoeEventualSuccessTimeTaken.hashCode());
        result = prime * result + ((this.qoeNumOfAttempts == null) ? 0 : this.qoeNumOfAttempts.hashCode());
        result = prime * result + ((this.qoeNumOfSuccess == null) ? 0 : this.qoeNumOfSuccess.hashCode());
        result = prime * result + ((this.qoeNumRepeatedAttempts == null) ? 0 : this.qoeNumRepeatedAttempts.hashCode());
        result = prime * result + ((this.qoeUserError == null) ? 0 : this.qoeUserError.hashCode());
        return result;
    }

    public void setQoeAssociatedDuration(Long qoeAssociatedDuration) {
        this.qoeAssociatedDuration = qoeAssociatedDuration;
    }

    public void setQoeAttemptDuration(Long qoeAttemptDuration) {
        this.qoeAttemptDuration = qoeAttemptDuration;
    }

    public void setQoeDeltaDuration(Long qoeDeltaDuration) {
        this.qoeDeltaDuration = qoeDeltaDuration;
    }

    public void setQoeEventualSuccessTimeTaken(Long qoeEventualSuccessTimeTaken) {
        this.qoeEventualSuccessTimeTaken = qoeEventualSuccessTimeTaken;
    }

    public void setQoeNumOfAttempts(Long qoeNumOfAttempts) {
        this.qoeNumOfAttempts = qoeNumOfAttempts;
    }

    public void setQoeNumOfSuccess(Long qoeNumOfSuccess) {
        this.qoeNumOfSuccess = qoeNumOfSuccess;
    }

    public void setQoeNumRepeatedAttempts(Long qoeNumRepeatedAttempts) {
        this.qoeNumRepeatedAttempts = qoeNumRepeatedAttempts;
    }

    public void setQoeUserError(Long qoeUserError) {
        this.qoeUserError = qoeUserError;
    }
}
