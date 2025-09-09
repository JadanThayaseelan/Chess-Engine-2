package Test;

import Main.Bitboard;
import Main.Game;
import Main.Game;
import Main.Pieces.Knight;
import com.sun.source.tree.BinaryTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class KnightMovesTest
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

    public void testPossibleKnightMoves(Game game, int[] startingPosition, int[][] possibleMoves, String piece)
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
        long actualPossibleMovesBitBoard = new Knight().possibleMoves(pieceToMoveBitBoard, game.getFriendlyPieces());

        assertEquals(expectedPossibleMovesBitBoard, actualPossibleMovesBitBoard);
    }

    @Test
    @DisplayName("White Knight can move to empty spaces")
    void testWhiteEmptySpaceMove()
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

        int[] startingPosition = new int[]{7,6};
        int[][] possibleMoves = new int[][]{{5,5}, {5,7}};
        testPossibleKnightMoves(game, startingPosition, possibleMoves, "N");
    }

    @Test
    @DisplayName("Black Knight can move to empty spaces")
    void testBlackEmptySpaceMove()
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

        game.turn += 1;

        int[] startingPosition = new int[]{0,1};
        int[][] possibleMoves = new int[][]{{2,0}, {2,2}};
        testPossibleKnightMoves(game, startingPosition, possibleMoves, "n");
    }

    @Test
    @DisplayName("Test Knight moves in all directions")
    void testALlMove()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "N", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", " ", "R"}
        });

        int[] startingPosition = new int[]{3,5};
        int[][] possibleMoves = new int[][]{{1,4}, {1,6}, {2, 7}, {4, 7}, {5, 6}, {5, 4}, {4, 3}, {2, 3}};
        testPossibleKnightMoves(game, startingPosition, possibleMoves, "N");
    }

    @Test
    @DisplayName("Test knights will not wrap around the board when on edge")
    void testEdgeMove()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "N"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", "P", "P", "P", "P", "P"},
                {"R", "N", "B", "Q", "K", "B", " ", "R"}
        });

        int[] startingPosition = new int[]{3,7};
        int[][] possibleMoves = new int[][]{{1, 6}, {2, 5}, {4, 5}, {5, 6}};
        testPossibleKnightMoves(game, startingPosition, possibleMoves, "N");
    }

    @Test
    @DisplayName("Test knight cant take own pieces")
    void testTakeFriendlyPieces()
    {
        Game game = new Game(new String[][]{
                {"r", "n", "b", "q", "k", "b", "n", "r"},
                {"p", "p", "p", "p", "p", "p", "p", "p"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "P", " ", "P"},
                {"P", "P", "P", "P", "P", " ", "P", " "},
                {"R", "N", "B", "Q", "K", "B", "N", "R"}
        });

        int[] startingPosition = new int[]{7,6};
        int[][] possibleMoves = new int[][]{};
        testPossibleKnightMoves(game, startingPosition, possibleMoves, "N");
    }





}
