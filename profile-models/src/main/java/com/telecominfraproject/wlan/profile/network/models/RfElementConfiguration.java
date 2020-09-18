package com.telecominfraproject.wlan.profile.network.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.ActiveScanSettings;
import com.telecominfraproject.wlan.equipment.models.MimoMode;
import com.telecominfraproject.wlan.equipment.models.MulticastRate;
import com.telecominfraproject.wlan.equipment.models.StateSetting;

public class RfElementConfiguration extends BaseJsonModel {

	private static final long serialVersionUID = 1071268246965238451L;
	
    private static final Logger LOG = LoggerFactory.getLogger(RfElementConfiguration.class);
    
    // Parameters for each RadioType
    private String rf;
	private Integer beaconInterval; // keep this in sync with fields in RadioCfg (in protobuf)
    private StateSetting forceScanDuringVoice;
    private Integer rtsCtsThreshold;
    private ChannelBandwidth channelBandwidth;
    private MimoMode mimoMode;
    private Integer maxNumClients;
    private MulticastRate multicastRate;
    private boolean autoChannelSelection;
    private ActiveScanSettings activeScanSettings;
    
    private RfElementConfiguration() {
    	long timestamp = System.currentTimeMillis();
    	setRf("DefaultRf-" + timestamp);
    	setBeaconInterval(100);
    	setForceScanDuringVoice(StateSetting.disabled);
    	setRtsCtsThreshold(65535);
    	setMimoMode(MimoMode.twoByTwo);
    	setMaxNumClients(100);
    	setMulticastRate(MulticastRate.auto);
    	setAutoChannelSelection(false);
    	setActiveScanSettings(ActiveScanSettings.createWithDefaults());
    }
    
    public static RfElementConfiguration createWithDefaults(RadioType radioType) {
        RfElementConfiguration ret = new RfElementConfiguration();  
        if (radioType == RadioType.is5GHz || radioType == RadioType.is5GHzL || radioType == RadioType.is5GHzU) {
    		ret.setChannelBandwidth(ChannelBandwidth.is80MHz);
        } else {
        	ret.setChannelBandwidth(ChannelBandwidth.is20MHz);
        }
    	return ret;
    }
    
    public String getRf() {
		return rf;
	}

	public void setRf(String rf) {
		this.rf = rf;
	}

	public Integer getBeaconInterval() {
		return beaconInterval;
	}

	public void setBeaconInterval(Integer beaconInterval) {
		this.beaconInterval = beaconInterval;
	}

	public StateSetting getForceScanDuringVoice() {
		return forceScanDuringVoice;
	}

	public void setForceScanDuringVoice(StateSetting forceScanDuringVoice) {
		this.forceScanDuringVoice = forceScanDuringVoice;
	}

	public Integer getRtsCtsThreshold() {
		return rtsCtsThreshold;
	}

	public void setRtsCtsThreshold(Integer rtsCtsThreshold) {
		this.rtsCtsThreshold = rtsCtsThreshold;
	}

	public ChannelBandwidth getChannelBandwidth() {
		return channelBandwidth;
	}

	public void setChannelBandwidth(ChannelBandwidth channelBandwidth) {
		this.channelBandwidth = channelBandwidth;
	}

	public MimoMode getMimoMode() {
		return mimoMode;
	}

	public void setMimoMode(MimoMode mimoMode) {
		this.mimoMode = mimoMode;
	}

	public Integer getMaxNumClients() {
		return maxNumClients;
	}

	public void setMaxNumClients(Integer maxNumClients) {
		this.maxNumClients = maxNumClients;
	}

	public MulticastRate getMulticastRate() {
		return multicastRate;
	}

	public void setMulticastRate(MulticastRate multicastRate) {
		this.multicastRate = multicastRate;
	}

	public boolean getAutoChannelSelection() {
		return autoChannelSelection;
	}

	public void setAutoChannelSelection(boolean autoChannelSelection) {
		this.autoChannelSelection = autoChannelSelection;
	}

	public ActiveScanSettings getActiveScanSettings() {
		return activeScanSettings;
	}

	public void setActiveScanSettings(ActiveScanSettings activeScanSettings) {
		this.activeScanSettings = activeScanSettings;
	}

	@Override
    public RfElementConfiguration clone() {
        return (RfElementConfiguration) super.clone();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activeScanSettings == null) ? 0 : activeScanSettings.hashCode());
		result = prime * result + (autoChannelSelection ? 1231 : 1237);
		result = prime * result + ((beaconInterval == null) ? 0 : beaconInterval.hashCode());
		result = prime * result + ((channelBandwidth == null) ? 0 : channelBandwidth.hashCode());
		result = prime * result + ((forceScanDuringVoice == null) ? 0 : forceScanDuringVoice.hashCode());
		result = prime * result + ((maxNumClients == null) ? 0 : maxNumClients.hashCode());
		result = prime * result + ((mimoMode == null) ? 0 : mimoMode.hashCode());
		result = prime * result + ((multicastRate == null) ? 0 : multicastRate.hashCode());
		result = prime * result + ((rf == null) ? 0 : rf.hashCode());
		result = prime * result + ((rtsCtsThreshold == null) ? 0 : rtsCtsThreshold.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RfElementConfiguration other = (RfElementConfiguration) obj;
		if (activeScanSettings == null) {
			if (other.activeScanSettings != null)
				return false;
		} else if (!activeScanSettings.equals(other.activeScanSettings))
			return false;
		if (autoChannelSelection != other.autoChannelSelection)
			return false;
		if (beaconInterval == null) {
			if (other.beaconInterval != null)
				return false;
		} else if (!beaconInterval.equals(other.beaconInterval))
			return false;
		if (channelBandwidth == null) {
			if (other.channelBandwidth != null)
				return false;
		} else if (!channelBandwidth.equals(other.channelBandwidth))
			return false;
		if (forceScanDuringVoice != other.forceScanDuringVoice)
			return false;
		if (maxNumClients == null) {
			if (other.maxNumClients != null)
				return false;
		} else if (!maxNumClients.equals(other.maxNumClients))
			return false;
		if (mimoMode != other.mimoMode)
			return false;
		if (multicastRate != other.multicastRate)
			return false;
		if (rf == null) {
			if (other.rf != null)
				return false;
		} else if (!rf.equals(other.rf))
			return false;
		if (rtsCtsThreshold == null) {
			if (other.rtsCtsThreshold != null)
				return false;
		} else if (!rtsCtsThreshold.equals(other.rtsCtsThreshold))
			return false;
		return true;
	}
	
	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}
		if (StateSetting.isUnsupported(forceScanDuringVoice) || ChannelBandwidth.isUnsupported(channelBandwidth)
				|| MimoMode.isUnsupported(mimoMode) || MulticastRate.isUnsupported(multicastRate)
				|| ActiveScanSettings.hasUnsupportedValue(activeScanSettings)) {
			return true;
		}
		return false;
	}

}
