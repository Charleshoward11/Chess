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
    private ArrayList<Move> castles;
    private ArrayList<Move> checks;
    private ArrayList<Move> checkmates;
    private ArrayList<Move> selfChecks;
    private ArrayList<Move> stalemates;
    
    public ValidMoveList()
    {
        this.moves = new ArrayList<Move>();
        this.captures = new ArrayList<Move>();
        this.castles = new ArrayList<Move>();
        this.checks = new ArrayList<Move>();
        this.checkmates = new ArrayList<Move>();
        this.selfChecks = new ArrayList<Move>();
        this.stalemates = new ArrayList<Move>();
    }
    
    public ValidMoveList(ArrayList<Move> moves, 
                         ArrayList<Move> captures, 
                         ArrayList<Move> castles, 
                         ArrayList<Move> checks, 
                         ArrayList<Move> checkmates, 
                         ArrayList<Move> selfChecks,
                         ArrayList<Move> stalemates)
    {
        this.moves = moves;
        this.captures = captures;
        this.castles = castles;
        this.checks = checks;
        this.checkmates = checkmates;
        this.selfChecks = selfChecks;
        this.stalemates = stalemates;
    }
    
    public ValidMoveList addMove(Move m)
    {
        this.moves.add(m);
        return this;
    }
    
    public ValidMoveList addCapture(Move m)
    {
        this.captures.add(m);
        return this;
    }
    
    public ValidMoveList addCastle(Move m)
    {
        this.castles.add(m);
        return this;
    }
    
    public ValidMoveList addCheck(Move m)
    {
        this.checks.add(m);
        return this;
    }
    
    public ValidMoveList addCheckmate(Move m)
    {
        this.checkmates.add(m);
        return this;
    }
    
    public ValidMoveList addSelfCheck(Move m)
    {
        this.selfChecks.add(m);
        return this;
    }
    
    public ValidMoveList addStalemate(Move m)
    {
        this.stalemates.add(m);
        return this;
    }
    
    public ArrayList<Move> getMoves()
    {
        return this.moves;
    }
    
    public ArrayList<Move> getCastles()
    {
        return this.castles;
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
    
    public ArrayList<Move> getStalemates()
    {
        return this.stalemates;
    }
    
    public ArrayList<Move> getAll()
    {
        ArrayList<Move> all = new ArrayList<Move>();
        
        all.addAll(this.moves);
        all.addAll(this.captures);
        all.addAll(this.castles);
        all.addAll(this.checks);
        all.addAll(this.checkmates);
        all.addAll(this.selfChecks);
        all.addAll(this.stalemates);
        
        return all;
    }
    
    public ValidMoveList combineWith(ValidMoveList other)
    {
        this.moves.addAll(other.getMoves());
        this.captures.addAll(other.getCaptures());
        this.castles.addAll(other.getCastles());
        this.checks.addAll(other.getChecks());
        this.checkmates.addAll(other.getCheckmates());
        this.selfChecks.addAll(other.getSelfChecks());
        this.stalemates.addAll(other.getStalemates());
        
        return this;
    }
    
    /**
     * Removes all duplicates of moves in less "important" lists.
     * 
     * The hierarchy of importance is:
     * selfChecks > staleMates > checkmates > checks > castles > captures > moves
     */
    public ValidMoveList removeDuplicates()
    {
        for(Move m : selfChecks)
        {
            stalemates.remove(m);
            checkmates.remove(m);
            checks.remove(m);
            castles.remove(m);
            captures.remove(m);
            moves.remove(m);
        }
        
        for(Move m : stalemates)
        {
            checkmates.remove(m);
            checks.remove(m);
            castles.remove(m);
            captures.remove(m);
            moves.remove(m);
        }
        
        for(Move m : checkmates)
        {
            checks.remove(m);
            castles.remove(m);
            captures.remove(m);
            moves.remove(m);
        }
        
        for(Move m : checks)
        {
            castles.remove(m);
            captures.remove(m);
            moves.remove(m);
        }
        
        for(Move m : castles)
        {
            captures.remove(m);
            moves.remove(m);
        }
        
        for(Move m : captures)
        {
            moves.remove(m);
        }
        
        return this;
    }
    
    public String toString()
    {
        return "I'd really rather not.";
    }
}
