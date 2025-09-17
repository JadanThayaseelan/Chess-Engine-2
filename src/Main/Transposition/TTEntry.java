package Main.Transposition;

public class TTEntry
{
    long zobristKey;
    public char bestMove;
    public int depth;
    public int score;
    public NodeType nodeType;

    public TTEntry(long zobristKey, char bestMove, int depth, int score, NodeType nodeType)
    {
        this.zobristKey = zobristKey;
        this.bestMove = bestMove;
        this.depth = depth;
        this.score = score;
        this.nodeType = nodeType;
    }


}
