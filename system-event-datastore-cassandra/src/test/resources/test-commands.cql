use tip_wlan_keyspace;

-------------------------------------------------------------------------------- 
-- Operations on system_event
--------------------------------------------------------------------------------

insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 1, 197, 'dt_1', 1594837187000, textAsBlob('test1_1') );
insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 1, 197, 'dt_1', 1594837188000, textAsBlob('test1_2') );
insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 1, 197, 'dt_1', 1594837189000, textAsBlob('test1_3') );

insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 2, 197, 'dt_1', 1594837187000, textAsBlob('test2_1') );
insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 2, 197, 'dt_1', 1594837188000, textAsBlob('test2_2') );
insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 2, 197, 'dt_1', 1594837189000, textAsBlob('test2_3') );

insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 2, 197, 'dt_2', 1594837187000, textAsBlob('test2_2_2_1') );
insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 2, 197, 'dt_2', 1594837188000, textAsBlob('test2_2_2_2') );
insert into system_event (customerId, equipmentId, dayOfYear, dataType, eventTimestamp, details) values ( 1, 2, 197, 'dt_2', 1594837189000, textAsBlob('test2_2_2_3') );

select * from system_event ;

-- no need for this one - we are relying on the default_time_to_live configured on the table
-- delete from system_event where eventTimestamp < 1594837188000;

-- find out distinct dayOfYear values for the delete operation
-- since the delete operation is not performance-sensitive (used only in tests) we can use allow filtering here
select dayOfYear, dataType from system_event where customerId = 1 and equipmentId  = 1  and eventTimestamp < 1594837188000 allow filtering;
delete from system_event where customerId = 1 and equipmentId  = 1 and dayOfYear in (1,2,3,4,5,365) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;

-- This query is used to retrieve a page of the results when navigating through one of the index tables
select * from system_event where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and eventTimestamp in (1594837187000, 1594837188000 , 1594837189000) and dataType in ('dt_1', 'dt_2', 'dt_3');

--
-- This is the most efficient way to query system events - when all the filters are provided.
-- No need to use the index tables for it. 
-- dayOfYear collection can be computed from the supplied fromTime and toTime
--
select * from system_event where customerId = 1 and equipmentId  in (1,2,3) and dayOfYear in (1,2,197) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-------------------------------------------------------------------------------- 
-- Operations on system_event_by_customer
--------------------------------------------------------------------------------

insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837187000);
insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837188000);
insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837189000);

insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837187000);
insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837188000);
insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837189000);

insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837187000);
insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837188000);
insert into system_event_by_customer (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837189000);

select * from system_event_by_customer ;

select * from system_event_by_customer where customerId = 1 and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, eventTimestamp from system_event_by_customer where customerId = 1 and eventTimestamp < 1594837188000;
delete from system_event_by_customer where customerId = 1 and equipmentId =1 and eventTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on system_event_by_equipment
--------------------------------------------------------------------------------

insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837187000);
insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837188000);
insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837189000);

insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837187000);
insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837188000);
insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837189000);

insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837187000);
insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837188000);
insert into system_event_by_equipment (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837189000);

select * from system_event_by_equipment ;

select * from system_event_by_equipment where customerId = 1 and equipmentId in (1, 2) and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

delete from system_event_by_equipment where customerId = 1 and equipmentId  = 1 and eventTimestamp < 1594837188000;

================================================================================

-------------------------------------------------------------------------------- 
-- Operations on system_event_by_customer_datatype
--------------------------------------------------------------------------------

insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837187000);
insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837188000);
insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837189000);

insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837187000);
insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837188000);
insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837189000);

insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837187000);
insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837188000);
insert into system_event_by_customer_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837189000);

select * from system_event_by_customer_datatype ;

select * from system_event_by_customer_datatype where customerId = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, dataType, eventTimestamp from system_event_by_customer_datatype where customerId = 1;
delete from system_event_by_customer_datatype where customerId = 1 and equipmentId = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp in (1594837188000, 1594837189000);

-------------------------------------------------------------------------------- 
-- Operations on system_event_by_equipment_datatype
--------------------------------------------------------------------------------

insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837187000);
insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837188000);
insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 1, 197, 'dt_1', 1594837189000);

insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837187000);
insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837188000);
insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_1', 1594837189000);

insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837187000);
insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837188000);
insert into system_event_by_equipment_datatype (customerId, equipmentId, dayOfYear, dataType, eventTimestamp) values ( 1, 2, 197, 'dt_2', 1594837189000);

select * from system_event_by_equipment_datatype ;

select * from system_event_by_equipment_datatype where customerId = 1 and equipmentId in (1, 2) and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp >= 1594837187000 and eventTimestamp <= 1594837189000;

-- will remove all data for customer and equipmentId 
-- not performance-efficient, but this query is used only in the tests, so it's ok
select equipmentId, dataType from system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1;
delete from system_event_by_equipment_datatype where customerId = 1 and equipmentId  = 1 and dataType in ('dt_1', 'dt_2', 'dt_3') and eventTimestamp < 1594837188000;



-------------------------------------------------------------------------------- 
-- Commands to drop the tables 
-------------------------------------------------------------------------------- 

drop table system_event;
drop table system_event_by_customer;
drop table system_event_by_equipment;
drop table system_event_by_customer_datatype;
drop table system_event_by_equipment_datatype;


-------------------------------------------------------------------------------- 
-- Commands to clean up after the test 
-------------------------------------------------------------------------------- 

TRUNCATE TABLE system_event ;
TRUNCATE TABLE system_event_by_customer ;
TRUNCATE TABLE system_event_by_equipment ;
TRUNCATE TABLE system_event_by_customer_datatype ;
TRUNCATE TABLE system_event_by_equipment_datatype ;


