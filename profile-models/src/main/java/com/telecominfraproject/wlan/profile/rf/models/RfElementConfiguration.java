package com.telecominfraproject.wlan.profile.rf.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualValue;
import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.ChannelHopSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.ActiveScanSettings;
import com.telecominfraproject.wlan.equipment.models.ManagementRate;
import com.telecominfraproject.wlan.equipment.models.MimoMode;
import com.telecominfraproject.wlan.equipment.models.MulticastRate;
import com.telecominfraproject.wlan.equipment.models.NeighbouringAPListConfiguration;
import com.telecominfraproject.wlan.equipment.models.StateSetting;

public class RfElementConfiguration extends BaseJsonModel {

	private static final long serialVersionUID = 1071268246965238451L;
	
    private static final Logger LOG = LoggerFactory.getLogger(RfElementConfiguration.class);
    
    public final static int MIN_BG_RADIO_CELL_SIZE = -80;
	public final static int MIN_AC_RADIO_CELL_SIZE = -80;
	public final static int DEFAULT_RX_CELL_SIZE_DB = -90;
	public final static int DEFAULT_EIRP_TX_POWER = 18;
	public final static int DEFAULT_BEACON_INTERVAL = 100;
	
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
//    
//    private ManagementRate managementRate;
//    private AutoOrManualValue rxCellSizeDb;
//	private AutoOrManualValue probeResponseThresholdDb;
//	private AutoOrManualValue clientDisconnectThresholdDb;
//	private AutoOrManualValue eirpTxPower;
//	private Boolean bestApEnabled;
//	private NeighbouringAPListConfiguration neighbouringListApConfig;
//	private Integer minAutoCellSize;
//	private Boolean perimeterDetectionEnabled;
//    private ChannelHopSettings channelHopSettings;
//    private RadioBestApSettings bestApSettings;
    
    private RfElementConfiguration() {
    	long timestamp = System.currentTimeMillis();
    	setRf("DefaultRf-" + timestamp);
    	setBeaconInterval(DEFAULT_BEACON_INTERVAL);
    	setForceScanDuringVoice(StateSetting.disabled);
    	setRtsCtsThreshold(65535);
    	setMimoMode(MimoMode.twoByTwo);
    	setMaxNumClients(100);
    	setMulticastRate(MulticastRate.auto);
    	setAutoChannelSelection(false);
    	setActiveScanSettings(ActiveScanSettings.createWithDefaults());
//    	setManagementRate(ManagementRate.auto);
//    	setRxCellSizeDb(AutoOrManualValue.createAutomaticInstance(DEFAULT_RX_CELL_SIZE_DB));
//    	setProbeResponseThresholdDb(AutoOrManualValue.createAutomaticInstance(-90));
//    	setClientDisconnectThresholdDb(AutoOrManualValue.createAutomaticInstance(-90));
//    	setEirpTxPower(AutoOrManualValue.createAutomaticInstance(DEFAULT_EIRP_TX_POWER));
//    	setBestApEnabled(null);
//    	setNeighbouringListApConfig(NeighbouringAPListConfiguration.createDefault());
//    	setPerimeterDetectionEnabled(true);
//    	setChannelHopSettings(ChannelHopSettings.createWithDefaults());
    }
    
