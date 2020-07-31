package com.telecominfraproject.wlan.adoptionmetrics.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;

/**
 * @author dtoptygin
 *
 */
public interface AdoptionMetricsDatastore {

    /**
     * @param serviceAdoptionMetricsList - list of objects representing updates to the ServiceAdoptionMetrics.
     * <br>If an incoming entry is not found in the DB, then the entry will be inserted into the DB
     * <br>If an incoming entry is found in the DB, then the incoming numBytesUpstream/numBytesDownstream will be added to the values in the DB, 
     * and the incoming numUniqueConnectedMacs will overwrite the value in the DB 
     */
    void update(List<ServiceAdoptionMetrics> serviceAdoptionMetricsList);
    
    List<ServiceAdoptionMetrics> get(int year, Set<Long> equipmentIds);
    List<ServiceAdoptionMetrics> getAggregatePerLocationPerDay(int year, Set<Long> locationIds);
    List<ServiceAdoptionMetrics> getAggregatePerCustomerPerDay(int year, Set<Integer> customerIds);
    List<ServiceAdoptionMetrics> getAllPerMonth(int year);
    List<ServiceAdoptionMetrics> getAllPerWeek(int year);
    List<ServiceAdoptionMetrics> getAllPerDay(int year);

    default List<ServiceAdoptionMetrics> getAllPerMonth() { return getAllPerMonth(0); }
    default List<ServiceAdoptionMetrics> getAllPerWeek() { return getAllPerWeek(0); }
    default List<ServiceAdoptionMetrics> getAllPerDay() { return getAllPerDay(0); }

    void updateUniqueMacs(long timestampMs, int customerId, long locationId, long equipmentId, Set<Long> clientMacSet);
    long getUniqueMacsCount(int year, int dayOfYear, int customerId, long locationId, long equipmentId);

    void deleteUniqueMacs(long createdBeforeTimestampMs, int customerId, long locationId, long equipmentId);
    
    /**
     * For the given day update all the adoption metrics with the exact count of unique mac addresses seen.
     * @param year
     * @param dayOfYear
     */
    void finalizeUniqueMacsCount(int year, int dayOfYear);
}
