package Main;


import Main.Pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
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

    public boolean whiteCannotShortCastle = false;
    public boolean whiteCannotLongCastle = false;

    public boolean blackCannotShortCastle = false;
    public boolean blackCannotLongCastle = false;

    public long whitePiecesBitboard;
    public long blackPiecesBitboard;
    public long allPiecesBitboard;


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
//                    {"r", " ", " ", " ", "k", " ", " ", "r"},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", " ", " ", " ", " "},
//                    {" ", " ", " ", " ", "K", " ", " ", " "},
//            };


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

    public int determineKingSquare()
    {
        if(turn % 2 == 0)
        {
            return Long.numberOfLeadingZeros(bitBoards[5]);
        }
        return Long.numberOfLeadingZeros(bitBoards[11]);
    }


    public boolean isFriendly(long selectedPieceBitBoard)
    {
        return (getFriendlyPieces() & selectedPieceBitBoard) != 0;
    }

    public long getAllPossibleMoves()
    {
        if(turn % 2 == 0)
        {
            return getAllPossibleWhiteMoves();
        }
        return getAllPossibleBlackMoves();
    }

    public long getAllPossibleWhiteMoves()
    {
        long allMoves = 0L;
        for(int i = 0 ; i < 6; i++)
        {
            if(bitBoards[i] == 0)
            {
                continue;
            }
            allMoves |= calculatePossibleMoves(bitBoards[i]);
        }

        return allMoves;
    }

    public long getAllPossibleBlackMoves()
    {
        long allMoves = 0L;
        for(int i = 6 ; i < 12; i++)
        {
            if(bitBoards[i] == 0)
            {
                continue;
            }
            allMoves |= calculatePossibleMoves(bitBoards[i]);
        }

        return allMoves;
    }

    public long getPossibleMoves(long selectedPieceBitBoard)
    {
        if(!isFriendly(selectedPieceBitBoard))
        {
            return 0L;
        }

        if(isInCheck())
        {
            if((selectedPieceBitBoard & (bitBoards[5] | bitBoards[11])) != 0)
            {
                return calculatePossibleMoves(selectedPieceBitBoard);
            }

            return calculatePossibleMoves(selectedPieceBitBoard) & getMovesToStopCheck();
        }


        return calculatePossibleMoves(selectedPieceBitBoard);
    }

    public long getPseudoLegalMoves(long selectedPieceBitBoard)
    {
        if(!isFriendly(selectedPieceBitBoard))
        {
            return 0L;
        }
        return calculatePseudoLegalMoves(selectedPieceBitBoard);
    }

    public boolean isMoveLegal(long selectedPieceBitBoard, long moveBitBoard)
    {
        long[] bitboardsCopy = Bitboard.copy(bitBoards);
        boolean whiteCannotShortCastleCopy = whiteCannotShortCastle;
        boolean whiteCannotLongCastleCopy = whiteCannotLongCastle;
        boolean blackCannotShortCastleCopy = blackCannotShortCastle;
        boolean blackCannotLongCastleCopy = blackCannotLongCastle;

        makeMove(selectedPieceBitBoard, moveBitBoard);

        turn -= 1;
        boolean isCheck = isInCheck();

        bitBoards = bitboardsCopy;
        whiteCannotShortCastle = whiteCannotShortCastleCopy;
        whiteCannotLongCastle = whiteCannotLongCastleCopy;
        blackCannotShortCastle = blackCannotShortCastleCopy;

        blackCannotLongCastle = blackCannotLongCastleCopy;
        whitePiecesBitboard = Bitboard.getWhitePieces(bitBoards);
        blackPiecesBitboard = Bitboard.getBlackPieces(bitBoards);
        allPiecesBitboard = Bitboard.getAllPieces(bitBoards);

        return !isCheck;

    }

    public long calculatePseudoLegalMoves(long selectedPieceBitBoard)
    {
        long possibleMoves = 0L;

        if((selectedPieceBitBoard & (bitBoards[0] | bitBoards[6])) != 0)
        {
            possibleMoves = pawn.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, whitePiecesBitboard, blackPiecesBitboard, turn);
        }
        else if ((selectedPieceBitBoard & (bitBoards[1] | bitBoards[7])) != 0)
        {
            possibleMoves = knight.possibleMoves(selectedPieceBitBoard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[2] | bitBoards[8])) != 0)
        {
            possibleMoves = bishopMagic.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[3] | bitBoards[9])) != 0)
        {
            possibleMoves = rookMagic.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[4] | bitBoards[10])) != 0)
        {
            possibleMoves = queen.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[5] | bitBoards[11])) != 0)
        {
            possibleMoves = possibleKingMoves(selectedPieceBitBoard);
        }
        else
        {
            throw new RuntimeException("Invalid move starting piece");
        }

        return possibleMoves;
    }

    public long calculatePossibleMoves(long selectedPieceBitBoard)
    {
        long possibleMoves = 0L;

        if((selectedPieceBitBoard & (bitBoards[0] | bitBoards[6])) != 0)
        {
            possibleMoves = pawn.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, whitePiecesBitboard, blackPiecesBitboard, turn);
        }
        else if ((selectedPieceBitBoard & (bitBoards[1] | bitBoards[7])) != 0)
        {
            possibleMoves = knight.possibleMoves(selectedPieceBitBoard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[2] | bitBoards[8])) != 0)
        {
            possibleMoves = bishopMagic.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[3] | bitBoards[9])) != 0)
        {
            possibleMoves = rookMagic.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[4] | bitBoards[10])) != 0)
        {
            possibleMoves = queen.possibleMoves(selectedPieceBitBoard, allPiecesBitboard, getFriendlyPieces());
        }
        else if ((selectedPieceBitBoard & (bitBoards[5] | bitBoards[11])) != 0)
        {
            possibleMoves = possibleKingMoves(selectedPieceBitBoard) & ~getAllPossibleEnemyAttacks();
        }
        else
        {
            throw new RuntimeException("Invalid move starting piece");
        }


        if ((getAllPinnedPiecesValidMoves() & selectedPieceBitBoard) != 0) {
            return possibleMoves & getAllPinnedPiecesValidMoves();
        }

        return possibleMoves;
    }


    public char convertTo16BitMove(long selectedPieceBitBoard, long moveBitBoard)
    {
        char startingSquare = (char) Long.numberOfLeadingZeros(selectedPieceBitBoard);
        char endingSquare = (char) Long.numberOfLeadingZeros(moveBitBoard);
        char promotion = 0;
        char capture = 0;
        char special1 = 0;
        char special0 = 0;


        if(isMoveDoublePawnPush(selectedPieceBitBoard, moveBitBoard))
        {
            special0 = 1;
        }

        if(isMoveCastle(selectedPieceBitBoard, moveBitBoard, true))
        {
            special1 = 1;
        }

        if(isMoveCastle(selectedPieceBitBoard, moveBitBoard, false))
        {
            special1 = 1;
            special0 = 1;
        }

        if(determinePieceIndexFromBitBoard(moveBitBoard) != -1)
        {
            capture = 1;
        }

        if (isMovePromotion(selectedPieceBitBoard, moveBitBoard))
        {
            promotion = 1;
            special0 = 1;
            special1 = 1;
        }

        return (char) (startingSquare << 10 | endingSquare << 4 | promotion << 3 | capture << 2 | special0 << 1 | special1);
    }

    public boolean isMoveDoublePawnPush(long selectedPieceBitBoard, long moveBitBoard)
    {
        if((selectedPieceBitBoard & (bitBoards[0] | bitBoards[6])) == 0)
        {
            return false;
        }

        return (selectedPieceBitBoard << 16 == moveBitBoard) || (selectedPieceBitBoard >> 16 == moveBitBoard);
    }

    public boolean isMoveCastle(long selectedPieceBitBoard, long moveBitBoard, boolean kingside)
    {
        //Queen side castling
        long whiteKingStartingPosition = 0x0000000000000008L;
        long whiteKingEndingPosition = 0x0000000000000020L;

        long blackKingStartingPosition = 0x0800000000000000L;
        long blackKingEndingPosition = 0x2000000000000000L;

        //King side castling
        if(kingside)
        {
            whiteKingEndingPosition = 0x0000000000000002L;
            blackKingEndingPosition = 0x0200000000000000L;
        }

        if((selectedPieceBitBoard & (bitBoards[5] | bitBoards[11])) == 0)
        {
            return false;
        }

        return ((selectedPieceBitBoard & whiteKingStartingPosition) != 0 &&  (moveBitBoard & whiteKingEndingPosition) != 0) ||
                ((selectedPieceBitBoard & blackKingStartingPosition) != 0 &&  (moveBitBoard & blackKingEndingPosition) != 0);
    }

    public boolean isMovePromotion(long selectedPieceBitBoard, long moveBitBoard)
    {
        if((selectedPieceBitBoard & (bitBoards[0] | bitBoards[6])) == 0)
        {
            return false;
        }

        long whitePromotionSquaresMask = 0xFF00000000000000L;
        long blackPromotionSquaresMask = 0x00000000000000FFL;

        return (moveBitBoard & (whitePromotionSquaresMask | blackPromotionSquaresMask)) != 0;
    }





    public char[] getAllMoves()
    {
        char[] moves = new char[218];
        int currentIndex = 0;

        long friendlyPieces = getFriendlyPieces();
        for(int i = 0; i < 64; i++)
        {
            long currentSquareBitboard = 1L << 63 - i;

            if((currentSquareBitboard & friendlyPieces) == 0)
            {
                continue;
            }

            long possibleMoves = getPseudoLegalMoves(currentSquareBitboard);
            while (possibleMoves != 0)
            {
                long currentMove = 1L << 63 - Long.numberOfLeadingZeros(possibleMoves);
                possibleMoves &= ~currentMove;

                moves[currentIndex] = convertTo16BitMove(currentSquareBitboard, currentMove);
                currentIndex += 1;
            }
        }

        char[] allMoves = new char[currentIndex];
        System.arraycopy(moves, 0, allMoves, 0, currentIndex);
        return allMoves;
    }

