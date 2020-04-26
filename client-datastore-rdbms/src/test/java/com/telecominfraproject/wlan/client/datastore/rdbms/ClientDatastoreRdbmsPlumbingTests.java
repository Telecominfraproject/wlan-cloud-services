package com.telecominfraproject.wlan.client.datastore.rdbms;

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

import com.telecominfraproject.wlan.client.models.Client;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        ClientDatastoreRdbms.class,
        ClientDataSourceConfig.class,
        ClientDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class ClientDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private ClientDatastoreRdbms clientDatastore;
    @Autowired private ClientDAO clientDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from client where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteClient() {
                
        //GET by Id test
        Client ret = clientDatastore.get(1L);        

        //DELETE Test
        clientDAO.delete(ret.getId());
        
        try{
            clientDatastore.get(ret.getId());
            fail("failed to delete Client");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
