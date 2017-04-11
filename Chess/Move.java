import java.io.*;
import java.net.*;

/**
 * @author Charles Howard
 */
public class Move implements Serializable
{
    // Where the move is FROM.
    //private final int fromX;
    //private final int fromY;
    
    // Where the move is TO.
    //private final int toX;
    //private final int toY;
    
    // Maybe the object should be inherently based around two references to squares
    private final Square from;
    private final Square to;
    
    // Maybe there should be a variable saying which piece is doing the move.
    
    // If the move is a capture. I'm not actually sure if this is necessary, 
    // because they're sorted when put into the ValidMoveList class.
    private boolean capture;
    
    public Move(Square from, Square to, boolean capture)
    {
        this.from = from;
        this.to = to;
        this.capture = capture;
    }
    
    public Move(Board b, int fromX, int fromY, int toX, int toY, boolean capture)
    {
        this.from = b.getSquare(fromX, fromY);
        this.to =   b.getSquare(toX, toY);
        this.capture = capture;
    }
    
    public Move(Board b, int fromX, int fromY, int toX, int toY)
    {
        this.from =    b.getSquare(fromX, fromY);
        this.to =      b.getSquare(toX, toY);
        this.capture = false;
    }
    
    public Move(Board b, Piece p, Square to)
    {
        this.from = b.getSquare(p.getX(), p.getY());
        this.to =   to;
        // Capture?
    }
    
    public Move(Board b, Piece p, int toX, int toY)
    {
        this.from = b.getSquare(p.getX(), p.getY());
        this.to =   b.getSquare(toX, toY);
    }
    
    public Square getFrom()
    {
        return this.from;
    }
    
    public Square getTo()
    {
        return this.to;
    }
    
    public boolean isCapture()
    {
        return this.capture;
    }
    
    //public Move setCapture(boolean c)
    //{
    //    this.capture = c;
    //    
    //    return this;
    //}
    
    public String toString()
    {
       return "from " + this.from.x + "," + this.from.y + " to " +  this.to.x + "," + this.to.y;
    }
}