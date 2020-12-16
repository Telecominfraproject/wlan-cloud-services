create table if not exists equipment_location (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    -- 1==site, 2==building, 3==floor
    locationType int,
    customerId int,
    name varchar(100),
    parentId bigint,
    details varchar(65535),

    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null,

    detailsBin bytea,

    FOREIGN KEY (parentId) REFERENCES equipment_location(id) ON DELETE RESTRICT
);

create index if not exists idx_equipment_location_customerId on equipment_location (customerId);
create index if not exists idx_equipment_location_parentId on equipment_location (parentId);

ALTER TABLE equipment_location ADD COLUMN IF NOT EXISTS detailsBin bytea;
