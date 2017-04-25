DROP database IF EXISTS chess;

CREATE DATABASE chess;
use chess;

START TRANSACTION;
-- CREATE tables

CREATE TABLE p_def(
	symbol VARBINARY(1), 
	colour VARCHAR(1), 
	def VARCHAR(10),
	val INT,
	p_count INT,
	PRIMARY KEY (symbol));

CREATE TABLE boardrank(
	rank INT, 
	a VARBINARY(1) , 
	b VARBINARY(1) , 
	c VARBINARY(1) , 
	d VARBINARY(1) , 
	e VARBINARY(1) , 
	f VARBINARY(1) , 
	g VARBINARY(1) , 
	h VARBINARY(1) , 
	PRIMARY KEY(rank));
	
CREATE TABLE boardfile(
	file VARCHAR(1), 
	r1 VARBINARY(1) , 
	r2 VARBINARY(1) , 
	r3 VARBINARY(1) , 
	r4 VARBINARY(1) , 
	r5 VARBINARY(1) , 
	r6 VARBINARY(1) , 
	r7 VARBINARY(1) , 
	r8 VARBINARY(1) , 
	PRIMARY KEY(file));
	
CREATE TABLE file_int(
	file VARCHAR(1), 
	f_int INT, 
	PRIMARY KEY (file), 
	FOREIGN KEY (file) REFERENCES boardfile(file));
	
CREATE TABLE squares(
	snum INT NOT NULL auto_increment,
	file VARCHAR(1), 
	rank INT, 
	occupiedBy VARBINARY(1) , 
	squarecolour INT, 
	blackattack VARBINARY(5), 
	whiteattack VARBINARY(5), 
	UNIQUE KEY(snum), 
	PRIMARY KEY(file,rank), 
	FOREIGN KEY(file) REFERENCES boardfile(file), 
	FOREIGN KEY(rank) REFERENCES boardrank(rank));
	
CREATE TABLE Rmoves(
	file VARCHAR(1),
	rank INT,
	occupiedBy VARBINARY(1),
	PRIMARY KEY (file,rank),
	FOREIGN KEY (file) REFERENCES squares(file),
	FOREIGN KEY (rank) REFERENCES squares(rank));
	
CREATE TABLE Nmoves(
	file VARCHAR(1),
	rank INT,
	occupiedBy VARBINARY(1),
	PRIMARY KEY (file,rank),
	FOREIGN KEY (file) REFERENCES squares(file),
	FOREIGN KEY (rank) REFERENCES squares(rank));
	
	
CREATE TABLE Bmoves(
	file VARCHAR(1),
	rank INT,
	occupiedBy VARBINARY(1),
	PRIMARY KEY (file,rank),
	FOREIGN KEY (file) REFERENCES squares(file),
	FOREIGN KEY (rank) REFERENCES squares(rank));
	
CREATE TABLE Qmoves(
	file VARCHAR(1),
	rank INT,
	occupiedBy VARBINARY(1),
	PRIMARY KEY (file,rank),
	FOREIGN KEY (file) REFERENCES squares(file),
	FOREIGN KEY (rank) REFERENCES squares(rank));
	
CREATE TABLE Kmoves(
	file VARCHAR(1),
	rank INT,
	occupiedBy VARBINARY(1),
	PRIMARY KEY(file,rank),
	FOREIGN KEY (file) REFERENCES squares(file),
	FOREIGN KEY (rank) REFERENCES squares(rank));
	
CREATE TABLE Pmoves(
	file VARCHAR(1),
	rank INT,
	occupiedBy VARBINARY(1),
	PRIMARY KEY(file,rank),
	FOREIGN KEY (file) REFERENCES squares(file),
	FOREIGN KEY (rank) REFERENCES squares(rank));
	
	
	
	
-- populate tables
	
START TRANSACTION;
SET autocommit=0;

-- populate p_def

