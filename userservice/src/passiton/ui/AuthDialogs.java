package passiton.ui;

import passiton.model.User;
import passiton.service.Session;
import passiton.service.UserService;
import passiton.util.PassItOnException;

import javax.swing.*;
import java.awt.*;

/** Login and Register dialogs. */
public final class AuthDialogs {
    private AuthDialogs() { }

    public static void showLogin(Component parent, UserService users) {
        JTextField email = new JTextField(18);
        JPasswordField pass = new JPasswordField(18);
        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.add(new JLabel("Email:"));    p.add(email);
        p.add(new JLabel("Password:")); p.add(pass);

        while (JOptionPane.showConfirmDialog(parent, p, "Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                User s = users.login(email.getText(), new String(pass.getPassword()));
                Session.getInstance().login(s);
                return;
            } catch (PassItOnException ex) {
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Login failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void showRegister(Component parent, UserService users) {
        JTextField name = new JTextField(18);
        JTextField regNo = new JTextField(18);
        JTextField dept = new JTextField(18);
        JSpinner sem = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JTextField email = new JTextField(18);
        JPasswordField pass = new JPasswordField(18);

        JPanel p = new JPanel(new GridLayout(0, 2, 6, 6));
        p.add(new JLabel("Name:"));         p.add(name);
        p.add(new JLabel("Register No:"));  p.add(regNo);
        p.add(new JLabel("Department:"));   p.add(dept);
        p.add(new JLabel("Semester:"));     p.add(sem);
        p.add(new JLabel("Email:"));        p.add(email);
        p.add(new JLabel("Password:"));     p.add(pass);

        while (JOptionPane.showConfirmDialog(parent, p, "Register",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try {
                users.register(name.getText(), regNo.getText(), dept.getText(),
                        (Integer) sem.getValue(), email.getText(), new String(pass.getPassword()));
                JOptionPane.showMessageDialog(parent, "Registered! You can log in now.");
                return;
            } catch (PassItOnException ex) {
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Registration failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
