insert into PELICULAS
(titulo, genero, sinopsis, duracion, director, estreno, clasificacion_edad, activa, imagen)
values

    ('Toy Story', 'AVENTURAS',
     'Los juguetes cobran vida cuando los humanos no están.',
     81, 'John Lasseter', '2026-04-03', 'TP', true, 'toystory.jpg'),

    ('El Rey León', 'AVENTURAS',
     'Un joven león debe asumir su destino como rey.',
     88, 'Roger Allers', '2026-04-07', 'TP', false, 'reyleon.jpg'),

    ('Regreso al Futuro', 'CIENCIA_FICCION',
     'Un adolescente viaja accidentalmente al pasado.',
     116, 'Robert Zemeckis', '2026-04-10', 'TP', true, 'regresoalfuturo.jpg'),

    ('Forrest Gump', 'DRAMA',
     'La extraordinaria vida de un hombre sencillo.',
     142, 'Robert Zemeckis', '2026-04-12', 'MAYORES_7', true, 'forrestgump.jpg'),

    ('La La Land', 'ROMANCE',
     'Una historia de amor entre música y cine.',
     128, 'Damien Chazelle', '2026-04-15', 'MAYORES_7', true, 'lalaland.jpg'),

    ('Inception', 'CIENCIA_FICCION',
     'Un ladrón roba secretos a través de los sueños.',
     148, 'Christopher Nolan', '2026-04-18', 'MAYORES_12', true, 'inception.jpg'),

    ('Interstellar', 'CIENCIA_FICCION',
     'Exploradores viajan a través de un agujero de gusano.',
     169, 'Christopher Nolan', '2026-04-21', 'MAYORES_12', false, 'interstellar.jpg'),

    ('El Señor de los Anillos: La Comunidad del Anillo', 'FANTASIA',
     'Un hobbit inicia un viaje para destruir un anillo.',
     178, 'Peter Jackson', '2026-04-23', 'MAYORES_12', true, 'lotr.jpg'),

    ('Jurassic Park', 'CIENCIA_FICCION',
     'Dinosaurios clonados escapan de control.',
     127, 'Steven Spielberg', '2026-04-25', 'MAYORES_12', false, 'jurassicpark.jpg'),

    ('Gladiator', 'ACCION',
     'Un general romano busca venganza.',
     155, 'Ridley Scott', '2026-04-02', 'MAYORES_16', true, 'gladiator.jpg'),

    ('Matrix', 'CIENCIA_FICCION',
     'Un hacker descubre la verdad sobre su realidad.',
     136, 'Lana Wachowski', '2026-04-05', 'MAYORES_16', false, 'matrix.jpg'),

    ('Alien', 'TERROR',
     'Una criatura mortal acecha a una tripulación espacial.',
     117, 'Ridley Scott', '2026-04-08', 'MAYORES_16', true, 'alien.jpg'),

    ('El Sexto Sentido', 'SUSPENSE',
     'Un niño afirma ver personas muertas.',
     107, 'M. Night Shyamalan', '2026-04-11', 'MAYORES_16', true, 'sextosentido.jpg'),

    ('Pulp Fiction', 'DRAMA',
     'Historias entrelazadas del crimen en Los Ángeles.',
     154, 'Quentin Tarantino', '2026-04-14', 'MAYORES_18', true, 'pulpfiction.jpg'),

    ('El Lobo de Wall Street', 'DRAMA',
     'El ascenso y caída de un corredor de bolsa.',
     180, 'Martin Scorsese', '2026-04-17', 'MAYORES_18', false, 'lobowallstreet.jpg'),

    ('Joker', 'DRAMA',
     'El origen oscuro de un villano.',
     122, 'Todd Phillips', '2026-04-20', 'MAYORES_18', true, 'joker.jpg'),

    ('La Máscara', 'COMEDIA',
     'Un hombre descubre una máscara con poderes.',
     101, 'Chuck Russell', '2026-04-22', 'MAYORES_7', true, 'lamascara.jpg'),

    ('Resacón en Las Vegas', 'COMEDIA',
     'Una despedida de soltero se sale de control.',
     100, 'Todd Phillips', '2026-04-26', 'MAYORES_16', true, 'resacon.jpg'),

    ('Ocho Apellidos Vascos', 'COMEDIA',
     'Un andaluz viaja al País Vasco por amor.',
     98, 'Emilio Martínez-Lázaro', '2026-04-28', 'MAYORES_7', true, 'ochoapellidosvascos.jpg'),

    ('Seven', 'SUSPENSE',
     'Un asesino en serie basado en los pecados capitales.',
     127, 'David Fincher', '2026-04-30', 'MAYORES_18', true, 'seven.jpg');

