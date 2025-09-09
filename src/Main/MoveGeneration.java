package Main;

public final class MoveGeneration
{
    //flags is meant to be 4 bits but no 4 bit variable
    // 1st bit in flag is promotion
    //2nd bit is capture
    // 3rd bit is special1
    //4th bit is special0
    public static char encodeMove(long startBitboard, long endBitboard, byte flags)
    {
        char startSquare = (char)Long.numberOfLeadingZeros(startBitboard);
        char endSquare = (char)Long.numberOfLeadingZeros(endBitboard);

        return (char) (startSquare << 10 | endSquare << 4 | flags);
    }

    public static int getStartSquare(char move)
    {
        return (move & 0xFC00) >> 10;
    }

    public static int getEndSquare(char move)
    {
        return (move & 0x03F0) >> 4;
    }

}
