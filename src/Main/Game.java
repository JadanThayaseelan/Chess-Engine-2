package Main;


import Main.Pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;


/**
 * Using indexes instead of hashmaps because it is better for performance
 * 0 - P
 * 1 - N
 * 2 - B
 * 3 - R
 * 4 - Q
 * 5 - K
 * 
 * 6 - p
 * 7 - n
 * 8 - b
 * 9 - r
 * 10 - q
 * 11 - k
 *
 * cannotCastleFlags
 * white short castle
 * white long castle
 * black short castle
 * black long castle
 */
public class Game
{
    public long[] bitBoards = new long[12];

    public Pawn pawn = new Pawn();
    public Knight knight = new Knight();
    public RookMagic rookMagic = new RookMagic();
    public BishopMagic bishopMagic = new BishopMagic();
    public Queen queen = new Queen();
    public King king = new King();

    public int turn = 0;
    public int ply = 0;

    public byte cannotCastleFlags = 0b0000;

    public long whitePiecesBitboard;
    public long blackPiecesBitboard;
    public long allPiecesBitboard;

    public long doublePawnPushBitboard;

    private byte quiet = 0;
    private byte capture = 4;
    private byte kingCastle = 2;
    private byte queenCastle = 3;

    int maxDepth = 1024;
    UndoState[] undoStack = new UndoState[maxDepth];
    public int moveCount = 0;

    public Game(String[][] stringBoard)
    {
        if(stringBoard == null) {
            stringBoard = new String[][]{
                    {"r", "n", "b", "q", "k", "b", "n", "r"},
                    {"p", "p", "p", "p", "p", "p", "p", "p"},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {" ", " ", " ", " ", " ", " ", " ", " "},
                    {"P", "P", "P", "P", "P", "P", "P", "P"},
                    {"R", "N", "B", "Q", "K", "B", "N", "R"}
            };
//            stringBoard = new String[][]{
//                    {" ", " ", " ", " ", " ", " ", "k", " "},
//                    {"K", "p", " ", " ", " ", " ", "p", "p"},
//                    {"p", " ", " ", " ", " ", " ", " ", " "},
//                    {"P", "r", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", "q", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//            };

        }

        for(int i = 0; i < maxDepth; i++)
        {
            undoStack[i] = new UndoState();
        }

        bitBoards = Bitboard.convertStringBoardToBitBoards(stringBoard);
        whitePiecesBitboard = Bitboard.getWhitePieces(bitBoards);
        blackPiecesBitboard = Bitboard.getBlackPieces(bitBoards);
        allPiecesBitboard = Bitboard.getAllPieces(bitBoards);

    }


    public long getFriendlyPieces()
    {
        if(turn % 2 == 0)
        {
            return whitePiecesBitboard;
        }
        return blackPiecesBitboard;
    }

    public long getEnemyPieces()
    {
        if(turn % 2 == 0)
        {
            return blackPiecesBitboard;
        }

        return whitePiecesBitboard;
    }

