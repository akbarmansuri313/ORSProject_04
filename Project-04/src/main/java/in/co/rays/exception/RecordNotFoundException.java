package in.co.rays.exception;

/**
 * RecordNotFoundException is a custom exception class used to indicate
 * that a requested record could not be found in the database.
 * It extends the standard Java {@link Exception} class.
 * 
 * @author Akbar Mansuri
 * @version 1.0
 */
public class RecordNotFoundException extends Exception {

    /**
     * Constructs a new RecordNotFoundException with the specified detail message.
     *
     * @param msg the detail message
     */
    public RecordNotFoundException(String msg) {
        super(msg);
    }

}
