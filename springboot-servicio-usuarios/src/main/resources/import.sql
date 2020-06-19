INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('wmarmolejo','$2a$10$9qCqVCjU7617RnCTDKyn.O5XE5GKbFy9fKWS.UY6S6nBU6IGfYV0O',true, 'Wilmar', 'Marmolejo','wmarmolejo@hotmail.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) VALUES ('admin','$2a$10$n0wyjRq0XMyD.K3jwMMuy.YWwMAeZir5k5kFD.GJyAGUaKyl2cCKq',true, 'admin', 'admin','admin@hotmail.com');

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (1, 1);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2, 2);
INSERT INTO usuarios_roles (usuario_id, role_id) VALUES (2, 1);