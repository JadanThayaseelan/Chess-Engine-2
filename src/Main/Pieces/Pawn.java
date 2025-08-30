package Main.Pieces;

public class Pawn
{
    public long whitePawnStartingPosition = 0x000000000000FF00L;
    public long blackPawnStartingPosition = 0x00FF000000000000L;

    long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
    long notHFileMask = 0xfefefefefefefefeL;
    long notEightFileMask = 0x00ffffffffffffffL;
    long notOneFileMask = 0xffffffffffffff00L;


    public long possibleMoves(long board, long allPieces, long whitePieces, long blackPieces, int turn)
    {
        if(turn % 2 == 0)
        {
            return possibleWhitePawnMoves(board, allPieces, whitePieces, blackPieces);
        }

        return possibleBlackPawnMoves(board, allPieces, whitePieces, blackPieces);
    }

    public long possibleWhitePawnMoves(long board, long allPieces, long whitePieces, long blackPieces)
    {
        long moveOneUp = (board << 8) & (notEightFileMask) & ~allPieces;
        long moveTwoUp = ((board & whitePawnStartingPosition) << 16) & ~allPieces;

        if(moveOneUp == 0L)
        {
            moveTwoUp = 0;
        }

        long takeRight = (board << 7 & (notAFileMask)) & blackPieces;
        long takeLeft = (board << 9 & (notHFileMask))  & blackPieces;

        return (moveOneUp | moveTwoUp | takeRight | takeLeft) & ~whitePieces;
    }

    public long possibleBlackPawnMoves(long board, long allPieces, long whitePieces, long blackPieces)
    {
        long moveOneUp = (board >> 8) & (notOneFileMask) & ~allPieces;
        long moveTwoUp = ((board & blackPawnStartingPosition) >> 16) & ~allPieces;

        if(moveOneUp == 0L)
        {
            moveTwoUp = 0;
        }

        long takeRight = (board >> 9 & (notAFileMask)) & whitePieces;
        long takeLeft = (board >> 7 & (notHFileMask))  & whitePieces;
        return (moveOneUp | moveTwoUp | takeRight | takeLeft) & ~blackPieces;
    }

    public long getPawnAttacks(long board, int turn)
    {
        if(turn % 2 == 0)
        {
            return (board << 7 & (notAFileMask)) | (board << 9 & (notHFileMask));
        }

        return (board >> 9 & (notAFileMask)) | (board >> 7 & (notHFileMask));
    }
}
