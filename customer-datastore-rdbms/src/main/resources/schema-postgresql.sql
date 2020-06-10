create table customer_info (
    -- postgresql     
    id    SERIAL PRIMARY KEY,

    email varchar(320) ,
    name varchar(100),

    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
);

create UNIQUE INDEX idx_customer_info_email on customer_info (email);
