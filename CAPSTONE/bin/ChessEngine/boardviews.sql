create table boardrank(rank int, a varchar(1) COLLATE latin1_bin, b varchar(1) COLLATE latin1_bin, c varchar(1) COLLATE latin1_bin, d varchar(1) COLLATE latin1_bin, e varchar(1) COLLATE latin1_bin, f varchar(1) COLLATE latin1_bin, g varchar(1) COLLATE latin1_bin, h varchar(1) COLLATE latin1_bin, primary key(rank));

insert into boardrank values(1,'R','N','B','K','Q','B','N','R');
insert into boardrank values(2,'P','P','P','P','P','P','P','P');
insert into boardrank(rank) values(3);
insert into boardrank(rank) values(4);
insert into boardrank(rank) values(5);
insert into boardrank(rank) values(6);
insert into boardrank values(7,'p','p','p','p','p','p','p','p');
insert into boardrank values(8,'r','n','b','k','q','b','n','r');

create table boardfile(file varchar(1), r1 varchar(1) COLLATE latin1_bin, r2 varchar(1) COLLATE latin1_bin, r3 varchar(1) COLLATE latin1_bin, r4 varchar(1) COLLATE latin1_bin, r5 varchar(1) COLLATE latin1_bin, r6 varchar(1) COLLATE latin1_bin, r7 varchar(1) COLLATE latin1_bin, r8 varchar(1) COLLATE latin1_bin, primary key(file));

insert into boardfile(file,r1,r2,r7,r8) values('a','R','P','p','r');
insert into boardfile(file,r1,r2,r7,r8) values('b','N','P','p','n');
insert into boardfile(file,r1,r2,r7,r8) values('c','B','P','p','b');
insert into boardfile(file,r1,r2,r7,r8) values('d','Q','P','p','q');
insert into boardfile(file,r1,r2,r7,r8) values('e','K','P','p','k');
insert into boardfile(file,r1,r2,r7,r8) values('f','B','P','p','b');
insert into boardfile(file,r1,r2,r7,r8) values('g','N','P','p','n');
insert into boardfile(file,r1,r2,r7,r8) values('h','R','P','p','r');



create table squares(file varchar(1), rank int, occupiedBy varchar(1) COLLATE latin1_bin, colour int, blackattack varchar(5), whiteattack varchar(5), primary key(file,rank), foreign key(file) references boardfile(file), foreign key(rank) references boardrank(rank));

insert into squares(file,rank) select distinct boardfile.file,boardrank.rank from boardfile,boardrank;

update squares join boardfile on boardfile.file=squares.file join boardrank on boardrank.rank=squares.rank set occupiedBy = 
case 
when boardfile.r1 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r1 
when boardfile.r2 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r2 
when boardfile.r3 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r3 
when boardfile.r4 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r4 
when boardfile.r5 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r5 
when boardfile.r6 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r6 
when boardfile.r7 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r7 
when boardfile.r8 in (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) then boardfile.r8 
end;

update squares set colour= 
case 
when file in ('a', 'c', 'e', 'g') and rank%2=0 then 1 
when file in ('b', 'd', 'f', 'h') and rank%2>0 then 1 
else 0 
end;


create table whitePs(piece varchar(1) COLLATE latin1_bin, file varchar(1), rank int, primary key(piece, file, rank), foreign key(file) references boardfile(file) on update cascade, foreign key(rank) references boardrank(rank) on update cascade);
insert into whitePs(piece, file, rank) select occupiedBy, file, rank from squares 
where occupiedBy in ('P','R','N','B','K','Q');


create table blackPs(piece varchar(1) COLLATE latin1_bin, file varchar(1), rank int, primary key(piece, file, rank),  foreign key(file) references boardfile(file) on update cascade, foreign key(rank) references boardrank(rank) on update cascade);
insert into blackPs(piece, file, rank) select occupiedBy, file, rank from squares 
where occupiedBy in ('p','r','n','b','k','q');

