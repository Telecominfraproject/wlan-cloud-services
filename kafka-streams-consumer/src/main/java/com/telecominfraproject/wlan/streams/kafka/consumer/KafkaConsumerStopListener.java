package com.telecominfraproject.wlan.streams.kafka.consumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * Register for container stop event so that we can close the kafka consumer
 * 
 * @author dtop
 *
 */
@Component
public class KafkaConsumerStopListener implements ApplicationListener<ContextClosedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerStopListener.class);

	@Autowired
	private Consumer<String,  byte[]> consumer;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		LOG.debug("Processing ContextClosedEvent event");
    	if(consumer!=null) {
    		consumer.close(Duration.of(10, ChronoUnit.SECONDS));
    	}
	}

}
