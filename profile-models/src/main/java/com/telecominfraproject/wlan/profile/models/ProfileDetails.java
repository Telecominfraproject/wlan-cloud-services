package com.telecominfraproject.wlan.profile.models;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public abstract class ProfileDetails extends BaseJsonModel {
    
	private static final long serialVersionUID = 5570757656953699233L;
	

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		return false;
	}
	
    @Override
    public ProfileDetails clone() {
        return (ProfileDetails) super.clone();
    }
    
    public abstract ProfileType getProfileType(); 
    
}
