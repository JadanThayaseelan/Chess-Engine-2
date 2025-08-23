package Main;

import java.util.ArrayList;
import java.util.Random;

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
            0x89a1121896040240L, 0x2004844802002010L, 0x000A442240140800L, 0x0084025141400000L,
            0x0005480000042040L, 0x4088084A00428010L, 0x3908296005082000L, 0x0801000000221204L,
            0x0000600A000204B8L, 0x2100210414200004L, 0x2101008004001084L, 0x00000020080020C0L,
            0x0000000000404082L, 0x0000000800204100L, 0x0000000020D01005L, 0x00000000384000A0L,
            0x0000020044010100L, 0x0000040210030010L, 0x2000400210090102L, 0x0000008004012003L,
            0x0000040880020400L, 0x0000204081010440L, 0x0000000901020280L, 0x8000082080040202L,
            0x0000020082410021L, 0x0000008020000202L, 0x0000100420002501L, 0x0000000800080410L,
            0x0010000400800800L, 0x8000000401040002L, 0x0000000800100082L, 0x0000000100400100L,
            0x0000000040420000L, 0x0000000010900002L, 0x0000000004000028L, 0x0000000C00000080L,
            0x0000002000000208L, 0x0000000020081000L, 0x0000001C00000080L, 0x0000040100100202L,
            0x0000008000008000L, 0x0000000088000800L, 0x000000000000A400L, 0x0000000400002000L,
            0x0000000002010001L, 0x0000000000008801L, 0x0000008000010080L, 0x0000000200000012L,
            0x0000000000210000L, 0x0000000000000800L, 0x0000000204000044L, 0x0000000008000010L,
            0x0000000108000002L, 0x0000000000000801L, 0x0000002000000210L, 0x0000000008000100L,
            0x0000000000208000L, 0x0000000000000A00L, 0x0000000200002020L, 0x0000000008200900L,
            0x0000000028000004L, 0x0000000000000240L, 0x0000008890002200L, 0x0000004000002000L
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

    public long findMagic(int square)
    {
        long mask = 1L << 63 - square;

        boolean isValid = false;
        long magic = 0L;

        int count = 0;

       for(int i = 0; i < 1; i++)
        {

            long[] attacks = new long[512];
            isValid = true;

            Random rand = new Random();
            magic = rand.nextLong() & rand.nextLong() & rand.nextLong();




            for (long blocker : getAllBlockerCombinations(mask))
            {
                count += 1;
                long bishopMask = bishopAttackMasks[square];
                int shift = 64 - bishopBits[square];
                int index =  (int)( (blocker & bishopMask) * magic >>> shift);
                System.out.println(magic);
                System.out.println(blocker);
                System.out.println(bishopMask);

                if (attacks[index] == 1)
                {
                    isValid = false;
                    break;
                }
                attacks[index] = 1;
            }

            if(isValid)
            {
                return magic;
            }
        }

        return 0;
    }



    public static void main (String[] args)
    {
        BishopMagic bishopMagic = new BishopMagic();
        Game game = new Game(null);
        bishopMagic.fillBishopAttackMasks();

        for (long blocker : bishopMagic.getAllBlockerCombinations(bishopMagic.bishopAttackMasks[0]))
        {
            System.out.println("");
            game.displayBitBoard(blocker);
        }
    }



}
