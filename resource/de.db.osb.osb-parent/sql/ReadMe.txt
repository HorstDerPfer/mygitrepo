Mit /nolog bleibt das Skrip hängen! todo: Warum?
z.B. sqlplus OSBADM_IB/password @ alterTables.sql


######## Hinweis zum Ausfuehren der Datenbankupdates ########

Um Schemaaenderungen und Datenupdates durchfuehren zu koennen, muss folgende Schritte durchgefuehrt werden:
1. Steuerscript.sql
	- Anpassen der Zugangsdaten zur Datenbank (Admin-User und Admin-Passwort, Zeilen 11 und 13)
	- Anpassen der Connection (Zeile 15)
	- Anpassen des Schema Owners (Zeile 17)
2. AlterTables.sql
	- es muessen alle SQL-Skripte, welche ausgefuehrt werden sollen, ab Zeile 69 aufgelistet werden

######## Hinweis fuer Entwickler ########

VORGEHEN ZUM EINFUEGEN VON DATENBANKAENDERUNGEN BZW. DATENUPDATES:
1. Pro Ticket ist eine eigene SQL-Datei zu erstellen
2. Die Bezeichnung der Datei folgt dieser Vorgabe:
	- LFDNR_TICKETNUMMER_BEZEICHNUNG.sql
	- Bsp: 01_604_zugebzogeneRegelungen.sql
3. In der Datei "alterTables.sql" muss die zuvor erstellte Datei referenziert werden
	- ab Zeile 69 nach folgender Vorgabe: @LFDNR_TICKETNUMMER_BEZEICHNUNG.sql
	- Bsp: @01_604_zugebzogeneRegelungen.sql

Hinweis: 
sollte es zu einem Ticket mehrere SQL-Dateien geben, dann soll diese Vorgbe gelten:
- LFDNR_TICKETNUMMER_REIHENFOLGENUMMER_BEZEICHNUNG.sql
- Bsp: 
	01_604_1_zugebzogeneRegelungen.sql
	01_604_2_zugebzogeneRegelungen.sql
FERTIG