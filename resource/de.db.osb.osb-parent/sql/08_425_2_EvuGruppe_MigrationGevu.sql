
declare
  -- Variablen deklarieren
  v_last_gevu_id number(10, 0);
  v_newid number(10, 0);
  
  cursor c_new_gevu_records is
    select
        bm_gevu.bm_id,  -- ID der Baumaßnahme
        gevu.evugruppe_id,
        max(gevu.EINGABEGFDZ) as EINGABEGFDZ, max(gevu.EINGABEGFDZ_SOLL) as EINGABEGFDZ_SOLL,
        max(gevu.FPLO) as FPLO, max(gevu.FPLO_SOLL) as FPLO_SOLL,
        max(gevu.MASTERUEBGV) as MASTERUEBGV, max(gevu.MASTERUEBGV_SOLL) as MASTERUEBGV_SOLL,
        max(gevu.STELLUNGNAHMEEVU) as STELLUNGNAHMEEVU, max(gevu.STELLUNGNAHMEEVU_SOLL) as STELLUNGNAHMEEVU_SOLL,
        max(gevu.STUDIEGROBKONZEPT) as STUDIEGROBKONZEPT, max(gevu.STUDIEGROBKONZEPT_SOLL) as STUDIEGROBKONZEPT_SOLL,
        max(gevu.UEBGV) as UEBGV, max(gevu.UEBGV_SOLL) as UEBGV_SOLL,
        max(gevu.ZVF) as ZVF, max(gevu.ZVFENTWURF) as ZVFENTWURF, max(gevu.ZVFENTWURF_SOLL) as ZVFENTWURF_SOLL,
        max(gevu.ZVF_SOLL) as ZVF_SOLL,
        max(gevu.EINGABEGFDZERFORDERLICH) as EINGABEGFDZERFORDERLICH,
        max(gevu.FPLOERFORDERLICH) as FPLOERFORDERLICH,
        max(gevu.MASTERUEBGVERFORDERLICH) as MASTERUEBGVERFORDERLICH,
        max(gevu.STELLUNGNAHMEEVUERFORDERLICH) as STELLUNGNAHMEEVUERFORDERLICH,
        max(gevu.UEBGVERFORDERLICH) as UEBGVERFORDERLICH,
        max(gevu.ZVFENTWURFERFORDERLICH) as ZVFENTWURFERFORDERLICH,
        max(gevu.ZVFERFORDERLICH) as ZVFERFORDERLICH
        
      from bm_terminuebersicht_gevu bm_gevu
        inner join terminuebersicht_gevu gevu on bm_gevu.gevus_id = gevu.id
      
      GROUP BY bm_gevu.bm_id, gevu.evugruppe_id;
  vc_new_gevu c_new_gevu_records%ROWTYPE;
begin
  --
  -- Vorbereitung
  --  Link zur Baumaßnahme sichern
  update terminuebersicht_gevu gevu set baumassnahme_id = (select bm_id from bm_terminuebersicht_gevu where gevus_id = gevu.id);

  -- höchste IDs der Schnittstellentabellen speichern (wichtig für Aufräumen am Ende)
  select max(id) into v_last_gevu_id from terminuebersicht_gevu;
  dbms_output.put_line('v_last_gevu_id = '||v_last_gevu_id);
  
  --
  -- Migration
  -- 1. in allen Terminübersichten die EVUGruppe_ID ergänzen
  dbms_output.put_line('EVUGRUPPE_ID in GEVU Schnittstelle aktualisieren...');
  update terminuebersicht_gevu t1 set evugruppe_id = (select evugruppe_id from evu sub where sub.id = t1.evu_id);

  -- 2. Terminübersichten neu erstellen
  -- Daten für neu zu erstellende Schnittstellendatensätze abfragen
  open c_new_gevu_records;
  loop
    fetch c_new_gevu_records into vc_new_gevu;
    exit when c_new_gevu_records%NOTFOUND;
    
    select hibernate_sequence.nextval into v_newid from dual;
    
-- uncommenting line below may cause an buffer overflow
--    dbms_output.put_line('neuen Datensatz erzeugen ID=' ||v_newid || ' (' || c_new_gevu_records%ROWCOUNT || ')');
    
    -- nach EVU Gruppe aggregierte Schnittstellendaten schreiben
    insert into terminuebersicht_gevu (ID,
      EINGABEGFDZ, EINGABEGFDZERFORDERLICH, EINGABEGFDZ_SOLL,
      EVUGRUPPE_ID, EVU_ID,
      FPLO, FPLOERFORDERLICH, FPLO_SOLL,
      MASTERUEBGV, MASTERUEBGVERFORDERLICH, MASTERUEBGV_SOLL,
      STELLUNGNAHMEEVU, STELLUNGNAHMEEVUERFORDERLICH, STELLUNGNAHMEEVU_SOLL,
      STUDIEGROBKONZEPT, STUDIEGROBKONZEPT_SOLL,
      UEBGV, UEBGVERFORDERLICH, UEBGV_SOLL,
      ZVF, ZVFENTWURF, ZVFENTWURFERFORDERLICH, ZVFENTWURF_SOLL,
      ZVFERFORDERLICH, ZVF_SOLL
    ) values (
      v_newid,
      vc_new_gevu.EINGABEGFDZ, vc_new_gevu.EINGABEGFDZERFORDERLICH, vc_new_gevu.EINGABEGFDZ_SOLL,
      vc_new_gevu.EVUGRUPPE_ID, null, 
      vc_new_gevu.FPLO, vc_new_gevu.FPLOERFORDERLICH, vc_new_gevu.FPLO_SOLL,
      vc_new_gevu.MASTERUEBGV, vc_new_gevu.MASTERUEBGVERFORDERLICH, vc_new_gevu.MASTERUEBGV_SOLL,
      vc_new_gevu.STELLUNGNAHMEEVU, vc_new_gevu.STELLUNGNAHMEEVUERFORDERLICH, vc_new_gevu.STELLUNGNAHMEEVU_SOLL,
      vc_new_gevu.STUDIEGROBKONZEPT, vc_new_gevu.STUDIEGROBKONZEPT_SOLL,
      vc_new_gevu.UEBGV, vc_new_gevu.UEBGVERFORDERLICH, vc_new_gevu.UEBGV_SOLL,
      vc_new_gevu.ZVF, vc_new_gevu.ZVFENTWURF, vc_new_gevu.ZVFENTWURFERFORDERLICH, vc_new_gevu.ZVFENTWURF_SOLL,
      vc_new_gevu.ZVFERFORDERLICH, vc_new_gevu.ZVF_SOLL
    );

  -- alte Schnittstellen-Links löschen
	-- uncommenting line below may cause an buffer overflow
--    dbms_output.put_line('bm_terminuebersicht_gevu löschen: ID = ' || VC_NEW_GEVU.BM_ID);
  delete from bm_terminuebersicht_gevu where BM_ID = VC_NEW_GEVU.BM_ID and GEVUS_ID <= V_LAST_GEVU_ID;
  -- alte Schnittstellendaten bleiben als Backup erhalten und können später gelöscht werden
  
  -- neue Schnittstellendaten mit Baumaßnahme verknüpfen
  insert into bm_terminuebersicht_gevu (bm_id, gevus_id) values (VC_NEW_GEVU.BM_ID, v_newid);
  
  end loop;
  close c_new_gevu_records;

  commit;
end;
/
