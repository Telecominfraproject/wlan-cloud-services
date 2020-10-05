package com.telecominfraproject.wlan.profile.passpoint.models;

import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class Hotspot20Duple extends BaseJsonModel implements PushableConfiguration<Hotspot20Duple> {

    private static final long serialVersionUID = -9089020959902874433L;
    public static final Locale DEFAULT_LOCALE = Locale.CANADA;
    public static final String DEFAULT_NAME = "Duple Name";
    private static final Logger LOG = LoggerFactory.getLogger(Hotspot20Duple.class);

    private Locale locale;
    private String dupleIso3Language;
    private String dupleName;
    private String defaultDupleSeparator;

    private Hotspot20Duple() {

    }

    public static Hotspot20Duple createWithDefaults() {
        return new Hotspot20Duple();

    };

    public String getDupleIso3Language() {
        return dupleIso3Language;
    }

    public void setDupleIso3Language(String language) {
        this.locale = new Locale(language);
        this.dupleIso3Language = locale.getISO3Language();
        LOG.info("Update dupleIso3Language to {}, auto-updates locale to {} for Hotspot20Duple {}",
                this.dupleIso3Language, locale, dupleName);
    }


    public Locale getLocale() {
        return locale;
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
        this.dupleIso3Language = locale.getISO3Language();
        LOG.info("Update locale to {}, auto-updates dupleIso3Language to {} for Hotspot20Duple {}", this.locale,
                dupleIso3Language, dupleName);
    }


    public String getDupleName() {
        return dupleName;
    }

    public void setDupleName(String friendlyName) {
        this.dupleName = friendlyName;
    }


    public String getDefaultDupleSeparator() {
        return defaultDupleSeparator;
    }


    public void setDefaultDupleSeparator(String defaultDupleSeparator) {
        this.defaultDupleSeparator = defaultDupleSeparator;
    }

    public String getAsDuple() {
        return this.dupleIso3Language + defaultDupleSeparator + this.dupleName;
    }

    public boolean needsToBeUpdatedOnDevice(Hotspot20Duple previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    public Hotspot20Duple clone() {
        Hotspot20Duple returnValue = (Hotspot20Duple) super.clone();
        returnValue.dupleName = this.dupleName;
        returnValue.dupleIso3Language = this.dupleIso3Language;
        returnValue.locale = this.locale;
        returnValue.defaultDupleSeparator = this.defaultDupleSeparator;
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultDupleSeparator, dupleIso3Language, dupleName, locale);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hotspot20Duple)) {
            return false;
        }
        Hotspot20Duple other = (Hotspot20Duple) obj;
        return Objects.equals(defaultDupleSeparator, other.defaultDupleSeparator)
                && Objects.equals(dupleIso3Language, other.dupleIso3Language)
                && Objects.equals(dupleName, other.dupleName) && Objects.equals(locale, other.locale);
    }


}
