import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Intersector.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;

/**
 * A wrapper (I think that's the correct term) for the Piece class, 
 * and by extension all the actual piece classes.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PieceActor extends DragAndDropActor
{
    public final Piece piece;
    
    public PieceActor(Piece p, Stage s)
    {
        super(0,0, s, p.getBoard());
        
        piece = p.setActor(this);
        
        if(piece.isWhite)
        {
            if(piece instanceof Pawn)
                loadTexture("assets/Pawn-White.png");
            else if(piece instanceof Bishop)
                loadTexture("assets/Bishop-White.png");
            else if(piece instanceof Knight)
                loadTexture("assets/Knight-White.png");
            else if(piece instanceof Rook)
                loadTexture("assets/Rook-White.png");
            else if(piece instanceof Queen)
                loadTexture("assets/Queen-White.png");
            else if(piece instanceof King)
                loadTexture("assets/King-White.png");
        }
        else
        {
            if(piece instanceof Pawn)
                loadTexture("assets/Pawn-Black.png");
            else if(piece instanceof Bishop)
                loadTexture("assets/Bishop-Black.png");
            else if(piece instanceof Knight)
                loadTexture("assets/Knight-Black.png");
            else if(piece instanceof Rook)
                loadTexture("assets/Rook-Black.png");
            else if(piece instanceof Queen)
                loadTexture("assets/Queen-Black.png");
            else if(piece instanceof King)
                loadTexture("assets/King-Black.png");
        }
        
        setDraggable(true);
        setTargetable(true);
        
        //s.addActor(this);
    }
    
    /**
     * Since when has this been here? Do I even need it?
     */
    public void draw(Batch batch, float parentAlpha) 
    {

        // create a drop shadow effect:
        //   set tint color to translucent black
        batch.setColor(0,0,0, 0.5f);

        // ranges from 1.00 to 1.25
        float scale = this.getScaleX();
        
        // ranges from 0 to 1
        float percent = 4 * scale - 4;
        
        batch.draw(animation.getKeyFrame(elapsedTime), 
            getX() + 10 * percent, getY() - 10 * percent, getOriginX(), getOriginY(),
            getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        super.draw(batch, parentAlpha);

        /*
        Color c = getColor(); // used to apply tint color effect

        batch.setColor(c.r, c.g, c.b, c.a);

        if (animation != null && isVisible())
        batch.draw(animation.getKeyFrame(elapsedTime), 
        getX(), getY(), getOriginX(), getOriginY(),
        getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
         */
    }
    
    public void act(float dt)
    {
        super.act(dt);
    }
}