INSERT INTO p_def(symbol,colour,def,val) values('P','w','pawn',1);
INSERT INTO p_def(symbol,colour,def,val) values('R','w','rook',5);
INSERT INTO p_def(symbol,colour,def,val) values('N','w','knight',3);
INSERT INTO p_def(symbol,colour,def,val) values('B','w','bishop',3);
INSERT INTO p_def(symbol,colour,def,val) values('K','w','king',100);
INSERT INTO p_def(symbol,colour,def,val) values('Q','w','queen',9);
INSERT INTO p_def(symbol,colour,def,val) values('p','b','pawn',1);
INSERT INTO p_def(symbol,colour,def,val) values('r','b','rook',5);
INSERT INTO p_def(symbol,colour,def,val) values('n','b','knight',3);
INSERT INTO p_def(symbol,colour,def,val) values('b','b','bishop',3);
INSERT INTO p_def(symbol,colour,def,val) values('k','b','king',100);
INSERT INTO p_def(symbol,colour,def,val) values('q','b','queen',9);

-- populate boardrank

INSERT INTO boardrank VALUES(1,'R','N','B','K','Q','B','N','R');
INSERT INTO boardrank VALUES(2,'P','P','P','P','P','P','P','P');
INSERT INTO boardrank(rank) VALUES(3);
INSERT INTO boardrank(rank) VALUES(4);
INSERT INTO boardrank(rank) VALUES(5);
INSERT INTO boardrank(rank) VALUES(6);
INSERT INTO boardrank VALUES(7,'p','p','p','p','p','p','p','p');
INSERT INTO boardrank VALUES(8,'r','n','b','k','q','b','n','r');

-- populate boardfile

INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('a','R','P','p','r');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('b','N','P','p','n');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('c','B','P','p','b');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('d','Q','P','p','q');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('e','K','P','p','k');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('f','B','P','p','b');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('g','N','P','p','n');
INSERT INTO boardfile(file,r1,r2,r7,r8) VALUES('h','R','P','p','r');

-- populate file_int

INSERT INTO file_int values('a',1);
INSERT INTO file_int values('b',2);
INSERT INTO file_int values('c',3);
INSERT INTO file_int values('d',4);
INSERT INTO file_int values('e',5);
INSERT INTO file_int values('f',6);
INSERT INTO file_int values('g',7);
INSERT INTO file_int values('h',8);

-- populate squares rank & file

INSERT INTO squares(file,rank) 
SELECT DISTINCT boardfile.file,boardrank.rank FROM boardfile,boardrank;

-- SET up squares TABLE

UPDATE squares JOIN boardfile ON boardfile.file=squares.file JOIN boardrank ON boardrank.rank=squares.rank SET occupiedBy = 
CASE 
WHEN boardfile.r1 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r1 
WHEN boardfile.r2 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r2 
WHEN boardfile.r3 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r3 
WHEN boardfile.r4 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r4 
WHEN boardfile.r5 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r5 
WHEN boardfile.r6 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r6 
WHEN boardfile.r7 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r7 
WHEN boardfile.r8 IN (boardrank.a, boardrank.b, boardrank.c, boardrank.d, boardrank.e, boardrank.f, boardrank.g, boardrank.h) THEN boardfile.r8 
END;

UPDATE squares SET squarecolour= 
CASE 
WHEN file IN ('a', 'c', 'e', 'g') AND rank%2=0 THEN 1 
WHEN file IN ('b', 'd', 'f', 'h') AND rank%2>0 THEN 1 
ELSE 0 
END; 

COMMIT; 




-- functions

-- count a particular piece
DROP FUNCTION IF EXISTS p_count; 

DELIMITER $  
CREATE FUNCTION p_count(piece VARBINARY(1)) 
	RETURNS INT 
	NOT DETERMINISTIC 
BEGIN 
DECLARE counter INTEGER DEFAULT 0; 
DECLARE fin INTEGER DEFAULT 0; 
DECLARE p_symbol VARBINARY(1) DEFAULT ""; 

DECLARE c_iter CURSOR FOR 
SELECT occupiedby FROM squares; 

DECLARE CONTINUE HANDLER 
FOR NOT FOUND SET fin=1; 

OPEN c_iter; 
fetch_piece: LOOP 

fetch c_iter INTO p_symbol; 

IF fin =1 THEN
LEAVE fetch_piece; 
END IF; 

IF p_symbol = piece THEN
SET counter = counter + 1; 
END IF; 

END LOOP fetch_piece; 

RETURN counter; 

CLOSE c_iter; 
 
END$ 

DELIMITER ; 

