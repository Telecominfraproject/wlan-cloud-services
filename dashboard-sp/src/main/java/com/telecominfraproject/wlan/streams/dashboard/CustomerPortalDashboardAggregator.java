package com.telecominfraproject.wlan.streams.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.CustomerEquipmentCounts;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.dashboard.models.CustomerPortalDashboardStatus;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.stream.StreamProcessor;
import com.telecominfraproject.wlan.systemevent.aggregation.models.CustomerPortalDashboardPartialEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 * 
 *         <br>
 *         This stream processor is listening for events related to the counters displayed on the portal dashboards. It performs
 *         aggregation of raw data at configured intervals. <br>
 *         Each instance of this SP reads CustomerPortalDashboardPartialEvent from topic customer_events, combines partial aggregations per customer and posts results as a CustomerPortalDashboardStatus into the status service at configured intervals.<br>
 *         CustomerPortalDashboardPartialEvents are produced by another SP - CustomerPortalDashboardPartialAggregator - and they contain counters for a portion of all customer equipment (non-overlapping subsets). <br>
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
public class CustomerPortalDashboardAggregator extends StreamProcessor {

	    private static final Logger LOG = LoggerFactory.getLogger(CustomerPortalDashboardAggregator.class);
        
        @Value("${tip.wlan.customerEventsTopic:customer_events}")
    	private String customerEventsTopic;

        @Value("${tip.wlan.customerPortalDashboard.timeBucketMs:300000}") //5 minutes aggregation buckets
        private long timeBucketMs;
        
        @Value("${tip.wlan.customerPortalDashboard.timeBucketsInFlight:2}") //maintain the last 2 aggregation buckets
        private long timeBucketsInFlight;

        @Autowired 
        private StatusServiceInterface statusService;
        
        @Autowired 
        private EquipmentServiceInterface equipmentService;
        
	    private ConcurrentHashMap<Integer, CustomerPortalDashboardContext> contextPerCustomerIdMap = new ConcurrentHashMap<>();
	    

	    @Override
	    protected boolean acceptMessage(QueuedStreamMessage message) {
		    boolean ret = message.getTopic().equals(customerEventsTopic);

		    if(ret && ( message.getModel() instanceof SystemEventRecord) ) {
				    
		    	SystemEventRecord sm = (SystemEventRecord) message.getModel(); 
				    ret = ret &&
				    		(
				    			sm.getDetails() instanceof CustomerPortalDashboardPartialEvent		    			
				    		);
		    } else {
		    	ret = false;
		    }
		    
		    LOG.trace("acceptMessage {}", ret);
		    
		    return ret;
	    }
	    
	    @Override
	    protected void processMessage(QueuedStreamMessage message) {
	    	
	    	SystemEventRecord mdl = (SystemEventRecord) message.getModel();
	    	SystemEvent smd = mdl.getDetails();
	    	LOG.debug("Processing {}", mdl);
	    	
	    	switch ( smd.getClass().getSimpleName() ) {
	    	case "CustomerPortalDashboardPartialEvent":
	    		process(mdl.getCustomerId(), (CustomerPortalDashboardPartialEvent) smd);
	    		break;
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(int customerId, CustomerPortalDashboardPartialEvent model) {
			LOG.debug("Processing CustomerPortalDashboardPartialEvent");
			//get context for the customerId
			CustomerPortalDashboardContext context = contextPerCustomerIdMap.get(customerId);
			if(context == null) {
				context = new CustomerPortalDashboardContext(customerId);
				context = contextPerCustomerIdMap.putIfAbsent(customerId, context);
				if( context == null ) {
					context = contextPerCustomerIdMap.get(customerId);
				}
			}
			
			// Get CustomerPortalDashboardStatus from the context for the timeBucket that corresponds to the partial event
			CustomerPortalDashboardStatus dashboardStatus = context.getOrCreateDashboardStatus(model.getTimeBucketId());
			
			// Update counters
			dashboardStatus.applyPartialEvent(model);

			LOG.debug("Processed {}", dashboardStatus);

		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    @PostConstruct
	    private void postCreate() {
	    	
	    	Thread customerPortalDashboardStatusPusherThread = new Thread( new Runnable() {
				
				@Override
				public void run() {
					LOG.info("Starting customerPortalDashboardStatusPusherThread");
					while(true) {
	                	LOG.trace("Pushing customerPortalDashboardStatuses");

	                	List<CustomerPortalDashboardContext> contexts = new ArrayList<>(contextPerCustomerIdMap.values());
		                	
	                	contexts.forEach(context -> {
		                	try {
		            			// Get the oldest CustomerPortalDashboardStatus from the context,
		                		// If it is older than ( (timeBucketsInFlight + 1) x timeBucket) ms - save it and remove from the context
		            			CustomerPortalDashboardStatus oldestDashboardStatus = context.getOldestDashboardStatusOrNull();
		            			if(oldestDashboardStatus!=null && oldestDashboardStatus.getTimeBucketId() < System.currentTimeMillis() - (timeBucketsInFlight +1)* timeBucketMs) {
		            				// finalize oldestDashboardStatus counters, and store it
		            				
		            				CustomerEquipmentCounts equipmentCounts = equipmentService.getEquipmentCounts(context.getCustomerId());
		            				oldestDashboardStatus.setTotalProvisionedEquipment(equipmentCounts.getTotalCount());
		            				equipmentCounts.getOuiCounts().forEach((oui, cnt) -> oldestDashboardStatus.getEquipmentCountPerOui().put(oui, new AtomicInteger(cnt)));

		            				//save the oldest status
		            				Status status = new Status();
		            				status.setCustomerId(context.getCustomerId());
		            				status.setDetails(oldestDashboardStatus);
		            				statusService.update(status);
		            				
		            				//remove that oldest CustomerPortalDashboardStatus from the context				
		            				context.removeDashboardStatus(oldestDashboardStatus.getTimeBucketId());
		            				LOG.trace("Finalized processing of {}", oldestDashboardStatus);
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
	                	
	                	LOG.trace("Done pushing customerPortalDashboardStatuses");
					}
				}
			}, "customerPortalDashboardStatusPusherThread"); 
	    	
	    	customerPortalDashboardStatusPusherThread.setDaemon(true);
	    	customerPortalDashboardStatusPusherThread.start();
	    }
	    
}
