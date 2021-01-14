package com.telecominfraproject.wlan.servicemetric.datastore;

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
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
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
public abstract class BaseServiceMetricDatastoreTest {
    @Autowired
    protected ServiceMetricDatastore testInterface;

    protected static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRD() {
    	long baseTimestamp = System.currentTimeMillis();
    	ServiceMetric serviceMetric = new ServiceMetric();
        serviceMetric.setCustomerId((int) testSequence.incrementAndGet());
        serviceMetric.setEquipmentId(testSequence.incrementAndGet());
        serviceMetric.setClientMac(0);
        serviceMetric.setCreatedTimestamp(baseTimestamp);
        
        ApNodeMetrics details = new ApNodeMetrics();
        details.setTxBytes(RadioType.is2dot4GHz, 42L);
        serviceMetric.setDetails(details );

        //create single
        testInterface.create(serviceMetric);        
        PaginationResponse<ServiceMetric> resp = testInterface.getForCustomer(0, baseTimestamp, serviceMetric.getCustomerId(), null, Collections.singleton(serviceMetric.getEquipmentId()), null, null, null, null);
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
        testInterface.create(metricsToCreate);
        PaginationResponse<ServiceMetric> respBulk = testInterface.getForCustomer(0, baseTimestamp, serviceMetric.getCustomerId(), null, Collections.singleton(serviceMetric.getEquipmentId()), null, null, null, null);
        assertEquals(11, respBulk.getItems().size());
        metricsToCreate.forEach(m -> assertTrue(respBulk.getItems().contains(m)));
        assertTrue(respBulk.getItems().contains(serviceMetric));

                
        //delete
        testInterface.delete(serviceMetric.getCustomerId(), serviceMetric.getEquipmentId(), baseTimestamp + 1);
        resp = testInterface.getForCustomer(0, baseTimestamp, serviceMetric.getCustomerId(), null, Collections.singleton(serviceMetric.getEquipmentId()), null, null, null, null);
        assertTrue(resp.getItems().isEmpty());
        
    }
    
