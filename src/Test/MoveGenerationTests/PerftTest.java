package Test.MoveGenerationTests;

import Main.Bitboard;
import Main.Game;
import Main.MoveGeneration;
import Main.Pieces.King;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PerftTest
{
    int totalCount = 0;
    Game game = new Game(new String[][]{
                    {"r", "n", "b", "q", "k", "b", "n", "r"},
                    {"p", "p", "p", "p", "p", "p", "p", "p"},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {"P", "P", "P", "P", "P", "P", "P", "P"},
                    {"R", "N", "B", "Q", "K", "B", "N", "R"}});

    String[] squareNames = new String[]{
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
    };


    public void displayStringBoard(String[][] stringBoard)
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(stringBoard[i][j] != null)
                {
                    System.out.print(stringBoard[i][j]);
                }
                else
                {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
    }


    public int perft(int depth)
    {
        if (depth == 0)
        {
            return 1;
        }

        int nodes = 0;
        char[] moves = game.calculateAllPseudoLegalMoves();

        for (int i = 0; i < moves.length; i++)
        {
            if(moves[i] == 0 || !game.isMoveLegal(moves[i]))
            {
                continue;
            }

            game.makeEngineMove(moves[i]);
            nodes += perft(depth - 1);
            game.undoEngineMove();

        }

        return nodes;
    }

//    public void debugPerft(int depth) {
//        ArrayList<Character> moves = game.calculateAllPseudoLegalMoves();
//
//        long totalNodes = 0;
//        System.out.println("Depth " + depth + " perft debug:");
//        for (char move : moves) {
//            // Make the move
//            if(!game.isMoveLegal(move))
//            {
//                continue;
//            }
//            game.makeEngineMove(move);
//            long nodes = perft(depth - 1);
//            game.undoEngineMove();
//
//            totalNodes += nodes;
//
//            int start = MoveGeneration.getStartSquare(move);
//            int end = MoveGeneration.getEndSquare(move);
//            byte flags = (byte) MoveGeneration.getFlags(move);
//
//            System.out.println("Move: " + squareNames[start] + "(" + start + ")" + "->" + squareNames[end] + "(" + end + ")" + " | flags: " + flags + " | nodes: " + nodes);
//        }
//        System.out.println("Total nodes at depth " + depth + ": " + totalNodes);
//    }
//
//
//    public int perftDivide(int depth)
//    {
//        if (depth < 1) throw new IllegalArgumentException("depth must be >= 1");
//
//        int total = 0;
//        ArrayList<Character> moves = game.calculateAllPseudoLegalMoves();
//
//        for (char move : moves)
//        {
//            if (game.isMoveLegal(move))
//            {
//                game.makeEngineMove(move);
//                int nodes = perft(depth - 1);
//                game.undoEngineMove();
//                System.out.println(squareNames[MoveGeneration.getStartSquare(move)] + squareNames[MoveGeneration.getEndSquare(move)] + ": " + nodes);
//                total += nodes;
//            }
//        }
//
//        return total;
//    }

    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void test1DepthOne()
    {
        assertEquals(20, perft(1));
    }


    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void test1DepthTwo()
    {
        assertEquals(400, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void test1DepthThree()
    {
        assertEquals(8902, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void test1DepthFour()
    {
        assertEquals(197281, perft(4));
    }

    @Test
    @DisplayName("Test move count is correct for depth 5")
    public void test1DepthFive()
    {
        assertEquals(4865609, perft(5));
    }

    @Test
    @DisplayName("Test move count is correct for depth 6")
    public void test1DepthSix()
    {

        assertEquals(119060324, perft(6));
    }

    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void test2DepthOne()
    {
        game = new Game(new String[][] {
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"p", " ", "p", "p", "q", "p", "b", " "},
                {"b", "n", " ", " ", "p", "n", "p", " "},
                {" ", " ", " ", "P", "N", " ", " ", " "},
                {" ", "p", " ", " ", "P", " ", " ", " "},
                {" ", " ", "N", " ", " ", "Q", " ", "p"},
                {"P", "P", "P", "B", "B", "P", "P", "P"},
                {"R", " ", " ", " ", "K", " ", " ", "R"},

        });

        assertEquals(48, perft(1));
    }

    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void test2DepthTwo()
    {
        game = new Game(new String[][] {
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"p", " ", "p", "p", "q", "p", "b", " "},
                {"b", "n", " ", " ", "p", "n", "p", " "},
                {" ", " ", " ", "P", "N", " ", " ", " "},
                {" ", "p", " ", " ", "P", " ", " ", " "},
                {" ", " ", "N", " ", " ", "Q", " ", "p"},
                {"P", "P", "P", "B", "B", "P", "P", "P"},
                {"R", " ", " ", " ", "K", " ", " ", "R"},

        });

        assertEquals(2039, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void test2DepthThree()
    {
        game = new Game(new String[][] {
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"p", " ", "p", "p", "q", "p", "b", " "},
                {"b", "n", " ", " ", "p", "n", "p", " "},
                {" ", " ", " ", "P", "N", " ", " ", " "},
                {" ", "p", " ", " ", "P", " ", " ", " "},
                {" ", " ", "N", " ", " ", "Q", " ", "p"},
                {"P", "P", "P", "B", "B", "P", "P", "P"},
                {"R", " ", " ", " ", "K", " ", " ", "R"},

        });

        assertEquals(97862, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void test2DepthFour()
    {
        game = new Game(new String[][] {
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"p", " ", "p", "p", "q", "p", "b", " "},
                {"b", "n", " ", " ", "p", "n", "p", " "},
                {" ", " ", " ", "P", "N", " ", " ", " "},
                {" ", "p", " ", " ", "P", " ", " ", " "},
                {" ", " ", "N", " ", " ", "Q", " ", "p"},
                {"P", "P", "P", "B", "B", "P", "P", "P"},
                {"R", " ", " ", " ", "K", " ", " ", "R"},

        });

        assertEquals(4085603, perft(4));
    }


    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void test3DepthOne()
    {
        game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {"K", "P", " ", " ", " ", " ", " ", "r"},
                {" ", "R", " ", " ", " ", "p", " ", "k"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", "P", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.cannotCastleFlags = 0b1111;
        assertEquals(14, perft(1));
    }

    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void test3DepthTwo()
    {
        game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {"K", "P", " ", " ", " ", " ", " ", "r"},
                {" ", "R", " ", " ", " ", "p", " ", "k"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", "P", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.cannotCastleFlags = 0b1111;
        assertEquals(191, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void test3DepthThree()
    {
        game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {"K", "P", " ", " ", " ", " ", " ", "r"},
                {" ", "R", " ", " ", " ", "p", " ", "k"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", "P", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.cannotCastleFlags = 0b1111;
        assertEquals(2812, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void test3DepthFour()
    {
        game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {"K", "P", " ", " ", " ", " ", " ", "r"},
                {" ", "R", " ", " ", " ", "p", " ", "k"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", "P", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.cannotCastleFlags = 0b1111;
        assertEquals(43238, perft(4));
    }

    @Test
    @DisplayName("Test move count is correct for depth 5")
    public void test3DepthFive()
    {
        game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {"K", "P", " ", " ", " ", " ", " ", "r"},
                {" ", "R", " ", " ", " ", "p", " ", "k"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", "P", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.cannotCastleFlags = 0b1111;
        assertEquals(674624, perft(5));
    }

    @Test
    @DisplayName("Test move count is correct for depth 6")
    public void test3DepthSix()
    {
        game = new Game(new String[][]{
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", "p", " ", " ", " ", " "},
                {"K", "P", " ", " ", " ", " ", " ", "r"},
                {" ", "R", " ", " ", " ", "p", " ", "k"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", "P", " ", "P", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
        });
        game.cannotCastleFlags = 0b1111;

        assertEquals(11030083, perft(6));
    }

    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void test4DepthOne()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"P", "p", "p", "p", " ", "p", "p", "p"},
                {" ", "b", " ", " ", " ", "n", "b", "N"},
                {"n", "P", " ", " ", " ", " ", " ", " "},
                {"B", "B", "P", " ", "P", " ", " ", " "},
                {"q", " ", " ", " ", " ", "N", " ", " "},
                {"P", "p", " ", "P", " ", " ", "P", "P"},
                {"R", " ", " ", "Q", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1100;

        assertEquals(6, perft(1));
    }

    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void test4DepthTwo()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"P", "p", "p", "p", " ", "p", "p", "p"},
                {" ", "b", " ", " ", " ", "n", "b", "N"},
                {"n", "P", " ", " ", " ", " ", " ", " "},
                {"B", "B", "P", " ", "P", " ", " ", " "},
                {"q", " ", " ", " ", " ", "N", " ", " "},
                {"P", "p", " ", "P", " ", " ", "P", "P"},
                {"R", " ", " ", "Q", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1100;

        assertEquals(264, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void test4DepthThree()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"P", "p", "p", "p", " ", "p", "p", "p"},
                {" ", "b", " ", " ", " ", "n", "b", "N"},
                {"n", "P", " ", " ", " ", " ", " ", " "},
                {"B", "B", "P", " ", "P", " ", " ", " "},
                {"q", " ", " ", " ", " ", "N", " ", " "},
                {"P", "p", " ", "P", " ", " ", "P", "P"},
                {"R", " ", " ", "Q", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1100;
        assertEquals(9467, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void test4DepthFour()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"P", "p", "p", "p", " ", "p", "p", "p"},
                {" ", "b", " ", " ", " ", "n", "b", "N"},
                {"n", "P", " ", " ", " ", " ", " ", " "},
                {"B", "B", "P", " ", "P", " ", " ", " "},
                {"q", " ", " ", " ", " ", "N", " ", " "},
                {"P", "p", " ", "P", " ", " ", "P", "P"},
                {"R", " ", " ", "Q", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1100;
        assertEquals(422333, perft(4));
    }

    @Test
    @DisplayName("Test move count is correct for depth 5")
    public void test4DepthFive()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", "r"},
                {"P", "p", "p", "p", " ", "p", "p", "p"},
                {" ", "b", " ", " ", " ", "n", "b", "N"},
                {"n", "P", " ", " ", " ", " ", " ", " "},
                {"B", "B", "P", " ", "P", " ", " ", " "},
                {"q", " ", " ", " ", " ", "N", " ", " "},
                {"P", "p", " ", "P", " ", " ", "P", "P"},
                {"R", " ", " ", "Q", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1100;
        assertEquals(15833292, perft(5));
    }

    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void test5DepthOne()
    {
        game = new Game(new String[][]{
                {"r", "n", "b", "q", " ", "k", " ", "r"},
                {"p", "p", " ", "P", "b", "p", "p", "p"},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "B", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "N", "n", "P", "P"},
                {"R", "N", "B", "Q", "K", " ", " ", "R"},
        });
        game.cannotCastleFlags = 0b0011;

        assertEquals(44, perft(1));
    }


    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void test5DepthTwo()
    {
        game = new Game(new String[][]{
                {"r", "n", "b", "q", " ", "k", " ", "r"},
                {"p", "p", " ", "P", "b", "p", "p", "p"},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "B", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "N", "n", "P", "P"},
                {"R", "N", "B", "Q", "K", " ", " ", "R"},
        });
        game.cannotCastleFlags = 0b0011;

        assertEquals(1486, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void test5DepthThree()
    {
        game = new Game(new String[][]{
                {"r", "n", "b", "q", " ", "k", " ", "r"},
                {"p", "p", " ", "P", "b", "p", "p", "p"},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "B", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "N", "n", "P", "P"},
                {"R", "N", "B", "Q", "K", " ", " ", "R"},
        });
        game.cannotCastleFlags = 0b0011;

        assertEquals(62379, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void test5DepthFour()
    {
        game = new Game(new String[][]{
                {"r", "n", "b", "q", " ", "k", " ", "r"},
                {"p", "p", " ", "P", "b", "p", "p", "p"},
                {" ", " ", "p", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", "B", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"P", "P", "P", " ", "N", "n", "P", "P"},
                {"R", "N", "B", "Q", "K", " ", " ", "R"},
        });
        game.cannotCastleFlags = 0b0011;

        assertEquals(2103487, perft(4));
    }


    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void test6DepthOne()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", " ", "r", "k", " "},
                {" ", "p", "p", " ", "q", "p", "p", "p"},
                {"p", " ", "n", "p", " ", "n", " ", " "},
                {" ", " ", "b", " ", "p", " ", "B", " "},
                {" ", " ", "B", " ", "P", " ", "b", " "},
                {"P", " ", "N", "P", " ", "N", " ", " "},
                {" ", "P", "P", " ", "Q", "P", "P", "P"},
                {"R", " ", " ", " ", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1111;

        assertEquals(46, perft(1));
    }

    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void test6DepthTwo()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", " ", "r", "k", " "},
                {" ", "p", "p", " ", "q", "p", "p", "p"},
                {"p", " ", "n", "p", " ", "n", " ", " "},
                {" ", " ", "b", " ", "p", " ", "B", " "},
                {" ", " ", "B", " ", "P", " ", "b", " "},
                {"P", " ", "N", "P", " ", "N", " ", " "},
                {" ", "P", "P", " ", "Q", "P", "P", "P"},
                {"R", " ", " ", " ", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1111;

        assertEquals(2079, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void test6DepthThree()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", " ", "r", "k", " "},
                {" ", "p", "p", " ", "q", "p", "p", "p"},
                {"p", " ", "n", "p", " ", "n", " ", " "},
                {" ", " ", "b", " ", "p", " ", "B", " "},
                {" ", " ", "B", " ", "P", " ", "b", " "},
                {"P", " ", "N", "P", " ", "N", " ", " "},
                {" ", "P", "P", " ", "Q", "P", "P", "P"},
                {"R", " ", " ", " ", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1111;

        assertEquals(89890, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void test6DepthFour()
    {
        game = new Game(new String[][]{
                {"r", " ", " ", " ", " ", "r", "k", " "},
                {" ", "p", "p", " ", "q", "p", "p", "p"},
                {"p", " ", "n", "p", " ", "n", " ", " "},
                {" ", " ", "b", " ", "p", " ", "B", " "},
                {" ", " ", "B", " ", "P", " ", "b", " "},
                {"P", " ", "N", "P", " ", "N", " ", " "},
                {" ", "P", "P", " ", "Q", "P", "P", "P"},
                {"R", " ", " ", " ", " ", "R", "K", " "},
        });
        game.cannotCastleFlags = 0b1111;

        assertEquals(3894594, perft(4));

    }





}
