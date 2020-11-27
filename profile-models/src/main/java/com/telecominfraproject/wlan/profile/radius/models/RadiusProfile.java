package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * Radius Profile
 * 
 * It contains the following information:
 * <ul>
 * <li>set of service region configurations.
 * <li>set of subnet with optional proxy floating IP and reference the service
 * region.
 * </ul>
 * 
 * @author yongli
 *
 */
public class RadiusProfile extends ProfileDetails implements PushableConfiguration<RadiusProfile> {
    private static final long serialVersionUID = 5489888031341902764L;

    public static final String DEFALUT_PROFILE_NAME = "Default";

    @Override
    public ProfileType getProfileType() {
    	return ProfileType.radius;
    }

    /**
     * Check if an IP address is in the subnet/prefix
     * 
     * @param ipAddress
     * @param subnetAddress
     * @param subnetCidrPrefix
     * @return true if ip is in subnet, false if not
     */
    public static boolean isIpInSubnet(InetAddress ipAddress, InetAddress subnetAddress, Integer subnetCidrPrefix) {
        boolean found = true;
        byte[] subnet = subnetAddress.getAddress();
        byte[] reported = ipAddress.getAddress();
        int i = 0;
        for (; i < subnetCidrPrefix / 8; ++i) {
            if (subnet[i] != reported[i]) {
                found = false;
            }
        }
        if (subnetCidrPrefix % 8 != 0) {
            // need to check some bits
            byte mask = 0;
            for (int k = 0; k < subnetCidrPrefix % 8; ++k) {
                mask |= 0b1 << (7 - k);
            }
            if ((subnet[i] & mask) != (reported[i] & mask)) {
                found = false;
            }
        }
        return found;
    }

    /**
     * Map of subnet configurations. Key is the subnet name, value is the actual configuration.
     */
    private Map<String, RadiusSubnetConfiguration> subnetConfiguration;

    /**
     * Map of Radius Service Regions, keyed by Region Name
     */
    private Map<String, RadiusServiceRegion> serviceRegionMap;


    /**
     * Add a new {@link RadiusServiceRegion} into the profile. The region name
     * must be unique. Throws ConfigurationException if fails.
     * 
     * @param configValue
     */
    public void addRadiusServiceRegion(RadiusServiceRegion configValue) {
        configValue.validateConfig();
        if (null == serviceRegionMap) {
            serviceRegionMap = new HashMap<>();
        } else if (serviceRegionMap.containsKey(configValue.getRegionName())) {
            throw new ConfigurationException("duplicated radius service zone " + configValue.getRegionName());
        }
        serviceRegionMap.put(configValue.getRegionName(), configValue);
    }

    /**
     * Add a new {@link RadiusSubnetConfiguration} into the profile. The subnet
     * name must be unique. It throws ConfigurationException if fails.
     * 
     * @param configValue
     */
    public void addSubnetConfiguration(RadiusSubnetConfiguration configValue) {
        configValue.validateConfig();

        if (null == this.subnetConfiguration) {
            this.subnetConfiguration = new HashMap<>();
        } else if (this.subnetConfiguration.containsKey(configValue.getSubnetName())) {
            throw new ConfigurationException("duplicated subnet configuration name " + configValue.getSubnetName());
        }
        if (null == this.serviceRegionMap || !this.serviceRegionMap.containsKey(configValue.getServiceRegionName())) {
            // missing server key
            throw new ConfigurationException("invalid service zone " + configValue.getServiceRegionName());
        }
        // now validate there isn't an overlap subnet
        for (RadiusSubnetConfiguration existing : this.subnetConfiguration.values()) {
            if (existing.overlapSubnet(configValue)) {
                throw new ConfigurationException("overlap subnet address in " + existing.showSubnetString() + " and "
                        + configValue.showSubnetString());
            }
        }
        this.subnetConfiguration.put(configValue.getSubnetName(), configValue);
    }

    public void clearConfiguration() {
        clearSubnetConfiguration();
        if (null != this.serviceRegionMap) {
            this.serviceRegionMap.clear();
        }
    }

    public void clearSubnetConfiguration() {
        if (null != this.subnetConfiguration) {
            subnetConfiguration.clear();
        }
    }

    @Override
    public RadiusProfile clone() {
        RadiusProfile result = (RadiusProfile) super.clone();
        if (null != this.serviceRegionMap) {
            result.serviceRegionMap = new HashMap<>(this.serviceRegionMap.size());
            for (Entry<String, RadiusServiceRegion> entry : this.serviceRegionMap.entrySet()) {
                result.serviceRegionMap.put(entry.getKey(), entry.getValue().clone());
            }

        }
        if (null != this.subnetConfiguration) {
            result.subnetConfiguration = new HashMap<>(
                    this.subnetConfiguration.size());
            for (Entry<String, RadiusSubnetConfiguration> entry : this.subnetConfiguration.entrySet()) {
                result.subnetConfiguration.put(entry.getKey(), entry.getValue().clone());
            }
        }
        return result;
    }

