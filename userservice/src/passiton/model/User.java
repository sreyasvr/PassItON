package passiton.model;

import passiton.util.PasswordHasher;

/** Abstract base class: common id, name, email and password logic. */
public abstract class User {
    private final int id;
    private String name;
    private String email;
    private final String passwordHash;   // encapsulated: only the hash is kept

    protected User(int id, String name, String email, String rawPassword) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = PasswordHasher.hash(rawPassword);
    }

    public int getId()          { return id; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }

    public void setName(String name)   { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    /** Compares hashes, never plain text. */
    public boolean checkPassword(String rawPassword) {
        return passwordHash.equals(PasswordHasher.hash(rawPassword));
    }

    /** Overridden by subclasses (polymorphism). */
    public abstract String getRole();
}
