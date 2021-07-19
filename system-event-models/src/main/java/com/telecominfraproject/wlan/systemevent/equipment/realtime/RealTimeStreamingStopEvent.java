package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

public class RealTimeStreamingStopEvent extends RealTimeEvent
        implements HasCustomerId, HasEquipmentId, HasClientMac, HasProducedTimestamp {

    private static final long serialVersionUID = 6433913573274597688L;
    private String videoSessionId;
    private String sessionId;
    private MacAddress clientMac;
    private InetAddress serverIp;
    private Long totalBytes;
    private StreamingVideoType type;
    private Integer durationInSecs;

    public RealTimeStreamingStopEvent() {
        this(0L);
    }

    public RealTimeStreamingStopEvent(long eventTimestamp) {
        super(RealTimeEventType.VideoStreamStop, eventTimestamp);
    }

    public RealTimeStreamingStopEvent(int customerId, long locationId, long equipmentId, long eventTimestamp) {
        super(RealTimeEventType.VideoStreamStop, customerId, locationId, equipmentId, eventTimestamp);
    }

    public String getVideoSessionId() {
        return videoSessionId;
    }

    public void setVideoSessionId(String videoSessionId) {
        this.videoSessionId = videoSessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public InetAddress getServerIp() {
        return serverIp;
    }

    public void setServerIp(InetAddress serverIp) {
        this.serverIp = serverIp;
    }

    public Long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public StreamingVideoType getType() {
        return type;
    }

    public void setType(StreamingVideoType type) {
        this.type = type;
    }

    public Integer getDurationInSecs() {
        return durationInSecs;
    }

    public void setDurationInSecs(Integer durationInSecs) {
        this.durationInSecs = durationInSecs;
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
        result = (prime * result)
                + Objects.hash(clientMac, durationInSecs, serverIp, sessionId, totalBytes, type, videoSessionId);
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
        if (!(obj instanceof RealTimeStreamingStopEvent)) {
            return false;
        }
        RealTimeStreamingStopEvent other = (RealTimeStreamingStopEvent) obj;
        return Objects.equals(clientMac, other.clientMac) && Objects.equals(durationInSecs, other.durationInSecs)
                && Objects.equals(serverIp, other.serverIp) && Objects.equals(sessionId, other.sessionId)
                && Objects.equals(totalBytes, other.totalBytes) && (type == other.type)
                && Objects.equals(videoSessionId, other.videoSessionId);
    }

}
