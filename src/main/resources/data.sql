insert into PELICULAS
(titulo, genero, sinopsis, duracion, director, estreno, clasificacion_edad, activa, imagen)
values

('Toy Story', 'AVENTURAS',
 'sinopsis.toystory',
 81, 'John Lasseter', '1995-11-22', 'TP', true, 'toystory.jpg'),

('El Rey León', 'AVENTURAS',
 'sinopsis.reyleon',
 88, 'Roger Allers', '1994-06-15', 'TP', false, 'reyleon.jpg'),

('Regreso al Futuro', 'CIENCIA_FICCION',
 'sinopsis.regresoalfuturo',
 116, 'Robert Zemeckis', '1985-07-03', 'TP', true, 'regresoalfuturo.jpg'),

('Forrest Gump', 'DRAMA',
 'sinopsis.forrestgump',
 142, 'Robert Zemeckis', '1994-07-06', 'MAYORES_7', true, 'forrestgump.jpg'),

('La La Land', 'ROMANCE',
 'sinopsis.lalaland',
 128, 'Damien Chazelle', '2016-12-09', 'MAYORES_7', true, 'lalaland.jpg'),

('Inception', 'CIENCIA_FICCION',
 'sinopsis.inception',
 148, 'Christopher Nolan', '2010-07-16', 'MAYORES_12', true, 'inception.jpg'),

('Interstellar', 'CIENCIA_FICCION',
 'sinopsis.interstellar',
 169, 'Christopher Nolan', '2014-11-07', 'MAYORES_12', false, 'interstellar.jpg'),

('El Señor de los Anillos: La Comunidad del Anillo', 'FANTASIA',
 'sinopsis.lotr',
 178, 'Peter Jackson', '2001-12-19', 'MAYORES_12', true, 'lotr.jpg'),

('Jurassic Park', 'CIENCIA_FICCION',
 'sinopsis.jurassicpark',
 127, 'Steven Spielberg', '1993-06-11', 'MAYORES_12', false, 'jurassicpark.jpg'),

('Gladiator', 'ACCION',
 'sinopsis.gladiator',
 155, 'Ridley Scott', '2000-05-05', 'MAYORES_16', true, 'gladiator.jpg'),

('Matrix', 'CIENCIA_FICCION',
 'sinopsis.matrix',
 136, 'Lana Wachowski', '1999-03-31', 'MAYORES_16', false, 'matrix.jpg'),

('Alien', 'TERROR',
 'sinopsis.alien',
 117, 'Ridley Scott', '1979-05-25', 'MAYORES_16', true, 'alien.jpg'),

('El Sexto Sentido', 'SUSPENSE',
 'sinopsis.sextosentido',
 107, 'M. Night Shyamalan', '1999-08-06', 'MAYORES_16', true, 'sextosentido.jpg'),

('Pulp Fiction', 'DRAMA',
 'sinopsis.pulpfiction',
 154, 'Quentin Tarantino', '1994-10-14', 'MAYORES_18', true, 'pulpfiction.jpg'),

('El Lobo de Wall Street', 'DRAMA',
 'sinopsis.lobowallstreet',
 180, 'Martin Scorsese', '2013-12-25', 'MAYORES_18', false, 'lobowallstreet.jpg'),

('Joker', 'DRAMA',
 'sinopsis.joker',
 122, 'Todd Phillips', '2019-10-04', 'MAYORES_18', true, 'joker.jpg'),

('La Máscara', 'COMEDIA',
 'sinopsis.lamascara',
 101, 'Chuck Russell', '1994-07-29', 'MAYORES_7', true, 'lamascara.jpg'),

('Resacón en Las Vegas', 'COMEDIA',
 'sinopsis.resacon',
 100, 'Todd Phillips', '2009-06-05', 'MAYORES_16', true, 'resacon.jpg'),

('Ocho Apellidos Vascos', 'COMEDIA',
 'sinopsis.ochoapellidosvascos',
 98, 'Emilio Martínez-Lázaro', '2014-03-14', 'MAYORES_7', true, 'ochoapellidosvascos.jpg'),

('Seven', 'SUSPENSE',
 'sinopsis.seven',
 127, 'David Fincher', '1995-09-22', 'MAYORES_18', true, 'seven.jpg');


