package com.telecominfraproject.wlan.status.equipment.models;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.equipment.models.PeerInfo;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author yongli
 *
 */
public class EquipmentPeerStatusData extends StatusDetails {
    private static final long serialVersionUID = -2907069616195082322L;

    /**
     * Peer Information
     */
    private List<PeerInfo> peers;

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.PEERINFO;
    }
    
    public EquipmentPeerStatusData clone() {
        EquipmentPeerStatusData result = (EquipmentPeerStatusData) super.clone();
        if (null != getPeers()) {
            result.setPeers(new ArrayList<>(getPeers().size()));
            for (PeerInfo p : getPeers()) {
                result.getPeers().add(p.clone());
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EquipmentPeerStatusData)) {
            return false;
        }
        EquipmentPeerStatusData other = (EquipmentPeerStatusData) obj;
        if (peers == null) {
            if (other.peers != null) {
                return false;
            }
        } else if (!peers.equals(other.peers)) {
            return false;
        }
        return true;
    }

    public List<PeerInfo> getPeers() {
        return peers;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((peers == null) ? 0 : peers.hashCode());
        return result;
    }

    public void setPeers(List<PeerInfo> peers) {
        this.peers = peers;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        
        if (hasUnsupportedValue(peers)) {
            return true;
        }
        return false;
    }

}
