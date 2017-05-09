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
    public static int movePoints =      1;
    public static int pawnPoints =      1;
    public static int bishopPoints =    3;
    public static int knightPoints =    3;
    public static int rookPoints =      5;
    public static int queenPoints =     10;
    public static int checkPoints =     8;
    public static int checkMatePoints = 1000000;
    
    
    // Points for this specific move.
    public int points;
    
    // Whether this is a move by a white piece or not.
    public boolean isWhite;
    
    // Points for the entire tree of moves.
    public int totalPoints;
    
    public final Move move;
    public Board moveResult;
    
    ArrayList<Node> subsequentMoves;
    
    /**
     * @param   layer   How many more layers there should be.
     * @param   m       The move.
     * @param   b       The board this node is being made for.
     * @param   capture Whether this particular move is a capture or not.
     */
    public Node(int layer, Move m, Board b, boolean capture, boolean isWhite)
    {
        this.move = m;
        
        if(layer > 0)
        {
            ValidMoveList v 
        }
    }
    
    public int setPoints()
    {
        
    }
}
