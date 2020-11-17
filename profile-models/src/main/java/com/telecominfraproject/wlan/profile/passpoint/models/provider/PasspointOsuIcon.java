package com.telecominfraproject.wlan.profile.passpoint.models.provider;

import java.util.Locale;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class PasspointOsuIcon extends BaseJsonModel implements PushableConfiguration<PasspointOsuIcon> {

    private static final String ICON_STRING_DELIMITER = ":";

    private static final long serialVersionUID = 6779033020793748979L;

    private static final Locale DEFAULT_ICON_LOCALE = Locale.CANADA;

    private Integer iconWidth;
    private Integer iconHeight;
    private String languageCode;
    private Locale iconLocale;
    public static final String ICON_TYPE = "image/png";
    private String iconName;
    private String filePath;
    private String imageUrl;

    public PasspointOsuIcon() {

    }

    public static PasspointOsuIcon createWithDefaults() {
        PasspointOsuIcon ret = new PasspointOsuIcon();
        ret.iconLocale = DEFAULT_ICON_LOCALE;
        ret.iconWidth = 32;
        ret.iconHeight = 32;
        ret.languageCode = ret.iconLocale.getISO3Language();
        ret.iconName = "icon32.png";
        ret.filePath = "/tmp/icon32.png";
        ret.imageUrl = "https://localhost:9096";
        return ret;
    }


    public Integer getIconWidth() {
        return iconWidth;
    }


    public void setIconWidth(Integer iconWidth) {
        this.iconWidth = iconWidth;
    }


    public Integer getIconHeight() {
        return iconHeight;
    }


    public void setIconHeight(Integer iconHeight) {
        this.iconHeight = iconHeight;
    }


    public String getLanguageCode() {
        return languageCode;
    }


    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }


    public String getIconName() {
        return iconName;
    }


    public void setIconName(String iconName) {
        this.iconName = iconName;
    }


    public String getFilePath() {
        return filePath;
    }


    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public static String getIconStringDelimiter() {
        return ICON_STRING_DELIMITER;
    }


    public static String getIconType() {
        return ICON_TYPE;
    }


    public Locale getIconLocale() {
        return iconLocale;
    }


    public void setIconLocale(Locale iconLocale) {
        this.iconLocale = iconLocale;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointOsuIcon previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    public PasspointOsuIcon clone() {
        PasspointOsuIcon ret = new PasspointOsuIcon();
        ret.filePath = getFilePath();
        ret.iconHeight = getIconHeight();
        ret.iconWidth = getIconWidth();
        ret.iconLocale = getIconLocale();
        ret.iconName = getIconName();
        ret.languageCode = getLanguageCode();
        ret.imageUrl = getImageUrl();
        return ret;
    }


   
    @Override
    public int hashCode() {
        return Objects.hash(filePath, iconHeight, iconLocale, iconName, iconWidth, imageUrl, languageCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PasspointOsuIcon)) {
            return false;
        }
        PasspointOsuIcon other = (PasspointOsuIcon) obj;
        return Objects.equals(filePath, other.filePath) && Objects.equals(iconHeight, other.iconHeight)
                && Objects.equals(iconLocale, other.iconLocale) && Objects.equals(iconName, other.iconName)
                && Objects.equals(iconWidth, other.iconWidth) && Objects.equals(imageUrl, other.imageUrl)
                && Objects.equals(languageCode, other.languageCode);
    }

    public String getHs20IconString() {

        return iconWidth + ICON_STRING_DELIMITER + iconHeight + ICON_STRING_DELIMITER + languageCode
                + ICON_STRING_DELIMITER + ICON_TYPE + ICON_STRING_DELIMITER + iconName + ICON_STRING_DELIMITER
                + filePath;

    }

   

}
