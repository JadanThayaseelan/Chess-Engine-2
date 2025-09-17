package Test.GameTests;

import Main.Bitboard;
import Main.MoveGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import Main.Game;

import static org.junit.Assert.assertEquals;

public class TestMakeEngineMove
{
    @Test
    @DisplayName("Tests if short castle works")
    public void testShortCastle()
    {
        Game game = new Game(null);
        game.makeEngineMove(MoveGeneration.encodeMoveSquares(60, 62, (byte)0b0010));

        assertEquals(Bitboard.convertSquareToBitboard(62) ,game.bitBoards[5]);
        assertEquals(Bitboard.convertSquareToBitboard(61) ,game.bitBoards[3] & ~128);
    }

    @Test
    @DisplayName("Tests if long castle works")
    public void testLongCastle()
    {
        Game game = new Game(null);
        game.makeEngineMove(MoveGeneration.encodeMoveSquares(60, 58, (byte)0b0011));

        assertEquals(Bitboard.convertSquareToBitboard(58) ,game.bitBoards[5]);
        assertEquals(Bitboard.convertSquareToBitboard(59) ,game.bitBoards[3] & ~1);
    }

    @Test
    @DisplayName("Test en passant")
    public void testEnPassant()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "p", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "P", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.turn = 1;
        game.makeEngineMove(MoveGeneration.encodeMoveSquares(12, 28, (byte)0b0001));

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(27, 20, (byte)0b0101));

        assertEquals(Bitboard.convertSquareToBitboard(20) ,game.bitBoards[0]);
        assertEquals(0 ,game.bitBoards[6]);


    }

    @Test
    @DisplayName("Test if captures update enemy bitboard")
    public void testCaptures()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "p", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "N", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(53, 36, (byte)0b0100));


        assertEquals(Bitboard.convertSquareToBitboard(36) ,game.bitBoards[1]);
        assertEquals(0 ,game.bitBoards[6]);
    }

    @Test
    @DisplayName("Test promote to knight")
    public void testPromoteKnight()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 5, (byte)0b1000));

        assertEquals(Bitboard.convertSquareToBitboard(5) ,game.bitBoards[1]);
        assertEquals(0 ,game.bitBoards[0]);
    }

    @Test
    @DisplayName("Test promote to bishop")
    public void testPromoteBishop()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 5, (byte)0b1001));

        assertEquals(Bitboard.convertSquareToBitboard(5) ,game.bitBoards[2]);
        assertEquals(0 ,game.bitBoards[0]);
    }

    @Test
    @DisplayName("Test promote to rook")
    public void testPromoteRook()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 5, (byte)0b1010));

        assertEquals(Bitboard.convertSquareToBitboard(5) ,game.bitBoards[3]);
        assertEquals(0 ,game.bitBoards[0]);
    }

    @Test
    @DisplayName("Test promote to queen")
    public void testPromoteQueen()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 5, (byte)0b1011));

        assertEquals(Bitboard.convertSquareToBitboard(5) ,game.bitBoards[4]);
        assertEquals(0 ,game.bitBoards[0]);
    }

    @Test
    @DisplayName("Test promote to knight capture")
    public void testPromoteKnightCapture()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", "p", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 6, (byte)0b1100));


        assertEquals(Bitboard.convertSquareToBitboard(6) ,game.bitBoards[1]);
        assertEquals(0 ,game.bitBoards[0]);
        assertEquals(0 ,game.bitBoards[6]);
    }

    @Test
    @DisplayName("Test promote to bishop capture")
    public void testPromoteBishopCapture()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", "p", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 6, (byte)0b1101));


        assertEquals(Bitboard.convertSquareToBitboard(6) ,game.bitBoards[2]);
        assertEquals(0 ,game.bitBoards[0]);
        assertEquals(0 ,game.bitBoards[6]);
    }

    @Test
    @DisplayName("Test promote to rook capture")
    public void testPromoteRookCapture()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", "p", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 6, (byte)0b1110));


        assertEquals(Bitboard.convertSquareToBitboard(6) ,game.bitBoards[3]);
        assertEquals(0 ,game.bitBoards[0]);
        assertEquals(0 ,game.bitBoards[6]);
    }

    @Test
    @DisplayName("Test promote to queen capture")
    public void testPromoteQueenCapture()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", "p", " "},
                {" ", " ", " ", " ", " ", "P", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 6, (byte)0b1111));


        assertEquals(Bitboard.convertSquareToBitboard(6) ,game.bitBoards[4]);
        assertEquals(0 ,game.bitBoards[0]);
        assertEquals(0 ,game.bitBoards[6]);
    }
}
