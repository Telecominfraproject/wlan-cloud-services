create table status (
    -- postgresql     
    customerId int not null,
    equipmentId bigint not null,
    statusDataType int not null,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
    
    primary key (customerId, equipmentId, statusDataType)
);

create index idx_status_customerId on status (customerId);
create index idx_status_equipmentId on status (equipmentId);
create index idx_status_customerEquipmentDatatype on status (customerId, equipmentId, statusDataType);


