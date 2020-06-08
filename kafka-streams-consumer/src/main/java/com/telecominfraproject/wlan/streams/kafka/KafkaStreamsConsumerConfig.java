package com.telecominfraproject.wlan.streams.kafka;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.stream.StreamMessageDispatcher;

/**
 * @author dtop
 * This class configures consumers of the messages read from Kafka - System Events and Service Metrics
 */
@Configuration
public class KafkaStreamsConsumerConfig {

    	private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsConsumerConfig.class);
	
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;
    	
        @Autowired
        private StreamMessageDispatcher streamMessageDispatcher;

    	@Autowired 
    	private Consumer<String,  byte[]> consumer;

	    @PostConstruct
	    private void postCreate() {
	    	
	    	Thread streamReaderThread = new Thread( new Runnable() {
				
				@Override
				public void run() {
					LOG.info("Starting streamReaderThread");
					while(true) {
						List<QueuedStreamMessage> messages = poll();
						messages.forEach(msg -> {
		                	LOG.trace("Read message {}", msg);
							streamMessageDispatcher.push(msg);
						});
					}
				}
			}, "streamReaderThread"); 
	    	
	    	streamReaderThread.setDaemon(true);
	    	streamReaderThread.start();
	    }

	    private List<QueuedStreamMessage> poll() {
	    	ConsumerRecords<String, byte[]> consumerRecords;
	    	
	    	// Consume messages from Kafka cluster
	    	try {
	    		consumerRecords = consumer.poll(Duration.of(5, ChronoUnit.SECONDS));
	    	}catch(Exception e) {
				LOG.error("Got exception when polling Kafka ", e);
				
				//back off a bit
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// nothing to do here
				}
				
				return Collections.emptyList();
			}

	    	// Decode consumed messages into subclasses of BaseJsonModel
	    	List<QueuedStreamMessage> ret = new ArrayList<>(consumerRecords.count());
	    	
			consumerRecords.forEach(cr -> {
				try {
					ret.add(new QueuedStreamMessage(cr.topic(),
						BaseJsonModel.fromZippedBytes(cr.value(), BaseJsonModel.class)));
				}catch(RuntimeException e) {
					LOG.error("Failed to decode BaseJsonModel from topic {} for key {}", cr.topic(), cr.key());
				}
			});
	    	
	    	return ret;
	    }
}
