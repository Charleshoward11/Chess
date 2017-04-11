import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import com.badlogic.gdx.Input.*;

/**
 * Actor that contains a square.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SquareActor extends DragAndDropActor
{
    public final Square square;
    
    /**
     * If the sum of the square's x and y coordinates is even, it's white.
     * Otherwise, it's gray.
     */
    public SquareActor(Square p, Stage s)
    {
        super(0,0, s);
        
        this.square = p.setActor(this);
        
        if((this.square.x + this.square.y) % 2 == 0)
            loadTexture("assets/Square-White.png");
        else
            loadTexture("assets/Square-Gray.png");
        
        setDraggable(false);
        
        s.addActor(this);
    }
    
    public void highlight()
    {
        //Set color or something...
    }
    
    /**
     * I'm not sure it's efficient to load from the file each time...
     */
    public void select()
    {
        /*
        if((this.square.x + this.square.y) % 2 == 0)
            loadTexture("assets/Square-White-Select.png");
        else
            loadTexture("assets/Square-Gray-Select.png");
         */
        
        
    }
    
    public void deselect()
    {
        /*
        if((this.square.x + this.square.y) % 2 == 0)
            loadTexture("assets/Square-White.png");
        else
            loadTexture("assets/Square-Gray.png");
         */
        
        
    }
    
    public void act(float dt)
    {
        
    }
}