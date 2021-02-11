package com.telecominfraproject.wlan.streams.provisioning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentRemovedEvent;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCloseSessionRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWConfigChangeNotification;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.location.models.events.LocationChangedApImpactingEvent;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.events.ProfileAddedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileChangedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileRemovedEvent;
import com.telecominfraproject.wlan.stream.StreamProcessor;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop 
 * 			This stream processor is listening for events related to changes
 *         in Equipment, Location, and Profile objects. If a change is detected,
 *         it uses Routing service to find affected equipment and delivers
 *         CEGWConfigChangeNotification command to the equipment, which results
 *         in the config push.
 */
@Component
public class EquipmentConfigPushTrigger extends StreamProcessor {

	    private static final Logger LOG = LoggerFactory.getLogger(EquipmentConfigPushTrigger.class);
	    
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;
        
        @Autowired
        private EquipmentGatewayServiceInterface equipmentGatewayInterface;
        @Autowired
        private ProfileServiceInterface profileServiceInterface;
        @Autowired
        private EquipmentServiceInterface equipmentServiceInterface;

	    @Override
	    protected boolean acceptMessage(QueuedStreamMessage message) {
		    boolean ret = message.getTopic().equals(systemEventsTopic);

		    if(ret && ( message.getModel() instanceof SystemEventRecord) ) {
		    
			    SystemEventRecord ser = (SystemEventRecord) message.getModel(); 
			    ret = ret &&
			    		(
			    			ser.getDetails() instanceof EquipmentChangedEvent ||
			    			ser.getDetails() instanceof EquipmentRemovedEvent ||
			    			ser.getDetails() instanceof ProfileAddedEvent ||
			    			ser.getDetails() instanceof ProfileChangedEvent ||
			    			ser.getDetails() instanceof ProfileRemovedEvent ||
			    			ser.getDetails() instanceof LocationChangedApImpactingEvent
			    		);
		    } else {
		    	ret = false;
		    }
		    
		    LOG.trace("acceptMessage {}", ret);
		    
		    return ret;
	    }
	    
	    @Override
	    protected void processMessage(QueuedStreamMessage message) {
	    	SystemEventRecord mdl = (SystemEventRecord) message.getModel();
	    	SystemEvent se = mdl.getDetails();
	    	LOG.debug("Processing {}", mdl);
	    	
	    	switch ( se.getClass().getSimpleName() ) {
	    	case "EquipmentChangedEvent":
	    		process((EquipmentChangedEvent) se);
	    		break;
	    	case "EquipmentRemovedEvent":
                process((EquipmentRemovedEvent) se);
                break;
	    	case "ProfileAddedEvent":
	    		process((ProfileAddedEvent) se);
	    		break;
	    	case "ProfileChangedEvent":
	    		process((ProfileChangedEvent) se);
	    		break;
	    	case "ProfileRemovedEvent":
	    		process((ProfileRemovedEvent) se);
	    		break;
	    	case "LocationChangedApImpactingEvent":
	    		process((LocationChangedApImpactingEvent) se);
	    		break;	    	
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(EquipmentChangedEvent model) {
			LOG.debug("Processing EquipmentChangedEvent");
			equipmentGatewayInterface.sendCommand(new CEGWConfigChangeNotification(model.getPayload().getInventoryId(), model.getEquipmentId()));
		}
		
        private void process(EquipmentRemovedEvent model) {
            LOG.debug("Processing EquipmentRemovedEvent");
            equipmentGatewayInterface.sendCommand(new CEGWCloseSessionRequest(model.getPayload().getInventoryId(), model.getEquipmentId()));
        }
        
		private void process(ProfileAddedEvent model) {
			LOG.debug("Processing ProfileAddedEvent {}", model.getPayload().getId());
			processProfile(model.getPayload());
		}

		private void process(ProfileChangedEvent model) {
			LOG.debug("Processing ProfileChangedEvent {}", model.getPayload().getId());
			processProfile(model.getPayload());
		}

		private void process(ProfileRemovedEvent model) {
			LOG.debug("Processing ProfileRemovedEvent {}", model.getPayload().getId());
			processProfile(model.getPayload());
		}


		private void processProfile(Profile profile) {
			
			List<PairLongLong> ret = profileServiceInterface.getTopLevelProfiles(new HashSet<>(Arrays.asList(profile.getId())));
			if(ret == null || ret.isEmpty()) {
				//nothing to do here
				return;
			}
			
			Set<Long> parentProfileIds = new HashSet<>();
			ret.forEach(p -> parentProfileIds.add(p.getValue2()));
			
			//go through all equipmentIds that refer to parent profiles and trigger change config notification on them
			PaginationContext<PairLongLong> context = new PaginationContext<>(100);
			
			while(!context.isLastPage()) {
				PaginationResponse<PairLongLong> page = equipmentServiceInterface.getEquipmentIdsByProfileIds(parentProfileIds, context );
				context = page.getContext();
				
				Set<Long> equipmentIds = new HashSet<>();
				page.getItems().forEach(p -> equipmentIds.add(p.getValue2()));
				
				//retrieve full equipment objects to get the inventory id 
				List<Equipment> equipmentForPage = equipmentServiceInterface.get(equipmentIds);
				
				List<CEGWBaseCommand> commands = new ArrayList<>(equipmentForPage.size());
				equipmentForPage.forEach(eq -> commands.add(new CEGWConfigChangeNotification(eq.getInventoryId(), eq.getId())));
			
				equipmentGatewayInterface.sendCommands(commands);
				LOG.debug("Page {} - sent {} commands to equipment gateway", context.getLastReturnedPageNumber(), commands.size());
			}			
			
			LOG.debug("Finished processing profile {}", profile.getId());
		}
		
		private void process(LocationChangedApImpactingEvent model) {
			LOG.debug("Processing LocationChangedApImpactingEvent {}", model.getPayload().getId());
			
			Set<Long> locationIds = new HashSet<>(Arrays.asList(model.getPayload().getId()));
			
			//go through all equipmentIds that reside in the specified location and trigger change config notification on them
			PaginationContext<PairLongLong> context = new PaginationContext<>(100);
			
			while(!context.isLastPage()) {
				PaginationResponse<PairLongLong> page = equipmentServiceInterface.getEquipmentIdsByLocationIds(locationIds, context );
				context = page.getContext();
				
				Set<Long> equipmentIds = new HashSet<>();
				page.getItems().forEach(p -> equipmentIds.add(p.getValue2()));
				
				//retrieve full equipment objects to get the inventory id 
				List<Equipment> equipmentForPage = equipmentServiceInterface.get(equipmentIds);
				
				List<CEGWBaseCommand> commands = new ArrayList<>(equipmentForPage.size());
				equipmentForPage.forEach(eq -> commands.add(new CEGWConfigChangeNotification(eq.getInventoryId(), eq.getId())));
			
				equipmentGatewayInterface.sendCommands(commands);
				LOG.debug("Page {} - sent {} commands to equipment gateway", context.getLastReturnedPageNumber(), commands.size());
			}		
			
			LOG.debug("Finished processing LocationChangedApImpactingEvent {}", model.getPayload().getId());

		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    
}
