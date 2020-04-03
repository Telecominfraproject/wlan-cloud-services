package com.telecominfraproject.wlan.location.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.location.datastore.BaseLocationDatastoreTest;
import com.telecominfraproject.wlan.location.datastore.inmemory.LocationDatastoreInMemory;

/**
 * @author dtop
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = LocationDatastoreTest.class)
@Import(value = { LocationDatastoreInMemory.class })
public class LocationDatastoreTest extends BaseLocationDatastoreTest {

}
