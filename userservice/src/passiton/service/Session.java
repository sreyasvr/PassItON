package passiton.service;

import passiton.model.Student;

import java.util.ArrayList;
import java.util.List;

/** Session helper: remembers who is logged in and tells the navbar when it changes. */
public final class Session {
    private static final Session INSTANCE = new Session();
    private Student currentUser;
    private final List<Runnable> listeners = new ArrayList<>();

    private Session() { }

    public static Session getInstance() { return INSTANCE; }

    public Student getCurrentUser() { return currentUser; }
    public boolean isLoggedIn()     { return currentUser != null; }

    public void login(Student student) {
        this.currentUser = student;
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
