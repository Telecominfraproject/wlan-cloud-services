package com.telecominfraproject.wlan.profile.event.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;

public class ApEventRateConfigurationDetails extends ProfileDetails
        implements PushableConfiguration<ApEventRateConfigurationDetails> {

    private static final long serialVersionUID = -4396345492525165619L;

    private Map<ApEventDataType, ApEventTimeBasedThrottle> eventRateMap;

    public ApEventRateConfigurationDetails() {
        eventRateMap = new EnumMap<>(ApEventDataType.class);
    }

    public static ApEventRateConfigurationDetails createWithDefaults() {

        ApEventRateConfigurationDetails ret = new ApEventRateConfigurationDetails();

        for (ApEventDataType data : ApEventDataType.values()) {
            if (data.equals(ApEventDataType.UNSUPPORTED)) continue; 
            if (data.toString().startsWith("Client")) {
                ret.eventRateMap.put(data, new ApEventTimeBasedThrottle(false, 60, 10));
            } else if (data.toString().startsWith("RealTimeSip")) {
                ret.eventRateMap.put(data, new ApEventTimeBasedThrottle(false, 60, 5));
            } else if (data.toString().startsWith("Dhcp")) {
                ret.eventRateMap.put(data, new ApEventTimeBasedThrottle(false, 60, 20));
              } else {
                  ret.eventRateMap.put(data, new ApEventTimeBasedThrottle(true, 60, 0));
              }
        }

        return ret;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(ApEventRateConfigurationDetails previousVersion) {
        return !Objects.equals(this, previousVersion);
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.ap_event_rate;
    }

    public Map<ApEventDataType, ApEventTimeBasedThrottle> getEventRateMap() {
        return eventRateMap;
    }

    public void setEventRateMap(Map<ApEventDataType, ApEventTimeBasedThrottle> eventRateMap) {
        this.eventRateMap = eventRateMap;
    }

    @Override
    public ApEventRateConfigurationDetails clone() {
        ApEventRateConfigurationDetails ret = (ApEventRateConfigurationDetails) super.clone();
        if (this.eventRateMap != null) {

            ret.eventRateMap = new EnumMap<>(ApEventDataType.class);

            for (Entry<ApEventDataType, ApEventTimeBasedThrottle> entry : this.eventRateMap.entrySet()) {
                ret.eventRateMap.put(entry.getKey(), entry.getValue().clone());
            }

        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventRateMap == null) ? 0 : eventRateMap.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApEventRateConfigurationDetails other = (ApEventRateConfigurationDetails) obj;
        if (eventRateMap == null) {
            if (other.eventRateMap != null)
                return false;
        } else if (!eventRateMap.equals(other.eventRateMap))
            return false;
        return true;
    }

}
