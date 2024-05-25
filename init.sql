-- create table persons (
--      id      bigint primary key auto_increment,
--      name    varchar(255) not null,
--      age     int not null,
--      avatar  varchar(255) not null
-- )

CREATE TABLE persons
(
    id     SERIAL PRIMARY KEY,
    name   CHARACTER VARYING(30),
    age    integer,
    avatar CHARACTER VARYING(30)
);