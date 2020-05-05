package com.telecominfraproject.wlan.profile.captiveportal.user.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

/**
 * User credentials that are only valid for a specified time range.
 * @author mpreston
 *
 */
public class TimedAccessUserDetails extends BaseJsonModel 
{
    /**
     * 
     */
    private static final long serialVersionUID = 2654678301971968110L;
    
    private static final int MAX_FIRSTNAME_LENGTH = 50;
    private static final int MAX_LASTNAME_LENGTH = 50;
    
    private String firstName;
    private String lastName;
    private boolean passwordNeedsReset;
    
    public TimedAccessUserDetails() 
    {
        passwordNeedsReset = false;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public boolean isPasswordNeedsReset() {
        return passwordNeedsReset;
    }

    public void setPasswordNeedsReset(boolean passwordNeedsReset) {
        this.passwordNeedsReset = passwordNeedsReset;
    }

    @Override 
    public TimedAccessUserDetails clone() {
        TimedAccessUserDetails ret = (TimedAccessUserDetails)super.clone();
        
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimedAccessUserDetails other = (TimedAccessUserDetails) obj;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        return true;
    }
    
    public static void validateTimedAccessUserDetails(TimedAccessUserDetails details) 
    {
        if(details != null)
        {
            if (details.getFirstName() != null && details.getFirstName().length() > MAX_FIRSTNAME_LENGTH) {
                throw new ConfigurationException("Unable to provision User with first name larger than "
                        + MAX_FIRSTNAME_LENGTH + " characters.");
            }

            if (details.getLastName() != null && details.getLastName().length() > MAX_LASTNAME_LENGTH) {
                throw new ConfigurationException("Unable to provision User with last name larger than "
                        + MAX_LASTNAME_LENGTH + " characters.");
            }
        }
    }
}
