package com.telecominfraproject.wlan.servicemetric.datastore.cassandra;

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
import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtop
 *
 */
@Component
public class ServiceMetricDatastoreCassandra implements ServiceMetricDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricDatastoreCassandra.class);

    private static final String[] ALL_COLUMNS_LIST = {        
            
            //TODO: add columns from properties of ServiceMetric in here
            "customerId",
            "locationId",
            "equipmentId",
            "dayOfYear",
            "clientMac",
            "dataType",
            "createdTimestamp",
            "details"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());

        private static final Set<String> columnsToSkipForInsertInIndex = new HashSet<>(Arrays.asList("details", "locationId"));

        private static final String TABLE_NAME = "service_metric";
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
                "insert into service_metric_by_customer ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_EQUIPMENT =
                "insert into service_metric_by_equipment ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC =
                "insert into service_metric_by_mac ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC_AND_EQUIPMENT =
                "insert into service_metric_by_mac_and_equipment ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_CUSTOMER_DATATYPE =
                "insert into service_metric_by_customer_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_EQUIPMENT_DATATYPE =
                "insert into service_metric_by_equipment_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC_DATATYPE =
                "insert into service_metric_by_mac_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_MAC_AND_EQUIPMENT_DATATYPE =
                "insert into service_metric_by_mac_and_equipment_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        

    private static final RowMapper<ServiceMetric> serviceMetricRowMapper = new ServiceMetricRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_create_idx_by_customer;
	private PreparedStatement preparedStmt_create_idx_by_equipment;
	private PreparedStatement preparedStmt_create_idx_by_mac;
	private PreparedStatement preparedStmt_create_idx_by_mac_and_equipment;
	private PreparedStatement preparedStmt_create_idx_by_customer_datatype;
	private PreparedStatement preparedStmt_create_idx_by_equipment_datatype;
	private PreparedStatement preparedStmt_create_idx_by_mac_datatype;
	private PreparedStatement preparedStmt_create_idx_by_mac_and_equipment_datatype;
	
	private List<PreparedStatement> preparedStmt_create_idx_list = new ArrayList<>();

	@PostConstruct
	private void postConstruct(){

		preparedStmt_create = cqlSession.prepare(CQL_INSERT);
		
		preparedStmt_create_idx_by_customer = cqlSession.prepare(CQL_INSERT_IDX_BY_CUSTOMER);
		preparedStmt_create_idx_by_equipment = cqlSession.prepare(CQL_INSERT_IDX_BY_EQUIPMENT);
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

	}
	
	@Override
	public void create(ServiceMetric serviceMetric) {
		
        final long ts = System.currentTimeMillis();
        if(serviceMetric.getCreatedTimestamp()<=0) {
        	serviceMetric.setCreatedTimestamp(ts);
        }
        
        int dayOfYear = getDayOfYear(serviceMetric.getCreatedTimestamp());
        
        //create main record
		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from ServiceMetric here 
                serviceMetric.getCustomerId(),
                serviceMetric.getLocationId(),
                serviceMetric.getEquipmentId(),
                dayOfYear,
                serviceMetric.getClientMac(),
                serviceMetric.getDataType().getId(),
                serviceMetric.getCreatedTimestamp(),
                
              	(serviceMetric.getDetails()!=null) ? ByteBuffer.wrap(serviceMetric.getDetails().toZippedBytes()) : null
				
				));
		
		//create index records
		preparedStmt_create_idx_list.forEach(ps -> 	cqlSession.execute(ps.bind(
                serviceMetric.getCustomerId(),
                serviceMetric.getEquipmentId(),
                dayOfYear,
                serviceMetric.getClientMac(),                
                serviceMetric.getDataType().getId(),
                serviceMetric.getCreatedTimestamp()
				
				)));
				
        LOG.debug("Stored ServiceMetric {}", serviceMetric);
	}

	@Override
	public void create(List<ServiceMetric> serviceMetrics) {
		if(serviceMetrics == null || serviceMetrics.isEmpty()) {
			return;
		}
		
		//TODO: implement create a batch of metrics
		serviceMetrics.forEach(sm -> create(sm));
	}

	@Override
	public void delete(long createdBeforeTimestamp) {
		// This method is used for purging the old data
		// For cassandra there is no need for it - we are relying on the default_time_to_live configured on the table
		// do nothing here
	}
	
	//Important: do not use this method outside of local testing 
	private void getDaysOfYearAndMacsAndDataTypes_service_metric(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Integer> daysOfYear, Set<Long> clientMacs, Set<Integer> dataTypes){
		PreparedStatement preparedStmt = cqlSession.prepare("select dayOfYear, clientMac, dataType from service_metric where customerId = ? and equipmentId  = ?  and createdTimestamp < ? allow filtering");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
		
		Row row;
		while((row = rs.one()) !=null) {
			daysOfYear.add(row.getInt("dayOfYear"));
			clientMacs.add(row.getLong("clientMac"));
			dataTypes.add(row.getInt("dataType"));
		}
		
	}

	//Important: do not use this method outside of local testing 
	private Set<Long> getCreatedTimestamps_service_metric_by_customer(int customerId, long equipmentId, long createdBeforeTimestamp){
		PreparedStatement preparedStmt = cqlSession.prepare("select equipmentId, createdTimestamp from service_metric_by_customer where customerId = ? and createdTimestamp < ?");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, createdBeforeTimestamp));
		
		Set<Long> ret = new HashSet<>();
		Row row;
		while((row = rs.one()) !=null) {
			if(row.getLong("equipmentId") == equipmentId) {
				ret.add(row.getLong("createdTimestamp"));
			}
		}
		
		return ret;
	}

	//Important: do not use this method outside of local testing 
	private void getMacsAndTimestamps_service_metric_by_mac(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs, Set<Long> timestamps){
		PreparedStatement preparedStmt = cqlSession.prepare("select clientMac, createdTimestamp from service_metric_by_mac where customerId = ? and equipmentId = ? and createdTimestamp < ? allow filtering");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
		
		Row row;
		while((row = rs.one()) !=null) {
			clientMacs.add(row.getLong("clientMac"));
			timestamps.add(row.getLong("createdTimestamp"));
		}
		
	}

	//Important: do not use this method outside of local testing 
	private void getMacs_service_metric_by_mac_and_equipment(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs){
		PreparedStatement preparedStmt = cqlSession.prepare("select clientMac from service_metric_by_mac_and_equipment where customerId = ? and equipmentId = ? and createdTimestamp < ? allow filtering");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
		
		Row row;
		while((row = rs.one()) !=null) {
			clientMacs.add(row.getLong("clientMac"));
		}
		
	}
	
	//Important: do not use this method outside of local testing 
	private void getDataTypes_service_metric_by_customer_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Integer> dataTypes, Set<Long> timestamps){
		PreparedStatement preparedStmt = cqlSession.prepare("select equipmentId, dataType, createdTimestamp from service_metric_by_customer_datatype where customerId = ?");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId));
		
		Row row;
		while((row = rs.one()) !=null) {
			if(row.getLong("equipmentId") == equipmentId) {
				dataTypes.add(row.getInt("dataType"));
				if(row.getLong("createdTimestamp")< createdBeforeTimestamp) {
					timestamps.add(row.getLong("createdTimestamp"));
				}
			}
		}
	}

	//Important: do not use this method outside of local testing 
	private void getDataTypes_service_metric_by_equipment_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Integer> dataTypes){
		PreparedStatement preparedStmt = cqlSession.prepare("select dataType, createdTimestamp from service_metric_by_equipment_datatype where customerId = ? and equipmentId  = ? ");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId));
		
		Row row;
		while((row = rs.one()) !=null) {
			if(row.getLong("createdTimestamp")< createdBeforeTimestamp) {
				dataTypes.add(row.getInt("dataType"));
			}
		}
	}


	//Important: do not use this method outside of local testing 
	private void getDataTypes_service_metric_by_mac_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs, Set<Integer> dataTypes, Set<Long> timestamps){
		PreparedStatement preparedStmt = cqlSession.prepare("select clientMac, dataType, createdTimestamp from service_metric_by_mac_datatype where customerId = ? and equipmentId = ? and createdTimestamp < ? allow filtering");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
		
		Row row;
		while((row = rs.one()) !=null) {
			clientMacs.add(row.getLong("clientMac"));		
			dataTypes.add(row.getInt("dataType"));
			timestamps.add(row.getLong("createdTimestamp"));
		}
	}

	//Important: do not use this method outside of local testing 
	private void getDataTypes_service_metric_by_mac_and_equipment_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Long> clientMacs, Set<Integer> dataTypes){
		PreparedStatement preparedStmt = cqlSession.prepare("select clientMac, dataType from service_metric_by_mac_and_equipment_datatype where customerId = ? and equipmentId = ? and createdTimestamp < ? allow filtering");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
		
		Row row;
		while((row = rs.one()) !=null) {
			clientMacs.add(row.getLong("clientMac"));		
			dataTypes.add(row.getInt("dataType"));
		}
	}

	
	@Override
	//Important: do not use this method outside of local testing 
	public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {

		//select dayOfYear, clientMac, dataType from service_metric where customerId = 1 and equipmentId  = 1  and createdTimestamp < 1594837188000 allow filtering;
		//delete from service_metric where customerId = 1 and equipmentId  = 1 and dayOfYear in (1,2,3,4,5,365) and clientMac in (1,2,3) and dataType in (1,2,3) and createdTimestamp < 1594837188000;
		Set<Integer> daysOfYear = new HashSet<>(); 
		Set<Long> clientMacs = new HashSet<>();
		Set<Integer> dataTypes = new HashSet<>();

		getDaysOfYearAndMacsAndDataTypes_service_metric(customerId, equipmentId, createdBeforeTimestamp, daysOfYear, clientMacs, dataTypes);
		
		if(!daysOfYear.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(daysOfYear);
	        queryArgs.addAll(clientMacs);
	        queryArgs.addAll(dataTypes);
	        queryArgs.add(createdBeforeTimestamp);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric where customerId = ? and equipmentId  = ? and dayOfYear in "+getBindPlaceholders(daysOfYear)+" and clientMac in "+getBindPlaceholders(clientMacs)+" and dataType in "+getBindPlaceholders(dataTypes)+" and createdTimestamp < ? ");
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select equipmentId, createdTimestamp from service_metric_by_customer where customerId = 1 and createdTimestamp < 1594837188000;
		//delete from service_metric_by_customer where customerId = 1 and equipmentId =1 and createdTimestamp in (1594837188000, 1594837189000);
		Set<Long> timestamps = getCreatedTimestamps_service_metric_by_customer(customerId, equipmentId, createdBeforeTimestamp);

		if(!timestamps.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(timestamps);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_customer where customerId = ? and equipmentId =? and createdTimestamp in " + getBindPlaceholders(timestamps));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_customer {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//delete from service_metric_by_equipment where customerId = 1 and equipmentId  = 1 and createdTimestamp < 1594837188000;
		{
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.add(createdBeforeTimestamp);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_equipment where customerId = ? and equipmentId  = ? and createdTimestamp < ?");
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_equipment {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select clientMac, createdTimestamp from service_metric_by_mac where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
		//delete from service_metric_by_mac where customerId = 1  and equipmentId = 1 and clientMac in (1, 2) and createdTimestamp in (1594837188000, 1594837189000);
		clientMacs.clear();
		timestamps.clear();
		getMacsAndTimestamps_service_metric_by_mac(customerId, equipmentId, createdBeforeTimestamp, clientMacs, timestamps);
		
		if(!clientMacs.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(clientMacs);
	        queryArgs.addAll(timestamps);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_mac where customerId = ?  and equipmentId = ? and clientMac in "+getBindPlaceholders(clientMacs)+" and createdTimestamp in " + getBindPlaceholders(timestamps));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_mac {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select clientMac from service_metric_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
		//delete from service_metric_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and  createdTimestamp < 1594837188000  and clientMac in (1, 2) ;
		clientMacs.clear();
		getMacs_service_metric_by_mac_and_equipment(customerId, equipmentId, createdBeforeTimestamp, clientMacs);
		if(!clientMacs.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.add(createdBeforeTimestamp);
	        queryArgs.addAll(clientMacs);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_mac_and_equipment where customerId = ? and equipmentId = ? and  createdTimestamp < ?  and clientMac in "+getBindPlaceholders(clientMacs));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_mac_and_equipment {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}
		
		//select equipmentId, dataType, createdTimestamp from service_metric_by_customer_datatype where customerId = 1;
		//delete from service_metric_by_customer_datatype where customerId = 1 and equipmentId = 1 and dataType in (1,2,3) and createdTimestamp in (1594837188000, 1594837189000);
		dataTypes.clear();
		timestamps.clear();
		getDataTypes_service_metric_by_customer_datatype(customerId, equipmentId, createdBeforeTimestamp, dataTypes, timestamps);
		if(!dataTypes.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(dataTypes);
	        queryArgs.addAll(timestamps);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_customer_datatype where customerId = ? and equipmentId = ? and dataType in "+getBindPlaceholders(dataTypes)+" and createdTimestamp in "+ getBindPlaceholders(timestamps));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_customer_datatype {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}
		
		//select dataType from service_metric_by_equipment_datatype where customerId = 1 and equipmentId  = 1;
		//delete from service_metric_by_equipment_datatype where customerId = 1 and equipmentId  = 1 and createdTimestamp < 1594837188000  and dataType in (1,2,3) ;
		dataTypes.clear();
		getDataTypes_service_metric_by_equipment_datatype(customerId, equipmentId, createdBeforeTimestamp, dataTypes);
		if(!dataTypes.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.add(createdBeforeTimestamp);
	        queryArgs.addAll(dataTypes);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_equipment_datatype where customerId = ? and equipmentId  = ? and createdTimestamp < ? and dataType in  "+getBindPlaceholders(dataTypes) );
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_equipment_datatype {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select clientMac, dataType, createdTimestamp from service_metric_by_mac_datatype where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
		//delete from service_metric_by_mac_datatype where customerId = 1 and equipmentId = 1 and clientMac in (1, 2) and dataType in (1,2,3) and  createdTimestamp in (1594837188000, 1594837189000);
		clientMacs.clear();
		dataTypes.clear();
		timestamps.clear();
		getDataTypes_service_metric_by_mac_datatype(customerId, equipmentId, createdBeforeTimestamp, clientMacs, dataTypes, timestamps);
		if(!dataTypes.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(clientMacs);
	        queryArgs.addAll(dataTypes);
	        queryArgs.addAll(timestamps);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_mac_datatype where customerId = ? and equipmentId = ? and clientMac in "+getBindPlaceholders(clientMacs)+" and dataType in "+getBindPlaceholders(dataTypes)+" and  createdTimestamp in "+ getBindPlaceholders(timestamps));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_mac_datatype {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select clientMac, dataType from service_metric_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
		//delete from service_metric_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1  and createdTimestamp < 1594837188000 and clientMac in (1, 2)  and dataType in (1,2,3);
		clientMacs.clear();
		dataTypes.clear();
		getDataTypes_service_metric_by_mac_and_equipment_datatype(customerId, equipmentId, createdBeforeTimestamp, clientMacs, dataTypes);
		if(!dataTypes.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.add(createdBeforeTimestamp);
	        queryArgs.addAll(clientMacs);
	        queryArgs.addAll(dataTypes);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from service_metric_by_mac_and_equipment_datatype where customerId = ? and equipmentId = ?  and createdTimestamp < ? and clientMac in "+getBindPlaceholders(clientMacs)+"  and dataType in "+getBindPlaceholders(dataTypes));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from service_metric_by_mac_and_equipment_datatype {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}


        LOG.debug("Deleted service metrics {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
	}
	
	private List<ServiceMetric> getPage(int customerId, Set<Long> equipmentIds, Set<Integer> daysOfYear, Set<Long> clientMacs, Set<Long> timestamps, Set<Integer> dataTypes) {
		//select * from service_metric where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and clientMac in (1,2,3) and createdTimestamp in (1594837187000, 1594837188000 , 1594837189000) and dataType in (1,2,3);
		List<ServiceMetric> ret = new ArrayList<>();
		
		ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.addAll(equipmentIds);
        queryArgs.addAll(daysOfYear);
        queryArgs.addAll(clientMacs);
        queryArgs.addAll(timestamps);
        queryArgs.addAll(dataTypes);

		PreparedStatement preparedStmt = cqlSession.prepare("select * from service_metric where customerId = ? "
				+ "and equipmentId  in "+getBindPlaceholders(equipmentIds)
				+" and dayOfYear in "+getBindPlaceholders(daysOfYear)
				+" and clientMac in " +getBindPlaceholders(clientMacs)
				+" and createdTimestamp in "+getBindPlaceholders(timestamps)
				+" and dataType in " + getBindPlaceholders(dataTypes));	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
		
		Row row;
		while((row = rs.one()) !=null) {
			ret.add(serviceMetricRowMapper.mapRow(row));
		}

		return ret;
	}

	private static enum FilterOptions {
		/**
		 * All filters are provided and not empty - customerId, equipmentIds, clientMacs and dataTypes. This is the most efficient way to query, does not use index tables, goes through the service_metric table directly
		 */
		all_filters, 
		/**
		 * Only customerId is provided. First iterate through the service_metric_by_customer table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		customer,   
		/**
		 * Only customerId and equipmentIds are provided. First iterate through the service_metric_by_equipment table, collect a page of keys, then use getPage() method to read from the service_metric table. 
		 */
		equipment,   
		/**
		 * Only customerId and clientMacs are provided. First iterate through the service_metric_by_mac table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		mac, 
		/**
		 * Only customerId, equipmentIds and clientMacs are provided. First iterate through the service_metric_by_mac_and_equipment table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		mac_and_equipment,  
		/**
		 * Only customerId and dataTypes are provided. First iterate through the service_metric_by_customer_datatype table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		customer_dataType,  
		/**
		 * Only customerId, equipmentIds and dataTypes are provided. First iterate through the service_metric_by_equipment_datatype table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		equipment_dataType,  
		/**
		 * Only customerId, clientMacs and dataTypes are provided. First iterate through the service_metric_by_mac_datatype table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		mac_dataType, 
		/**
		 * Note: this case is covered by the all_filters element above. Keeping it here only in case we need to extend ways of filtering. May remove it and the corresponding index table in the future. only customerId, equipmentIds, clientMacs and dataTypes are provided. First iterate through the service_metric_by_mac_and_equipment_datatype table, collect a page of keys, then use getPage() method to read from the service_metric table.
		 */
		mac_and_equipment_dataType  
	}

	@Override
	public PaginationResponse<ServiceMetric> getForCustomer(
			long fromTime,
			long toTime,
			int customerId, 
			Set<Long> equipmentIds,
			Set<MacAddress> clientMacAdresses, 
			Set<ServiceMetricDataType> dataTypes, 
			List<ColumnAndSort> sortBy,
			PaginationContext<ServiceMetric> context) {

    	if(context ==null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<ServiceMetric> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up ServiceMetrics for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up ServiceMetrics for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        //select the index table based on provided inputs, create a query and bind variables
        FilterOptions filterOptions = null;
        String query = null;
        ArrayList<Object> queryArgs = new ArrayList<>();
    	Set<Integer> daysOfYear = calculateDaysOfYear(fromTime, toTime);

        if(isPresent(equipmentIds) && isPresent(clientMacAdresses) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.all_filters;
			query = "select * from service_metric where customerId = ? "
					+ " and equipmentId in "+getBindPlaceholders(equipmentIds)
					+" and dayOfYear in "+getBindPlaceholders(daysOfYear)
					+" and clientMac in "+getBindPlaceholders(clientMacAdresses)
					+" and dataType in "+getBindPlaceholders(dataTypes)
					+" and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            queryArgs.addAll(daysOfYear);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            dataTypes.forEach(dt -> queryArgs.add(dt.getId()));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && !isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
        	filterOptions = FilterOptions.customer;
        	query = "select * from service_metric_by_customer where customerId = ? "
        			+ " and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && !isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
        	filterOptions = FilterOptions.equipment;
        	query = "select * from service_metric_by_equipment where customerId = ? "
        			+ " and equipmentId in "+getBindPlaceholders(equipmentIds) 
        			+" and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
        	filterOptions = FilterOptions.mac;
        	query = "select * from service_metric_by_mac where customerId = ? "
        			+ " and clientMac in "+getBindPlaceholders(clientMacAdresses)
        			+" and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && isPresent(clientMacAdresses) && !isPresent(dataTypes)) {
        	filterOptions = FilterOptions.mac_and_equipment;
        	query = "select * from service_metric_by_mac_and_equipment where customerId = ? "
        			+ " and clientMac in  "+getBindPlaceholders(clientMacAdresses)
        			+ " and equipmentId in "+getBindPlaceholders(equipmentIds)
        			+ " and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));
            queryArgs.addAll(equipmentIds);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && !isPresent(clientMacAdresses) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.customer_dataType;
        	query = "select * from service_metric_by_customer_datatype where customerId = ? "
        			+ " and dataType in "+getBindPlaceholders(dataTypes)
        			+ " and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            dataTypes.forEach(dt -> queryArgs.add(dt.getId()));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && !isPresent(clientMacAdresses) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.equipment_dataType;
        	query = "select * from service_metric_by_equipment_datatype where customerId = ? "
        			+ " and equipmentId in  "+getBindPlaceholders(equipmentIds)
        			+ " and dataType in "+getBindPlaceholders(dataTypes)
        			+ " and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);            
            dataTypes.forEach(dt -> queryArgs.add(dt.getId()));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && isPresent(clientMacAdresses) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.mac_dataType;
        	query = "select * from service_metric_by_mac_datatype where customerId = ? "
        			+ " and clientMac in "+getBindPlaceholders(clientMacAdresses)
        			+ " and dataType in "+getBindPlaceholders(dataTypes)
        			+ " and createdTimestamp >= ? and createdTimestamp <= ? ";
            queryArgs.add(customerId);
            clientMacAdresses.forEach(m -> queryArgs.add(m.getAddressAsLong()));            
            dataTypes.forEach(dt -> queryArgs.add(dt.getId()));
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
        
        if(context.getThirdPartyPagingState()!=null) {
        	ByteBuffer currentPagingState = ByteBuffer.wrap(context.getThirdPartyPagingState());
        	//have to do it this way - setPagingState creates new object
        	boundStmt = boundStmt.setPagingState(currentPagingState);
        }
        
		ResultSet rs = cqlSession.execute(boundStmt);
		ByteBuffer nextPagingState = rs.getExecutionInfo().getPagingState();

		//populate page items
		List<ServiceMetric> pageItems = new ArrayList<>();
		
        //get a page of keys from it, call getPage(...) with those keys

		switch(filterOptions) {
		case all_filters:
			// iterate through the current page directly
			while (rs.getAvailableWithoutFetching() > 0) {
			  pageItems.add(serviceMetricRowMapper.mapRow(rs.one()));
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
			//the query was against the index table table
			//find all the keys for the page, then retrieve records for them from service_metric table
			Set<Long> equipmentIdsIdx = new HashSet<>();
			Set<Long> clientMacAdressesIdx = new HashSet<>();
			Set<Long> timestampsIdx = new HashSet<>();
			Set<Integer> dataTypesIdx = new HashSet<>();
			Set<Integer> daysOfYearIdx = new HashSet<>();
			Row row;
			while (rs.getAvailableWithoutFetching() > 0) {
				row = rs.one();
				equipmentIdsIdx.add(row.getLong("equipmentId"));
				clientMacAdressesIdx.add(row.getLong("clientMac"));
				timestampsIdx.add(row.getLong("createdTimestamp"));
				dataTypesIdx.add(row.getInt("dataType"));
				daysOfYearIdx.add(row.getInt("dayOfYear"));
			}

			List<ServiceMetric> pageOfMetrics = getPage(customerId, equipmentIdsIdx, daysOfYearIdx, clientMacAdressesIdx, timestampsIdx, dataTypesIdx);
			pageItems.addAll(pageOfMetrics);			
			break;
		default:
			LOG.warn("Unknown filter option:", filterOptions);
			throw new IllegalArgumentException("Unknown filter option " + filterOptions);
		}

        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find ServiceMetrics for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} ServiceMetrics for customer {} with last returned page number {}",
                    pageItems.size(), customerId, context.getLastReturnedPageNumber());
        }

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(nextPagingState!=null) {
        	ret.getContext().setThirdPartyPagingState(nextPagingState.array());
        } else {
        	ret.getContext().setThirdPartyPagingState(null);
        }
        
        // startAfterItem is not used in Cassandra datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;	
    }

}
