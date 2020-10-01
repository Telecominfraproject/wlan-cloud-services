package com.telecominfraproject.wlan.profile.passpoint.venue.models;

import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class VenueName extends BaseJsonModel implements PushableConfiguration<VenueName> {

    private static final long serialVersionUID = -5799254220064185304L;

    private static final Logger LOG = LoggerFactory.getLogger(VenueName.class);

    private Locale locale;
    private String language;
    private String venueName;
    private String venueUrl;

    private VenueName() {
        locale = Locale.CANADA;
        language = locale.getISO3Language();
        venueName = "Example venue";
        venueUrl = "http://www.example.com/info-eng";
    }

    public static VenueName createWithDefaults() {
        return new VenueName();
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(VenueName previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    public void setVenueName(String friendlyName) {
        this.venueName = friendlyName;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.language = locale.getISO3Language();
        LOG.info("Update locale to {}, auto-updates language to {} for VenueName {}", this.locale, language,
                venueName);

    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
        this.language = locale.getISO3Language();
        LOG.info("Update language to {}, auto-updates locale to {} for VenueName {}", this.language, locale,
                venueName);
    }

    public String getLanguage() {
        return language;
    }

    public String getFormattedVenueName() {
        return this.language + ":" + this.venueName;
    }


    public VenueName clone() {
        VenueName returnValue = (VenueName) super.clone();
        returnValue.venueName = this.venueName;
        returnValue.language = this.language;
        returnValue.locale = this.locale;
        returnValue.venueUrl = this.venueUrl;
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(venueName, language, locale);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VenueName)) {
            return false;
        }
        VenueName other = (VenueName) obj;
        return Objects.equals(venueName, other.venueName) && Objects.equals(language, other.language)
                && Objects.equals(locale, other.locale);
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }


}
