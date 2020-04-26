create table routing (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    sampleStr varchar(50),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index routing_customerId on routing (customerId);


