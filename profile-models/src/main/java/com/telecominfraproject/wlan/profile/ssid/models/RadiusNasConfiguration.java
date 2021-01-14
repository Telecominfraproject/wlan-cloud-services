/**
 * 
 */
package com.telecominfraproject.wlan.profile.ssid.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author mikehansen
 *
 */
public class RadiusNasConfiguration extends BaseJsonModel
        implements PushableConfiguration<RadiusNasConfiguration> {

    private static final long serialVersionUID = 1445736724292031168L;
    NasIdType nasClientId;
    NasIpType nasClientIp;
    String userDefinedNasId;
    String userDefinedNasIp;
    String operatorId;

    private RadiusNasConfiguration() {
        nasClientId = NasIdType.DEFAULT;
        nasClientIp = NasIpType.WAN_IP;
    }

    public static RadiusNasConfiguration createWithDefaults() {
        return new RadiusNasConfiguration();
    }
    
    /**
     * @return the nasClientId
     */
    public NasIdType getNasClientId() {
        return nasClientId;
    }

    /**
     * @param nasClientId the nasClientId to set
     */
    public void setNasClientId(NasIdType nasClientId) {
        this.nasClientId = nasClientId;
    }

    /**
     * @return the nasClientIp
     */
    public NasIpType getNasClientIp() {
        return nasClientIp;
    }

    /**
     * @param nasClientIp the nasClientIp to set
     */
    public void setNasClientIp(NasIpType nasClientIp) {
        this.nasClientIp = nasClientIp;
    }

    /**
     * @return the userDefinedNasId
     */
    public String getUserDefinedNasId() {
        return userDefinedNasId;
    }

    /**
     * @param userDefinedNasId the userDefinedNasId to set
     */
    public void setUserDefinedNasId(String userDefinedNasId) {
        this.userDefinedNasId = userDefinedNasId;
    }

    /**
     * @return the userDefinedNasIp
     */
    public String getUserDefinedNasIp() {
        return userDefinedNasIp;
    }

    /**
     * @param userDefinedNasIp the userDefinedNasIp to set
     */
    public void setUserDefinedNasIp(String userDefinedNasIp) {
        this.userDefinedNasIp = userDefinedNasIp;
    }

    /**
     * @return the operatorId
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId the operatorId to set
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadiusNasConfiguration previousVersion) {
        // TODO Auto-generated method stub
        return !Objects.equals(this, previousVersion);
    }
    
    @Override
    public RadiusNasConfiguration clone() {
        RadiusNasConfiguration returnValue = (RadiusNasConfiguration)super.clone();       
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nasClientId, nasClientIp, operatorId, userDefinedNasId, userDefinedNasIp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadiusNasConfiguration other = (RadiusNasConfiguration) obj;
        return nasClientId == other.nasClientId && nasClientIp == other.nasClientIp
                && Objects.equals(operatorId, other.operatorId)
                && Objects.equals(userDefinedNasId, other.userDefinedNasId)
                && Objects.equals(userDefinedNasIp, other.userDefinedNasIp);
    }
    
    

}
