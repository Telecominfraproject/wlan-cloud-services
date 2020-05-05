package com.telecominfraproject.wlan.profile.captiveportal.user.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

public class TimedAccessUserRecord extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = 8290788890856308932L;

    // The firmware caps it at roughly 256 but we realized it didn't work.
    // 130 seems to work (and seems long enough)
    private static final int MAX_USERNAME_LENGTH = 130;
    private static final int MAX_PASSWORD_LENGTH = 130;

    private String username;
    private String password;

    private Long activationTime;
    private Long expirationTime;

    private int numDevices;

    private TimedAccessUserDetails userDetails;
    private List<MacAddress> userMacAddresses = new ArrayList<>();

    private long lastModifiedTimestamp;

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Will do lower case conversion
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = tranformUserName(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Long activationTime) {
        this.activationTime = activationTime;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public TimedAccessUserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(TimedAccessUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public int getNumDevices() {
        return numDevices;
    }

    public void setNumDevices(int numDevices) {
        this.numDevices = numDevices;
    }

    public List<MacAddress> getUserMacAddresses() {
		return userMacAddresses;
	}

	public void setUserMacAddresses(List<MacAddress> userMacAddresses) {
		this.userMacAddresses = userMacAddresses;
	}

	@Override
    public TimedAccessUserRecord clone() {
        TimedAccessUserRecord result = (TimedAccessUserRecord) super.clone();

        if (this.userDetails != null) {
            result.setUserDetails((TimedAccessUserDetails) this.userDetails.clone());
        }
        
        if(this.userMacAddresses!=null) {
        	result.setUserMacAddresses(new ArrayList<>(this.userMacAddresses));
        }

        return result;
    }


    @Override
	public int hashCode() {
		return Objects.hash(activationTime, expirationTime, lastModifiedTimestamp, password, userDetails,
				userMacAddresses, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TimedAccessUserRecord)) {
			return false;
		}
		TimedAccessUserRecord other = (TimedAccessUserRecord) obj;
		return Objects.equals(activationTime, other.activationTime)
				&& Objects.equals(expirationTime, other.expirationTime)
				&& lastModifiedTimestamp == other.lastModifiedTimestamp && Objects.equals(password, other.password)
				&& Objects.equals(userDetails, other.userDetails)
				&& Objects.equals(userMacAddresses, other.userMacAddresses) && Objects.equals(username, other.username);
	}

	public static void validateTimedAccessUserRecord(TimedAccessUserRecord record) {

        if (record.getUsername() == null || record.getUsername().length() == 0) {
            throw new ConfigurationException("Unable to provision User as no username was provided.");
        }

        if (record.getUsername().length() > MAX_USERNAME_LENGTH) {
            throw new ConfigurationException(
                    "Unable to provision User with username larger than " + MAX_USERNAME_LENGTH + " characters.");
        }

        if (record.getPassword() != null && record.getPassword().length() > MAX_PASSWORD_LENGTH) {
            throw new ConfigurationException(
                    "Unable to provision User with password larger than " + MAX_PASSWORD_LENGTH + " characters.");
        }
    }

    public static String tranformUserName(String username) {
        if (null == username) {
            return null;
        }
        return username.toLowerCase();
    }
}
