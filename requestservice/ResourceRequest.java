import java.time.LocalDateTime;
import java.util.Objects;

public class ResourceRequest {
    private final int id;
    private final Resource resource;
    private final Student requester;
    private final String note;
    private RequestStatus status; // Defaults to RequestStatus.PENDING
    private LocalDateTime expiresAt;

    public ResourceRequest(int id, Resource resource, Student requester, String note) {
        if (id < 1) {
            throw new IllegalArgumentException("Request ID must be positive");
        }
        this.id = id;
        this.resource = Objects.requireNonNull(resource, "resource");
        this.requester = Objects.requireNonNull(requester, "requester");
        this.note = note;
        this.status = RequestStatus.PENDING;
    }

    public int getId() { return this.id; }
    public Resource getResource() { return this.resource; }
    public Student getRequester() { return this.requester; }
    public String getNote() { return this.note; }
    public RequestStatus getStatus() { return this.status; }
    public LocalDateTime getExpiresAt() { return this.expiresAt; }

    // Called when owner accepts the request
    public void accept(int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Reservation duration must be positive");
        }
        if (status != RequestStatus.PENDING) {
            throw new IllegalStateException("Only a pending request can be accepted");
        }
        this.resource.markReserved();
        this.status = RequestStatus.ACCEPTED;
        this.expiresAt = LocalDateTime.now().plusHours(hours);
    }

    // Used by RequestService (reject / auto-reject)
    public void reject() {
        if (status != RequestStatus.PENDING) {
            throw new IllegalStateException("Only a pending request can be rejected");
        }
        this.status = RequestStatus.REJECTED;
    }

    // Used by RequestService (cancel / expiry)
    public void cancel() {
        if (status != RequestStatus.PENDING && status != RequestStatus.ACCEPTED) {
            throw new IllegalStateException("Only a pending or accepted request can be cancelled");
        }
        this.status = RequestStatus.CANCELLED;
    }

    // Must provide this boolean check for Sreyas's chat module
    public boolean isAccepted() {
        return this.status == RequestStatus.ACCEPTED;
    }

    public boolean isExpired() {
        return expiresAt != null && !LocalDateTime.now().isBefore(expiresAt);
    }

    public String display() {
        String s = "#" + id + " | " + resource.getTitle()
                + " | From: " + requester.display()
                + " | " + status;
        if (note != null && !note.isBlank()) s += "\n      Note: " + note;
        if (expiresAt != null) s += "\n      Expires: " + expiresAt;
        return s;
    }
}