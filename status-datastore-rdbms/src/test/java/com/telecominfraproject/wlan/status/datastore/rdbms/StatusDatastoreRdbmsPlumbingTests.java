package com.telecominfraproject.wlan.status.datastore.rdbms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.telecominfraproject.wlan.status.models.StatusDataType;

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
            Integer ret = jdbcTemplate.queryForObject(
                    "select customerId from status where customerid = ? and equipmentid = ? and statusDataType = ?", 
                    Integer.class, 1, 1L, 1);
            
            assertEquals(1, ret.intValue());
        }
    }

    
    @Test
    public void testCreateUpdateDeleteStatus() {
                
        //GET by Id test
        Status ret = statusDatastore.getOrNull(1,1L, StatusDataType.getById(1));        

        //DELETE Test
        statusDAO.delete(ret.getCustomerId(), ret.getEquipmentId(), null);
        
       	assertNull(statusDatastore.getOrNull(1,1L, StatusDataType.getById(1)));
                
    }
    
}
