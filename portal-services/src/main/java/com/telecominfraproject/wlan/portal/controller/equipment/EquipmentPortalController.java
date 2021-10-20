package com.telecominfraproject.wlan.portal.controller.equipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.ChannelPowerLevel;
import com.telecominfraproject.wlan.equipment.models.ElementRadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentCellSizeAttributesUpdateRequest;
import com.telecominfraproject.wlan.equipment.models.EquipmentChannelsUpdateRequest;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.equipment.models.RadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;
import com.telecominfraproject.wlan.location.service.LocationServiceInterface;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.status.StatusServiceInterface;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class EquipmentPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentPortalController.class);
    
    public static enum CHANNEL_NUMBER_TYPE {channelNumber, backupChannelNumber, manualChannelNumber,
        manualBackupChannelNumber};

    @Value("${tip.wlan.portal.equipment.numRetryUpdate:10}") 
    private int numRetryUpdate;
    
    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfEquipment extends ArrayList<Equipment> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @Autowired
    private AlarmServiceInterface alarmServiceInterface;

    @Autowired
    private StatusServiceInterface statusServiceInterface;
    
    @Autowired
    private ProfileServiceInterface profileServiceInterface;
    
    @Autowired
    private LocationServiceInterface locationServiceInterface;

    @RequestMapping(value = "/equipment", method = RequestMethod.GET)
    public Equipment getEquipment(@RequestParam long equipmentId) {
        LOG.debug("Getting equipment {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);

        return equipment;
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.PUT)
    public Equipment updateEquipment(@RequestBody Equipment equipment) {
        LOG.debug("Updating equipment {}", equipment.getId());

        Equipment ret = null;
        Equipment existing  = equipmentServiceInterface.getOrNull(equipment.getId());

        for(int i=0; i<numRetryUpdate; i++) {
	        try {
	            if (equipment != null && existing != null && equipment.getLocationId() != existing.getLocationId()) {
	                Location location = locationServiceInterface.get(equipment.getLocationId());
	                Location existingLocation = locationServiceInterface.get(existing.getLocationId());
	                
	                if (!Objects.equals(Location.getCountryCode(location), Location.getCountryCode(existingLocation))) {
	                    updateEquipmentWithDefaultChannels(equipment);
	                }
	            }
	            validateChannelNum(equipment);
	            
	            ret = equipmentServiceInterface.update(equipment);
	            break;
	        } catch (DsConcurrentModificationException e) {
	            LOG.debug("Equipment was concurrently updated, retrying: {}", e.getMessage());
	            existing  = equipmentServiceInterface.getOrNull(equipment.getId());
	            equipment.setLastModifiedTimestamp(existing.getLastModifiedTimestamp());
	        }
        }

        if(ret == null) {
        	throw new DsConcurrentModificationException("Could not update equipment after " + numRetryUpdate + " retries");
        }
        
        return ret;
    }
    
    private void updateEquipmentWithDefaultChannels(Equipment equipment) {
        if (equipment.getDetails() instanceof ApElementConfiguration) {
            ApElementConfiguration apElementConfiguration = (ApElementConfiguration) equipment.getDetails();
            if (apElementConfiguration.getRadioMap() != null) {
                
                for (RadioType radioType : apElementConfiguration.getRadioMap().keySet()) {
                    ElementRadioConfiguration elementRadioConfig = apElementConfiguration.getRadioMap().get(radioType);

                    elementRadioConfig.setChannelNumber(ElementRadioConfiguration.getDefaultChannelNumber(radioType));
                    elementRadioConfig.setBackupChannelNumber(ElementRadioConfiguration.getDefaultBackupChannelNumber(radioType));
                    elementRadioConfig.setManualChannelNumber(ElementRadioConfiguration.getDefaultChannelNumber(radioType));
                    elementRadioConfig.setManualBackupChannelNumber(ElementRadioConfiguration.getDefaultBackupChannelNumber(radioType));
                }
            }
        }
    }
    
    private void validateChannelNum(Equipment equipment) {
        if (equipment != null && equipment.getDetails() instanceof ApElementConfiguration) {
            ApElementConfiguration apElementConfiguration = (ApElementConfiguration) equipment.getDetails();
            if (apElementConfiguration.getRadioMap() != null) {
                
                for (RadioType radioType : apElementConfiguration.getRadioMap().keySet()) {
                    ElementRadioConfiguration elementRadioConfig = apElementConfiguration.getRadioMap().get(radioType);
                    List<Integer> allowedChannels = elementRadioConfig.getAllowedChannelsPowerLevels().stream().map(
                            ChannelPowerLevel::getChannelNumber).collect(Collectors.toList());
                    
                    if (allowedChannels != null && !allowedChannels.isEmpty()) {
                        for (CHANNEL_NUMBER_TYPE channelType : CHANNEL_NUMBER_TYPE.values()) {
                            checkAllowedChannels(elementRadioConfig, channelType, allowedChannels, radioType);
                        }
                    }
                }
            }
        }
    }
    
    private void checkAllowedChannels(ElementRadioConfiguration elementRadioConfig, CHANNEL_NUMBER_TYPE channelType,
            List<Integer> allowedChannels, RadioType radioType) {
        Integer channelNum = null;
        switch (channelType) {
        case channelNumber: {
            channelNum = elementRadioConfig.getChannelNumber();
            break;
        }
        case backupChannelNumber: {
            channelNum = elementRadioConfig.getBackupChannelNumber();
            break;
        }
        case manualChannelNumber: {
            channelNum = elementRadioConfig.getManualChannelNumber();
            break;
        }
        case manualBackupChannelNumber: {
            channelNum = elementRadioConfig.getManualBackupChannelNumber();
            break;
        }
        default:
            break;
        }
        if (channelNum != null && !allowedChannels.contains(channelNum)) {
            LOG.error("Failed to update Equipment. The {} ({}) is out of the allowed channels range {} for radioType {}",
                    channelType, channelNum, allowedChannels, radioType);
            throw new DsDataValidationException("Equipment contains disallowed " + channelType);
        }
    }
    
    @RequestMapping(value = "/equipment/channel", method = RequestMethod.PUT)
    public Equipment updateEquipmentChannels(@RequestBody EquipmentChannelsUpdateRequest request) {
        LOG.debug("updateEquipmentChannels {}", request);
        return equipmentServiceInterface.updateChannels(request);
    }
    
    @RequestMapping(value = "/equipment/cellSize", method = RequestMethod.PUT)
    public Equipment updateEquipmentCellSizeAttributes(@RequestBody EquipmentCellSizeAttributesUpdateRequest request) {
        LOG.debug("updateEquipmentCellSizeAttributes {}", request);
        return equipmentServiceInterface.updateCellSizeAttributes(request);
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.POST)
    public Equipment createEquipment(@RequestBody Equipment equipment) {
        LOG.debug("Creating equipment {}", equipment.getId());

        if(equipment.getEquipmentType() == EquipmentType.AP) {
            if(equipment.getDetails() == null ) {
                equipment.setDetails(ApElementConfiguration.createWithDefaults());
            }
            
            ApElementConfiguration apElementConfig = (ApElementConfiguration) equipment.getDetails();
            apElementConfig.setDeviceName(equipment.getName());
            //Equipment Model will be populated by the OSGW when AP connects
            //apElementConfig.setEquipmentModel(connectNodeInfo.model);
            
            Map<RadioType, RadioConfiguration> advancedRadioMap = apElementConfig.getAdvancedRadioMap();
            Map<RadioType, ElementRadioConfiguration> radioMap = apElementConfig.getRadioMap();
            
            if(advancedRadioMap == null) {
                advancedRadioMap  = new HashMap<>();
                apElementConfig.setAdvancedRadioMap(advancedRadioMap);
            }
            
            if(radioMap == null) {
                radioMap = new HashMap<>();
                apElementConfig.setRadioMap(radioMap);
            }

            //We will initialize all radios, and initially set them to disabled state
            //Non-applicable radios will be removed by the OSGW when the AP connects
            //@see com.telecominfraproject.wlan.opensync.external.integration.OpensyncExternalIntegrationCloud.apConnected(String, ConnectNodeInfo)
            //
            for (RadioType radioType: RadioType.validValues()) {
                RadioConfiguration advancedRadioConfiguration = advancedRadioMap.get(radioType);
                ElementRadioConfiguration radioConfiguration = radioMap.get(radioType);
                
               if(advancedRadioConfiguration == null) {
                   advancedRadioConfiguration = RadioConfiguration.createWithDefaults(radioType);
                   advancedRadioMap.put(radioType, advancedRadioConfiguration);
               }
               
               if(radioConfiguration == null) {
                   radioConfiguration = ElementRadioConfiguration.createWithDefaults(radioType);
                   radioMap.put(radioType, radioConfiguration);                   
               }
               
               //Disabling radio by default may be too much 
               //advancedRadioConfiguration.setRadioAdminState(StateSetting.disabled);
            }

        }
        
        Equipment ret = equipmentServiceInterface.create(equipment);

        return ret;
    }

    @RequestMapping(value = "/equipment", method = RequestMethod.DELETE)
    public Equipment deleteEquipment(@RequestParam long equipmentId) {
        LOG.debug("Deleting equipment {}", equipmentId);

        Equipment ret = equipmentServiceInterface.delete(equipmentId);

        if(ret!= null && ret.getCustomerId() > 0) {
            try {
                alarmServiceInterface.delete(ret.getCustomerId(), equipmentId);
            }catch (RuntimeException e) {
                LOG.error("Cannot delete alarms for equipment {} ", equipmentId);
            }
            
            try {
                statusServiceInterface.delete(ret.getCustomerId(), equipmentId);
            }catch (RuntimeException e) {
                LOG.error("Cannot delete status for equipment {} ", equipmentId);
            }
            
        }

        return ret;
    }
    

    @RequestMapping(value = "/equipment/inSet", method = RequestMethod.GET)
    public ListOfEquipment getAllInSet(@RequestParam Set<Long> equipmentIdSet) {
        LOG.debug("getAllInSet({})", equipmentIdSet);
        try {
            List<Equipment> result = equipmentServiceInterface.get(equipmentIdSet);
            LOG.debug("getAllInSet({}) return {} entries", equipmentIdSet, result.size());
            ListOfEquipment ret = new ListOfEquipment();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", equipmentIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/equipment/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
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

        PaginationResponse<Equipment> onePage = this.equipmentServiceInterface
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Equipments for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    @RequestMapping(value = "/equipment/forCustomerWithFilter", method = RequestMethod.GET)
    public PaginationResponse<Equipment> getForCustomerWithFilter(@RequestParam int customerId,
            @RequestParam(required = false) EquipmentType equipmentType, 
            @RequestParam(required = false) Set<Long> locationIds,
            @RequestParam(required = false) Long profileId,
            @RequestParam(required = false) String criteria,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Equipment> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up equipment {} for customer {} locations {} profileId {} criteria {} last returned page number {}", equipmentType,
                customerId, locationIds, profileId, criteria, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Equipment> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up equipment {} for customer {} locations {} profileId {} criteria {} last returned page number {}",
                    equipmentType, customerId, locationIds, profileId, criteria, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }
        
        Set<Long> topProfileIds = new HashSet<>();
        if (profileId != null) {
            //first get top-level profiles for the supplied set - only top-level profiles are linked to equipment
            List<PairLongLong> topLevelProfiles = profileServiceInterface.getTopLevelProfiles(Collections.singleton(profileId));
            
            //gather top-level profile ids
            topLevelProfiles.forEach(pair -> topProfileIds.add(pair.getValue2()));
        }

        PaginationResponse<Equipment> cePage = this.equipmentServiceInterface
                .getForCustomer(customerId, equipmentType, locationIds, topProfileIds, criteria, sortBy, paginationContext);
        ret.setContext(cePage.getContext());
        ret.getItems().addAll(cePage.getItems());

        LOG.debug("Retrieved {} equipment {} for customer {} locations {} profileId {} criteria {}", cePage.getItems().size(), equipmentType,
                customerId, locationIds, profileId, criteria);

        return ret;
    }    
    
    @RequestMapping(value = "/equipment/defaultDetails", method=RequestMethod.GET)
    public EquipmentDetails getDefaultEquipmentDetails(@RequestParam EquipmentType equipmentType) {
    	return this.equipmentServiceInterface.getDefaultEquipmentDetails(equipmentType);
    }

    @RequestMapping(value = "/equipment/rrmBulk", method=RequestMethod.PUT)
	public GenericResponse updateRrmBulk(@RequestBody EquipmentRrmBulkUpdateRequest request) {
        LOG.debug("updateRrmBulk {}", request);
        
        //validate equipment before the bulk update
        Set<Long> equipmentIds = new HashSet<>();
        request.getItems().forEach(item -> equipmentIds.add(item.getEquipmentId()));
        List<Equipment> equipmentBeforeUpdate = equipmentServiceInterface.get(equipmentIds);
        Map<Long, Equipment> eqMap = new HashMap<>();
        equipmentBeforeUpdate.forEach(eq -> eqMap.put(eq.getId(), eq));
        
        request.getItems().forEach(item -> {
            Equipment eq = eqMap.get(item.getEquipmentId());
            if(item.applyToEquipment(eq)) {
                validateChannelNum(eq);
            }
        });
        equipmentServiceInterface.updateRrmBulk(request);
        return new GenericResponse(true, "");
    }

}
