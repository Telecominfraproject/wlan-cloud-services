package com.telecominfraproject.wlan.servicemetric.models.events;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ServiceMetricRemovedEvent extends CustomerEvent<ServiceMetric> {
    private static final long serialVersionUID = 7142208488887559985L;

    public ServiceMetricRemovedEvent(ServiceMetric serviceMetric){
        super(serviceMetric.getCustomerId(), serviceMetric.getLastModifiedTimestamp(), serviceMetric);
    }
    
    /**
     * Constructor used by JSON
     */
    public ServiceMetricRemovedEvent() {
        super(0, 0, null);
    }
    
}
