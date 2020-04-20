package com.telecominfraproject.wlan.equipment.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.equipment.datastore.BaseEquipmentDatastoreTest;
import com.telecominfraproject.wlan.equipment.datastore.inmemory.EquipmentDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = EquipmentDatastoreInMemoryTests.class)
@Import(value = { EquipmentDatastoreInMemory.class })
public class EquipmentDatastoreInMemoryTests extends BaseEquipmentDatastoreTest {

}
