package com.telecominfraproject.wlan.profile.metrics;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


class ServiceMetricConfigParametersTest {

    @Test
    void testHashCode() {
        
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS); 
        
        ServiceMetricConfigParameters configParameters2 = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS); 
        
        ServiceMetricConfigParameters configParameters3 = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS + 100,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS + 10);  
        
        
        assert(configParameters.hashCode() == configParameters2.hashCode());
        assert(configParameters.hashCode() != configParameters3.hashCode());

    }

    @Test
    void testServiceMetricConfigParameters() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        assertNotNull(configParameters);
        assertEquals(ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                configParameters.getSamplingInterval());
        assertEquals(ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS,
                configParameters.getReportingIntervalSeconds());
    }

    @Test
    void testNeedsToBeUpdatedOnDevice() {

        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);
        
        
        ServiceMetricConfigParameters configParameters2 = configParameters.clone();
        assertFalse(configParameters.needsToBeUpdatedOnDevice(configParameters2));

        configParameters2.setReportingIntervalSeconds(30);
        
        assertTrue(configParameters2.needsToBeUpdatedOnDevice(configParameters));

        
        
    
    }

    @Test
    void testGetSamplingInterval() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        assertEquals(ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                configParameters.getSamplingInterval());

    }

    @Test
    void testSetSamplingInterval() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        configParameters
                .setSamplingInterval(ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS + 1000);
        assertEquals(ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS + 1000,
                configParameters.getSamplingInterval());
    }

    @Test
    void testGetReportingIntervalSeconds() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        assertEquals(ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                configParameters.getSamplingInterval());
    }

    @Test
    void testSetReportingIntervalSeconds() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        configParameters
                .setReportingIntervalSeconds(ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS + 10);
        assertEquals(ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS + 10,
                configParameters.getReportingIntervalSeconds());
    }

    @Test
    void testGetServiceMetricDataType() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        assertEquals(configParameters.getServiceMetricDataType(), ServiceMetricDataType.Client);
    }

    @Test
    void testSetServiceMetricDataType() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        configParameters.setServiceMetricDataType(ServiceMetricDataType.ClientQoE);

        assertEquals(configParameters.getServiceMetricDataType(), ServiceMetricDataType.ClientQoE);

    }

    @Test
    void testClone() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        assertEquals(configParameters.clone(), configParameters);

    }

    @Test
    void testEqualsObject() {
        ServiceMetricConfigParameters configParameters = new ServiceMetricConfigParameters(ServiceMetricDataType.Client,
                ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        ServiceMetricConfigParameters configParameters2 = new ServiceMetricConfigParameters(
                ServiceMetricDataType.Client, ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);
        
        assertTrue(configParameters.equals(configParameters2));


        ServiceMetricConfigParameters configParameters3 = new ServiceMetricConfigParameters(
                ServiceMetricDataType.ApSsid, ServiceMetricConfigParameterDefaults.DEFAULT_SAMPLE_INTERVAL_MILLIS,
                ServiceMetricConfigParameterDefaults.DEFAULT_REPORT_INTERVAL_SECONDS);

        assertFalse(configParameters.equals(configParameters3));
    }

}
