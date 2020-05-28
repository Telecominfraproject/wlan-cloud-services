drop table alarm if exists;

create table alarm (
    customerId int not null,
    equipmentId bigint default 0 not null,
    alarmCode int not null,
    createdTimestamp bigint not null,
    
    originatorType int not null,
    severity int not null,
    scopeType int not null,
    scopeId varchar(255) not null,
    details varbinary(65535),
    acknowledged boolean,
    
    lastModifiedTimestamp bigint not null,
    
    primary key (customerId, equipmentId, alarmCode, createdTimestamp)
  
);

create index alarm_customerId on alarm (customerId);
create index alarm_equipmentId on alarm (equipmentId );

