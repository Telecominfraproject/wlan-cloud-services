package com.telecominfraproject.wlan.client.datastore.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.client.datastore.BaseClientDatastoreTest;
import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;

/**
 * @author dtoptygin
 * NOTE: these tests require an cluster of Cassandra nodes to be up. Need to find a solution for small in-memory cluster for unit tests.
 * We'll not run these tests during the CI/CD build for now, they need to be manually executed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseCassandraTest.Config.class)
@Import(value = { ClientDatastoreCassandra.class, ClientDAO.class, ClientSessionDAO.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class ClientDatastoreCassandraTests extends BaseClientDatastoreTest {
	

    @Override
    protected List<String> getClientPagination_ExpectedPage3Strings() {
        //in cassandra the sort order is weird but consistent - although it may change on the cassandra server restart
        return new ArrayList<>(Arrays.asList(new String[]{"qr_10", "qr_13", "qr_8", "qr_18", "qr_42", "qr_30", "qr_35", "qr_28", "qr_14", "qr_22" }));
    }

    @Override
    protected List<String> getClientPagination_ExpectedPage1EmptySortStrings(){
        return new ArrayList<>(Arrays.asList(new String[]{"qr_32", "qr_0", "qr_12", "qr_25", "qr_40", "qr_39", "qr_29", "qr_41", "qr_4", "qr_44" }));
    }



    @Override
    protected List<String> getClientPagination_ExpectedPage1SingleSortDescStrings() {
        return getClientPagination_ExpectedPage1EmptySortStrings();
    }
    
    
    @Override
    protected List<String> getClientSessionPagination_expectedPage1EmptySortStrings(){
        return new ArrayList<>(Arrays.asList(new String[]{"qr_12", "qr_47", "qr_4", "qr_1", "qr_21", "qr_42", "qr_45", "qr_35", "qr_14", "qr_0" }));
    }
    
    @Override
    protected List<String> getClientSessionPagination_expectedPage3Strings(){
        return new ArrayList<>(Arrays.asList(new String[]{"qr_28", "qr_32", "qr_6", "qr_19", "qr_49", "qr_31", "qr_33", "qr_25", "qr_7", "qr_40" }));
    }

    @Override
    protected List<String> getClientSessionPagination_expectedPage1SingleSortDescStrings() { 
        return getClientSessionPagination_expectedPage1EmptySortStrings();
    }
    

}
