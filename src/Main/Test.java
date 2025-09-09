package Main;

import Main.Pieces.BishopMagic;
import Main.Pieces.Knight;
import Main.Pieces.Pawn;
import Main.Pieces.RookMagic;

import java.util.ArrayList;
import java.util.HashMap;

public class Test
{
    public static void main(String[] args)
    {
        Game game = new Game(null);
        Pawn pawn = new Pawn();
        Knight knight = new Knight();
        BishopMagic bishopMagic = new BishopMagic();

        long startTime = System.nanoTime();

        // Stop measuring execution time
        long endTime = System.nanoTime();

//        long moves = pawn.possibleWhitePawnMoves(0x0001000000000000L, 0, 0,0xFF00000000000000L);
//        while(moves != 0)
//        {
//            long move = 1L << Long.numberOfTrailingZeros(moves);
//            moves &= ~move;
//            System.out.println(moves);
//        }

//        for(char move : pawn.calculateWhitePawnMoves(0x0001000000000000L, 0, 0xFF00000000000000L))
//        {
//            System.out.println(MoveGeneration.getStartSquare(move));
//            System.out.println(MoveGeneration.getEndSquare(move));
//        }


//        for(char move : knight.calculateKnightMoves(1, 0, 0))
//        {
//            System.out.println(MoveGeneration.getStartSquare(move));
//            System.out.println(MoveGeneration.getEndSquare(move));
//        }

//        long moves = knight.possibleMoves(1, 0);
//        while(moves != 0)
//        {
//            long move = 1L << Long.numberOfTrailingZeros(moves);
//            moves &= ~move;
//            System.out.println(moves);
//        }


//        for(char move : bishopMagic.calculateBishopMoves(1, 0, 0, 0))
//        {
//            System.out.println(MoveGeneration.getStartSquare(move));
//            System.out.println(MoveGeneration.getEndSquare(move));
//        }

//        long moves = bishopMagic.possibleMoves(1, 0, 0);
//        while(moves != 0)
//        {
//            long move = 1L << Long.numberOfTrailingZeros(moves);
//            moves &= ~move;
//            System.out.println(moves);
//        }


        // Calculate the execution time in milliseconds
        long executionTime
                = (endTime - startTime);

        System.out.println("Execution Time:  "
                + executionTime + "ns");

    }

}
