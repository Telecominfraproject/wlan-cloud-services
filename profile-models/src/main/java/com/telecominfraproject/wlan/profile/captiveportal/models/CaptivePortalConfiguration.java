package com.telecominfraproject.wlan.profile.captiveportal.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.pegdown.PegDownProcessor;
import org.springframework.util.CollectionUtils;

import com.telecominfraproject.wlan.core.model.entity.InetAddressConstants;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.profile.captiveportal.user.models.TimedAccessUserRecord;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * @author mpreston
 *
 */
public class CaptivePortalConfiguration extends ProfileDetails 
{

    private static final long serialVersionUID = -7369822768587770363L;

    public static final int MAX_CAPTIVE_PORTAL_BODY_TEXT_LENGTH = 32000;
    public static final int MAX_SUCCESS_BODY_TEXT_LENGTH = 2000;
    public static final int MAX_SESSION_TIMEOUT_MINUTES = 1440;
    public static final int MIN_SESSION_TIMEOUT_MINUTES = 0;
    public static final int MAX_EXTERNAL_CP_URL_LENGTH = 256;

    public static final int MAX_WALLED_GARDEN_ENTRY_LENGTH = 256;
    private static final int MAX_PROFILE_NAME_LENGTH = 80;
    private static final int MAX_TITLE_LENGTH = 64;
    private static final int MAX_HEADER_LENGTH = 256;
    private static final int MAX_REDIRECT_URL_LENGTH = 256;
    private static final int MAX_USERS_SAME_CREDENTIALS = 100;
    public static final int MAX_WALLED_GARDEN_ENTRIES = 32;

