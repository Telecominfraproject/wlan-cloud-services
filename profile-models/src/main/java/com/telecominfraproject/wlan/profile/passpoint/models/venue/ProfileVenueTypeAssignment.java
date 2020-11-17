package com.telecominfraproject.wlan.profile.passpoint.models.venue;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class ProfileVenueTypeAssignment extends BaseJsonModel {

    private static final long serialVersionUID = -7846203730973163371L;

    private String venueDescription;
    private int venueGroupId;
    private int venueTypeId;


    private ProfileVenueTypeAssignment() {

    }

    public static ProfileVenueTypeAssignment createWithDefaults() {
        return new ProfileVenueTypeAssignment();
    }


    public String getVenueDescription() {
        return venueDescription;
    }


    public void setVenueDescription(String venueDescription) {
        this.venueDescription = venueDescription;
    }


    public int getVenueGroupId() {
        return venueGroupId;
    }


    public void setVenueGroupId(int venueGroupId) {
        this.venueGroupId = venueGroupId;
    }


    public int getVenueTypeId() {
        return venueTypeId;
    }


    public void setVenueTypeId(int venueTypeId) {
        this.venueTypeId = venueTypeId;
    }

    public ProfileVenueTypeAssignment clone() {
        ProfileVenueTypeAssignment ret = (ProfileVenueTypeAssignment) super.clone();
        ret.venueDescription = venueDescription;
        ret.venueGroupId = venueGroupId;
        ret.venueTypeId = venueTypeId;
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueDescription, venueGroupId, venueTypeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProfileVenueTypeAssignment)) {
            return false;
        }
        ProfileVenueTypeAssignment other = (ProfileVenueTypeAssignment) obj;
        return Objects.equals(venueDescription, other.venueDescription) && venueGroupId == other.venueGroupId
                && venueTypeId == other.venueTypeId;
    }

    
    
}