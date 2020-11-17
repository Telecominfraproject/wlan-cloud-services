package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.TestSystemEvent;

//NOTE: these profiles will be ADDED to the list of active profiles  
@ActiveProfiles(profiles = {
      "integration_test",
      "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
      }) 
public class CloudEventDispatcherRemoteTest extends BaseRemoteTest {

    private static final Logger LOG = LoggerFactory.getLogger(CloudEventDispatcherRemoteTest.class);

    private static final int METRICS_COUNTS = 20;

    @Autowired CloudEventDispatcherInterface remoteInterface;

    private int customerId = getNextCustomerId();
    private long equipmentId = getNextEquipmentId();

    @Configuration
    public static class Config {

        @Bean
        public StreamInterface<ServiceMetric> metricStreamInterface() {
        	StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using empty stream for the service_metrics_collection_config");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                	LOG.info("publishing metric {}", record);
                	
                }
                
              };
              
              return si;
        }
        
        @Bean
        public StreamInterface<SystemEventRecord> eventStreamInterface() {
        	StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using empty stream for the system events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                	LOG.info("publishing system event {}", record);
                	
                }
                
              };
              
              return si;
        }

    }
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.cloudEventDispatcherBaseUrl");
    }
    
    @Test
    public void testPublishMetric() throws Exception {
    	ServiceMetric metricRecord = new ServiceMetric();
        metricRecord.setCustomerId(customerId);
        metricRecord.setCreatedTimestamp(System.currentTimeMillis());
        metricRecord.setEquipmentId(equipmentId);
        
        remoteInterface.publishMetric(metricRecord);
    }

    @Test
    public void testPublishMetricList() throws Exception {
        List<ServiceMetric> recordList = new ArrayList<>(METRICS_COUNTS);
        for (int i = 0; i < METRICS_COUNTS; ++i) {
        	ServiceMetric metricRecord = new ServiceMetric();
            metricRecord.setCustomerId(customerId);
            metricRecord.setCreatedTimestamp(System.currentTimeMillis());
            metricRecord.setEquipmentId(equipmentId);
            recordList.add(metricRecord);
        }
        
        remoteInterface.publishMetrics(recordList);
    }

    @Test
    public void testPublishEvent() throws Exception {
        
        TestSystemEvent testEvent = new TestSystemEvent();
        testEvent.setCustomerId(customerId);
        testEvent.setEventTimestamp(System.currentTimeMillis());
        
        remoteInterface.publishEvent(testEvent);
    }

    @Test
    public void testPublishEvents() throws Exception {
        
        List<SystemEventRecord> events = new ArrayList<>();
        
        TestSystemEvent testEvent1 = new TestSystemEvent();
        testEvent1.setCustomerId(customerId);
        testEvent1.setEventTimestamp(System.currentTimeMillis());
        events.add(new SystemEventRecord(testEvent1));

        TestSystemEvent testEvent2 = new TestSystemEvent();
        testEvent2.setCustomerId(customerId);
        testEvent2.setEventTimestamp(System.currentTimeMillis()+1);
        events.add(new SystemEventRecord(testEvent2));

        remoteInterface.publishEvents(events);
    }      

}
