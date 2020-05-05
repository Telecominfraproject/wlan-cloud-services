package com.telecominfraproject.wlan.profile.network.models;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;


public class SyslogRelay extends BaseJsonModel {

    private static final long serialVersionUID = 8587220564376017229L;
    private static final int DEFAULT_HOST_PORT = 514;
    
    private boolean enabled;
    private InetAddress srvHostIp;
    private int srvHostPort;
    SeverityType severity;
    
    public enum SeverityType {

        EMERG(0),
        ALERT(1),
        CRIT(2),
        ERR(3),
        WARING(4),
        NOTICE(5),
        INFO(6),
        DEBUG(7),
        
        UNSUPPORTED(-1);

        private final int id;
        private static final Map<Integer, SeverityType> ELEMENTS = new HashMap<>();

        private SeverityType(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static SeverityType getById(int enumId) {
            if (ELEMENTS.isEmpty()) {
                synchronized (ELEMENTS) {
                    if (ELEMENTS.isEmpty()) {
                        //initialize elements map
                        for(SeverityType met : SeverityType.values()) {
                            ELEMENTS.put(met.getId(), met);
                        }
                    }
                }
            }
            return ELEMENTS.get(enumId);
        }
        @JsonCreator
        public static SeverityType getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, SeverityType.class, UNSUPPORTED);
        }
        
        public static boolean isUnsupported(SeverityType value) {
            return UNSUPPORTED.equals(value);
        }
    }
    
    public SyslogRelay() {
        severity = SeverityType.NOTICE;
        srvHostPort = DEFAULT_HOST_PORT;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public InetAddress getSrvHostIp() {
        return srvHostIp;
    }
    
    public void setSrvHostIp(InetAddress srvHostIp) {
        this.srvHostIp = srvHostIp;
    }
    
    public int getSrvHostPort() {
        return srvHostPort;
    }
    
    public void setSrvHostPort(int srvHostPort) {
        this.srvHostPort = srvHostPort;
    }
    
    public SeverityType getSeverity() {
        return severity;
    }
    
    public void setSeverity(SeverityType severity) {
        this.severity = severity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((severity == null) ? 0 : severity.hashCode());
        result = prime * result + ((srvHostIp == null) ? 0 : srvHostIp.hashCode());
        result = prime * result + srvHostPort;
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
        SyslogRelay other = (SyslogRelay) obj;
        if (enabled != other.enabled)
            return false;
        if (severity != other.severity)
            return false;
        if (srvHostIp == null) {
            if (other.srvHostIp != null)
                return false;
        } else if (!srvHostIp.equals(other.srvHostIp))
            return false;
        if (srvHostPort != other.srvHostPort)
            return false;
        return true;
    }

    @Override
    public SyslogRelay clone() {
        return (SyslogRelay)super.clone();
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (SeverityType.isUnsupported(severity)) {
            return true;
        }
        
        return false;
    }
}
