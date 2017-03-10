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
    
    private final int x;
    private final int y;
    
    public Square(int x, int y)
    {
        this.selectionStatus = " ";
        this.currentPiece= null;
        this.x = x;
        this.y = y;
    }
    
    public Square setPiece(Piece p)
    {
        this.currentPiece = p;
        
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
    
    // Is this necessary? Can I just call setPiece with null as an argument?
    public Piece removePiece()
    {
        Piece p = this.currentPiece;
        this.currentPiece = null;
        return p;
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
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
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