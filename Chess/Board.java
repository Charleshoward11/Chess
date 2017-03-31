import java.io.*;
import java.net.*;
import java.util.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board implements Serializable, Cloneable
{
    private Square[][] board = new Square[8][8];
    
    // Maybe I should have ArrayLists for each color's pieces
    private ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    private ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    
    // If I'm going to have en passant capabilities, 
    // there should probably be a variable here that stores a pawn that just double moved
    
    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                board[x][y] = new Square(x,y);
            }
        }
    }
    
    public Board clone(Board b)
    {
        Board copy = new Board();
        
        this.whitePieces = new ArrayList<Piece>();
        this.blackPieces = new ArrayList<Piece>();
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                if(this.getPiece(x,y) != null)
                {
                    copy.getSquare(x,y).setPiece(this.getPiece(x,y).clone());
                }
            }
        }
        
        return copy;
    }
    
    /**
     * Prints out a text representation of the board to the console.
     * Used for testing.
     */
    public void printBoard()
    {
        System.out.println(" +--0-+--1-+--2-+--3-+--4-+--5-+--6-+--7-+");
        
        for(int y = 0; y < 8; y++)
        {
            System.out.print(" " + y);
            
            for(int x = 0; x < 8; x++)
            {
                System.out.print(board[x][y]);
                if (x < 7)
                    System.out.print("|");
            }
            
            System.out.println(y);
            if (y < 7)
                System.out.println(" +----+----+----+----+----+----+----+----+");
        }
        System.out.println(" +--0-+--1-+--2-+--3-+--4-+--5-+--6-+--7-+");
    }
    
    public Board clearSelections()
    {
        for(Square[] ss : board)
        {
            for(Square s : ss)
            {
                s.setSelectionStatus(" ");
            }
        }
        
        return this;
    }
    
    /**
     * Requests a piece's moveset, then does a second pass on it to apply it to the board's current state.
     */
    public ValidMoveList getValidMoves(Piece p)
    {
        ValidMoveList processedMoves = new ValidMoveList();
        
        ArrayList<ArrayList<Move>> moves = p.getMoves(this);
        
        // There's a LOT of other rules associated with castling, but I'll put those in later.
        if((p.isRook() || p.isKing()) && !p.hasMoved())
        {
            for(Move m : moves.get(0))
            {
                processedMoves.addMove(m);
            }
            moves.remove(0);
        }
        
        // If/else block for the different styles of movement:
        if(p.isQueen() || p.isRook() || p.isBishop())
        {
            // "Runners": Queen, Rook, Bishop
            // These pieces can move and capture in a straight line of any distance, as long as that line is not interrupted by another piece (hostile or friendly).
            // I need to evaluate each line of moves and figure out if and where it gets interrupted, figure out if the interruption is an opportunity for a capture,
            // and invalidate the rest of the moves in that line.
            
            // Iterating through each direction of movement.
            for(int i = 0; i < moves.size(); i++)
            {
                // Iterating through each square in that direction.
                // This for loop actually starts on the second element because of how the pieces say they move.
                // The first move of each direction just puts the piece on the space where it already is.
                // Irregardless of the fact that such a move is completely illegal in chess, 
                // it triggers the conditional for a move to a space that's already occupied by a piece,  
                // and the program essentially decides that the piece can't move because it's in its own way.
                // Instead of changing the piece's getMoves method to not return the illegal moves in the first place,
                // I just told the program to start on the second move of each direction. It was simpler.
                for(int j = 1; j < moves.get(i).size(); j++)
                {
                    Move m = moves.get(i).get(j);
                    
                    if(m.getTo().hasPiece())
                    {
                        Piece d = m.getTo().getPiece();
                        
                        // If the piece on the destination square is the opposing color, 
                        // then the move is marked as a capture and added to the list.
                        if(p.isWhite != d.isWhite)
                        {
                            processedMoves.addCapture(m);
                        }
                        
                        // Regardless of whether this move is a capture or not, the piece can move no further in this direction.
                        break;
                    }
                    
                    processedMoves.addMove(m);
                }
            }
        }
        else if(p.isKing() || p.isKnight())
        {
            // "Walkers": King, Knight
            // These pieces have set distances at which they can move and capture. I just need to evaluate each move's viability.
            
            for(Move m : moves.get(0))
            {
                if(m.getTo().hasPiece())
                {
                    Piece d = m.getTo().getPiece();
                    
                    // If the piece on the destination square is the opposing color, 
                    // then the move is added to the list of captures.
                    if(p.isWhite != d.isWhite)
                    {
                        //m.setCapture(true);
                        processedMoves.addCapture(m);
                    }
                }
                else
                {
                    // Why is this adding it as a capture? Is that a mistake?
                    processedMoves.addCapture(m);
                }
            }
        }
        else if(p.isPawn())
        {
            // Pawns just kinda do their own thing
            
            Move m = moves.get(0).get(0);
            
            if(!m.getTo().hasPiece())
            {
                processedMoves.addMove(m);
                
                if(!p.hasMoved() && (moves.get(0).size() == 2))
                {
                    m = moves.get(0).get(1);
                    if(!m.getTo().hasPiece())
                    {
                        processedMoves.addMove(m);
                    }
                }
            }
            
            // Still need the captures
            for(Move mm : moves.get(1))
            {
                Square s = mm.getTo();
                if(s.hasPiece())
                {
                    if(p.isWhite != s.getPiece().isWhite)
                    {
                        processedMoves.addCapture(mm);
                    }
                }
            }
        }
        
        return processedMoves;
    }
    
    public ValidMoveList getValidMoves(Square s)
    {
        return getValidMoves(s.getPiece());
    }
    
    public ValidMoveList getValidMoves(int x, int y)
    {
        return getValidMoves(board[x][y].getPiece());
    }
    
    public Square getSquare(int x, int y)
    {
        return board[x][y];
    }
    
    /**
     * I was doing this so much that there might as well be a method for it.
     */
    public Piece getPiece(int x, int y)
    {
        return board[x][y].getPiece();
    }
    
    /**
     * Places a piece on the board based on the piece's internal x and y values.
     * 
     * Returns true if successful, false if not (e.g. if the requested square is occupied).
     * Maybe this should throw an exception or something.
     */
    public boolean placePiece(Piece p)
    {
        int x = p.getX();
        int y = p.getY();
        
        // Making sure that the requested square is actually ON the board.
        if(x < 0 || y < 0 || x > 7 || y > 7)
        {
            return false;
        }
        
        // Making sure that the requested square is unoccupied.
        if(board[x][y].hasPiece())
        {
            return false;
        }
        
        board[x][y].setPiece(p);
        
        return true;
    }
    
    /**
     * Move a piece from one square to another.
     * 
     * Returns true if successful, false if not (e.g. if the requested square is occupied).
     * Maybe this should throw an exception or something.
     */
    public boolean movePiece(Move m)
    //throws Exception
    {
        // Might be able to remove these two, as the class logically shouldn't be able to call this method with bad coordinates.
        if(m.getFrom().getPiece() == null)
        {
            return false;
        }
        
        // If there's a piece on the TO space that's the same color IE trying to take your own piece.
        if(m.getTo().getPiece() != null && m.getTo().getPiece().isWhite == m.getFrom().getPiece().isWhite)
        {
            return false;
        }
        
        Piece p = m.getFrom().removePiece();
        
        p.setX(m.getTo().getX()).setY(m.getTo().getY());
        
        return placePiece(p);
    }
    
    public boolean movePiece(int fX, int fY, int tX, int tY)
    //throws Exception
    {
        return movePiece(new Move(this, fX,fY, tX,tY));
    }
    
    /**
     * Sets up the board in the default starting state for chess.
     */
    public Board resetBoard()
    {
        // Maybe I can have two ArrayLists, one of each color of pieces.
        // I add all the pieces to their respective lists, then use a for/each
        // loop on each list to place the pieces on the board.
        
        // Black pieces
        blackPieces.add(new Rook(false,   0,0));
        blackPieces.add(new Knight(false, 1,0));
        blackPieces.add(new Bishop(false, 2,0));
        blackPieces.add(new Queen(false,  3,0));
        blackPieces.add(new King(false,   4,0));
        blackPieces.add(new Bishop(false, 5,0));
        blackPieces.add(new Knight(false, 6,0));
        blackPieces.add(new Rook(false,   7,0));
        for(int x = 0; x < 8; x++)
        {
            blackPieces.add(new Pawn(false, x,1));
        }
        for(Piece p : blackPieces)
        {
            placePiece(p);
        }
        
        // White pieces
        whitePieces.add(new Rook(true,   0,7));
        whitePieces.add(new Knight(true, 1,7));
        whitePieces.add(new Bishop(true, 2,7));
        whitePieces.add(new Queen(true,  3,7));
        whitePieces.add(new King(true,   4,7));
        whitePieces.add(new Bishop(true, 5,7));
        whitePieces.add(new Knight(true, 6,7));
        whitePieces.add(new Rook(true,   7,7));
        for(int x = 0; x < 8; x++)
        {
            whitePieces.add(new Pawn(true, x,6));
        }
        for(Piece p : whitePieces)
        {
            placePiece(p);
        }
        
        return this;
    }
    
    // Need a cloning method or something like that for all the methods dealing in potential moves.
    
    /**
     * Takes in a King, and checks every possible move in the other player's turn.
     * 
     * @returns true if the player of the given King is in check.
     */
    public boolean isInCheck(King k)
    {
        for(Square[] ss : board)
        {
            for(Square s : ss)
            {
                if(s.hasPiece())
                {
                    Piece p = s.getPiece();
                
                    if(p.isWhite != k.isWhite)
                    {
                        ValidMoveList ml = getValidMoves(p);
                        
                        for(Move c : ml.getCaptures())
                        {
                            if(c.getTo().getX() == k.getX() && c.getTo().getY() == k.getY())
                            {
                                System.out.println("Check");
                                
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("No check");
        
        return false;
    }
    
    public boolean canCastle(King k, Rook r)
    {
        // I might need to make it so the method takes in two PIECES instead, 
        // and checks if they're a king and a rook here. This might avoid crashes.
        
        // If either piece has moved
        if(k.hasMoved() || r.hasMoved())
        {
            return false;
        }
        
        // If other pieces are in the way
        int i;
        for(i = k.getX() - 1; i > r.getX(); i--)
        {
            if(board[i][k.getY()].getPiece() != null)
            {
                return false;
            }
        }
        for(i = k.getX() + 1; i < r.getX(); i++)
        {
            if(board[i][k.getY()].getPiece() != null)
            {
                return false;
            }
        }
        
        
        // If King is in check
        if(isInCheck(k))
        {
            return false;
        }
        
        // If King would be moving into check
        
        // If King would be moving through check
        
        // If none of those were true
        return true;
    }
    
    //public boolean isThreatenedBy()
}
