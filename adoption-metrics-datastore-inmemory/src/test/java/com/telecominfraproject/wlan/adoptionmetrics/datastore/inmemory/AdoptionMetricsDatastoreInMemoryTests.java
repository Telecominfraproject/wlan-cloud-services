package com.telecominfraproject.wlan.adoptionmetrics.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.adoptionmetrics.datastore.BaseAdoptionMetricsDatastoreTest;
import com.telecominfraproject.wlan.adoptionmetrics.datastore.inmemory.AdoptionMetricsDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = AdoptionMetricsDatastoreInMemoryTests.class)
@Import(value = { AdoptionMetricsDatastoreInMemory.class })
public class AdoptionMetricsDatastoreInMemoryTests extends BaseAdoptionMetricsDatastoreTest {

}
