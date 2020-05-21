package com.telecominfraproject.wlan.client.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class ClientDetails extends BaseJsonModel {
    
	private static final long serialVersionUID = 5570757656953699233L;
	

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		return false;
	}
	
    @Override
    public ClientDetails clone() {
        return (ClientDetails) super.clone();
    }

    
}
