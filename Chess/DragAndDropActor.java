import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *  Enables drag and drop functionality for actors.
 *  
 */
public class DragAndDropActor extends BaseActor
{
    private float grabOffsetX;
    private float grabOffsetY;
    
    private Float startPositionX;
    private Float startPositionY;
    
    private boolean draggable;
    private boolean dropped;
    private boolean targetable;
    private DragAndDropActor dropTarget;
    
    /**
     *  Set whether this actor can be dragged.
     */
    public void setDraggable(boolean b)
    {
        draggable = b;
    }
    
    /**
     *  Check if this actor can be dragged.
     */
    public boolean isDraggable()
    {
        return draggable;
    }
    
    /**
     *  Automatically set when actor is dropped; manually set to false after processing information.
     */
    public void setDropped(boolean b)
    {
        dropped = b;
    }
    
    /**
     *  Check if this actor was dropped.
     */
    public boolean isDropped()
    {
        return dropped;
    }
    
    /**
     *  Set whether this actor is considered as a "target" and will be stored if another actor is dropped on it.
     */
    public void setTargetable(boolean b)
    {
        targetable = b;
    }
    
    /**
     *  Check if this actor is considered a "target".
     */
    public boolean isTargetable()
    {
        return targetable;
    }
    
    /**
     *  Automatically set when actor is dropped on a target; manually set to null after processing information.
     */
    public void setDropTarget(DragAndDropActor a)
    {
        dropTarget = a;
    }
    
    /**
     *  If this actor is dropped on a "targetable" actor, that actor can be obtained from this method.
     */
    public DragAndDropActor getDropTarget() 
    {
        return dropTarget;
    }
    
    public DragAndDropActor(float x, float y, Stage s)
    {
        super(x,y,s);
        
        grabOffsetX = 0;
        grabOffsetY = 0;
        startPositionX = getX();
        startPositionY = getY();
        
        draggable = true;
        dropped = false;
        targetable = true;
        dropTarget = null;
        
        addListener(
            new InputListener() 
            {
                public DragAndDropActor self;
                public boolean touchDown(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer, int button) 
                {  
                    self = (DragAndDropActor)event.getListenerActor();
                    
                    if(!self.isDraggable())
                        return false;
                        
                    self.grabOffsetX = eventOffsetX;
                    self.grabOffsetY = eventOffsetY;
                    
                    // store original position to return to later
                    self.startPositionX = self.getX();
                    self.startPositionY = self.getY();
                    
                    self.toFront();
                    
                    return true; // returning true indicates other touch methods are called
                }
                
                // touchDragged: touchDown && mouseMoved && !touchUp
                public void touchDragged(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer) 
                {
                    float deltaX = eventOffsetX - self.grabOffsetX;
                    float deltaY = eventOffsetY - self.grabOffsetY;
                    
                    self.moveBy(deltaX, deltaY);
                }
                
                public void touchUp(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer, int button) 
                {
                    self.setDropped(true);
                    
                    // determine if self is dropped on a target, and if so, store reference to target.
                    // if self is dropped on a multiple targets, select target "on top"
                    DragAndDropActor target = null;
                    
                    for(Actor otherA : self.getStage().getActors())
                    {
                        if(!(otherA instanceof DragAndDropActor))
                            continue;
                        
                        DragAndDropActor other = (DragAndDropActor)otherA;
                        
                        if(!self.equals(other) && other.isTargetable() && self.overlaps(other))
                        {
                            if(target == null)
                                target = other;
                            else if(other.getZIndex() > target.getZIndex())
                                target = other;
                        }
                    }
                    
                    self.setDropTarget(target);
                    
                    // for debugging:
                    // System.out.println(self + " dropped on " + target);
                }
                
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor)
                {
                    //fromActor.select();
                }
                
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
                {
                    //toActor.deselect();
                }
            }
        );
    }
    
    public void moveToActor(BaseActor other)
    {
        addAction(Actions.moveTo(other.getX(), other.getY(), 0.50f, Interpolation.pow3));
    }
    
    public void moveToStart()
    {
        addAction(Actions.moveTo(startPositionX, startPositionY, 0.50f, Interpolation.pow3));
        startPositionX = null;
        startPositionY = null;
    }
    
    public void act(float dt)
    {
        super.act(dt);
    }
}