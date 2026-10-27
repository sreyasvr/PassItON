import java.util.Vector;

/**
 * Singleton service managing all active chat rooms across the PassItOn application.
 */
public class ChatService {

    private static ChatService instance;
    private Vector<ChatRoom> activeChats;
    private String[] quickReplies;

    private ChatService() {
        this.activeChats = new Vector<ChatRoom>();
        this.quickReplies = new String[] {
            "Where are you?",
            "I'm here at the campus pickup spot.",
            "Can we meet in 10 minutes near the library?",
            "I have reached the department corridor.",
            "Thank you! Item successfully received."
        };
    }

    /**
     * Returns the single shared instance of ChatService.
     * 
     * @return The ChatService instance
     */
    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }
        return instance;
    }

    /**
     * Retrieves an existing chat room for a request or creates one if it does not exist.
     * 
     * @param request The ResourceRequest coordinating the hand-over
     * @return The ChatRoom instance
     * @throws PassItOnException If the request is null or not accepted
     */
    public ChatRoom getOrCreateChatRoom(ResourceRequest request) throws PassItOnException {
        if (request == null) {
            throw new PassItOnException("Invalid Request: ResourceRequest cannot be null.");
        }

        for (int i = 0; i < this.activeChats.size(); i++) {
            ChatRoom room = this.activeChats.get(i);
            if (room.getRequest() != null && room.getRequest().getId() == request.getId()) {
                return room;
            }
        }

        if (!request.isAccepted()) {
            throw new PassItOnException("Cannot open chat: Hand-over chat only unlocks after the owner accepts the request.");
        }

        ChatRoom newRoom = new ChatRoom(request);
        this.activeChats.add(newRoom);
        return newRoom;
    }

    /**
     * Finds a chat room by its ResourceRequest ID.
     * 
     * @param requestId The ID of the resource request
     * @return The ChatRoom, or null if not found
     */
    public ChatRoom getChatRoomByRequestId(int requestId) {
        for (int i = 0; i < this.activeChats.size(); i++) {
            ChatRoom room = this.activeChats.get(i);
            if (room.getRequest() != null && room.getRequest().getId() == requestId) {
                return room;
            }
        }
        return null;
    }

    /**
     * Retrieves all chat rooms where a specific student is a participant.
     * 
     * @param student The student whose chat rooms are to be retrieved
     * @return Vector of ChatRoom objects
     */
    public Vector<ChatRoom> getChatsForStudent(Student student) {
        Vector<ChatRoom> userChats = new Vector<ChatRoom>();
        if (student == null) {
            return userChats;
        }

        for (int i = 0; i < this.activeChats.size(); i++) {
            ChatRoom room = this.activeChats.get(i);
            ResourceRequest req = room.getRequest();

            if (req != null) {
                Student requester = req.getRequester();
                Student owner = req.getResource().getOwner();

                if (isSameStudent(student, requester) || isSameStudent(student, owner)) {
                    userChats.add(room);
                }
            }
        }
        return userChats;
    }

    /**
     * Calculates the total unread message count across all chat rooms for a student.
     * 
     * @param student The student checking total unread messages
     * @return Total count of unread incoming messages
     */
    public int getTotalUnreadCount(Student student) {
        int totalUnread = 0;
        Vector<ChatRoom> studentChats = getChatsForStudent(student);

        for (int i = 0; i < studentChats.size(); i++) {
            ChatRoom room = studentChats.get(i);
            totalUnread += room.unreadFor(student);
        }
        return totalUnread;
    }

    /**
     * Returns the array of standard quick replies for campus hand-overs.
     * 
     * @return Array of quick reply strings
     */
    public String[] getQuickReplies() {
        return this.quickReplies;
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
}

