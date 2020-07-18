package com.telecominfraproject.wlan.routing.datastore.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;
import com.telecominfraproject.wlan.routing.datastore.BaseRoutingDatastoreTest;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 * NOTE: these tests require an cluster of Cassandra nodes to be up. Need to find a solution for small in-memory cluster for unit tests.
 * We'll not run these tests during the CI/CD build for now, they need to be manually executed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseCassandraTest.Config.class)
@Import(value = { RoutingDatastoreCassandra.class, RoutingDAO.class, GatewayDAO.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class RoutingDatastoreCassandraTests extends BaseRoutingDatastoreTest {

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
       //Note: sort order is ignored by cassandra
       //sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
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
       
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)  -> the sort order is ignored by cassandra datastore
       PaginationResponse<EquipmentRoutingRecord> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<Long> actualPage1EmptySortLongs = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortLongs.add( ce.getEquipmentId()) );


       //test first page of the results with null sort order -> default sort order (by Id ascending)  -> the sort order is ignored by cassandra datastore
       PaginationResponse<EquipmentRoutingRecord> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<Long> expectedPage1NullSortLongs = new ArrayList<>(actualPage1EmptySortLongs);
       List<Long> actualPage1NullSortLongs = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortLongs.add( ce.getEquipmentId()) );

       assertEquals(expectedPage1NullSortLongs, actualPage1NullSortLongs);

       
       //test first page of the results with sort descending order by a gatewayId property  -> the sort order is ignored by cassandra datastore
       PaginationResponse<EquipmentRoutingRecord> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<Long> expectedPage1SingleSortDescLongs = new ArrayList<>(actualPage1EmptySortLongs);
       List<Long> actualPage1SingleSortDescLongs = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescLongs.add( ce.getEquipmentId()) );
       
       assertEquals(expectedPage1SingleSortDescLongs, actualPage1SingleSortDescLongs);

       createdRecords.forEach(r -> testInterface.delete(r.getId()));
       testInterface.deleteGateway(gateway.getId());

    }
}
