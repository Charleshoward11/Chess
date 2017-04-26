import java.util.ArrayList;

/**
 * This object will probably make certain things easier.
 * 
 * I'm not actually sure if it's necessary for the Move class to have a boolean stating if it's a capture.
 * I might just be able to determine that by which ArrayList I put it into here.
 * That could potentially save memory, I think. Memory usage may become an issue with this project.
 * 
 * Maybe I should just have the constructor take in six ArrayLists, 
 * and make the six in this class be public final.
 * 
 * In fact, maybe I could just offload most of the move proccessing to this class...
 * 
 * @author Charles Howard
 * @version (a version number or a date)
 */
public class ValidMoveList
{
    private ArrayList<Move> moves;
    private ArrayList<Move> captures;
    private ArrayList<Move> checks;
    private ArrayList<Move> checkmates;
    private ArrayList<Move> selfChecks;
    private ArrayList<Move> castles;
    
    public ValidMoveList()
    {
        this.moves = new ArrayList<Move>();
        this.captures = new ArrayList<Move>();
        this.checks = new ArrayList<Move>();
        this.checkmates = new ArrayList<Move>();
        this.selfChecks = new ArrayList<Move>();
        this.castles = new ArrayList<Move>();
    }
    
    public ValidMoveList(ArrayList<Move> moves, ArrayList<Move> captures, ArrayList<Move> checks, ArrayList<Move> checkmates, ArrayList<Move> selfChecks, ArrayList<Move> castles)
    {
        this.moves = moves;
        this.captures = captures;
        this.checks = checks;
        this.checkmates = checkmates;
        this.selfChecks = selfChecks;
        this.castles = castles;
    }
    
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
    
    public ValidMoveList addCheck(Move m)
    {
        checks.add(m);
        return this;
    }
    
    public ValidMoveList addCheckmate(Move m)
    {
        checkmates.add(m);
        return this;
    }
    
    public ValidMoveList addSelfCheck(Move m)
    {
        selfChecks.add(m);
        return this;
    }
    
    public ValidMoveList addCastle(Move m)
    {
        castles.add(m);
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
    
    public ArrayList<Move> getChecks()
    {
        return this.checks;
    }
    
    public ArrayList<Move> getCheckmates()
    {
        return this.checkmates;
    }
    
    public ArrayList<Move> getSelfChecks()
    {
        return this.selfChecks;
    }
    
    public ArrayList<Move> getCastles()
    {
        return this.castles;
    }
    
    public ArrayList<Move> getAll()
    {
        ArrayList<Move> all = new ArrayList<Move>();
        
        all.addAll(this.moves);
        all.addAll(this.captures);
        all.addAll(this.checks);
        all.addAll(this.checkmates);
        all.addAll(this.selfChecks);
        all.addAll(this.castles);
        
        return all;
    }
}
