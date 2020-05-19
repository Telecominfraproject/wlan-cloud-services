package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author ekeddy
 *
 */
public class EquipmentScanDetails extends StatusDetails 
{
    private static final long serialVersionUID = -6496297030960362593L;
    private static final Logger LOG = LoggerFactory.getLogger(EquipmentScanDetails.class);


    // Neighbouring APs that belong to a different customer that is managed by
    // A2W
    private Map<Integer, List<ManagedNeighbourEquipmentInfo>> managedNeighbours = new HashMap<>();

    // Neighbouring APs that are not managed by A2W
    private List<UnmanagedNeighbourEquipmentInfo> unmanagedNeighbours = new ArrayList<>();

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.NEIGHBOUR_SCAN;
    }
    
    public Map<Integer, List<ManagedNeighbourEquipmentInfo>> getManagedNeighbours() {
        return managedNeighbours;
    }

    @JsonIgnore
    public List<ManagedNeighbourEquipmentInfo> getManagedNeighboursList() {

        List<ManagedNeighbourEquipmentInfo> ret = new ArrayList<>();

        if (managedNeighbours != null) {
            for (List<ManagedNeighbourEquipmentInfo> list : managedNeighbours.values()) {
                ret.addAll(list);
            }
        }

        return ret;
    }

    public void setManagedNeighbours(Map<Integer, List<ManagedNeighbourEquipmentInfo>> managedNeighbours) {
        this.managedNeighbours = managedNeighbours;
    }

    public List<UnmanagedNeighbourEquipmentInfo> getUnmanagedNeighbours() {
        return unmanagedNeighbours;
    }

    public void setUnmanagedNeighbours(List<UnmanagedNeighbourEquipmentInfo> unmanagedNeighbours) {
        this.unmanagedNeighbours = unmanagedNeighbours;
    }

    /*
     * A quick util to know whether or not this report contains information
     * about a specific equipmentId.
     * 
     */
    @JsonIgnore
    // NOTE: THIS IS CALLED FROM RULES (so doing a "called from" will not show
    // you where
    // it's used.
    public boolean seesEquipment(long equipmentId) {
        for (ManagedNeighbourEquipmentInfo managedInfo : getManagedNeighboursList()) {
            if (managedInfo.getEquipmentId() == equipmentId) {
                return true;
            }
        }

        return false;

    }

    @JsonIgnore
    /**
     * This is a util method to remove any A2W equipment that has been flagged
     * as offline.
     * 
     * @param customerId
     * @param equipmentId
     * @return
     */
    public boolean removeDisconnectedEquipment(int customerId, long equipmentId) {
        if (this.managedNeighbours != null) {
            List<ManagedNeighbourEquipmentInfo> managedEquipment = this.managedNeighbours.get(customerId);

            if (managedEquipment != null) {
                Iterator<ManagedNeighbourEquipmentInfo> equipmentIterator = managedEquipment.iterator();

                while (equipmentIterator.hasNext()) {
                    ManagedNeighbourEquipmentInfo next = equipmentIterator.next();

                    if (Objects.equals(next.getEquipmentId(), equipmentId)) {
                        equipmentIterator.remove();
                        return true;
                    }
                }
            }
        }

        return false;

    }

    public void addFakeManagedNeighbourIfAbsent(int customer, long equId, int twoGRadio, int fiveGRadio, int signalStrength)
    {
        LOG.debug("addFakeManagedNeighbourIfAbsent(customer {}, equId {}, twoGRadio {}, fiveGRadio {}, signalStrength {})", customer, equId, twoGRadio, fiveGRadio, signalStrength);

        if(this.managedNeighbours == null)
        {
            this.managedNeighbours = new HashMap<>();
        }
        
        List<ManagedNeighbourEquipmentInfo> managedAps = this.managedNeighbours.get(customer);
        
        if(managedAps == null)
        {
            managedAps = new ArrayList<>();
            this.managedNeighbours.put(customer, managedAps);
        }
        
        Optional<ManagedNeighbourEquipmentInfo> optManaged = managedAps.stream().filter(c -> c.getEquipmentId() == equId).findAny();
        ManagedNeighbourEquipmentInfo managedAp = new ManagedNeighbourEquipmentInfo();
        
        if(optManaged.isPresent())
        {
            managedAp = optManaged.get();
        }
        else
        {
            managedAp.setEquipmentId(equId);
            managedAps.add(managedAp);
        }

    	NeighbourRadioInfo twoGRadioInfo = managedAp.getRadioInfo().get(RadioType.is2dot4GHz);
    	
        if(twoGRadioInfo == null)
        {
            NeighbourBssidInfo bssidInfo = new NeighbourBssidInfo();
            bssidInfo.setSignal(signalStrength);
            bssidInfo.setRadioType(RadioType.is2dot4GHz);
            bssidInfo.setChannel(twoGRadio);

            twoGRadioInfo = new NeighbourRadioInfo();
            twoGRadioInfo.setRadioType(RadioType.is2dot4GHz);
            twoGRadioInfo.setBssIds(Collections.singletonList(bssidInfo));
            twoGRadioInfo.setChannel(twoGRadio);
            
            managedAp.getRadioInfo().put(RadioType.is2dot4GHz, twoGRadioInfo);
        }
        else
        {
            // We override the channel
        	twoGRadioInfo.setChannel(twoGRadio);
            if(twoGRadioInfo.getBssIds() != null)
            {
            	twoGRadioInfo.getBssIds().forEach(c -> c.setChannel(twoGRadio));
            }
        }

    	NeighbourRadioInfo fiveGRadioInfo = managedAp.getRadioInfo(RadioType.is5GHz);

        if(fiveGRadioInfo == null)
        {
            NeighbourBssidInfo bssidInfo = new NeighbourBssidInfo();
            bssidInfo.setSignal(signalStrength);
            bssidInfo.setRadioType(RadioType.is5GHz);
            bssidInfo.setChannel(fiveGRadio);

            fiveGRadioInfo = new NeighbourRadioInfo();
            fiveGRadioInfo.setRadioType(RadioType.is5GHz);
            fiveGRadioInfo.setBssIds(Collections.singletonList(bssidInfo));
            fiveGRadioInfo.setChannel(fiveGRadio);
            
            managedAp.getRadioInfo().put(RadioType.is5GHz, fiveGRadioInfo);
        }
        else
        {
            // We override the channel
        	fiveGRadioInfo.setChannel(fiveGRadio);            
            if(fiveGRadioInfo.getBssIds() != null)
            {
            	fiveGRadioInfo.getBssIds().forEach(c -> c.setChannel(fiveGRadio));
            }
        }
        
        LOG.debug("addFakeManagedNeighbourIfAbsent(customer {}, equId {}, twoGRadio {}, fiveGRadio {}, signalStrength {}) resulted in {}", customer, equId, twoGRadio, fiveGRadio, signalStrength, managedNeighbours);

    }
    
    @Override
    public EquipmentScanDetails clone() {
        EquipmentScanDetails ret = (EquipmentScanDetails) super.clone();

        if (this.managedNeighbours != null) {
            ret.managedNeighbours = new HashMap<>();
            this.managedNeighbours.forEach((k, v) -> {
                if (v != null) {
                	List<ManagedNeighbourEquipmentInfo> apList = new ArrayList<>(v.size());
                    v.forEach( p -> apList.add(p.clone()));
                    ret.managedNeighbours.put(k, apList);
                } else {
                	ret.managedNeighbours.put(k, null);
                }
            });
        }
        
        if (this.unmanagedNeighbours != null) {
            ret.unmanagedNeighbours = new ArrayList<>(this.unmanagedNeighbours.size());
            this.unmanagedNeighbours.forEach(p -> ret.unmanagedNeighbours.add(p.clone()));
        }
        return ret;
    }

}
