package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@Component
public class CloudEventDispatcherLocal implements CloudEventDispatcherInterface {

    @Autowired private CloudEventDispatcherController cloudEventDispatcherController;

	@Override
	public void publishMetric(ServiceMetric metricRecord) {
		cloudEventDispatcherController.publishMetric(metricRecord);
	}

	@Override
	public void publishMetrics(List<ServiceMetric> metricRecordList) {
		cloudEventDispatcherController.publishMetrics(metricRecordList);
	}

	@Override
	public void publishEvent(SystemEventRecord systemEventRecord) {
		cloudEventDispatcherController.publishEvent(systemEventRecord);	
	}

	@Override
	public void publishEvents(List<SystemEventRecord> systemEventRecordList) {
		cloudEventDispatcherController.publishEvents(systemEventRecordList);
	}

}
