import passiton.model.Student;
import passiton.model.User;
import passiton.service.Session;
import passiton.service.UserService;
import passiton.util.PassItOnException;
import passiton.util.PasswordHasher;

public class AuthTest {
    static int fails = 0;
    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (!ok) fails++;
    }
    public static void main(String[] a) throws Exception {
        UserService us = new UserService();
        Student s = us.register("Sreyas V R", "71", "CSD", 3, "sreyas@college.edu", "secret123");
        check("register ok", s.getId() == 2 && s.getRole().equals("STUDENT"));
        check("sha-256 known value", PasswordHasher.hash("abc").equals(
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"));
        check("checkPassword correct", s.checkPassword("secret123"));
        check("checkPassword wrong", !s.checkPassword("nope"));
        check("login ok (email case-insensitive)", us.login("SREYAS@college.edu", "secret123") == s);
        try { us.login("sreyas@college.edu", "bad"); check("bad login throws", false); }
        catch (PassItOnException e) { check("bad login throws", true); }
        try { us.register("X", "72", "CSD", 3, "sreyas@college.edu", "secret123"); check("dup email", false); }
        catch (PassItOnException e) { check("dup email rejected", true); }
        try { us.register("X", "71", "CSD", 3, "x@college.edu", "secret123"); check("dup regno", false); }
        catch (PassItOnException e) { check("dup register no rejected", true); }
        try { us.register("X", "80", "CSD", 3, "bad-email", "secret123"); check("bad email", false); }
        catch (PassItOnException e) { check("invalid email rejected", true); }
        User admin = us.login(UserService.DEFAULT_ADMIN_EMAIL, UserService.DEFAULT_ADMIN_PASSWORD);
        check("admin login, role ADMIN", admin.getRole().equals("ADMIN"));
        check("admin lists students", us.getAllStudents(admin).size() == 1);
        try { us.getAllStudents(s); check("student blocked from admin ops", false); }
        catch (PassItOnException e) { check("student blocked from admin ops", true); }
        try { us.register("A", "99", "CSD", 3, "admin@passiton.com", "secret123"); check("admin email taken", false); }
        catch (PassItOnException e) { check("admin email cannot be registered", true); }
        us.removeStudent(admin, "sreyas@college.edu");
        check("admin removes student", us.getAllStudents(admin).isEmpty());
        Session.getInstance().login(admin);
        check("session isAdmin", Session.getInstance().isAdmin());
        Session.getInstance().logout();
        Session.getInstance().login(s);
        check("session logged in", Session.getInstance().isLoggedIn());
        Session.getInstance().logout();
        check("session logged out", !Session.getInstance().isLoggedIn());
        System.exit(fails);
    }
}
