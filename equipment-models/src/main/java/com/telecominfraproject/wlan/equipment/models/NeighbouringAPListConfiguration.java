package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class NeighbouringAPListConfiguration extends BaseJsonModel {
    private static final long serialVersionUID = -3448137750866017623L;
    private Integer minSignal;
    private Integer maxAps;

    private NeighbouringAPListConfiguration() {
        // serialization
        minSignal = -85;
        maxAps = 25;
    }

    public Integer getMinSignal() {
        return minSignal;
    }

    public Integer getMaxAps() {
        return maxAps;
    }

    public void setMinSignal(Integer minSignal) {
        this.minSignal = minSignal;
    }

    public void setMaxAps(Integer maxAps) {
        this.maxAps = maxAps;
    }

    @Override
	public int hashCode() {
		return Objects.hash(maxAps, minSignal);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NeighbouringAPListConfiguration)) {
			return false;
		}
		NeighbouringAPListConfiguration other = (NeighbouringAPListConfiguration) obj;
		return Objects.equals(maxAps, other.maxAps) && Objects.equals(minSignal, other.minSignal);
	}

	/**
     * Our static builder
     * 
     * @return
     */
    public static NeighbouringAPListConfiguration createDefault() {
        return new NeighbouringAPListConfiguration();
    }

}
