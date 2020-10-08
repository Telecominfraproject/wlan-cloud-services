package com.telecominfraproject.wlan.profile.rf.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualValue;
import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.ChannelHopSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.ActiveScanSettings;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration.ApModel;
import com.telecominfraproject.wlan.equipment.models.ManagementRate;
import com.telecominfraproject.wlan.equipment.models.MimoMode;
import com.telecominfraproject.wlan.equipment.models.MulticastRate;
import com.telecominfraproject.wlan.equipment.models.NeighbouringAPListConfiguration;
import com.telecominfraproject.wlan.equipment.models.StateSetting;

public class RfElementConfiguration extends BaseJsonModel {

	private static final long serialVersionUID = 1071268246965238451L;
	
    private static final Logger LOG = LoggerFactory.getLogger(RfElementConfiguration.class);
    
    private final static Map<RadioType, Integer> MIN_CELL_SIZE_MAP;
    static {
    	Map<RadioType, Integer> map = new HashMap<>();
    	map.put(RadioType.is2dot4GHz, -80);
    	map.put(RadioType.is5GHz, -80);
    	map.put(RadioType.is5GHzL, -80);
    	map.put(RadioType.is5GHzU, -80);
    	MIN_CELL_SIZE_MAP = Collections.unmodifiableMap(map);
    }
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
    
