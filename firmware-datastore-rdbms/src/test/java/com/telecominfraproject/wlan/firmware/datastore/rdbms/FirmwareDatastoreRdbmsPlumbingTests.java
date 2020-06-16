package com.telecominfraproject.wlan.firmware.datastore.rdbms;

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
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        FirmwareDatastoreRdbms.class,
        FirmwareDataSourceConfig.class,
        FirmwareVersionDAO.class,
        FirmwareTrackDAO.class,
        FirmwareTrackAssignmentDAO.class,
        CustomerFirmwareTrackDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class FirmwareDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private FirmwareDatastoreRdbms firmwareDatastore;
    @Autowired private FirmwareVersionDAO firmwareDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from firmware_version where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteFirmware() {
                
        //GET by Id test
        FirmwareVersion ret = firmwareDatastore.get(1L);        

        //DELETE Test
        firmwareDAO.delete(ret.getId());
        
        try{
            firmwareDatastore.get(ret.getId());
            fail("failed to delete Firmware");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
