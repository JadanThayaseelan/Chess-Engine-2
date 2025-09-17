package Test.Pawn;

import Main.Game;
import Main.MoveGeneration;
import Main.Pieces.Pawn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPossibleBlackPawnMoves
{
//    @Test
//    @DisplayName("Test black pawn double push")
//    public void testDoublePush()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 11), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(11, 27, (byte)0b0001)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(11, 19, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test black pawn single push")
//    public void testSinglePush()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "p", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 28), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(28, 36, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test black pawn take right")
//    public void testTakeRight()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", " ", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", "p", " ", " "},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 29), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(29, 37, (byte)0b0000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(29, 36, (byte)0b0100)));
//    }
//
//    @Test
//    @DisplayName("Test black pawn take left")
//    public void testTakeLeft()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", " ", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", "p", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 27), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(27, 36, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(27, 35, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test black pawn double push blocked")
//    public void testDoublePushBlocked()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 12), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(12, 20, (byte)0b0000)));
//
//    }
//
//    @Test
//    @DisplayName("Test black pawn push blocked")
//    public void testSinglePushBlocked()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 12), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.isEmpty());
//
//    }
//
//    @Test
//    @DisplayName("Test black pawn and enemy both sides")
//    public void testEnemyBoth()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", " ", " ", " ", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", "p", " ", " "},
//                {" ", " ", " ", " ", "P", " ", "P", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 29), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(29, 37, (byte)0b0000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(29, 36, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(29, 38, (byte)0b0100)));
//
//    }
//
//    @Test
//    @DisplayName("Test black pawn en passant")
//    public void testEnPassant()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", "p", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//        game.makeEngineMove(MoveGeneration.encodeMoveSquares(54, 38, (byte)0b0001));
//
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 37), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(37, 46, (byte)0b0101)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(37, 45, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test black pawn take left on edge")
//    public void testTakeLeftEdge()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {"p", " ", " ", " ", " ", " ", " ", " "},
//                {" ", "P", " ", " ", " ", " ", " ", "P"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 16), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(16, 25, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(16, 24, (byte)0b0000)));
//        assertEquals(2, pawnMoves.size());
//    }
//
//    @Test
//    @DisplayName("Test black pawn take right on edge")
//    public void testTakeRightEdge()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", "p"},
//                {"P", " ", " ", " ", " ", " ", "P", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 23), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(23, 30, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(23, 31, (byte)0b0000)));
//        assertEquals(2, pawnMoves.size());
//    }
//
//    @Test
//    @DisplayName("Test black pawn promotion")
//    public void testPromotion()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", " ", " ", " "},
//                {"p", "p", "p", "p", "p", "p", " ", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", "", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "p", "P"},
//                {"R", "N", "B", "Q", "K", " ", " ", " "}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 54), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1001)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1010)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1011)));
//        assertEquals(4, pawnMoves.size());
//    }
//
//    @Test
//    @DisplayName("Test black pawn promotion capture")
//    public void testPromotionCapture()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", " ", " ", "n"},
//                {"p", "p", "p", "p", "p", "p", " ", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", "", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "p", "P"},
//                {"R", "N", "B", "Q", "K", "B", " ", " "}
//        });
//        game.turn = 1;
//        ArrayList<Character> pawnMoves = pawn.calculateBlackPawnMoves(1L << (63 - 54), game.allPiecesBitboard, game.whitePiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1001)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1010)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 62, (byte)0b1011)));
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 61, (byte)0b1100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 61, (byte)0b1101)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 61, (byte)0b1110)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(54, 61, (byte)0b1111)));
//        assertEquals(8, pawnMoves.size());
//    }
}
