create table client (
    -- postgresql     
    macAddress bigint ,

    customerId int,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, macAddress)
);

create index client_customerId on client (customerId);

create table client_session (
    -- postgresql     
    macAddress bigint ,

    customerId int,
    equipmentId bigint,
    details bytea,
    
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, equipmentId, macAddress)
);

create index clientSession_customerId on client (customerId);

