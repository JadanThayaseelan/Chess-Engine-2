package Main;

import Main.Pieces.BishopMagic;
import Main.Pieces.Knight;
import Main.Pieces.RookMagic;

import java.util.ArrayList;
import java.util.HashMap;

public class Test
{
    public static void main(String[] args)
    {
        Game game = new Game(null);
        RookMagic rookMagic = new RookMagic();
        BishopMagic bishopMagic = new BishopMagic();


        long startTime = System.nanoTime();

        rookMagic.getRookAttacks(1, 0);

        // Stop measuring execution time
        long endTime = System.nanoTime();

        // Calculate the execution time in milliseconds
        long executionTime
                = (endTime - startTime);

        System.out.println("Execution Time:  "
                + executionTime + "ns");

    }

}
