
create table if not exists manufacturer_details(
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    manufacturerName varchar(128) unique not null,
    manufacturerAlias varchar(20),
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
);

create index if not exists idx_manufacturer_alias on manufacturer_details (manufacturerAlias); 


create table if not exists manufacturer_oui(
    -- postgresql     
    oui varchar(6) PRIMARY KEY,

    manufacturerDetails bigint not null,

    FOREIGN KEY(manufacturerDetails) REFERENCES manufacturer_details(id) ON DELETE CASCADE
);

create index if not exists idx_oui_manufacturer_details on manufacturer_oui (manufacturerDetails);  