-- get file INT
DROP FUNCTION IF EXISTS f_to_int; 

DELIMITER $ 
CREATE FUNCTION f_to_int(f VARCHAR(1)) 
	RETURNS INT 
	DETERMINISTIC 
BEGIN 
DECLARE fl INT; 

SELECT f_int INTO fl FROM file_int WHERE file=f; 

RETURN fl;  

END$
 
DELIMITER ; 

-- get file
DROP FUNCTION IF EXISTS int_to_f; 

DELIMITER $ 
CREATE FUNCTION int_to_f(fint INT) 
	RETURNS VARCHAR(1) 
	DETERMINISTIC 
BEGIN 
DECLARE f VARCHAR(1); 

SELECT file INTO f FROM file_int WHERE f_int=fint; 

RETURN f; 

END$ 

DELIMITER ; 

-- get piece colour
DROP FUNCTION IF EXISTS getPlayer;  

DELIMITER $ 
CREATE FUNCTION getPlayer(s VARBINARY(1)) 
	RETURNS VARCHAR(1) 
	DETERMINISTIC 
BEGIN 
DECLARE pc VARCHAR(1); 

SELECT colour INTO pc FROM p_def WHERE p_def.symbol=s; 

RETURN pc; 

END$ 

DELIMITER ;




-- stored procedures

-- get rook moves
DROP PROCEDURE IF EXISTS getRookMoves; 

DELIMITER # 
CREATE PROCEDURE getRookMoves(IN f VARCHAR(1), IN r INT) 
BEGIN 
DELETE FROM Rmoves; 
INSERT INTO Rmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares WHERE (squares.file=f AND squares.rank<>r) OR (squares.file<>f AND squares.rank=r); 

SELECT * FROM Rmoves; 
 
END#   

DELIMITER ; 

-- get bishop moves
DROP PROCEDURE IF EXISTS getBishopMoves; 


DELIMITER # 
CREATE PROCEDURE getBishopMoves(IN f VARCHAR(1), IN r INT) 
BEGIN 
DECLARE next_rank INT; 
DECLARE prev_rank INT; 
DECLARE next_file INT; 
DECLARE prev_file INT; 
DECLARE nf VARCHAR(1); 
DECLARE pf VARCHAR(1); 

SET next_rank = r+1; 
SET prev_rank = r-1; 
SET next_file = f_to_int(f)+1; 
SET prev_file = f_to_int(f)-1; 

DELETE FROM Bmoves; 

WHILE next_rank<9 OR prev_rank>0 OR next_file<9 OR prev_file>0 DO
	IF next_file<9 THEN
	SET nf = int_to_f(next_file); 
	END IF; 
	IF prev_file>0 THEN
	SET pf = int_to_f(prev_file); 
	END IF; 
	
	IF next_rank<9 AND next_file<9 THEN
	INSERT INTO Bmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=nf AND squares.rank=next_rank; 
	END IF; 
	
	IF next_rank<9 AND prev_file>0 THEN
	INSERT INTO Bmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=pf AND squares.rank=next_rank; 
	END IF; 
	
	IF prev_rank>0 AND next_file<9 THEN
	INSERT INTO Bmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=nf AND squares.rank=prev_rank; 
	END IF; 
	
	IF prev_rank>0 AND prev_file>0 THEN
	INSERT INTO Bmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=pf AND squares.rank=prev_rank; 
	END IF; 

	SET next_rank = next_rank+1; 
	SET prev_rank = prev_rank-1; 
	SET next_file = f_to_int(nf)+1; 
	SET prev_file = f_to_int(pf)-1; 
	
END WHILE; 

SELECT * FROM Bmoves; 
 
END# 
 
DELIMITER ; 

-- get knight moves
DROP PROCEDURE IF EXISTS getKnightMoves; 

DELIMITER #  
CREATE PROCEDURE getKnightMoves(IN f VARCHAR(1), IN r INT) 
BEGIN 
DECLARE r1, r2, r3, r4, f1, f2, f3, f4 INT; 
DECLARE nf1, nf2, pf1, pf2 VARCHAR(1); 

