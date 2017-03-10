import java.util.Scanner; 

/**
 * Write a description of class ChessGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ChessGame
{
    public static void main()
    {
        Scanner scan = new Scanner(System.in);
        
        Board b = new Board();
        
        b.resetBoard();
        
        while(true)
        {
            
        }
    }
    
    /**
     *  Halts execution until ENTER key is pressed.
     */
    public static void pressEnterToContinue(Scanner scan)
    { 
        System.out.println("Press ENTER to continue...");
        scan.nextLine();
    }
    
    /**
     *  Clears the terminal window (in BlueJ) by printing the "form feed" character.
     */
    public static void clearTerminalWindow()
    {
        System.out.print('\u000C'); 
    }
}
