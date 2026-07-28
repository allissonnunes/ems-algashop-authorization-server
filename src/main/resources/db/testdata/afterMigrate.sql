set
session_replication_role = 'replica';

DELETE
FROM auth_user;
DELETE
FROM oauth2_authorization;
DELETE
FROM oauth2_authorization_consent;
DELETE
FROM spring_session_attributes;
DELETE
FROM spring_session;

INSERT INTO auth_user (id,
                       email,
                       password_hash,
                       name,
                       type,
                       enabled,
                       version,
                       created_by,
                       created_at,
                       last_modified_by,
                       last_modified_at)
VALUES ('019f9bc3-47d3-7353-9c53-1eee7a119d59',
        'customer@example.com',
        '{bcrypt}$2a$10$AZkXeAchTnL.rtUNlQvP5u5ZXUP9Xqv0v6cUb2QZ6.vo8BatwFTca',
        'João Cliente',
        'CUSTOMER',
        TRUE,
        0,
        NULL,
        CURRENT_TIMESTAMP,
        NULL,
        CURRENT_TIMESTAMP),
       ('019f9bc3-6c31-74d8-90f1-d00942215335',
        'manager@example.com',
        '{bcrypt}$2a$10$AZkXeAchTnL.rtUNlQvP5u5ZXUP9Xqv0v6cUb2QZ6.vo8BatwFTca',
        'Maria Gerente',
        'MANAGER',
        TRUE,
        0,
        NULL,
        CURRENT_TIMESTAMP,
        NULL,
        CURRENT_TIMESTAMP),
       ('019f9bc3-8239-7312-abfd-15f9a6b05d82',
        'operator@example.com',
        '{bcrypt}$2a$10$AZkXeAchTnL.rtUNlQvP5u5ZXUP9Xqv0v6cUb2QZ6.vo8BatwFTca',
        'Carlos Operador',
        'OPERATOR',
        TRUE,
        0,
        NULL,
        CURRENT_TIMESTAMP,
        NULL,
        CURRENT_TIMESTAMP);

set
session_replication_role = 'origin';