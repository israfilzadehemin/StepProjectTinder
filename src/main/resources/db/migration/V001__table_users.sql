create table users
(
    id          serial  not null
        constraint users_pk
            primary key,
    username    varchar not null,
    mail        varchar not null,
    password    varchar not null,
    profile_pic varchar not null,
    last_seen   varchar not null,
    fullname    varchar not null
);

create unique index users_id_uindex
    on users (id);

create unique index users_mail_uindex
    on users (mail);

create unique index users_username_uindex
    on users (username);