    public RadiusServiceRegion findServiceRegion(String regionName) {
        if (null != this.serviceRegionMap) {
            return this.serviceRegionMap.get(regionName);
        }
        return null;
    }

    /**
     * Find a subnet configuration for a given equipment IP address
     * 
     * @param equipmentIp
     * @return null if not found
     */
    public RadiusSubnetConfiguration findSubnetConifguration(InetAddress equipmentIp) {
        if (null == this.subnetConfiguration) {
            return null;
        }
        for (RadiusSubnetConfiguration subnetConfig : this.subnetConfiguration.values()) {
            if (isIpInSubnet(equipmentIp, subnetConfig.getSubnetAddress(), subnetConfig.getSubnetCidrPrefix())) {
                return subnetConfig;
            }
        }
        return null;
    }


    /**
     * DO NOT use this directly. It's only used by JSON serialization.
     * 
     * @return service region map
     */
    @Deprecated
    public Map<String, RadiusServiceRegion> getServiceRegionMap() {
        return serviceRegionMap;
    }

    @JsonIgnore
    public int getServiceRegionMapSize() {
        if (null != this.serviceRegionMap) {
            return this.serviceRegionMap.size();
        }
        return 0;
    }

    /**
     * DO NOT use this directly
     * 
     * @return subnet configuration
     */
    @Deprecated
    public Map<String, RadiusSubnetConfiguration> getSubnetConfiguration() {
        return subnetConfiguration;
    }

    @JsonIgnore
    public Set<Entry<String, RadiusSubnetConfiguration>> getSubnetConfigurationEntrySet() {
        if (null != this.subnetConfiguration) {
            return this.subnetConfiguration.entrySet();
        }
        return Collections.emptySet();
    }

    @JsonIgnore
    public int getSubnetConfigurationSize() {
        if (null == this.subnetConfiguration) {
            return 0;
        }
        return this.subnetConfiguration.size();
    }

    /**
     * DO NOT use this directly. It's only used by JSON serialization. Use
     * {@link #addRadiusServiceRegion(RadiusServiceRegion)}.
     * 
     * @param serviceRegionMap
     */
    @Deprecated
    public void setServiceRegionMap(Map<String, RadiusServiceRegion> serviceRegionMap) {
        this.serviceRegionMap = serviceRegionMap;
    }

    /**
     * DO NOT use this directly. It's only used by JSON serialization. Use
     * {@link #addSubnetConfiguration(RadiusSubnetConfiguration)}
     * 
     * @param subnetConfiguration
     */
    @Deprecated
    public void setSubnetConfiguration(Map<String, RadiusSubnetConfiguration> subnetConfiguration) {
        this.subnetConfiguration = subnetConfiguration;
    }

    /**
     * Validate RADIUS profile. Throws ConfigurationException if it's not valid.
     */
    public void validateConfig() {
        if (null != this.serviceRegionMap) {
            for (RadiusServiceRegion entry : this.serviceRegionMap.values()) {
                entry.validateConfig();
            }
        }
        if (null == this.subnetConfiguration) {
            return;
        }
        ArrayList<RadiusSubnetConfiguration> list = new ArrayList<>(
                this.subnetConfiguration.values());

        for (int i = 0; i < list.size(); ++i) {
            RadiusSubnetConfiguration entry = list.get(i);
            entry.validateConfig();
            if ((null == this.serviceRegionMap) || (!this.serviceRegionMap.containsKey(entry.getServiceRegionName()))) {
                throw new ConfigurationException("missing server mapping " + entry.getServiceRegionName()
                        + " in subnet " + entry.getSubnetName());
            }
            // now check for overlap with the rest
            for (int j = i + 1; j < list.size(); ++j) {
                RadiusSubnetConfiguration nextSubnet = list.get(j);
                if (entry.overlapSubnet(nextSubnet)) {
                    throw new ConfigurationException("overlap subnet address between " + entry.showSubnetString()
                            + " and " + nextSubnet.showSubnetString());
                }
            }
        }
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadiusProfile previousVersion) {
        if (this.equals(previousVersion)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceRegionMap, subnetConfiguration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadiusProfile other = (RadiusProfile) obj;
        return Objects.equals(serviceRegionMap, other.serviceRegionMap)
                && Objects.equals(subnetConfiguration, other.subnetConfiguration);
    }
    
    
}
