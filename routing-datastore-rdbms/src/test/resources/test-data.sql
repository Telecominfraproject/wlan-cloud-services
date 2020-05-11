insert into equipment_gateway (
    id,
    hostname,     
    ipAddr,
    port,
    createdTimestamp,
    lastModifiedTimestamp
) values (
    1,
    'test',
    '127.0.0.1',
    40000,
    0,0
);

insert into equipment_routing (
    id,
    equipmentId,     
    customerId,
    gatewayId,
    createdTimestamp,
    lastModifiedTimestamp
) values (
    1,
    1,
    1,
    1,
    0,0
);

