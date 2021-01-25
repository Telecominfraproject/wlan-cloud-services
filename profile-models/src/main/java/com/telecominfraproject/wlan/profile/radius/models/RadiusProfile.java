package com.telecominfraproject.wlan.profile.radius.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;

/**
 * Radius Profile
 * 
 *
 */
public class RadiusProfile extends ProfileDetails implements PushableConfiguration<RadiusProfile> {
    public static final int DEFAULT_RADIUS_TIMEOUT = 5;
    public static final String DEFAULT_RADIUS_SECRET = "secret";
    public static final int DEFAULT_RADIUS_AUTH_PORT = 1812;
    public static final int DEFAULT_RADIUS_ACCOUNTING_PORT = 1813;

    private static final long serialVersionUID = 5489888031341902764L;

    private static final Logger LOG = LoggerFactory.getLogger(RadiusProfile.class);

    @Override
    public ProfileType getProfileType() {
        return ProfileType.radius;
    }

    private RadiusServer primaryRadiusAuthServer;
    private RadiusServer secondaryRadiusAuthServer;
    private RadiusServer primaryRadiusAccountingServer;
    private RadiusServer secondaryRadiusAccountingServer;

    public RadiusProfile() {
        // for serialization
    }

    public static RadiusProfile createWithDefaults() {
        RadiusProfile r = new RadiusProfile();
        RadiusServer s = new RadiusServer();
        s.setPort(DEFAULT_RADIUS_AUTH_PORT);
        s.setSecret(DEFAULT_RADIUS_SECRET);
        s.setTimeout(DEFAULT_RADIUS_TIMEOUT);
        try {
            s.setIpAddress(InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            LOG.warn("UnknownHostException for default radius profile {}", Arrays.toString(e.getStackTrace()));
        }

        r.setPrimaryRadiusAuthServer(s);
        return r;
    }

    public RadiusServer getPrimaryRadiusAuthServer() {
        return primaryRadiusAuthServer;
    }

    public void setPrimaryRadiusAuthServer(RadiusServer primaryRadiusAuthServer) {
        this.primaryRadiusAuthServer = primaryRadiusAuthServer;
    }

    public RadiusServer getSecondaryRadiusAuthServer() {
        return secondaryRadiusAuthServer;
    }

    public void setSecondaryRadiusAuthServer(RadiusServer secondaryRadiusAuthServer) {
        this.secondaryRadiusAuthServer = secondaryRadiusAuthServer;
    }

    public RadiusServer getPrimaryRadiusAccountingServer() {
        return primaryRadiusAccountingServer;
    }

    public void setPrimaryRadiusAccountingServer(RadiusServer primaryRadiusAccountingServer) {
        this.primaryRadiusAccountingServer = primaryRadiusAccountingServer;
    }

    public RadiusServer getSecondaryRadiusAccountingServer() {
        return secondaryRadiusAccountingServer;
    }

    public void setSecondaryRadiusAccountingServer(RadiusServer secondaryRadiusAccountingServer) {
        this.secondaryRadiusAccountingServer = secondaryRadiusAccountingServer;
    }

    @Override
    public RadiusProfile clone() {
        RadiusProfile result = (RadiusProfile) super.clone();
        if (primaryRadiusAuthServer != null) {
            result.primaryRadiusAuthServer = primaryRadiusAuthServer.clone();
        }
        if (secondaryRadiusAuthServer != null) {
            result.secondaryRadiusAuthServer = secondaryRadiusAuthServer.clone();
        }
        if (primaryRadiusAccountingServer != null) {
            result.primaryRadiusAccountingServer = primaryRadiusAccountingServer.clone();
        }
        if (secondaryRadiusAccountingServer != null) {
            result.secondaryRadiusAccountingServer = secondaryRadiusAccountingServer.clone();
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryRadiusAccountingServer, primaryRadiusAuthServer, secondaryRadiusAccountingServer,
                secondaryRadiusAuthServer);
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
        return Objects.equals(primaryRadiusAccountingServer, other.primaryRadiusAccountingServer)
                && Objects.equals(primaryRadiusAuthServer, other.primaryRadiusAuthServer)
                && Objects.equals(secondaryRadiusAccountingServer, other.secondaryRadiusAccountingServer)
                && Objects.equals(secondaryRadiusAuthServer, other.secondaryRadiusAuthServer);
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadiusProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

}
