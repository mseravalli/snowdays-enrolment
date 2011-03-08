DROP TABLE vb_teams CASCADE;
DROP TABLE participants CASCADE;
DROP TABLE comments CASCADE;
DROP TABLE articles CASCADE;
DROP TABLE users CASCADE;
DROP TABLE uni CASCADE;
DROP TABLE roles CASCADE;
DROP TABLE friday_program CASCADE;
DROP TABLE rental CASCADE;
DROP TABLE uploads CASCADE;

CREATE TABLE roles (
	role VARCHAR(30) PRIMARY KEY,
	role_id INT UNIQUE
);


CREATE TABLE uni (
	university VARCHAR(30) PRIMARY KEY,
	max_participants INT NOT NULL,
	max_teams INT NOT NULL DEFAULT 0,
	CHECK (max_participants >= 0),
	CHECK (max_teams >= 0) 
);

CREATE TABLE users (
	username VARCHAR(30) PRIMARY KEY,
	password VARCHAR(30) NOT NULL,
	name VARCHAR(30),
	surname VARCHAR(30),
	email VARCHAR(50) NOT NULL,
	university VARCHAR(30) NOT NULL,
	role VARCHAR(30) NOT NULL,
	FOREIGN KEY (university) REFERENCES uni (university) ON UPDATE CASCADE,
	FOREIGN KEY (role) REFERENCES roles (role) ON UPDATE CASCADE
);

CREATE TABLE friday_program(
	activity VARCHAR(30) PRIMARY KEY
);

CREATE TABLE rental(
	element VARCHAR(30) PRIMARY KEY
);

CREATE TABLE participants (
	id SERIAL PRIMARY KEY,
	university VARCHAR(30) NOT NULL,
	sex CHAR(1) NOT NULL,
	name VARCHAR(30) NOT NULL,
	surname VARCHAR(30) NOT NULL,
	email VARCHAR(50) NOT NULL,
	phone VARCHAR(30) NOT NULL,
	birthdate DATE NOT NULL,
	alimentary_intolerance VARCHAR(30),
	friday_activity VARCHAR(30) NOT NULL,
	rental VARCHAR(30),
	shoe_size VARCHAR(5),
	tshirt_size VARCHAR(3) NOT NULL,
	last_editor VARCHAR(30),
	photo VARCHAR (30),
	FOREIGN KEY (university) REFERENCES uni (university) ON UPDATE CASCADE,
	FOREIGN KEY (friday_activity) REFERENCES friday_program(activity) ON UPDATE CASCADE,
	FOREIGN KEY (rental) REFERENCES rental(element) ON UPDATE CASCADE,
	FOREIGN KEY (last_editor) REFERENCES users (username) ON UPDATE CASCADE,
	CHECK ((rental IS NOT NULL AND shoe_size IS NOT NULL) OR (rental IS NULL AND shoe_size IS NULL))
);

CREATE TABLE vb_teams (
	team_name VARCHAR(30) PRIMARY KEY,
	university VARCHAR(30) NOT NULL,
	part1 INT NOT NULL,
	part2 INT NOT NULL,
	part3 INT NOT NULL,
	part4 INT NOT NULL,
	part5 INT,
	part6 INT,
	last_editor VARCHAR(30),
	FOREIGN KEY (university) REFERENCES uni (university) ON UPDATE CASCADE,
	FOREIGN KEY (part1) REFERENCES participants (id) ON UPDATE CASCADE,
	FOREIGN KEY (part2) REFERENCES participants (id) ON UPDATE CASCADE,
	FOREIGN KEY (part3) REFERENCES participants (id) ON UPDATE CASCADE,
	FOREIGN KEY (part4) REFERENCES participants (id) ON UPDATE CASCADE,
	FOREIGN KEY (part5) REFERENCES participants (id) ON UPDATE CASCADE,
	FOREIGN KEY (part6) REFERENCES participants (id) ON UPDATE CASCADE,
	FOREIGN KEY (last_editor) REFERENCES users (username) ON UPDATE CASCADE,
	CHECK(part1 <> part2),
	CHECK(part1 <> part3),
	CHECK(part1 <> part4),
	CHECK(part1 <> part5),
	CHECK(part1 <> part6),
	CHECK(part2 <> part3),
	CHECK(part2 <> part4),
	CHECK(part2 <> part5),
	CHECK(part2 <> part6),
	CHECK(part3 <> part4),
	CHECK(part3 <> part5),
	CHECK(part3 <> part6),
	CHECK(part4 <> part5),
	CHECK(part4 <> part6),
	CHECK(part5 <> part6)
);


