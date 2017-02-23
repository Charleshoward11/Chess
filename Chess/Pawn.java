import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Pawns have a surprisingly complex set of rules associated with them.
 * 
 * @author Charles Howard
 * @version (a version number or a date)
 */
public class Pawn extends Piece
{
    public Pawn(String color, int x)
    {
        super(color, x, 8);
        
        if (color.equals("White"))
        {
            setY(6);
        }
        else if (color.equals("Black"))
        {
            setY(1);
        }
    }
    
    // I should have a special rule/graphic associated with en passant. 
    // If there's an opportunity for an en passant capture, fancy-looking text should appear above it.
    public ArrayList<ArrayList<Move>> getMoves()
    {
        ArrayList<ArrayList<Move>> m = new ArrayList<ArrayList<Move>>();
        
        // This is a trick that I came up for my checkers program.
        // A modifier variable, used to simplify determining which direction the pawn will move in.
        int mod = 0;
        
        if(getColor().equals("White"))
        {
            mod = -1;
        }
        else if(getColor().equals("Black"))
        {
            mod = 1;
        }
        
        ArrayList<Move> forward = new ArrayList<Move>();
        
        // ArrayList for the pawn moving forward.
        forward.add(new Move(getX(), getY(), getX(), getY() + mod));
        // Adding the initial double move
        if(!hasMoved())
        {
            forward.add(new Move(getX(), getY(), getX(), getY() + (2 * mod)));
        }
        m.add(forward);
        
        // ArrayList for the pawn's diagonal capture moves
        ArrayList<Move> capture = new ArrayList<Move>();
        capture.add(new Move(getX(), getY(), getX() + 1, getY() + mod));
        capture.add(new Move(getX(), getY(), getX() - 1, getY() + mod));
        m.add(capture);
        
        return m;
    }
    
    public String toString()
    {
        if(getColor().equals("White"))
        {
            return "\u200A\u2659\u200A";
        }
        else if(getColor().equals("Black"))
        {
            return "\u200A\u265F\u200A";
        }
        
        return "P";
    }
    
    public boolean isPawn(){return true;}
}