ALTER TABLE PELICULAS ALTER COLUMN ID RESTART WITH 21;


insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (1, '2026-04-03', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (1, '2026-04-03', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (1, '2026-04-03', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (1, '2026-04-03', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

INSERT INTO SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (2, '2026-04-07', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (2, '2026-04-07', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (2, '2026-04-07', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (2, '2026-04-07', 'H23_30', 'SALA_4', 'TRES_D', 11.00);


INSERT INTO SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (3, '2026-04-10', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (3, '2026-04-10', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (3, '2026-04-10', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (3, '2026-04-10', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (4, '2026-04-12', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (4, '2026-04-12', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (4, '2026-04-12', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (4, '2026-04-12', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (5, '2026-04-15', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (5, '2026-04-15', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (5, '2026-04-15', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (5, '2026-04-15', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (6, '2026-04-18', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (6, '2026-04-18', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (6, '2026-04-18', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (6, '2026-04-18', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (7, '2026-04-21', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (7, '2026-04-21', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (7, '2026-04-21', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (7, '2026-04-21', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (8, '2026-04-23', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (8, '2026-04-23', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (8, '2026-04-23', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (8, '2026-04-23', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (9, '2026-04-25', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (9, '2026-04-25', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (9, '2026-04-25', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (9, '2026-04-25', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (10, '2026-04-02', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (10, '2026-04-02', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (10, '2026-04-02', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (10, '2026-04-02', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (11, '2026-04-05', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (11, '2026-04-05', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (11, '2026-04-05', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (11, '2026-04-05', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (12, '2026-04-08', 'H16_00', 'SALA_5', 'NORMAL', 7.50),
    (12, '2026-04-08', 'H18_30', 'SALA_6', 'VOSE', 8.50),
    (13, '2026-04-11', 'H21_00', 'SALA_5', 'IMAX', 10.00),
    (13, '2026-04-11', 'H23_30', 'SALA_6', 'TRES_D', 11.50),
    (14, '2026-04-14', 'H16_00', 'SALA_5', 'NORMAL', 8.00),
    (14, '2026-04-14', 'H18_30', 'SALA_6', 'IMAX', 10.50),
    (15, '2026-04-17', 'H21_00', 'SALA_5', 'VOSE', 9.00),
    (15, '2026-04-17', 'H23_30', 'SALA_6', 'TRES_D', 11.00),
    (16, '2026-04-20', 'H18_30', 'SALA_5', 'NORMAL', 8.25),
    (16, '2026-04-20', 'H21_00', 'SALA_6', 'IMAX', 10.75);

/*
    La contrasña para usuarios es la misma clave123

    La contraseña para admin es admin123
*/
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

-- Sesiones adicionales (películas 17–20) tomadas del branch marius-frontend; ids nuevos para no romper ENTRADAS existentes

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (17, '2026-04-22', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (17, '2026-04-22', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (17, '2026-04-22', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (17, '2026-04-22', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (18, '2026-04-26', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (18, '2026-04-26', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (18, '2026-04-26', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (18, '2026-04-26', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (19, '2026-04-28', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (19, '2026-04-28', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (19, '2026-04-28', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (19, '2026-04-28', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

insert into SESIONES
(id_pelicula, fecha, horario, sala, tipo_proyeccion, precio)
VALUES
    (20, '2026-04-30', 'H16_00', 'SALA_1', 'NORMAL', 8.00),
    (20, '2026-04-30', 'H18_30', 'SALA_2', 'IMAX', 10.50),
    (20, '2026-04-30', 'H21_00', 'SALA_3', 'VOSE', 9.00),
    (20, '2026-04-30', 'H23_30', 'SALA_4', 'TRES_D', 11.00);

ALTER TABLE SESIONES ALTER COLUMN ID RESTART WITH 71;
