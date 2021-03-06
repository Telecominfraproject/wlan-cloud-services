package com.telecominfraproject.wlan.status.dashboard.models.events;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtop
 * <br>This model contains aggregate data for counters for a portion of all customer equipment (non-overlapping subsets).
 * It is produced by CustomerPortalDashboardPartialAggregator, placed on a kafka queue (customer_events), and consumed by CustomerPortalDashboardAggregator. 
 */
public class CustomerPortalDashboardPartialEvent extends SystemEvent implements HasCustomerId {

	private static final long serialVersionUID = -8139895289619328335L;

	private int customerId;
	
	/**
	 * Time Bucket Id for this partial event
	 * All metrics/events that have (createdTimestamp % timeBucketMs == timeBucketId) are counted in this partial event.   
	 */
	private long timeBucketId;
	
	/**
	 * Length of the bucket in milliseconds
	 */
	private long timeBucketMs = TimeUnit.MINUTES.toMillis(5);
	
	private AtomicInteger equipmentInServiceCount = new AtomicInteger();
	private AtomicInteger equipmentWithClientsCount = new AtomicInteger();
	
	private Map<RadioType, AtomicInteger> associatedClientsCountPerRadio = Collections.synchronizedMap(new EnumMap<>(RadioType.class));
	
	private AtomicLong trafficBytesDownstream = new AtomicLong();
	private AtomicLong trafficBytesUpstream = new AtomicLong();
	
	private Map<String, AtomicInteger> clientCountPerOui = new ConcurrentHashMap<>();
	
	private Map<StatusCode, AtomicInteger> alarmsCountBySeverity = new ConcurrentHashMap<>();
	
	//intermediate data, not serialized, used when computing various counts
	@JsonIgnore
    private Set<Long> equipmentIds = Collections.synchronizedSet(new HashSet<>());
    //intermediate data, not serialized, used when computing various counts
    @JsonIgnore
    private Set<Long> equipmentIdsWithClients = Collections.synchronizedSet(new HashSet<>());
    //intermediate data, not serialized, used when computing various counts
    @JsonIgnore
    private Map<RadioType, Set<MacAddress>> clientMacsPerRadio = new EnumMap<>(RadioType.class);


	public long getTimeBucketId() {
		return timeBucketId;
	}

	public void setTimeBucketId(long timeBucketId) {
		this.timeBucketId = timeBucketId;
	}

	public long getTimeBucketMs() {
		return timeBucketMs;
	}

	public void setTimeBucketMs(long timeBucketMs) {
		this.timeBucketMs = timeBucketMs;
	}

	public AtomicInteger getEquipmentInServiceCount() {
		return equipmentInServiceCount;
	}

	public void setEquipmentInServiceCount(AtomicInteger equipmentInServiceCount) {
		this.equipmentInServiceCount = equipmentInServiceCount;
	}

	public AtomicInteger getEquipmentWithClientsCount() {
		return equipmentWithClientsCount;
	}

	public void setEquipmentWithClientsCount(AtomicInteger equipmentWithClientsCount) {
		this.equipmentWithClientsCount = equipmentWithClientsCount;
	}

	public Map<RadioType, AtomicInteger> getAssociatedClientsCountPerRadio() {
		return associatedClientsCountPerRadio;
	}

	public void setAssociatedClientsCountPerRadio(Map<RadioType, AtomicInteger> associatedClientsCountPerRadio) {
		this.associatedClientsCountPerRadio = associatedClientsCountPerRadio;
	}

	public AtomicLong getTrafficBytesDownstream() {
		return trafficBytesDownstream;
	}

	public void setTrafficBytesDownstream(AtomicLong trafficBytesDownstream) {
		this.trafficBytesDownstream = trafficBytesDownstream;
	}

	public AtomicLong getTrafficBytesUpstream() {
		return trafficBytesUpstream;
	}

	public void setTrafficBytesUpstream(AtomicLong trafficBytesUpstream) {
		this.trafficBytesUpstream = trafficBytesUpstream;
	}

	public Map<String, AtomicInteger> getClientCountPerOui() {
		return clientCountPerOui;
	}

	public void setClientCountPerOui(Map<String, AtomicInteger> clientCountPerOui) {
		this.clientCountPerOui = clientCountPerOui;
	}
	
	public Map<StatusCode, AtomicInteger> getAlarmsCountBySeverity() {
		return alarmsCountBySeverity;
	}

	public void setAlarmsCountBySeverity(Map<StatusCode, AtomicInteger> alarmsCountBySeverity) {
		this.alarmsCountBySeverity = alarmsCountBySeverity;
	}

	/// Utility methods
	
