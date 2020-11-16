package com.telecominfraproject.wlan.location.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.scheduler.ScheduleSetting;
import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.entity.DayOfTheWeek;

public class LocationDetails extends BaseJsonModel {
    private static final long serialVersionUID = -3348288052201681790L;

    public static final String DEFAULT_TIMEZONE = "US/Eastern";
    
    public static LocationDetails createWithDefaults()
    {
        return new LocationDetails();
    }

    private static Map<DayOfTheWeek, LocationActivityDetails> createDefaultActivityDetails() {

        Map<DayOfTheWeek, LocationActivityDetails> tmp = new EnumMap<>(DayOfTheWeek.class);

        for (DayOfTheWeek day : DayOfTheWeek.values()) {
            LocationActivityDetails details = new LocationActivityDetails();
            details.setBusyTime("13:30");
            details.setQuietTime("3:30");
            details.setTimezone(DEFAULT_TIMEZONE);
            tmp.put(day, details);
        }

        return tmp;
    }

    private CountryCode countryCode;

    private Map<DayOfTheWeek, LocationActivityDetails> dailyActivityDetails;
    
    private ScheduleSetting maintenanceWindow; 

    private boolean rrmEnabled;

    private LocationDetails()
    {
        // use static builders
        this.countryCode = CountryCode.US;
        this.rrmEnabled = true;
        this.dailyActivityDetails = createDefaultActivityDetails();
    }

    @Override
    public LocationDetails clone() {
        LocationDetails result = (LocationDetails) super.clone();
        if (null != this.maintenanceWindow) {
            result.maintenanceWindow = this.maintenanceWindow.clone();
        }
        return result;
    }

    public String getTimezone() {
    	for(DayOfTheWeek day: DayOfTheWeek.values()) {
    		if((dailyActivityDetails.get(day) != null) && (dailyActivityDetails.get(day).getTimezone() != null)) {
    			return dailyActivityDetails.get(day).getTimezone();
    		}
    	}
    	return null;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public Map<DayOfTheWeek, LocationActivityDetails> getDailyRebalancingDetails() {
        return dailyActivityDetails;
    }


    public ScheduleSetting getMaintenanceWindow() {
        return maintenanceWindow;
    }


    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (CountryCode.isUnsupported(countryCode) || hasUnsupportedValue(dailyActivityDetails)
                || hasUnsupportedValue(this.maintenanceWindow)) {
            return true;
        }
        return false;
    }

    public boolean isRrmEnabled() {
        return rrmEnabled;
    }


    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public void setDailyRebalancingDetails(Map<DayOfTheWeek, LocationActivityDetails> dailyRebalancingDetails) {
        this.dailyActivityDetails = dailyRebalancingDetails;
    }

    public void setMaintenanceWindow(ScheduleSetting maintenanceWindow) {
        this.maintenanceWindow = maintenanceWindow;
    }

    public void setRrmEnabled(boolean rrmEnabled) {
        this.rrmEnabled = rrmEnabled;
    }

	@Override
	public int hashCode() {
		return Objects.hash(countryCode, dailyActivityDetails, maintenanceWindow, rrmEnabled);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LocationDetails)) {
			return false;
		}
		LocationDetails other = (LocationDetails) obj;
		return countryCode == other.countryCode && Objects.equals(dailyActivityDetails, other.dailyActivityDetails)
				&& Objects.equals(maintenanceWindow, other.maintenanceWindow) && rrmEnabled == other.rrmEnabled;
	}
    
    
}
