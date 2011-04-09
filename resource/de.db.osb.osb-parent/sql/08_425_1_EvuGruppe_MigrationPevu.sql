
declare
  -- Variablen deklarieren
  v_last_pevu_id number(10, 0);
  v_newid number(10, 0);
  
  cursor c_new_pevu_records is
    select
        bm_pevu.bm_id,  -- ID der Baumaßnahme
        pevu.evugruppe_id,
        max(pevu.BKONZEPTEVU) as BKONZEPTEVU, max(pevu.BKONZEPTEVU_SOLL) as BKONZEPTEVU_SOLL,
        max(pevu.EINGABEGFDZ) as EINGABEGFDZ, max(pevu.EINGABEGFDZ_SOLL) as EINGABEGFDZ_SOLL,
        max(pevu.FPLO) as FPLO, max(pevu.FPLO_SOLL) as FPLO_SOLL,
        max(pevu.MASTERUEBPV) as MASTERUEBPV, max(pevu.MASTERUEBPV_SOLL) as MASTERUEBPV_SOLL,
        max(pevu.STELLUNGNAHMEEVU) as STELLUNGNAHMEEVU, max(pevu.STELLUNGNAHMEEVU_SOLL) as STELLUNGNAHMEEVU_SOLL,
        max(pevu.STUDIEGROBKONZEPT) as STUDIEGROBKONZEPT, max(pevu.STUDIEGROBKONZEPT_SOLL) as STUDIEGROBKONZEPT_SOLL,
        max(pevu.UEBPV) as UEBPV, max(pevu.UEBPV_SOLL) as UEBPV_SOLL,
        max(pevu.ZVF) as ZVF, max(pevu.ZVFENTWURF) as ZVFENTWURF, max(pevu.ZVFENTWURF_SOLL) as ZVFENTWURF_SOLL,
        max(pevu.ZVF_SOLL) as ZVF_SOLL,
        max(pevu.AUSFAELLESEV) as AUSFAELLESEV,
        max(pevu.BKONZEPTEVUERFORDERLICH) as BKONZEPTEVUERFORDERLICH,
        max(pevu.EINGABEGFDZERFORDERLICH) as EINGABEGFDZERFORDERLICH,
        max(pevu.FPLOERFORDERLICH) as FPLOERFORDERLICH,
        max(pevu.MASTERUEBPVERFORDERLICH) as MASTERUEBPVERFORDERLICH,
        max(pevu.STELLUNGNAHMEEVUERFORDERLICH) as STELLUNGNAHMEEVUERFORDERLICH,
        max(pevu.UEBPVERFORDERLICH) as UEBPVERFORDERLICH,
        max(pevu.ZVFENTWURFERFORDERLICH) as ZVFENTWURFERFORDERLICH,
        max(pevu.ZVFERFORDERLICH) as ZVFERFORDERLICH
        
      from bm_terminuebersicht_pevu bm_pevu
        inner join terminuebersicht_pevu pevu on bm_pevu.pevus_id = pevu.id
      
      GROUP BY bm_pevu.bm_id, pevu.evugruppe_id;
  vc_new_pevu c_new_pevu_records%ROWTYPE;
begin
  --
  -- Vorbereitung
  --  Link zur Baumaßnahme sichern
  update terminuebersicht_pevu pevu set baumassnahme_id = (select bm_id from bm_terminuebersicht_pevu where pevus_id = pevu.id);

  -- höchste IDs der Schnittstellentabellen speichern (wichtig für Aufräumen am Ende)
  select max(id) into v_last_pevu_id from terminuebersicht_pevu;
  dbms_output.put_line('v_last_pevu_id = '||v_last_pevu_id);
  
  --
  -- Migration
  -- 1. in allen Terminübersichten die EVUGruppe_ID ergänzen
  dbms_output.put_line('EVUGRUPPE_ID in PEVU Schnittstelle aktualisieren...');
  update terminuebersicht_pevu t1 set evugruppe_id = (select evugruppe_id from evu sub where sub.id = t1.evu_id);

  -- 2. Terminübersichten neu erstellen
  -- Daten für neu zu erstellende Schnittstellendatensätze abfragen
  open c_new_pevu_records;
  loop
    fetch c_new_pevu_records into vc_new_pevu;
    exit when c_new_pevu_records%NOTFOUND;
    
    select hibernate_sequence.nextval into v_newid from dual;