SET r1 = r+1; 
SET r2 = r+2; 
SET r3 = r-1;  
SET r4 = r-2; 
SET f1 = f_to_int(f)+1; 
SET f2 = f_to_int(f)+2; 
SET f3 = f_to_int(f)-1; 
SET f4 = f_to_int(f)-2; 

DELETE FROM Nmoves; 

IF f1<9 THEN
SET nf1 = int_to_f(f1); 
END IF; 
IF f2<9 THEN
SET nf2 = int_to_f(f2); 
END IF; 
IF f3>0 THEN
SET pf1 = int_to_f(f3); 
END IF; 
IF f4>0 THEN
SET pf2 = int_to_f(f4); 
END IF; 

IF r1<9 AND f2<9 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=nf2 AND squares.rank=r1; 
END IF; 

IF r1<9 AND f4>0 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=pf2 AND squares.rank=r1; 
END IF; 

IF r3>0 AND f2<9 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=nf2 AND squares.rank=r3; 
END IF; 

IF r3>0 AND f4>0 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=pf2 AND squares.rank=r3; 
END IF; 

IF r2<9 AND f1<9 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=nf1 AND squares.rank=r2; 
END IF; 

IF r2<9 AND f3>0 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=pf1 AND squares.rank=r2; 
END IF; 

IF r4>0 AND f1<9 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=nf1 AND squares.rank=r4; 
END IF;  

IF r4>0 AND f3>0 THEN
INSERT INTO Nmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares 
WHERE squares.file=pf1 AND squares.rank=r4;  
END IF;  

SELECT * FROM Nmoves; 

END# 
 
DELIMITER ; 

-- generate queen moves
DROP PROCEDURE IF EXISTS getQueenMoves; 

DELIMITER # 
CREATE PROCEDURE getQueenMoves(IN f VARCHAR(1), IN r INT) 
BEGIN 

DECLARE next_rank INT; 
DECLARE prev_rank INT; 
DECLARE next_file INT; 
DECLARE prev_file INT; 
DECLARE nf VARCHAR(1); 
DECLARE pf VARCHAR(1); 

SET next_rank = r+1; 
SET prev_rank = r-1; 
SET next_file = f_to_int(f)+1; 
SET prev_file = f_to_int(f)-1; 

DELETE FROM Qmoves; 

WHILE next_rank<9 OR prev_rank>0 OR next_file<9 OR prev_file>0 DO
	IF next_file<9 THEN
	SET nf = int_to_f(next_file); 
	END IF; 
	IF prev_file>0 THEN
	SET pf = int_to_f(prev_file); 
	END IF; 
	
	IF next_rank<9 AND next_file<9 THEN
	INSERT INTO Qmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=nf AND squares.rank=next_rank; 
	END IF; 
	
	IF next_rank<9 AND prev_file>0 THEN
	INSERT INTO Qmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=pf AND squares.rank=next_rank; 
	END IF; 
	
	IF prev_rank>0 AND next_file<9 THEN
	INSERT INTO Qmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=nf AND squares.rank=prev_rank; 
	END IF; 
	
	IF prev_rank>0 AND prev_file>0 THEN
	INSERT INTO Qmoves (file,rank,occupiedBy)
	SELECT file, rank, occupiedBy FROM squares 
	WHERE squares.file=pf AND squares.rank=prev_rank; 
	END IF; 

	SET next_rank = next_rank+1; 
	SET prev_rank = prev_rank-1; 
	SET next_file = f_to_int(nf)+1; 
	SET prev_file = f_to_int(pf)-1; 
	
END WHILE; 

INSERT INTO Qmoves (file,rank,occupiedBy)
SELECT file, rank, occupiedBy FROM squares WHERE (squares.file=f AND squares.rank<>r) OR (squares.file<>f AND squares.rank=r); 

SELECT * FROM Qmoves;

END# 
 
DELIMITER ; 

-- get king moves
DROP PROCEDURE IF EXISTS getKingMoves;

DELIMITER # 
CREATE PROCEDURE getKingMoves(IN f VARCHAR(1), IN r INT) 
BEGIN 

DECLARE next_rank INT; 
DECLARE prev_rank INT; 
DECLARE next_file INT; 
DECLARE prev_file INT; 
DECLARE nf VARCHAR(1); 
DECLARE pf VARCHAR(1); 

