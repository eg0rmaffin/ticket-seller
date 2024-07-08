DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

