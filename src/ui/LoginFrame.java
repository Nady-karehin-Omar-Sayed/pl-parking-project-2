package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Users;
import services.ParkingManager;

public class LoginFrame extends JFrame {

    private ParkingManager manager;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    private static final Color BG_DARK = new Color(30, 30, 30);
    private static final Color BG_PANEL = new Color(40, 40, 40);
    private static final Color BG_FIELD = new Color(55, 55, 55);
    private static final Color TEXT_PRIMARY = new Color(220, 220, 220);
    private static final Color TEXT_DIM = new Color(140, 140, 140);
    private static final Color ACCENT_BLUE = new Color(50, 110, 210);
    private static final Color ACCENT_RED = new Color(200, 60, 60);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    public LoginFrame(ParkingManager manager) {
        this.manager = manager;
        buildUI();
    }

    private void buildUI() {
        setTitle("Parking System — Login");
        setSize(380, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_PANEL);
        card.setBorder(new EmptyBorder(36, 44, 36, 44));

        JLabel title = new JLabel("Parking System");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to continue");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_DIM);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLbl = darkLabel("Username");
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField = darkField();

        JLabel passLbl = darkLabel("Password");
        passLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField = new JPasswordField();
        passwordField.setBackground(BG_FIELD);
        passwordField.setForeground(TEXT_PRIMARY);
        passwordField.setCaretColor(TEXT_PRIMARY);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(4, 8, 4, 8)));
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        errorLabel.setForeground(ACCENT_RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(ACCENT_BLUE);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ActionListener onLogin = e -> handleLogin();
        loginBtn.addActionListener(onLogin);
        passwordField.addActionListener(onLogin);

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 24)));
        card.add(userLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(usernameField);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(passLbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(passwordField);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(errorLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(loginBtn);

        add(card);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        Users user = manager.login(username, password);

        if (user == null) {
            errorLabel.setText("Invalid username or password.");
            passwordField.setText("");
            return;
        }
        this.setVisible(false);

        switch (user.getRole()) {
            case "ADMIN":
                new AdminMenu(manager);
                break;
            case "ENTRY_OPERATOR":
                new EntryOperatorMenu(manager);
                break;
            case "EXIT_OPERATOR":
                new ExitOperatorMenu(manager);
                break;
            default:
                errorLabel.setText("Unknown role: " + user.getRole());
                this.setVisible(true);
        }
    }

    private JTextField darkField() {
        JTextField f = new JTextField();
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(4, 8, 4, 8)));
        return f;
    }

    private JLabel darkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_DIM);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
}