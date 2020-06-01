package com.telecominfraproject.wlan.servicemetric.channelinfo.models;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;

public class ChannelInfoReports extends ServiceMetricDetails {
	
    private static final long serialVersionUID = 8835774986697535976L;
    private Map<RadioType, List<ChannelInfo>> channelInformationReportsPerRadio = new EnumMap<>(RadioType.class);


    public Map<RadioType, List<ChannelInfo>> getChannelInformationReportsPerRadio() {
		return channelInformationReportsPerRadio;
	}

	public void setChannelInformationReportsPerRadio(Map<RadioType, List<ChannelInfo>> channelInformationReportsPerRadio) {
		this.channelInformationReportsPerRadio = channelInformationReportsPerRadio;
	}

	@Override
    public ServiceMetricDataType getDataType() {
    	return ServiceMetricDataType.Channel;
    }
    
    @JsonIgnore
    public long getChannelInformationReportCount(RadioType radioType) {
    	List<ChannelInfo> reports = channelInformationReportsPerRadio.get(radioType);
        return (reports == null) ? 0 : reports.size();
    }


    @JsonIgnore
    public List<ChannelInfo> getRadioInfo(RadioType radioType)
    {
        return channelInformationReportsPerRadio.get(radioType);
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if(channelInformationReportsPerRadio!=null) {
	        for( 	List<ChannelInfo> reports : channelInformationReportsPerRadio.values()) {
		        if (hasUnsupportedValue(reports)) {
		            return true;
		        }
	        }
        }
        
        return false;
    }

    @Override
    public ChannelInfoReports clone() {
        ChannelInfoReports ret = (ChannelInfoReports) super.clone();
        
        if(this.channelInformationReportsPerRadio !=null) {
            ret.channelInformationReportsPerRadio = new EnumMap<>(RadioType.class);
            
            channelInformationReportsPerRadio.forEach((key, existingList) -> {
				List<ChannelInfo> newList = new ArrayList<>();
				existingList.forEach(m -> newList.add(m.clone()));
				ret.channelInformationReportsPerRadio.put(key, newList);
			});

        }

        return ret;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(channelInformationReportsPerRadio);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof ChannelInfoReports)) {
			return false;
		}
		ChannelInfoReports other = (ChannelInfoReports) obj;
		return Objects.equals(channelInformationReportsPerRadio, other.channelInformationReportsPerRadio);
	}
    
}
