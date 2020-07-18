package com.telecominfraproject.wlan.routing.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseRoutingDatastoreTest {
    @Autowired
    protected RoutingDatastore testInterface;

    protected static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRUD() {
    	EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();
    	gateway.setGatewayType(GatewayType.CEGW);
    	gateway.setHostname("test-hostname"+testSequence.incrementAndGet());
    	gateway.setIpAddr("127.0.0.1");
    	gateway.setPort(4242);
    	gateway = testInterface.registerGateway(gateway);
    	
    	EquipmentRoutingRecord routing = createEquipmentRoutingRecordObject();
    	routing.setGatewayId(gateway.getId());

        //create
    	EquipmentRoutingRecord created = testInterface.create(routing);
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(routing.getCustomerId(), created.getCustomerId());
        assertEquals(routing.getEquipmentId(), created.getEquipmentId());
        assertEquals(routing.getGatewayId(), created.getGatewayId());       
                
        // update
        created.setEquipmentId(created.getEquipmentId() + 1);
        EquipmentRoutingRecord updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getEquipmentId(), updated.getEquipmentId());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
        	created.setEquipmentId(created.getEquipmentId() + 1);
            updated = testInterface.update(created);
        }

        //UPDATE test - fail because of concurrent modification exception
        try{
        	EquipmentRoutingRecord modelConcurrentUpdate = created.clone();
        	modelConcurrentUpdate.setGatewayId(42);
        	testInterface.update(modelConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }
        
        //retrieve
        EquipmentRoutingRecord retrieved = testInterface.get(created.getId());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());

        retrieved = testInterface.getOrNull(created.getId());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent
        try {
        	testInterface.get(-1);
        	fail("retrieve non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }
        
        assertNull(testInterface.getOrNull(-1));
        
        //delete
        retrieved = testInterface.delete(created.getId());
        assertNotNull(retrieved);
        
        //delete non-existent
        try {
        	testInterface.delete(-1);
        	fail("delete non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        //update non-existent
        try {
        	testInterface.update(retrieved);
        	fail("update non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        testInterface.deleteGateway(gateway.getId());
    }
    
    @Test
    public void testGetAllInSet() {
        Set<EquipmentRoutingRecord> createdSet = new HashSet<>();
        Set<EquipmentRoutingRecord> createdTestSet = new HashSet<>();

    	EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();
    	gateway.setGatewayType(GatewayType.CEGW);
    	gateway.setHostname("test-hostname"+testSequence.incrementAndGet());
    	gateway.setIpAddr("127.0.0.1");
    	gateway.setPort(4242);
    	gateway = testInterface.registerGateway(gateway);

        //Create test Routings
        EquipmentRoutingRecord routing = new EquipmentRoutingRecord();

        for (int i = 0; i < 10; i++) {
            routing.setCustomerId(i);
            routing.setGatewayId(gateway.getId());

            EquipmentRoutingRecord ret = testInterface.create(routing);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (EquipmentRoutingRecord c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<EquipmentRoutingRecord> routingsRetrievedByIdSet = testInterface.get(testSetIds);
        assertEquals(5, routingsRetrievedByIdSet.size());
        for (EquipmentRoutingRecord c : routingsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the routings from the non-test set are not in the list
        for (EquipmentRoutingRecord c : routingsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (EquipmentRoutingRecord c : createdSet) {
        	testInterface.delete(c.getId());
        }
        for (EquipmentRoutingRecord c : createdTestSet) {
        	testInterface.delete(c.getId());
        }

        testInterface.deleteGateway(gateway.getId());
    }
    
    @Test
    public void testRoutingPagination()
    {
       //create 100 Routings
       EquipmentRoutingRecord mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
	   EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();
	   gateway.setGatewayType(GatewayType.CEGW);
	   gateway.setHostname("test-hostname"+testSequence.incrementAndGet());
	   gateway.setIpAddr("127.0.0.1");
	   gateway.setPort(4242);
	   gateway = testInterface.registerGateway(gateway);
	   
	   List<EquipmentRoutingRecord> createdRecords = new ArrayList<>(); 
       
       long eqId = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new EquipmentRoutingRecord();
           mdl.setCustomerId(customerId_1);
           mdl.setGatewayId(gateway.getId());
           mdl.setEquipmentId(eqId);

           eqId++;
           mdl = testInterface.create(mdl);
           createdRecords.add(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new EquipmentRoutingRecord();
           mdl.setCustomerId(customerId_2);
           mdl.setGatewayId(gateway.getId());
           mdl.setEquipmentId(eqId);

           eqId++;
           mdl = testInterface.create(mdl);
           createdRecords.add(mdl);
       }

       //paginate over Routings
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       PaginationContext<EquipmentRoutingRecord> context = new PaginationContext<>(10);
       PaginationResponse<EquipmentRoutingRecord> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<EquipmentRoutingRecord> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<EquipmentRoutingRecord> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<EquipmentRoutingRecord> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<EquipmentRoutingRecord> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<EquipmentRoutingRecord> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<EquipmentRoutingRecord> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       
       List<Long> expectedPage3Longs = new ArrayList<	>(Arrays.asList(new Long[]{20L, 21L, 22L, 23L, 24L, 25L, 26L, 27L, 28L, 29L }));
       List<Long> actualPage3Longs = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Longs.add( ce.getEquipmentId()) );
       
       assertEquals(expectedPage3Longs, actualPage3Longs);

       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<EquipmentRoutingRecord> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<Long> expectedPage1EmptySortLongs = new ArrayList<>(Arrays.asList(new Long[]{0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L }));
       List<Long> actualPage1EmptySortLongs = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortLongs.add( ce.getEquipmentId()) );

       assertEquals(expectedPage1EmptySortLongs, actualPage1EmptySortLongs);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<EquipmentRoutingRecord> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<Long> expectedPage1NullSortLongs = new ArrayList<>(Arrays.asList(new Long[]{0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L }));
       List<Long> actualPage1NullSortLongs = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortLongs.add( ce.getEquipmentId()) );

       assertEquals(expectedPage1NullSortLongs, actualPage1NullSortLongs);

       
       //test first page of the results with sort descending order by a gatewayId property 
       PaginationResponse<EquipmentRoutingRecord> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<Long> expectedPage1SingleSortDescLongs = new ArrayList<	>(Arrays.asList(new Long[]{ 49L, 48L, 47L, 46L, 45L, 44L, 43L, 42L, 41L, 40L }));
       List<Long> actualPage1SingleSortDescLongs = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescLongs.add( ce.getEquipmentId()) );
       
       assertEquals(expectedPage1SingleSortDescLongs, actualPage1SingleSortDescLongs);

       createdRecords.forEach(r -> testInterface.delete(r.getId()));
       testInterface.deleteGateway(gateway.getId());

    }
    
    @Test
    public void testGatewayCRUD(){
    	EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();
    	gateway.setGatewayType(GatewayType.CEGW);
    	gateway.setHostname("test-hostname"+testSequence.incrementAndGet());
    	gateway.setIpAddr("127.0.0.1");
    	gateway.setPort(4242);
    	
        //create
    	EquipmentGatewayRecord created = testInterface.registerGateway(gateway);
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(gateway.getHostname(), created.getHostname());
        assertEquals(gateway.getIpAddr(), created.getIpAddr());
        assertEquals(gateway.getPort(), created.getPort());       
                
        // update
        created.setHostname(created.getHostname() + 1);
        EquipmentGatewayRecord updated = testInterface.updateGateway(created);
        assertNotNull(updated);
        assertEquals(created.getHostname(), updated.getHostname());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
        	created.setHostname(created.getHostname() + 1);
            updated = testInterface.updateGateway(created);
        }

        //UPDATE test - fail because of concurrent modification exception
        try{
        	EquipmentGatewayRecord modelConcurrentUpdate = created.clone();
        	modelConcurrentUpdate.setHostname("42");
        	testInterface.updateGateway(modelConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }
        
        //retrieve
        EquipmentGatewayRecord retrieved = testInterface.getGateway(created.getId());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent
        try {
        	testInterface.getGateway(-1);
        	fail("retrieve non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        List<EquipmentGatewayRecord> retrievedList = testInterface.getGateway(created.getGatewayType());
        assertNotNull(retrievedList);
        assertTrue(retrievedList.contains(retrieved));

        retrievedList = testInterface.getGateway(created.getHostname());
        assertNotNull(retrievedList);
        assertTrue(retrievedList.contains(retrieved));
        
        //delete
        retrieved = testInterface.deleteGateway(created.getId());
        assertNotNull(retrieved);
        
        //delete non-existent
        try {
        	testInterface.deleteGateway(-1);
        	fail("delete non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        //update non-existent
        try {
        	testInterface.updateGateway(retrieved);
        	fail("update non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

    }
    
    @Test
    public void testGatewayRoutes(){
    	EquipmentGatewayRecord gateway1 = new EquipmentGatewayRecord();
    	gateway1.setGatewayType(GatewayType.CEGW);
    	gateway1.setHostname("test-hostname-routes-"+testSequence.incrementAndGet());
    	gateway1.setIpAddr("127.0.0.1");
    	gateway1.setPort(4242);

    	EquipmentGatewayRecord gateway2 = gateway1.clone();
    	EquipmentGatewayRecord gateway3 = gateway1.clone();
    	
    	gateway3.setHostname("test-hostname-routes-3-"+testSequence.incrementAndGet());

        //create a couple of gateways
    	gateway1 = testInterface.registerGateway(gateway1);
    	gateway2 = testInterface.registerGateway(gateway2);
    	gateway3 = testInterface.registerGateway(gateway3);

    	//create some equipment routes
    	EquipmentRoutingRecord routing1_1 = createEquipmentRoutingRecordObject();
    	routing1_1.setGatewayId(gateway1.getId());

    	
    	EquipmentRoutingRecord routing1_2 = createEquipmentRoutingRecordObject();
    	routing1_2.setEquipmentId(routing1_1.getEquipmentId());
    	routing1_2.setGatewayId(gateway2.getId());

    	EquipmentRoutingRecord routing1_3 = createEquipmentRoutingRecordObject();
    	routing1_3.setGatewayId(gateway3.getId());
    	routing1_3.setEquipmentId(routing1_3.getEquipmentId() + 1);

    	routing1_1 = testInterface.create(routing1_1);
    	routing1_2 = testInterface.create(routing1_2);
    	routing1_3 = testInterface.create(routing1_3);


    	//check retrieval of gateways
        List<EquipmentGatewayRecord> retrievedList = testInterface.getGateway(gateway1.getGatewayType());
        assertNotNull(retrievedList);
        assertTrue(retrievedList.contains(gateway1));
        assertTrue(retrievedList.contains(gateway2));

        retrievedList = testInterface.getGateway(gateway1.getHostname());
        assertNotNull(retrievedList);
        assertTrue(retrievedList.contains(gateway1));
        assertTrue(retrievedList.contains(gateway2));

        retrievedList = testInterface.getRegisteredGatewayRecordList(routing1_1.getEquipmentId());
        assertNotNull(retrievedList);
        assertTrue(retrievedList.contains(gateway1));
        assertTrue(retrievedList.contains(gateway2));
        
    	//check retrieval of equipment routes
        List<EquipmentRoutingRecord> retrievedRouteList = testInterface.getRegisteredRouteList(routing1_1.getEquipmentId());
        assertNotNull(retrievedRouteList);
        assertTrue(retrievedRouteList.contains(routing1_1));
        assertTrue(retrievedRouteList.contains(routing1_2));
        assertFalse(retrievedRouteList.contains(routing1_3));

        //delete routes
        testInterface.delete(routing1_1.getId());
    	testInterface.delete(routing1_2.getId());
    	testInterface.delete(routing1_3.getId());

        //delete gateways
        retrievedList = testInterface.deleteGateway(gateway1.getHostname());
        assertNotNull(retrievedList);
        assertTrue(retrievedList.contains(gateway1));
        assertTrue(retrievedList.contains(gateway2));
        
        //verify delete
        try {
        	testInterface.getGateway(gateway1.getId());
        	fail("delete did not work");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        //verify delete
        try {
        	testInterface.getGateway(gateway2.getId());
        	fail("delete did not work");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }
        
        testInterface.deleteGateway(gateway3.getId());

    }
 
    protected EquipmentRoutingRecord createEquipmentRoutingRecordObject() {
    	EquipmentRoutingRecord result = new EquipmentRoutingRecord();
        long nextId = testSequence.getAndIncrement();
        result.setCustomerId((int) nextId);
        result.setEquipmentId(testSequence.getAndIncrement());
        
        return result;
    }
}
