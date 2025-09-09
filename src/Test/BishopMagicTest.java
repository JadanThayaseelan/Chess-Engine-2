package Test;

import Main.Pieces.BishopMagic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BishopMagicTest
{

    static BishopMagic bishopMagic;

    @BeforeAll
    public static void setupBishopMagic()
    {
        bishopMagic = new BishopMagic();
    }

    //Test Calculate bishop mask
    @Test
    @DisplayName("Test function CalculateBishopMasks for corners")
    public void testCalculateBishopMasksCorner()
    {
        assertEquals(0x0040201008040200L, bishopMagic.calculateBishopMask(0));
    }

    @Test
    @DisplayName("Test function CalculateBishopMasks for edge")
    public void testCalculateBishopMasksEdge()
    {
        assertEquals(0x0002000204081000L, bishopMagic.calculateBishopMask(23));
    }

    @Test
    @DisplayName("Test CalculateBishopMasks for centre")
    public void testCalculateBishopMasksCentre()
    {
        assertEquals(0x0000402214001400L, bishopMagic.calculateBishopMask(44));
    }

    //Test bishopAttacksOnTheFly

    @Test
    @DisplayName("Test bishopAttacksOnTheFly on a corner with no blockers")
    public void testBishopAttacksOnTheFlyCorner()
    {
        assertEquals(0x0040201008040201L, bishopMagic.bishopAttacksOnTheFly(0, 0));
    }

    @Test
    @DisplayName("Test bishopAttacksOnTheFly on an edge with blockers")
    public void testBishopAttacksOnTheFlyEdgeBlockers()
    {
        assertEquals(0x0000000402000200L, bishopMagic.bishopAttacksOnTheFly(47, 0x0000000400000200L));
    }

    @Test
    @DisplayName("Test bishopAttacksOnTheFly in the center with blockers")
    public void testBishopAttacksOnTheFlyCentreBlockers()
    {
        assertEquals(0x0040201400142201L, bishopMagic.bishopAttacksOnTheFly(36, 0x0040000400002001L));
    }



}
