package Test;

import Main.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckMovesTest
{
    //Test isInCheck()
    @Test
    @DisplayName("Test is in check for white")
    public void testIsInCheckWhite()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "B", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
            });

        assertTrue(game.isInCheck());

    }

    @Test
    @DisplayName("Test is in check for black")
    public void testIsInCheckBlack()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", "k", " ", "Q", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "N", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.turn = 1;

        assertTrue(game.isInCheck());

    }

    //Test getMovesToStopCheck()
    @Test
    @DisplayName("Tests all squares that can prevent check excluding king squares")
    public void testGetMovesToStopCheck()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "B", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
        });

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getMovesToStopCheck());
    }

    @Test
    @DisplayName("Tests no blocking moves are generated for double check")
    public void testGetMovesToStopCheckDouble()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "k", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "B", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
        });

        game.turn = 1;

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getMovesToStopCheck());
    }

    @Test
    @DisplayName("Tests whether squares to prevent non sliding pieces are generated")
    public void testGetMovesToStopCheckNonSliding()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "n", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "-", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getMovesToStopCheck());
    }


    //Test getAllPinnedPiecesValidMoves
    @Test
    @DisplayName("Gets pinned rook path")
    public void testGetAllPinnedPiecesValidMovesRook()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getAllPinnedPiecesValidMoves());
    }

    @Test
    @DisplayName("Gets pinned bishops path")
    public void testGetAllPinnedPiecesValidMovesBishop()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", "q", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "b"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "B", "B", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", "-"},
                {" ", " ", " ", "-", " ", " ", "-", " "},
                {" ", " ", " ", "-", " ", "-", " ", " "},
                {" ", " ", " ", "-", "-", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getAllPinnedPiecesValidMoves());
    }

    @Test
    @DisplayName("Gets No pin")
    public void testGetAllPinnedPiecesValidMovesNoPin()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getAllPinnedPiecesValidMoves());
    }

    @Test
    @DisplayName("Gets No pin2")
    public void testGetAllPinnedPiecesValidMovesNoPin2()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "b", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        game.displayBitBoard(game.getAllPinnedPiecesValidMoves());
        assertEquals(possibleMovesBitboard, game.getAllPinnedPiecesValidMoves());
    }

    @Test
    @DisplayName("Gets No pin3")
    public void testGetAllPinnedPiecesValidMovesNoPin3()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", "r", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "R", " ", " ", " ", " "},
                {" ", " ", " ", "q", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        game.displayBitBoard(game.getAllPinnedPiecesValidMoves());
        assertEquals(possibleMovesBitboard, game.getAllPinnedPiecesValidMoves());
    }


    //Tests getAllPossibleMoves when in check
    @Test
    @DisplayName("Tests possible moves when in singular check with ability to block")
    public void testPossibleMovesAfterCheck()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", "q", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "b"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "B", " ", " ", " "},
                {"Q", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });


        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", "q", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", "b"},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "-", " ", "B", " ", " ", " "},
                {"Q", " ", "-", "K", "-", " ", " ", " "},
                {" ", " ", "-", " ", "-", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getAllPossibleMoves());
    }

    @Test
    @DisplayName("Tests possible moves when in double check with only king moves available")
    public void testPossibleMovesAfterDoubleCheck()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "k", " ", "R", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "B", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });

        game.turn = 1;

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", "-", " ", " ", " "},
                {" ", " ", " ", "k", " ", "R", " ", " "},
                {" ", " ", "-", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", "B", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        };

        long possibleMovesBitboard = game.generateBitBoard(possibleMoves, "-");

        assertEquals(possibleMovesBitboard, game.getAllPossibleMoves());
    }
}
