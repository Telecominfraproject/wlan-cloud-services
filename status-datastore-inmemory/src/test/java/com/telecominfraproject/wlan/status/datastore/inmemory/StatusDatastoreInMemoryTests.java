package com.telecominfraproject.wlan.status.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.status.datastore.BaseStatusDatastoreTest;
import com.telecominfraproject.wlan.status.datastore.inmemory.StatusDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = StatusDatastoreInMemoryTests.class)
@Import(value = { StatusDatastoreInMemory.class })
public class StatusDatastoreInMemoryTests extends BaseStatusDatastoreTest {

}
