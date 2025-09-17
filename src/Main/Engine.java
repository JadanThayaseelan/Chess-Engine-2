package Main;

import Main.Transposition.NodeType;
import Main.Transposition.TTEntry;
import Main.Transposition.TranspositionTable;
import Main.Transposition.ZobristHashing;
import org.w3c.dom.Node;

import javax.print.attribute.standard.JobKOctets;
import java.util.*;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Engine
{
    char bestMoveBlack = 0;

    Evaluation evaluation = new Evaluation();
    ZobristHashing zobrist = new ZobristHashing();
    TranspositionTable transpositionTable = new TranspositionTable(50);

    long start = 0L;
    long end = 0L;

    char startSquareMask = 0xFC00;
    char endSquareMask = 0x03F0;
    char flagsMask = 0x000F;


    int[][] mvvlvaMatrix = new int[][]{
            {15, 14, 13, 12, 12, 10}, //victim P
            {25, 24, 23, 22, 21, 20}, //victim N
            {35, 34, 33, 32, 31, 30}, //victim B
            {45, 44, 43, 42, 41, 40}, //victim R
            {55, 54 ,53, 52, 51, 50}, //victim Q
            {0, 0, 0, 0, 0, 0}        //victim K
    };

    static int count = 0;


    public int getMoveIndex (char[] moves, char moveToFind)
    {
        for(int i = 0; i < moves.length; i++)
        {
            if(moves[i] == moveToFind)
            {
                return i;
            }
        }

        return -1;
    }

    public int maximise(int depth, int alpha, int beta, Game game)
    {
        long key = zobrist.hash(game, game.bitBoards);
        char ttMove = 0;
        TTEntry entry = transpositionTable.probe(key);
        if(entry != null && entry.depth >= depth)
        {
            ttMove = entry.bestMove;
            switch (entry.nodeType)
            {
                case EXACT:
                    return entry.score;
                case LOWER:
                    if (entry.score >= beta) return entry.score;
                    break;
                case UPPER:
                    if (entry.score <= alpha) return entry.score;
                    break;
            }
        }


        if ( depth == 0 )
        {
            count += 1;
            //return evaluation.evaluate(game.bitBoards, game);
            return quiesceneSearch(game, alpha, beta, 0);
        }

        int alphaOrig = alpha;
        int max = Integer.MIN_VALUE;
        char bestMove = 0;

        char[] moves = game.calculateAllPseudoLegalMoves();

        int ttMoveIndex = getMoveIndex(moves, ttMove);
        if(ttMove != 0 && ttMoveIndex != -1)
        {
            char temp = moves[ttMoveIndex];
            moves[ttMoveIndex] = moves[0];
            moves[0] = temp;
        }
        for (int i = 0 ; i < moves.length; i++)
        {
            char currentMove = moves[i];


            if(moves[i] == 0 || !game.isMoveLegal(currentMove))
            {
                continue;
            }

            game.makeEngineMove(currentMove);
            int score = minimise(depth - 1, alpha, beta, game);

            game.undoEngineMove();

            if( score > max )
            {
                max = score;
                bestMove = currentMove;
            }

            alpha = max(alpha, score);
            if(beta <= alpha)
            {
                transpositionTable.store(key, bestMove, depth, max, NodeType.LOWER);
                break;
            }
        }

        NodeType type;
        if(max <= alphaOrig)
        {
            type = NodeType.UPPER;
        }
        else if(max >= beta)
        {
            type = NodeType.LOWER;
        }
        else
        {
            type = NodeType.EXACT;
        }
        transpositionTable.store(key, bestMove, depth, max, type);

        return max;
    }

    public int minimise(int depth, int alpha, int beta, Game game)
    {
        long key = zobrist.hash(game, game.bitBoards);
        TTEntry entry = transpositionTable.probe(key);
        char ttMove = 0;
        if(entry != null && entry.depth >= depth)
        {
            ttMove = entry.bestMove;
            switch (entry.nodeType)
            {
                case EXACT:
                    return entry.score;
                case LOWER:
                    if (entry.score >= beta) return entry.score;
                    break;
                case UPPER:
                    if (entry.score <= alpha) return entry.score;
                    break;
            }
        }


        if ( depth == 0 )
        {
            count += 1;
            //return evaluation.evaluate(game.bitBoards, game);
            return quiesceneSearch(game, alpha, beta, 0);
        }

        int betaOrig = beta;
        int min = Integer.MAX_VALUE;
        char bestMove = 0;

        char[] moves = game.calculateAllPseudoLegalMoves();
        int ttMoveIndex = getMoveIndex(moves, ttMove);
        if(ttMove != 0 && ttMoveIndex != -1)
        {
            char temp = moves[ttMoveIndex];
            moves[ttMoveIndex] = moves[0];
            moves[0] = temp;
        }
        for (int i = 0 ; i < moves.length; i++)
        {
            char currentMove = moves[i];

            if(moves[i] == 0 || !game.isMoveLegal(currentMove))
            {
                continue;
            }

            game.makeEngineMove(currentMove);
            int score = maximise(depth - 1, alpha, beta, game);

            game.undoEngineMove();


            if( score < min )
            {
                min = score;
                bestMove = currentMove;
            }
            beta = min(beta, score);
            if(beta <= alpha)
            {
                transpositionTable.store(key, bestMove, depth, min, NodeType.UPPER);
                break;
            }

        }

        NodeType type;
        if(min <= alpha)
        {
            type = NodeType.UPPER;
        }
        else if(min >= betaOrig)
        {
            type = NodeType.LOWER;
        }
        else
        {
            type = NodeType.EXACT;
        }
        transpositionTable.store(key, bestMove, depth, min, type);

        return min;
    }


    public int quiesceneSearch(Game game, int alpha, int beta, int depth)
    {
        if(depth >= 10)
        {
            return evaluation.evaluate(game.bitBoards, game);
        }
        count += 1;
        int staticEval = evaluation.evaluate(game.bitBoards, game);
        int bestValue = staticEval;
        if( bestValue >= beta )
            return bestValue;
        if( bestValue > alpha )
            alpha = bestValue;


        char[] moves = game.calculateAllPseudoLegalMoves();
        for (int i = 0 ; i < moves.length; i++)
        {
            moves = orderMoves(game, moves, i);
            if((moves[i] & 0b0100) == 0)
            {
                continue;
            }

            char currentCapture = moves[i];

            if(moves[i] == 0 || !game.isMoveLegal(currentCapture))
            {
                continue;
            }
            count += 1;


            game.makeEngineMove(currentCapture);
            int score = -quiesceneSearch(game, -beta, -alpha, depth + 1);
            game.undoEngineMove();


            if (score >= beta)
                return score;
            if (score > bestValue)
                bestValue = score;
            if (score > alpha)
                alpha = score;
        }

        return bestValue;
    }

    public char iterativeDeepening(int maxDepth, Game game)
    {
        start = System.currentTimeMillis();
        end = start + 10 * 1000;


        int depth = 1;
        char bestCurrentMove = 0;
        int bestEval = Integer.MAX_VALUE;
        int bestIndex = -1;

        char[] moves = game.calculateAllPseudoLegalMoves();
        while (System.currentTimeMillis() < end && depth < maxDepth - 1)
        {
            for(int i = 0; i < moves.length; i++)
            {
                char currentMove = moves[i];
                if(moves[i] == 0 || !game.isMoveLegal(currentMove))
                {
                    continue;
                }

                game.makeEngineMove(currentMove);
                int eval = maximise(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, game);

                game.undoEngineMove();

                if(eval < bestEval && start < end)
                {
                    bestEval = eval;
                    bestIndex = i;
                    bestCurrentMove = moves[i];
                }
            }
            bestMoveBlack = bestCurrentMove;
            depth += 1;

            char temp = moves[bestIndex];
            moves[bestIndex] = moves[0];
            moves[0] = temp;

        }
        System.out.println(depth + 1);

        return bestMoveBlack;
    }


    public char[] orderMoves(Game game, char[] moves, int indexToSwap)
    {
        if(moves.length == 0)
        {
            return moves;
        }

        int bestIndex = -1;
        int bestScore = Integer.MIN_VALUE;

        for(int i = indexToSwap; i < game.moveCount; i++)
        {
            long startMove = 1L << 63 - ((moves[i] & startSquareMask)  >> 10);
            long endMove = 1L << 63 - ((moves[i] & endSquareMask) >> 4);
            int score = 0;
            if((moves[i] & 0b0100) != 0)
            {
                score = mvvlva(game, startMove, endMove);
            }
            if(score > bestScore)
            {
                bestScore = score;
                bestIndex = i;
            }
        }

        if(bestIndex != -1) {
            char temp = moves[indexToSwap];
            moves[indexToSwap] = moves[bestIndex];
            moves[bestIndex] = temp;
        }

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
        if(pieceToMove == -1)
        {
            return 0;
        }

        return mvvlvaMatrix[pieceToCapture % 6][pieceToMove % 6];
    }

    public static void main(String[] args)
    {
        Engine engine = new Engine();

        Game game = new Game(null);
        game.turn = 1;

        long startTime = System.nanoTime();

        engine.iterativeDeepening( 7, game);

        //engine.minimise(6, Integer.MIN_VALUE, Integer.MAX_VALUE, game);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println(duration + "ns");

        System.out.println(count);
    }


}
