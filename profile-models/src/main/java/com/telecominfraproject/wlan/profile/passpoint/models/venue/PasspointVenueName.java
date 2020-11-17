package com.telecominfraproject.wlan.profile.passpoint.models.venue;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.profile.passpoint.models.PasspointDuple;


public class PasspointVenueName extends PasspointDuple {

    private static final long serialVersionUID = -5799254220064185304L;

    private static final Logger LOG = LoggerFactory.getLogger(PasspointVenueName.class);

    private String venueUrl;

    
    private PasspointVenueName() {
        super();
    }
    
    public static PasspointVenueName createWithDefaults() {
        PasspointVenueName passpointVenueName = new PasspointVenueName();
        passpointVenueName.setDupleName("Example passpoint_venue");
        passpointVenueName.venueUrl = "http://www.example.com/info-eng";

        return passpointVenueName;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointDuple previousVersion) {
        if (this.equals((PasspointVenueName) previousVersion)) {
            return false;
        }
        return true;
    }


    public PasspointVenueName clone() {
        PasspointVenueName returnValue = (PasspointVenueName) super.clone();
        returnValue.venueUrl = this.venueUrl;
        return returnValue;
    }


    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(venueUrl);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof PasspointVenueName)) {
            return false;
        }
        PasspointVenueName other = (PasspointVenueName) obj;
        return Objects.equals(venueUrl, other.venueUrl);
    }


}
