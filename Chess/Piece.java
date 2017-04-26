import java.io.*;
import java.net.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;

/**
 * Abstract class that is the base for all the other pieces.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Piece implements Cloneable
{
    /**
     * (Deprecated) Integers that store the piece's position on the board.
     */
    private int x, y;
    
    /**
     * Square that stores the piece's position on the board.
     */
    private Square currentSquare;
    
    /**
     * I replaced the color string with this, to make things simpler.
     */
    public final boolean isWhite;
    
    /**
     * Tells if the piece has moved, is used in castling and pawn double-moves.
     */
    private boolean moved;
    
    /**
     * I wish I could make this a public final, but I don't think I can.
     */
    private PieceActor actor;
    
    // Maybe there should be a variable for the piece's point value. Can you overload variables?
    
    // This class should probably also function as the base actor class.
    
    public Piece(boolean isWhite, int x, int y)
    {
        this.isWhite = isWhite;
        this.x = x;
        this.y = y;
        this.moved = false;
    }
    
    /**
     * Sets this piece's position to a specified square,
     * and sets that square's current piece to this one.
     * 
     * Also updates the deprecated x and y values.
     * 
     * No longer causes an infinite loop!
     */
    public Piece setSquare(Square target)
    {
        //this.currentSquare.removePiece();
        this.currentSquare = target;
        if(target.getPiece() != this)
            target.setPiece(this);
        this.x = currentSquare.x;
        this.y = currentSquare.y;
        this.moved = true;
        return this;
    }
    
    public Square getSquare()
    {
        return this.currentSquare;
    }
    
    public Board getBoard()
    {
        return this.currentSquare.board;
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
    
    /**
     * If the piece already has an actor, this method does nothing.
     * 
     * It should probably throw an exception, though.
     */
    public Piece setActor(PieceActor a)
    {
        if(this.actor == null)
            this.actor = a;
        
        return this;
    }
    
    public PieceActor getActor()
    {
        return this.actor;
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
    public abstract ArrayList<ArrayList<Move>> getMoves(Board b);
    
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
