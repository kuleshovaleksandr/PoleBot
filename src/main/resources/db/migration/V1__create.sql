create table if not exists animations (
    id       varchar(255) not null
        primary key,
    file_id  varchar(255),
    name     varchar(255),
    week_day varchar(255)
);

create table if not exists currency_rate (
    id       integer not null
        primary key,
    currency varchar(255),
    date     timestamp(6),
    rate     double precision,
    scale    double precision
);

create table if not exists stickers (
    id      varchar(255) not null
        primary key,
    emoji   varchar(255),
    file_id varchar(255),
    name    varchar(255)
);

create table if not exists users (
    id            bigint not null
        primary key,
    first_name    varchar(255),
    last_name     varchar(255),
    registered_at timestamp(6),
    user_name     varchar(255)
);