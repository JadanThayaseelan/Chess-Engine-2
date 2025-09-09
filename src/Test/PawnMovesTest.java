package Test;

import Main.Bitboard;
import Main.Game;
import Main.Pieces.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PawnMovesTest
{

    public String[][] convertBitBoardToStringBoard(long bitBoard, String piece)
    {
        String[][] stringBoard = new String[8][8];

        String stringBitBoard = String.format("%64s", Long.toBinaryString(bitBoard)).replace(' ', '0');
        for(int i = 0 ; i < stringBitBoard.length(); i++)
        {
            int row = i / 8;
            int col = i - (row*8);

            if(stringBitBoard.charAt(i) == '1')
            {
                stringBoard[row][col] = piece;
            }
        }

        return stringBoard;
    }

    public void displayStringBoard(String[][] stringBoard)
    {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (stringBoard[i][j] == null) {
                    System.out.print("_");
                } else {
                    System.out.print(stringBoard[i][j]);
                }
            }
            System.out.println();
        }
    }

    public void testPossiblePawnMoves(Game game, int[] startingPosition, int[][] possibleMoves, String piece)
    {
        String[][] pieceToMove = new String[8][8];
        pieceToMove[startingPosition[0]][startingPosition[1]] = piece;

        String[][] expectedPossibleMoves = new String[8][8];

        for(int[] move : possibleMoves)
        {
            expectedPossibleMoves[move[0]][move[1]] = piece;
        }

        long pieceToMoveBitBoard = Bitboard.generateBitBoard(pieceToMove, piece);

        long expectedPossibleMovesBitBoard = Bitboard.generateBitBoard(expectedPossibleMoves, piece);
        long actualPossibleMovesBitBoard = new Pawn().possibleMoves(pieceToMoveBitBoard, Bitboard.getAllPieces(game.bitBoards), Bitboard.getWhitePieces(game.bitBoards), Bitboard.getBlackPieces(game.bitBoards), game.turn);

        assertEquals(expectedPossibleMovesBitBoard, actualPossibleMovesBitBoard);
    }

    @Test
    @DisplayName("Two possible moves from starting position")
    void testPawnStartingPosition()
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

        int[] startingPosition = new int[]{6,0};
        int[][] possibleMoves = new int[][]{{5,0}, {4,0}};
        testPossiblePawnMoves(game, startingPosition, possibleMoves, "P");
    }

    @Test
    @DisplayName("Pawn cant move if blocked")
    void testPawnBlocked()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", " ", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {" ", " ", " ", "P", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{4,3};
        int[][] possibleMoves = new int[][]{};
        testPossiblePawnMoves(game, startingPosition, possibleMoves, "P");
    }

    @Test
    @DisplayName("Pawn can take enemy pieces on right")
    void testPawnTakeEnemyRight() {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", " ", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "p", " ", " ", " "},
                {" ", " ", " ", "P", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{4,3};
        int[][] possibleMoves = new int[][]{{3,3}, {3,4}};
        testPossiblePawnMoves(game, startingPosition, possibleMoves, "P");
    }

    @Test
    @DisplayName("Pawn can take enemy pieces on left")
    void testPawnTakeEnemyLeft()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", " ", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "P", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{4,3};
        int[][] possibleMoves = new int[][]{{3,3}, {3,2}};
        testPossiblePawnMoves(game, startingPosition, possibleMoves, "P");
    }

    @Test
    @DisplayName("Pawn can move up by one")
    void testPawnMoveUpOne()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "P", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{4,3};
        int[][] possibleMoves = new int[][]{{3,3}};
        testPossiblePawnMoves(game, startingPosition, possibleMoves, "P");
    }

}
