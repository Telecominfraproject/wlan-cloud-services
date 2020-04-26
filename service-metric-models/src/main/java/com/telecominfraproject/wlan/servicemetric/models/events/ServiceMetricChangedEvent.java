package com.telecominfraproject.wlan.servicemetric.models.events;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ServiceMetricChangedEvent extends CustomerEvent<ServiceMetric> {
    private static final long serialVersionUID = 7142209997917559985L;

    public ServiceMetricChangedEvent(ServiceMetric serviceMetric){
        super(serviceMetric.getCustomerId(), serviceMetric.getLastModifiedTimestamp(), serviceMetric);
    }
    
    /**
     * Constructor used by JSON
     */
    public ServiceMetricChangedEvent() {
        super(0, 0, null);
    }
    
}
