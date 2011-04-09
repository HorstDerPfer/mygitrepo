
--
-- Die Rolle ADMINISTRATOR_ZENTRAL erhält Berechtigungen zum Verwenden der SQL-Auswertungsfunktionen
--

insert into sec_authorization (id, name) values (30, 'ROLE_SQLQUERY_ANLEGEN_ALLE');
insert into sec_authorization (id, name) values (31, 'ROLE_SQLQUERY_BEARBEITEN_ALLE');
insert into sec_authorization (id, name) values (32, 'ROLE_SQLQUERY_LOESCHEN_ALLE');
insert into sec_authorization (id, name) values (33, 'ROLE_SQLQUERY_AUSFUEHREN_ALLE');

insert into sec_role_authorization (role_id, authorization_id) values (1, 30);
insert into sec_role_authorization (role_id, authorization_id) values (1, 31);
insert into sec_role_authorization (role_id, authorization_id) values (1, 32);
insert into sec_role_authorization (role_id, authorization_id) values (1, 33);