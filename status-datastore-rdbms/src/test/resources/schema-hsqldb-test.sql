drop table status if exists;

create table status (
     -- hsqldb 
    customerId int not null,
    equipmentId bigint not null,
    statusDataType int not null,
    details varbinary(65535),
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,
    
    primary key (customerId, equipmentId, statusDataType)
);

create index status_customerId on status (customerId);
create index status_equipmentId on status (equipmentId);
create index status_customerEquipmentDatatype on status (customerId, equipmentId, statusDataType);


