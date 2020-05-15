create table status (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int not null,
    equipmentId bigint,
    statusDataType int not null,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index status_customerId on status (customerId);
create index status_equipmentId on status (equipmentId);
create index status_customerEquipmentDatatype on status (customerId, equipmentId, statusDataType);


