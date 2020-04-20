create table equipment (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    name varchar(50),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index equipment_customerId on equipment (customerId);


