DROP TABLE IF EXISTS USERS cascade;
DROP TABLE IF EXISTS CATEGORIES cascade;
DROP TABLE IF EXISTS COMPILATIONS cascade;
DROP TABLE IF EXISTS EVENTS cascade;
DROP TABLE IF EXISTS REQUEST cascade;
DROP TABLE IF EXISTS EVENTS_COMPILATIONS cascade;


create table IF NOT EXISTS USERS
(
    ID    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        primary key,
    NAME  CHARACTER VARYING(50)                   not null,
    EMAIL CHARACTER VARYING(100)                  not null
        unique
);

create table IF NOT EXISTS CATEGORIES
(
    ID   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        primary key,
    NAME CHARACTER VARYING(100)                  not null unique
);

create table IF NOT EXISTS COMPILATIONS
(
    ID     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        primary key,
    PINNED boolean                                 not null,
    TITLE  CHARACTER VARYING(200)                  not null
);

create table IF NOT EXISTS EVENTS
(
    ID                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        primary key,
    USER_ID            BIGINT                                  not null REFERENCES USERS (id),
    PAID               boolean                                 not null,
    PARTICIPANT_LIMIT  BIGINT,
    PUBLISHED_ON       TIMESTAMP,
    REQUEST_MODERATION boolean,
    STATE              CHARACTER VARYING(50)                   not null,
    TITLE              CHARACTER VARYING(200)                  not null,
    ANNOTATION         CHARACTER VARYING(500)            ,--      not null,
    CATEGORY_ID        BIGINT                                  not null REFERENCES CATEGORIES (id),
    COMPILATION_ID     BIGINT REFERENCES COMPILATIONS (id),
    CREATED_ON         TIMESTAMP,
    DESCRIPTION        CHARACTER VARYING(1000),
    EVENT_DATE         TIMESTAMP                               not null,
    LAT                FLOAT                                   not null,
    LON                FLOAT                                   not null
);

create table IF NOT EXISTS REQUEST
(
    ID      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
        primary key,
    USER_ID BIGINT                                  not null REFERENCES USERS (id),
    STATUS  CHARACTER VARYING(50)                   not null,
    CREATED TIMESTAMP,
    EVENT_ID BIGINT                                  not null REFERENCES EVENTS (id)
);

CREATE TABLE IF NOT EXISTS COMPILATIONS_EVENTS (
                                                   EVENTS_ID INTEGER,
                                                   compilation_id INTEGER,
                                                   PRIMARY KEY (EVENTS_ID, compilation_id),
                                                   CONSTRAINT fk_events_compilations_event FOREIGN KEY (EVENTS_ID) REFERENCES events(id) ON DELETE CASCADE,
                                                   CONSTRAINT fk_events_compilations_compilation FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE
);