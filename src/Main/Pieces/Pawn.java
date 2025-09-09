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


    public ArrayList<Character> calculatePawnMoves(int startSquare, long allPieces, long whitePieces, long blackPieces, int turn)
    {
        if(turn % 2 == 0)
        {
            return calculateWhitePawnMoves(startSquare, allPieces, blackPieces);
        }

        return calculateBlackPawnMoves(startSquare, allPieces, whitePieces);
    }

    public ArrayList<Character> calculateWhitePawnMoves(int startSquare, long allPieces,  long blackPieces)
    {
        ArrayList<Character> moves = new ArrayList<>();

        long startBitboard = Bitboard.convertSquareToBitboard(startSquare);


        long moveOneUp = (startBitboard << 8) & (notEightFileMask) & ~allPieces;
        if(moveOneUp != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, moveOneUp, (byte) 0));
        }

        long moveTwoUp = startBitboard;
        moveTwoUp = ((moveTwoUp & whitePawnStartingPosition) << 8) & ~allPieces;
        moveTwoUp = ((moveTwoUp) << 8) & ~allPieces;

        if(moveTwoUp != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, moveTwoUp, (byte) 0));
        }

        long takeRight = (startBitboard << 7 & (notAFileMask)) & blackPieces;
        if(takeRight != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, takeRight, (byte) 0b0100));
        }

        long takeLeft = (startBitboard << 9 & (notHFileMask))  & blackPieces;
        if(takeLeft != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, takeLeft, (byte) 0b0100));
        }

        return moves;
    }

    public ArrayList<Character> calculateBlackPawnMoves(int startSquare, long allPieces, long whitePieces)
    {
        ArrayList<Character> moves = new ArrayList<>();

        long startBitboard = Bitboard.convertSquareToBitboard(startSquare);


        long moveOneUp = (startBitboard >> 8) & (notEightFileMask) & ~allPieces;
        if(moveOneUp != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, moveOneUp, (byte) 0));
        }

        long moveTwoUp = startBitboard;
        moveTwoUp = ((moveTwoUp & whitePawnStartingPosition) >> 8) & ~allPieces;
        moveTwoUp = ((moveTwoUp) >> 8) & ~allPieces;

        if(moveTwoUp != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, moveTwoUp, (byte) 0));
        }

        long takeRight = (startBitboard >> 7 & (notAFileMask)) & whitePieces;
        if(takeRight != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, takeRight, (byte) 0b0100));
        }

        long takeLeft = (startBitboard >> 9 & (notHFileMask))  & whitePieces;
        if(takeLeft != 0)
        {
            moves.add(MoveGeneration.encodeMove(startBitboard, takeLeft, (byte) 0b0100));
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
