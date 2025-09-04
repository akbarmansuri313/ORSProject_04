package in.co.rays.exception;

/**
 * ApplicationException is a custom exception class used to handle
 * general application-level exceptions in the system.
 * It extends the standard Java {@link Exception} class.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class ApplicationException extends Exception {

    /**
     * Constructs a new ApplicationException with the specified detail message.
     *
     * @param msg the detail message
     */
    public ApplicationException(String msg) {
        super(msg);
    }

}
