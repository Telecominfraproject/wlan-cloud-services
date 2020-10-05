package com.telecominfraproject.wlan.profile.passpoint.venue.models;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.profile.passpoint.models.Hotspot20Duple;


public class VenueName extends Hotspot20Duple {

    private static final long serialVersionUID = -5799254220064185304L;

    private static final Logger LOG = LoggerFactory.getLogger(VenueName.class);

    private String venueUrl;

    
    private VenueName() {
        super();
    }
    
    public static VenueName createWithDefaults() {
        VenueName venueName = new VenueName();
        venueName.setDupleName("Example venue");
        venueName.venueUrl = "http://www.example.com/info-eng";

        return venueName;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(Hotspot20Duple previousVersion) {
        if (this.equals((VenueName) previousVersion)) {
            return false;
        }
        return true;
    }


    public VenueName clone() {
        VenueName returnValue = (VenueName) super.clone();
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
        if (!(obj instanceof VenueName)) {
            return false;
        }
        VenueName other = (VenueName) obj;
        return Objects.equals(venueUrl, other.venueUrl);
    }


}
