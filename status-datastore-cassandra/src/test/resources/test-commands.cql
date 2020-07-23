use tip_wlan_keyspace;

-------------------------------------------------------------------------------- 
-- Operations on status
--------------------------------------------------------------------------------

insert into status (customerId, equipmentId, statusDataType, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 1, 1, 1594837187000, 1594837187000, textAsBlob('test1') );
insert into status (customerId, equipmentId, statusDataType, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 1, 2, 1594837187000, 1594837187000, textAsBlob('test1_2') );
insert into status (customerId, equipmentId, statusDataType, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 2, 1, 1594837187000, 1594837187000, textAsBlob('test2') );
insert into status (customerId, equipmentId, statusDataType, createdTimestamp, lastModifiedTimestamp, details) values ( 1, 3, 1, 1594837187000, 1594837187000, textAsBlob('test3') );

select * from status where customerId = 1;

select * from status where customerId = 1 and equipmentId in (1,2);

select * from status where customerId = 1 and equipmentId in (1,2) and statusDataType in(1,2) ;

select * from status where customerId = 1 and statusDataType in(1,2) ;


update status set details=textAsBlob('test1_u'), lastModifiedTimestamp = 1594837187001 where customerId = 1 and equipmentId = 1 and statusDataType = 1 IF lastModifiedTimestamp = 1594837187000 ;

select lastModifiedTimestamp from status where customerId = 1 and equipmentId = 1 and statusDataType = 1;
            
delete from status where customerId = 1 and equipmentId = 1 and statusDataType = 1;

select customerId, equipmentId, statusDataType from status where customerId = 1 and equipmentId = 1 ;

-------------------------------------------------------------------------------- 
-- Operations on client_blocklist
--------------------------------------------------------------------------------
insert into status_by_datatype (customerId, equipmentId, statusDataType) values ( 1, 1, 1 );
insert into status_by_datatype (customerId, equipmentId, statusDataType) values ( 1, 1, 2 );
insert into status_by_datatype (customerId, equipmentId, statusDataType) values ( 1, 2, 1 );
insert into status_by_datatype (customerId, equipmentId, statusDataType) values ( 1, 3, 1 );

select * from status_by_datatype ;

select * from status_by_datatype where customerId = 1 and statusDataType in(1,2) ;

delete from status_by_datatype where customerId = 1 and equipmentId = 1 and statusDataType = 1;

delete from status_by_datatype where customerId = 1 and equipmentId in (1,2,3) and statusDataType in (1,2,3);


-------------------------------------------------------------------------------- 
-- Commands to clean up after the test 
-------------------------------------------------------------------------------- 

 TRUNCATE TABLE status;
 TRUNCATE TABLE status_by_datatype;
