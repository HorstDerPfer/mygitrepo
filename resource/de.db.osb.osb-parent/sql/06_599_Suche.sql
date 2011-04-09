-- #599
alter table searchconfig add masId varchar2(255 char);
alter table searchconfig add korridorZeitfenster varchar2(255 char);
alter table searchconfig add aenderungen number(1,0);
alter table searchconfig add ausfaelle number(1,0);
alter table searchconfig add art_a number(1,0);
alter table searchconfig add art_b number(1,0);
alter table searchconfig add art_ks number(1,0);
alter table searchconfig add art_qs number(1,0);
COMMIT;
