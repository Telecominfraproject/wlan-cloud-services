package com.telecominfraproject.wlan.servicemetric.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.servicemetric.datastore.BaseServiceMetricDatastoreTest;
import com.telecominfraproject.wlan.servicemetric.datastore.inmemory.ServiceMetricDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ServiceMetricDatastoreInMemoryTests.class)
@Import(value = { ServiceMetricDatastoreInMemory.class })
public class ServiceMetricDatastoreInMemoryTests extends BaseServiceMetricDatastoreTest {

}
