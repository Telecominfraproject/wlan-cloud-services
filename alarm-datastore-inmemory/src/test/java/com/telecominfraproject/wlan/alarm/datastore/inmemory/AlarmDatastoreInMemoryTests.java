package com.telecominfraproject.wlan.alarm.datastore.inmemory;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.alarm.datastore.BaseAlarmDatastoreTest;
import com.telecominfraproject.wlan.alarm.datastore.inmemory.AlarmDatastoreInMemory;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = AlarmDatastoreInMemoryTests.class)
@Import(value = { AlarmDatastoreInMemory.class })
public class AlarmDatastoreInMemoryTests extends BaseAlarmDatastoreTest {

}
