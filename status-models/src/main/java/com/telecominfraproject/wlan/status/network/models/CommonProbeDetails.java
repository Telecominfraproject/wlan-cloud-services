package com.telecominfraproject.wlan.status.network.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.models.StatusCode;

/**
 * @author dtop
 *
 */
public class CommonProbeDetails extends BaseJsonModel {

    private static final long serialVersionUID = -9029108689918137602L;
    
    private MinMaxAvgValueInt latencyMs; 
    private int numSuccessProbeRequests;
    private int numFailedProbeRequests;
    private StatusCode status;
    
    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }

    public MinMaxAvgValueInt getLatencyMs() {
		return latencyMs;
	}

	public void setLatencyMs(MinMaxAvgValueInt latencyMs) {
		this.latencyMs = latencyMs;
	}

	public int getNumSuccessProbeRequests() {
        return numSuccessProbeRequests;
    }

    public void setNumSuccessProbeRequests(int numSuccessProbeRequests) {
        this.numSuccessProbeRequests = numSuccessProbeRequests;
    }

    public int getNumFailedProbeRequests() {
        return numFailedProbeRequests;
    }

    public void setNumFailedProbeRequests(int numFailedProbeRequests) {
        this.numFailedProbeRequests = numFailedProbeRequests;
    }

    @Override
    public CommonProbeDetails clone() {
        CommonProbeDetails ret = (CommonProbeDetails) super.clone();

        if(latencyMs!=null){
            ret.latencyMs = latencyMs.clone();
        } 

        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public CommonProbeDetails combineWith(CommonProbeDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.latencyMs!=null){
            this.latencyMs.combineWith(other.latencyMs);
        } else {
        	this.latencyMs = other.latencyMs;
        }
        
        if(this.numSuccessProbeRequests==0){
            this.numSuccessProbeRequests = other.numSuccessProbeRequests;
        }

        if(this.numFailedProbeRequests==0){
            this.numFailedProbeRequests = other.numFailedProbeRequests;
        }

        if(this.status == null){
            this.status = other.status;
        }
        
        return this;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StatusCode.isUnsupported(status)) {
            return true;
        }
        return false;
    }

}
