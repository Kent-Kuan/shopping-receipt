CREATE TABLE state_tax(
    state varchar(20) not null primary key,
    tax decimal not null,
    tax_free_categories varchar(30)
);
CREATE TABLE product(
    name varchar(20) not null primary key,
    category varchar(30) not null default 'others'
);