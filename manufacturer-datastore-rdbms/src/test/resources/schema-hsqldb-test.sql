drop table manufacturer_oui if exists;
drop table manufacturer_details if exists;

create table manufacturer_details(
    -- hsqldb     
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,

    manufacturerName varchar(128) unique not null,
    manufacturerAlias varchar(20),
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
);

create index idx_manufacturer_alias on manufacturer_details (manufacturerAlias); 


create table manufacturer_oui(
    oui varchar(6) PRIMARY KEY,

    manufacturerDetails bigint not null,
    
    FOREIGN KEY(manufacturerDetails) REFERENCES manufacturer_details(id) ON DELETE CASCADE
);

create index idx_oui_manufacturer_details on manufacturer_oui (manufacturerDetails);  


