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
    public boolean doesContain(char moveToFind, char[] moves)
    {
        for(char move : moves)
        {
            if(move == moveToFind)
            {
                return true;
            }
        }
        return false;
    }

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

        char[] knightMoves = new char[256];
        knight.calculateKnightMoves(1 << 63 - 36, game.getFriendlyPieces(), game.getEnemyPieces(), 0, knightMoves);

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 30, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 46, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 26, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 42, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 51, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 53, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 19, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 21, (byte) 0b0000), knightMoves));

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

        char[] knightMoves = new char[256];
        knight.calculateKnightMoves(1 << 63 - 39, game.getFriendlyPieces(), game.getEnemyPieces(), 0, knightMoves);

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(39, 54, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(39, 22, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(39, 29, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(39, 45, (byte) 0b0000), knightMoves));

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

        char[] knightMoves = new char[256];
        knight.calculateKnightMoves(1 << 63 - 63, game.getFriendlyPieces(), game.getEnemyPieces(), 0, knightMoves);

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(63, 53, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(63, 46, (byte) 0b0000), knightMoves));
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

        char[] knightMoves = new char[256];
        int moveCount = knight.calculateKnightMoves(1 << 63 - 63, game.getFriendlyPieces(), game.getEnemyPieces(), 0, knightMoves);

        assertTrue(moveCount == 0);
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

        char[] knightMoves = new char[256];
        knight.calculateKnightMoves(1 << 63 - 63, game.getFriendlyPieces(), game.getEnemyPieces(), 0, knightMoves);

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(63, 53, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(63, 46, (byte) 0b0100), knightMoves));
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

        char[] knightMoves = new char[256];
        knight.calculateKnightMoves(1 << 63 - 36, game.getFriendlyPieces(), game.getEnemyPieces(), 0, knightMoves);

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 30, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 46, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 42, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 51, (byte) 0b0000), knightMoves));
        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 53, (byte) 0b0000), knightMoves));

        assertTrue(doesContain(MoveGeneration.encodeMoveSquares(36, 19, (byte) 0b0100), knightMoves));

    }
}
