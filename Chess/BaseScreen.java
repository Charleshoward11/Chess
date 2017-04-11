import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.*;

public abstract class BaseScreen implements Screen, InputProcessor
{
    protected Stage mainStage;
    protected Stage uiStage;
    
    // Add UI elements to Table (not Stage) for organization
    protected Table uiTable;
    
    public BaseScreen()
    {
        mainStage = new Stage(new FitViewport(640,640));
        uiStage   = new Stage(new FitViewport(640,640));
        
        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);
        
        // discrete events can be handled by different objects
        InputMultiplexer im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor(im);
        
        BaseActor.clearAllLists();
        
        create();
    }
    
    public abstract void create();
    
    public abstract void update(float dt);

    // Gameloop:
    // (1) process input (discrete handled by listener; continuous in update)
    // (2) update game logic
    // (3) render the graphics
    public void render(float dt) 
    {
        // act methods
        uiStage.act(dt);
        mainStage.act(dt);
        
        // defined by user
        update(dt);
        
        BaseActor.updateAllLists();
        
        // clear the screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // draw the graphics
        mainStage.draw();
        uiStage.draw();
    }
    
    // methods required by Screen interface
    public void resize(int width, int height) 
    {    
        mainStage.getViewport().update(width, height, true); 
        uiStage.getViewport().update(width, height, true);
    }
    
    public void pause(){}
    public void resume(){}
    public void dispose(){}
    public void show(){}
    public void hide(){}
    
    // methods required by InputProcessor interface
    public boolean keyDown(int keycode)
    {return false;}
    
    public boolean keyUp(int keycode)
    {return false;}
    
    public boolean keyTyped(char c)
    {return false;}
    
    public boolean mouseMoved(int screenX, int screenY)
    {return false;}
    
    public boolean scrolled(int amount)
    {return false;}
    
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {return false;}
    
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {return false;}
    
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {return false;}
}