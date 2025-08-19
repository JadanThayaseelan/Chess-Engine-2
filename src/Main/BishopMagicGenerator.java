package Main;

import java.util.*;

public class BishopMagicGenerator {
    static final int BOARD_SIZE = 64;

    // Directions for bishop: NE, NW, SE, SW
    static final int[] directions = {9, 7, -7, -9};

    // Generate bishop mask for a given square
    static long bishopMask(int square) {
        long mask = 0L;
        int rank = square / 8;
        int file = square % 8;

        for (int dr : directions) {
            int r = rank;
            int f = file;

            while (true) {
                r += dr / 8;
                f += dr % 8;
                if (r <= 0 || r >= 7 || f <= 0 || f >= 7) break; // exclude edges
                mask |= 1L << (r * 8 + f);
            }
        }
        return mask;
    }

    // Generate all subsets of a mask (occupancies)
    static long[] generateOccupancies(long mask) {
        int bits = Long.bitCount(mask);
        int subsetCount = 1 << bits;
        long[] subsets = new long[subsetCount];
        int[] bitPositions = new int[bits];

        int idx = 0;
        for (int i = 0; i < 64; i++) {
            if ((mask & (1L << i)) != 0) bitPositions[idx++] = i;
        }

        for (int i = 0; i < subsetCount; i++) {
            long subset = 0L;
            for (int j = 0; j < bits; j++) {
                if ((i & (1 << j)) != 0) subset |= 1L << bitPositions[j];
            }
            subsets[i] = subset;
        }

        return subsets;
    }

    // Example function to generate a random candidate magic number
    static long randomMagic() {
        Random rand = new Random();
        long magic = rand.nextLong() & rand.nextLong() & rand.nextLong(); // sparse bits
        return magic;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 64; i++)
        {
            System.out.println();
            int square = i; // example: a8 if flipped board convention
        long mask = bishopMask(square);

        System.out.printf("Bishop mask for square %d: 0x%016X\n", square, mask);

        // Generate all occupancies
        long[] occupancies = generateOccupancies(mask);
        System.out.println("Number of occupancies: " + occupancies.length);

        // Try random magic numbers (brute-force search)
        for (int attempt = 0; attempt < 1000000; attempt++) {
            long magic = randomMagic();
            boolean collision = false;
            Map<Long, Long> used = new HashMap<>();

            for (long occ : occupancies) {
                long key = (occ * magic) >>> (64 - Long.bitCount(mask));
                if (used.containsKey(key) && used.get(key) != occ) {
                    collision = true;
                    break;
                }
                used.put(key, occ);
            }

            if (!collision) {
                System.out.printf("Found magic: 0x%016XL\n", magic);
                break;
            }
        }
    }
    }
}
