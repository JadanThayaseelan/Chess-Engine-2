package Test.Knight;

import Main.Game;
import Main.MoveGeneration;
import Main.Pieces.Knight;
import Main.Pieces.Pawn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCalculateKnightMoves
{
    @Test
    @DisplayName("Test Knight has all possible 8 positions")
    public void testKnightMiddle()
    {
        Knight knight = new Knight();
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "N", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}
        });

        ArrayList<Character> knightMoves = knight.calculateKnightMoves(1 << 63 - 36, game.getFriendlyPieces(), game.getEnemyPieces());

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 30, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 46, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 26, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 42, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 51, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 53, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 19, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 21, (byte) 0b0000)));

    }

    @Test
    @DisplayName("Test Knight on edge")
    public void testKnightEdge()
    {
        Knight knight = new Knight();
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "N"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}
        });

        ArrayList<Character> knightMoves = knight.calculateKnightMoves(1 << 63 - 39, game.getFriendlyPieces(), game.getEnemyPieces());

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(39, 54, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(39, 22, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(39, 29, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(39, 45, (byte) 0b0000)));

    }

    @Test
    @DisplayName("Test Knight on corner")
    public void testKnightCorner()
    {
        Knight knight = new Knight();
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "N"}
        });

        ArrayList<Character> knightMoves = knight.calculateKnightMoves(1 << 63 - 63, game.getFriendlyPieces(), game.getEnemyPieces());

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(63, 53, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(63, 46, (byte) 0b0000)));
    }

    @Test
    @DisplayName("Test Knight friendly")
    public void testKnightFriendly()
    {
        Knight knight = new Knight();
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", "P", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "N"}
        });

        ArrayList<Character> knightMoves = knight.calculateKnightMoves(1 << 63 - 63, game.getFriendlyPieces(), game.getEnemyPieces());

        assertTrue(knightMoves.isEmpty());
    }

    @Test
    @DisplayName("Test Knight enemy")
    public void testKnightEnemy()
    {
        Knight knight = new Knight();
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", "p", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "N"}
        });

        ArrayList<Character> knightMoves = knight.calculateKnightMoves(1 << 63 - 63, game.getFriendlyPieces(), game.getEnemyPieces());

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(63, 53, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(63, 46, (byte) 0b0100)));
    }

    @Test
    @DisplayName("Test Knight mixed")
    public void testKnightMixed()
    {
        Knight knight = new Knight();
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", "P", " ", " "},
                {" ", " ", "R", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "N", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}
        });

        ArrayList<Character> knightMoves = knight.calculateKnightMoves(1 << 63 - 36, game.getFriendlyPieces(), game.getEnemyPieces());

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 30, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 46, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 42, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 51, (byte) 0b0000)));
        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 53, (byte) 0b0000)));

        assertTrue(knightMoves.contains(MoveGeneration.encodeMoveSquares(36, 19, (byte) 0b0100)));

        assertEquals(6, knightMoves.size());
    }
}
