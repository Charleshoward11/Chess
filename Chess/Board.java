import java.io.*;
import java.net.*;
import java.util.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;

/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board implements Serializable//, Cloneable
{
    private Square[][] board = new Square[8][8];
    
    // ArrayLists for each color's pieces
    private ArrayList<Piece> whitePieces = new ArrayList<Piece>();
    private ArrayList<Piece> blackPieces = new ArrayList<Piece>();
    
    // These might be useful.
    private King whiteKing;
    private King blackKing;
    
    public boolean isClone = false;
    
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
                board[x][y] = new Square(x,y, this);
            }
        }
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
        blackPieces = new ArrayList<Piece>();
        
        blackKing = new King(false, 4,0);
        blackPieces.add(blackKing);
        
        blackPieces.add(new Rook(false,   0,0));
        blackPieces.add(new Knight(false, 1,0));
        blackPieces.add(new Bishop(false, 2,0));
        blackPieces.add(new Queen(false,  3,0));
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
            p.setMoved(false);
        }
        
        // White pieces
        whitePieces = new ArrayList<Piece>();
        
        whiteKing = new King(true, 4,7);
        whitePieces.add(whiteKing);
        
        whitePieces.add(new Rook(true,   0,7));
        whitePieces.add(new Knight(true, 1,7));
        whitePieces.add(new Bishop(true, 2,7));
        whitePieces.add(new Queen(true,  3,7));
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
            p.setMoved(false);
        }
        
        return this;
    }
    
    public Board clone()
    {
        Board copy = new Board();
        
        // Boolean to check if it's a clone
        copy.isClone = true;
        
        copy.whitePieces = new ArrayList<Piece>();
        copy.blackPieces = new ArrayList<Piece>();
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                if(this.getPiece(x,y) != null)
                {
                    Piece copyPiece = this.getPiece(x,y).clone();
                    
                    copy.getSquare(x,y).setPiece(copyPiece);
                    
                    copyPiece.setMoved(this.getPiece(x,y).hasMoved());
                    
                    //Add the piece to the ArrayList?
                    if(copyPiece.isWhite)
                    {
                        copy.whitePieces.add(copyPiece);
                        if(copyPiece.isKing())
                            copy.whiteKing = (King)copyPiece;
                    }
                    else
                    {
                        copy.blackPieces.add(copyPiece);
                        if(copyPiece.isKing())
                            copy.blackKing = (King)copyPiece;
                    }
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
    
    /**
     * Requests a piece's moveset, then does a second pass on it to apply it to the board's current state.
     */
    public ValidMoveList getValidMoves(Piece p, boolean getChecks)
    {
        ArrayList<Move> moves = new ArrayList<Move>();
        ArrayList<Move> captures = new ArrayList<Move>();
        ArrayList<Move> checks = new ArrayList<Move>();
        ArrayList<Move> checkmates = new ArrayList<Move>();
        ArrayList<Move> selfChecks = new ArrayList<Move>();
        ArrayList<Move> castles = new ArrayList<Move>();
        
        
        
        ArrayList<ArrayList<Move>> baseMoves = p.getMoves(this);
        
        // If/else block for the different styles of movement:
        if(p.isQueen() || p.isRook() || p.isBishop())
        {
            // "Runners": Queen, Rook, Bishop
            // These pieces can move and capture in a straight line of any distance, as long as that line is not interrupted by another piece (hostile or friendly).
            // I need to evaluate each line of moves and figure out if and where it gets interrupted, figure out if the interruption is an opportunity for a capture,
            // and invalidate the rest of the moves in that line.
            
            // Iterating through each direction of movement.
            for(int i = 0; i < baseMoves.size(); i++)
            {
                // Iterating through each square in that direction.
                // This for loop actually starts on the second element because of how the pieces say they move.
                // The first move of each direction just puts the piece on the space where it already is.
                // Irregardless of the fact that such a move is completely illegal in chess, 
                // it triggers the conditional for a move to a space that's already occupied by a piece,  
                // and the program essentially decides that the piece can't move because it's in its own way.
                // Instead of changing the piece's getMoves method to not return the illegal moves in the first place,
                // I just told the program to start on the second move of each direction. It was simpler.
                for(int j = 1; j < baseMoves.get(i).size(); j++)
                {
                    Move m = baseMoves.get(i).get(j);
                    
                    if(m.getTo().hasPiece())
                    {
                        Piece d = m.getTo().getPiece();
                        
                        // If the piece on the destination square is the opposing color, 
                        // then the move is marked as a capture and added to the list.
                        if(p.isWhite != d.isWhite)
                        {
                            captures.add(m);
                        }
                        
                        // Regardless of whether this move is a capture or not, the piece can move no further in this direction.
                        break;
                    }
                    
                    moves.add(m);
                }
            }
        }
        else if(p.isKing() || p.isKnight())
        {
            // "Walkers": King, Knight
            // These pieces have set distances at which they can move and capture. I just need to evaluate each move's viability.
            
            // Need to call the canCastle method.
            
            for(Move m : baseMoves.get(0))
            {
                if(m.getTo().hasPiece())
                {
                    Piece d = m.getTo().getPiece();
                    
                    // If the piece on the destination square is the opposing color, 
                    // then the move is added to the list of captures.
                    if(p.isWhite != d.isWhite)
                    {
                        //m.setCapture(true);
                        captures.add(m);
                    }
                }
                else
                {
                    // Why is this adding it as a capture? Is that a mistake?
                    moves.add(m);
                }
            }
        }
        else if(p.isPawn())
        {
            // Pawns just kinda do their own thing
            
            Move m = baseMoves.get(0).get(0);
            
            // They can't capture forward.
            if(!m.getTo().hasPiece())
            {
                moves.add(m);
                
                // The double move was added sometime between 1200 and 1600 to speed up the early game.
                if(!p.hasMoved() && (baseMoves.get(0).size() == 2))
                {
                    m = baseMoves.get(0).get(1);
                    if(!m.getTo().hasPiece())
                    {
                        moves.add(m);
                    }
                }
            }
            
            // The captures.
            for(Move mm : baseMoves.get(1))
            {
                Square s = mm.getTo();
                if(s.hasPiece())
                {
                    if(p.isWhite != s.getPiece().isWhite)
                    {
                        captures.add(mm);
                    }
                }
            }
        }
        
        // Maybe I should handle castling down here.
        
        // There's a LOT of other rules associated with castling, but I'll put those in later.
        if(!p.hasMoved())
        {
            if(p.isRook() || p.isKing())
            {
                for(Move m : baseMoves.get(baseMoves.size() - 1))
                {
                    castles.add(m);
                }
                baseMoves.remove(0);
            }
        }
        
        // Go over the moves/captures again, figure out what their results are.
        if(getChecks)
        {
            ArrayList<Move> mm = new ArrayList<Move>();
            mm.addAll(moves);
            mm.addAll(captures);
            mm.addAll(castles);
            
            for(Move m : mm)
            {
                if(wouldCauseCheck(m, !p.isWhite))
                {
                    //moves.remove(m);
                    selfChecks.add(m);
                }
                else if(wouldCauseCheck(m, p.isWhite))
                {
                    //moves.remove(m);
                    checks.add(m);
                }
            }
        }
        
        ValidMoveList processedMoves = new ValidMoveList(moves, captures, checks, checkmates, selfChecks, castles);
        
        return processedMoves;
    }
    
    public ValidMoveList getValidMoves(Piece p)
    {
        return getValidMoves(p, true);
    }
    
    public ValidMoveList getValidMoves(Square s)
    {
        return getValidMoves(s.getPiece(), true);
    }
    
    public ValidMoveList getValidMoves(int x, int y)
    {
        return getValidMoves(board[x][y].getPiece(), true);
    }
    
    public Square getSquare(int x, int y)
    {
        return board[x][y];
    }
    
    public Piece getPiece(int x, int y)
    {
        return board[x][y].getPiece();
    }
    
    /**
     * Places a piece on the board based on the piece's internal x and y values.
     * 
     * Returns true if successful, false if not (e.g. if the requested square is occupied).
     * Maybe this should throw an exception or something.
     * 
     * (Somewhat deprecated)
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
        
        
        Piece f = m.getFrom().getPiece();
        Piece t = m.getTo().getPiece();
        
        if(f != null && t != null)
        {
            // If there's a piece on the TO space that's the same color, I.E. trying to take your own piece.
            if(t.isWhite == f.isWhite)
            {
                // Maybe this should throw some sort of InvalidMoveException?
                return false;
            }
            else
            {
                // Deal with the capturing of the piece here.
                // Make sure to remove the piece from its ArrayList.
                if(t.isWhite)
                {
                    this.whitePieces.remove(t);
                }
                else
                {
                    this.blackPieces.remove(t);
                }
            }
        }
        //else
        //{
            // Maybe this should throw some sort of InvalidMoveException?
        //    return false;
        //}
        
        //m.getFrom().removePiece();
        
        f.setSquare(m.getTo());
        
        //return placePiece(p);
        return true;
    }
    
    public boolean movePiece(int fX, int fY, int tX, int tY)
    //throws Exception
    {
        return movePiece(new Move(this, fX,fY, tX,tY));
    }
    
    public boolean movePiece(Square from, Square to)
    {
        return movePiece(new Move(from, to));
    }
    
    public boolean movePiece(Piece p, Square target)
    {
        return movePiece(p.getSquare(), target);
    }
    
    /**
     * Takes in a move for any board,
     * and returns an identical move on this board.
     */
    public Move cloneMove(Move m)
    {
        return new Move(this, m.from.x,m.from.y, m.to.x,m.to.y);
    }
    
    /**
     * Takes in a King, and checks every possible move in the other player's turn.
     * 
     * @returns true if the given King is in check.
     */
    public boolean isInCheck(King k)
    {
        ArrayList<Piece> enemyPieces = new ArrayList<Piece>();
        
        enemyPieces.addAll(getPieces(!k.isWhite));
        
        for(Piece p : enemyPieces)
        {
            ValidMoveList ml = getValidMoves(p, false);
            
            for(Move c : ml.getCaptures())
            {
                if(c.getTo().x == k.getX() && c.getTo().y == k.getY())
                {
                    //System.out.println("Check");
                    return true;
                }
            }
        }
        
        //System.out.println("No check");
        return false;
    }
    
    /**
     * Takes in a Move and a Piece's color (represented by a boolean).
     * Maybe I can refactor all this into an all-purpose method that 
     * returns an enum stating WHICH player is in check. The less 
     * clone-boards I have to create, the better.
     * 
     * @returns true if the Move would put the other player's King in check.
     */
    public boolean wouldCauseCheck(Move m, boolean isWhite)
    {
        Board copyBoard = this.clone();
        
        King k = copyBoard.getKing(!isWhite);
        
        Move cm = copyBoard.cloneMove(m);
        
        copyBoard.movePiece(cm);
        
        return copyBoard.isInCheck(k);
    }
    
    public boolean canCastle(King k, Rook r)
    {
        // I might need to make it so the method takes in two PIECES instead, 
        // and checks if they're a king and a rook here. This might avoid crashes.
        
        // If either piece has moved, or if they're not the same color.
        if(k.hasMoved() || r.hasMoved() || k.isWhite != r.isWhite)
            return false;
        
        // If other pieces are in the way. Not working?
        // Maybe do a thing with a modifier variable to process 
        // if the rook is to the left or right of the king.
        
        // I actually don't need to bother with that.
        // The for loops do it for me.
        
        for(int i = 3; i > r.getX(); i--)
        {
            Square s = board[i][k.getY()];
            
            if(s.getPiece() != null)
                return false;
                
            // I'm not creating an entire clone unless I have to.
            if(i > 1)
                if(wouldCauseCheck(new Move(k.getSquare(), s), k.isWhite))
                    return false;
        }
        
        for(int i = 5; i < r.getX(); i++)
        {
            Square s = board[i][k.getY()];
            
            if(board[i][k.getY()].getPiece() != null)
                return false;
                
            if(wouldCauseCheck(new Move(k.getSquare(), s), k.isWhite))
                return false;
        }
        
        // If King is in check
        if(isInCheck(k))
            return false;
        
        // If none of those were true
        return true;
    }
    
    /**
     * Maybe I can put the castling moves in their own little moveList.
     */
    public void castle(King k, Rook r)
    {
        if(k.hasMoved() || r.hasMoved())
        {
            // Throw exception?
        }
        
        
    }
    
    public ArrayList<Piece> getWhitePieces()
    {
        return this.whitePieces;
    }
    
    public ArrayList<Piece> getBlackPieces()
    {
        return this.blackPieces;
    }
    
    /**
     * Combined version of two older methods, getWhitePieces() and getBlackPieces().
     * 
     * @param isWhite    Whether the player is black or white.
     * @return           That player's pieces.
     */
    public ArrayList<Piece> getPieces(boolean isWhite)
    {
        if(isWhite)
            return this.whitePieces;
        
        return this.blackPieces;
    }
    
    public ArrayList<Piece> getAllPieces()
    {
        ArrayList<Piece> allPieces = new ArrayList<Piece>();
        allPieces.addAll(whitePieces);
        allPieces.addAll(blackPieces);
        
        return allPieces;
    }
    
    public King getWhiteKing()
    {
        return this.whiteKing;
    }
    
    public King getBlackKing()
    {
        return this.blackKing;
    }
    
    public King getKing(boolean isWhite)
    {
        if(isWhite)
            return this.whiteKing;
        else
            return this.blackKing;
    }
    
    /**
     * Gets an ArrayList of all rooks of a specified player.
     * Used with castling.
     * 
     * @param isWhite    Whether the player is black or white.
     * @return           That player's rooks.
     */
    public ArrayList<Rook> getRooks(boolean isWhite)
    {
        ArrayList<Rook> rooks = new ArrayList<Rook>();
        
        for(Piece p : getPieces(isWhite))
        {
            if(p.isRook())
            {
                Rook r = (Rook)p;
                rooks.add(r);
            }
        }
        
        return rooks;
    }
    
    //public boolean isThreatenedBy()
}
