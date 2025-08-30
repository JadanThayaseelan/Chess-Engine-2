package Main;

import Main.Pieces.*;

import java.util.HashMap;

public class Game
{
    public HashMap<String, Long> bitBoards = new HashMap<>();


    public Pawn pawn = new Pawn();
    public Knight knight = new Knight();
    public RookMagic rookMagic = new RookMagic();
    public BishopMagic bishopMagic = new BishopMagic();
    public Queen queen = new Queen();
    public King king = new King();

    public int turn = 0;

    private boolean whiteCannotShortCastle = false;
    private boolean whiteCannotLongCastle = false;

    private boolean blackCannotShortCastle = false;
    private boolean blackCannotLongCastle = false;


    public Game(String[][] stringBoard)
    {

        if(stringBoard == null) {
            //stringBoard = new String[][]{
            //        {"r", "n", "b", "q", "k", "b", "n", "r"},
            //        {"p", "p", "p", "p", "p", "p", "p", "p"},
            //        {" ", " ", " ", " ", " ", " ", " ", " "},
            //        {" ", " ", " ", " ", " ", " ", " ", " "},
            //        {" ", " ", " ", " ", " ", " ", " ", " "},
            //        {" ", " ", " ", " ", " ", " ", " ", " "},
            //        {"P", "P", "P", "P", "P", "P", "P", "P"},
            //        {"R", "N", "B", "Q", "K", "B", "N", "R"}
            //};
           stringBoard = new String[][]{
                   {" ", " ", " ", " ", " ", " ", " ", " "},
                   {" ", " ", " ", " ", " ", " ", " ", " "},
                   {" ", " ", " ", " ", " ", " ", " ", " "},
                   {" ", " ", "k", " ", "R", " ", " ", " "},
                   {" ", " ", " ", " ", " ", " ", " ", " "},
                   {" ", " ", " ", " ", " ", " ", " ", " "},
                   {" ", " ", "Q", " ", " ", " ", " ", " "},
                   {" ", " ", " ", " ", " ", " ", " ", " "}};
        }


        bitBoards = convertStringBoardToBitBoards(stringBoard);
    }

    public HashMap<String, Long> convertStringBoardToBitBoards(String[][] stringBoard)
    {
        HashMap<String, Long> bitBoards = new HashMap<>();

        String allPieces = "RNBKQPrnbkqp";
        for (int i = 0; i < allPieces.length(); i++)
        {
            String currentPiece = String.valueOf(allPieces.charAt(i));
            bitBoards.put(currentPiece, generateBitBoard(stringBoard, currentPiece));
        }

        return bitBoards;
    }

    public String[][] convertBitBoardsToStringBoard()
    {
        String[][] stringBoard = new String[8][8];
        for(String piece : bitBoards.keySet())
        {
            String stringBitBoard = String.format("%64s", Long.toBinaryString(bitBoards.get(piece))).replace(' ', '0');
            for(int i = 0 ; i < stringBitBoard.length(); i++)
            {
                int row = i / 8;
                int col = i - (row*8);

                if(stringBitBoard.charAt(i) == '1')
                {
                    stringBoard[row][col] = piece;
                }
            }
        }

        return stringBoard;
    }



    public long getWhitePiecesBitBoard()
    {
        return getCombinedBitBoards("RNBKQP");
    }

    public long getBlackPiecesBitBoard()
    {
        return getCombinedBitBoards("rnbkqp");
    }

    public long getAllPiecesBitBoard()
    {
        return getCombinedBitBoards("RNBKQPrnbkqp");
    }

    public long getFriendlyPieces()
    {
        if(turn % 2 == 0)
        {
            return getWhitePiecesBitBoard();
        }
        return getBlackPiecesBitBoard();
    }

    public long getEnemyPieces()
    {
        if(turn % 2 == 0)
        {
            return getBlackPiecesBitBoard();
        }

        return getWhitePiecesBitBoard();
    }

    public int determineKingSquare()
    {
        if(turn % 2 == 0)
        {
            return Long.numberOfLeadingZeros(bitBoards.get("K"));
        }
        return Long.numberOfLeadingZeros(bitBoards.get("k"));
    }

