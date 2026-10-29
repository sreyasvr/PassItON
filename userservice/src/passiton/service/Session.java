package passiton.service;

import passiton.model.User;

import java.util.ArrayList;
import java.util.List;

/** Session helper: remembers who is logged in and tells the navbar when it changes. */
public final class Session {
    private static final Session INSTANCE = new Session();
    private User currentUser;
    private final List<Runnable> listeners = new ArrayList<>();

    private Session() { }

    public static Session getInstance() { return INSTANCE; }

    public User getCurrentUser() { return currentUser; }
    public boolean isLoggedIn()  { return currentUser != null; }
    public boolean isAdmin()     { return currentUser != null && "ADMIN".equals(currentUser.getRole()); }

    public void login(User user) {
        this.currentUser = user;
        notifyListeners();
    }

    public void logout() {
        this.currentUser = null;
        notifyListeners();
    }

    public void addListener(Runnable r) { listeners.add(r); }

    private void notifyListeners() {
        for (Runnable r : listeners) r.run();
    }
}
