package com.telecominfraproject.wlan.profile.bonjour.models;

import java.util.HashSet;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.NetworkConstants;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * Contains set of explicit Bonjour Service Name.
 * 
 * @author yongli
 *
 */
public class BonjourServiceSet extends BaseJsonModel {
    private static final long serialVersionUID = -868316834055313732L;
    /**
     * VLAN id, null if it's the default VLAN
     */
    private Short vlanId;
    private boolean supportAllService;

    /**
     * List of service. Should be empty if {@link #isSupportAllService()} is
     * true
     */
    private Set<String> serviceNames;

    /**
     * Add a service name
     * 
     * @param serviceName
     */
    public void addServiceName(String serviceName) {
        if (null == this.serviceNames) {
            this.serviceNames = new HashSet<>();
        }
        this.serviceNames.add(serviceName);
    }

    @Override
    public BonjourServiceSet clone() {
        BonjourServiceSet result = (BonjourServiceSet) super.clone();
        if (null != this.serviceNames) {
            result.serviceNames = new HashSet<>(this.serviceNames);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BonjourServiceSet)) {
            return false;
        }
        BonjourServiceSet other = (BonjourServiceSet) obj;
        if (serviceNames == null) {
            if (other.serviceNames != null) {
                return false;
            }
        } else if (!serviceNames.equals(other.serviceNames)) {
            return false;
        }
        if (supportAllService != other.supportAllService) {
            return false;
        }
        if (vlanId == null) {
            if (other.vlanId != null) {
                return false;
            }
        } else if (!vlanId.equals(other.vlanId)) {
            return false;
        }
        return true;
    }

    public Set<String> getServiceNames() {
        if (isSupportAllService()) {
            return null;
        }
        return this.serviceNames;
    }

    public Short getVlanId() {
        return this.vlanId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serviceNames == null) ? 0 : serviceNames.hashCode());
        result = prime * result + (supportAllService ? 1231 : 1237);
        result = prime * result + ((vlanId == null) ? 0 : vlanId.hashCode());
        return result;
    }

    public boolean isSupportAllService() {
        return this.supportAllService;
    }

    /**
     * Merge the set of services.
     * 
     * @param bonjourServiceSet
     */
    public void mergeServiceSet(BonjourServiceSet bonjourServiceSet) {
        if (this.supportAllService) {
            return;
        }
        if (bonjourServiceSet.supportAllService) {
            this.supportAllService = true;

            this.serviceNames.clear();
        } else if (null != bonjourServiceSet.serviceNames) {
            if (null == this.serviceNames) {
                this.serviceNames = new HashSet<>(bonjourServiceSet.getServiceNames());
            } else {
                this.serviceNames.addAll(bonjourServiceSet.getServiceNames());
            }
        }
    }

    public void setServiceNames(Set<String> serviceNames) {
        this.serviceNames = serviceNames;
    }

    public void setSupportAllService(boolean supportAllService) {
        this.supportAllService = supportAllService;
    }

    public void setVlanId(Short vlanId) {
        this.vlanId = vlanId;
    }

    /**
     * Make sure the settings are valid
     */
    public void validateValue() {
        // check VLAN id
        if ((null != this.vlanId) && !NetworkConstants.isValidVLANTagForProvision(this.vlanId)) {
            throw new DsDataValidationException("Invalid VLAN id specified: " + this.vlanId);
        }
        if (!this.supportAllService && ((null == this.serviceNames) || this.serviceNames.isEmpty())) {
            throw new DsDataValidationException("No bonjour service name defined for service VLAN " + this.vlanId);
        }
    }
}
