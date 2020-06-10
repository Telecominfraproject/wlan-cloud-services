create table equipment_location (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    -- 1==site, 2==building, 3==floor
    locationType int,
    customerId int,
    name varchar(100),
    parentId bigint,
    details varchar(65535),
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
);

create index idx_equipment_location_customerId on equipment_location (customerId);
create index idx_equipment_location_parentId on equipment_location (parentId);
