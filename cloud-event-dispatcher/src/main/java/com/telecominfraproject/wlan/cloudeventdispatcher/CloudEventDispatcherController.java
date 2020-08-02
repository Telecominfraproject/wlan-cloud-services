	package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.servicemetric.ServiceMetricServiceInterface;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.SystemEventServiceInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@RestController
@RequestMapping(value="/api/cloudEventDispatcher")
public class CloudEventDispatcherController {

    private static final Logger LOG = LoggerFactory.getLogger(CloudEventDispatcherController.class);

    @Autowired private StreamInterface<ServiceMetric> metricStream;
    @Autowired @Qualifier("eventStreamInterface") private StreamInterface<SystemEventRecord> systemEventStream;
    @Autowired private ServiceMetricServiceInterface serviceMetricInterface;
    @Autowired private SystemEventServiceInterface systemEventInterface;

    @RequestMapping(value="/metric", method=RequestMethod.POST)
    public GenericResponse publishMetric(@RequestBody ServiceMetric metricRecord) {

        LOG.debug("calling publishMetric {}", metricRecord);
        
        long ts = System.currentTimeMillis();
        if (metricRecord.getCreatedTimestamp() == 0) {
            metricRecord.setCreatedTimestamp(ts);
        }

        metricStream.publish(metricRecord);
        serviceMetricInterface.create(metricRecord);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishMetric done");

        return ret;
    }
    
    @RequestMapping(value="/metrics", method=RequestMethod.POST)
    public GenericResponse publishMetrics(@RequestBody List<ServiceMetric> metricList) {

        LOG.debug("calling publishMetrics {}", metricList);

        if(metricList==null || metricList.isEmpty()) {
            return new GenericResponse(true, "empty metrics");
        }
        
        AtomicLong ts = new AtomicLong(System.currentTimeMillis());
        metricList.forEach(m -> {
            if (m.getCreatedTimestamp() == 0) {
                m.setCreatedTimestamp(ts.incrementAndGet());
            }
        });

        metricStream.publish(metricList);
        serviceMetricInterface.create(metricList);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishMetricList done");

        return ret;
    }

    @RequestMapping(value="/event", method=RequestMethod.POST)
    public GenericResponse publishEvent(@RequestBody SystemEventRecord systemEventRecord) {
        LOG.debug("calling publishEvent {}", systemEventRecord);

        long ts = System.currentTimeMillis();
        if (systemEventRecord.getEventTimestamp() == 0) {
            systemEventRecord.setEventTimestamp(ts);
        }

        systemEventStream.publish(systemEventRecord);
        systemEventInterface.create(systemEventRecord);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishEvent for {} done", systemEventRecord);
        return ret;
    }
 
    @RequestMapping(value="/events", method=RequestMethod.POST)
    public GenericResponse publishEvents(@RequestBody List<SystemEventRecord> systemEventRecords) {

        LOG.debug("calling publishEvents {}", systemEventRecords.size());

        if(systemEventRecords==null || systemEventRecords.isEmpty()) {
            return new GenericResponse(true, "empty event list");
        }
        
        AtomicLong ts = new AtomicLong(System.currentTimeMillis());
        systemEventRecords.forEach(m -> {
            if (m.getEventTimestamp() == 0) {
                m.setEventTimestamp(ts.incrementAndGet());
            }
        });

        systemEventStream.publish(systemEventRecords);
        systemEventInterface.create(systemEventRecords);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishEvents done");

        return ret;
    }

    
}
