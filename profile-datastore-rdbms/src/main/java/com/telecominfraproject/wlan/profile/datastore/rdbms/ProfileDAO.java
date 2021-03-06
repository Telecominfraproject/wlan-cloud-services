package com.telecominfraproject.wlan.profile.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequest;

/**
 * @author dtoptygin
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class ProfileDAO extends BaseJdbcDao {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileDatastoreRdbms.class);
    
    private static final String COL_ID = "id";
    
    private static final String[] GENERATED_KEY_COLS = { COL_ID };
    
    private static final String[] ALL_COLUMNS_LIST = {        
        COL_ID,
        
        //TODO: add columns from properties Profile in here
        "customerId",
        "profileType",
        "name",
        "details",
        //make sure the order of properties matches this list and list in ProfileRowMapper and list in create/update methods
        
        "createdTimestamp",
        "lastModifiedTimestamp"
    };
    
    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));
    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(Arrays.asList(COL_ID, "createdTimestamp"));
    
    private static final String TABLE_NAME = "profile";
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
    

    private static final String SQL_GET_BY_ID =
        "select " + ALL_COLUMNS +
        " from "+TABLE_NAME+" " +
        " where " + COL_ID + " = ?";
    
    private static final String SQL_GET_BY_CUSTOMER_ID = 
    		"select " + ALL_COLUMNS +
    		" from " + TABLE_NAME + " " + 
    		" where customerId = ? ";
    
    private static final String SQL_WITH_PROFILETYPE_APPEND = 
    		" AND profileType = ?" ;
    
    private static final String SQL_WITH_NAME_SUBSTRING_APPEND = 
    		" AND LOWER(name) LIKE ? " ;

    private static final String SQL_GET_LASTMOD_BY_ID =
        "select lastModifiedTimestamp " +
        " from "+TABLE_NAME+" " +
        " where " + COL_ID + " = ?";

    private static final String SQL_INSERT =
        "insert into "+TABLE_NAME+" ( " 
        + ALL_COLUMNS_FOR_INSERT
        + " ) values ( "+BIND_VARS_FOR_INSERT+" ) ";

    private static final String SQL_DELETE =
        "delete from "+TABLE_NAME+" where " + COL_ID + " = ? ";

    private static final String SQL_UPDATE =
        "update "+TABLE_NAME+" set "
        + ALL_COLUMNS_UPDATE +
        " where " + COL_ID + " = ? "
        + " and ( lastModifiedTimestamp = ? or ? = true) " //last parameter will allow us to skip check for concurrent modification, if necessary
        ;

    private static final String SQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from "+TABLE_NAME + " where "+ COL_ID +" in ";

    private static final String SQL_PAGING_SUFFIX = " LIMIT ? OFFSET ? ";
    private static final String SORT_SUFFIX = "";

    private static final String SQL_GET_ALL_CHILDREN_IDS = "WITH RECURSIVE recursetree( childProfileId ) AS ("
            + " SELECT childProfileId FROM profile_map WHERE parentProfileId = ?"
            + " UNION "
            + " SELECT s.childProfileId " 
            + " FROM profile_map s JOIN recursetree rt ON rt.childProfileId = s.parentProfileId)"
            + " SELECT * FROM recursetree";

    private static final String SQL_GET_TOP_LEVEL_PARENT_IDS = "WITH RECURSIVE recursetree( childProfileId, parentProfileId ) AS ("
            + " SELECT childProfileId, parentProfileId FROM profile_map WHERE childProfileId = ?"
            + " UNION "
            + " SELECT s.childProfileId, s.parentProfileId"
            + " FROM profile_map s JOIN recursetree rt ON rt.parentProfileId = s.childProfileId)"
            + "   SELECT distinct(parentProfileId) FROM recursetree where parentProfileId not in (select childProfileId FROM recursetree) ";

    private static final RowMapper<Profile> profileRowMapper = new ProfileRowMapper();


    @Autowired(required=false)
    public void setDataSource(ProfileDataSourceInterface dataSource) {
        setDataSource((DataSource)dataSource);        
    }


    public Profile create(final Profile profile) {
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();

        try{
            jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(SQL_INSERT, keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS) );
                        int colIdx = 1;
                        
                        //TODO: add remaining properties from Profile here 
                        ps.setInt(colIdx++, profile.getCustomerId());
                        ps.setInt(colIdx++, profile.getProfileType().getId());
                        ps.setString(colIdx++, profile.getName());
                      	ps.setBytes(colIdx++, (profile.getDetails()!=null)?profile.getDetails().toZippedBytes():null);
                        
                        ps.setLong(colIdx++, ts);
                        ps.setLong(colIdx++, ts);
                        
                        return ps;
                    }
                },
                keyHolder);
        }catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        
        // keyHolder.getKey() now contains the generated key   
        profile.setId(((Long)keyHolder.getKeys().get(COL_ID)));
        profile.setCreatedTimestamp(ts);
        profile.setLastModifiedTimestamp(ts);

        if(!profile.getChildProfileIds().isEmpty()) {
        	updateChildProfileIds(profile);
        }

        LOG.debug("Stored Profile {}", profile);
        
        return profile.clone();
    }

    
    public Profile get(long profileId) {
        LOG.debug("Looking up Profile for id {}", profileId);

        try{
            Profile profile = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    profileRowMapper, profileId);
            
            //retrieve child profile ids
            profile.setChildProfileIds(getChildProfileIds(profile.getId()));

            LOG.debug("Found Profile {}", profile);
            
            return profile;
        }catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }
    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Profile getOrNull(long profileId) {
        LOG.debug("Looking up Profile for id {}", profileId);

        try{
            Profile profile = this.jdbcTemplate.queryForObject(
                    SQL_GET_BY_ID,
                    profileRowMapper, profileId);
            
            //retrieve child profile ids
            profile.setChildProfileIds(getChildProfileIds(profile.getId()));

            LOG.debug("Found Profile {}", profile);
            
            return profile;
        }catch (EmptyResultDataAccessException e) {
            LOG.debug("Could not find Profile for id {}", profileId);
            return null;
        }
    }

    public Profile update(Profile profile) {

        long newLastModifiedTs = System.currentTimeMillis();
        long incomingLastModifiedTs = profile.getLastModifiedTimestamp();
        int updateCount;
        
        try {
	        updateCount = this.jdbcTemplate.update(SQL_UPDATE, new Object[]{ 
	                //profile.getId(), - not updating this one
	
	                //TODO: add remaining properties from Profile here
	        		profile.getCustomerId(),
	        		profile.getProfileType().getId(),
	                profile.getName(),
	                (profile.getDetails()!=null)?profile.getDetails().toZippedBytes():null ,
	                                
	                //profile.getCreatedTimestamp(), - not updating this one
	                newLastModifiedTs,
	                
	                // use id for update operation
	                profile.getId(),
	                // use lastModifiedTimestamp for data protection against concurrent modifications
	                incomingLastModifiedTs,
	                isSkipCheckForConcurrentUpdates()
	        }); 
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }
        
        if(updateCount==0){
            
            try{
                
                if(isSkipCheckForConcurrentUpdates()){
                    //in this case we did not request protection against concurrent updates,
                    //so the updateCount is 0 because record in db was not found
                    throw new EmptyResultDataAccessException(1);
                }
                
                //find out if record could not be updated because it does not exist or because it was modified concurrently
                long recordTimestamp = this.jdbcTemplate.queryForObject(
                    SQL_GET_LASTMOD_BY_ID,
                    Long.class,
                    profile.getId()
                    );
                
                LOG.debug("Concurrent modification detected for Profile with id {} expected version is {} but version in db was {}", 
                        profile.getId(),
                        incomingLastModifiedTs,
                        recordTimestamp
                        );
                throw new DsConcurrentModificationException("Concurrent modification detected for Profile with id " + profile.getId()
                        +" expected version is " + incomingLastModifiedTs
                        +" but version in db was " + recordTimestamp
                        );
                
            }catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find Profile for {}", profile.getId());
                throw new DsEntityNotFoundException("Profile not found " + profile.getId());
            }
        }

        //make a copy so that we don't accidentally update caller's version by reference
        Profile profileCopy = profile.clone();
        profileCopy.setLastModifiedTimestamp(newLastModifiedTs);
        
        updateChildProfileIds(profileCopy);

        LOG.debug("Updated Profile {}", profileCopy);
        
        return profileCopy;
    }

    
    public Profile delete(long profileId) {
        Profile ret = get(profileId);
        
        this.jdbcTemplate.update(SQL_DELETE, profileId);
                
        LOG.debug("Deleted Profile {}", ret);
        
        return ret;
    }

    public List<Profile> getAllForCustomer(int customerId) {
        LOG.debug("Looking up Profiles for customer {}", customerId);

        List<Profile> ret = this.jdbcTemplate.query(SQL_GET_BY_CUSTOMER_ID,
                profileRowMapper, customerId);

        //retrieve child profile ids
        ret.forEach(p -> { p.setChildProfileIds(getChildProfileIds(p.getId()));} );

        LOG.debug("Found Profiles for customer {} : {}", customerId, ret);

        return ret;
    }

    public List<Profile> get(Set<Long> profileIdSet) {
        LOG.debug("calling get({})", profileIdSet);

        if (profileIdSet == null || profileIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder set = new StringBuilder(256);
        set.append("(");
        for(int i =0; i< profileIdSet.size(); i++) {
        		set.append("?,");
        }
        //remove last comma
        set.deleteCharAt(set.length()-1);
        set.append(")");
        
        String query = SQL_GET_ALL_IN_SET + set;
        List<Profile> results = this.jdbcTemplate.query(query, profileIdSet.toArray(), profileRowMapper);
        
        //retrieve child profile ids
        results.forEach(p -> { p.setChildProfileIds(getChildProfileIds(p.getId()));} );

        LOG.debug("get({}) returns {} record(s)", profileIdSet, results.size());
        return results;
    }


	public PaginationResponse<Profile> getForCustomer(ProfileByCustomerRequest profileByCustomerRequest) {

        PaginationResponse<Profile> ret = new PaginationResponse<>();
        ret.setContext(profileByCustomerRequest.getPaginationContext().clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Profiles for customer {} with last returned page number {}",
                    profileByCustomerRequest.getCustomerId(), profileByCustomerRequest.getPaginationContext().getLastReturnedPageNumber());
            return ret;
        }

        LOG.debug("Looking up Profiles for customer {} with last returned page number {}", 
        		profileByCustomerRequest.getCustomerId(), profileByCustomerRequest.getPaginationContext().getLastReturnedPageNumber());

        SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder();
        
        sqlQueryBuilder.addSqlWithArgument(SQL_GET_BY_CUSTOMER_ID, profileByCustomerRequest.getCustomerId());
        
        profileByCustomerRequest.getProfileType().ifPresent(profileType -> sqlQueryBuilder.addSqlWithArgument(SQL_WITH_PROFILETYPE_APPEND, profileType.getId()));
        profileByCustomerRequest.getNameSubstring().ifPresent(nameSubstring -> sqlQueryBuilder.addSqlWithArgument(SQL_WITH_NAME_SUBSTRING_APPEND, "%" + nameSubstring.toLowerCase() + "%"));

        // add sorting options for the query
        StringBuilder strbSort = new StringBuilder(100);
        strbSort.append(" order by ");

        if (profileByCustomerRequest.getSortBy().isPresent() && !profileByCustomerRequest.getSortBy().get().isEmpty()) {

            // use supplied sorting options
            for (ColumnAndSort column : profileByCustomerRequest.getSortBy().get()) {
                if (!ALL_COLUMNS_LOWERCASE.contains(column.getColumnName().toLowerCase())) {
                    // unknown column, skip it
                    continue;
                }

                strbSort.append(column.getColumnName());

                if (column.getSortOrder() == SortOrder.desc) {
                    strbSort.append(" desc");
                }

                strbSort.append(",");
            }

            // remove last ','
            strbSort.deleteCharAt(strbSort.length() - 1);

        } else {
            // no sort order was specified - sort by id to have consistent
            // paging
            strbSort.append(COL_ID);
        }

        sqlQueryBuilder.addSql(strbSort.toString());

        sqlQueryBuilder.addSql(SQL_PAGING_SUFFIX);
        sqlQueryBuilder.addArgument(profileByCustomerRequest.getPaginationContext().getMaxItemsPerPage());
        sqlQueryBuilder.addArgument(profileByCustomerRequest.getPaginationContext().getTotalItemsReturned());

        /*
         * https://www.citusdata.com/blog/2016/03/30/five-ways-to-paginate/
         * Choosing offset=1000 makes cost about 19 and has a 0.609 ms execution
         * time. Once offset=5,000,000 the cost goes up to 92734 and execution
         * time is 758.484 ms. - DT: still acceptable for our use case
         */
        List<Profile> pageItems = this.jdbcTemplate.query(sqlQueryBuilder.getSql(), sqlQueryBuilder.getQueryArgs().toArray(),
                profileRowMapper);

        //retrieve child profile ids
    	pageItems.forEach(p -> { p.setChildProfileIds(getChildProfileIds(p.getId()));} );

        LOG.debug("Found {} Profiles for customer {} with last returned page number {}",
                pageItems.size(), profileByCustomerRequest.getCustomerId(), profileByCustomerRequest.getPaginationContext().getLastReturnedPageNumber());

        ret.setItems(pageItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        // startAfterItem is not used in RDBMS datastores, set it to null
        ret.getContext().setStartAfterItem(null);

        return ret;
    }
	
	private Set<Long> getChildProfileIds(long profileId){
        LOG.debug("Looking up child profiles for Profile {}", profileId);

        Set<Long> ret = new HashSet<>();
        
        List<Long> idList = this.jdbcTemplate.queryForList(
                "select childProfileId from profile_map where parentProfileId = ?", Long.class, 
                profileId);
        
        LOG.debug("Found {} child profiles for Profile {}", idList.size(), profileId);
        
        if(!idList.isEmpty()) {
        	ret.addAll(idList);
        }
        
        return ret;
	}
    
    private void updateChildProfileIds(Profile profile){
        LOG.debug("Updating child profiles for Profile {}", profile.getId());

        //remove old ids
        this.jdbcTemplate.update(
                "delete from profile_map where parentProfileId = ?",  
                profile.getId());
        
        //insert new ids in a batch
        if(!profile.getChildProfileIds().isEmpty()) {
	        List<Object[]> batchArgs = new ArrayList<Object[]>();
	        profile.getChildProfileIds().forEach(cpId -> { batchArgs.add( new Object[] {profile.getCustomerId(), profile.getId(), cpId} ); });
	        
			this.jdbcTemplate.batchUpdate(
	                "insert into profile_map (customerId, parentProfileId, childProfileId) values (?, ?, ? )", batchArgs );
        }
        
        LOG.debug("Updated child profiles for Profile {}", profile.getId());        
            	        
    }


	public List<Profile> getProfileWithChildren(long profileId) {
        List<Long> profileIds = this.jdbcTemplate.queryForList(SQL_GET_ALL_CHILDREN_IDS,
                Long.class, profileId);
        
        profileIds.add(profileId);
        
        List<Profile> ret = new ArrayList<Profile>();
        if(!profileIds.isEmpty()) {
        	ret.addAll(get(new HashSet<>(profileIds)));
        }
        
		return ret;
	}


	public List<PairLongLong> getTopLevelProfiles(Set<Long> profileIds) {
        LOG.debug("Looking up top-level profiles for {}", profileIds);
        Set<PairLongLong> ret = new HashSet<>();
        
        if(profileIds == null) {
        	return Collections.emptyList();
        }

    	profileIds.forEach(profileId -> {
            List<Long> parentIds = this.jdbcTemplate.queryForList(SQL_GET_TOP_LEVEL_PARENT_IDS, Long.class, profileId);
            if(parentIds.isEmpty()) {
            	//this profile is a top-level one
            	ret.add(new PairLongLong(profileId, profileId));
            } else {
            	parentIds.forEach(parentId -> ret.add(new PairLongLong(profileId,parentId)));
            }
    	});

        LOG.debug("Found top-level profiles {} for {}", profileIds, ret);

        return new ArrayList<>(ret);
    }
    
}
