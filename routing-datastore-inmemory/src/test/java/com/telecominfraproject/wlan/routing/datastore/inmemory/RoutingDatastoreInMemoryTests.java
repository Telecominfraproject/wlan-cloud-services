package com.telecominfraproject.wlan.routing.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.routing.datastore.BaseRoutingDatastoreTest;
import com.telecominfraproject.wlan.routing.datastore.inmemory.RoutingDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = RoutingDatastoreInMemoryTests.class)
@Import(value = { RoutingDatastoreInMemory.class })
public class RoutingDatastoreInMemoryTests extends BaseRoutingDatastoreTest {

}
