# DeepChess
CAPSTONE 3901

DeepChess V1.2/5

The current version of DeepChess implements implements the board representation and move generation features of teh chess engine. The program requires access to a local MYSQL database title "chess". SQL statements to create and populate the database tables are read form a .sql script and executed once the Main class is executed.

The current algorithms for generating and parsing moves are still tentative. This project is an experimental approach in exploring new and possibly insightful ways of developing a competent chess engine. The running hypothesis is that Java's objective framework will allow us to better create and manipulate various levels of abstraction towards refining the chess engine's msearch mechanism. This is especially important because the engine will not rely on reference tables or any other kind of foreknoweledge regarding suitable board states. Instead, the AI component of the engine will rely solely on its own logic and gameplay experience. In terms of managing and coordinating the various interactions between the different agents on the board, Java also offers an advantage.

MySQL database tables were chosen as an added layer of board represenation for two main reasons: 

1. Generating possible moves and traversing the many connections between squares is believed to be made more efficient through MYSQL's simple, yet powerful syntax, over applying complex iterative functions on a Java array. This slight processing advantage migh seem trivial to the current version, only because it is currently only relevant to move generation. It is expected however, that the engine's time performance will be taxed heavily in exchange for relying so heavily on coded logic and so measures are being taken wherever possible to boost overall efficiency.

2. Various views of the board can be obtained from a single sql table with the write attributes. For instance, from the squares table we can project only white diagonals or black diaganols with a single, simple query.

The next phase of the project is to design and implement the search and evaluation mechanism. 


# To Run:
1. Create a MYSQL Database Table named "CHESS"; (This can be automated but the source code would still need to be edited to grant access to the database;

2. Run the Main Class;

# Note:
The gui is meant to demonstrate basic board representation. The engine can translate between a fen string and its internal board rep but this is mainly necessary for UCI integration. Some moves will not be generate, such as ...
