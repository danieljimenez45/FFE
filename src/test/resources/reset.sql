
drop table if exists SESIONES;
drop table if exists PELICULAS;


create table PELICULAS (
    id bigint primary key auto_increment,
    titulo varchar(60) not null,
    genero varchar(30) not null,
    sinopsis varchar(250) not null,
    duracion int not null,
    director varchar(40) not null,
    estreno date not null,
    clasificacion_edad varchar(20) not null,
    activa boolean not null

);

create table SESIONES (
    id bigint primary key auto_increment,
    id_pelicula bigint not null,
    fecha date not null,
    horario varchar(10) not null,
    sala varchar(10) not null,
    tipo_proyeccion varchar(20) not null,
    precio decimal(10,2) not null,

    constraint fk_sesion_pelicula
        foreign key (id_pelicula) references PELICULAS(id)
            on delete cascade
);