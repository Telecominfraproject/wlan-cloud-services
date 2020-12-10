package com.telecominfraproject.wlan.streams.equipmentalarms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmDetails;
import com.telecominfraproject.wlan.alarm.models.AlarmScopeType;
import com.telecominfraproject.wlan.alarm.models.OriginatorType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.equipment.report.models.OperatingSystemPerformance;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.stream.StreamProcessor;

/**
 * @author dtop 
 * <br> This stream processor is listening for APNodeMetrics, aggregating them in sliding windows of 5 minutes and raising/clearing alarms based on preconfigured thresholds.
 * <br> "AP out of reach" alarm is raised when no APNodeMetrics have been received for the equipment in the last interval, cleared when APNodeMetrics appear again
 * <br> "Temperature too high" alarm is raised when average temperature over the last interval goes above the configured threshold of 80C, cleared when average temperature goes below the threshold
 * <br> "CPU utilization is too high" alarm is raised when average CPU utilization on AP over the last interval goes above the configured threshold of 80%, cleared when average CPU utilization goes below the threshold
 * <br> "Memory utilization is too high" alarm is raised when average RAM utilization on AP over the last interval goes above the configured threshold of 70%, cleared when average RAM utilization goes below the threshold
 * 
 */
@Component
public class EquipmentAlarmsProcessor extends StreamProcessor {

	    private static final Logger LOG = LoggerFactory.getLogger(EquipmentAlarmsProcessor.class);
	    
        @Value("${tip.wlan.wlanServiceMetricsTopic:wlan_service_metrics}")
    	private String wlanServiceMetricsTopic;
        
        @Value("${tip.wlan.equipmentAlarmProcessor.timeBucketMs:300000}") //5 minutes aggregation buckets
        private long timeBucketMs;

        @Value("${tip.wlan.equipmentAlarmProcessor.checkAlarmsIntervalMs:15000}") //check for raising/clearing alarms every 15 seconds
        private long checkAlarmsIntervalMs;

        @Value("${tip.wlan.equipmentAlarmProcessor.temperatureThresholdInC:80}")
        private int temperatureThresholdInC;

        @Value("${tip.wlan.equipmentAlarmProcessor.cpuUtilThresholdPct:80}")
        private int cpuUtilThresholdPct;

        @Value("${tip.wlan.equipmentAlarmProcessor.memoryUtilThresholdPct:70}")
        private int memoryUtilThresholdPct;


	    private final ConcurrentHashMap<Long, EquipmentAlarmsContext> contextPerEquipmentIdMap = new ConcurrentHashMap<>();

		private final Set<AlarmCode> alarmCodeSet = new HashSet<>();

        @Autowired
        private AlarmServiceInterface alarmServiceInterface;

        @Autowired
        private StatusServiceInterface statusServiceInterface;

