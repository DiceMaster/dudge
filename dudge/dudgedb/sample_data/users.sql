INSERT INTO users (login, pwd_hash, real_name , organization , register_date , age , email, can_create_contest)
VALUES ('olorin', MD5('olorin'), 'Michael Antonov' , 'SGTU' , '2007-08-14' , 19 , 'olorinbant@gmail.com', 'true');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email , can_create_problem)
VALUES ('foo', MD5('foo'), 'Ivan Petrov' , 'MegaWork' , '2007-08-16' , 'none@none.no' , 'true');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email  , can_create_problem)
VALUES ('red', MD5('red'), 'Ivanov Ivan' , 'Factory' , '2007-08-16' , 'none@none.no' , 'true'); 

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email )
VALUES ('blue', MD5('blue'), 'Petrov Petr' , 'Family' , '2007-08-16' , 'none@none.no');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email )
VALUES ('green', MD5('green'), 'Ivanov Petr' , 'Plant' , '2007-08-16' , 'none@none.no');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email )
VALUES ('yellow', MD5('yellow'), 'Petrov Ivan' , 'Factory' , '2007-08-16' , 'none@none.no');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email )
VALUES ('black', MD5('black'), 'Sergeev Ivan' , 'Factory' , '2007-08-16' , 'none@none.no');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email )
VALUES ('magenta', MD5('magenta'), 'Ivanov Ivan' , 'Plant' , '2007-08-16' , 'none@none.no');

INSERT INTO users (login, pwd_hash, real_name , organization , register_date  , email )
VALUES ('white', MD5('white'), 'Ivanov Ivan' , 'Trade' , '2007-08-16' , 'none@none.no');


INSERT INTO contest_languages(contest_id, language_id)
VALUES ((SELECT last_value FROM contests_contest_id_seq), 'gcc');

INSERT INTO problems (owner, title, description , create_time)
VALUES ('admin', 'A plus B', 'Calculate sum of a and b.' , now());

INSERT INTO contest_problems(contest_id, problem_id, problem_order, problem_mark)
VALUES ((SELECT last_value FROM contests_contest_id_seq), (SELECT last_value FROM problems_problem_id_seq), 0, 'A');

INSERT INTO roles (contest_id, username, role_type)
VALUES ((SELECT last_value FROM contests_contest_id_seq), 'admin', 'ADMINISTRATOR');

INSERT INTO roles (contest_id, username, role_type)
VALUES ((SELECT last_value FROM contests_contest_id_seq), 'foo', 'USER');

INSERT INTO contests (caption, description , con_type, start_time, duration)
VALUES ('Global contest', 'Test Global contest.' , 'GLOBAL', 'epoch', 0);

INSERT INTO problems (owner, title, description , create_time)
VALUES ('olorin', 'A minus B', 'Calculate  value of A-B expression.' , 'epoch');

INSERT INTO contest_problems(contest_id, problem_id, problem_order, problem_mark)
VALUES ((SELECT last_value FROM contests_contest_id_seq), (SELECT last_value FROM problems_problem_id_seq), 1, 'B');

INSERT INTO contests (caption, description , con_type, start_time, duration)
VALUES ('Example lab work', 'Test lab work.' , 'LAB', now(), 0);

INSERT INTO problems (owner, title, description , create_time , is_hidden)
VALUES ('foo', 'Cube', 'Calculate volume of cube with edge of length a.' , 'tomorrow' , 'true');
