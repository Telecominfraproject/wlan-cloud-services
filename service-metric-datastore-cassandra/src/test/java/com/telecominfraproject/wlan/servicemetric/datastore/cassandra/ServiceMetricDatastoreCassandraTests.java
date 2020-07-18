package com.telecominfraproject.wlan.servicemetric.datastore.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.telecominfraproject.wlan.servicemetric.client.models.ClientMetrics;
import com.telecominfraproject.wlan.servicemetric.datastore.BaseServiceMetricDatastoreTest;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

/**
 * @author dtoptygin
 * NOTE: these tests require an cluster of Cassandra nodes to be up. Need to find a solution for small in-memory cluster for unit tests.
 * We'll not run these tests during the CI/CD build for now, they need to be manually executed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseCassandraTest.Config.class)
@Import(value = { ServiceMetricDatastoreCassandra.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class ServiceMetricDatastoreCassandraTests extends BaseServiceMetricDatastoreTest {
	
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
        PaginationResponse<ServiceMetric> page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, context);
        PaginationResponse<ServiceMetric> page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, page1.getContext());
        PaginationResponse<ServiceMetric> page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, page2.getContext());
        PaginationResponse<ServiceMetric> page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, page3.getContext());
        PaginationResponse<ServiceMetric> page5 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, page4.getContext());
        PaginationResponse<ServiceMetric> page6 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, page5.getContext());
        PaginationResponse<ServiceMetric> page7 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, sortBy, page6.getContext());
        
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
        PaginationResponse<ServiceMetric> page1EmptySort = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, Collections.emptyList(), context);
        assertEquals(10, page1EmptySort.getItems().size());

        List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1EmptySortStrings = new ArrayList<>();
        page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((ClientMetrics) ce.getDetails()).getClassificationName() ) );

        assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

        //test first page of the results with null sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<ServiceMetric> page1NullSort = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, context);
        assertEquals(10, page1NullSort.getItems().size());

        List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1NullSortStrings = new ArrayList<>();
        page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((ClientMetrics) ce.getDetails()).getClassificationName() ) );

        assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

        
        //test first page of the results with sort descending order by a equipmentId property  -> cassandra ignores supplied sort order
        PaginationResponse<ServiceMetric> page1SingleSortDesc = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
        assertEquals(10, page1SingleSortDesc.getItems().size());

        List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9"  }));
        List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
        page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((ClientMetrics) ce.getDetails()).getClassificationName() ) );
        
        assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);        

        used_equipmentIds.forEach(eqId -> testInterface.delete(customerId_1, eqId, System.currentTimeMillis()));
        used_equipmentIds.forEach(eqId -> testInterface.delete(customerId_2, eqId, System.currentTimeMillis()));

     }
}
