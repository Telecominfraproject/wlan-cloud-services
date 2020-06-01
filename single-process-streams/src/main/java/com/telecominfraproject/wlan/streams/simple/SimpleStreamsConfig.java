package com.telecominfraproject.wlan.streams.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@Configuration
public class SimpleStreamsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleStreamsConfig.class);
	
        @Bean
        public StreamInterface<ServiceMetric> metricStreamInterface() {
        	StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using simple stream for the metrics");
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
                    LOG.info("*** Using simple stream for the system events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                	LOG.info("publishing system event {}", record);
                	
                }
                
              };
              
              return si;
        }

}
