package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

/**
 * Ssid metrics for the AP
 * @author ekeddy
 *
 */
public class ApSsidMetrics extends MetricData {

    private static final long serialVersionUID = 6334213644766085284L;

    public static final String TYPE_NAME = ApSsidMetrics.class.getSimpleName();

    private Map<RadioType, List<SsidStatistics>> ssidStats = new EnumMap<>(RadioType.class);

    /* (non-Javadoc)
     * @see com.whizcontrol.servicemetrics.models.MetricData#getType()
     */
    @Override
    public String getType() {
        return TYPE_NAME;
    }

    @JsonIgnore
    public long getSsidStatsCount(RadioType radioType) {
    	List<SsidStatistics> sList = ssidStats.get(radioType);
    	
        return (sList == null) ? 0 : sList.size();
    }

	public Map<RadioType, List<SsidStatistics>> getSsidStats() {
		return ssidStats;
	}

	public void setSsidStats(Map<RadioType, List<SsidStatistics>> ssidStats) {
		this.ssidStats = ssidStats;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ssidStats);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ApSsidMetrics)) {
			return false;
		}
		ApSsidMetrics other = (ApSsidMetrics) obj;
		return Objects.equals(ssidStats, other.ssidStats);
	}
    
	@Override
	public ApSsidMetrics clone() {
		ApSsidMetrics ret = (ApSsidMetrics) super.clone();
		
		if(ssidStats!=null) {
			ret.ssidStats = new EnumMap<>(RadioType.class);
			
			ssidStats.forEach((k, v) -> {
				List<SsidStatistics> sList = new ArrayList<>();
				if(v!=null) {
					v.forEach(s -> sList.add(s.clone()));
				}
				ret.ssidStats.put(k, sList);
			});
		}
		
		return ret;
	}
}
