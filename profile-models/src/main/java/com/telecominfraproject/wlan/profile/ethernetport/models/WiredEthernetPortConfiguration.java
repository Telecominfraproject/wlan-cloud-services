package com.telecominfraproject.wlan.profile.ethernetport.models;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.ssid.models.SsidConfiguration;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author rsharma
 */
public class WiredEthernetPortConfiguration extends ProfileDetails implements PushableConfiguration<WiredEthernetPortConfiguration> {

    private static final long serialVersionUID = 5326345492525165619L;

    private String equipmentModel; // e.g., MR8300-CA, EC420-G1

    private Set<WiredPort> ethPorts;

    public WiredEthernetPortConfiguration() {
        // for Serialization
    }

    public WiredEthernetPortConfiguration(String equipmentModel, Set<WiredPort> ethPorts) {
        this.equipmentModel = equipmentModel;
        this.ethPorts = ethPorts;
    }

    public static WiredEthernetPortConfiguration createWithDefaults() {
    	return new WiredEthernetPortConfiguration();
    }
    
    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public Set<WiredPort> getEthPorts() {
        return ethPorts;
    }

    public void setEthPorts(Set<WiredPort> ethPorts) {
        this.ethPorts = ethPorts;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(WiredEthernetPortConfiguration previousVersion) {
        if (previousVersion != null) {
            return !Objects.equals(this, previousVersion);
        }

        return true;
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.wired_ethernet_port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WiredEthernetPortConfiguration)) {
            return false;
        }
        WiredEthernetPortConfiguration that = (WiredEthernetPortConfiguration) o;
        return Objects.equals(getEquipmentModel(), that.getEquipmentModel()) &&
                Objects.equals(getEthPorts(), that.getEthPorts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEquipmentModel(), getEthPorts());
    }

    @Override
    public WiredEthernetPortConfiguration clone() {
        WiredEthernetPortConfiguration ret = (WiredEthernetPortConfiguration)super.clone();

        if (ethPorts != null) {
            ret.ethPorts = new HashSet<>(ethPorts);
        }

        return ret;
    }
}
