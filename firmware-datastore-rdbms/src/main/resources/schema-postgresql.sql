create table firmware (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    sampleStr varchar(50),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index firmware_customerId on firmware (customerId);


