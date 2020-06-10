package com.telecominfraproject.wlan.profile;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.profile.models.Profile;


/**
 * @author dtoptygin
 *
 */
public interface ProfileServiceInterface {
    
    /**
     * Creates new Profile
     *  
     * @param Profile
     * @return stored Profile object
     * @throws RuntimeException if Profile record already exists
     */
    Profile create(Profile profile );
    
    /**
     * Retrieves Profile by id
     * @param profileId
     * @return Profile for the supplied id
     * @throws RuntimeException if Profile record does not exist
     */
    Profile get(long profileId );

    /**
     * Retrieves Profile by id
     * @param profileId
     * @return Profile for the supplied id
     */
    Profile getOrNull(long profileId );

    /**
     * Retrieves a list of Profile records that which have their Id in the provided set.
     * 
     * @param profileIdSet
     * @return list of matching Profile objects.
     */
    List<Profile> get(Set<Long> profileIdSet);

    /**
     * Updates Profile 
     * 
     * @param Profile
     * @return updated Profile object
     * @throws RuntimeException if Profile record does not exist or if it was modified concurrently
     */
    Profile update(Profile profile);
    
    /**
     * Deletes Profile
     * 
     * @param profileId
     * @return deleted Profile object
     */
    Profile delete(long profileId );
    
    /**
     * <br>Retrieves all of the Profile records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "id"
	 *<li> "sampleStr"
     *<br> 
     * @param customerId
     * @return next page of matching Profile objects.
     */
    PaginationResponse<Profile> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Profile> context);

    /**
	 * @param profileId
	 * @return list of profiles that includes the one with supplied profile id, as
	 *         well as all other profiles that it refers to directly or indirectly
	 *         via the child relationships.
	 */
    List<Profile> getProfileWithChildren(long profileId);

    /**
     * Find top-level parent profiles for the specified set of profile ids.
     * 
     * @param profileIds
     * @return list of pairs (profileId, parentProfileId)
     */
    List<PairLongLong> getTopLevelProfiles(Set<Long> profileIds);
    
}
