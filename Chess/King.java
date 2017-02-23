import java.io.*;
import java.net.*;
import java.util.*;

/**
 * You probably know how a king works in chess.
 * 
 * @author Charles Howard 
 * @version (a version number or a date)
 */
public class King extends Piece
{
    public boolean check;
    
    /**
     * Since there are only two kings on the board, 
     * you only actually need the color for the 
     * constructor to know where to put it.
     */
    public King(String color)
    {
        super(color, 4, 8);
        
        if(color.equals("White"))
        {
            setY(7);
        }
        else if(color.equals("Black"))
        {
            setY(0);
        }
    }
    
    /**
     * Castling is going to be tricky.
     */
    public ArrayList<ArrayList<Move>> getMoves()
    {
        ArrayList<ArrayList<Move>> m = new ArrayList<ArrayList<Move>>();
        
        
        // Castling. This was surprisingly simple.
        if(!hasMoved())
        {
            ArrayList<Move> castling = new ArrayList<Move>();
            castling.add(new Move(4, getY(), 6, getY()));
            castling.add(new Move(4, getY(), 2, getY()));
            m.add(castling);
        }
        
        ArrayList<Move> moves = new ArrayList<Move>();
        m.add(moves);
        
        /* Adding these moves:
         * [+][ ][ ]
         * [+][K][ ]
         * [+][ ][ ]
         */
        if(getX() > 0)
        {
            moves.add(new Move(getX(), getY(), getX() - 1, getY()));
            if(getY() > 0)
            {
                moves.add(new Move(getX(), getY(), getX() - 1, getY() - 1));
            }
            if(getY() < 7)
            {
                moves.add(new Move(getX(), getY(), getX() - 1, getY() + 1));
            }
        }
        
        /* Adding these moves:
         * [ ][ ][+]
         * [ ][K][+]
         * [ ][ ][+]
         */
        if(getX() < 7)
        {
            moves.add(new Move(getX(), getY(), getX() + 1, getY()));
            if(getY() > 0)
            {
                moves.add(new Move(getX(), getY(), getX() + 1, getY() - 1));
            }
            if(getY() < 7)
            {
                moves.add(new Move(getX(), getY(), getX() + 1, getY() + 1));
            }
        }
        
        /* Adding these moves:
         * [ ][+][ ]
         * [ ][K][ ]
         * [ ][+][ ]
         */
        if(getY() > 0)
        {
            moves.add(new Move(getX(), getY(), getX(), getY() - 1));
        }
        if(getY() > 0)
        {
            moves.add(new Move(getX(), getY(), getX(), getY() - 1));
        }
        
        
        
        return m;
    }
    
    public King setCheck(boolean c)
    {
        this.check = c;
        
        return this;
    }
    
    public boolean isCheck()
    {
        return this.check;
    }
    
    public String toString()
    {
        if(getColor().equals("White"))
        {
            return "\u200A\u2654\u200A";
        }
        else if(getColor().equals("Black"))
        {
            return "\u200A\u265A\u200A";
        }
        return "K";
    }
    
    public boolean isKing()
    {
        return true;
    }
}
