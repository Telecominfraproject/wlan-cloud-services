package com.telecominfraproject.wlan.adoptionmetrics.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.adoptionmetrics.datastore.AdoptionMetricsDatastore;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/adoptionMetrics")
public class AdoptionMetricsController {

    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsController.class);

    public static class ListOfServiceAdoptionMetrics extends ArrayList<ServiceAdoptionMetrics> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private AdoptionMetricsDatastore adoptionMetricsDatastore;

    @RequestMapping(method=RequestMethod.POST)
    public GenericResponse update(@RequestBody List<ServiceAdoptionMetrics> serviceAdoptionMetricsList) {
        adoptionMetricsDatastore.update(serviceAdoptionMetricsList);
        return new GenericResponse(true, "");
    }

    @RequestMapping(value = "/perEquipmentPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics get(@RequestParam int year, @RequestParam Set<Long> equipmentIds) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsDatastore.get(year, equipmentIds));

        LOG.debug("Got {} adoption metrics for {} equipment {}", ret.size(), year, equipmentIds);
        
        return ret;
    }

    @RequestMapping(value = "/perLocationPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAggregatePerLocationPerDay(@RequestParam int year, @RequestParam Set<Long> locationIds) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsDatastore.getAggregatePerLocationPerDay(year, locationIds));

        LOG.debug("Got {} adoption metrics for {} locations {}", ret.size(), year, locationIds);
        
        return ret;
    }

    @RequestMapping(value = "/perCustomerPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAggregatePerCustomerPerDay(@RequestParam int year, @RequestParam Set<Integer> customerIds) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsDatastore.getAggregatePerCustomerPerDay(year, customerIds));

        LOG.debug("Got {} adoption metrics for {} customers {}", ret.size(), year, customerIds);
        
        return ret;
    }

    @RequestMapping(value = "/allPerMonth", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAllPerMonth(@RequestParam int year) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsDatastore.getAllPerMonth(year));

        LOG.debug("Got {} adoption metrics per month for {}", ret.size(), year);
        
        return ret;
    }

    @RequestMapping(value = "/allPerWeek", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAllPerWeek(@RequestParam int year) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsDatastore.getAllPerWeek(year));

        LOG.debug("Got {} adoption metrics per week for {}", ret.size(), year);
        
        return ret;
    }

    @RequestMapping(value = "/allPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAllPerDay(@RequestParam int year) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsDatastore.getAllPerDay(year));

        LOG.debug("Got {} adoption metrics per day for {}", ret.size(), year);
        
        return ret;
    }

    @RequestMapping(value = "/uniqueMacs", method=RequestMethod.POST)
    public GenericResponse updateUniqueMacs(@RequestParam long timestampMs, 
            @RequestParam int customerId, 
            @RequestParam long locationId, 
            @RequestParam long equipmentId,
            @RequestParam Set<Long> clientMacSet) {

        LOG.debug("Updating UniqueMacs {} {} {} {}", timestampMs, customerId, locationId, equipmentId);
        adoptionMetricsDatastore.updateUniqueMacs(timestampMs, customerId, locationId, equipmentId, clientMacSet);

        return new GenericResponse(true, "");
    }

    @RequestMapping(value = "/uniqueMacsCount", method=RequestMethod.GET)
    public long getUniqueMacsCount(@RequestParam int year,
            @RequestParam int dayOfYear, 
            @RequestParam int customerId, 
            @RequestParam long locationId, 
            @RequestParam long equipmentId) {

        long ret = adoptionMetricsDatastore.getUniqueMacsCount(year, dayOfYear, customerId, locationId, equipmentId);

        LOG.debug("Counted {} UniqueMacs for {} {} {} {} {}", ret, year, dayOfYear, customerId, locationId, equipmentId);

        return ret;
    }

    @RequestMapping(value = "/uniqueMacs", method=RequestMethod.DELETE)
    public GenericResponse deleteUniqueMacs(@RequestParam long createdBeforeTimestampMs, 
            @RequestParam int customerId, 
            @RequestParam long locationId,
            @RequestParam long equipmentId) {

        LOG.debug("Deleting UniqueMacs {} {} {} {}", createdBeforeTimestampMs, customerId, locationId, equipmentId);
        adoptionMetricsDatastore.deleteUniqueMacs(createdBeforeTimestampMs, customerId, locationId, equipmentId);

        return new GenericResponse(true, "");
    }

    
}
