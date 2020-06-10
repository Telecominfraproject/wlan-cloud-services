create table profile (
    -- postgresql     
    id BIGSERIAL PRIMARY KEY,

    customerId int,
    profileType int not null,
    name varchar(255),
    
    details bytea,
    
    createdTimestamp bigint not null,
    lastModifiedTimestamp bigint not null
  
);

create index idx_profile_customerId on profile (customerId);

create table profile_map (
    customerId int,
    parentProfileId bigint, 
    childProfileId bigint,
    
    FOREIGN KEY (parentProfileId) REFERENCES profile(id) ON DELETE CASCADE,
    FOREIGN KEY (childProfileId) REFERENCES profile(id) ON DELETE CASCADE    
);


create index idx_profile_map_customerId on profile_map (customerId);
create index idx_profile_map_customerId_parent on profile_map (customerId, parentProfileId);
create index idx_profile_map_parent on profile_map (parentProfileId);
create index idx_profile_map_child on profile_map (childProfileId);