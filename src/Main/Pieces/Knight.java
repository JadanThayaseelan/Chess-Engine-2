package Main.Pieces;

import Main.Bitboard;
import Main.MoveGeneration;

import java.util.ArrayList;

public class Knight
{
    public long[] knightAttacks = new long[64];
    private byte quiet = 0;
    private byte capture = 4;

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

    public ArrayList<Character> calculateKnightMoves(long board, long friendlyPieces, long enemyPieces)
    {
        ArrayList<Character> moves = new ArrayList<>();
        while(board != 0)
        {
            long knightStart = 1L << Long.numberOfTrailingZeros(board);
            board = board ^ knightStart;

            long possibleMoves = getKnightAttack(knightStart) & ~friendlyPieces;
            while (possibleMoves != 0)
            {
                long move = 1L << Long.numberOfTrailingZeros(possibleMoves);
                possibleMoves &= ~ move;
                if((possibleMoves & enemyPieces) != 0)
                {
                    moves.add(MoveGeneration.encodeMove(knightStart, move, capture));
                }
                else
                {
                    moves.add(MoveGeneration.encodeMove(knightStart, move, quiet));
                }
            }
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
