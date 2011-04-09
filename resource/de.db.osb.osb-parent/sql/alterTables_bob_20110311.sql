
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

@11_689_BbzrAuswertungen.sql
@12_729_ZvfLoeschen.sql

-- REM if new tables are created the script below has to be executed
-- @create-synonyms_BOB.sql

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
