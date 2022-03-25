# DeepChess
CAPSTONE 3901

DeepChess V1.2/5

The current version of DeepChess implements the board representation and move generation features of the chess engine. The program requires access to a local MYSQL database titled "chess". SQL statements to create and populate the database tables are read form a .sql script and executed once the program starts.

The current algorithms for generating and evaluating moves are still tentative. This project is an experimental approach in exploring new and possibly insightful ways of developing a competent chess engine. The running hypothesis is that Java's objective framework will allow us to better create and manipulate various levels of abstraction towards refining the chess engine's search mechanism. This is especially important because the engine will not rely on reference tables or any other kind of foreknoweledge regarding suitable board states. Instead, the AI component of the engine will rely solely on its own logic and gameplay experience. In terms of managing and coordinating the various interactions between the different agents on the board, Java offers another advantage over standard procedural languages.

MySQL database tables were chosen as an added layer of board represenation for two main reasons: 

1. Various views of the board can be obtained from a single sql table with the right attributes. For instance, from the squares table we can project only white diagonals or black diaganols with a single, simple query.

2. Generating possible moves and traversing the many connections between squares is believed to be made more efficient through applying MYSQL's simple, yet robust syntax, over complex iterative functions on a Java array. This slight processing advantage migh seem trivial in the current version, only because it is currently only relevant to one aspect of the engine's function. It is expected however that the engine's overall time performance will be taxed in exchange for relying so heavily on coded logic and so measures are being taken wherever possible to boost overall efficiency.

The next phase of the project is to design and implement the search and evaluation mechanisms. 


# To Run:
1. Create a MYSQL Database named "CHESS"; (This can be automated but the source code would still need to be edited to grant access to the database);

2. Run the Main Class in the src folder;

3. Enter valid FEN string or use the start fen provided and click 'set board' to configure the internal board and print the resulting board array;

4. Select the player, square and piece and select genrate moves to see attack moves, legal moves and blocked moves;

# Note:
The gui is meant to demonstrate basic board representation and board generation. The engine can translate between a fen string and its internal board rep but this is mainly necessary for UCI integration. Pawn moves from certain squares will not be generated because the parsing of a pawn's moves is decided during standard gameplay.
