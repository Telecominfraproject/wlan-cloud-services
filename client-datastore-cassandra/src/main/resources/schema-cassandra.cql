CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client (
    customerId int,
    macAddress bigint ,

    createdTimestamp bigint,
    lastModifiedTimestamp bigint,

    details blob,
    
    PRIMARY KEY ((customerId, macAddress))
) WITH comment='Wireless client details used by TIP WLAN CloudSDK';

CREATE INDEX IF NOT EXISTS idx_client_customerId ON tip_wlan_keyspace.client (customerId);

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_blocklist (
    customerId int,
    macAddress bigint,    
    PRIMARY KEY ((customerId), macAddress)
) WITH comment='Wireless client blocklist by MAC used by TIP WLAN CloudSDK';

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_by_mac_string (
    customerId int,
    macAddress bigint,
    macAddressString text,    
    
    PRIMARY KEY (customerId, macAddressString)
) WITH comment='Index Table to look up wireless client details by macAddress in text form used by TIP WLAN CloudSDK';

DROP INDEX IF EXISTS tip_wlan_keyspace.idx_client_macAddressString;
CREATE CUSTOM INDEX IF NOT EXISTS idx_client_macAddressString_nonTokenizing ON tip_wlan_keyspace.client_by_mac_string (macAddressString) USING 'org.apache.cassandra.index.sasi.SASIIndex' WITH OPTIONS = {'mode': 'CONTAINS', 'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer', 'case_sensitive': 'false'};


CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session (
    customerId int,
    equipmentId bigint,    
    macAddress bigint ,

    locationId bigint,

    lastModifiedTimestamp bigint,

    details blob,
    
    PRIMARY KEY ((customerId, macAddress), equipmentId)
) WITH comment='Wireless client sessions used by TIP WLAN CloudSDK. Records automatically expire after 24 hours'
    AND default_time_to_live = 86400;

CREATE INDEX IF NOT EXISTS idx_client_session_customerId ON tip_wlan_keyspace.client_session (customerId);

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session_by_equipment (
    customerId int,
    equipmentId bigint,    
    macAddress bigint ,
    
    PRIMARY KEY ((customerId, equipmentId), macAddress)
) WITH comment='Index Table to look up wireless client sessions by equipmentId used by TIP WLAN CloudSDK. Records automatically expire after 24 hours'
    AND default_time_to_live = 86400;

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session_by_location (
    customerId int,
    locationId bigint,
    equipmentId bigint,    
    macAddress bigint,
    
    PRIMARY KEY ((locationId), equipmentId, macAddress)
) WITH comment='Index Table to look up wireless client sessions by location and equipment used by TIP WLAN CloudSDK. Records automatically expire after 24 hours'
    AND default_time_to_live = 86400;

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session_by_mac (
    customerId int,
    locationId bigint,
    equipmentId bigint,    
    macAddress bigint,
    macAddressString text,
    
    PRIMARY KEY ((macAddress), locationId, equipmentId, macAddressString)
) WITH comment='Index Table to look up wireless client sessions by macAddressString, location, and equipment by TIP WLAN CloudSDK. Records automatically expire after 24 hours'
    AND default_time_to_live = 86400;

CREATE TABLE IF NOT EXISTS tip_wlan_keyspace.client_session_by_mac_and_equipment (
    customerId int,
    locationId bigint,
    equipmentId bigint,    
    macAddress bigint,
    macAddressString text,
    
    PRIMARY KEY ((macAddress), equipmentId, locationId, macAddressString)
) WITH comment='Index Table to look up wireless client sessions by macAddressString, location, and equipment by TIP WLAN CloudSDK. Records automatically expire after 24 hours'
    AND default_time_to_live = 86400;

DROP INDEX IF EXISTS tip_wlan_keyspace.idx_client_session_by_mac_macAddressString;
DROP INDEX IF EXISTS tip_wlan_keyspace.idx_client_session_by_mac_and_equipment_macAddressString;

CREATE CUSTOM INDEX IF NOT EXISTS idx_client_session_by_mac_macAddressString_nonTokenizing ON tip_wlan_keyspace.client_session_by_mac (macAddressString) USING 'org.apache.cassandra.index.sasi.SASIIndex' WITH OPTIONS = {'mode': 'CONTAINS', 'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer', 'case_sensitive': 'false'};
CREATE CUSTOM INDEX IF NOT EXISTS idx_client_session_by_mac_and_equipment_macAddressString_nonTokenizing ON tip_wlan_keyspace.client_session_by_mac_and_equipment (macAddressString) USING 'org.apache.cassandra.index.sasi.SASIIndex' WITH OPTIONS = {'mode': 'CONTAINS', 'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer', 'case_sensitive': 'false'};
