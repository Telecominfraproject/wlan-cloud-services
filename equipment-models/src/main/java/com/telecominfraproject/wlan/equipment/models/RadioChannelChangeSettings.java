package com.telecominfraproject.wlan.equipment.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class RadioChannelChangeSettings extends BaseJsonModel
        implements PushableConfiguration<RadioChannelChangeSettings> {

    private static final long serialVersionUID = -5393966465839133406L;
    
    private Map<RadioType, Integer> primaryChannel = new EnumMap<>(RadioType.class);
    private Map<RadioType, Integer> backupChannel = new EnumMap<>(RadioType.class);      

    public RadioChannelChangeSettings () {
        
    }

    public Integer getPrimaryChannel(RadioType radioType) {
        if (primaryChannel == null) { 
            return null;
        }
        return primaryChannel.get(radioType);
    }

    public void setPrimaryChannel(RadioType radioType, Integer channel) {
        if (primaryChannel == null) {
            primaryChannel = new EnumMap<>(RadioType.class);
        }
        primaryChannel.put(radioType, channel);        
    }
    
    public Map<RadioType, Integer> getPrimaryChannel() {
        return primaryChannel;
    }
    
    public void setPrimaryChannel(Map<RadioType, Integer> primaryChannel) {
        this.primaryChannel = primaryChannel;
    }

    public Integer getBackupChannel(RadioType radioType) {
        if (backupChannel == null) { 
            return null;
        }
        return backupChannel.get(radioType);
    }

    public void setBackupChannel(RadioType radioType, Integer channel) {
        if (backupChannel == null) {
            backupChannel = new EnumMap<>(RadioType.class);
        }
        backupChannel.put(radioType, channel);        
    }    
    
    public Map<RadioType, Integer> getBackupChannel() {
        return backupChannel;
    }
    
    public void setBackupChannel(Map<RadioType, Integer> backupChannel) {
        this.backupChannel = backupChannel;
    }
               
    @Override
    public boolean needsToBeUpdatedOnDevice(RadioChannelChangeSettings previousVersion) {
        return !Objects.equals(previousVersion, this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backupChannel, primaryChannel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadioChannelChangeSettings other = (RadioChannelChangeSettings) obj;
        return Objects.equals(backupChannel, other.backupChannel)
                && Objects.equals(primaryChannel, other.primaryChannel);
    }

}
