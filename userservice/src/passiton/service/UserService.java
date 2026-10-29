package passiton.service;

import passiton.model.Student;
import passiton.util.PassItOnException;

import java.util.HashMap;
import java.util.Map;

/** Registers and authenticates users. Users are kept in a Map keyed by email. */
public class UserService {
    private final Map<String, Student> users = new HashMap<>();
    private int nextId = 1;

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
        for (Student s : users.values()) {
            if (s.getRegisterNo().equalsIgnoreCase(registerNo.trim())) {
                throw new PassItOnException("This register number is already registered.");
            }
        }
        Student student = new Student(nextId++, name.trim(), registerNo.trim(),
                department.trim(), semester, key, password);
        users.put(key, student);
        return student;
    }

    public Student login(String email, String password) throws PassItOnException {
        if (isBlank(email) || isBlank(password)) {
            throw new PassItOnException("Enter email and password.");
        }
        Student s = users.get(email.trim().toLowerCase());
        if (s == null || !s.checkPassword(password)) {
            throw new PassItOnException("Invalid email or password.");
        }
        return s;
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
