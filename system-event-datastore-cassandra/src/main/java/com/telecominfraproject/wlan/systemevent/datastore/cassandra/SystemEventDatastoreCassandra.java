package com.telecominfraproject.wlan.systemevent.datastore.cassandra;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

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
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtop
 *
 */
@Component
public class SystemEventDatastoreCassandra implements SystemEventDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventDatastoreCassandra.class);

    private static final String[] ALL_COLUMNS_LIST = {        
            
            //TODO: add colums from properties of SystemEventRecord in here
            "customerId",
            "equipmentId",
            "dayOfYear",
            "dataType",
            "eventTimestamp",
            "details"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());

        private static final Set<String> columnsToSkipForInsertInIndex = new HashSet<>(Arrays.asList("details"));

        private static final String TABLE_NAME = "system_event";
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
                "insert into system_event_by_customer ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_EQUIPMENT =
                "insert into system_event_by_equipment ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_CUSTOMER_DATATYPE =
                "insert into system_event_by_customer_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";

        private static final String CQL_INSERT_IDX_BY_EQUIPMENT_DATATYPE =
                "insert into system_event_by_equipment_datatype ( " 
                + ALL_COLUMNS_FOR_INSERT_INDEX
                + " ) values ( "+BIND_VARS_FOR_INSERT_INDEX+" ) ";


    private static final RowMapper<SystemEventRecord> systemEventRowMapper = new SystemEventRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_create_idx_by_customer;
	private PreparedStatement preparedStmt_create_idx_by_equipment;
	private PreparedStatement preparedStmt_create_idx_by_customer_datatype;
	private PreparedStatement preparedStmt_create_idx_by_equipment_datatype;
	
	private List<PreparedStatement> preparedStmt_create_idx_list = new ArrayList<>();

	@PostConstruct
	private void postConstruct(){

		preparedStmt_create = cqlSession.prepare(CQL_INSERT);
		
		preparedStmt_create_idx_by_customer = cqlSession.prepare(CQL_INSERT_IDX_BY_CUSTOMER);
		preparedStmt_create_idx_by_equipment = cqlSession.prepare(CQL_INSERT_IDX_BY_EQUIPMENT);
		preparedStmt_create_idx_by_customer_datatype = cqlSession.prepare(CQL_INSERT_IDX_BY_CUSTOMER_DATATYPE);
		preparedStmt_create_idx_by_equipment_datatype = cqlSession.prepare(CQL_INSERT_IDX_BY_EQUIPMENT_DATATYPE);
		
		preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_customer);
		preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_customer_datatype);
		preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_equipment);
		preparedStmt_create_idx_list.add(preparedStmt_create_idx_by_equipment_datatype);

	}

	private static final TimeZone tz = TimeZone.getTimeZone("GMT");

	private int getDayOfYear(long timestampMs) {
		//all the date-time operations on the server are always in GMT
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTimeInMillis(timestampMs);
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

		return dayOfYear;
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
                //TODO: add remaining properties from SystemEventRecord here 
                systemEvent.getCustomerId(),
                systemEvent.getEquipmentId(),
                dayOfYear,
                systemEvent.getDataType(),
                systemEvent.getEventTimestamp(),
                
              	(systemEvent.getDetails()!=null) ? ByteBuffer.wrap(systemEvent.getDetails().toZippedBytes()) : null
				
				));
		
		//create index records
		preparedStmt_create_idx_list.forEach(ps -> 	cqlSession.execute(ps.bind(
                systemEvent.getCustomerId(),
                systemEvent.getEquipmentId(),
                dayOfYear,
                systemEvent.getDataType(),
                systemEvent.getEventTimestamp()
				
				)));
				
        LOG.debug("Stored SystemEventRecord {}", systemEvent);
	}

	@Override
	public void create(List<SystemEventRecord> serviceMetrics) {
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
	private void getDaysOfYearAndDataTypes_system_event(int customerId, long equipmentId, long createdBeforeTimestamp, Set<Integer> daysOfYear, Set<String> dataTypes){
		PreparedStatement preparedStmt = cqlSession.prepare("select dayOfYear, dataType from system_event where customerId = ? and equipmentId  = ?  and eventTimestamp < ? allow filtering");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId, createdBeforeTimestamp));
		
		Row row;
		while((row = rs.one()) !=null) {
			daysOfYear.add(row.getInt("dayOfYear"));
			dataTypes.add(row.getString("dataType"));
		}
		
	}

	//Important: do not use this method outside of local testing 
	private Set<Long> getEventTimestamps_system_event_by_customer(int customerId, long equipmentId, long createdBeforeTimestamp){
		PreparedStatement preparedStmt = cqlSession.prepare("select equipmentId, eventTimestamp from system_event_by_customer where customerId = ? and eventTimestamp < ?");	
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
	private void getDataTypes_system_event_by_customer_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<String> dataTypes, Set<Long> timestamps){
		PreparedStatement preparedStmt = cqlSession.prepare("select equipmentId, dataType, eventTimestamp from system_event_by_customer_datatype where customerId = ?");	
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
	private void getDataTypes_system_event_by_equipment_datatype(int customerId, long equipmentId, long createdBeforeTimestamp, Set<String> dataTypes){
		PreparedStatement preparedStmt = cqlSession.prepare("select dataType, eventTimestamp from system_event_by_equipment_datatype where customerId = ? and equipmentId  = ? ");	
		ResultSet rs = cqlSession.execute(preparedStmt.bind(customerId, equipmentId));
		
		Row row;
		while((row = rs.one()) !=null) {
			if(row.getLong("eventTimestamp")< createdBeforeTimestamp) {
				dataTypes.add(row.getString("dataType"));
			}
		}
	}

	/**
	 * @param c - collection of values
	 * @return string containing bind variable placeholders - one for each element of the supplied collection. e.g. " (?,?,?) "
	 */
	private String getBindPlaceholders(Collection<?> c) {
        StringBuilder strb = new StringBuilder(100);
        strb.append(" (");
        for (int i = 0; i < c.size(); i++) {
            strb.append("?");
            if (i < c.size() - 1) {
                strb.append(",");
            }
        }
        strb.append(") ");

        return strb.toString();
	}
	
	@Override
	//Important: do not use this method outside of local testing 
	public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {

		//select dayOfYear, dataType from system_event where customerId = 1 and equipmentId  = 1  and eventTimestamp < 1594837188000 allow filtering;
		//delete from system_event where customerId = 1 and equipmentId  = 1 and dayOfYear in (1,2,3,4,5,365) and dataType in ('1','2') and eventTimestamp < 1594837188000;
		Set<Integer> daysOfYear = new HashSet<>(); 
		Set<String> dataTypes = new HashSet<>();

		getDaysOfYearAndDataTypes_system_event(customerId, equipmentId, createdBeforeTimestamp, daysOfYear, dataTypes);
		
		if(!daysOfYear.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(daysOfYear);
	        queryArgs.addAll(dataTypes);
	        queryArgs.add(createdBeforeTimestamp);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from system_event where customerId = ? and equipmentId  = ? and dayOfYear in "+getBindPlaceholders(daysOfYear)+" and dataType in "+getBindPlaceholders(dataTypes)+" and eventTimestamp < ? ");
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from system_event {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select equipmentId, eventTimestamp from system_event_by_customer where customerId = 1 and eventTimestamp < 1594837188000;
		//delete from system_event_by_customer where customerId = 1 and equipmentId =1 and eventTimestamp in (1594837188000, 1594837189000);
		Set<Long> timestamps = getEventTimestamps_system_event_by_customer(customerId, equipmentId, createdBeforeTimestamp);

		if(!timestamps.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(timestamps);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from system_event_by_customer where customerId = ? and equipmentId =? and eventTimestamp in " + getBindPlaceholders(timestamps));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from system_event_by_customer {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//delete from system_event_by_equipment where customerId = 1 and equipmentId  = 1 and eventTimestamp < 1594837188000;
		{
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.add(createdBeforeTimestamp);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from system_event_by_equipment where customerId = ? and equipmentId  = ? and eventTimestamp < ?");
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from system_event_by_equipment {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

		//select equipmentId, dataType, eventTimestamp from system_event_by_customer_datatype where customerId = 1;
		//delete from system_event_by_customer_datatype where customerId = 1 and equipmentId = 1 and dataType in ('1','2') and eventTimestamp in (1594837188000, 1594837189000);
		dataTypes.clear();
		timestamps.clear();
		getDataTypes_system_event_by_customer_datatype(customerId, equipmentId, createdBeforeTimestamp, dataTypes, timestamps);
		if(!dataTypes.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.addAll(dataTypes);
	        queryArgs.addAll(timestamps);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from system_event_by_customer_datatype where customerId = ? and equipmentId = ? and dataType in "+getBindPlaceholders(dataTypes)+" and eventTimestamp in "+ getBindPlaceholders(timestamps));
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from system_event_by_customer_datatype {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}
		
		//select dataType from system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1;
		//delete from system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1 and eventTimestamp < 1594837188000  and dataType in ('1','2') ;
		dataTypes.clear();
		getDataTypes_system_event_by_equipment_datatype(customerId, equipmentId, createdBeforeTimestamp, dataTypes);
		if(!dataTypes.isEmpty()) {
			ArrayList<Object> queryArgs = new ArrayList<>();
	        queryArgs.add(customerId);
	        queryArgs.add(equipmentId);
	        queryArgs.add(createdBeforeTimestamp);
	        queryArgs.addAll(dataTypes);
	        
			PreparedStatement preparedStmt = cqlSession.prepare("delete from system_event_by_equipment_datatype where customerId = ? and equipmentId  = ? and eventTimestamp < ? and dataType in  "+getBindPlaceholders(dataTypes) );
			cqlSession.execute(preparedStmt.bind(queryArgs.toArray()));
			
			LOG.debug("Removed records from system_event_by_equipment_datatype {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
		}

        LOG.debug("Deleted service metrics {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
	}
	
	private List<SystemEventRecord> getPage(int customerId, Set<Long> equipmentIds, Set<Integer> daysOfYear, Set<Long> timestamps, Set<String> dataTypes) {
		//select * from system_event where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and eventTimestamp in (1594837187000, 1594837188000 , 1594837189000) and dataType in ('1','2');
		List<SystemEventRecord> ret = new ArrayList<>();
		
		ArrayList<Object> queryArgs = new ArrayList<>();
        queryArgs.add(customerId);
        queryArgs.addAll(equipmentIds);
        queryArgs.addAll(daysOfYear);
        queryArgs.addAll(timestamps);
        queryArgs.addAll(dataTypes);

		PreparedStatement preparedStmt = cqlSession.prepare("select * from system_event where customerId = ? "
				+ "and equipmentId  in "+getBindPlaceholders(equipmentIds)
				+" and dayOfYear in "+getBindPlaceholders(daysOfYear)
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
		 * All filters are provided and not empty - customerId, equipmentIds and dataTypes. This is the most efficient way to query, does not use index tables, goes through the system_event table directly
		 */
		all_filters, 
		/**
		 * Only customerId is provided. First iterate through the system_event_by_customer table, collect a page of keys, then use getPage() method to read from the system_event table.
		 */
		customer,   
		/**
		 * Only customerId and equipmentIds are provided. First iterate through the system_event_by_equipment table, collect a page of keys, then use getPage() method to read from the system_event table. 
		 */
		equipment,   
		/**
		 * Only customerId and dataTypes are provided. First iterate through the system_event_by_customer_datatype table, collect a page of keys, then use getPage() method to read from the system_event table.
		 */
		customer_dataType,  
		/**
		 * Note: this case is covered by the all_filters element above. Keeping it here only in case we need to extend ways of filtering. May remove it and the corresponding index table in the future. Only customerId, equipmentIds and dataTypes are provided. First iterate through the system_event_by_equipment_datatype table, collect a page of keys, then use getPage() method to read from the system_event table.
		 */
		equipment_dataType,  
	}

	private boolean isPresent( Collection<?> c) {
		return c!=null && !c.isEmpty();
	}
	
	@Override
	public PaginationResponse<SystemEventRecord> getForCustomer(
			long fromTime,
			long toTime,
			int customerId, 
			Set<Long> equipmentIds,
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
                    "No more pages available when looking up SystemEventRecords for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up SystemEventRecords for customer {} with last returned page number {}", 
                customerId, context.getLastReturnedPageNumber());

        //select the index table based on provided inputs, create a query and bind variables
        FilterOptions filterOptions = null;
        String query = null;
        ArrayList<Object> queryArgs = new ArrayList<>();
    	Set<Integer> daysOfYear = calculateDaysOfYear(fromTime, toTime);

        if(isPresent(equipmentIds) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.all_filters;
			query = "select * from system_event where customerId = ? "
					+ " and equipmentId in "+getBindPlaceholders(equipmentIds)
					+" and dayOfYear in "+getBindPlaceholders(daysOfYear)
					+" and dataType in "+getBindPlaceholders(dataTypes)
					+" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            queryArgs.addAll(daysOfYear);
            dataTypes.forEach(dt -> queryArgs.add(dt));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && !isPresent(dataTypes)) {
        	filterOptions = FilterOptions.customer;
        	query = "select * from system_event_by_customer where customerId = ? "
        			+ " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && !isPresent(dataTypes)) {
        	filterOptions = FilterOptions.equipment;
        	query = "select * from system_event_by_equipment where customerId = ? "
        			+ " and equipmentId in "+getBindPlaceholders(equipmentIds) 
        			+" and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(!isPresent(equipmentIds) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.customer_dataType;
        	query = "select * from system_event_by_customer_datatype where customerId = ? "
        			+ " and dataType in "+getBindPlaceholders(dataTypes)
        			+ " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            dataTypes.forEach(dt -> queryArgs.add(dt));
            queryArgs.add(fromTime);
            queryArgs.add(toTime);
        } else if(isPresent(equipmentIds) && isPresent(dataTypes)) {
        	filterOptions = FilterOptions.equipment_dataType;
        	query = "select * from system_event_by_equipment_datatype where customerId = ? "
        			+ " and equipmentId in  "+getBindPlaceholders(equipmentIds)
        			+ " and dataType in "+getBindPlaceholders(dataTypes)
        			+ " and eventTimestamp >= ? and eventTimestamp <= ? ";
            queryArgs.add(customerId);
            queryArgs.addAll(equipmentIds);            
            dataTypes.forEach(dt -> queryArgs.add(dt));
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
		List<SystemEventRecord> pageItems = new ArrayList<>();
		
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
			//the query was against the index table table
			//find all the keys for the page, then retrieve records for them from system_event table
			Set<Long> equipmentIdsIdx = new HashSet<>();
			Set<Long> clientMacAdressesIdx = new HashSet<>();
			Set<Long> timestampsIdx = new HashSet<>();
			Set<String> dataTypesIdx = new HashSet<>();
			Set<Integer> daysOfYearIdx = new HashSet<>();
			Row row;
			while (rs.getAvailableWithoutFetching() > 0) {
				row = rs.one();
				equipmentIdsIdx.add(row.getLong("equipmentId"));
				timestampsIdx.add(row.getLong("eventTimestamp"));
				dataTypesIdx.add(row.getString("dataType"));
				daysOfYearIdx.add(row.getInt("dayOfYear"));
			}

			List<SystemEventRecord> pageOfMetrics = getPage(customerId, equipmentIdsIdx, daysOfYearIdx, timestampsIdx, dataTypesIdx);
			pageItems.addAll(pageOfMetrics);			
			break;
		default:
			LOG.warn("Unknown filter option:", filterOptions);
			throw new IllegalArgumentException("Unknown filter option " + filterOptions);
		}

        if (pageItems.isEmpty()) {
            LOG.debug("Cannot find SystemEventRecords for customer {} with last returned page number {}",
                    customerId, context.getLastReturnedPageNumber());
        } else {
            LOG.debug("Found {} SystemEventRecords for customer {} with last returned page number {}",
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

	private static Set<Integer> calculateDaysOfYear(long fromTime, long toTime) {
		if(fromTime > toTime) {
			throw new IllegalArgumentException("From time must be before To time");
		}

		//all the date-time operations on the server are always in GMT
		Calendar calendarFrom = Calendar.getInstance(tz);
		calendarFrom.setTimeInMillis(fromTime);
		int fromDayOfYear = calendarFrom.get(Calendar.DAY_OF_YEAR);
		int fromYear = calendarFrom.get(Calendar.YEAR);  

		Calendar calendarTo = Calendar.getInstance(tz);
		calendarTo.setTimeInMillis(toTime);
		int toDayOfYear = calendarTo.get(Calendar.DAY_OF_YEAR);
		int toYear = calendarTo.get(Calendar.YEAR);  
		
		Set<Integer> days = new HashSet<>();
		//build a set of daysOfYear - it may contain extra days, they will be ignored because there are other filters used in addition to this synthetic one
		if(fromDayOfYear <= toDayOfYear && fromYear == toYear) {
			//simple case - the from and to dates are in the same year
			for(int i = fromDayOfYear; i<=toDayOfYear; i++) {
				days.add(i);
			}
		} else if(fromDayOfYear > toDayOfYear && fromYear == toYear - 1) {
			//the from and to dates are in the adjacent years
			for(int i = fromDayOfYear; i<=366; i++) {
				days.add(i);
			}
			
			for(int i = 1; i<=toDayOfYear; i++) {
				days.add(i);
			}
		} else {
			//the from and to dates are more than a year apart
			for(int i = 1; i<=366; i++) {
				days.add(i);
			}
			
		}

		LOG.trace("Calendar from doy = {} y = {}", fromDayOfYear , fromYear);
		LOG.trace("Calendar to doy = {} y = {} ", toDayOfYear, toYear);
		LOG.trace("days = {}", days);
		
		return days;
	}

	public static void main(String[] args) {
		long fromTime = 1579357425000L; // Saturday, January 18, 2020 2:23:45 PM
		long toTime = 1600611825000L; //Sunday, September 20, 2020 2:23:45 PM
		calculateDaysOfYear(fromTime , toTime );

		fromTime = 1579357425000L; // Saturday, January 18, 2020 2:23:45 PM
		toTime = 1579359634000L; //Saturday, January 18, 2020 3:00:34 PM
		calculateDaysOfYear(fromTime , toTime );

		fromTime = 1576008034000L; // Tuesday, December 10, 2019 8:00:34 PM
		toTime = 1579359634000L; //Saturday, January 18, 2020 3:00:34 PM
		calculateDaysOfYear(fromTime , toTime );

		fromTime = 1453320034000L; // Wednesday, January 20, 2016 8:00:34 PM
		toTime = 1579359634000L; //Saturday, January 18, 2020 3:00:34 PM
		calculateDaysOfYear(fromTime , toTime );

	}
}
