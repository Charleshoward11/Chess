import java.util.ArrayList;

/**
 * Node used by the ChessAI.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Node
{
    // I figured this would be the best place to put these, to make them easily tweakable.
    public final int pawnPoints =   1;
    public final int bishopPoints = 3;
    public final int knightPoints = 3;
    public final int rookPoints =   5;
    public final int queenPoints = 10;
    
    // Points for this specific move.
    public final int points;
    
    // Points for the entire tree of moves.
    public int totalPoints;
    
    public final Move move;
    
    /**
     * @param   m       The move.
     * @param   b       The board this node is being made for.
     * @param   capture Whether this particular move is a capture or not.
     */
    public Node(Move m, Board b, boolean capture)
    {
        
    }
}
