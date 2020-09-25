package com.telecominfraproject.wlan.profile.metrics;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class ServiceMetricConfigParameters extends BaseJsonModel implements PushableConfiguration<ServiceMetricConfigParameters> {

    private static final long serialVersionUID = 1469611396924992464L;


    /**
     * Type of cloud service metric being configured
     */
    private ServiceMetricDataType serviceMetricDataType;

    /**
     * Time between sample collections.
     */
    private Integer samplingInterval;
    /**
     * Reporting interval for Metric
     */
    private Integer reportingIntervalSeconds;

    public ServiceMetricConfigParameters(ServiceMetricDataType dataType, int defaultSampleIntervalMillis,
            int defaultReportIntervalSeconds) {
        this.reportingIntervalSeconds = defaultReportIntervalSeconds;
        this.samplingInterval = defaultSampleIntervalMillis;
        this.serviceMetricDataType = dataType;
    }


    @Override
    public boolean needsToBeUpdatedOnDevice(ServiceMetricConfigParameters previousVersion) {
        return (!this.equals(previousVersion));
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

    public ServiceMetricDataType getServiceMetricDataType() {
        return serviceMetricDataType;
    }

    public void setServiceMetricDataType(ServiceMetricDataType serviceMetricDataType) {
        this.serviceMetricDataType = serviceMetricDataType;
    }

    @Override
    public ServiceMetricConfigParameters clone() {
        ServiceMetricConfigParameters ret = (ServiceMetricConfigParameters) super.clone();
        
        ret.reportingIntervalSeconds = this.reportingIntervalSeconds;
        ret.samplingInterval = this.samplingInterval;
        ret.serviceMetricDataType = this.serviceMetricDataType;
        
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportingIntervalSeconds, samplingInterval, serviceMetricDataType);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceMetricConfigParameters)) {
            return false;
        }
        ServiceMetricConfigParameters other = (ServiceMetricConfigParameters) obj;
        return Objects.equals(reportingIntervalSeconds, other.reportingIntervalSeconds)
                && Objects.equals(samplingInterval, other.samplingInterval)
                && Objects.equals(serviceMetricDataType, other.serviceMetricDataType);
    }

}