        @Autowired
        private EquipmentServiceInterface equipmentServiceInterface;

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
	    		process(mdl.getCustomerId(), mdl.getCreatedTimestamp(), mdl.getEquipmentId(), (ApNodeMetrics) smd);
	    		break;
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(int customerId, long timestamp, long equipmentId, ApNodeMetrics model) {
			LOG.debug("Processing ApNodeMetrics");
			//get context for the equipmentId
			EquipmentAlarmsContext context = contextPerEquipmentIdMap.get(equipmentId);
			if(context == null) {
				
        		//When creating EquipmentAlarmsContext - read currently raised alarms of the types we're interested, keep it in memory.
				//Only this SP is responsible for raising/clearing those alarms for this particular equipment				
				List<Alarm> existingAlarms = alarmServiceInterface.get(customerId, Collections.singleton(equipmentId), alarmCodeSet);

				//All alarms that are handled by this stream processor logically exist only once-per-alarmCode-per-equipment.
				//Ensure that there is only one alarm per alarmCode per equipment (the latest one), remove all others.
				//This is needed to recover from some corner-case scenarios that can happen with the distributed datastores (for example split-brain).
				existingAlarms = cleanUpDuplicateAlarms(existingAlarms);

				context = new EquipmentAlarmsContext(customerId, equipmentId, existingAlarms, timeBucketMs, temperatureThresholdInC, cpuUtilThresholdPct, memoryUtilThresholdPct);				
				context = contextPerEquipmentIdMap.putIfAbsent(equipmentId, context);
				if(context == null) {
					context = contextPerEquipmentIdMap.get(equipmentId);
				}
			}

			//find out TotalAvailableMemory in kb, which is available as part of the OperatingSystemPerformance status object
			//we look for it separately because this status may not be populated when the first service_metrics_collection_config are coming in
			if(context.getTotalAvailableMemoryKb()==0L) {
				Status osPerformanceStatus = statusServiceInterface.getOrNull(customerId, equipmentId, StatusDataType.OS_PERFORMANCE);
				if(osPerformanceStatus!=null) {
					OperatingSystemPerformance osPerformance = (OperatingSystemPerformance) osPerformanceStatus.getDetails();				
					if(osPerformance !=null && osPerformance.getTotalAvailableMemoryKb() > 0) {
						context.setTotalAvailableMemoryKb(osPerformance.getTotalAvailableMemoryKb());
					}
				}
			}
					
			// Update counters on the context
			context.addDataSamples(timestamp, model);
			
			LOG.debug("Processed {}", model);
		}

		private List<Alarm> cleanUpDuplicateAlarms(List<Alarm> existingAlarms) {

		    Map<AlarmCode, Alarm> alarmsToKeep= new HashMap<>();

		    //find out the latest existing alarms for each alarmCode
		    existingAlarms.forEach(a -> {
		        Alarm eA = alarmsToKeep.get(a.getAlarmCode());
		        if(eA == null) {
		            eA = a;
		            alarmsToKeep.put(a.getAlarmCode(), a);
		        }

		        if(eA.getCreatedTimestamp() < a.getCreatedTimestamp() ) {
		            alarmsToKeep.put(a.getAlarmCode(), a);
		        }
		    });

            List<Alarm> alarmsToRemove = new ArrayList<>(existingAlarms);
            alarmsToRemove.removeAll(alarmsToKeep.values());

            alarmsToRemove.forEach(a -> {
                try {
                    alarmServiceInterface.delete(a.getCustomerId(), a.getEquipmentId(), a.getAlarmCode(), a.getCreatedTimestamp());
                } catch(Exception e) {
                    LOG.debug("Alarm was already deleted: {}", a);
                }
            });

		    return new ArrayList<>(alarmsToKeep.values());
        }