  	// RRM related parameters
  	private ManagementRate managementRate;
  	private AutoOrManualValue rxCellSizeDb;
	private AutoOrManualValue probeResponseThresholdDb;
	private AutoOrManualValue clientDisconnectThresholdDb;
	private AutoOrManualValue eirpTxPower;
	private Boolean bestApEnabled;
	private NeighbouringAPListConfiguration neighbouringListApConfig;
	private Integer minAutoCellSize;
	private Boolean perimeterDetectionEnabled;
	private ChannelHopSettings channelHopSettings;
	private RadioBestApSettings bestApSettings;
    
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
    	setManagementRate(ManagementRate.auto);
    	setRxCellSizeDb(AutoOrManualValue.createAutomaticInstance(DEFAULT_RX_CELL_SIZE_DB));
    	setProbeResponseThresholdDb(AutoOrManualValue.createAutomaticInstance(-90));
    	setClientDisconnectThresholdDb(AutoOrManualValue.createAutomaticInstance(-90));
    	setEirpTxPower(AutoOrManualValue.createAutomaticInstance(DEFAULT_EIRP_TX_POWER));
    	setBestApEnabled(null);
    	setNeighbouringListApConfig(NeighbouringAPListConfiguration.createDefault());
    	setPerimeterDetectionEnabled(true);
    	setChannelHopSettings(ChannelHopSettings.createWithDefaults());
    }
    
    public static RfElementConfiguration createWithDefaults(RadioType radioType) {
        RfElementConfiguration ret = new RfElementConfiguration();  
        ret.setBestApSettings(RadioBestApSettings.createWithDefaults(radioType));
        ret.setMinAutoCellSize(MIN_CELL_SIZE_MAP.get(radioType));
        if (radioType == RadioType.is5GHz || radioType == RadioType.is5GHzL || radioType == RadioType.is5GHzU) {
    		ret.setChannelBandwidth(ChannelBandwidth.is80MHz);
        } else {
        	ret.setChannelBandwidth(ChannelBandwidth.is20MHz);
        }
    	return ret;
    }
    
    public static RfElementConfiguration createWithDefaults(RadioType radioType, ApModel apModel) {
		RfElementConfiguration ret = createWithDefaults(radioType);
		if (apModel == ApModel.OUTDOOR) {
			// TODO: This logic is carried over from ApElementConfig during RF profile implementation. Is it still necessary?
	    	// NAAS-8919 change mimo for outdoor to 3x3
			ret.setMimoMode(MimoMode.threeByThree);
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

	public ManagementRate getManagementRate() {
		return managementRate;
	}

	public void setManagementRate(ManagementRate managementRate) {
		this.managementRate = managementRate;
	}

	public AutoOrManualValue getRxCellSizeDb() {
		return rxCellSizeDb;
	}

	public void setRxCellSizeDb(AutoOrManualValue rxCellSizeDb) {
		this.rxCellSizeDb = rxCellSizeDb;
	}

	public AutoOrManualValue getProbeResponseThresholdDb() {
		return probeResponseThresholdDb;
	}

	public void setProbeResponseThresholdDb(AutoOrManualValue probeResponseThresholdDb) {
		this.probeResponseThresholdDb = probeResponseThresholdDb;
	}

	public AutoOrManualValue getClientDisconnectThresholdDb() {
		return clientDisconnectThresholdDb;
	}

	public void setClientDisconnectThresholdDb(AutoOrManualValue clientDisconnectThresholdDb) {
		this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
	}

	public AutoOrManualValue getEirpTxPower() {
		return eirpTxPower;
	}

	public void setEirpTxPower(AutoOrManualValue eirpTxPower) {
		this.eirpTxPower = eirpTxPower;
	}

	public Boolean getBestApEnabled() {
		return bestApEnabled;
	}

	public void setBestApEnabled(Boolean bestApEnabled) {
		this.bestApEnabled = bestApEnabled;
	}

	public NeighbouringAPListConfiguration getNeighbouringListApConfig() {
		return neighbouringListApConfig;
	}

	public void setNeighbouringListApConfig(NeighbouringAPListConfiguration neighbouringListApConfig) {
		this.neighbouringListApConfig = neighbouringListApConfig;
	}

	public int getMinAutoCellSize(RadioType radioType) {
		if (minAutoCellSize == null) {
    		if (MIN_CELL_SIZE_MAP.containsKey(radioType)) {
    			return MIN_CELL_SIZE_MAP.get(radioType);
    		} else {
    			return MIN_CELL_SIZE_MAP.get(RadioType.is2dot4GHz);
    		}
		}

		return minAutoCellSize;
	}

	public void setMinAutoCellSize(Integer minAutoCellSize) {
		this.minAutoCellSize = minAutoCellSize;
	}

	public Boolean getPerimeterDetectionEnabled() {
		return perimeterDetectionEnabled;
	}

	public void setPerimeterDetectionEnabled(Boolean perimeterDetectionEnabled) {
		this.perimeterDetectionEnabled = perimeterDetectionEnabled;
	}

	public ChannelHopSettings getChannelHopSettings() {
		return channelHopSettings;
	}

	public void setChannelHopSettings(ChannelHopSettings channelHopSettings) {
		this.channelHopSettings = channelHopSettings;
	}

	public RadioBestApSettings getBestApSettings() {
		return bestApSettings;
	}

	public void setBestApSettings(RadioBestApSettings bestApSettings) {
		this.bestApSettings = bestApSettings;
	}

	@Override
    public RfElementConfiguration clone() {
        return (RfElementConfiguration) super.clone();
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(rf, beaconInterval, forceScanDuringVoice, rtsCtsThreshold, channelBandwidth,
				mimoMode, maxNumClients, multicastRate, activeScanSettings, managementRate, rxCellSizeDb,
				probeResponseThresholdDb, clientDisconnectThresholdDb, eirpTxPower, bestApEnabled,
				neighbouringListApConfig, minAutoCellSize, perimeterDetectionEnabled, 
				channelHopSettings, bestApSettings);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof RfElementConfiguration))
			return false;
		RfElementConfiguration other = (RfElementConfiguration) obj;
		return Objects.equals(rf, other.rf)
				&& Objects.equals(beaconInterval, other.beaconInterval)
				&& Objects.equals(forceScanDuringVoice, other.forceScanDuringVoice)
				&& Objects.equals(rtsCtsThreshold, other.rtsCtsThreshold)
				&& Objects.equals(channelBandwidth, other.channelBandwidth)
				&& Objects.equals(mimoMode, other.mimoMode)
				&& Objects.equals(maxNumClients, other.maxNumClients)
				&& Objects.equals(multicastRate, other.multicastRate)
				&& Objects.equals(activeScanSettings, other.activeScanSettings)
				&& Objects.equals(managementRate, other.managementRate)
				&& Objects.equals(rxCellSizeDb, other.rxCellSizeDb)
				&& Objects.equals(probeResponseThresholdDb, other.probeResponseThresholdDb)
				&& Objects.equals(clientDisconnectThresholdDb, other.clientDisconnectThresholdDb)
				&& Objects.equals(eirpTxPower, other.eirpTxPower)
				&& Objects.equals(bestApEnabled, other.bestApEnabled)
				&& Objects.equals(neighbouringListApConfig, other.neighbouringListApConfig)
				&& Objects.equals(minAutoCellSize, other.minAutoCellSize)
				&& Objects.equals(perimeterDetectionEnabled, other.perimeterDetectionEnabled)
				&& Objects.equals(channelHopSettings, other.channelHopSettings)
				&& Objects.equals(bestApSettings, other.bestApSettings);
	}
	
	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}
		if (StateSetting.isUnsupported(forceScanDuringVoice) || ChannelBandwidth.isUnsupported(channelBandwidth)
				|| MimoMode.isUnsupported(mimoMode) || MulticastRate.isUnsupported(multicastRate) 
				|| ActiveScanSettings.hasUnsupportedValue(activeScanSettings) 
				|| AutoOrManualValue.hasUnsupportedValue(rxCellSizeDb) || ManagementRate.isUnsupported(managementRate)
				|| AutoOrManualValue.hasUnsupportedValue(probeResponseThresholdDb) || AutoOrManualValue.hasUnsupportedValue(clientDisconnectThresholdDb) 
				|| AutoOrManualValue.hasUnsupportedValue(eirpTxPower) || NeighbouringAPListConfiguration.hasUnsupportedValue(neighbouringListApConfig)
				|| ChannelHopSettings.hasUnsupportedValue(channelHopSettings) || RadioBestApSettings.hasUnsupportedValue(bestApSettings)
				) {
			return true;
		}
		return false;
	}

}
