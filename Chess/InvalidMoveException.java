/**
 * 
 */
public class InvalidMoveException extends Exception
{
    private String message = null;
    
    public InvalidMoveException()
    {
        super();
    }
    
    public InvalidMoveException(String message)
    {
        super(message);
        this.message = message;
    }
    
    public InvalidMoveException(Throwable cause)
    {
        super(cause);
    }
    
    @Override
    public String toString()
    {
        return message;
    }
    
    @Override
    public String getMessage()
    {
        return message;
    }
}