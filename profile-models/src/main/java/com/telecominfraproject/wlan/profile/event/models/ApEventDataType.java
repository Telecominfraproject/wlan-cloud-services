package com.telecominfraproject.wlan.profile.event.models;

public enum ApEventDataType {

    ClientAddedEvent,
    ClientChangedEvent,
    ClientRemovedEvent,
    ClientSessionAddedEvent,
    ClientSessionChangedEvent,
    ClientSessionRemovedEvent,
    DhcpAckEvent,
    DhcpNakEvent,
    DhcpDeclineEvent,
    DhcpDiscoverEvent,
    DhcpInformEvent,
    DhcpOfferEvent,       
    DhcpRequestEvent,
    ClientAssocEvent,
    ClientConnectSuccessEvent,
    ClientFailureEvent,
    ClientIdEvent,
    ClientTimeoutEvent,
    ClientAuthEvent,
    ClientDisconnectEvent,
    ClientFirstDataEvent,
    ClientIpAddressEvent,
    RealTimeChannelHopEvent,
    RealTimeSipCallEventWithStats,
    RealTimeSipCallReportEvent,
    RealTimeSipCallStartEvent,
    RealTimeSipCallStopEvent,
    RealTimeStreamingStartEvent,
    RealTimeStreamingStartSessionEvent,
    RealTimeStreamingStopEvent,
    UNSUPPORTED
    
}
