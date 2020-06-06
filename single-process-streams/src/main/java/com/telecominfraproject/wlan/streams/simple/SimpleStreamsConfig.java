package com.telecominfraproject.wlan.streams.simple;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@Configuration
@EnableScheduling
public class SimpleStreamsConfig {

	    private static final Logger LOG = LoggerFactory.getLogger(SimpleStreamsConfig.class);
	    
	    @Value("${tip.wlan.serviceMetricsQueueMax:5000}") 
	    private int serviceMetricsQueueMax;
	
	    @Value("${tip.wlan.systemEventsQueueMax:5000}") 
	    private int systemEventsQueueMax;
	
	    @Value("${tip.wlan.purgeStreamRecordsOlderThanSec:600}") 
	    private int purgeStreamRecordsOlderThanSec;
	
	    
	    private LinkedBlockingDeque<ServiceMetric> serviceMetricsQueue;
	    private LinkedBlockingDeque<SystemEventRecord> systemEventsQueue;  

	    //TODO: figure out how to handle consumers:
	    //NO - one separate blocking queue per StreamProcessor for each of the subscribed topics
	    //
		// * - have a special StreamRecordMover thread moving records from incoming
		// queues into each registered
		// StreamProcessor ( SP can maintain its own queue if needed) - this way record
		// can be removed from the incoming queue once it has been delivered to all
		// interested SPs. Each SP registers itself as an Autowired Bean.
		// StreamRecordMover thread will iterate through all registered beans and will
		// deliver each incoming record to each SP Bean.
	    // StreamProcessor.push(String topic, BaseJsonModel record){
	    //    if(myTopicsSet.contains(topic) && record instanceOf ServiceMetric) {
	    //			-- place record into the internal blocking queue, if cannot - do SP-specific draining of the internal queue
	    //	  }
	    // }
	    //
		// NO - or have each SP do its own polling and populating of its internal queue -
		// but here how would we know when to remove the record from the incoming queue
	    //
	    
	    @PostConstruct
	    private void postCreate() {
		    serviceMetricsQueue = new LinkedBlockingDeque<>(serviceMetricsQueueMax);
		    systemEventsQueue = new LinkedBlockingDeque<>(systemEventsQueueMax);  
	    }

	
        @Bean
        public StreamInterface<ServiceMetric> metricStreamInterface() {
        	StreamInterface<ServiceMetric> si = new StreamInterface<ServiceMetric>() {

                {
                    LOG.info("*** Using simple stream for the metrics");
                }
                
                @Override
                public void publish(ServiceMetric record) {
                	LOG.info("publishing metric {}", record);
                	boolean elementAdded = false;
                	while(!elementAdded) {
	                	try {
	                		elementAdded = serviceMetricsQueue.offer(record, 5, TimeUnit.SECONDS);
						} catch (InterruptedException e) {
							LOG.warn("Interrupted while waiting for the queue to be consumed");
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
                	LOG.info("publishing system event {}", record);
                	
                	boolean elementAdded = false;
                	while(!elementAdded) {
	                	try {
	                		elementAdded = systemEventsQueue.offer(record, 5, TimeUnit.SECONDS);
						} catch (InterruptedException e) {
							LOG.warn("Interrupted while waiting for the queue to be consumed");
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

        	LOG.info("removeOldMetricsAndEvents periodic task started, removing stream records older than {}", createdBeforeMs);

        	serviceMetricsQueue.removeIf(sm -> sm.getCreatedTimestamp() < createdBeforeMs);
        	systemEventsQueue.removeIf(se -> se.getEventTimestamp() < createdBeforeMs);
        	
        }
}
