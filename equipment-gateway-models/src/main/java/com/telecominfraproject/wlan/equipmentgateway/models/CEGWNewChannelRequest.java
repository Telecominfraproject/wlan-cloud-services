package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

public class CEGWNewChannelRequest extends EquipmentCommand {
    /**
     *
     */
    private static final long serialVersionUID = -8251272945594285119L;
    /**
     *
     */
    private Map<RadioType, Integer> newBackupChannels = new EnumMap<>(RadioType.class);

    protected CEGWNewChannelRequest() {
        // serial
    }

    public CEGWNewChannelRequest(String inventoryId, long equipmentId, Map<RadioType, Integer> newBackupChannels) {
        super(CEGWCommandType.NewChannelRequest, inventoryId, equipmentId);
        this.newBackupChannels = newBackupChannels;
    }

    @JsonIgnore
    public Integer getNewBackupChannel(RadioType radioType) {
        return newBackupChannels.get(radioType);
    }
    
    @JsonIgnore
    public void setNewBackupChannel(RadioType radioType, Integer newBackupChannel) {
    	if (this.newBackupChannels == null) {
    		this.newBackupChannels = new EnumMap<>(RadioType.class);
    	}
        this.newBackupChannels.put(radioType, newBackupChannel);
    }

    public Map<RadioType, Integer> getNewBackupChannels() {
        return newBackupChannels;
    }

    public void setNewBackupChannels(Map<RadioType, Integer> newBackupChannels) {
        this.newBackupChannels = newBackupChannels;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (newBackupChannels != null && !newBackupChannels.isEmpty()) {
            if (newBackupChannels.get(RadioType.UNSUPPORTED) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(newBackupChannels);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CEGWNewChannelRequest)) {
            return false;
        }
        CEGWNewChannelRequest other = (CEGWNewChannelRequest) obj;
        return Objects.equals(newBackupChannels, other.newBackupChannels);
    }

}
