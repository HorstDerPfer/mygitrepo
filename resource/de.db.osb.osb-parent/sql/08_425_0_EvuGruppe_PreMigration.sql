
-- #425
alter table terminuebersicht_gevu add baumassnahme_ID number(10,0);
alter table terminuebersicht_pevu add baumassnahme_ID number(10,0);
alter table terminuebersicht_gevu add evugruppe_ID number(10,0);
alter table terminuebersicht_pevu add evugruppe_ID number(10,0);
alter table terminuebersicht_gevu add constraint FK376678913274AAF8 foreign key (evugruppe_ID) references evugruppe;
alter table terminuebersicht_pevu add constraint FK376A8FE83274AAF8 foreign key (evugruppe_ID) references evugruppe;

COMMIT;