package in.co.rays.util;

/**
 * EmailMessage class represents an email with its basic attributes
 * such as recipient, subject, message content, and message type.
 * <p>
 * The message type can be either plain text (TEXT_MSG) or HTML (HTML_MSG).
 * </p>
 * 
 * @author Akbar
 * @version 1.0
 */
public class EmailMessage {

    /** Recipient email address */
    private String to;

    /** Subject of the email */
    private String subject;

    /** Message content of the email */
    private String message;

    /** Type of the message (HTML_MSG or TEXT_MSG), default is TEXT_MSG */
    private int messageType = TEXT_MSG;

    /** Constant for HTML message type */
    public static final int HTML_MSG = 1;

    /** Constant for plain text message type */
    public static final int TEXT_MSG = 2;

    /** Default constructor */
    public EmailMessage() {
    }

    /**
     * Parameterized constructor to initialize email message.
     * 
     * @param to      recipient email address
     * @param subject subject of the email
     * @param message message content
     */
    public EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Sets the recipient email address.
     * 
     * @param to recipient email address
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Returns the recipient email address.
     * 
     * @return recipient email address
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the subject of the email.
     * 
     * @param subject subject of the email
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the subject of the email.
     * 
     * @return subject of the email
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the message content of the email.
     * 
     * @param message message content
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the message content of the email.
     * 
     * @return message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message type (HTML_MSG or TEXT_MSG).
     * 
     * @param messageType type of the message
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * Returns the type of the message.
     * 
     * @return message type
     */
    public int getMessageType() {
        return messageType;
    }

}
