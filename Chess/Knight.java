import java.io.*;
import java.net.*;
import java.util.*;

/**
 * "Screw the rules, I'm on a horse."
 * 
 * @author Charles Howard 
 * @version (a version number or a date)
 */
public class Knight extends Piece
{
    public Knight(boolean isWhite, int x, int y)
    {
        super(isWhite, x, y);
    }
    
    public Knight clone()
    {
        Knight copy = new Knight(this.isWhite, this.getX(), this.getY());
        
        copy.setMoved(this.hasMoved());
        
        return copy;
    }
    
    /**
     * This'll be fun.
     */
    public ArrayList<ArrayList<Move>> getMoves(Board b)
    {
        ArrayList<ArrayList<Move>> m = new ArrayList<ArrayList<Move>>();
        ArrayList<Move> moves = new ArrayList<Move>();
        m.add(moves);
        
        // It felt kind of silly to call the get methods every single time I needed these values.
        final int x1 = super.getX();
        final int y1 = super.getY();
        
        /* Adding these moves.
         * [+][ ][ ]
         * [ ][ ][N]
         * [+][ ][ ]
         */
        if(x1 > 1)
        {
            if(y1 > 0)
            {
                moves.add(new Move(b, this, x1 - 2, y1 - 1));
            }
            if(y1 < 7)
            {
                moves.add(new Move(b, this, x1 - 2, y1 + 1));
            }
        }
        
        /* Adding these moves.
         * [+][ ][+]
         * [ ][ ][ ]
         * [ ][N][ ]
         */
        if(y1 > 1)
        {
            if(x1 > 0)
            {
                moves.add(new Move(b, this, x1 - 1, y1 - 2));
            }
            if(x1 < 7)
            {
                moves.add(new Move(b, this, x1 + 1, y1 - 2));
            }
        }
        
        /* Adding these moves.
         * [ ][ ][+]
         * [N][ ][ ]
         * [ ][ ][+]
         */
        if(x1 < 6)
        {
            if(y1 > 0)
            {
                moves.add(new Move(b, this, x1 + 2, y1 - 1));
            }
            if(y1 < 7)
            {
                moves.add(new Move(b, this, x1 + 2, y1 + 1));
            }
        }
        
        /* Adding these moves.
         * [ ][N][ ]
         * [ ][ ][ ]
         * [+][ ][+]
         */
        if(y1 < 6)
        {
            if(x1 > 0)
            {
                moves.add(new Move(b, this, x1 - 1, y1 + 2));
            }
            if(x1 < 7)
            {
                moves.add(new Move(b, this, x1 + 1, y1 + 2));
            }
        }
        
        return m;
    }
    
    public String toString()
    {
        if(isWhite)
        {
            return "\u200A\u2658\u200A";
        }
        else
        {
            return "\u200A\u265E\u200A";
        }
    }
    
    public boolean isKnight(){return true;}
}