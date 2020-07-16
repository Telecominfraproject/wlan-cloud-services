insert into customer_info (id, email, name, createdTimestamp, lastModifiedTimestamp)
values (2, 'tip-customer@tip.org', 'TIP Customer Inc.', 0,0) ON CONFLICT (id) DO NOTHING;

insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (1, 1, 2, 'Ottawa', null, 0,0) ON CONFLICT (id) DO NOTHING;
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (2, 2, 2, 'TipBuilding', 1, 0,0) ON CONFLICT (id) DO NOTHING;
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (3, 3, 2, 'FirstFloor', 2, 0,0) ON CONFLICT (id) DO NOTHING;
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (8, 1, 2, 'Toronto', null, 0,0) ON CONFLICT (id) DO NOTHING;