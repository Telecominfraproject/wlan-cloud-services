package com.telecominfraproject.wlan.alarm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.alarm.datastore.AlarmDatastore;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.events.AlarmAddedEvent;
import com.telecominfraproject.wlan.alarm.models.events.AlarmChangedEvent;
import com.telecominfraproject.wlan.alarm.models.events.AlarmRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/alarm")
public class AlarmController {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmController.class);

    public static class ListOfAlarms extends ArrayList<Alarm> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private AlarmDatastore alarmDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new Alarm.
     *  
     * @param Alarm
     * @return stored Alarm object
     * @throws RuntimeException if Alarm record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Alarm create(@RequestBody Alarm alarm ) {

        LOG.debug("Creating Alarm {}", alarm);

        if (BaseJsonModel.hasUnsupportedValue(alarm)) {
            LOG.error("Failed to create Alarm, request contains unsupported value: {}", alarm);
            throw new DsDataValidationException("Alarm contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (alarm.getCreatedTimestamp() == 0) {
        	alarm.setCreatedTimestamp(ts);
        }
        alarm.setLastModifiedTimestamp(ts);

        Alarm ret = alarmDatastore.create(alarm);

        LOG.debug("Created Alarm {}", ret);

        AlarmAddedEvent event = new AlarmAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Alarm by id
     * @param alarmId
     * @return Alarm for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Alarm get(@RequestParam long alarmId ) {
        
        LOG.debug("Retrieving Alarm {}", alarmId);
        
        Alarm ret = alarmDatastore.get(alarmId);

        LOG.debug("Retrieved Alarm {}", ret);

        return ret;
    }

    /**
     * Retrieves Alarm by id
     * @param alarmId
     * @return Alarm for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Alarm getOrNull(@RequestParam long alarmId ) {
        
        LOG.debug("Retrieving Alarm {}", alarmId);
        
        Alarm ret = alarmDatastore.getOrNull(alarmId);

        LOG.debug("Retrieved Alarm {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfAlarms getAllInSet(@RequestParam Set<Long> alarmIdSet) {
        LOG.debug("getAllInSet({})", alarmIdSet);
        try {
            List<Alarm> result = alarmDatastore.get(alarmIdSet);
            LOG.debug("getAllInSet({}) return {} entries", alarmIdSet, result.size());
            ListOfAlarms ret = new ListOfAlarms();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", alarmIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Alarm> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<Alarm> paginationContext) {

        LOG.debug("Looking up Alarms for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Alarm> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Alarms for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Alarm> onePage = this.alarmDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Alarms for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Alarm record
     * 
     * @param Alarm
     * @return updated Alarm object
     * @throws RuntimeException if Alarm record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Alarm update(@RequestBody Alarm alarm){
        
        LOG.debug("Updating Alarm {}", alarm);
        
        if (BaseJsonModel.hasUnsupportedValue(alarm)) {
            LOG.error("Failed to update Alarm, request contains unsupported value: {}", alarm);
            throw new DsDataValidationException("Alarm contains unsupported value");
        }

        Alarm ret = alarmDatastore.update(alarm);

        LOG.debug("Updated Alarm {}", ret);

        AlarmChangedEvent event = new AlarmChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Alarm record
     * 
     * @param alarmId
     * @return deleted Alarm object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Alarm delete(@RequestParam long alarmId ) {
        
        LOG.debug("Deleting Alarm {}", alarmId);
        
        Alarm ret = alarmDatastore.delete(alarmId);

        LOG.debug("Deleted Alarm {}", ret);
        
        AlarmRemovedEvent event = new AlarmRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        
        try {
            cloudEventDispatcher.publishEvent(event);
        } catch (Exception e) {
            LOG.error("Failed to publish event : {}", event, e);
        }
    }

    
}
