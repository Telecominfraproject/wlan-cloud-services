create table if not exists adoption_metrics_counters (

    -- time dimension keys -- all computed from the timestamp
    year int not null,
    month int not null,
    weekOfYear int not null,
    dayOfYear int not null,

    -- object hierarchy dimension keys
    customerId int not null, -- from ApNodeMetrics
    locationId bigint not null, -- from ApNodeMetrics
    equipmentId bigint not null, -- from ApNodeMetrics

    -- counters to track
    numUniqueConnectedMacs bigint not null, -- Size of set<clientMac>, update from the accumulated in-memory data, finalize once a day
    numBytesUpstream bigint not null, -- accumulate from ApNodeMetrics report
    numBytesDownstream bigint not null, -- accumulate from ApNodeMetrics report

    primary key (year, month, weekOfYear, dayOfYear, customerId, locationId, equipmentId)

);

create table if not exists adoption_metrics_uniq_macs (

    metric_timestamp bigint not null,

    -- object hierarchy dimension keys
    customerId int not null, -- from ApNodeMetrics
    locationId bigint not null, -- from ApNodeMetrics
    equipmentId bigint not null, -- from ApNodeMetrics

    clientMac bigint not null,

    primary key (metric_timestamp, customerId, locationId, equipmentId, clientMac)

);


