/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.example.moogratalab.ui;

/**
 *
 * @author USER
 */
import com.example.moogratalab.moogata.model.Bill;
import com.example.moogratalab.moogata.model.DiningTable;
import com.example.moogratalab.moogata.model.Employee;
import com.example.moogratalab.moogata.service.BillService;
import com.example.moogratalab.moogata.service.TableService;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PosFrame extends javax.swing.JFrame {

// Table status colors — Green: Vacant / Pink: In Use / Yellow: Awaiting Payment
    private static final java.awt.Color COLOR_VACANT = new java.awt.Color(0xCDE5AD);
    private static final java.awt.Color COLOR_IN_USE = new java.awt.Color(0xF2C5D5);
    private static final java.awt.Color COLOR_AWAITING = new java.awt.Color(0xF6E991);

    private final TableService tableService = new TableService();
    private final BillService billService = new BillService();

    // Table data loaded from the database — ordered to match btnTable1–6
    private List<DiningTable> tables = new ArrayList<>();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PosFrame.class.getName());
    private Employee currentEmployee;

    public PosFrame(Employee employee) {
        ThaiFont.apply();
        this.currentEmployee = employee;
        initComponents();
        initCustomComponents();
        refreshTables();
        setLocationRelativeTo(null);
    }

    public PosFrame() {
        ThaiFont.apply();
        initComponents();
        initCustomComponents();
        refreshTables();
        setLocationRelativeTo(null);
    }

    private void initCustomComponents() {
        if (currentEmployee != null) {
            String roleText = currentEmployee.getRole() == 1 ? "Manager" : "Cashier";
            lblUser.setText("Employee: " + currentEmployee.getName() + " (" + roleText + ")");
        }

        // Bind Action Events to Table Buttons 1-6 and Refresh Button
        btnTable1.addActionListener(e -> tableClicked(0));
        btnTable2.addActionListener(e -> tableClicked(1));
        btnTable3.addActionListener(e -> tableClicked(2));
        btnTable4.addActionListener(e -> tableClicked(3));
        btnTable5.addActionListener(e -> tableClicked(4));
        btnTable6.addActionListener(e -> tableClicked(5));
        btnRefresh.addActionListener(e -> refreshTables());
    }

    public void refreshTables() {
        javax.swing.JButton[] tableButtons = {
            btnTable1, btnTable2, btnTable3, btnTable4, btnTable5, btnTable6
        };
        tables = tableService.getAllTables();
        int vacant = 0;
        int inUse = 0;
        for (int i = 0; i < tables.size() && i < tableButtons.length; i++) {
            DiningTable table = tables.get(i);
            javax.swing.JButton btn = tableButtons[i];
            String detail;
            java.awt.Color color;
            if (DiningTable.STATUS_IN_USE.equals(table.getStatus())) {
                detail = "In Use (" + table.getHeadcount() + " guests)";
                color = COLOR_IN_USE;
                inUse++;
            } else if (DiningTable.STATUS_AWAITING.equals(table.getStatus())) {
                detail = "Awaiting Payment";
                color = COLOR_AWAITING;
            } else {
                detail = "Vacant";
                color = COLOR_VACANT;
                vacant++;
            }
            btn.setText("<html><center><b>Table " + table.getNumber() + "</b><br>" + detail + "</center></html>");
            btn.setBackground(color);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
        }
        lblSummary.setText("Vacant: " + vacant + " tables · Occupied: " + inUse + " tables");
    }

    private void tableClicked(int index) {
        if (index >= tables.size()) return;
        DiningTable table = tables.get(index);
        if (table.isVacant()) {
            String input = JOptionPane.showInputDialog(this,
                    "Number of guests (Table " + table.getNumber() + "):",
                    "Open Table", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                return;
            }
            int headcount;
            try {
                headcount = Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                headcount = 0;
            }
            Bill bill = tableService.openTable(table, headcount, currentEmployee);
            if (bill == null) {
                JOptionPane.showMessageDialog(this,
                        "Failed to open table — must specify at least 1 guest\nor this table is already occupied.",
                        "Open Table Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            refreshTables();
        } else if (DiningTable.STATUS_IN_USE.equals(table.getStatus())) {
            Bill bill = billService.getOpenBillForTable(table.getId());
            if (bill == null) {
                JOptionPane.showMessageDialog(this,
                        "Table is in use but no open bill was found — check database.",
                        "Data Imbalance", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Open order screen for the existing bill
            // OrderFrame orderFrame = new OrderFrame(this, currentEmployee, table, bill);
            // orderFrame.setLocationRelativeTo(this);
            // orderFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Table " + table.getNumber() + " is awaiting payment.",
                    "Awaiting Payment", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        btnTable1 = new javax.swing.JButton();
        btnTable2 = new javax.swing.JButton();
        btnTable3 = new javax.swing.JButton();
        btnTable4 = new javax.swing.JButton();
        btnTable5 = new javax.swing.JButton();
        btnTable6 = new javax.swing.JButton();
        lblSummary = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblHeader.setFont(new java.awt.Font("Leelawadee", 0, 18)); // NOI18N
        lblHeader.setText("Moo Krata Table Layout");

        btnTable1.setBackground(new java.awt.Color(204, 255, 204));

        btnTable2.setBackground(new java.awt.Color(204, 255, 204));

        btnTable3.setBackground(new java.awt.Color(255, 204, 204));

        btnTable4.setBackground(new java.awt.Color(204, 255, 204));

        btnTable5.setBackground(new java.awt.Color(204, 255, 204));

        btnTable6.setBackground(new java.awt.Color(204, 255, 204));

        lblSummary.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSummary.setText("Vacant: 5 tables · Occupied: 1 table");

        btnRefresh.setText("Refresh");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(btnTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnRefresh)
                                .addGap(15, 15, 15))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTable4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable5, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTable6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSummary)
                    .addComponent(btnRefresh))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PosFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTable1;
    private javax.swing.JButton btnTable2;
    private javax.swing.JButton btnTable3;
    private javax.swing.JButton btnTable4;
    private javax.swing.JButton btnTable5;
    private javax.swing.JButton btnTable6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblUser;
    // End of variables declaration//GEN-END:variables
}
