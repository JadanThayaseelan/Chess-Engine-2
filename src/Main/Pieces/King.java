package Main.Pieces;

public class King
{
    public long[] kingAttacks = new long[64];

    public King()
    {
        fillKingAttacks();
    }

    public void fillKingAttacks()
    {
        long kingPosition = 0x0000000000000001L;
        for(int i = 0; i < 64; i++)
        {
            long currentKingPosition = kingPosition << (63 - i);

            if(i == 0)
            {
                kingAttacks[i] = 0x40C0000000000000L;
            }
            else
            {
                kingAttacks[i] = calculateKingAttacks(currentKingPosition);
            }

        }
    }

    public long calculateKingAttacks(long bitBoard)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;
        long notEightFileMask = 0x00ffffffffffffffL;
        long notOneFileMask = 0xffffffffffffff00L;

        long northMove = bitBoard << 8 & notOneFileMask;
        long northEastMove = bitBoard << 7 & (notAFileMask | notOneFileMask);

        long eastMove = bitBoard >> 1 & notAFileMask;


        long southEastMove = (bitBoard >>> 9) & notEightFileMask;

        long southMove = bitBoard >> 8 & notEightFileMask;
        long southWestMove = bitBoard >> 7 & (notHFileMask | notEightFileMask);

        long westMove = bitBoard << 1 & notHFileMask;

        long northWestMove = bitBoard << 9 & (notHFileMask | notOneFileMask);

        return northMove | northEastMove | eastMove | southEastMove | southMove | southWestMove | westMove | northWestMove;
    }

    public long getKingAttack(long board)
    {
        if(board == 0)
        {
            return 0;
        }
        int index = Long.numberOfLeadingZeros(board);
        return kingAttacks[index];
    }



}