    private static final String IPV4_DOT_PATTERN = 
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String IPV4_RANGE_DOT_PATTERN = 
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])-" +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static final String WILCARD_URL_2 = "^([\\D]+(.)*\\.[\\D]+(.)*)$";
    private static final String WILCARD_URL_3 = "^([\\D]+(.)*\\.[\\D]+(.)*\\.[\\D]+(.)*)$";
    private static final String WILCARD_URL_4 = "^([\\D]+(.)*\\.[\\D]+(.)*\\.[\\D]+(.)*\\.[\\D]+(.)*)$";
    private static final int MAX_HOSTNAME_PART_LENGTH = 63;

    private static final PegDownProcessor markdownProcessor = new PegDownProcessor();
    private static final Set<String> INVALID_ADDRESSES = new HashSet<>();

    static {
        INVALID_ADDRESSES.add(InetAddressConstants.IPV4_IN_ANY);
        INVALID_ADDRESSES.add(InetAddressConstants.IPV4_IN_LIMITED_BROADCAST);
        INVALID_ADDRESSES.add(InetAddressConstants.IPV4_IN_LOOPBACK);
    }

    
    public static final long LAST_UPDATE_TO_RECORD_DEFINITION = 1449779234000L;
    
    public static final String DEFAULT_UAP = "Please agree to the following terms for using this network:";
    public static final String DEFAULT_SUCCESS_TEXT = "You are now authorized and connected to the network.";
            
    public static final int DEFAULT_SESSION_TIMEOUT_IN_MINUTES = 60;
    public static final int DEFAULT_MAX_USERS_SAME_CREDENTIALS = 3;
    public static final int MAX_USERS = 512;
    public static final String DEFAULT_CONFIG_NAME = "Default";
    
    private String name;
    private String browserTitle;
    private String headerContent;
    private String userAcceptancePolicy;
    private String successPageMarkdownText;
    private String redirectURL;
    private String externalCaptivePortalURL;
    private int sessionTimeoutInMinutes;
    private ManagedFileInfo logoFile;
    private ManagedFileInfo backgroundFile;
    private List<String> walledGardenAllowlist = new ArrayList<>();
    private ManagedFileInfo usernamePasswordFile;
    private CaptivePortalAuthenticationType authenticationType;
    private RadiusAuthenticationMethod radiusAuthMethod;
    private int maxUsersWithSameCredentials;
    private ManagedFileInfo externalPolicyFile;
    private BackgroundPosition backgroundPosition;
    private BackgroundRepeat backgroundRepeat;
    private String radiusServiceName;
    private SessionExpiryType expiryType;
    
    private List<TimedAccessUserRecord> userList = new ArrayList<>();
    private List<MacAllowlistRecord> macAllowList = new ArrayList<>();

    public CaptivePortalConfiguration()
    {
       setName(DEFAULT_CONFIG_NAME);
       setBrowserTitle("Captive Portal");
       setHeaderContent("Captive Portal");
       setUserAcceptancePolicy(DEFAULT_UAP);
       setRedirectURL("");
       setSessionTimeoutInMinutes(DEFAULT_SESSION_TIMEOUT_IN_MINUTES);
       setAuthenticationType(CaptivePortalAuthenticationType.guest);
       setMaxUsersWithSameCredentials(DEFAULT_MAX_USERS_SAME_CREDENTIALS);
       setSuccessPageMarkdownText(DEFAULT_SUCCESS_TEXT);
       setBackgroundPosition(BackgroundPosition.left_top);
       setBackgroundRepeat(BackgroundRepeat.no_repeat);
       setRadiusServiceName(null);
       setRadiusAuthMethod(RadiusAuthenticationMethod.CHAP);
       setExpiryType(SessionExpiryType.time_limited);
    }
    
    @Override
    public ProfileType getProfileType() {
    	return ProfileType.captive_portal;
    }

    public String getBrowserTitle() {
        return browserTitle;
    }

    public void setBrowserTitle(String browserTitle) {
        this.browserTitle = browserTitle;
    }

    public String getHeaderContent() {
        return headerContent;
    }

    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }

    public String getUserAcceptancePolicy() {
        return userAcceptancePolicy;
    }

    public void setUserAcceptancePolicy(String userAcceptancePolicy) {
        this.userAcceptancePolicy = userAcceptancePolicy;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public int getSessionTimeoutInMinutes() {
        return sessionTimeoutInMinutes;
    }

    public void setSessionTimeoutInMinutes(int sessionTimeoutInMinutes) {
        this.sessionTimeoutInMinutes = sessionTimeoutInMinutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public ManagedFileInfo getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(ManagedFileInfo logoFile) {
        this.logoFile = logoFile;
    }

    public ManagedFileInfo getBackgroundFile() {
        return backgroundFile;
    }

    public void setBackgroundFile(ManagedFileInfo backgroundFile) {
        this.backgroundFile = backgroundFile;
    }
    
    public List<String> getWalledGardenAllowlist() {
        return walledGardenAllowlist;
    }

    public void setWalledGardenAllowlist(List<String> walledGardenAllowlist) {
        if (walledGardenAllowlist == null) {
            walledGardenAllowlist = new ArrayList<>();
        }
        this.walledGardenAllowlist = walledGardenAllowlist;
    }

    public CaptivePortalAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(CaptivePortalAuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public int getMaxUsersWithSameCredentials() {
        return maxUsersWithSameCredentials;
    }

    public void setMaxUsersWithSameCredentials(int maxUsersWithSameCredentials) {
        this.maxUsersWithSameCredentials = maxUsersWithSameCredentials;
    }

    public ManagedFileInfo getExternalPolicyFile() {
        return externalPolicyFile;
    }

    public void setExternalPolicyFile(ManagedFileInfo externalPolicyFile) {
        this.externalPolicyFile = externalPolicyFile;
    }
    
    public String getExternalCaptivePortalURL() {
        return externalCaptivePortalURL;
    }

    public void setExternalCaptivePortalURL(String externalCaptivePortalURL) {
        this.externalCaptivePortalURL = externalCaptivePortalURL;
    }
    
    public String getSuccessPageMarkdownText() {
        return successPageMarkdownText;
    }

    public void setSuccessPageMarkdownText(String successPageMarkdownText) {
        this.successPageMarkdownText = successPageMarkdownText;
    }

    public BackgroundPosition getBackgroundPosition() {
        return backgroundPosition;
    }

    public void setBackgroundPosition(BackgroundPosition bgPosition) {
        this.backgroundPosition = bgPosition;
    }

    public BackgroundRepeat getBackgroundRepeat() {
        return backgroundRepeat;
    }

    public void setBackgroundRepeat(BackgroundRepeat bgRepeat) {
        this.backgroundRepeat = bgRepeat;
    }

    public ManagedFileInfo getUsernamePasswordFile() {
        return usernamePasswordFile;
    }

    public void setUsernamePasswordFile(ManagedFileInfo usernamePasswordFile) {
        this.usernamePasswordFile = usernamePasswordFile;
    }
    
    public String getRadiusServiceName() {
        return radiusServiceName;
    }

    public void setRadiusServiceName(String radiusServiceName) {
        this.radiusServiceName = radiusServiceName;
    }

    public RadiusAuthenticationMethod getRadiusAuthMethod() {
        return radiusAuthMethod;
    }

    public void setRadiusAuthMethod(RadiusAuthenticationMethod radiusAuthMethod) {
        this.radiusAuthMethod = radiusAuthMethod;
    }

    public SessionExpiryType getExpiryType() {
        return expiryType;
    }

    public void setExpiryType(SessionExpiryType expiryType) {
        this.expiryType = expiryType;
    }

    public List<TimedAccessUserRecord> getUserList() {
		return userList;
	}

	public void setUserList(List<TimedAccessUserRecord> userList) {
		this.userList = userList;
	}

	public List<MacAllowlistRecord> getMacAllowList() {
		return macAllowList;
	}

	public void setMacAllowList(List<MacAllowlistRecord> macAllowList) {
		this.macAllowList = macAllowList;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(authenticationType, backgroundFile, backgroundPosition, backgroundRepeat,
				browserTitle, expiryType, externalCaptivePortalURL, externalPolicyFile, headerContent, logoFile,
				macAllowList, maxUsersWithSameCredentials, name, radiusAuthMethod, radiusServiceName, redirectURL,
				sessionTimeoutInMinutes, successPageMarkdownText, userAcceptancePolicy, userList, usernamePasswordFile,
				walledGardenAllowlist);
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
		if (!(obj instanceof CaptivePortalConfiguration)) {
			return false;
		}
		CaptivePortalConfiguration other = (CaptivePortalConfiguration) obj;
		return authenticationType == other.authenticationType && Objects.equals(backgroundFile, other.backgroundFile)
				&& backgroundPosition == other.backgroundPosition && backgroundRepeat == other.backgroundRepeat
				&& Objects.equals(browserTitle, other.browserTitle) && expiryType == other.expiryType
				&& Objects.equals(externalCaptivePortalURL, other.externalCaptivePortalURL)
				&& Objects.equals(externalPolicyFile, other.externalPolicyFile)
				&& Objects.equals(headerContent, other.headerContent) && Objects.equals(logoFile, other.logoFile)
				&& Objects.equals(macAllowList, other.macAllowList)
				&& maxUsersWithSameCredentials == other.maxUsersWithSameCredentials && Objects.equals(name, other.name)
				&& radiusAuthMethod == other.radiusAuthMethod
				&& Objects.equals(radiusServiceName, other.radiusServiceName)
				&& Objects.equals(redirectURL, other.redirectURL)
				&& sessionTimeoutInMinutes == other.sessionTimeoutInMinutes
				&& Objects.equals(successPageMarkdownText, other.successPageMarkdownText)
				&& Objects.equals(userAcceptancePolicy, other.userAcceptancePolicy)
				&& Objects.equals(userList, other.userList)
				&& Objects.equals(usernamePasswordFile, other.usernamePasswordFile)
				&& Objects.equals(walledGardenAllowlist, other.walledGardenAllowlist);
	}

	@Override
    public CaptivePortalConfiguration clone() {
        CaptivePortalConfiguration ret = (CaptivePortalConfiguration)super.clone();
        
        if (!CollectionUtils.isEmpty(this.walledGardenAllowlist)) {
            List<String> newWalledGarden = new ArrayList<>();
            for (String str : this.walledGardenAllowlist) {
                newWalledGarden.add(str);
            }
            
            ret.setWalledGardenAllowlist(newWalledGarden);
        }
        
        if(userList!=null) {
        	ret.setUserList(new ArrayList<>(userList));
        }

        if(macAllowList!=null) {
        	ret.setMacAllowList(new ArrayList<>(macAllowList));
        }

        return ret;
    }

    @Override 
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (hasUnsupportedValue(logoFile) || hasUnsupportedValue(backgroundFile)
                || hasUnsupportedValue(usernamePasswordFile)
                || CaptivePortalAuthenticationType.isUnsupported(authenticationType)
                || RadiusAuthenticationMethod.isUnsupported(radiusAuthMethod) || hasUnsupportedValue(externalPolicyFile)
                || BackgroundPosition.isUnsupported(backgroundPosition)
                || BackgroundRepeat.isUnsupported(backgroundRepeat)
                || SessionExpiryType.isUnsupported(getExpiryType()))
        {
            return true;
        }
        return false;
    }
    
    public static void validateCaptivePortalProfile(CaptivePortalConfiguration config) {
        if (config.getWalledGardenAllowlist() != null && config.getWalledGardenAllowlist().size() > MAX_WALLED_GARDEN_ENTRIES) 
        {
            throw new ConfigurationException("Unable to provision a Captive Portal with more than " +
                    MAX_WALLED_GARDEN_ENTRIES +
                    " walled garden entries.");
        }
        
        if (config.getName() != null && config.getName().length() > MAX_PROFILE_NAME_LENGTH) {
            throw new ConfigurationException("Unable to provision a Captive Portal with profile name longer than " +
                    MAX_PROFILE_NAME_LENGTH + " characters.");
        }
        
        if (config.getBrowserTitle() != null && config.getBrowserTitle().length() > MAX_TITLE_LENGTH) {
            throw new ConfigurationException("Unable to provision a Captive Portal with browser title longer than " +
                    MAX_TITLE_LENGTH + " characters.");
        }
        
        if (config.getHeaderContent() != null && config.getHeaderContent().length() > MAX_HEADER_LENGTH) {
            throw new ConfigurationException("Unable to provision a Captive Portal with header content longer than " +
                    MAX_HEADER_LENGTH + " characters.");
        }
        
        // Validate the format of the allowlist entries:
        for (String s : config.getWalledGardenAllowlist()) {
            validateAllowlistEntry(s);
        }
        
        // Make sure the text of the body portions are not too long.
        if (config.getUserAcceptancePolicy() != null) {
            String renderedText;
            synchronized (markdownProcessor) {
                renderedText = markdownProcessor.markdownToHtml(config.getUserAcceptancePolicy());
            }
             
            if (renderedText.length() > MAX_CAPTIVE_PORTAL_BODY_TEXT_LENGTH) {
                throw new ConfigurationException("Unable to provision a Captive Portal with User Acceptance Policy text length larger than "
                        + MAX_CAPTIVE_PORTAL_BODY_TEXT_LENGTH + " characters.");
            }
        }
          
        if (config.getSuccessPageMarkdownText() != null) {
            String renderedText;
            synchronized (markdownProcessor) {
                renderedText = markdownProcessor.markdownToHtml(config.getSuccessPageMarkdownText());
            }
            
            if (renderedText.length() > MAX_SUCCESS_BODY_TEXT_LENGTH) {
                throw new ConfigurationException("Unable to provision a Captive Portal with Success Page text length larger than "
                        + MAX_SUCCESS_BODY_TEXT_LENGTH + " characters.");
            }
        }
        
        // Validate session timeout.
        if (config.getSessionTimeoutInMinutes() < MIN_SESSION_TIMEOUT_MINUTES || config.getSessionTimeoutInMinutes() > MAX_SESSION_TIMEOUT_MINUTES) {
            throw new ConfigurationException("Unable to provision a Captive Portal with Session Timeout outside of range: " + MIN_SESSION_TIMEOUT_MINUTES
                     + " - " + MAX_SESSION_TIMEOUT_MINUTES);
        }
        
        if (config.getExternalCaptivePortalURL() != null && config.getExternalCaptivePortalURL().length() > MAX_EXTERNAL_CP_URL_LENGTH) {
            throw new ConfigurationException("Unable to provision a Captive Portal with an External Portal URL length larger than "
                    + MAX_EXTERNAL_CP_URL_LENGTH + " characters.");
        }
        
        if (config.getRedirectURL() != null && config.getRedirectURL().length() > MAX_REDIRECT_URL_LENGTH) {
            throw new ConfigurationException("Unable to provision a Captive Portal with a redirect URL length larger than "
                    + MAX_REDIRECT_URL_LENGTH + " characters.");
        }
        
        if (config.getMaxUsersWithSameCredentials() > MAX_USERS_SAME_CREDENTIALS) {
            throw new ConfigurationException("Unable to provision a Captive Portal allowing more than "
                    + MAX_USERS_SAME_CREDENTIALS + " clients access with the same credentials at one time.");
        }
    }
    
    private static void validateAllowlistEntry(String entry) {
        if (entry.contains("/")) {
            throw new ConfigurationException("Captive Portal Allowlist entres must not contain the / character");
        }
        if (entry.length() > MAX_WALLED_GARDEN_ENTRY_LENGTH) {
            throw new ConfigurationException("Captive Portal Allowlist entres must not exceed " + MAX_WALLED_GARDEN_ENTRY_LENGTH + " characters");
        }
        
        if (entry.matches(IPV4_DOT_PATTERN)) {
            if (INVALID_ADDRESSES.contains(entry)) {
                throw new ConfigurationException("Captive Portal Allowlist entries may not include: " + INVALID_ADDRESSES);
            }
            
            // Valid IPv4 with dot notation, W.X.Y.Z
            return;
        }
        
        if (entry.matches(IPV4_RANGE_DOT_PATTERN)) {
            String[] entryParts = entry.split("-");
            for (String s : entryParts) {
                if (INVALID_ADDRESSES.contains(s)) {
                    throw new ConfigurationException("Captive Portal Allowlist entries may not include: " + INVALID_ADDRESSES);
                }
            }
            
            // Valid IPv4 range with dot notation A.B.C.D-W.X.Y.Z
            return;
        }
        
        if (!entry.matches(IPV4_DOT_PATTERN) && (entry.matches(WILCARD_URL_2) || entry.matches(WILCARD_URL_3) || entry.matches(WILCARD_URL_4))) {
            char lastChar = entry.charAt(entry.length()-1);
            if (lastChar == '.' || lastChar == '*') {
                throw new ConfigurationException("Captive Portal Allowlist entries may not end with the '.' or '*' character.");
            }
            
            String[] hostnameParts = entry.split("\\.");

            // hostname must contain at least two parts (e.g. google.com)
            if (hostnameParts.length < 2) {
                throw new ConfigurationException("Captive Portal Allowlist entries must have at least 1 sub-domain");
            }

            for (String s : hostnameParts) {
                // hostname labels must be between 1 and 63 characters
                if (s.length() < 1 || s.length() > MAX_HOSTNAME_PART_LENGTH) {
                    throw new ConfigurationException("Captive Portal Allowlist entry sections must not exceed " + MAX_HOSTNAME_PART_LENGTH + " characters.");
                }
                
                if (s.indexOf('*') >= 0 && !"*".equals(s)) {
                    throw new ConfigurationException("Captive Portal Allowlist entry hostname labels must include a '*' wildcard character in combination with other characters");
                }
            }
            
            // It was a valid url.
            return;
        }
        
        throw new DsDataValidationException("'" + entry + "' is an invalid walled garden allowlist format.");
    }    
}
