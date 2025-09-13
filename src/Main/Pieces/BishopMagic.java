package Main.Pieces;

import Main.MoveGeneration;

import java.util.ArrayList;
import java.util.Random;

public class BishopMagic
{

    private byte quiet = 0;
    byte capture = 4;

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
            0x89a1121896040240L,
            0x080004480A000000L,
            0x2068080051921000L,
            0x62880a0220200808L,

            0x0004042004000000L,
            0x0100822020200011L,
            0xc00444222012000aL,
            0x0028808801216001L,

            0x0400492088408100L,
            0x0201c401040c0084L,
            0x00840800910a0010L,
            0x300000261044000aL,

            0x2000840504006000L,
            0x30010c4108405004L,
            0x1008005410080802L,
            0x8144042209100900L,

            0x2080810200144000L,
            0x4800201208ca0000L,
            0xf181404080120080L,
            0x1004002802102001L,

            0x8410008200808110L,
            0x40200200a4200800L,
            0x8000540420000000L,
            0x88010400410c9000L,

            0x5200404701042900L,
            0x1004040051500081L,
            0x2002081833080021L,
            0x400c00c010142000L,

            0x941408200c002000L,
            0x6588100008060110L,
            0x188071040440a00L,
            0x4800404002011c00L,

            0x104442040404200L,
            0x511080202091021L,
            0x4022401120400L,
            0x80c0040400080120L,

            0x8040010040820802L,
            0x480810700020090L,
            0x102008e00040242L,
            0x809005202050100L,

            0x8002024220104080L,
            0x431008804142000L,
            0x19001802081400L,
            0x200014208040080L,

            0x3308082008200100L,
            0x41010500040c020L,
            0x4012020c04210308L,
            0x208220a202004080L,

            0x111040120082000L,
            0x6803040141280a00L,
            0x2101004202410000L,
            0x8200000041108022L,

            0x21082088000L,
            0x2410204010040L,
            0x40100400809000L,
            0x822088220820214L,

            0x40808090012004L,
            0x910224040218c9L,
            0x402814422015008L,
            0x90014004842410L,

            0x1000042304105L,
            0x10008830412a00L,
            0x2520081090008908L,
            0x40102000a0a60140L,
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
                if(bishopAttacks[i][index] == 0L)
                {
                    bishopAttacks[i][index] = bishopAttacksOnTheFly(i, occupancy);

                } else if (bishopAttacks[i][index] != bishopAttacksOnTheFly(i, occupancy))
                {
                    duplicates += 1;
                    break;
                }

            }
        }
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

    public long getBishopAttacks(long board, long allPieces)
    {
        if(board == 0)
        {
            return 0;
        }
        int square = Long.numberOfLeadingZeros(board);
        long bishopMask = bishopAttackMasks[square];
        long occupancy = allPieces & bishopMask;

        int index = calculateBishopIndex(square, occupancy);

        return bishopAttacksOnTheFly(square, allPieces);
    }

    public long getAllBishopAttacks(long board, long allPieces)
    {
        long moves = 0L;

        while(board != 0)
        {
            long bishop = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ bishop;

            moves = moves | getBishopAttacks(bishop, allPieces);
        }

        return moves;
    }

    public long findMagic(int square)
    {
        long mask = calculateBishopMask(square);

        boolean isValid = false;
        long magic = 0L;

        int count = 0;

       for(int i = 0; i < 1000000000; i++)
        {

            long[] attacks = new long[512];
            isValid = true;

            Random rand = new Random();
            magic = rand.nextLong() & rand.nextLong() & rand.nextLong();
            System.out.println(magic);

            for (long blocker : getAllBlockerCombinations(mask))
            {
                int index = calculateBishopIndex(square, blocker);

                if (attacks[index] == 0)
                {
                    attacks[index] = bishopAttacksOnTheFly(square, blocker);

                }
                else if (attacks[index] != bishopAttacksOnTheFly(square, blocker))
                {
                    isValid = false;
                    break;
                }

            }

            if(isValid)
            {
                return magic;
            }
        }


        return 0;
    }

    public long possibleMoves(long board, long allPieces, long friendlyPieces)
    {
        long moves = 0L;

        while(board != 0)
        {
            long bishop = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ bishop;

            moves = moves | getBishopAttacks(bishop, allPieces);
        }

        return moves & ~friendlyPieces;
    }

    public ArrayList<Character> calculateBishopMoves(long board, long allPieces, long friendlyPieces, long enemyPieces)
    {
        ArrayList<Character> moves = new ArrayList<>();
        while(board != 0)
        {
            long bishopStart = 1L << Long.numberOfTrailingZeros(board);
            board = board ^ bishopStart;

            long possibleMoves = getBishopAttacks(bishopStart, allPieces) & ~friendlyPieces;
            while (possibleMoves != 0)
            {
                long move = 1L << Long.numberOfTrailingZeros(possibleMoves);
                possibleMoves &= ~move;
                if((move & enemyPieces) != 0)
                {
                    moves.add(MoveGeneration.encodeMove(bishopStart, move, capture));
                }
                else
                {
                    moves.add(MoveGeneration.encodeMove(bishopStart, move, quiet));
                }
            }
        }

        return moves;
    }
}
