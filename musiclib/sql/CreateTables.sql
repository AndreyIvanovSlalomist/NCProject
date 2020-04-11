-- таблица ролей
create table lib_role(
	roleName varchar(100), -- название роли
	id serial PRIMARY key -- код роли
);
-- таблица с пользователями
create table lib_user(
	userName varchar(100), -- логин
	password varchar(100), -- хэшированный пароль
	id_rile integer REFERENCES lib_role (id), -- код роли
	id serial PRIMARY key -- код пользователя
);
-- таблица с жанрами
create table lib_Genre(
	genreName varchar(100), -- название жанра
	id serial PRIMARY key -- код жанра
);
-- таблица с треками
create table track(
	name varchar(100), -- название
	singer varchar(100), -- исполнитель
	album varchar(100), -- альбом
	length integer, -- длинна
	id_Genre integer REFERENCES lib_Genre (id),-- код жанра
	id serial PRIMARY key -- код трека
);

-- создание ролей
insert into lib_role (roleName) values('administrator');
insert into lib_role (roleName) values('moderator');
insert into lib_role (roleName) values('user');