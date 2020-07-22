drop table client if exists;
drop table client_session if exists;

create table client (
     -- hsqldb 
    macAddress bigint,

    customerId int,
    details varbinary(65535),
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, macAddress)
);

create index idx_client_customerId on client (customerId);

create table if not exists client_blocklist (
    -- hsqldb     
    customerId int,
    macAddress bigint ,
  
    primary key (customerId, macAddress),
    FOREIGN KEY (customerId, macAddress) REFERENCES client(customerId, macAddress) ON DELETE CASCADE
);

create table client_session (
    -- postgresql     
    macAddress bigint ,

    customerId int,
    equipmentId bigint,
    locationId bigint,
    details varbinary(65535),
    
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, equipmentId, macAddress)
);

create index idx_clientSession_customerId on client_session (customerId);
create index idx_clientSession_locationId on client_session (customerId, locationId);

