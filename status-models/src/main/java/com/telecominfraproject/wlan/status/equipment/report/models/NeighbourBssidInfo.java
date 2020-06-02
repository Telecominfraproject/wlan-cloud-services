package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.DetectedAuthMode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.NeighborScanPacketType;
import com.telecominfraproject.wlan.core.model.equipment.NetworkType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class NeighbourBssidInfo extends BaseJsonModel {
    private static final long serialVersionUID = 1050412506735266748L;

    private MacAddress macAddress;
    private String ssid;
    private int beaconInterval;
    private NetworkType networkType;
    private boolean privacy;
    private RadioType radioType;
    private int channel;
    private int rate;
    private int rssi;
    private int signal;
    private long scanTimeInSeconds;
    private boolean nMode;
    private boolean acMode;
    private boolean bMode;
    private NeighborScanPacketType scanPacketType;
    private DetectedAuthMode detectedAuthMode;
        
    public MacAddress getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(MacAddress macAddress) {
		this.macAddress = macAddress;
	}

	public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getBeaconInterval() {
        return beaconInterval;
    }

    public void setBeaconInterval(int beaconInterval) {
        this.beaconInterval = beaconInterval;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getScanTimeInSeconds() {
        return scanTimeInSeconds;
    }

    public void setScanTimeInSeconds(long scanTimeInSeconds) {
        this.scanTimeInSeconds = scanTimeInSeconds;
    }

    public boolean getnMode() {
        return nMode;
    }

    public void setnMode(boolean nMode) {
        this.nMode = nMode;
    }

    public boolean getbMode() {
        return bMode;
    }

    public void setbMode(boolean bMode) {
        this.bMode = bMode;
    }

    public boolean getAcMode() {
        return acMode;
    }

    public void setAcMode(boolean acMode) {
        this.acMode = acMode;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public NeighborScanPacketType getScanPacketType() {
        return scanPacketType;
    }

    public void setScanPacketType(NeighborScanPacketType scanPacketType) {
        this.scanPacketType = scanPacketType;
    }

    public DetectedAuthMode getDetectedAuthMode() {
        return detectedAuthMode;
    }

    public void setDetectedAuthMode(DetectedAuthMode detectedAuthMode) {
        this.detectedAuthMode = detectedAuthMode;
    }

    @Override
    public NeighbourBssidInfo clone() {
    	NeighbourBssidInfo ret =  (NeighbourBssidInfo) super.clone();
    	
    	if(macAddress!=null) {
    		ret.macAddress = macAddress.clone();
    	}
    	
    	return ret;
    }


    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (NetworkType.isUnsupported(networkType) 
                || RadioType.isUnsupported(radioType) || NeighborScanPacketType.isUnsupported(scanPacketType)
                || DetectedAuthMode.isUnsupported(detectedAuthMode)) 
        {
            return true;
        }
        return false;
    }

	@Override
	public int hashCode() {
		return Objects.hash(acMode, bMode, beaconInterval, channel, detectedAuthMode, macAddress, nMode, networkType,
				privacy, radioType, rate, rssi, scanPacketType, scanTimeInSeconds, signal, ssid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NeighbourBssidInfo)) {
			return false;
		}
		NeighbourBssidInfo other = (NeighbourBssidInfo) obj;
		return acMode == other.acMode && bMode == other.bMode && beaconInterval == other.beaconInterval
				&& channel == other.channel && detectedAuthMode == other.detectedAuthMode
				&& Objects.equals(macAddress, other.macAddress) && nMode == other.nMode
				&& networkType == other.networkType && privacy == other.privacy && radioType == other.radioType
				&& rate == other.rate && rssi == other.rssi && scanPacketType == other.scanPacketType
				&& scanTimeInSeconds == other.scanTimeInSeconds && signal == other.signal
				&& Objects.equals(ssid, other.ssid);
	}

}
