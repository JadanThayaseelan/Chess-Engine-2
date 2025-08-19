package Main;

import java.util.ArrayList;

public class BishopMagic
{

    public BishopMagic()
    {
        fillBishopAttackMasks();
        fillBishopAttacks();
    }

    int[] bishopBits = {
            6, 5, 5, 5, 5, 5, 5, 6,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 5, 5, 5, 5, 5, 5, 6
    };

    long[] bishopMagics = {
            0x007bfeffbfeffbffL, 0x003effbfeffbfe08L, 0x0000401020200000L, 0x0000200810000000L,
            0x0000110080000000L, 0x0000080100800000L, 0x0007efe0bfff8000L, 0x00000fb0203fff80L,
            0x00007dff7fdff7fdL, 0x0000011fdff7efffL, 0x0000004010202000L, 0x0000002008100000L,
            0x0000001100800000L, 0x0000000801008000L, 0x000007efe0bfff80L, 0x000000080f9fffc0L,
            0x0000400080808080L, 0x0000200040404040L, 0x0000400080808080L, 0x0000200200801000L,
            0x0000240080840000L, 0x0000080080840080L, 0x0000040010410040L, 0x0000020008208020L,
            0x0000804000810100L, 0x0000402000408080L, 0x0000804000810100L, 0x0000404004010200L,
            0x0000404004010040L, 0x0000101000804400L, 0x0000080800104100L, 0x0000040400082080L,
            0x0000410040008200L, 0x0000208020004100L, 0x0000110080040008L, 0x0000020080080080L,
            0x0000404040040100L, 0x0000202040008040L, 0x0000101010002080L, 0x0000080808001040L,
            0x0000208200400080L, 0x0000104100200040L, 0x0000208200400080L, 0x0000008840200040L,
            0x0000020040100100L, 0x007fff80c0280050L, 0x0000202020200040L, 0x0000101010100020L,
            0x0007ffdfc17f8000L, 0x0003ffefe0bfc000L, 0x0000000820806000L, 0x00000003ff004000L,
            0x0000000100202000L, 0x0000004040802000L, 0x007ffeffbfeff820L, 0x003fff7fdff7fc10L,
            0x0003ffdfdfc27f80L, 0x000003ffefe0bfc0L, 0x0000000008208060L, 0x0000000003ff0040L,
            0x0000000001002020L, 0x0000000040408020L, 0x00007ffeffbfeff9L, 0x007ffdff7fdff7fdL
    };

    long[][] bishopAttacks = new long[64][512];

    long[] bishopAttackMasks = new long[64];

    public long calculateBishopMask(int square)
    {
        long mask = 0L;

        int row = square / 8;
        int col = square % 8;

        //left up
        for(int r = row - 1, c = col - 1; r >= 1 && c >= 1; r--, c--)
        {
            mask = mask | 1L << (63 - (8 * r + c));
        }

        //right up
        for(int r = row - 1, c = col + 1; r >= 1 && c <= 6; r--, c++)
        {
            mask = mask | 1L << (63 - (8 * r + c));
        }

        //left down
        for(int r = row + 1, c = col - 1; r <= 6 && c >= 1; r++, c--)
        {
            mask = mask | 1L << (63 - (8 * r + c));
        }

        //right down
        for(int r = row + 1, c = col + 1; r <= 6 && c <= 6; r++, c++)
        {
            mask = mask | 1L << (63 - (8 * r + c));
        }

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

    public long bishopAttacksOnTheFly(int square, long blockers)
    {
        int row = square / 8;
        int col = square % 8;

        long attack = 0L;

        //left up
        for(int r = row - 1, c = col - 1; r >= 0 && c >= 0; r--, c--)
        {
            long move = 1L << (63 - (8 * r + c));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        //right up
        for(int r = row - 1, c = col + 1; r >= 0 && c <= 7; r--, c++)
        {
            long move = 1L << (63 - (8 * r + c));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        //left down
        for(int r = row + 1, c = col - 1; r <= 7 && c >= 0; r++, c--)
        {
            long move = 1L << (63 - (8 * r + c));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        //right down
        for(int r = row + 1, c = col + 1; r <= 7 && c <= 7; r++, c++)
        {
            long move = 1L << (63 - (8 * r + c));
            attack = attack | move;
            if((move & blockers) != 0)
            {
                break;
            }
        }

        return attack;
    }


    public void fillBishopAttacks()
    {
        int duplicates = 0;
        double count = 0;
        for(int i = 0; i < 64; i++)
        {
            long bishopMask = bishopAttackMasks[i];
            long[] blockers = getAllBlockerCombinations(bishopMask);

            count += 1;
            for(long occupancy : blockers)
            {
                int index = calculateBishopIndex(i, occupancy);
                if(bishopAttacks[i][index] != 0)
                {
                    duplicates += 1;

                }
                bishopAttacks[i][index] = bishopAttacksOnTheFly(i, occupancy);
            }
        }

        System.out.println("Percentage of duplicates: " + duplicates / count);
    }



    public void fillBishopAttackMasks()
    {
        for(int i = 0; i < 64; i++)
        {
            bishopAttackMasks[i] = calculateBishopMask(i);
        }
    }

    public int calculateBishopIndex(int square, long occupancy)
    {
        long bishopMask = bishopAttackMasks[square];
        int shift = 64 - bishopBits[square];
        return (int)((occupancy & bishopMask) * bishopMagics[square] >>> shift);
    }

    public long getBishopAttacks(long board, long allPieces, long friendlyPieces)
    {
        int square = Long.numberOfLeadingZeros(board);
        long bishopMask = bishopAttackMasks[square];
        long occupancy = allPieces & bishopMask;

        int index = calculateBishopIndex(square, occupancy);

        return bishopAttacks[square][index] & ~friendlyPieces;
    }

}
