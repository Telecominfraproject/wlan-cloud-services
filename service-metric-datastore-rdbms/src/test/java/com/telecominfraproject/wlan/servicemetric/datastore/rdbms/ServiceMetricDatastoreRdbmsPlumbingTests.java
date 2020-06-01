package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.telecominfraproject.wlan.core.server.jdbc.test.BaseJdbcTest;
import com.telecominfraproject.wlan.core.server.jdbc.test.TestWithEmbeddedDB;

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
    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select customerId from service_metric where customerid = ? and equipmentId = ? and clientMac = ? and dataType = ?", 
                    Long.class, 1,1,0,1);               
            
            assertEquals((Long)1L, ret);
        }
    }
    
}
