package Main;

public class UndoState
{
    public char move;
    public int movedPiece;
    public int capturedPiece;
    public byte cannotCastleFlags;
    public long doublePawnPushSquare;

    public void copyFrom(char move, int movedPiece, int capturedPiece, byte cannotCastleFlags, long doublePawnPushSquare)
    {
        this.move = move;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.cannotCastleFlags = cannotCastleFlags;
        this.doublePawnPushSquare = doublePawnPushSquare;

    }
}
