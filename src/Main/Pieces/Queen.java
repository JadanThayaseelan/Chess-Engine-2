package Main.Pieces;

import Main.MoveGeneration;

import java.util.ArrayList;

public class Queen
{
    BishopMagic bishopMagic = new BishopMagic();
    RookMagic rookMagic = new RookMagic();
    private byte quiet = 0;
    private byte capture = 4;

    public long possibleMoves(long board, long allPieces, long friendlyPieces)
    {
        return bishopMagic.possibleMoves(board, allPieces, friendlyPieces) | rookMagic.possibleMoves(board, allPieces, friendlyPieces);
    }

    public ArrayList<Character> calculateQueenMoves(long board, long allPieces, long friendlyPieces, long enemyPieces)
    {
        ArrayList<Character> moves = new ArrayList<>();
        while(board != 0)
        {
            long queenStart = 1L << Long.numberOfTrailingZeros(board);
            board = board ^ queenStart;

            long possibleMoves = getQueenAttacks(queenStart, allPieces) & ~friendlyPieces;
            while (possibleMoves != 0)
            {
                long move = 1L << Long.numberOfTrailingZeros(possibleMoves);
                possibleMoves &= ~ move;
                if((move & enemyPieces) != 0)
                {

                    moves.add(MoveGeneration.encodeMove(queenStart, move, capture));
                }
                else
                {
                    moves.add(MoveGeneration.encodeMove(queenStart, move, quiet));
                }
            }
        }

        return moves;
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
