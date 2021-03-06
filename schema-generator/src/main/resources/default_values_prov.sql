-- CREATING DEFAULT VALUES IN THE TABLES FOR POSTGRES

-- Create Default Customer
INSERT INTO "customer_info" (id, email, name, details, createdTimestamp, lastModifiedTimestamp)
values(2, 'tip-customer@tip.org', 'TIP Customer Inc.', decode('504b0304140008080800c2a4ff5000000000000000000000000001000000615d4f3d0bc23010fd2f375768ab15c9166a870e4240c141445273d5409ad4f62288f4bf9b561d74ba77efde07f784c62934277ab4080c72df936bb05b23496d7a88407a72a27377dd6b67b5bd007bfe5a8a9bd76d8396f89f728b44618c216865655001a3ce6304c69d250551199855b87e1382bbd6064b25b0db8c1d63d9ae14272e80651114f93e5b2431b0e51ba74932612ed234fdd07c358fe359cedf86699ba0c25a7a43010f430495b416557e1d4778931d8ec30b504b0708be151e61b70000000a010000504b01021400140008080800c2a4ff50be151e61b70000000a01000001000000000000000000000000000000000061504b050600000000010001002f000000e60000000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Create Default Locations
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (1, 1, 2, 'Ottawa', null, 0,0) ON CONFLICT (id) DO NOTHING;
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (2, 2, 2, 'TipBuilding', 1, 0,0) ON CONFLICT (id) DO NOTHING;
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (3, 3, 2, 'FirstFloor', 2, 0,0) ON CONFLICT (id) DO NOTHING;
insert into equipment_location (id, locationType, customerId, name, parentId, createdTimestamp, lastModifiedTimestamp)
values (8, 1, 2, 'Toronto', null, 0,0) ON CONFLICT (id) DO NOTHING;

-- Default Portal User
-- testuser@tip.com/tippassword - role: CustomerIT
INSERT INTO portal_user (id, customerId, username, password, role, details, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 2, 'testuser@tip.com', 'qzsJoR0IfoDMlnpetYIC9I5onT5naYUg1azRtvBXxCdHJTcFfkbZeHiE712LfkNZmbG5zV1l1IEzh6nh7pbvh.', 1, null, 0, 0) ON CONFLICT (id) DO NOTHING;

-- superuser@tip.com/superpassword - role: SuperUser
INSERT INTO portal_user (id, customerId, username, password, role, details, createdTimestamp, lastModifiedTimestamp)
VALUES (2, 2, 'superuser@tip.com', 'Fxheoz7xKpgatnx5rjYTSDfrK2RkrSrkMhWEcuXKxOtYGtu3KDRW1SdmL0j1/aPGo2zhvnmqnxtEYULM1gGFq.', 2147483647, null, 0, 0) ON CONFLICT (id) DO NOTHING;

-- Creating Radius Profile
INSERT INTO "profile" (id, customerId, profileType, name, details, createdTimestamp, lastModifiedTimestamp)
VALUES(9, 2, 5, 'Radius-Profile', decode('504b03041400080808003aa82e5100000000000000000000000001000000617551c14ec3300cfd179fbb6a29088dde1007b403506d880b42285bdc2e529a544eca364dfd77d2a55dabb1de92f7fcecf7ec139446a0fa71c70a21851517b2b619995c2a84086cbdd1e89e8dce65511377d268484fe0d0ba59e0daef8d16eb1bc2bedd931084d6fa5af698c4ec6111b3f93c66c338292823cce501d2e4be47df78d9b61f8f8ea022733886291346b2a162f0912be39fba5856172fba566a44e46313d7dccb7e52f6a9b8ee51bbe384628d5b6ad7d462cdd9f10697da21fd7275a9f43fb9c51516de5f17f4dd39bee7d03457f42bafdaa81d3db1fdb1004203a44e194a66fd91d3afc916485e2babfff7eaaed5253bdfc4a767c99d4779ed7699218fb3054b2270b24453f70bf8f671682266151c7d041b74b601cd1f504b07085faf75621c010000a2020000504b010214001400080808003aa82e515faf75621c010000a202000001000000000000000000000000000000000061504b050600000000010001002f0000004b0100000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Creating TipWlan-cloud-wifi Profile
INSERT INTO "profile" (id, customerId, profileType, name, details, createdTimestamp, lastModifiedTimestamp)
VALUES(4, 2, 3, 'TipWlan-Cloud-Wifi', decode('504b03041400080808002b03f7500000000000000000000000000100000061bd934d4f23310c86ff4bce53a91f8bb4ea8d05894514b6eab4da0342c83371c09089a32433a32ee2bfafa7a1d04a9c7bf33c76fcbe1e276faa618df6316d3daab92a23e90b76869eda0089d8a9424561925a93ff6bc18d6acbad1ef5644872e0bd25d42bd0c451cdef15c5b3abdfff249383c567b4d94553cde9c750f090fb9eeb865c99200de2e8a0b2a80749acdb80b7e24c70ef61fac7d9edb2bc9154271eaec5cfa450afb82d53182a26616667318e4c98cdf4b891b22a30e81a622ab3fbafde726a8526607c56f371a11c2fb8065bb695c3242318b0110b1564a23696183aaaf10e1a31e25a6b0b55834fd4e19243023b18c9b802a77bd2e979410da54beeddaefb31def81dec4823af031843f530d89128ff82881f3b103b6f873f4dbe8eb6b5faacff6e6f79e49fe3e96412f6360fd8eb37accbec7dbfb4d32b6e4e2fb9389da46856ec5eb80d5772e57bd82e031bb2f8758f0e0ef57bd6a3cf1ef6c070e821e88ff77177be16573e775ae709766ff6fd3f504b07084c340c7a75010000dd030000504b010214001400080808002b03f7504c340c7a75010000dd03000001000000000000000000000000000000000061504b050600000000010001002f000000a40100000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Creating TipWlan-3-radios Profile
INSERT INTO "profile" (id, customerId, profileType, name, details, createdTimestamp, lastModifiedTimestamp)
VALUES(5, 2, 1, 'TipWlan-3-Radios', decode('504b03041400080808007481f5500000000000000000000000000100000061b591c16ec2301044ffc5672a410597dc104505a9404468afc89005ac1aafbb5ea74a11ffde0d41110a270edcacb7eb99b1e7a48e98835d73e941256ae8e7c0bf48df23743bb38fa4d9a0531de56ef11750a8b0eca72f3d99c24f34fe088e575799546061b59bcbfd420853849aa8a42b6aec33a0024825a75680c8b8a0997651db8cc9b8bd2869812ad9691b2a116da36cba68edb9a342192cee97607579a795ddcc24a3d31b0b79a313a89860e0a9afb51a9022b14a06bdbe109088864559cd17abe968acc491d8860c98255ab8b35cde0e1ff5ec5edee3f8006cb6236be43fc72d01394b054c689b49fdb54d036f266c516297cd1dd2b9c199f655581306ef93bffbd8d54a4ab83316dacd6f20f0d0b7ec2e30cd1880ae956f900fd5f798f09a23f79fee52bde3f3f9161fcfb3100f5fcb5c71d3e15aca3aff03504b07089a19fe2b3b0100009a030000504b010214001400080808007481f5509a19fe2b3b0100009a03000001000000000000000000000000000000000061504b050600000000010001002f0000006a0100000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Creating TipWlan-2-radios Profile
INSERT INTO "profile" (id, customerId, profileType, name, details, createdTimestamp, lastModifiedTimestamp)
VALUES(6, 2, 1, 'TipWlan-2-Radios', decode('504b0304140008080800ae10f7500000000000000000000000000100000061b591c16ec2301044ffc5672a410597dc104505a9404468afc89005ac2eb6bb5ea74a11ffde0d41110a270edcacb7eb99b1e7a48e2e075c73e941256ae8e7c0bf8ebe47ceeecc3e9266e3acea287b8bbf804285653f7de9c9147ea2f147b0bcbacaa4020bd4762ef70b214c116aa292aea8b1cf800a20959c5a0122bb05cdb48d1a332663f7a2a405361a1aa32cda8878eea8500674fb25a02eefa4b29b9944b47a8390ab64a731884ea062e2024f7dadd580d411ab64d0eb0b0149685894d57cb19a8ec64a1c893164c02cc9c29de5f276f8a867f7f21ecb0760b31da191ef1cb704e42c0d30396c26f5af3405bc99b07512bb6cee90ce8d9b695f853561f03ef9bb8f5dada4e47606a15dfc06020f7dcbee02d38c01e8daf8c6f1a1fa1e135e73c7fda7bb54eff87cbec5c7f32cc4c3d73257dc74b896b2ceff504b07083eb7508c3a01000099030000504b01021400140008080800ae10f7503eb7508c3a0100009903000001000000000000000000000000000000000061504b050600000000010001002f000000690100000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Creating Captive Portal Profile
INSERT INTO "profile" (id, customerId, profileType, name, details, createdTimestamp, lastModifiedTimestamp)
VALUES(8, 2, 6, 'Captive-Portal', decode('504b030414000808080087b2f65000000000000000000000000001000000615d93418fda301085ff8a95f3165555db0337845aba12a06801eda1aad0ac3d09168e27b2c71ba2d5fef78e1328d9dec0f3fccdf39bc95bd1904177e4bec5625e2ca165fb8a250506b7245fd93a05604bbe78283c3413cda77610c9f94ba02e62d85b76b9bed01a63547c42e5913b0a670551ad124616f109c1601034a3e73b4d95375a125446b40c5e8b1367752fba434441da7867b2ea2905459d57c1c6f34c6d4975100278ee15550a7cafced69b9940631a4c9550e306c2d9c8a53d5e72ff67749a1a61d3d4b15c09686c40cd87a7b5c8e440f418bcc432cd68a8fae49cf4900612d4de3648891ffdc6fac4188bf9f7cf0f85a39a7eda9ccfdbc7c437e0c594c9b5475f91f469cc377d427d8ea9b9a11d44de90b1954593f991a1696f45687f5cf22c0ec1094f1e207fd7d26ed6fa5a70959097c05853e8ff9f70965d25fbd14eb95dc90138de3992782a7011df65c4a0cf75a0e4cdf888b17307cea1594130e817ce51e7accc78fefbcf38c5bc2f25c428817eb8064992f66cf5b058d7c6f5753f02189be242241be413996cf9d7a2ccb9c0459620c467cba79da09732a18c118bc5fceb97fb80c69d9976bcdb2f29da619de785c38a8f4c6d31ad3f618b90f7c2d3318cbf6f9e76185eadc6edf0158c5cbcb436f4d71724ef6c6319cd7589d7ffc268400ff1dc4fda4093d0f53893e3f58b7aff0b504b070818521290f701000095030000504b0102140014000808080087b2f65018521290f70100009503000001000000000000000000000000000000000061504b050600000000010001002f000000260200000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Creating RF Profile
INSERT INTO "profile" (id, customerId, profileType, name, details, createdTimestamp, lastModifiedTimestamp)
VALUES(10, 2, 9, 'TipWlan-rf', decode('504b03041400080808007c9e55510000000000000000000000000100000061ed564d6f1a3110fd2f3e1389926c9a7283253448402320c93132bb038ce41d6f6d6fc887f8ef1d9b85842ce925952ab57b41ace7ebcd9bf1935f44a65350f7ee2907d1169345ac6981cbc248879a444398f2642473d17e1168a3ef57cffedfbbb84b051990ab84cb14f56ceb54c69687238ee7439fa6138742fc35c3fc4e493ae18f8698834c340dc88179904ab4bf349b0db1d026816922a95718a4e5adc6c4a749d1cab982d42772367676b63260575aa5a27d1e45a75143242b4904aa2b295d63ea5601d1457314206598ed10b9b5ee3ecdd6da9fcac77191c50ab9335b029085d3f136d5141424a1d1f6422a0b6ce4cf87006f0ace313e5ba1aa53756908a02dfab63305a7b16ced1bf85900254f536016d2507e6be995f48e5029b4bebf4d4310e07235d78194215ad7c9b7a3a8d41fbf71ec5c7bd7f733cb90a6b8244ff9c9451458e8e45ca7e5ebb0b1e31900a5a6f8cc094fced925078319f0a07afc1328b93ceca824ff4ae71f1213575db82d8d16fa4a6bb31fe8807a5d2efb353a6a9d318e01bd7276d1f46e74870b7c93e11a788bc8c925178e8e781ccba2e7d632b8724bc63fc6f77783fe406cfca27abef71d53a1141355288789b46e229d0ff07b13568ab8aabf2a87e7e67147696fcedd7de382b9d1739880cd3559d843db9b93b0963db40c9298f4aa07a0c9678fd77a0dc6f7b003fae10426fe5e760f7d18b18a759617ee7598a9d139b343e62d8da7cdb01c432dd33e5f03cd25cf9a1b66076d2bd5eeec13b2b18faf4ac7f8af2947ab568e5a396ae5f803cad1aa2a47542a877f30dc7ce6b571533f376ad1a845e3df138ddf3c37fcbd1f7e463486b568d4a2518bc67f231a9bd0c702159432c0577bf30b504b07087bbc5b02750200001a110000504b010214001400080808007c9e55517bbc5b02750200001a11000001000000000000000000000000000000000061504b050600000000010001002f000000a40200000000', 'hex'),
0, 0) ON CONFLICT (id) DO NOTHING;

-- Create Parent child relationship between profiles
INSERT INTO profile_map (customerId, parentProfileId, childProfileId)
SELECT 2, 5, 4
WHERE
 NOT EXISTS (
    SELECT parentProfileId, childProfileId from profile_map where parentProfileId = 5 and childProfileId = 4
 );

  -- adding relationship between equipment-ap and rf profile
INSERT INTO profile_map (customerId, parentProfileId, childProfileId)
SELECT 2, 5, 10
WHERE
 NOT EXISTS (
    SELECT parentProfileId, childProfileId from profile_map where parentProfileId = 5 and childProfileId = 10
 );
    -- adding relationship between equipment-ap and rf profile
INSERT INTO profile_map (customerId, parentProfileId, childProfileId)
SELECT 2, 6, 10
WHERE
 NOT EXISTS (
    SELECT parentProfileId, childProfileId from profile_map where parentProfileId = 6 and childProfileId = 10
 );
 
     -- adding relationship between equipment-ap and rf profile
INSERT INTO profile_map (customerId, parentProfileId, childProfileId)
SELECT 2, 6, 4
WHERE
 NOT EXISTS (
    SELECT parentProfileId, childProfileId from profile_map where parentProfileId = 6 and childProfileId = 4
 );

-- Create Firmware Version related entities
--INSERT INTO firmware_version (id, equipmentType, modelId, versionName, commitTag, description, filename, validationMethod, validationCode, releaseDate, createdTimestamp, lastModifiedTimestamp)
--VALUES (1, 1, 'ap2220', 'ap2220-2020-06-25-ce03472', 'ce03472', '', 'https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ap2220/ap2220-2020-06-25-ce03472.tar.gz',
-- 1, 'c69370aa5b6622d91a0fba3a5441f31c', EXTRACT(EPOCH FROM TIMESTAMP '2020-07-31 10:40:28.876944') * 1000, 0, 0 ) ON CONFLICT (id) DO NOTHING;

-- Only run the update clause if created at time 0; i.e.; it's created from here
INSERT INTO firmware_version (id, equipmentType, modelId, versionName, commitTag, description, filename, validationMethod, validationCode, releaseDate, createdTimestamp, lastModifiedTimestamp)
VALUES (2, 1, 'EA8300', 'ea8300-2020-08-18-pending-5d9ea41', '5d9ea41', 'EA8300 Firmware Version', 'https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ea8300/dev/ea8300-2020-08-18-pending-5d9ea41.tar.gz',
 1, '19494befa87eb6bb90a64fd515634263', EXTRACT(EPOCH FROM TIMESTAMP '2020-08-18 06:00:00') * 1000, 0, 0 ) ON CONFLICT (id) DO UPDATE
 SET equipmentType=1, modelId='EA8300', commitTag='5d9ea41', validationCode='19494befa87eb6bb90a64fd515634263',
 description='EA8300 Firmware Version', filename='https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ea8300/dev/ea8300-2020-08-18-pending-5d9ea41.tar.gz',
 releaseDate= EXTRACT(EPOCH FROM TIMESTAMP '2020-08-18 06:00:00') * 1000
 WHERE firmware_version.createdTimestamp=0;

--INSERT INTO firmware_version (id, equipmentType, modelId, versionName, commitTag, description, filename, validationMethod, validationCode, releaseDate, createdTimestamp, lastModifiedTimestamp)
--VALUES (3, 1, 'ea8300-ca', 'ea8300-2020-06-25-ce03472-ca', 'ce03472', '', 'https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ea8300/ea8300-2020-06-25-ce03472.tar.gz',
-- 1, 'b209deb9847bdf40a31e45edf2e5a8d7', EXTRACT(EPOCH FROM TIMESTAMP '2020-07-31 10:40:28.876944') * 1000, 0, 0 ) ON CONFLICT (id) DO NOTHING;

-- Only run the update clause if created at time 0; i.e.; it's created from here
INSERT INTO firmware_version (id, equipmentType, modelId, versionName, commitTag, description, filename, validationMethod, validationCode, releaseDate, createdTimestamp, lastModifiedTimestamp)
VALUES (4, 1, 'ECW5211', 'ecw5211-2020-08-18-pending-5d9ea41', '5d9ea41', 'ECW5211 Firmware Version', 'https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ecw5211/dev/ecw5211-2020-08-18-pending-5d9ea41.tar.gz',
 1, '1ab76e9adb33bf96276f3f346049e1a4d65952b4', EXTRACT(EPOCH FROM TIMESTAMP '2020-08-18 06:00:00') * 1000, 0, 0 ) ON CONFLICT (id) DO UPDATE
 SET equipmentType=1, modelId='ECW5211', versionName='ecw5211-2020-08-18-pending-5d9ea41', commitTag='5d9ea41', validationCode='1ab76e9adb33bf96276f3f346049e1a4d65952b4',
 description='ECW5211 Firmware Version', filename='https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ecw5211/dev/ecw5211-2020-08-18-pending-5d9ea41.tar.gz',
 releaseDate= EXTRACT(EPOCH FROM TIMESTAMP '2020-08-18 06:00:00') * 1000
 WHERE firmware_version.createdTimestamp=0;

-- Only run the update clause if created at time 0; i.e.; it's created from here
INSERT INTO firmware_version (id, equipmentType, modelId, versionName, commitTag, description, filename, validationMethod, validationCode, releaseDate, createdTimestamp, lastModifiedTimestamp)
VALUES (5, 1, 'ECW5410', 'ecw5410-2020-08-18-pending-175558b', '175558b', 'ECW5410 Firmware Version', 'https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ecw5410/dev/ecw5410-2020-08-18-pending-175558b.tar.gz',
 1, '5d944896e54f96422dd0da921d4dd310', EXTRACT(EPOCH FROM TIMESTAMP '2020-08-18 06:00:00') * 1000, 0, 0 ) ON CONFLICT (id) DO UPDATE
 SET equipmentType=1, modelId='ECW5410', versionName='ecw5410-2020-08-18-pending-175558b', commitTag='175558b', validationCode='5d944896e54f96422dd0da921d4dd310',
 description='ECW5410 Firmware Version', filename='https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ecw5410/dev/ecw5410-2020-08-18-pending-175558b.tar.gz',
 releaseDate= EXTRACT(EPOCH FROM TIMESTAMP '2020-08-18 06:00:00') * 1000
 WHERE firmware_version.createdTimestamp=0;

-- Create Firmware Track Assignment related entities
INSERT INTO firmware_track_assignment (trackId, firmwareId, defaultForTrack, deprecated, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 1, false, false, 0, 0) ON CONFLICT (trackId, firmwareId) DO NOTHING;

INSERT INTO firmware_track_assignment (trackId, firmwareId, defaultForTrack, deprecated, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 2, false, false, 0, 0) ON CONFLICT (trackId, firmwareId) DO NOTHING;

INSERT INTO firmware_track_assignment (trackId, firmwareId, defaultForTrack, deprecated, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 3, false, false, 0, 0) ON CONFLICT (trackId, firmwareId) DO NOTHING;

INSERT INTO firmware_track_assignment (trackId, firmwareId, defaultForTrack, deprecated, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 4, false, false, 0, 0) ON CONFLICT (trackId, firmwareId) DO NOTHING;

INSERT INTO firmware_track_assignment (trackId, firmwareId, defaultForTrack, deprecated, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 5, false, false, 0, 0) ON CONFLICT (trackId, firmwareId) DO NOTHING;

-- Create Customer Track Assignment entities
INSERT INTO customer_track_assignment (trackId, customerId, settings, createdTimestamp, lastModifiedTimestamp)
VALUES (1, 2, '{"autoUpgradeDeprecatedDuringMaintenance": "DEFAULT","autoUpgradeDeprecatedOnBind": "NEVER","autoUpgradeUnknownDuringMaintenance": "DEFAULT","autoUpgradeUnknownOnBind": "DEFAULT"}', 0, 0) ON CONFLICT (customerId) DO NOTHING;
