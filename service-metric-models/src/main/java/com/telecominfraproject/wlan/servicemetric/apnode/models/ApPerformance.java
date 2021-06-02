package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasSourceTimestamp;

/**
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApPerformance extends BaseJsonModel implements HasSourceTimestamp {

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
    private int[] cpuUtilized;

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
    
    /**
     * Timestamp of the stats report used to generate this metric (i.e. timestamp from the AP)
     */
    private long sourceTimestampMs;
    
    /**
     *  Top CPU consuming processes.
     *  CPU usage in percent.
     */
    private List<PerProcessUtilization> psCpuUtil;
    
    /**
     *  Most memory consuming processes.
     *  Memory usage in kB.
     */
    private List<PerProcessUtilization> psMemUtil;

    @Override
    public ApPerformance clone() {
        ApPerformance ret = (ApPerformance) super.clone();
        if(this.cpuUtilized!=null){
            ret.cpuUtilized = this.cpuUtilized.clone();
        }
        if (this.psCpuUtil!=null) {
            List<PerProcessUtilization> newPsCpuUtil = new ArrayList<>();
            psCpuUtil.stream().forEach(p -> newPsCpuUtil.add(p.clone()));
            ret.setPsCpuUtil(newPsCpuUtil);
        }
        if (this.psMemUtil!=null) {
            List<PerProcessUtilization> newPsMemUtil = new ArrayList<>();
            psMemUtil.stream().forEach(p -> newPsMemUtil.add(p.clone()));
            ret.setPsMemUtil(newPsMemUtil); 
        }
        return ret;
    }

  

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(cpuUtilized);
        result = prime * result + Objects.hash(camiCrashed, cloudRxBytes, cloudTxBytes, cpuTemperature, ethLinkState, freeMemory, lowMemoryReboot, psCpuUtil,
                psMemUtil, sourceTimestampMs, upTime);
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
        ApPerformance other = (ApPerformance) obj;
        return Objects.equals(camiCrashed, other.camiCrashed) && Objects.equals(cloudRxBytes, other.cloudRxBytes)
                && Objects.equals(cloudTxBytes, other.cloudTxBytes) && Objects.equals(cpuTemperature, other.cpuTemperature)
                && Arrays.equals(cpuUtilized, other.cpuUtilized) && ethLinkState == other.ethLinkState && Objects.equals(freeMemory, other.freeMemory)
                && Objects.equals(lowMemoryReboot, other.lowMemoryReboot) && Objects.equals(psCpuUtil, other.psCpuUtil)
                && Objects.equals(psMemUtil, other.psMemUtil) && sourceTimestampMs == other.sourceTimestampMs && Objects.equals(upTime, other.upTime);
    }



    @JsonIgnore
    public Float getAvgCpuUtilized() {
        if (cpuUtilized == null) {
            return null;
        }
        Float ret = 0f;
        for (int b : cpuUtilized) {
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

    public int[] getCpuUtilized() {
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

    public List<PerProcessUtilization> getPsCpuUtil() {
        return psCpuUtil;
    }

    public List<PerProcessUtilization> getPsMemUtil() {
        return psMemUtil;
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

    public void setCpuUtilized(int[] cpuUtilized) {
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

    public void setPsCpuUtil(List<PerProcessUtilization> psCpuUtil) {
        this.psCpuUtil = psCpuUtil;
    }

    public void setPsMemUtil(List<PerProcessUtilization> psMemUtil) {
        this.psMemUtil = psMemUtil;
    }
    
    public void setSourceTimestampMs(long sourceTimestamp) {
        this.sourceTimestampMs = sourceTimestamp;
    }

    @Override
    public long getSourceTimestampMs() {
        return sourceTimestampMs;
    }
    
}
