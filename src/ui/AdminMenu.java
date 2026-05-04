package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.ParkingSpot;
import models.Users;
import services.ParkingManager;

public class AdminMenu extends JFrame {

    private final ParkingManager manager;
    private DefaultTableModel usersTableModel;
    private JTextField nameField, usernameField, passwordField;
    private JComboBox<String> roleBox;
    private JTextField userIdField;
    private JComboBox<String> zoneBox;
    private DefaultTableModel spotsTableModel;
    private JTextArea reportArea;
    private JLabel statusBarLabel;
    private static final Color BG_DARK = new Color(30, 30, 30);
    private static final Color BG_PANEL = new Color(40, 40, 40);
    private static final Color BG_FIELD = new Color(55, 55, 55);
    private static final Color BG_TABLE_ROW = new Color(45, 45, 45);
    private static final Color BG_TABLE_HEAD = new Color(35, 35, 35);
    private static final Color BG_STATUS = new Color(25, 25, 25);
    private static final Color TEXT_PRIMARY = new Color(220, 220, 220);
    private static final Color TEXT_DIM = new Color(140, 140, 140);
    private static final Color ACCENT_GREEN = new Color(50, 160, 80);
    private static final Color ACCENT_BLUE = new Color(50, 110, 210);
    private static final Color ACCENT_RED = new Color(200, 60, 60);
    private static final Color ACCENT_ORANGE = new Color(200, 110, 30);
    private static final Color BORDER_COLOR = new Color(60, 60, 60);

    public AdminMenu(ParkingManager manager) {
        this.manager = manager;
        applyGlobalDarkDefaults();
        buildUI();
    }

