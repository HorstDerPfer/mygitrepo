
@steuerskript.sql

connect &OSB_ADM/&pwd_OSB_ADM@&connection
set serveroutput on size 1000000
SPOOL steuerskript_osb_bob.log
show user

REM *********************************
REM		NUTZTEIL
REM *********************************
PROMPT Nutzteil
SELECT TO_CHAR(SYSDATE, 'HH24:MI:SS') STARTZEIT FROM DUAL;

@01_604_zugebzogeneRegelungen.sql
@02_427_RegionalbereichBM.sql
@03_600_Fahrplanjahr.sql
@04_14_RegelungIndex.sql
@05_425_EVUGruppe.sql
@06_599_Suche.sql
@07_425_0_ZuweisungEvuGruppeZuEvu.sql
@07_425_1_ZuweisungEvuGruppeZuEvu.sql
@07_425_2_ZuweisungEvuGruppeZuEvu.sql
@07_425_3_ZuweisungEvuGruppeZuEvu.sql
@08_425_0_EvuGruppe_PreMigration.sql
@08_425_1_EvuGruppe_MigrationPevu.sql
@08_425_2_EvuGruppe_MigrationGevu.sql
@09_703_securityInserts.sql
@10_711_securityInsertsFavorit.sql
@12_729_ZvfLoeschen.sql

REM if new tables are created the script below has to be executed
@create-synonyms_BOB.sql

REM Falls in einem Script das Commit vergessen wurde, werden die Aenderungen hier comitted
commit;

REM **********************************************
REM		ENDE
REM **********************************************
PROMPT Skript steuerskript_osb_bob.log executed!
SELECT TO_CHAR(SYSDATE, 'HH24:MI:SS') STARTZEIT FROM DUAL;

show user

spool off

exit;
