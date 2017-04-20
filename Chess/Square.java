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
    
    // Determines if the square is highlighted from a click or not.
    // Possibilities are " ", "current selection", "possible move", "possible capture"
    private String selectionStatus;
    
    public final Board b;
    public final int x;
    public final int y;
    
    private SquareActor actor;
    
    public Square(int x, int y, Board b)
    {
        this.selectionStatus = " ";
        this.currentPiece= null;
        this.x = x;
        this.y = y;
        this.b = b;
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
        if (this.currentPiece == null)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * Is this necessary? Can I just call setPiece with null as an argument?
     * Probably not at this point.
     */
    public void removePiece()
    {
        this.currentPiece = null;
    }
    
    public Square setSelectionStatus(String s)
    {
        this.selectionStatus = s;
        
        return this;
    }
    
    public String getSelectionStatus()
    {
        return this.selectionStatus;
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
            return this.selectionStatus + "  " + this.selectionStatus;
        }
        
        return this.selectionStatus + this.currentPiece + this.selectionStatus;
    }
}