package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.servicemetric.ServiceMetricServiceInterface;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@RestController
@RequestMapping(value="/api/cloudEventDispatcher")
public class CloudEventDispatcherController {

    private static final Logger LOG = LoggerFactory.getLogger(CloudEventDispatcherController.class);

    @Autowired private StreamInterface<ServiceMetric> metricStream;
    @Autowired private StreamInterface<SystemEventRecord> systemEventStream;
    @Autowired private ServiceMetricServiceInterface serviceMetricInterface;

    @RequestMapping(value="/metric", method=RequestMethod.POST)
    public GenericResponse publishMetric(@RequestBody ServiceMetric metricRecord) {

        LOG.debug("calling publishMetric {}", metricRecord);
        
        metricStream.publish(metricRecord);
        
        serviceMetricInterface.create(metricRecord);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishMetric done");

        return ret;
    }
    
    @RequestMapping(value="/metrics", method=RequestMethod.POST)
    public GenericResponse publishMetrics(@RequestBody List<ServiceMetric> metricList) {

        LOG.debug("calling publishMetrics {}", metricList);

        for (ServiceMetric metricRecord : metricList) {
            metricStream.publish(metricRecord);
        }
        
        serviceMetricInterface.create(metricList);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishMetricList done");

        return ret;
    }

    @RequestMapping(value="/event", method=RequestMethod.POST)
    public GenericResponse publishEvent(@RequestBody SystemEventRecord systemEventRecord) {
        LOG.debug("calling publishEvent {}", systemEventRecord);
                       
        systemEventStream.publish(systemEventRecord);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishEvent for {} done", systemEventRecord);
        return ret;
    }
 
    @RequestMapping(value="/events", method=RequestMethod.POST)
    public GenericResponse publishEvents(@RequestBody List<SystemEventRecord> systemEventRecords) {

        LOG.debug("calling publishEvents {}", systemEventRecords.size());

        for (SystemEventRecord ser : systemEventRecords) {            
            systemEventStream.publish(ser);
        }
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishEvents done");

        return ret;
    }

    
}
