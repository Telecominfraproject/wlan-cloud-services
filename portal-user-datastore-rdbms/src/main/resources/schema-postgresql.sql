create table if not exists portal_user (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    username varchar(100),
    password varchar(300),
    role int,
    roles varchar(600),
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index if not exists idx_portal_user_customerId on portal_user (customerId);
create unique index if not exists portal_user_customerId_username on portal_user (customerId, username);

alter table portal_user add column IF NOT EXISTS roles varchar(600);
update portal_user set roles = '[' || role || ']' where roles is null;