import java.io.*;
import java.net.*;

/**
 * @author Charles Howard
 */
public class Move implements Serializable
{
    // Where the move is FROM.
    private final int fromX;
    private final int fromY;
    
    // Where the move is TO.
    private final int toX;
    private final int toY;
    
    // Maybe there should be a variable saying which piece is doing the move.
    
    // If the move is a capture.
    private boolean capture;
    
    public Move(int fromX, int fromY, int toX, int toY, boolean capture)
    {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.capture = capture;
    }
    
    public Move(int fromX, int fromY, int toX, int toY)
    {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.capture = false;
    }
    
    public Move(Square from, Square to, boolean capture)
    {
        this.fromX = from.getX();
        this.fromY = from.getY();
        this.toX = to.getX();
        this.toY = to.getY();
        this.capture = capture;
    }

    public int getFromX()
    {
        return this.fromX;
    }
    
    public int getFromY()
    {
        return this.fromY;
    }
    
    public int getToX()
    {
        return this.toX;
    }
    
    public int getToY()
    {
        return this.toY;
    }
    
    public boolean isCapture()
    {
        return this.capture;
    }
    
    public Move setCapture(boolean c)
    {
        this.capture = c;
        
        return this;
    }
    
    public String toString()
    {
        return "from " + this.fromX + "," + this.fromY + " to " +  this.toX + "," + this.toY;
    }
}