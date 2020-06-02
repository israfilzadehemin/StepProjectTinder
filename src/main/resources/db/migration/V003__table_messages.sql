create table messages
(
    id          serial  not null
        constraint messages_pk
            primary key,
    sender_id   integer not null,
    receiver_id integer not null,
    body        varchar not null,
    time        varchar not null
);

