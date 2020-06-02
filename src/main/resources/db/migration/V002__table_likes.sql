create table likes
(
    id          serial  not null
        constraint likes_pk
            primary key,
    sender_id   integer not null,
    receiver_id integer not null,
    action      varchar not null
);
