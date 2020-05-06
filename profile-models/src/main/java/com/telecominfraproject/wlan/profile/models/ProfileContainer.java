package com.telecominfraproject.wlan.profile.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
