DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id VARCHAR(50) NOT NULL,
  picture_url VARCHAR(250) NOT NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,

  PRIMARY KEY (id)
);
DROP TABLE IF EXISTS list;

CREATE TABLE list (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(250) NOT NULL,
  description VARCHAR(250),
  owner_id VARCHAR(250) NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) 
	REFERENCES user(id)
	ON DELETE CASCADE
);
DROP TABLE IF EXISTS template;

CREATE TABLE template (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(250) NOT NULL,
  owner_id VARCHAR(250) NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) 
	REFERENCES user(id)
	ON DELETE CASCADE
);
DROP TABLE IF EXISTS template_listitem;

CREATE TABLE template_listitem (
  id INT AUTO_INCREMENT NOT NULL,
  name VARCHAR(250) NOT NULL,
  quantity INT,
  owner_id VARCHAR(250) NOT NULL,
  template_id INT NOT NULL,
  priority VARCHAR(250),

  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) 
	REFERENCES user(id)
	ON DELETE CASCADE,
  FOREIGN KEY (template_id) 
	REFERENCES template(id)
        ON DELETE CASCADE
);
DROP TABLE IF EXISTS listitem;

CREATE TABLE listitem (
  id INT AUTO_INCREMENT NOT NULL,
  checked BIT NOT NULL,
  name VARCHAR(250) NOT NULL,
  quantity INT,
  owner_id VARCHAR(250) NOT NULL,
  list_id INT NOT NULL,
  priority VARCHAR(250),

  PRIMARY KEY (id),
  FOREIGN KEY (owner_id) 
	REFERENCES user(id)
	ON DELETE CASCADE,
  FOREIGN KEY (list_id) 
	REFERENCES list(id)
        ON DELETE CASCADE
);

