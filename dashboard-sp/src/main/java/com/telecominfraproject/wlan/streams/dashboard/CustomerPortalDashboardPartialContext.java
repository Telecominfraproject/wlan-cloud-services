package com.telecominfraproject.wlan.streams.dashboard;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import com.telecominfraproject.wlan.status.dashboard.models.events.CustomerPortalDashboardPartialEvent;

public class CustomerPortalDashboardPartialContext {
	
	private int customerId;	
	private ConcurrentHashMap<Long, CustomerPortalDashboardPartialEvent> timeBucketToPartialEventMap = new ConcurrentHashMap<>();
	private long lastPublishedTimestampMs;

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

    public long getLastPublishedTimestampMs() {
        return lastPublishedTimestampMs;
    }

    public void setLastPublishedTimestampMs(long lastPublishedTimestampMs) {
        this.lastPublishedTimestampMs = lastPublishedTimestampMs;
    }

}
