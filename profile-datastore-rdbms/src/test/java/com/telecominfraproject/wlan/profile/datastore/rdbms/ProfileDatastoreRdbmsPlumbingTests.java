package com.telecominfraproject.wlan.profile.datastore.rdbms;

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

import com.telecominfraproject.wlan.profile.models.Profile;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        ProfileDatastoreRdbms.class,
        ProfileDataSourceConfig.class,
        ProfileDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class ProfileDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private ProfileDatastoreRdbms profileDatastore;
    @Autowired private ProfileDAO profileDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from profile where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteProfile() {
                
        //GET by Id test
        Profile ret = profileDatastore.get(1L);        

        //DELETE Test
        profileDAO.delete(ret.getId());
        
        try{
            profileDatastore.get(ret.getId());
            fail("failed to delete Profile");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
