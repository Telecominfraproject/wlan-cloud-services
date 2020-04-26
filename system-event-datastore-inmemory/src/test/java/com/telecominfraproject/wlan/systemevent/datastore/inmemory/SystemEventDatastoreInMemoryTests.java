package com.telecominfraproject.wlan.systemevent.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.systemevent.datastore.BaseSystemEventDatastoreTest;
import com.telecominfraproject.wlan.systemevent.datastore.inmemory.SystemEventDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SystemEventDatastoreInMemoryTests.class)
@Import(value = { SystemEventDatastoreInMemory.class })
public class SystemEventDatastoreInMemoryTests extends BaseSystemEventDatastoreTest {

}
