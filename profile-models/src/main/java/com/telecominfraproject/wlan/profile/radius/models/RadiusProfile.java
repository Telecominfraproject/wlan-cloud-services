package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;

/**
 * Radius Profile
 * 
 *
 */
public class RadiusProfile extends ProfileDetails implements PushableConfiguration<RadiusProfile> {
    private static final long serialVersionUID = 5489888031341902764L;

    @Override
    public ProfileType getProfileType() {
        return ProfileType.radius;
    }

    private RadiusServer primaryRadiusServer;
    private RadiusServer secondaryRadiusServer;
    private RadiusSubnetConfiguration radiusSubnetConfiguration;

    public RadiusProfile() {
        // for serialization
    }

    public RadiusServer getPrimaryRadiusServer() {
        return primaryRadiusServer;
    }

    public void setPrimaryRadiusServer(RadiusServer primaryRadiusServer) {
        this.primaryRadiusServer = primaryRadiusServer;
    }

    public RadiusServer getSecondaryRadiusServer() {
        return secondaryRadiusServer;
    }

    public void setSecondaryRadiusServer(RadiusServer secondaryRadiusServer) {
        this.secondaryRadiusServer = secondaryRadiusServer;
    }

    public RadiusSubnetConfiguration getRadiusSubnetConfiguration() {
        return radiusSubnetConfiguration;
    }

    public void setRadiusSubnetConfiguration(RadiusSubnetConfiguration radiusSubnetConfiguration) {
        this.radiusSubnetConfiguration = radiusSubnetConfiguration;
    }
    
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

    @Override
    public RadiusProfile clone() {
        RadiusProfile result = (RadiusProfile) super.clone();
        if (primaryRadiusServer != null) {
            result.primaryRadiusServer = primaryRadiusServer.clone();
        }
        if (secondaryRadiusServer != null) {
            result.secondaryRadiusServer = secondaryRadiusServer.clone();
        }
        if (radiusSubnetConfiguration != null) {
            result.radiusSubnetConfiguration = radiusSubnetConfiguration.clone();
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryRadiusServer, radiusSubnetConfiguration, secondaryRadiusServer);
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
        return Objects.equals(primaryRadiusServer, other.primaryRadiusServer)
                && Objects.equals(radiusSubnetConfiguration, other.radiusSubnetConfiguration)
                && Objects.equals(secondaryRadiusServer, other.secondaryRadiusServer);
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadiusProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

}
