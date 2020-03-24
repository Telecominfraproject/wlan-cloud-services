package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

/**
 * Set of client metrics reported by the access Point.
 * 
 * @author ekeddy
 *
 */
public class ApClientMetrics extends MetricData implements ClientMetricsInterface {

    private static final long serialVersionUID = 6863684725639609684L;

    public static final String TYPE_NAME = ApClientMetrics.class.getSimpleName();
    private ClientMetrics[] clientMetrics2g;
    private ClientMetrics[] clientMetrics5g;
    private ClientQoEMetrics[] clientMetricsQoE;

    /**
     * How many seconds the AP measured for the metric
     */
    private Integer periodLengthSec = 5;

    @Override
    public String getType() {
        return TYPE_NAME;
    }

    public Integer getPeriodLengthSec() {
        return periodLengthSec;
    }

    public void setPeriodLengthSec(Integer periodLengthSec) {
        this.periodLengthSec = periodLengthSec;
    }

    public ClientMetrics[] getClientMetrics2g() {
        return clientMetrics2g;
    }

    public void setClientMetrics2g(ClientMetrics[] clientMetrics2g) {
        this.clientMetrics2g = clientMetrics2g;
    }

    public ClientMetrics[] getClientMetrics5g() {
        return clientMetrics5g;
    }

    public void setClientMetrics5g(ClientMetrics[] clientMetrics5g) {
        this.clientMetrics5g = clientMetrics5g;
    }

    public long getClientCount2g() {
        return clientMetrics2g != null ? clientMetrics2g.length : 0;
    }

    public long getClientCount5g() {
        return clientMetrics5g != null ? clientMetrics5g.length : 0;
    }

    @Override
    @JsonIgnore
    public Set<MacAddress> getDeviceMacAddresses() {
        Set<MacAddress> ret = new HashSet<>();

        if (clientMetrics2g != null) {
            for (ClientMetrics cm : clientMetrics2g) {
                ret.add(cm.getDeviceMacAddress());
            }
        }

        if (clientMetrics5g != null) {
            for (ClientMetrics cm : clientMetrics5g) {
                ret.add(cm.getDeviceMacAddress());
            }
        }

        if (clientMetricsQoE != null) {
            for (ClientQoEMetrics cm : clientMetricsQoE) {
                ret.add(cm.getDeviceMacAddress());
            }
        }

        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(clientMetrics2g);
        result = prime * result + Arrays.hashCode(clientMetrics5g);
        result = prime * result + ((periodLengthSec == null) ? 0 : periodLengthSec.hashCode());
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
        ApClientMetrics other = (ApClientMetrics) obj;
        if (!Arrays.equals(clientMetrics2g, other.clientMetrics2g))
            return false;
        if (!Arrays.equals(clientMetrics5g, other.clientMetrics5g))
            return false;
        if (!Arrays.equals(clientMetricsQoE, other.clientMetricsQoE))
        if (periodLengthSec == null) {
            if (other.periodLengthSec != null)
                return false;
        } else if (!periodLengthSec.equals(other.periodLengthSec))
            return false;
        return true;
    }

    @Override
    public boolean containsClientMac(MacAddress clientMac) {
        return containsClientMac(clientMetrics2g, clientMac) || containsClientMac(clientMetrics5g, clientMac)
                || containsClientMac(clientMetricsQoE, clientMac);
    }

    private boolean containsClientMac(BaseClientMetrics[] clientMetrics, MacAddress clientMac) {
        if (clientMetrics == null)
            return false;
        for (BaseClientMetrics m : clientMetrics) {
            if (m.getDeviceMacAddress() != null && m.getDeviceMacAddress().equals(clientMac)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ApClientMetrics clone() {
        ApClientMetrics ret = (ApClientMetrics) super.clone();

        if (this.clientMetrics2g != null) {
            ret.clientMetrics2g = new ClientMetrics[this.clientMetrics2g.length];
            for (int i = 0; i < this.clientMetrics2g.length; i++) {
                ret.clientMetrics2g[i] = this.clientMetrics2g[i].clone();
            }
        }

        if (this.clientMetrics5g != null) {
            ret.clientMetrics5g = new ClientMetrics[this.clientMetrics5g.length];
            for (int i = 0; i < this.clientMetrics5g.length; i++) {
                ret.clientMetrics5g[i] = this.clientMetrics5g[i].clone();
            }
        }
        
        if (this.clientMetricsQoE != null) {
            ret.clientMetricsQoE = new ClientQoEMetrics[this.clientMetricsQoE.length];
            for (int i = 0; i < this.clientMetricsQoE.length; i++) {
                ret.clientMetricsQoE[i] = this.clientMetricsQoE[i].clone();
            }
        }

        return ret;
    }

    @JsonIgnore
    public ClientMetrics[] getClientMetrics(RadioType type) {
        if (type == RadioType.is2dot4GHz) {
            return this.clientMetrics2g;
        } else {
            return this.clientMetrics5g;
        }
    }

    public ClientQoEMetrics[] getClientMetricsQoE() {
        return clientMetricsQoE;
    }

    public void setClientMetricsQoE(ClientQoEMetrics[] clientMetricsQoE) {
        this.clientMetricsQoE = clientMetricsQoE;
    }

    public static boolean isDataType(String dataType) {
        return TYPE_NAME.equals(dataType);
    }
}
