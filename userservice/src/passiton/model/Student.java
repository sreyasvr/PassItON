package passiton.model;

/** A campus student who can list and request resources. */
public class Student extends User {
    private final String registerNo;
    private String department;
    private int semester;

    public Student(int id, String name, String registerNo, String department,
                   int semester, String email, String rawPassword) {
        super(id, name, email, rawPassword);
        this.registerNo = registerNo;
        this.department = department;
        this.semester = semester;
    }

    public String getRegisterNo() { return registerNo; }
    public String getDepartment() { return department; }
    public int getSemester()      { return semester; }

    @Override
    public String getRole() { return "STUDENT"; }

    public String display() {
        return getName() + " (" + registerNo + ", " + department + ", S" + semester + ")";
    }

    @Override
    public String toString() { return display(); }
}
