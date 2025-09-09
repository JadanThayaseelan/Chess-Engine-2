package Main.Pieces;

import Main.Bitboard;
import Main.MoveGeneration;

import java.util.ArrayList;

public class Knight
{
    public long[] knightAttacks = new long[64];

    public Knight()
    {
        fillKnightAttacks();
    }

    public void fillKnightAttacks()
    {
        long knightPosition = 0x0000000000000001;
        for(int i = 0; i < 64; i++)
        {
            long currentKnightPosition = knightPosition << i;
            knightAttacks[i] = knightAttacksOnTheFly(currentKnightPosition);
        }
    }

    public long getKnightAttack(long board)
    {
        if(board == 0)
        {
            return 0L;
        }
        int index = 63 - Long.numberOfLeadingZeros(board);
        return knightAttacks[index];
    }

    public long knightAttacksOnTheFly(long board)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;

        long notABFileMask = 0x3f3f3f3f3f3f3f3fL;
        long notGHFileMask = 0xfcfcfcfcfcfcfcfcL;

        long upLeftMove = board << 17 & notHFileMask;
        long upRightMove = board << 15 & notAFileMask;

        long downLeftMove = board >> 15 & notHFileMask;
        long downRightMove = board >> 17 & notAFileMask;

        long rightUpMove = board << 6 & notABFileMask;
        long rightDownMove = board >> 10 & notABFileMask;

        long leftUpMove = board << 10 & notGHFileMask;
        long leftDownMove = board >> 6 & notGHFileMask;


        return (upLeftMove | upRightMove | downLeftMove | downRightMove | rightUpMove | rightDownMove | leftUpMove | leftDownMove);
    }

    public long possibleMoves(long board, long friendlyPieces)
    {
        long moves = 0L;

        while(board != 0)
        {
            long knight = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ knight;

            moves = moves | getKnightAttack(knight);
        }

        return moves & ~friendlyPieces;
    }

    public ArrayList<Character> calculateKnightMoves(int startSquare, long allPieces, long enemyPieces)
    {
        ArrayList<Character> moves = new ArrayList<>();
        long startBitboard = Bitboard.convertSquareToBitboard(startSquare);

        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;

        long notABFileMask = 0x3f3f3f3f3f3f3f3fL;
        long notGHFileMask = 0xfcfcfcfcfcfcfcfcL;

        long upLeftMove = startBitboard << 17 & notHFileMask;
        char upLeftEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(upLeftEncoded != 0)
        {
            moves.add(upLeftEncoded);
        }

        long upRightMove = startBitboard << 15 & notAFileMask;
        char upRightEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(upRightEncoded != 0)
        {
            moves.add(upRightEncoded);
        }


        long downLeftMove = startBitboard >> 15 & notHFileMask;
        char downLeftEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(downLeftEncoded != 0)
        {
            moves.add(downLeftEncoded);
        }

        long downRightMove = startBitboard >> 17 & notAFileMask;
        char downRightEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(downRightEncoded != 0)
        {
            moves.add(downRightEncoded);
        }

        long rightUpMove = startBitboard << 6 & notABFileMask;
        char rightUpEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(rightUpEncoded != 0)
        {
            moves.add(rightUpEncoded);
        }

        long rightDownMove = startBitboard >> 10 & notABFileMask;
        char rightDownEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(rightDownEncoded != 0)
        {
            moves.add(rightDownEncoded);
        }

        long leftUpMove = startBitboard << 10 & notGHFileMask;
        char leftUpEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(leftUpEncoded != 0)
        {
            moves.add(leftUpEncoded);
        }

        long leftDownMove = startBitboard >> 6 & notGHFileMask;
        char leftDownEncoded = encodeMove(startBitboard, upLeftMove, allPieces, enemyPieces);
        if(leftDownEncoded != 0)
        {
            moves.add(leftDownEncoded);
        }

        return moves;
    }

    public char encodeMove(long startBitboard, long endBitboard, long allPieces, long enemyPieces)
    {
        if(endBitboard == 0)
        {
            return 0;
        }

        byte flags = 0b0000;
        if((endBitboard & enemyPieces) != 0)
        {
            flags = 0b0100;
            return MoveGeneration.encodeMove(startBitboard, endBitboard, flags);
        }
        else if((endBitboard & allPieces) == 0)
        {
            return MoveGeneration.encodeMove(startBitboard, endBitboard, flags);
        }

        return 0;
    }



}
