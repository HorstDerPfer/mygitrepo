insert into EVUGRUPPE values(HIBERNATE_SEQUENCE.NEXTVAL, 'N.E.B.');
insert into EVUGRUPPE values(HIBERNATE_SEQUENCE.NEXTVAL, '(nicht verwendet)');

update EVU e1 set name = 'Nostalgie Express Berlin e.V.', KURZBEZEICHNUNG = 'N.E.B.', 
EVUGRUPPE_ID = (select EG.id 
from EVUGRUPPE EG where EG.name = 'N.E.B.') 
WHERE kundennr='B3010';

update EVU set name = 'TSD Transport-Schienen-Dienst GmbH', KURZBEZEICHNUNG = 'TSD', 
EVUGRUPPE_ID = (select EG.id 
from EVUGRUPPE EG where EG.name = 'TSD') 
where KUNDENNR='D3139';

update EVU set KURZBEZEICHNUNG = '(n/a)',
	EVUGRUPPE_ID = (select EG.id from EVUGRUPPE EG where EG.name = '(nicht verwendet)') 
where KUNDENNR='L4556';