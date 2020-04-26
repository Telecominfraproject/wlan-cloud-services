create table equipment (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    profileId bigint,
    locationId bigint,
    equipmentType int,
    inventoryId varchar(100) unique not null,
    
    name varchar(50),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index equipment_customerId on equipment (customerId);
create index equipment_customerId_equipmentType on equipment (customerId, equipmentType);  
create index equipment_profileId on equipment (profileId);  
create index equipment_inventoryId on equipment (inventoryId);
create index equipment_location on equipment (locationId);


