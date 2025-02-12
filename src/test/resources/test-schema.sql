create table if not exists posts
(
    id         bigserial primary key,
    title      varchar(255) not null,
    content    text         not null,
    image      bytea,
    created_at timestamp default CURRENT_TIMESTAMP,
    updated_at timestamp default CURRENT_TIMESTAMP
    );

create index idx_post_created_at on posts (created_at);

create table if not exists comments
(
    id         bigserial primary key,
    post_id    integer not null references posts on delete cascade,
    content    text    not null,
    created_at timestamp default CURRENT_TIMESTAMP,
    updated_at timestamp default CURRENT_TIMESTAMP
);

create index idx_comment_created_at on comments (created_at);

create table if not exists likes
(
    id         bigserial primary key,
    post_id    integer not null references posts on delete cascade,
    user_id    integer not null,
    created_at timestamp default CURRENT_TIMESTAMP
);

create index idx_like_created_at on likes (created_at);

create table tags
(
    id   bigserial primary key,
    name varchar(50) not null unique
);

create index idx_tag_name on tags (name);

create table post_tags
(
    post_id integer not null references posts on delete cascade,
    tag_id  integer not null references tags on delete cascade,
    primary key (post_id, tag_id)
);
