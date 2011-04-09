rem Ausfuehren von Shemaaenderungen in der Datenbank und updte der vorhandenen Datensaetze
rem
rem--------------------------------------------------------------------------
rem
SET DEFINE ON
-- todo comment
SET TIMING ON
set concat off
-- 
set echo on
-- 
set verify on
-- 
set feedback on

rem ========HIER muss der Admin User der OSB-Datenbank angegeben werden!!! ========
define OSB_ADM=OSBADM_BOB
rem ========HIER muss das Admin Passwort der OSB-Datenbank angegeben werden!!! ========
define pwd_OSB_ADM=pass_wor
rem ========HIER muss der Connection spezifiziert werden !!! ========
define connection=FLIP
rem ========HIER muss der SchemaOwner spezifiziert werden !!! ========
define shemaOwner=OSBADM_BOB
