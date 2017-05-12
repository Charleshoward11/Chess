import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.*;
import java.util.ArrayList;
import java.util.HashMap;

// Shouldn't these have been imported already?
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ChessScreenVsAI extends BaseScreen
{
    Board board;
    Table boardTable;
    
    boolean playerIsWhite;
    
    boolean gameOver;
    
    BaseActor checkmateText;
    BaseActor stalemateText;
    
    ChessAI chester;
    
    public ChessScreenVsAI(Game g)
    {
        super(g);
    }
    
    public void create() 
    {
        board = new Board().resetBoard();
        
        playerIsWhite = true;
        
        boardTable = new Table();
        boardTable.setFillParent(true);
        boardTable.defaults().width(72).height(72);
        
        mainStage.addActor(boardTable);
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                SquareActor s = new SquareActor(board.getSquare(x,y), mainStage);
                
                boardTable.add(s);
            }
            boardTable.row();
        }
        
        
        for(Piece p : board.getPieces(true))
        {
            p.setActor(new PieceActor(p, mainStage));
        }
        
        for(Piece p : board.getPieces(false))
        {
            p.setActor(new PieceActor(p, mainStage));
        }
        
        // I can't believe this worked.
        render(0f);
        
        for(BaseActor b : DragAndDropActor.getList("SquareActor"))
        {
            SquareActor s = (SquareActor)b;
            
            if(s.square.getPiece() != null)
            {
                PieceActor p = s.square.getPiece().getActor();
                
                p.setSize(s.getWidth(),s.getHeight());
                
                p.alignToActorCenter(s);
            }
        }
        
        checkmateText = new BaseActor(0,0, uiStage);
        checkmateText.loadTexture("assets/Checkmate2.png");
        checkmateText.alignToActorCenter(uiTable);
        checkmateText.toFront();
        checkmateText.setVisible(false);
        
        stalemateText = new BaseActor(0,0, uiStage);
        stalemateText.loadTexture("assets/Stalemate2.png");
        stalemateText.alignToActorCenter(uiTable);
        stalemateText.toFront();
        stalemateText.setVisible(false);
        
        gameOver = false;
        
        // Create the AI
        chester = new ChessAI(2, !playerIsWhite, board);
    }
    
    public void update(float dt) 
    {
        if(board.isWhoseTurn() == playerIsWhite)
        {
            PieceActor dropped = null;
            
            for(BaseActor b : BaseActor.getList("PieceActor"))
            {
                PieceActor c = (PieceActor)b;
                if(c.isDropped())
                    dropped = c;
            }
            
            // Was something dropped?
            if(dropped != null)
            {
                SquareActor s = (SquareActor)dropped.getDropTarget();
                
                //System.out.println(p.name);
                
                if(s != null)
                {
                    dropped.piece.setSquare(s.square);
                    //dropped.moveToActor(s);
                }
                
                movePieceActors();
                
                dropped.setDropped(false);
                
                if(board.checkmate && !gameOver)
                {
                    checkmateText.setVisible(true);
                    gameOver = true;
                    // Could have it also say "You win" here.
                }
                else if(board.stalemate && !gameOver)
                {
                    stalemateText.setVisible(true);
                    gameOver = true;
                }
            }
        }
        else
        {
            // Get the AI's move and perform it.
            performMove(chester.decideMove());
            movePieceActors();
            
            // Then check if the board's in checkmate
            if(board.checkmate && !gameOver)
            {
                checkmateText.setVisible(true);
                gameOver = true;
                // Could have it also say "You win" here.
            }
            else if(board.stalemate && !gameOver)
            {
                stalemateText.setVisible(true);
                gameOver = true;
            }
        }
    }
    
    /**
     * Performs a move without the player's input.
     */
    public void performMove(Move m)
    {
        // Make captured piece fade?
        
        try
        {
            Thread.sleep(300);
        }
        catch(Exception e)
        {
        }
        
        if(m.to.hasPiece())
            m.to.getPiece().getActor().addAction(Actions.sequence(
            Actions.fadeOut(0.05f), Actions.removeActor()));
        
        try 
        {
            board.movePiece(m);
        }
        catch(InvalidMoveException e)
        {
        }
        
        movePieceActors();
    }
    
    public void movePieceActors()
    {
        // Finds each piece and moves it to where it should be.
        for(BaseActor b : BaseActor.getList("PieceActor"))
        {
            PieceActor c = (PieceActor)b;
            c.moveToActor(c.piece.getSquare().getActor());
            
            // Should I remove captured pieces here?
            if(!(board.getAllPieces().contains(c.piece)))
            {
                c.remove();
            }
            
            //c.alignToActorCenter(c.piece.getSquare().getActor());
        }
    }
}