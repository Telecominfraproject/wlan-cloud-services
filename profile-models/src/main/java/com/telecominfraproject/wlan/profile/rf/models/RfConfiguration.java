package com.telecominfraproject.wlan.profile.rf.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;

public class RfConfiguration extends ProfileDetails implements PushableConfiguration<RfConfiguration>{

	private static final long serialVersionUID = -8842969359594832744L;

	private static final Logger LOG = LoggerFactory.getLogger(RfConfiguration.class);
    
    // Map of RfConfigs for each RadioType
    private Map<RadioType, RfElementConfiguration> rfConfigMap;
    
    private RfConfiguration() {
    	super();
    	rfConfigMap = new EnumMap<>(RadioType.class);
    	rfConfigMap.put(RadioType.is2dot4GHz, RfElementConfiguration.createWithDefaults(RadioType.is2dot4GHz));
    	rfConfigMap.put(RadioType.is5GHz, RfElementConfiguration.createWithDefaults(RadioType.is5GHz));
    	rfConfigMap.put(RadioType.is5GHzL, RfElementConfiguration.createWithDefaults(RadioType.is5GHzL));
    	rfConfigMap.put(RadioType.is5GHzU, RfElementConfiguration.createWithDefaults(RadioType.is5GHzU));
    }
    
    public static RfConfiguration createWithDefaults() {
        RfConfiguration ret = new RfConfiguration();  	
    	return ret;
    }
    
    // Get RfConfig for all RadioTypes
	public Map<RadioType, RfElementConfiguration> getRfConfigMap() {
		return rfConfigMap;
	}

	public void setRfConfigMap(Map<RadioType, RfElementConfiguration> rfConfigMap) {
		this.rfConfigMap = rfConfigMap;
	}
	
	// Get RfConfig for specific RadioType
	public RfElementConfiguration getRfConfig(RadioType radioType) {
		if (rfConfigMap == null)
			return null;
		return rfConfigMap.get(radioType);
	}
	
	public void setRfConfig(RadioType radioType, RfElementConfiguration rfConfig) {
		this.rfConfigMap.put(radioType, rfConfig);
	}
	
	@Override
	public boolean needsToBeUpdatedOnDevice(RfConfiguration previousVersion) {
		return !equals(previousVersion);
	}

	@Override
	public ProfileType getProfileType() {
		return ProfileType.rf;
	}

    @Override
    public int hashCode() {
        return Objects.hash(rfConfigMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RfConfiguration other = (RfConfiguration) obj;
        return Objects.equals(rfConfigMap, other.rfConfigMap);
    }
	
	

}
