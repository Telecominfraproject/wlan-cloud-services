
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

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.client.ClientServiceInterface;
import com.telecominfraproject.wlan.client.session.models.AssociationState;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.core.model.streams.QueuedStreamMessage;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentApImpactingChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentBlinkLEDsEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentCellSizeAttributesChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentChannelsChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentCustomerChangedEvent;
import com.telecominfraproject.wlan.equipment.models.events.EquipmentRemovedEvent;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBlinkRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCloseSessionRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWConfigChangeNotification;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWNewChannelRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCellSizeAttributesRequest;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.location.models.events.LocationChangedApImpactingEvent;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.events.ProfileAddedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileChangedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileRemovedEvent;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.stream.StreamProcessor;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 *         This stream processor is listening for events related to changes
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
    @Autowired
    private StatusServiceInterface statusServiceInterface;
    @Autowired
    private AlarmServiceInterface alarmServiceInterface;
    @Autowired
    private ClientServiceInterface clientServiceInterface;

    @Override
    protected boolean acceptMessage(QueuedStreamMessage message) {
        boolean ret = message.getTopic().equals(systemEventsTopic);

        if (ret && (message.getModel() instanceof SystemEventRecord)) {

            SystemEventRecord ser = (SystemEventRecord) message.getModel();
            ret = ret && (ser.getDetails() instanceof EquipmentApImpactingChangedEvent || ser.getDetails() instanceof EquipmentBlinkLEDsEvent
                    || ser.getDetails() instanceof EquipmentChannelsChangedEvent || ser.getDetails() instanceof EquipmentCellSizeAttributesChangedEvent
                    || ser.getDetails() instanceof EquipmentRemovedEvent || ser.getDetails() instanceof ProfileAddedEvent
                    || ser.getDetails() instanceof ProfileChangedEvent || ser.getDetails() instanceof ProfileRemovedEvent
                    || ser.getDetails() instanceof LocationChangedApImpactingEvent || ser.getDetails() instanceof EquipmentCustomerChangedEvent);
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

        switch (se.getClass().getSimpleName()) {
            case "EquipmentApImpactingChangedEvent":
                process((EquipmentApImpactingChangedEvent) se);
                break;
            case "EquipmentChannelsChangedEvent":
                process((EquipmentChannelsChangedEvent) se);
                break;
            case "EquipmentCellSizeAttributesChangedEvent":
                process((EquipmentCellSizeAttributesChangedEvent) se);
                break;
            case "EquipmentBlinkLEDsEvent":
                process((EquipmentBlinkLEDsEvent) se);
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
            case "EquipmentCustomerChangedEvent":
                process((EquipmentCustomerChangedEvent) se);
            default:
                process(mdl);
        }

    }

    private void process(EquipmentApImpactingChangedEvent model) {
        LOG.debug("Processing EquipmentChangedEvent");
        equipmentGatewayInterface.sendCommand(new CEGWConfigChangeNotification(model.getPayload().getInventoryId(), model.getEquipmentId()));
    }

    private void process(EquipmentChannelsChangedEvent model) {
        LOG.debug("Processing EquipmentChannelsChangedEvent for equipmentId {}", model.getEquipmentId());
        equipmentGatewayInterface.sendCommand(new CEGWNewChannelRequest(model.getPayload().getInventoryId(), model.getEquipmentId(),
                model.getNewBackupChannels(), model.getNewPrimaryChannels()));
    }

    private void process(EquipmentCellSizeAttributesChangedEvent model) {
        LOG.debug("Processing EquipmentCellSizeAttributesChangedEvent for equipmentId {}", model.getEquipmentId());
        equipmentGatewayInterface
                .sendCommand(new CEGWCellSizeAttributesRequest(model.getPayload().getInventoryId(), model.getEquipmentId(), model.getCellSizeAttributesMap()));
    }

    private void process(EquipmentBlinkLEDsEvent model) {
        LOG.debug("Processing EquipmentBlinkLEDsEvent for equipmentId {}", model.getEquipmentId());
        CEGWBlinkRequest br = new CEGWBlinkRequest(model.getPayload().getInventoryId(), model.getEquipmentId());
        br.setBlinkAllLEDs(((ApElementConfiguration) model.getPayload().getDetails()).isBlinkAllLEDs());
        equipmentGatewayInterface.sendCommand(br);
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
        if (ret == null || ret.isEmpty()) {
            // nothing to do here
            return;
        }

        Set<Long> parentProfileIds = new HashSet<>();
        ret.forEach(p -> parentProfileIds.add(p.getValue2()));

        // go through all equipmentIds that refer to parent profiles and trigger change config notification on them
        PaginationContext<PairLongLong> context = new PaginationContext<>(100);

        while (!context.isLastPage()) {
            PaginationResponse<PairLongLong> page = equipmentServiceInterface.getEquipmentIdsByProfileIds(parentProfileIds, context);
            context = page.getContext();

            Set<Long> equipmentIds = new HashSet<>();
            page.getItems().forEach(p -> equipmentIds.add(p.getValue2()));

            // retrieve full equipment objects to get the inventory id
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

        // go through all equipmentIds that reside in the specified location and trigger change config notification on
        // them
        PaginationContext<PairLongLong> context = new PaginationContext<>(100);

        while (!context.isLastPage()) {
            PaginationResponse<PairLongLong> page = equipmentServiceInterface.getEquipmentIdsByLocationIds(locationIds, context);
            context = page.getContext();

            Set<Long> equipmentIds = new HashSet<>();
            page.getItems().forEach(p -> equipmentIds.add(p.getValue2()));

            // retrieve full equipment objects to get the inventory id
            List<Equipment> equipmentForPage = equipmentServiceInterface.get(equipmentIds);

            List<CEGWBaseCommand> commands = new ArrayList<>(equipmentForPage.size());
            equipmentForPage.forEach(eq -> commands.add(new CEGWConfigChangeNotification(eq.getInventoryId(), eq.getId())));

            equipmentGatewayInterface.sendCommands(commands);
            LOG.debug("Page {} - sent {} commands to equipment gateway", context.getLastReturnedPageNumber(), commands.size());
        }

        LOG.debug("Finished processing LocationChangedApImpactingEvent {}", model.getPayload().getId());

    }

    private void process(EquipmentCustomerChangedEvent model) {
        LOG.info("Processing EquipmentCustomerChangedEvent {}", model.getPayload().getId());
        
        Equipment existingEquipment = model.getExistingEquipment();
        Equipment equipment = model.getEquipment();

        // when customerId changes, we keep the EQUIPMENT_ADMIN and PROTOCOL status of the AP
        Status status = statusServiceInterface.getOrNull(existingEquipment.getCustomerId(), existingEquipment.getId(), StatusDataType.EQUIPMENT_ADMIN);
        if (status != null) {
            status.setCustomerId(equipment.getCustomerId());
            statusServiceInterface.update(status);
        }
        status = statusServiceInterface.getOrNull(existingEquipment.getCustomerId(), existingEquipment.getId(), StatusDataType.PROTOCOL);
        if (status != null) {
            status.setCustomerId(equipment.getCustomerId());
            statusServiceInterface.update(status);
        }

        // Alarms has to move to new customerId as well
        List<Alarm> oldCustomerAlarms = alarmServiceInterface.get(existingEquipment.getCustomerId(), Set.of(existingEquipment.getId()), null);
        if (!oldCustomerAlarms.isEmpty()) {
            oldCustomerAlarms.stream().forEach(a -> {
                a.setCustomerId(equipment.getCustomerId());
                Alarm alarm = alarmServiceInterface.create(a);
                LOG.debug("Move an alarm to new customer {}", alarm);
            });
        }
        alarmServiceInterface.delete(existingEquipment.getCustomerId(), existingEquipment.getId());
        
        // Disconnect all associated client devices from existing equipment
        disconnectClients(existingEquipment);
        
    }

    private void process(BaseJsonModel model) {
        LOG.warn("Unprocessed model: {}", model);
    }
    
    private void disconnectClients(Equipment ce) {

        LOG.info("EquipmentConfigPushTrigger::disconnectClients for Equipment {}", ce);
        PaginationResponse<ClientSession> clientSessions = clientServiceInterface.getSessionsForCustomer(
                ce.getCustomerId(), Set.of(ce.getId()), Set.of(ce.getLocationId()), null, null,
                new PaginationContext<ClientSession>(100));

        if (clientSessions == null) {
            LOG.info("There are no existing client sessions to disconnect.");
            return;
        }

        List<ClientSession> toBeDisconnected = new ArrayList<>();

        clientSessions.getItems().stream().forEach(c -> {
            if (c.getDetails().getAssociationState() != null
                    && !c.getDetails().getAssociationState().equals(AssociationState.Disconnected)) {
                LOG.info("Change association state for client {} from {} to {}", c.getMacAddress(),
                        c.getDetails().getAssociationState(), AssociationState.Disconnected);

                c.getDetails().setAssociationState(AssociationState.Disconnected);
                toBeDisconnected.add(c);

            }
        });

        if (!toBeDisconnected.isEmpty()) {
            LOG.info("Sending disconnect for client sessions {}", toBeDisconnected);
            List<ClientSession> disconnectedSessions = clientServiceInterface.updateSessions(toBeDisconnected);
            LOG.info("Result of client disconnect {}", disconnectedSessions);
        } else {
            LOG.info("There are no existing client sessions that are not already in Disconnected state.");
        }

    }
}
