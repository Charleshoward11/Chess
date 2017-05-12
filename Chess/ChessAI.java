import java.io.*;
import java.net.*;
import java.util.*;
/**
 * For the basic logic, I'm thinking that the AI should be preloaded with classic chess opening strategies.
 * Each move, the AI checks which captures are available, and evaluates if it's worth it to deviate.
 * 
 * Actually, maybe I should have it look 3-5 moves ahead (difficulty levels?), and assign values to each potential 
 * move depending on how many captures become available (along with which ones; a Queen would be worth more than a 
 * Pawn), along with how many captures become available to the player. Checkmate would obviously have extra value.
 * 
 * Making it behave defensively might prove to be tricky, this might allow it to tactically put the player in check,
 * as a check would restrict the player's options.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChessAI
{
    public static int movePoints;
    public static int checkPoints;
    public static int checkmatePoints;
    public static int selfCheckPoints;
    public static int stalematePoints;
    
    public int depth;
    public final boolean isWhite;
    public final Board primaryBoard;
    
    
    /**
     * Constructor might take in a difficulty or a knowlege base or something.
     */
    public ChessAI(int d, boolean iW, Board b)
    {
        // I figured this would be the best place to put these, to make them easily tweakable.
        movePoints =        1;
        checkPoints =       80;
        checkmatePoints =   1000000;
        selfCheckPoints =  -10;
        stalematePoints =   100; // Because of the whole node tree, I think it will only go for a stalemate with no other option.
        
        Pawn.points =       10;
        Bishop.points =     30;
        Knight.points =     30;
        Rook.points =       50;
        Queen.points =      100;
        
        this.depth = d;
        this.isWhite = iW;
        this.primaryBoard = b;
    }
    
    /**
     * 
     */
    public Move decideMove()
    {
        // Make sure it's your turn, dummy.
        if(primaryBoard.isWhoseTurn() != this.isWhite)
            return null;
        
        ValidMoveList availableMoves = primaryBoard.getAllValidMoves(this.isWhite);
        
        // If there's a checkmate available, take it.
        if(availableMoves.getCheckmates().size() > 0)
            return availableMoves.getCheckmates().get(0); // I was going to make this random, but that would be pointless.
        
        // Otherwise, calculate.
        ArrayList<Node> choices = new ArrayList<Node>();
        
        
        ArrayList<Move> movesAndCastles = new ArrayList<Move>();
        movesAndCastles.addAll(availableMoves.getMoves());
        //movesAndCastles.addAll(availableMoves.getCastles());
        
        System.out.println("Creating nodes...");
        int i = 0;
        
        for(Move m : movesAndCastles)
        {
            i++;
            //System.out.println("Creating node " + i);
            
            Node n = new Node(m, this.primaryBoard, this.depth, ChessAI.movePoints, this.isWhite);
            choices.add(n);
        }
        
        for(Move m : availableMoves.getCaptures())
        {
            i++;
            //System.out.println("Creating node " + i);
            
            Node n = new Node(m, this.primaryBoard, this.depth, m.to.getPiece().points, this.isWhite);
            choices.add(n);
        }
        
        for(Move m : availableMoves.getChecks())
        {
            i++;
            //System.out.println("Creating node " + i);
            
            Node n = new Node(m, this.primaryBoard, this.depth, ChessAI.checkPoints, this.isWhite);
            choices.add(n);
        }
        
        for(Move m : availableMoves.getStalemates())
        {
            i++;
            //System.out.println("Creating node " + i);
            
            Node n = new Node(m, this.primaryBoard, 0, ChessAI.stalematePoints, this.isWhite);
            choices.add(n);
        }
        /*
        for(Move m : availableMoves.getSelfChecks())
        {
            i++;
            //System.out.println("Creating node " + i);
            
            Node n = new Node(m, this.primaryBoard, 0, ChessAI.selfCheckPoints, this.isWhite);
            choices.add(n);
        }
         */
        
        Node topChoice = null;
        int topScore = 0;
        
        for(Node n : choices)
        {
            if(topChoice == null)
            {
                topChoice = n;
                topScore = topChoice.getPoints();
                continue;
            }
            
            int nScore = n.getPoints();
            
            if(nScore > topScore || (nScore == topScore && Math.random() > 0.5))
            {
                topChoice = n;
                topScore = nScore;
            }
        }
        
        return topChoice.move;
    }
}
