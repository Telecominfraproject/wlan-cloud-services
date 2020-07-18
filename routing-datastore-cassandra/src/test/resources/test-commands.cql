use tip_wlan_keyspace;

-- 
-- Operations on equipment_gateway
--

insert into equipment_gateway (id, hostname, ipAddr, port, gatewayType, createdTimestamp, lastModifiedTimestamp) values ( 1, 'localhost-1', '127.0.0.1', 8080, 1, 1594837187000, 1594837187000);
insert into equipment_gateway (id, hostname, ipAddr, port, gatewayType, createdTimestamp, lastModifiedTimestamp) values ( 2, 'localhost-2', '127.0.0.2', 8080, 1, 1594837187000, 1594837187000);
insert into equipment_gateway (id, hostname, ipAddr, port, gatewayType, createdTimestamp, lastModifiedTimestamp) values ( 3, 'localhost-3', '127.0.0.3', 8080, 1, 1594837187000, 1594837187000);
insert into equipment_gateway (id, hostname, ipAddr, port, gatewayType, createdTimestamp, lastModifiedTimestamp) values ( 4, 'localhost-1', '127.0.0.1', 8081, 2, 1594837187000, 1594837187000);

select * from equipment_gateway where id = 1;

select * from equipment_gateway where hostname = 'localhost-2';

select * from equipment_gateway where gatewayType = 1;

select * from equipment_gateway where id in (1,2);

update equipment_gateway set hostname='localhost-1-u', ipAddr = '127.0.0.1', port = 8084, gatewayType = 1, lastModifiedTimestamp = 1594837187001 where  id = 1 IF lastModifiedTimestamp = 1594837187000 ;

select lastModifiedTimestamp from equipment_gateway where id = 1;
            
delete from equipment_gateway where id = 1;

select id from equipment_gateway where hostname = 'localhost-1';
delete from equipment_gateway where id in (1, 4);


-- 
-- Operations on equipment_routing
--

insert into equipment_routing (id, equipmentId, customerId, gatewayId, createdTimestamp, lastModifiedTimestamp) values (1, 1, 1, 1, 1594837187000, 1594837187000);
insert into equipment_routing (id, equipmentId, customerId, gatewayId, createdTimestamp, lastModifiedTimestamp) values (2, 2, 1, 1, 1594837187000, 1594837187000);
insert into equipment_routing (id, equipmentId, customerId, gatewayId, createdTimestamp, lastModifiedTimestamp) values (3, 3, 1, 1, 1594837187000, 1594837187000);
insert into equipment_routing (id, equipmentId, customerId, gatewayId, createdTimestamp, lastModifiedTimestamp) values (4, 4, 1, 1, 1594837187000, 1594837187000);

insert into equipment_routing (id, equipmentId, customerId, gatewayId, createdTimestamp, lastModifiedTimestamp) values (5, 1, 1, 2, 1594837187000, 1594837187000);
insert into equipment_routing (id, equipmentId, customerId, gatewayId, createdTimestamp, lastModifiedTimestamp) values (6, 1, 2, 3, 1594837187000, 1594837187000);


select * from equipment_routing where id = 1;

update equipment_routing set equipmentId = 2, customerId = 3, gatewayId = 4, lastModifiedTimestamp = 1594837187001 where  id = 1 IF lastModifiedTimestamp = 1594837187000 ;

select lastModifiedTimestamp from equipment_routing where id = 1;

select * from equipment_routing where id in (1,2,3);

delete from equipment_routing where id = 1;

select * from equipment_routing where customerId = 1;

select * from equipment_routing where equipmentId = 1;
