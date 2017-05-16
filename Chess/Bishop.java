import java.io.*;
import java.net.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;

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
    public Bishop(boolean isWhite, int x, int y)
    {
        super(isWhite, x, y);
    }
    
    public Bishop clone()
    {
        Bishop copy = new Bishop(this.isWhite, this.getX(), this.getY());
        
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
        final int h1 = super.getX();
        final int v1 = super.getY();
        
        int h, v;
        
        ArrayList<Move> current;
        
        // Adding moves in this direction:
        // [+][ ][ ]
        // [ ][B][ ]
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
        // [ ][B][ ]
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
        // [ ][B][ ]
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
        // [ ][B][ ]
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
            return "\u200A\u2657\u200A";
        else
            return "\u200A\u265D\u200A";
    }
}