drop table service_metric if exists;

create table service_metric (
     -- hsqldb 
  
    customerId int,
    equipmentId bigint not null,
    clientMac bigint not null,
    dataType int not null,
    createdTimestamp bigint not null,
    
    details varbinary(65535),
    
    primary key (customerId, equipmentId, clientMac, dataType, createdTimestamp)
  
);

create index idx_service_metric_customerId on service_metric (customerId);
create index idx_service_metric_customerEquipmentDatatype on service_metric (customerId, equipmentId, dataType);
