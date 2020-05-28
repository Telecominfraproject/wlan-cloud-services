package com.telecominfraproject.wlan.alarm.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class AlarmDetails extends BaseJsonModel {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
    private String message;
	private List<Long> affectedEquipmentIds;
	private String generatedBy;
	private Map<String,Object> contextAttrs;
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public List<Long> getAffectedEquipmentIds() {
		return affectedEquipmentIds;
	}

	public void setAffectedEquipmentIds(List<Long> affectedEquipmentIds) {
		this.affectedEquipmentIds = affectedEquipmentIds;
	}

    public void addEquipmentId(long equipmentId) {
        if(this.affectedEquipmentIds == null){
            this.affectedEquipmentIds = new ArrayList<>();
        }
        
        this.affectedEquipmentIds.add(equipmentId);
    }


    public String getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(String generatedBy) {
		this.generatedBy = generatedBy;
	}



	public Map<String, Object> getContextAttrs() {
		return contextAttrs;
	}

	public void setContextAttrs(Map<String, Object> contextAttrs) {
		this.contextAttrs = contextAttrs;
	}

	@Override
    public AlarmDetails clone() {
        AlarmDetails ret = (AlarmDetails) super.clone();
        if(affectedEquipmentIds != null) {
        	ret.affectedEquipmentIds = new ArrayList<>();
        	for(Long d: affectedEquipmentIds) {
        		ret.affectedEquipmentIds.add(d);
        	}
        }
        if(contextAttrs != null) {
        	ret.contextAttrs = new HashMap<>();
			for(Entry<String, Object> attr: contextAttrs.entrySet()) {
				ret.contextAttrs.put(attr.getKey(),attr.getValue());
        	}
        }
        
        return ret;
    }

	@JsonIgnore
    public String getContextAttrAsString(String attrName) {
	    String ret = null;
	    if(contextAttrs != null) {
	        Object obj = contextAttrs.get(attrName);
	        if(obj != null) {
	            ret = obj.toString();
	        }
	    }
	    return ret;
    }

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(affectedEquipmentIds, contextAttrs, generatedBy, message);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AlarmDetails)) {
			return false;
		}
		AlarmDetails other = (AlarmDetails) obj;
		return Objects.equals(affectedEquipmentIds, other.affectedEquipmentIds)
				&& Objects.equals(contextAttrs, other.contextAttrs) && Objects.equals(generatedBy, other.generatedBy)
				&& Objects.equals(message, other.message);
	}

}
