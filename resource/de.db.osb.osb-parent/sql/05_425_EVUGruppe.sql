--#425 
--Neue tabelle EVUgruppe
create table evugruppe (ID number(10,0) not null, name varchar2(255 char), primary key (ID));
-- EVU aendern
ALTER TABLE evu ADD (
KURZBEZEICHNUNG varchar2(255 CHAR),
EVUGRUPPE_ID number(10,0)
);
alter table evu add constraint FK189E43274AAF8 foreign key (evugruppe_ID) references evugruppe;
commit;