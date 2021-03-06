package com.telecominfraproject.wlan.firmware.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.firmware.datastore.BaseFirmwareDatastoreTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = FirmwareDatastoreInMemoryTests.class)
@Import(value = { FirmwareDatastoreInMemory.class, FirmwareVersionDatastoreInMemory.class,
		FirmwareTrackDatastoreInMemory.class, FirmwareTrackAssignmentDatastoreInMemory.class,
		CustomerFirmwareTrackDatastoreInMemory.class })
public class FirmwareDatastoreInMemoryTests extends BaseFirmwareDatastoreTest {

}
