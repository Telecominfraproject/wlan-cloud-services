package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.List;

import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.servicemetrics.models.SingleMetricRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@Component
public class CloudEventDispatcherEmpty implements CloudEventDispatcherInterface {

	@Override
	public void publishMetric(SingleMetricRecord metricRecord) {
		// do nothing
	}

	@Override
	public void publishMetrics(List<SingleMetricRecord> metricRecordList) {
		// do nothing
	}

	@Override
	public void publishEvent(SystemEventRecord systemEventRecord) {
		// do nothing
	}

	@Override
	public void publishEvents(List<SystemEventRecord> systemEventRecordList) {
		// do nothing
	}

}
