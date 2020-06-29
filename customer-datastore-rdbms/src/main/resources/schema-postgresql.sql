create table if not exists customer_info (
    -- postgresql     
    id    SERIAL PRIMARY KEY,

    email varchar(320) ,
    name varchar(100),
    
    details bytea,

    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
);

create UNIQUE INDEX if not exists idx_customer_info_email on customer_info (email);
