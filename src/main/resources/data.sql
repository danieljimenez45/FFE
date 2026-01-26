insert into PELICULAS
(id, titulo, genero, sinopsis, duracion, director, estreno, clasificacion_edad, activa)
values

(1, 'Toy Story', 'AVENTURAS',
 'Los juguetes cobran vida cuando los humanos no están.',
 81, 'John Lasseter', '1995-11-22', 'TP',true),

(2, 'El Rey León', 'AVENTURAS',
 'Un joven león debe asumir su destino como rey.',
 88, 'Roger Allers', '1994-06-15', 'TP', false),

(3, 'Regreso al Futuro', 'CIENCIA_FICCION',
 'Un adolescente viaja accidentalmente al pasado.',
 116, 'Robert Zemeckis', '1985-07-03', 'TP', true),

(4, 'Forrest Gump', 'DRAMA',
 'La extraordinaria vida de un hombre sencillo.',
 142, 'Robert Zemeckis', '1994-07-06', 'MAYORES_7' ,true),

(5, 'La La Land', 'ROMANCE',
 'Una historia de amor entre música y cine.',
 128, 'Damien Chazelle', '2016-12-09', 'MAYORES_7', true),

(6, 'Inception', 'CIENCIA_FICCION',
 'Un ladrón roba secretos a través de los sueños.',
 148, 'Christopher Nolan', '2010-07-16', 'MAYORES_12', true),

(7, 'Interstellar', 'CIENCIA_FICCION',
 'Exploradores viajan a través de un agujero de gusano.',
 169, 'Christopher Nolan', '2014-11-07', 'MAYORES_12',false),

(8, 'El Señor de los Anillos: La Comunidad del Anillo', 'FANTASIA',
 'Un hobbit inicia un viaje para destruir un anillo.',
 178, 'Peter Jackson', '2001-12-19', 'MAYORES_12', true),

(9, 'Jurassic Park', 'CIENCIA_FICCION',
 'Dinosaurios clonados escapan de control.',
 127, 'Steven Spielberg', '1993-06-11', 'MAYORES_12',false),

(10, 'Gladiator', 'ACCION',
 'Un general romano busca venganza.',
 155, 'Ridley Scott', '2000-05-05', 'MAYORES_16', true),

(11, 'Matrix', 'CIENCIA_FICCION',
 'Un hacker descubre la verdad sobre su realidad.',
 136, 'Lana Wachowski', '1999-03-31', 'MAYORES_16', false),

(12, 'Alien', 'TERROR',
 'Una criatura mortal acecha a una tripulación espacial.',
 117, 'Ridley Scott', '1979-05-25', 'MAYORES_16', true),

(13, 'El Sexto Sentido', 'SUSPENSE',
 'Un niño afirma ver personas muertas.',
 107, 'M. Night Shyamalan', '1999-08-06', 'MAYORES_16', true),

(14, 'Pulp Fiction', 'DRAMA',
 'Historias entrelazadas del crimen en Los Ángeles.',
 154, 'Quentin Tarantino', '1994-10-14', 'MAYORES_18', true),

(15, 'El Lobo de Wall Street', 'DRAMA',
 'El ascenso y caída de un corredor de bolsa.',
 180, 'Martin Scorsese', '2013-12-25', 'MAYORES_18', false),

(16, 'Joker', 'DRAMA',
 'El origen oscuro de un villano.',
 122, 'Todd Phillips', '2019-10-04', 'MAYORES_18', true),

(17, 'La Máscara', 'COMEDIA',
 'Un hombre descubre una máscara con poderes.',
 101, 'Chuck Russell', '1994-07-29', 'MAYORES_7', true),

(18, 'Resacón en Las Vegas', 'COMEDIA',
 'Una despedida de soltero se sale de control.',
 100, 'Todd Phillips', '2009-06-05', 'MAYORES_16', true),

(19, 'Ocho Apellidos Vascos', 'COMEDIA',
 'Un andaluz viaja al País Vasco por amor.',
 98, 'Emilio Martínez-Lázaro', '2014-03-14', 'MAYORES_7',true),

(20, 'Seven', 'SUSPENSE',
 'Un asesino en serie basado en los pecados capitales.',
 127, 'David Fincher', '1995-09-22', 'MAYORES_18', true);


insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (1, 1, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (2, 1, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (3, 1, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (4, 1, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

INSERT INTO SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (5, 2, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (6, 2, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (7, 2, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (8, 2, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);


INSERT INTO SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (9, 3, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (10, 3, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (11, 3, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (12, 3, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (13, 4, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (14, 4, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (15, 4, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (16, 4, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (17, 5, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (18, 5, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (19, 5, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (20, 5, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (21, 6, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (22, 6, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (23, 6, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (24, 6, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (25, 7, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (26, 7, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (27, 7, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (28, 7, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (29, 8, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (30, 8, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (31, 8, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (32, 8, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (33, 9, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (34, 9, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (35, 9, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (36, 9, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (37, 10, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (38, 10, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (39, 10, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (40, 10, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id, id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (41, 11, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (42, 11, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (43, 11, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (44, 11, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);


-- Datos de ejemplo USUARIOS
-- Contraseña: Admin1
insert into USUARIOS (username, nombre, apellidos, email, password)
values ('admin', 'Admin', 'Admin Admin', 'admin@prueba.net', 'Admin1');

insert into USER_ROLES (user_id, roles)
values (1, 'ADMIN');

-- Contraseña: User1
insert into USUARIOS (username, nombre, apellidos, email, password)
values ('jose', 'Jose', 'Jose User', 'user@prueba.net', 'User1');

insert into USER_ROLES (user_id, roles)
values (2, 'USER');

-- Contraseña: Test1
insert into USUARIOS (username, nombre, apellidos, email, password)
values ('test', 'Test', 'Test Test', 'test@prueba.net', 'Test1');

insert into USER_ROLES (user_id, roles)
values (3, 'USER');

-- Contraseña: Otro1
insert into USUARIOS (username, nombre, apellidos, email, password)
values ('maría', 'María', 'María Otro', 'otro@prueba.net', 'Otro1');

insert into USER_ROLES (user_id, roles)
values (4, 'USER');