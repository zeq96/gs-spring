create table user
(
    id                int auto_increment primary key,
    username          varchar(20),
    encryptedPassword varchar(100),
    avatar            varchar(100),
    created_at        datetime,
    updated_at        datetime
);

create table blog
(
    id          bigint auto_increment primary key,
    title       varchar(100),
    description varchar(200),
    content     text,
    user_id     bigint,
    created_at  datetime,
    updated_at  datetime
);