    public char[] calculateAllPseudoLegalMoves()
    {
        char[] moves = new char[256];
        moveCount = 0;
        int turnOffset = 0;
        if(turn % 2 == 0)
        {
            moveCount = pawn.calculateWhitePawnMoves(bitBoards[0], allPiecesBitboard, blackPiecesBitboard, doublePawnPushBitboard, moveCount, moves);
        }
        else
        {
            turnOffset = 6;
            moveCount = pawn.calculateBlackPawnMoves(bitBoards[6], allPiecesBitboard, whitePiecesBitboard, doublePawnPushBitboard, moveCount, moves);
        }

        moveCount = knight.calculateKnightMoves(bitBoards[1+turnOffset], getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        moveCount = bishopMagic.calculateBishopMoves(bitBoards[2+turnOffset], allPiecesBitboard, getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        moveCount = rookMagic.calculateRookMoves(bitBoards[3+turnOffset], allPiecesBitboard, getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        moveCount = queen.calculateQueenMoves(bitBoards[4+turnOffset], allPiecesBitboard, getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        moveCount = calculateKingMoves(bitBoards[5+turnOffset], getEnemyPieces(), moveCount, moves);

        return moves;
    }

    public boolean isMoveLegal(char move)
    {
        makeEngineMove(move);
        turn -= 1;
        boolean isCheck = isInCheck();
        turn += 1;
        undoEngineMove();

        return !isCheck;
    }

    public ArrayList<Character> calculateAllLegalMoves()
    {
        ArrayList<Character> legalMoves = new ArrayList<>();
        for(char move : calculateAllPseudoLegalMoves())
        {
            if(isMoveLegal(move))
            {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }



    public char[] calculatePseudoLegalMoves(long selectedPieceBitboard)
    {

        char[] moves = new char[256];
        if((selectedPieceBitboard & bitBoards[0]) != 0)
        {
            moveCount = pawn.calculateWhitePawnMoves(selectedPieceBitboard, allPiecesBitboard, blackPiecesBitboard, doublePawnPushBitboard, moveCount, moves);
        }
        else if((selectedPieceBitboard & bitBoards[6]) != 0)
        {
            moveCount =pawn.calculateBlackPawnMoves(selectedPieceBitboard, allPiecesBitboard, whitePiecesBitboard, doublePawnPushBitboard, moveCount, moves);
        }

        else if((selectedPieceBitboard & (bitBoards[1] | bitBoards[7])) != 0)
        {
            moveCount =knight.calculateKnightMoves(selectedPieceBitboard, getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        }

        else if((selectedPieceBitboard & (bitBoards[2] | bitBoards[8])) != 0)
        {
            moveCount =bishopMagic.calculateBishopMoves(selectedPieceBitboard, allPiecesBitboard, getFriendlyPieces(),  getEnemyPieces(), moveCount, moves);
        }

        else if((selectedPieceBitboard & (bitBoards[3] | bitBoards[9])) != 0)
        {
            moveCount =rookMagic.calculateRookMoves(selectedPieceBitboard, allPiecesBitboard, getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        }

        else if((selectedPieceBitboard & (bitBoards[4] | bitBoards[10])) != 0)
        {
            moveCount =queen.calculateQueenMoves(selectedPieceBitboard, allPiecesBitboard, getFriendlyPieces(), getEnemyPieces(), moveCount, moves);
        }

        else if((selectedPieceBitboard & (bitBoards[5] | bitBoards[11])) != 0)
        {
            moveCount = calculateKingMoves(selectedPieceBitboard, getEnemyPieces(), moveCount, moves);
        }
        else
        {
            return null;
        }

        return moves;
    }

    public ArrayList<Character> calculateLegalMoves(long selectedPieceBitboard)
    {
        ArrayList<Character> legalMoves = new ArrayList<>();

        char[] pseudoLegalMoves = calculatePseudoLegalMoves(selectedPieceBitboard);
        if(pseudoLegalMoves == null)
        {
            return null;
        }

        for(char move : pseudoLegalMoves)
        {
            if(move != 0 && isMoveLegal(move))
            {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    public char getValidMove(char moveToValidate)
    {
        ArrayList<Character> legalMoves = calculateLegalMoves(1L << 63 - MoveGeneration.getStartSquare(moveToValidate));
        if(legalMoves == null)
        {
            return 0;
        }

        for (char move : legalMoves)
        {
            if (MoveGeneration.getEndSquare(move) == MoveGeneration.getEndSquare(moveToValidate))
            {
                return move;
            }
        }
        return 0;
    }

    public long convertEncodedMovesToBitboard(ArrayList<Character> moves)
    {
        long combinedBitboard = 0L;
        for(char move : moves)
        {
            int endSquare = MoveGeneration.getEndSquare(move);
            long endBitboard = 1L << 63 - endSquare;
            combinedBitboard |= endBitboard;
        }
        return combinedBitboard;
    }

    public void makeEngineMove(char move)
    {
        int startSquare = MoveGeneration.getStartSquare(move);
        int endSquare = MoveGeneration.getEndSquare(move);

        long pieceToMoveBitBoard = 1L << (63 - startSquare);
        long moveBitBoard = 1L << (63 - endSquare);

        int pieceIndex = determineFriendlyPieceIndex(pieceToMoveBitBoard);
        int capturedPieceIndex = determineEnemyPieceIndex(moveBitBoard);

        byte flags = (byte) MoveGeneration.getFlags(move);

        undoStack[ply].copyFrom(move, pieceIndex,  capturedPieceIndex, cannotCastleFlags, doublePawnPushBitboard);
        updateCastlingRights(pieceToMoveBitBoard, moveBitBoard, capturedPieceIndex);

        switch (flags)
        {
            case 0b0010: shortCastle(); break;
            case 0b0011: longCastle(); break;
            case (0b0101):
            {
                enPassant(pieceToMoveBitBoard, moveBitBoard);
                break;
            }
        }

        //Moves targeted piece and removes enemy captured piece if there is one
        bitBoards[pieceIndex] &= ~pieceToMoveBitBoard;
        bitBoards[pieceIndex] |= moveBitBoard;
        if((flags & 0b0100) != 0 && (flags != 0b0101))
        {
            if(capturedPieceIndex != -1)
            {
                bitBoards[capturedPieceIndex] &= ~moveBitBoard;
            }
            else
            {
                System.out.println(startSquare);
                System.out.println(endSquare);
                throw new IllegalArgumentException("Illegal move attempted");
            }
        }

        //promotion
        if((flags & 0b1000) != 0)
        {
            promote(flags);
        }

        //Double pawn push
        if(flags == 0b0001)
        {
            doublePawnPushBitboard = moveBitBoard;
        }
        else
        {
            doublePawnPushBitboard = 0;
        }

        whitePiecesBitboard = Bitboard.getWhitePieces(bitBoards);
        blackPiecesBitboard = Bitboard.getBlackPieces(bitBoards);
        allPiecesBitboard = Bitboard.getAllPieces(bitBoards);

        turn += 1;
        ply += 1;

    }

    public void undoEngineMove()
    {
        turn -= 1;
        ply -= 1;
        UndoState undoState = undoStack[ply];

        int startSquare = MoveGeneration.getStartSquare(undoState.move);
        int endSquare = MoveGeneration.getEndSquare(undoState.move);

        long pieceToMoveBitBoard = 1L << (63 - startSquare);
        long moveBitBoard = 1L << (63 - endSquare);

        byte flags = (byte) (undoState.move & 0b1111);

        switch (flags)
        {
            case 0b0010: undoShortCastle(); break;
            case 0b0011: undoLongCastle(); break;
            case 0b0101: undoEnPassant(pieceToMoveBitBoard, moveBitBoard); break;
        }

        //undo promotion
        if((flags & 0b1000) != 0)
        {
            undoPromote(flags, undoState.move);
        }


        bitBoards[undoState.movedPiece] &= ~moveBitBoard;
        bitBoards[undoState.movedPiece] |= pieceToMoveBitBoard;
        if((flags & 0b0100) != 0 && (flags != 0b0101))
        {
            bitBoards[undoState.capturedPiece] |= (moveBitBoard);
        }

        doublePawnPushBitboard = undoState.doublePawnPushSquare;
        cannotCastleFlags = undoState.cannotCastleFlags;

        whitePiecesBitboard = Bitboard.getWhitePieces(bitBoards);
        blackPiecesBitboard = Bitboard.getBlackPieces(bitBoards);
        allPiecesBitboard = Bitboard.getAllPieces(bitBoards);
    }

    public int determineFriendlyPieceIndex(long selectedPieceBitboard)
    {
        int start = 0;
        int end = 5;
        if(turn % 2 != 0)
        {
            start = 6;
            end = 11;
        }
        for(int i = start; i <= end; i++)
        {
            if((bitBoards[i] & selectedPieceBitboard) != 0)
            {
                return i;
            }
        }

        return -1;
    }

    public int determineEnemyPieceIndex(long selectedPieceBitboard)
    {
        int start = 6;
        int end = 11;
        if(turn % 2 != 0)
        {
            start = 0;
            end = 5;
        }
        for(int i = start; i <= end; i++)
        {
            if((bitBoards[i] & selectedPieceBitboard) != 0)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean hasRookMovedWithoutCastling(long pieceBoard, long rookStartingPosition)
    {
        int rook = 3;
        if(turn % 2 != 0)
        {
            rook = 9;
        }

        return determinePieceIndexFromBitBoard(pieceBoard) == (rook) && (pieceBoard & rookStartingPosition) != 0;
    }

    public boolean hasKingMovedWithoutCastling(long pieceBoard)
    {
        int king = 5;
        if(turn % 2 != 0)
        {
            king = 11;
        }

        return determinePieceIndexFromBitBoard(pieceBoard) == (king);
    }

    public void updateCastlingRights(long pieceToMoveBitBoard, long moveBitboard, int capturedPieceIndex) {
        if (turn % 2 == 0)
        {
            if (determinePieceIndexFromBitBoard(pieceToMoveBitBoard) == 5) {
                cannotCastleFlags |= 0b1100;
            }
            // White rooks moved
            if ((pieceToMoveBitBoard & 0x0000000000000001L) != 0) { // a1
                cannotCastleFlags |= 0b1000;
            }
            if ((pieceToMoveBitBoard & 0x0000000000000080L) != 0) { // h1
                cannotCastleFlags |= 0b0100;
            }
            // White rooks captured
            if (capturedPieceIndex == 3 && (moveBitboard & 0x0000000000000001L) != 0) { // rook on a1
                cannotCastleFlags |= 0b1000;
            }
            if (capturedPieceIndex == 3 && (moveBitboard & 0x0000000000000080L) != 0) { // rook on h1
                cannotCastleFlags |= 0b0100;
            }
        } else
        {
            if (determinePieceIndexFromBitBoard(pieceToMoveBitBoard) == 11) {
                cannotCastleFlags |= 0b0011;
            }
            // Black rooks moved
            if ((pieceToMoveBitBoard & 0x0100000000000000L) != 0) { // a8
                cannotCastleFlags |= 0b0010;
            }
            if ((pieceToMoveBitBoard & 0x8000000000000000L) != 0) { // h8
                cannotCastleFlags |= 0b0001;
            }
            // Black rooks captured
            if (capturedPieceIndex == 9 && (moveBitboard & 0x0100000000000000L) != 0) { // rook on a8
                cannotCastleFlags |= 0b0010;
            }
            if (capturedPieceIndex == 9 && (moveBitboard & 0x8000000000000000L) != 0) { // rook on h8
                cannotCastleFlags |= 0b0001;
            }
        }
    }

    public void updateEnemyPiecesBitBoard(long moveBitBoard)
    {
        if(turn % 2 == 0)
        {
            for(int i = 6; i < 12; i++)
            {
                bitBoards[i] = bitBoards[i] & ~ moveBitBoard;
            }
        }
        else
        {
            for(int i = 0; i < 6; i++)
            {
                bitBoards[i] = bitBoards[i] & ~ moveBitBoard;
            }
        }
        
    }


    public int determinePieceIndexFromBitBoard(long bitboard)
    {
        for(int i = 0; i < bitBoards.length; i++)
        {
            if((bitBoards[i] & bitboard) != 0)
            {
                  return i;
            }
        }

        return -1;
    }

    public void displayBitBoard(long board)
    {
        String stringBitBoard = String.format("%64s", Long.toBinaryString(board)).replace(' ', '0');
        for(int i = 0; i < stringBitBoard.length(); i++)
        {
            if(i % 8 == 0)
            {
                System.out.println("");
            }
            System.out.print(stringBitBoard.charAt(i));
        }
    }



    public int calculateKingMoves(long board, long enemyPieces, int moveCount, char[] moves)
    {
        long possibleMoves = king.getKingAttack(board) & ~getFriendlyPieces();
        while(possibleMoves != 0)
        {
            long move = 1L << Long.numberOfTrailingZeros(possibleMoves);
            possibleMoves &= ~move;

            if((move & enemyPieces) != 0)
            {
                moves[moveCount++] = MoveGeneration.encodeMove(board, move, capture);
            }
            else
            {
                moves[moveCount++] = MoveGeneration.encodeMove(board, move, quiet);
            }
        }

        long shortCastle = 0;
        if (canShortCastle())
        {
            if(turn % 2 == 0)
            {
                shortCastle = 0x0000000000000002L;
            }
            else
            {
                shortCastle = 0x0200000000000000L;
            }

            moves[moveCount++] = MoveGeneration.encodeMove(board, shortCastle, kingCastle);
        }

        long longCastle = 0;
        if(canLongCastle())
        {
            if(turn % 2 == 0)
            {
                longCastle = 0x0000000000000020L;
            }
            else
            {
                longCastle = 0x2000000000000000L;
            }
            moves[moveCount++] = MoveGeneration.encodeMove(board, longCastle, queenCastle);
        }


        return moveCount;
    }


    public long getAllPossibleEnemyAttacks()
    {
        if(turn % 2 == 0)
        {
            return  pawn.getPawnAttacks(bitBoards[6], turn + 1)
                    | knight.getKnightAttacks(bitBoards[7])
                    | bishopMagic.getAllBishopAttacks(bitBoards[8], allPiecesBitboard & ~bitBoards[5])
                    | rookMagic.getAllRookAttacks(bitBoards[9], allPiecesBitboard  & ~bitBoards[5])
                    | queen.getAllQueenAttacks(bitBoards[10], allPiecesBitboard & ~bitBoards[5])
                    | king.getKingAttack(bitBoards[11]);
        }

        return pawn.getPawnAttacks(bitBoards[0], turn + 1)
                | knight.getKnightAttacks(bitBoards[1])
                | bishopMagic.getAllBishopAttacks(bitBoards[2], allPiecesBitboard & ~bitBoards[11])
                | rookMagic.getAllRookAttacks(bitBoards[3], allPiecesBitboard & ~bitBoards[11])
                | queen.getAllQueenAttacks(bitBoards[4], allPiecesBitboard & ~bitBoards[11])
                | king.getKingAttack(bitBoards[5]);
    }

    public boolean isInCheck()
    {
        if(turn % 2 == 0)
        {
            return ((bitBoards[5] & getAllPossibleEnemyAttacks()) != 0);
        }
        return ((bitBoards[11] & getAllPossibleEnemyAttacks()) != 0);
    }


    public boolean canShortCastle()
    {
        if((cannotCastleFlags & 0b1000) != 0 && turn % 2 == 0)
        {
            return false;
        }
        else if ((cannotCastleFlags & 0b0010) != 0 && turn % 2 != 0)
        {
            return false;
        }

        long shortCastlingSpaceMask = 0x0000000000000006L;
        long kingLocation = 0x0000000000000008L;
        long rookLocation = 0x0000000000000001L;
        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            shortCastlingSpaceMask = 0x0600000000000000L;
            kingLocation = 0x0800000000000000L;
            rookLocation = 0x0100000000000000L;

            king = 11;
            rook = 9;
        }

        if(isInCheck())
        {
            return false;
        }

        if((bitBoards[king] & kingLocation) != 0 && (bitBoards[rook] & rookLocation) != 0)
        {
            if((allPiecesBitboard & shortCastlingSpaceMask) == 0 && (shortCastlingSpaceMask & getAllPossibleEnemyAttacks()) == 0)
            {
                return true;
            }
        }

        return false;
    }

    public void shortCastle()
    {
        long rookOldPosition = 0x0000000000000001L;
        long rookNewPosition = 0x0000000000000004L;

        long kingNewPosition = 0x0000000000000002L;

        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            rookOldPosition = 0x0100000000000000L;
            rookNewPosition =  0x0400000000000000L;

            kingNewPosition = 0x0200000000000000L;

            king = 11;
            rook = 9;
        }

        bitBoards[rook] = (bitBoards[rook]  & ~rookOldPosition) | rookNewPosition;
        bitBoards[king] = kingNewPosition;
    }

    public void undoShortCastle()
    {
        long rookOldPosition = 0x0000000000000004L;
        long rookNewPosition = 0x0000000000000001L;


        long kingNewPosition = 0x0000000000000008L;

        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            rookOldPosition = 0x0400000000000000L;
            rookNewPosition =  0x0100000000000000L;

            kingNewPosition = 0x0800000000000000L;

            king = 11;
            rook = 9;
        }


        bitBoards[rook] = (bitBoards[rook]  & ~rookOldPosition) | rookNewPosition;
        //bitBoards[king] = kingNewPosition;
    }


    public boolean canLongCastle()
    {
        if((cannotCastleFlags & 0b0100) != 0 && turn % 2 == 0)
        {
            return false;
        }
        else if ((cannotCastleFlags & 0b0001) != 0 && turn % 2 != 0)
        {
            return false;
        }

        long longCastlingSpaceMask = 0x0000000000000070L;
        long kingMoveMask = 0x0000000000000030L;
        long kingLocation = 0x0000000000000008L;
        long rookLocation = 0x0000000000000080L;
        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            longCastlingSpaceMask = 0x7000000000000000L;
            kingMoveMask = 0x3000000000000000L;
            kingLocation = 0x0800000000000000L;
            rookLocation = 0x8000000000000000L;

            king = 11;
            rook = 9;
        }

        if(isInCheck())
        {
            return false;
        }

        if((bitBoards[king] & kingLocation) != 0 && (bitBoards[rook] & rookLocation) != 0)
        {
            if((allPiecesBitboard & longCastlingSpaceMask) == 0 && (getAllPossibleEnemyAttacks() & kingMoveMask) == 0)
            {
                return true;
            }
        }

        return false;
    }

    public void longCastle()
    {
        long rookOldPosition = 0x0000000000000080L;
        long rookNewPosition = 0x0000000000000010L;

        long kingOldPosition = 0x0000000000000008L;
        long kingNewPosition = 0x0000000000000020L;


        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            rookOldPosition = 0x8000000000000000L;
            rookNewPosition = 0x1000000000000000L;

            kingNewPosition = 0x2000000000000000L;

            king = 11;
            rook = 9;
        }

        if((bitBoards[rook] & rookOldPosition) == 0)
        {
            return;
        }

        bitBoards[rook] = (bitBoards[rook] & ~rookOldPosition) | rookNewPosition;
        bitBoards[king] = kingNewPosition;
    }

    public void undoLongCastle()
    {
        long rookOldPosition = 0x0000000000000010L;
        long rookNewPosition = 0x0000000000000080L;

        long kingOldPosition = 0x0000000000000020L;
        long kingNewPosition = 0x0000000000000008L;


        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            rookOldPosition = 0x1000000000000000L;
            rookNewPosition = 0x8000000000000000L;

            kingNewPosition = 0x8000000000000000L;

            king = 11;
            rook = 9;
        }

        if((bitBoards[rook] & rookOldPosition) == 0)
        {
            return;
        }

        bitBoards[rook] = (bitBoards[rook] & ~rookOldPosition) | rookNewPosition;
        //bitBoards[king] = kingNewPosition;
    }


    public void enPassant(long pieceToMoveBitBoard, long moveBitboard)
    {
        if(turn % 2 == 0)
        {
            bitBoards[0] &= ~pieceToMoveBitBoard;
            bitBoards[0] |= moveBitboard;
            bitBoards[6] &= ~doublePawnPushBitboard;
        }
        else
        {
            bitBoards[6] &= ~pieceToMoveBitBoard;
            bitBoards[6] |= moveBitboard;
            bitBoards[0] &= ~doublePawnPushBitboard;
        }
    }

    public void undoEnPassant(long pieceToMoveBitBoard, long moveBitboard)
    {
        if(turn % 2 == 0)
        {
            bitBoards[0] &= ~moveBitboard;
            bitBoards[0] |= pieceToMoveBitBoard;

            bitBoards[6] |= (moveBitboard >> 8);
        }
        else
        {
            bitBoards[6] &= ~moveBitboard;
            bitBoards[6] |= pieceToMoveBitBoard;

            bitBoards[0] |= (moveBitboard << 8);
        }
    }

    public void promote(byte promotion)
    {
        int promotionPieceIndex = -1;
        if(promotion == 0b1000 || promotion == 0b1100)
        {
            promotionPieceIndex = 1;
        }
        else if(promotion == 0b1001 || promotion == 0b1101)
        {
            promotionPieceIndex = 2;
        }
        else if(promotion == 0b1010 || promotion == 0b1110)
        {
            promotionPieceIndex = 3;
        }
        else if(promotion == 0b1011 || promotion == 0b1111)
        {
            promotionPieceIndex = 4;
        }

        long whitePromotionSquaresMask = 0xFF00000000000000L;
        long blackPromotionSquaresMask = 0x00000000000000FFL;

        if(turn % 2 == 0)
        {
            long pawnToPromote = bitBoards[0] & whitePromotionSquaresMask;
            bitBoards[0] = bitBoards[0] ^ pawnToPromote;
            bitBoards[promotionPieceIndex] = bitBoards[promotionPieceIndex] | pawnToPromote;
        }
        else
        {
            promotionPieceIndex += 6;
            long pawnToPromote = bitBoards[6] & blackPromotionSquaresMask;
            bitBoards[6] = bitBoards[6] ^ pawnToPromote;
            bitBoards[promotionPieceIndex] = bitBoards[promotionPieceIndex] | pawnToPromote;
        }
    }

    public void undoPromote(byte promotion, char move)
    {
        int promotionPieceIndex = -1;
        if(promotion == 0b1000 || promotion == 0b1100)
        {
            promotionPieceIndex = 1;
        }
        else if(promotion == 0b1001 || promotion == 0b1101)
        {
            promotionPieceIndex = 2;
        }
        else if(promotion == 0b1010 || promotion == 0b1110)
        {
            promotionPieceIndex = 3;
        }
        else if(promotion == 0b1011 || promotion == 0b1111)
        {
            promotionPieceIndex = 4;
        }

        int startSquare = MoveGeneration.getStartSquare(move);
        int endSquare = MoveGeneration.getEndSquare(move);

        long pieceToMoveBitBoard = 1L << (63 - startSquare);
        long moveBitBoard = 1L << (63 - endSquare);

        if(turn % 2 == 0)
        {
            bitBoards[0] |= pieceToMoveBitBoard;
            bitBoards[promotionPieceIndex] &= ~moveBitBoard;
        }
        else
        {
            promotionPieceIndex += 6;
            bitBoards[6] |= pieceToMoveBitBoard;
            bitBoards[promotionPieceIndex] &= ~moveBitBoard;
        }
    }

}
