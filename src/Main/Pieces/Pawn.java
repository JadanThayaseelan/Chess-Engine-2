package Main.Pieces;

import Main.Bitboard;
import Main.MoveGeneration;

import java.util.ArrayList;

public class Pawn
{
    public long whitePawnStartingPosition = 0x000000000000FF00L;
    public long blackPawnStartingPosition = 0x00FF000000000000L;

    long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
    long notHFileMask = 0xfefefefefefefefeL;
    long notEightFileMask = 0x00ffffffffffffffL;
    long notOneFileMask = 0xffffffffffffff00L;

    long eightFileMask = 0xFF00000000000000L;
    long oneFileMask = 0x00000000000000FFL;

    byte quiet = 0;
    byte doublePawnPush = 1;
    byte capture = 4;
    byte enPassant = 5;
    byte knightPromotion = 8;
    byte bishopPromotion = 9;
    byte rookPromotion = 10;
    byte queenPromotion = 11;

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
        long moveOneUp = (board << 8) & (notOneFileMask) & ~allPieces;
        long moveTwoUp = board;


        moveTwoUp = ((moveTwoUp & whitePawnStartingPosition) << 8) & ~allPieces;
        moveTwoUp = ((moveTwoUp) << 8) & ~allPieces;

        long takeRight = (board << 7 & (notAFileMask)) & blackPieces;
        long takeLeft = (board << 9 & (notHFileMask))  & blackPieces;

