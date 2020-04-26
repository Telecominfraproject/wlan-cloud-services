package com.telecominfraproject.wlan.routing.datastore.rdbms;

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

import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        RoutingDatastoreRdbms.class,
        RoutingDataSourceConfig.class,
        RoutingDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class RoutingDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private RoutingDatastoreRdbms routingDatastore;
    @Autowired private RoutingDAO routingDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from routing where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteRouting() {
                
        //GET by Id test
        Routing ret = routingDatastore.get(1L);        

        //DELETE Test
        routingDAO.delete(ret.getId());
        
        try{
            routingDatastore.get(ret.getId());
            fail("failed to delete Routing");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
