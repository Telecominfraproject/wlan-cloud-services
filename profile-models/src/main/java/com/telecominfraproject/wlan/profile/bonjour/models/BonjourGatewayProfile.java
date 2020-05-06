package com.telecominfraproject.wlan.profile.bonjour.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;

/**
 * Bonjour Gateway Profile. Contains configuration of set of Bonjour service(s)
 * running on a specific service vlan.
 * 
 * @author yongli
 *
 */
public class BonjourGatewayProfile extends ProfileDetails implements PushableConfiguration<BonjourGatewayProfile> {
    private static final long serialVersionUID = -4902368512062769590L;

    /**
     * Description
     */
    private String profileDescription;

    /**
     * Map of service vlan ID to service set
     */
    private Map<Short, BonjourServiceSet> vlanServiceMap;

    /**
     * Add a BonjourServiceSet into the profile. Merge it with existing entry if
     * found by VLAN Id.
     * 
     * @param bonjourServiceSet
     * 
     * @throws DsDataValidationException
     *             if the bonjourServiceSet is invalid
     */
    public void addBonjourServiceSet(BonjourServiceSet bonjourServiceSet) {
        bonjourServiceSet.validateValue();
        if (this.vlanServiceMap == null) {
            this.vlanServiceMap = new HashMap<>();
            this.vlanServiceMap.put(bonjourServiceSet.getVlanId(), bonjourServiceSet);
        } else {
            BonjourServiceSet entry = this.vlanServiceMap.get(bonjourServiceSet.getVlanId());
            if (null == entry) {
                this.vlanServiceMap.put(bonjourServiceSet.getVlanId(), bonjourServiceSet);
            } else {
                // merge the name
                entry.mergeServiceSet(bonjourServiceSet);
            }
        }
    }

    @Override
    public BonjourGatewayProfile clone() {
        BonjourGatewayProfile result = (BonjourGatewayProfile) super.clone();
        if (null != this.vlanServiceMap) {
            result.vlanServiceMap = new HashMap<>();
            for (Entry<Short, BonjourServiceSet> entry : this.vlanServiceMap.entrySet()) {
                if (null != entry.getValue()) {
                    result.vlanServiceMap.put(entry.getKey(), entry.getValue().clone());
                }
            }
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
        if (!(obj instanceof BonjourGatewayProfile)) {
            return false;
        }
        BonjourGatewayProfile other = (BonjourGatewayProfile) obj;
        if (profileDescription == null) {
            if (other.profileDescription != null) {
                return false;
            }
        } else if (!profileDescription.equals(other.profileDescription)) {
            return false;
        }
        if (vlanServiceMap == null) {
            if (other.vlanServiceMap != null) {
                return false;
            }
        } else if (!vlanServiceMap.equals(other.vlanServiceMap)) {
            return false;
        }
        return true;
    }

    public BonjourServiceSet findBonjourServiceSet(short vlanId) {
        if (null == this.vlanServiceMap) {
            return null;
        }
        return this.vlanServiceMap.get(vlanId);
    }

    public Collection<BonjourServiceSet> getBonjourServices() {
        if (null != this.vlanServiceMap) {
            return this.vlanServiceMap.values();
        }
        return null;
    }


    /**
     * @return the profileDescription
     */
    public String getProfileDescription() {
        return profileDescription;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((profileDescription == null) ? 0 : profileDescription.hashCode());
        result = prime * result + ((vlanServiceMap == null) ? 0 : vlanServiceMap.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        return (super.hasUnsupportedValue() || BaseJsonModel.hasUnsupportedValue(this.vlanServiceMap));
    }

    /**
     * Only need to compare the vlan service sets
     */
    @Override
    public boolean needsToBeUpdatedOnDevice(BonjourGatewayProfile previousVersion) {
        if (this.vlanServiceMap.equals(previousVersion.vlanServiceMap)) {
            return false;
        }
        return true;
    }

    public void setBonjourServices(Collection<BonjourServiceSet> bonjourServices) {
        if (null == bonjourServices || bonjourServices.isEmpty()) {
            this.vlanServiceMap = null;
        } else {
            this.vlanServiceMap = new HashMap<>();
            for (BonjourServiceSet service : bonjourServices) {
                this.vlanServiceMap.put(service.getVlanId(), service);
            }
        }
    }

    /**
     * @param profileDescription the profileDescription to set
     */
    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    /**
     * Check if all services are supported by the system.
     * 
     * @param supportedServices
     *            list of supported services
     * 
     * @throws DsDataValidationException
     */
    public void validateValue(Collection<String> supportedServices) {
        if (hasUnsupportedValue()) {
            throw new DsDataValidationException("Contains unsupported value");
        }

        if (null == this.vlanServiceMap || this.vlanServiceMap.isEmpty()) {
            throw new DsDataValidationException("No service vlan defined");
        }

        Set<String> unknownServiceNames = null;
        for (Entry<Short, BonjourServiceSet> serviceEntry : this.vlanServiceMap.entrySet()) {
            serviceEntry.getValue().validateValue();

            // check service name
            if (serviceEntry.getValue().isSupportAllServices()) {
                continue;
            }

            for (String service : serviceEntry.getValue().getServiceNames()) {
                if (!supportedServices.contains(service)) {
                    if (null == unknownServiceNames) {
                        unknownServiceNames = new HashSet<>();
                    }
                    unknownServiceNames.add(service);
                }
            }
        }

        if (null != unknownServiceNames && !unknownServiceNames.isEmpty()) {
            throw new DsDataValidationException(
                    "Unsupported bonjour service(s): " + Arrays.toString(unknownServiceNames.toArray()));
        }
    }
}
