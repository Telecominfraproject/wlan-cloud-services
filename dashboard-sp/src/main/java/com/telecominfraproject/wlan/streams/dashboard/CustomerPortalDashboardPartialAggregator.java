package com.telecominfraproject.wlan.streams.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;
import com.telecominfraproject.wlan.status.dashboard.models.events.CustomerPortalDashboardPartialEvent;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.stream.StreamProcessor;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 * 
 *         <br>
 *         This stream processor is listening for events and service_metrics_collection_config related to the counters displayed on the portal dashboards. It performs
 *         aggregation of raw data at configured intervals. <br>
 *         Each instance of this SP will aggregate a subset of counters for a portion of all customer equipment (non-overlapping subsets), it will post results of that
 *         aggregation directly into a separate kafka queue (customer_events) as CustomerPortalDashboardPartialEvent, partitioned only by customerId. <br>
 *         Kafka queue customer_events will be read by another SP
 *         (CustomerPortalDashboardAggregator). It will combine partial aggregations and will post results as a CustomerPortalDashboardStatus into the status service at configured intervals.<br>
 *         
 *         Initial set of counters to aggregate: <br>
 *         For Equipment:
 *         <ul>
 *         <li>Total Provisioned - read from DB when combining CustomerPortalDashboardStatus
 *         <li>In Service -  collect unique equipmentIds per time interval from ApNodeMetrics
 *         <li>With Clients - calculate N per time interval based on ApNodeMetrics
 *         <li>Out of Service - (Total Provisioned - total_reported_per_time_interval(In Service)) - populate when combining CustomerPortalDashboardStatus
 *         <li>Never Connected -  skip for now
 *         </ul>
 *         <br>
 *         For clients:
 *         <ul>
 *         <li>Number of associated clients for each frequency band - ApNodeMetrics.clientMacAddressesPerRadio
 *         </ul>
 *         <br>
 *         Usage traffic:
 *         <ul>
 *         <li>Total traffic volume DS - sum of ApNodeMetrics.txBytesPerRadio per time interval
 *         <li>Total traffic volume US - sum of ApNodeMetrics.rxBytesPerRadio per time interval
 *         <li>Average traffic rate DS in mbps -total traffic volume/reporting interval (do we need to do average of average AP-reported rate per interval?) 
 *         <li>Average traffic rate US in mbps - total traffic volume/reporting interval
 *         </ul>
 *         <br>
 *         OUI Stats:
 *         <ul>
 *         <li>Aggregate number of AP Vendor OUIs - per OUI - read from DB when combining CustomerPortalDashboardStatus (add this column to Equipment, index it, provide an API to report counts per OUI )
 *         <li>Aggregate number of Client OUIs - per OUI - ApNodeMetrics.clientMacAddressesPerRadio - collect per time interval
 *         </ul>
 *         <br>
 * 
 */
@Component
public class CustomerPortalDashboardPartialAggregator extends StreamProcessor {

