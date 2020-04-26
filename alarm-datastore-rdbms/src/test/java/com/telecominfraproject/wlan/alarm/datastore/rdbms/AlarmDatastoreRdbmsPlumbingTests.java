package com.telecominfraproject.wlan.alarm.datastore.rdbms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.telecominfraproject.wlan.core.server.jdbc.test.BaseJdbcTest;
import com.telecominfraproject.wlan.core.server.jdbc.test.TestWithEmbeddedDB;

import com.telecominfraproject.wlan.alarm.models.Alarm;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        AlarmDatastoreRdbms.class,
        AlarmDataSourceConfig.class,
        AlarmDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class AlarmDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private AlarmDatastoreRdbms alarmDatastore;
    @Autowired private AlarmDAO alarmDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from alarm where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteAlarm() {
                
        //GET by Id test
        Alarm ret = alarmDatastore.get(1L);        

        //DELETE Test
        alarmDAO.delete(ret.getId());
        
        try{
            alarmDatastore.get(ret.getId());
            fail("failed to delete Alarm");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
