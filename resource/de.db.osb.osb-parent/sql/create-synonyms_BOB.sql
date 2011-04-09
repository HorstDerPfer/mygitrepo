-- Settings
DEFINE d_instance=flip

-- Connect als Schema-Owner
connect OSBADM_BOB/geheim@&d_instance

-- Rechte an die user vergeben
DECLARE
    stmt VARCHAR2(1000);
BEGIN
    for myrec in (select  owner,object_name, object_type 
                    from  all_objects 
                    where object_type in ('TABLE')
                    and owner in ('OSBADM_BOB')
                    ) loop
        stmt := 'GRANT SELECT ON '||myrec.owner||'.'||myrec.object_name||' TO BOB_REPL';
        execute immediate stmt;
        stmt := 'GRANT SELECT, INSERT, UPDATE, DELETE ON '||myrec.owner||'.'||myrec.object_name||' TO BOB_USER';
        execute immediate stmt;
    end loop;
END;
/

-- Connect als technischer User
connect BOB_USER/geheim@&d_instance
-- Synonyme erstellen
DECLARE
    stmt3 VARCHAR2(1000);
BEGIN
    for myrec in (select  object_name, object_type 
                    from  all_objects 
                    where owner = 'OSBADM_BOB'
                      and object_type in ('TABLE','SEQUENCE')) loop
        begin
        stmt3 := 'CREATE SYNONYM BOB_USER.'||myrec.object_name||' for OSBADM_BOB.'||myrec.object_name;
        execute immediate stmt3;
        exception
         when others then null; -- falls das synonym schon existierte
        end;
    end loop;
END;
/
-- Connect als Lese-User
connect BOB_REPL/geheim@&d_instance
-- Synonyme erstellen
DECLARE
    stmt4 VARCHAR2(1000);
BEGIN
    for myrec in (select  object_name, object_type 
                    from  all_objects 
                    where owner = 'OSBADM_BOB'
                      and object_type in ('TABLE','SEQUENCE')
                      ) loop
        begin
          stmt4 := 'CREATE SYNONYM BOB_REPL.'||myrec.object_name||' for OSBADM_BOB.'||myrec.object_name;
          execute immediate stmt4;
        exception
         when others then null; -- falls das synonym schon existierte
        end;
    end loop;
END;
/

exit;
