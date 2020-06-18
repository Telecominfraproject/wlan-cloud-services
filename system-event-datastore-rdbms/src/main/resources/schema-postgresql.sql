create table if not exists system_event (
    -- postgresql     

    customerId int,
    equipmentId bigint not null,
    dataType varchar(100) not null,
    eventTimestamp bigint not null,
    
    details bytea,
    
    primary key (customerId, equipmentId, dataType, eventTimestamp)
  
);

create index if not exists idx_system_event_customerId on system_event (customerId);
create index if not exists idx_system_event_customerEquipmentDatatype on system_event (customerId, equipmentId, dataType);


