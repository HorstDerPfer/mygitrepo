-- #14
drop index regelungid;
create index regelungId on Regelung (regelungId asc);
commit;