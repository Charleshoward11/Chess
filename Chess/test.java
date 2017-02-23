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
    public static void mainTest()
    {
        Board b = new Board();
        
        b.resetBoard();
        
        b.printBoard();
    }
    
    public static void pawnTest()
    {
        Board b = new Board();
        
        b.placePiece(new Pawn("White", 4,6));
        
        b.placePiece(new Pawn("Black", 5,5));
        
        ValidMoveList ml = b.getValidMoves(4,6);
        
        for(Move m : ml.getMoves())
        {
            b.getSquare(m.getToX(), m.getToY()).setSelectionStatus("M");
        }
        for(Move m : ml.getCaptures())
        {
            b.getSquare(m.getToX(), m.getToY()).setSelectionStatus("C");
        }
        
        b.printBoard();
    }
    
    
}
