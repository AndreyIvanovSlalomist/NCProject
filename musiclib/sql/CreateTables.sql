create table lib_role(
	roleName varchar(100),
	id serial PRIMARY key
);

create table lib_user(
	userName varchar(100),
	password varchar(100),
	id_rile integer REFERENCES lib_role (id),
	id serial PRIMARY key
);

create table lib_Genre(
	genreName varchar(100),
	id serial PRIMARY key
);

create table track(
	name varchar(100),
	singer varchar(100),
	album varchar(100),
	length integer,
	id_Genre integer REFERENCES lib_Genre (id),
	id serial PRIMARY key
);