CREATE TABLE articles (
	id SERIAL PRIMARY KEY,
	author VARCHAR(30),
	last_edit DATE,
	title VARCHAR(100),
	text TEXT,
	FOREIGN KEY (author) REFERENCES users (username) ON UPDATE CASCADE
);

CREATE TABLE comments (
	id SERIAL PRIMARY KEY,
	article_id INT NOT NULL,
	text TEXT,
	FOREIGN KEY (article_id) REFERENCES articles (id) ON UPDATE CASCADE
);


INSERT INTO roles (role, role_id) VALUES ('administrator', 0);
INSERT INTO roles (role, role_id) VALUES ('manager', 1);
INSERT INTO roles (role, role_id) VALUES ('collaborator', 2);

INSERT INTO friday_program (activity) VALUES ('relax');
INSERT INTO friday_program (activity) VALUES ('snowshoe hiking');
INSERT INTO friday_program (activity) VALUES ('free ride');
INSERT INTO friday_program (activity) VALUES ('snowboard race');
INSERT INTO friday_program (activity) VALUES ('ski race');

INSERT INTO rental (element) VALUES ('no');
INSERT INTO rental (element) VALUES ('ski');
INSERT INTO rental (element) VALUES ('snowboard');





INSERT INTO uni (university, max_participants, max_teams) VALUES ('bolzano', 100, 3);
INSERT INTO uni (university, max_participants, max_teams) VALUES ('roma', 50, 2);
INSERT INTO uni (university, max_participants, max_teams) VALUES ('torino', 25, 1);
INSERT INTO uni (university, max_participants, max_teams) VALUES ('allUni', 0, 0);

INSERT INTO users (username, password, name, surname, email, university, role) VALUES ('marco', 'rococo', 'marco', 'seravalli', 'aa@aa.aa', 'allUni', 'administrator');
INSERT INTO users (username, password, name, surname, email, university, role) VALUES ('giuli', 'baroque', 'giulia', 'bellemo', '11@aa.aa', 'allUni', 'manager');
INSERT INTO users (username, password, name, surname, email, university, role) VALUES ('luci', 'renaissance', 'luciano', 'seravalli', 'luci@aa.aa', 'roma', 'collaborator');

INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('bolzano', 'pietro', 'spangaro', 'm', 'aa@aa.aa', '123', '1989-10-12', 'snowshoe hiking', 'l');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('torino', 'anna', 'ellero', 'f', 'aa@aa.aa', '123', '1985-11-12', 'free ride', 's');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('roma', 'aldo', 'rossi', 'm', 'aa@aa.aa', '123', '1983-10-13', 'snowshoe hiking', 'l');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('torino', 'franco', 'ellero', 'm', 'aa@aa.aa', '123', '1988-12-12', 'free ride', 's');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('bolzano', 'marco', 'londero', 'm', 'aa@aa.aa', '123', '1989-10-12', 'snowshoe hiking', 'l');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('torino', 'lucia', 'urbani', 'f', 'aa@aa.aa', '123', '1985-11-12', 'snowboard race', 's');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('roma', 'giovanna', 'bellina', 'f', 'aa@aa.aa', '123', '1983-10-13', 'ski race', 'l');
INSERT INTO participants (university, name, surname, sex, email, phone, birthdate, friday_activity, tshirt_size) 
			VALUES ('roma', 'luca', 'percoto', 'm', 'aa@aa.aa', '123', '1988-12-12', 'ski race', 's');

INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-01', 'null article', 'Mauris iaculis, massa molestie convallis posuere, neque justo laoreet augue, ut aliquet erat sem ac felis. Sed aliquam varius fermentum. Proin nec metus sit amet dui condimentum sodales. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Quisque gravida consequat urna, et ornare nisl euismod non. Curabitur urna odio, imperdiet quis rutrum sit amet, varius vitae ante. Vivamus pharetra suscipit tellus varius condimentum. In a pretium turpis. Morbi nec est quam, at vulputate lectus. In hac habitasse platea dictumst.');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-01', '1st article', 'text of the first article');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-02', '2nd article', 'text of the second article');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-03', '3rd article', 'text of the third article');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-04', '4th article', 'text of the 4444 article');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-05', '5th article', 'text of the 5555 article');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-06', '6th article', 'text of the 6666 article');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-07', '7th article', 'Integer laoreet imperdiet nunc eu lobortis. Mauris suscipit tellus ac nulla mattis non aliquet augue auctor. Suspendisse nulla orci, tincidunt non laoreet sit amet, dapibus in dolor. Sed a ipsum lectus, ut tristique sapien. Morbi ullamcorper sollicitudin orci ac venenatis. Mauris libero ipsum, ullamcorper ac bibendum eu, vehicula et purus. Morbi lacus tellus, fringilla in fermentum a, sollicitudin in lectus. Sed ut dolor eu justo placerat pharetra id in metus. Vivamus aliquam accumsan nisl, porta laoreet magna sagittis quis. Praesent ac eros erat. Nullam id nulla eu risus rhoncus dapibus.');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-08', '8th article', 'Aenean velit nunc, adipiscing a ullamcorper ac, interdum at quam. Fusce facilisis turpis eu ligula malesuada a suscipit nunc vestibulum. Proin ante lorem, condimentum id ultricies vitae, iaculis at velit. Donec pharetra sem eget nunc vestibulum sit amet elementum arcu pellentesque. Nam sed turpis nunc. Nunc ultrices mauris ut mi ornare id dapibus leo mollis. In cursus sapien vitae est fermentum convallis dictum leo pretium. Integer lectus magna, sollicitudin quis interdum a, congue ac justo. Cras leo diam, aliquet vitae consequat in, fringilla sagittis augue.');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-09', '9th article', 'Fusce molestie lacus quis felis hendrerit mattis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nunc ultricies, lectus id fermentum iaculis, nisi erat egestas nulla, pulvinar bibendum orci sem id elit. Aliquam et elit eget dolor varius varius quis non dui. Praesent nec aliquam quam. Aenean vitae arcu sit amet ipsum hendrerit vulputate tempor vitae purus. Nam gravida dignissim mi, et sollicitudin lacus sollicitudin sagittis. Integer ut malesuada augue. Sed nisi velit, ornare vitae tempor sed, viverra vitae turpis. Cras vulputate est eget velit luctus scelerisque. Sed facilisis magna at turpis ultrices et imperdiet velit viverra. Phasellus nec lacus dolor, eget ornare nulla. Vivamus tempor dictum odio, at ultricies enim blandit ut.');
INSERT INTO articles(author, last_edit, title, text) VALUES('marco', '2010-06-09', '10th article', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer pretium urna a purus vehicula laoreet. Donec laoreet iaculis eros sit amet fringilla. Curabitur porta adipiscing nunc nec ullamcorper. Donec vehicula tempus dui, non eleifend elit convallis nec. Etiam sagittis molestie urna, vitae hendrerit lorem dapibus sit amet. In vel porta mi. Fusce orci purus, varius quis adipiscing vitae, iaculis at mi. Maecenas vehicula porta sollicitudin. Duis sodales magna vitae eros facilisis tincidunt. Quisque libero dolor, consectetur eu varius sed, tempor sagittis turpis. Vivamus vestibulum libero eu urna placerat molestie. Fusce fringilla volutpat massa vehicula pharetra. Etiam ultricies molestie pharetra.');

ALTER TABLE participants ADD COLUMN hosted_guests int;
ALTER TABLE participants ADD COLUMN hosted_location VARCHAR(50);

