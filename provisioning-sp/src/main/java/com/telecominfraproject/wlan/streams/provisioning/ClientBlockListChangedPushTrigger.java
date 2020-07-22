package com.telecominfraproject.wlan.streams.provisioning;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.client.ClientServiceInterface;
import com.telecominfraproject.wlan.client.models.events.ClientBlockListChangedEvent;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWClientBlocklistChangeNotification;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.stream.StreamProcessor;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop 
 * 			This stream processor is listening for ClientBlockListChangedEvents. If a change is detected,
 *         it uses Routing service to find all equipment of type AP and delivers
 *         CEGWClientBlocklistChangeNotification command to the equipment, which results
 *         in the push of updated blacklist to the AP.
 */
@Component
public class ClientBlockListChangedPushTrigger extends StreamProcessor {

	    private static final Logger LOG = LoggerFactory.getLogger(ClientBlockListChangedPushTrigger.class);
	    
        @Value("${tip.wlan.systemEventsTopic:system_events}")
    	private String systemEventsTopic;
        
        @Autowired
        private EquipmentGatewayServiceInterface equipmentGatewayInterface;
        @Autowired
        private EquipmentServiceInterface equipmentServiceInterface;

        @Autowired
        private ClientServiceInterface clientServiceInterface;

	    @Override
	    protected boolean acceptMessage(QueuedStreamMessage message) {
		    boolean ret = message.getTopic().equals(systemEventsTopic);

		    if(ret && ( message.getModel() instanceof SystemEventRecord) ) {
		    
			    SystemEventRecord ser = (SystemEventRecord) message.getModel(); 
			    ret = ret &&
			    		(
			    			ser.getDetails() instanceof ClientBlockListChangedEvent		    			
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
	    	case "ClientBlockListChangedEvent":
	    		process(mdl.getCustomerId(), (ClientBlockListChangedEvent) se);
	    		break;
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(int customerId, ClientBlockListChangedEvent model) {
			LOG.debug("Processing ClientBlockListChangedEvent");
			
			//get the list of blocked clients - do it once
			List<MacAddress> blockedMacs = new ArrayList<>();
			clientServiceInterface.getBlockedClients(customerId).forEach(c -> blockedMacs.add(c.getMacAddress()));
			
			//go through all equipmentIds for the customer and trigger CEGWClientBlocklistChangeNotification on them
			PaginationContext<Equipment> context = new PaginationContext<>(100);
			
			while(!context.isLastPage()) {
				PaginationResponse<Equipment> page = equipmentServiceInterface.getForCustomer(customerId, null, context);
				context = page.getContext();
				
				List<Equipment> equipmentForPage = page.getItems();
				
				List<CEGWBaseCommand> commands = new ArrayList<>(equipmentForPage.size());
				equipmentForPage.forEach(eq -> commands.add(new CEGWClientBlocklistChangeNotification(eq.getInventoryId(), eq.getId(), blockedMacs)));
			
				equipmentGatewayInterface.sendCommands(commands);
				LOG.debug("Page {} - sent {} commands to equipment gateway", context.getLastReturnedPageNumber(), commands.size());
			}		
			
			LOG.debug("Finished processing ClientBlockListChangedEvent {}", model);

		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    
}
