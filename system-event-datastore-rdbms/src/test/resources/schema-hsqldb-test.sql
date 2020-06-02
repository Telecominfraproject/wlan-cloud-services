drop table system_event if exists;

create table system_event (
     -- hsqldb 

    customerId int,
    equipmentId bigint not null,
    dataType varchar(100) not null,
    eventTimestamp bigint not null,
    
    details varbinary(65535),
    
    primary key (customerId, equipmentId, dataType, eventTimestamp)
  
);

create index system_event_customerId on system_event (customerId);
create index system_event_customerEquipmentDatatype on system_event (customerId, equipmentId, dataType);
