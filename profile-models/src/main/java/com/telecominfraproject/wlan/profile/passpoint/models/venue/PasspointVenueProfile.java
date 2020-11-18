package com.telecominfraproject.wlan.profile.passpoint.models.venue;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;


public class PasspointVenueProfile extends ProfileDetails implements PushableConfiguration<PasspointVenueProfile> {

    private static final long serialVersionUID = 1942358406244382179L;

    private Set<PasspointVenueName> venueNameSet;
    private PasspointVenueTypeAssignment passpointVenueTypeAssignment;

    private PasspointVenueProfile() {
        venueNameSet = new HashSet<>();
        venueNameSet.add(PasspointVenueName.createWithDefaults());

        passpointVenueTypeAssignment = PasspointVenueTypeAssignment.createWithDefaults();
        passpointVenueTypeAssignment.setVenueDescription("Research and Development Facility");
        List<Integer> groupType = PasspointVenueInfo.venueMap.get("Research and Development Facility");
        passpointVenueTypeAssignment.setVenueGroupId(groupType.get(0));
        passpointVenueTypeAssignment.setVenueTypeId(groupType.get(1));

        PasspointVenueName frVenueName = PasspointVenueName.createWithDefaults();
        frVenueName.setLocale(Locale.CANADA_FRENCH);
        frVenueName.setDupleName("Exemple de lieu");
        frVenueName.setVenueUrl("http://www.example.com/info-fra");
        venueNameSet.add(frVenueName);
    }

    public static PasspointVenueProfile createWithDefaults() {
        return new PasspointVenueProfile();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.passpoint_venue;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointVenueProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public Set<PasspointVenueName> getVenueNameSet() {
        return venueNameSet;
    }


    public void setVenueNameSet(Set<PasspointVenueName> venueNameSet) {
        this.venueNameSet = venueNameSet;
    }


    public PasspointVenueTypeAssignment getVenueTypeAssignment() {
        return passpointVenueTypeAssignment;
    }


    public void setVenueTypeAssignment(PasspointVenueTypeAssignment passpointVenueTypeAssignment) {
        this.passpointVenueTypeAssignment = passpointVenueTypeAssignment;
    }

    public PasspointVenueProfile clone() {
        PasspointVenueProfile retValue = (PasspointVenueProfile) super.clone();
        if (venueNameSet != null)
            retValue.venueNameSet = venueNameSet;
        if (passpointVenueTypeAssignment != null)
            retValue.passpointVenueTypeAssignment = passpointVenueTypeAssignment;

        return retValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueNameSet, passpointVenueTypeAssignment);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PasspointVenueProfile)) {
            return false;
        }
        PasspointVenueProfile other = (PasspointVenueProfile) obj;
        return Objects.equals(venueNameSet, other.venueNameSet)
                && Objects.equals(passpointVenueTypeAssignment, other.passpointVenueTypeAssignment);
    }


}
