package passiton.ui;

import passiton.model.Student;
import passiton.service.Session;
import passiton.service.UserService;
import passiton.util.PassItOnException;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Simple admin screen: list registered students and remove one. */
public final class AdminPanel {
    private AdminPanel() { }

    public static void show(Component parent, UserService users) {
        DefaultListModel<Student> model = new DefaultListModel<>();
        JList<Student> list = new JList<>(model);
        JButton remove = new JButton("Remove selected student");

        Runnable reload = () -> {
            model.clear();
            try {
                List<Student> all = users.getAllStudents(Session.getInstance().getCurrentUser());
                for (Student s : all) model.addElement(s);
            } catch (PassItOnException ex) {
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        };
        reload.run();

        remove.addActionListener(e -> {
            Student sel = list.getSelectedValue();
            if (sel == null) return;
            try {
                users.removeStudent(Session.getInstance().getCurrentUser(), sel.getEmail());
                reload.run();
            } catch (PassItOnException ex) {
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.add(new JScrollPane(list), BorderLayout.CENTER);
        p.add(remove, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(420, 260));
        JOptionPane.showMessageDialog(parent, p, "Admin Panel - Registered Students",
                JOptionPane.PLAIN_MESSAGE);
    }
}
