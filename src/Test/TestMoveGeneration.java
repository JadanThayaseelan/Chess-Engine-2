package Test;

import Main.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TestMoveGeneration
{
    @Test
    @DisplayName("Test 16bit code is correct for normal move")
    void test16BitMoveNormal()
    {
        Game game = new Game(null);

        //57
        //42
        //1110011010100000
        //111001 101010 0000

        char startMask = 0xFC00;
        char endMask = 0x3F0;
        char move = (char) game.convertTo16BitMove(1L << 63-57, 1L << 63-42);
        System.out.println((startMask & move) >> 10);
        System.out.println((endMask & move) >> 4);
        assertEquals(0xE6A0, move);
    }
}
