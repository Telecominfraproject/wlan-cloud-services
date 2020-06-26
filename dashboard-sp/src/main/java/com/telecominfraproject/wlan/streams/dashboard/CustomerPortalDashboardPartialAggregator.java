package com.telecominfraproject.wlan.streams.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.stream.StreamProcessor;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 * 
 *         <br>
 *         This stream processor is listening for events and metrics related to the counters displayed on the portal dashboards. It performs
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
        
        @Autowired @Qualifier("customerEventStreamInterface") private StreamInterface<SystemEventRecord> customerEventStream;

        @Autowired
        private EquipmentGatewayServiceInterface equipmentGatewayInterface;
        @Autowired
        private ProfileServiceInterface profileServiceInterface;
        @Autowired
        private EquipmentServiceInterface equipmentServiceInterface;
        @Autowired
        private AlarmServiceInterface alarmServiceInterface;

        @Autowired
        private StatusServiceInterface statusServiceInterface;

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
	    	ServiceMetricDetails se = mdl.getDetails();
	    	LOG.debug("Processing {}", mdl);
	    	
	    	switch ( se.getClass().getSimpleName() ) {
	    	case "ApNodeMetrics":
	    		process((ApNodeMetrics) se);
	    		break;
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(ApNodeMetrics model) {
			LOG.debug("Processing ApNodeMetrics");
			//equipmentGatewayInterface.sendCommand(new CEGWConfigChangeNotification(model.getPayload().getInventoryId(), model.getEquipmentId()));
		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    
}
