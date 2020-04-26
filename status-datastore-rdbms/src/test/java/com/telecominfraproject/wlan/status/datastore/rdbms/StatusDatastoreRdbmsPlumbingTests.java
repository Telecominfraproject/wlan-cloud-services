package com.telecominfraproject.wlan.status.datastore.rdbms;

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

import com.telecominfraproject.wlan.status.models.Status;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        StatusDatastoreRdbms.class,
        StatusDataSourceConfig.class,
        StatusDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class StatusDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private StatusDatastoreRdbms statusDatastore;
    @Autowired private StatusDAO statusDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from status where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteStatus() {
                
        //GET by Id test
        Status ret = statusDatastore.get(1L);        

        //DELETE Test
        statusDAO.delete(ret.getId());
        
        try{
            statusDatastore.get(ret.getId());
            fail("failed to delete Status");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
