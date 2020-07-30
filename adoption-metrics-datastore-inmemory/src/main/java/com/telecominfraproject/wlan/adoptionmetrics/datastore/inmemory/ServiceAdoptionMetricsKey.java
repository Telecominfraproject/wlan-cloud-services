package com.telecominfraproject.wlan.adoptionmetrics.datastore.inmemory;

import java.util.Objects;

import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;

public class ServiceAdoptionMetricsKey {

    private int year;
    private int month;
    private int weekOfYear;
    private int dayOfYear;

    private int customerId;
    private long locationId;
    private long equipmentId;
    
    public ServiceAdoptionMetricsKey(ServiceAdoptionMetrics metrics) {
        this.year = metrics.getYear();
        this.month = metrics.getMonth();
        this.weekOfYear = metrics.getWeekOfYear();
        this.dayOfYear = metrics.getDayOfYear();
        
        this.customerId = metrics.getCustomerId();
        this.locationId = metrics.getLocationId();
        this.equipmentId = metrics.getEquipmentId();
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
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId, dayOfYear, equipmentId, locationId, month, weekOfYear, year);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceAdoptionMetricsKey)) {
            return false;
        }
        ServiceAdoptionMetricsKey other = (ServiceAdoptionMetricsKey) obj;
        return customerId == other.customerId && dayOfYear == other.dayOfYear && equipmentId == other.equipmentId
                && locationId == other.locationId && month == other.month && weekOfYear == other.weekOfYear
                && year == other.year;
    }

    
}
