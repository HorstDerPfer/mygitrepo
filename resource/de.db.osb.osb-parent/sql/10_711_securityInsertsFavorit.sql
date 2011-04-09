
--
--Die Rolle unten genannten verschiedenen Rollen erhalten 
--Berechtigungen zum Beabeiten von Favoriten einer Baumassnahme
--

insert into sec_authorization (id, name) values (34, 'ROLE_FAVORIT_BEARBEITEN_ALLE');
insert into sec_authorization (id, name) values (134, 'ROLE_FAVORIT_BEARBEITEN_REGIONALBEREICH');

-- Rolle ADMINISTRATOR_ZENTRAL
insert into sec_role_authorization (role_id, authorization_id) values (1, 34);
insert into sec_role_authorization (role_id, authorization_id) values (1, 134);

-- Rolle ADMINISTRATOR_REGIONAL
insert into sec_role_authorization (role_id, authorization_id) values (2, 34);
insert into sec_role_authorization (role_id, authorization_id) values (2, 134);

-- Rolle BEARBEITER_ZENTRAL
insert into sec_role_authorization (role_id, authorization_id) values (3, 34);
insert into sec_role_authorization (role_id, authorization_id) values (3, 134);

-- Rolle BEARBEITER_REGIONAL
insert into sec_role_authorization (role_id, authorization_id) values (4, 34);
insert into sec_role_authorization (role_id, authorization_id) values (4, 134);

-- Rolle AUSWERTER_ZENTRAL
insert into sec_role_authorization (role_id, authorization_id) values (7, 34);
insert into sec_role_authorization (role_id, authorization_id) values (7, 134);

-- Rolle AUSWERTER_REGIONAL
insert into sec_role_authorization (role_id, authorization_id) values (8, 34);
insert into sec_role_authorization (role_id, authorization_id) values (8, 134);
