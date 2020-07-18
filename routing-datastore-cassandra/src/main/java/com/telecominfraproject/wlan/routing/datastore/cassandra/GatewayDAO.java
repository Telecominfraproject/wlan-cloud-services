package com.telecominfraproject.wlan.routing.datastore.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.InvalidQueryException;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.core.model.utils.TimeBasedId;
import com.telecominfraproject.wlan.core.server.cassandra.RowMapper;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtop
 *
 */
@Component
public class GatewayDAO  {

    private static final Logger LOG = LoggerFactory.getLogger(GatewayDAO.class);

    private static final String[] ALL_COLUMNS_LIST = {        
            
            //TODO: add columns from properties of EquipmentGatewayRecord in here
    		"id",
            "hostname",
            "ipAddr",
            "port",
            "gatewayType",
            
            "createdTimestamp",
            "lastModifiedTimestamp"
        };
        
        private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList());
        private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList( 
        		"id",
                "createdTimestamp"));
        
        private static final String TABLE_NAME = "equipment_gateway";
        private static final String ALL_COLUMNS;

        private static final String ALL_COLUMNS_FOR_INSERT; 
        private static final String BIND_VARS_FOR_INSERT;
        private static final String ALL_COLUMNS_UPDATE;
        
        static{
            StringBuilder strbAllColumns = new StringBuilder(1024);
            StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
            StringBuilder strbBindVarsForInsert = new StringBuilder(128);
            StringBuilder strbColumnsForUpdate = new StringBuilder(512);
            for(String colName: ALL_COLUMNS_LIST){

                strbAllColumns.append(colName).append(","); 
                
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
            strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
            strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
            strbColumnsForUpdate.deleteCharAt(strbColumnsForUpdate.length() - 1);

            ALL_COLUMNS = strbAllColumns.toString();
            ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
            BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
            ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();
            
        }
        

        private static final String CQL_GET_ALL =
                "select " + ALL_COLUMNS +
                " from "+TABLE_NAME+" ";

        private static final String CQL_GET_BY_ID =
        		CQL_GET_ALL +
        		" where id = ? ";
        
        private static final String CQL_GET_BY_HOSTNAME =
        		CQL_GET_ALL +
        		" where hostname = ? ";

        private static final String CQL_GET_BY_GATEWAY_TYPE =
        		CQL_GET_ALL +
        		" where gatewayType = ? ";
        
        private static final String CQL_GET_LASTMOD_BY_ID =
                "select lastModifiedTimestamp " +
                " from "+TABLE_NAME+" " +
                " where id = ?";

        private static final String CQL_INSERT =
            "insert into "+TABLE_NAME+" ( " 
            + ALL_COLUMNS_FOR_INSERT
            + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

        private static final String CQL_DELETE_BY_ID =
            "delete from "+TABLE_NAME+
            " where id = ? ";
        
        private static final String CQL_DELETE_HEADER =
                "delete from "+TABLE_NAME + " ";

        private static final String CQL_UPDATE =
            "update "+TABLE_NAME+" set "
            + ALL_COLUMNS_UPDATE +
            " where  id = ? "
            + " IF lastModifiedTimestamp = ? " 
            ;
    

    private static final RowMapper<EquipmentGatewayRecord> gatewayRowMapper = new GatewayRowMapper();
    
	@Autowired
	private CqlSession cqlSession;
	
	@Autowired
	private RoutingDAO routingDAO;
	
	private PreparedStatement preparedStmt_getByIdOrNull;
	private PreparedStatement preparedStmt_getByHostname;
	private PreparedStatement preparedStmt_getByGatewayType;
	private PreparedStatement preparedStmt_getLastmod;
	private PreparedStatement preparedStmt_create;
	private PreparedStatement preparedStmt_update;
	private PreparedStatement preparedStmt_deleteById;

	@PostConstruct
	private void postConstruct(){

		try {
			preparedStmt_getByIdOrNull = cqlSession.prepare(CQL_GET_BY_ID);
			preparedStmt_getByHostname = cqlSession.prepare(CQL_GET_BY_HOSTNAME);
			preparedStmt_getByGatewayType = cqlSession.prepare(CQL_GET_BY_GATEWAY_TYPE);
			preparedStmt_getLastmod = cqlSession.prepare(CQL_GET_LASTMOD_BY_ID);
			preparedStmt_create = cqlSession.prepare(CQL_INSERT);
			preparedStmt_update = cqlSession.prepare(CQL_UPDATE);
			preparedStmt_deleteById = cqlSession.prepare(CQL_DELETE_BY_ID);
		} catch (InvalidQueryException e) {
			LOG.error("Cannot prepare query", e);
			throw e;
		}
	}
	
	public EquipmentGatewayRecord create(EquipmentGatewayRecord gateway) {
		
        final long ts = System.currentTimeMillis();
        if(gateway.getCreatedTimestamp()<=0) {
        	gateway.setCreatedTimestamp(ts);
        }
        
        gateway.setLastModifiedTimestamp(ts);
        
        gateway.setId(TimeBasedId.generateIdFromTimeNanos());

		cqlSession.execute(preparedStmt_create.bind(
                //TODO: add remaining properties from EquipmentGatewayRecord here
				gateway.getId(),
                gateway.getHostname(),
                gateway.getIpAddr(),
                gateway.getPort(),
                gateway.getGatewayType().getId(),
                
                gateway.getCreatedTimestamp(),
                gateway.getLastModifiedTimestamp()
				
				));
		
        LOG.debug("Stored EquipmentGatewayRecord {}", gateway);

        return gateway.clone();
	}

		
	public EquipmentGatewayRecord getOrNull(long gatewayId) {
        LOG.debug("Looking up EquipmentGatewayRecord for id {}", gatewayId);

		ResultSet rs = cqlSession.execute(preparedStmt_getByIdOrNull.bind(gatewayId));
		
		Row row = rs.one();

		if(row!=null) {
			EquipmentGatewayRecord gatewayRecord = gatewayRowMapper.mapRow(row);
            LOG.debug("Found EquipmentGatewayRecord {}", gatewayRecord);
            
            return gatewayRecord;
		} else {
            LOG.debug("Could not find EquipmentGatewayRecord for id {}", gatewayId);
            return null;			
		}

	}
	
	public EquipmentGatewayRecord get(long gatewayId) {
		EquipmentGatewayRecord ret = getOrNull(gatewayId);
		if(ret == null) {
			throw new DsEntityNotFoundException("Cannot find EquipmentGatewayRecord " + gatewayId);
		}
		
		return ret;
	}


	public EquipmentGatewayRecord update(EquipmentGatewayRecord gateway) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = gateway.getLastModifiedTimestamp();
        
		ResultSet rs = cqlSession.execute(preparedStmt_update.bind(

                //TODO: add remaining properties from EquipmentGatewayRecord here
                gateway.getHostname(),
                gateway.getIpAddr(),
                gateway.getPort(),
                gateway.getGatewayType().getId(),
                                
                newLastModifiedTs,
                
                // use id for update operation
                gateway.getId(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs
                
        ));
        
		
        if(!rs.wasApplied()){
            
                //find out if record could not be updated because it does not exist or because it was modified concurrently
            	rs = cqlSession.execute(preparedStmt_getLastmod.bind(gateway.getId()) );
            	Row row = rs.one();
            	
            	if(row!=null) {
	                long recordTimestamp = row.getLong(0);
	                
	                LOG.debug("Concurrent modification detected for EquipmentGatewayRecord with id {} expected version is {} but version in db was {}", 
	                		gateway.getId(),
	                        incomingLastModifiedTs,
	                        recordTimestamp
	                        );
	                throw new DsConcurrentModificationException("Concurrent modification detected for EquipmentGatewayRecord with id " + 
	                		gateway.getId()
	                        +" expected version is " + incomingLastModifiedTs
	                        +" but version in db was " + recordTimestamp
	                        );
            } else {
                LOG.debug("Cannot find EquipmentGatewayRecord for {} ", gateway.getId());
                throw new DsEntityNotFoundException("EquipmentGatewayRecord not found " +  
                		gateway.getId());
            }
        }
        

        //make a copy so that we don't accidentally update caller's version by reference
        EquipmentGatewayRecord gatewayCopy = gateway.clone();
        gatewayCopy.setLastModifiedTimestamp(newLastModifiedTs);

        LOG.debug("Updated EquipmentGatewayRecord {}", gatewayCopy);
        
        return gatewayCopy;
    }
	
    public EquipmentGatewayRecord delete(long gatewayId) {
        EquipmentGatewayRecord ret = get(gatewayId);
        
        cqlSession.execute(preparedStmt_deleteById.bind(gatewayId));
                
        LOG.debug("Deleted EquipmentGatewayRecord {}", ret);
        
        return ret;
    }

	public List<EquipmentGatewayRecord> deleteGateway(String hostname) {
        LOG.debug("calling deleteGateway({})", hostname);

        List<EquipmentGatewayRecord> results = getGateway(hostname);
        
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> gatewayIds = new HashSet<Long>();
        results.forEach(r -> gatewayIds.add(r.getId()));

        ArrayList<Object> queryArgs = new ArrayList<>();

		//build the query	
		StringBuilder query = new StringBuilder();
		query.append(CQL_DELETE_HEADER);

        //add gatewayIds filters
        {
            queryArgs.addAll(gatewayIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append(" where id in (");
            for (int i = 0; i < gatewayIds.size(); i++) {
                strb.append("?");
                if (i < gatewayIds.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query.append(strb);
        }

        //TODO: create a cache of these prepared statements, keyed by the numberOfGatewayIds
        PreparedStatement preparedStmt_deleteGatewaysByIds;
        try {
        	preparedStmt_deleteGatewaysByIds = cqlSession.prepare(query.toString());
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query.toString(), e);
        	throw e;
        }
        
		cqlSession.execute(preparedStmt_deleteGatewaysByIds.bind(queryArgs.toArray() ));

        LOG.debug("deleteGateway({}) returns {} record(s)", hostname, results.size());
        return results;
	}

	public List<EquipmentGatewayRecord> getGateway(String hostname) {
        LOG.debug("calling getGateway({})", hostname);

        if (hostname == null || hostname.isEmpty()) {
            return Collections.emptyList();
        }

		ResultSet rs = cqlSession.execute(preparedStmt_getByHostname.bind(hostname ));
		
		List<EquipmentGatewayRecord> ret = new ArrayList<>();
		rs.forEach( row -> ret.add(gatewayRowMapper.mapRow(row)) );

        LOG.debug("getGateway({}) returns {} record(s)", hostname, ret.size());
        return ret;
	}

	public List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType) {
        LOG.debug("calling getGateway({})", gatewayType);

        ResultSet rs;
        if (gatewayType == null ) {
        	rs = cqlSession.execute(CQL_GET_ALL);
        } else {
        	rs = cqlSession.execute(preparedStmt_getByGatewayType.bind(gatewayType.getId()));
        }

		List<EquipmentGatewayRecord> ret = new ArrayList<>();
		rs.forEach( row -> ret.add(gatewayRowMapper.mapRow(row)) );

        LOG.debug("getGateway({}) returns {} record(s)", gatewayType, ret.size());
        return ret;
	}


	public List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId) {
        LOG.debug("calling getRegisteredGatewayRecordList({})", equipmentId);

        List<EquipmentRoutingRecord> routingRecords = routingDAO.getRegisteredRouteList(equipmentId);
        Set<Long> gatewayIds = new HashSet<Long>();
        routingRecords.forEach(r -> gatewayIds.add(r.getGatewayId()));
        
        List<EquipmentGatewayRecord> results = new ArrayList<>();
        if(gatewayIds.isEmpty()) {
        	return results;
        }
        
        ArrayList<Object> queryArgs = new ArrayList<>();

		//build the query	
		StringBuilder query = new StringBuilder();
		query.append(CQL_GET_ALL);

        //add gatewayIds filters
        {
            queryArgs.addAll(gatewayIds);

            StringBuilder strb = new StringBuilder(100);
            strb.append(" where id in (");
            for (int i = 0; i < gatewayIds.size(); i++) {
                strb.append("?");
                if (i < gatewayIds.size() - 1) {
                    strb.append(",");
                }
            }
            strb.append(") ");

            query.append(strb);
        }

        //TODO: create a cache of these prepared statements, keyed by the numberOfGatewayIds
        PreparedStatement preparedStmt_getGatewaysByIds;
        try {
        	preparedStmt_getGatewaysByIds = cqlSession.prepare(query.toString());
        } catch(InvalidQueryException e) {
        	LOG.error("Cannot prepare cassandra query '{}'", query.toString(), e);
        	throw e;
        }
        
		ResultSet rs = cqlSession.execute(preparedStmt_getGatewaysByIds.bind(queryArgs.toArray() ));

		rs.forEach( row -> results.add(gatewayRowMapper.mapRow(row)) );

        LOG.debug("getRegisteredGatewayRecordList({}) returns {} record(s)", equipmentId, results.size());
        return results;
	}


}
