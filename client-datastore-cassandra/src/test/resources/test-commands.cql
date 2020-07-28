use tip_wlan_keyspace;

-------------------------------------------------------------------------------- 
-- Operations on client
--------------------------------------------------------------------------------

insert into client (customerId, macAddress, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 1, 1594837187000, 1594837187000, textAsBlob('test1') );
insert into client (customerId, macAddress, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 2, 1594837187000, 1594837187000, textAsBlob('test2') );
insert into client (customerId, macAddress, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 3, 1594837187000, 1594837187000, textAsBlob('test3') );

select * from client where customerId = 1 and macAddress = 1;

select * from client where customerId = 1 and macAddress in (1,2);

select * from client where customerId = 1;


update client set details=textAsBlob('test1_u'), lastModifiedTimestamp = 1594837187001 where customerId = 1 and macAddress = 1 IF lastModifiedTimestamp = 1594837187000 ;

select lastModifiedTimestamp from client where customerId = 1 and macAddress = 1;
            
delete from client where customerId = 1 and macAddress = 1;


-------------------------------------------------------------------------------- 
-- Operations on client_blocklist
--------------------------------------------------------------------------------
insert into client_blocklist (customerId, macAddress) values ( 1, 1);
insert into client_blocklist (customerId, macAddress) values ( 1, 2);
insert into client_blocklist (customerId, macAddress) values ( 1, 3);

select * from client_blocklist ;

select customerId, macAddress from client_blocklist where customerId = 1 ;

delete from client_blocklist where customerId = 1 and  macAddress = 2;


-------------------------------------------------------------------------------- 
-- Operations on client_session
--------------------------------------------------------------------------------


insert into client_session(customerId, equipmentId, macAddress, locationId, lastModifiedTimestamp, details) values ( 1, 1, 1, 1, 1594837187000, textAsBlob('test1_1') );
insert into client_session(customerId, equipmentId, macAddress, locationId, lastModifiedTimestamp, details) values ( 1, 2, 1, 1, 1594837187000, textAsBlob('test1_2') );
insert into client_session(customerId, equipmentId, macAddress, locationId, lastModifiedTimestamp, details) values ( 1, 1, 2, 1, 1594837187000, textAsBlob('test2_1') );
insert into client_session(customerId, equipmentId, macAddress, locationId, lastModifiedTimestamp, details) values ( 1, 1, 3, 2, 1594837187000, textAsBlob('test3_1') );

select * from client_session;

select * from client_session where customerId = 1 and equipmentId = 1 and macAddress = 1;

update client_session set locationId = 2, details = textAsBlob('test_1_1_upd'), lastModifiedTimestamp = 1594837187001 where  customerId = 1 and equipmentId = 1 and macAddress = 1 IF locationId = 1;

-- if a record with this key but with a different locationId exists, the result set returns applied = false and existing value for the locationId, update fails, need to repeat without IF condition (or insert) 
 [applied] | locationid
-----------+------------
     False |          2

-- if a record with this key and the same locationId exists, the result set returns applied = true, update succeeds 
 [applied]
-----------
      True

-- if a record with this key does not exist, the result set returns applied = false, update fails, need to repeat without IF condition (or insert)
 [applied]
-----------
     False


delete from client_session where customerId = 1 and equipmentId = 2 and macAddress = 1;

select * from client_session where customerId = 1 and macAddress in (1,2,3);

select * from client_session where customerId = 1;

-- Index table for looking up sessions by equipment:
-- insert into it when client_session record is inserted
-- delete from it when client_session record is deleted
-- no update

insert into client_session_by_equipment(customerId, equipmentId, macAddress) values ( 1, 1, 1);
insert into client_session_by_equipment(customerId, equipmentId, macAddress) values ( 1, 2, 1);
insert into client_session_by_equipment(customerId, equipmentId, macAddress) values ( 1, 1, 2);
insert into client_session_by_equipment(customerId, equipmentId, macAddress) values ( 1, 1, 3);

select * from client_session_by_equipment;

delete from client_session_by_equipment where customerId = 1 and equipmentId = 2 and macAddress = 1;

select * from client_session_by_equipment where customerId = 1 and equipmentId in (1,2,3); 

select customerId, equipmentId, macAddress from client_session_by_equipment where customerId = 1 and equipmentId in (1,2,3);

-- Index table for looking up sessions by location and equipment:
-- insert into it when client_session record is inserted
-- delete from it when client_session record is deleted (get location from old client_session record)
-- when locationId in the client_session record is updated - delete old index record and create new index record

insert into client_session_by_location(customerId, locationId, equipmentId, macAddress) values ( 1, 1, 1, 1);
insert into client_session_by_location(customerId, locationId, equipmentId, macAddress) values ( 1, 1, 2, 1);
insert into client_session_by_location(customerId, locationId, equipmentId, macAddress) values ( 1, 1, 1, 2);
insert into client_session_by_location(customerId, locationId, equipmentId, macAddress) values ( 1, 2, 1, 3);

select * from client_session_by_location;

delete from client_session_by_location where locationId = 1 and equipmentId = 2 and macAddress = 1;

select * from client_session_by_location where locationId in (1,2,3);

select customerId, locationId, equipmentId, macAddress from client_session_by_location where locationId in (1,2,3);

select * from client_session_by_location where locationId in (1,2,3) and equipmentId in (1,2,3);

select customerId, locationId, equipmentId, macAddress from client_session_by_location where locationId in (1,2,3) and equipmentId in (1,2,3);


-------------------------------------------------------------------------------- 
-- Commands to clean up after the test 
-------------------------------------------------------------------------------- 

 TRUNCATE TABLE client_session_by_location ;
 TRUNCATE TABLE client_session_by_equipment ;
 TRUNCATE TABLE client_session;
 TRUNCATE TABLE client;