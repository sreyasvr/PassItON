import java.util.Vector;

/**
 * Manages a private hand-over chat session between an item owner 
 * and an accepted requester for a specific ResourceRequest.
 */
public class ChatRoom {

    private ResourceRequest request;
    private Vector<Message> messages;

    /**
     * Constructs a ChatRoom associated with a ResourceRequest.
     * 
     * @param request The ResourceRequest coordinating resource hand-over
     */
    public ChatRoom(ResourceRequest request) {
        this.request = request;
        this.messages = new Vector<Message>();
    }

    /**
     * Sends a new message in this chat room.
     * 
     * @param sender The student sending the message
     * @param text   The message content
     * @throws PassItOnException If the request is not accepted, the sender is unauthorized, or text is blank
     */
    public void send(Student sender, String text) throws PassItOnException {
        if (this.request == null || !isRequestAccepted()) {
            throw new PassItOnException("Access Denied: Messages can only be sent after the request has been accepted.");
        }

        Student requester = this.request.getRequester();
        Student owner = this.request.getResource().getOwner();

        if (!isSameStudent(sender, requester) && !isSameStudent(sender, owner)) {
            throw new PassItOnException("Access Denied: You are not authorized to send messages in this chat room.");
        }

        if (text == null || text.trim().isEmpty()) {
            throw new PassItOnException("Invalid Action: Message text cannot be empty.");
        }

        Message newMessage = new Message(sender, text.trim());
        this.messages.add(newMessage);
    }

    /**
     * Calculates the number of unread messages for a specific student.
     * 
     * @param student The student checking for unread messages
     * @return The number of unread incoming messages
     */
    public int unreadFor(Student student) {
        int unreadCount = 0;
        for (int i = 0; i < this.messages.size(); i++) {
            Message currentMsg = this.messages.get(i);
            if (!isSameStudent(currentMsg.getSender(), student) && !currentMsg.isRead()) {
                unreadCount++;
            }
        }
        return unreadCount;
    }

    /**
     * Marks all incoming messages for a student as read.
     * 
     * @param student The student viewing the conversation
     */
    public void markAllAsReadFor(Student student) {
        for (int i = 0; i < this.messages.size(); i++) {
            Message currentMsg = this.messages.get(i);
            if (!isSameStudent(currentMsg.getSender(), student)) {
                currentMsg.markAsRead();
            }
        }
    }

    private boolean isRequestAccepted() {
        if (this.request == null) {
            return false;
        }
        try {
            return this.request.isAccepted();
        } catch (Exception e) {
            if (this.request.getStatus() != null) {
                String statusStr = this.request.getStatus().toString();
                return statusStr.equalsIgnoreCase("ACCEPTED") || statusStr.equalsIgnoreCase("RESERVED");
            }
        }
        return false;
    }

    private boolean isSameStudent(Student s1, Student s2) {
        if (s1 == null || s2 == null) {
            return false;
        }
        if (s1 == s2) {
            return true;
        }
        return s1.getId() == s2.getId();
    }

    /**
     * Returns the list of messages in this chat room.
     * 
     * @return Vector of Message objects
     */
    public Vector<Message> getMessages() {
        return this.messages;
    }

    /**
     * Returns the associated ResourceRequest.
     * 
     * @return The ResourceRequest instance
     */
    public ResourceRequest getRequest() {
        return this.request;
    }

    /**
     * Returns the other participant in the conversation.
     * 
     * @param currentStudent The current student
     * @return The opposing student in the chat
     */
    public Student getOtherParticipant(Student currentStudent) {
        if (this.request == null) {
            return null;
        }
        Student requester = this.request.getRequester();
        Student owner = this.request.getResource().getOwner();

        if (isSameStudent(currentStudent, requester)) {
            return owner;
        } else {
            return requester;
        }
    }

    /**
     * Returns the most recent message in this room.
     * 
     * @return The last Message, or null if the chat is empty
     */
    public Message getLastMessage() {
        if (this.messages == null || this.messages.isEmpty()) {
            return null;
        }
        return this.messages.lastElement();
    }
}

