package com.telecominfraproject.wlan.equipment.datastore.rdbms;

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

import com.telecominfraproject.wlan.equipment.models.Equipment;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        EquipmentDatastoreRdbms.class,
        EquipmentDataSourceConfig.class,
        EquipmentDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class EquipmentDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private EquipmentDatastoreRdbms equipmentDatastore;
    @Autowired private EquipmentDAO equipmentDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from equipment where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteEquipment() {
                
        //GET by Id test
        Equipment ret = equipmentDatastore.get(1L);        

        //DELETE Test
        equipmentDAO.delete(ret.getId());
        
        try{
            equipmentDatastore.get(ret.getId());
            fail("failed to delete Equipment");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
