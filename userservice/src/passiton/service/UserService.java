package passiton.service;

import passiton.model.Admin;
import passiton.model.Student;
import passiton.model.User;
import passiton.util.PassItOnException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Registers and authenticates users. Users are kept in a Map keyed by email. */
public class UserService {
    public static final String DEFAULT_ADMIN_EMAIL = "admin@passiton.com";
    public static final String DEFAULT_ADMIN_PASSWORD = "admin123";

    private final Map<String, User> users = new HashMap<>();
    private int nextId = 1;

    public UserService() {
        // Seed one default admin (admins cannot self-register).
        users.put(DEFAULT_ADMIN_EMAIL,
                new Admin(nextId++, "Administrator", DEFAULT_ADMIN_EMAIL, DEFAULT_ADMIN_PASSWORD));
    }

    public Student register(String name, String registerNo, String department,
                            int semester, String email, String password) throws PassItOnException {
        if (isBlank(name) || isBlank(registerNo) || isBlank(department)
                || isBlank(email) || isBlank(password)) {
            throw new PassItOnException("All fields are required.");
        }
        if (semester < 1 || semester > 8) {
            throw new PassItOnException("Semester must be between 1 and 8.");
        }
        if (!email.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new PassItOnException("Enter a valid email address.");
        }
        if (password.length() < 6) {
            throw new PassItOnException("Password must be at least 6 characters.");
        }
        String key = email.trim().toLowerCase();
        if (users.containsKey(key)) {
            throw new PassItOnException("This email is already registered.");
        }
        for (Student s : getAllStudents()) {
            if (s.getRegisterNo().equalsIgnoreCase(registerNo.trim())) {
                throw new PassItOnException("This register number is already registered.");
            }
        }
        Student student = new Student(nextId++, name.trim(), registerNo.trim(),
                department.trim(), semester, key, password);
        users.put(key, student);
        return student;
    }

    /** Logs in a student or an admin; check getRole() to tell them apart. */
    public User login(String email, String password) throws PassItOnException {
        if (isBlank(email) || isBlank(password)) {
            throw new PassItOnException("Enter email and password.");
        }
        User u = users.get(email.trim().toLowerCase());
        if (u == null || !u.checkPassword(password)) {
            throw new PassItOnException("Invalid email or password.");
        }
        return u;
    }

    // ---------- Admin operations ----------

    public List<Student> getAllStudents(User actor) throws PassItOnException {
        requireAdmin(actor);
        return getAllStudents();
    }

    public void removeStudent(User actor, String email) throws PassItOnException {
        requireAdmin(actor);
        User target = users.get(email == null ? "" : email.trim().toLowerCase());
        if (!(target instanceof Student)) {
            throw new PassItOnException("Student not found.");
        }
        users.remove(target.getEmail());
    }

    private List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        for (User u : users.values()) {
            if (u instanceof Student) list.add((Student) u);
        }
        return list;
    }

    private void requireAdmin(User actor) throws PassItOnException {
        if (actor == null || !"ADMIN".equals(actor.getRole())) {
            throw new PassItOnException("Admin access required.");
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
