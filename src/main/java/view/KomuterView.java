package view;

import controller.KomuterController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.PerjalananKomuter;
import model.Stasiun;

public class KomuterView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(KomuterView.class.getName());
    private final KomuterController controller = new KomuterController();
    private String activeBarcodeText = null;

    public KomuterView() {
        initComponents();
        this.setSize(1200, 920);
        this.setLocationRelativeTo(null);

        rakitTampilan();
        setupEvents();
        sinkronkanStatusTampilan();
    }

    private void rakitTampilan() {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(244, 246, 245));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(7, 59, 39));
        pnlHeader.setPreferredSize(new Dimension(1200, 110));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(25, 70, 25, 70));

        JLabel lblTitle = new JLabel("Layanan Kereta Komuter");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        btnKembali.setText("Kembali ke Menu Utama");
        btnKembali.setFont(new Font("Verdana", Font.BOLD, 13));
        btnKembali.setBackground(new Color(232, 245, 233));
        btnKembali.setForeground(new Color(7, 59, 39));
        btnKembali.setPreferredSize(new Dimension(230, 42));
        pnlHeader.add(btnKembali, BorderLayout.EAST);

        JPanel pnlCenterWrap = new JPanel(new GridBagLayout());
        pnlCenterWrap.setOpaque(false);

        JPanel cardKomuter = new JPanel();
        cardKomuter.setLayout(new BoxLayout(cardKomuter, BoxLayout.Y_AXIS));
        cardKomuter.setBackground(Color.WHITE);
        cardKomuter.setPreferredSize(new Dimension(680, 620));
        cardKomuter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 222), 1, true),
            BorderFactory.createEmptyBorder(35, 50, 35, 50)
        ));

        JLabel lblPilih = new JLabel("Pilih Stasiun Awal Tap-In:");
        lblPilih.setFont(new Font("Verdana", Font.BOLD, 14));
        lblPilih.setAlignmentX(Component.CENTER_ALIGNMENT);

        cmbStasiunAwal.setFont(new Font("Verdana", Font.PLAIN, 15));
        cmbStasiunAwal.setMaximumSize(new Dimension(500, 45));
        cmbStasiunAwal.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnTapIn.setFont(new Font("Verdana", Font.BOLD, 15));
        btnTapIn.setBackground(new Color(0, 108, 59));
        btnTapIn.setForeground(Color.WHITE);
        btnTapIn.setMaximumSize(new Dimension(500, 48));
        btnTapIn.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatusKomuter.setFont(new Font("Verdana", Font.BOLD, 14));
        lblStatusKomuter.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStatusKomuter.setHorizontalAlignment(SwingConstants.CENTER);

        pnlAreaBarcode.removeAll();
        pnlAreaBarcode.setLayout(new BorderLayout());
        pnlAreaBarcode.setBackground(new Color(250, 252, 250));
        pnlAreaBarcode.setMaximumSize(new Dimension(500, 210));
        pnlAreaBarcode.setPreferredSize(new Dimension(500, 210));
        pnlAreaBarcode.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 212), 1, true));

        JPanel canvasGarisBarcode = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (activeBarcodeText == null) {
                    g.setColor(new Color(160, 160, 160));
                    g.setFont(new Font("Verdana", Font.ITALIC, 14));
                    g.drawString("-", 55, getHeight() / 2);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                int x = 45;
                int y = 25;
                int height = getHeight() - 50;
                for (int i = 0; i < activeBarcodeText.length(); i++) {
                    int val = activeBarcodeText.charAt(i);
                    int bar1 = (val % 3) + 2;
                    int space1 = (val % 2) + 2;
                    int bar2 = ((val / 3) % 4) + 1;
                    int space2 = 3;
                    g2.fillRect(x, y, bar1, height);
                    x += bar1 + space1;
                    g2.fillRect(x, y, bar2, height);
                    x += bar2 + space2;
                }
            }
        };
        canvasGarisBarcode.setOpaque(false);
        pnlAreaBarcode.add(canvasGarisBarcode, BorderLayout.CENTER);

        lblKodeBarcode.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblKodeBarcode.setForeground(new Color(7, 59, 39));
        lblKodeBarcode.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblKodeBarcode.setHorizontalAlignment(SwingConstants.CENTER);

        cardKomuter.add(lblPilih);
        cardKomuter.add(Box.createVerticalStrut(10));
        cardKomuter.add(cmbStasiunAwal);
        cardKomuter.add(Box.createVerticalStrut(18));
        cardKomuter.add(btnTapIn);
        cardKomuter.add(Box.createVerticalStrut(28));
        cardKomuter.add(lblStatusKomuter);
        cardKomuter.add(Box.createVerticalStrut(15));
        cardKomuter.add(pnlAreaBarcode);
        cardKomuter.add(Box.createVerticalStrut(12));
        cardKomuter.add(lblKodeBarcode);

        pnlCenterWrap.add(cardKomuter);

        getContentPane().add(pnlHeader, BorderLayout.NORTH);
        getContentPane().add(pnlCenterWrap, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void sinkronkanStatusTampilan() {
        cmbStasiunAwal.removeAllItems();
        List<Stasiun> listStasiun = controller.muatDaftarStasiun();
        for (Stasiun s : listStasiun) {
            cmbStasiunAwal.addItem(s.getIdStasiun() + " - " + s.getNamaStasiun() + " (" + s.getKodeKota() + ")");
        }

        PerjalananKomuter aktif = controller.cekPerjalananAktif();
        if (aktif != null && aktif.isMasihAktif()) {
            cmbStasiunAwal.setSelectedItem(controller.cariNamaStasiunById(aktif.getStasiunIdAsal()));
            cmbStasiunAwal.setEnabled(false);

            btnTapIn.setEnabled(false);
            btnTapIn.setText("Menunggu Tap-Out)");
            btnTapIn.setBackground(new Color(150, 150, 150));

            lblStatusKomuter.setText("<html><center>Anda sedang dalam perjalanan dari <b>" 
                + controller.cariNamaStasiunById(aktif.getStasiunIdAsal()) 
                + "</b><br>Waktu Tap-In: " + aktif.getWaktuTapIn() + " | Menunggu Verifikasi Tap-Out</center></html>");
            lblStatusKomuter.setForeground(new Color(198, 40, 40));

            activeBarcodeText = aktif.getKodeUnikGate();
            lblKodeBarcode.setText("KODE AKSES GATE: " + activeBarcodeText);
        } else {
            cmbStasiunAwal.setEnabled(true);

            btnTapIn.setEnabled(true);
            btnTapIn.setText("Tap-In");
            btnTapIn.setBackground(new Color(0, 108, 59));

            lblStatusKomuter.setText("Silakan pilih stasiun awal untuk melakukan Tap-In di gate komuter.");
            lblStatusKomuter.setForeground(new Color(0, 108, 59));

            activeBarcodeText = null;
            lblKodeBarcode.setText("KODE AKSES GATE: -");
        }
        pnlAreaBarcode.repaint();
    }

    private void setupEvents() {
        for (java.awt.event.ActionListener al : btnTapIn.getActionListeners()) btnTapIn.removeActionListener(al);
        for (java.awt.event.ActionListener al : btnKembali.getActionListeners()) btnKembali.removeActionListener(al);

        btnTapIn.addActionListener(e -> {
            String stasiunPilihan = (String) cmbStasiunAwal.getSelectedItem();
            String res = controller.prosesTapIn(stasiunPilihan);
            if (res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Tap-In Berhasil! Gunakan Barcode ini pada gate komuter.");
                sinkronkanStatusTampilan();
            } else {
                JOptionPane.showMessageDialog(this, res, "Peringatan Tap-In", JOptionPane.WARNING_MESSAGE);
                sinkronkanStatusTampilan();
            }
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
        jPanel2 = new javax.swing.JPanel();
        btnKembali = new javax.swing.JButton();
        lblStatusKomuter1 = new javax.swing.JLabel();
        cmbStasiunAwal = new javax.swing.JComboBox<>();
        btnTapIn = new javax.swing.JButton();
        pnlAreaBarcode = new javax.swing.JPanel();
        lblStatusKomuter = new javax.swing.JLabel();
        lblKodeBarcode = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jPanel2.setBackground(new java.awt.Color(0, 102, 0));

        btnKembali.setText("Kembali Ke Menu Utama");

        lblStatusKomuter1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblStatusKomuter1.setForeground(new java.awt.Color(0, 0, 0));
        lblStatusKomuter1.setText("Layanan Kereta Komuter ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(lblStatusKomuter1, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblStatusKomuter1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKembali, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );

        cmbStasiunAwal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbStasiunAwal.addActionListener(this::cmbStasiunAwalActionPerformed);

        btnTapIn.setText("Tap In");
        btnTapIn.addActionListener(this::btnTapInActionPerformed);

        pnlAreaBarcode.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlAreaBarcodeLayout = new javax.swing.GroupLayout(pnlAreaBarcode);
        pnlAreaBarcode.setLayout(pnlAreaBarcodeLayout);
        pnlAreaBarcodeLayout.setHorizontalGroup(
            pnlAreaBarcodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );
        pnlAreaBarcodeLayout.setVerticalGroup(
            pnlAreaBarcodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 139, Short.MAX_VALUE)
        );

        lblStatusKomuter.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblStatusKomuter.setForeground(new java.awt.Color(0, 0, 0));
        lblStatusKomuter.setText("Status Perjalanan");

        lblKodeBarcode.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblKodeBarcode.setForeground(new java.awt.Color(153, 0, 0));
        lblKodeBarcode.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pnlAreaBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(232, 232, 232))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbStasiunAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(247, 247, 247))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblStatusKomuter, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(293, 293, 293))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblKodeBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(289, 289, 289))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnTapIn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(349, 349, 349))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(lblStatusKomuter, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbStasiunAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTapIn)
                .addGap(39, 39, 39)
                .addComponent(pnlAreaBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(lblKodeBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTapInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTapInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTapInActionPerformed

    private void cmbStasiunAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbStasiunAwalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbStasiunAwalActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new KomuterView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTapIn;
    private javax.swing.JComboBox<String> cmbStasiunAwal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblKodeBarcode;
    private javax.swing.JLabel lblStatusKomuter;
    private javax.swing.JLabel lblStatusKomuter1;
    private javax.swing.JPanel pnlAreaBarcode;
    // End of variables declaration//GEN-END:variables
}
