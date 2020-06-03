package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

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
        SystemEventDatastoreRdbms.class,
        SystemEventDataSourceConfig.class,
        SystemEventDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class SystemEventDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            jdbcTemplate.queryForObject(
                    "select count(1) from system_event where customerId = ?  and equipmentId = ? and dataType = ?", 
                    Long.class, 1, 1, "TestSystemEvent");               

            //this is not a stable test, its outcome depends on the order of execution
            //we'll leave it here though - to guard the table name and the key columns

        }
    }
    
}
