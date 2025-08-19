package Test;

import Main.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BishopMovesTest
{
    public void testPossibleBishopMoves(Game game, int[] startingPosition, int[][] possibleMoves, String piece)
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
        long actualPossibleMovesBitBoard = game.possibleBishopMoves(pieceToMoveBitBoard);

        assertEquals(expectedPossibleMovesBitBoard, actualPossibleMovesBitBoard);
    }

    @Test
    @DisplayName("Test bishop with no move")
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

        int[] startingPosition = new int[]{7,2};
        int[][] possibleMoves = new int[][]{};
        testPossibleBishopMoves(game, startingPosition, possibleMoves, "B");
    }

    @Test
    @DisplayName("Test white bishop stops for blockers correctly")
    void testWhiteMoveBlockers()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", "B", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", " ", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{5,1};
        int[][] possibleMoves = new int[][]{{4, 0}, {4, 2}, {3, 3}, {2, 4}, {1, 5}};
        testPossibleBishopMoves(game, startingPosition, possibleMoves, "B");
    }

    @Test
    @DisplayName("Test black bishops stops for blockers correctly")
    void testBlackMoveBlockers()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", " ", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", "b"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        game.turn += 1;

        int[] startingPosition = new int[]{2, 7};
        int[][] possibleMoves = new int[][]{{3, 6}, {4, 5}, {5, 4}, {6, 3}};
        testPossibleBishopMoves(game, startingPosition, possibleMoves, "b");
    }


}

