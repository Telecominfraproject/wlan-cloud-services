package com.telecominfraproject.wlan.profile.metrics;

import java.util.Objects;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class ServiceMetricConfigParameters extends CommonServiceMetricConfigParameters {

    private static final long serialVersionUID = 1469611396924992464L;
    private ServiceMetricDataType serviceMetricDataType;



    private ServiceMetricConfigParameters() {
        super();
    }
    
    public static ServiceMetricConfigParameters createWithDefaults() {
        return new ServiceMetricConfigParameters();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (ServiceMetricDataType.isUnsupported(getServiceMetricDataType())) {
            return true;
        }
        return false;
    }

    
    @Override
    public ServiceMetricConfigParameters clone() {
        ServiceMetricConfigParameters ret = (ServiceMetricConfigParameters) super.clone();
        ret.setServiceMetricDataType(getServiceMetricDataType());
        return ret;
    }
  
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(serviceMetricDataType);
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
        if (!(obj instanceof ServiceMetricConfigParameters)) {
            return false;
        }
        ServiceMetricConfigParameters other = (ServiceMetricConfigParameters) obj;
        return Objects.equals(serviceMetricDataType, other.serviceMetricDataType);
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
