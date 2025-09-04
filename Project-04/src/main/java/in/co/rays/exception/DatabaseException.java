package in.co.rays.exception;

/**
 * DatabaseException is a custom exception class used to indicate
 * errors related to database operations in the system.
 * It extends the standard Java {@link Exception} class.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class DatabaseException extends Exception {

    /**
     * Constructs a new DatabaseException with the specified detail message.
     *
     * @param msg the detail message
     */
    public DatabaseException(String msg) {
        super(msg);
    }

}
