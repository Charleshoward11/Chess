import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board implements Serializable
{
    private Square[][] board = new Square[8][8];
    
    // Maybe I should have ArrayLists for each color's pieces
    private ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    private ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    
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
    
    /**
     * Prints out a text representation of the board to the console.
     * Used for testing.
     */
    public void printBoard()
    {
        System.out.println("+----+----+----+----+----+----+----+----+");
        
        for(int y = 0; y < 8; y++)
        {
            System.out.print("|");
            
            for(int x = 0; x < 8; x++)
            {
                System.out.print(board[x][y] + "|");
            }
            
            System.out.println();
            System.out.println("+----+----+----+----+----+----+----+----+");
        }
    }
    
    /**
     * Requests a piece's moveset, then does a second pass on it to apply it to the board's current state.
     */
    public ValidMoveList getValidMoves(Piece p)
    {
        ValidMoveList processedMoves = new ValidMoveList();
        
        ArrayList<ArrayList<Move>> moves = p.getMoves();
        
        
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
                    
                    if(board[m.getToX()][m.getToY()].hasPiece())
                    {
                        Piece d = board[m.getToX()][m.getToY()].getPiece();
                        
                        // If the piece on the destination square is the opposing color, 
                        // then the move is marked as a capture and added to the list.
                        if(!p.getColor().equals(d.getColor()))
                        {
                            //m.setCapture(true);
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
            // These pieces have set distances at which they can move and capture (generally one space). I just need to evaluate each move's viability.
            
            for(Move m : moves.get(0))
            {
                if(board[m.getToX()][m.getToY()].hasPiece())
                {
                    Piece d = board[m.getToX()][m.getToY()].getPiece();
                    
                    // If the piece on the destination square is the opposing color, 
                    // then the move is added to the list of captures.
                    if(!p.getColor().equals(d.getColor()))
                    {
                        //m.setCapture(true);
                        processedMoves.addCapture(m);
                    }
                }
                else
                {
                    processedMoves.addCapture(m);
                }
            }
        }
        else if(p.isPawn())
        {
            // Pawns just kinda do their own thing
            
            Move m = moves.get(0).get(0);
            
            if(!board[m.getToX()][m.getToY()].hasPiece())
            {
                processedMoves.addMove(m);
                
                if(!p.hasMoved() && (moves.get(0).size() == 2))
                {
                    m = moves.get(0).get(1);
                    if(!board[m.getToX()][m.getToY()].hasPiece())
                    {
                        processedMoves.addMove(m);
                    }
                }
            }
            
            // Still need the captures
            for(Move mm : moves.get(1))
            {
                Square s = board[mm.getToX()][mm.getToY()];
                if(s.hasPiece())
                {
                    if(!p.getColor().equals(s.getPiece().getColor()))
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
     * Sets up the board in the default starting state for chess.
     */
    public Board resetBoard()
    {
        // Maybe I can have two ArrayLists, one of each color of pieces.
        // I add all the pieces to their respective lists, then use a for/each
        // loop on each list to place the pieces on the board.
        
        // Black pieces
        placePiece(new Rook("Black", 0, 0));
        placePiece(new Knight("Black", 1, 0));
        placePiece(new Bishop("Black", 2, 0));
        placePiece(new Queen("Black", 3, 0));
        placePiece(new King("Black"));
        placePiece(new Bishop("Black", 5, 0));
        placePiece(new Knight("Black", 6, 0));
        placePiece(new Rook("Black", 7, 0));
        for(int x = 0; x < 8; x++)
        {
            placePiece(new Pawn("Black", x, 1));
        }
        
        // White pieces
        placePiece(new Rook("White", 0, 7));
        placePiece(new Knight("White", 1, 7));
        placePiece(new Bishop("White", 2, 7));
        placePiece(new Queen("White", 3, 7));
        placePiece(new King("White"));
        placePiece(new Bishop("White", 5, 7));
        placePiece(new Knight("White", 6, 7));
        placePiece(new Rook("White", 7, 7));
        for(int x = 0; x < 8; x++)
        {
            placePiece(new Pawn("White", x, 6));
        }
        
        return this;
    }
    
    // Need a cloning method or something like that for all the methods dealing in potential moves.
    
    /**
     * Takes in a color, and checks every possible move in that player's turn.
     * 
     * @returns true if that player is in check.
     */
    public boolean checkCheck(String color)
    {
        boolean check = false;
        
        for(Square[] ss : board)
        {
            for(Square s : ss)
            {
                if(s.hasPiece())
                {
                    Piece p = s.getPiece();
                
                    if(!p.getColor().equals(color))
                    {
                        ValidMoveList ml = getValidMoves(p);
                        
                        for(Move c : ml.getCaptures())
                        {
                            Piece pp = board[c.getToX()][c.getToY()].getPiece();
                            if(pp.isKing() && !pp.getColor().equals(color))
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return check;
    }
   }
