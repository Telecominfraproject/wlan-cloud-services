package com.telecominfraproject.wlan.client.datastore.rdbms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.server.jdbc.test.BaseJdbcTest;
import com.telecominfraproject.wlan.core.server.jdbc.test.TestWithEmbeddedDB;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        ClientDatastoreRdbms.class,
        ClientDataSourceConfig.class,
        ClientDAO.class,
        ClientSessionDAO.class,
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
                    "select macAddress from client where customerId = ? and macAddress = ?", 
                    Long.class, 1, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteClient() {
                
        //GET by Id test
        Client ret = clientDatastore.getOrNull(1, new MacAddress(1L));        

        //DELETE Test
        clientDAO.delete(ret.getCustomerId(), ret.getMacAddress());
      
        assertNull(clientDatastore.getOrNull(ret.getCustomerId(), ret.getMacAddress()));
                
    }
    
}
