drop table if exists events;

CREATE TABLE IF NOT EXISTS events
(
    id
    INTEGER
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    app
    VARCHAR
(
    255
),
    uri VARCHAR
(
    512
),
    ip VARCHAR
(
    512
),
    created timestamp,
    CONSTRAINT pk_event PRIMARY KEY
(
    id
)
    );