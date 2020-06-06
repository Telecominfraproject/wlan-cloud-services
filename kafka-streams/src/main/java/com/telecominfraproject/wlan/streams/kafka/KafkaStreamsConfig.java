package com.telecominfraproject.wlan.streams.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@Configuration
public class KafkaStreamsConfig {

    	private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsConfig.class);
	
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;
    	
        @Bean
        public StreamInterface<ServiceMetric> metricStreamInterface(@Autowired Producer<String,  byte[]> producer) {
        	StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using kafka stream for the metrics");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                	LOG.trace("publishing metric {}", record);
                	String recordKey = record.getCustomerId() + "_" + record.getEquipmentId() + "_" + record.getClientMac() + "_" + record.getDataType(); 
                	producer.send(new ProducerRecord<String, byte[]>(wlanServiceMetricsTopic, recordKey, record.toZippedBytes()));
                }
                
              };
              
              return si;
        }
        
        @Bean
        public StreamInterface<SystemEventRecord> eventStreamInterface(@Autowired Producer<String,  byte[]> producer) {
        	StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using kafka stream for the system events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                	LOG.trace("publishing system event {}", record);
                	String recordKey = record.getCustomerId() + "_" + record.getEquipmentId() + "_" + record.getDataType(); 
                	producer.send(new ProducerRecord<String, byte[]>(systemEventsTopic, recordKey, record.toZippedBytes()));                	
                }
                
              };
              
              return si;
        }

}
