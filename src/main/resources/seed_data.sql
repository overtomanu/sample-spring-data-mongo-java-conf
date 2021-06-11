INSERT INTO USERS (username,password,enabled) VALUES ('john','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1);
INSERT INTO USERS (username,password,enabled) VALUES ('mary','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1);
INSERT INTO USERS (username,password,enabled) VALUES ('susan','{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K',1);

INSERT INTO authorities (authority) VALUES ('ROLE_EMPLOYEE');
INSERT INTO authorities (authority) VALUES ('ROLE_MANAGER');
INSERT INTO authorities (authority) VALUES ('ROLE_ADMIN');

INSERT INTO user_authorities(username,authority) values ('john','ROLE_EMPLOYEE');
INSERT INTO user_authorities(username,authority) values ('mary','ROLE_EMPLOYEE');
INSERT INTO user_authorities(username,authority) values ('susan','ROLE_EMPLOYEE');
INSERT INTO user_authorities(username,authority) values ('susan','ROLE_ADMIN');
INSERT INTO user_authorities(username,authority) values ('mary','ROLE_MANAGER');