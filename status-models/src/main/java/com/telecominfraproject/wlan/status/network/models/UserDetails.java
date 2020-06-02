package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class UserDetails extends BaseJsonModel {

    private static final long serialVersionUID = 3034727832551606882L;
    
    // Min/Max/Avg number of users per AP
    private MinMaxAvgValueInt totalUsers;

    private Map<RadioType, MinMaxAvgValueInt> usersPerRadio = new EnumMap<>(RadioType.class);

    private int numGoodEquipment;
    private int numWarnEquipment;
    private int numBadEquipment;

    private Map<String, Integer> userDevicePerManufacturerCounts = new HashMap<>();

    private int totalApsReported;
    private int indicatorValue;
    
    private Map<RadioType, Integer> indicatorValuePerRadio = new EnumMap<>(RadioType.class);
    
    // Bins of user counts with good/average/bad link quality
    private Map<RadioType, LinkQualityAggregatedStats> linkQualityPerRadio = new EnumMap<>(RadioType.class);

    // Bins of user counts with low/medium/high activity
    private Map<RadioType, ClientActivityAggregatedStats> clientActivityPerRadio = new EnumMap<>(RadioType.class);
    
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public UserDetails combineWith(UserDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.indicatorValue==0){
            this.indicatorValue = other.indicatorValue;
        }

        if(indicatorValuePerRadio!=null) {
        	if(other.indicatorValuePerRadio!=null) {
	        	for(Map.Entry<RadioType, Integer> entry : other.indicatorValuePerRadio.entrySet()) {
	        		Integer existingEntry =indicatorValuePerRadio.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingEntry!=null && existingEntry==0) {
	        			indicatorValuePerRadio.put(entry.getKey(), entry.getValue());
	        		}
	        	}
        	}
        } else {
        	if(other.indicatorValuePerRadio!=null) {
        		this.indicatorValuePerRadio = new HashMap<>(other.indicatorValuePerRadio);
        	}
        }

        
        if(linkQualityPerRadio!=null) {
        	if(other.linkQualityPerRadio!=null) {
	        	for(Map.Entry<RadioType, LinkQualityAggregatedStats> entry : other.linkQualityPerRadio.entrySet()) {
	        		LinkQualityAggregatedStats existingDetails =linkQualityPerRadio.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingDetails!=null) {
	        			linkQualityPerRadio.get(entry.getKey()).combineWith(existingDetails);
	        		}
	        	}
        	}
        } else {
        	if(other.linkQualityPerRadio!=null) {
        		this.linkQualityPerRadio = new EnumMap<>(other.linkQualityPerRadio);
        	}
        }

        if(clientActivityPerRadio!=null) {
        	if(other.clientActivityPerRadio!=null) {
	        	for(Map.Entry<RadioType, ClientActivityAggregatedStats> entry : other.clientActivityPerRadio.entrySet()) {
	        		ClientActivityAggregatedStats existingDetails =clientActivityPerRadio.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingDetails!=null) {
	        			clientActivityPerRadio.get(entry.getKey()).combineWith(existingDetails);
	        		}
	        	}
        	}
        } else {
        	if(other.clientActivityPerRadio!=null) {
        		this.clientActivityPerRadio = new EnumMap<>(other.clientActivityPerRadio);
        	}
        }

        if(usersPerRadio!=null) {
        	if(other.usersPerRadio!=null) {
	        	for(Map.Entry<RadioType, MinMaxAvgValueInt> entry : other.usersPerRadio.entrySet()) {
	        		MinMaxAvgValueInt existingDetails =usersPerRadio.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingDetails!=null) {
	        			usersPerRadio.get(entry.getKey()).combineWith(existingDetails);
	        		}
	        	}
        	}
        } else {
        	if(other.usersPerRadio!=null) {
        		this.usersPerRadio = new EnumMap<>(other.usersPerRadio);
        	}
        }

        if(totalUsers!= null) {
        	totalUsers.combineWith(other.totalUsers);
        } else {
        	totalUsers = other.totalUsers;
        }
        
        if(this.totalApsReported == 0){
            this.totalApsReported = other.totalApsReported;
        }

        if(this.userDevicePerManufacturerCounts==null || this.userDevicePerManufacturerCounts.isEmpty()){
            this.userDevicePerManufacturerCounts = other.userDevicePerManufacturerCounts;
        }


        return this;
    }

    @Override
    public UserDetails clone() {
    	UserDetails ret = (UserDetails) super.clone();
    	
    	if(clientActivityPerRadio!=null) {
    		ret.clientActivityPerRadio = new EnumMap<>(RadioType.class);
    		clientActivityPerRadio.forEach((k, v) -> ret.clientActivityPerRadio.put(k, v.clone()));
    	}
    	
    	if(indicatorValuePerRadio!=null) {
    		ret.indicatorValuePerRadio = new EnumMap<>(RadioType.class);
    		indicatorValuePerRadio.forEach((k, v) -> ret.indicatorValuePerRadio.put(k, v));
    	}

    	if(linkQualityPerRadio!=null) {
    		ret.linkQualityPerRadio = new EnumMap<>(RadioType.class);
    		linkQualityPerRadio.forEach((k, v) -> ret.linkQualityPerRadio.put(k, v.clone()));
    	}
    	    	
    	if(usersPerRadio!=null) {
    		ret.usersPerRadio = new EnumMap<>(RadioType.class);
    		usersPerRadio.forEach((k, v) -> ret.usersPerRadio.put(k, v.clone()));
    	}

    	if(userDevicePerManufacturerCounts!=null) {
    		ret.userDevicePerManufacturerCounts = new HashMap<>();
    		userDevicePerManufacturerCounts.forEach((k, v) -> ret.userDevicePerManufacturerCounts.put(k, v));
    	}

    	if(totalUsers!=null) {
    		ret.totalUsers = totalUsers.clone();
    	}
    		
    	return ret;    	
    }

	public MinMaxAvgValueInt getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(MinMaxAvgValueInt totalUsers) {
		this.totalUsers = totalUsers;
	}

	public Map<RadioType, MinMaxAvgValueInt> getUsersPerRadio() {
		return usersPerRadio;
	}

	public void setUsersPerRadio(Map<RadioType, MinMaxAvgValueInt> usersPerRadio) {
		this.usersPerRadio = usersPerRadio;
	}

	public int getNumGoodEquipment() {
		return numGoodEquipment;
	}

	public void setNumGoodEquipment(int numGoodEquipment) {
		this.numGoodEquipment = numGoodEquipment;
	}

	public int getNumWarnEquipment() {
		return numWarnEquipment;
	}

	public void setNumWarnEquipment(int numWarnEquipment) {
		this.numWarnEquipment = numWarnEquipment;
	}

	public int getNumBadEquipment() {
		return numBadEquipment;
	}

	public void setNumBadEquipment(int numBadEquipment) {
		this.numBadEquipment = numBadEquipment;
	}

	public Map<String, Integer> getUserDevicePerManufacturerCounts() {
		return userDevicePerManufacturerCounts;
	}

	public void setUserDevicePerManufacturerCounts(Map<String, Integer> userDevicePerManufacturerCounts) {
		this.userDevicePerManufacturerCounts = userDevicePerManufacturerCounts;
	}

	public int getTotalApsReported() {
		return totalApsReported;
	}

	public void setTotalApsReported(int totalApsReported) {
		this.totalApsReported = totalApsReported;
	}

	public int getIndicatorValue() {
		return indicatorValue;
	}

	public void setIndicatorValue(int indicatorValue) {
		this.indicatorValue = indicatorValue;
	}

	public Map<RadioType, Integer> getIndicatorValuePerRadio() {
		return indicatorValuePerRadio;
	}

	public void setIndicatorValuePerRadio(Map<RadioType, Integer> indicatorValuePerRadio) {
		this.indicatorValuePerRadio = indicatorValuePerRadio;
	}

	public Map<RadioType, LinkQualityAggregatedStats> getLinkQualityPerRadio() {
		return linkQualityPerRadio;
	}

	public void setLinkQualityPerRadio(Map<RadioType, LinkQualityAggregatedStats> linkQualityPerRadio) {
		this.linkQualityPerRadio = linkQualityPerRadio;
	}

	public Map<RadioType, ClientActivityAggregatedStats> getClientActivityPerRadio() {
		return clientActivityPerRadio;
	}

	public void setClientActivityPerRadio(Map<RadioType, ClientActivityAggregatedStats> clientActivityPerRadio) {
		this.clientActivityPerRadio = clientActivityPerRadio;
	}

    

}
