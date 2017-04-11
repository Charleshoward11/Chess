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
        
        //Texture texture = new Texture( Gdx.files.internal(fileName));
        //texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        // Need to figure out how to make drag & drop.
        DragAndDrop drag = new DragAndDrop();
        
        for(Piece p : board.getWhitePieces())
        {
            p.setActor(new PieceActor(p, mainStage));
        }
        
        for(Piece p : board.getBlackPieces())
        {
            p.setActor(new PieceActor(p, mainStage));
        }
        
        for(int y = 0; y < 8; y++)
        {
            for(int x = 0; x < 8; x++)
            {
                SquareActor s = new SquareActor(board.getSquare(x,y), mainStage);
                
                boardTable.add(s);
            }
            boardTable.row();
        }
        
        
    }
    
    public void update(float dt) 
    {
        PieceActor droppedPiece = null;
        
        for(BaseActor b : BaseActor.getList("PieceActor"))
        {
            PieceActor c = (PieceActor)b;
            if(c.isDropped())
                droppedPiece = c;
        }
        
        // was something dropped? and did it land on a target?
        if(droppedPiece != null && droppedPiece.getDropTarget() != null)
        {
            SquareActor p = (SquareActor)droppedPiece.getDropTarget();
            
            //System.out.println(p.name);
            
            droppedPiece.setDropped(false);
        }
        for(BaseActor b : BaseActor.getList("SquareActor"))
        {
            SquareActor s = (SquareActor)b;
            
            if(s.square.getPiece() != null)
            {
                PieceActor p = s.square.getPiece().getActor();
                
                p.setSize(s.getWidth(),s.getHeight());
                
                p.alignToActorCenter(s);
            }
        }
        
        BaseActor.updateAllLists();
    }
    
    // handle discrete input
    public boolean keyDown(int keycode)
    {
        if(keycode == Keys.SPACE)
        {
            
        }
        
        return false;
    }
    
    public boolean mouseMoved(int screenX, int screenY)
    {
        //BaseActor a = mainStage.hit(screenX,screenY, true);
        
        //if(a != null)
        //{
        //}
        
        return true;
    }
    
    
    
    
}
