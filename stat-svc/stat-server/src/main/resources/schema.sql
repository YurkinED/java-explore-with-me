drop table if exists hits;

CREATE TABLE IF NOT EXISTS hits
(
    id  INTEGER  GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(512),
    uri VARCHAR(512),
    ip  VARCHAR(512),
    created timestamp,
    CONSTRAINT pk_hits PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS app
(
    app_id  INTEGER  GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_app PRIMARY KEY(app_id)
);