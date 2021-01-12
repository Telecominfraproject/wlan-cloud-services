package com.telecominfraproject.wlan.status.datastore.cassandra;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;
import com.telecominfraproject.wlan.status.datastore.BaseStatusDatastoreTest;

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

    @Override
    protected List<String> getStatusPagination_expectedPage1SingleSortDescStrings(){ 
        return getStatusPagination_expectedPage1EmptySortStrings();
    }
    

    @Override
    protected List<String> getStatusPagination_expectedPage1EmptySortStrings(){
        return Arrays.asList(new String[]{"qr_3", "qr_13", "qr_38", "qr_40", "qr_44", "qr_17", "qr_41", "qr_39", "qr_19", "qr_34" });
    }


    @Override
    protected List<String> getStatusPagination_expectedPage3Strings(){ 
        return Arrays.asList(new String[]{"qr_49", "qr_45", "qr_8", "qr_33", "qr_7", "qr_35", "qr_22", "qr_11", "qr_16", "qr_1" });
    }

}
