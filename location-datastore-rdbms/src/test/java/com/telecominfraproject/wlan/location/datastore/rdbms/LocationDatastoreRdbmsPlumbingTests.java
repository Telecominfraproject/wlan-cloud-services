package com.telecominfraproject.wlan.location.datastore.rdbms;

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
import com.telecominfraproject.wlan.location.models.Location;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        LocationDatastoreRdbms.class,
        LocationDataSourceConfig.class,
        LocationDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class LocationDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private LocationDatastoreRdbms locationDatastore;
    @Autowired private LocationDAO locationDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from equipment_location where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteGeneric() {
                
        //GET by Id test
        Location ret = locationDatastore.get(1L);        

        //DELETE Test
        locationDAO.delete(ret.getId());
        
        try{
            locationDatastore.get(ret.getId());
            fail("failed to delete GenericDbModel");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
