package Main;

import java.util.HashMap;
import java.util.Locale;

public class Evaluation
{
    int[][] pawnTable = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-20,-20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    int[][] knightTable = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    int[][] bishopTable = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}
    };

    int[][] rookTable = {
            { 0,  0,  5,  10, 10, 5,  0,  0},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {5,  10, 10, 10, 10, 10, 10, 5},
            {0,  0,  0,  0,  0,  0,  0,  0},
    };

    int[][] queenTable = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    int[][] kingTable = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {20,  20,  0,   0,   0,   0,   20,  20},
            {20,  30,  10,  0,   0,   10,  30,  20}
    };


    int[] pieceValues = new int[]{100, 320, 330, 500, 900, 20000, 100, 320, 330, 500, 900, 20000};

    public int evaluate(long[] bitboards, Game game)
    {
        return (getColourEvaluation(true, bitboards) - getColourEvaluation(false, bitboards));

        //return getColourEvaluation("B", bitboards) - getColourEvaluation("W", bitboards);
    }

    public int evaluateBasedOnSide(long[] bitboards, Game game)
    {
        if(game.turn % 2 == 0)
        {
            return getColourEvaluation(true, bitboards) - getColourEvaluation(false, bitboards);
        }

        return getColourEvaluation(false, bitboards) - getColourEvaluation(true, bitboards);
    }




    public int getColourEvaluation(boolean isWhite, long[] bitboards)
    {
        int start = 0;
        int end = 5;
        if(!isWhite)
        {
            start = 6;
            end = 11;
        }

        int evaluation = 0;


        for(int i = start; i <= end; i++)
        {
            long currentPieceBitboard = bitboards[i];

            while(currentPieceBitboard != 0)
            {
                int currentSquare = Long.numberOfLeadingZeros(currentPieceBitboard);
                currentPieceBitboard = currentPieceBitboard & ~(1L << 63 - currentSquare);

                int row = currentSquare / 8;
                int col = currentSquare % 8;
                evaluation += pieceValues[i] + getPieceEvaluation(i, row, col);
            }
        }


        return evaluation;
    }



    public int getPieceEvaluation(int pieceIndex, int row, int column)
    {
        return switch (pieceIndex) {
            case 0 -> pawnTable[row][column];
            case 6 -> pawnTable[7 - row][column];

            case 1 -> knightTable[row][column];
            case 7 -> knightTable[7 - row][column];

            case 2 -> bishopTable[row][column];
            case 8 -> bishopTable[7 - row][column];

            case 3 -> rookTable[row][column];
            case 9 -> rookTable[7 - row][column];

            case 4 -> queenTable[row][column];
            case 10 -> queenTable[7 - row][column];

            case 5 -> kingTable[row][column];
            case 11 -> kingTable[7 - row][column];
            default -> 0;
        };
    }
}
