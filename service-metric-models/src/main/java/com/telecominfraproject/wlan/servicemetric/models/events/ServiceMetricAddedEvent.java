package com.telecominfraproject.wlan.servicemetric.models.events;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ServiceMetricAddedEvent extends CustomerEvent<ServiceMetric> {
    private static final long serialVersionUID = 7142208487917559985L;

    public ServiceMetricAddedEvent(ServiceMetric serviceMetric){
        super(serviceMetric.getCustomerId(), serviceMetric.getLastModifiedTimestamp(), serviceMetric);
    }
    
    /**
     * Constructor used by JSON
     */
    public ServiceMetricAddedEvent() {
        super(0, 0, null);
    }
    
}