    public void printStringBoard()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                System.out.print(convertBitBoardsToStringBoard()[i][j]);
            }
            System.out.println();
        }
    }

    public long getCombinedBitBoards(String pieces)
    {
        long combinedBitBoard = 0L;
        for(int i = 0; i < pieces.length(); i++)
        {
            String currentPieceLetter = String.valueOf(pieces.charAt(i));
            combinedBitBoard = combinedBitBoard | bitBoards.get(currentPieceLetter);
        }

        return combinedBitBoard;
    }

    public Long generateBitBoard(String[][] board, String piece)
    {
        long bitBoard = 0L;

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if (board[i][j] != null && board[i][j].equals(piece))
                {
                    int squareNumber = i * 8 + j;
                    bitBoard = bitBoard | (1L << (63 - squareNumber));
                }
            }
        }

        return bitBoard;
    }



    public boolean isFriendly(long selectedPieceBitBoard)
    {
        return (getFriendlyPieces() & selectedPieceBitBoard) != 0;
    }

    public long getPossibleMoves(long selectedPieceBitBoard)
    {
        if(!isFriendly(selectedPieceBitBoard))
        {
            return 0L;
        }

        String piece = determinePieceFromBitBoard(selectedPieceBitBoard);

        if(isInCheck())
        {
            if(piece.equals("K") || piece.equals("k"))
            {
                return calculatePossibleMoves(piece, selectedPieceBitBoard);
            }

            return calculatePossibleMoves(piece, selectedPieceBitBoard) & getMovesToStopCheck();
        }

        if((getAllPinnedPiecesValidMoves() & selectedPieceBitBoard) != 0)
        {
            return calculatePossibleMoves(piece, selectedPieceBitBoard) & getAllPinnedPiecesValidMoves();
        }

        return calculatePossibleMoves(piece, selectedPieceBitBoard);
    }

    public long calculatePossibleMoves(String piece, long selectedPieceBitBoard)
    {
        if(piece == null)
        {
            return 0L;
        }
        return switch (piece) {
            case "P", "p" -> pawn.possibleMoves(selectedPieceBitBoard, getAllPiecesBitBoard(), getWhitePiecesBitBoard(), getBlackPiecesBitBoard(), turn);
            case "R", "r" -> rookMagic.possibleMoves(selectedPieceBitBoard, getAllPiecesBitBoard(), getFriendlyPieces());
            case "N", "n" -> knight.possibleMoves(selectedPieceBitBoard, getFriendlyPieces());
            case "B", "b" -> bishopMagic.possibleMoves(selectedPieceBitBoard, getAllPiecesBitBoard(), getFriendlyPieces());
            case "Q", "q" -> queen.possibleMoves(selectedPieceBitBoard, getAllPiecesBitBoard(), getFriendlyPieces());
            case "K", "k" -> possibleKingMoves(selectedPieceBitBoard);
            default -> 0;
        };
    }


    public boolean isValidMove(long selectedPieceBitBoard, long moveBitBoard)
    {
        long possibleMovesBitBoard = getPossibleMoves(selectedPieceBitBoard);

        if((selectedPieceBitBoard & getFriendlyPieces()) != 0L && (moveBitBoard & possibleMovesBitBoard) != 0L)
        {
            return true;
        }

        return false;
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

        String pieceToMove = determinePieceFromBitBoard(pieceToMoveBitBoard);
        long oldBoard = bitBoards.get(pieceToMove);

        bitBoards.remove(pieceToMove);
        bitBoards.put(pieceToMove, (oldBoard ^ pieceToMoveBitBoard) | moveBitBoard);

        updateEnemyPiecesBitBoard(moveBitBoard);

        turn += 1;
    }

    public boolean hasRookMovedWithoutCastling(long pieceBoard, long rookStartingPosition)
    {
        String rook = "R";
        if(turn % 2 != 0)
        {
            rook = "r";
        }

        return determinePieceFromBitBoard(pieceBoard).equals(rook) && (pieceBoard & rookStartingPosition) != 0;
    }

    public boolean hasKingMovedWithoutCastling(long pieceBoard, long kingStartingPosition)
    {
        String king = "K";
        if(turn % 2 != 0)
        {
            king = "k";
        }

        return determinePieceFromBitBoard(pieceBoard).equals(king) && (pieceBoard & kingStartingPosition) != 0;
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
                blackCannotLongCastle = hasRookMovedWithoutCastling(pieceToMoveBitBoard, 0x0800000000000000L);
            }
        }
    }

    public void updateEnemyPiecesBitBoard(long moveBitBoard)
    {
        String[] enemyPieces;
        if(turn % 2 == 0)
        {
            enemyPieces = new String[]{"r", "n", "b", "k", "q", "p"};
        }
        else
        {
            enemyPieces = new String[]{"R", "N", "B", "K", "Q", "P"};
        }

        for(String piece : enemyPieces)
        {
            long oldBoard = bitBoards.get(piece);
            bitBoards.remove(piece);
            bitBoards.put(piece, oldBoard & ~moveBitBoard);
        }
    }


    public String determinePieceFromBitBoard(long bitBoard)
    {
        for(String piece : bitBoards.keySet())
        {
            if((bitBoards.get(piece) & bitBoard) != 0L)
            {
                return piece;
            }
        }

        return null;
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

        return (king.getKingAttack(bitBoard) | shortCastle | longCastle) & ~getFriendlyPieces() & ~getAllPossibleEnemyAttacks();
    }


    public long getAllPossibleEnemyAttacks()
    {
        if(turn % 2 == 0)
        {
            return  pawn.getPawnAttacks(bitBoards.get("p"), turn + 1) | bishopMagic.getAllBishopAttacks(bitBoards.get("b"), getAllPiecesBitBoard() & ~bitBoards.get("K"))
                    | rookMagic.getAllRookAttacks(bitBoards.get("r"), getAllPiecesBitBoard()  & ~bitBoards.get("K")) | queen.getQueenAttacks(bitBoards.get("q"), getAllPiecesBitBoard() & ~bitBoards.get("K"))
                    | knight.getKnightAttack(bitBoards.get("n")) | king.getKingAttack(bitBoards.get("k"));
        }

        return pawn.getPawnAttacks(bitBoards.get("P"), turn + 1) | bishopMagic.getAllBishopAttacks(bitBoards.get("B"), getAllPiecesBitBoard() & ~bitBoards.get("k"))
                | rookMagic.getAllRookAttacks(bitBoards.get("R"), getAllPiecesBitBoard() & ~bitBoards.get("k")) | queen.getQueenAttacks(bitBoards.get("Q"), getAllPiecesBitBoard() & ~bitBoards.get("k"))
                | knight.getKnightAttack(bitBoards.get("N")) | king.getKingAttack(bitBoards.get("K"));
    }

    public boolean isInCheck()
    {
        if(turn % 2 == 0)
        {
            return (bitBoards.get("K") & getAllPossibleEnemyAttacks()) != 0;
        }
        return (bitBoards.get("k") & getAllPossibleEnemyAttacks()) != 0;
    }



    public long getMovesToStopCheck()
    {
        int kingSquare = determineKingSquare();
        String[] axialPieces =  new String[]{"r", "q"};
        String[] diagonalPieces = new String[]{"b", "q"};
        String knightPiece = "n";
        String pawnPiece = "p";

        int[][] axialDirections = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        int[][] diagonalDirections = {{-1, -1}, {1, 1}, {1, -1}, {-1, 1}};
        int[][] knightDirections = {{1, 2}, {-1, 2}, {-1, -2}, {1, -2}, {2, 1}, {-2, -1}, {-2, 1}, {2, -1}};
        int[][] pawnDirections = {{-1, -1}, {-1, 1}};

        if(turn % 2 != 0)
        {
            axialPieces =  new String[]{"R", "Q"};
            diagonalPieces = new String[]{"B", "Q"};
            knightPiece = "N";
            pawnPiece = "P";
            
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

    public long getMovesToStopSlidingCheck(int kingSquare, int[] direction, String[] slidingPiece)
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

            for(String piece : slidingPiece)
            {
                if ((currentSquare & bitBoards.get(piece)) != 0 )
                {
                    return moves;
                }
            }

            row += rowDirection;
            col += colDirection;
        }
        return 0;
    }

    
    public long getMovesToStopNonSlidingCheck(int kingSquare, int[] direction, String nonSlidingPiece)
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
        if((currentSquare & bitBoards.get(nonSlidingPiece)) == 0)
        {
            return 0L;
        }

        return currentSquare;
    }
    
 

    public long getAllPinnedPiecesValidMoves()
    {
        int kingSquare = determineKingSquare();
        String[] axialPieces =  new String[]{"r", "q"};
        String[] diagonalPieces = new String[]{"b", "q"};

        if(turn % 2 != 0)
        {
            axialPieces =  new String[]{"R", "Q"};
            diagonalPieces = new String[]{"B", "Q"};
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

    public long getPinnedPieceValidMove(int kingSquare, int[] direction, String[] checkingPieces)
    {
        boolean friendlySeen = false;
        long moves = 0L;

        int row = kingSquare / 8;
        int col = kingSquare % 8;
        int rowDirection = direction[0];
        int colDirection = direction[1];

        row += rowDirection;
        col += colDirection;


        while(0 <= row && row <= 7 && 0 <= col && col <= 7)
        {
            long currentSquare = 1L << (63 - (8 * row + col));

            if ((currentSquare & getFriendlyPieces()) != 0)
            {
                if(friendlySeen)
                {
                    return 0;
                }
                else
                {
                    friendlySeen = true;
                }
            }

            moves |= currentSquare;

            for(String piece : checkingPieces)
            {
                if ((currentSquare & bitBoards.get(piece)) != 0)
                {
                    if(friendlySeen)
                    {
                        return moves;
                    }
                    else
                    {
                        return 0L;
                    }
                }
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
        String king = "K";
        String rook = "R";

        if(turn % 2 != 0)
        {
            shortCastlingSpaceMask = 0x0600000000000000L;
            kingLocation = 0x0800000000000000L;
            rookLocation = 0x0100000000000000L;

            king = "k";
            rook = "r";
        }

        if(isInCheck())
        {
            return false;
        }

        if((bitBoards.get(king) & kingLocation) != 0 && (bitBoards.get(rook) & rookLocation) != 0)
        {
            if((~getAllPiecesBitBoard() & shortCastlingSpaceMask) != 0)
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

        String rook = "R";
        String king = "K";

        if(turn % 2 != 0)
        {
            rookOldPosition = 0x0100000000000000L;
            rookNewPosition =  0x0400000000000000L;

            kingOldPosition = 0x0800000000000000L;
            kingNewPosition = 0x0200000000000000L;

            rook = "r";
            king = "k";
        }

        bitBoards.replace(rook, bitBoards.get(rook), (bitBoards.get(rook) ^ rookOldPosition) | rookNewPosition);

        bitBoards.replace(king, bitBoards.get(king), (bitBoards.get(king) ^ kingOldPosition) | kingNewPosition);
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
        String king = "K";
        String rook = "R";

        if(turn % 2 != 0)
        {
            longCastlingSpaceMask = 0x7000000000000000L;
            kingLocation = 0x0800000000000000L;
            rookLocation = 0x8000000000000000L;

            king = "k";
            rook = "r";
        }

        if(isInCheck())
        {
            return false;
        }

        if((bitBoards.get(king) & kingLocation) != 0 && (bitBoards.get(rook) & rookLocation) != 0)
        {
            if((~getAllPiecesBitBoard() & longCastlingSpaceMask) != 0)
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

        String rook = "R";
        String king = "K";

        if(turn % 2 != 0)
        {
            rookOldPosition = 0x8000000000000000L;
            rookNewPosition =  0x1000000000000000L;

            kingOldPosition = 0x0800000000000000L;
            kingNewPosition = 0x2000000000000000L;

            rook = "r";
            king = "k";
        }

        bitBoards.replace(rook, bitBoards.get(rook), (bitBoards.get(rook) ^ rookOldPosition) | rookNewPosition);

        bitBoards.replace(king, bitBoards.get(king), (bitBoards.get(king) ^ kingOldPosition) | kingNewPosition);
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



}
