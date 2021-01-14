package com.telecominfraproject.wlan.routing.datastore.cassandra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;
import com.telecominfraproject.wlan.routing.datastore.BaseRoutingDatastoreTest;
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
 
    @Override
    protected List<Long> getRoutingPagination_expectedPage1SingleSortDescLongs(int customerId){ 
        return getRoutingPagination_expectedPage1EmptySortLongs(customerId);
    }
    
    @Override
    protected List<Long> getRoutingPagination_expectedPage1EmptySortLongs(int customerId){

        List<Long> actualPage1EmptySortLongs = new ArrayList<>();
        testInterface
                .getForCustomer(customerId, Collections.emptyList(), new PaginationContext<EquipmentRoutingRecord>(10))
                .getItems().forEach(ce -> actualPage1EmptySortLongs.add(ce.getEquipmentId()));

        return actualPage1EmptySortLongs;
    }
    
    @Override
    protected List<Long> getRoutingPagination_expectedPage3Longs(){ 
        return Collections.emptyList();
    }
    
}
