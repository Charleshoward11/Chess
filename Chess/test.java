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
    
    public static void cloneTest()
    {
        Board b1 = new Board();
        b1.resetBoard();
    }
    
    public static void pawnTest()
    {
        Board b = new Board();
        
        b.placePiece(new Pawn(true, 4,6));
        
        b.placePiece(new Pawn(false, 5,5));
        
        b.placePiece(new Pawn(false, 3,3));
        
        ValidMoveList ml = b.getValidMoves(4,6);
        
        for(Move m : ml.getMoves())
        {
            m.getTo().setSelectionStatus("M");
        }
        for(Move m : ml.getCaptures())
        {
            m.getTo().setSelectionStatus("C");
        }
        
        b.printBoard();
        
        b.movePiece(ml.getMoves().get(1));
        
        b.clearSelections();
        
        ml = b.getValidMoves(4,4);
        
        for(Move m : ml.getMoves())
        {
            m.getTo().setSelectionStatus("M");
        }
        for(Move m : ml.getCaptures())
        {
            m.getTo().setSelectionStatus("C");
        }
        
        b.printBoard();
    }
    
    public static void checkTest()
    {
        Board b = new Board();
        
        King k = new King(true, 4,4);
        
        b.placePiece(k);
        
        b.placePiece(new Queen(false, 2,4));
        
        b.placePiece(new Pawn(true, 3,4));
        
        b.placePiece(new Rook(false, 0,5));
        
        b.printBoard();
        
        b.isInCheck(k);
        
        b.movePiece(4,4, 4,5);
        
        b.printBoard();
        
        b.isInCheck(k);
        
    }
    
    public static void castlingTest()
    {
        Board b = new Board();
        
        King king = new King(true, 4,7);
        
        Rook rook1 = new Rook(true, 0,7);
        
        Rook rook2 = new Rook(true, 7,7);
        
        b.placePiece(king);
        
        b.placePiece(rook1);
        
        b.placePiece(rook2);
        
        b.printBoard();
        if(b.canCastle(king, rook1))
        {
            System.out.println("Can castle to left");
        }
        else if(!b.canCastle(king, rook1))
        {
            System.out.println("Can't castle to left");
        }
        if(b.canCastle(king, rook2))
        {
            System.out.println("Can castle to right");
        }
        else if(!b.canCastle(king, rook2))
        {
            System.out.println("Can't castle to right");
        }
        System.out.println();
        
        Bishop bishop1 = new Bishop(true, 2,7);
        b.placePiece(bishop1);
        b.printBoard();
        System.out.println("bishop1 is now on [" + bishop1.getX() + "][" + bishop1.getY() + "]");
        if(b.canCastle(king, rook1))
        {
            System.out.println("Can castle to left");
        }
        else
        {
            System.out.println("Can't castle to left");
        }
        if(b.canCastle(king, rook2))
        {
            System.out.println("Can castle to right");
        }
        else
        {
            System.out.println("Can't castle to right");
        }
        System.out.println();
        
        Bishop bishop2 = new Bishop(true, 5,7);
        b.placePiece(bishop2);
        b.printBoard();
        System.out.println("bishop2 is now on [" + bishop2.getX() + "][" + bishop2.getY() + "]");
        if(b.canCastle(king, rook1))
        {
            System.out.println("Can castle to left");
        }
        else
        {
            System.out.println("Can't castle to left");
        }
        if(b.canCastle(king, rook2))
        {
            System.out.println("Can castle to right");
        }
        else
        {
            System.out.println("Can't castle to right");
        }
        System.out.println();
        
        b.movePiece(2,7, 2,6);
        b.movePiece(5,7, 5,6);
        b.printBoard();
        System.out.println("bishop1 is now on [" + bishop1.getX() + "][" + bishop1.getY() + "]");
        System.out.println("bishop2 is now on [" + bishop2.getX() + "][" + bishop2.getY() + "]");
        if(b.canCastle(king, rook1))
        {
            System.out.println("Can castle to left");
        }
        else
        {
            System.out.println("Can't castle to left");
        }
        if(b.canCastle(king, rook2))
        {
            System.out.println("Can castle to right");
        }
        else
        {
            System.out.println("Can't castle to right");
        }
        System.out.println();
        
        b.movePiece(2,6, 3,7);
        b.movePiece(5,6, 6,7);
        b.printBoard();
        System.out.println("bishop1 is now on [" + bishop1.getX() + "][" + bishop1.getY() + "]");
        System.out.println("bishop2 is now on [" + bishop2.getX() + "][" + bishop2.getY() + "]");
        if(b.canCastle(king, rook1))
        {
            System.out.println("Can castle to left");
        }
        else
        {
            System.out.println("Can't castle to left");
        }
        if(b.canCastle(king, rook2))
        {
            System.out.println("Can castle to right");
        }
        else
        {
            System.out.println("Can't castle to right");
        }
        System.out.println();
        
    }
}
