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

create index client_customerId on client (customerId);

create table client_session (
    -- postgresql     
    macAddress bigint ,

    customerId int,
    equipmentId bigint,
    details varbinary(65535),
    
    lastModifiedTimestamp bigint not null,
  
    primary key (customerId, equipmentId, macAddress)
);

create index clientSession_customerId on client (customerId);

