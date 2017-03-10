import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Abstract class that is the base for all the other pieces.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Piece implements Cloneable
{
    // Integers that store the piece's position on the board.
    private int x, y;
    
    // Take a wild guess what this String stores.
    private final String color;
    
    // Tells if the piece has moved, is used in castling and pawn double-moves.
    private boolean moved;
    
    // Maybe there should be a variable for the piece's point value. Can you overload variables?
    
    // This class should probably also function as the base actor class.
    
    public Piece(String color, int x, int y)
    {
        // initialise instance variables
        this.color = color;
        this.x = x;
        this.y = y;
        this.moved = false;
    }
    
    public String getColor()
    {
        return this.color;
    }
    
    public Piece setX(int x)
    {
        this.x = x;
        
        this.moved = true;
        
        return this;
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public Piece setY(int y)
    {
        this.y = y;
        
        this.moved = true;
        
        return this;
    }
    
    public int getY()
    {
        return this.y;
    }
    
    public Piece setMoved(boolean m)
    {
        this.moved = m;
        
        return this;
    }
    
    public boolean hasMoved()
    {
        return this.moved;
    }
    
    public Piece clone()
    {
        return null;
    }
    
    /**
     * This method "cooperates" with a method in the board class.
     * This method returns the way the piece moves, while the board's method analyzes 
     * the output and figures out how to apply it to the current game scenario.
     * 
     * I might make this return a multidimensional arraylist, 
     * with each dimension representing a line of moves for a "runner"-type piece.
     */
    public abstract ArrayList<ArrayList<Move>> getMoves();
    
    /**
     * I found the unicode characters for chess pieces, but they were all 1+1/2 spaces wide,
     * so I also had to find the unicode character for 1/4 space. It was a little annoying.
     */
    public abstract String toString();
    
    // Each piece overloads one of these methods.
    public boolean isKing(){return false;}
    public boolean isRook(){return false;}
    public boolean isPawn(){return false;}
    public boolean isQueen(){return false;}
    public boolean isBishop(){return false;}
    public boolean isKnight(){return false;}
}
