package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.Arrays;
import java.util.Objects;

import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author ekeddy
 *
 *         Equipment operating information summarized over the last window.
 */
public class OperatingSystemPerformance extends StatusDetails {
    private static final long serialVersionUID = -1015915219847711740L;
    private int numCamiCrashes;
    private long uptimeInSeconds;
    private float avgCpuUtilization;
    private float[] avgCpuPerCore;
    private int avgFreeMemoryKb;
    private long totalAvailableMemoryKb;
    private float avgCpuTemperature;

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.OS_PERFORMANCE;
    }
    
    public int getNumCamiCrashes() {
        return numCamiCrashes;
    }

    public void setNumCamiCrashes(int numCamiCrashes) {
        this.numCamiCrashes = numCamiCrashes;
    }

    public long getUptimeInSeconds() {
        return uptimeInSeconds;
    }

    public void setUptimeInSeconds(long uptimeInSeconds) {
        this.uptimeInSeconds = uptimeInSeconds;
    }

    public float getAvgCpuUtilization() {
        return avgCpuUtilization;
    }

    public void setAvgCpuUtilization(float avgCpuUtilization) {
        this.avgCpuUtilization = avgCpuUtilization;
    }

    public float[] getAvgCpuPerCore() {
        return avgCpuPerCore;
    }

    public void setAvgCpuPerCore(float[] avgCpuPerCore) {
        this.avgCpuPerCore = avgCpuPerCore;
    }

    public int getAvgFreeMemoryKb() {
        return avgFreeMemoryKb;
    }

    public void setAvgFreeMemoryKb(int avgFreeMemoryKb) {
        this.avgFreeMemoryKb = avgFreeMemoryKb;
    }

    public float getAvgCpuTemperature() {
        return avgCpuTemperature;
    }

    public void setAvgCpuTemperature(float avgCpuTemperature) {
        this.avgCpuTemperature = avgCpuTemperature;
    }

    public long getTotalAvailableMemoryKb() {
		return totalAvailableMemoryKb;
	}

	public void setTotalAvailableMemoryKb(long totalAvailableMemoryKb) {
		this.totalAvailableMemoryKb = totalAvailableMemoryKb;
	}

	@Override
    public OperatingSystemPerformance clone() {
        return (OperatingSystemPerformance) super.clone();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(avgCpuPerCore);
		result = prime * result
				+ Objects.hash(avgCpuTemperature, avgCpuUtilization, avgFreeMemoryKb, numCamiCrashes, uptimeInSeconds, totalAvailableMemoryKb);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof OperatingSystemPerformance)) {
			return false;
		}
		OperatingSystemPerformance other = (OperatingSystemPerformance) obj;
		return Arrays.equals(avgCpuPerCore, other.avgCpuPerCore)
				&& Float.floatToIntBits(avgCpuTemperature) == Float.floatToIntBits(other.avgCpuTemperature)
				&& Float.floatToIntBits(avgCpuUtilization) == Float.floatToIntBits(other.avgCpuUtilization)
				&& avgFreeMemoryKb == other.avgFreeMemoryKb && numCamiCrashes == other.numCamiCrashes
				&& uptimeInSeconds == other.uptimeInSeconds
				&& totalAvailableMemoryKb == other.totalAvailableMemoryKb;
	}

}
