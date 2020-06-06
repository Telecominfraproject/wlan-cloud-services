package com.telecominfraproject.wlan.streams.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

    	private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerConfig.class);

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

        @Bean
        public Producer<String,  byte[]> streamProducer(){
        	//https://kafka.apache.org/25/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
        	
        	//To see how to configure consumer:
        	//	http://kafka.apache.org/documentation.html#consumerconfigs
        	//
        	// How to configure Kafka Streams client library:
        	// http://kafka.apache.org/documentation.html#streamsconfigs
        	//  Consumers are not thread-safe.
        	//		Set auto-commit or call commitAsync(OffsetCommitCallback callback) periodically to store consumer's offsets (stream positions) in the Kafka itself.
        	//	
        	//
        	//  Configure (a pool of) consumers with:
        	//		key.deserializer
        	//		value.deserializer
        	//		bootstrap.servers
        	//		group.id
        	//		ssl.key.password
        	//		ssl.keystore.location
        	//		ssl.keystore.password
        	//		ssl.truststore.location
        	//		ssl.truststore.password
        	//		auto.offset.reset=latest
        	//		enable.auto.commit=true //If true the consumer's offset will be periodically committed in the background.
        	//		auto.commit.interval.ms=5000
        	//		security.protocol=SSL //default PLAINTEXT
        	//		ssl.keystore.type=JKS
        	//		ssl.truststore.type=JKS
        	//		client.id=something //nice to have
        	//		ssl.endpoint.identification.algorithm=https // do we need this to be something else?
 /*
      Properties props = new Properties();
     props.setProperty("bootstrap.servers", "localhost:9092");
     props.setProperty("group.id", "test");
     props.setProperty("enable.auto.commit", "true");
     props.setProperty("auto.commit.interval.ms", "1000");
     props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
     props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
     consumer.subscribe(Arrays.asList("foo", "bar"));
     while (true) {
         ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
         for (ConsumerRecord<String, String> record : records) {
             System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
         }
     }
     
     
     =================
     
 public class KafkaConsumerRunner implements Runnable {
     private final AtomicBoolean closed = new AtomicBoolean(false);
     private final KafkaConsumer consumer;

     public KafkaConsumerRunner(KafkaConsumer consumer) {
       this.consumer = consumer;
     }

     public void run() {
         try {
             consumer.subscribe(Arrays.asList("topic"));
             while (!closed.get()) {
                 ConsumerRecords records = consumer.poll(Duration.ofMillis(10000));
                 // Handle new records
             }
         } catch (WakeupException e) {
             // Ignore exception if closing
             if (!closed.get()) throw e;
         } finally {
             consumer.close();
         }
     }

     // Shutdown hook which can be called from a separate thread
     public void shutdown() {
         closed.set(true);
         consumer.wakeup();
     }
 }
 

Then in a separate thread, the consumer can be shutdown by setting the closed flag and waking up the consumer.

     closed.set(true);
     consumer.wakeup();
      
 */
        	
        	//		
        	//
        	// How to configure Kafka Admin client library:
        	// http://kafka.apache.org/documentation.html#adminclientconfigs
        	
        	//To test the connectivity:
        	//  Get the kafka distribution from https://www.apache.org/dyn/closer.cgi?path=/kafka/2.5.0/kafka_2.12-2.5.0.tgz
        	//  $ wget http://apache.mirror.iweb.ca/kafka/2.5.0/kafka_2.12-2.5.0.tgz
        	//  $ wget https://downloads.apache.org/kafka/2.5.0/kafka_2.12-2.5.0.tgz.sha512
        	//  $ shasum -a 512 kafka_2.12-2.5.0.tgz
        	//  $ tar -xzf kafka_2.12-2.5.0.tgz
        	//  $ cd kafka_2.12-2.5.0
        	//  In another terminal: $ bin/zookeeper-server-start.sh config/zookeeper.properties
        	//	 edit config/server.properties 
/*

# For SSL setup see http://kafka.apache.org/documentation.html#security_ssl_key
# *** Important: server certificate must list the ip address or a hostname of the listener in the [alternate_names]
# alternatively - set property ssl.endpoint.identification.algorithm= <empty_string>
# by default it is set to ssl.endpoint.identification.algorithm=HTTPS (means perform host name validation against alternate_names of the certificate ) 
listeners=SSL://localhost:9092
#ssl.client.auth=required
# for the ssl.client.auth to work the server certificate must be generated with "extendedKeyUsage     = critical, serverAuth, clientAuth"
ssl.endpoint.identification.algorithm=
security.inter.broker.protocol=SSL
ssl.key.password=mypassword
ssl.keystore.location=/opt/tip-wlan/certs/server.pkcs12
ssl.keystore.password=mypassword
ssl.keystore.type=PKCS12
ssl.truststore.location=/opt/tip-wlan/certs/truststore.jks
ssl.truststore.password=mypassword
ssl.truststore.type=JKS

### working config:
listeners=SSL://localhost:9092
ssl.client.auth=required
#ssl.endpoint.identification.algorithm=  <using default value of HTTPS here, since we included localhost into alternate_names of the kafka-server.pkcs12 certificate>
security.inter.broker.protocol=SSL
ssl.key.password=mypassword
ssl.keystore.location=/opt/tip-wlan/certs/kafka-server.pkcs12
ssl.keystore.password=mypassword
#ssl.keystore.type=JKS
ssl.keystore.type=PKCS12
ssl.truststore.location=/opt/tip-wlan/certs/truststore.jks
ssl.truststore.password=mypassword
ssl.truststore.type=JKS


 */
        	
/* configure properties for SSL communication with zookeeper
	zookeeper.connect = localhost:2181
?	zookeeper.ssl.client.enable = false
?	zookeeper.ssl.enabled.protocols = null
?	zookeeper.ssl.endpoint.identification.algorithm = HTTPS
	zookeeper.ssl.keystore.location = null
	zookeeper.ssl.keystore.password = null
	zookeeper.ssl.keystore.type = null
	zookeeper.ssl.truststore.location = null
	zookeeper.ssl.truststore.password = null
	zookeeper.ssl.truststore.type = null
 */
        	//  In another terminal: $ bin/kafka-server-start.sh config/server.properties
        	//

        	//create config/admin-client.properties for communicating with broker over SSL:
        	// vim config/admin-client.properties
/*
ssl.endpoint.identification.algorithm=
security.protocol=SSL
ssl.key.password=mypassword
ssl.keystore.location=/opt/tip-wlan/certs/kafka-server.pkcs12
ssl.keystore.password=mypassword
ssl.keystore.type=PKCS12
ssl.truststore.location=/opt/tip-wlan/certs/truststore.jks
ssl.truststore.password=mypassword
ssl.truststore.type=JKS
 */
        	// create topics:
        	// $ bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --command-config config/admin-client.properties --replication-factor 1 --partitions 1 --topic test
        	// $ bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --command-config config/admin-client.properties --replication-factor 1 --partitions 1 --topic wlan_service_metrics
        	// $ bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --command-config config/admin-client.properties --replication-factor 1 --partitions 1 --topic system_events
        	
        	//list topics:
        	// $ bin/kafka-topics.sh --list --bootstrap-server localhost:9092 --command-config config/admin-client.properties
        	
        	//publish test messages:
        	// $ bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --producer.config config/admin-client.properties --topic test
        	
        	//consume test messages:
        	// $ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --consumer.config config/admin-client.properties --topic test

        	//delete test topics:
        	// $ bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --command-config config/admin-client.properties --topic test1
        	
        	Properties props = new Properties();
        	//For full list of config properties - see
        	// http://kafka.apache.org/documentation.html#producerconfigs
        	props.put("bootstrap.servers", bootstrapServers);
        	props.put("acks", "0");
        	props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        	props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        	props.put("ssl.key.password", sslKeyPassword);
        	props.put("ssl.keystore.location", sslKeystoreLocation);
        	props.put("ssl.keystore.password", sslKeystorePassword);
        	props.put("ssl.keystore.type", sslKeystoreType);
        	props.put("ssl.truststore.location", sslTruststoreLocation);
        	props.put("ssl.truststore.password", sslTruststorePassword);
        	props.put("ssl.truststore.type", sslTruststoreType);
        	props.put("security.protocol", securityProtocol);

        	//
        	//partitioner.class - default is org.apache.kafka.clients.producer.internals.DefaultPartitioner
        	//

			// Producers are thread safe, it is better to have small number of producers
			// shared for all application threads than each thread having its own producer.
        	Producer<String,  byte[]> producer = new KafkaProducer<>(props);

        	//mask passwords when displaying properties
        	StringBuilder strb = new StringBuilder();
        	props.forEach((k, v) -> strb.append(k).append("=").append(((String)k).toLowerCase().contains("password")?"***":v).append(", "));
        	LOG.info("Created Kafka Producer with properties [{}]", strb);
        	return producer;
        }
        
}
