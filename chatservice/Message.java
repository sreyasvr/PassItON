import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an individual chat message exchanged between students 
 * during campus resource hand-over coordination.
 */
public class Message {

    private Student sender;
    private String text;
    private LocalDateTime time;
    private boolean isRead;

    /**
     * Constructs a new Message with the current system timestamp.
     * 
     * @param sender The student sending the message
     * @param text   The message content
     */
    public Message(Student sender, String text) {
        this.sender = sender;
        this.text = text;
        this.time = LocalDateTime.now();
        this.isRead = false;
    }

    /**
     * Constructs a Message with a specified timestamp.
     * 
     * @param sender The student sending the message
     * @param text   The message content
     * @param time   The message timestamp
     */
    public Message(Student sender, String text, LocalDateTime time) {
        this.sender = sender;
        this.text = text;
        this.time = time;
        this.isRead = false;
    }

    /**
     * Returns the sender of this message.
     * 
     * @return The student who sent the message
     */
    public Student getSender() {
        return this.sender;
    }

    /**
     * Returns the message text content.
     * 
     * @return The message text
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the timestamp when the message was created.
     * 
     * @return The message timestamp
     */
    public LocalDateTime getTime() {
        return this.time;
    }

    /**
     * Checks whether the message has been read by the recipient.
     * 
     * @return true if read; false otherwise
     */
    public boolean isRead() {
        return this.isRead;
    }

    /**
     * Sets the read status of the message.
     * 
     * @param read The read status to set
     */
    public void setRead(boolean read) {
        this.isRead = read;
    }

    /**
     * Marks the message as read.
     */
    public void markAsRead() {
        this.isRead = true;
    }

    /**
     * Formats the timestamp for GUI display.
     * 
     * @return Formatted time string
     */
    public String getFormattedTime() {
        if (this.time == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return this.time.format(formatter);
    }

    @Override
    public String toString() {
        String senderName = (this.sender != null) ? this.sender.getName() : "Unknown";
        return "[" + getFormattedTime() + "] " + senderName + ": " + this.text;
    }
}

