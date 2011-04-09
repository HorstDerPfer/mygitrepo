--EVUGRUPPEID einfuegen
update evu e1 set e1.evugruppe_id =(
select EG.id 
from EVU E join EVUGRUPPE EG on E.EVUGRUPPENAME = EG.name
where E1.ID = E.ID
);
commit;

alter table EVU drop column EVUGRUPPENAME;
commit;
