import java.util.List;
import java.util.Scanner;

/** Console "Incoming Requests" page for an owner. */
public class IncomingRequestsPage {
    private final RequestService requestService;
    private final Student owner;
    private final Scanner sc;

    public IncomingRequestsPage(RequestService requestService, Student owner, Scanner sc) {
        this.requestService = requestService;
        this.owner = owner;
        this.sc = sc;
    }

    public void show() {
        while (true) {
            List<ResourceRequest> incoming = requestService.getIncomingRequests(owner);
            List<ResourceRequest> reserved = requestService.getReservedRequests(owner);

            System.out.println("\n===== INCOMING REQUESTS =====");
            if (incoming.isEmpty()) System.out.println("  No pending requests.");
            for (ResourceRequest r : incoming) System.out.println("  " + r.display());

            System.out.println("\n----- Reserved (awaiting hand-over) -----");
            if (reserved.isEmpty()) System.out.println("  None.");
            for (ResourceRequest r : reserved) System.out.println("  " + r.display());

            System.out.println("\n1. Accept  2. Reject  3. Confirm collected  0. Back");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();
            if (choice.equals("0")) return;
            if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
                System.out.println("Invalid choice.");
                continue;
            }

            try {
                System.out.print("Request ID: ");
                int id = Integer.parseInt(sc.nextLine().trim());

                // Ownership check: the ID must belong to one of this owner's lists
                List<ResourceRequest> allowed = choice.equals("3") ? reserved : incoming;
                boolean ok = false;
                for (ResourceRequest r : allowed) {
                    if (r.getId() == id) { ok = true; break; }
                }
                if (!ok) {
                    System.out.println("Invalid request ID for this action.");
                    continue;
                }

                switch (choice) {
                    case "1":
                        requestService.accept(id, RequestService.DEFAULT_HOURS);
                        System.out.println("Accepted. Reserved for " + RequestService.DEFAULT_HOURS
                                + " hours. Other pending requests were rejected. Chat is now open.");
                        break;
                    case "2":
                        requestService.reject(id);
                        System.out.println("Request rejected.");
                        break;
                    case "3":
                        requestService.markCollected(id);
                        System.out.println("Marked as collected. Thank you for sharing!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (PassItOnException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}