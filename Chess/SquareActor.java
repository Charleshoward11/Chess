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
 * When you pick up a piece, the available moves 
 * get highlighted depending on their results:
 * 
 * @author Charles Howard
 * @version (a version number or a date)
 */
public class SquareActor extends DragAndDropActor
{
    public final Square square;
    
    /**
     * Normal moves are red
     * Captures are green
     * Check is purple?
     * Checkmate is gold?
     * Self-check is black
     * Castle is blue
     * Stalemate is brown
     */
    private enum Status 
    {
        BLANK, MOVE, CAPTURE, CHECK, CHECKMATE, SELFCHECK, CASTLE, STALEMATE
    }
    
    private Status status;
    
    private boolean move;
    private boolean capture;
    private boolean check;
    private boolean checkMate;
    private boolean selfCheck;
    private boolean castle;
    
    /**
     * If the sum of the square's x and y coordinates is even, it's white.
     * Otherwise, it's gray.
     */
    public SquareActor(Square p, Stage s)
    {
        super(0,0, s, p.board);
        
        this.square = p.setActor(this);
        
        if((this.square.x + this.square.y) % 2 == 0)
            loadTexture("assets/Square-White.png");
        else
            loadTexture("assets/Square-Gray.png");
        
        setDraggable(false);
        setTargetable(true);
        
        status = Status.BLANK;
        
        s.addActor(this);
    }
    
    public boolean isBlank()
    {
        return status == Status.BLANK;
    }
    
    public boolean isMove()
    {
        return status == Status.MOVE;
    }
    
    public boolean isCapture()
    {
        return status == Status.CAPTURE;
    }
    
    public boolean isCheck()
    {
        return status == Status.CHECK;
    }
    
    public boolean isCheckmate()
    {
        return status == Status.CHECKMATE;
    }
    
    public boolean isSelfCheck()
    {
        return status == Status.SELFCHECK;
    }
    
    public boolean isCastle()
    {
        return status == Status.CASTLE;
    }
    
    public boolean isStalemate()
    {
        return status == Status.STALEMATE;
    }
    
    public Status getStatus()
    {
        return this.status;
    }
    
    public SquareActor setBlank()
    {
        status = Status.BLANK;
        
        this.addAction(Actions.color(Color.WHITE, 0.1f));
        
        return this;
    }
    
    public SquareActor setMove()
    {
        status = Status.MOVE;
        
        this.addAction(Actions.color(Color.RED, 0.1f));
        
        //this.addAction(Actions.color(Color.FOREST, 0.1f));
        
        return this;
    }
    
    public SquareActor setCapture()
    {
        status = Status.CAPTURE;
        
        this.addAction(Actions.color(Color.MAROON, 0.1f));
        
        return this;
    }
    
    public SquareActor setCheck()
    {
        status = Status.CHECK;
        
        this.addAction(Actions.color(Color.FOREST, 0.1f));
        
        return this;
    }
    
    public SquareActor setCheckmate()
    {
        status = Status.CHECKMATE;
        
        this.addAction(Actions.color(Color.GOLDENROD, 0.1f));
        
        return this;
    }
    
    public SquareActor setSelfCheck()
    {
        status = Status.SELFCHECK;
        
        this.addAction(Actions.color(Color.LIGHT_GRAY, 0.1f));
        
        return this;
    }
    
    public SquareActor setCastle()
    {
        status = Status.CASTLE;
        
        this.addAction(Actions.color(Color.BLUE, 0.1f));
        
        return this;
    }
    
    public SquareActor setStalemate()
    {
        status = Status.STALEMATE;
        
        this.addAction(Actions.color(Color.BROWN, 0.1f));
        
        return this;
    }
    
    public void act(float dt)
    {
        super.act(dt);
    }
}