	public void incrementAssociatedClientsCountPerRadio(RadioType radioType, int value) {
		AtomicInteger counter = associatedClientsCountPerRadio.get(radioType);
		if(counter == null) {
			counter = new AtomicInteger();
			counter = associatedClientsCountPerRadio.putIfAbsent(radioType, counter);
			if(counter == null) {
				counter = associatedClientsCountPerRadio.get(radioType);
			}
		}
		
		counter.addAndGet(value);
	}

	public void incrementClientCountPerOui(String oui, int value) {
		AtomicInteger counter = clientCountPerOui.get(oui);
		if(counter == null) {
			counter = new AtomicInteger();
			counter = clientCountPerOui.putIfAbsent(oui, counter);
			if(counter == null) {
				counter = clientCountPerOui.get(oui);
			}
		}
		
		counter.addAndGet(value);
	}
	
	public void incrementAlarmsCountBySeverity(StatusCode severity, int value) {
		AtomicInteger counter = alarmsCountBySeverity.get(severity);
		if(counter == null) {
			counter = new AtomicInteger();
			counter = alarmsCountBySeverity.putIfAbsent(severity, counter);
			if(counter == null) {
				counter = alarmsCountBySeverity.get(severity);
			}
		}
		
		counter.addAndGet(value);
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Override
	public CustomerPortalDashboardPartialEvent clone() {
		CustomerPortalDashboardPartialEvent ret = (CustomerPortalDashboardPartialEvent) super.clone();
		
		if(associatedClientsCountPerRadio!=null) {
			ret.associatedClientsCountPerRadio = Collections.synchronizedMap(new EnumMap<>(RadioType.class));
			associatedClientsCountPerRadio.forEach((k,v) -> ret.associatedClientsCountPerRadio.put(k, new AtomicInteger(v.get())));			
		}
		
		if(clientCountPerOui!=null) {
			ret.clientCountPerOui = new ConcurrentHashMap<>();
			clientCountPerOui.forEach((k,v) -> ret.clientCountPerOui.put(k, new AtomicInteger(v.get())));			
		}
		
		if(alarmsCountBySeverity!=null) {
			ret.alarmsCountBySeverity = new ConcurrentHashMap<>();
			alarmsCountBySeverity.forEach((k,v) -> ret.alarmsCountBySeverity.put(k, new AtomicInteger(v.get())));			
		}
		
		return ret;		
	}

	   public void addClientMacs(Map<RadioType, List<MacAddress>> clientMacAddressesPerRadio) {
	        //make sure all required sets exist
	       clientMacAddressesPerRadio.keySet().forEach((rt) -> {
	            Set<MacAddress> macSet = clientMacsPerRadio.get(rt);
	            if(macSet == null) {
	                macSet = Collections.synchronizedSet(new HashSet<>());
	                macSet = clientMacsPerRadio.putIfAbsent(rt, macSet);
	                if(macSet == null) {
	                    macSet = clientMacsPerRadio.get(rt);
	                }
	            }
	        });
	        
	        //add each client mac into appropriate set
	       clientMacAddressesPerRadio.forEach((rt, macList) -> {
	            Set<MacAddress> macSet = clientMacsPerRadio.get(rt);
	            macList.forEach(m -> macSet.add(m));            
	        });

	    }
	    
       @JsonIgnore
	    private Map<String, AtomicInteger> getClientMacCountsPerOui(){
	        Map<String, AtomicInteger> ret = new HashMap<>();
	        clientMacsPerRadio.values().forEach(macSet -> macSet.forEach( m-> {
	            String oui = m.toOuiString();
	            AtomicInteger cnt = ret.get(oui);
	            if(cnt == null) {
	                cnt = new AtomicInteger();
	                cnt = ret.putIfAbsent(oui, cnt);
	                if(cnt == null) {
	                    cnt = ret.get(oui);
	                }
	            }
	            cnt.incrementAndGet();
	        }));
	        
	        return ret;
	    }

       @JsonIgnore
       public Set<Long> getEquipmentIds() {
           return equipmentIds;
       }

       @JsonIgnore
       public Set<Long> getEquipmentIdsWithClients() {
           return equipmentIdsWithClients;
       }

        public void aggregateCounters() {
            equipmentInServiceCount.set(this.equipmentIds.size());
            equipmentWithClientsCount.set(this.equipmentIdsWithClients.size());
            clientMacsPerRadio.forEach( (rt,macSet) -> this.associatedClientsCountPerRadio.put(rt, new AtomicInteger(macSet.size())));
            this.getClientMacCountsPerOui().forEach((oui, cnt) -> this.clientCountPerOui.put(oui, cnt));
        }

}
