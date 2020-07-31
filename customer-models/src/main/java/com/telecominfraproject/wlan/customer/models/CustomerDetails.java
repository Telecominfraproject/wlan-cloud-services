package com.telecominfraproject.wlan.customer.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


/**
 * @author dtoptygin
 *
 */
public class CustomerDetails extends BaseJsonModel{
    private static final long serialVersionUID = 3061657893135029599L;

    private EquipmentAutoProvisioningSettings autoProvisioning;
    private Set<Integer> bannedChannels = new HashSet<>();
    private String clientFingerPrintsDbUrl;

    @Override
    public CustomerDetails clone() {
        CustomerDetails ret = (CustomerDetails) super.clone();

        if(autoProvisioning!=null) {
        	ret.autoProvisioning = autoProvisioning.clone();
        }

        return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(autoProvisioning);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CustomerDetails)) {
			return false;
		}
		CustomerDetails other = (CustomerDetails) obj;
		return Objects.equals(autoProvisioning, other.autoProvisioning) &&
				Objects.equals(bannedChannels, other.bannedChannels);
	}

	public EquipmentAutoProvisioningSettings getAutoProvisioning() {
		return autoProvisioning;
	}

	public void setAutoProvisioning(EquipmentAutoProvisioningSettings autoProvisioning) {
		this.autoProvisioning = autoProvisioning;
	}

	public Set<Integer> getBannedChannels() {
		return bannedChannels;
	}

	public void setBannedChannels(Set<Integer> bannedChannels) {
		this.bannedChannels = bannedChannels;
	}

    public String getClientFingerPrintsDbUrl() {
        return clientFingerPrintsDbUrl;
    }

    public void setClientFingerPrintsDbUrl(String clientFingerPrintsDbUrl) {
        this.clientFingerPrintsDbUrl = clientFingerPrintsDbUrl;
    }
    
	
}
