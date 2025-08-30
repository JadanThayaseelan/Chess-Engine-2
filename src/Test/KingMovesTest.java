package Test;

import Main.Game;
import Main.Pieces.Knight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KingMovesTest
{

    
    public void testPossibleKingMoves(Game game, String[][] possibleMoves, String piece)
    {
        if(Character.isLowerCase(piece.charAt(0)))
        {
            game.turn = 1;
        }

        int[] startingPosition = getStartingPositionFromStringBoard(game);

        String[][] pieceToMove = new String[8][8];
        pieceToMove[startingPosition[0]][startingPosition[1]] = piece;
        long pieceToMoveBitBoard = game.generateBitBoard(pieceToMove, piece);

        long expectedPossibleMovesBitBoard = game.generateBitBoard(possibleMoves, "-");
        long actualPossibleMovesBitBoard = game.calculatePossibleMoves(piece, pieceToMoveBitBoard);

        assertEquals(expectedPossibleMovesBitBoard, actualPossibleMovesBitBoard);
    }

    public int[] getStartingPositionFromStringBoard(Game game)
    {
        String[][] stringBoard = game.convertBitBoardsToStringBoard();
        int[] startingPosition = new int[2];

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(stringBoard[i][j] != null && (stringBoard[i][j].equals("K") || stringBoard[i][j].equals("k")))
                {
                    startingPosition = new int[]{i, j};
                }
            }
        }

        return startingPosition;
    }

    @Test
    @DisplayName("White King can move when in center of board")
    public void testWhiteKingCenterMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "-", "-", "-", " ", " ", " "},
                {" ", " ", "-", " ", "-", " ", " ", " "},
                {" ", " ", "-", "-", "-", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }

    @Test
    @DisplayName("White King can move when in center of board")
    public void testBlackKingCenterMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "k", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", "-", "-", " ", " "},
                {" ", " ", " ", "-", " ", "-", " ", " "},
                {" ", " ", " ", "-", "-", "-", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");
    }

    @Test
    @DisplayName("White King can move when on corner of board")
    public void testWhiteKingCornerMoves()
    {
        Game game = new Game(new String[][]{
                {"K", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", "-", " ", " ", " ", " ", " ", " "},
                {"-", "-", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }

    @Test
    @DisplayName("Black King can move when on corner of board")
    public void testBlackKingCornerMoves()
    {
        Game game = new Game(new String[][]{
                {"k", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", "-", " ", " ", " ", " ", " ", " "},
                {"-", "-", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");
    }

    @Test
    @DisplayName("White King can escape attacks from edge")
    public void testWhiteKingAttackedEdgeMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", "r", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "K", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", "-", " ", " "},
                {" ", " ", " ", "-", " ", "-", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }

    @Test
    @DisplayName("White King can escape attacks from centre")
    public void testWhiteKingAttackedCentreMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", "b", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", "-", " ", " ", " "},
                {" ", " ", "-", " ", "-", " ", " ", " "},
                {" ", " ", "-", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }

    @Test
    @DisplayName("White King can escape double attacks from centre")
    public void testWhiteKingDoubleAttackedCentreMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "r", " ", " ", "b", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "-", " ", " ", " "},
                {" ", " ", "-", " ", "-", " ", " ", " "},
                {" ", " ", "-", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }

    @Test
    @DisplayName("White King cant move into attacks")
    public void testWhiteKingAvoidsAttackedSquares()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "K", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "r", " ", "r", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }

    @Test
    @DisplayName("White King cant take protected pieces")
    public void testWhiteKingCannotTakeProtected()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", "K", "r", " ", " ", " ", " ", " "},
                {" ", " ", " ", "b", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"-", "-", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"-", "-", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "K");
    }




    @Test
    @DisplayName("Black King can escape attacks from edge")
    public void testBlackKingAttackedEdgeMoves()
    {
        Game game = new Game(new String[][]{
                {"k", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"R", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", "-", " ", " ", " ", " ", " ", " "},
                {" ", "-", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");
    }

    @Test
    @DisplayName("Black King can escape attacks from centre")
    public void testBlackKingAttackedCentreMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "k", " ", " ", " "},
                {" ", " ", " ", "B", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", "-", " ", " ", " "},
                {" ", " ", " ", "-", " ", "-", " ", " "},
                {" ", " ", " ", "-", "-", "-", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");
    }

    @Test
    @DisplayName("Black King can escape double attacks from centre")
    public void testBlackKingDoubleAttackedCentreMoves()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "k", " ", "R", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "Q", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", "-", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", "-", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");

    }

    @Test
    @DisplayName("Black King cant move into attacks")
    public void testBlackKingAvoidsAttackedSquares()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"Q", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "k", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", "-", " ", " ", " ", " "},
                {" ", "-", " ", "-", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");
    }

    @Test
    @DisplayName("Black King cant take protected pieces")
    public void testBlackKingCannotTakeProtected()
    {
        Game game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "Q", " ", " ", " ", " ", " "},
                {" ", " ", "R", " ", " ", " ", " ", " "},
                {" ", "k", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}});

        String[][] possibleMoves = new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"-", "-", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "}};
        testPossibleKingMoves(game, possibleMoves, "k");
    }


}
