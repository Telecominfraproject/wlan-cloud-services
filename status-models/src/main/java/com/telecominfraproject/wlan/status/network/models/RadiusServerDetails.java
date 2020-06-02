package com.telecominfraproject.wlan.status.network.models;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class RadiusServerDetails extends BaseJsonModel {

	private static final long serialVersionUID = -9087767166420077413L;
	private InetAddress address;
	private MinMaxAvgValueInt radiusLatency;

	public MinMaxAvgValueInt getRadiusLatency() {
		return radiusLatency;
	}
	public void setRadiusLatency(MinMaxAvgValueInt radiusLatency) {
		this.radiusLatency = radiusLatency;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	@Override
	public RadiusServerDetails clone() {
		RadiusServerDetails ret = (RadiusServerDetails) super.clone();
		
		if(radiusLatency!=null) {
			ret.radiusLatency = radiusLatency.clone();
		}
		
		return ret;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(address, radiusLatency);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RadiusServerDetails)) {
			return false;
		}
		RadiusServerDetails other = (RadiusServerDetails) obj;
		return Objects.equals(address, other.address) && Objects.equals(radiusLatency, other.radiusLatency);
	}
	
	
}