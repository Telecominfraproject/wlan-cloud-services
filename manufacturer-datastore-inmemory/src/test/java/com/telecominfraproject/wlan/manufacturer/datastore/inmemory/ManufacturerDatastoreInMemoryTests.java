package com.telecominfraproject.wlan.manufacturer.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.manufacturer.datastore.BaseManufacturerDatastoreTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ManufacturerDatastoreInMemoryTests.class)
@Import(value = { ManufacturerDatastoreInMemory.class })
public class ManufacturerDatastoreInMemoryTests extends BaseManufacturerDatastoreTest {

}
