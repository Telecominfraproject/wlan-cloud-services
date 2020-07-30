package com.telecominfraproject.wlan.adoptionmetrics.datastore.rdbms;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.core.model.utils.DateTimeUtils;
import com.telecominfraproject.wlan.core.server.jdbc.BaseDataSourceConfig;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class AdoptionMetricsDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsDatastoreRdbms.class);
    
    private static final String[] ALL_COLUMNS_LIST = {        
        
        //TODO: add columns from properties of ServiceAdoptionMetrics in here
        //make sure the order of properties matches this list and list in AdoptionMetricsRowMapper and list in create/update methods
        "year",
        "month",
        "weekOfYear",
        "dayOfYear",
        
        "customerId",
        "locationId",        
        "equipmentId",
        
        "numUniqueConnectedMacs",        
        "numBytesUpstream",
        "numBytesDownstream"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>();
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(
            "year",
            "month",
            "weekOfYear",
            "dayOfYear",
            
            "customerId",
            "locationId",        
            "equipmentId"));
    
    private static final String TABLE_NAME = "adoption_metrics_counters";
    private static final String TABLE_PREFIX = "s.";
    private static final String ALL_COLUMNS;

    private static final Set<String> ALL_COLUMNS_LOWERCASE = new HashSet<>();

    @SuppressWarnings("unused")
    //use this for queries where multiple tables are involved
    private static final String ALL_COLUMNS_WITH_PREFIX;
    
    private static final String ALL_COLUMNS_FOR_INSERT; 
    private static final String BIND_VARS_FOR_INSERT;
    private static final String ALL_COLUMNS_UPDATE;
    
    static{
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsWithPrefix = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        StringBuilder strbColumnsForUpdate = new StringBuilder(512);
        for(String colName: ALL_COLUMNS_LIST){

            ALL_COLUMNS_LOWERCASE.add(colName.toLowerCase());

            strbAllColumns.append(colName).append(","); 
            strbAllColumnsWithPrefix.append(TABLE_PREFIX).append(colName).append(",");
            
            if(!columnsToSkipForInsert.contains(colName)){
                strbAllColumnsForInsert.append(colName).append(",");
                strbBindVarsForInsert.append("?,");
            }
            
            if(!columnsToSkipForUpdate.contains(colName)){
                strbColumnsForUpdate.append(colName).append("=?,");
            }
        }

        // remove trailing ','
        strbAllColumns.deleteCharAt(strbAllColumns.length() - 1);
        strbAllColumnsWithPrefix.deleteCharAt(strbAllColumnsWithPrefix.length() - 1);
        strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
        strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
        strbColumnsForUpdate.deleteCharAt(strbColumnsForUpdate.length() - 1);

        ALL_COLUMNS = strbAllColumns.toString();
        ALL_COLUMNS_WITH_PREFIX = strbAllColumnsWithPrefix.toString();
        ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
        BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
        ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();
        
    }
    

    private static final String SQL_GET_DAILY_BY_AP =
        "select " + ALL_COLUMNS +
        " from "+TABLE_NAME+" " +
        " where year = ? and equipmentId in ";
    private static final String SQL_GET_DAILY_BY_AP_FOOTER = " order by year, dayOfYear ";

    private static final String SQL_GET_DAILY_BY_LOCATION =
            "select year, month, weekOfYear, dayOfYear, customerId, locationId, 0 as equipmentId, sum(numUniqueConnectedMacs) as macs, sum(numBytesUpstream) as bytesUp, sum(numBytesDownstream) as bytesDown "
            + " from adoption_metrics_counters " +
            " where year = ? and locationId in ";
    private static final String SQL_GET_DAILY_BY_LOCATION_FOOTER =" group by year, month, weekOfYear, dayOfYear, customerId, locationId order by customerId, locationId, year, dayOfYear ";
    
    private static final String SQL_GET_DAILY_BY_CUSTOMER =
            " select year, month, weekOfYear, dayOfYear, customerId, 0 as locationId, 0 as equipmentId, sum(numUniqueConnectedMacs) as macs, sum(numBytesUpstream) as bytesUp, sum(numBytesDownstream) as bytesDown "
            + "from adoption_metrics_counters "
            + "where year = ? and customerId in ";
    private static final String SQL_GET_DAILY_BY_CUSTOMER_FOOTER = " group by year, month, weekOfYear, dayOfYear, customerId order by customerId, year, dayOfYear ";

    private static final String SQL_GET_ALL_BY_MONTH =
            "select year, month, -1 as weekOfYear, -1 as dayOfYear, 0 as customerId, 0 as locationId, 0 as equipmentId, sum(numUniqueConnectedMacs) as macs, sum(numBytesUpstream) as bytesUp, sum(numBytesDownstream) as bytesDown "
            + "from adoption_metrics_counters "
            + "where year = ? "
            + "group by year, month "
            + "order by year, month ";

    private static final String SQL_GET_ALL_BY_WEEK =
            "select year, month, weekOfYear, -1 as dayOfYear, 0 as customerId, 0 as locationId, 0 as equipmentId, sum(numUniqueConnectedMacs) as macs, sum(numBytesUpstream) as bytesUp, sum(numBytesDownstream) as bytesDown "
            + "from adoption_metrics_counters "
            + "where year = ? "
            + "group by year, month, weekOfYear "
            + "order by year, month, weekOfYear ";
    
    private static final String SQL_GET_ALL_BY_DAY = 
            "select year, month, weekOfYear, dayOfYear, 0 as customerId, 0 as locationId, 0 as equipmentId, sum(numUniqueConnectedMacs) as macs, sum(numBytesUpstream) as bytesUp, sum(numBytesDownstream) as bytesDown "
            + "from adoption_metrics_counters "
            + "where year = ? "
            + "group by year, month, weekOfYear, dayOfYear "
            + "order by year, dayOfYear ";

    
    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_UPDATE =
        "update adoption_metrics_counters "
        + "set numUniqueConnectedMacs = ? , numBytesUpstream = numBytesUpstream + ? , numBytesDownstream = numBytesDownstream + ? "
        + "where year = ? and month = ? and weekOfYear = ? and dayOfYear = ?  and customerId = ? and locationId = ? and equipmentId = ? "
        ;

    private static final String SQL_INSERT_UNIQ_MAC = 
            "insert into adoption_metrics_uniq_macs (metric_timestamp, customerId, locationId, equipmentId, clientMac ) values (?, ?, ?, ?, ?)  "
            ;
    private static final String SQL_INSERT_ON_CONFLICT_DO_NOTHING_FOOTER =" ON CONFLICT DO NOTHING ";
    
    private static final String SQL_COUNT_UNIQ_MACS = 
            "SELECT COUNT(*) FROM (SELECT DISTINCT clientMac FROM adoption_metrics_uniq_macs where metric_timestamp >= ? and metric_timestamp <= ? and customerId = ? and locationId = ? and equipmentId = ?) AS temp "
            ;

    private static final String SQL_DELETE_UNIQ_MACS = 
            "delete FROM adoption_metrics_uniq_macs where metric_timestamp <= ? and customerId = ? and locationId = ? and equipmentId = ? "
            ; 
    
    private static final RowMapper<ServiceAdoptionMetrics> serviceAdoptionMetricsRowMapper = new AdoptionMetricsRowMapper();

    private boolean usingPostgresDb = false;

    @Autowired(required=false)
    public void setDataSource(AdoptionMetricsDataSourceInterface dataSource, Environment env, BaseDataSourceConfig dsConfig) {
        setDataSource((DataSource)dataSource);
        //determine if we're dealing with the postgres DB - we want to take advantage of "INSERT ... ON CONFLICT DO NOTHING" feature
        String dbUrl = env.getProperty(dsConfig.getDataSourceName() + ".url", "");
        if(dbUrl.toLowerCase().contains("portgres")) {
            usingPostgresDb = true; 
        }
    }

    @Transactional(noRollbackFor = { DuplicateKeyException.class })
    public void update(List<ServiceAdoptionMetrics> serviceAdoptionMetricsList) {

        if(serviceAdoptionMetricsList == null || serviceAdoptionMetricsList.isEmpty()) {
            return;
        }
        
        int batchSize = 100;
        //try update first, see what records were not updated, then insert those records
        int[][] updateResult = this.jdbcTemplate.batchUpdate(SQL_UPDATE, serviceAdoptionMetricsList, batchSize,
                new ParameterizedPreparedStatementSetter<ServiceAdoptionMetrics>() {
                    @Override
                    public void setValues(PreparedStatement ps, ServiceAdoptionMetrics metric) throws SQLException {

                        int colIdx = 1;
                        
                        ps.setLong(colIdx++, metric.getNumUniqueConnectedMacs());
                        ps.setLong(colIdx++, metric.getNumBytesUpstream());
                        ps.setLong(colIdx++, metric.getNumBytesDownstream());

                        ps.setInt(colIdx++, metric.getYear());
                        ps.setInt(colIdx++, metric.getMonth());
                        ps.setInt(colIdx++, metric.getWeekOfYear());
                        ps.setInt(colIdx++, metric.getDayOfYear());

                        ps.setInt(colIdx++, metric.getCustomerId());
                        ps.setLong(colIdx++, metric.getLocationId());
                        ps.setLong(colIdx++, metric.getEquipmentId());
                        
                    }
                });

        
        List<ServiceAdoptionMetrics> metricsToInsert = new ArrayList<>();
        int idx = 0;
        //populate metricsToInsert from updateResult where value is 0:
        for(int[] singleBatchResult : updateResult) {
            for(int singleResult : singleBatchResult) {
                if(singleResult ==0) {
                    //record was not updated, add it into the insert list
                    metricsToInsert.add(serviceAdoptionMetricsList.get(idx));
                }
                idx++;
                if(idx >= serviceAdoptionMetricsList.size()) {
                    break;
                }
            }
        }

        LOG.debug("Updated {} metrics, need to insert {} metrics", serviceAdoptionMetricsList.size() - metricsToInsert.size(), metricsToInsert.size());

        //insert not-updated records
        if(!metricsToInsert.isEmpty()) {
            try{
                
                this.jdbcTemplate.batchUpdate(SQL_INSERT + (usingPostgresDb?SQL_INSERT_ON_CONFLICT_DO_NOTHING_FOOTER:""), metricsToInsert, batchSize,
                        new ParameterizedPreparedStatementSetter<ServiceAdoptionMetrics>() {
                            @Override
                            public void setValues(PreparedStatement ps, ServiceAdoptionMetrics metric) throws SQLException {
    
                                int colIdx = 1;
                                
                                //TODO: add remaining properties from ServiceAdoptionMetrics here 
                                ps.setInt(colIdx++, metric.getYear());
                                ps.setInt(colIdx++, metric.getMonth());
                                ps.setInt(colIdx++, metric.getWeekOfYear());
                                ps.setInt(colIdx++, metric.getDayOfYear());
    
                                ps.setInt(colIdx++, metric.getCustomerId());
                                ps.setLong(colIdx++, metric.getLocationId());
                                ps.setLong(colIdx++, metric.getEquipmentId());
                                
                                ps.setLong(colIdx++, metric.getNumUniqueConnectedMacs());
                                ps.setLong(colIdx++, metric.getNumBytesUpstream());
                                ps.setLong(colIdx++, metric.getNumBytesDownstream());
                                
                            }
                        });
    
            }catch (DuplicateKeyException e) {
                LOG.warn("Exception when inserting ServiceAdoptionMetrics ", e);
            }
        }
        

        LOG.debug("Stored {} ServiceAdoptionMetrics", serviceAdoptionMetricsList.size());
    }


    public List<ServiceAdoptionMetrics> get(int year, Set<Long> equipmentIds) {
        LOG.debug("calling get({}, {})", year, equipmentIds);

        if (equipmentIds == null || equipmentIds.isEmpty()) {
            throw new IllegalArgumentException("equipmentIds must be provided");
        }

        StringBuilder setStr = new StringBuilder(256);
        setStr.append("(");
        for(int i =0; i< equipmentIds.size(); i++) {
                setStr.append("?,");
        }
        //remove last comma
        setStr.deleteCharAt(setStr.length()-1);
        setStr.append(")");
        
        String query = SQL_GET_DAILY_BY_AP + setStr + SQL_GET_DAILY_BY_AP_FOOTER;
        List<Object> bindVars = new ArrayList<>();
        bindVars.add(year);
        bindVars.addAll(equipmentIds);
        
        List<ServiceAdoptionMetrics> results = this.jdbcTemplate.query(query, bindVars.toArray(), serviceAdoptionMetricsRowMapper);

        LOG.debug("get({}, {}) returns {} record(s)", year, equipmentIds, results.size());
        
        return results;
    }


    public List<ServiceAdoptionMetrics> getAggregatePerLocationPerDay(int year, Set<Long> locationIds) {
        LOG.debug("calling getAggregatePerLocationPerDay({}, {})", year, locationIds);

        if (locationIds == null || locationIds.isEmpty()) {
            throw new IllegalArgumentException("locationIds must be provided");
        }

        StringBuilder setStr = new StringBuilder(256);
        setStr.append("(");
        for(int i =0; i< locationIds.size(); i++) {
                setStr.append("?,");
        }
        //remove last comma
        setStr.deleteCharAt(setStr.length()-1);
        setStr.append(")");
        
        String query = SQL_GET_DAILY_BY_LOCATION + setStr + SQL_GET_DAILY_BY_LOCATION_FOOTER;
        List<Object> bindVars = new ArrayList<>();
        bindVars.add(year);
        bindVars.addAll(locationIds);
        
        List<ServiceAdoptionMetrics> results = this.jdbcTemplate.query(query, bindVars.toArray(), serviceAdoptionMetricsRowMapper);

        LOG.debug("getAggregatePerLocationPerDay({}, {}) returns {} record(s)", year, locationIds, results.size());
        
        return results;
    }


    public List<ServiceAdoptionMetrics> getAggregatePerCustomerPerDay(int year, Set<Integer> customerIds) {
        LOG.debug("calling getAggregatePerCustomerPerDay({}, {})", year, customerIds);

        if (customerIds == null || customerIds.isEmpty()) {
            throw new IllegalArgumentException("customerIds must be provided");
        }

        StringBuilder setStr = new StringBuilder(256);
        setStr.append("(");
        for(int i =0; i< customerIds.size(); i++) {
                setStr.append("?,");
        }
        //remove last comma
        setStr.deleteCharAt(setStr.length()-1);
        setStr.append(")");
        
        String query = SQL_GET_DAILY_BY_CUSTOMER + setStr + SQL_GET_DAILY_BY_CUSTOMER_FOOTER;
        List<Object> bindVars = new ArrayList<>();
        bindVars.add(year);
        bindVars.addAll(customerIds);
        
        List<ServiceAdoptionMetrics> results = this.jdbcTemplate.query(query, bindVars.toArray(), serviceAdoptionMetricsRowMapper);

        LOG.debug("getAggregatePerCustomerPerDay({}, {}) returns {} record(s)", year, customerIds, results.size());
        
        return results;
    }


    public List<ServiceAdoptionMetrics> getAllPerMonth(int year) {
        LOG.debug("calling getAllPerMonth({})", year);
        
        List<ServiceAdoptionMetrics> results = this.jdbcTemplate.query(SQL_GET_ALL_BY_MONTH, new Object[] {year}, serviceAdoptionMetricsRowMapper);

        LOG.debug("getAllPerMonth({}) returns {} records", year, results.size());
        
        return results;
    }


    public List<ServiceAdoptionMetrics> getAllPerWeek(int year) {
        LOG.debug("calling getAllPerWeek({})", year);
        
        List<ServiceAdoptionMetrics> results = this.jdbcTemplate.query(SQL_GET_ALL_BY_WEEK, new Object[] {year}, serviceAdoptionMetricsRowMapper);

        LOG.debug("getAllPerWeek({}) returns {} records", year, results.size());
        
        return results;
    }


    public List<ServiceAdoptionMetrics> getAllPerDay(int year) {
        LOG.debug("calling getAllPerDay({})", year);
        
        List<ServiceAdoptionMetrics> results = this.jdbcTemplate.query(SQL_GET_ALL_BY_DAY, new Object[] {year}, serviceAdoptionMetricsRowMapper);

        LOG.debug("getAllPerDay({}) returns {} records", year, results.size());
        
        return results;
    }


    @Transactional(noRollbackFor = { DuplicateKeyException.class })
    public void updateUniqueMacs(long timestampMs, int customerId, long locationId, long equipmentId, Set<Long> clientMacSet) {

        if(clientMacSet == null || clientMacSet.isEmpty()) {
            return;
        }
        
        int batchSize = 100;
        
        try{
            
            this.jdbcTemplate.batchUpdate(SQL_INSERT_UNIQ_MAC + (usingPostgresDb?SQL_INSERT_ON_CONFLICT_DO_NOTHING_FOOTER:""), clientMacSet, batchSize ,
                    new ParameterizedPreparedStatementSetter<Long>() {
                        @Override
                        public void setValues(PreparedStatement ps, Long clientMac) throws SQLException {

                            int colIdx = 1;
                            
                            ps.setLong(colIdx++, timestampMs);

                            ps.setInt(colIdx++, customerId);
                            ps.setLong(colIdx++, locationId);
                            ps.setLong(colIdx++, equipmentId);
                            
                            ps.setLong(colIdx++, clientMac);
                            
                        }
                    });

        }catch (DuplicateKeyException e) {
            LOG.warn("Exception when inserting unique macs ", e);
        }
    

        LOG.debug("Stored {} unique macs", clientMacSet.size());
        
    }


    public long getUniqueMacsCount(int year, int dayOfYear, int customerId, long locationId, long equipmentId) {
        LOG.debug("calling getUniqueMacsCount({}, {}, {}, {}, {})", year, dayOfYear, customerId, locationId, equipmentId);
                        
        Calendar calendar = Calendar.getInstance(DateTimeUtils.TZ_GMT);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        long fromTs = calendar.getTimeInMillis();
        
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);  
        
        long toTs = calendar.getTimeInMillis();

        //metric_timestamp >= ? and metric_timestamp <= ? and customerId = ? and locationId = ? and equipmentId = ?) AS temp "
        long result = this.jdbcTemplate.queryForObject(SQL_COUNT_UNIQ_MACS, Long.class,  fromTs, toTs, customerId, locationId, equipmentId );

        LOG.debug("getUniqueMacsCount({}, {}, {}, {}, {}) returns {} ", year, dayOfYear, customerId, locationId, equipmentId, result);
        
        return result;
    }


    public void deleteUniqueMacs(long createdBeforeTimestampMs, int customerId, long locationId, long equipmentId) {
        LOG.debug("calling deleteUniqueMacs({}, {}, {}, {})", createdBeforeTimestampMs, customerId, locationId, equipmentId);
        
        //metric_timestamp <= ? and customerId = ? and locationId = ? and equipmentId = ? "
        int result = this.jdbcTemplate.update(SQL_DELETE_UNIQ_MACS, createdBeforeTimestampMs, customerId, locationId, equipmentId );

        LOG.debug("deleteUniqueMacs({}, {}, {}, {}) deleted {}  records", createdBeforeTimestampMs, customerId, locationId, equipmentId, result);        
    }
}
