package com.telecominfraproject.wlan.equipment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SourceSelectionValue;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.datastore.EquipmentDatastore;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.CellSizeAttributes;
import com.telecominfraproject.wlan.equipment.models.ChannelPowerLevel;
import com.telecominfraproject.wlan.equipment.models.CustomerEquipmentCounts;
import com.telecominfraproject.wlan.equipment.models.ElementRadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentCellSizeAttributesUpdateRequest;
import com.telecominfraproject.wlan.equipment.models.EquipmentChannelsUpdateRequest;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.equipment.models.RadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.SourceSelectionManagement;
import com.telecominfraproject.wlan.equipment.models.SourceSelectionMulticast;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentAddedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentApImpactingChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentBlinkLEDsEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentCellSizeAttributesChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentChannelsChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentCustomerChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentRemovedEvent;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/equipment")
public class EquipmentController {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentController.class);

    public static class ListOfEquipments extends ArrayList<Equipment> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private EquipmentDatastore equipmentDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    /**
     * Creates new Equipment.
     *  
     * @param Equipment
     * @return stored Equipment object
     * @throws RuntimeException if Equipment record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Equipment create(@RequestBody Equipment equipment ) {

        LOG.debug("Creating Equipment {}", equipment);

        if (BaseJsonModel.hasUnsupportedValue(equipment)) {
            LOG.error("Failed to create Equipment, request contains unsupported value: {}", equipment);
            throw new DsDataValidationException("Equipment contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (equipment.getCreatedTimestamp() == 0) {
        	equipment.setCreatedTimestamp(ts);
        }
        equipment.setLastModifiedTimestamp(ts);

        Equipment ret = equipmentDatastore.create(equipment);

        LOG.debug("Created Equipment {}", ret);

        EquipmentAddedEvent event = new EquipmentAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Equipment by id
     * @param equipmentId
     * @return Equipment for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Equipment get(@RequestParam long equipmentId ) {
        
        LOG.debug("Retrieving Equipment {}", equipmentId);
        
        Equipment ret = equipmentDatastore.get(equipmentId);

        LOG.debug("Retrieved Equipment {}", ret);

        return ret;
    }

    /**
     * Retrieves Equipment by id
     * @param equipmentId
     * @return Equipment for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Equipment getOrNull(@RequestParam long equipmentId ) {
        
        LOG.debug("Retrieving Equipment {}", equipmentId);
        
        Equipment ret = equipmentDatastore.getOrNull(equipmentId);

        LOG.debug("Retrieved Equipment {}", ret);

        return ret;
    }

    /**
     * Retrieves Equipment by inventory id
     * @param inventory Id
     * @return Equipment for the supplied id
     */
    @RequestMapping(value = "/byInventoryIdOrNull", method=RequestMethod.GET)
    public Equipment getByInventoryIdOrNull(@RequestParam String inventoryId ) {
        
        LOG.debug("Retrieving Equipment {}", inventoryId);
        
        Equipment ret = equipmentDatastore.getByInventoryIdOrNull(inventoryId);

        LOG.debug("Retrieved Equipment {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfEquipments getAllInSet(@RequestParam Set<Long> equipmentIdSet) {
        LOG.debug("getAllInSet({})", equipmentIdSet);
        try {
            List<Equipment> result = equipmentDatastore.get(equipmentIdSet);
            LOG.debug("getAllInSet({}) return {} entries", equipmentIdSet, result.size());
            ListOfEquipments ret = new ListOfEquipments();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", equipmentIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Equipment> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Equipments for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Equipment> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Equipments for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Equipment> onePage = this.equipmentDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Equipments for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }

    @RequestMapping(value = "/forCustomerWithFilter", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomerWithFilter(@RequestParam int customerId,
            @RequestParam(required = false) EquipmentType equipmentType, 
            @RequestParam(required = false) Set<Long> locationIds,
            @RequestParam(required = false) Set<Long> profileIds,
            @RequestParam(required = false) String criteria,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Equipment> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up equipment {} for customer {} locations {} profiles {} criteria {} last returned page number {}", equipmentType,
                customerId, locationIds, profileIds, criteria, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Equipment> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up equipment {} for customer {} locations {} profiles {} criteria {} last returned page number {}",
                    equipmentType, customerId, locationIds, profileIds, criteria, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Equipment> cePage = this.equipmentDatastore
                .getForCustomer(customerId, equipmentType, locationIds, profileIds, criteria, sortBy, paginationContext);
        ret.setContext(cePage.getContext());
        ret.getItems().addAll(cePage.getItems());

        LOG.debug("Retrieved {} equipment {} for customer {} locations {} profiles {} criteria {}", cePage.getItems().size(), equipmentType,
                customerId, locationIds, profileIds, criteria);

        return ret;
    }

    /**
     * Updates Equipment record
     * 
     * @param Equipment
     * @return updated Equipment object
     * @throws RuntimeException if Equipment record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Equipment update(@RequestBody Equipment equipment){
        
        LOG.debug("Updating Equipment {}", equipment);
        
        if (BaseJsonModel.hasUnsupportedValue(equipment)) {
            LOG.error("Failed to update Equipment, request contains unsupported value: {}", equipment);
            throw new DsDataValidationException("Equipment contains unsupported value");
        }
        
        validateChannelNum(equipment);
        
        Equipment existingEquipment = equipmentDatastore.getOrNull(equipment.getId());
        ApElementConfiguration existingApElementConfig = null;
        if (existingEquipment != null && existingEquipment.getDetails() instanceof ApElementConfiguration) {
            existingApElementConfig = (ApElementConfiguration) existingEquipment.getDetails();
        }

        Equipment ret = equipmentDatastore.update(equipment);
        ApElementConfiguration updatedApElementConfig = null;
        if (ret != null && ret.getDetails() instanceof ApElementConfiguration) {
            updatedApElementConfig = (ApElementConfiguration) ret.getDetails();
        }

        LOG.debug("Updated Equipment {}", ret);
        
        EquipmentChangedEvent event;
        if (equipment.getCustomerId() != existingEquipment.getCustomerId()) {
            publishEvent(new EquipmentCustomerChangedEvent(existingEquipment, ret));
        }
        if ((equipment.getProfileId() != existingEquipment.getProfileId()) ||  (existingApElementConfig != null && updatedApElementConfig != null &&
                updatedApElementConfig.needsToBeUpdatedOnDevice(existingApElementConfig))) {
            event = new EquipmentApImpactingChangedEvent(ret);
        } else if (existingApElementConfig != null && existingApElementConfig.isBlinkAllLEDs() != updatedApElementConfig.isBlinkAllLEDs()) {
            LOG.debug("Updated BlinkingLEDs {}", ret);
            event = new EquipmentBlinkLEDsEvent(ret);
        } else {
            event = new EquipmentChangedEvent(ret);
        }
        publishEvent(event);
        
        return ret;
    }
    
	private void validateChannelNum(Equipment equipment) {
		if (equipment.getDetails() instanceof ApElementConfiguration) {
			ApElementConfiguration apElementConfiguration = (ApElementConfiguration) equipment.getDetails();
			if (apElementConfiguration.getRadioMap() != null) {
				for (RadioType radioType : apElementConfiguration.getRadioMap().keySet()) {

					ElementRadioConfiguration elementRadioConfig = apElementConfiguration.getRadioMap().get(radioType);
					Integer channelNum = elementRadioConfig.getChannelNumber();
					Integer manualChannelNum = elementRadioConfig.getManualChannelNumber();
					Integer backupChannelNum = elementRadioConfig.getBackupChannelNumber();
					Integer manualBackupChannelNum = elementRadioConfig.getManualBackupChannelNumber();
					
					List<Integer> allowedChannels = elementRadioConfig.getAllowedChannelsPowerLevels().stream().map(ChannelPowerLevel::getChannelNumber).collect(Collectors.toList());
					
					if (allowedChannels != null && !allowedChannels.isEmpty()) {
						checkAllowedChannels(channelNum, "channelNumber", allowedChannels);
						checkAllowedChannels(backupChannelNum, "backupChannelNumber", allowedChannels);
						checkAllowedChannels(manualChannelNum, "manualChannelNumber", allowedChannels);
						checkAllowedChannels(manualBackupChannelNum, "manualBackupChannelNumber", allowedChannels);
					}
				}
			}
		}
	}
	
	private void checkAllowedChannels(Integer channelNum, String channelType, List<Integer> allowedChannels) {
		if (channelNum != null && !allowedChannels.contains(channelNum)) {
			LOG.error("Failed to update Equipment. The {} ({}) is out of the allowed channels range {}",
					channelType, channelNum, allowedChannels);
			throw new DsDataValidationException("Equipment contains disallowed " + channelType);
		}
	}
	
    /**
     * Updates Equipment Channels
     *
     * @param EquipmentChannelsUpdateRequest
     * @return updated Equipment object
     * @throws RuntimeException if Equipment record does not exist or if it was modified concurrently
     */
    @RequestMapping(value = "/channel", method=RequestMethod.PUT)
    public Equipment updateChannels(@RequestBody EquipmentChannelsUpdateRequest request) {
        
        LOG.debug("updateChannels {}", request);
        if (request == null) {
            return null;
        }
        Equipment existingEquipment = get(request.getEquipmentId());
        
        if (existingEquipment.getDetails() == null) {
            LOG.info("updateChannels: no details on equipment");
            return null;
        }
        Equipment equipmentCopy = existingEquipment.clone();
        
        Map<RadioType, Integer> primaryChannels = request.getPrimaryChannels();
        Map<RadioType, Integer> backupChannels = request.getBackupChannels();
        Map<RadioType, Boolean> autoChannelSelections = request.getAutoChannelSelections();
        
        if (CollectionUtils.isEmpty(autoChannelSelections) ||
                (CollectionUtils.isEmpty(primaryChannels) && CollectionUtils.isEmpty(backupChannels))) {
            LOG.info("updateChannels no update");
            return equipmentCopy;
        }
        
        ApElementConfiguration apElementConfiguration = (ApElementConfiguration) equipmentCopy.getDetails();
        
        for (RadioType radioType : RadioType.validValues()) {
            ElementRadioConfiguration toRadio = apElementConfiguration.getElementRadioConfiguration(radioType);
            if (toRadio == null) {
                continue;
            }
            if (primaryChannels != null && primaryChannels.get(radioType) != null && autoChannelSelections.get(radioType) != null) {
                toRadio.alterActiveChannel(primaryChannels.get(radioType), autoChannelSelections.get(radioType));
            }
            if (backupChannels != null && backupChannels.get(radioType) != null && autoChannelSelections.get(radioType) != null) {
                toRadio.alterActiveBackupChannel(backupChannels.get(radioType), autoChannelSelections.get(radioType));
            }
        }
        
        Equipment ret = equipmentDatastore.update(equipmentCopy);

        EquipmentChannelsChangedEvent event = new EquipmentChannelsChangedEvent(ret, primaryChannels, backupChannels);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Updates Equipment CellSizeAttributes
     *
     * @param EquipmentCellSizeAttributesUpdateRequest
     * @return updated Equipment object
     * @throws RuntimeException if Equipment record does not exist or if it was modified concurrently
     */
    @RequestMapping(value = "/cellSize", method=RequestMethod.PUT)
    public Equipment updateCellSizeAttributes(@RequestBody EquipmentCellSizeAttributesUpdateRequest request) {
        
        LOG.debug("updateCellSizeAttributes {}", request);
        if (request == null) {
            return null;
        }
        Equipment existingEquipment = get(request.getEquipmentId());
        
        if (existingEquipment.getDetails() == null) {
            LOG.info("updateCellSizeAttributes: no details on equipment");
            return null;
        }
        Equipment equipmentCopy = existingEquipment.clone();
        
        Map<RadioType, CellSizeAttributes> cellSizeAttributesMap = request.getCellSizeAttributesMap();
        Map<RadioType, Boolean> autoCellSizeSelections = request.getAutoCellSizeSelections();
        
        if (CollectionUtils.isEmpty(autoCellSizeSelections) ||
                CollectionUtils.isEmpty(cellSizeAttributesMap)) {
            LOG.info("updateCellSizeAttributes no update");
            return equipmentCopy;
        }
        
        ApElementConfiguration apElementConfiguration = (ApElementConfiguration) equipmentCopy.getDetails();
        
        for (RadioType radioType : RadioType.validValues()) {
            ElementRadioConfiguration elementRadioConfig = apElementConfiguration.getElementRadioConfiguration(radioType);
            RadioConfiguration radioConfig = apElementConfiguration.getAdvancedRadioMap().get(radioType);
            if (elementRadioConfig == null || radioConfig == null) {
                continue;
            }
            CellSizeAttributes cellSizeAttributes = cellSizeAttributesMap.get(radioType);
            if (cellSizeAttributes != null && autoCellSizeSelections.get(radioType) != null) {
                if (autoCellSizeSelections.get(radioType)) {
                    elementRadioConfig.setRxCellSizeDb(SourceSelectionValue.createAutomaticInstance(
                            cellSizeAttributes.getRxCellSizeDb()));
                    elementRadioConfig.setProbeResponseThresholdDb(SourceSelectionValue.createAutomaticInstance(
                            cellSizeAttributes.getProbeResponseThresholdDb()));
                    elementRadioConfig.setClientDisconnectThresholdDb(SourceSelectionValue.createAutomaticInstance(
                            cellSizeAttributes.getClientDisconnectThresholdDb()));
                    elementRadioConfig.setEirpTxPower(SourceSelectionValue.createAutomaticInstance(
                            cellSizeAttributes.getEirpTxPowerDb()));
                    radioConfig.setMulticastRate(SourceSelectionMulticast.createAutomaticInstance(
                            cellSizeAttributes.getMulticastRate()));
                    radioConfig.setManagementRate(SourceSelectionManagement.createAutomaticInstance(
                            cellSizeAttributes.getManagementRate()));
                } else {
                    throw new ConfigurationException("Unable to update Cell Size Attributes on equipment " + request.getEquipmentId()
                    + " for radioType " + radioType + " because its autoCellSizeSelection is disabled");
                }
            }
        }
        
        Equipment ret = equipmentDatastore.update(equipmentCopy);

        EquipmentCellSizeAttributesChangedEvent event = new EquipmentCellSizeAttributesChangedEvent(ret, cellSizeAttributesMap);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Equipment record
     * 
     * @param equipmentId
     * @return deleted Equipment object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Equipment delete(@RequestParam long equipmentId ) {
        
        LOG.debug("Deleting Equipment {}", equipmentId);
        
        Equipment ret = equipmentDatastore.delete(equipmentId);

        LOG.debug("Deleted Equipment {}", ret);
        
        EquipmentRemovedEvent event = new EquipmentRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    @RequestMapping(value = "/defaultDetails", method=RequestMethod.GET)
    public EquipmentDetails getDefaultEquipmentDetails(@RequestParam EquipmentType equipmentType) {
    	ApElementConfiguration apElementConfiguration = ApElementConfiguration.createWithDefaults();
    	return apElementConfiguration;
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

    private void publishEvents(List<SystemEvent> events) {
        try {
            cloudEventDispatcher.publishEventsBulk(events);
        } catch (Exception e) {
            LOG.error("Failed to publish {} events ", events.size(), e);
        }
    }

    @RequestMapping(value = "/equipmentIdsByLocationIds", method = RequestMethod.GET)
	public PaginationResponse<PairLongLong> getEquipmentIdsByLocationIds(@RequestParam Set<Long> locationIds,
            @RequestParam(required = false) PaginationContext<PairLongLong> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}
    	
        LOG.debug("Looking up Equipment ids for locations {} with last returned page number {}", 
                locationIds, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<PairLongLong> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Equipment ids for locations {} with last returned page number {}",
                    locationIds, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<PairLongLong> onePage = this.equipmentDatastore.getEquipmentIdsByLocationIds(locationIds, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Equipment ids for locations {} ", onePage.getItems().size(), locationIds);

        return ret;
        
    }

    @RequestMapping(value = "/equipmentIdsByProfileIds", method = RequestMethod.GET)
	public PaginationResponse<PairLongLong> getEquipmentIdsByProfileIds(@RequestParam Set<Long> profileIds,
            @RequestParam(required = false) PaginationContext<PairLongLong> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}
    	
        LOG.debug("Looking up Equipment ids for profiles {} with last returned page number {}", 
                profileIds, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<PairLongLong> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Equipment ids for profiles {} with last returned page number {}",
                    profileIds, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<PairLongLong> onePage = this.equipmentDatastore.getEquipmentIdsByProfileIds(profileIds, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Equipment ids for profiles {} ", onePage.getItems().size(), profileIds);

        return ret;
        
    }

    @RequestMapping(value = "/rrmBulk", method=RequestMethod.PUT)
	public GenericResponse updateRrmBulk(@RequestBody EquipmentRrmBulkUpdateRequest request) {
        
        LOG.debug("updateRrmBulk {}", request);
        Set<Long> equipmentIds = new HashSet<>();
        request.getItems().forEach(item -> equipmentIds.add(item.getEquipmentId()));
        
        //validate equipment before the bulk update
        List<Equipment> equipmentBeforeUpdate = equipmentDatastore.get(equipmentIds);
        Map<Long, Equipment> eqMap = new HashMap<>();        
        equipmentBeforeUpdate.forEach(eq -> eqMap.put(eq.getId(), eq));
        
        request.getItems().forEach(item -> {
        	Equipment eq = eqMap.get(item.getEquipmentId());
        	if(item.applyToEquipment(eq)) {
        		validateChannelNum(eq);
        	}
        });
        
        equipmentDatastore.updateRrmBulk(request);

        //send events after the bulk update
        List<Equipment> equipmentAfterUpdate = equipmentDatastore.get(equipmentIds);

        List<SystemEvent> events = new ArrayList<>();
        equipmentAfterUpdate.forEach(eq -> events.add(new EquipmentChangedEvent(eq)));
        publishEvents(events);

        return new GenericResponse(true, "");
    }

    @GetMapping("/countsForCustomer")
	public CustomerEquipmentCounts getEquipmentCounts(@RequestParam int customerId) {
		CustomerEquipmentCounts ret = equipmentDatastore.getEquipmentCounts(customerId);
        LOG.debug("getEquipmentCounts({})  {}", customerId, ret);
		return ret;
	}

}
