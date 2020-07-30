package com.telecominfraproject.wlan.adoptionmetrics;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.adoptionmetrics.controller.AdoptionMetricsController;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;

/**
 * @author dtoptygin
 *
 */
@Component
public class AdoptionMetricsServiceLocal implements AdoptionMetricsServiceInterface {

    @Autowired private AdoptionMetricsController controller;
    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsServiceLocal.class);
    
    @Override
    public GenericResponse update(List<ServiceAdoptionMetrics> serviceAdoptionMetricsList) {
        return controller.update(serviceAdoptionMetricsList);
    }
    
    @Override
    public List<ServiceAdoptionMetrics> get(int year, Set<Long> equipmentIds) {
        return controller.get(year, equipmentIds);
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerLocationPerDay(int year, Set<Long> locationIds) {
        return controller.getAggregatePerLocationPerDay(year, locationIds);
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerCustomerPerDay(int year, Set<Integer> customerIds) {
        return controller.getAggregatePerCustomerPerDay(year, customerIds);
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAllPerMonth(int year) {
        return controller.getAllPerMonth(year);
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAllPerWeek(int year) {
        return controller.getAllPerWeek(year);
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAllPerDay(int year) {
        return controller.getAllPerDay(year);
    }
    
    @Override
    public GenericResponse updateUniqueMacs(long timestampMs, int customerId, long locationId, long equipmentId,
            Set<Long> clientMacSet) {
        return controller.updateUniqueMacs(timestampMs, customerId, locationId, equipmentId, clientMacSet);
    }
    
    @Override
    public long getUniqueMacsCount(int year, int dayOfYear, int customerId, long locationId, long equipmentId) {
        return controller.getUniqueMacsCount(year, dayOfYear, customerId, locationId, equipmentId);
    }
    
    @Override
    public GenericResponse deleteUniqueMacs(long createdBeforeTimestampMs, int customerId, long locationId,
            long equipmentId) {
        return controller.deleteUniqueMacs(createdBeforeTimestampMs, customerId, locationId, equipmentId);
    }

}
