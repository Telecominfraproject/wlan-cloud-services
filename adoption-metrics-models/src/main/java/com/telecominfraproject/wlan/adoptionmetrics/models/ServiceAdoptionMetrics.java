package com.telecominfraproject.wlan.adoptionmetrics.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 *
 */
public class ServiceAdoptionMetrics extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
    private int year;
    private int month;
    private int weekOfYear;
    private int dayOfYear;

    private int customerId;
    private long locationId;
    private long equipmentId;

    private long numUniqueConnectedMacs;
    private long numBytesUpstream;
    private long numBytesDownstream;
    
    public ServiceAdoptionMetrics() {
        // for serialization
    }
    
    public ServiceAdoptionMetrics( int year, int month, int weekOfYear, int dayOfYear, int customerId, long locationId, long equipmentId,
            long numUniqueConnectedMacs, long numBytesUpstream, long numBytesDownstream ) {
        this.year = year;
        this.month = month;
        this.weekOfYear = weekOfYear;
        this.dayOfYear = dayOfYear;

        this.customerId = customerId;
        this.locationId = locationId;
        this.equipmentId = equipmentId;

        this.numUniqueConnectedMacs = numUniqueConnectedMacs;
        this.numBytesUpstream = numBytesUpstream;
        this.numBytesDownstream = numBytesDownstream;

    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getNumUniqueConnectedMacs() {
        return numUniqueConnectedMacs;
    }

    public void setNumUniqueConnectedMacs(long numUniqueConnectedMacs) {
        this.numUniqueConnectedMacs = numUniqueConnectedMacs;
    }

    public long getNumBytesUpstream() {
        return numBytesUpstream;
    }

    public void setNumBytesUpstream(long numBytesUpstream) {
        this.numBytesUpstream = numBytesUpstream;
    }

    public long getNumBytesDownstream() {
        return numBytesDownstream;
    }

    public void setNumBytesDownstream(long numBytesDownstream) {
        this.numBytesDownstream = numBytesDownstream;
    }

    @Override
    public ServiceAdoptionMetrics clone() {
    	ServiceAdoptionMetrics ret = (ServiceAdoptionMetrics) super.clone();
    	
    	return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, dayOfYear, equipmentId, locationId, month, numBytesDownstream, numBytesUpstream,
                numUniqueConnectedMacs, weekOfYear, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceAdoptionMetrics)) {
            return false;
        }
        ServiceAdoptionMetrics other = (ServiceAdoptionMetrics) obj;
        return customerId == other.customerId && dayOfYear == other.dayOfYear && equipmentId == other.equipmentId
                && locationId == other.locationId && month == other.month
                && numBytesDownstream == other.numBytesDownstream && numBytesUpstream == other.numBytesUpstream
                && numUniqueConnectedMacs == other.numUniqueConnectedMacs && weekOfYear == other.weekOfYear
                && year == other.year;
    }
    
    public void addCounters(ServiceAdoptionMetrics other) {
        this.numBytesDownstream += other.numBytesDownstream;
        this.numBytesUpstream += other.numBytesUpstream;
        this.numUniqueConnectedMacs += other.numUniqueConnectedMacs;
    }
}
