package com.telecominfraproject.wlan.profile.metrics;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class ServiceMetricRadioConfigParameters extends ServiceMetricConfigParameters {

    /**
     * 
     */
    private static final long serialVersionUID = 165731309820030327L;
    RadioType radioType;

    public ServiceMetricRadioConfigParameters(RadioType radioType, ServiceMetricDataType dataType,
            int sampleIntervalMillis, int reportingIntervalSeconds) {
        super(dataType, sampleIntervalMillis, reportingIntervalSeconds);
        this.setRadioType(radioType);
    }

    ServiceMetricRadioConfigParameters() {
        reportingIntervalSeconds = ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS;
        samplingInterval = ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS;

    }

    public static ServiceMetricRadioConfigParameters generateDefault(ServiceMetricDataType serviceMetricDataType,
            RadioType radioType) {
        ServiceMetricRadioConfigParameters ret = new ServiceMetricRadioConfigParameters();
        ret.setServiceMetricDataType(serviceMetricDataType);
        ret.radioType = radioType;
        return ret;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }


    @Override
    public ServiceMetricRadioConfigParameters clone() {
        ServiceMetricRadioConfigParameters ret = (ServiceMetricRadioConfigParameters) super.clone();
        ret.radioType = this.radioType;
        return ret;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(radioType);
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
        if (!(obj instanceof ServiceMetricRadioConfigParameters)) {
            return false;
        }
        ServiceMetricRadioConfigParameters other = (ServiceMetricRadioConfigParameters) obj;
        return radioType == other.radioType;
    }


}
