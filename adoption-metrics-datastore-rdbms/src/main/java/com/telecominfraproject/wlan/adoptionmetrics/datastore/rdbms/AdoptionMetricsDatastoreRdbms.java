package com.telecominfraproject.wlan.adoptionmetrics.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.adoptionmetrics.datastore.AdoptionMetricsDatastore;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class AdoptionMetricsDatastoreRdbms implements AdoptionMetricsDatastore {

    @Autowired AdoptionMetricsDAO adoptionMetricsDAO;

    @Override
    public void update(List<ServiceAdoptionMetrics> serviceAdoptionMetricsList) {
        adoptionMetricsDAO.update(serviceAdoptionMetricsList);        
    }

    @Override
    public List<ServiceAdoptionMetrics> get(int year, Set<Long> equipmentIds) {
        return adoptionMetricsDAO.get(year, equipmentIds);
    }

    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerLocationPerDay(int year, Set<Long> locationIds) {
        return adoptionMetricsDAO.getAggregatePerLocationPerDay(year, locationIds);
    }

    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerCustomerPerDay(int year, Set<Integer> customerIds) {
        return adoptionMetricsDAO.getAggregatePerCustomerPerDay(year, customerIds);
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerMonth(int year) {
        return adoptionMetricsDAO.getAllPerMonth(year);
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerWeek(int year) {
        return adoptionMetricsDAO.getAllPerWeek(year);
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerDay(int year) {
        return adoptionMetricsDAO.getAllPerDay(year);
    }

    @Override
    public void updateUniqueMacs(long timestampMs, int customerId, long locationId, long equipmentId, Set<Long> clientMacSet) {
        adoptionMetricsDAO.updateUniqueMacs(timestampMs, customerId, locationId, equipmentId, clientMacSet);
    }

    @Override
    public long getUniqueMacsCount(int year, int dayOfYear, int customerId, long locationId, long equipmentId) {
        return adoptionMetricsDAO.getUniqueMacsCount(year, dayOfYear, customerId, locationId, equipmentId);
    }

    @Override
    public void deleteUniqueMacs(long createdBeforeTimestampMs, int customerId, long locationId, long equipmentId) {
        adoptionMetricsDAO.deleteUniqueMacs(createdBeforeTimestampMs, customerId, locationId, equipmentId);
    }

    @Override
    public void finalizeUniqueMacsCount(int year, int dayOfYear) {
        adoptionMetricsDAO.finalizeUniqueMacsCount(year, dayOfYear);
    }
}
