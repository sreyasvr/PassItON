import java.util.List;
import java.util.ArrayList;

public class RequestService {
    public static final int DEFAULT_HOURS = 48;

    private List<ResourceRequest> requests = new ArrayList<>();
    private int nextId = 1;

    // Business Rule: Student cannot request their own resource!
    public ResourceRequest createRequest(Resource resource, Student requester, String note)
            throws PassItOnException {
        checkExpiry();

        if (resource.getOwner().getId() == requester.getId()) {
            throw new PassItOnException("You cannot request your own item");
        }
        if (resource.getStatus() == ResourceStatus.RESERVED
                || resource.getStatus() == ResourceStatus.COLLECTED) {
            throw new PassItOnException("This item is no longer available");
        }
        for (ResourceRequest r : requests) {
            if (r.getResource() == resource
                    && r.getRequester().getId() == requester.getId()
                    && r.getStatus() == RequestStatus.PENDING) {
                throw new PassItOnException("You already have a pending request for this item");
            }
        }

        ResourceRequest req = new ResourceRequest(nextId++, resource, requester, note);
        requests.add(req);
        if (resource.getStatus() == ResourceStatus.AVAILABLE) {
            resource.markRequested();
        }
        return req;
    }

    // Business Rule: Accepting one request AUTO-REJECTS all other pending requests for the same resource!
    public void accept(int requestId, int hours) throws PassItOnException {
        checkExpiry();
        ResourceRequest req = findById(requestId);
        if (req.getStatus() != RequestStatus.PENDING) {
            throw new PassItOnException("Only a pending request can be accepted");
        }

        req.accept(hours); // status = ACCEPTED, starts timer, resource -> RESERVED

        for (ResourceRequest other : requests) {
            if (other != req
                    && other.getResource() == req.getResource()
                    && other.getStatus() == RequestStatus.PENDING) {
                other.reject();
            }
        }
    }

    public void reject(int requestId) throws PassItOnException {
        checkExpiry();
        ResourceRequest req = findById(requestId);
        if (req.getStatus() != RequestStatus.PENDING) {
            throw new PassItOnException("Only a pending request can be rejected");
        }
        req.reject();
        releaseIfNoPending(req.getResource());
    }

    public void cancel(int requestId) throws PassItOnException {
        checkExpiry();
        ResourceRequest req = findById(requestId);
        RequestStatus before = req.getStatus();
        if (before != RequestStatus.PENDING && before != RequestStatus.ACCEPTED) {
            throw new PassItOnException("This request can no longer be cancelled");
        }
        req.cancel();

        if (before == RequestStatus.ACCEPTED) {
            req.getResource().markAvailable();   // reservation released
        } else {
            releaseIfNoPending(req.getResource());
        }
    }

    public void markCollected(int requestId) throws PassItOnException {
        checkExpiry();
        ResourceRequest req = findById(requestId);
        if (!req.isAccepted()) {
            throw new PassItOnException("Only an accepted request can be collected");
        }
        if (req.getResource().getStatus() != ResourceStatus.RESERVED) {
            throw new PassItOnException("Reservation is no longer active");
        }
        req.getResource().markCollected();
    }

    // Reverts expired reservations back to AVAILABLE
    public void checkExpiry() {
        for (ResourceRequest r : requests) {
            if (r.isAccepted()
                    && r.isExpired()
                    && r.getResource().getStatus() == ResourceStatus.RESERVED) {
                r.cancel();                        // reservation lapsed
                r.getResource().markAvailable();
            }
        }
    }

    // ---------- Extra helpers (used by the UI) ----------

    /** Pending requests for items listed by this owner. */
    public List<ResourceRequest> getIncomingRequests(Student owner) {
        checkExpiry();
        List<ResourceRequest> list = new ArrayList<>();
        for (ResourceRequest r : requests) {
            if (r.getResource().getOwner().getId() == owner.getId()
                    && r.getStatus() == RequestStatus.PENDING) {
                list.add(r);
            }
        }
        return list;
    }

    /** Accepted requests whose item is still RESERVED (awaiting hand-over). */
    public List<ResourceRequest> getReservedRequests(Student owner) {
        checkExpiry();
        List<ResourceRequest> list = new ArrayList<>();
        for (ResourceRequest r : requests) {
            if (r.getResource().getOwner().getId() == owner.getId()
                    && r.isAccepted()
                    && r.getResource().getStatus() == ResourceStatus.RESERVED) {
                list.add(r);
            }
        }
        return list;
    }

    /** All requests sent by this student (for "My Activity"). */
    public List<ResourceRequest> getRequestsByRequester(Student requester) {
        checkExpiry();
        List<ResourceRequest> list = new ArrayList<>();
        for (ResourceRequest r : requests) {
            if (r.getRequester().getId() == requester.getId()) list.add(r);
        }
        return list;
    }

    public ResourceRequest findById(int id) throws PassItOnException {
        for (ResourceRequest r : requests) {
            if (r.getId() == id) return r;
        }
        throw new PassItOnException("Request #" + id + " not found");
    }

    private void releaseIfNoPending(Resource res) {
        for (ResourceRequest r : requests) {
            if (r.getResource() == res && r.getStatus() == RequestStatus.PENDING) return;
        }
        if (res.getStatus() == ResourceStatus.REQUESTED) {
            res.markAvailable();
        }
    }
}