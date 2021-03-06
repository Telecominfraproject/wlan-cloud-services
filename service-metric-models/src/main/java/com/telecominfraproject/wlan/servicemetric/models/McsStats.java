package com.telecominfraproject.wlan.servicemetric.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class McsStats extends BaseJsonModel
{
   
    private static final long serialVersionUID = 2366899901343104028L;

    /**
    *  The MCS number. This is table index.
    */
    private McsType mcsNum;

    /**
    *  The number of successfully transmitted frames at this rate. Do not count failed transmission.
    */
    private Integer txFrames;

    /**
    * The number of received frames at this rate.
    */
    private Integer rxFrames;
    
    private Long rate;
    
    
    public McsStats()
    {
        // for serialization
    }


    public McsType getMcsNum() {
        return mcsNum;
    }


    public void setMcsNum(McsType mcsNum) {
        this.mcsNum = mcsNum;
    }


    public Integer getTxFrames() {
        return txFrames;
    }


    public void setTxFrames(Integer txFrames) {
        this.txFrames = txFrames;
    }


    public Integer getRxFrames() {
        return rxFrames;
    }


    public void setRxFrames(Integer rxFrames) {
        this.rxFrames = rxFrames;
    }
   
    public Long getRate() {
        return rate;
    }


    public void setRate(Long rate) {
        this.rate = rate;
    }


    @Override
    public McsStats clone() {
        McsStats ret = (McsStats) super.clone();
        return ret;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (McsType.isUnsupported(mcsNum)) {
            return true;
        }
        return false;
    }
}
