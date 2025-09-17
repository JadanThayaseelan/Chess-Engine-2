package Test.GameTests;

import Main.Bitboard;
import Main.Game;
import Main.MoveGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestUndoEngineMove
{
    @Test
    @DisplayName("Test undo white short castle")
    public void testUndoShortCastle()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "K", " ", " ", "R"},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(60, 62, (byte)0b0010));
        game.undoEngineMove();
        assertEquals(Bitboard.convertSquareToBitboard(60) ,game.bitBoards[5]);
        assertEquals(Bitboard.convertSquareToBitboard(63) ,game.bitBoards[3]);
    }

    @Test
    @DisplayName("Test undo white long castle")
    public void testUndoLongCastle()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"R", " ", " ", " ", "K", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(60, 58, (byte)0b0011));
        game.undoEngineMove();
        assertEquals(Bitboard.convertSquareToBitboard(60) ,game.bitBoards[5]);
        assertEquals(Bitboard.convertSquareToBitboard(56) ,game.bitBoards[3]);
    }

    @Test
    @DisplayName("Test undo white en Passant")
    public void testUndoEnPassant()
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
        game.undoEngineMove();

        assertEquals(Bitboard.convertSquareToBitboard(27), game.bitBoards[0]);
        assertEquals(Bitboard.convertSquareToBitboard(28), game.bitBoards[6]);

    }

    @Test
    @DisplayName("Test undo black short castle")
    public void testUndoBlackShortCastle()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", "k", " ", " ", "r"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.turn = 1;

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(4, 6, (byte)0b0010));
        game.undoEngineMove();
        assertEquals(Bitboard.convertSquareToBitboard(4) ,game.bitBoards[11]);
        assertEquals(Bitboard.convertSquareToBitboard(7) ,game.bitBoards[9]);
    }

    @Test
    @DisplayName("Test undo black long castle")
    public void testUndoBlackLongCastle()
    {
        Game game = new Game(new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.turn = 1;

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(4, 2, (byte)0b0011));
        game.undoEngineMove();
        assertEquals(Bitboard.convertSquareToBitboard(4) ,game.bitBoards[11]);
        assertEquals(Bitboard.convertSquareToBitboard(0) ,game.bitBoards[9]);
    }

    @Test
    @DisplayName("Test undo black En Passant")
    public void testUndoBlackEnPassant()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "p", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "P", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.makeEngineMove(MoveGeneration.encodeMoveSquares(51, 35, (byte)0b0001));

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(36, 42, (byte)0b0101));
        game.undoEngineMove();
        assertEquals(Bitboard.convertSquareToBitboard(36) ,game.bitBoards[6]);
    }




    @Test
    @DisplayName("Test undo capture")
    public void undoCapture()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "p", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "N", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(27, 12, (byte)0b0100));
        game.undoEngineMove();


        assertEquals(Bitboard.convertSquareToBitboard(27), game.bitBoards[1]);
        assertEquals(Bitboard.convertSquareToBitboard(12), game.bitBoards[6]);
    }

    @Test
    @DisplayName("Test undo knight promotion")
    public void undoKnightPromotion()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(12, 4, (byte)0b1000));
        game.undoEngineMove();


        assertEquals(Bitboard.convertSquareToBitboard(12), game.bitBoards[0]);
        assertEquals(0, game.bitBoards[1]);
    }

    @Test
    @DisplayName("Test undo bishop promotion")
    public void undoBishopPromotion()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(12, 4, (byte)0b1001));
        game.undoEngineMove();

        assertEquals(Bitboard.convertSquareToBitboard(12), game.bitBoards[0]);
        assertEquals(0, game.bitBoards[2]);
    }

    @Test
    @DisplayName("Test undo rook promotion")
    public void undoRookPromotion()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(12, 4, (byte)0b1010));
        game.undoEngineMove();

        assertEquals(Bitboard.convertSquareToBitboard(12), game.bitBoards[0]);
        assertEquals(0, game.bitBoards[3]);
    }

    @Test
    @DisplayName("Test undo queen promotion")
    public void undoQueenPromotion()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.makeEngineMove(MoveGeneration.encodeMoveSquares(12, 4, (byte)0b1011));
        game.undoEngineMove();

        assertEquals(Bitboard.convertSquareToBitboard(12), game.bitBoards[0]);
        assertEquals(0, game.bitBoards[4]);
    }
}