    public static RfElementConfiguration createWithDefaults(RadioType radioType) {
        RfElementConfiguration ret = new RfElementConfiguration();  
//        ret.setBestApSettings(RadioBestApSettings.createWithDefaults(radioType));
        if (radioType == RadioType.is5GHz || radioType == RadioType.is5GHzL || radioType == RadioType.is5GHzU) {
    		ret.setChannelBandwidth(ChannelBandwidth.is80MHz);
//    		ret.setMinAutoCellSize(MIN_AC_RADIO_CELL_SIZE);
        } else {
        	ret.setChannelBandwidth(ChannelBandwidth.is20MHz);
//        	ret.setMinAutoCellSize(MIN_BG_RADIO_CELL_SIZE);
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

//	public ManagementRate getManagementRate() {
//		return managementRate;
//	}
//
//	public void setManagementRate(ManagementRate managementRate) {
//		this.managementRate = managementRate;
//	}
//
//	public AutoOrManualValue getRxCellSizeDb() {
//		return rxCellSizeDb;
//	}
//
//	public void setRxCellSizeDb(AutoOrManualValue rxCellSizeDb) {
//		this.rxCellSizeDb = rxCellSizeDb;
//	}
//
//	public AutoOrManualValue getProbeResponseThresholdDb() {
//		return probeResponseThresholdDb;
//	}
//
//	public void setProbeResponseThresholdDb(AutoOrManualValue probeResponseThresholdDb) {
//		this.probeResponseThresholdDb = probeResponseThresholdDb;
//	}
//
//	public AutoOrManualValue getClientDisconnectThresholdDb() {
//		return clientDisconnectThresholdDb;
//	}
//
//	public void setClientDisconnectThresholdDb(AutoOrManualValue clientDisconnectThresholdDb) {
//		this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
//	}
//
//	public AutoOrManualValue getEirpTxPower() {
//		return eirpTxPower;
//	}
//
//	public void setEirpTxPower(AutoOrManualValue eirpTxPower) {
//		this.eirpTxPower = eirpTxPower;
//	}
//
//	public Boolean getBestApEnabled() {
//		return bestApEnabled;
//	}
//
//	public void setBestApEnabled(Boolean bestApEnabled) {
//		this.bestApEnabled = bestApEnabled;
//	}
//
//	public NeighbouringAPListConfiguration getNeighbouringListApConfig() {
//		return neighbouringListApConfig;
//	}
//
//	public void setNeighbouringListApConfig(NeighbouringAPListConfiguration neighbouringListApConfig) {
//		this.neighbouringListApConfig = neighbouringListApConfig;
//	}
//
//	public int getMinAutoCellSize(RadioType radioType) {
//		if (minAutoCellSize == null) {
//			return radioType == RadioType.is2dot4GHz ? MIN_BG_RADIO_CELL_SIZE : MIN_AC_RADIO_CELL_SIZE;
//		}
//
//		return minAutoCellSize;
//	}
//
//	public void setMinAutoCellSize(Integer minAutoCellSize) {
//		this.minAutoCellSize = minAutoCellSize;
//	}
//
//	public Boolean getPerimeterDetectionEnabled() {
//		return perimeterDetectionEnabled;
//	}
//
//	public void setPerimeterDetectionEnabled(Boolean perimeterDetectionEnabled) {
//		this.perimeterDetectionEnabled = perimeterDetectionEnabled;
//	}
//
//	public ChannelHopSettings getChannelHopSettings() {
//		return channelHopSettings;
//	}
//
//	public void setChannelHopSettings(ChannelHopSettings channelHopSettings) {
//		this.channelHopSettings = channelHopSettings;
//	}
//
//	public RadioBestApSettings getBestApSettings() {
//		return bestApSettings;
//	}
//
//	public void setBestApSettings(RadioBestApSettings bestApSettings) {
//		this.bestApSettings = bestApSettings;
//	}

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
//		result = prime * result + ((bestApEnabled == null) ? 0 : bestApEnabled.hashCode());
//		result = prime * result + ((bestApSettings == null) ? 0 : bestApSettings.hashCode());
		result = prime * result + ((channelBandwidth == null) ? 0 : channelBandwidth.hashCode());
//		result = prime * result + ((channelHopSettings == null) ? 0 : channelHopSettings.hashCode());
//		result = prime * result + ((clientDisconnectThresholdDb == null) ? 0 : clientDisconnectThresholdDb.hashCode());
//		result = prime * result + ((eirpTxPower == null) ? 0 : eirpTxPower.hashCode());
		result = prime * result + ((forceScanDuringVoice == null) ? 0 : forceScanDuringVoice.hashCode());
//		result = prime * result + ((managementRate == null) ? 0 : managementRate.hashCode());
		result = prime * result + ((maxNumClients == null) ? 0 : maxNumClients.hashCode());
		result = prime * result + ((mimoMode == null) ? 0 : mimoMode.hashCode());
//		result = prime * result + ((minAutoCellSize == null) ? 0 : minAutoCellSize.hashCode());
		result = prime * result + ((multicastRate == null) ? 0 : multicastRate.hashCode());
//		result = prime * result + ((neighbouringListApConfig == null) ? 0 : neighbouringListApConfig.hashCode());
//		result = prime * result + ((perimeterDetectionEnabled == null) ? 0 : perimeterDetectionEnabled.hashCode());
//		result = prime * result + ((probeResponseThresholdDb == null) ? 0 : probeResponseThresholdDb.hashCode());
		result = prime * result + ((rf == null) ? 0 : rf.hashCode());
		result = prime * result + ((rtsCtsThreshold == null) ? 0 : rtsCtsThreshold.hashCode());
//		result = prime * result + ((rxCellSizeDb == null) ? 0 : rxCellSizeDb.hashCode());
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
//		if (bestApEnabled == null) {
//			if (other.bestApEnabled != null)
//				return false;
//		} else if (!bestApEnabled.equals(other.bestApEnabled))
//			return false;
//		if (bestApSettings == null) {
//			if (other.bestApSettings != null)
//				return false;
//		} else if (!bestApSettings.equals(other.bestApSettings))
//			return false;
		if (channelBandwidth != other.channelBandwidth)
			return false;
//		if (channelHopSettings == null) {
//			if (other.channelHopSettings != null)
//				return false;
//		} else if (!channelHopSettings.equals(other.channelHopSettings))
//			return false;
//		if (clientDisconnectThresholdDb == null) {
//			if (other.clientDisconnectThresholdDb != null)
//				return false;
//		} else if (!clientDisconnectThresholdDb.equals(other.clientDisconnectThresholdDb))
//			return false;
//		if (eirpTxPower == null) {
//			if (other.eirpTxPower != null)
//				return false;
//		} else if (!eirpTxPower.equals(other.eirpTxPower))
//			return false;
		if (forceScanDuringVoice != other.forceScanDuringVoice)
			return false;
//		if (managementRate != other.managementRate)
//			return false;
		if (maxNumClients == null) {
			if (other.maxNumClients != null)
				return false;
		} else if (!maxNumClients.equals(other.maxNumClients))
			return false;
		if (mimoMode != other.mimoMode)
			return false;
//		if (minAutoCellSize == null) {
//			if (other.minAutoCellSize != null)
//				return false;
//		} else if (!minAutoCellSize.equals(other.minAutoCellSize))
//			return false;
		if (multicastRate != other.multicastRate)
			return false;
//		if (neighbouringListApConfig == null) {
//			if (other.neighbouringListApConfig != null)
//				return false;
//		} else if (!neighbouringListApConfig.equals(other.neighbouringListApConfig))
//			return false;
//		if (perimeterDetectionEnabled == null) {
//			if (other.perimeterDetectionEnabled != null)
//				return false;
//		} else if (!perimeterDetectionEnabled.equals(other.perimeterDetectionEnabled))
//			return false;
//		if (probeResponseThresholdDb == null) {
//			if (other.probeResponseThresholdDb != null)
//				return false;
//		} else if (!probeResponseThresholdDb.equals(other.probeResponseThresholdDb))
//			return false;
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
//		if (rxCellSizeDb == null) {
//			if (other.rxCellSizeDb != null)
//				return false;
//		} else if (!rxCellSizeDb.equals(other.rxCellSizeDb))
//			return false;
		return true;
	}
	
	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}
		if (StateSetting.isUnsupported(forceScanDuringVoice) || ChannelBandwidth.isUnsupported(channelBandwidth)
				|| MimoMode.isUnsupported(mimoMode) || MulticastRate.isUnsupported(multicastRate) 
//				|| ManagementRate.isUnsupported(managementRate)
				|| ActiveScanSettings.hasUnsupportedValue(activeScanSettings) 
//				|| AutoOrManualValue.hasUnsupportedValue(rxCellSizeDb) 
//				|| AutoOrManualValue.hasUnsupportedValue(probeResponseThresholdDb) || AutoOrManualValue.hasUnsupportedValue(clientDisconnectThresholdDb) 
//				|| AutoOrManualValue.hasUnsupportedValue(eirpTxPower) || NeighbouringAPListConfiguration.hasUnsupportedValue(neighbouringListApConfig)
//				|| ChannelHopSettings.hasUnsupportedValue(channelHopSettings) || RadioBestApSettings.hasUnsupportedValue(bestApSettings)
				) {
			return true;
		}
		return false;
	}

}
