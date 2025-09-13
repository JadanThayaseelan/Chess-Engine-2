package Test.MoveGenerationTests;

import Main.Bitboard;
import Main.Game;
import Main.MoveGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PerftTest
{
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

    int count = 0;
    public int perft(int depth)
    {
        if (depth == 0) return 1;

        int nodes = 0;
        ArrayList<Character> moves = game.calculateAllPseudoLegalMoves();

        for (char move : moves)
        {
            if(!game.isMoveLegal(move))
            {
//                displayStringBoard(Bitboard.convertBitBoardsToStringBoard(game.bitBoards));
//                System.out.println("Pruned illegal move: " + MoveGeneration.getStartSquare(move) + " " + MoveGeneration.getEndSquare(move) + " " + MoveGeneration.getFlags(move));
            }
            else
            {
                if((move & 0b0100) != 0)
                {
                    count += 1;
                }
                game.makeEngineMove(move);
                nodes += perft(depth - 1);
                game.undoEngineMove();
            }
        }

        return nodes;
    }

    public void debugPerft(int depth) {
        ArrayList<Character> moves = game.calculateAllPseudoLegalMoves();

        long totalNodes = 0;
        System.out.println("Depth " + depth + " perft debug:");
        for (char move : moves) {
            // Make the move
            if(!game.isMoveLegal(move))
            {
                continue;
            }
            game.makeEngineMove(move);
            long nodes = perft(depth - 1);
            game.undoEngineMove();

            totalNodes += nodes;

            int start = MoveGeneration.getStartSquare(move);
            int end = MoveGeneration.getEndSquare(move);
            byte flags = (byte) MoveGeneration.getFlags(move);

            System.out.println("Move: " + squareNames[start] + "->" + squareNames[end] + " | flags: " + flags + " | nodes: " + nodes);
        }
        System.out.println("Total nodes at depth " + depth + ": " + totalNodes);
    }


    public int perftDivide(int depth)
    {
        if (depth < 1) throw new IllegalArgumentException("depth must be >= 1");

        int total = 0;
        ArrayList<Character> moves = game.calculateAllPseudoLegalMoves();

        for (char move : moves)
        {
            if (game.isMoveLegal(move))
            {
                game.makeEngineMove(move);
                int nodes = perft(depth - 1);
                game.undoEngineMove();
                System.out.println(squareNames[MoveGeneration.getStartSquare(move)] + squareNames[MoveGeneration.getEndSquare(move)] + ": " + nodes);
                total += nodes;
            }
        }

        return total;
    }

    @Test
    @DisplayName("Test move count is correct for depth 1")
    public void testDepthOne()
    {
        assertEquals(20, perft(1));
    }


    @Test
    @DisplayName("Test move count is correct for depth 2")
    public void testDepthTwo()
    {
        assertEquals(400, perft(2));
    }

    @Test
    @DisplayName("Test move count is correct for depth 3")
    public void testDepthThree()
    {
        assertEquals(8902, perft(3));
    }

    @Test
    @DisplayName("Test move count is correct for depth 4")
    public void testDepthFour()
    {
//        game.makeEngineMove(MoveGeneration.encodeMoveSquares(62, 47, (byte)0));
//        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 21, (byte)0b0001));
//        game.makeEngineMove(MoveGeneration.encodeMoveSquares(47, 30, (byte)0b0001));
//
//        debugPerft(1);
        //g1h3 +2
        //g1h3 f7f6 +1

        //g1h3 f7f6 h3g5 +1


        //g1h3 f7f5 + 1

        //g1f3 +6
        //b1c3 +2
        assertEquals(197281, perft(4));
    }

    @Test
    @DisplayName("Test move count is correct for depth 5")
    public void testDepthFive()
    {
        assertEquals(4865609, perft(5));
        //game.makeEngineMove(MoveGeneration.encodeMoveSquares(48, 40, (byte)0));
        //game.makeEngineMove(MoveGeneration.encodeMoveSquares(1, 18, (byte)0));
        //game.makeEngineMove(MoveGeneration.encodeMoveSquares(52, 36, (byte)0));
        //game.makeEngineMove(MoveGeneration.encodeMoveSquares(18, 35, (byte)0));
        //displayStringBoard(Bitboard.convertBitBoardsToStringBoard(game.bitBoards));
//        game.displayBitBoard(game.getAllPossibleEnemyAttacks());
//        debugPerft(5);




        //perft(4);
        //System.out.println(count);

        //a2a3 b8c6 +2

        //a2a3 b8c6 e2e3 +1
        //a2a3 b8c6 e2e4 +1

        //a2a3 b8c6 e2e3 c6d4 +1
        //illegal move e1e2
        //king cannot move into check

        //a2a3 b8c6 e2e4




        //a2a3 g8f6 +6
        //a2a3 g8h6 +2
    }

    @Test
    @DisplayName("Test move count is correct for depth 6")
    public void testDepthSix()
    {
        assertEquals(119060324, perft(6));
    }



//    @Test
//    @DisplayName("Test individual moves")
//    public void testIndividual()
//    {
//
//        game.makeEngineMove(MoveGeneration.encodeMoveSquares(57, 40, (byte) 0));
//        perftDivide( 4); // one less depth
//        game.undoEngineMove();
//    }

//    @Test
//    @DisplayName("Test move count is correct for depth 6")
//    public void testDepthSix()
//    {
//
//        assertEquals(119060324, perft(6));
//    }
}
