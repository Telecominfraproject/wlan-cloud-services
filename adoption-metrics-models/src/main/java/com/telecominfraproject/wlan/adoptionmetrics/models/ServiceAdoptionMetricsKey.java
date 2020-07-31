package com.telecominfraproject.wlan.adoptionmetrics.models;

import java.util.Calendar;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.utils.DateTimeUtils;

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
    
    public ServiceAdoptionMetricsKey(long timestampMs,  int customerId, long locationId, long equipmentId) {
        
        Calendar calendar = Calendar.getInstance(DateTimeUtils.TZ_GMT);
        calendar.setTimeInMillis(timestampMs);
        
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        this.dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        this.customerId = customerId;
        this.locationId = locationId;
        this.equipmentId = equipmentId;

    }

    public long getTimestampMs() {

        Calendar calendar = Calendar.getInstance(DateTimeUtils.TZ_GMT);
       
        calendar.set(Calendar.YEAR, this.year);
        calendar.set(Calendar.DAY_OF_YEAR, this.dayOfYear);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        return calendar.getTimeInMillis();
        
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
