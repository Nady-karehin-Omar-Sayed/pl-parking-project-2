package ui;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.Payment;
import models.Ticket;
import services.ParkingManager;

public class ExitOperatorMenu extends JFrame {

    private final  ParkingManager manager;
    private DefaultTableModel ticketsTableModel;
    private JTextField ticketIdField;
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
    private static final Color ACCENT_ORANGE = new Color(200, 110,  30);
    private static final Color BORDER_COLOR  = new Color(60,   60,  60);

    public ExitOperatorMenu(ParkingManager manager) {
        this.manager = manager;
        buildUI();
    }

    private void buildUI() {
        setTitle("Exit Operator");
        setSize(660, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(10, 10));

        add(buildTicketsPanel(), BorderLayout.CENTER);
        add(buildFormPanel(),    BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel buildTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 8));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new EmptyBorder(14, 14, 0, 14));

        JLabel title = new JLabel("Currently Parked Cars");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(TEXT_PRIMARY);

        String[] cols = {"Ticket ID", "Plate", "Spot", "Entry Time"};
        ticketsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = styledTable(ticketsTableModel);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0)
                ticketIdField.setText(ticketsTableModel.getValueAt(row, 0).toString());
        });

        refreshTicketsTable();

        panel.add(title, 
            BorderLayout.NORTH);
        panel.add(new JScrollPane(table),
            BorderLayout.CENTER);
        return panel;
    }

    private void refreshTicketsTable() {
        ticketsTableModel.setRowCount(0);
        for (Ticket t : manager.getTickets()) {
            if (!t.isPaid()) {
                ticketsTableModel.addRow(new Object[]{
                    t.getTicketId(),
                    t.getPlateNumber(),
                    t.getSpotId(),
                    new Date(t.getEntryTimeMillis()).toString()
                });
            }
        }
    }

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setBackground(BG_PANEL);
        wrapper.setBorder(new EmptyBorder(0, 14, 14, 14));

        JPanel form = new JPanel(new GridLayout(1, 2, 8, 8));
        form.setBackground(BG_PANEL);
        form.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "Process Exit & Payment",
            0, 0, new Font("SansSerif", Font.PLAIN, 12), TEXT_DIM));

        ticketIdField = darkField();
        form.add(darkLabel("Ticket ID:")); form.add(ticketIdField);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif",
         Font.PLAIN, 12));

        JButton btn = makeButton("Process Payment", ACCENT_ORANGE);
        btn.addActionListener(e -> handleExit());

        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setBackground(BG_PANEL);
        bottom.add(statusLabel, 
            BorderLayout.CENTER);
        bottom.add(btn,         
            BorderLayout.EAST);

        wrapper.add(form,  
             BorderLayout.CENTER);
        wrapper.add(bottom,
             BorderLayout.SOUTH);
        return wrapper;
    }

    private void handleExit() {
        String txt = ticketIdField.getText().trim();
        if (txt.isEmpty()) {
            showStatus("Please enter a Ticket ID.", ACCENT_RED);
            return;
        }

        int ticketId;
        try { ticketId = Integer.parseInt(txt); }
        catch (NumberFormatException ex) {
            showStatus("Ticket ID must be a number.", ACCENT_RED);
            return;
        }

        Payment payment = manager.processPayment(ticketId);
        if (payment != null) {
            showStatus("Paid!", ACCENT_GREEN);
            ticketIdField.setText("");
            refreshTicketsTable(); 
        } else {
            showStatus("Ticket not found or already paid.", ACCENT_RED);
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