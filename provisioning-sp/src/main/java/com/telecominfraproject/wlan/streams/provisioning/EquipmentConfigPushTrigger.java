package com.telecominfraproject.wlan.streams.provisioning;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentChangedEvent;
import com.telecominfraproject.wlan.location.models.events.LocationChangedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileAddedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileChangedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileRemovedEvent;
import com.telecominfraproject.wlan.routing.RoutingServiceInterface;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
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
        private RoutingServiceInterface routingServiceInterface;

	    @Override
	    protected boolean acceptMessage(QueuedStreamMessage message) {
		    boolean ret = message.getTopic().equals(systemEventsTopic);

		    if(ret && ( message.getModel() instanceof SystemEventRecord) ) {
		    
			    SystemEventRecord ser = (SystemEventRecord) message.getModel(); 
			    ret = ret &&
			    		(
			    			ser.getDetails() instanceof EquipmentChangedEvent ||
			    			ser.getDetails() instanceof ProfileAddedEvent ||
			    			ser.getDetails() instanceof ProfileChangedEvent ||
			    			ser.getDetails() instanceof ProfileRemovedEvent ||
			    			ser.getDetails() instanceof LocationChangedEvent		    			
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
	    	case "ProfileAddedEvent":
	    		process((ProfileAddedEvent) se);
	    		break;
	    	case "ProfileChangedEvent":
	    		process((ProfileChangedEvent) se);
	    		break;
	    	case "ProfileRemovedEvent":
	    		process((ProfileRemovedEvent) se);
	    		break;
	    	case "LocationChangedEvent":
	    		process((LocationChangedEvent) se);
	    		break;	    	
	    	default:
	    		process(mdl);
	    	} 
	    	
	    }

		private void process(EquipmentChangedEvent model) {
			LOG.debug("Processing EquipmentChangedEvent");
			//get the affected equipmentId
			long eqId = model.getEquipmentId();
			//find gateways for equipment 
			List<EquipmentGatewayRecord> gateways = routingServiceInterface.getRegisteredGatewayRecordList(eqId);
			gateways.forEach(gw -> { gw.getIpAddr(); gw.getPort(); });
			// TODO: for each route - send the ConfigChangedCommand
		}

		private void process(ProfileAddedEvent model) {
			LOG.debug("Processing ProfileAddedEvent");
			//TODO: implement this
		}

		private void process(ProfileChangedEvent model) {
			LOG.debug("Processing ProfileChangedEvent");
			//TODO: implement this
		}

		private void process(ProfileRemovedEvent model) {
			LOG.debug("Processing ProfileRemovedEvent");
			//TODO: implement this
		}

		private void process(LocationChangedEvent model) {
			LOG.debug("Processing LocationChangedEvent");
			//TODO: implement this
		}

		private void process(BaseJsonModel model) {
			LOG.warn("Unprocessed model: {}", model);
		}
	    
	    
}