	    private static final Logger LOG = LoggerFactory.getLogger(CustomerPortalDashboardPartialAggregator.class);
	    
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;
        
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.customerPortalDashboard.timeBucketMs:300000}") //5 minutes aggregation buckets
        private long timeBucketMs;

        @Value("${tip.wlan.customerPortalDashboard.timeBucketsInFlight:2}") //maintain the last 2 aggregation buckets
        private long timeBucketsInFlight;
        
        @Autowired @Qualifier("customerEventStreamInterface") private StreamInterface<SystemEventRecord> customerEventStream;

	    private ConcurrentHashMap<Integer, CustomerPortalDashboardPartialContext> contextPerCustomerIdMap = new ConcurrentHashMap<>();
	    
	    @Autowired
	    private AlarmServiceInterface alarmServiceInterface;
	    

	    @Override
	    protected boolean acceptMessage(QueuedStreamMessage message) {
		    boolean ret = message.getTopic().equals(wlanServiceMetricsTopic);

		    if(ret && ( message.getModel() instanceof ServiceMetric) ) {
				    
		    		ServiceMetric sm = (ServiceMetric) message.getModel(); 
				    ret = ret &&
				    		(
				    			sm.getDetails() instanceof ApNodeMetrics		    			
				    		);
		    } else {
		    	ret = false;
		    }
		    
		    LOG.trace("acceptMessage {}", ret);
		    
		    return ret;
	    }
	    
		@Override
		protected void processMessage(QueuedStreamMessage message) {

			ServiceMetric mdl = (ServiceMetric) message.getModel();
			ServiceMetricDetails smd = mdl.getDetails();
			LOG.debug("Processing {}", mdl);

			switch (smd.getClass().getSimpleName()) {
			case "ApNodeMetrics":
				process(mdl.getCustomerId(), System.currentTimeMillis(), mdl.getEquipmentId(), (ApNodeMetrics) smd);
				break;
			default:
				process(mdl);
			}

		}

		private void process(int customerId, long timestamp, long equipmentId, ApNodeMetrics model) {
			LOG.debug("Processing ApNodeMetrics");
			//get context for the customerId
			CustomerPortalDashboardPartialContext context = contextPerCustomerIdMap.get(customerId);
			if(context == null) {
				context = new CustomerPortalDashboardPartialContext(customerId);
				context = contextPerCustomerIdMap.putIfAbsent(customerId, context);
				if(context == null) {
					context = contextPerCustomerIdMap.get(customerId);
				}
			}
			
			// Get CustomerPortalDashboardPartialEvent from the context for the timeBucket that corresponds to the ApNodeMetrics timestamp
			long timeBucketId = timestamp - ( timestamp % timeBucketMs);
			CustomerPortalDashboardPartialEvent partialEvent = context.getOrCreatePartialEvent(timeBucketId);
			
			// Update counters on the context and/or CustomerPortalDashboardPartialEvent
			partialEvent.getEquipmentIds().add(equipmentId);
			
			if(model.getClientCount() > 0) {
			    partialEvent.getEquipmentIdsWithClients().add(equipmentId);
			}
			
			partialEvent.addClientMacs(model.getClientMacAddressesPerRadio());
			
			AtomicLong txBytes = new AtomicLong();
			model.getTxBytesPerRadio().values().forEach(v -> txBytes.addAndGet(v));			
			partialEvent.getTrafficBytesDownstream().addAndGet(txBytes.get());

			AtomicLong rxBytes = new AtomicLong();
			model.getRxBytesPerRadio().values().forEach(v -> rxBytes.addAndGet(v));			
			partialEvent.getTrafficBytesUpstream().addAndGet(rxBytes.get());

			LOG.debug("Processed {}", partialEvent);

		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    @PostConstruct
	    private void postCreate() {
	    	
	    	Thread customerPortalDashboardPartialEventPusherThread = new Thread( new Runnable() {
				
				@Override
				public void run() {
					LOG.info("Starting customerPortalDashboardPartialEventPusherThread");
					while(true) {
	                	LOG.trace("Pushing partial events");

	                	List<CustomerPortalDashboardPartialContext> contexts = new ArrayList<>(contextPerCustomerIdMap.values());
		                	
	                	contexts.forEach(context -> {
		                	try {
		                		// Get the oldest CustomerPortalDashboardPartialEvent from the context,
								// if it is older than timeBucket ms - finalize it and publish it
			        			CustomerPortalDashboardPartialEvent oldestPartialEvent = context.getOldestPartialEventOrNull();
								if (oldestPartialEvent != null && oldestPartialEvent
										.getTimeBucketId() < System.currentTimeMillis() - timeBucketMs) {
									// finalize oldestPartialEvent counters, and put it into customerEventStream
			        			    oldestPartialEvent.aggregateCounters();
			        				
			        				AlarmCounts alarmCounts = alarmServiceInterface.getAlarmCounts(context.getCustomerId(), oldestPartialEvent.getEquipmentIds(), Collections.emptySet(), null);
			        				for (Entry<AlarmCode, AtomicInteger> entry : alarmCounts.getTotalCountsPerAlarmCodeMap().entrySet()) {
			        					oldestPartialEvent.incrementAlarmsCountBySeverity(entry.getKey().getSeverity(), entry.getValue().get());
			        				}
			        				
			        				customerEventStream.publish(new SystemEventRecord(oldestPartialEvent));
			        				context.setLastPublishedTimestampMs(System.currentTimeMillis());
			        				
			        				//remove that oldest CustomerPortalDashboardPartialEvent from the context				
			        				context.removePartialEvent(oldestPartialEvent.getTimeBucketId());
			        				LOG.trace("Finalized processing of {}", oldestPartialEvent);
			        			}
			        			
								if (oldestPartialEvent == null && context
										.getLastPublishedTimestampMs() < System.currentTimeMillis() - timeBucketMs) {
			        			    //there have not been any metrics reported for this subset of customer equipment in a while, 
			        			    //we'll create an empty partial event and will post 0 counters for it 
			        			    long timestamp = System.currentTimeMillis();
			        			    long timeBucketId = timestamp - ( timestamp % timeBucketMs);
 		        		            context.getOrCreatePartialEvent(timeBucketId);
			        			}
			        			
		                	} catch(Exception e) {
		                		LOG.error("Error when processing context for customer {}", context.getCustomerId(), e);
		                	}
	                	});
	                	
	                	try {
	                		Thread.sleep(10000);
	                	} catch (InterruptedException e) {
	                		Thread.currentThread().interrupt();
						}
	                	
	                	LOG.trace("Done pushing partial events");
					}
				}
			}, "customerPortalDashboardPartialEventPusherThread"); 
	    	
	    	customerPortalDashboardPartialEventPusherThread.setDaemon(true);
	    	customerPortalDashboardPartialEventPusherThread.start();
	    }

	    
}
