package com.telecominfraproject.wlan.streams.kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.servo.DefaultMonitorRegistry;
import com.netflix.servo.monitor.BasicCounter;
import com.netflix.servo.monitor.BasicTimer;
import com.netflix.servo.monitor.Counter;
import com.netflix.servo.monitor.MonitorConfig;
import com.netflix.servo.monitor.Stopwatch;
import com.netflix.servo.monitor.Timer;
import com.netflix.servo.tag.TagList;
import com.telecominfraproject.wlan.cloudmetrics.CloudMetricsTags;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 * This class configures producers of the messages to be pushed into Kafka - System Events and Service Metrics
 */
@Configuration
public class KafkaStreamsConfig {

    	private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsConfig.class);
	
        private final TagList tags = CloudMetricsTags.commonTags;

        private final Counter metricsMessagesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-wlan-metrics-msg-written").withTags(tags).build());
        private final Counter metricsBytesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-wlan-metrics-bytes-written").withTags(tags).build());
        private final Counter metricsMessagesErrorsCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-wlan-metrics-msg-errors").withTags(tags).build());
        private final Timer metricsMessagesTimer = new BasicTimer(MonitorConfig.builder("kafka-producer-wlan-metrics-msgTimer").withTags(tags).build());

        private final Counter locationMetricsMessagesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-locationMetrics-msg-written").withTags(tags).build());
        private final Counter locationMetricsBytesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-locationMetrics-bytes-written").withTags(tags).build());
        private final Counter locationMetricsMessagesErrorsCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-locationMetrics-msg-errors").withTags(tags).build());
        private final Timer locationMetricsMessagesTimer = new BasicTimer(MonitorConfig.builder("kafka-producer-locationMetrics-msgTimer").withTags(tags).build());

        
        private final Counter eventsMessagesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-events-msg-written").withTags(tags).build());
        private final Counter eventsBytesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-events-bytes-written").withTags(tags).build());
        private final Counter eventsMessagesErrorsCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-events-msg-errors").withTags(tags).build());
        private final Timer eventsMessagesTimer = new BasicTimer(MonitorConfig.builder("kafka-producer-events-msgTimer").withTags(tags).build());

        private final Counter customerEventsMessagesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-customerEvents-msg-written").withTags(tags).build());
        private final Counter customerEventsBytesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-customerEvents-bytes-written").withTags(tags).build());
        private final Counter customerEventsMessagesErrorsCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-customerEvents-msg-errors").withTags(tags).build());
        private final Timer customerEventsMessagesTimer = new BasicTimer(MonitorConfig.builder("kafka-producer-customerEvents-msgTimer").withTags(tags).build());

        private final Counter locationEventsMessagesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-locationEvents-msg-written").withTags(tags).build());
        private final Counter locationEventsBytesWrittenCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-locationEvents-bytes-written").withTags(tags).build());
        private final Counter locationEventsMessagesErrorsCounter = new BasicCounter(MonitorConfig.builder("kafka-producer-locationEvents-msg-errors").withTags(tags).build());
        private final Timer locationEventsMessagesTimer = new BasicTimer(MonitorConfig.builder("kafka-producer-locationEvents-msgTimer").withTags(tags).build());

        
        // dtop: use anonymous constructor to ensure that the following code always
        // get executed,
        // even when somebody adds another constructor in here
        {
            DefaultMonitorRegistry.getInstance().register(metricsMessagesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(metricsBytesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(metricsMessagesErrorsCounter);
            DefaultMonitorRegistry.getInstance().register(metricsMessagesTimer);

            DefaultMonitorRegistry.getInstance().register(locationMetricsMessagesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(locationMetricsBytesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(locationMetricsMessagesErrorsCounter);
            DefaultMonitorRegistry.getInstance().register(locationMetricsMessagesTimer);

            DefaultMonitorRegistry.getInstance().register(eventsMessagesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(eventsBytesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(eventsMessagesErrorsCounter);
            DefaultMonitorRegistry.getInstance().register(eventsMessagesTimer);

            DefaultMonitorRegistry.getInstance().register(customerEventsMessagesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(customerEventsBytesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(customerEventsMessagesErrorsCounter);
            DefaultMonitorRegistry.getInstance().register(customerEventsMessagesTimer);

            DefaultMonitorRegistry.getInstance().register(locationEventsMessagesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(locationEventsBytesWrittenCounter);
            DefaultMonitorRegistry.getInstance().register(locationEventsMessagesErrorsCounter);
            DefaultMonitorRegistry.getInstance().register(locationEventsMessagesTimer);

        }
        
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;

        @Value("${tip.wlan.locationMetricsTopic:location_metrics}")
        private String locationMetricsTopic;

        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;

        @Value("${tip.wlan.customerEventsTopic:customer_events}")
    	private String customerEventsTopic;

        @Value("${tip.wlan.locationEventsTopic:location_events}")
        private String locationEventsTopic;

        /**
         * @param producer
         * @return interface for publishing wlan service metrics into a topic that is partitioned by customerId, equipmentId, clientMac and dataType.
         */
        @Bean
        public StreamInterface<ServiceMetric> metricStreamInterface(@Autowired Producer<String,  byte[]> producer) {
        	StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using kafka stream for the metrics");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                	LOG.trace("publishing metric {}", record);
                	
                	metricsMessagesWrittenCounter.increment();
                    Stopwatch stopwatch = metricsMessagesTimer.start();
                    boolean success = false;
                    
                    try {
                        String recordKey = record.getCustomerId() + "_" + record.getLocationId() + "_" + record.getEquipmentId() + "_" + record.getClientMac() + "_" + record.getDataType();
                        byte[] payload = record.toZippedBytes();
                        metricsBytesWrittenCounter.increment(payload.length);
                        producer.send(new ProducerRecord<String, byte[]>(wlanServiceMetricsTopic, recordKey, payload));
                        success = true;
                    } finally {
                        stopwatch.stop();
                        if(!success) {
                            metricsMessagesErrorsCounter.increment();
                        }
                    }

                }
                
              };
              
              return si;
        }

        /**
         * @param producer
         * @return interface for publishing service metrics into a topic that is partitioned by locationId.
         */
        @Bean
        public StreamInterface<ServiceMetric> locationMetricStreamInterface(@Autowired Producer<String,  byte[]> producer) {
            StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using kafka stream for the location metrics");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                    LOG.trace("publishing metric {}", record);
                    
                    locationMetricsMessagesWrittenCounter.increment();
                    Stopwatch stopwatch = locationMetricsMessagesTimer.start();
                    boolean success = false;
                    
                    try {
                        String recordKey = "" + record.getLocationId();
                        byte[] payload = record.toZippedBytes();
                        locationMetricsBytesWrittenCounter.increment(payload.length);
                        producer.send(new ProducerRecord<String, byte[]>(locationMetricsTopic, recordKey, payload));
                        success = true;
                    } finally {
                        stopwatch.stop();
                        if(!success) {
                            locationMetricsMessagesErrorsCounter.increment();
                        }
                    }

                }
                
              };
              
              return si;
        }

        /**
         * @param producer
         * @return interface for publishing system events into a topic that is partitioned by customerId, equipmentId and dataType.
         */
        @Bean
        public StreamInterface<SystemEventRecord> eventStreamInterface(@Autowired Producer<String,  byte[]> producer) {
        	StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using kafka stream for the system events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                	LOG.trace("publishing system event {}", record);
                	
                    eventsMessagesWrittenCounter.increment();
                    Stopwatch stopwatch = eventsMessagesTimer.start();
                    boolean success = false;
                    
                    try {
                        String recordKey = record.getCustomerId() + "_" + record.getLocationId() + "_" + record.getEquipmentId() + "_" + record.getDataType(); 
                        byte[] payload = record.toZippedBytes();
                        eventsBytesWrittenCounter.increment(payload.length);                        
                        producer.send(new ProducerRecord<String, byte[]>(systemEventsTopic, recordKey, payload));
                        success = true;
                    } finally {
                        stopwatch.stop();
                        if(!success) {
                            eventsMessagesErrorsCounter.increment();
                        }
                    }
                	
                }
                
              };
              
              return si;
        }

        /**
		 * @param producer
		 * @return interface for publishing system invents into a topic that is
		 *         partitioned only by customerId - used to combine results of partial
		 *         aggregations into customer-centric view
		 */
        @Bean
        public StreamInterface<SystemEventRecord> customerEventStreamInterface(@Autowired Producer<String,  byte[]> producer) {
        	StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using kafka stream for the customer events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                	LOG.trace("publishing customer event {}", record);
                	
                    customerEventsMessagesWrittenCounter.increment();
                    Stopwatch stopwatch = customerEventsMessagesTimer.start();
                    boolean success = false;
                    
                    try {
                        String recordKey = record.getCustomerId() + "_" + record.getDataType(); 
                        byte[] payload = record.toZippedBytes();
                        customerEventsBytesWrittenCounter.increment(payload.length);
                        producer.send(new ProducerRecord<String, byte[]>(customerEventsTopic, recordKey, payload));                  
                        success = true;
                    } finally {
                        stopwatch.stop();
                        if(!success) {
                            customerEventsMessagesErrorsCounter.increment();
                        }
                    }

                }
                
              };
              
              return si;
        }


        /**
         * @param producer
         * @return interface for publishing system invents into a topic that is
         *         partitioned only by locationId - used to combine results of partial
         *         aggregations into location-centric view
         */
        @Bean
        public StreamInterface<SystemEventRecord> locationEventStreamInterface(@Autowired Producer<String,  byte[]> producer) {
            StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using kafka stream for the location events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                    LOG.trace("publishing location event {}", record);
                    
                    locationEventsMessagesWrittenCounter.increment();
                    Stopwatch stopwatch = locationEventsMessagesTimer.start();
                    boolean success = false;
                    
                    try {
                        String recordKey = "" + record.getLocationId(); 
                        byte[] payload = record.toZippedBytes();
                        locationEventsBytesWrittenCounter.increment(payload.length);
                        producer.send(new ProducerRecord<String, byte[]>(locationEventsTopic, recordKey, payload));                  
                        success = true;
                    } finally {
                        stopwatch.stop();
                        if(!success) {
                            locationEventsMessagesErrorsCounter.increment();
                        }
                    }

                }
                
              };
              
              return si;
        }

}
