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
    
    public Knight(String color, int x, int y)
    {
        super(color, x, y);
    }
    
    /**
     * This'll be fun.
     */
    public ArrayList<ArrayList<Move>> getMoves()
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
                moves.add(new Move(x1, y1, x1 - 2, y1 - 1));
            }
            if(y1 < 7)
            {
                moves.add(new Move(x1, y1, x1 - 2, y1 + 1));
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
                moves.add(new Move(x1, y1, x1 - 1, y1 - 2));
            }
            if(x1 < 7)
            {
                moves.add(new Move(x1, y1, x1 + 1, y1 - 2));
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
                moves.add(new Move(x1, y1, x1 + 2, y1 - 1));
            }
            if(y1 < 7)
            {
                moves.add(new Move(x1, y1, x1 + 2, y1 + 1));
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
                moves.add(new Move(x1, y1, x1 - 1, y1 + 2));
            }
            if(x1 < 7)
            {
                moves.add(new Move(x1, y1, x1 + 1, y1 + 2));
            }
        }
        
        return m;
    }
    
    public String toString()
    {
        if(getColor().equals("White"))
        {
            return "\u200A\u2658\u200A";
        }
        else if(getColor().equals("Black"))
        {
            return "\u200A\u265E\u200A";
        }
        
        return "N";
    }
    
    public boolean isKnight(){return true;}
}