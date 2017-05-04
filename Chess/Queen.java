import java.io.*;
import java.net.*;
import java.util.*;

/**
 * You probably know how a queen works in chess.
 * 
 * @author Charles Howard 
 * @version (a version number or a date)
 */
public class Queen extends Piece
{
    public Queen(boolean isWhite, int x, int y)
    {
        super(isWhite, x, y);
    }
    
    public Queen clone()
    {
        Queen copy = new Queen(this.isWhite, this.getX(), this.getY());
        
        // Effectively pointless.
        //copy.setMoved(this.hasMoved());
        
        return copy;
    }
    
    /**
     * This'll be fun.
     */
    public ArrayList<ArrayList<Move>> getMoves(Board b)
    {
        ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
        
        // It felt kind of silly to call the get methods every single time I needed these values.
        final int h1 = super.getX();
        final int v1 = super.getY();
        
        int h, v;
        
        ArrayList<Move> current;
        
        // These loops could probably be optimized.
        
        // Adding moves in this direction:
        // Horizontal/Vertical
        // [ ][ ][ ]
        // [+][Q][ ]
        // [ ][ ][ ]
        h = h1 - 1;
        v = v1;
        current = new ArrayList<Move>();
        while(h >= 0)
        {
            current.add(new Move(b, h1, v1, h, v));
            h--;
        }
        moves.add(current);
        
        // [ ][+][ ]
        // [ ][Q][ ]
        // [ ][ ][ ]
        h = h1;
        v = v1 - 1;
        current = new ArrayList<Move>();
        while(v >= 0)
        {
            current.add(new Move(b, h1, v1, h, v));
            v--;
        }
        moves.add(current);
        
        // [ ][ ][ ]
        // [ ][Q][+]
        // [ ][ ][ ]
        h = h1 + 1;
        v = v1;
        current = new ArrayList<Move>();
        while(h <= 7)
        {
            current.add(new Move(b, h1, v1, h, v));
            h++;
        }
        moves.add(current);
        
        // [ ][ ][ ]
        // [ ][Q][ ]
        // [ ][+][ ]
        h = h1;
        v = v1 + 1;
        current = new ArrayList<Move>();
        while(v <= 7)
        {
            current.add(new Move(b, h1, v1, h, v));
            v++;
        }
        moves.add(current);
        
        // Diagonals
        // [+][ ][ ]
        // [ ][Q][ ]
        // [ ][ ][ ]
        h = h1 - 1;
        v = v1 - 1;
        current = new ArrayList<Move>();
        while((h >= 0) && (v >= 0))
        {
            current.add(new Move(b, h1, v1, h, v));
            h--;
            v--;
        }
        moves.add(current);
        
        // [ ][ ][+]
        // [ ][Q][ ]
        // [ ][ ][ ]
        h = h1 + 1;
        v = v1 - 1;
        current = new ArrayList<Move>();
        while((h <= 7) && (v >= 0))
        {
            current.add(new Move(b, h1, v1, h, v));
            h++;
            v--;
        }
        moves.add(current);
        
        // [ ][ ][ ]
        // [ ][Q][ ]
        // [ ][ ][+]
        h = h1 + 1;
        v = v1 + 1;
        current = new ArrayList<Move>();
        while((h <= 7) && (v <= 7))
        {
            current.add(new Move(b, h1, v1, h, v));
            h++;
            v++;
        }
        moves.add(current);
        
        // [ ][ ][ ]
        // [ ][Q][ ]
        // [+][ ][ ]
        h = h1 - 1;
        v = v1 + 1;
        current = new ArrayList<Move>();
        while((h >= 0) && (v <= 7))
        {
            current.add(new Move(b, h1, v1, h, v));
            h--;
            v++;
        }
        moves.add(current);
        
        return moves;
    }
    
    public String toString()
    {
        if(isWhite)
        {
            return "\u200A\u2655\u200A";
        }
        else
        {
            return "\u200A\u265B\u200A";
        }
    }
    
    public boolean isQueen(){return true;}
}