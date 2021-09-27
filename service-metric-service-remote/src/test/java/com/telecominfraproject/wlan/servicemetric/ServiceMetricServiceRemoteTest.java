package com.telecominfraproject.wlan.servicemetric;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.apssid.models.ApSsidMetrics;
import com.telecominfraproject.wlan.servicemetric.apssid.models.SsidStatistics;
import com.telecominfraproject.wlan.servicemetric.client.models.ClientMetrics;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class ServiceMetricServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired ServiceMetricServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.serviceMetricServiceBaseUrl");
    }
    
    
    @Test
    public void testCRD() {
    	long baseTimestamp = System.currentTimeMillis();
    	ServiceMetric serviceMetric = new ServiceMetric();
        serviceMetric.setCustomerId(getNextCustomerId());
        serviceMetric.setEquipmentId(getNextEquipmentId());
        serviceMetric.setClientMac(0);
        serviceMetric.setCreatedTimestamp(baseTimestamp);
        
        ApNodeMetrics details = new ApNodeMetrics();
        details.setTxBytes(RadioType.is2dot4GHz, 42L);
        serviceMetric.setDetails(details );

        //create single
        remoteInterface.create(serviceMetric);        
        PaginationResponse<ServiceMetric> resp = remoteInterface.getForCustomer(0, baseTimestamp, serviceMetric.getCustomerId(), null, Collections.singleton(serviceMetric.getEquipmentId()), null, null, null, null);
        assertEquals(1, resp.getItems().size());
        assertEquals(serviceMetric, resp.getItems().get(0)); 
            
        List<ServiceMetric> metricsToCreate = new ArrayList<>();
        for(int i = 0; i< 10; i++) {
        	ServiceMetric sm = new ServiceMetric();
            sm.setCustomerId(serviceMetric.getCustomerId());
            sm.setEquipmentId(serviceMetric.getEquipmentId());
            sm.setClientMac(0);
            sm.setCreatedTimestamp(baseTimestamp - (i+1) * 10);
            
            ApNodeMetrics det = new ApNodeMetrics();
            det.setTxBytes(RadioType.is2dot4GHz, (long) (42 + 1 +  i));
            sm.setDetails(det);
        	
            metricsToCreate.add(sm);
        }

        //create bulk
        remoteInterface.create(metricsToCreate);
        PaginationResponse<ServiceMetric> respBulk = remoteInterface.getForCustomer(0, baseTimestamp, serviceMetric.getCustomerId(), null, Collections.singleton(serviceMetric.getEquipmentId()), null, null, null, null);
        assertEquals(11, respBulk.getItems().size());
        metricsToCreate.forEach(m -> assertTrue(respBulk.getItems().contains(m)));
        assertTrue(respBulk.getItems().contains(serviceMetric));

                
        //delete
        remoteInterface.delete(serviceMetric.getCustomerId(), serviceMetric.getEquipmentId(), baseTimestamp + 1);
        resp = remoteInterface.getForCustomer(0, baseTimestamp, serviceMetric.getCustomerId(), null, Collections.singleton(serviceMetric.getEquipmentId()), null, null, null, null);
        assertTrue(resp.getItems().isEmpty());
        
    }
    
    //verify returned data and sort options
    @Test
    public void testPagination() {
    	
        int customerId_1 = getNextCustomerId();
        int customerId_2 = getNextCustomerId();

         long baseTimestamp = System.currentTimeMillis();

        long apNameIdx = 0;
                
        long fromTime = 0;
        long toTime = baseTimestamp;
        
        //create 150 Service service_metrics_collection_config
        
        //service_metrics_collection_config to be tested
        for(int i = 0; i< 50; i++){
        	ServiceMetric serviceMetric = new ServiceMetric();
            serviceMetric.setCustomerId(customerId_1);
            serviceMetric.setEquipmentId(getNextEquipmentId());
            serviceMetric.setClientMac(getNextEquipmentId());
            serviceMetric.setCreatedTimestamp(baseTimestamp - 100000 + getNextEquipmentId());
            
            apNameIdx++;

            remoteInterface.create(serviceMetric);
        }

        //service_metrics_collection_config outside the target time
        for(int i = 0; i< 50; i++){
        	ServiceMetric serviceMetric = new ServiceMetric();
            serviceMetric.setCustomerId(customerId_1);
            serviceMetric.setEquipmentId(getNextEquipmentId());
            serviceMetric.setClientMac(getNextEquipmentId());
            serviceMetric.setCreatedTimestamp(baseTimestamp + getNextEquipmentId());
            
            apNameIdx++;

            remoteInterface.create(serviceMetric);
        }
        
        //service_metrics_collection_config for another customer
        for(int i = 0; i< 50; i++){
        	ServiceMetric serviceMetric = new ServiceMetric();
            serviceMetric.setCustomerId(customerId_2);
            serviceMetric.setEquipmentId(getNextEquipmentId());
            serviceMetric.setClientMac(getNextEquipmentId());
            serviceMetric.setCreatedTimestamp(baseTimestamp  - 100000 + getNextEquipmentId());
            
            apNameIdx++;

            remoteInterface.create(serviceMetric);
        }

        //paginate over Metrics
        
        List<ColumnAndSort> sortBy = new ArrayList<>();
        sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
        
        PaginationContext<ServiceMetric> context = new PaginationContext<>(10);
        PaginationResponse<ServiceMetric> page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, context);
        PaginationResponse<ServiceMetric> page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page1.getContext());
        PaginationResponse<ServiceMetric> page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page2.getContext());
        PaginationResponse<ServiceMetric> page4 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page3.getContext());
        PaginationResponse<ServiceMetric> page5 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page4.getContext());
        PaginationResponse<ServiceMetric> page6 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page5.getContext());
        PaginationResponse<ServiceMetric> page7 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page6.getContext());
        
        //verify returned pages
        assertEquals(10, page1.getItems().size());
        assertEquals(10, page2.getItems().size());
        assertEquals(10, page3.getItems().size());
        assertEquals(10, page4.getItems().size());
        assertEquals(10, page5.getItems().size());
        
        page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
        page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
        page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
        page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
        page5.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
        
        assertEquals(0, page6.getItems().size());
        assertEquals(0, page7.getItems().size());
        
        assertFalse(page1.getContext().isLastPage());
        assertFalse(page2.getContext().isLastPage());
        assertFalse(page3.getContext().isLastPage());
        assertFalse(page4.getContext().isLastPage());
        assertFalse(page5.getContext().isLastPage());
        
        assertTrue(page6.getContext().isLastPage());
        assertTrue(page7.getContext().isLastPage());
        
        //test first page of the results with empty sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<ServiceMetric> page1EmptySort = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, Collections.emptyList(), context);
        assertEquals(10, page1EmptySort.getItems().size());

        //test first page of the results with null sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<ServiceMetric> page1NullSort = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, null, context);
        assertEquals(10, page1NullSort.getItems().size());

        //test first page of the results with sort descending order by a equipmentId property 
        PaginationResponse<ServiceMetric> page1SingleSortDesc = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
        assertEquals(10, page1SingleSortDesc.getItems().size());

     }
    
    
    @Test
    public void testPaginationWithFilters() {
       //create 30 Metrics for our customer_1: 3 service_metrics_collection_config per 10 equipment 
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       
       long[] equipmentIds_1 = new long[10];
       for(int i=0; i<10; i++) {
    	   equipmentIds_1[i] =  getNextEquipmentId(); 
       }

       List<MacAddress> clientMacs = new ArrayList<>();
       for(int i=0; i<10; i++) {
    	   clientMacs.add(new MacAddress(getNextEquipmentId())); 
       }
       
       long[] locationIds = new long[10];
       for(int i=0; i<10; i++) {
           locationIds[i] =  getNextLocationId(); 
       }

       ServiceMetricDataType dataType_1 = ServiceMetricDataType.ApNode;
       ServiceMetricDataType dataType_2 = ServiceMetricDataType.Client;
       ServiceMetricDataType dataType_3 = ServiceMetricDataType.ApSsid;

       Set<Long> emptyLocations = new HashSet<>();
       Set<Long> oneLocation = new HashSet<>();
       oneLocation.add(locationIds[0]);

       Set<Long> twoLocations = new HashSet<>();
       twoLocations.add(locationIds[0]);
       twoLocations.add(locationIds[1]);

       Set<Long> emptyEquipment = new HashSet<>();
       Set<Long> oneEquipment = new HashSet<>();
       oneEquipment.add(equipmentIds_1[0]);

       Set<Long> twoEquipment = new HashSet<>();
       twoEquipment.add(equipmentIds_1[0]);
       twoEquipment.add(equipmentIds_1[1]);

       Set<MacAddress> emptyMacs = new HashSet<>();
       Set<MacAddress> oneMac = new HashSet<>();
       oneMac.add(clientMacs.get(0));

       Set<MacAddress> twoMacs = new HashSet<>();
       twoMacs.add(clientMacs.get(0));
       twoMacs.add(clientMacs.get(1));

       Set<ServiceMetricDataType> emptyDataTypes = new HashSet<>();
       Set<ServiceMetricDataType> oneDataType = new HashSet<>();
       oneDataType.add(dataType_1);
       
       Set<ServiceMetricDataType> twoDataTypes = new HashSet<>();
       twoDataTypes.add(dataType_1);
       twoDataTypes.add(dataType_2);

       Set<ServiceMetricDataType> threeDataTypes = new HashSet<>();
       threeDataTypes.add(dataType_1);
       threeDataTypes.add(dataType_2);
       threeDataTypes.add(dataType_3);

       long baseTimestamp = System.currentTimeMillis();

       long apNameIdx = 0;
       
       for(int i = 0; i< equipmentIds_1.length; i++){
           //first metric - apNode
           ServiceMetric serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_1);
           serviceMetric.setLocationId(locationIds[i]);           
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(0);
           serviceMetric.setCreatedTimestamp(baseTimestamp - getNextEquipmentId());
           
           ApNodeMetrics details = new ApNodeMetrics();
           details.setTxBytes(RadioType.is2dot4GHz, apNameIdx);
           serviceMetric.setDetails(details );
           remoteInterface.create(serviceMetric);

           //second metric - client
           serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_1);
           serviceMetric.setLocationId(locationIds[i]);           
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(clientMacs.get(i).getAddressAsLong());
           serviceMetric.setCreatedTimestamp(baseTimestamp - getNextEquipmentId());
           
           ClientMetrics details2 = new ClientMetrics();
           serviceMetric.setDetails(details2);

           remoteInterface.create(serviceMetric);

           //third metric - neighbour
           serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_1);
           serviceMetric.setLocationId(locationIds[i]);           
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(0);
           serviceMetric.setCreatedTimestamp(baseTimestamp - getNextEquipmentId());
           
           ApSsidMetrics details3 = new ApSsidMetrics();
           Map<RadioType, List<SsidStatistics>> ssidStats = new EnumMap<>(RadioType.class);
           List<SsidStatistics> ssid24Stats = new ArrayList<>();           
           ssidStats.put(RadioType.is2dot4GHz, ssid24Stats );
           SsidStatistics s = new SsidStatistics();
           s.setRxBytes(apNameIdx);
           ssid24Stats.add(s );
           details3.setSsidStats(ssidStats);
           serviceMetric.setDetails(details3);

           remoteInterface.create(serviceMetric);

           apNameIdx++;
       }

       //add some service_metrics_collection_config for another customer into the mix
       for(int i = 0; i< 10; i++){
           ServiceMetric serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_2);
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(0);
           serviceMetric.setCreatedTimestamp(baseTimestamp - getNextEquipmentId());
           
           ApNodeMetrics details = new ApNodeMetrics();
           details.setTxBytes(RadioType.is2dot4GHz, apNameIdx);
           serviceMetric.setDetails(details );
           remoteInterface.create(serviceMetric);

           apNameIdx++;
       }

       //
       //Now paginate over Metrics with various filters:
       //
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       long fromTime = 0;
       long toTime = baseTimestamp;
       
       //Paginate over all equipment and all service_metrics_collection_config
       PaginationContext<ServiceMetric> context = new PaginationContext<>(10);
       PaginationResponse<ServiceMetric> page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       PaginationResponse<ServiceMetric> page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       PaginationResponse<ServiceMetric> page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page2.getContext());
       PaginationResponse<ServiceMetric> page4 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page3.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(0, page4.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());

       assertTrue(page4.getContext().isLastPage());
       
       //Paginate over all equipment and all statuses - with null parameters
       context = new PaginationContext<>(10);
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page1.getContext());
       page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page2.getContext());
       page4 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page3.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(0, page4.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());

       assertTrue(page4.getContext().isLastPage());

       Set<Long> returnedEquipmentIds = new HashSet<>();
       Set<ServiceMetricDataType> returnedDataTypes = new HashSet<>();
       
       //Paginate over oneEquipment, all client macs and all statuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(3, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(threeDataTypes, returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over twoEquipment, all client macs and all statuses
       context = new PaginationContext<>(5);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, emptyDataTypes, sortBy, page2.getContext());
       
       //verify returned pages
       assertEquals(5, page1.getItems().size());
       assertEquals(1, page2.getItems().size());
       assertEquals(0, page3.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});

		page2.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});

		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(threeDataTypes, returnedDataTypes);

       
       assertFalse(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       assertTrue(page3.getContext().isLastPage());

       //Paginate over oneEquipment, all client macs and oneStatus
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, oneDataType, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, oneDataType, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(oneDataType, returnedDataTypes);

       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over oneEquipment, all client macs and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(2, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(twoDataTypes, returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over twoEquipment, all client macs and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(4, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(twoDataTypes, returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //
       //Test pagination with different combinations of client macs
       //
       
       //Paginate over twoEquipment, two client macs and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, twoMacs, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, twoMacs, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(2, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(ServiceMetricDataType.Client, returnedDataTypes.iterator().next());
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over oneEquipment, one client macs and oneStatus
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, oneMac, Collections.singleton(ServiceMetricDataType.Client), sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, oneMac, Collections.singleton(ServiceMetricDataType.Client), sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(ServiceMetricDataType.Client, returnedDataTypes.iterator().next());
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over twoEquipment, two locations, two client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, twoMacs, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, twoMacs, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(2, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedEquipmentIds.add(e.getEquipmentId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(twoEquipment, returnedEquipmentIds);
        assertEquals(ServiceMetricDataType.Client, returnedDataTypes.iterator().next());
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());


    }

}
