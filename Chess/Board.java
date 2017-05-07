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
 * Surprisingly, this object emulates a chessboard, storing pieces' positions and handling their interactions.
 * 
 * @author Charles Howard
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
    
    /**
     * Tells you whose turn it is.
     * White = true
     * Black = false
     */
    private boolean whoseTurn;
    
    public boolean isClone = false;
    
    // If I'm going to have en passant capabilities, 
    // there should probably be a variable here that stores a pawn that just double moved
    
    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        for(int y = 0; y < 8; y++)
            for(int x = 0; x < 8; x++)
                board[x][y] = new Square(x,y, this);
    }
    
    /**
     * Sets up the board in the default starting state for chess.
     */
    public Board resetBoard()
    {
        // Maybe I can have two ArrayLists, one of each color of pieces.
        // I add all the pieces to their respective lists, then use a for/each
        // loop on each list to place the pieces on the board.
        
        whoseTurn = true;
        
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
            blackPieces.add(new Pawn(false, x,1));
        
        for(Piece p : blackPieces)
        {
            try
            {
                placePiece(p);
            }
            catch(InvalidMoveException e)
            {
            }
            
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
            whitePieces.add(new Pawn(true, x,6));
        
        for(Piece p : whitePieces)
        {
            try
            {
                placePiece(p);
            }
            catch(InvalidMoveException e)
            {
            }
            
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
        
        // Maybe I should make it go over the just the pieces instead of all the squares.
        for(int y = 0; y < 8; y++)
            for(int x = 0; x < 8; x++)
                if(this.getPiece(x,y) != null)
                {
                    Piece copyPiece = this.getPiece(x,y).clone();
                    
                    copy.getSquare(x,y).setPiece(copyPiece);
                    
                    copyPiece.setMoved(this.getPiece(x,y).hasMoved());
                    
                    //Add the piece to the ArrayLists
                    if(copyPiece.isWhite)
                    {
                        copy.whitePieces.add(copyPiece);
                        if(copyPiece instanceof King)
                            copy.whiteKing = (King)copyPiece;
                    }
                    else
                    {
                        copy.blackPieces.add(copyPiece);
                        if(copyPiece instanceof King)
                            copy.blackKing = (King)copyPiece;
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
                if(x < 7)
                    System.out.print("|");
            }
            
            System.out.println(y);
            if(y < 7)
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
        ArrayList<Move> stalemates = new ArrayList<Move>();
        
        ArrayList<ArrayList<Move>> baseMoves = p.getMoves(this);
        
        // There are several different "styles" of movement:
        if(p instanceof Queen || p instanceof Rook || p instanceof Bishop)
        {
            // "Runners": Queen, Rook, Bishop
            // These pieces can move and capture in a straight line of any distance, as long as that line is not interrupted by another piece (hostile or friendly).
            // I need to evaluate each line of moves and figure out if and where it gets interrupted, figure out if the interruption is an opportunity for a capture,
            // and invalidate the rest of the moves in that line.
            
            // Iterating through each direction of movement.
            for(int i = 0; i < baseMoves.size(); i++)
            {
                // Iterating through each square in that direction.
                for(int j = 0; j < baseMoves.get(i).size(); j++)
                {
                    Move m = baseMoves.get(i).get(j);
                    
                    if(m.getTo().hasPiece())
                    {
                        Piece d = m.getTo().getPiece();
                        
                        // If the piece on the destination square is the opposing color, 
                        // then the move is marked as a capture and added to the list.
                        if(p.isWhite != m.getTo().getPiece().isWhite)
                            captures.add(m);
                        
                        // Regardless of whether this move is a capture or not, the piece can move no further in this direction.
                        break;
                    }
                    
                    moves.add(m);
                }
            }
        }
        else if(p instanceof King || p instanceof Knight)
        {
            // "Walkers": King, Knight
            // These pieces have set distances at which they can move and capture. I just need to evaluate each move's viability.
            for(Move m : baseMoves.get(0))
                if(m.getTo().hasPiece())
                {
                    // I had to make this nested to dodge a NullPointerException.
                    if(p.isWhite != m.getTo().getPiece().isWhite)
                        captures.add(m);
                }
                else
                    moves.add(m);
        }
        else if(p instanceof Pawn)
        {
            // Pawns just kinda do their own thing
            
            Move m = baseMoves.get(0).get(0);
            
            // They can't capture forward.
            if(!m.getTo().hasPiece())
            {
                moves.add(m);
                
                // The double move was added sometime between the thirteenth and seventeenth century to speed up the early game.
                if(!p.hasMoved() && (baseMoves.get(0).size() == 2))
                {
                    m = baseMoves.get(0).get(1);
                    if(!m.getTo().hasPiece())
                        moves.add(m);
                }
            }
            
            // The captures.
            for(Move mm : baseMoves.get(1))
            {
                Square s = mm.getTo();
                
                if(s.hasPiece())
                    if(p.isWhite != s.getPiece().isWhite)
                        captures.add(mm);
            }
        }
        
        // Castling is complex enough to require several of its own methods.
        if(!p.hasMoved() && p instanceof King)
        {
            // I am SO GLAD that I learned about casting.
            King k = (King)p;
            if(k != null)
                for(Rook r : getRooks(k.isWhite))
                    if(canCastle(k, r))
                        if(r.getSquare().x == 0)
                            castles.add(new Move(k.getSquare(), this.board[2][k.getSquare().y], k, r));
                        else
                            castles.add(new Move(k.getSquare(), this.board[6][k.getSquare().y], k, r));
        }
        
        // Go over the moves/captures again, figure out what their results are.
        if(getChecks)
        {
            ArrayList<Move> mm = new ArrayList<Move>();
            mm.addAll(moves);
            mm.addAll(captures);
            mm.addAll(castles);
            
            for(Move m : mm)
                // Do mates here?
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
            
            // Or do mates here?
        }
        
        ValidMoveList processedMoves = new ValidMoveList(moves, captures, castles, checks, checkmates, selfChecks, stalemates);
        //processedMoves.removeDuplicates();
        
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
    
    /**
     * Gets every possible move of every piece a player has.
     * 
     * @param isWhite   Whether the player is black or white.
     * @return          ValidMoveList containing all of that player's possible moves.
     */
    public ValidMoveList getAllValidMoves(boolean isWhite)
    {
        ValidMoveList allValidMoves = new ValidMoveList();
        
        for(Piece p : this.getPieces(isWhite))
        {
            allValidMoves.combineWith(this.getValidMoves(p));
        }
        
        return allValidMoves;
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
     * (Somewhat deprecated)
     * Places a piece on the board based on the piece's internal x and y values.
     * 
     * @return          true if successful, false if not (e.g. if the requested square is occupied).
     */
    public boolean placePiece(Piece p)
    throws InvalidMoveException
    {
        int x = p.getX();
        int y = p.getY();
        
        // Making sure that the requested square is actually ON the board.
        if(x < 0 || y < 0 || x > 7 || y > 7)
            throw new InvalidMoveException("" + x + "," + y + " is not a valid square on the board.");
        
        // Making sure that the requested square is unoccupied.
        if(board[x][y].hasPiece())
            throw new InvalidMoveException("" + x + "," + y + " is currently occupied.");
        
        board[x][y].setPiece(p);
        
        return true;
    }
    
    /**
     * Move a piece from one square to another. Also changes whoseTurn. 
     * 
     * @return          true if successful (possibly unnecessary?).
     */
    public boolean movePiece(Move m)
    throws InvalidMoveException
    {
        if(m.castlingKing != null && m.castlingRook != null)
            return castle(m.castlingKing, m.castlingRook);
        
        Piece f = m.getFrom().getPiece();
        Piece t = m.getTo().getPiece();
        
        if(f == null)
            throw new InvalidMoveException("There is no piece on Square " + m.getFrom().x + "," + m.getFrom().y + ".");
        else if(t != null)
            if(t.isWhite == f.isWhite) // If there's a piece on the TO space that's the same color, I.E. trying to take your own piece.
            {
                throw new InvalidMoveException("Cannot capture Piece " + t + " with Piece " + f + ", as they are the same color.");
            }
            else // Dealing with the capturing of the piece.
            {
                if(t.isWhite) // Removing the piece from its ArrayList.
                    this.whitePieces.remove(t);
                else
                    this.blackPieces.remove(t);
            }
        
        
        m.getFrom().removePiece();
        f.setSquare(m.getTo());
        
        // I could change the whoseTurn variable here.
        this.whoseTurn = !this.whoseTurn;
        
        //return placePiece(p);
        return true;
    }
    
    // Do I actually use any of these methods?
    public boolean movePiece(int fX, int fY, int tX, int tY)
    throws InvalidMoveException
    {
        return movePiece(new Move(this, fX,fY, tX,tY));
    }
    
    public boolean movePiece(Square from, Square to)
    throws InvalidMoveException
    {
        return movePiece(new Move(from, to));
    }
    
    public boolean movePiece(Piece p, Square target)
    throws InvalidMoveException
    {
        return movePiece(p.getSquare(), target);
    }
    
    /**
     * Takes in a move for any board,
     * and returns an identical move on this board.
     */
    public Move cloneMove(Move m)
    {
        if(m.castlingKing != null && m.castlingRook != null)
        {
            // Need to figure out how to get the corresponding pieces.
            
            King k = this.getKing(m.castlingKing.isWhite);
            Rook r = (Rook)this.getPiece(m.castlingRook.getSquare().x, m.castlingRook.getSquare().y);
            
            return new Move(this, m.from.x,m.from.y, m.to.x,m.to.y, k,r);
        }
        
        return new Move(this, m.from.x,m.from.y, m.to.x,m.to.y);
    }
    
    /**
     * Takes in a King, and checks every possible move in the other player's turn.
     * 
     * @returns true if the given King is in check.
     */
    public boolean isInCheck(King k)
    {
        for(Piece p : this.getPieces(!k.isWhite))
        {
            ValidMoveList ml = this.getValidMoves(p, false);
            
            for(Move c : ml.getCaptures())
                if(c.getTo().x == k.getX() && c.getTo().y == k.getY())
                    return true;
        }
        
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
        
        try
        {
            copyBoard.movePiece(cm);
        }
        catch(InvalidMoveException e)
        {
            return false;
        }
        
        return copyBoard.isInCheck(k);
    }
    
    /**
     * Figures out if the given player is in checkmate or stalemate.
     * 
     * @param isWhite    Whether the player is black or white.
     * @return           1 if Checkmate
     * @return          -1 if Stalemate.
     * @return           0 if Neither.
     */
    public int isMate(boolean isWhite)
    {
        // Not sure I need this here. I've got enough decomposition that I might not.
        King k = this.getKing(isWhite);
        
        // True if the player is currently in check.
        boolean check = false;
        
        // True if the player has no moves that won't RESULT in check.
        boolean mate = true;
        
        // If it's not in check, it obviously can't be in checkmate.
        check = this.isInCheck(k);
        
        // Gotta get getAllMoves() working.
        // If I use a break as soon as it finds a "safe" move,
        // that might make it more efficient.
        for(Move m : this.getAllValidMoves(isWhite).getAll())
        {
            if(!this.wouldCauseCheck(m, isWhite))
            {
                mate = false;
                break;
            }
        }
        
        
        if(check && mate)
            return 1;
        else if(!check && mate)
            return -1;
        else
            return 0;
    }
    
    public int wouldCauseMate(Move m, boolean isWhite)
    {
        Board copyBoard = this.clone();
        
        //King k = copyBoard.getKing(!isWhite);
        
        Move cm = copyBoard.cloneMove(m);
        
        try
        {
            copyBoard.movePiece(cm);
        }
        catch(InvalidMoveException e)
        {
            return 0;
        }
        
        return copyBoard.isMate(isWhite);
    }
    
    public boolean canCastle(King k, Rook r)
    {
        // If either piece has moved, or if they're not the same color.
        if(k.hasMoved() || r.hasMoved() || k.isWhite != r.isWhite)
            return false;
        
        // If other pieces are in the way/if any of the King's squares would be check.
        // The for loops themselves actually figure out which Rook it is. 
        for(int i = 3; i > r.getX(); i--)
        {
            Square s = board[i][k.getY()];
            
            if(s.getPiece() != null)
                return false;
                
            // I'm not creating an entire clone unless I have to.
            if(i > 1)
                if(wouldCauseCheck(new Move(k.getSquare(), s), !k.isWhite))
                    return false;
        }
        for(int i = 5; i < r.getX(); i++)
        {
            Square s = board[i][k.getY()];
            
            if(board[i][k.getY()].getPiece() != null)
                return false;
                
            if(wouldCauseCheck(new Move(k.getSquare(), s), !k.isWhite))
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
    public boolean castle(King k, Rook r)
    throws InvalidMoveException
    {
        if(k.hasMoved())
            throw new InvalidMoveException("King has moved.");
        else if(r.hasMoved())
            throw new InvalidMoveException("Rook has moved.");
        
        if(r.getSquare().x == 0)
        {
            movePiece(k, this.board[2][k.getSquare().y]);
            movePiece(r, this.board[3][k.getSquare().y]);
            // I could change the whoseTurn variable here.
            this.whoseTurn = !this.whoseTurn;
            return true;
        }
        else if(r.getSquare().x == 7)
        {
            movePiece(k, this.board[6][k.getSquare().y]);
            movePiece(r, this.board[5][k.getSquare().y]);
            // I could change the whoseTurn variable here.
            this.whoseTurn = !this.whoseTurn;
            return true;
        }
        
        return false;
    }
    
    /**
     * Combined version of two older methods, getWhitePieces() and getBlackPieces().
     * 
     * @param isWhite    Whether the player is black or white.
     * @return           ArrayList containing that player's pieces.
     */
    public ArrayList<Piece> getPieces(boolean isWhite)
    {
        if(isWhite)
            return this.whitePieces;
        
        return this.blackPieces;
    }
    
    /**
     * Quick way to get all of the pieces.
     * 
     * @return           ArrayList containing all pieces.
     */
    public ArrayList<Piece> getAllPieces()
    {
        ArrayList<Piece> allPieces = new ArrayList<Piece>();
        allPieces.addAll(whitePieces);
        allPieces.addAll(blackPieces);
        
        return allPieces;
    }
    
    /**
     * Combined version of two older methods, getWhiteKing() and getBlackKing().
     * 
     * @param isWhite    Whether the player is black or white.
     * @return           That player's King.
     */
    public King getKing(boolean isWhite)
    //throws InvalidMoveException
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
            if(p instanceof Rook)
            {
                Rook r = (Rook)p;
                rooks.add(r);
            }
        
        return rooks;
    }
    
    //public boolean isThreatenedBy()
    
    public boolean isWhoseTurn()
    {
        return whoseTurn;
    }
}
