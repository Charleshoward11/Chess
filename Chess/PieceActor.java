import com.badlogic.gdx.scenes.scene2d.Stage;

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
        super(0,0, s);
        
        piece = p.setActor(this);
        
        if(p.isWhite)
        {
            if(p.isPawn())
                loadTexture("assets/Pawn-White.png");
            else if(p.isBishop())
                loadTexture("assets/Bishop-White.png");
            else if(p.isKnight())
                loadTexture("assets/Knight-White.png");
            else if(p.isRook())
                loadTexture("assets/Rook-White.png");
            else if(p.isQueen())
                loadTexture("assets/Queen-White.png");
            else if(p.isKing())
                loadTexture("assets/King-White.png");
        }
        else
        {
            if(p.isPawn())
                loadTexture("assets/Pawn-Black.png");
            else if(p.isBishop())
                loadTexture("assets/Bishop-Black.png");
            else if(p.isKnight())
                loadTexture("assets/Knight-Black.png");
            else if(p.isRook())
                loadTexture("assets/Rook-Black.png");
            else if(p.isQueen())
                loadTexture("assets/Queen-Black.png");
            else if(p.isKing())
                loadTexture("assets/King-Black.png");
        }
        
        setDraggable(true);
        setTargetable(false);
        
        s.addActor(this);
    }
    
    public void act(float dt)
    {
    }
}
