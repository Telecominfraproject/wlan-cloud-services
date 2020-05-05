package com.telecominfraproject.wlan.profile.network.models;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.BestAPSteerType;

/**
 * This class holds all the values that may be set per radio at an AP Profile
 * level. Each value in this class should have an equivalent parameter at the
 * ApElementConfig level that will override the value set in this class
 * if the value in the ApElementConfig is not null.
 * 
 * @author mpreston
 */
public class RadioProfileConfiguration extends BaseJsonModel implements PushableConfiguration<RadioProfileConfiguration> {

    private static final long serialVersionUID = -3707130735532947828L;
    
    public static final Boolean DEFAULT_BEST_AP_ENABLED = true;

    /**
     * Will return a default radio profile configuration
     */
    public static RadioProfileConfiguration createWithDefaults(RadioType type) {
        RadioProfileConfiguration configuration = new RadioProfileConfiguration();

        return configuration;
    }

    private Boolean bestApEnabled;
    
    // Initialize here to cover backward compatibility.
    private BestAPSteerType bestAPSteerType = BestAPSteerType.both;

    /*
     * Use static creator please.
     */
    protected RadioProfileConfiguration() {
        bestApEnabled = DEFAULT_BEST_AP_ENABLED;
        setBestAPSteerType(BestAPSteerType.both);
    }

    @Override
    public RadioProfileConfiguration clone() {
        return (RadioProfileConfiguration) super.clone();
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadioProfileConfiguration previousVersion) {
        return !equals(previousVersion);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bestApEnabled == null) ? 0 : bestApEnabled.hashCode());
        result = prime * result + ((bestAPSteerType == null) ? 0 : bestAPSteerType.hashCode());
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
        RadioProfileConfiguration other = (RadioProfileConfiguration) obj;
        if (bestApEnabled == null) {
            if (other.bestApEnabled != null)
                return false;
        } else if (!bestApEnabled.equals(other.bestApEnabled))
            return false;

        if (bestAPSteerType == null ) {
            if (other.bestAPSteerType != null) {
                return false;
            }
        } else if (!bestAPSteerType.equals(other.bestAPSteerType)) {
            return false;
        }
        return true;
    }

    public Boolean getBestApEnabled() {
        return bestApEnabled;
    }

    public void setBestApEnabled(Boolean bestApEnabled) {
        this.bestApEnabled = bestApEnabled;
    }

	public BestAPSteerType getBestAPSteerType() {
		return bestAPSteerType;
	}

	public void setBestAPSteerType(BestAPSteerType bestAPSteerType) {
		this.bestAPSteerType = bestAPSteerType;
	}
	
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (BestAPSteerType.isUnsupported(bestAPSteerType)) {
            return true;
        }
        return false;
    }
}