        return (moveOneUp | moveTwoUp | takeRight | takeLeft) & ~whitePieces;
    }

    public long possibleBlackPawnMoves(long board, long allPieces, long whitePieces, long blackPieces)
    {
        long moveOneUp = (board >> 8) & (notEightFileMask) & ~allPieces;
        long moveTwoUp = board;

        moveTwoUp = ((moveTwoUp & blackPawnStartingPosition) >> 8) & ~allPieces;
        moveTwoUp = ((moveTwoUp) >> 8) & ~allPieces;

        if(moveOneUp == 0L)
        {
            moveTwoUp = 0;
        }

        long takeRight = (board >> 9 & (notAFileMask)) & whitePieces;
        long takeLeft = (board >> 7 & (notHFileMask))  & whitePieces;
        return (moveOneUp | moveTwoUp | takeRight | takeLeft) & ~blackPieces;
    }



    public ArrayList<Character> calculatePawnMoves(long board, long allPieces, long whitePieces, long blackPieces, int turn, long doublePawnPushBitboard)
    {
        if(turn % 2 == 0)
        {
            return calculateWhitePawnMoves(board, allPieces, blackPieces, doublePawnPushBitboard);
        }
        return calculateBlackPawnMoves(board, allPieces, whitePieces, doublePawnPushBitboard);
    }

    public ArrayList<Character> calculateWhitePawnMoves(long board, long allPieces,  long blackPieces, long doublePawnPushBitboard)
    {
        ArrayList<Character> moves = new ArrayList<>();

        long moveOneUp = (board << 8) & (notOneFileMask) & ~allPieces;
        while(moveOneUp != 0)
        {
            long endSquare =  (1L << Long.numberOfTrailingZeros(moveOneUp));
            moveOneUp &= ~endSquare;

            long startSquare = (endSquare  >> 8);
            if((endSquare & eightFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, queenPromotion));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, knightPromotion));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, bishopPromotion));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, rookPromotion));
            }
            else
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, quiet));
            }
        }



        long moveTwoUp = ((board & whitePawnStartingPosition) << 8) & ~allPieces;
        moveTwoUp = ((moveTwoUp) << 8) & ~allPieces;
        while(moveTwoUp != 0)
        {
            long endSquare = (1L << Long.numberOfTrailingZeros(moveTwoUp));
            moveTwoUp &= ~endSquare;
            long startSquare = (endSquare >> 16);
            moves.add(MoveGeneration.encodeMove(startSquare, endSquare, doublePawnPush));
        }

        long takeRight = (board << 7 & (notAFileMask)) & blackPieces;
        while(takeRight != 0)
        {
            long endSquare = (1L << Long.numberOfTrailingZeros(takeRight));
            takeRight &= ~endSquare;
            long startSquare = endSquare >> 7;
            if((endSquare & eightFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (queenPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (knightPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (bishopPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (rookPromotion | capture)));
            }
            else
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, capture));
            }
        }

        long takeLeft = (board << 9 & (notHFileMask))  & blackPieces;
        while(takeLeft != 0)
        {
            long endSquare = (1L << Long.numberOfTrailingZeros(takeLeft));
            takeLeft &= ~endSquare;
            long startSquare = endSquare >> 9;
            if((endSquare & eightFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (queenPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (knightPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (bishopPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (rookPromotion | capture)));
            }
            else
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, capture));
            }
        }

        if(doublePawnPushBitboard != 0 && ((doublePawnPushBitboard << 8) & allPieces) == 0)
        {
            if(((doublePawnPushBitboard << 1) & board & notHFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(doublePawnPushBitboard << 1, doublePawnPushBitboard << 8, enPassant));
            }
            if(((doublePawnPushBitboard >> 1) & board & notAFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(doublePawnPushBitboard >> 1, doublePawnPushBitboard << 8, enPassant));
            }
        }


        return moves;
    }

    public ArrayList<Character> calculateBlackPawnMoves(long board, long allPieces, long whitePieces, long doublePawnPushBitboard)
    {
        ArrayList<Character> moves = new ArrayList<>();

        long moveOneUp = (board >> 8) & (notEightFileMask) & ~allPieces;
        while(moveOneUp != 0)
        {
            long endSquare =  (1L << Long.numberOfTrailingZeros(moveOneUp));
            moveOneUp &= ~endSquare;

            long startSquare = (endSquare << 8);
            if((endSquare & oneFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, queenPromotion));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, knightPromotion));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, bishopPromotion));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, rookPromotion));
            }
            else
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, quiet));
            }
        }

        long moveTwoUp = board;

        moveTwoUp = ((moveTwoUp & blackPawnStartingPosition) >> 8) & ~allPieces;
        moveTwoUp = ((moveTwoUp) >> 8) & ~allPieces;
        while(moveTwoUp != 0)
        {
            long endSquare = (1L << Long.numberOfTrailingZeros(moveTwoUp));
            moveTwoUp &= ~endSquare;
            long startSquare = (endSquare << 16);
            moves.add(MoveGeneration.encodeMove(startSquare, endSquare, doublePawnPush));
        }

        long takeRight = (board >> 9 & (notAFileMask)) & whitePieces;
        while(takeRight != 0)
        {
            long endSquare = (1L << Long.numberOfTrailingZeros(takeRight));
            takeRight &= ~endSquare;
            long startSquare = endSquare << 9;
            if((endSquare & oneFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (queenPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (knightPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (bishopPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (rookPromotion | capture)));
            }
            else
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, capture));
            }
        }

        long takeLeft = (board >> 7 & (notHFileMask))  & whitePieces;
        while(takeLeft != 0)
        {
            long endSquare = (1L << Long.numberOfTrailingZeros(takeLeft));
            takeLeft &= ~endSquare;
            long startSquare = endSquare << 7;
            if((endSquare & oneFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (queenPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (knightPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (bishopPromotion | capture)));
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, (byte) (rookPromotion | capture)));
            }
            else
            {
                moves.add(MoveGeneration.encodeMove(startSquare, endSquare, capture));
            }
        }


        if(doublePawnPushBitboard != 0 && ((doublePawnPushBitboard >> 8) & allPieces) == 0)
        {
            if(((doublePawnPushBitboard << 1) & board & notHFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(doublePawnPushBitboard << 1, doublePawnPushBitboard >> 8, enPassant));
            }
            if(((doublePawnPushBitboard >> 1) & board & notAFileMask) != 0)
            {
                moves.add(MoveGeneration.encodeMove(doublePawnPushBitboard >> 1, doublePawnPushBitboard >> 8, enPassant));
            }
        }

        return moves;
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
