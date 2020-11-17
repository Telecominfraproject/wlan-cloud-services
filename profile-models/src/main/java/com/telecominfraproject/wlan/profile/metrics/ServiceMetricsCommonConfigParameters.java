package com.telecominfraproject.wlan.profile.metrics;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


public abstract class ServiceMetricsCommonConfigParameters extends BaseJsonModel {

    private static final long serialVersionUID = 5329884188461218203L;
    private Integer samplingInterval;
    private Integer reportingIntervalSeconds;

    public ServiceMetricsCommonConfigParameters() {
        super();
        reportingIntervalSeconds = ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS;
        samplingInterval = ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS;

        
    }

    public Integer getSamplingInterval() {
        return samplingInterval;
    }

    public void setSamplingInterval(Integer samplingInterval) {
        this.samplingInterval = samplingInterval;
    }

    public Integer getReportingIntervalSeconds() {
        return reportingIntervalSeconds;
    }

    public void setReportingIntervalSeconds(Integer reportingIntervalSeconds) {
        this.reportingIntervalSeconds = reportingIntervalSeconds;
    }

    public abstract ServiceMetricDataType getServiceMetricDataType() ;

    public abstract void setServiceMetricDataType(ServiceMetricDataType serviceMetricDataType);

    @Override
    public ServiceMetricsCommonConfigParameters clone() {
        ServiceMetricsCommonConfigParameters ret = (ServiceMetricsCommonConfigParameters) super.clone();
        ret.setReportingIntervalSeconds(getReportingIntervalSeconds());
        ret.setSamplingInterval(getSamplingInterval());
        return ret;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
      
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportingIntervalSeconds, samplingInterval);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceMetricsCommonConfigParameters)) {
            return false;
        }
        ServiceMetricsCommonConfigParameters other = (ServiceMetricsCommonConfigParameters) obj;
        return Objects.equals(reportingIntervalSeconds, other.reportingIntervalSeconds)
                && Objects.equals(samplingInterval, other.samplingInterval);
    }

  




}
