create table if not exists status (
    -- postgresql     
    customerId int not null,
    equipmentId bigint not null,
    statusDataType int not null,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
    
    primary key (customerId, equipmentId, statusDataType)
);

create index if not exists idx_status_customerId on status (customerId);
create index if not exists idx_status_equipmentId on status (equipmentId);
create index if not exists idx_status_customerEquipmentDatatype on status (customerId, equipmentId, statusDataType);


