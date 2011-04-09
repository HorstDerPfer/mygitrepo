--Aenderungen #427
-----------------BBPMASSNAHME------------
/* neue Spalte einfuegen */
alter table BBPMASSNAHME add REGIONALBEREICH varchar2(255 char);
commit;

/*neue Spalte mit den Daten der Alten fuellen */
update BBPMASSNAHME BB set BB.REGIONALBEREICH = 
(select rb.name from BBPMASSNAHME BBP
join REGIONALBEREICH RB on BBP.REGIONALBEREICH_ID = RB.id
where BB.id = BBP.id);
commit;

/*alte Spalte loeschen */
alter table BBPMASSNAHME drop column  REGIONALBEREICH_ID;
commit;
-----------------ENDE BBPMASSNAHME------------

-----------------Baumassnahme------------
/* neue Spalte einfuegen */
alter table BM add REGIONALBEREICHBM varchar2(255 char);
commit;

/*neue Spalte mit den Daten der Alten fuellen */
update BM BB set BB.REGIONALBEREICHBM = 
(select rb.name from BM BBP
join REGIONALBEREICH RB on BBP.REGIONALBEREICHBM_ID = RB.id
where BB.id = BBP.id);
commit;

/*alte Spalte loeschen */
alter table BM drop column  REGIONALBEREICHBM_ID;
commit;
-----------------ENDE Baumassnahme------------
