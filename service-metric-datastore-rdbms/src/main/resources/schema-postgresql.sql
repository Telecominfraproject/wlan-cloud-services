create table service_metric (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    sampleStr varchar(50),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index service_metric_customerId on service_metric (customerId);


