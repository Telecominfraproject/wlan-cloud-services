package com.telecominfraproject.wlan.profile.passpoint.models;

import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class PasspointDuple extends BaseJsonModel implements PushableConfiguration<PasspointDuple> {

    private static final long serialVersionUID = -9089020959902874433L;
    public static final Locale DEFAULT_LOCALE = Locale.CANADA;
    public static final String DEFAULT_NAME = "Duple Name";
    public static final String DEFAULT_DUPLE_SEPARATOR = ":";
    private static final Logger LOG = LoggerFactory.getLogger(PasspointDuple.class);

    private Locale locale;
    private String dupleIso3Language;
    private String dupleName;
    private String defaultDupleSeparator;

    public PasspointDuple() {
        this.locale = DEFAULT_LOCALE;
        this.dupleIso3Language = this.locale.getISO3Language();
        this.dupleName = DEFAULT_NAME;
        this.defaultDupleSeparator = DEFAULT_DUPLE_SEPARATOR;
    }

    public static PasspointDuple createWithDefaults() {
        return new PasspointDuple();
    };

    public String getDupleIso3Language() {
        return dupleIso3Language;
    }

    public void setDupleIso3Language(String language) {
        this.locale = new Locale(language);
        this.dupleIso3Language = locale.getISO3Language();
    }


    public Locale getLocale() {
        return locale;
    }


    public void setLocale(Locale locale) {
        this.locale = locale;
        this.dupleIso3Language = locale.getISO3Language();
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

    public boolean needsToBeUpdatedOnDevice(PasspointDuple previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    public PasspointDuple clone() {
        PasspointDuple returnValue = (PasspointDuple) super.clone();
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
        if (!(obj instanceof PasspointDuple)) {
            return false;
        }
        PasspointDuple other = (PasspointDuple) obj;
        return Objects.equals(defaultDupleSeparator, other.defaultDupleSeparator)
                && Objects.equals(dupleIso3Language, other.dupleIso3Language)
                && Objects.equals(dupleName, other.dupleName) && Objects.equals(locale, other.locale);
    }


}
