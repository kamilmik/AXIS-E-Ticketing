package view;

import controller.PemesananController;
import controller.SessionManager;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.JadwalAntarkota;
import model.Penumpang;
import model.Tiket;

public class PilihJadwalView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PilihJadwalView.class.getName());
    private final PemesananController controller;
    private DefaultTableModel modelJadwal;
    private JButton[] btnPaxCards;
    private JCheckBox chkSamaPemesan;

    public PilihJadwalView() {
        this(null, 1);
    }

    public PilihJadwalView(List<JadwalAntarkota> hasilJadwal, int jumlahPenumpang) {
        initComponents();
        this.setSize(1200, 920);
        this.setLocationRelativeTo(null);

        this.controller = new PemesananController();
        this.controller.inisialisasiPencarian(hasilJadwal, jumlahPenumpang);

        rakitTampilan();
        setupEvents();
        this.controller.isiTabelJadwal(modelJadwal);
        if (tblJadwal.getRowCount() > 0) {
            tblJadwal.setRowSelectionInterval(0, 0);
        }
    }

    private void rakitTampilan() {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(0, 15));
        getContentPane().setBackground(new Color(244, 246, 245));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(7, 59, 39));
        pnlHeader.setPreferredSize(new Dimension(1200, 95));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JLabel lblTitle = new JLabel("1. Pilih Jadwal & Isi Data Penumpang");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JLabel lblStep = new JLabel(" ");
        lblStep.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblStep.setForeground(new Color(165, 214, 167));
        pnlHeader.add(lblStep, BorderLayout.EAST);

        JPanel pnlBody = new JPanel(new BorderLayout(25, 20));
        pnlBody.setOpaque(false);
        pnlBody.setBorder(BorderFactory.createEmptyBorder(15, 60, 25, 60));

        JPanel pnlLeftJadwal = new JPanel(new BorderLayout(0, 10));
        pnlLeftJadwal.setOpaque(false);
        JLabel lblSubJadwal = new JLabel("Daftar Jadwal Kereta Tersedia:");
        lblSubJadwal.setFont(new Font("Verdana", Font.BOLD, 14));
        lblSubJadwal.setForeground(new Color(7, 59, 39));

        modelJadwal = new DefaultTableModel(
            new String[]{"ID", "Nama Kereta", "Berangkat", "Tiba", "Harga / Pax"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblJadwal.setModel(modelJadwal);
        tblJadwal.setRowHeight(36);
        tblJadwal.setFont(new Font("Verdana", Font.PLAIN, 13));
        tblJadwal.setSelectionBackground(new Color(0, 108, 59));
        tblJadwal.setSelectionForeground(Color.WHITE);
        tblJadwal.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 13));

        pnlLeftJadwal.add(lblSubJadwal, BorderLayout.NORTH);
        pnlLeftJadwal.add(new JScrollPane(tblJadwal), BorderLayout.CENTER);

        JPanel pnlRightPax = new JPanel();
        pnlRightPax.setLayout(new BoxLayout(pnlRightPax, BoxLayout.Y_AXIS));
        pnlRightPax.setOpaque(false);
        pnlRightPax.setPreferredSize(new Dimension(430, 600));

        Penumpang current = SessionManager.getCurrentPenumpang();
        String namaLogin = (current != null) ? current.getNamaLengkap() : "Penumpang AXIS";
        String infoKontak = (current != null) ? current.getEmail() + " | " + current.getNomorTelepon() : "-";

        JPanel cardPemesan = new JPanel(new GridLayout(3, 1, 0, 4));
        cardPemesan.setBackground(Color.WHITE);
        cardPemesan.setMaximumSize(new Dimension(430, 95));
        cardPemesan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 222), 1, true),
            BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        JLabel lblLoginAs = new JLabel(namaLogin);
        lblLoginAs.setFont(new Font("Verdana", Font.BOLD, 13));
        lblLoginAs.setForeground(new Color(7, 59, 39));
        JLabel lblTitleKontak = new JLabel(" ");
        lblTitleKontak.setFont(new Font("Verdana", Font.PLAIN, 11));
        lblTitleKontak.setForeground(Color.GRAY);
        JLabel lblIsiKontak = new JLabel(infoKontak);
        lblIsiKontak.setFont(new Font("Verdana", Font.PLAIN, 12));
        cardPemesan.add(lblLoginAs);
        cardPemesan.add(lblTitleKontak);
        cardPemesan.add(lblIsiKontak);

        JPanel cardPaxContainer = new JPanel();
        cardPaxContainer.setLayout(new BoxLayout(cardPaxContainer, BoxLayout.Y_AXIS));
        cardPaxContainer.setBackground(Color.WHITE);
        cardPaxContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 222), 1, true),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblHeaderPax = new JLabel("Detail Penumpang" + controller.getJumlahPenumpang() );
        lblHeaderPax.setFont(new Font("Verdana", Font.BOLD, 14));
        lblHeaderPax.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPaxContainer.add(lblHeaderPax);
        cardPaxContainer.add(Box.createVerticalStrut(12));

        chkSamaPemesan = new JCheckBox("Penumpang 1 sama dengan pemesan");
        chkSamaPemesan.setFont(new Font("Verdana", Font.PLAIN, 13));
        chkSamaPemesan.setOpaque(false);
        chkSamaPemesan.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPaxContainer.add(chkSamaPemesan);
        cardPaxContainer.add(Box.createVerticalStrut(12));

        int jml = controller.getJumlahPenumpang();
        btnPaxCards = new JButton[jml];
        for (int i = 0; i < jml; i++) {
            final int index = i;
            JButton btnPax = new JButton();
            btnPax.setHorizontalAlignment(SwingConstants.LEFT);
            btnPax.setFont(new Font("Verdana", Font.PLAIN, 13));
            btnPax.setBackground(new Color(248, 250, 249));
            btnPax.setForeground(new Color(60, 60, 60));
            btnPax.setMaximumSize(new Dimension(400, 48));
            btnPax.setPreferredSize(new Dimension(380, 48));
            btnPax.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnPax.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnPax.addActionListener(e -> bukaDialogInputPenumpang(index));
            btnPaxCards[i] = btnPax;
            cardPaxContainer.add(btnPax);
            cardPaxContainer.add(Box.createVerticalStrut(10));
        }

        perbaruiTampilanKartuPenumpang();

        pnlRightPax.add(cardPemesan);
        pnlRightPax.add(Box.createVerticalStrut(15));
        pnlRightPax.add(new JScrollPane(cardPaxContainer));

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setOpaque(false);

        btnKembali.setText("Kembali");
        btnKembali.setFont(new Font("Verdana", Font.BOLD, 14));
        btnKembali.setPreferredSize(new Dimension(190, 48));
        btnKembali.setBackground(new Color(255, 248, 240));
        btnKembali.setForeground(new Color(216, 67, 21));

        btnPilihJadwal.setText("Lanjutkan");
        btnPilihJadwal.setFont(new Font("Verdana", Font.BOLD, 14));
        btnPilihJadwal.setPreferredSize(new Dimension(300, 48));
        btnPilihJadwal.setBackground(new Color(0, 108, 59));
        btnPilihJadwal.setForeground(Color.WHITE);

        pnlBottom.add(btnKembali);
        pnlBottom.add(btnPilihJadwal);

        pnlBody.add(pnlLeftJadwal, BorderLayout.CENTER);
        pnlBody.add(pnlRightPax, BorderLayout.EAST);
        pnlBody.add(pnlBottom, BorderLayout.SOUTH);

        getContentPane().add(pnlHeader, BorderLayout.NORTH);
        getContentPane().add(pnlBody, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void perbaruiTampilanKartuPenumpang() {
        for (int i = 0; i < btnPaxCards.length; i++) {
            Tiket t = controller.getTiketPenumpang(i);
            if (t == null) {
                btnPaxCards[i].setText("\u270E  Isi Detail Penumpang " + (i + 1) + " (Dewasa)");
                btnPaxCards[i].setBackground(new Color(248, 250, 249));
                btnPaxCards[i].setForeground(new Color(100, 100, 100));
            } else {
                btnPaxCards[i].setText("\u2713  Penumpang " + (i + 1) + ": " + t.getNamaPenumpangTiket() + " (" + t.getNoIdentitas() + ")");
                btnPaxCards[i].setBackground(new Color(232, 245, 233));
                btnPaxCards[i].setForeground(new Color(0, 108, 59));
            }
        }
    }

    private void bukaDialogInputPenumpang(int index) {
        Tiket existing = controller.getTiketPenumpang(index);
        JTextField txtNama = new JTextField(existing != null ? existing.getNamaPenumpangTiket() : "");
        JTextField txtTelp = new JTextField(controller.getTeleponPenumpang(index));
        JTextField txtNik = new JTextField(existing != null ? existing.getNoIdentitas() : "");

        Object[] formPopup = {
            "Nama Lengkap Penumpang:", txtNama,
            "Nomor Telepon:", txtTelp,
            "Nomor NIK / Paspor:", txtNik
        };

        int opt = JOptionPane.showConfirmDialog(
            this, formPopup,
            "Isi Detail Penumpang " + (index + 1) + " (Dewasa)",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (opt == JOptionPane.OK_OPTION) {
            String res = controller.simpanDataPenumpang(
                index,
                txtNik.getText().trim(),
                txtNama.getText().trim(),
                txtTelp.getText().trim()
            );
            if (!res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, res, "Validasi Input", JOptionPane.ERROR_MESSAGE);
                bukaDialogInputPenumpang(index);
            } else {
                perbaruiTampilanKartuPenumpang();
            }
        }
    }

    private void setupEvents() {
        for (java.awt.event.ActionListener al : btnPilihJadwal.getActionListeners()) btnPilihJadwal.removeActionListener(al);
        for (java.awt.event.ActionListener al : btnKembali.getActionListeners()) btnKembali.removeActionListener(al);

        chkSamaPemesan.addActionListener(e -> {
            controller.setSamaDenganPemesan(chkSamaPemesan.isSelected());
            perbaruiTampilanKartuPenumpang();
        });

        btnPilihJadwal.addActionListener(e -> {
            int row = tblJadwal.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih salah satu jadwal kereta pada tabel di sebelah kiri!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!controller.isSemuaPenumpangLengkap()) {
                JOptionPane.showMessageDialog(this, "Silakan lengkapi seluruh Detail Penumpang terlebih dahulu!", "Data Belum Lengkap", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = tblJadwal.convertRowIndexToModel(row);
            int idJadwal = Integer.parseInt(modelJadwal.getValueAt(modelRow, 0).toString());
            controller.setJadwalTerpilih(idJadwal);

            new ReviewPemesananView(controller).setVisible(true);
            this.dispose();
        });

        btnKembali.addActionListener(e -> {
            new MenuUtamaView().setVisible(true);
            this.dispose();
        });
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJadwal = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnPilihJadwal = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        tblJadwal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tblJadwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblJadwal);

        jLabel2.setFont(new java.awt.Font("Verdana", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("1. Pilih Jadwal & Isi Data Penumpang");

        btnPilihJadwal.setText("Lanjutkan");
        btnPilihJadwal.addActionListener(this::btnPilihJadwalActionPerformed);

        btnKembali.setText("Kembali");

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Daftar Jadwal Kereta Tersedia:");

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Detail Penumpang");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jRadioButton1.setText("Penumpang 1 sama dengan pemesan");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(241, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Nama user");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Email + NIK");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(16, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(63, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(17, 17, 17)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 500, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnKembali)
                        .addGap(51, 51, 51)
                        .addComponent(btnPilihJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(72, 72, 72))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKembali)
                    .addComponent(btnPilihJadwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
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

    private void btnPilihJadwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPilihJadwalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPilihJadwalActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new PilihJadwalView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnPilihJadwal;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblJadwal;
    // End of variables declaration//GEN-END:variables
}
