create table if not exists system_event (
    -- postgresql     

    customerId int,
    locationId bigint not null,    
    equipmentId bigint not null,
    clientMac bigint not null,    
    dataType varchar(100) not null,
    eventTimestamp bigint not null,
    
    details bytea,
    
    primary key (customerId, locationId, equipmentId, clientMac, dataType, eventTimestamp)
  
);

create index if not exists idx_system_event_customerId on system_event (customerId);
create index if not exists idx_system_event_customerEquipmentDatatype on system_event (customerId, equipmentId, dataType);


