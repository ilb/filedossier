drop schema if exists filedossier;
create schema filedossier;
grant all privileges on filedossier.* to 'filedossier'@'localhost' identified by 'filedossier';
grant all privileges on filedossier.* to 'filedossier'@'%' identified by 'filedossier';

