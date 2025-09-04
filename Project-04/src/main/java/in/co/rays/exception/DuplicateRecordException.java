package in.co.rays.exception;

/**
 * DuplicateRecordException is a custom exception class used to indicate
 * that a record being added or updated already exists in the database.
 * It extends the standard Java {@link Exception} class.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class DuplicateRecordException extends Exception {

    /**
     * Constructs a new DuplicateRecordException with the specified detail message.
     *
     * @param msg the detail message
     */
    public DuplicateRecordException(String msg) {
        super(msg);
    }

}
