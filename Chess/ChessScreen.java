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
        
        //SquareActor sa = new SquareActor(board.getSquare(4,4), mainStage);
        //sa.setPosition(300, 300);
        
        //Pawn pawn = new Pawn(true, 4,4);
        //pawn.setActor(new PieceActor(pawn, mainStage));
        //board.placePiece(pawn);
        
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
            
            //Something to figure out if the piece was dropped on a valid target, 
            //and then correct its position onto the target.
            
            
            
            
            if(s != null)
            {
                dropped.piece.setSquare(s.square);
                //dropped.moveToActor(s);
            }
            
            for(BaseActor b : BaseActor.getList("PieceActor"))
            {
                PieceActor c = (PieceActor)b;
                //c.moveToActor(c.piece.getSquare().getActor());
                
                c.alignToActorCenter(c.piece.getSquare().getActor());
            }
            
            dropped.setDropped(false);
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
        //BaseActor b = (BaseActor)mainStage.hit(screenX,screenY, true);
        
        //if(b != null)
        //{
        //    s.select();
        //}
        
        return false;
    }
}
