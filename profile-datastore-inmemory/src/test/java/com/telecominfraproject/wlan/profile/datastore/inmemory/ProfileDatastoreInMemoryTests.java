package com.telecominfraproject.wlan.profile.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.profile.datastore.BaseProfileDatastoreTest;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequestFactory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ProfileDatastoreInMemoryTests.class)
@Import(value = { ProfileDatastoreInMemory.class , ProfileByCustomerRequestFactory.class})
public class ProfileDatastoreInMemoryTests extends BaseProfileDatastoreTest {

}
