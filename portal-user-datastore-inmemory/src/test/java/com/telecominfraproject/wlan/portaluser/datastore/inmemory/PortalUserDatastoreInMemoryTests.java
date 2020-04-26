package com.telecominfraproject.wlan.portaluser.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.portaluser.datastore.BasePortalUserDatastoreTest;
import com.telecominfraproject.wlan.portaluser.datastore.inmemory.PortalUserDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = PortalUserDatastoreInMemoryTests.class)
@Import(value = { PortalUserDatastoreInMemory.class })
public class PortalUserDatastoreInMemoryTests extends BasePortalUserDatastoreTest {

}
