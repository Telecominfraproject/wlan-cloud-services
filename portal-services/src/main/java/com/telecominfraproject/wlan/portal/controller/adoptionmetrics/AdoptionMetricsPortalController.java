package com.telecominfraproject.wlan.portal.controller.adoptionmetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.adoptionmetrics.AdoptionMetricsServiceInterface;
import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal/adoptionMetrics")
public class AdoptionMetricsPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsPortalController.class);

    public static class ListOfServiceAdoptionMetrics extends ArrayList<ServiceAdoptionMetrics> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private AdoptionMetricsServiceInterface adoptionMetricsInterface;

    @RequestMapping(value = "/perEquipmentPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getPerEquipmentPerDay(@RequestParam int year, @RequestParam Set<Long> equipmentIds) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsInterface.get(year, equipmentIds));

        LOG.debug("Got {} adoption metrics for {} equipment {}", ret.size(), year, equipmentIds);
        
        return ret;
    }

    @RequestMapping(value = "/perLocationPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAggregatePerLocationPerDay(@RequestParam int year, @RequestParam Set<Long> locationIds) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsInterface.getAggregatePerLocationPerDay(year, locationIds));

        LOG.debug("Got {} adoption metrics for {} locations {}", ret.size(), year, locationIds);
        
        return ret;
    }

    @RequestMapping(value = "/perCustomerPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAggregatePerCustomerPerDay(@RequestParam int year, @RequestParam Set<Integer> customerIds) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsInterface.getAggregatePerCustomerPerDay(year, customerIds));

        LOG.debug("Got {} adoption metrics for {} customers {}", ret.size(), year, customerIds);
        
        return ret;
    }

    @RequestMapping(value = "/allPerMonth", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAllPerMonth(@RequestParam int year) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsInterface.getAllPerMonth(year));

        LOG.debug("Got {} adoption metrics per month for {}", ret.size(), year);
        
        return ret;
    }

    @RequestMapping(value = "/allPerWeek", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAllPerWeek(@RequestParam int year) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsInterface.getAllPerWeek(year));

        LOG.debug("Got {} adoption metrics per week for {}", ret.size(), year);
        
        return ret;
    }

    @RequestMapping(value = "/allPerDay", method=RequestMethod.GET)
    public ListOfServiceAdoptionMetrics getAllPerDay(@RequestParam int year) {
        ListOfServiceAdoptionMetrics ret = new ListOfServiceAdoptionMetrics();
        ret.addAll(adoptionMetricsInterface.getAllPerDay(year));

        LOG.debug("Got {} adoption metrics per day for {}", ret.size(), year);
        
        return ret;
    }

}
