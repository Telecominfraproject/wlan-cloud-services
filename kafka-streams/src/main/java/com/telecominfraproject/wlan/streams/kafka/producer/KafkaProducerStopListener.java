package com.telecominfraproject.wlan.streams.kafka.producer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Register for container stop event so that we can close the kafka producer
 * 
 * @author dtop
 *
 */
public class KafkaProducerStopListener implements ApplicationListener<ContextClosedEvent> {
	private Producer<String,  byte[]> producer;

	private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerStopListener.class);

	public KafkaProducerStopListener(Producer<String,  byte[]> producer) {
		this.producer = producer;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		LOG.debug("Processing ContextClosedEvent event");
    	if(producer!=null) {
    		producer.close(Duration.of(10, ChronoUnit.SECONDS));
    	}
	}

}
