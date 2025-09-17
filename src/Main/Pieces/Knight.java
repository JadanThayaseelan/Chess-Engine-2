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
        for(int i = 0; i < 64; i++)
        {
            if(i == 0)
            {
                knightAttacks[i] = 0x0020400000000000L;
            }
            else
            {
                long currentKnightPosition = 1L << 63 - i;
                knightAttacks[i] = knightAttacksOnTheFly(currentKnightPosition);
            }
        }
    }

    public long getKnightAttack(long board)
    {
        if(board == 0)
        {
            return 0L;
        }
        int index =  Long.numberOfLeadingZeros(board);
        return knightAttacks[index];
    }

    public long getKnightAttacks(long board)
    {
        long attacks = 0L;
        while(board != 0)
        {
            long knight = 1L << 63 - Long.numberOfLeadingZeros(board);
            board &= ~knight;

            attacks |= getKnightAttack(knight);
        }
        return attacks;
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

    public int calculateKnightMoves(long board, long friendlyPieces, long enemyPieces, int moveCount, char[] moves)
    {
        while(board != 0)
        {
            long knightStart = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ knightStart;

            long possibleMoves = getKnightAttack(knightStart) & ~friendlyPieces;
            while (possibleMoves != 0)
            {
                long move = 1L << 63 - Long.numberOfLeadingZeros(possibleMoves);
                possibleMoves &= ~ move;
                if((move & enemyPieces) != 0)
                {
                    moves[moveCount++] = MoveGeneration.encodeMove(knightStart, move, capture);
                }
                else
                {
                    moves[moveCount++] = MoveGeneration.encodeMove(knightStart, move, quiet);
                }
            }
        }

        return moveCount;
    }
}
