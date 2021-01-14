drop table system_event if exists;

create table system_event (
     -- hsqldb 

    customerId int,
    locationId bigint not null,    
    equipmentId bigint not null,
    clientMac bigint not null,    
    dataType varchar(100) not null,
    eventTimestamp bigint not null,
    
    details varbinary(65535),
    
    primary key (customerId, locationId, equipmentId, clientMac, dataType, eventTimestamp)
  
);

create index idx_system_event_customerId on system_event (customerId);
create index idx_system_event_customerEquipmentDatatype on system_event (customerId, equipmentId, dataType);
