# PassItOn — Campus Resource-Sharing System
## Module 4: Hand-over Chat Module (Formal Technical Documentation)

**Student Name:** Sreyas V R  
**Roll No:** 71  
**Batch / Semester:** S3 CSD  
**Review Stage:** Micro-project Review  

---

### 1. Module Overview
The **Hand-over Chat Module** provides a private, secure, in-app communication channel between a campus resource **Owner** and an accepted **Requester**. It eliminates the need to expose personal phone numbers publicly, facilitates physical hand-over logistics on campus, and provides:
- Access-controlled chat initiation (only permitted after an owner accepts a request).
- Real-time unread badge calculation for the web navigation bar.
- Pre-configured quick-reply options for efficient campus meetups.
- A centralized Singleton service to ensure consistent chat state across all UI views.

---

### 2. Architectural Role & Class Relationships

```
+--------------------------------------------------------------+
|                    ChatService (Singleton)                   |
|  - instance : ChatService                                    |
|  - activeChats : Vector<ChatRoom>                            |
|  - quickReplies : String[]                                   |
|  + getInstance() : ChatService                               |
|  + getOrCreateChatRoom(request) : ChatRoom                   |
|  + getChatsForStudent(student) : Vector<ChatRoom>            |
|  + getTotalUnreadCount(student) : int                        |
|  + getQuickReplies() : String[]                              |
+--------------------------------------------------------------+
                               |
                               | manages 1..*
                               v
+--------------------------------------------------------------+
|                          ChatRoom                            |
|  - request : ResourceRequest                                 |
|  - messages : Vector<Message>                                |
|  + send(sender: Student, text: String)                       |
|  + unreadFor(student: Student) : int                         |
|  + markAllAsReadFor(student: Student)                        |
|  + getMessages() : Vector<Message>                           |
+--------------------------------------------------------------+
                               |
                               | contains 0..*
                               v
+--------------------------------------------------------------+
|                           Message                            |
|  - sender : Student                                          |
|  - text : String                                             |
|  - time : LocalDateTime                                      |
|  - isRead : boolean                                          |
|  + getSender() : Student                                     |
|  + getText() : String                                        |
|  + getTime() : LocalDateTime                                 |
|  + isRead() : boolean                                        |
+--------------------------------------------------------------+
```

---

### 3. Class Specifications

#### 3.1. `PassItOnException`
- **Superclass:** `java.lang.Exception` (Checked Exception)
- **Purpose:** Thrown when unauthorized operations occur, including:
  - Attempting to send messages before a request is marked as accepted.
  - Non-participants attempting to send messages in a private room.
  - Submitting null or whitespace-only messages.

#### 3.2. `Message`
- **Fields:**
  - `private Student sender` — The user sending the message.
  - `private String text` — Text body.
  - `private LocalDateTime time` — Time of sending (`java.time.LocalDateTime`).
  - `private boolean isRead` — Read receipt flag for badge tracking.
- **Key Methods:**
  - `getSender() : Student`
  - `getText() : String`
  - `getTime() : LocalDateTime`
  - `isRead() : boolean`
  - `setRead(boolean) : void`
  - `markAsRead() : void`
  - `getFormattedTime() : String` — Formats timestamp (`hh:mm a`) for UI chat bubbles.

#### 3.3. `ChatRoom`
- **Fields:**
  - `private ResourceRequest request` — Target request coordinating item hand-over.
  - `private Vector<Message> messages` — Dynamic, thread-safe collection of messages.
- **Key Methods:**
  - `send(Student sender, String text) throws PassItOnException`:
    - Enforces acceptance verification.
    - Validates sender authorization (must be resource owner or requester).
    - Appends valid `Message` to the `messages` Vector.
  - `unreadFor(Student student) : int`:
    - Iterates via index loop over `messages`.
    - Returns count of incoming unread messages.
  - `markAllAsReadFor(Student student) : void`:
    - Clears unread status on incoming messages when student opens chat.
  - `getOtherParticipant(Student currentStudent) : Student`:
    - Helper for GUI conversation title display.

#### 3.4. `ChatService` (Singleton)
- **Fields:**
  - `private static ChatService instance`
  - `private Vector<ChatRoom> activeChats`
  - `private String[] quickReplies`
- **Key Methods:**
  - `getInstance() : ChatService` (Public static accessor with lazy initialization)
  - `getOrCreateChatRoom(ResourceRequest) : ChatRoom throws PassItOnException`
  - `getChatsForStudent(Student) : Vector<ChatRoom>`
  - `getTotalUnreadCount(Student) : int` (Computes navbar notification count)
  - `getQuickReplies() : String[]` (Supplies standard quick reply phrases)

---

### 4. OOP & Syllabus Concepts Matrix

| Syllabus Requirement | Applied Implementation in Chat Module |
|---|---|
| **Encapsulation** | Strict `private` fields with `public` accessors across all classes. |
| **Constructors & `this`** | Explicit constructor initialization and parameter disambiguation. |
| **Collections Framework** | `java.util.Vector` used for dynamic message and room storage. |
| **Custom Exceptions** | `PassItOnException` extending `Exception` with guarded checks. |
| **Design Patterns** | **Singleton Pattern** in `ChatService` ensuring global consistency. |
| **Arrays** | `String[]` array used for quick reply choices. |

