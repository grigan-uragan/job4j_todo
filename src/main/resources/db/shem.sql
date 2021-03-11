create table tasks (
    id          serial primary key,
    description varchar(255),
    created     timestamp,
    done        boolean
);

create table users (
    user_id       serial primary key,
    user_name     text,
    user_email    text,
    user_password text
);
