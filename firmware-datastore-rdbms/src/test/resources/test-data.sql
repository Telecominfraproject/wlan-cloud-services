insert into firmware_version (
    id,    
    equipmentType,
    modelId,
    versionName,
    commitTag,
    description,
    filename,
    validationMethod,
    validationCode,
    releaseDate,
    createdTimestamp,
    lastModifiedTimestamp
) values (
    1,
    2,
    'model1',
    'v1',
    'abc',
    'this is version 1',
    '/abc/123/v1.q',
    1,
    'ab432f72ed3ada00e80631e5138df2a6',
    0,0,0
);
