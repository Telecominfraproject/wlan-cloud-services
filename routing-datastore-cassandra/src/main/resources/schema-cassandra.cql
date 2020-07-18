-- to populate ids we can use the following: 
-- (System.currentTimeMillis() << 20) | (System.nanoTime() & ~0b1111111111111111111111111111111111111111111100000000000000000000L )

create table if not exists tip_wlan_keyspace.equipment_gateway (

    id bigint,

    hostname varchar,
    ipAddr varchar,
    port int,
    gatewayType int,

    createdTimestamp bigint,
    lastModifiedTimestamp bigint,
    
    PRIMARY KEY (id)
) WITH comment='Registered Equipment gateways';

CREATE INDEX IF NOT EXISTS idx_equipment_gateway_hostname ON tip_wlan_keyspace.equipment_gateway (hostname);
CREATE INDEX IF NOT EXISTS idx_equipment_gateway_gatewayType ON tip_wlan_keyspace.equipment_gateway (gatewayType);


create table if not exists tip_wlan_keyspace.equipment_routing (
    id bigint,

    equipmentId bigint,
    customerId int,
    gatewayId bigint,

    createdTimestamp bigint,
    lastModifiedTimestamp bigint,
    
    PRIMARY KEY (id)        
) WITH comment='Registered Equipment routes';

CREATE INDEX IF NOT EXISTS idx_equipment_routing_customerId ON tip_wlan_keyspace.equipment_routing (customerId);
CREATE INDEX IF NOT EXISTS idx_equipment_routing_equipmentId ON tip_wlan_keyspace.equipment_routing (equipmentId);

