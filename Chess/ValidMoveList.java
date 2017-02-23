import java.util.ArrayList;

/**
 * This object will probably make certain things easier.
 * 
 * I'm not actually sure if it's necessary for the Move class to have a boolean stating if it's a capture.
 * I might just be able to determine that by which ArrayList I put it into here.
 * That could potentially save memory, I think. Memory usage is probably going to become an issue with this project.
 * 
 * @author Charles Howard
 * @version (a version number or a date)
 */
public class ValidMoveList
{
    private ArrayList<Move> moves;
    private ArrayList<Move> captures;
    
    public ValidMoveList()
    {
        moves = new ArrayList<Move>();
        captures = new ArrayList<Move>();
    }
    
    //    public ValidMoveList add(Move m)
    //    {
    //        if(m.isCapture())
    //        {
    //            captures.add(m);
    //        }
    //        else
    //        {
    //            moves.add(m);
    //        }
    //        
    //        return this;
    //    }
    
    public ValidMoveList addMove(Move m)
    {
        moves.add(m);
        return this;
    }
    
    public ValidMoveList addCapture(Move m)
    {
        captures.add(m);
        return this;
    }
    
    public ArrayList<Move> getMoves()
    {
        return this.moves;
    }
    
    public ArrayList<Move> getCaptures()
    {
        return this.captures;
    }
}
