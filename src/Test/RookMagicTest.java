package Test;

import Main.Pieces.RookMagic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RookMagicTest
{
    public static RookMagic rookMagic;

    @BeforeAll
    public static void setupRookMagic()
    {
        rookMagic = new RookMagic();
    }

    //Test Calculate rook mask
    @Test
    @DisplayName("Test function CalculateRookMasks for corners")
    public void testCalculateRookMasksCorner()
    {
        assertEquals(0x7e80808080808000L, rookMagic.calculateRookMask(0));
    }

    @Test
    @DisplayName("Test function CalculateRookMasks for center")
    public void testCalculateRookMasksCenter()
    {
        assertEquals(0x0040403E40404000L, rookMagic.calculateRookMask(25));
    }

    @Test
    @DisplayName("Test function CalculateRookMasks for edge")
    public void testCalculateRookMasksedge()
    {
        assertEquals(0x007E010101010100L, rookMagic.calculateRookMask(15));
    }

    //Test rookAttacksOnTheFly

    @Test
    @DisplayName("Tests the rook attacks on a corner with no blockers")
    public void testRookAttacksOnTheFlyCorner()
    {
        assertEquals(0x01010101010101FEL, rookMagic.rookAttacksOnTheFly(63, 0));
    }

    @Test
    @DisplayName("Test rook attacks from center with blockers in every direction")
    public void testRookAttacksOnTheFlyCornerBlockers()
    {
        assertEquals(0x0008087408080808L, rookMagic.rookAttacksOnTheFly(28, 0x0008004400000008L));
    }

    @Test
    @DisplayName("Test get rook attacks")
    public void testGetRookAttacks()
    {
        assertEquals(0x0000000814080808L ,rookMagic.getRookAttacks(0x0000000008000000L, 0x0000000814000008L));
    }



}
