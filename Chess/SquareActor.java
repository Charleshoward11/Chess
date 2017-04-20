import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * Actor that contains a square.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SquareActor extends DragAndDropActor
{
    public final Square square;
    
    private boolean isM;
    private boolean isC;
    
    /**
     * If the sum of the square's x and y coordinates is even, it's white.
     * Otherwise, it's gray.
     */
    public SquareActor(Square p, Stage s)
    {
        super(0,0, s, p.b);
        
        this.square = p.setActor(this);
        
        if((this.square.x + this.square.y) % 2 == 0)
            loadTexture("assets/Square-White.png");
        else
            loadTexture("assets/Square-Gray.png");
        
        /*
        loadTexture("assets/Square-White.png");
        
        if((this.square.x + this.square.y) % 2 == 0)
        {
            setColor(new Color(0.8f, 0.8f, 0.8f, 1));
        }
        else
        {
            setColor(new Color(0.6f, 0.6f, 0.6f, 1));
        }
         */
        
        setDraggable(false);
        setTargetable(true);
        
        isM = false;
        isC = false;
        
        s.addActor(this);
    }
    
    public boolean isMove()
    {
        return isM;
    }
    
    public boolean isCapture()
    {
        return isC;
    }
    
    public void setMove()
    {
        isM = true;
        isC = false;
        
        this.addAction(Actions.color(new Color(1.0f, 0.5f, 0.5f, 1), 0.1f));
    }
    
    public void setCapture()
    {
        isM = false;
        isC = true;
        
        this.addAction(Actions.color(new Color(1.0f, 1.0f, 0.5f, 1), 0.1f));
    }
    
    public void setBlank()
    {
        isM = false;
        isC = false;
        
        this.addAction(Actions.color(new Color(1.0f, 1.0f, 1.0f, 1), 0.1f));
    }
    
    /**
     * I'm not sure it's efficient to load from the file each time...
     */
    public void select()
    {
        if((this.square.x + this.square.y) % 2 == 0)
            loadTexture("assets/Square-White-Select.png");
        else
            loadTexture("assets/Square-Gray-Select.png");
    }
    
    public void deselect()
    {
        
        
    }
    
    public void act(float dt)
    {
        super.act(dt);
    }
}