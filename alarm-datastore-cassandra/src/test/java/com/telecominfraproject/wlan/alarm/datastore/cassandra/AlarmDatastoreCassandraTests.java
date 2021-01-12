package com.telecominfraproject.wlan.alarm.datastore.cassandra;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.alarm.datastore.BaseAlarmDatastoreTest;
import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;

/**
 * @author dtoptygin
 * NOTE: these tests require an cluster of Cassandra nodes to be up. Need to find a solution for small in-memory cluster for unit tests.
 * We'll not run these tests during the CI/CD build for now, they need to be manually executed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseCassandraTest.Config.class)
@Import(value = { AlarmDatastoreCassandra.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class AlarmDatastoreCassandraTests extends BaseAlarmDatastoreTest {

    @Override
    protected List<String> getAlarmPagination_expectedPage3Strings(){
        //in cassandra the sort order is weird but consistent - although it may change on the cassandra server restart
        return Arrays.asList(new String[]{"qr_6", "qr_2", "qr_26", "qr_43", "qr_41", "qr_3", "qr_49", "qr_40", "qr_24", "qr_28" });
    }

    @Override
    protected List<String> getAlarmPagination_expectedPage1EmptySortStrings() {
        return Arrays.asList(new String[]{"qr_17", "qr_14", "qr_9", "qr_38", "qr_18", "qr_30", "qr_0", "qr_7", "qr_25", "qr_19" });
    }

    @Override
    protected List<String> getAlarmPagination_expectedPage1SingleSortDescStrings(){ 
        return getAlarmPagination_expectedPage1EmptySortStrings();
    }


}
