package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.ParkingSpot;
import models.Ticket;
import services.ParkingManager;

public class EntryOperatorMenu extends JFrame {

    private ParkingManager manager;
    private DefaultTableModel spotsTableModel;
    private JTextField plateField;
    private JTextField spotIdField;
    private JLabel statusLabel;

    private static final Color BG_DARK       = new Color(30,  30,  30);
    private static final Color BG_PANEL      = new Color(40,  40,  40);
    private static final Color BG_FIELD      = new Color(55,  55,  55);
    private static final Color BG_TABLE_ROW  = new Color(45,  45,  45);
    private static final Color BG_TABLE_HEAD = new Color(35,  35,  35);
    private static final Color TEXT_PRIMARY  = new Color(220, 220, 220);
    private static final Color TEXT_DIM      = new Color(140, 140, 140);
    private static final Color ACCENT_GREEN  = new Color(50,  160,  80);
    private static final Color ACCENT_BLUE   = new Color(50,  110, 210);
    private static final Color ACCENT_RED    = new Color(200,  60,  60);
    private static final Color BORDER_COLOR  = new Color(60,   60,  60);

    public EntryOperatorMenu(ParkingManager manager) {
        this.manager = manager;
        buildUI();
    }

    private void buildUI() {
        setTitle("Entry Operator");
        setSize(680, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(10, 10));
        add(buildSpotsPanel(), BorderLayout.CENTER);
        add(buildFormPanel(),  BorderLayout.SOUTH);
        setVisible(true);
    }
    private JPanel buildSpotsPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 8));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(14, 14, 0, 14));

        JLabel title = new JLabel("Parking Spots");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);

        String[] cols = {"Spot ID", "Zone", "Status"};
        spotsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = styledTable(spotsTableModel);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0)
                spotIdField.setText(spotsTableModel.getValueAt(row, 0).toString());
        });

        refreshSpotsTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(BG_DARK);
        scroll.getViewport().setBackground(BG_TABLE_ROW);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        panel.add(title,  BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshSpotsTable() {
        spotsTableModel.setRowCount(0);
        for (ParkingSpot s : manager.getSpots())
            spotsTableModel.addRow(new Object[]{
                s.GetSpotId(), s.GetZone(),
                s.GetisOccupied() ? "Occupied" : "Free"
            });
    }

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setBackground(BG_PANEL);
        wrapper.setBorder(new EmptyBorder(0, 14, 14, 14));
        JPanel entryForm = new JPanel(new GridLayout(2, 2, 8, 8));
        entryForm.setBackground(BG_PANEL);
        entryForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "Register Car Entry",
            0, 0, new Font("SansSerif", Font.PLAIN, 12), TEXT_DIM));

        plateField  = darkField();
        spotIdField = darkField();

        entryForm.add(darkLabel("Plate Number:")); entryForm.add(plateField);
        entryForm.add(darkLabel("Spot ID:"));      entryForm.add(spotIdField);
        JPanel addSpotForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        addSpotForm.setBackground(BG_PANEL);
        addSpotForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "Add Spot",
            0, 0, new Font("SansSerif", Font.PLAIN, 12), TEXT_DIM));

        JComboBox<String> zoneBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        zoneBox.setBackground(BG_FIELD);
        zoneBox.setForeground(TEXT_PRIMARY);

        JButton addSpotBtn = makeButton("Add Spot", ACCENT_BLUE);
        addSpotBtn.addActionListener(e -> {
            manager.addSpot((String) zoneBox.getSelectedItem());
            refreshSpotsTable();
        });

        addSpotForm.add(darkLabel("Zone:"));
        addSpotForm.add(zoneBox);
        addSpotForm.add(addSpotBtn);
        JPanel formsRow = new JPanel(new GridLayout(1, 2, 10, 0));
        formsRow.setBackground(BG_PANEL);
        formsRow.add(entryForm);
        formsRow.add(addSpotForm);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton entryBtn = makeButton("Register Entry", ACCENT_GREEN);
        entryBtn.addActionListener(e -> handleEntry());

        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setBackground(BG_PANEL);
        bottom.add(statusLabel, BorderLayout.CENTER);
        bottom.add(entryBtn,    BorderLayout.EAST);

        wrapper.add(formsRow, BorderLayout.CENTER);
        wrapper.add(bottom,   BorderLayout.SOUTH);
        return wrapper;
    }

    private void handleEntry() {
        String plate   = plateField.getText().trim();
        String spotTxt = spotIdField.getText().trim();

        if (plate.isEmpty() || spotTxt.isEmpty()) {
            showStatus("Please fill in all fields.", ACCENT_RED);
            return;
        }

        int spotId;
        try { spotId = Integer.parseInt(spotTxt); }
        catch (NumberFormatException ex) {
            showStatus("Spot ID must be a number.", ACCENT_RED);
            return;
        }

        Ticket ticket = manager.createTicket(plate, spotId);
        if (ticket != null) {
            showStatus("Ticket #" + ticket.getTicketId() + " created for: " + plate, ACCENT_GREEN);
            plateField.setText("");
            spotIdField.setText("");
            refreshSpotsTable();
        } else {
            showStatus("Spot unavailable or not found.", ACCENT_RED);
        }
    }

    private void showStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setBackground(BG_TABLE_ROW);
        t.setForeground(TEXT_PRIMARY);
        t.setGridColor(BORDER_COLOR);
        t.setSelectionBackground(ACCENT_BLUE);
        t.setSelectionForeground(Color.WHITE);
        t.getTableHeader().setBackground(BG_TABLE_HEAD);
        t.getTableHeader().setForeground(TEXT_PRIMARY);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        return t;
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
}