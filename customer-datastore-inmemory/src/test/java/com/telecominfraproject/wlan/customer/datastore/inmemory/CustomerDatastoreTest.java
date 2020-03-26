package com.telecominfraproject.wlan.customer.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.customer.datastore.BaseCustomerDatastoreTest;

/**
 * @author dtop
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = CustomerDatastoreTest.class)
@Import(value = { CustomerDatastoreInMemory.class })
public class CustomerDatastoreTest extends BaseCustomerDatastoreTest {

}
