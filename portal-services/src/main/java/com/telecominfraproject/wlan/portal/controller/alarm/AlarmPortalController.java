package com.telecominfraproject.wlan.portal.controller.alarm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.service.CustomerServiceInterface;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class AlarmPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfAlarms extends ArrayList<Alarm> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private AlarmServiceInterface alarmServiceInterface;

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @Autowired
    private CustomerServiceInterface customerServiceInterface;

    
    @RequestMapping(value = "/alarm/forEquipment", method = RequestMethod.GET)
    public ListOfAlarms getAlarms(@RequestParam int customerId, 
    		@RequestParam Set<Long> equipmentIds,  
    		@RequestParam(required = false) Set<AlarmCode> alarmCodes, 
    		@RequestParam(required = false, defaultValue = "-1") long createdAfterTimestamp) {
        LOG.debug("Getting alarms {} {} {} {}", customerId, equipmentIds, alarmCodes, createdAfterTimestamp);

        ListOfAlarms ret = new ListOfAlarms();
        
        List<Alarm> sList= alarmServiceInterface.get(customerId, equipmentIds, alarmCodes, createdAfterTimestamp);
        ret.addAll(sList);

        return ret;
    }

    @RequestMapping(value = "/alarm/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Alarm> getForCustomer(@RequestParam int customerId,
    		@RequestParam(required = false) Set<Long> equipmentIds,  
    		@RequestParam(required = false) Set<AlarmCode> alarmCodes, 
    		@RequestParam(required = false, defaultValue = "-1") long createdAfterTimestamp,
    		@RequestParam(required = false) Boolean acknowledged,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Alarm> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

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

        PaginationResponse<Alarm> onePage = this.alarmServiceInterface
                .getForCustomer(customerId, equipmentIds, alarmCodes, createdAfterTimestamp, acknowledged, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Alarms for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }

    @RequestMapping(value = "/alarm/counts", method = RequestMethod.GET)
	public AlarmCounts getAlarmCounts(@RequestParam int customerId, 
			@RequestParam(required = false) Set<Long> equipmentIds,
			@RequestParam(required = false) Set<AlarmCode> alarmCodes) {
    	
        LOG.debug("Getting Alarm counts for {} {} {}", customerId, equipmentIds, alarmCodes);

        AlarmCounts ret = this.alarmServiceInterface.getAlarmCounts(customerId, equipmentIds, alarmCodes);
        
        LOG.debug("Alarm counts: {}", ret);
        return ret;
	}

    @RequestMapping(value = "/alarm", method = RequestMethod.PUT)
    public Alarm updateAlarm(@RequestBody Alarm alarm) {
        LOG.debug("Updating alarm {}", alarm);

        Alarm ret = alarmServiceInterface.update(alarm);

        return ret;
    }
    
    @RequestMapping(value = "/alarm", method = RequestMethod.DELETE)
    public Alarm deleteAlarm(@RequestParam int customerId, @RequestParam long equipmentId, @RequestParam AlarmCode alarmCode, @RequestParam long createdTimestamp) {
        LOG.debug("Deleting alarm {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);

        Alarm ret = alarmServiceInterface.delete(customerId, equipmentId, alarmCode, createdTimestamp);

        return ret;
    }

    @RequestMapping(value = "/alarm/resetCounts", method = RequestMethod.POST)
    public GenericResponse resetAlarmCounters() {
        //first remove all alarms for non-existent equipment, if any
        
        LOG.info("Processing obsolete alarms");
        
        Set<Long> alarmEquipmentIds = new HashSet<>();
        Set<Long> existingEquipmentIds =  new HashSet<>();
        
        int batchSize = 100;
        int continueAfterCustomerId = 0;
        
        //go through all the customers
        List<PairIntString> customerBatch = customerServiceInterface.getAll(batchSize , continueAfterCustomerId );
        while(!customerBatch.isEmpty()) {
            
            for(PairIntString item : customerBatch) {
                int customerId = item.getIntVal();
                
                LOG.debug("Processing obsolete alarms for customer {}", customerId);

                //process alarms for one customer
                PaginationContext<Alarm> context = new PaginationContext<Alarm>(100);
                while(!context.isLastPage()) {
                    PaginationResponse<Alarm> alarmPage = alarmServiceInterface.getForCustomer(customerId, null, null, -1, null, null, context );
                    alarmEquipmentIds.clear();
                    alarmPage.getItems().forEach(a -> alarmEquipmentIds.add(a.getEquipmentId()));
        
                    if(!alarmEquipmentIds.isEmpty()) {
                        List<Equipment> existingEquipment = equipmentServiceInterface.get(alarmEquipmentIds);
                        existingEquipmentIds.clear();
                        existingEquipment.forEach(e -> existingEquipmentIds.add(e.getId()));
                        
                        alarmEquipmentIds.removeAll(existingEquipmentIds);
                        if(!alarmEquipmentIds.isEmpty()) {
                            LOG.debug("Removing obsolete alarms for customer {} equipment {} ", customerId, alarmEquipmentIds);
                            alarmEquipmentIds.forEach(eqId -> alarmServiceInterface.delete(customerId, eqId));
                        }
                    }
                    
                    context = alarmPage.getContext();
                }
                
                continueAfterCustomerId = customerId;
            }
            
            customerBatch = customerServiceInterface.getAll(batchSize , continueAfterCustomerId );
        }
        
        LOG.info("Completed processing of obsolete alarms");
        
        //now re-calculate alarm counts 
        return alarmServiceInterface.resetAlarmCounters();
    }

    
}
