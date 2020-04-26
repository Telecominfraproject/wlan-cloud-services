package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

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

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

/**
 * @author dtoptygin
 *
 */
@Import(value = {
        ServiceMetricDatastoreRdbms.class,
        ServiceMetricDataSourceConfig.class,
        ServiceMetricDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class ServiceMetricDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private ServiceMetricDatastoreRdbms serviceMetricDatastore;
    @Autowired private ServiceMetricDAO serviceMetricDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select id from service_metric where id = ?", 
                    Long.class, 1);               
            
            assertEquals((Long)1L, ret);
        }
    }

    
    @Test
    public void testCreateUpdateDeleteServiceMetric() {
                
        //GET by Id test
        ServiceMetric ret = serviceMetricDatastore.get(1L);        

        //DELETE Test
        serviceMetricDAO.delete(ret.getId());
        
        try{
            serviceMetricDatastore.get(ret.getId());
            fail("failed to delete ServiceMetric");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
}
