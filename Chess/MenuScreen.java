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

public class MenuScreen extends BaseScreen
{
    public MenuScreen(Game g)
    {
        super(g);
    }
    
    public void create()
    {
        //BaseActor background = new BaseActor(0,0,mainStage);
        //background.loadTexture("assets/space.png");
        
        Board board = new Board();
        
        Table boardTable = new Table();
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
        
        BaseActor titleText = new BaseActor(0,0, mainStage);
        titleText.loadTexture("assets/title.png");
        
        TextButton pvpButton = new TextButton("Player VS Player", BaseGame.skin, "uiTextButtonStyle");
        pvpButton.addListener(new InputListener()
            {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    game.setScreen(new ChessScreenVsPlayer(game));
                    return false; // continue processing?
                }
            }
        );
        
        TextButton pvcButton = new TextButton("Player VS AI", BaseGame.skin, "uiTextButtonStyle");
        pvcButton.addListener(new InputListener()
            {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
                {
                    game.setScreen(new ChessScreenVsAI(game));
                    return false; // continue processing?
                }
            }
        );
        
        BitmapFont font = new BitmapFont();
        //instructions.setPosition(100, 50);
        //uiStage.addActor(instructions);
        
        float w1 = pvpButton.getWidth();
        float w2 = pvpButton.getWidth();
        
        uiTable.add(titleText).colspan(2);
        uiTable.row();
        uiTable.add(pvpButton).width(w1);
        uiTable.row();
        uiTable.add(pvcButton).width(w2);
        uiTable.row();
    }
    
    // must be included, even if blank
    public void update(float dt)
    {
    }
    
    // InputProcessor methods for handling discrete input
    public boolean keyDown(int keycode)
    {
        if(keycode == Keys.P)
        {
            game.setScreen(new ChessScreenVsPlayer(game));
        }
        
        if(keycode == Keys.C)
        {
            game.setScreen(new ChessScreenVsAI(game));
        }
        
        return false;
    }
}