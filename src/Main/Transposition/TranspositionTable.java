package Main.Transposition;
import Main.Transposition.TTEntry;
public class TranspositionTable
{
    private final TTEntry[] table;
    private final int size; //number of entries

    public TranspositionTable(int mbSize)
    {
        long bytes = (long) mbSize * 1024 * 1024;
        this.size = (int)(bytes / 32);
        this.table = new TTEntry[size];
    }

    private int index(long zobristKey)
    {
        return (int)(Math.abs(zobristKey) % size);
    }

    public TTEntry probe(long zobristKey)
    {
        TTEntry entry = table[index(zobristKey)];
        if(entry != null && zobristKey == entry.zobristKey)
        {
            return entry;
        }
        return null;
    }

    public void store(long zobristKey, char bestMove, int depth, int score, NodeType nodeType)
    {
        int index = index(zobristKey);
        TTEntry existingEntry = table[index];
        if(existingEntry == null || depth > existingEntry.depth)
        {
            table[index] = new TTEntry(zobristKey, bestMove, depth, score, nodeType);
        }
    }
}
