package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static Main.RookMagicGenerator.RNG;

public class Game
{

    public long[] knightAttacks = new long[64];
    public long[] kingAttacks = new long[64];

    public void setupKnightAttacks()
    {
        long knightPosition = 0x0000000000000001;
        for(int i = 0; i < 64; i++)
        {
            long currentKnightPosition = knightPosition << i;
            knightAttacks[i] = possibleKnightMoves(currentKnightPosition);
        }
    }

    public void setupKingAttacks()
    {
        long kingPosition = 0x0000000000000001;
        for(int i = 0; i < 64; i++)
        {
            long currentKingPosition = kingPosition << i;
            kingAttacks[i] = calculateKingAttacks(currentKingPosition);
        }
    }

    public long calculateKingAttacks(long bitBoard)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;
        long notEightFileMask = 0x00ffffffffffffffL;
        long notOneFileMask = 0xffffffffffffff00L;

        long northMove = bitBoard << 8 & notOneFileMask;
        long northEastMove = bitBoard << 7 & notAFileMask;

        long eastMove = bitBoard >> 1 & notAFileMask;

        long southEastMove = bitBoard >> 9 & notAFileMask;
        long southMove = bitBoard >> 8 & notEightFileMask;
        long southWestMove = bitBoard >> 7 & notHFileMask;

        long westMove = bitBoard << 1 & notHFileMask;

        long northWestMove = bitBoard << 9 & notHFileMask;

