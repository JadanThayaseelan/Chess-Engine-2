package Main;

import java.util.ArrayList;

public class Test
{
    public static void main(String[] args)
    {
        Game game = new Game(null);

        long startTime = System.nanoTime();

        long board = 0x0000000f00000000L;
        long board2 = 0x0000000fL;

        System.out.println(board + " " + board2);


        // Stop measuring execution time
        long endTime = System.nanoTime();

        // Calculate the execution time in milliseconds
        long executionTime
                = (endTime - startTime);

        System.out.println("Execution Time:  "
                + executionTime + "ns");

    }

}
