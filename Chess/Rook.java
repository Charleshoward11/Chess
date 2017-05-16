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
    // Possibly two Square variables to store the King and this Rook's destination Squares in castling.
    // Actually, I'm not sure if this'll work with the cloning...
    private Square rookDestination;
    private Square kingDestination;
    
    public Rook(boolean isWhite, int x, int y)
    {
        super(isWhite, x, y);
    }
    
    public Rook clone()
    {
        Rook copy = new Rook(this.isWhite, this.getX(), this.getY());
        
        copy.setMoved(this.hasMoved());
        
        return copy;
    }
    
    /**
     * This'll be fun.
     */
    public ArrayList<ArrayList<Move>> getMoves(Board b)
    {
        ArrayList<ArrayList<Move>> moves = new ArrayList<ArrayList<Move>>();
        
        // It felt kind of silly to call the get methods every single time I needed these values.
        final int h1 = getX();
        final int v1 = getY();
        
        int h, v;
        
        /* Adding moves in this direction:
         * [ ][ ][ ]
         * [+][R][ ]
         * [ ][ ][ ]
         */
        h = h1 - 1; v = v1;
        ArrayList<Move> current = new ArrayList<Move>();
        while(h >= 0)
        {
            current.add(new Move(b, h1, v1, h, v));
            h--;
        }
        moves.add(current);
        
        /* Adding moves in this direction:
         * [ ][+][ ]
         * [ ][R][ ]
         * [ ][ ][ ]
         */
        h = h1; v = v1 - 1;
        current = new ArrayList<Move>();
        while(v >= 0)
        {
            current.add(new Move(b, h1, v1, h, v));
            v--;
        }
        moves.add(current);
        
        /* Adding moves in this direction:
         * [ ][ ][ ]
         * [ ][R][+]
         * [ ][ ][ ]
         */
        h = h1 + 1; v = v1;
        current = new ArrayList<Move>();
        while(h <= 7)
        {
            current.add(new Move(b, h1, v1, h, v));
            h++;
        }
        moves.add(current);
        
        /* Adding moves in this direction:
         * [ ][ ][ ]
         * [ ][R][ ]
         * [ ][+][ ]
         */
        h = h1; v = v1 + 1;
        current = new ArrayList<Move>();
        while(v <= 7)
        {
            current.add(new Move(b, h1, v1, h, v));
            v++;
        }
        moves.add(current);
        
        return moves;
    }
    
    public String toString()
    {
        if(isWhite)
            return "\u200A\u2656\u200A";
        else
            return "\u200A\u265C\u200A";
    }
}