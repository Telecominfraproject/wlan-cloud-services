package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApPerformance extends BaseJsonModel {

	private static final long serialVersionUID = 4520822419578448525L;

    /**
     * free memory in kilobytes
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer freeMemory;

    /**
     * CPU utilization in percentage, one per byte
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private byte[] cpuUtilized;

    /**
     * AP uptime in seconds
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long upTime;

    /**
     * number of time wc-cami crashed
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer camiCrashed;

    /**
     * cpu temperature in celsius
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer cpuTemperature;

    /**
     * low memory reboot flag
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean lowMemoryReboot;

    /**
     * Ethernet Link State
     */
    private EthernetLinkState ethLinkState;

    /**
     * Data sent by AP to the cloud
     */
    private Long cloudTxBytes;

    /**
     * Data received by AP from cloud
     */
    private Long cloudRxBytes;

    @Override
    public ApPerformance clone() {
        ApPerformance ret = (ApPerformance) super.clone();
        if(this.cpuUtilized!=null){
            ret.cpuUtilized = this.cpuUtilized.clone();
        }
        
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ApPerformance)) {
            return false;
        }
        ApPerformance other = (ApPerformance) obj;
        if (camiCrashed == null) {
            if (other.camiCrashed != null) {
                return false;
            }
        } else if (!camiCrashed.equals(other.camiCrashed)) {
            return false;
        }
        if (cloudRxBytes == null) {
            if (other.cloudRxBytes != null) {
                return false;
            }
        } else if (!cloudRxBytes.equals(other.cloudRxBytes)) {
            return false;
        }
        if (cloudTxBytes == null) {
            if (other.cloudTxBytes != null) {
                return false;
            }
        } else if (!cloudTxBytes.equals(other.cloudTxBytes)) {
            return false;
        }
        if (cpuTemperature == null) {
            if (other.cpuTemperature != null) {
                return false;
            }
        } else if (!cpuTemperature.equals(other.cpuTemperature)) {
            return false;
        }
        if (!Arrays.equals(cpuUtilized, other.cpuUtilized)) {
            return false;
        }
        if (ethLinkState != other.ethLinkState) {
            return false;
        }
        if (freeMemory == null) {
            if (other.freeMemory != null) {
                return false;
            }
        } else if (!freeMemory.equals(other.freeMemory)) {
            return false;
        }
        if (lowMemoryReboot == null) {
            if (other.lowMemoryReboot != null) {
                return false;
            }
        } else if (!lowMemoryReboot.equals(other.lowMemoryReboot)) {
            return false;
        }
        if (upTime == null) {
            if (other.upTime != null) {
                return false;
            }
        } else if (!upTime.equals(other.upTime)) {
            return false;
        }
        return true;
    }

    @JsonIgnore
    public Float getAvgCpuUtilized() {
        if (cpuUtilized == null) {
            return null;
        }
        Float ret = 0f;
        for (byte b : cpuUtilized) {
            ret += b;
        }
        return ret / cpuUtilized.length;
    }

    public Integer getCamiCrashed() {
        return camiCrashed;
    }

    /**
     * @return the cloudRxBytes
     */
    public Long getCloudRxBytes() {
        return cloudRxBytes;
    }

    /**
     * @return the cloudTxBytes
     */
    public Long getCloudTxBytes() {
        return cloudTxBytes;
    }

    public Integer getCpuTemperature() {
        return cpuTemperature;
    }

    public byte[] getCpuUtilized() {
        return cpuUtilized;
    }

    /**
     * @return the ethLinkState
     */
    public EthernetLinkState getEthLinkState() {
        return ethLinkState;
    }

    public Integer getFreeMemory() {
        return freeMemory;
    }

    public Boolean getLowMemoryReboot() {
        return lowMemoryReboot;
    }

    public Long getUpTime() {
        return upTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((camiCrashed == null) ? 0 : camiCrashed.hashCode());
        result = prime * result + ((cloudRxBytes == null) ? 0 : cloudRxBytes.hashCode());
        result = prime * result + ((cloudTxBytes == null) ? 0 : cloudTxBytes.hashCode());
        result = prime * result + ((cpuTemperature == null) ? 0 : cpuTemperature.hashCode());
        result = prime * result + Arrays.hashCode(cpuUtilized);
        result = prime * result + ((ethLinkState == null) ? 0 : ethLinkState.hashCode());
        result = prime * result + ((freeMemory == null) ? 0 : freeMemory.hashCode());
        result = prime * result + ((lowMemoryReboot == null) ? 0 : lowMemoryReboot.hashCode());
        result = prime * result + ((upTime == null) ? 0 : upTime.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        return (super.hasUnsupportedValue() || EthernetLinkState.isUnsupported(this.ethLinkState));
    }

    public void setCamiCrashed(Integer camiCrashed) {
        this.camiCrashed = camiCrashed;
    }

    /**
     * @param cloudRxBytes
     *            the cloudRxBytes to set
     */
    public void setCloudRxBytes(Long cloudRxBytes) {
        this.cloudRxBytes = cloudRxBytes;
    }

    /**
     * @param cloudTxBytes
     *            the cloudTxBytes to set
     */
    public void setCloudTxBytes(Long cloudTxBytes) {
        this.cloudTxBytes = cloudTxBytes;
    }

    public void setCpuTemperature(Integer cpuTemperature) {
        this.cpuTemperature = cpuTemperature;
    }

    public void setCpuUtilized(byte[] cpuUtilized) {
        this.cpuUtilized = cpuUtilized;
    }

    /**
     * @param ethLinkState
     *            the ethLinkState to set
     */
    public void setEthLinkState(EthernetLinkState ethLinkState) {
        this.ethLinkState = ethLinkState;
    }

    public void setFreeMemory(Integer freeMemory) {
        this.freeMemory = freeMemory;
    }

    public void setLowMemoryReboot(Boolean lowMemoryReboot) {
        this.lowMemoryReboot = lowMemoryReboot;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }
}
