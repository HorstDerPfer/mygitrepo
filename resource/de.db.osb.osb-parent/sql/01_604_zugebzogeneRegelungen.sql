-- ab Revision 3211 #604
create table kid (ID number(10,0) not null, anzahlKID number(10,0), timestampKID date, primary key (ID));
alter table massnahme rename COLUMN kid1 TO anzahlKID;
alter table massnahme add timestampKID date;
create table massnahme_kid (massnahme_ID number(10,0) not null, kID4_ID number(10,0) not null, nr number(10,0) not null, primary key (massnahme_ID, nr), unique (kID4_ID));
alter table massnahme_kid add constraint FK66D1A080FA94494C foreign key (kID4_ID) references kid;
alter table massnahme_kid add constraint FK66D1A08050FD54 foreign key (massnahme_ID) references massnahme;

alter table zug add bza varchar2(255 char);
create table regelungabw (ID number(10,0) not null, art varchar2(255 char), text varchar2(255 char), giltIn_ID number(10,0), primary key (ID));
create table zug_regelungabw (zug_ID number(10,0) not null, regelungen_ID number(10,0) not null, regelungnr number(10,0) not null, primary key (zug_ID, regelungnr), unique (regelungen_ID));
alter table zug_regelungabw add constraint FK4FC8C190FC62E3F4 foreign key (zug_ID) references zug;
alter table zug_regelungabw add constraint FK4FC8C1908D0B55DB foreign key (regelungen_ID) references regelungabw;

update bearbeitungsbereich set name = 'Team Konzeptionelle Planung' where name = 'Team Umleitung';
commit;