        return northMove | northEastMove | eastMove | southEastMove | southMove | southWestMove | westMove | northWestMove;
    }

    public long getKnightAttack(long board)
    {
        int index = 63 - Long.numberOfLeadingZeros(board);
        return knightAttacks[index] & ~getCurrentTurnPiecesBitBoard();
    }

    public long getKingAttack(long board)
    {
        int index = 63 - Long.numberOfLeadingZeros(board);
        return kingAttacks[index] & ~getCurrentTurnPiecesBitBoard();
    }

    public HashMap<String, Long> bitBoards = new HashMap<>();

    public long whitePawnStartingPosition = 0x000000000000FF00L;
    public long blackPawnStartingPosition = 0x00FF000000000000L;

    public RookMagic rookMagic = new RookMagic();
    public BishopMagic bishopMagic = new BishopMagic();

    public int turn = 0;


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
           // stringBoard = new String[][]{
           //         {" ", " ", " ", "R", " ", " ", "r", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "},
           //         {" ", " ", " ", " ", " ", " ", " ", " "}
           // };
        }

        bitBoards = convertStringBoardToBitBoards(stringBoard);

        setupKnightAttacks();
        setupKingAttacks();

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


    public long getPossibleMoves(long selectedPieceBitBoard)
    {
        String[] friendlyPieces = {"P", "B", "N", "R", "K", "Q"};
        if(turn % 2 != 0)
        {
            friendlyPieces = new String[]{"p", "b", "n", "r", "k", "q"};
        }

        for(String piece : friendlyPieces)
        {
            if((bitBoards.get(piece) & selectedPieceBitBoard) != 0)
            {
                return calculatePossibleMoves(piece, selectedPieceBitBoard);
            }
        }

        return 0;
    }

    public long calculatePossibleMoves(String piece, long selectedPieceBitBoard)
    {
        if(piece == null)
        {
            return 0L;
        }
        return switch (piece) {
            case "P", "p" -> possiblePawnMoves(selectedPieceBitBoard);
            case "R", "r" -> rookMagic.getRookAttacks(selectedPieceBitBoard, getAllPiecesBitBoard(), getCurrentTurnPiecesBitBoard());
            case "N", "n" -> getKnightAttack(selectedPieceBitBoard);
            case "B", "b" -> bishopMagic.getBishopAttacks(selectedPieceBitBoard, getAllPiecesBitBoard(), getCurrentTurnPiecesBitBoard());
            case "Q", "q" -> possibleQueenMoves(selectedPieceBitBoard);
            case "K", "k" -> possibleKingMoves(selectedPieceBitBoard);
            default -> 0;
        };
    }


    public boolean isValidMove(long selectedPieceBitBoard, long moveBitBoard)
    {
        long possibleMovesBitBoard = getPossibleMoves(selectedPieceBitBoard);

        if((selectedPieceBitBoard & getCurrentTurnPiecesBitBoard()) != 0L && (moveBitBoard & possibleMovesBitBoard) != 0L)
        {
            return true;
        }

        return false;
    }

    public void makeMove(long pieceToMoveBitBoard, long moveBitBoard)
    {
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

    public long getCurrentTurnPiecesBitBoard()
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


    public long possiblePawnMoves(long board)
    {
        if(turn % 2 == 0)
        {
            return possibleWhitePawnMoves(board);
        }

        return possibleBlackPawnMoves(board);
    }

    public long possibleWhitePawnMoves(long board)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;
        long notEightFileMask = 0x00ffffffffffffffL;

        long moveOneUp = (board << 8) & (notEightFileMask) & ~getAllPiecesBitBoard();
        long moveTwoUp = ((board & whitePawnStartingPosition) << 16) & ~getAllPiecesBitBoard();

        if(moveOneUp == 0L)
        {
            moveTwoUp = 0;
        }

        long takeRight = (board << 7 & (notAFileMask)) & getBlackPiecesBitBoard();
        long takeLeft = (board << 9 & (notHFileMask))  & getBlackPiecesBitBoard();

        return (moveOneUp | moveTwoUp | takeRight | takeLeft) & ~getWhitePiecesBitBoard();
    }

    public long possibleBlackPawnMoves(long board)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;
        long notOneFileMask = 0xffffffffffffff00L;

        long moveOneUp = (board >> 8) & (notOneFileMask) & ~getAllPiecesBitBoard();
        long moveTwoUp = ((board & blackPawnStartingPosition) >> 16) & ~getAllPiecesBitBoard();

        if(moveOneUp == 0L)
        {
            moveTwoUp = 0;
        }

        long takeRight = (board >> 9 & (notAFileMask)) & getWhitePiecesBitBoard();
        long takeLeft = (board >> 7 & (notHFileMask))  & getWhitePiecesBitBoard();
        return (moveOneUp | moveTwoUp | takeRight | takeLeft) & ~getBlackPiecesBitBoard();
    }



    public long possibleKnightMoves(long board)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;

        long notABFileMask = 0x3f3f3f3f3f3f3f3fL;
        long notGHFileMask = 0xfcfcfcfcfcfcfcfcL;

        long upLeftMove = board << 17 & notHFileMask;
        long upRightMove = board << 15 & notAFileMask;

        long downLeftMove = board >> 15 & notHFileMask;
        long downRightMove = board >> 17 & notAFileMask;

        long rightUpMove = board << 6 & notABFileMask;
        long rightDownMove = board >> 10 & notABFileMask;

        long leftUpMove = board << 10 & notGHFileMask;
        long leftDownMove = board >> 6 & notGHFileMask;


        return (upLeftMove | upRightMove | downLeftMove | downRightMove | rightUpMove | rightDownMove | leftUpMove | leftDownMove) & ~getCurrentTurnPiecesBitBoard();
    }

    public long possibleBishopMoves(long board)
    {
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;
        long allMoves = 0L;

        while(board != 0)
        {
            long bishop = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ (1L << 63 - Long.numberOfLeadingZeros(board));

            long moveDiagonalNE = calculateSlidingMoves(bishop, true, 7, notHFileMask) & ~getCurrentTurnPiecesBitBoard();
            long moveDiagonalNW = calculateSlidingMoves(bishop, true, 9, notAFileMask) & ~getCurrentTurnPiecesBitBoard();
            long moveDiagonalSE = calculateSlidingMoves(bishop, false, 9, notHFileMask) & ~getCurrentTurnPiecesBitBoard();
            long moveDiagonalSW = calculateSlidingMoves(bishop, false, 7, notAFileMask) & ~getCurrentTurnPiecesBitBoard();

            allMoves = allMoves | moveDiagonalNE | moveDiagonalNW | moveDiagonalSE | moveDiagonalSW;
        }
        return allMoves;

    }

    public long possibleRookMoves(long board)
    {
        long allMoves = 0L;
        long notAFileMask = 0x7f7f7f7f7f7f7f7fL;
        long notHFileMask = 0xfefefefefefefefeL;
        long notEightFileMask = 0x00ffffffffffffffL;
        long notOneFileMask = 0xffffffffffffff00L;

        while(board != 0)
        {
            long rook = 1L << 63 - Long.numberOfLeadingZeros(board);
            board = board ^ (1L << 63 - Long.numberOfLeadingZeros(board));

            long moveRight = calculateSlidingMoves(rook, false, 1, notHFileMask);
            long moveLeft = calculateSlidingMoves(rook, true, 1, notAFileMask);

            long moveUp = calculateSlidingMoves(rook, true, 8, notEightFileMask);
            long moveDown = calculateSlidingMoves(rook, false, 8, notOneFileMask);

            allMoves = allMoves | moveRight | moveLeft | moveUp | moveDown;

        }
        return allMoves;
    }

    public long possibleQueenMoves(long board)
    {
        return possibleBishopMoves(board) | possibleRookMoves(board);
    }

    public long calculateSlidingMoves(long bitBoard, boolean isLeftShift, int shiftAmount, long notFileMask)
    {
        long moves = 0L;
        long currentMove = bitBoard;
        int count = 0;

        while((getBlackPiecesBitBoard() & moves) == 0 && (getWhitePiecesBitBoard() & moves) == 0 && (currentMove & notFileMask) != 0)
        {
            count += 1;
            if(isLeftShift)
            {
                currentMove = bitBoard << count * shiftAmount;
            }
            else
            {
                currentMove = bitBoard >> count * shiftAmount;
            }

            moves = (moves | currentMove);
        }

        return moves;
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

        return (getKingAttack(bitBoard) | shortCastle | longCastle) & ~getCurrentTurnPiecesBitBoard();
    }





    public boolean isInCheck()
    {

        return false;
    }

    public boolean canShortCastle()
    {
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
