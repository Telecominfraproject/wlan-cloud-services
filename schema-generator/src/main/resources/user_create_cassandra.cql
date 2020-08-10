CREATE KEYSPACE IF NOT EXISTS tip_wlan_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

USE tip_wlan_keyspace;

CREATE ROLE IF NOT EXISTS tip_user
WITH SUPERUSER = true
AND LOGIN = true
AND PASSWORD =  'tip_password';

GRANT CREATE ON ALL KEYSPACES TO tip_user;
GRANT SELECT ON ALL KEYSPACES TO tip_user;
GRANT MODIFY ON ALL KEYSPACES TO tip_user;
LIST ALL PERMISSIONS OF tip_user;

