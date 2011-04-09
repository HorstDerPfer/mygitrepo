-- #600
-- mit diesem Skript werden alle Bauma?nahmen zwischen dem 12.12.2004 und 12.12.2020 migriert, d.h. um das Fahrplanjahr erg?nzt
--
-- OSBADM1_BOB bezeichnet das Datenbankschema, in dem die Tabelle liegt und muss ggfs. angepasst werden!

ALTER TABLE BM ADD FAHRPLANJAHR NUMBER(10,0) NULL;

UPDATE BM SET fahrplanjahr=2005 WHERE BM.beginnfuerterminberechnung >= TO_DATE('12.12.2004', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('10.12.2005', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2006 WHERE BM.beginnfuerterminberechnung >= TO_DATE('11.12.2005', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('16.12.2006', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2007 WHERE BM.beginnfuerterminberechnung >= TO_DATE('17.12.2006', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('15.12.2007', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2008 WHERE BM.beginnfuerterminberechnung >= TO_DATE('16.12.2007', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('13.12.2008', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2009 WHERE BM.beginnfuerterminberechnung >= TO_DATE('14.12.2008', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('12.12.2009', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2010 WHERE BM.beginnfuerterminberechnung >= TO_DATE('13.12.2009', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('11.12.2010', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2011 WHERE BM.beginnfuerterminberechnung >= TO_DATE('12.12.2010', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('10.12.2011', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2012 WHERE BM.beginnfuerterminberechnung >= TO_DATE('11.12.2011', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('15.12.2012', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2013 WHERE BM.beginnfuerterminberechnung >= TO_DATE('16.12.2012', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('14.12.2013', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2014 WHERE BM.beginnfuerterminberechnung >= TO_DATE('15.12.2013', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('13.12.2014', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2015 WHERE BM.beginnfuerterminberechnung >= TO_DATE('14.12.2014', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('12.12.2015', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2016 WHERE BM.beginnfuerterminberechnung >= TO_DATE('13.12.2015', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('10.12.2016', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2017 WHERE BM.beginnfuerterminberechnung >= TO_DATE('11.12.2016', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('16.12.2017', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2018 WHERE BM.beginnfuerterminberechnung >= TO_DATE('17.12.2017', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('15.12.2018', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2019 WHERE BM.beginnfuerterminberechnung >= TO_DATE('16.12.2018', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('14.12.2019', 'DD.MM.YYYY');
UPDATE BM SET fahrplanjahr=2020 WHERE BM.beginnfuerterminberechnung >= TO_DATE('15.12.2019', 'DD.MM.YYYY') AND BM.beginnfuerterminberechnung <= TO_DATE('12.12.2020', 'DD.MM.YYYY');

ALTER TABLE BM MODIFY FAHRPLANJAHR NUMBER(10,0) NOT NULL;
COMMIT;

-- #600 Ergänzung
-- gespeicherten Suchfilter um Attribut Fahrplanjahr ergänzt
ALTER TABLE SEARCHCONFIG ADD fahrplanjahr NUMBER(10,0) NULL;
COMMIT;