package Main;

import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Engine
{
    char bestMoveBlack = 0;

    Evaluation evaluation = new Evaluation();
    ZobristHashing zobrist = new ZobristHashing();

    long start = 0L;
    long end = 0L;

    char startSquareMask = 0xFC00;
    char endSquareMask = 0x03F0;
    char flagsMask = 0x000F;

    HashMap<Long, Integer> transpositionTable = new HashMap<>();
    int[][] mvvlvaMatrix = new int[][]{
            {15, 14, 13, 12, 12, 10}, //victim P
            {25, 24, 23, 22, 21, 20}, //victim N
            {35, 34, 33, 32, 31, 30}, //victim B
            {45, 44, 43, 42, 41, 40}, //victim R
            {55, 54 ,53, 52, 51, 50}, //victim Q
            {0, 0, 0, 0, 0, 0}        //victim K
    };

    static int count = 0;


    public int maximise(int depth, int alpha, int beta, Game game)
    {
        if ( depth == 0 )
        {
            count += 1;
            return quiesceneSearch(game, alpha, beta, 5);
        }
        int max = Integer.MIN_VALUE;

        ArrayList<Character> moves = game.getAllMoves();
        for (int i = 0 ; i < moves.size(); i++)
        {
            moves = orderMoves(game, moves, i);
            char currentMove = moves.get(i);

            if(!game.isMoveLegal(currentMove))
            {
                continue;
            }

            long[] bitboardsCopy = Bitboard.copy(game.bitBoards);
            byte cannotCastleFlagsCopy = game.cannotCastleFlags;

            game.makeEngineMove(currentMove);

            int score = minimise( depth - 1, alpha, beta, game);

            game.bitBoards = bitboardsCopy;
            game.cannotCastleFlags = cannotCastleFlagsCopy;

            game.turn -= 1;

            if( score > max )
            {
                max = score;
            }

            alpha = max(alpha, score);
            if(beta <= alpha)
            {
                break;
            }
        }
        return max;
    }

    public int minimise(int depth, int alpha, int beta, Game game)
    {
        if ( depth == 0 )
        {
            count += 1;
            return quiesceneSearch(game, alpha, beta, 5);
        }
        int min = Integer.MAX_VALUE;

        ArrayList<Character> moves = game.getAllMoves();
        for (int i = 0 ; i < moves.size(); i++)
        {
            moves = orderMoves(game, moves, i);
            char currentMove = moves.get(i);

            if(!game.isMoveLegal(currentMove))
            {
                continue;
            }

            long[] bitboardsCopy = Bitboard.copy(game.bitBoards);
            byte cannotCastleFlagsCopy = game.cannotCastleFlags;


            game.makeEngineMove(currentMove);


            int score = maximise( depth - 1, alpha, beta, game);

            game.bitBoards = bitboardsCopy;
            game.cannotCastleFlags = cannotCastleFlagsCopy;

            game.whitePiecesBitboard = Bitboard.getWhitePieces(game.bitBoards);
            game.blackPiecesBitboard = Bitboard.getBlackPieces(game.bitBoards);
            game.allPiecesBitboard = Bitboard.getAllPieces(game.bitBoards);


            game.turn -= 1;

            if( score < min )
            {
                min = score;
            }
            beta = min(beta, score);
            if(beta <= alpha)
            {
                break;
            }

        }
        return min;
    }

    public int quiesceneSearch(Game game, int alpha, int beta, int depth)
    {

        int staticEval = evaluation.evaluate(game.bitBoards, game);
        int bestValue = staticEval;
        if( bestValue >= beta )
            return bestValue;
        if( bestValue > alpha )
            alpha = bestValue;

        if(depth == 0)
        {
            return bestValue;
        }

        ArrayList<Character> moves = game.getAllMoves();
        for (int i = 0 ; i < moves.size(); i++)
        {
            moves = orderMoves(game, moves, i);
            if((moves.get(i) & 0b0100) != 0)
            {
                char currentMove = moves.get(i);

                if(game.isMoveLegal(currentMove))
                {
                    continue;
                }

                long[] bitboardsCopy = Bitboard.copy(game.bitBoards);
                byte cannotCastleFlagsCopy = game.cannotCastleFlags;;


                game.makeEngineMove(currentMove);


                int score = -quiesceneSearch(game, -beta, -alpha, depth - 1);
                game.bitBoards = bitboardsCopy;
                game.cannotCastleFlags = cannotCastleFlagsCopy;

                game.turn -= 1;

                if (score >= beta)
                    return score;
                if (score > bestValue)
                    bestValue = score;
                if (score > alpha)
                    alpha = score;
            }

        }

        return bestValue;
    }

    public char iterativeDeepening(int maxDepth, Game game)
    {
        start = System.currentTimeMillis();
        end = start + 4 * 1000;

        char bestCurrentMove = 0;

        long[] bitboardsCopy;
        byte cannotCastleFlagsCopy;

        int bestEval = Integer.MAX_VALUE;
        int bestIndex = -1;

        int depth = 1;

        ArrayList<Character> moves = game.getAllMoves();
        while (System.currentTimeMillis() < end && depth < maxDepth - 1)
        {
            for(int i = 0; i < moves.size(); i++)
            {
                char currentMove = moves.get(i);
                if(!game.isMoveLegal(currentMove))
                {
                    continue;
                }

                bitboardsCopy = Bitboard.copy(game.bitBoards);
                cannotCastleFlagsCopy = game.cannotCastleFlags;


                game.makeEngineMove(currentMove);
                int eval = maximise(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, game);

                game.bitBoards = bitboardsCopy;
                game.cannotCastleFlags = cannotCastleFlagsCopy;

                game.turn -= 1;

                game.whitePiecesBitboard = Bitboard.getWhitePieces(game.bitBoards);
                game.blackPiecesBitboard = Bitboard.getBlackPieces(game.bitBoards);
                game.allPiecesBitboard = Bitboard.getAllPieces(game.bitBoards);

                if(eval < bestEval && start < end)
                {
                    bestEval = eval;
                    bestCurrentMove = moves.get(i);
                    bestIndex = i;
                }
            }
            bestMoveBlack = bestCurrentMove;
            depth += 1;
            char bestTemp = moves.get(bestIndex);
            moves.set(bestIndex, moves.get(0));
            moves.set(0, bestTemp);


        }
        System.out.println(depth + 1);

        return bestMoveBlack;
    }

    public Long[][] ttMoveOrdering(Long[][] moves)
    {
        return null;
    }

    public ArrayList<Character> orderMoves(Game game, ArrayList<Character> moves, int indexToSwap)
    {
        if(moves.isEmpty())
        {
            return moves;
        }

        int bestIndex = -1;
        int bestScore = Integer.MIN_VALUE;

        for(int i = indexToSwap; i < moves.size(); i++)
        {
            long startMove = 1L << 63 - ((moves.get(i) & startSquareMask)  >> 10);
            long endMove = 1L << 63 - ((moves.get(i) & endSquareMask) >> 4);
            int score = 0;
            if((moves.get(i) & 0b0100) != 0)
            {
                score = mvvlva(game, startMove, endMove);
            }
            if(score > bestScore)
            {
                bestScore = score;
                bestIndex = i;
            }
        }

        char temp = moves.get(indexToSwap);
        moves.set(indexToSwap, moves.get(bestIndex));
        moves.set(bestIndex, temp);

        return moves;
    }


    public int mvvlva(Game game, long pieceToMoveBitboard, long moveBitboard)
    {
        int pieceToCapture = game.determinePieceIndexFromBitBoard(moveBitboard);
        if(pieceToCapture == -1)
        {
            return 0;
        }
        int pieceToMove = game.determinePieceIndexFromBitBoard(pieceToMoveBitboard);

        return mvvlvaMatrix[pieceToCapture % 6][pieceToMove % 6];
    }

    public static void main(String[] args)
    {
        Engine engine = new Engine();

        Game game = new Game(null);
        game.turn += 1;

        long startTime = System.nanoTime();

        engine.iterativeDeepening( 6, game);

        //engine.minimise(6, Integer.MIN_VALUE, Integer.MAX_VALUE, game);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration + "ns");

        System.out.println(count);
    }


}
