CREATE ROLE IF NOT EXISTS tip_user
WITH LOGIN = true
| PASSWORD =  'tip_password';

CREATE KEYSPACE IF NOT EXISTS tip_wlan_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

GRANT CREATE ON ALL KEYSPACES TO tip_user;
GRANT SELECT ON ALL KEYSPACES TO tip_user;
GRANT MODIFY ON ALL KEYSPACES TO tip_user;
LIST ALL PERMISSIONS OF tip_user;

USE tip_wlan_keyspace;

