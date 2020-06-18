create table if not exists portal_user (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    username varchar(100),
    password varchar(300),
    role int,
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index if not exists idx_portal_user_customerId on portal_user (customerId);
create unique index if not exists portal_user_customerId_username on portal_user (customerId, username);


