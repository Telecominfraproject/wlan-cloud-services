	package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
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
    
    @Autowired private EquipmentServiceInterface equipmentServiceInterface;
    @Autowired private CacheManager cacheManagerShortLived;
    
    private Cache equipmentLocationCache;

    @PostConstruct
    private void postCreate() {
        equipmentLocationCache = cacheManagerShortLived.getCache("equipment_location_cache");
    }

    private Long getEquipmentLocation(long equipmentId) {
        Long eqLocation = null;

        try {
            eqLocation = equipmentLocationCache.get(equipmentId, () -> {
                Equipment eq = equipmentServiceInterface.getOrNull(equipmentId);
                return eq == null ? null : eq.getLocationId();
            });
        } catch (Exception e) {
            LOG.error("Could not get equipment location for {}", equipmentId, e);
        }

        return eqLocation;
    }

    @RequestMapping(value="/metric", method=RequestMethod.POST)
    public GenericResponse publishMetric(@RequestBody ServiceMetric metricRecord) {

        LOG.debug("calling publishMetric {}", metricRecord);
        
        long ts = System.currentTimeMillis();
        if (metricRecord.getCreatedTimestamp() == 0) {
            metricRecord.setCreatedTimestamp(ts);
        }
        
        populateLocationIdIfNeeded(metricRecord);

        metricStream.publish(metricRecord);
        serviceMetricInterface.create(metricRecord);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishMetric done");

        return ret;
    }
    
    @RequestMapping(value="/service_metrics_collection_config", method=RequestMethod.POST)
    public GenericResponse publishMetrics(@RequestBody List<ServiceMetric> metricList) {

        LOG.debug("calling publishMetrics {}", metricList);

        if(metricList==null || metricList.isEmpty()) {
            return new GenericResponse(true, "empty service_metrics_collection_config");
        }
        
        AtomicLong ts = new AtomicLong(System.currentTimeMillis());
        metricList.forEach(m -> {
            if (m.getCreatedTimestamp() == 0) {
                m.setCreatedTimestamp(ts.incrementAndGet());
            }
            
            populateLocationIdIfNeeded(m);
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

        populateLocationIdIfNeeded(systemEventRecord);

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
            
            populateLocationIdIfNeeded(m);
        });

        systemEventStream.publish(systemEventRecords);
        systemEventInterface.create(systemEventRecords);
        
        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("publishEvents done");

        return ret;
    }

    private void populateLocationIdIfNeeded(SystemEventRecord ser) {
        if(ser.getEquipmentId()!=0 && ser.getLocationId() == 0) {
            //caller did not have location id, extract it from the equipment record, if any
            Long eqLocation = getEquipmentLocation(ser.getEquipmentId());
            if(eqLocation!=null) {
                ser.setLocationId(eqLocation);
            }
        }
    }
    
    private void populateLocationIdIfNeeded(ServiceMetric sm) {
        if(sm.getEquipmentId()!=0 && sm.getLocationId() == 0) {
            //caller did not have location id, extract it from the equipment record, if any
            Long eqLocation = getEquipmentLocation(sm.getEquipmentId());
            if(eqLocation!=null) {
                sm.setLocationId(eqLocation);
            }
        }
    }

}
