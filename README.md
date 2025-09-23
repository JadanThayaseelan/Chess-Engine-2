Chess Engine

The move generation system was created using bitboards and magic bitboards for performance.

The AI uses minimax, alpha-beta pruning, transposition tables, iterative deepening and quiescence search to deteremine the best move based on chess position using an evaluation function.

The evaluation function compares the materials of white and black and the location of each piece using PSTs to evaluate the board and determine who is winning.