        private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    @PostConstruct
	    private void postCreate() {
	    	
	    	Thread  equipmentAlarmsThread = new Thread( new Runnable() {
				
				@Override
				public void run() {
					LOG.info("Starting equipmentAlarmsThread");
					while(true) {
	                	LOG.trace("Checking if alarms need to be raised");

	                	//take a snapshot of existing contexts for this iteration of alarm checks
                        List<EquipmentAlarmsContext> contexts = new ArrayList<>(contextPerEquipmentIdMap.values());
                        
                        HashSet<Long> equipmentIdsFromContexts = new HashSet<>();
                        contexts.forEach(ctx -> equipmentIdsFromContexts.add(ctx.getEquipmentId()));

                        //get a list of equipment records for the current contexts
                        HashSet<Long> existingEquipmentIds = new HashSet<>();

	                	try {
                            List<Equipment> existingEquipment = equipmentServiceInterface.get(equipmentIdsFromContexts);
                            existingEquipment.forEach(e -> existingEquipmentIds.add(e.getId()));
                        } catch(Exception e) {
                            LOG.error("Error when retrieving existing equipment",  e);
                            sleep(checkAlarmsIntervalMs);
                            continue;
                        }

                        		                	
	                	for(EquipmentAlarmsContext context : contexts ) {
	                	    
	                	    if(!existingEquipmentIds.contains(context.getEquipmentId())) {
                                //equipment was removed, let's delete the context and remove all existing alarms for that equipmentId
	                	        try {
    	                	        contextPerEquipmentIdMap.remove(context.getEquipmentId());
    	                	        alarmServiceInterface.delete(context.getCustomerId(), context.getEquipmentId());
	                            } catch(Exception e) {
	                                LOG.error("Error when removing stale alarms for deleted equipment",  e);
	                            }
	                	        //nothing else to do for this context
	                	        continue;
	                	    }
	                		
	                		context.removeOldDataSamples();
	                		
	                		alarmCodeSet.forEach(alarmCode -> {
			                	try {
			                		//check alarms against thresholds
			                		//if alarm needs to be raised - check if it is currently present, and if not - raise it, and add it to the context
			                		if(context.isAlarmNeedsToBeRaised(alarmCode)) {
				                		Alarm alarm = new Alarm();
				                		alarm.setCustomerId(context.getCustomerId());
				                		alarm.setEquipmentId(context.getEquipmentId());
				                		alarm.setAlarmCode(alarmCode);
				                		alarm.setOriginatorType(OriginatorType.AP);
				                		alarm.setSeverity(StatusCode.requiresAttention);
				                		alarm.setScopeType(AlarmScopeType.EQUIPMENT);
				                		alarm.setScopeId("" + context.getEquipmentId());
				                		AlarmDetails alarmDetails = new AlarmDetails();
				                		alarmDetails.setMessage(alarmCode.getDescription());
				                		alarmDetails.setAffectedEquipmentIds(Collections.singletonList(context.getEquipmentId()));
				                		alarm.setDetails(alarmDetails);
		
				                		alarm = alarmServiceInterface.create(alarm);				                		
				                		context.getExistingAlarms().put(alarmCode, alarm);
			                		}
			                		
			                		if(context.isAlarmNeedsToBeCleared(alarmCode)) {
			                			//if alarm needs to be cleared - check if it is currently present, and if it is - clear it, and remove it from the context
			                			Alarm alarm = context.getExistingAlarms().remove(alarmCode);
			                			if(alarm!=null) {
                                            //All alarms that are handled by this stream processor logically exist only once-per-alarmCode-per-equipment.
                                            //In order to self-heal from the corner cases with the datastore where more than one alarm with the same alarmCode
                                            //    is raised per equipment (for example when the datastore experienced a split-brain scenario), we would remove
                                            //    all alarms with the specified alarmCode for this equipment.
                                            //Most of the time the existingAlarms list below would contain only one alarm.
                                            //
                                            List<Alarm> existingAlarms = alarmServiceInterface.get(alarm.getCustomerId(), Collections.singleton(alarm.getEquipmentId()), Collections.singleton(alarmCode));
                                            existingAlarms.forEach(a -> {
                                                try {
                                                    alarmServiceInterface.delete(a.getCustomerId(), a.getEquipmentId(), a.getAlarmCode(), a.getCreatedTimestamp());
                                                } catch(Exception e) {
                                                    LOG.debug("Alarm was already deleted: {}", alarm);
                                                }

                                            });
			                			}
			                		}
			                	} catch(Exception e) {
			                		LOG.error("Error when processing context for customer {}", context.getCustomerId(), e);
			                	}
		                	
	                		});
	                		
	                	}
	                	
	                	LOG.trace("Done alarms check");

	                	sleep(checkAlarmsIntervalMs);
	                	
					}
				}
			}, "equipmentAlarmsThread"); 
	    	
	    	equipmentAlarmsThread.setDaemon(true);
	    	equipmentAlarmsThread.start();
	    	
	    	//populate alarm codes this SP is responsible for
			alarmCodeSet.add(AlarmCode.CPUTemperature);
			alarmCodeSet.add(AlarmCode.CPUUtilization);
			alarmCodeSet.add(AlarmCode.MemoryUtilization);
			alarmCodeSet.add(AlarmCode.AccessPointIsUnreachable);

	    }

	    private static void sleep(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
	    }

}
