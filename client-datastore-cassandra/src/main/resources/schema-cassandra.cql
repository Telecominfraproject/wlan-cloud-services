CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client (
    customerId int,
    macAddress bigint ,

    createdTimestamp bigint,
    lastModifiedTimestamp bigint,

    details blob,
    
    PRIMARY KEY ((customerId, macAddress))
) WITH comment='Wireless client details used by TIP WLAN CloudSDK';


CREATE INDEX IF NOT EXISTS idx_client_customerId ON tip_wlan_keyspace.client (customerId);


CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session (
    customerId int,
    equipmentId bigint,    
    macAddress bigint ,

    locationId bigint,

    lastModifiedTimestamp bigint,

    details blob,
    
    PRIMARY KEY ((customerId, macAddress), equipmentId)
) WITH comment='Wireless client sessions used by TIP WLAN CloudSDK';

CREATE INDEX IF NOT EXISTS idx_client_session_customerId ON tip_wlan_keyspace.client_session (customerId);

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session_by_equipment (
    customerId int,
    equipmentId bigint,    
    macAddress bigint ,
    
    PRIMARY KEY ((customerId, equipmentId), macAddress)
) WITH comment='Index Table to look up wireless client sessions by equipmentId used by TIP WLAN CloudSDK';

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session_by_location (
    customerId int,
    locationId bigint,
    equipmentId bigint,    
    macAddress bigint ,
    
    PRIMARY KEY ((locationId), equipmentId, macAddress)
) WITH comment='Index Table to look up wireless client sessions by location and equipment used by TIP WLAN CloudSDK';

