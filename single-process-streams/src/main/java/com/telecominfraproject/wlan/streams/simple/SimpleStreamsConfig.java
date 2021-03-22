package com.telecominfraproject.wlan.streams.simple;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.stream.StreamMessageDispatcher;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 *	This class configures simple implementation of stream producers and consumers that are co-located in the same java process.
 */
@Configuration
@EnableScheduling
public class SimpleStreamsConfig {

	    private static final Logger LOG = LoggerFactory.getLogger(SimpleStreamsConfig.class);
	    
	    @Value("${tip.wlan.simpleStreamQueueMax:5000}") 
	    private int simpleStreamQueueMax;
		
	    @Value("${tip.wlan.purgeStreamRecordsOlderThanSec:600}") 
	    private int purgeStreamRecordsOlderThanSec;
	
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;
        
        @Value("${tip.wlan.customerEventsTopic:customer_events}")
    	private String customerEventsTopic;
        
        @Value("${tip.wlan.locationMetricsTopic:location_metrics}")
        private String locationMetricsTopic;

        @Value("${tip.wlan.locationEventsTopic:location_events}")
        private String locationEventsTopic;
        
    	
        @Autowired
        private StreamMessageDispatcher streamMessageDispatcher;

	    private LinkedBlockingDeque<QueuedStreamMessage> simpleStreamQueue;
	    
	    @PostConstruct
	    private void postCreate() {
	    	simpleStreamQueue = new LinkedBlockingDeque<>(simpleStreamQueueMax);
	    	
	    	Thread streamReaderThread = new Thread( new Runnable() {
				
				@Override
				public void run() {
					LOG.info("Starting streamReaderThread");
					while(true) {
						QueuedStreamMessage msg = poll();
	                	LOG.trace("Read message {}", msg);
						streamMessageDispatcher.push(msg);
					}
				}
			}, "streamReaderThread"); 
	    	
	    	streamReaderThread.setDaemon(true);
	    	streamReaderThread.start();
	    }

	
        @Bean
        public StreamInterface<ServiceMetric> metricStreamInterface() {
        	StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using simple stream for the metrics");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                	LOG.debug("publishing metric {}", record);
                	boolean elementAdded = false;
                	while(!elementAdded) {
	                	try {
	                		elementAdded = simpleStreamQueue.offer(new QueuedStreamMessage(wlanServiceMetricsTopic, record), 5, TimeUnit.SECONDS);
                            LOG.trace("publishing metric completed");
						} catch (InterruptedException e) {
							LOG.warn("Interrupted while waiting for the message to be added to the queue");
							Thread.currentThread().interrupt();
							break;
						}
                	}
                }
                
              };
              
              return si;
        }

        @Bean
        public StreamInterface<ServiceMetric> locationMetricStreamInterface() {
            StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using simple stream for the location metrics");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                    LOG.debug("publishing location metric {}", record);
                    boolean elementAdded = false;
                    while(!elementAdded) {
                        try {
                            elementAdded = simpleStreamQueue.offer(new QueuedStreamMessage(locationMetricsTopic, record), 5, TimeUnit.SECONDS);
                            LOG.trace("publishing location metric completed");
                        } catch (InterruptedException e) {
                            LOG.warn("Interrupted while waiting for the message to be added to the queue");
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
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
                	LOG.debug("publishing system event {}", record);
                	
                	boolean elementAdded = false;
                	while(!elementAdded) {
	                	try {
	                		elementAdded = simpleStreamQueue.offer(new QueuedStreamMessage(systemEventsTopic, record), 5, TimeUnit.SECONDS);
	                    	LOG.trace("publishing system event completed");
						} catch (InterruptedException e) {
							LOG.warn("Interrupted while waiting for the message to be added to the queue");
							Thread.currentThread().interrupt();
							break;
						}
                	}

                }
                
              };
              
              return si;
        }
        
        @Bean
        public StreamInterface<SystemEventRecord> customerEventStreamInterface() {
        	StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using simple stream for the customer events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                	LOG.debug("publishing customer event {}", record);
                	
                	boolean elementAdded = false;
                	while(!elementAdded) {
	                	try {
	                		elementAdded = simpleStreamQueue.offer(new QueuedStreamMessage(customerEventsTopic, record), 5, TimeUnit.SECONDS);
	                    	LOG.trace("publishing customer event completed");
						} catch (InterruptedException e) {
							LOG.warn("Interrupted while waiting for the message to be added to the queue");
							Thread.currentThread().interrupt();
							break;
						}
                	}

                }
                
              };
              
              return si;
        }

        @Bean
        public StreamInterface<SystemEventRecord> locationEventStreamInterface() {
            StreamInterface<SystemEventRecord> si = new StreamInterface<SystemEventRecord>() {

                {
                    LOG.info("*** Using simple stream for the location events");
                }
                
                @Override
                public void publish(SystemEventRecord record) {
                    LOG.debug("publishing location event {}", record);
                    
                    boolean elementAdded = false;
                    while(!elementAdded) {
                        try {
                            elementAdded = simpleStreamQueue.offer(new QueuedStreamMessage(locationEventsTopic, record), 5, TimeUnit.SECONDS);
                            LOG.trace("publishing location event completed");
                        } catch (InterruptedException e) {
                            LOG.warn("Interrupted while waiting for the message to be added to the queue");
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }

                }
                
              };
              
              return si;
        }

        @Scheduled(initialDelay=60000, fixedDelay=60000)
        public void removeOldMetricsAndEvents(){
        	//remove everything older than x seconds
        	long createdBeforeMs = System.currentTimeMillis() - (purgeStreamRecordsOlderThanSec * 1000);

        	LOG.debug("removeOldMetricsAndEvents periodic task started, removing stream records older than {}", createdBeforeMs);

			simpleStreamQueue.removeIf(sm -> sm.getProducedTimestampMs() < createdBeforeMs);
        	
        }
        
        /**
         * This method will block until a message becomes available in the queue.
         * 
         * @return next queued message
         */
        public QueuedStreamMessage poll() {
        	QueuedStreamMessage ret = null;
        	
        	while(ret == null) {
            	try {
            		ret = simpleStreamQueue.poll(5, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					LOG.warn("Interrupted while waiting for the message to be read from the queue");
					Thread.currentThread().interrupt();
					break;
				}
        	}
        	
        	return ret;
        }
}
