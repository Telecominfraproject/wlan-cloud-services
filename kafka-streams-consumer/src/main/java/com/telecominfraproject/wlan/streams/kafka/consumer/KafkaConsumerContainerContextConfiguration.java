package com.telecominfraproject.wlan.streams.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerContainerContextConfiguration {
	
	@Autowired 
	private Consumer<String,  byte[]> consumer;

	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerContainerContextConfiguration.class);

    @Bean
    public ApplicationListener<ContextClosedEvent> kafkaConsumerContainerStopEventListener() {
        LOG.debug("Creating kafka consumer container stop event listener");
        return new KafkaConsumerStopListener(consumer);
    }
    
}
