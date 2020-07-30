package com.telecominfraproject.wlan.adoptionmetrics.datastore.rdbms;

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
        AdoptionMetricsDatastoreRdbms.class,
        AdoptionMetricsDataSourceConfig.class,
        AdoptionMetricsDAO.class,
        BaseJdbcTest.Config.class
        })
@TestWithEmbeddedDB
public class AdoptionMetricsDatastoreRdbmsPlumbingTests extends BaseJdbcTest {
    
    @Autowired(required=false) private EmbeddedDatabase db;
    @Autowired private AdoptionMetricsDatastoreRdbms adoptionMetricsDatastore;
    @Autowired private AdoptionMetricsDAO adoptionMetricsDAO;

    
    @Before
    public void setUp() {
    }

    @Test
    public void testDataAccess() {
        if(db!=null){
            //this is a simple test to see if embedded db is working in test environment
            JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
            Long ret = jdbcTemplate.queryForObject(
                    "select numUniqueConnectedMacs from adoption_metrics_counters where "
                    + "year = ? and month = ? and weekOfYear = ? and dayOfYear = ?  "
                    + "and customerId = ? and locationId = ? and equipmentId = ?", 
                    Long.class, 2000, 1, 1, 1, 1, 1, 1);               
            
            assertEquals((Long)10L, ret);
        }
    }

        
}
