package com.telecominfraproject.wlan.profile.passpoint.venue.models;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;


public class VenueProfile extends ProfileDetails implements PushableConfiguration<VenueProfile> {

    private static final long serialVersionUID = 1942358406244382179L;

    private Set<VenueName> venueNameSet;
    private VenueTypeAssignment venueTypeAssignment;

    private VenueProfile() {
        venueNameSet = new HashSet<>();
        venueNameSet.add(VenueName.createWithDefaults());

        venueTypeAssignment = VenueTypeAssignment.createWithDefaults();
        venueTypeAssignment.setVenueDescription("Research and Development Facility");
        List<Integer> groupType = VenueInfo.venueMap.get("Research and Development Facility");
        venueTypeAssignment.setVenueGroupId(groupType.get(0));
        venueTypeAssignment.setVenueTypeId(groupType.get(1));

        VenueName frVenueName = VenueName.createWithDefaults();
        frVenueName.setLocale(Locale.CANADA_FRENCH);
        frVenueName.setDupleName("Exemple de lieu");
        frVenueName.setVenueUrl("http://www.example.com/info-fra");
        venueNameSet.add(frVenueName);
    }

    public static VenueProfile createWithDefaults() {
        return new VenueProfile();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.venue;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(VenueProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public Set<VenueName> getVenueNameSet() {
        return venueNameSet;
    }


    public void setVenueNameSet(Set<VenueName> venueNameSet) {
        this.venueNameSet = venueNameSet;
    }


    public VenueTypeAssignment getVenueTypeAssignment() {
        return venueTypeAssignment;
    }


    public void setVenueTypeAssignment(VenueTypeAssignment venueTypeAssignment) {
        this.venueTypeAssignment = venueTypeAssignment;
    }

    public VenueProfile clone() {
        VenueProfile retValue = (VenueProfile) super.clone();
        if (venueNameSet != null)
            retValue.venueNameSet = venueNameSet;
        if (venueTypeAssignment != null)
            retValue.venueTypeAssignment = venueTypeAssignment;

        return retValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueNameSet, venueTypeAssignment);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VenueProfile)) {
            return false;
        }
        VenueProfile other = (VenueProfile) obj;
        return Objects.equals(venueNameSet, other.venueNameSet)
                && Objects.equals(venueTypeAssignment, other.venueTypeAssignment);
    }


}
