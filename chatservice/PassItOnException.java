/**
 * Custom exception thrown when resource-sharing business rules 
 * or chat access controls are violated.
 */
public class PassItOnException extends Exception {

    /**
     * Constructs a PassItOnException with a specified detail message.
     * 
     * @param message Description of the error condition
     */
    public PassItOnException(String message) {
        super(message);
    }
}

