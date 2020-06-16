package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.server.jdbc.test.BaseJdbcTest;
import com.telecominfraproject.wlan.core.server.jdbc.test.TestWithEmbeddedDB;

import com.telecominfraproject.wlan.firmware.datastore.BaseFirmwareDatastoreTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseJdbcTest.Config.class)
@Rollback(value = true)
@Transactional
@Import(value = { 
        FirmwareDatastoreRdbms.class,
        FirmwareDataSourceConfig.class,
        FirmwareVersionDAO.class,
        FirmwareTrackDAO.class,
        FirmwareTrackAssignmentDAO.class,
        CustomerFirmwareTrackDAO.class,
 })
@TestWithEmbeddedDB
public class FirmwareDatastoreRdbmsTests extends BaseFirmwareDatastoreTest {

}
