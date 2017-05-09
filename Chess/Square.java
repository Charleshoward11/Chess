import java.io.*;
import java.net.*;

/**
 * This was originally called 'CheckersSquare', and it did a LOT more of the work for some reason.
 * 
 * @author Charles Howard 
 * @version (a version number or a date)
 */
public class Square implements Serializable
{
    // Stores the piece currently on the square.
    private Piece currentPiece;
    
    public final Board board;
    public final int x;
    public final int y;
    
    private SquareActor actor;
    
    public Square(int x, int y, Board b)
    {
        this.currentPiece= null;
        this.x = x;
        this.y = y;
        this.board = b;
    }
    
    /**
     * Sets this square's occupant to a specified piece,
     * and sets that piece's current square to this one.
     * 
     * No longer causes an infinite loop!
     */
    public Square setPiece(Piece p)
    {
        this.currentPiece = p;
        
        if(p.getSquare() != this)
            p.setSquare(this);
        
        return this;
    }
    
    public Piece getPiece()
    {
        return this.currentPiece;
    }
    
    public boolean hasPiece()
    {
        if(this.currentPiece != null)
            return true;
        
        return false;
    }
    
    /**
     * Is this necessary? Can I just call setPiece with null as an argument?
     * Probably not at this point.
     */
    public void removePiece()
    {
        this.currentPiece = null;
    }
    
    /**
     * If the Square already has an actor, this method does nothing.
     * 
     * It should probably throw an exception, though.
     */
    public Square setActor(SquareActor a)
    {
        if(this.actor == null)
            this.actor = a;
        
        return this;
    }
    
    public SquareActor getActor()
    {
        return this.actor;
    }
    
    public String toString()
    {
        if(currentPiece == null)
        {
            return "  ";
        }
        
        return "" + this.currentPiece;
    }
}