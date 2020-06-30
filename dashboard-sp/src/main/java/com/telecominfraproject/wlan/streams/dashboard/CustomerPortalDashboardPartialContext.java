package com.telecominfraproject.wlan.streams.dashboard;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.systemevent.aggregation.models.CustomerPortalDashboardPartialEvent;

public class CustomerPortalDashboardPartialContext {
	
	private int customerId;	
	private ConcurrentHashMap<Long, CustomerPortalDashboardPartialEvent> timeBucketToPartialEventMap = new ConcurrentHashMap<>();
	private Set<Long> equipmentIds = Collections.synchronizedSet(new HashSet<>());
	private Set<Long> equipmentIdsWithClients = Collections.synchronizedSet(new HashSet<>());

	private Map<RadioType, Set<MacAddress>> clientMacsPerRadio = new EnumMap<>(RadioType.class);

	public CustomerPortalDashboardPartialContext(int customerId) {
		this.customerId = customerId;
	}

	public int getCustomerId() {
		return customerId;
	}
	
	public CustomerPortalDashboardPartialEvent getEventForOldestTimeBucket() {
		CustomerPortalDashboardPartialEvent ret = null;
		
		if(!timeBucketToPartialEventMap.isEmpty()) {
			long oldestBucketKey = Collections.min(timeBucketToPartialEventMap.keySet());
			ret = timeBucketToPartialEventMap.get(oldestBucketKey);
		}
		
		return ret;
	}
	
	public Set<Long> getEquipmentIds() {
		return equipmentIds;
	}
	public Set<Long> getEquipmentIdsWithClients() {
		return equipmentIdsWithClients;
	}
	
	/**
	 * Get the CustomerPortalDashboardPartialEvent for the specified time bucket, or create one is it is not present
	 * @param timeBucketId
	 * @return CustomerPortalDashboardPartialEvent for the specified time bucket (auto-created if not present)
	 */
	public CustomerPortalDashboardPartialEvent getOrCreatePartialEvent(long timeBucketId) {
		CustomerPortalDashboardPartialEvent ret = timeBucketToPartialEventMap.get(timeBucketId);
		if(ret == null) {
			ret = new CustomerPortalDashboardPartialEvent();
			ret.setCustomerId(customerId);
			ret.setTimeBucketId(timeBucketId);
			ret.setEventTimestamp(timeBucketId);
			ret = timeBucketToPartialEventMap.putIfAbsent(timeBucketId, ret);
			if(ret == null) {
				ret = timeBucketToPartialEventMap.get(timeBucketId);
			}
		}

		return ret;
	}
	
	public CustomerPortalDashboardPartialEvent getOldestPartialEventOrNull() {
		if(timeBucketToPartialEventMap.isEmpty()) {
			return null;
		}
		
		long timeBucketId = Collections.min(timeBucketToPartialEventMap.keySet());		
		CustomerPortalDashboardPartialEvent ret = timeBucketToPartialEventMap.get(timeBucketId);
		
		return ret;
	}
	
	public void removePartialEvent(long timeBucketId) {
		timeBucketToPartialEventMap.remove(timeBucketId);		
	}

	
	public void addClientMacs(ApNodeMetrics model) {
		//make sure all required sets exist
		model.getClientMacAddressesPerRadio().keySet().forEach((rt) -> {
			Set<MacAddress> macSet = clientMacsPerRadio.get(rt);
			if(macSet == null) {
				macSet = Collections.synchronizedSet(new HashSet<>());
				macSet = clientMacsPerRadio.putIfAbsent(rt, macSet);
			}
		});
		
		//add each client mac into appropriate set
		model.getClientMacAddressesPerRadio().forEach((rt, macList) -> {
			Set<MacAddress> macSet = clientMacsPerRadio.get(rt);
			macList.forEach(m -> macSet.add(m));			
		});

	}
	
	public Map<String, AtomicInteger> getClientMacCountsPerOui(){
		Map<String, AtomicInteger> ret = new HashMap<>();
		clientMacsPerRadio.values().forEach(macSet -> macSet.forEach( m-> {
			String oui = m.toOuiString();
			AtomicInteger cnt = ret.get(oui);
			if(cnt == null) {
				cnt = new AtomicInteger();
				cnt = ret.putIfAbsent(oui, cnt);
			}
			cnt.incrementAndGet();
		}));
		
		return ret;
	}
	
	public Map<RadioType, Integer> getClientCountsPerRadio(){
		Map<RadioType, Integer> ret = new HashMap<>();
		clientMacsPerRadio.forEach( (rt,macSet) -> ret.put(rt, macSet.size()));
		return ret;
	}

}
