# Chess Engine
This chess engine was created in Java with Spring for the GUI.
A lot of the techniques I used are explained in detail on the [Chess Programming Wiki](https://chessprogramming.org)

This project can be broken down into:
* Movement Generation System
* Searching System
* Evaluation

## Movement Generation
The chess board is represented as bitboards (64-bit integers). There are 12 bitboards, one for every piece and colour. 
For example this white pawn bitboard

00000000
00000000
00000000
00000000
00000000
00000000
11111111
00000000

represents the white pawns in the starting position.

The reason we store positions like this is because we can perform bit shifts to determine potential moves much quicker compared to array based methods. 
For pawns, knights and kings we can calculate the possible moves at compile team from each square and then during run time xor the possible moves and enemy pieces to determine valid moves.

For bishops, rooks and queens we use magic bitboards where we determine the possible moves at every position but we also take into account pieces that block the paths using some clever bitshifts and hashing we can figure this out for every position and possible blocker arrangement at compile time and store it in an array which then can be accessed very quickly when we want to determine a move.

## Searching System
We use a standard minimax algorithm which calculates every possible move after a state up to a certain depth and compares each position using an evaluation function to determine the best move to make.

This also has some extensions such as:
* Alpha-Beta Pruning
* Transposition Table
* Iterative Deepening
* MVV-LVA
* Quiescence Search 


## Evaluation
Given a board state there is an evaluation function that determines whether white or black has the advantage and this is used to determine what is the best move for the chess engine to make. Piece Square Tables are used which are 8 x 8 matrices for each piece which show the best squares for each piece and this helps determine which moves to make.

## Note
I am currently working on porting this project over to C++
