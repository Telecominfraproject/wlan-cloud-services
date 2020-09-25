package com.telecominfraproject.wlan.profile.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


public class ServiceMetricsCollectionConfigProfile extends ProfileDetails
        implements PushableConfiguration<ServiceMetricsCollectionConfigProfile> {

    private static final long serialVersionUID = 1213028270690178108L;
    private static final ServiceMetricDataType[] defaultServiceMetrics = { ServiceMetricDataType.ApNode,
            ServiceMetricDataType.ApSsid, ServiceMetricDataType.Channel, ServiceMetricDataType.Client,
            ServiceMetricDataType.Neighbour };
    private List<RadioType> radioTypeList;

    private Map<String, ServiceMetricConfigParameters> metricConfigParameterMap;

    public static ServiceMetricsCollectionConfigProfile createWithDefaults() {
        ServiceMetricsCollectionConfigProfile ret = new ServiceMetricsCollectionConfigProfile();
        ret.radioTypeList = Arrays.asList(RadioType.validValues());
        for (ServiceMetricDataType metricDataType : Arrays.asList(defaultServiceMetrics)) {
            for (RadioType radioType : ret.radioTypeList) {
                ret.metricConfigParameterMap.put(metricDataType.getName(),
                        ret.getDefaultConfigForMetricType(metricDataType, radioType));
            }
        }
        return ret;
    }

    private ServiceMetricsCollectionConfigProfile() {
        
        metricConfigParameterMap = new HashMap<>();
        radioTypeList = new ArrayList<>();
    }

    public static ServiceMetricsCollectionConfigProfile createWithDefaults(List<RadioType> radioTypes) {


        ServiceMetricsCollectionConfigProfile ret = new ServiceMetricsCollectionConfigProfile();
        ret.radioTypeList = radioTypes;
        for (ServiceMetricDataType metricDataType : Arrays.asList(defaultServiceMetrics)) {
            for (RadioType radioType : radioTypes) {
                ret.metricConfigParameterMap.put(metricDataType.getName(),
                        ret.getDefaultConfigForMetricType(metricDataType, radioType));
            }
        }
        return ret;
    }

    public ServiceMetricsCollectionConfigProfile(List<RadioType> radioTypes, List<ServiceMetricDataType> metricTypes) {
        radioTypeList = radioTypes;
        metricConfigParameterMap = new HashMap<>();
        for (RadioType radioType : radioTypes) {
            createMetricConfgData(metricTypes, radioType);
        }
    }

    private void createMetricConfgData(List<ServiceMetricDataType> metricTypes, RadioType radioType) {

        for (ServiceMetricDataType metricDataType : metricTypes) {
            metricConfigParameterMap.put(metricDataType.getName(),
                    getDefaultConfigForMetricType(metricDataType, radioType));
        }

    }

    /**
     * @param dataType
     * @return default parameter set for on-channel stats collection on the AP
     *         for the given ServiceMetricDataType
     */
    public ServiceMetricConfigParameters getDefaultConfigForMetricType(ServiceMetricDataType dataType,
            RadioType radioType) {
        ServiceMetricConfigParameters ret = null;

        if (dataType.equals(ServiceMetricDataType.ApNode) || dataType.equals(ServiceMetricDataType.Channel)
                || dataType.equals(ServiceMetricDataType.Neighbour)) {
            ret = ServiceMetricSurveyConfigParameters.generateDefault(dataType);
        } else if (dataType.equals(ServiceMetricDataType.ApSsid) || dataType.equals(ServiceMetricDataType.Client)
                || dataType.equals(ServiceMetricDataType.ClientQoE)) {
            ret = ServiceMetricConfigParameters.generateDefault(dataType);
        } else {
            ret = ServiceMetricConfigParameters.generateDefault(dataType);
        }
        return ret;
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.metrics;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(ServiceMetricsCollectionConfigProfile previousVersion) {
        return (!this.equals(previousVersion));
    }

    public List<RadioType> getRadioTypeList() {
        return radioTypeList;
    }

    public void setRadioTypeList(List<RadioType> radioTypeList) {
        this.radioTypeList = radioTypeList;
    }

    public Map<String, ServiceMetricConfigParameters> getMetricConfigParameterMap() {
        return metricConfigParameterMap;
    }

    public void setMetricConfigParameterMap(Map<String, ServiceMetricConfigParameters> metricConfigParameterMap) {
        this.metricConfigParameterMap = metricConfigParameterMap;
    }

    @Override
    public ServiceMetricsCollectionConfigProfile clone() {
        ServiceMetricsCollectionConfigProfile returnValue = (ServiceMetricsCollectionConfigProfile) super.clone();

        return returnValue;
    }


    @Override
    public int hashCode() {
        return Objects.hash(metricConfigParameterMap, radioTypeList);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceMetricsCollectionConfigProfile)) {
            return false;
        }
        ServiceMetricsCollectionConfigProfile other = (ServiceMetricsCollectionConfigProfile) obj;
        return Objects.equals(metricConfigParameterMap, other.metricConfigParameterMap)
                && Objects.equals(radioTypeList, other.radioTypeList);
    }


}
