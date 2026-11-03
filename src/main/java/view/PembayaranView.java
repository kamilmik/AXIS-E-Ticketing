package view;

import controller.PemesananController;
import controller.SessionManager;
import java.awt.*;
import javax.swing.*;

public class PembayaranView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PembayaranView.class.getName());
    private final PemesananController controller;

    public PembayaranView() {
        this(new PemesananController());
    }

    public PembayaranView(PemesananController controller) {
        initComponents();
        this.setSize(1200, 920);
        this.setLocationRelativeTo(null);
        this.controller = controller;

        rakitTampilan();
        setupEvents();
        tampilkanInfoTagihan();
    }

    private void tampilkanInfoTagihan() {
        lblTotalTagihan.setText(String.format("Rp %,.0f", controller.hitungTotalBayar()));
        if (SessionManager.getCurrentPenumpang() != null) {
            lblSaldoUser.setText(String.format("Saldo Axis E-Wallet Anda : Rp %,.0f", SessionManager.getCurrentPenumpang().getSaldoEwallet()));
        }
    }

    private void rakitTampilan() {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(244, 246, 245));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(7, 59, 39));
        pnlHeader.setPreferredSize(new Dimension(1200, 110));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(25, 70, 25, 70));

        JLabel lblHeader = new JLabel("Menu Pembayaran Tiket Antarkota");
        lblHeader.setFont(new Font("Verdana", Font.BOLD, 26));
        lblHeader.setForeground(Color.WHITE);
        pnlHeader.add(lblHeader, BorderLayout.WEST);

        JPanel pnlCenterWrap = new JPanel(new GridBagLayout());
        pnlCenterWrap.setOpaque(false);

        JPanel cardBayar = new JPanel(new GridLayout(6, 1, 0, 15));
        cardBayar.setBackground(Color.WHITE);
        cardBayar.setPreferredSize(new Dimension(560, 440));
        cardBayar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 222), 1, true),
            BorderFactory.createEmptyBorder(35, 45, 35, 45)
        ));

        JLabel lblMetode = new JLabel("Pembayaran Menggunakan Axis E-Wallet");
        lblMetode.setFont(new Font("Verdana", Font.BOLD, 18));
        lblMetode.setForeground(new Color(7, 59, 39));

        JLabel lblSubTagihan = new JLabel("Total Tagihan Pemesanan:");
        lblSubTagihan.setFont(new Font("Verdana", Font.PLAIN, 14));

        lblTotalTagihan.setFont(new Font("Verdana", Font.BOLD, 34));
        lblTotalTagihan.setForeground(new Color(198, 40, 40));

        lblSaldoUser.setFont(new Font("Verdana", Font.BOLD, 15));
        lblSaldoUser.setForeground(new Color(0, 108, 59));

        btnBayarEwallet.setText("Bayar Sekarang (Potong Saldo E-Wallet)");
        btnBayarEwallet.setFont(new Font("Verdana", Font.BOLD, 15));
        btnBayarEwallet.setBackground(new Color(0, 108, 59));
        btnBayarEwallet.setForeground(Color.WHITE);

        btnBatalBayar.setText("Batalkan Pesanan");
        btnBatalBayar.setFont(new Font("Verdana", Font.BOLD, 13));
        btnBatalBayar.setBackground(new Color(225, 230, 228));

        cardBayar.add(lblMetode);
        cardBayar.add(lblSubTagihan);
        cardBayar.add(lblTotalTagihan);
        cardBayar.add(lblSaldoUser);
        cardBayar.add(btnBayarEwallet);
        cardBayar.add(btnBatalBayar);

        pnlCenterWrap.add(cardBayar);

        getContentPane().add(pnlHeader, BorderLayout.NORTH);
        getContentPane().add(pnlCenterWrap, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void setupEvents() {
        for (java.awt.event.ActionListener al : btnBayarEwallet.getActionListeners()) btnBayarEwallet.removeActionListener(al);
        for (java.awt.event.ActionListener al : btnBatalBayar.getActionListeners()) btnBatalBayar.removeActionListener(al);

        btnBayarEwallet.addActionListener(e -> {
            String res = controller.prosesBayar();
            if (res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil!");
                new MenuUtamaView().setVisible(true);
                this.dispose();
            } else if (res.equals("SALDO_KURANG")) {
                JOptionPane.showMessageDialog(this, "Saldo tidak mencukupi, silakan Top-Up terlebih dahulu!", "Saldo Kurang", JOptionPane.WARNING_MESSAGE);
                new MenuUtamaView().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, res, "Gagal Membayar", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBatalBayar.addActionListener(e -> {
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
        lblTotalTagihan = new javax.swing.JLabel();
        lblSaldoUser = new javax.swing.JLabel();
        btnBayarEwallet = new javax.swing.JButton();
        btnBatalBayar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        lblTotalTagihan.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblTotalTagihan.setForeground(new java.awt.Color(0, 0, 0));
        lblTotalTagihan.setText("Rp 0");

        lblSaldoUser.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblSaldoUser.setForeground(new java.awt.Color(0, 0, 0));
        lblSaldoUser.setText("Saldo E-Wallet: Rp 0");

        btnBayarEwallet.setText("Bayar");

        btnBatalBayar.setText("Batal");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnBayarEwallet)
                        .addComponent(lblSaldoUser, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnBatalBayar)
                        .addGap(199, 199, 199)))
                .addContainerGap(513, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(lblTotalTagihan)
                .addGap(18, 18, 18)
                .addComponent(lblSaldoUser)
                .addGap(27, 27, 27)
                .addComponent(btnBayarEwallet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBatalBayar)
                .addContainerGap(368, Short.MAX_VALUE))
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
        java.awt.EventQueue.invokeLater(() -> new PembayaranView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalBayar;
    private javax.swing.JButton btnBayarEwallet;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblSaldoUser;
    private javax.swing.JLabel lblTotalTagihan;
    // End of variables declaration//GEN-END:variables
}
