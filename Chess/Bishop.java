import java.io.*;
import java.net.*;
import java.util.*;

/**
 * You probably know how a bishop works in chess. 
 * I basically just copied over the code for the queen
 * and deleted the horizontal/diagonal moves in the getPossibleMoves method.
 * 
 * @author Charles Howard 
 * @version (a version number or a date)
 */
public class Bishop extends Piece
{
    public Bishop(String color, int x, int y)
    {
        super(color, x, y);
    }
    
    /**
     * This'll be fun.
     */
    public ArrayList<ArrayList<Move>> getMoves()
    {
        ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
        
        // It felt kind of silly to call the get methods every single time I needed these values.
        final int h1 = super.getX();
        final int v1 = super.getY();
        
        int h, v;
        
        ArrayList<Move> current;
        
        // Adding moves in this direction:
        // [+][ ][ ]
        // [ ][B][ ]
        // [ ][ ][ ]
        h = h1;
        v = v1;
        current = new ArrayList<Move>();
        while((h >= 0) && (v >= 0))
        {
            current.add(new Move(h1, v1, h, v));
            h--;
            v--;
        }
        moves.add(current);
        
        // [ ][ ][+]
        // [ ][B][ ]
        // [ ][ ][ ]
        h = h1;
        v = v1;
        current = new ArrayList<Move>();
        while((h <= 7) && (v >= 0))
        {
            current.add(new Move(h1, v1, h, v));
            h++;
            v--;
        }
        moves.add(current);
        
        // [ ][ ][ ]
        // [ ][B][ ]
        // [ ][ ][+]
        h = h1;
        v = v1;
        current = new ArrayList<Move>();
        while((h <= 7) && (v <= 7))
        {
            current.add(new Move(h1, v1, h, v));
            h++;
            v++;
        }
        moves.add(current);
        
        // [ ][ ][ ]
        // [ ][B][ ]
        // [+][ ][ ]
        h = h1;
        v = v1;
        current = new ArrayList<Move>();
        while((h >= 0) && (v <= 7))
        {
            current.add(new Move(h1, v1, h, v));
            h--;
            v++;
        }
        moves.add(current);
        
        return moves;
    }
    
    public String toString()
    {
        if(getColor().equals("White"))
        {
            return "\u200A\u2657\u200A";
        }
        else if(getColor().equals("Black"))
        {
            return "\u200A\u265D\u200A";
        }
        
        return "B";
    }
    
    public boolean isBishop(){return true;}
}