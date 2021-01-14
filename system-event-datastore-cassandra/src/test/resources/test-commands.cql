use tip_wlan_keyspace;


-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event
--------------------------------------------------------------------------------

insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 1, 197, 1, 'dt_1', 1594837187000, textAsBlob('test1_1') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 1, 197, 1, 'dt_1', 1594837188000, textAsBlob('test1_2') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 1, 197, 1, 'dt_1', 1594837189000, textAsBlob('test1_3') );

insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 1, 'dt_1', 1594837187000, textAsBlob('test2_1') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 1, 'dt_1', 1594837188000, textAsBlob('test2_2') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 1, 'dt_1', 1594837189000, textAsBlob('test2_3') );

insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 2, 'dt_1', 1594837187000, textAsBlob('test2_2_1') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 2, 'dt_1', 1594837188000, textAsBlob('test2_2_2') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 2, 'dt_1', 1594837189000, textAsBlob('test2_2_3') );

insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 2, 'dt_2', 1594837187000, textAsBlob('test2_2_2_1') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 2, 'dt_2', 1594837188000, textAsBlob('test2_2_2_2') );
insert into wlan_system_event (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp, details) values ( 1, 2, 197, 2, 'dt_2', 1594837189000, textAsBlob('test2_2_2_3') );

select * from wlan_system_event ;

-- no need for this one - we are relying on the default_time_to_live configured on the table
-- delete from wlan_system_event where eventTimestamp < 1594837188000;

-- find out distinct dayOfYear values for the delete operation
-- since the delete operation is not performance-sensitive (used only in tests) we can use allow filtering here
select dayOfYear, clientMac, dataType from wlan_system_event where customerId = 1 and equipmentId  = 1  and eventTimestamp < 1594837188000 allow filtering;
delete from wlan_system_event where customerId = 1 and equipmentId  = 1 and dayOfYear in (1,2,3,4,5,365) and clientMac in (1,2,3) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;

-- This query is used to retrieve a page of the results when navigating through one of the index tables
select * from wlan_system_event where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and clientMac in (1,2,3) and eventTimestamp in (1594837187000, 1594837188000 , 1594837189000) and dataType in ('dt_1', 'dt_2', 'dt_3');

--
-- This is the most efficient way to query service metrics - when all the filters are provided.
-- No need to use the index tables for it. 
-- dayOfYear collection can be computed from the supplied fromTime and toTime
--
select * from wlan_system_event where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and clientMac in (1,2,3) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_customer
--------------------------------------------------------------------------------

insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_customer (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_customer ;

select * from wlan_system_event_by_customer where customerId = 1 and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, eventTimestamp from wlan_system_event_by_customer where customerId = 1 and eventTimestamp < 1594837188000;
delete from wlan_system_event_by_customer where customerId = 1 and equipmentId =1 and eventTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_equipment
--------------------------------------------------------------------------------

insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_equipment ;

select * from wlan_system_event_by_equipment where customerId = 1 and equipmentId in (1, 2) and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

delete from wlan_system_event_by_equipment where customerId = 1 and equipmentId  = 1 and eventTimestamp < 1594837188000;

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_mac
--------------------------------------------------------------------------------

insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_mac (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_mac ;

select * from wlan_system_event_by_mac where customerId = 1 and clientMac in (1, 2) and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac, eventTimestamp from wlan_system_event_by_mac where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
delete from wlan_system_event_by_mac where customerId = 1  and clientMac in (1, 2) and equipmentId = 1 and  eventTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_mac_and_equipment
--------------------------------------------------------------------------------

insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_mac_and_equipment ;

select * from wlan_system_event_by_mac_and_equipment where customerId = 1 and clientMac in (1, 2) and equipmentId in (1, 4) and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac from wlan_system_event_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
delete from wlan_system_event_by_mac_and_equipment where customerId = 1 and equipmentId = 1 and clientMac in (1, 2) and  eventTimestamp < 1594837188000;

================================================================================

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_customer_datatype
--------------------------------------------------------------------------------

insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_customer_datatype ;

select * from wlan_system_event_by_customer_datatype where customerId = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, dataType, eventTimestamp from wlan_system_event_by_customer_datatype where customerId = 1;
delete from wlan_system_event_by_customer_datatype where customerId = 1 and equipmentId = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_equipment_datatype
--------------------------------------------------------------------------------

insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_equipment_datatype ;

select * from wlan_system_event_by_equipment_datatype where customerId = 1 and equipmentId in (1, 2) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, dataType from wlan_system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1;
delete from wlan_system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;


-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_mac_datatype
--------------------------------------------------------------------------------

insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_mac_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_mac_datatype ;

select * from wlan_system_event_by_mac_datatype where customerId = 1 and clientMac in (1, 2) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac, dataType, eventTimestamp from wlan_system_event_by_mac_datatype where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
delete from wlan_system_event_by_mac_datatype where customerId = 1  and clientMac in (1, 2) and equipmentId = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and  eventTimestamp in (1594837188000, 1594837189000);



-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_mac_and_equipment_datatype
--------------------------------------------------------------------------------

insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_mac_and_equipment_datatype (customerId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_mac_and_equipment_datatype ;

select * from wlan_system_event_by_mac_and_equipment_datatype where customerId = 1 and clientMac in (1, 2) and equipmentId in (1, 4) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer as equipmentId is not available on this table
-- not performance-efficient, but this query is used only in the tests, so it's ok
select clientMac, dataType from wlan_system_event_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837188000 allow filtering;
delete from wlan_system_event_by_mac_and_equipment_datatype where customerId = 1 and equipmentId = 1 and clientMac in (1, 2)  and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_location
--------------------------------------------------------------------------------

insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_location (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_location ;

select * from wlan_system_event_by_location where customerId = 1 and locationId in (1, 2) and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

delete from wlan_system_event_by_location where customerId = 1 and locationId  = 1 and eventTimestamp < 1594837188000;

-------------------------------------------------------------------------------- 
-- Operations on wlan_system_event_by_location_all_filters
--------------------------------------------------------------------------------

insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 1, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 1, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 1, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 2, 197, 1, 'dt_1', 1594837187000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 2, 197, 1, 'dt_1', 1594837188000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 1, 2, 197, 1, 'dt_1', 1594837189000);

insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_1', 1594837187000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_1', 1594837188000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_1', 1594837189000);

insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_2', 1594837187000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_2', 1594837188000);
insert into wlan_system_event_by_location_all_filters (customerId, locationId, equipmentId, dayOfYear, clientMac, dataType, eventTimestamp) values ( 1, 2, 2, 197, 2, 'dt_2', 1594837189000);

select * from wlan_system_event_by_location_all_filters ;

--this does not work - all columns need to be provided for filtering
--select * from wlan_system_event_by_location_all_filters where customerId = 1 and locationId in (1, 2) and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;


select * from wlan_system_event_by_location_all_filters where customerId = 1 and locationId in (1, 2)  and equipmentId in (1, 2)  and clientMac in (1, 2) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- not performance-efficient, but this query is used only in the tests, so it's ok

select * from wlan_system_event_by_location_all_filters where customerId = 1 and equipmentId = 1 and eventTimestamp < 1594837189000 allow filtering;

delete  from wlan_system_event_by_location_all_filters where customerId = 1 and locationId in (1, 2)  and equipmentId in (1, 2)  and clientMac in (1, 2) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;

-------------------------------------------------------------------------------- 
-- Commands to drop the tables 
-------------------------------------------------------------------------------- 

drop table wlan_system_event;
drop table wlan_system_event_by_customer;
drop table wlan_system_event_by_equipment;
drop table wlan_system_event_by_mac;
drop table wlan_system_event_by_mac_and_equipment;
drop table wlan_system_event_by_customer_datatype;
drop table wlan_system_event_by_equipment_datatype;
drop table wlan_system_event_by_mac_datatype;
drop table wlan_system_event_by_mac_and_equipment_datatype;
drop table wlan_system_event_by_location;
drop table wlan_system_event_by_location_all_filters;

-------------------------------------------------------------------------------- 
-- Commands to clean up after the test 
-------------------------------------------------------------------------------- 

TRUNCATE TABLE wlan_system_event ;
TRUNCATE TABLE wlan_system_event_by_customer ;
TRUNCATE TABLE wlan_system_event_by_equipment ;
TRUNCATE TABLE wlan_system_event_by_mac ;
TRUNCATE TABLE wlan_system_event_by_mac_and_equipment;
TRUNCATE TABLE wlan_system_event_by_customer_datatype ;
TRUNCATE TABLE wlan_system_event_by_equipment_datatype ;
TRUNCATE TABLE wlan_system_event_by_mac_datatype ;
TRUNCATE TABLE wlan_system_event_by_mac_and_equipment_datatype ;
TRUNCATE TABLE wlan_system_event_by_location;
TRUNCATE TABLE wlan_system_event_by_location_all_filters;
