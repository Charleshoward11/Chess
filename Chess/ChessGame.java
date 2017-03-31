import java.util.Scanner; 

// Import LibGDX stuff?

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
        
        // If it's not white's turn, it's obviously black's
        boolean whitesTurn = true;
        
        b.resetBoard();
        
        while(true)
        {
            clearTerminalWindow();
            b.printBoard();
            
            getMoveFromPlayer(b, whitesTurn, scan);
        }
    }
    
    public static Move getMoveFromPlayer(Board b, boolean whitesTurn, Scanner scan)
    {
        scan.reset();
        
        int x, y;
        
        if(whitesTurn)
        {
            System.out.print("White");
        }
        else
        {
            System.out.print("Black");
        }
        
        System.out.print(" player, enter the coordinates of the piece you want to move (in \"x y\" format): ");
        
        try
        {
            x = scan.nextInt();
            y = scan.nextInt();
            
            Piece p = b.getPiece(x, y);
            
            if(p == null)
            {
                System.out.print("That square is empty. ");
                pressEnterToContinue(scan);
                return null;
            }
            else if(p.isWhite != whitesTurn)
            {
                System.out.print("That piece isn't yours. ");
                pressEnterToContinue(scan);
                return null;
            }
        }
        catch(Exception e)
        {
            return null;
        }
        
        return null;
    }
    
    public static void drawBoard()
    {
        
    }
    
    /**
     *  Halts execution until ENTER key is pressed.
     */
    public static void pressEnterToContinue(Scanner scan)
    { 
        scan.reset();
        System.out.print("Press ENTER to continue...");
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
