package com.telecominfraproject.wlan.profile.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telecominfraproject.wlan.profile.ethernetport.models.WiredEthernetPortConfiguration;

/**
 * @author dtop
 * Utility class for dealing with profiles
 */
public class ProfileContainer {
	
	private Map<Long, Profile> profileMap = new HashMap<>();
	
	public ProfileContainer(List<Profile> profiles) {
		if(profiles!=null) {
			profiles.forEach( p -> profileMap.put(p.getId(), p) );
		}
	}
	
	public Profile getOrNull(long profileId) {
		return profileMap.get(profileId);
	}

	public List<Profile> getForType(ProfileType profileType){
		List<Profile> ret = new ArrayList<>();
		profileMap.values().forEach(p -> { if(p.getProfileType() == profileType) { ret.add(p); } });		
		return ret;
	}
	
	public Profile getChildOfTypeOrNull(long profileId, ProfileType childProfileType) {
		List<Profile> ret = getChildrenOfType(profileId, childProfileType);
		
		if(ret.isEmpty()) {
			return null;
		}
		
		//return first child that matches the profile type
		return ret.get(0);
	}

	public Profile getChildOfTypeOrNullByEquipmentModel(long profileId, ProfileType childProfileType,
														String equipmentModel) {
		// The profile type of the profileId should be equipment_ap
		Profile apProfile = profileMap.get(profileId);
		if (apProfile != null && apProfile.getProfileType() == ProfileType.equipment_ap) {

			List<Profile> profiles = getChildrenOfType(profileId, childProfileType);
			for (Profile ret : profiles) {
				WiredEthernetPortConfiguration config = (WiredEthernetPortConfiguration) ret.getDetails();
				if (config != null && config.getEquipmentModel() != null
						&& config.getEquipmentModel().equals(equipmentModel)) {
					return ret;
				}
			}
		} else {
			throw new IllegalArgumentException("Profile Id " + profileId + " is not of type equipment_ap");
		}
		return null;
	}

	public List<Profile> getChildrenOfType(long profileId, ProfileType childProfileType) {
		Profile parent = profileMap.get(profileId);
		List<Profile> ret = new ArrayList<>();	
		
		if(parent == null || parent.getChildProfileIds()==null || parent.getChildProfileIds().isEmpty()) {
			return ret;
		}
		
		parent.getChildProfileIds().forEach(pId -> { Profile p = profileMap.get(pId); if(p!=null && p.getProfileType() == childProfileType) { ret.add(p); } });
		
		return ret;
	}

}
