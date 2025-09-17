package Main.Transposition;

import Main.Evaluation;
import Main.Game;

import java.util.HashMap;
import java.util.Random;

public final class ZobristHashing
{
    Long[][][] pieceRandoms = new Long[64][6][2];
    Long[][] castlingRandoms = new Long[2][2];
    Long[][] enPassantRandoms = new Long[2][8];
    Long sideToMove = new Random().nextLong();

    HashMap<String, Integer> pieceToIndex = new HashMap<>();

    //0 - pawn
    //1 - knight
    //2 - bishop
    //3 - rook
    //4 - queen
    //5 - king

    //0 white
    //1 black

    //0 kingside
    //1 queenside

    public ZobristHashing()
    {
        for(int square = 0; square < 64; square++)
        {
            for(int piece = 0; piece < 6; piece++)
            {
                for(int colour = 0; colour < 2; colour++)
                {
                    pieceRandoms[square][piece][colour] = new Random().nextLong();
                }
            }
        }

        for(int colour = 0; colour < 2; colour++)
        {
            for(int castleSide = 0; castleSide < 2; castleSide++)
            {
                castlingRandoms[colour][castleSide] = new Random().nextLong();
            }
        }

        for(int colour = 0; colour < 2; colour++)
        {
            for(int enpassantSquare = 0; enpassantSquare < 8; enpassantSquare++)
            {
                enPassantRandoms[colour][enpassantSquare] = new Random().nextLong();
            }
        }

        pieceToIndex.put("P", 0);
        pieceToIndex.put("p", 0);

        pieceToIndex.put("N", 1);
        pieceToIndex.put("n", 1);

        pieceToIndex.put("B", 2);
        pieceToIndex.put("b", 2);

        pieceToIndex.put("R", 3);
        pieceToIndex.put("r", 3);

        pieceToIndex.put("Q", 4);
        pieceToIndex.put("q", 4);

        pieceToIndex.put("K", 5);
        pieceToIndex.put("k", 5);
    }
    public long hash(Game game, long[] bitboards)
    {
        long hash = 0L;

        for(int i = 0; i < bitboards.length; i++)
        {
            long currentPieceBitboard = bitboards[i];
            while (currentPieceBitboard != 0)
            {
                int currentSquare = Long.numberOfLeadingZeros(currentPieceBitboard);
                long pieceBoard = 1L << 63 - currentSquare;
                currentPieceBitboard &= ~pieceBoard;

                int colour = 1;
                if(i <= 5)
                {
                    colour = 0;
                }

                hash ^= pieceRandoms[currentSquare][i % 6][colour];

            }
        }

        if((game.cannotCastleFlags & 0b1000) != 0)
        {
            hash ^= castlingRandoms[0][0];
        }
        if((game.cannotCastleFlags & 0b0100) != 0)
        {
            hash ^= castlingRandoms[0][1];
        }

        if((game.cannotCastleFlags & 0b0010) != 0)
        {
            hash ^= castlingRandoms[1][0];
        }
        if((game.cannotCastleFlags & 0b0001) != 0)
        {
            hash ^= castlingRandoms[1][1];
        }

        if(game.turn % 2 != 0)
        {
            hash ^= sideToMove;
        }

        if(game.doublePawnPushBitboard != 0)
        {
            int doubleSquare = Long.numberOfLeadingZeros(game.doublePawnPushBitboard);
            int column = doubleSquare % 8;
            if(game.turn % 2 == 0)
            {
                hash ^= enPassantRandoms[0][column];
            }
            else
            {
                hash ^= enPassantRandoms[1][column];
            }
        }

        return hash;
    }

    public static void main(String[] args)
    {
        Game game = new Game(null);
        Evaluation evaluation = new Evaluation();
        HashMap<Long, Integer> evalMap = new HashMap<>();


        ZobristHashing zobristHashing = new ZobristHashing();


        long hash = zobristHashing.hash(game, game.bitBoards);
        int eval = evaluation.evaluate(game.bitBoards, game);

        evalMap.put(hash, eval);

        Game game1 = new Game(null);

        System.out.println(evalMap.get(hash));
    }

}
