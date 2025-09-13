package Main.Pieces;

import Main.MoveGeneration;

import java.util.ArrayList;

public class RookMagic
{

    private byte quiet = 0;
    private byte capture = 4;

    public RookMagic()
    {
        fillRookAttackMasks();
        fillRookAttacks();
    }

    int[] rookBits = {
            12, 11, 11, 11, 11, 11, 11, 12,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            12, 11, 11, 11, 11, 11, 11, 12
    };


    long[] rookMagics = new long[]{
            0x0001FFFAABFAD1A2L, 0x0001000082000401L, 0x0001000204000801L, 0x0001000204080011L,
            0x0000040810002101L, 0x003FFFCDFFD88096L, 0x007FFCDDFCED714AL, 0x00FFFCDDFCED714AL,

            0x0000800041000080L, 0x0000800100020080L, 0x0000020004008080L, 0x0000040008008080L,
            0x0000080010008080L, 0x0000100020008080L, 0x0000200040008080L, 0x0000204000800080L,

            0x0000004081020004L, 0x0000010002008080L, 0x0000020004008080L, 0x0000040008008080L,
            0x0000080010008080L, 0x0000100020008080L, 0x0000200040008080L, 0x0000204000808000L,

            0x0000800040800100L, 0x0000020001010004L, 0x0000020080800400L, 0x0000040080800800L,
            0x0000080080801000L, 0x0000100080802000L, 0x0000200040401000L, 0x0000204000800080L,

            0x0000800080004100L, 0x0000010080800200L, 0x0000020080040080L, 0x0000040080080080L,
            0x0000080080100080L, 0x0000100080200080L, 0x0000200040005000L, 0x0000208080004000L,

            0x0000020000408104L, 0x0000010100020004L, 0x0000808002000400L, 0x0000808004000800L,
            0x0000808008001000L, 0x0000808010002000L, 0x0000404000201000L, 0x0000208000400080L,

            0x0000800040800100L, 0x0000800100020080L, 0x0000800200040080L, 0x0000800400080080L,
            0x0000800800100080L, 0x0000801000200080L, 0x0000400020005000L, 0x0000800020400080L,

            0x0080002040800100L, 0x0080008001000200L, 0x0080010200040080L, 0x0080020400080080L,
            0x0080040800100080L, 0x0080081000200080L, 0x0040001000200040L, 0x0080001020400080L
    };



    long[][] rookAttacks = new long[64][4096];

    long[] rookAttackMasks = new long[64];


    public long calculateRookMask(int square)
    {
        long mask = 0L;

        int row = square / 8;
        int col = square % 8;

        //left
        for(int c = col - 1; c >= 1; c--) { mask = mask | 1L << (63 - (8 * row + c));}

        //right
        for(int c = col + 1; c <= 6; c++) { mask = mask | 1L << (63 - (8 * row + c));}

        //up
        for(int r = row - 1; r >= 1; r--) { mask = mask | 1L << (63 - (8 * r + col));}

        //down
        for(int r = row + 1; r <= 6; r++) { mask = mask | 1L << (63 - (8 * r + col));}

        return mask;
    }

    public long[] getAllBlockerCombinations(long mask)
    {
        ArrayList<Integer> blockerPositions = getPossibleBlockerPositions(mask);
        long blockerCombinations = 1L << blockerPositions.size();

        long[] blockerBoards = new long[(int) blockerCombinations];

        for(int j = 0; j < blockerBoards.length; j++)
        {
            long currentBlocker = 0L;
            for(int k = 0; k < blockerPositions.size(); k++)
            {
                if(((1 << k) & j) != 0)
                {
                    currentBlocker |= (1L << blockerPositions.get(k));
                }
            }

            blockerBoards[j] = currentBlocker;
        }
        return blockerBoards;
    }

    public ArrayList<Integer> getPossibleBlockerPositions(long attackMask)
    {
        ArrayList<Integer> blockerPositions = new ArrayList<>();
        for(int i = 0; i < 64; i++)
        {
            if((attackMask & (1L << i)) != 0)
            {
                blockerPositions.add(i);
            }
        }

        return blockerPositions;
    }


    public long rookAttacksOnTheFly(int square, long blockers)
    {
        int row = square / 8;
        int col = square % 8;

        long attack = 0L;

        //left
        for(int i = col - 1; i >= 0; i--)
        {
            long move = 1L << (63 - (8 * row + i));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        //right
        for(int i = col + 1; i <= 7; i++)
        {
            long move = 1L << (63 - (8 * row + i));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        //up
        for(int i = row - 1; i >= 0; i--)
        {
            long move = 1L << (63 - (8 * i + col));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        //down
        for(int i = row + 1; i <= 7; i++)
        {
            long move = 1L << (63 - (8 * i + col));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        return attack;
    }


    public void fillRookAttacks()
    {
        for(int i = 0; i < 64; i++)
        {
            long rookMask = rookAttackMasks[i];
            long[] blockers = getAllBlockerCombinations(rookMask);

            for(long occupancy : blockers)
            {
                int index = calculateRookIndex(i, occupancy);
                rookAttacks[i][index] = rookAttacksOnTheFly(i, occupancy);
            }
        }
    }

    public void fillRookAttackMasks()
    {
        for(int i = 0; i < 64; i++)
        {
            rookAttackMasks[i] = calculateRookMask(i);
        }
    }

    public int calculateRookIndex(int square, long occupancy)
    {
        long rookMask = rookAttackMasks[square];
        int shift = 64 - rookBits[square];
        return (int)((occupancy & rookMask) * rookMagics[square] >>> shift);
    }

    public long getRookAttacks(long board, long allPieces)
    {
        if(board == 0)
        {
            return 0;
        }
        int square = Long.numberOfLeadingZeros(board);
        long rookMask = rookAttackMasks[square];
        long occupancy = allPieces & rookMask;

        int index = calculateRookIndex(square, occupancy);

        return rookAttacks[square][index];
    }

    public long getAllRookAttacks(long board, long allPieces)
    {
        long moves = 0L;

        while(board != 0)
        {
            long rook = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ rook;

            moves = moves | getRookAttacks(rook, allPieces);
        }

        return moves;
    }

    public long possibleMoves(long board, long allPieces, long friendlyPieces)
    {
        long moves = 0L;

        while(board != 0)
        {
            long rook = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ rook;

            moves = moves | getRookAttacks(rook, allPieces);
        }

        return moves & ~friendlyPieces;
    }

    public ArrayList<Character> calculateRookMoves(long board, long allPieces, long friendlyPieces, long enemyPieces)
    {
        ArrayList<Character> moves = new ArrayList<>();
        while(board != 0)
        {
            long rookStart = 1L << Long.numberOfTrailingZeros(board);
            board = board ^ rookStart;

            long possibleMoves = getRookAttacks(rookStart, allPieces) & ~friendlyPieces;
            while (possibleMoves != 0)
            {
                long move = 1L << Long.numberOfTrailingZeros(possibleMoves);
                possibleMoves &= ~ move;
                if((move & enemyPieces) != 0)
                {

                    moves.add(MoveGeneration.encodeMove(rookStart, move, capture));
                }
                else
                {
                    moves.add(MoveGeneration.encodeMove(rookStart, move, quiet));
                }
            }
        }

        return moves;
    }


}
