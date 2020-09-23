package com.telecominfraproject.wlan.profile.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


class MetricsCollectionConfigProfileTest {


    List<RadioType> profileMetrics_3_radioTypes = ImmutableList.of(RadioType.is2dot4GHz, RadioType.is5GHzL,
            RadioType.is5GHzU);
    List<RadioType> profileMetrics_2_radioTypes = ImmutableList.of(RadioType.is2dot4GHz, RadioType.is5GHz);

    List<ServiceMetricDataType> metricDataTypes = ImmutableList.of(ServiceMetricDataType.ApNode,
            ServiceMetricDataType.ApSsid, ServiceMetricDataType.Channel, ServiceMetricDataType.Client,
            ServiceMetricDataType.Neighbour);

    List<ServiceMetricDataType> metricDataTypes_2 = ImmutableList.of(ServiceMetricDataType.ApSsid,
            ServiceMetricDataType.Client, ServiceMetricDataType.Neighbour);


    @Test
    void testGetProfileType() {
        MetricsCollectionConfigProfile metricsProfileDetails_3_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_3_radioTypes);
        assertTrue(metricsProfileDetails_3_radios.getProfileType().equals(ProfileType.metrics));
    }

    @Test
    void testCreateWithDefaults() {
        MetricsCollectionConfigProfile metricsProfileDetails_default_radios = MetricsCollectionConfigProfile
                .createWithDefaults();
        assertTrue(
                metricsProfileDetails_default_radios.getRadioTypeList().equals(Arrays.asList(RadioType.validValues())));
    }

    @Test
    void testCreateWithDefaultsListOfRadioType() {
        MetricsCollectionConfigProfile metricsProfileDetails_3_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_3_radioTypes);
        assertTrue(metricsProfileDetails_3_radios.getRadioTypeList().equals(profileMetrics_3_radioTypes));

        MetricsCollectionConfigProfile metricsProfileDetails_2_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_2_radioTypes);
        assertTrue(metricsProfileDetails_2_radios.getRadioTypeList().equals(profileMetrics_2_radioTypes));


    }

    @Test
    void testMetricsCollectionConfigProfile() {

        MetricsCollectionConfigProfile metricsProfile = new MetricsCollectionConfigProfile(profileMetrics_3_radioTypes,
                metricDataTypes);

        assertNotNull(metricsProfile);
        assertEquals(metricsProfile.getRadioTypeList(), profileMetrics_3_radioTypes);
        assertEquals(metricDataTypes.size(), metricsProfile.getMetricConfigParameterMap().values().size());
        assertEquals(metricsProfile.getProfileType(), ProfileType.metrics);
        for (MetricConfigParameters configParameters : metricsProfile.getMetricConfigParameterMap().values()) {
            assertTrue(metricDataTypes.contains(configParameters.getServiceMetricDataType()));
        }

        MetricsCollectionConfigProfile metricsProfile_2 = new MetricsCollectionConfigProfile(
                profileMetrics_3_radioTypes, metricDataTypes_2);
        assertNotNull(metricsProfile_2);
        assertEquals(metricsProfile_2.getRadioTypeList(), profileMetrics_3_radioTypes);
        assertEquals(metricsProfile_2.getProfileType(), ProfileType.metrics);
        assertEquals(metricDataTypes_2.size(), metricsProfile_2.getMetricConfigParameterMap().values().size());
        for (MetricConfigParameters configParameters : metricsProfile_2.getMetricConfigParameterMap().values()) {
            assertTrue(metricDataTypes_2.contains(configParameters.getServiceMetricDataType()));
        }

        assertNotEquals(metricsProfile, metricsProfile_2);

    }

    @Test
    void testNeedsToBeUpdatedOnDevice() {

        MetricsCollectionConfigProfile metricsProfile = MetricsCollectionConfigProfile.createWithDefaults();
        MetricsCollectionConfigProfile metricsProfile2 = MetricsCollectionConfigProfile.createWithDefaults();

        assertFalse(metricsProfile2.needsToBeUpdatedOnDevice(metricsProfile));
        metricsProfile2.setRadioTypeList(profileMetrics_2_radioTypes);
        assertTrue(metricsProfile2.needsToBeUpdatedOnDevice(metricsProfile));

    }

    @Test
    void testGetRadioTypeList() {
        MetricsCollectionConfigProfile metricsProfile = MetricsCollectionConfigProfile.createWithDefaults();
        MetricsCollectionConfigProfile metricsProfile2 = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_2_radioTypes);

        assertEquals(metricsProfile.getRadioTypeList(), Arrays.asList(RadioType.validValues()));
        assertEquals(metricsProfile2.getRadioTypeList(), profileMetrics_2_radioTypes);

    }

    @Test
    void testSetRadioTypeList() {
        MetricsCollectionConfigProfile metricsProfile = MetricsCollectionConfigProfile.createWithDefaults();
        MetricsCollectionConfigProfile metricsProfile2 = MetricsCollectionConfigProfile.createWithDefaults();

        assertEquals(metricsProfile.getRadioTypeList(), metricsProfile2.getRadioTypeList());

        metricsProfile.setRadioTypeList(profileMetrics_2_radioTypes);
        assertEquals(metricsProfile.getRadioTypeList(), profileMetrics_2_radioTypes);
        assertNotEquals(metricsProfile.getRadioTypeList(), metricsProfile2.getRadioTypeList());
    }

    @Test
    void testGetMetricConfigParameterMap() {
        MetricsCollectionConfigProfile metricsProfile = MetricsCollectionConfigProfile.createWithDefaults();


        assertTrue(metricsProfile.getMetricConfigParameterMap().size() == metricDataTypes.size());
        metricDataTypes.stream().forEach(d -> {
            assertTrue(metricsProfile.getMetricConfigParameterMap().containsKey(d.getName()));
        });


    }

    @Test
    void testSetMetricConfigParameterMap() {
        MetricsCollectionConfigProfile metricsProfile = MetricsCollectionConfigProfile.createWithDefaults();
        assertTrue(metricsProfile.getMetricConfigParameterMap().values().size() == metricDataTypes.size());
        metricDataTypes.stream().forEach(d -> {
            assertTrue(metricsProfile.getMetricConfigParameterMap().containsKey(d.getName()));
        });
        
        
        List<RadioType> profileMetrics_all_radioTypes = ImmutableList.of(RadioType.is2dot4GHz, RadioType.is5GHz, RadioType.is5GHzL,
                RadioType.is5GHzU);
        MetricsCollectionConfigProfile metricsProfile2 = new MetricsCollectionConfigProfile(profileMetrics_all_radioTypes,metricDataTypes_2);
        


        assertNotEquals(metricsProfile.getMetricConfigParameterMap(), metricsProfile2.getMetricConfigParameterMap());
        
        metricsProfile.setMetricConfigParameterMap(metricsProfile2.getMetricConfigParameterMap());
        assertEquals(metricsProfile.getMetricConfigParameterMap(), metricsProfile2.getMetricConfigParameterMap());
        
        
    }

    @Test
    void testClone() {
        MetricsCollectionConfigProfile metricsProfileDetails_default_radios = MetricsCollectionConfigProfile
                .createWithDefaults();

        MetricsCollectionConfigProfile metricsProfileDetails_3_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_3_radioTypes);

        MetricsCollectionConfigProfile metricsProfileDetails_2_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_2_radioTypes);


        assertEquals(metricsProfileDetails_default_radios, metricsProfileDetails_default_radios.clone());
        assertEquals(metricsProfileDetails_3_radios, metricsProfileDetails_3_radios.clone());
        assertEquals(metricsProfileDetails_2_radios, metricsProfileDetails_2_radios.clone());

        assertNotEquals(metricsProfileDetails_default_radios, metricsProfileDetails_2_radios.clone());
        assertNotEquals(metricsProfileDetails_3_radios, metricsProfileDetails_2_radios.clone());
        assertNotEquals(metricsProfileDetails_default_radios, metricsProfileDetails_3_radios.clone());
    }

    @Test
    void testEqualsObject() {
        MetricsCollectionConfigProfile metricsProfileDetails_default_radios = MetricsCollectionConfigProfile
                .createWithDefaults();
        MetricsCollectionConfigProfile metricsProfileDetails_default_radios1 = MetricsCollectionConfigProfile
                .createWithDefaults();
        MetricsCollectionConfigProfile metricsProfileDetails_3_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_3_radioTypes);
        MetricsCollectionConfigProfile metricsProfileDetails_3_radios1 = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_3_radioTypes);
        MetricsCollectionConfigProfile metricsProfileDetails_2_radios = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_2_radioTypes);
        MetricsCollectionConfigProfile metricsProfileDetails_2_radios1 = MetricsCollectionConfigProfile
                .createWithDefaults(profileMetrics_2_radioTypes);

        assertEquals(metricsProfileDetails_default_radios, metricsProfileDetails_default_radios1);
        assertEquals(metricsProfileDetails_3_radios, metricsProfileDetails_3_radios1);
        assertEquals(metricsProfileDetails_2_radios, metricsProfileDetails_2_radios1);

        assertNotEquals(metricsProfileDetails_2_radios, metricsProfileDetails_3_radios);
        assertNotEquals(metricsProfileDetails_default_radios, metricsProfileDetails_3_radios);
        assertNotEquals(metricsProfileDetails_default_radios, metricsProfileDetails_2_radios);

    }

}
