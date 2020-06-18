create table if not exists client (
    -- postgresql     
    macAddress bigint ,

    customerId int,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, macAddress)
);

create index if not exists idx_client_customerId on client (customerId);

create table if not exists client_session (
    -- postgresql     
    macAddress bigint ,

    customerId int,
    equipmentId bigint,
    locationId bigint,
    details bytea,
    
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, equipmentId, macAddress)
);

create index if not exists idx_clientSession_customerId on client_session (customerId);
create index if not exists idx_clientSession_locationId on client_session (customerId, locationId);

