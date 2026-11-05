package view;

import controller.PemesananController;
import controller.SessionManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.JadwalAntarkota;

public class ReviewPemesananView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReviewPemesananView.class.getName());
    private final PemesananController controller;
    private DefaultTableModel modelReview;
    private JLabel lblTotalTagihan;
    private JLabel lblSaldoUser;
    private JLabel lblInfoKereta;

    public ReviewPemesananView() {
        this(new PemesananController());
    }

    public ReviewPemesananView(PemesananController controller) {
        initComponents();
        this.setSize(1200, 920);
        this.setLocationRelativeTo(null);
        this.controller = controller;

        rakitTampilan();
        setupEvents();
        muatDataReviewDanTagihan();
    }

    private void muatDataReviewDanTagihan() {
        this.controller.isiTabelReview(modelReview);
        JadwalAntarkota j = controller.getJadwalTerpilih();
        if (j != null) {
            lblInfoKereta.setText(String.format("Kereta: %s  |  Berangkat: %s  |  Tiba: %s  |  Harga/Pax: Rp %,.0f",
                j.getNamaKereta(), j.getWaktuBerangkat(), j.getWaktuTiba(), j.getHargaDasar()));
        }
        lblTotalTagihan.setText(String.format("Rp %,.0f", controller.hitungTotalBayar()));
        if (SessionManager.getCurrentPenumpang() != null) {
            lblSaldoUser.setText(String.format("Saldo Axis E-Wallet Anda: Rp %,.0f", SessionManager.getCurrentPenumpang().getSaldoEwallet()));
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

        JLabel lblTitle = new JLabel("2. Review Pesanan & Pembayaran");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JPanel pnlBody = new JPanel(new BorderLayout(0, 20));
        pnlBody.setOpaque(false);
        pnlBody.setBorder(BorderFactory.createEmptyBorder(15, 60, 30, 60));

        lblInfoKereta = new JLabel("Detail Jadwal Kereta");
        lblInfoKereta.setFont(new Font("Verdana", Font.BOLD, 14));
        lblInfoKereta.setForeground(new Color(7, 59, 39));
        lblInfoKereta.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        modelReview = new DefaultTableModel(
            new String[]{"NIK / Paspor", "Nama Penumpang", "Kereta", "Gerbong (Otomatis)", "Nomor Kursi (Otomatis)"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblReviewTiket.setModel(modelReview);
        tblReviewTiket.setRowHeight(36);
        tblReviewTiket.setFont(new Font("Verdana", Font.PLAIN, 14));
        tblReviewTiket.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 14));

        JPanel cardPembayaran = new JPanel(new BorderLayout(30, 10));
        cardPembayaran.setBackground(Color.WHITE);
        cardPembayaran.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 222), 1, true),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));

        JPanel pnlInfoBayar = new JPanel(new GridLayout(3, 1, 0, 6));
        pnlInfoBayar.setOpaque(false);
        JLabel lblMetode = new JLabel("Metode Pembayaran: AXIS E-Wallet");
        lblMetode.setFont(new Font("Verdana", Font.BOLD, 16));
        lblMetode.setForeground(new Color(7, 59, 39));

        lblTotalTagihan = new JLabel("Rp 0");
        lblTotalTagihan.setFont(new Font("Verdana", Font.BOLD, 30));
        lblTotalTagihan.setForeground(new Color(198, 40, 40));

        lblSaldoUser = new JLabel("Saldo Axis E-Wallet Anda: Rp 0");
        lblSaldoUser.setFont(new Font("Verdana", Font.BOLD, 14));
        lblSaldoUser.setForeground(new Color(0, 108, 59));

        pnlInfoBayar.add(lblMetode);
        pnlInfoBayar.add(lblTotalTagihan);
        pnlInfoBayar.add(lblSaldoUser);

        JPanel pnlActionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        pnlActionButtons.setOpaque(false);

        btnUlangiInput.setText("Batalkan Pesanan");
        btnUlangiInput.setFont(new Font("Verdana", Font.BOLD, 14));
        btnUlangiInput.setPreferredSize(new Dimension(200, 48));
        btnUlangiInput.setBackground(new Color(225, 230, 228));

        btnBenarLanjut.setText("Bayar Sekarang (Potong Saldo)");
        btnBenarLanjut.setFont(new Font("Verdana", Font.BOLD, 14));
        btnBenarLanjut.setPreferredSize(new Dimension(310, 48));
        btnBenarLanjut.setBackground(new Color(0, 108, 59));
        btnBenarLanjut.setForeground(Color.WHITE);

        pnlActionButtons.add(btnUlangiInput);
        pnlActionButtons.add(btnBenarLanjut);

        cardPembayaran.add(pnlInfoBayar, BorderLayout.WEST);
        cardPembayaran.add(pnlActionButtons, BorderLayout.EAST);

        pnlBody.add(lblInfoKereta, BorderLayout.NORTH);
        pnlBody.add(new JScrollPane(tblReviewTiket), BorderLayout.CENTER);
        pnlBody.add(cardPembayaran, BorderLayout.SOUTH);

        getContentPane().add(pnlHeader, BorderLayout.NORTH);
        getContentPane().add(pnlBody, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void setupEvents() {
        for (java.awt.event.ActionListener al : btnBenarLanjut.getActionListeners()) btnBenarLanjut.removeActionListener(al);
        for (java.awt.event.ActionListener al : btnUlangiInput.getActionListeners()) btnUlangiInput.removeActionListener(al);

        btnBenarLanjut.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Konfirmasi pembayaran sebesar " + lblTotalTagihan.getText() + " menggunakan Axis E-Wallet?",
                "Konfirmasi Pembayaran",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm != JOptionPane.YES_OPTION) return;

            String res = controller.prosesBayar();
            if (res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil! Tiket Anda telah terbit");
                new MenuUtamaView().setVisible(true);
                this.dispose();
            } else if (res.equals("SALDO_KURANG")) {
                JOptionPane.showMessageDialog(this, "Saldo Axis E-Wallet tidak mencukupi, silakan Top-Up terlebih dahulu!", "Saldo Kurang", JOptionPane.WARNING_MESSAGE);
                new MenuUtamaView().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, res, "Gagal Membayar", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUlangiInput.addActionListener(e -> {
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
        tblReviewTiket = new javax.swing.JTable();
        lblJudulPenumpang = new javax.swing.JLabel();
        btnBenarLanjut = new javax.swing.JButton();
        btnUlangiInput = new javax.swing.JButton();
        lblJudulPenumpang1 = new javax.swing.JLabel();
        lblJudulPenumpang2 = new javax.swing.JLabel();
        lblJudulPenumpang3 = new javax.swing.JLabel();
        lblJudulPenumpang4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        tblReviewTiket.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblReviewTiket);

        lblJudulPenumpang.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblJudulPenumpang.setForeground(new java.awt.Color(0, 0, 0));
        lblJudulPenumpang.setText("2. Review Pesanan & Pembayaran");

        btnBenarLanjut.setText("CheckOut");
        btnBenarLanjut.addActionListener(this::btnBenarLanjutActionPerformed);

        btnUlangiInput.setText("Batalkan Pesanan");

        lblJudulPenumpang1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblJudulPenumpang1.setForeground(new java.awt.Color(0, 0, 0));
        lblJudulPenumpang1.setText("Saldo Axis Anda: Rp. ");

        lblJudulPenumpang2.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblJudulPenumpang2.setForeground(new java.awt.Color(204, 0, 51));
        lblJudulPenumpang2.setText("Rp. ");

        lblJudulPenumpang3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblJudulPenumpang3.setForeground(new java.awt.Color(0, 0, 0));
        lblJudulPenumpang3.setText("Detail kereta");

        lblJudulPenumpang4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        lblJudulPenumpang4.setForeground(new java.awt.Color(0, 0, 0));
        lblJudulPenumpang4.setText("Metode Pembayaran: AXIS E-Wallet");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 782, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblJudulPenumpang1, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblJudulPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblJudulPenumpang2, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblJudulPenumpang4, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(87, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnUlangiInput)
                .addGap(18, 18, 18)
                .addComponent(btnBenarLanjut)
                .addGap(100, 100, 100))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(56, 56, 56)
                    .addComponent(lblJudulPenumpang3, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(234, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(lblJudulPenumpang, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBenarLanjut)
                    .addComponent(btnUlangiInput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblJudulPenumpang4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblJudulPenumpang2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblJudulPenumpang1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(62, 62, 62)
                    .addComponent(lblJudulPenumpang3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(496, Short.MAX_VALUE)))
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

    private void btnBenarLanjutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBenarLanjutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBenarLanjutActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ReviewPemesananView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBenarLanjut;
    private javax.swing.JButton btnUlangiInput;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblJudulPenumpang;
    private javax.swing.JLabel lblJudulPenumpang1;
    private javax.swing.JLabel lblJudulPenumpang2;
    private javax.swing.JLabel lblJudulPenumpang3;
    private javax.swing.JLabel lblJudulPenumpang4;
    private javax.swing.JTable tblReviewTiket;
    // End of variables declaration//GEN-END:variables
}
