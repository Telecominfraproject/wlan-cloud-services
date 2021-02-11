package com.telecominfraproject.wlan.status.dashboard.models.events;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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

}
