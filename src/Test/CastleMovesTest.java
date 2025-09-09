package Test;

import Main.Bitboard;
import Main.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import static org.junit.Assert.*;

public class CastleMovesTest
{
    @Test
    @DisplayName("If king is in check can't short castle")
    public void testCanShortCastleInCheck()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "q", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "K", " ", " ", "R"},
        });

        Assertions.assertFalse(game.canShortCastle());
    }

    @Test
    @DisplayName("If king is in check can't long castle")
    public void testCanLongCastleInCheck()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", "b", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"R", " ", " ", " ", "K", " ", " ", " "},
        });

        Assertions.assertFalse(game.canLongCastle());
    }

    @Test
    @DisplayName("King can't castle into check")
    public void testCastleIntoCheck()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "b", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "K", " ", " ", "R"},
        });

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "b", " ", " ", " "},
                {" ", " ", " ", " ", "-", " ", " ", " "},
                {" ", " ", " ", "-", "K", "-", " ", "R"},
        };

        long possibleMovesBitboard = Bitboard.generateBitBoard(possibleMoves, "-");

        assertEquals(game.calculatePossibleMoves(1L << 3), possibleMovesBitboard);
    }

    @Test
    @DisplayName("if rook moves king cant castle to that side")
    public void testCastleWhenRookMoves()
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

        game.makeMove(0x0000000000000080L, 0x0000000000008000L);
        game.turn += 1;

        assertFalse(game.canLongCastle());
    }

    @Test
    @DisplayName("if king moves no castling")
    public void testCastleWhenKingMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"R", " ", " ", " ", "K", " ", " ", "R"},
        });

        game.makeMove(0x0000000000000008L, 0x0000000000000800L);
        game.turn += 1;
        game.makeMove(0x0000000000000800L, 0x0000000000000008L);
        game.turn += 1;

        assertFalse(game.canLongCastle());
        assertFalse(game.canShortCastle());
    }

}
