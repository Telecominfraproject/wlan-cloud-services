package com.telecominfraproject.wlan.status.network.models;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.models.StatusCode;


/**
 * @author ekeddy
 *
 */
public class RadiusDetails extends BaseJsonModel {

	private static final long serialVersionUID = -8484094473328408318L;
    private StatusCode status;
	private List<RadiusServerDetails> radiusServerDetails;

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }
    
	public List<RadiusServerDetails> getRadiusServerDetails() {
		return radiusServerDetails;
	}

	public void setRadiusServerDetails(List<RadiusServerDetails> radiusServerDetails) {
		this.radiusServerDetails = radiusServerDetails;
	}

	@Override
	public RadiusDetails clone() {
		RadiusDetails ret = (RadiusDetails) super.clone();
        
        if(radiusServerDetails!=null && !radiusServerDetails.isEmpty()){
            ret.radiusServerDetails = new ArrayList<>(radiusServerDetails.size());
            
            for(RadiusServerDetails rsd: radiusServerDetails){
                ret.radiusServerDetails.add(rsd.clone());
            }
        }
		return ret;
	}

	public RadiusDetails combineWith(RadiusDetails other) {
		if(other == null) {
			return this;
		}
		
        if(this.status == null){
            this.status = other.status;
        }
        
		if(this.radiusServerDetails == null || this.radiusServerDetails.isEmpty()) {
			this.radiusServerDetails = other.radiusServerDetails;
		}
		return this;

	}

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (StatusCode.isUnsupported(status) || hasUnsupportedValue(radiusServerDetails)) {
            return true;
        }
        return false;
    }

}
