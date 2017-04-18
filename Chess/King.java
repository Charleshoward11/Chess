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
    
    public King(boolean isWhite, int x, int y)
    {
        super(isWhite, x, y);
    }
    
    public Piece clone()
    {
        King copy = new King(this.isWhite, this.getX(), this.getY());
        
        copy.setMoved(this.hasMoved());
        
        return copy;
    }
    
    /**
     * Castling is going to be tricky.
     */
    public ArrayList<ArrayList<Move>> getMoves(Board b)
    {
        ArrayList<ArrayList<Move>> m = new ArrayList<ArrayList<Move>>();
        
        // Castling.
        if(!hasMoved())
        {
            ArrayList<Move> castling = new ArrayList<Move>();
            castling.add(new Move(b, this, b.getSquare(6, getY())));
            castling.add(new Move(b, this, b.getSquare(2, getY())));
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
            moves.add(new Move(b, this, b.getSquare(getX() - 1, getY())));
            if(getY() > 0)
            {
                moves.add(new Move(b, this, b.getSquare(getX() - 1, getY() - 1)));
            }
            if(getY() < 7)
            {
                moves.add(new Move(b, this, b.getSquare(getX() - 1, getY() + 1)));
            }
        }
        
        /* Adding these moves:
         * [ ][ ][+]
         * [ ][K][+]
         * [ ][ ][+]
         */
        if(getX() < 7)
        {
            moves.add(new Move(b, this, b.getSquare(getX() + 1, getY())));
            if(getY() > 0)
            {
                moves.add(new Move(b, this, b.getSquare(getX() + 1, getY() - 1)));
            }
            if(getY() < 7)
            {
                moves.add(new Move(b, this, b.getSquare(getX() + 1, getY() + 1)));
            }
        }
        
        /* Adding these moves:
         * [ ][+][ ]
         * [ ][K][ ]
         * [ ][+][ ]
         */
        if(getY() > 0)
        {
            moves.add(new Move(b, this, b.getSquare(getX(), getY() - 1)));
        }
        if(getY() < 7)
        {
            moves.add(new Move(b, this, b.getSquare(getX(), getY() - 1)));
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
        if(isWhite)
        {
            return "\u200A\u2654\u200A";
        }
        else
        {
            return "\u200A\u265A\u200A";
        }
    }
    
    public boolean isKing()
    {
        return true;
    }
}
