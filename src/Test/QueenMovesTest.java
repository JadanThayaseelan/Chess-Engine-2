package Test;

import Main.Game;
import Main.Pieces.Queen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueenMovesTest
{
    public void testPossibleQueenMoves(Game game, int[] startingPosition, int[][] possibleMoves, String piece)
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
        long actualPossibleMovesBitBoard = new Queen().possibleMoves(pieceToMoveBitBoard, game.getAllPiecesBitBoard(), game.getFriendlyPieces());

        assertEquals(expectedPossibleMovesBitBoard, actualPossibleMovesBitBoard);
    }

    @Test
    @DisplayName("Test when the queen has no moves")
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

        int[] startingPosition = new int[]{7,3};
        int[][] possibleMoves = new int[][]{};
        testPossibleQueenMoves(game, startingPosition, possibleMoves, "Q");
    }

    @Test
    @DisplayName("Test how white queen reacts to blockers")
    void testWhiteMoveBlockers()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"Q", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", " ", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{5,0};
        int[][] possibleMoves = new int[][]{{4, 0}, {3, 0}, {2, 0}, {1, 0}, {5, 1}, {
            5, 2}, {5, 3}, {5,4}, {5, 5}, {5, 6}, {5, 7}, {4, 1}, {3, 2}, {2, 3}, {1, 4}};
        testPossibleQueenMoves(game, startingPosition, possibleMoves, "Q");
    }

    @Test
    @DisplayName("Test how black queen reacts to blockers")
    void testBlackMoveBlockers()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", " ", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", "q"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });
        game.turn += 1;

        int[] startingPosition = new int[]{2, 7};
        int[][] possibleMoves = new int[][]{{3, 7}, {4, 7}, {5, 7}, {6, 7}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, {3, 6}, {4, 5}, {5, 4}, {6, 3}};
        testPossibleQueenMoves(game, startingPosition, possibleMoves, "q");
    }

}
