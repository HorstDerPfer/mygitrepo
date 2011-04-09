
-- Strukturänderung
alter table zug add massnahme_id number(10,0);
alter table zug add nr number(10,0);
alter table zug add constraint FK1D88C50FD54 foreign key (massnahme_id) references massnahme;

-- Datenmigration
update zug z1 set massnahme_id = (select massnahme_id from massnahme_zug where zug_id = z1.id);
update zug z1 set nr = (select nr from massnahme_zug where zug_id = z1.id);

-- nicht mehr benötigte Constraints löschen
-- die Daten bleiben zunächst gespeichert, als Backup

DECLARE
  ITEMEXISTS NUMBER;
BEGIN
  ITEMEXISTS := 0;
  SELECT COUNT(CONSTRAINT_NAME) INTO ITEMEXISTS FROM ALL_CONSTRAINTS WHERE UPPER(TABLE_NAME) = UPPER('MASSNAHME_ZUG') AND UPPER(CONSTRAINT_NAME) = UPPER('FK66D1DA46FC62E3F4') AND CONSTRAINT_TYPE = 'R';
  IF ITEMEXISTS > 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE MASSNAHME_ZUG DROP CONSTRAINT FK66D1DA46FC62E3F4';
  END IF;

  ITEMEXISTS := 0;
  SELECT COUNT(CONSTRAINT_NAME) INTO ITEMEXISTS FROM ALL_CONSTRAINTS WHERE UPPER(TABLE_NAME) = UPPER('MASSNAHME_ZUG') AND UPPER(CONSTRAINT_NAME) = UPPER('FK66D1DA4650FD54') AND CONSTRAINT_TYPE = 'R';

  IF ITEMEXISTS > 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE MASSNAHME_ZUG DROP CONSTRAINT FK66D1DA4650FD54';
  END IF;
END;
/

commit;