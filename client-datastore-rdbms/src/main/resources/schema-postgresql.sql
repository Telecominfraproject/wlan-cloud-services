create table client (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    sampleStr varchar(50),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index client_customerId on client (customerId);


