package passiton.model;

/** Administrator account. Can view and remove students. */
public class Admin extends User {

    public Admin(int id, String name, String email, String rawPassword) {
        super(id, name, email, rawPassword);
    }

    @Override
    public String getRole() { return "ADMIN"; }
}
