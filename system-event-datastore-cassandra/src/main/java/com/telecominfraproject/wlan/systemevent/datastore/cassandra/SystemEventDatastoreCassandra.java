package com.telecominfraproject.wlan.systemevent.datastore.cassandra;

import static com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils.calculateDaysOfYear;
import static com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils.getBindPlaceholders;
import static com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils.getDayOfYear;
import static com.telecominfraproject.wlan.core.server.cassandra.CassandraUtils.isPresent;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEventStats;


/**
 * @author dtop
 *
 */
@Component
public class SystemEventDatastoreCassandra implements SystemEventDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventDatastoreCassandra.class);

    
    private static final String[] ALL_COLUMNS_LIST = {        
            
            //TODO: add columns from properties of SystemEventRecord in here
            "customerId",
            "locationId",
            "equipmentId",
            "dayOfYear",
            "clientMac",
            "dataType",
            "eventTimestamp",
            "details"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());

        private static final Set<String> columnsToSkipForInsertInIndex = new HashSet<>(Arrays.asList("details", "locationId"));

        private static final String TABLE_NAME = "wlan_system_event";
        private static final String ALL_COLUMNS;

        private static final String ALL_COLUMNS_FOR_INSERT; 
        private static final String ALL_COLUMNS_FOR_INSERT_INDEX; 
        private static final String BIND_VARS_FOR_INSERT;
        private static final String BIND_VARS_FOR_INSERT_INDEX;
        
        static{
            StringBuilder strbAllColumns = new StringBuilder(1024);
            StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
            StringBuilder strbAllColumnsForInsertIndex = new StringBuilder(1024);
            StringBuilder strbBindVarsForInsert = new StringBuilder(128);
            StringBuilder strbBindVarsForInsertIndex = new StringBuilder(128);
            for(String colName: ALL_COLUMNS_LIST){

                strbAllColumns.append(colName).append(","); 
                
                if(!columnsToSkipForInsert.contains(colName)){
                    strbAllColumnsForInsert.append(colName).append(",");
                    strbBindVarsForInsert.append("?,");
                }

                if(!columnsToSkipForInsertInIndex.contains(colName)){
                    strbAllColumnsForInsertIndex.append(colName).append(",");
                    strbBindVarsForInsertIndex.append("?,");
                }

            }

            // remove trailing ','
            strbAllColumns.deleteCharAt(strbAllColumns.length() - 1);
            strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
            strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
            strbAllColumnsForInsertIndex.deleteCharAt(strbAllColumnsForInsertIndex.length() - 1);
            strbBindVarsForInsertIndex.deleteCharAt(strbBindVarsForInsertIndex.length() - 1);

            
            ALL_COLUMNS = strbAllColumns.toString();
            ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
            BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();

            ALL_COLUMNS_FOR_INSERT_INDEX = strbAllColumnsForInsertIndex.toString();
            BIND_VARS_FOR_INSERT_INDEX = strbBindVarsForInsertIndex.toString();

        }
        
        private static final String CQL_INSERT =
            "insert into "+TABLE_NAME+" ( " 
            + ALL_COLUMNS_FOR_INSERT
            + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

        private static final String CQL_INSERT_IDX_BY_CUSTOMER =
                "insert into "+TABLE_NAME+"_by_customer ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_EQUIPMENT =
                "insert into "+TABLE_NAME+"_by_equipment ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_LOCATION =
                "insert into "+TABLE_NAME+"_by_location ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " , locationId ) values ( "+BIND_VARS_FOR_INSERT_INDEX+", ? ) ";

        private static final String CQL_INSERT_IDX_BY_LOCATION_ALL_FILTERS =
                "insert into "+TABLE_NAME+"_by_location_all_filters ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " , locationId ) values ( "+BIND_VARS_FOR_INSERT_INDEX+", ? ) ";

        private static final String CQL_INSERT_IDX_BY_MAC =
                "insert into "+TABLE_NAME+"_by_mac ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC_AND_EQUIPMENT =
                "insert into "+TABLE_NAME+"_by_mac_and_equipment ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_CUSTOMER_DATATYPE =
                "insert into "+TABLE_NAME+"_by_customer_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_EQUIPMENT_DATATYPE =
                "insert into "+TABLE_NAME+"_by_equipment_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC_DATATYPE =
                "insert into "+TABLE_NAME+"_by_mac_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC_AND_EQUIPMENT_DATATYPE =
                "insert into "+TABLE_NAME+"_by_mac_and_equipment_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        

    private static final RowMapper<SystemEventRecord> systemEventRowMapper = new SystemEventRowMapper();
    
    @Autowired
    private CqlSession cqlSession;
    
    private PreparedStatement preparedStmt_create;
    private PreparedStatement preparedStmt_create_idx_by_customer;
    private PreparedStatement preparedStmt_create_idx_by_equipment;
    private PreparedStatement preparedStmt_create_idx_by_location;
    private PreparedStatement preparedStmt_create_idx_by_location_all_filters;
    private PreparedStatement preparedStmt_create_idx_by_mac;
    private PreparedStatement preparedStmt_create_idx_by_mac_and_equipment;
    private PreparedStatement preparedStmt_create_idx_by_customer_datatype;
    private PreparedStatement preparedStmt_create_idx_by_equipment_datatype;
    private PreparedStatement preparedStmt_create_idx_by_mac_datatype;
    private PreparedStatement preparedStmt_create_idx_by_mac_and_equipment_datatype;
    
    private List<PreparedStatement> preparedStmt_create_idx_list = new ArrayList<>();
    private List<PreparedStatement> preparedStmt_create_idx_location_list = new ArrayList<>();

    @PostConstruct
    private void postConstruct(){

        preparedStmt_create = cqlSession.prepare(CQL_INSERT);
        
        preparedStmt_create_idx_by_customer = cqlSession.prepare(CQL_INSERT_IDX_BY_CUSTOMER);
        preparedStmt_create_idx_by_equipment = cqlSession.prepare(CQL_INSERT_IDX_BY_EQUIPMENT);
        preparedStmt_create_idx_by_location = cqlSession.prepare(CQL_INSERT_IDX_BY_LOCATION);
        preparedStmt_create_idx_by_location_all_filters = cqlSession.prepare(CQL_INSERT_IDX_BY_LOCATION_ALL_FILTERS);

        preparedStmt_create_idx_by_mac = cqlSession.prepare(CQL_INSERT_IDX_BY_MAC);
        preparedStmt_create_idx_by_mac_and_equipment = cqlSession.prepare(CQL_INSERT_IDX_BY_MAC_AND_EQUIPMENT);
        preparedStmt_create_idx_by_customer_datatype = cqlSession.prepare(CQL_INSERT_IDX_BY_CUSTOMER_DATATYPE);
        preparedStmt_create_idx_by_equipment_datatype = cqlSession.prepare(CQL_INSERT_IDX_BY_EQUIPMENT_DATATYPE);
        preparedStmt_create_idx_by_mac_datatype = cqlSession.prepare(CQL_INSERT_IDX_BY_MAC_DATATYPE);
        preparedStmt_create_idx_by_mac_and_equipment_datatype = cqlSession.prepare(CQL_INSERT_IDX_BY_MAC_AND_EQUIPMENT_DATATYPE);
        
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_customer);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_customer_datatype);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_equipment);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_equipment_datatype);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_mac);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_mac_datatype);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_mac_and_equipment);
        preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_mac_and_equipment_datatype);
        
        preparedStmt_create_idx_location_list.add(preparedStmt_create_idx_by_location);
        preparedStmt_create_idx_location_list.add(preparedStmt_create_idx_by_location_all_filters);

    }
    
    @Override
    public void create(SystemEventRecord systemEvent) {
        
        final long ts = System.currentTimeMillis();
        if(systemEvent.getEventTimestamp()<=0) {
            systemEvent.setEventTimestamp(ts);
        }
        
        int dayOfYear = getDayOfYear(systemEvent.getEventTimestamp());
        
        //create main record
        cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from SystemEvent here 
                systemEvent.getCustomerId(),
                systemEvent.getLocationId(),
                systemEvent.getEquipmentId(),
                dayOfYear,
                systemEvent.getClientMac(),
                systemEvent.getDataType(),
                systemEvent.getEventTimestamp(),
                
                (systemEvent.getDetails()!=null) ? ByteBuffer.wrap(systemEvent.getDetails().toZippedBytes()) : null
                
                ));
        
        //create index records
        preparedStmt_create_idx_list.forEach(ps ->  cqlSession.execute(ps.bind(
                systemEvent.getCustomerId(),
                systemEvent.getEquipmentId(),
                dayOfYear,
                systemEvent.getClientMac(),                
                systemEvent.getDataType(),
                systemEvent.getEventTimestamp()
                
                )));
        
        //create indexes by locationId - separately because they have different columns from the rest of the indexes
        preparedStmt_create_idx_location_list.forEach(ps ->  cqlSession.execute(ps.bind(
                systemEvent.getCustomerId(),
                systemEvent.getEquipmentId(),
                dayOfYear,
                systemEvent.getClientMac(),                
                systemEvent.getDataType(),
                systemEvent.getEventTimestamp(),
                systemEvent.getLocationId()
                )));
                
        LOG.debug("Stored SystemEvent {}", systemEvent);
    }

    @Override
    public void create(List<SystemEventRecord> systemEvents) {
        if(systemEvents == null || systemEvents.isEmpty()) {
            return;
        }
        
        //TODO: implement create a batch of system events
        systemEvents.forEach(sm -> create(sm));
    }

    @Override
    public void delete(long createdBeforeTimestamp) {
        // This method is used for purging the old data
        // For cassandra there is no need for it - we are relying on the default_time_to_live configured on the table
        // do nothing here
    }
    
    //Important: do not use this method outside of local testing 
    private void getDaysOfYearAndMacsAndDataTypes_wlan_system_event(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Integer> daysOfYear, Set<Long> clientMacs, Set<String> dataTypes){
        PreparedStatement preparedStmt = cqlSession.prepare("select dayOfYear, clientMac, dataType from "+TABLE_NAME+" where customerId = ? and equipmentId  = ?  and eventTimestamp < ? allow filtering");   
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            daysOfYear.add(row.getInt("dayOfYear"));
            clientMacs.add(row.getLong("clientMac"));
            dataTypes.add(row.getString("dataType"));
        }
        
    }

    //Important: do not use this method outside of local testing 
    private Set<Long> getEventTimestamps_wlan_system_event_by_customer(int customerId, long equipmentId, long createdBeforeTimestamp){
        PreparedStatement preparedStmt = cqlSession.prepare("select equipmentId, eventTimestamp from "+TABLE_NAME+"_by_customer where customerId = ? and eventTimestamp < ?");  
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, createdBeforeTimestamp));
        
        Set<Long> ret = new HashSet<>();
        Row row;
        while((row = rs.one()) !=null) {
            if(row.getLong("equipmentId") == equipmentId) {
                ret.add(row.getLong("eventTimestamp"));
            }
        }
        
        return ret;
    }

    //Important: do not use this method outside of local testing 
    private void getMacsAndTimestamps_wlan_system_event_by_mac(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs, Set<Long> timestamps){
        PreparedStatement preparedStmt = cqlSession.prepare("select clientMac, eventTimestamp from "+TABLE_NAME+"_by_mac where customerId = ? and equipmentId = ? and eventTimestamp < ? allow filtering"); 
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            clientMacs.add(row.getLong("clientMac"));
            timestamps.add(row.getLong("eventTimestamp"));
        }
        
    }

    //Important: do not use this method outside of local testing 
    private void getMacs_wlan_system_event_by_mac_and_equipment(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs){
        PreparedStatement preparedStmt = cqlSession.prepare("select clientMac from "+TABLE_NAME+"_by_mac_and_equipment where customerId = ? and equipmentId = ? and eventTimestamp < ? allow filtering"); 
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            clientMacs.add(row.getLong("clientMac"));
        }
        
    }
    
    //Important: do not use this method outside of local testing 
    private void getDataTypes_wlan_system_event_by_customer_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<String> dataTypes, Set<Long> timestamps){
        PreparedStatement preparedStmt = cqlSession.prepare("select equipmentId, dataType, eventTimestamp from "+TABLE_NAME+"_by_customer_datatype where customerId = ?");    
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId));
        
        Row row;
        while((row = rs.one()) !=null) {
            if(row.getLong("equipmentId") == equipmentId) {
                dataTypes.add(row.getString("dataType"));
                if(row.getLong("eventTimestamp")< createdBeforeTimestamp) {
                    timestamps.add(row.getLong("eventTimestamp"));
                }
            }
        }
    }

    //Important: do not use this method outside of local testing 
    private void getDataTypes_wlan_system_event_by_equipment_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<String> dataTypes){
        PreparedStatement preparedStmt = cqlSession.prepare("select dataType, eventTimestamp from "+TABLE_NAME+"_by_equipment_datatype where customerId = ? and equipmentId  = ? ");  
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId));
        
        Row row;
        while((row = rs.one()) !=null) {
            if(row.getLong("eventTimestamp")< createdBeforeTimestamp) {
                dataTypes.add(row.getString("dataType"));
            }
        }
    }


    //Important: do not use this method outside of local testing 
    private void getDataTypes_wlan_system_event_by_mac_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs, Set<String> dataTypes, Set<Long> timestamps){
        PreparedStatement preparedStmt = cqlSession.prepare("select clientMac, dataType, eventTimestamp from "+TABLE_NAME+"_by_mac_datatype where customerId = ? and equipmentId = ? and eventTimestamp < ? allow filtering");  
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            clientMacs.add(row.getLong("clientMac"));       
            dataTypes.add(row.getString("dataType"));
            timestamps.add(row.getLong("eventTimestamp"));
        }
    }

    //Important: do not use this method outside of local testing 
    private void getDataTypes_wlan_system_event_by_mac_and_equipment_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs, Set<String> dataTypes){
        PreparedStatement preparedStmt = cqlSession.prepare("select clientMac, dataType from "+TABLE_NAME+"_by_mac_and_equipment_datatype where customerId = ? and equipmentId = ? and eventTimestamp < ? allow filtering");  
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            clientMacs.add(row.getLong("clientMac"));       
            dataTypes.add(row.getString("dataType"));
        }
    }

    
    @Override
    //Important: do not use this method outside of local testing 
    public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {

        //select dayOfYear, clientMac, dataType from wlan_system_event where customerId = 1 and equipmentId  = 1  and eventTimestamp < 1594837188000 allow filtering;
        //delete from wlan_system_event where customerId = 1 and equipmentId  = 1 and dayOfYear in (1,2,3,4,5,365) and clientMac in (1,2,3) and dataType in ('1','2','3') and eventTimestamp < 1594837188000;
        Set<Integer> daysOfYear = new HashSet<>(); 
        Set<Long> clientMacs = new HashSet<>();
        Set<String> dataTypes = new HashSet<>();
        Set<Long> locationIds = new HashSet<>();

        getDaysOfYearAndMacsAndDataTypes_wlan_system_event(customerId, equipmentId, createdBeforeTimestamp, daysOfYear, clientMacs, dataTypes);
        
        if(!daysOfYear.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.addAll(daysOfYear);
            queryArgs.addAll(clientMacs);
            queryArgs.addAll(dataTypes);
            queryArgs.add(createdBeforeTimestamp);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+" where customerId = ? and equipmentId  = ? and dayOfYear in "+getBindPlaceholders(daysOfYear)+" and clientMac in "+getBindPlaceholders(clientMacs)+" and dataType in "+getBindPlaceholders(dataTypes)+" and eventTimestamp < ? ");
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {} {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //select equipmentId, eventTimestamp from wlan_system_event_by_customer where customerId = 1 and eventTimestamp < 1594837188000;
        //delete from wlan_system_event_by_customer where customerId = 1 and equipmentId =1 and eventTimestamp in (1594837188000, 1594837189000);
        Set<Long> timestamps = getEventTimestamps_wlan_system_event_by_customer(customerId, equipmentId, createdBeforeTimestamp);

        if(!timestamps.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.addAll(timestamps);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_customer where customerId = ? and equipmentId =? and eventTimestamp in " + getBindPlaceholders(timestamps));
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_customer {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //delete from wlan_system_event_by_equipment where customerId = 1 and equipmentId  = 1 and eventTimestamp < 1594837188000;
        {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.add(createdBeforeTimestamp);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_equipment where customerId = ? and equipmentId  = ? and eventTimestamp < ?");
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_equipment {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //select locationId from wlan_system_event_by_location where customerId = 1 and equipmentId = 2 and eventTimestamp < 1594837188000 allow filtering;
        //delete from wlan_system_event_by_location where customerId = 1 and locationId  in (1,2) and eventTimestamp < 1594837188000;
        {
            locationIds.clear();
            getLocations_wlan_system_event_by_location(customerId, equipmentId, createdBeforeTimestamp, locationIds);
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.addAll(locationIds);
            queryArgs.add(createdBeforeTimestamp);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_location where customerId = ? and locationId  in " + getBindPlaceholders(locationIds)+ " and eventTimestamp < ?");
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_location {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //select * from wlan_system_event_by_location_all_filters where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837189000 allow filtering;
        //delete  from wlan_system_event_by_location_all_filters where customerId = 1 and locationId in (1, 2)  and equipmentId = 1  and clientMac in (1, 2) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;
        {
            locationIds.clear();
            clientMacs.clear();
            dataTypes.clear();
            getAll_wlan_system_event_by_location_all_filters(customerId, equipmentId, createdBeforeTimestamp, locationIds, clientMacs, dataTypes);
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.addAll(locationIds);
            queryArgs.add(equipmentId);
            queryArgs.addAll(clientMacs);
            queryArgs.addAll(dataTypes);
            queryArgs.add(createdBeforeTimestamp);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_location_all_filters where customerId = ? and locationId  in " + getBindPlaceholders(locationIds)+ " and equipmentId = ? and clientMac in "+getBindPlaceholders(clientMacs)+" and dataType in " +getBindPlaceholders(dataTypes)+ "  and eventTimestamp < ?");
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_location_all_filters {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }
        
        //select clientMac, eventTimestamp from wlan_system_event_by_mac where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
        //delete from wlan_system_event_by_mac where customerId = 1  and equipmentId = 1 and clientMac in (1, 2) and eventTimestamp in (1594837188000, 1594837189000);
        clientMacs.clear();
        timestamps.clear();
        getMacsAndTimestamps_wlan_system_event_by_mac(customerId, equipmentId, createdBeforeTimestamp, clientMacs, timestamps);
        
        if(!clientMacs.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.addAll(clientMacs);
            queryArgs.addAll(timestamps);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_mac where customerId = ?  and equipmentId = ? and clientMac in "+getBindPlaceholders(clientMacs)+" and eventTimestamp in " + getBindPlaceholders(timestamps));
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_mac {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //select clientMac from wlan_system_event_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
        //delete from wlan_system_event_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and  eventTimestamp < 1594837188000  and clientMac in (1, 2) ;
        clientMacs.clear();
        getMacs_wlan_system_event_by_mac_and_equipment(customerId, equipmentId, createdBeforeTimestamp, clientMacs);
        if(!clientMacs.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.add(createdBeforeTimestamp);
            queryArgs.addAll(clientMacs);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_mac_and_equipment where customerId = ? and equipmentId = ? and  eventTimestamp < ?  and clientMac in "+getBindPlaceholders(clientMacs));
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_mac_and_equipment {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }
        
        //select equipmentId, dataType, eventTimestamp from wlan_system_event_by_customer_datatype where customerId = 1;
        //delete from wlan_system_event_by_customer_datatype where customerId = 1 and equipmentId = 1 and dataType in ('1','2','3') and eventTimestamp in (1594837188000, 1594837189000);
        dataTypes.clear();
        timestamps.clear();
        getDataTypes_wlan_system_event_by_customer_datatype(customerId, equipmentId, createdBeforeTimestamp, dataTypes, timestamps);
        if(!dataTypes.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.addAll(dataTypes);
            queryArgs.addAll(timestamps);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_customer_datatype where customerId = ? and equipmentId = ? and dataType in "+getBindPlaceholders(dataTypes)+" and eventTimestamp in "+ getBindPlaceholders(timestamps));
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_customer_datatype {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }
        
        //select dataType from wlan_system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1;
        //delete from wlan_system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1 and eventTimestamp < 1594837188000  and dataType in ('1','2','3') ;
        dataTypes.clear();
        getDataTypes_wlan_system_event_by_equipment_datatype(customerId, equipmentId, createdBeforeTimestamp, dataTypes);
        if(!dataTypes.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.add(createdBeforeTimestamp);
            queryArgs.addAll(dataTypes);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_equipment_datatype where customerId = ? and equipmentId  = ? and eventTimestamp < ? and dataType in  "+getBindPlaceholders(dataTypes) );
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_equipment_datatype {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //select clientMac, dataType, eventTimestamp from wlan_system_event_by_mac_datatype where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
        //delete from wlan_system_event_by_mac_datatype where customerId = 1 and equipmentId = 1 and clientMac in (1, 2) and dataType in (1,2,3) and  eventTimestamp in (1594837188000, 1594837189000);
        clientMacs.clear();
        dataTypes.clear();
        timestamps.clear();
        getDataTypes_wlan_system_event_by_mac_datatype(customerId, equipmentId, createdBeforeTimestamp, clientMacs, dataTypes, timestamps);
        if(!dataTypes.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.addAll(clientMacs);
            queryArgs.addAll(dataTypes);
            queryArgs.addAll(timestamps);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_mac_datatype where customerId = ? and equipmentId = ? and clientMac in "+getBindPlaceholders(clientMacs)+" and dataType in "+getBindPlaceholders(dataTypes)+" and  eventTimestamp in "+ getBindPlaceholders(timestamps));
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_mac_datatype {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }

        //select clientMac, dataType from wlan_system_event_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
        //delete from wlan_system_event_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1  and eventTimestamp < 1594837188000 and clientMac in (1, 2)  and dataType in ('1','2','3');
        clientMacs.clear();
        dataTypes.clear();
        getDataTypes_wlan_system_event_by_mac_and_equipment_datatype(customerId, equipmentId, createdBeforeTimestamp, clientMacs, dataTypes);
        if(!dataTypes.isEmpty()) {
            ArrayList<Object> queryArgs = new ArrayList<>();
            queryArgs.add(customerId);
            queryArgs.add(equipmentId);
            queryArgs.add(createdBeforeTimestamp);
            queryArgs.addAll(clientMacs);
            queryArgs.addAll(dataTypes);
            
            PreparedStatement preparedStmt = cqlSession.prepare("delete from "+TABLE_NAME+"_by_mac_and_equipment_datatype where customerId = ? and equipmentId = ?  and eventTimestamp < ? and clientMac in "+getBindPlaceholders(clientMacs)+"  and dataType in "+getBindPlaceholders(dataTypes));
            cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
            
            LOG.debug("Removed records from {}_by_mac_and_equipment_datatype {} {} {}", TABLE_NAME, customerId, equipmentId, createdBeforeTimestamp);
        }


        LOG.debug("Deleted service metrics {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
    }
    
    private void getAll_wlan_system_event_by_location_all_filters(int customerId, long equipmentId,
            long createdBeforeTimestamp, Set<Long> locationIds, Set<Long> clientMacs, Set<String> dataTypes) {
        
        PreparedStatement preparedStmt = cqlSession.prepare("select * from "+TABLE_NAME+"_by_location_all_filters where customerId = ? and equipmentId = ? and eventTimestamp < ? allow filtering"); 
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            locationIds.add(row.getLong("locationId"));
            clientMacs.add(row.getLong("clientMac"));
            dataTypes.add(row.getString("dataType"));
        }
    }

    private void getLocations_wlan_system_event_by_location(int customerId, long equipmentId, long createdBeforeTimestamp,
            Set<Long> locationIds) {
        PreparedStatement preparedStmt = cqlSession.prepare("select locationId from "+TABLE_NAME+"_by_location where customerId = ? and equipmentId = ? and eventTimestamp < ? allow filtering"); 
        ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
        
        Row row;
        while((row = rs.one()) !=null) {
            locationIds.add(row.getLong("locationId"));
        }        
    }

    private List<SystemEventRecord> getPage(int customerId, Set<Long> equipmentIds, Set<Integer> daysOfYear, Set<Long> clientMacs, Set<Long> timestamps, Set<String> dataTypes) {
        //select * from wlan_system_event where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and clientMac in (1,2,3) and eventTimestamp in (1594837187000, 1594837188000 , 1594837189000) and dataType in ('1','2','3');
        List<SystemEventRecord> ret = new ArrayList<>();
        
        ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.addAll(equipmentIds);
        queryArgs.addAll(daysOfYear);
        queryArgs.addAll(clientMacs);
        queryArgs.addAll(timestamps);
        queryArgs.addAll(dataTypes);

        PreparedStatement preparedStmt = cqlSession.prepare("select * from "+TABLE_NAME+" where customerId = ? "
                + "and equipmentId  in "+getBindPlaceholders(equipmentIds)
                +" and dayOfYear in "+getBindPlaceholders(daysOfYear)
                +" and clientMac in " +getBindPlaceholders(clientMacs)
                +" and eventTimestamp in "+getBindPlaceholders(timestamps)
                +" and dataType in " + getBindPlaceholders(dataTypes)); 
        ResultSet rs = cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
        
        Row row;
        while((row = rs.one()) !=null) {
            ret.add(systemEventRowMapper.mapRow(row));
        }

        return ret;
    }

    private static enum FilterOptions {
        /**
         * All filters except location are provided and not empty - customerId, equipmentIds, clientMacs and dataTypes. This is the most efficient way to query, does not use index tables, goes through the wlan_system_event table directly
         */
        all_filters, 
        /**
         * Only customerId is provided. First iterate through the wlan_system_event_by_customer table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        customer,   
        /**
         * Only customerId and equipmentIds are provided. First iterate through the wlan_system_event_by_equipment table, collect a page of keys, then use getPage() method to read from the wlan_system_event table. 
         */
        equipment,   
        /**
         * Only customerId and clientMacs are provided. First iterate through the wlan_system_event_by_mac table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        mac, 
        /**
         * Only customerId, equipmentIds and clientMacs are provided. First iterate through the wlan_system_event_by_mac_and_equipment table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        mac_and_equipment,  
        /**
         * Only customerId and dataTypes are provided. First iterate through the wlan_system_event_by_customer_datatype table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        customer_dataType,  
        /**
         * Only customerId, equipmentIds and dataTypes are provided. First iterate through the wlan_system_event_by_equipment_datatype table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        equipment_dataType,  
        /**
         * Only customerId, clientMacs and dataTypes are provided. First iterate through the wlan_system_event_by_mac_datatype table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        mac_dataType, 
        /**
         * Note: this case is covered by the all_filters element above. Keeping it here only in case we need to extend ways of filtering. May remove it and the corresponding index table in the future. only customerId, equipmentIds, clientMacs and dataTypes are provided. First iterate through the wlan_system_event_by_mac_and_equipment_datatype table, collect a page of keys, then use getPage() method to read from the wlan_system_event table.
         */
        mac_and_equipment_dataType,
        
        /**
         * All filters including location are provided and not empty - customerId, locationIds, equipmentIds, clientMacs and dataTypes. First iterate through the wlan_system_event_by_location_all_filters table, collect a page of keys, then use getPage() method to read from the wlan_system_event table. 
         */
        location_all_filters,
        /**
         * CustomerId and locationIds are provided. Other filters may be provided as well. First iterate through the wlan_system_event_by_location table, collect a page of keys, then filter that page according to remaining filters (using client-side filtering), then use getPage() method to read from the wlan_system_event table. 
         */
        location   
        
    }

    @Override
    public PaginationResponse<SystemEventRecord> getForCustomer(
            long fromTime,
            long toTime,
            int customerId, 
            Set<Long> locationIds,
            Set<Long> equipmentIds,
            Set<MacAddress> clientMacAdresses, 
            Set<String> dataTypes, 
            List<ColumnAndSort> sortBy,
            PaginationContext<SystemEventRecord> context) {

        if(context ==null) {
            context = new PaginationContext<>();
        }

        PaginationResponse<SystemEventRecord> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up SystemEvents for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up SystemEvents for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        //select the index table based on provided inputs, create a query and bind variables
        FilterOptions filterOptions = null;
        String query = null;
        ArrayList<Object> queryArgs = new ArrayList<>();
        Set<Integer> daysOfYear = calculateDaysOfYear(fromTime, toTime);

        if(isPresent(locationIds) && isPresent(equipmentIds) && isPresent(clientMacAdresses) && isPresent(dataTypes)) {
            filterOptions = FilterOptions.location_all_filters;
            query = "select * from "+TABLE_NAME+"_by_location_all_filters where customerId = ? "
                    + " and locationId in "+getBindPlaceholders(locationIds)                 
                    + " and equipmentId in "+getBindPlaceholders(equipmentIds)
                    +" and clientMac in "+getBindPlaceholders(clientMacAdresses)
                    +" and dataType in "+getBindPlaceholders(dataTypes)
                    +" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(locationIds);
            queryArgs.addAll(equipmentIds);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            queryArgs.addAll(dataTypes);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);

        } else if(isPresent(locationIds)) {
            
            //Whenever filtering by location is required and not all of the remaining filters are provided, we will perform a client side filtering of the results from the *_by_location index table.
            //We are doing that so that we do not need to deal with 16 combinations of index tables to cover all the cases of supplied parameters.
            //If querying by location with other filters becomes frequent enough operation, we will add the remaining index tables as needed.
            filterOptions = FilterOptions.location;
            query = "select * from "+TABLE_NAME+"_by_location where customerId = ? "
                    + " and locationId in "+getBindPlaceholders(locationIds) 
                    +" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(locationIds);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && isPresent(clientMacAdresses) && isPresent(dataTypes)) {
            filterOptions = FilterOptions.all_filters;
            query = "select * from "+TABLE_NAME+" where customerId = ? "
                    + " and equipmentId in "+getBindPlaceholders(equipmentIds)
                    +" and dayOfYear in "+getBindPlaceholders(daysOfYear)
                    +" and clientMac in "+getBindPlaceholders(clientMacAdresses)
                    +" and dataType in "+getBindPlaceholders(dataTypes)
                    +" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            queryArgs.addAll(daysOfYear);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            queryArgs.addAll(dataTypes);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && !isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
            filterOptions = FilterOptions.customer;
            query = "select * from "+TABLE_NAME+"_by_customer where customerId = ? "
                    + " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && !isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
            filterOptions = FilterOptions.equipment;
            query = "select * from "+TABLE_NAME+"_by_equipment where customerId = ? "
                    + " and equipmentId in "+getBindPlaceholders(equipmentIds) 
                    +" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
            filterOptions = FilterOptions.mac;
            query = "select * from "+TABLE_NAME+"_by_mac where customerId = ? "
                    + " and clientMac in "+getBindPlaceholders(clientMacAdresses)
                    +" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
            filterOptions = FilterOptions.mac_and_equipment;
            query = "select * from "+TABLE_NAME+"_by_mac_and_equipment where customerId = ? "
                    + " and clientMac in  "+getBindPlaceholders(clientMacAdresses)
                    + " and equipmentId in "+getBindPlaceholders(equipmentIds)
                    + " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            queryArgs.addAll(equipmentIds);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && !isPresent(clientMacAdresses) && isPresent(dataTypes)) {
            filterOptions = FilterOptions.customer_dataType;
            query = "select * from "+TABLE_NAME+"_by_customer_datatype where customerId = ? "
                    + " and dataType in "+getBindPlaceholders(dataTypes)
                    + " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(dataTypes);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && !isPresent(clientMacAdresses) && isPresent(dataTypes)) {
            filterOptions = FilterOptions.equipment_dataType;
            query = "select * from "+TABLE_NAME+"_by_equipment_datatype where customerId = ? "
                    + " and equipmentId in  "+getBindPlaceholders(equipmentIds)
                    + " and dataType in "+getBindPlaceholders(dataTypes)
                    + " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);          
            queryArgs.addAll(dataTypes);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && isPresent(clientMacAdresses) && isPresent(dataTypes)) {
            filterOptions = FilterOptions.mac_dataType;
            query = "select * from "+TABLE_NAME+"_by_mac_datatype where customerId = ? "
                    + " and clientMac in "+getBindPlaceholders(clientMacAdresses)
                    + " and dataType in "+getBindPlaceholders(dataTypes)
                    + " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));            
            queryArgs.addAll(dataTypes);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } 
        
        //this is to protect the future code changes - when new filter operations are added
        if(filterOptions == null){
            throw new IllegalStateException("Unknown combination of query filters");
        }
               
        // add sorting options for the query
        // Cassandra allows very limited support for ordering results
        // See https://cassandra.apache.org/doc/latest/cql/dml.html#select
        // In here allowed orderings are the order induced by the clustering columns and the reverse of that one.
        // also, order by with secondary indexes is not supported
        // ***** We will ignore the order supplied by the caller for this datastore

        //TODO: create a cache of these prepared statements - may be too many variations
        PreparedStatement preparedStmt_getPageForCustomer;
        try {
            preparedStmt_getPageForCustomer = cqlSession.prepare(query);
        } catch(InvalidQueryException e) {
            LOG.error("Cannot prepare cassandra query '{}'", query, e);
            throw e;
        }

        // add pagination parameters for the query
        BoundStatement boundStmt = preparedStmt_getPageForCustomer.bind(queryArgs.toArray() );
        //have to do it this way - setPageSize creates new object
        boundStmt = boundStmt.setPageSize(context.getMaxItemsPerPage());
        
        ByteBuffer pagingState = null;
        if(context.getThirdPartyPagingState()!=null) {
            pagingState = ByteBuffer.wrap(context.getThirdPartyPagingState());
        }

        List<SystemEventRecord> pageItems = new ArrayList<>();
        AtomicBoolean keepPaging = new AtomicBoolean(true);
        while(keepPaging.get()) {
            ByteBuffer nextPagingState = readNextPage(boundStmt, filterOptions, pagingState, pageItems, keepPaging, customerId, locationIds, equipmentIds, clientMacAdresses, dataTypes);
            pagingState = nextPagingState;
        }
        
        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find SystemEvents for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} SystemEvents for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());
        }        

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(pagingState!=null) {
            ret.getContext().setThirdPartyPagingState(pagingState.array());
        } else {
            ret.getContext().setThirdPartyPagingState(null);
        }
        
        // startAfterItem is not used in Cassandra datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        //in cassandra we will rely only on nextPagingState to set the lastPage indicator
        ret.getContext().setLastPage(false);

        if(pagingState == null) {
            //in cassandra, if there are no more pages available, the pagingState is returned as null by the driver
            //this overrides all other heuristics related to guessing the indication of the last page
            ret.getContext().setLastPage(true);
        }

        return ret; 
    }

    private ByteBuffer readNextPage(BoundStatement boundStmt, FilterOptions filterOptions, ByteBuffer pagingState, List<SystemEventRecord> pageItems,
            AtomicBoolean keepPaging,
            int customerId, 
            Set<Long> locationIds,
            Set<Long> equipmentIds,
            Set<MacAddress> clientMacAdresses, 
            Set<String> dataTypes ) {
        
        if(pagingState!=null) {
            //have to do it this way - setPagingState creates new object
            boundStmt = boundStmt.setPagingState(pagingState);
        }
        
        ResultSet rs = cqlSession.execute(boundStmt);
        ByteBuffer nextPagingState = rs.getExecutionInfo().getPagingState();

        int indexRecordsRead = 0;

        //get a page of keys from it, call getPage(...) with those keys

        switch(filterOptions) {
        case all_filters:
            // iterate through the current page directly
            while (rs.getAvailableWithoutFetching() > 0) {
              pageItems.add(systemEventRowMapper.mapRow(rs.one()));
            }
            break;      
        case customer:
        case customer_dataType:
        case equipment:
        case equipment_dataType:
        case mac:
        case mac_and_equipment:
        case mac_dataType:
        case mac_and_equipment_dataType:
        case location_all_filters:
        case location:
            //the query was against the index table table
            //find all the keys for the page, then retrieve records for them from wlan_system_event table
            Set<Long> equipmentIdsIdx = new HashSet<>();
            Set<Long> clientMacAdressesIdx = new HashSet<>();
            Set<Long> timestampsIdx = new HashSet<>();
            Set<String> dataTypesIdx = new HashSet<>();
            Set<Integer> daysOfYearIdx = new HashSet<>();
            Row row;
            while (rs.getAvailableWithoutFetching() > 0) {
                row = rs.one();
                indexRecordsRead++;
                equipmentIdsIdx.add(row.getLong("equipmentId"));
                clientMacAdressesIdx.add(row.getLong("clientMac"));
                timestampsIdx.add(row.getLong("eventTimestamp"));
                dataTypesIdx.add(row.getString("dataType"));
                daysOfYearIdx.add(row.getInt("dayOfYear"));
            }

            if(filterOptions == FilterOptions.location) {
                //for the location query perform client-side filtering by the remaining fields(if any). 
                if(isPresent(equipmentIds)) {
                    equipmentIdsIdx.retainAll(equipmentIds);
                }

                if(isPresent(clientMacAdresses)) {
                     Set<Long> cmLongs = new HashSet<>();
                     clientMacAdresses.forEach(cMac -> cmLongs.add(cMac.getAddressAsLong()));
                     clientMacAdressesIdx.retainAll(cmLongs);
                }

               if(isPresent(dataTypes)) {
                   dataTypesIdx.retainAll(dataTypes);
               }

            }
            
            if(isPresent(equipmentIdsIdx) && isPresent(daysOfYearIdx) && isPresent(clientMacAdressesIdx) && isPresent(timestampsIdx) && isPresent(dataTypesIdx)) {
                List<SystemEventRecord> pageOfEvents = getPage(customerId, equipmentIdsIdx, daysOfYearIdx, clientMacAdressesIdx, timestampsIdx, dataTypesIdx);

                if(isPresent(locationIds)) {
                    //apply locationId filtering separately in here - because the main table does not have locationId as part of the primary key.
                    //this is still not the full solution, unless locationId is declared part of the PK on the main table there can be situations where 
                    //events/metrics are lost because of PK collisions - this will be happening more frequently in large deployments.  
                    pageOfEvents.forEach(se -> {
                        if(locationIds.contains(se.getLocationId())) {
                            pageItems.add(se);
                        }
                    });
                } else {
                    pageItems.addAll(pageOfEvents);
                }

            }
            
            break;
        default:
            LOG.warn("Unknown filter option:", filterOptions);
            throw new IllegalArgumentException("Unknown filter option " + filterOptions);
        }

        // When paging for locations we use client-side filtering, meaning the page from
        // the index filtered on the client side may be empty, but that does not mean we
        // should stop looking for more data, so we'll move to the next page on the index in that case
        keepPaging.set((filterOptions == FilterOptions.location || filterOptions == FilterOptions.location_all_filters) && indexRecordsRead > 0 && pageItems.isEmpty()  && nextPagingState!=null );
        
        return nextPagingState;
    }

    @Override
    public SystemEventStats getSystemEventStats(String filterAttributeName, String filterAttributeValue, long fromTime, long toTime) {
         return new SystemEventStats();
    }

}
