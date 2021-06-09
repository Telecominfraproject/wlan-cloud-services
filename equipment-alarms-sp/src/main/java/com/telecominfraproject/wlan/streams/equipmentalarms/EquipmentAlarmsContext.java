package com.telecominfraproject.wlan.streams.equipmentalarms;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;

/**
 * <br>This context keeps track of information needed for raising/clearing the alarms with the following codes:
 * <ul> 
 * 	<li>AlarmCode.CPUTemperature
 * 	<li>AlarmCode.CPUUtilization
 * 	<li>AlarmCode.MemoryUtilization
 * 	<li>AlarmCode.NoMetricsReceived
 * </ul>
 * 
 * @author dtop
 */
public class EquipmentAlarmsContext {

	private final int customerId;
	private final long equipmentId;
	private final Map<AlarmCode, Alarm> existingAlarms = new ConcurrentHashMap<>();
	private final long timeBucketMs;
	private final int temperatureThresholdInC;
	private final int cpuUtilThresholdPct;
	private final int memoryUtilThresholdPct;
	
	private final Map<Long, Integer> cpuTempSamples = new ConcurrentHashMap<>();
	private final Map<Long, Integer> cpuUtilSamples = new ConcurrentHashMap<>();
	private final Map<Long, Integer> memoryUtilSamples = new ConcurrentHashMap<>();
	private final Map<Long, Long> metricReceivedTimestamps = new ConcurrentHashMap<>();
	
	private long totalAvailableMemoryKb;
	private final long contextCreationTimestampMs = System.currentTimeMillis(); 
	
	public EquipmentAlarmsContext(int customerId, long equipmentId, List<Alarm> existingAlarmsList, long timeBucketMs, int temperatureThresholdInC, int cpuUtilThresholdPct, int memoryUtilThresholdPct) {
		this.customerId = customerId;
		this.equipmentId = equipmentId;
		existingAlarmsList.forEach(a -> this.existingAlarms.put(a.getAlarmCode(), a));
		this.timeBucketMs = timeBucketMs;
		this.temperatureThresholdInC = temperatureThresholdInC;
		this.cpuUtilThresholdPct = cpuUtilThresholdPct;
		this.memoryUtilThresholdPct = memoryUtilThresholdPct;
	}

	public long getTotalAvailableMemoryKb() {
		return totalAvailableMemoryKb;
	}

	public void setTotalAvailableMemoryKb(long totalAvailableMemoryKb) {
		this.totalAvailableMemoryKb = totalAvailableMemoryKb;
	}

	public int getCustomerId() {
		return customerId;
	}

	public long getEquipmentId() {
		return equipmentId;
	}

	public Map<AlarmCode, Alarm> getExistingAlarms() {
		return existingAlarms;
	}


	public void addDataSamples(long timestamp, ApNodeMetrics model) {
		// add new samples
		if(model.getApPerformance()!=null ) {
			if(model.getApPerformance().getCpuTemperature()!=null) {
				cpuTempSamples.put(timestamp, model.getApPerformance().getCpuTemperature());
			}
			
			if(model.getApPerformance().getAvgCpuUtilized()!=null) {
				cpuUtilSamples.put(timestamp, model.getApPerformance().getAvgCpuUtilized().intValue());
			}
			
			if(model.getApPerformance().getFreeMemory()!=null && totalAvailableMemoryKb>0) {
				memoryUtilSamples.put(timestamp, 100 - (int) (model.getApPerformance().getFreeMemory() * 100 / totalAvailableMemoryKb));
			}
		}
		
		//we are using our own timestamp in here in case AP's time is out of sync - we do not want to raise connectivity alarm in that case
		Long currentTs = System.currentTimeMillis();
		metricReceivedTimestamps.put(currentTs, currentTs);

	}

