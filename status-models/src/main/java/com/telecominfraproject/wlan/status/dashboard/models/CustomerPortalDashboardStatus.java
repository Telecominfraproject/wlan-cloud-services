package com.telecominfraproject.wlan.status.dashboard.models;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.dashboard.models.events.CustomerPortalDashboardPartialEvent;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

public class CustomerPortalDashboardStatus extends StatusDetails {

	private static final long serialVersionUID = -8139895289819328335L;

	/**
	 * Time Bucket Id for this event
	 * All CustomerPortalDashboardPartialEvents that have the same timeBucketId are counted in this status object.   
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
	
	private int totalProvisionedEquipment;
	private Map<String, AtomicInteger> equipmentCountPerOui = new ConcurrentHashMap<>();
	
	private Map<StatusCode, AtomicInteger> alarmsCountBySeverity = Collections.synchronizedMap(new EnumMap<>(StatusCode.class));
	

	@Override
	public StatusDataType getStatusDataType() {
		return StatusDataType.CUSTOMER_DASHBOARD;
	}

	public void applyPartialEvent(CustomerPortalDashboardPartialEvent event){
		if(event == null || event.getTimeBucketId() != timeBucketId) {
			return;
		}
		
		equipmentInServiceCount.addAndGet(event.getEquipmentInServiceCount().get());
		equipmentWithClientsCount.addAndGet(event.getEquipmentWithClientsCount().get());
		trafficBytesDownstream.addAndGet(event.getTrafficBytesDownstream().get());
		trafficBytesUpstream.addAndGet(event.getTrafficBytesUpstream().get());
		
		event.getAssociatedClientsCountPerRadio().forEach((rt, count) -> incrementAssociatedClientsCountPerRadio(rt, count.get()) );
		event.getClientCountPerOui().forEach((oui, count) -> incrementClientCountPerOui(oui, count.get()) );
		event.getAlarmsCountBySeverity().forEach((severity, count) -> incrementAlarmsCountBySeverity(severity, count.get()));
		
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

	public void incrementEquipmentCountPerOui(String oui, int value) {
		AtomicInteger counter = equipmentCountPerOui.get(oui);
		if(counter == null) {
			counter = new AtomicInteger();
			counter = equipmentCountPerOui.putIfAbsent(oui, counter);
			if(counter == null) {
				counter = equipmentCountPerOui.get(oui);
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

	public int getTotalProvisionedEquipment() {
		return totalProvisionedEquipment;
	}

	public void setTotalProvisionedEquipment(int totalProvisionedEquipment) {
		this.totalProvisionedEquipment = totalProvisionedEquipment;
	}

	public Map<String, AtomicInteger> getEquipmentCountPerOui() {
		return equipmentCountPerOui;
	}

	public void setEquipmentCountPerOui(Map<String, AtomicInteger> equipmentCountPerOui) {
		this.equipmentCountPerOui = equipmentCountPerOui;
	}
	
	public Map<StatusCode, AtomicInteger> getAlarmsCountBySeverity() {
		return alarmsCountBySeverity;
	}

	public void setAlarmsCountBySeverity(Map<StatusCode, AtomicInteger> alarmsCountBySeverity) {
		this.alarmsCountBySeverity = alarmsCountBySeverity;
	}
	
	@Override
	public CustomerPortalDashboardStatus clone() {
		CustomerPortalDashboardStatus ret = (CustomerPortalDashboardStatus) super.clone();
		
		if(clientCountPerOui !=null) {
			ret.clientCountPerOui = new ConcurrentHashMap<>();
			clientCountPerOui.forEach((k, v) -> ret.clientCountPerOui.put(k, new AtomicInteger(v.get())));
		}

		if(equipmentCountPerOui != null) {
			ret.equipmentCountPerOui = new ConcurrentHashMap<>();
			equipmentCountPerOui.forEach((k, v) -> ret.equipmentCountPerOui.put(k, new AtomicInteger(v.get())));
		}
		
		if(associatedClientsCountPerRadio != null) {
			ret.associatedClientsCountPerRadio = Collections.synchronizedMap(new EnumMap<>(RadioType.class));
			associatedClientsCountPerRadio.forEach((k, v) -> ret.associatedClientsCountPerRadio.put(k, new AtomicInteger(v.get())));
		}
		
		if(alarmsCountBySeverity != null) {
			ret.alarmsCountBySeverity = Collections.synchronizedMap(new EnumMap<>(StatusCode.class));
			alarmsCountBySeverity.forEach((k, v) -> ret.alarmsCountBySeverity.put(k, new AtomicInteger(v.get())));
		}
		
		return ret;
	}

}
