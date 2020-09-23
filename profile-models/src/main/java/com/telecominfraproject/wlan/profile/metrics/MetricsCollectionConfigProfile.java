package com.telecominfraproject.wlan.profile.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


public class MetricsCollectionConfigProfile extends ProfileDetails
        implements PushableConfiguration<MetricsCollectionConfigProfile> {

    private static final long serialVersionUID = 1213028270690178108L;
    private static final ServiceMetricDataType[] defaultServiceMetrics = { ServiceMetricDataType.ApNode,
            ServiceMetricDataType.ApSsid, ServiceMetricDataType.Channel, ServiceMetricDataType.Client,
            ServiceMetricDataType.Neighbour };
    private List<RadioType> radioTypeList;

    private Map<String, MetricConfigParameters> metricConfigParameterMap;

    public static MetricsCollectionConfigProfile createWithDefaults() {

        MetricsCollectionConfigProfile ret = new MetricsCollectionConfigProfile(Arrays.asList(RadioType.validValues()),
                Arrays.asList(defaultServiceMetrics));

        return ret;
    }


    public static MetricsCollectionConfigProfile createWithDefaults(List<RadioType> radioTypes) {


        MetricsCollectionConfigProfile ret = new MetricsCollectionConfigProfile(radioTypes,
                Arrays.asList(defaultServiceMetrics));

        return ret;
    }

    public MetricsCollectionConfigProfile(List<RadioType> radioTypes, List<ServiceMetricDataType> metricTypes) {
        radioTypeList = radioTypes;
        metricConfigParameterMap = new HashMap<>();
        createMetricConfgData(metricTypes);
    }

    private void createMetricConfgData(List<ServiceMetricDataType> metricTypes) {

        for (ServiceMetricDataType metricDataType : metricTypes) {
            metricConfigParameterMap.put(metricDataType.getName(),
                    MetricConfigParameters.getDefaultConfigForMetricType(metricDataType));
        }

    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.metrics;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(MetricsCollectionConfigProfile previousVersion) {
        return (!this.equals(previousVersion));
    }

    public List<RadioType> getRadioTypeList() {
        return radioTypeList;
    }

    public void setRadioTypeList(List<RadioType> radioTypeList) {
        this.radioTypeList = radioTypeList;
    }

    public Map<String, MetricConfigParameters> getMetricConfigParameterMap() {
        return metricConfigParameterMap;
    }

    public void setMetricConfigParameterMap(Map<String, MetricConfigParameters> metricConfigParameterMap) {
        this.metricConfigParameterMap = metricConfigParameterMap;
    }

    @Override
    public MetricsCollectionConfigProfile clone() {
        MetricsCollectionConfigProfile ret = (MetricsCollectionConfigProfile) super.clone();

        if (radioTypeList != null) {
            ret.radioTypeList = new ArrayList<>();
            ret.radioTypeList.addAll(radioTypeList);
        }

        if (metricConfigParameterMap != null) {

            for (Map.Entry<String, MetricConfigParameters> entry : metricConfigParameterMap.entrySet()) {
                ret.metricConfigParameterMap.put(entry.getKey(), entry.getValue().clone());
            }
        }

        return ret;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetricsCollectionConfigProfile other = (MetricsCollectionConfigProfile) obj;
        if (radioTypeList == null) {
            if (other.radioTypeList != null)
                return false;
        } else if (!radioTypeList.equals(other.radioTypeList))
            return false;

        if (metricConfigParameterMap == null) {
            if (other.metricConfigParameterMap != null) {
                return false;
            }
        } else if (!metricConfigParameterMap.equals(other.metricConfigParameterMap)) {
            return false;
        }
        return true;
    }

}