-- Sesiones: IDs generados automáticamente por el motor (1-70 en orden de inserción)
-- Las referencias id_pelicula coinciden con el orden de inserción de PELICULAS (1-20)

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (1, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (1, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (1, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (1, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

INSERT INTO SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (2, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (2, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (2, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (2, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

INSERT INTO SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (3, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (3, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (3, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (3, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (4, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (4, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (4, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (4, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (5, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (5, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (5, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (5, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (6, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (6, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (6, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (6, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (7, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (7, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (7, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (7, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (8, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (8, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (8, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (8, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (9, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (9, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (9, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (9, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (10, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (10, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (10, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (10, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (11, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (11, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (11, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (11, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (12, '2025-01-01', 'H16_00', 'SALA_5', 'NORMAL', 7.50),
    (12, '2025-01-01', 'H18_30', 'SALA_6', 'VOSE', 8.50),
    (13, '2025-01-01', 'H21_00', 'SALA_5', 'IMAX', 10.00),
    (13, '2025-01-01', 'H23_30', 'SALA_6', 'TRES_D', 11.50),
    (14, '2025-01-02', 'H16_00', 'SALA_5', 'NORMAL', 8.00),
    (14, '2025-01-02', 'H18_30', 'SALA_6', 'IMAX', 10.50),
    (15, '2025-01-02', 'H21_00', 'SALA_5', 'VOSE', 9.00),
    (15, '2025-01-02', 'H23_30', 'SALA_6', 'TRES_D', 11.00),
    (16, '2025-01-03', 'H18_30', 'SALA_5', 'NORMAL', 8.25),
    (16, '2025-01-03', 'H21_00', 'SALA_6', 'IMAX', 10.75);

insert into USUARIOS
(username, nombre, apellidos, email, password, fecha_nacimiento, fecha_registro, rol)
values
    ('admin', 'Admin', 'Sistema', 'admin@cineffe.com', '$2a$10$XIq.srTsZxnXDSCWd/jATeBukHFh9SQHJTlcCvKd3Pa1wjRuNejIu', '1990-01-10', '2026-01-10 10:00:00', 'ADMIN'),
    ('laura', 'Laura', 'Martinez Ruiz', 'laura@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1995-04-15', '2026-01-11 10:00:00', 'USER'),
    ('david', 'David', 'Sanchez Lopez', 'david@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1993-07-22', '2026-01-12 10:00:00', 'USER'),
    ('marta', 'Marta', 'Fernandez Gil', 'marta@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1998-09-30', '2026-01-13 10:00:00', 'USER'),
    ('sergio', 'Sergio', 'Diaz Perez', 'sergio@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1991-12-05', '2026-01-14 10:00:00', 'USER'),
    ('irene', 'Irene', 'Romero Navas', 'irene@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1997-03-18', '2026-01-15 10:00:00', 'USER'),
    ('pablo', 'Pablo', 'Torres Cano', 'pablo@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1992-11-11', '2026-01-16 10:00:00', 'USER'),
    ('nerea', 'Nerea', 'Molina Cruz', 'nerea@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1996-06-08', '2026-01-17 10:00:00', 'USER'),
    ('carlos', 'Carlos', 'Vega Pastor', 'carlos@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '1989-08-27', '2026-01-18 10:00:00', 'USER'),
    ('andrea', 'Andrea', 'Ortega Leon', 'andrea@email.com', '$2a$10$2uuJWWkfzRzOICbinKm4ouss9eYA5pETHMcc.5fTig6G9CpTTFR.K', '2000-02-21', '2026-01-19 10:00:00', 'USER');

insert into ENTRADAS
(id_sesion, fila, numero, precio, fecha)
values
    (17, 1, 1, 8.00, '2026-01-20 12:00:00'),
    (17, 1, 2, 8.00, '2026-01-20 12:01:00'),
    (2, 2, 5, 10.50, '2026-01-20 12:05:00'),
    (2, 3, 8, 10.50, '2026-01-20 12:08:00'),
    (3, 4, 6, 9.00, '2026-01-20 12:10:00'),
    (3, 5, 7, 9.00, '2026-01-20 12:12:00'),
    (4, 2, 3, 11.00, '2026-01-20 12:15:00'),
    (5, 6, 10, 8.00, '2026-01-20 12:20:00'),
    (6, 7, 11, 10.50, '2026-01-20 12:25:00'),
    (7, 8, 12, 9.00, '2026-01-20 12:30:00'),
    (8, 8, 1, 11.00, '2026-01-20 12:35:00'),
    (9, 10, 15, 8.00, '2026-01-20 12:40:00'),
    (10, 9, 14, 10.50, '2026-01-20 12:45:00'),
    (11, 8, 12, 9.00, '2026-01-20 12:50:00'),
    (12, 7, 11, 11.00, '2026-01-20 12:55:00'),
    (45, 6, 10, 7.50, '2026-01-20 13:00:00'),
    (46, 5, 9, 8.50, '2026-01-20 13:05:00'),
    (47, 4, 8, 10.00, '2026-01-20 13:10:00'),
    (48, 3, 7, 11.50, '2026-01-20 13:15:00'),
    (54, 2, 6, 10.75, '2026-01-20 13:20:00');

-- Sesiones adicionales para películas 17-20
insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (17, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (17, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (17, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (17, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (18, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (18, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (18, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (18, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (19, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (19, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (19, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (19, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (20, '2024-12-31', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (20, '2024-12-31', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (20, '2024-12-31', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (20, '2024-12-31', 'H23_30', 'SALA_4', 'TRES_D', 11.00);
