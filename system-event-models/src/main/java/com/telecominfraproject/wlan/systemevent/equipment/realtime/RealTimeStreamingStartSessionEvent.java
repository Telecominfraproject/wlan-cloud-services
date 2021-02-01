package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

public class RealTimeStreamingStartSessionEvent extends RealTimeEvent
        implements HasCustomerId, HasEquipmentId, HasClientMac, HasProducedTimestamp {

    private static final long serialVersionUID = 4395850344272425198L;
    private Long videoSessionId;
    private Long sessionId;
    private MacAddress clientMac;
    private InetAddress serverIp;
    private StreamingVideoType type;

    public RealTimeStreamingStartSessionEvent() {
        this(0L);
    }

    public RealTimeStreamingStartSessionEvent(long eventTimestamp) {
        super(RealTimeEventType.VideoStreamDebugStart, eventTimestamp);
    }

    public RealTimeStreamingStartSessionEvent(int customerId, long locationId, long equipmentId, long eventTimestamp) {
        super(RealTimeEventType.VideoStreamDebugStart, customerId, locationId, equipmentId, eventTimestamp);
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
        result = (prime * result) + Objects.hash(clientMac, serverIp, sessionId, type, videoSessionId);
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
        if (!(obj instanceof RealTimeStreamingStartSessionEvent)) {
            return false;
        }
        RealTimeStreamingStartSessionEvent other = (RealTimeStreamingStartSessionEvent) obj;
        return Objects.equals(clientMac, other.clientMac) && Objects.equals(serverIp, other.serverIp)
                && Objects.equals(sessionId, other.sessionId) && (type == other.type)
                && Objects.equals(videoSessionId, other.videoSessionId);
    }

}
