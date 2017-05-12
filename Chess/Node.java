import java.util.ArrayList;

/**
 * Node used by the ChessAI.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Node
{
    public static int nodeNum = 0;
    
    // Whether this is a move by a white piece or not.
    public boolean isWhite;
    
    // Points for this specific move.
    public int basePoints;
    
    public final Move move;
    public Board moveResult;
    
    ArrayList<Node> moveTree;
    
    /**
     * @param   layer   How many more moves to look ahead.
     * @param   m       The move.
     * @param   b       The board this node is being made for.
     * @param   bP      Base Points
     */
    public Node(Move mv, Board b, int layer, int bP, boolean aiIsWhite)
    {
        nodeNum++;
        System.out.println("Creating node #" + nodeNum + " on layer #" + layer + "...");
        
        this.move = mv;
        
        this.basePoints = basePoints;
        
        this.isWhite = mv.getPiece().isWhite;
        
        //if(mv.to.hasPiece())
        
        try
        {
            this.moveResult = b.getMoveResult(mv);
        }
        catch(InvalidMoveException e)
        {
        }
        
        // If this is the AI's move or not.
        int mod;
        if(aiIsWhite == this.isWhite)
            mod = 2;
        else
            mod = -1;
            
        this.moveTree = new ArrayList<Node>();
        
        if(layer > 0)
        {
            ValidMoveList nextMoves = moveResult.getAllValidMoves(moveResult.isWhoseTurn());
        
            ArrayList<Move> movesAndCastles = new ArrayList<Move>();
            movesAndCastles.addAll(nextMoves.getMoves());
            movesAndCastles.addAll(nextMoves.getCastles());
            
            for(Move m : movesAndCastles)
            {
                Node n = new Node(m, this.moveResult, layer - 1, ChessAI.movePoints*mod, aiIsWhite);
                //this.totalPoints += n.getPoints();
                this.moveTree.add(n);
            }
            
            for(Move m : nextMoves.getCaptures())
            {
                Node n = new Node(m, this.moveResult, layer - 1, m.to.getPiece().points*mod, aiIsWhite);
                //this.totalPoints += n.getPoints();
                this.moveTree.add(n);
            }
            
            for(Move m : nextMoves.getChecks())
            {
                Node n = new Node(m, this.moveResult, layer - 1, ChessAI.checkPoints*mod, aiIsWhite);
                //this.totalPoints += n.getPoints();
                this.moveTree.add(n);
            }
            
            for(Move m : nextMoves.getStalemates())
            {
                Node n = new Node(m, this.moveResult, 0, ChessAI.stalematePoints*mod, aiIsWhite);
                //this.totalPoints += n.getPoints();
                this.moveTree.add(n);
            }
            
            /*
            for(Move m : nextMoves.getSelfChecks())
            {
                Node n = new Node(m, this.moveResult, 0, ChessAI.selfCheckPoints*mod, aiIsWhite);
                //this.totalPoints += n.getPoints();
                this.moveTree.add(n);
            }
             */
            
            for(Move m : nextMoves.getCheckmates())
            {
                Node n = new Node(m, this.moveResult, 0, ChessAI.checkmatePoints*mod, aiIsWhite);
                //this.totalPoints += n.getPoints();
                this.moveTree.add(n);
            }
        }
    }
    
    public int getPoints()
    {
        int points = this.basePoints;
        
        // Add the points of every Node in the moveTree.
        for(Node n : this.moveTree)
        {
            points += n.getPoints();
        }
        
        return points;
    }
}
