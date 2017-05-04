import java.io.*;
import java.net.*;
import java.util.*;
/**
 * For the basic logic, I'm thinking that the AI should be preloaded with classic chess opening strategies.
 * Each move, the AI checks which captures are available, and evaluates if it's worth it to deviate.
 * 
 * Actually, maybe I should have it look 3-5 moves ahead (difficulty levels?), and assign values to each potential 
 * move depending on how many captures become available (along with which ones; a Queen would be worth more than a 
 * Pawn), along with how many captures become available to the player. Checkmate would obviously have extra value.
 * 
 * Making it behave defensively might prove to be tricky, this might allow it to tactically put the player in check,
 * as a check would restrict the player's options.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChessAI
{
    /**
     * Constructor might take in a difficulty or a knowlege base or something.
     */
    public ChessAI(Board b)
    {
    }
    
    /**
     * 
     */
    public Move decideMove()
    {
        return null;
    }
    
    
}
