package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

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

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        ManufacturerDatastoreRdbms.class,
        ManufacturerDataSourceConfig.class,
        ManufacturerDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class ManufacturerDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private ManufacturerDatastoreRdbms manufacturerDatastore;
    @Autowired private ManufacturerDAO manufacturerDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from manufacturer where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteManufacturer() {
                
        //GET by Id test
        Manufacturer ret = manufacturerDatastore.get(1L);        

        //DELETE Test
        manufacturerDAO.delete(ret.getId());
        
        try{
            manufacturerDatastore.get(ret.getId());
            fail("failed to delete Manufacturer");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
