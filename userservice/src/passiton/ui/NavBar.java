package passiton.ui;

import passiton.service.Session;

import javax.swing.*;
import java.awt.*;

/** Top navbar. Shows Login/Register when logged out, "Hi, name" + Logout when logged in. */
public class NavBar extends JPanel {
    private final JLabel status = new JLabel();
    private final JButton loginBtn = new JButton("Login");
    private final JButton registerBtn = new JButton("Register");
    private final JButton adminBtn = new JButton("Admin Panel");
    private final JButton logoutBtn = new JButton("Logout");

    public NavBar(Runnable onLogin, Runnable onRegister, Runnable onAdminPanel) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        setBackground(new Color(0x2F3E46));
        status.setForeground(Color.WHITE);

        loginBtn.addActionListener(e -> onLogin.run());
        registerBtn.addActionListener(e -> onRegister.run());
        adminBtn.addActionListener(e -> onAdminPanel.run());
        logoutBtn.addActionListener(e -> Session.getInstance().logout());

        add(status);
        add(loginBtn);
        add(registerBtn);
        add(adminBtn);
        add(logoutBtn);

        Session.getInstance().addListener(this::refresh);
        refresh();
    }

    /** Updates the navbar to match the current login state. */
    private void refresh() {
        Session s = Session.getInstance();
        boolean in = s.isLoggedIn();
        status.setText(in
                ? "Hi, " + s.getCurrentUser().getName() + " (" + s.getCurrentUser().getRole() + ")"
                : "Not logged in");
        loginBtn.setVisible(!in);
        registerBtn.setVisible(!in);
        adminBtn.setVisible(s.isAdmin());
        logoutBtn.setVisible(in);
        revalidate();
        repaint();
    }
}
