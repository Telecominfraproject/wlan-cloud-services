package com.telecominfraproject.wlan.profile.metrics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


public class ServiceMetricsCollectionConfigProfile extends ProfileDetails
        implements PushableConfiguration<ServiceMetricsCollectionConfigProfile> {

    private static final long serialVersionUID = 1213028270690178108L;

    private Set<RadioType> radioTypes;
    private Set<ServiceMetricDataType> serviceMetricDataTypes;

    private Map<ServiceMetricDataType, Set<CommonServiceMetricConfigParameters>> metricConfigParameterMap;

    public static ServiceMetricsCollectionConfigProfile createWithDefaults() {
        return new ServiceMetricsCollectionConfigProfile();
    }


    public void setAllNetworkConfigParametersToDefaults(Set<RadioType> radioTypes,
            Set<ServiceMetricDataType> serviceMetricDataTypes, boolean includeOffChannel) {

        setRadioTypes(radioTypes);
        setServiceMetricDataTypes(serviceMetricDataTypes);
        setMetricConfigParameterMap(new HashMap<>());

        for (ServiceMetricDataType metricDataType : serviceMetricDataTypes) {

            Set<CommonServiceMetricConfigParameters> configParameterSet = new HashSet<>();

            getDefaultConfigForMetricType(metricDataType, radioTypes, configParameterSet, includeOffChannel);

            metricConfigParameterMap.put(metricDataType, configParameterSet);

        }
    }

    private ServiceMetricsCollectionConfigProfile() {
        serviceMetricDataTypes = new HashSet<>();
        serviceMetricDataTypes.addAll(ServiceMetricDataType.getValues());
        radioTypes = new HashSet<>();
        radioTypes.addAll(Arrays.asList(RadioType.validValues()));

        metricConfigParameterMap = new HashMap<>();

        for (ServiceMetricDataType metricDataType : serviceMetricDataTypes) {

            Set<CommonServiceMetricConfigParameters> configParameterSet = new HashSet<>();

            getDefaultConfigForMetricType(metricDataType, radioTypes, configParameterSet, true);

            metricConfigParameterMap.put(metricDataType, configParameterSet);

        }
    }

    /**
     * @param serviceMetricDataType
     * @param includeOffChannel
     * @return default parameter set for on-channel stats collection on the AP
     *         for the given ServiceMetricDataType
     */
    private void getDefaultConfigForMetricType(ServiceMetricDataType serviceMetricDataType, Set<RadioType> radioTypes,
            Set<CommonServiceMetricConfigParameters> parameterSet, boolean includeOffChannel) {
        if (serviceMetricDataType.equals(ServiceMetricDataType.ApNode)
                || serviceMetricDataType.equals(ServiceMetricDataType.Channel)
                || serviceMetricDataType.equals(ServiceMetricDataType.Neighbour)) {

            for (RadioType radioType : radioTypes) {
                ServiceMetricSurveyConfigParameters surveyConfigParameters = ServiceMetricSurveyConfigParameters
                        .createWithDefaults();
                surveyConfigParameters.setServiceMetricDataType(serviceMetricDataType);
                surveyConfigParameters.setRadioType(radioType);
                surveyConfigParameters.setChannelSurveyType(ChannelUtilizationSurveyType.ON_CHANNEL);
                surveyConfigParameters.setDelayMillisecondsThreshold(600);
                surveyConfigParameters.setPercentUtilizationThreshold(10);
                surveyConfigParameters.setStatsReportFormat(StatsReportFormat.RAW);
                surveyConfigParameters.setReportingIntervalSeconds(
                        ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);
                parameterSet.add(surveyConfigParameters);
                if (includeOffChannel) {
                    surveyConfigParameters = ServiceMetricSurveyConfigParameters.createWithDefaults();
                    surveyConfigParameters.setServiceMetricDataType(serviceMetricDataType);
                    surveyConfigParameters.setRadioType(radioType);
                    surveyConfigParameters.setChannelSurveyType(ChannelUtilizationSurveyType.OFF_CHANNEL);
                    surveyConfigParameters.setDelayMillisecondsThreshold(600);
                    surveyConfigParameters.setPercentUtilizationThreshold(10);
                    surveyConfigParameters.setStatsReportFormat(StatsReportFormat.RAW);
                    surveyConfigParameters.setReportingIntervalSeconds(
                            ServiceMetricConfigParameterDefaults.DEFAULT_OFF_CHANNEL_REPORT_INTERVAL_SECONDS);
                    parameterSet.add(surveyConfigParameters);
                }
            }
        } else if (serviceMetricDataType.equals(ServiceMetricDataType.ApSsid)
                || serviceMetricDataType.equals(ServiceMetricDataType.Client)) {
            for (RadioType radioType : radioTypes) {
                ServiceMetricRadioConfigParameters radioConfigParameters = ServiceMetricRadioConfigParameters
                        .createWithDefaults();
                radioConfigParameters.setServiceMetricDataType(serviceMetricDataType);
                radioConfigParameters.setRadioType(radioType);
                radioConfigParameters.setReportingIntervalSeconds(
                        ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);
                parameterSet.add(radioConfigParameters);
            }
        } else if (serviceMetricDataType.equals(ServiceMetricDataType.ClientQoE)
                || serviceMetricDataType.equals(ServiceMetricDataType.QoE)) {
            ServiceMetricConfigParameters configParameters = ServiceMetricConfigParameters.createWithDefaults();
            configParameters.setServiceMetricDataType(serviceMetricDataType);
            parameterSet.add(configParameters);
        }
    }

    public Set<RadioType> getRadioTypes() {
        return radioTypes;
    }

    public void setRadioTypes(Set<RadioType> radioTypes) {
        this.radioTypes = radioTypes;
    }

    public Map<ServiceMetricDataType, Set<CommonServiceMetricConfigParameters>> getMetricConfigParameterMap() {
        return metricConfigParameterMap;
    }

    public void setMetricConfigParameterMap(
            Map<ServiceMetricDataType, Set<CommonServiceMetricConfigParameters>> metricConfigParameterMap) {
        this.metricConfigParameterMap = metricConfigParameterMap;
    }

    public Set<ServiceMetricDataType> getServiceMetricDataTypes() {
        return serviceMetricDataTypes;
    }

    public void setServiceMetricDataTypes(Set<ServiceMetricDataType> serviceMetricDataTypes) {
        this.serviceMetricDataTypes = serviceMetricDataTypes;
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.metrics;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(ServiceMetricsCollectionConfigProfile previousVersion) {
        return (!this.equals(previousVersion));
    }

    @Override
    public ServiceMetricsCollectionConfigProfile clone() {
        ServiceMetricsCollectionConfigProfile returnValue = (ServiceMetricsCollectionConfigProfile) super.clone();
        returnValue.setMetricConfigParameterMap(getMetricConfigParameterMap());
        returnValue.setRadioTypes(getRadioTypes());
        returnValue.setServiceMetricDataTypes(getServiceMetricDataTypes());
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricConfigParameterMap, radioTypes, serviceMetricDataTypes);
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
                && Objects.equals(radioTypes, other.radioTypes)
                && Objects.equals(serviceMetricDataTypes, other.serviceMetricDataTypes);
    }


}
