package com.telecominfraproject.wlan.profile.passpoint.operator.models;

import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class OperatorFriendlyName extends BaseJsonModel implements PushableConfiguration<OperatorFriendlyName> {

    private static final long serialVersionUID = -5799254220064185304L;

    private static final Logger LOG = LoggerFactory.getLogger(OperatorFriendlyName.class);

    private Locale locale;
    private String language;
    private String friendlyName;

    private OperatorFriendlyName() {
        locale = Locale.CANADA;
        language = locale.getISO3Language();
        friendlyName = "defaultFriendlyOperatorName";
    }

    public static OperatorFriendlyName createWithDefaults() {
        return new OperatorFriendlyName();
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(OperatorFriendlyName previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.language = locale.getISO3Language();
        LOG.info("Update locale to {}, auto-updates language to {} for OperatorFriendlyName {}", this.locale, language,
                friendlyName);

    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
        this.language = locale.getISO3Language();
        LOG.info("Update language to {}, auto-updates locale to {} for OperatorFriendlyName {}", this.language, locale,
                friendlyName);
    }

    public String getLanguage() {
        return language;
    }

    public String getFormattedFriendlyName() {
        return this.language + ":" + this.friendlyName;
    }


    public OperatorFriendlyName clone() {
        OperatorFriendlyName returnValue = (OperatorFriendlyName) super.clone();
        returnValue.friendlyName = this.friendlyName;
        returnValue.language = this.language;
        returnValue.locale = this.locale;
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendlyName, language, locale);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OperatorFriendlyName)) {
            return false;
        }
        OperatorFriendlyName other = (OperatorFriendlyName) obj;
        return Objects.equals(friendlyName, other.friendlyName) && Objects.equals(language, other.language)
                && Objects.equals(locale, other.locale);
    }


}
