create table if not exists  equipment (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    profileId bigint,
    locationId bigint,
    equipmentType int not null,
    inventoryId varchar(100) unique not null,
    
    name varchar(50),
    details bytea,
    --TODO: investigate how to use point datatype here - point('35.21076593772987','11.22855348629825')
    --see https://stackoverflow.com/questions/21368385/regarding-storing-lat-lng-coordinates-in-postgresql-column-type
    latitude varchar(250),
    longitude varchar(250),
    baseMacAddress varchar(18),
    manufacturerOui varchar(6),
    serial varchar(100), 
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index if not exists idx_equipment_customerId on equipment (customerId);
create index if not exists idx_equipment_customerId_name on equipment (customerId, name);
create index if not exists idx_equipment_customerId_equipmentType on equipment (customerId, equipmentType);
create index if not exists idx_equipment_customerId_baseMacAddress on equipment (customerId, baseMacAddress);
create index if not exists idx_equipment_customerId_manufacturerOui on equipment (customerId, manufacturerOui);
create index if not exists idx_equipment_profileId on equipment (profileId);
create index if not exists idx_equipment_inventoryId on equipment (inventoryId);
create index if not exists idx_equipment_locationId on equipment (locationId);


