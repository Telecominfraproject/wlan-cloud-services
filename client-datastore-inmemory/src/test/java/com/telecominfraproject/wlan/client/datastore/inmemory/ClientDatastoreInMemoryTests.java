package com.telecominfraproject.wlan.client.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.client.datastore.BaseClientDatastoreTest;
import com.telecominfraproject.wlan.client.datastore.inmemory.ClientDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ClientDatastoreInMemoryTests.class)
@Import(value = { ClientDatastoreInMemory.class })
public class ClientDatastoreInMemoryTests extends BaseClientDatastoreTest {

}
