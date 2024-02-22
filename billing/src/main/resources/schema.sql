create table if not exists bills
(
    id        bigserial primary key,
    number    bigint not null unique,
    balance   decimal(12, 2)
);