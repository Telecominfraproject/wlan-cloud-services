package com.telecominfraproject.wlan.servicemetric.client.qoe.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;

/**
 * QoE related metrics which is independent of RadioType
 * 
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientQoEMetrics extends ServiceMetricDetails {

    private static final long serialVersionUID = 5242617221447159480L;

    /**
     * How many seconds the AP measured for the metric
     */
    private Integer periodLengthSec = 5;
    private Integer secondsSinceLastRecv;


    // Connectivity QoE stats.
    private Long qoeEventualSuccessTimeTaken;
    private Long qoeNumOfAttempts;
    private Long qoeNumOfSuccess;
    private Long qoeAttemptDuration;
    private Long qoeAssociatedDuration;
    private Long qoeDeltaDuration;
    private Long qoeNumRepeatedAttempts;
    private Long qoeUserError;

    @Override
    public ServiceMetricDataType getDataType() {
    	return ServiceMetricDataType.ClientQoE;
    }
    
    @Override
    public ClientQoEMetrics clone() {
        return (ClientQoEMetrics) super.clone();
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(periodLengthSec, qoeAssociatedDuration, qoeAttemptDuration,
				qoeDeltaDuration, qoeEventualSuccessTimeTaken, qoeNumOfAttempts, qoeNumOfSuccess,
				qoeNumRepeatedAttempts, qoeUserError, secondsSinceLastRecv);
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
		if (!(obj instanceof ClientQoEMetrics)) {
			return false;
		}
		ClientQoEMetrics other = (ClientQoEMetrics) obj;
		return Objects.equals(periodLengthSec, other.periodLengthSec)
				&& Objects.equals(qoeAssociatedDuration, other.qoeAssociatedDuration)
				&& Objects.equals(qoeAttemptDuration, other.qoeAttemptDuration)
				&& Objects.equals(qoeDeltaDuration, other.qoeDeltaDuration)
				&& Objects.equals(qoeEventualSuccessTimeTaken, other.qoeEventualSuccessTimeTaken)
				&& Objects.equals(qoeNumOfAttempts, other.qoeNumOfAttempts)
				&& Objects.equals(qoeNumOfSuccess, other.qoeNumOfSuccess)
				&& Objects.equals(qoeNumRepeatedAttempts, other.qoeNumRepeatedAttempts)
				&& Objects.equals(qoeUserError, other.qoeUserError)
				&& Objects.equals(secondsSinceLastRecv, other.secondsSinceLastRecv);
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

	public Integer getPeriodLengthSec() {
		return periodLengthSec;
	}

	public void setPeriodLengthSec(Integer periodLengthSec) {
		this.periodLengthSec = periodLengthSec;
	}

	public Integer getSecondsSinceLastRecv() {
		return secondsSinceLastRecv;
	}

	public void setSecondsSinceLastRecv(Integer secondsSinceLastRecv) {
		this.secondsSinceLastRecv = secondsSinceLastRecv;
	}
}