//    public void makeRandomMove()
//    {
//        ArrayList<Long[]> allMoves = getAllMoves();
//        int index = (int)(Math.random() * allMoves.size());
//
//        Long[] randomMove = allMoves.get(index);
//        makeMove(randomMove[0], randomMove[1]);
//    }


    public boolean isValidMove(long selectedPieceBitBoard, long moveBitBoard)
    {
        long possibleMovesBitBoard = getPossibleMoves(selectedPieceBitBoard);

        return (selectedPieceBitBoard & getFriendlyPieces()) != 0L && (moveBitBoard & possibleMovesBitBoard) != 0L;
    }

    public void makeMove(long pieceToMoveBitBoard, long moveBitBoard)
    {
        //Checks if move made affects rooks or king
        updateCastlingRights(pieceToMoveBitBoard);

        if(isMoveShortCastle(pieceToMoveBitBoard, moveBitBoard))
        {
            shortCastle();
            turn += 1;
            return;
        }
        else if(isMoveLongCastle(pieceToMoveBitBoard, moveBitBoard))
        {
            longCastle();
            turn += 1;
            return;
        }

        int pieceIndex = determinePieceIndexFromBitBoard(pieceToMoveBitBoard);
        int capturedPieceIndex = determinePieceIndexFromBitBoard(moveBitBoard);

        bitBoards[pieceIndex] = (bitBoards[pieceIndex] & ~pieceToMoveBitBoard) | moveBitBoard;

        if(capturedPieceIndex != -1)
        {
            updateEnemyPiecesBitBoard(moveBitBoard);
        }


        if(isPawnOnPromotionSquare())
        {
            promote();
        }

        whitePiecesBitboard = Bitboard.getWhitePieces(bitBoards);
        blackPiecesBitboard = Bitboard.getBlackPieces(bitBoards);
        allPiecesBitboard = Bitboard.getAllPieces(bitBoards);
        turn += 1;
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

    public boolean hasKingMovedWithoutCastling(long pieceBoard, long kingStartingPosition)
    {
        int king = 5;
        if(turn % 2 != 0)
        {
            king = 11;
        }

        return determinePieceIndexFromBitBoard(pieceBoard) == (king) && (pieceBoard & kingStartingPosition) != 0;
    }

    public void updateCastlingRights(long pieceToMoveBitBoard)
    {
        if(turn % 2 == 0)
        {
            if(!whiteCannotShortCastle && !whiteCannotLongCastle)
            {
                whiteCannotShortCastle = hasKingMovedWithoutCastling(pieceToMoveBitBoard, 0x0000000000000008L);
                whiteCannotLongCastle = hasKingMovedWithoutCastling(pieceToMoveBitBoard, 0x0000000000000008L);
            }

            else if(!whiteCannotShortCastle)
            {
                whiteCannotShortCastle = hasRookMovedWithoutCastling(pieceToMoveBitBoard, 0x0000000000000001L);
            }

            else if(!whiteCannotLongCastle)
            {
                whiteCannotLongCastle = hasRookMovedWithoutCastling(pieceToMoveBitBoard, 0x0000000000000080L);
            }
        }
        else
        {
            if(!blackCannotShortCastle && !blackCannotLongCastle)
            {
                blackCannotShortCastle = hasKingMovedWithoutCastling(pieceToMoveBitBoard, 0x0800000000000000L);
                blackCannotLongCastle = hasKingMovedWithoutCastling(pieceToMoveBitBoard, 0x0800000000000000L);
            }

            if(!blackCannotShortCastle)
            {
                blackCannotShortCastle = hasRookMovedWithoutCastling(pieceToMoveBitBoard, 0x0100000000000000L);
            }

            if (!blackCannotLongCastle)
            {
                blackCannotLongCastle = hasRookMovedWithoutCastling(pieceToMoveBitBoard, 0x8000000000000000L);
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



    public long possibleKingMoves(long bitBoard)
    {
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
        }

        return (king.getKingAttack(bitBoard) | shortCastle | longCastle) & ~getFriendlyPieces();
    }


    public long getAllPossibleEnemyAttacks()
    {
        if(turn % 2 == 0)
        {
            return  pawn.getPawnAttacks(bitBoards[6], turn + 1)
                    | knight.getKnightAttack(bitBoards[7])
                    | bishopMagic.getAllBishopAttacks(bitBoards[8], allPiecesBitboard & ~bitBoards[5])
                    | rookMagic.getAllRookAttacks(bitBoards[9], allPiecesBitboard  & ~bitBoards[5])
                    | queen.getAllQueenAttacks(bitBoards[10], allPiecesBitboard & ~bitBoards[5])
                    | king.getKingAttack(bitBoards[11]);
        }

        return pawn.getPawnAttacks(bitBoards[0], turn + 1)
                | knight.getKnightAttack(bitBoards[1])
                | bishopMagic.getAllBishopAttacks(bitBoards[2], allPiecesBitboard & ~bitBoards[11])
                | rookMagic.getAllRookAttacks(bitBoards[3], allPiecesBitboard & ~bitBoards[11])
                | queen.getAllQueenAttacks(bitBoards[4], allPiecesBitboard & ~bitBoards[11])
                | king.getKingAttack(bitBoards[5]);
    }

    public boolean isInCheck()
    {
        if(turn % 2 == 0)
        {
            return (bitBoards[5] & getAllPossibleEnemyAttacks()) != 0;
        }
        return (bitBoards[11] & getAllPossibleEnemyAttacks()) != 0;
    }

    public boolean isInCheckmate()
    {
        return getAllPossibleMoves() == 0;
    }

    public boolean isWhiteInCheckmate()
    {
        return getAllPossibleWhiteMoves() == 0;
    }

    public boolean isBlackInCheckmate()
    {
        return getAllPossibleBlackMoves() == 0;
    }



    public long getMovesToStopCheck()
    {
        int kingSquare = determineKingSquare();
        int[] axialPieces =  new int[]{9, 10};
        int[] diagonalPieces = new int[]{8, 10};
        int knightPiece = 7;
        int pawnPiece = 6;

        int[][] axialDirections = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        int[][] diagonalDirections = {{-1, -1}, {1, 1}, {1, -1}, {-1, 1}};
        int[][] knightDirections = {{1, 2}, {-1, 2}, {-1, -2}, {1, -2}, {2, 1}, {-2, -1}, {-2, 1}, {2, -1}};
        int[][] pawnDirections = {{-1, -1}, {-1, 1}};

        if(turn % 2 != 0)
        {
            axialPieces =  new int[]{3, 4};
            diagonalPieces = new int[]{2, 4};
            knightPiece = 1;
            pawnPiece = 0;
            
            pawnDirections = new int[][]{{1, -1}, {1, 1}};
        }

        long moves = 0L;


        for(int[] direction : axialDirections)
        {
            moves |= getMovesToStopSlidingCheck(kingSquare, direction, axialPieces);
        }

        for(int[] direction : diagonalDirections)
        {
            moves |= getMovesToStopSlidingCheck(kingSquare, direction, diagonalPieces);
        }

        for(int[] direction : knightDirections)
        {
            moves |= getMovesToStopNonSlidingCheck(kingSquare, direction, knightPiece);
        }
        
        for(int[] direction : pawnDirections)
        {
            moves |= getMovesToStopNonSlidingCheck(kingSquare, direction, pawnPiece);
        }

        //If more than 1 check
        if(Long.bitCount(moves & getEnemyPieces()) > 1)
        {
            return 0L;
        }

        return moves;
    }

    public long getMovesToStopSlidingCheck(int kingSquare, int[] direction, int[] slidingPiece)
    {
        int row = kingSquare / 8;
        int col = kingSquare % 8;
        int rowDirection = direction[0];
        int colDirection = direction[1];

        row += rowDirection;
        col += colDirection;

        long moves = 0L;
        while(0 <= row && row <= 7 && 0 <= col && col <= 7)
        {
            long currentSquare = 1L << (63 - (8 * row + col));

            if ((currentSquare & getFriendlyPieces()) != 0)
            {
                return 0;
            }

            moves |= currentSquare;

            for(int piece : slidingPiece)
            {
                if ((currentSquare & bitBoards[piece]) != 0 )
                {
                    return moves;
                }
            }

            row += rowDirection;
            col += colDirection;
        }
        return 0;
    }

    
    public long getMovesToStopNonSlidingCheck(int kingSquare, int[] direction, int nonSlidingPiece)
    {
        int row = kingSquare / 8;
        int col = kingSquare % 8;
        int rowDirection = direction[0];
        int colDirection = direction[1];

        row += rowDirection;
        col += colDirection;

        if(row < 0 || row > 7 || col < 0 || col > 7)
        {
            return 0L;
        }

        long currentSquare = 1L << (63 - (8 * row + col));
        if((currentSquare & bitBoards[nonSlidingPiece]) == 0)
        {
            return 0L;
        }

        return currentSquare;
    }
    
 

    public long getAllPinnedPiecesValidMoves()
    {
        int kingSquare = determineKingSquare();
        int[] axialPieces =  new int[]{9, 10};
        int[] diagonalPieces = new int[]{8, 10};

        if(turn % 2 != 0)
        {
            axialPieces = new int[]{3, 4};
            diagonalPieces = new int[]{2, 4};
        }

        long pinnedPieces = 0L;

        int[][] axialDirections = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        int[][] diagonalDirections = {{-1, -1}, {1, 1}, {1, -1}, {-1, 1}};

        for(int[] direction : axialDirections)
        {
            pinnedPieces |= getPinnedPieceValidMove(kingSquare, direction, axialPieces);
        }

        for(int[] direction : diagonalDirections)
        {
            pinnedPieces |= getPinnedPieceValidMove(kingSquare, direction, diagonalPieces);
        }

        return pinnedPieces;
    }

    public long getPinnedPieceValidMove(int kingSquare, int[] direction, int[] checkingPieces)
    {
        long movePath = getPotentialAttackerPath(kingSquare, direction, checkingPieces);
        int numberOfFriendlyPieces = Long.bitCount(movePath & getFriendlyPieces());

        if(numberOfFriendlyPieces == 1)
        {
            return movePath;
        }

        return 0L;
    }

    public long getPotentialAttackerPath(int kingSquare, int[] direction, int[] checkingPieces)
    {
        long moves = 0L;

        int rowDirection = direction[0];
        int colDirection = direction[1];

        int row = kingSquare / 8 + rowDirection;
        int col = kingSquare % 8 + colDirection;

        long checkingPiecesBitboard = bitBoards[checkingPieces[0]] | bitBoards[checkingPieces[1]];
        long nonCheckingPiecesBitboard = getEnemyPieces() & ~checkingPiecesBitboard;

        while(0 <= row && row <= 7 && 0 <= col && col <= 7)
        {
            long currentSquare = 1L << (63 - (8 * row + col));

            if((currentSquare & nonCheckingPiecesBitboard) != 0)
            {
                return 0L;
            }

            moves |= currentSquare;

            if((currentSquare & checkingPiecesBitboard) != 0)
            {
                return moves;
            }

            row += rowDirection;
            col += colDirection;
        }

        return 0L;

    }


    public boolean canShortCastle()
    {
        if(whiteCannotShortCastle && turn % 2 == 0)
        {
            return false;
        }
        else if (blackCannotShortCastle && turn % 2 != 0)
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
            if((allPiecesBitboard & shortCastlingSpaceMask) == 0)
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

        long kingOldPosition = 0x0000000000000008L;
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

        if((bitBoards[rook] & rookOldPosition) == 0)
        {
            return;
        }


        bitBoards[rook] = (bitBoards[rook]  & ~rookOldPosition) | rookNewPosition;
        bitBoards[king] = kingNewPosition;
    }

    public boolean isMoveShortCastle(long startingPosition, long endingPosition)
    {
        long kingStartingPosition = 0x0000000000000008L;
        long shortCastle = 0x0000000000000002L;

        if(turn % 2 != 0)
        {
            kingStartingPosition = 0x0800000000000000L;
            shortCastle = 0x0200000000000000L;
        }

        return (kingStartingPosition & startingPosition) != 0 && (shortCastle & endingPosition) != 0;
    }

    public boolean canLongCastle()
    {
        if(whiteCannotLongCastle && turn % 2 == 0)
        {
            return false;
        }
        else if (blackCannotLongCastle && turn % 2 != 0)
        {
            return false;
        }

        long longCastlingSpaceMask = 0x0000000000000070L;
        long kingLocation = 0x0000000000000008L;
        long rookLocation = 0x0000000000000080L;
        int king = 5;
        int rook = 3;

        if(turn % 2 != 0)
        {
            longCastlingSpaceMask = 0x7000000000000000L;
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
            if((allPiecesBitboard & longCastlingSpaceMask) == 0)
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

    public boolean isMoveLongCastle(long startingPosition, long endingPosition)
    {
        long kingStartingPosition = 0x0000000000000008L;
        long longCastle = 0x0000000000000020L;

        if(turn % 2 != 0)
        {
            kingStartingPosition = 0x0800000000000000L;
            longCastle = 0x2000000000000000L;
        }

        return (kingStartingPosition & startingPosition) != 0 && (longCastle & endingPosition) != 0;
    }


    public boolean isPawnOnPromotionSquare()
    {
        long whitePromotionSquaresMask = 0xFF00000000000000L;
        long blackPromotionSquaresMask = 0x00000000000000FFL;

        if(turn % 2 == 0)
        {
            return (whitePromotionSquaresMask & bitBoards[0]) != 0;
        }

        return (blackPromotionSquaresMask & bitBoards[6]) != 0;
    }

    public void promote()
    {
        long whitePromotionSquaresMask = 0xFF00000000000000L;
        long blackPromotionSquaresMask = 0x00000000000000FFL;

        if(turn % 2 == 0)
        {
            long pawnToPromote = bitBoards[0] & whitePromotionSquaresMask;
            bitBoards[0] = bitBoards[0] ^ pawnToPromote;
            bitBoards[4] = bitBoards[4] | pawnToPromote;
        }
        else
        {
            long pawnToPromote = bitBoards[6] & blackPromotionSquaresMask;
            bitBoards[6] = bitBoards[6] ^ pawnToPromote;
            bitBoards[10] = bitBoards[10] | pawnToPromote;
        }
    }

}
