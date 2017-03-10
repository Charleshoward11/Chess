import java.io.*;
import java.net.*;
import java.util.*;

/**
 * You probably know how a rook works in chess.
 * 
 * @author Charles Howard 
 * @version (a version number or a date)
 */
public class Rook extends Piece
{
    public Rook(String color, int x, int y)
    {
        super(color, x, y);
    }
    
    public Rook clone()
    {
        Rook copy = new Rook(this.getColor(), this.getX(), this.getY());
        
        copy.setMoved(this.hasMoved());
        
        return copy;
    }
    
    /**
     * This'll be fun.
     */
    public ArrayList<ArrayList<Move>> getMoves()
    {
        ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
        
        // It felt kind of silly to call the get methods every single time I needed these values.
        final int h1 = getX();
        final int v1 = getY();
        
        int h, v;
        
        ArrayList<Move> castling = new ArrayList<Move>();
        if(!hasMoved())
        {
            if(h1 == 0)
            {
                castling.add(new Move(h1, v1, 3, v1));
            }
            else if(h1 == 7)
            {
                castling.add(new Move(h1, v1, 5, v1));
            }
        }
        
        moves.add(castling);
        
        /* Adding moves in this direction:
         * [ ][ ][ ]
         * [+][R][ ]
         * [ ][ ][ ]
         */
        h = h1; v = v1;
        ArrayList<Move> current = new ArrayList<Move>();
        while(h >= 0)
        {
            current.add(new Move(h1, v1, h, v));
            h--;
        }
        moves.add(current);
        
        /* Adding moves in this direction:
         * [ ][+][ ]
         * [ ][R][ ]
         * [ ][ ][ ]
         */
        h = h1; v = v1;
        current = new ArrayList<Move>();
        while(v >= 0)
        {
            current.add(new Move(h1, v1, h, v));
            v--;
        }
        moves.add(current);
        
        /* Adding moves in this direction:
         * [ ][ ][ ]
         * [ ][R][+]
         * [ ][ ][ ]
         */
        h = h1; v = v1;
        current = new ArrayList<Move>();
        while(h <= 7)
        {
            current.add(new Move(h1, v1, h, v));
            h++;
        }
        moves.add(current);
        
        /* Adding moves in this direction:
         * [ ][ ][ ]
         * [ ][R][ ]
         * [ ][+][ ]
         */
        h = h1; v = v1;
        current = new ArrayList<Move>();
        while(v <= 7)
        {
            current.add(new Move(h1, v1, h, v));
            v++;
        }
        moves.add(current);
        
        return moves;
    }
    
    public String toString()
    {
        if(getColor().equals("White"))
        {
            return "\u200A\u2656\u200A";
        }
        else if(getColor().equals("Black"))
        {
            return "\u200A\u265C\u200A";
        }
        
        return "R";
    }
    
    public boolean isRook(){return true;}
}