SET next_rank = r+1; 
SET prev_rank = r-1; 
SET next_file = f_to_int(f)+1; 
SET prev_file = f_to_int(f)-1; 

IF next_file<9 THEN
SET nf = int_to_f(next_file); 
END IF; 
IF prev_file>0 THEN
SET pf = int_to_f(prev_file); 
END IF; 

DELETE FROM Kmoves;

IF next_rank<9 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares  
WHERE squares.file=f AND squares.rank=next_rank; 
END IF; 

IF next_rank<9 AND next_file<9 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=nf AND Squares.rank=next_rank; 
END IF; 

IF next_file<9 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=nf AND squares.rank=r; 
END IF; 

IF next_file<9 AND prev_rank>0 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=nf AND squares.rank=prev_rank; 
END IF; 

IF prev_rank>0 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=f AND squares.rank=prev_rank; 
END IF; 

IF prev_rank>0 AND prev_file>0 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=pf AND squares.rank=prev_rank; 
END IF; 

IF prev_file>0 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=pf AND squares.rank=r; 
END IF; 

IF prev_file>0 AND next_rank<9 THEN 
INSERT INTO Kmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=pf AND squares.rank=next_rank; 
END IF; 

SELECT * FROM Kmoves;


END#

DELIMITER ;

-- get pawn moves

DROP PROCEDURE IF EXISTS getPawnMoves;

DELIMITER # 
CREATE PROCEDURE getPawnMoves(IN sym VARBINARY(1), IN f VARCHAR(1), IN r INT) 
BEGIN 

DECLARE next_rank INT; 
DECLARE prev_rank INT; 
DECLARE next_file INT; 
DECLARE prev_file INT; 
DECLARE nf VARCHAR(1); 
DECLARE pf VARCHAR(1); 
DECLARE sym VARBINARY(1);

SET next_rank = r+1; 
SET prev_rank = r-1; 
SET next_file = f_to_int(f)+1; 
SET prev_file = f_to_int(f)-1; 

IF next_file<9 THEN
SET nf = int_to_f(next_file); 
END IF; 
IF prev_file>0 THEN
SET pf = int_to_f(prev_file); 
END IF; 

DELETE FROM Pmoves;

IF getPlayer(sym)='w' THEN 

IF next_rank<9 THEN 
INSERT INTO Pmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=f AND squares.rank=next_rank; 
END IF; 

IF next_rank<9 AND prev_file>0 THEN 
INSERT INTO Pmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=pf AND squares.rank=next_rank; 
END IF; 

IF next_rank<9 AND next_file<9 THEN 
INSERT INTO Pmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=nf AND squares.rank=next_rank; 
END IF; 

END IF; 

IF getPlayer(sym)='b' THEN 

IF prev_rank>0 THEN 
INSERT INTO Pmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=f AND squares.rank=prev_rank; 
END IF; 

IF prev_rank>0 AND prev_file>0 THEN 
INSERT INTO Pmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=pf AND squares.rank=prev_rank; 
END IF;

IF prev_rank>0 AND next_file<9 THEN 
INSERT INTO Pmoves(file,rank,occupiedBy) 
SELECT file,rank,occupiedBy FROM squares 
WHERE squares.file=nf AND squares.rank=prev_rank;
END IF; 

END IF; 

SELECT * FROM Pmoves; 

END# 

DELIMITER ;




-- triggers

-- count pieces after setting pieces
DROP TRIGGER IF EXISTS after_squares_update;  

DELIMITER ! 
CREATE TRIGGER after_squares_update 
AFTER UPDATE ON squares 
FOR EACH ROW 
BEGIN 
DECLARE piece VARBINARY(1); 
DECLARE fin INTEGER DEFAULT 0; 

DECLARE sym_iter CURSOR FOR
SELECT symbol FROM p_def; 

DECLARE CONTINUE HANDLER 
FOR NOT FOUND SET fin=1; 

OPEN sym_iter; 
fetch_sym: LOOP 

fetch sym_iter INTO piece; 

IF fin=1 THEN 
LEAVE fetch_sym; 
END IF; 

Update p_def SET p_count = p_count(piece) WHERE symbol=piece; 

END LOOP fetch_sym; 

CLOSE sym_iter; 
 
END!  

DELIMITER ; 

COMMIT;
