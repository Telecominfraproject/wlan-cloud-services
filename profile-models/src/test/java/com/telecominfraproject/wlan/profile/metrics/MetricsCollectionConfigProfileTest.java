package com.telecominfraproject.wlan.profile.metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


class MetricsCollectionConfigProfileTest {


    Set<RadioType> profileMetrics_3_radioTypes = ImmutableSet.of(RadioType.is2dot4GHz, RadioType.is5GHzL,
            RadioType.is5GHzU);
    Set<RadioType> profileMetrics_2_radioTypes = ImmutableSet.of(RadioType.is2dot4GHz, RadioType.is5GHz);

    Set<ServiceMetricDataType> metricDataTypes = ImmutableSet.of(ServiceMetricDataType.ApNode,
            ServiceMetricDataType.ApSsid, ServiceMetricDataType.Channel, ServiceMetricDataType.Client,
            ServiceMetricDataType.Neighbour);

    Set<ServiceMetricDataType> metricDataTypes_2 = ImmutableSet.of(ServiceMetricDataType.ApSsid,
            ServiceMetricDataType.Client, ServiceMetricDataType.Neighbour);

    ServiceMetricsCollectionConfigProfile metricsProfileDetails_3_radios;

    ServiceMetricsCollectionConfigProfile metricsProfileDetails_2_radios;

    @BeforeEach
    public void initialize() {
        metricsProfileDetails_3_radios = ServiceMetricsCollectionConfigProfile.createWithDefaults();
        metricsProfileDetails_3_radios.setAllNetworkConfigParametersToDefaults(profileMetrics_3_radioTypes,
                metricDataTypes, true);

        metricsProfileDetails_2_radios = ServiceMetricsCollectionConfigProfile.createWithDefaults();
        metricsProfileDetails_2_radios.setAllNetworkConfigParametersToDefaults(profileMetrics_2_radioTypes,
                metricDataTypes, true);
    }

    @AfterEach
    private void deleteInstances() {
        metricsProfileDetails_3_radios = null;
        metricsProfileDetails_2_radios = null;
    }

    @Test
    void testGetProfileType() {
        assertTrue(metricsProfileDetails_3_radios.getProfileType().equals(ProfileType.service_metrics_collection_config));
    }

    @Test
    void testCreateWithDefaults() {

        assertTrue(metricsProfileDetails_3_radios.getRadioTypes().equals(profileMetrics_3_radioTypes));

        assertTrue(metricsProfileDetails_2_radios.getRadioTypes().equals(profileMetrics_2_radioTypes));
    }


    @Test
    void testNeedsToBeUpdatedOnDevice() {

        ServiceMetricsCollectionConfigProfile metricsProfileDetails_3_radios_2 = ServiceMetricsCollectionConfigProfile
                .createWithDefaults();
        metricsProfileDetails_3_radios_2.setAllNetworkConfigParametersToDefaults(profileMetrics_3_radioTypes,
                metricDataTypes, true);


        assertFalse(metricsProfileDetails_3_radios_2.needsToBeUpdatedOnDevice(metricsProfileDetails_3_radios));
        metricsProfileDetails_3_radios_2.setServiceMetricDataTypes(metricDataTypes_2);
        assertTrue(metricsProfileDetails_3_radios_2.needsToBeUpdatedOnDevice(metricsProfileDetails_3_radios));

    }

    @Test
    void testGetRadioTypeList() {
        assertEquals(metricsProfileDetails_3_radios.getRadioTypes(), profileMetrics_3_radioTypes);
        assertEquals(metricsProfileDetails_2_radios.getRadioTypes(), profileMetrics_2_radioTypes);

    }

    @Test
    void testSetRadioTypeList() {


        assertEquals(metricsProfileDetails_3_radios.getRadioTypes(), profileMetrics_3_radioTypes);

        metricsProfileDetails_3_radios.setRadioTypes(profileMetrics_2_radioTypes);
        assertEquals(metricsProfileDetails_3_radios.getRadioTypes(), profileMetrics_2_radioTypes);
        assertNotEquals(metricsProfileDetails_3_radios.getRadioTypes(), profileMetrics_3_radioTypes);

    }

    @Test
    void testGetMetricConfigParameterMap() {

        metricDataTypes.stream().forEach(d -> {

            assertTrue(metricsProfileDetails_3_radios.getMetricConfigParameterMap().containsKey(d));
            assertTrue(metricsProfileDetails_2_radios.getMetricConfigParameterMap().containsKey(d));

            if (d.equals(ServiceMetricDataType.ApNode) || d.equals(ServiceMetricDataType.Neighbour)
                    || d.equals(ServiceMetricDataType.Channel)) {
                assertTrue(metricsProfileDetails_3_radios.getMetricConfigParameterMap().get(d).size() == 6);
                assertTrue(metricsProfileDetails_2_radios.getMetricConfigParameterMap().get(d).size() == 4);
            } 
            
            if (d.equals(ServiceMetricDataType.Client) || d.equals(ServiceMetricDataType.ApSsid)) {
                assertTrue(metricsProfileDetails_3_radios.getMetricConfigParameterMap().get(d).size() == 3);
                assertTrue(metricsProfileDetails_2_radios.getMetricConfigParameterMap().get(d).size() == 2);
            }

            if (d.equals(ServiceMetricDataType.ClientQoE) || d.equals(ServiceMetricDataType.QoE)) {
                assertTrue(metricsProfileDetails_3_radios.getMetricConfigParameterMap().get(d).size() == 1);
                assertTrue(metricsProfileDetails_2_radios.getMetricConfigParameterMap().get(d).size() == 1);
            }


        });


    }


    @Test
    void testClone() {

        assertEquals(metricsProfileDetails_3_radios, metricsProfileDetails_3_radios.clone());
        assertEquals(metricsProfileDetails_2_radios, metricsProfileDetails_2_radios.clone());

        assertNotEquals(metricsProfileDetails_3_radios, metricsProfileDetails_2_radios.clone());
        assertNotEquals(metricsProfileDetails_2_radios, metricsProfileDetails_3_radios.clone());
    }

    @Test
    void testEqualsObject() {

        ServiceMetricsCollectionConfigProfile metricsProfileDetails_3_radios2 = ServiceMetricsCollectionConfigProfile
                .createWithDefaults();
        metricsProfileDetails_3_radios2.setAllNetworkConfigParametersToDefaults(profileMetrics_3_radioTypes,
                metricDataTypes, true);

        ServiceMetricsCollectionConfigProfile metricsProfileDetails_2_radios2 = ServiceMetricsCollectionConfigProfile
                .createWithDefaults();
        metricsProfileDetails_2_radios2.setAllNetworkConfigParametersToDefaults(profileMetrics_2_radioTypes,
                metricDataTypes, true);


        assertEquals(metricsProfileDetails_3_radios, metricsProfileDetails_3_radios2);
        assertEquals(metricsProfileDetails_2_radios, metricsProfileDetails_2_radios2);

        assertNotEquals(metricsProfileDetails_2_radios, metricsProfileDetails_3_radios);
        assertNotEquals(metricsProfileDetails_3_radios, metricsProfileDetails_2_radios);

    }

}
