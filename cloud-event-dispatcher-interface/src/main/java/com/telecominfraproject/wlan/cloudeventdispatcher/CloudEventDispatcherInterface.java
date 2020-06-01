package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtoptygin
 *
 */
public interface CloudEventDispatcherInterface {

	void publishMetric(ServiceMetric serviceMetric);

    void publishMetrics(List<ServiceMetric> serviceMetricList);

    void publishEvent(SystemEventRecord systemEventRecord);

    void publishEvents(List<SystemEventRecord> systemEventRecordList);

    default void publishEvent(SystemEvent systemEvent)  {
    	publishEvent(new SystemEventRecord(systemEvent));
    }

    default void publishEventsBulk(List<SystemEvent> systemEvents)  {
    	if(systemEvents==null || systemEvents.isEmpty()) {
    		return;
    	}
    	
    	List<SystemEventRecord> records = new ArrayList<>();
    	systemEvents.forEach(e -> records.add(new SystemEventRecord(e)));

    	publishEvents(records);
    }

}
