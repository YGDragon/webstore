create table if not exists products
(
    id       bigserial primary key,
    title     varchar(255) not null,
    quantity   int check ( quantity > -1 ),
    price    decimal(12, 2) check ( price > 0 ),
    reserve int check ( reserve <= products.quantity )
);