-- uncommenting line below causes an buffer overflow
--    dbms_output.put_line('neuen Datensatz erzeugen ID=' ||v_newid || ' (' || c_new_pevu_records%ROWCOUNT || ')');
    
    -- nach EVU Gruppe aggregierte Schnittstellendaten schreiben
    insert into terminuebersicht_pevu (ID,
      BKONZEPTEVU, BKONZEPTEVU_SOLL, BKONZEPTEVUERFORDERLICH,
      AUSFAELLESEV,
      EINGABEGFDZ, EINGABEGFDZERFORDERLICH, EINGABEGFDZ_SOLL,
      EVUGRUPPE_ID, EVU_ID,
      FPLO, FPLOERFORDERLICH, FPLO_SOLL,
      MASTERUEBPV, MASTERUEBPVERFORDERLICH, MASTERUEBPV_SOLL,
      STELLUNGNAHMEEVU, STELLUNGNAHMEEVUERFORDERLICH, STELLUNGNAHMEEVU_SOLL,
      STUDIEGROBKONZEPT, STUDIEGROBKONZEPT_SOLL,
      UEBPV, UEBPVERFORDERLICH, UEBPV_SOLL,
      ZVF, ZVFENTWURF, ZVFENTWURFERFORDERLICH, ZVFENTWURF_SOLL,
      ZVFERFORDERLICH, ZVF_SOLL
    ) values (
      v_newid,
      vc_new_pevu.BKONZEPTEVU, vc_new_pevu.BKONZEPTEVU_SOLL, vc_new_pevu.BKONZEPTEVUERFORDERLICH,
      vc_new_pevu.AUSFAELLESEV,
      vc_new_pevu.EINGABEGFDZ, vc_new_pevu.EINGABEGFDZERFORDERLICH, vc_new_pevu.EINGABEGFDZ_SOLL,
      vc_new_pevu.EVUGRUPPE_ID, null, 
      vc_new_pevu.FPLO, vc_new_pevu.FPLOERFORDERLICH, vc_new_pevu.FPLO_SOLL,
      vc_new_pevu.MASTERUEBPV, vc_new_pevu.MASTERUEBPVERFORDERLICH, vc_new_pevu.MASTERUEBPV_SOLL,
      vc_new_pevu.STELLUNGNAHMEEVU, vc_new_pevu.STELLUNGNAHMEEVUERFORDERLICH, vc_new_pevu.STELLUNGNAHMEEVU_SOLL,
      vc_new_pevu.STUDIEGROBKONZEPT, vc_new_pevu.STUDIEGROBKONZEPT_SOLL,
      vc_new_pevu.UEBPV, vc_new_pevu.UEBPVERFORDERLICH, vc_new_pevu.UEBPV_SOLL,
      vc_new_pevu.ZVF, vc_new_pevu.ZVFENTWURF, vc_new_pevu.ZVFENTWURFERFORDERLICH, vc_new_pevu.ZVFENTWURF_SOLL,
      vc_new_pevu.ZVFERFORDERLICH, vc_new_pevu.ZVF_SOLL
    );

  -- alte Schnittstellen-Links löschen
-- uncommenting line below causes an buffer overflow
--  dbms_output.put_line('bm_terminuebersicht_pevu löschen: ID = ' || VC_NEW_pevu.BM_ID);
  delete from bm_terminuebersicht_pevu where BM_ID = VC_NEW_pevu.BM_ID and pevuS_ID <= V_LAST_pevu_ID;
  -- alte Schnittstellendaten bleiben als Backup erhalten und können später gelöscht werden
  
  -- neue Schnittstellendaten mit Baumaßnahme verknüpfen
  insert into bm_terminuebersicht_pevu (bm_id, pevus_id) values (VC_NEW_pevu.BM_ID, v_newid);
  
  end loop;
  close c_new_pevu_records;

  commit;
end;
/
