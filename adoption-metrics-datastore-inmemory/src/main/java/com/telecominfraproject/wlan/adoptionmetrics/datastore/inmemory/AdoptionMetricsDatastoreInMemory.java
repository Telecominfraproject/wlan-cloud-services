package com.telecominfraproject.wlan.adoptionmetrics.datastore.inmemory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.adoptionmetrics.datastore.AdoptionMetricsDatastore;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetricsKey;
import com.telecominfraproject.wlan.adoptionmetrics.models.UniqueMacsKey;
import com.telecominfraproject.wlan.core.model.utils.DateTimeUtils;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class AdoptionMetricsDatastoreInMemory extends BaseInMemoryDatastore implements AdoptionMetricsDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsDatastoreInMemory.class);

    private static final Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> idToServiceAdoptionMetricsMap = new ConcurrentHashMap<>();

    private static final Map<UniqueMacsKey, Set<Long>> idToUniqueMacsMap = new ConcurrentHashMap<>();

    @Override
    public void update(List<ServiceAdoptionMetrics> serviceAdoptionMetricsList) {
        LOG.debug("Updating adoption metrics: {}", serviceAdoptionMetricsList);
        
        serviceAdoptionMetricsList.forEach(m -> {
            ServiceAdoptionMetricsKey key = new ServiceAdoptionMetricsKey(m);
            ServiceAdoptionMetrics existing = idToServiceAdoptionMetricsMap.get(key);
            if(existing == null) {
                idToServiceAdoptionMetricsMap.put(key, m.clone());
            } else {
                existing.setNumBytesDownstream(existing.getNumBytesDownstream() + m.getNumBytesDownstream());
                existing.setNumBytesUpstream(existing.getNumBytesUpstream() + m.getNumBytesUpstream());
                existing.setNumUniqueConnectedMacs(m.getNumUniqueConnectedMacs());
            }
        });
    }
    
    @Override
    public List<ServiceAdoptionMetrics> get(int year, Set<Long> equipmentIds) {
        
        List<ServiceAdoptionMetrics> ret = new ArrayList<>();
        idToServiceAdoptionMetricsMap.values().forEach(m -> {
            if( m.getYear() == year && equipmentIds.contains(m.getEquipmentId()) ) {
                ret.add(m.clone());
            } 
        });

        return ret;
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerLocationPerDay(int year, Set<Long> locationIds) {
        Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> aggregatedMap = new HashMap<>();

        idToServiceAdoptionMetricsMap.values().forEach(m -> {
            if( m.getYear() == year && locationIds.contains(m.getLocationId()) ) {                
                ServiceAdoptionMetricsKey key = new ServiceAdoptionMetricsKey(m);
                key.setEquipmentId(0); //we're grouping per location, so ignore equipmentId
                ServiceAdoptionMetrics aggregatedValue = aggregatedMap.get(key);
                if(aggregatedValue == null) {
                    aggregatedValue = m.clone();
                    aggregatedValue.setEquipmentId(0); //we're grouping per location, so ignore equipmentId
                    aggregatedMap.put(key, aggregatedValue);
                } else {
                    aggregatedValue.addCounters(m);
                }

            } 
        });

        return new ArrayList<>(aggregatedMap.values());
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerCustomerPerDay(int year, Set<Integer> customerIds) {
        Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> aggregatedMap = new HashMap<>();

        idToServiceAdoptionMetricsMap.values().forEach(m -> {
            if( m.getYear() == year && customerIds.contains(m.getCustomerId()) ) {                
                ServiceAdoptionMetricsKey key = new ServiceAdoptionMetricsKey(m);
                key.setLocationId(0); //we're grouping per customer, so ignore locationId
                key.setEquipmentId(0); //we're grouping per customer, so ignore equipmentId
                ServiceAdoptionMetrics aggregatedValue = aggregatedMap.get(key);
                if(aggregatedValue == null) {
                    aggregatedValue = m.clone();
                    aggregatedValue.setLocationId(0); //we're grouping per customer, so ignore locationId
                    aggregatedValue.setEquipmentId(0); //we're grouping per customer, so ignore equipmentId
                    aggregatedMap.put(key, aggregatedValue);
                } else {
                    aggregatedValue.addCounters(m);
                }

            } 
        });

        return new ArrayList<>(aggregatedMap.values());
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerMonth(int year) {
        Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> aggregatedMap = new HashMap<>();

        idToServiceAdoptionMetricsMap.values().forEach(m -> {
            if( m.getYear() == year || year == 0 ) {                
                ServiceAdoptionMetricsKey key = new ServiceAdoptionMetricsKey(m);
                key.setCustomerId(0); //we're grouping for all, so ignore customerId
                key.setLocationId(0); //we're grouping for all, so ignore locationId
                key.setEquipmentId(0); //we're grouping for all, so ignore equipmentId
                key.setWeekOfYear(-1); //grouping per month
                key.setDayOfYear(-1); //grouping per month
                ServiceAdoptionMetrics aggregatedValue = aggregatedMap.get(key);
                if(aggregatedValue == null) {
                    aggregatedValue = m.clone();
                    aggregatedValue.setCustomerId(0); //we're grouping for all, so ignore customerId
                    aggregatedValue.setLocationId(0); //we're grouping for all, so ignore locationId
                    aggregatedValue.setEquipmentId(0); //we're grouping for all, so ignore equipmentId
                    aggregatedValue.setWeekOfYear(-1); //grouping per month
                    aggregatedValue.setDayOfYear(-1); //grouping per month
                    aggregatedMap.put(key, aggregatedValue);
                } else {
                    aggregatedValue.addCounters(m);
                }

            } 
        });

        return new ArrayList<>(aggregatedMap.values());
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerWeek(int year) {
        Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> aggregatedMap = new HashMap<>();

        idToServiceAdoptionMetricsMap.values().forEach(m -> {
            if( m.getYear() == year || year == 0 ) {                
                ServiceAdoptionMetricsKey key = new ServiceAdoptionMetricsKey(m);
                key.setCustomerId(0); //we're grouping for all, so ignore customerId
                key.setLocationId(0); //we're grouping for all, so ignore locationId
                key.setEquipmentId(0); //we're grouping for all, so ignore equipmentId
                key.setDayOfYear(-1); //grouping per week
                ServiceAdoptionMetrics aggregatedValue = aggregatedMap.get(key);
                if(aggregatedValue == null) {
                    aggregatedValue = m.clone();
                    aggregatedValue.setCustomerId(0); //we're grouping for all, so ignore customerId
                    aggregatedValue.setLocationId(0); //we're grouping for all, so ignore locationId
                    aggregatedValue.setEquipmentId(0); //we're grouping for all, so ignore equipmentId
                    aggregatedValue.setDayOfYear(-1); //grouping per week
                    aggregatedMap.put(key, aggregatedValue);
                } else {
                    aggregatedValue.addCounters(m);
                }

            } 
        });

        return new ArrayList<>(aggregatedMap.values());
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerDay(int year) {
        Map<ServiceAdoptionMetricsKey, ServiceAdoptionMetrics> aggregatedMap = new HashMap<>();

        idToServiceAdoptionMetricsMap.values().forEach(m -> {
            if( m.getYear() == year || year == 0 ) {                
                ServiceAdoptionMetricsKey key = new ServiceAdoptionMetricsKey(m);
                key.setCustomerId(0); //we're grouping for all, so ignore customerId
                key.setLocationId(0); //we're grouping for all, so ignore locationId
                key.setEquipmentId(0); //we're grouping for all, so ignore equipmentId
                ServiceAdoptionMetrics aggregatedValue = aggregatedMap.get(key);
                if(aggregatedValue == null) {
                    aggregatedValue = m.clone();
                    aggregatedValue.setCustomerId(0); //we're grouping for all, so ignore customerId
                    aggregatedValue.setLocationId(0); //we're grouping for all, so ignore locationId
                    aggregatedValue.setEquipmentId(0); //we're grouping for all, so ignore equipmentId
                    aggregatedMap.put(key, aggregatedValue);
                } else {
                    aggregatedValue.addCounters(m);
                }

            } 
        });

        return new ArrayList<>(aggregatedMap.values());
    }

    @Override
    public void updateUniqueMacs(long timestampMs, int customerId, long locationId, long equipmentId, Set<Long> clientMacSet) {
        
        LOG.debug("Updating unique macs for {} {} {} {} {} ", timestampMs, customerId, locationId, equipmentId, clientMacSet);

        UniqueMacsKey key = new UniqueMacsKey();
        key.setTimestampMs(timestampMs);
        key.setCustomerId(customerId);
        key.setLocationId(locationId);
        key.setEquipmentId(equipmentId);
        
        Set<Long> existingSet = idToUniqueMacsMap.get(key);
        if(existingSet == null) {
            existingSet = Collections.synchronizedSet(new HashSet<>());
            idToUniqueMacsMap.put(key, existingSet);
        }
        
        existingSet.addAll(clientMacSet);
        
    }

    @Override
    public long getUniqueMacsCount(int year, int dayOfYear, int customerId, long locationId, long equipmentId) {
        Set<Long> ret = new HashSet<>();
        
        idToUniqueMacsMap.forEach((k, v) -> {
            //build up a set of unique Macs for the specified day from the fragments stored in different entries in idToUniqueMacsMap   
            if(k.getCustomerId() == customerId && k.getLocationId() == locationId && k.getEquipmentId() == equipmentId) {
                long ts = k.getTimestampMs();
                Calendar calendar = Calendar.getInstance(DateTimeUtils.TZ_GMT);
                calendar.setTimeInMillis(ts);
                int yr = calendar.get(Calendar.YEAR);
                int doy = calendar.get(Calendar.DAY_OF_YEAR);

                if(yr == year && doy == dayOfYear) {
                    ret.addAll(v);
                }
            }
        });
        
        LOG.debug("Counted {} unique macs for {} {} {} - {}  {} ", ret.size(), customerId, locationId, equipmentId, year, dayOfYear);

        return ret.size();
    }

    @Override
    public void finalizeUniqueMacsCount(int year, int dayOfYear) {
        
        Calendar calendar = Calendar.getInstance(DateTimeUtils.TZ_GMT);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);  
        
        long toTs = calendar.getTimeInMillis();

        idToServiceAdoptionMetricsMap.forEach((k,v) -> {
            if(k.getYear() == year && k.getDayOfYear() == dayOfYear) {
                long numUniq = getUniqueMacsCount(year, dayOfYear, k.getCustomerId(), k.getLocationId(), k.getEquipmentId());
                if(numUniq>0) {
                    v.setNumUniqueConnectedMacs(numUniq);
                }
                
                deleteUniqueMacs(toTs, k.getCustomerId(), k.getLocationId(), k.getEquipmentId());
            }
        });
    }
    
    @Override
    public void deleteUniqueMacs(long createdBeforeTimestampMs, int customerId, long locationId, long equipmentId) {
        
        LOG.debug("Deleting unique macs for {} {} {} created before {} ", customerId, locationId, equipmentId, createdBeforeTimestampMs);
        
        idToUniqueMacsMap.entrySet().removeIf(  (e) -> {
                return  e.getKey().getCustomerId() == customerId 
                        && e.getKey().getLocationId() == locationId 
                        && e.getKey().getEquipmentId() == equipmentId
                        && e.getKey().getTimestampMs() <= createdBeforeTimestampMs
                        ;
            });
    }
    
}
