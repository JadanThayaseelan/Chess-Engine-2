package Test.Pawn;

import Main.Game;
import Main.MoveGeneration;
import Main.Pieces.Pawn;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPossibleWhitePawnMoves
{
//    @Test
//    @DisplayName("Test white pawn double push")
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
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 49), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(49, 33, (byte)0b0001)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(49, 41, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test white pawn single push")
//    public void testSinglePush()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 44), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(44, 36, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test white pawn take right")
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
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 36), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 29, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 28, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test white pawn take left")
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
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 36), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 27, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 28, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test white pawn double push blocked")
//    public void testDoublePushBlocked()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "p", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 52), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(52, 44, (byte)0b0000)));
//
//    }
//
//    @Test
//    @DisplayName("Test white pawn push blocked")
//    public void testSinglePushBlocked()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "p", " ", " ", " "},
//                {"P", "P", "P", "P", "P", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 52), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.isEmpty());
//
//    }
//
//    @Test
//    @DisplayName("Test white pawn and enemy both sides")
//    public void testEnemyBoth()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", " ", " ", " ", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", "p", " ", "p", " ", " "},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1 << (63 - 36), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 28, (byte)0b0000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 27, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(36, 29, (byte)0b0100)));
//
//    }
//
//    @Test
//    @DisplayName("Test white pawn en passant")
//    public void testEnPassant()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", "P", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//        game.turn = 1;
//        game.makeEngineMove(MoveGeneration.encodeMoveSquares(13, 29, (byte)0b0001));
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1L << (63 - 28), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(28, 21, (byte)0b0101)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(28, 20, (byte)0b0000)));
//    }
//
//    @Test
//    @DisplayName("Test white pawn take left on edge")
//    public void testTakeLeftEdge()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", "p", " ", " ", " ", " ", " ", "p"},
//                {"P", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1L << (63 - 32), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(32, 25, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(32, 24, (byte)0b0000)));
//        assertEquals(2, pawnMoves.size());
//    }
//
//    @Test
//    @DisplayName("Test white pawn take right on edge")
//    public void testTakeRightEdge()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", "b", "n", "r"},
//                {"p", "p", "p", "p", "p", "p", "p", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"p", " ", " ", " ", " ", " ", "p", " "},
//                {" ", " ", " ", " ", " ", " ", " ", "P"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1L << (63 - 39), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(39, 30, (byte)0b0100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(39, 31, (byte)0b0000)));
//        assertEquals(2, pawnMoves.size());
//    }
//
//    @Test
//    @DisplayName("Test white pawn promotion")
//    public void testPromotion()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", " ", " ", " "},
//                {"p", "p", "p", "p", "p", "p", "P", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", "", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1L << (63 - 14), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1001)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1010)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1011)));
//        assertEquals(4, pawnMoves.size());
//    }
//
//    @Test
//    @DisplayName("Test white pawn promotion capture")
//    public void testPromotionCapture()
//    {
//        Pawn pawn = new Pawn();
//        Game game = new Game(new String[][]{
//                {"r", "n", "b", "q", "k", " ", " ", "n"},
//                {"p", "p", "p", "p", "p", "p", "P", "p"},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", "", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {" ", " ", " ", " ", " ", " ", " ", " "},
//                {"P", "P", "P", "P", " ", "P", "P", "P"},
//                {"R", "N", "B", "Q", "K", "B", "N", "R"}
//        });
//
//
//        ArrayList<Character> pawnMoves = pawn.calculateWhitePawnMoves(1L << (63 - 14), game.allPiecesBitboard, game.blackPiecesBitboard, game.doublePawnPushBitboard);
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1000)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1001)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1010)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 6, (byte)0b1011)));
//
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 7, (byte)0b1100)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 7, (byte)0b1101)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 7, (byte)0b1110)));
//        assertTrue(pawnMoves.contains(MoveGeneration.encodeMoveSquares(14, 7, (byte)0b1111)));
//        assertEquals(8, pawnMoves.size());
//    }

}
