package Test;

import Main.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RookMovesTest
{
    public void testPossibleRookMoves(Game game, int[] startingPosition, int[][] possibleMoves, String piece)
    {
        String[][] pieceToMove = new String[8][8];
        pieceToMove[startingPosition[0]][startingPosition[1]] = piece;

        String[][] expectedPossibleMoves = new String[8][8];

        for(int[] move : possibleMoves)
        {
            expectedPossibleMoves[move[0]][move[1]] = piece;
        }

        long pieceToMoveBitBoard = game.generateBitBoard(pieceToMove, piece);

        long expectedPossibleMovesBitBoard = game.generateBitBoard(expectedPossibleMoves, piece);
        long actualPossibleMovesBitBoard = game.possibleRookMoves(pieceToMoveBitBoard);

        assertEquals(expectedPossibleMovesBitBoard, actualPossibleMovesBitBoard);
    }

    @Test
    @DisplayName("Test rook for no moves")
    void testNoMoves()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{7,7};
        int[][] possibleMoves = new int[][]{};
        testPossibleRookMoves(game, startingPosition, possibleMoves, "R");
    }

    @Test
    @DisplayName("Test white rook responds to blockers correctly")
    void testWhiteMoveBlockers()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", " "}
        });

        int[] startingPosition = new int[]{4,3};
        int[][] possibleMoves = new int[][]{{5, 3}, {3, 3}, {2, 3}, {1, 3}, {4, 0}, {4, 1}, {4, 2}, {4, 4}, {4, 5}, {4, 6}, {4, 7}};
        testPossibleRookMoves(game, startingPosition, possibleMoves, "R");

    }

    @Test
    @DisplayName("Test black rook responds to blocker correctly")
    void testBlackMoveBlockers()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", " "},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });
        game.turn += 1;

        int[] startingPosition = new int[]{4,3};
        int[][] possibleMoves = new int[][]{{5, 3}, {3, 3}, {2, 3}, {6, 3}, {4, 0}, {4, 1}, {4, 2}, {4, 4}, {4, 5}, {4, 6}, {4, 7}};
        testPossibleRookMoves(game, startingPosition, possibleMoves, "r");

    }



}
