package com.telecominfraproject.wlan.portaluser.datastore.rdbms;

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

import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        PortalUserDatastoreRdbms.class,
        PortalUserDataSourceConfig.class,
        PortalUserDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class PortalUserDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private PortalUserDatastoreRdbms portalUserDatastore;
    @Autowired private PortalUserDAO portalUserDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from portal_user where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeletePortalUser() {
                
        //GET by Id test
        PortalUser ret = portalUserDatastore.get(1L);        

        //DELETE Test
        portalUserDAO.delete(ret.getId());
        
        try{
            portalUserDatastore.get(ret.getId());
            fail("failed to delete PortalUser");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