    //verify returned data and sort options
    @Test
    public void testPagination() {
    	
        int customerId_1 = (int) testSequence.incrementAndGet();
        int customerId_2 = (int) testSequence.incrementAndGet();

         long baseTimestamp = System.currentTimeMillis();

        long apNameIdx = 0;
                
        long fromTime = 0;
        long toTime = baseTimestamp;
        
        //create 150 Service metrics
        
        Set<Long> used_equipmentIds = new HashSet<>();
        
        //metrics to be tested
        for(int i = 0; i< 50; i++){
        	ServiceMetric serviceMetric = new ServiceMetric();
            serviceMetric.setCustomerId(customerId_1);
            serviceMetric.setEquipmentId(testSequence.incrementAndGet());
            serviceMetric.setClientMac(testSequence.incrementAndGet());
            serviceMetric.setCreatedTimestamp(baseTimestamp - 100000 + testSequence.incrementAndGet());
            
            ClientMetrics details2 = new ClientMetrics();
            details2.setClassificationName("qr_"+apNameIdx);
            serviceMetric.setDetails(details2);

            apNameIdx++;

            testInterface.create(serviceMetric);
            used_equipmentIds.add(serviceMetric.getEquipmentId());
        }

        //metrics outside the target time
        for(int i = 0; i< 50; i++){
        	ServiceMetric serviceMetric = new ServiceMetric();
            serviceMetric.setCustomerId(customerId_1);
            serviceMetric.setEquipmentId(testSequence.incrementAndGet());
            serviceMetric.setClientMac(testSequence.incrementAndGet());
            serviceMetric.setCreatedTimestamp(baseTimestamp + testSequence.incrementAndGet());
            
            ClientMetrics details2 = new ClientMetrics();
            details2.setClassificationName("qr_"+apNameIdx);
            serviceMetric.setDetails(details2);

            apNameIdx++;

            testInterface.create(serviceMetric);
            used_equipmentIds.add(serviceMetric.getEquipmentId());
        }
        
        //metrics for another customer
        for(int i = 0; i< 50; i++){
        	ServiceMetric serviceMetric = new ServiceMetric();
            serviceMetric.setCustomerId(customerId_2);
            serviceMetric.setEquipmentId(testSequence.incrementAndGet());
            serviceMetric.setClientMac(testSequence.incrementAndGet());
            serviceMetric.setCreatedTimestamp(baseTimestamp  - 100000 + testSequence.incrementAndGet());
            
            ClientMetrics details2 = new ClientMetrics();
            details2.setClassificationName("qr_"+apNameIdx);
            serviceMetric.setDetails(details2);

            apNameIdx++;

            testInterface.create(serviceMetric);
            used_equipmentIds.add(serviceMetric.getEquipmentId());
        }

        //paginate over Metrics
        
        List<ColumnAndSort> sortBy = new ArrayList<>();
        sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
        
        PaginationContext<ServiceMetric> context = new PaginationContext<>(10);
        PaginationResponse<ServiceMetric> page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, context);
        PaginationResponse<ServiceMetric> page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page1.getContext());
        PaginationResponse<ServiceMetric> page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page2.getContext());
        PaginationResponse<ServiceMetric> page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page3.getContext());
        PaginationResponse<ServiceMetric> page5 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page4.getContext());
        PaginationResponse<ServiceMetric> page6 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page5.getContext());
        PaginationResponse<ServiceMetric> page7 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page6.getContext());
        
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
        
        List<String> expectedPage3Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_20", "qr_21", "qr_22", "qr_23", "qr_24", "qr_25", "qr_26", "qr_27", "qr_28", "qr_29" }));
        List<String> actualPage3Strings = new ArrayList<>();
        page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientMetrics) ce.getDetails()).getClassificationName()) );
        
        assertEquals(expectedPage3Strings, actualPage3Strings);
       
        //test first page of the results with empty sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<ServiceMetric> page1EmptySort = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, Collections.emptyList(), context);
        assertEquals(10, page1EmptySort.getItems().size());

        List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1EmptySortStrings = new ArrayList<>();
        page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((ClientMetrics) ce.getDetails()).getClassificationName() ) );

        assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

        //test first page of the results with null sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<ServiceMetric> page1NullSort = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, null, context);
        assertEquals(10, page1NullSort.getItems().size());

        List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1NullSortStrings = new ArrayList<>();
        page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((ClientMetrics) ce.getDetails()).getClassificationName() ) );

        assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

        
        //test first page of the results with sort descending order by a equipmentId property 
        PaginationResponse<ServiceMetric> page1SingleSortDesc = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
        assertEquals(10, page1SingleSortDesc.getItems().size());

        List<String> expectedPage1SingleSortDescStrings = getPagination_expectedPage1SingleSortDescStrings();
        List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
        page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((ClientMetrics) ce.getDetails()).getClassificationName() ) );
        
        assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);        

        used_equipmentIds.forEach(eqId -> testInterface.delete(customerId_1, eqId, System.currentTimeMillis()));
        used_equipmentIds.forEach(eqId -> testInterface.delete(customerId_2, eqId, System.currentTimeMillis()));

     }
    
    protected List<String> getPagination_expectedPage1SingleSortDescStrings(){ 
        return Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" });
    }
    
    @Test
    public void testPaginationWithFilters() {
       //create 30 Metrics for our customer_1: 3 metrics per 10 equipment 
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
     
       long[] equipmentIds_1 = new long[10];
       for(int i=0; i<10; i++) {
    	   equipmentIds_1[i] =  testSequence.incrementAndGet(); 
       }

       long[] locationIds = new long[10];
       for(int i=0; i<10; i++) {
           locationIds[i] =  testSequence.incrementAndGet(); 
       }


       List<MacAddress> clientMacs = new ArrayList<>();
       for(int i=0; i<10; i++) {
    	   clientMacs.add(new MacAddress(testSequence.incrementAndGet())); 
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
           serviceMetric.setCreatedTimestamp(baseTimestamp - testSequence.incrementAndGet());
           
           ApNodeMetrics details = new ApNodeMetrics();
           details.setTxBytes(RadioType.is2dot4GHz, apNameIdx);
           serviceMetric.setDetails(details );
           testInterface.create(serviceMetric);

           //second metric - client
           serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_1);
           serviceMetric.setLocationId(locationIds[i]);
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(clientMacs.get(i).getAddressAsLong());
           serviceMetric.setCreatedTimestamp(baseTimestamp - testSequence.incrementAndGet());
           
           ClientMetrics details2 = new ClientMetrics();
           details2.setClassificationName("qr_"+apNameIdx);
           serviceMetric.setDetails(details2);

           testInterface.create(serviceMetric);

           //third metric - neighbour
           serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_1);
           serviceMetric.setLocationId(locationIds[i]);           
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(0);
           serviceMetric.setCreatedTimestamp(baseTimestamp - testSequence.incrementAndGet());
           
           ApSsidMetrics details3 = new ApSsidMetrics();
           Map<RadioType, List<SsidStatistics>> ssidStats = new EnumMap<>(RadioType.class);
           List<SsidStatistics> ssid24Stats = new ArrayList<>();           
           ssidStats.put(RadioType.is2dot4GHz, ssid24Stats );
           SsidStatistics s = new SsidStatistics();
           s.setRxBytes(apNameIdx);
           ssid24Stats.add(s );
           details3.setSsidStats(ssidStats);
           serviceMetric.setDetails(details3);

           testInterface.create(serviceMetric);

           apNameIdx++;
       }

       //add some metrics for another customer into the mix
       for(int i = 0; i< 10; i++){
           ServiceMetric serviceMetric = new ServiceMetric();
           serviceMetric.setCustomerId(customerId_2);
           serviceMetric.setEquipmentId(equipmentIds_1[i]);
           serviceMetric.setClientMac(0);
           serviceMetric.setCreatedTimestamp(baseTimestamp - testSequence.incrementAndGet());
           
           ApNodeMetrics details = new ApNodeMetrics();
           details.setTxBytes(RadioType.is2dot4GHz, apNameIdx);
           serviceMetric.setDetails(details );
           testInterface.create(serviceMetric);

           apNameIdx++;
       }

        //
        // Now paginate over Metrics with various filters
        //
        //   The following combinations of parameters are covered:
        //
        //       Locations, Equipment, Macs, DataTypes
        //       0, 0, 0, 0 = ()
        //       0, 0, 0, 1 = (1 dt)
        //       0, 0, 1, 0 = (1 cm)
        //       0, 0, 1, 1 = (1 cm, 1 dt)
        //       0, 1, 0, 0 = (1 eq), (2 eq)
        //       0, 1, 0, 1 = (1 eq, 1 dt), (1 eq, 2 dt), (2 eq, 2 dt)
        //       0, 1, 1, 0 = (1 eq, 1 cm)
        //       0, 1, 1, 1 = (2 eq, 2 cm, 2 dt), (1 eq, 1 cm, 1 dt)
        //       1, 0, 0, 0 = (1 loc)
        //       1, 0, 0, 1 = (1 loc, 1 dt)
        //       1, 0, 1, 0 = (1 loc, 1 cm)
        //       1, 0, 1, 1 = (1 loc, 1 cm, 1 dt)
        //       1, 1, 0, 0 = (1 loc, 1 eq), (2 loc, 2 eq)
        //       1, 1, 0, 1 = (2 loc, 1 eq, 1 dt), (2 loc, 1 eq, 2 dt), (2 loc, 2 eq, 2 dt)
        //       1, 1, 1, 0 = (1 loc, 1 eq, 1 cm)
        //       1, 1, 1, 1 = (2 loc, 2 eq, 2 cm, 2 dt), (2 loc, 2 eq, 2 cm, 1 dt)

       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       long fromTime = 0;
       long toTime = baseTimestamp;
       
       //Paginate over all metrics for customer
       PaginationContext<ServiceMetric> context = new PaginationContext<>(10);
       PaginationResponse<ServiceMetric> page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyLocations, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       PaginationResponse<ServiceMetric> page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyLocations, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       PaginationResponse<ServiceMetric> page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyLocations, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page2.getContext());
       PaginationResponse<ServiceMetric> page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyLocations, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page3.getContext());
       
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
       
       //Paginate over all metrics for customer - with null parameters
       context = new PaginationContext<>(10);
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page2.getContext());
       page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page3.getContext());
       
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
       
       //
       // =====================================================================
       //
       //
       // Try various searches with locationIds = null
       //
       
       //Paginate over all equipment, all client macs and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, emptyMacs, oneDataType, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, emptyMacs, oneDataType, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedEquipmentIds.add(e.getEquipmentId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(equipmentIds_1.length, returnedEquipmentIds.size());
        assertEquals(oneDataType, returnedDataTypes);
       
       assertFalse(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over all equipment, one client mac and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, oneMac, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, oneMac, emptyDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over all equipment, one client mac and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, oneMac, Set.of(dataType_2), sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, oneMac, Set.of(dataType_2), sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over one equipment, one client mac and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, oneMac, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, oneMac, emptyDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       
       //Paginate over oneEquipment, all client macs and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       
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
       
       //Paginate over twoEquipment, all client macs and all data types
       context = new PaginationContext<>(5);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, emptyDataTypes, sortBy, page2.getContext());
       
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

       //Paginate over oneEquipment, all client macs and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, oneDataType, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, oneDataType, sortBy, page1.getContext());
       
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
       
       //Paginate over oneEquipment, all client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, emptyMacs, twoDataTypes, sortBy, page1.getContext());
       
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
       
       //Paginate over twoEquipment, all client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, emptyMacs, twoDataTypes, sortBy, page1.getContext());
       
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
       
       //Paginate over twoEquipment, two client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, twoMacs, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, twoMacs, twoDataTypes, sortBy, page1.getContext());
       
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

       //Paginate over oneEquipment, one client macs and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, oneMac, Collections.singleton(ServiceMetricDataType.Client), sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, oneMac, Collections.singleton(ServiceMetricDataType.Client), sortBy, page1.getContext());
       
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
       
       //
       // =====================================================================
       //
       //
       // Try various searches with locationIds != null
       //

       //Paginate over one location, all equipment, all client macs and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       
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

       //Paginate over one location, all equipment, all client macs and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, emptyMacs, oneDataType, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, emptyMacs, oneDataType, sortBy, page1.getContext());
       
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

       //Paginate over one location, all equipment, one client mac and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, oneMac, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, oneMac, emptyDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over one location, all equipment, one client mac and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, oneMac, Set.of(dataType_2), sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, emptyEquipment, oneMac, Set.of(dataType_2), sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //Paginate over one location, one equipment, one client mac and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, oneEquipment, oneMac, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, oneEquipment, oneMac, emptyDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over oneEquipment, one location, all client macs and all data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, oneEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, oneEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       
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
       
       //Paginate over twoEquipment, two locations, all client macs and all data types
       context = new PaginationContext<>(5);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, emptyMacs, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, emptyMacs, emptyDataTypes, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, emptyMacs, emptyDataTypes, sortBy, page2.getContext());
       
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

       //Paginate over oneEquipment, two locations, all client macs and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, oneEquipment, emptyMacs, oneDataType, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, oneEquipment, emptyMacs, oneDataType, sortBy, page1.getContext());
       
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
       
       //Paginate over oneEquipment, two locations, all client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, oneEquipment, emptyMacs, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, oneEquipment, emptyMacs, twoDataTypes, sortBy, page1.getContext());
       
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
       
       //Paginate over twoEquipment, two locations, all client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, emptyMacs, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, emptyMacs, twoDataTypes, sortBy, page1.getContext());
       
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
       
       //Paginate over twoEquipment, two locations, two client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, twoMacs, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, twoMacs, twoDataTypes, sortBy, page1.getContext());
       
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

       //Paginate over oneEquipment, one location, one client macs and one data type
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, oneEquipment, oneMac, Collections.singleton(ServiceMetricDataType.Client), sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneLocation, oneEquipment, oneMac, Collections.singleton(ServiceMetricDataType.Client), sortBy, page1.getContext());
       
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
       
       //
       // =====================================================================
       //

       Arrays.stream(equipmentIds_1).forEach(eqId -> testInterface.delete(customerId_1, eqId, System.currentTimeMillis()));
       Arrays.stream(equipmentIds_1).forEach(eqId -> testInterface.delete(customerId_2, eqId, System.currentTimeMillis()));

    }

}
