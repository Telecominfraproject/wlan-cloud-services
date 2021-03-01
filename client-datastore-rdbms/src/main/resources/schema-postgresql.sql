create table if not exists client (
    -- postgresql     
    macAddress bigint ,
    macAddressString varchar(100) ,

    customerId int,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, macAddress)
);

alter table client add column if not exists macAddressString varchar(100);

create index if not exists idx_client_customerId on client (customerId);
create index if not exists idx_client_customerId_macAddressString on client (customerId, macAddressString);

create table if not exists client_blocklist (
    -- postgresql     
    customerId int,
    macAddress bigint ,
  
    primary key (customerId, macAddress),
    FOREIGN KEY (customerId, macAddress) REFERENCES client(customerId, macAddress) ON DELETE CASCADE
);

create table if not exists client_session (
    -- postgresql     
    macAddress bigint ,
    macAddressString varchar(100) ,

    customerId int,
    equipmentId bigint,
    locationId bigint,
    details bytea,
    
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, equipmentId, macAddress)
);

alter table client_session add column if not exists macAddressString varchar(100);

create index if not exists idx_clientSession_customerId on client_session (customerId);
create index if not exists idx_clientSession_locationId on client_session (customerId, locationId);
create index if not exists idx_clientSession_customerId_macAddressString on client_session (customerId, macAddressString);


