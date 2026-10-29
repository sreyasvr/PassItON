import java.util.Objects;

public class Resource {
    private final String title;
    private final Student owner;
    private ResourceStatus status = ResourceStatus.AVAILABLE;

    public Resource(String title, Student owner) {
        this.title = Objects.requireNonNull(title, "title");
        this.owner = Objects.requireNonNull(owner, "owner");
    }

    public String getTitle() {
        return title;
    }

    public Student getOwner() {
        return owner;
    }

    public ResourceStatus getStatus() {
        return status;
    }

    public void markRequested() {
        requireStatus(ResourceStatus.AVAILABLE, "requested");
        status = ResourceStatus.REQUESTED;
    }

    public void markReserved() {
        requireStatus(ResourceStatus.REQUESTED, "reserved");
        status = ResourceStatus.RESERVED;
    }

    public void markAvailable() {
        if (status != ResourceStatus.REQUESTED && status != ResourceStatus.RESERVED) {
            throw new IllegalStateException(
                    "Only a requested or reserved resource can be made available");
        }
        status = ResourceStatus.AVAILABLE;
    }

    public void markCollected() {
        requireStatus(ResourceStatus.RESERVED, "collected");
        status = ResourceStatus.COLLECTED;
    }

    private void requireStatus(ResourceStatus expected, String transition) {
        if (status != expected) {
            throw new IllegalStateException(
                    "Resource must be " + expected + " before it can be " + transition);
        }
    }
}
