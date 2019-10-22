create table Conversation(
	convo_id integer primary key autoincrement,
	colour_id integer not null,
	bridge_id integer not null,
	profile_id integer not null
);

create table Bridge(
	bridge_id integer primary key autoincrement,
	convo_id integer not null
);

create table Connection(
	conn_id integer primary key autoincrement,
	bridge_id integer not null,
	server_IP varchar(255) not null,
	server_key blob not null,
	posistion integer not null
);

create table Profile(
	profile_id integer primary key autoincrement,
	icon_id integer not null,
	name varchar(255),
	user_key_public blob not null,
	user_key_private blob,
	user_id_key blob not null
);

create table Icon(
	icon_id integer primary key autoincrement,
	icon_loc varchar(255) not null
);

create table History(
	history_id integer primary key autoincrement,
	convo_id integer not null,
	message text not null
	time datetime not null,
	end date datetime not null,
	user_from integer not null
);

create table Colour(
	colour_id integer primary key autoincrement,
	prim_col varchar(20) not null,
	seco_col varchar(20) not null,
	text_col varchar(20) not null,
	hint_col varchar(20) not null,
	acce_col varchar(20) not null
);

