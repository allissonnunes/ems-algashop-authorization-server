CREATE TABLE auth_user
(
    id               UUID         NOT NULL,
    email            VARCHAR(255) NOT NULL,
    password_hash    VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    type             VARCHAR(255) NOT NULL,
    enabled          BOOLEAN      NOT NULL,
    version          BIGINT       NOT NULL,
    created_by       UUID,
    created_at       TIMESTAMPTZ,
    last_modified_by UUID,
    last_modified_at TIMESTAMPTZ,
    CONSTRAINT pk_auth_user PRIMARY KEY (id)
);

ALTER TABLE auth_user
    ADD CONSTRAINT uq_auth_user_email UNIQUE (email);