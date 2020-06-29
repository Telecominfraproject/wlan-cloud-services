package com.telecominfraproject.wlan.streams.dashboard;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import com.telecominfraproject.wlan.status.dashboard.models.CustomerPortalDashboardStatus;

public class CustomerPortalDashboardContext {

	private int customerId;	
	private ConcurrentHashMap<Long, CustomerPortalDashboardStatus> timeBucketToDashbordStatusMap = new ConcurrentHashMap<>();

	public CustomerPortalDashboardContext(int customerId) {
		this.customerId = customerId;
	}

	public int getCustomerId() {
		return customerId;
	}
	
	public CustomerPortalDashboardStatus getOrCreateDashboardStatus(long timeBucketId) {
		CustomerPortalDashboardStatus ret = timeBucketToDashbordStatusMap.get(timeBucketId);
		if(ret == null) {
			ret = new CustomerPortalDashboardStatus();
			ret.setTimeBucketId(timeBucketId);
			ret = timeBucketToDashbordStatusMap.putIfAbsent(timeBucketId, ret);
			if(ret == null) {
				ret = timeBucketToDashbordStatusMap.get(timeBucketId);
			}
		}

		return ret;
	}

	public CustomerPortalDashboardStatus getOldestDashboardStatusOrNull() {
		if(timeBucketToDashbordStatusMap.isEmpty()) {
			return null;
		}
		
		long timeBucketId = Collections.min(timeBucketToDashbordStatusMap.keySet());		
		CustomerPortalDashboardStatus ret = timeBucketToDashbordStatusMap.get(timeBucketId);
		
		return ret;
	}

	public void removeDashboardStatus(long timeBucketId) {
		timeBucketToDashbordStatusMap.remove(timeBucketId);		
	}

}
