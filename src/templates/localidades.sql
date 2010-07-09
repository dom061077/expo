begin;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,1 from buenosaires;

insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,concat(localidad,' DESDE ',altura1,' AL ',altura2),2 from capital;

insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,3 from catamarca;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,4 from chaco;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,5 from chubut;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,6 from cordoba;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,7 from corrientes;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,8 from entrerios;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,9 from formosa;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,10 from jujuy;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,11 from lapampa;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,12 from larioja;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,13 from mendoza;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,14 from misiones;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,15 from neuquen;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,16 from rionegro;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,17 from salta;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,18 from sanjuan;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,19 from sanluis;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,20 from santacruz;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,21 from santafe;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,22 from santiagodelestero;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,23 from tierradelfuego;
insert into expo.localidad(codigo_postal,nombre_loc,provincia_id)
select cp,localidad,24 from tucuman;
commit;


















