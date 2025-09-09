package Main;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class Bitboard
{
    public static long[] convertStringBoardToBitBoards(String[][] stringBoard)
    {
        long[] bitBoards = new long[12];

        String allPieces = "PNBRQKpnbrqk";
        for (int i = 0; i < allPieces.length(); i++)
        {
            String currentPiece = String.valueOf(allPieces.charAt(i));
            bitBoards[i] =  generateBitBoard(stringBoard, currentPiece);
        }

        return bitBoards;
    }

    public static String[][] convertBitBoardsToStringBoard(long[] bitboards)
    {
        HashMap<Integer, String> indexToPiece =  new HashMap<>();
        indexToPiece.put(0, "P");
        indexToPiece.put(1, "N");
        indexToPiece.put(2, "B");
        indexToPiece.put(3, "R");
        indexToPiece.put(4, "Q");
        indexToPiece.put(5, "K");

        indexToPiece.put(6, "p");
        indexToPiece.put(7, "n");
        indexToPiece.put(8, "b");
        indexToPiece.put(9, "r");
        indexToPiece.put(10, "q");
        indexToPiece.put(11, "k");

        String[][] stringBoard = new String[8][8];
        for(int index = 0; index < bitboards.length; index++)
        {
            String stringBitBoard = String.format("%64s", Long.toBinaryString(bitboards[index])).replace(' ', '0');
            for(int i = 0 ; i < stringBitBoard.length(); i++)
            {
                int row = i / 8;
                int col = i - (row*8);

                if(stringBitBoard.charAt(i) == '1')
                {
                    stringBoard[row][col] = indexToPiece.get(index);
                }
            }
        }

        return stringBoard;
    }


    public static long[] copy(long[] bitboards)
    {
        long[] bitboardsCopy = new long[12];

        for(int i = 0; i < bitboards.length; i++)
        {
            bitboardsCopy[i] = bitboards[i];
        }

        return bitboardsCopy;
    }

    public static long getWhitePieces(long[] bitboards)
    {
        //0 - 5 is white pieces
        long combined = 0L;
        for(int i = 0; i < 6; i++)
        {
            combined |= bitboards[i];
        }

        return combined;
    }

    public static long getBlackPieces(long[] bitboards)
    {
        //6 - 11 is black pieces
        long combined = 0L;
        for(int i = 6; i < bitboards.length; i++)
        {
            combined |= bitboards[i];
        }

        return combined;
    }

    public static long getAllPieces(long[] bitboards)
    {
        long combined = 0L;
        for (long bitboard : bitboards)
        {
            combined |= bitboard;
        }

        return combined;
    }



//    public static long getCombinedBitBoards(long[] bitboards)
//    {
//        long combinedBitBoard = 0L;
//        for(int i = 0; i < pieces.length(); i++)
//        {
//            String currentPieceLetter = String.valueOf(pieces.charAt(i));
//            combinedBitBoard = combinedBitBoard | bitboards.get(currentPieceLetter);
//        }
//
//        return combinedBitBoard;
//    }

    public static Long generateBitBoard(String[][] board, String piece)
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

    public static long convertSquareToBitboard(int square)
    {
        return 1L << 63 - square;
    }
}
