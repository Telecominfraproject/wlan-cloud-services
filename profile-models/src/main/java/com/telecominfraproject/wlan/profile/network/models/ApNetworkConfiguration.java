package com.telecominfraproject.wlan.profile.network.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualString;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;


/**
 * @author dtoptygin
 *
 * NOTE FROM ERIK:
 *    We're using the "equals" to see if changes need to be pushed to the devices. Make sure you update the "equals" if 
 *    you add any new parameters! 
 *
 */
public class ApNetworkConfiguration extends CommonNetworkConfiguration implements PushableConfiguration<ApNetworkConfiguration>
{
    private static final long serialVersionUID = 3703139686764951722L;
    public static final String NETWORK_CONFIG_VERSION = "AP-1"; 
    public static final String DEFAULT_NTP_SERVER = "pool.ntp.org";
    public static final Boolean DEFAULT_SYNTHETIC_CLIENT_ENABLED = Boolean.TRUE;
    public static final Boolean DEFAULT_LED_CONTROL_ENABLED = Boolean.TRUE;
    public static final Boolean DEFAULT_EQUIPMENT_DISCOVERY_ENABLED = Boolean.FALSE;
    
    /**
     * Added as a profile level setting that can be used to provide configuration to a group
     * of APs. There is an equivalent 'radioMap' parameter on each AP's ApElementConfiguration
     * which can be used to override the parameter values set here.
     */
    private Map<RadioType, RadioProfileConfiguration> radioMap;

    /*
     * Don't use the constructor, use the "create" methods.
     */
    private ApNetworkConfiguration() 
    {
        
        super(EquipmentType.AP);
        setNetworkConfigVersion(NETWORK_CONFIG_VERSION);
        setEquipmentType(EquipmentType.AP);
        setVlanNative(true);
        setNtpServer(new AutoOrManualString(true, DEFAULT_NTP_SERVER));
        setSyntheticClientEnabled(DEFAULT_SYNTHETIC_CLIENT_ENABLED);
        setLedControlEnabled(DEFAULT_LED_CONTROL_ENABLED);
        setEquipmentDiscovery(DEFAULT_EQUIPMENT_DISCOVERY_ENABLED);
        
        // initialize the profile level radio map settings parameter
        radioMap = new EnumMap<>(RadioType.class);
        for (RadioType type : RadioType.validValues()) {
            radioMap.put(type, RadioProfileConfiguration.createWithDefaults(type));
        }
    }
    
    @Override
    public ApNetworkConfiguration clone() {
        ApNetworkConfiguration ret = (ApNetworkConfiguration) super.clone();
        
        if (radioMap != null) {
            ret.radioMap = new EnumMap<>(RadioType.class);

            for (Map.Entry<RadioType, RadioProfileConfiguration> entry : radioMap.entrySet()) {
                ret.radioMap.put(entry.getKey(), entry.getValue().clone());
            }
        }
        
        return ret;
    }

    /**
     * Our testable initializer
     * 
     * @return
     */
    public static ApNetworkConfiguration createWithDefaults()
    {
       return new ApNetworkConfiguration();
    }
    
    
   @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((radioMap == null) ? 0 : radioMap.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApNetworkConfiguration other = (ApNetworkConfiguration) obj;
        if (radioMap == null) {
            if (other.radioMap != null)
                return false;
        } else if (!radioMap.equals(other.radioMap))
            return false;
        return true;
    }

@Override
   public boolean needsToBeUpdatedOnDevice(ApNetworkConfiguration previousVersion) 
   {
      // Essentially, if anything's changed, we need to update to the device 
      return !equals(previousVersion);
   }
    
   @Override
   public boolean hasUnsupportedValue() {
       if (super.hasUnsupportedValue()) {
           return true;
       }
       if (hasUnsupportedValue(radioMap.values())) {
           return true;
       }
       return false;
   }
   
   public Map<RadioType, RadioProfileConfiguration> getRadioMap() {
       return radioMap;
   }
   
   public void setRadioMap(Map<RadioType, RadioProfileConfiguration> radioMap) {
       this.radioMap = radioMap;
   }
   
   public static void main(String[] args) {
    ApNetworkConfiguration a = ApNetworkConfiguration.createWithDefaults();
    System.out.println(a);
}
}
