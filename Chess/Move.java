import java.io.*;
import java.net.*;

/**
 * @author Charles Howard
 */
public class Move implements Serializable
{
    public final Square from;
    public final Square to;
    
    // If the move is a capture. I'm not actually sure if this is necessary, 
    // because they're sorted when put into the ValidMoveList class.
    //private boolean capture;
    
    /**
     * Only used when the move is a castle.
     */
    public final King castlingKing;
    public final Rook castlingRook;
    
    public Move(Square from, Square to)
    {
        this.from = from;
        this.to = to;
        
        castlingKing = null;
        castlingRook = null;
    }
    
    /**
     * This one was kind of deprecated, but now it's useful again.
     */
    public Move(Board b, int fromX, int fromY, int toX, int toY)
    {
        this.from = b.getSquare(fromX, fromY);
        this.to = b.getSquare(toX, toY);
        
        castlingKing = null;
        castlingRook = null;
    }
    
    public Move(Board b, Piece p, Square to)
    {
        this.from = p.getSquare();//b.getSquare(p.getX(), p.getY());
        this.to = to;
        
        castlingKing = null;
        castlingRook = null;
    }
    
    public Move(Board b, Piece p, int toX, int toY)
    {
        this.from = p.getSquare();//b.getSquare(p.getX(), p.getY());
        this.to = b.getSquare(toX, toY);
        
        castlingKing = null;
        castlingRook = null;
    }
    
    // Same as above, but with castling.
    
    public Move(Square from, Square to, King k, Rook r)
    {
        this.from = from;
        this.to = to;
        
        castlingKing = k;
        castlingRook = r;
    }
    
    /**
     * This one was kind of deprecated, but now it's useful again.
     */
    public Move(Board b, int fromX, int fromY, int toX, int toY, King k, Rook r)
    {
        this.from = b.getSquare(fromX, fromY);
        this.to = b.getSquare(toX, toY);
        
        castlingKing = k;
        castlingRook = r;
    }
    
    public Move(Board b, Piece p, Square to, King k, Rook r)
    {
        this.from = p.getSquare();//b.getSquare(p.getX(), p.getY());
        this.to = to;
        
        castlingKing = k;
        castlingRook = r;
    }
    
    public Move(Board b, Piece p, int toX, int toY, King k, Rook r)
    {
        this.from = p.getSquare();//b.getSquare(p.getX(), p.getY());
        this.to = b.getSquare(toX, toY);
        
        castlingKing = k;
        castlingRook = r;
    }
    
    public Square getFrom()
    {
        return this.from;
    }
    
    public Square getTo()
    {
        return this.to;
    }
    
    /**
     * This is something that I seem to do a lot.
     * 
     * @return      The piece that is moving (I.E. the piece on the "from" square).
     */
    public Piece getPiece()
    {
        return this.from.getPiece();
    }
    
    public String toString()
    {
       return "from " + this.from.x + "," + this.from.y + " to " +  this.to.x + "," + this.to.y;
    }
}