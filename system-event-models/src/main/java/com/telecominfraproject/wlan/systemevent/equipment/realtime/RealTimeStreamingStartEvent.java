package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

public class RealTimeStreamingStartEvent extends RealTimeEvent
        implements HasCustomerId, HasEquipmentId, HasClientMac, HasProducedTimestamp {

    private static final long serialVersionUID = -591221857158333271L;
    private Long videoSessionId;
    private Long sessionId;
    private MacAddress clientMac;
    private InetAddress serverIp;
    private String serverDnsName;
    private StreamingVideoType type;

    public RealTimeStreamingStartEvent() {
        this(0L);
    }

    public RealTimeStreamingStartEvent(long eventTimestamp) {
        super(RealTimeEventType.VideoStreamStart, eventTimestamp);
    }

    public RealTimeStreamingStartEvent(int customerId, long equipmentId, long eventTimestamp) {
        super(RealTimeEventType.VideoStreamStart, customerId, equipmentId, eventTimestamp);
    }

    public String getServerDnsName() {
        return serverDnsName;
    }

    public void setServerDnsName(String serverDnsName) {
        this.serverDnsName = serverDnsName;
    }

    public Long getVideoSessionId() {
        return videoSessionId;
    }

    public void setVideoSessionId(Long videoSessionId) {
        this.videoSessionId = videoSessionId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public InetAddress getServerIp() {
        return serverIp;
    }

    public void setServerIp(InetAddress serverIp) {
        this.serverIp = serverIp;
    }

    public StreamingVideoType getType() {
        return type;
    }

    public void setType(StreamingVideoType type) {
        this.type = type;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue() || ((type != null) && StreamingVideoType.isUnsupported(type))) {
            return true;
        }

        return false;
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMac;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        clientMac = clientMacAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + Objects.hash(clientMac, serverDnsName, serverIp, sessionId, type, videoSessionId);
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
        if (!(obj instanceof RealTimeStreamingStartEvent)) {
            return false;
        }
        RealTimeStreamingStartEvent other = (RealTimeStreamingStartEvent) obj;
        return Objects.equals(clientMac, other.clientMac) && Objects.equals(serverDnsName, other.serverDnsName)
                && Objects.equals(serverIp, other.serverIp) && Objects.equals(sessionId, other.sessionId)
                && (type == other.type) && Objects.equals(videoSessionId, other.videoSessionId);
    }


}
