package com.telecominfraproject.wlan.streams.adoptionmetrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.adoptionmetrics.AdoptionMetricsServiceInterface;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetricsKey;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.core.model.utils.DateTimeUtils;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;
import com.telecominfraproject.wlan.stream.StreamProcessor;

/**
 * @author dtop 
 * <br> This stream processor is listening for APNodeMetrics, aggregating them and updating service adoption metrics every 15 minutes.
 * 
 */
@Component
public class AdoptionMetricsProcessor extends StreamProcessor {

	    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsProcessor.class);
	    
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.adoptionMetricsProcessor.timeBucketMs:900000}") //15 minutes aggregation buckets
        private long timeBucketMs;

        @Autowired
        private AdoptionMetricsServiceInterface adoptionMetricsServiceInterface;
        
        private static final Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> idToServiceAdoptionMetricsMap = new ConcurrentHashMap<>();
        private static final Map<ServiceAdoptionMetricsKey, Set<Long>> idToUniqueMacsMap = new ConcurrentHashMap<>();
        private static final Map<ServiceAdoptionMetricsKey, Set<Long>> idToNewUniqueMacsMap = new ConcurrentHashMap<>();


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
	    	
	    	switch ( smd.getClass().getSimpleName() ) {
	    	case "ApNodeMetrics":
	    		process(mdl.getCreatedTimestamp(), mdl.getCustomerId(), mdl.getEquipmentId(), mdl.getLocationId(), (ApNodeMetrics) smd);
	    		break;
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(long timestamp, int customerId, long equipmentId, long locationId, ApNodeMetrics model) {
			LOG.debug("Processing ApNodeMetrics");
					
			// Update in-memory counters and unique client MAC addresses 
			addDataSamples(timestamp, customerId, equipmentId, locationId, model);
			
			LOG.debug("Processed {}", model);
		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    public void addDataSamples(long timestampMs, int customerId, long equipmentId, long locationId, ApNodeMetrics model) {
	        // add new data samples        
	        AtomicLong txBytes = new AtomicLong();
	        model.getTxBytesPerRadio().values().forEach(v -> txBytes.addAndGet(v));         

	        AtomicLong rxBytes = new AtomicLong();
	        model.getRxBytesPerRadio().values().forEach(v -> rxBytes.addAndGet(v));         

	        Set<Long> incomingUniqMacs = new HashSet<>();
	        model.getClientMacAddressesPerRadio().forEach((rt, macList) -> {
	            macList.forEach(m -> incomingUniqMacs.add(m.getAddressAsLong()));            
	        });
	        
	        ServiceAdoptionMetricsKey samKey = new ServiceAdoptionMetricsKey(timestampMs, customerId, locationId, equipmentId);
	        
	        //update unique Macs seen
	        Set<Long> uniqMacs = idToUniqueMacsMap.get(samKey);
	        if(uniqMacs==null) {
	            uniqMacs = Collections.synchronizedSet(new HashSet<>());
	            idToUniqueMacsMap.put(samKey, uniqMacs);
	        }
	        
	        //update new unique Macs - to be written to DB at the next flush interval
	        Set<Long> newUniqMacs = new HashSet<>();
	        newUniqMacs.addAll(incomingUniqMacs);
	        newUniqMacs.removeAll(uniqMacs);
	        
	        Set<Long> existingNewUniqMacs = idToNewUniqueMacsMap.get(samKey);
	        if(existingNewUniqMacs==null) {
	            existingNewUniqMacs  = Collections.synchronizedSet(newUniqMacs);
	            idToNewUniqueMacsMap.put(samKey, existingNewUniqMacs);
	        } else {
	            existingNewUniqMacs.addAll(newUniqMacs);
	        }
	        
	        uniqMacs.addAll(incomingUniqMacs);

	        //update traffic counters
	        ServiceAdoptionMetrics sam = new ServiceAdoptionMetrics(timestampMs, customerId, locationId, equipmentId, uniqMacs.size(), txBytes.get(), rxBytes.get());
	        
	        ServiceAdoptionMetrics existingSam = idToServiceAdoptionMetricsMap.putIfAbsent(samKey, sam);
	        if(existingSam!=null) {
	            existingSam.addCounters(sam);
	            existingSam.setNumUniqueConnectedMacs(uniqMacs.size());
	        }
	        
	    }
	    
	    @PostConstruct
	    private void postCreate() {
	    	
	    	Thread  adoptionMetricsPusherThread = new Thread( new Runnable() {
				
                private int lastProcessedYear = DateTimeUtils.getYear(System.currentTimeMillis());
	    	    private int lastProcessedDayOfYear = DateTimeUtils.getDayOfYear(System.currentTimeMillis());
	    	    
				@Override
				public void run() {
					LOG.info("Starting adoptionMetricsPusherThread");
					
					while(true) {
	                	LOG.trace("Flushing adoption metrics into db");

	                	try {
    	                	//process what's need to be pushed
	                	    
	                	    //push accumulated adoption metrics
    	                	List<ServiceAdoptionMetrics> metricsToPush = new ArrayList<ServiceAdoptionMetrics>( idToServiceAdoptionMetricsMap.values());
    	                	idToServiceAdoptionMetricsMap.clear();
    	                	if(!metricsToPush.isEmpty()) {
    	                	    adoptionMetricsServiceInterface.update(metricsToPush);
    	                	}
    	                	
    	                	//push newly discovered unique mac addresses
    	                	idToNewUniqueMacsMap.forEach( (k, v) -> {
    	                	    Set<Long> clientMacSet = new HashSet<>(v);
    	                	    v.clear();
    	                	    if(!clientMacSet.isEmpty()) {
    	                	        adoptionMetricsServiceInterface.updateUniqueMacs(k.getTimestampMs(), k.getCustomerId(), k.getLocationId(), k.getEquipmentId(), clientMacSet);
    	                	    }
    	                	} );
    	                	
    	                	idToNewUniqueMacsMap.entrySet().removeIf((e) -> e.getValue().isEmpty());
    	                	
    	                	//determine if a new day has started
    	                	int currentDayOfYear = DateTimeUtils.getDayOfYear(System.currentTimeMillis());
    	                	if(currentDayOfYear != lastProcessedDayOfYear) {
    	                	    
    	                        //perform daily aggregation of unique macs, 
    	                	    // update adoption_metrics_counters.numUniqueConnectedMacs and purge adoption_metrics_uniq_macs table
    	                	    adoptionMetricsServiceInterface.finalizeUniqueMacsCount(lastProcessedYear, lastProcessedDayOfYear);

    	                	    //clean up accumulated uniqueMacs at the beginning of the new day
    	                	    idToUniqueMacsMap.clear();
    	                	    
                                lastProcessedDayOfYear = currentDayOfYear;
                                lastProcessedYear = DateTimeUtils.getYear(System.currentTimeMillis());

    	                	}
	                	
                        } catch(Exception e) {
                            LOG.error("Error when pushing adoption metrics " , e);
                        }

	                	LOG.trace("Done flushing adoption metrics into db");

	                	try {
	                		Thread.sleep(timeBucketMs);
	                	} catch (InterruptedException e) {
	                		Thread.currentThread().interrupt();
						}
	                	
					}
				}
			}, "adoptionMetricsPusherThread"); 
	    	
	    	adoptionMetricsPusherThread.setDaemon(true);
	    	adoptionMetricsPusherThread.start();
	    	
	    }

}
