package passiton;

import passiton.service.UserService;
import passiton.ui.AuthDialogs;
import passiton.ui.NavBar;

import javax.swing.*;
import java.awt.*;

/** Demo window for the User & Authentication module. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserService users = new UserService();
            JFrame frame = new JFrame("PassItOn - Campus Resource Sharing System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            NavBar nav = new NavBar(
                    () -> AuthDialogs.showLogin(frame, users),
                    () -> AuthDialogs.showRegister(frame, users));
            frame.add(nav, BorderLayout.NORTH);
            frame.add(new JLabel("Welcome to PassItOn", SwingConstants.CENTER), BorderLayout.CENTER);

            frame.setSize(640, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
