package com.telecominfraproject.wlan.alarm.models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.models.StatusCode;

public class AlarmCounts extends BaseJsonModel {
	
	private static final long serialVersionUID = -8513006445975878351L;
	
	private int customerId;
	private Boolean acknowledged;
	private Map<Long, Map<AlarmCode, AtomicInteger>> countsPerEquipmentIdMap = new HashMap<>();
	private Map<Long, AtomicInteger> totalCountsPerEquipmentIdMap = new HashMap<>();
	private Map<AlarmCode, AtomicInteger> totalCountsPerAlarmCodeMap = new HashMap<>();
	private Map<StatusCode, AtomicInteger> totalCountsBySeverityMap = new HashMap<>();
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public Boolean getAcknowledged() {
        return acknowledged;
    }
    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }
    public Map<Long, Map<AlarmCode, AtomicInteger>> getCountsPerEquipmentIdMap() {
		return countsPerEquipmentIdMap;
	}
	public void setCountsPerEquipmentIdMap(Map<Long, Map<AlarmCode, AtomicInteger>> countsPerEquipmentIdMap) {
		this.countsPerEquipmentIdMap = countsPerEquipmentIdMap;
	}
	public Map<Long, AtomicInteger> getTotalCountsPerEquipmentIdMap() {
        return totalCountsPerEquipmentIdMap;
    }
    public void setTotalCountsPerEquipmentIdMap(Map<Long, AtomicInteger> totalCountsPerEquipmentIdMap) {
        this.totalCountsPerEquipmentIdMap = totalCountsPerEquipmentIdMap;
    }
	public Map<AlarmCode, AtomicInteger> getTotalCountsPerAlarmCodeMap() {
		return totalCountsPerAlarmCodeMap;
	}
	public void setTotalCountsPerAlarmCodeMap(Map<AlarmCode, AtomicInteger> totalCountsPerAlarmCodeMap) {
		this.totalCountsPerAlarmCodeMap = totalCountsPerAlarmCodeMap;
	}
	public Map<StatusCode, AtomicInteger> getTotalCountsBySeverityMap() {
        return totalCountsBySeverityMap;
    }
    public void setTotalCountsBySeverityMap(Map<StatusCode, AtomicInteger> totalCountsBySeverityMap) {
        this.totalCountsBySeverityMap = totalCountsBySeverityMap;
    }
    
    public void addToCounter(long equipmentId, AlarmCode alarmCode, int valueToAdd) {
		//update total counts
		AtomicInteger totalCount = totalCountsPerAlarmCodeMap.get(alarmCode);
		if (totalCount == null) {
			totalCount = new AtomicInteger();
			totalCountsPerAlarmCodeMap.put(alarmCode, totalCount);
		}
		totalCount.addAndGet(valueToAdd);
		
		//update total counts by severity
		AtomicInteger totalBySeverityCount = totalCountsBySeverityMap.get(alarmCode.getSeverity());
		if (totalBySeverityCount == null) {
		    totalBySeverityCount = new AtomicInteger();
		    totalCountsBySeverityMap.put(alarmCode.getSeverity(), totalBySeverityCount);
		}
		totalBySeverityCount.addAndGet(valueToAdd);
		
		if(equipmentId>0) {
		    //update total counts by equipmentId
		    AtomicInteger totalEquipmentIdCount = totalCountsPerEquipmentIdMap.get(equipmentId);
            if (totalEquipmentIdCount == null) {
                totalEquipmentIdCount = new AtomicInteger();
                totalCountsPerEquipmentIdMap.put(equipmentId, totalEquipmentIdCount);
            }
            totalEquipmentIdCount.addAndGet(valueToAdd);
            
			//update per-equipmentId counts only for real equipmentIds
			Map<AlarmCode, AtomicInteger> perEquipmentMap = countsPerEquipmentIdMap.get(equipmentId);
			if(perEquipmentMap == null) {
				perEquipmentMap = new HashMap<>();
				countsPerEquipmentIdMap.put(equipmentId, perEquipmentMap);
			}
			
			//update counts by equipmentId and alarmCode
			AtomicInteger perEqCount = perEquipmentMap.get(alarmCode);
			if(perEqCount == null) {
				perEqCount = new AtomicInteger();
				perEquipmentMap.put(alarmCode, perEqCount);
			}
			
			perEqCount.addAndGet(valueToAdd);
		}
	}
	
	public int getCounter(long equipmentId, AlarmCode alarmCode) {
		
		if(equipmentId>0) {
		    if (alarmCode == null) {
		        AtomicInteger perEqTotalCount = totalCountsPerEquipmentIdMap.get(equipmentId);
                if (perEqTotalCount == null) {
                    return 0;
                }
                return perEqTotalCount.get();
            }
		    
			//get from per-equipmentId counts only for real equipmentIds
			Map<AlarmCode, AtomicInteger> perEquipmentMap = countsPerEquipmentIdMap.get(equipmentId);
			if(perEquipmentMap == null) {
				return 0;
			}
			
			AtomicInteger perEqCount = perEquipmentMap.get(alarmCode);
			if(perEqCount == null) {
				return 0;
			}
			
			return perEqCount.get();
			
		} else {
			//get from total counts
			AtomicInteger totalCount = totalCountsPerAlarmCodeMap.get(alarmCode);
			if (totalCount == null) {
				return 0;
			}
			
			return totalCount.get();
			
		}
	}
	
	public int getSeverityCounter(StatusCode severity) {
	    AtomicInteger ret = totalCountsBySeverityMap.get(severity);
	    if (ret == null) {
	        return 0;
	    }
	    return ret.get();
	}
	
}
