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

public class ChessScreen extends BaseScreen
{
    Board board;
    Table boardTable;
    
    public ChessScreen()
    {
        super();
    }
    
    public void create() 
    {
        board = new Board();
        
        board.resetBoard();
        
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
        
        
        for(Piece p : board.getWhitePieces())
        {
            p.setActor(new PieceActor(p, mainStage));
        }
        
        for(Piece p : board.getBlackPieces())
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
    }
    
    public void update(float dt) 
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
            
            // Finds each piece 
            for(BaseActor b : BaseActor.getList("PieceActor"))
            {
                PieceActor c = (PieceActor)b;
                c.moveToActor(c.piece.getSquare().getActor());
                
                // Should I remove captured pieces here?
                if(!(board.getWhitePieces().contains(c.piece) || board.getBlackPieces().contains(c.piece)))
                {
                    //c.remove();
                }
                
                //c.alignToActorCenter(c.piece.getSquare().getActor());
            }
            
            dropped.setDropped(false);
        }
    }
    
    /**
     * Performs a move without the player's input.
     */
    public void performMove(Move m)
    {
        
    }
}
