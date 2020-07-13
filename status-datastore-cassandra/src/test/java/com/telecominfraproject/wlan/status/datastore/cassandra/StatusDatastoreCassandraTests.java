package com.telecominfraproject.wlan.status.datastore.cassandra;

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
import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;
import com.telecominfraproject.wlan.status.datastore.BaseStatusDatastoreTest;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentAdminStatusData;
import com.telecominfraproject.wlan.status.models.Status;

/**
 * @author dtoptygin
 * NOTE: these tests require an cluster of Cassandra nodes to be up. Need to find a solution for small in-memory cluster for unit tests.
 * We'll not run these tests during the CI/CD build for now, they need to be manually executed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseCassandraTest.Config.class)
@Import(value = { StatusDatastoreCassandra.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class StatusDatastoreCassandraTests extends BaseStatusDatastoreTest {
	
	//NOTE: Even though every test case cleans up after itself, some extra steps are needed between successful test suite runs.
	//Cassandra is weird - after each successful run of the test suite we must either truncate the table of run nodetool flush:
	//
	// tip_wlan@cqlsh> truncate table tip_wlan_keyspace.status;
	//    or
	// $ bin/nodetool flush
	// 
	// See https://thelastpickle.com/blog/2016/07/27/about-deletes-and-tombstones.html for more details

    @Test
    @Override
    //Cassandra has limited support for sort options, reflect it in the test
    public void testStatusPagination()
    {
       //create 100 Statuses
       Status mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       List<Status> allCreatedStatuses = new ArrayList<>(); 

       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = createStatusObject();
           mdl.setCustomerId(customerId_1);
           ((EquipmentAdminStatusData) mdl.getDetails()).setStatusMessage("qr_"+apNameIdx);

           apNameIdx++;
           mdl = testInterface.update(mdl);
           allCreatedStatuses.add(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = createStatusObject();
           mdl.setCustomerId(customerId_2);
           ((EquipmentAdminStatusData) mdl.getDetails()).setStatusMessage("qr_"+apNameIdx);

           apNameIdx++;
           mdl = testInterface.update(mdl);
           allCreatedStatuses.add(mdl);
       }

       //paginate over Statuses
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       //NOTE: sort order is ignored by the Cassandra DAO
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       PaginationContext<Status> context = new PaginationContext<>(10);
       PaginationResponse<Status> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Status> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Status> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Status> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Status> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Status> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Status> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       
       //NOTE: because sort options are ignored by cassandra dao, the results come in weird but stable order:
       List<String> expectedPage3Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_49", "qr_45", "qr_8", "qr_33", "qr_7", "qr_35", "qr_22", "qr_11", "qr_16", "qr_1" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Status> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       //NOTE: because sort options are ignored by cassandra dao, the results come in weird but stable order:
       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_3", "qr_13", "qr_38", "qr_40", "qr_44", "qr_17", "qr_41", "qr_39", "qr_19", "qr_34" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Status> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       //NOTE: because sort options are ignored by cassandra dao, the results come in weird but stable order:
       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_3", "qr_13", "qr_38", "qr_40", "qr_44", "qr_17", "qr_41", "qr_39", "qr_19", "qr_34" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a equipmentId property 
       PaginationResponse<Status> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       //NOTE: because sort options are ignored by cassandra dao, the results come in weird but stable order:
       List<String> expectedPage1SingleSortDescStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_3", "qr_13", "qr_38", "qr_40", "qr_44", "qr_17", "qr_41", "qr_39", "qr_19", "qr_34" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

       //delete
       allCreatedStatuses.forEach(s ->  testInterface.delete(s.getCustomerId(), s.getEquipmentId()));

    }
}
