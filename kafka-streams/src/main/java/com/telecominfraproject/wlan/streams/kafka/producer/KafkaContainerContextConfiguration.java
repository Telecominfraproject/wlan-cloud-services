package com.telecominfraproject.wlan.streams.kafka.producer;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class KafkaContainerContextConfiguration {
	
	@Autowired 
	private Producer<String,  byte[]> producer;

	private static final Logger LOG = LoggerFactory.getLogger(KafkaContainerContextConfiguration.class);

    @Bean
    public ApplicationListener<ContextClosedEvent> kafkaContainerStopEventListner() {
        LOG.debug("Creating kafka container stop event listener");
        return new KafkaProducerStopListener(producer);
    }
    
}
