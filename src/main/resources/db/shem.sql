create table tasks
(
    id serial primary key,
    description varchar(255),
    created timestamp,
    done varchar(50)
)