    private void applyGlobalDarkDefaults() {
        UIManager.put("Panel.background", BG_PANEL);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.background", BG_FIELD);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("TextField.border",
                BorderFactory.createLineBorder(BORDER_COLOR));
        UIManager.put("ComboBox.background", BG_FIELD);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", ACCENT_BLUE);
        UIManager.put("Table.background", BG_TABLE_ROW);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER_COLOR);
        UIManager.put("Table.selectionBackground", ACCENT_BLUE);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("TableHeader.background", BG_TABLE_HEAD);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("Viewport.background", BG_TABLE_ROW);
        UIManager.put("TabbedPane.background", BG_DARK);
        UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
        UIManager.put("TabbedPane.selected", BG_PANEL);
        UIManager.put("TitledBorder.titleColor", TEXT_DIM);
        UIManager.put("OptionPane.background", BG_PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
    }

    private void buildUI() {
        setTitle("Admin Panel");
        setSize(780, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);

        add(buildToolbar(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bar.setBackground(
                new Color(35, 35, 35));
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JLabel lbl = new JLabel("Open window:");
        lbl.setForeground(TEXT_DIM);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton entryBtn = makeButton(" Entry Operator", ACCENT_GREEN);
        JButton exitBtn = makeButton(" Exit Operator", ACCENT_ORANGE);

        entryBtn.addActionListener(e -> new EntryOperatorMenu(manager));
        exitBtn.addActionListener(e -> new ExitOperatorMenu(manager));

        bar.add(lbl);
        bar.add(entryBtn);
        bar.add(exitBtn);
        return bar;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_DARK);
        tabs.setForeground(TEXT_PRIMARY);
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.addTab("Users", buildUsersTab());
        tabs.addTab("Spots", buildSpotsTab());
        tabs.addTab("Reports", buildReportsTab());
        return tabs;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5));
        bar.setBackground(BG_STATUS);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        statusBarLabel = new JLabel();
        statusBarLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusBarLabel.setForeground(TEXT_DIM);

        bar.add(statusBarLabel);
        refreshStatusBar();
        return bar;
    }

    private void refreshStatusBar() {
        long count = manager.getTickets().stream()
                .filter(t -> !t.isPaid())
                .count();
        statusBarLabel.setText("Cars currently parked: " + count);
    }

    private JPanel buildUsersTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        String[] cols = { "ID", "Name", "Username", "Role" };
        usersTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(usersTableModel);
        refreshUsersTable();

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), "Add / Update User",
                0, 0, new Font("SansSerif", Font.PLAIN, 12), TEXT_DIM));

        nameField = darkField();
        usernameField = darkField();
        passwordField = darkField();
        userIdField = darkField();
        roleBox = new JComboBox<>(new String[] { "ADMIN", "ENTRY_OPERATOR", "EXIT_OPERATOR" });
        roleBox.setBackground(BG_FIELD);
        roleBox.setForeground(TEXT_PRIMARY);

        form.add(darkLabel("Name:"));
        form.add(nameField);
        form.add(darkLabel("Username:"));
        form.add(usernameField);
        form.add(darkLabel("Password:"));
        form.add(passwordField);
        form.add(darkLabel("Role:"));
        form.add(roleBox);
        form.add(darkLabel("User ID (update/delete):"));
        form.add(userIdField);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(BG_PANEL);

        JButton addBtn = makeButton("Add", ACCENT_GREEN);
        JButton updateBtn = makeButton("Update", ACCENT_BLUE);
        JButton deleteBtn = makeButton("Delete", ACCENT_RED);

        addBtn.addActionListener(e -> {
            if (fieldsEmpty())
                return;
            manager.addUser(nameField.getText(), usernameField.getText(),
                    passwordField.getText(), (String) roleBox.getSelectedItem());
            refreshUsersTable();
            clearUserFields();
        });
        updateBtn.addActionListener(e -> {
            int id = parseId(userIdField.getText());
            if (id == -1 || fieldsEmpty())
                return;
            manager.updateUser(id, nameField.getText(), usernameField.getText(),
                    passwordField.getText(), (String) roleBox.getSelectedItem());
            refreshUsersTable();
            clearUserFields();
        });
        deleteBtn.addActionListener(e -> {
            int id = parseId(userIdField.getText());
            if (id == -1)
                return;
            manager.deleteUser(id);
            refreshUsersTable();
            clearUserFields();
        });

        btnRow.add(addBtn);
        btnRow.add(updateBtn);
        btnRow.add(deleteBtn);

        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setBackground(BG_PANEL);
        south.add(form,
                BorderLayout.CENTER);
        south.add(btnRow,
                BorderLayout.SOUTH);

        panel.add(new JScrollPane(table),
                BorderLayout.CENTER);
        panel.add(south,
                BorderLayout.SOUTH);
        return panel;
    }

    private void refreshUsersTable() {
        usersTableModel.setRowCount(0);
        for (Users u : manager.getUsers())
            usersTableModel.addRow(new Object[] { u.getId(), u.getName(), u.getUsername(), u.getRole() });
    }

    private void clearUserFields() {
        nameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        userIdField.setText("");
    }

    private boolean fieldsEmpty() {
        return nameField.getText().trim().isEmpty()
                || usernameField.getText().trim().isEmpty()
                || passwordField.getText().trim().isEmpty();
    }

    private JPanel buildSpotsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        String[] cols = { "Spot ID", "Zone", "Status" };
        spotsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(spotsTableModel);
        refreshSpotsTable();

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR), "Add Spot",
                0, 0, new Font("SansSerif", Font.PLAIN, 12), TEXT_DIM));

        zoneBox = new JComboBox<>(new String[] { "A", "B", "C", "D" });
        zoneBox.setBackground(BG_FIELD);
        zoneBox.setForeground(TEXT_PRIMARY);

        JButton addBtn = makeButton("Add Spot", ACCENT_GREEN);
        addBtn.addActionListener(e -> {
            manager.addSpot((String) zoneBox.getSelectedItem());
            refreshSpotsTable();
        });

        form.add(darkLabel("Zone:"));
        form.add(zoneBox);
        form.add(addBtn);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshSpotsTable() {
        spotsTableModel.setRowCount(0);
        for (ParkingSpot s : manager.getSpots())
            spotsTableModel.addRow(new Object[] {
                    s.GetSpotId(), s.GetZone(),
                    s.GetisOccupied() ? "Occupied" : "Free"
            });
    }

    private JPanel buildReportsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setEditable(false);
        reportArea.setBackground(new Color(35, 35, 35));
        reportArea.setForeground(TEXT_PRIMARY);
        reportArea.setCaretColor(TEXT_PRIMARY);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setBackground(BG_PANEL);

        JButton shiftBtn = makeButton("Shift Report", ACCENT_BLUE);
        JButton parkedBtn = makeButton("Parked Cars Report", ACCENT_GREEN);

        shiftBtn.addActionListener(e -> reportArea.setText(manager.getShiftReport()));
        parkedBtn.addActionListener(e -> {
            reportArea.setText(manager.getParkedCarsReport());
            refreshStatusBar();
        });

        btnRow.add(shiftBtn);
        btnRow.add(parkedBtn);

        panel.add(btnRow, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        return panel;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setBackground(BG_TABLE_ROW);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(BG_TABLE_HEAD);
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        return table;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    private JTextField darkField() {
        JTextField f = new JTextField();
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(4, 6, 4, 6)));
        return f;
    }
    private JLabel darkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_DIM);
        l.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return l;
    }
    private int parseId(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
}