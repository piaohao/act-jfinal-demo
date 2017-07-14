create table superfilm_beta.account
(
	id int not null auto_increment
		primary key,
	name varchar(50) not null,
	password varchar(50) not null,
	disable int null,
	createdTime timestamp default CURRENT_TIMESTAMP null,
	rule_id int null
)
;

INSERT INTO superfilm_beta.account (name, password, disable, createdTime, rule_id) VALUES ('act', '123', null, '2017-07-11 13:51:56', null);