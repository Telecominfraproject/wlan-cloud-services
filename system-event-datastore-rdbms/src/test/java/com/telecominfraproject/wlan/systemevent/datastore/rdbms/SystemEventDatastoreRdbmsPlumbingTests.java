package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

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

import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        SystemEventDatastoreRdbms.class,
        SystemEventDataSourceConfig.class,
        SystemEventDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class SystemEventDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private SystemEventDatastoreRdbms systemEventDatastore;
    @Autowired private SystemEventDAO systemEventRecordDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from system_event where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteSystemEventRecord() {
                
        //GET by Id test
        SystemEventContainer ret = systemEventDatastore.get(1L);        

        //DELETE Test
        systemEventRecordDAO.delete(ret.getId());
        
        try{
            systemEventDatastore.get(ret.getId());
            fail("failed to delete SystemEventContainer");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
