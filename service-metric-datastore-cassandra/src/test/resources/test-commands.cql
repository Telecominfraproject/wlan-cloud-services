use tip_wlan_keyspace;

-------------------------------------------------------------------------------- 
-- Operations on service_metric
--------------------------------------------------------------------------------

insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 1, 197, 1, 1, 1594837187000, textAsBlob('test1_1') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 1, 197, 1, 1, 1594837188000, textAsBlob('test1_2') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 1, 197, 1, 1, 1594837189000, textAsBlob('test1_3') );

insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 1, 1, 1594837187000, textAsBlob('test2_1') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 1, 1, 1594837188000, textAsBlob('test2_2') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 1, 1, 1594837189000, textAsBlob('test2_3') );

insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 2, 1, 1594837187000, textAsBlob('test2_2_1') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 2, 1, 1594837188000, textAsBlob('test2_2_2') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 2, 1, 1594837189000, textAsBlob('test2_2_3') );

insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 2, 2, 1594837187000, textAsBlob('test2_2_2_1') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 2, 2, 1594837188000, textAsBlob('test2_2_2_2') );
insert into service_metric (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp, details) values ( 1, 2, 197, 2, 2, 1594837189000, textAsBlob('test2_2_2_3') );

select * from service_metric ;

-- no need for this one - we are relying on the default_time_to_live configured on the table
-- delete from service_metric where createdTimestamp < 1594837188000;

-- find out distinct dayOfYear values for the delete operation
-- since the delete operation is not performance-sensitive (used only in tests) we can use allow filtering here
select dayOfYear, clientMac, dataType from service_metric where customerId = 1 and equipmentId  = 1  and createdTimestamp < 1594837188000 allow filtering;
delete from service_metric where customerId = 1 and equipmentId  = 1 and dayOfYear in (1,2,3,4,5,365) and clientMac in (1,2,3) and dataType in (1,2,3) and createdTimestamp < 1594837188000;

-- This query is used to retrieve a page of the results when navigating through one of the index tables
select * from service_metric where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and clientMac in (1,2,3) and createdTimestamp in (1594837187000, 1594837188000 , 1594837189000) and dataType in (1,2,3);

--
-- This is the most efficient way to query service metrics - when all the filters are provided.
-- No need to use the index tables for it. 
-- dayOfYear collection can be computed from the supplied fromTime and toTime
--
select * from service_metric where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and clientMac in (1,2,3) and dataType in (1,2,3) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_customer
--------------------------------------------------------------------------------

insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_customer ;

select * from service_metric_by_customer where customerId = 1 and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, createdTimestamp from service_metric_by_customer where customerId = 1 and createdTimestamp < 1594837188000;
delete from service_metric_by_customer where customerId = 1 and equipmentId =1 and createdTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_equipment
--------------------------------------------------------------------------------

insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_equipment ;

select * from service_metric_by_equipment where customerId = 1 and equipmentId in (1, 2) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

delete from service_metric_by_equipment where customerId = 1 and equipmentId  = 1 and createdTimestamp < 1594837188000;

-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_mac
--------------------------------------------------------------------------------

insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_mac ;

select * from service_metric_by_mac where customerId = 1 and clientMac in (1, 2) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac, createdTimestamp from service_metric_by_mac where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
delete from service_metric_by_mac where customerId = 1  and clientMac in (1, 2) and equipmentId = 1 and  createdTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_mac_and_equipment
--------------------------------------------------------------------------------

insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_mac_and_equipment ;

select * from service_metric_by_mac_and_equipment where customerId = 1 and clientMac in (1, 2) and equipmentId in (1, 4) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac from service_metric_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
delete from service_metric_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and clientMac in (1, 2) and  createdTimestamp < 1594837188000;

================================================================================

-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_customer_datatype
--------------------------------------------------------------------------------

insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_customer_datatype ;

select * from service_metric_by_customer_datatype where customerId = 1 and dataType in (1,2,3) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, dataType, createdTimestamp from service_metric_by_customer_datatype where customerId = 1;
delete from service_metric_by_customer_datatype where customerId = 1 and equipmentId = 1 and dataType in (1,2,3) and createdTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_equipment_datatype
--------------------------------------------------------------------------------

insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_equipment_datatype ;

select * from service_metric_by_equipment_datatype where customerId = 1 and equipmentId in (1, 2) and dataType in (1,2,3) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, dataType from service_metric_by_equipment_datatype where customerId = 1 and equipmentId  = 1;
delete from service_metric_by_equipment_datatype where customerId = 1 and equipmentId  = 1 and dataType in (1,2,3) and createdTimestamp < 1594837188000;


-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_mac_datatype
--------------------------------------------------------------------------------

insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_mac_datatype ;

select * from service_metric_by_mac_datatype where customerId = 1 and clientMac in (1, 2) and dataType in (1,2,3) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac, dataType, createdTimestamp from service_metric_by_mac_datatype where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
delete from service_metric_by_mac_datatype where customerId = 1  and clientMac in (1, 2) and equipmentId = 1 and dataType in (1,2,3) and  createdTimestamp in (1594837188000, 1594837189000);



-------------------------------------------------------------------------------- 
-- Operations on service_metric_by_mac_and_equipment_datatype
--------------------------------------------------------------------------------

insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 1, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837187000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837188000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 1, 1, 1594837189000);

insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837187000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837188000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 1, 1594837189000);

insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837187000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837188000);
insert into service_metric_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, createdTimestamp) values ( 1, 2, 197, 2, 2, 1594837189000);

select * from service_metric_by_mac_and_equipment_datatype ;

select * from service_metric_by_mac_and_equipment_datatype where customerId = 1 and clientMac in (1, 2) and equipmentId in (1, 4) and dataType in (1,2,3) and createdTimestamp >= 1594837187000 and createdTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac, dataType from service_metric_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1 and createdTimestamp < 1594837188000 allow filtering;
delete from service_metric_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1 and clientMac in (1, 2)  and dataType in (1,2,3) and createdTimestamp < 1594837188000;


-------------------------------------------------------------------------------- 
-- Commands to drop the tables 
-------------------------------------------------------------------------------- 

drop table service_metric;
drop table service_metric_by_customer;
drop table service_metric_by_equipment;
drop table service_metric_by_mac;
drop table service_metric_by_mac_and_equipment;
drop table service_metric_by_customer_datatype;
drop table service_metric_by_equipment_datatype;
drop table service_metric_by_mac_datatype;
drop table service_metric_by_mac_and_equipment_datatype;


-------------------------------------------------------------------------------- 
-- Commands to clean up after the test 
-------------------------------------------------------------------------------- 

TRUNCATE TABLE service_metric ;
TRUNCATE TABLE service_metric_by_customer ;
TRUNCATE TABLE service_metric_by_equipment ;
TRUNCATE TABLE service_metric_by_mac ;
TRUNCATE TABLE service_metric_by_mac_and_equipment;
TRUNCATE TABLE service_metric_by_customer_datatype ;
TRUNCATE TABLE service_metric_by_equipment_datatype ;
TRUNCATE TABLE service_metric_by_mac_datatype ;
TRUNCATE TABLE service_metric_by_mac_and_equipment_datatype ;


