package Main;

import java.util.*;

public class RookMagicGenerator {

    static final Random RNG = new Random();

    public static void main(String[] args) {
        long[] rookMagics = new long[64];
        int[] rookShifts = new int[64];

        for (int sq = 0; sq < 64; sq++) {
            long mask = rookMask(sq);
            int relevantBits = Long.bitCount(mask);
            int shift = 64 - relevantBits;
            rookShifts[sq] = shift;

            System.out.println("Generating magic for square " + sq + " (relevant bits = " + relevantBits + ")");
            rookMagics[sq] = findMagic(sq, mask, relevantBits, shift);
            System.out.printf("Square %d magic = 0x%016XL, shift = %d%n", sq, rookMagics[sq], shift);
        }
    }

    // Generate rook mask (excluding board edges)
    static long rookMask(int sq) {
        long mask = 0;
        int rank = sq / 8, file = sq % 8;
        for (int r = rank + 1; r <= 6; r++) mask |= 1L << (r * 8 + file);
        for (int r = rank - 1; r >= 1; r--) mask |= 1L << (r * 8 + file);
        for (int f = file + 1; f <= 6; f++) mask |= 1L << (rank * 8 + f);
        for (int f = file - 1; f >= 1; f--) mask |= 1L << (rank * 8 + f);
        return mask;
    }

    // Generate all occupancies of the mask
    static long[] generateOccupancies(long mask) {
        int bits = Long.bitCount(mask);
        int size = 1 << bits;
        long[] occs = new long[size];
        int[] setBits = new int[bits];

        int idx = 0;
        for (int i = 0; i < 64; i++) {
            if ((mask & (1L << i)) != 0) {
                setBits[idx++] = i;
            }
        }

        for (int i = 0; i < size; i++) {
            long occ = 0;
            for (int j = 0; j < bits; j++) {
                if (((i >> j) & 1) != 0) occ |= (1L << setBits[j]);
            }
            occs[i] = occ;
        }
        return occs;
    }

    // Compute rook attacks brute force
    static long rookAttacksOnTheFly(int sq, long occ) {
        long attacks = 0;
        int rank = sq / 8, file = sq % 8;

        for (int r = rank + 1; r < 8; r++) {
            attacks |= 1L << (r * 8 + file);
            if (((1L << (r * 8 + file)) & occ) != 0) break;
        }
        for (int r = rank - 1; r >= 0; r--) {
            attacks |= 1L << (r * 8 + file);
            if (((1L << (r * 8 + file)) & occ) != 0) break;
        }
        for (int f = file + 1; f < 8; f++) {
            attacks |= 1L << (rank * 8 + f);
            if (((1L << (rank * 8 + f)) & occ) != 0) break;
        }
        for (int f = file - 1; f >= 0; f--) {
            attacks |= 1L << (rank * 8 + f);
            if (((1L << (rank * 8 + f)) & occ) != 0) break;
        }
        return attacks;
    }

    // Try to find a magic number for a given square
    static long findMagic(int sq, long mask, int relevantBits, int shift) {
        long[] occupancies = generateOccupancies(mask);
        long[] attacks = new long[occupancies.length];
        for (int i = 0; i < occupancies.length; i++) {
            attacks[i] = rookAttacksOnTheFly(sq, occupancies[i]);
        }

        long[] used = new long[occupancies.length];

        while (true) {
            long magic = randomMagic();

            // Skip weak magics (too few high bits set)
            if (Long.bitCount((magic * mask) & 0xFF00000000000000L) < 6) continue;

            Arrays.fill(used, 0);

            boolean fail = false;
            for (int i = 0; i < occupancies.length; i++) {
                long index = (occupancies[i] * magic) >>> shift;
                if (used[(int) index] == 0) {
                    used[(int) index] = attacks[i];
                } else if (used[(int) index] != attacks[i]) {
                    fail = true;
                    break;
                }
            }
            if (!fail) return magic;
        }
    }

    // Generate a random 64-bit number with some masking
    static long randomMagic() {
        return (RNG.nextLong() & RNG.nextLong() & RNG.nextLong());
    }
}