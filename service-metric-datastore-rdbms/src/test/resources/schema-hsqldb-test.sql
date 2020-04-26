drop table service_metric if exists;

create table service_metric (
     -- hsqldb 
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,

    customerId int,
    sampleStr varchar(50),
    details varbinary(65535),
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index service_metric_customerId on service_metric (customerId);
