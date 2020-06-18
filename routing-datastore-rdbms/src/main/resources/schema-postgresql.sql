
create table if not exists equipment_gateway (
    -- postgresql
    id BIGSERIAL PRIMARY KEY,

    hostname varchar(80) not null,
    ipAddr varchar(50) not null,
    port integer not null,
    gatewayType integer default 1 not null,

    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null

);

create index if not exists idx_equipment_gateway_hostname on equipment_gateway (hostname);

create table if not exists equipment_routing (
    -- postgresql
    id BIGSERIAL PRIMARY KEY,

    equipmentId bigint not null,
    customerId integer,
    gatewayId bigint not null,

    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,

    FOREIGN KEY (gatewayId) REFERENCES equipment_gateway(id) ON delete CASCADE
    
);


create index if not exists idx_equipment_routing_customerId on equipment_routing (customerId);
create index if not exists idx_equipment_routing_equipmentId on equipment_routing (equipmentId);
create index if not exists idx_equipment_routing_gatewayId on equipment_routing (gatewayId);

