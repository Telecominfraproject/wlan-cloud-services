package com.telecominfraproject.wlan.streams.kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerConfig {

    	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerConfig.class);

        @Value("${tip.wlan.kafka.bootstrapServers:localhost:9092}")
    	private String bootstrapServers;

        @Value("${tip.wlan.kafka.sslKeyPassword:mypassword}")
        private String sslKeyPassword;
        @Value("${tip.wlan.kafka.sslKeystoreLocation:/opt/tip-wlan/certs/kafka-server.pkcs12}")
        private String sslKeystoreLocation;
        @Value("${tip.wlan.kafka.sslKeystorePassword:mypassword}")
        private String sslKeystorePassword;
        @Value("${tip.wlan.kafka.sslKeystoreType:PKCS12}")
        private String sslKeystoreType; // - kafka default is JKS
        
        @Value("${tip.wlan.kafka.sslTruststoreLocation:/opt/tip-wlan/certs/truststore.jks}")
        private String sslTruststoreLocation;
        @Value("${tip.wlan.kafka.sslTruststorePassword:mypassword}")
        private String sslTruststorePassword;
        @Value("${tip.wlan.kafka.sslTruststoreType:JKS}")
        private String sslTruststoreType; // - default is JKS
        
        @Value("${tip.wlan.kafka.securityProtocol:SSL}")
        private String securityProtocol; // - use SSL, kafka default is PLAINTEXT

        @Value("${tip.wlan.kafka.sslEndpointIdentificationAlgorithm:HTTPS}")
        private String sslEndpointIdentificationAlgorithm; // For the HTTPS value to work the alternative names in the certificates have to be correctly specified. Use empty string "" to turn it off.

        @Value("${tip.wlan.kafka.groupId:tip.wlan.main}")
        private String groupId;

        @Value("${tip.wlan.kafka.clientId:tip.wlan.main.consumer}")
        private String clientId;

        @Value("${tip.wlan.kafka.autoCommitIntervalMs:5000}")
        private String autoCommitIntervalMs;

        @Value("${tip.wlan.kafka.autoOffsetReset:latest}")
        private String autoOffsetReset;

        
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;

        @Value("${tip.wlan.customerEventsTopic:customer_events}")
    	private String customerEventsTopic;

        @Bean
        public Consumer<String,  byte[]> streamConsumer(){

        	//To see how to configure consumer:
        	//	http://kafka.apache.org/documentation.html#consumerconfigs

            Properties props = new Properties();
            props.setProperty("bootstrap.servers", bootstrapServers);
            props.setProperty("group.id", groupId);
            props.setProperty("enable.auto.commit", "true"); //If true the consumer's offset will be periodically committed in the background.
            props.setProperty("auto.commit.interval.ms", autoCommitIntervalMs);
            props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
            
        	props.put("ssl.key.password", sslKeyPassword);
        	props.put("ssl.keystore.location", sslKeystoreLocation);
        	props.put("ssl.keystore.password", sslKeystorePassword);
        	props.put("ssl.keystore.type", sslKeystoreType);
        	props.put("ssl.truststore.location", sslTruststoreLocation);
        	props.put("ssl.truststore.password", sslTruststorePassword);
        	props.put("ssl.truststore.type", sslTruststoreType);
        	props.put("security.protocol", securityProtocol);

        	props.put("auto.offset.reset", autoOffsetReset);
        	props.put("client.id", clientId);
        	props.put("ssl.endpoint.identification.algorithm", sslEndpointIdentificationAlgorithm); 

            KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList(wlanServiceMetricsTopic, systemEventsTopic, customerEventsTopic));
        			

        	//mask passwords when displaying properties
        	StringBuilder strb = new StringBuilder();
        	props.forEach((k, v) -> strb.append(k).append("=").append(((String)k).toLowerCase().contains("password")?"***":v).append(", "));
        	LOG.info("Created Kafka Consumer with properties [{}]", strb);

        	return consumer;

        }
        
}
