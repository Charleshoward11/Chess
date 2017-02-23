import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Write a description of class test here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class test
{
    public static void main()
    {
        Board b = new Board();
        
        b.resetBoard();
        
        /*
        Queen q = new Queen("White", 4, 4);
        
        b.placePiece(q);
        
        b.placePiece(new Bishop("White", 1, 4));
        
        b.placePiece(new Bishop("White", 2, 2));
        
        b.placePiece(new Bishop("Black", 1, 7));
        
        b.placePiece(new Bishop("Black", 4, 1));
        
        b.placePiece(new Bishop("Black", 6, 4));
        
        ArrayList<Move> moves = b.getPossibleMoves(q);
        
        for(Move m: moves)
        {
            Square s = b.getSquare(m.getToX(), m.getToY());
            
            if(!s.hasPiece())
            {
                s.setSelectionStatus("S");
            }
            else if(m.isCapture())
            {
                s.setSelectionStatus("C");
            }
        }
        */
        
        b.printBoard();
    }
}
