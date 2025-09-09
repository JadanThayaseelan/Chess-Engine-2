package Main.Pieces;

public class Queen
{
    BishopMagic bishopMagic = new BishopMagic();
    RookMagic rookMagic = new RookMagic();

    public long possibleMoves(long board, long allPieces, long friendlyPieces)
    {
        return bishopMagic.possibleMoves(board, allPieces, friendlyPieces) | rookMagic.possibleMoves(board, allPieces, friendlyPieces);
    }

    public long getQueenAttacks(long board, long allPieces)
    {
        return bishopMagic.getBishopAttacks(board, allPieces) | rookMagic.getRookAttacks(board, allPieces);
    }

    public long getAllQueenAttacks(long board, long allPieces)
    {
        long moves = 0L;

        while(board != 0)
        {
            long queen = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ queen;

            moves = moves | getQueenAttacks(queen, allPieces);
        }

        return moves;
    }
}