	public void removeOldDataSamples() {
		// remove samples older than timeBucketMs
		long timeThresholdMs = System.currentTimeMillis() - timeBucketMs;
		cpuTempSamples.entrySet().removeIf( t -> t.getKey() < timeThresholdMs );
		cpuUtilSamples.entrySet().removeIf( t -> t.getKey() < timeThresholdMs );
		memoryUtilSamples.entrySet().removeIf( t -> t.getKey() < timeThresholdMs );
		metricReceivedTimestamps.entrySet().removeIf( t -> t.getKey() < timeThresholdMs );
	}

	public boolean isAlarmNeedsToBeRaised(AlarmCode alarmCode) {

		if(existingAlarms.containsKey(alarmCode) || System.currentTimeMillis() < contextCreationTimestampMs + timeBucketMs) {
			//no need to check for thresholds - alarm is either already present, or it is too early to tell because the first time bucket has not been filled yet
			return false;
		}
		
		boolean ret = false;
		AtomicInteger sum = new AtomicInteger();
		AtomicInteger count = new AtomicInteger();
		
		//check alarms against thresholds
		if(alarmCode.getId() == AlarmCode.NoMetricsReceived.getId()) {
			ret = metricReceivedTimestamps.isEmpty();
		} else if(alarmCode.getId() == AlarmCode.CPUTemperature.getId()) {
			cpuTempSamples.values().forEach(v -> { sum.addAndGet(v); count.incrementAndGet(); });
			if(count.get() > 0) {
				int avg = sum.get() / count.get();
				ret = avg >= temperatureThresholdInC ;
			} 
		} else if(alarmCode.getId() == AlarmCode.CPUUtilization.getId()) {
			cpuUtilSamples.values().forEach(v -> { sum.addAndGet(v); count.incrementAndGet(); });
			if(count.get() > 0) {
				int avg = sum.get() / count.get();
				ret = avg >= cpuUtilThresholdPct ;
			} 
		} else if(alarmCode.getId() == AlarmCode.MemoryUtilization.getId()) {
			memoryUtilSamples.values().forEach(v -> { sum.addAndGet(v); count.incrementAndGet(); });
			if(count.get() > 0) {
				int avg = sum.get() / count.get();
				ret = avg >= memoryUtilThresholdPct ;
			} 
		}
		
		return ret;
	}

	public boolean isAlarmNeedsToBeCleared(AlarmCode alarmCode) {
		
		if(!existingAlarms.containsKey(alarmCode) || System.currentTimeMillis() < contextCreationTimestampMs + timeBucketMs) {
			//no need to check for thresholds - alarm is either not present, or it is too early to tell because the first time bucket has not been filled yet
			return false;
		}
		
		boolean ret = false;
		AtomicInteger sum = new AtomicInteger();
		AtomicInteger count = new AtomicInteger();
		
		//check alarms against thresholds
        if(alarmCode.getId() == AlarmCode.NoMetricsReceived.getId()) {
			ret = !metricReceivedTimestamps.isEmpty();
        } else if(alarmCode.getId() == AlarmCode.CPUTemperature.getId()) {
			cpuTempSamples.values().forEach(v -> { sum.addAndGet(v); count.incrementAndGet(); });
			if(count.get() > 0) {
				int avg = sum.get() / count.get();
				ret = avg < temperatureThresholdInC ;
			} else {
				//no new data available, but the alarm exists -> clear the alarm
				ret = true;
			}
        } else if(alarmCode.getId() == AlarmCode.CPUUtilization.getId()) {
			cpuUtilSamples.values().forEach(v -> { sum.addAndGet(v); count.incrementAndGet(); });
			if(count.get() > 0) {
				int avg = sum.get() / count.get();
				ret = avg < cpuUtilThresholdPct ;
			} else {
				//no new data available, but the alarm exists -> clear the alarm
				ret = true;
			} 
        } else if(alarmCode.getId() == AlarmCode.MemoryUtilization.getId()) {
			memoryUtilSamples.values().forEach(v -> { sum.addAndGet(v); count.incrementAndGet(); });
			if(count.get() > 0) {
				int avg = sum.get() / count.get();
				ret = avg < memoryUtilThresholdPct ;
			} else {
				//no new data available, but the alarm exists -> clear the alarm
				ret = true;
			} 
		}
		
		return ret;
	}

	
}
