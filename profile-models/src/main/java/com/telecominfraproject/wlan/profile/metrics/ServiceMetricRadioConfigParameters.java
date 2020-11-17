package com.telecominfraproject.wlan.profile.metrics;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class ServiceMetricRadioConfigParameters extends ServiceMetricsCommonConfigParameters {

    private static final long serialVersionUID = 165731309820030327L;
    private RadioType radioType;
    private ServiceMetricDataType serviceMetricDataType;


    private ServiceMetricRadioConfigParameters() {
        super();
    }

    public static ServiceMetricRadioConfigParameters createWithDefaults() {
        return new ServiceMetricRadioConfigParameters();
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }


    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (RadioType.isUnsupported(radioType)) {
            return true;
        }

        if (ServiceMetricDataType.isUnsupported(getServiceMetricDataType())) {
            return true;
        }

        return false;
    }


    @Override
    public ServiceMetricRadioConfigParameters clone() {
        ServiceMetricRadioConfigParameters ret = (ServiceMetricRadioConfigParameters) super.clone();
        ret.setRadioType(getRadioType());
        ret.setServiceMetricDataType(getServiceMetricDataType());
        return ret;
    }

 

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(radioType, serviceMetricDataType);
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
        return radioType == other.radioType && Objects.equals(serviceMetricDataType, other.serviceMetricDataType);
    }

    @Override
    public ServiceMetricDataType getServiceMetricDataType() {
        return serviceMetricDataType;
    }

    @Override
    public void setServiceMetricDataType(ServiceMetricDataType serviceMetricDataType) {
        this.serviceMetricDataType = serviceMetricDataType;

    }

}
