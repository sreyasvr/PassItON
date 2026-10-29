package passiton.util;

/** Custom exception for invalid actions (bad login, duplicate user, etc.). */
public class PassItOnException extends Exception {
    public PassItOnException(String message) {
        super(message);
    }
}
