package view;

import controller.MenuUtamaController;
import controller.SessionManager;
import java.awt.*;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.JadwalAntarkota;
import model.Penumpang;
import model.Stasiun;

public class MenuUtamaView extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MenuUtamaView.class.getName());
    private final MenuUtamaController controller = new MenuUtamaController();
    private CardLayout cardLayout;

    private DefaultTableModel modelTabelRiwayat;
    private DefaultTableModel modelTabelKomuter;
    private DefaultTableModel modelTabelTopup;

    public MenuUtamaView() {
        initComponents();
        this.setSize(1200, 920);
        this.setLocationRelativeTo(null);

        rakitTampilanModern();
        setupTableModels();
        setupEvents();
        muatUlangSemuaData();

        pindahHalaman("cardBeranda", btnNavBeranda);
    }

    private void rakitTampilanModern() {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        pnlMainContent.removeAll();
        pnlMainContent.setLayout(cardLayout);

        // =====================================================================
        // 1. DESAIN HALAMAN BERANDA
        // =====================================================================
        pnlBeranda.removeAll();
        pnlBeranda.setLayout(new BorderLayout());
        pnlBeranda.setBackground(new Color(244, 246, 245));

        JPanel pnlHero = new JPanel(new BorderLayout());
        pnlHero.setBackground(new Color(7, 59, 39));
        pnlHero.setPreferredSize(new Dimension(1200, 290));
        pnlHero.setBorder(BorderFactory.createEmptyBorder(25, 70, 30, 70));

        JPanel pnlTopBarHero = new JPanel(new BorderLayout());
        pnlTopBarHero.setOpaque(false);
        

        JLabel lblBrand = new JLabel();
        try {
            java.net.URL logoUrl = getClass().getResource("/image/asix-upd-wht.png");
            if (logoUrl != null) {
                ImageIcon origIcon = new ImageIcon(logoUrl);
                Image img = origIcon.getImage();
                int targetW = 160; 
                int targetH = (origIcon.getIconHeight() * targetW) / origIcon.getIconWidth();
                lblBrand.setIcon(new ImageIcon(img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {
            lblBrand.setText("AXIS RAILWAY");
        }

        lblSapaanUser.setFont(new Font("Verdana", Font.BOLD, 14));
        lblSapaanUser.setForeground(Color.WHITE);
        lblSapaanUser.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlTopBarHero.add(lblBrand, BorderLayout.WEST);
        pnlTopBarHero.add(lblSapaanUser, BorderLayout.EAST);

        JPanel pnlHeadline = new JPanel(new GridLayout(2, 1, 0, 5));
        pnlHeadline.setOpaque(false);
        pnlHeadline.setBorder(BorderFactory.createEmptyBorder(35, 0, 0, 0));
        JLabel lblSub = new JLabel("-");
        lblSub.setFont(new Font("Verdana", Font.PLAIN, 14));
        lblSub.setForeground(new Color(129, 199, 132));
        JLabel lblMainTitle = new JLabel("Temukan perjalanan terbaik Anda.");
        lblMainTitle.setFont(new Font("Verdana", Font.BOLD, 34));
        lblMainTitle.setForeground(Color.WHITE);
        pnlHeadline.add(lblSub);
        pnlHeadline.add(lblMainTitle);

        pnlHero.add(pnlTopBarHero, BorderLayout.NORTH);
        pnlHero.add(pnlHeadline, BorderLayout.CENTER);

        JPanel pnlBodyBeranda = new JPanel();
        pnlBodyBeranda.setLayout(new BoxLayout(pnlBodyBeranda, BoxLayout.Y_AXIS));
        pnlBodyBeranda.setBackground(new Color(244, 246, 245));
        pnlBodyBeranda.setBorder(BorderFactory.createEmptyBorder(25, 90, 25, 90));

        JPanel cardSearch = new JPanel(new BorderLayout(0, 20));
        cardSearch.setBackground(Color.WHITE);
        cardSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 228), 1, true),
            BorderFactory.createEmptyBorder(25, 35, 25, 35)
        ));
        cardSearch.setMaximumSize(new Dimension(1020, 270));

        JPanel gridInputs = new JPanel(new GridLayout(2, 2, 30, 18));
        gridInputs.setOpaque(false);

        spnTanggalBerangkat.setModel(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        spnTanggalBerangkat.setEditor(new JSpinner.DateEditor(spnTanggalBerangkat, "dd/MM/yyyy"));
        cmbJumlahPenumpang.setModel(new DefaultComboBoxModel<>(new String[]{"1 penumpang", "2 penumpang", "3 penumpang", "4 penumpang"}));

        gridInputs.add(buatFieldGroup("STASIUN ASAL", cmbStasiunAsal));
        gridInputs.add(buatFieldGroup("STASIUN TUJUAN", cmbStasiunTujuan));
        gridInputs.add(buatFieldGroup("TANGGAL BERANGKAT", spnTanggalBerangkat));
        gridInputs.add(buatFieldGroup("PENUMPANG", cmbJumlahPenumpang));

        btnCariJadwal.setText("Cari jadwal kereta");
        btnCariJadwal.setBackground(new Color(0, 108, 59));
        btnCariJadwal.setForeground(Color.WHITE);
        btnCariJadwal.setFont(new Font("Verdana", Font.BOLD, 14));
        btnCariJadwal.setPreferredSize(new Dimension(950, 48));
        btnCariJadwal.setFocusPainted(false);

        cardSearch.add(gridInputs, BorderLayout.CENTER);
        cardSearch.add(btnCariJadwal, BorderLayout.SOUTH);

        JPanel pnlBottomCards = new JPanel(new GridLayout(1, 2, 30, 0));
        pnlBottomCards.setOpaque(false);
        pnlBottomCards.setMaximumSize(new Dimension(1020, 145));

        JPanel cardSaldo = new JPanel(new BorderLayout(10, 10));
        cardSaldo.setBackground(Color.WHITE);
        cardSaldo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 228), 1, true),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        JLabel lblTitleSaldo = new JLabel("SALDO AXIS E-WALLET");
        lblTitleSaldo.setFont(new Font("Verdana", Font.BOLD, 12));
        lblTitleSaldo.setForeground(new Color(100, 120, 110));
        lblSaldoBeranda.setFont(new Font("Verdana", Font.BOLD, 26));
        lblSaldoBeranda.setForeground(new Color(7, 59, 39));
        btnTopUpSaldo.setText("+ Top Up Saldo");
        btnTopUpSaldo.setBackground(new Color(232, 245, 233));
        btnTopUpSaldo.setForeground(new Color(0, 108, 59));
        btnTopUpSaldo.setFont(new Font("Verdana", Font.BOLD, 12));
        cardSaldo.add(lblTitleSaldo, BorderLayout.NORTH);
        cardSaldo.add(lblSaldoBeranda, BorderLayout.CENTER);
        cardSaldo.add(btnTopUpSaldo, BorderLayout.EAST);

        JPanel cardKomuter = new JPanel(new BorderLayout(10, 10));
        cardKomuter.setBackground(Color.WHITE);
        cardKomuter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 228), 1, true),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        JLabel lblTitleKomuter = new JLabel("LAYANAN KERETA KOMUTER");
        lblTitleKomuter.setFont(new Font("Verdana", Font.BOLD, 12));
        lblTitleKomuter.setForeground(new Color(100, 120, 110));
        JLabel lblDescKomuter = new JLabel("Tap-In & perjalanan komuter harian");
        lblDescKomuter.setFont(new Font("Verdana", Font.PLAIN, 14));
        btnLayananKomuter.setText("Masuk Komuter ");
        btnLayananKomuter.setBackground(new Color(0, 108, 59));
        btnLayananKomuter.setForeground(Color.WHITE);
        btnLayananKomuter.setFont(new Font("Verdana", Font.BOLD, 12));
        cardKomuter.add(lblTitleKomuter, BorderLayout.NORTH);
        cardKomuter.add(lblDescKomuter, BorderLayout.CENTER);
        cardKomuter.add(btnLayananKomuter, BorderLayout.EAST);

        pnlBottomCards.add(cardSaldo);
        pnlBottomCards.add(cardKomuter);
        pnlBodyBeranda.add(cardSearch);
        pnlBodyBeranda.add(Box.createVerticalStrut(25));
        pnlBodyBeranda.add(pnlBottomCards);

        pnlBeranda.add(pnlHero, BorderLayout.NORTH);
        pnlBeranda.add(pnlBodyBeranda, BorderLayout.CENTER);

        // =====================================================================
        // 2. DESAIN HALAMAN RIWAYAT TIKET (PAKAI JTABBEDPANE)
        // =====================================================================
        pnlRiwayatTiket.removeAll();
        pnlRiwayatTiket.setLayout(new BorderLayout(0, 20));
        pnlRiwayatTiket.setBackground(new Color(7, 59, 39)); 
        pnlRiwayatTiket.setBorder(BorderFactory.createEmptyBorder(35, 70, 35, 70));

        JLabel lblJudulRiwayat = new JLabel("Riwayat Tiket & Perjalanan Saya");
        lblJudulRiwayat.setFont(new Font("Verdana", Font.BOLD, 24));
        lblJudulRiwayat.setForeground(Color.WHITE);

        jTabbedPane1.setFont(new Font("Verdana", Font.BOLD, 14));
        jTabbedPane1.setBackground(new Color(30, 30, 30));
        jTabbedPane1.setForeground(Color.WHITE);
        jTabbedPane1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        jPanel1.setLayout(new BorderLayout());
        jPanel1.setBackground(new Color(40, 40, 40));
        jScrollPane1.setBorder(null);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);

        jPanel5.setLayout(new BorderLayout());
        jPanel5.setBackground(new Color(40, 40, 40));
        jScrollPane2.setBorder(null);
        jPanel5.add(jScrollPane2, BorderLayout.CENTER);

        pnlRiwayatTiket.add(lblJudulRiwayat, BorderLayout.NORTH);
        pnlRiwayatTiket.add(jTabbedPane1, BorderLayout.CENTER);

        // =====================================================================
        // 3. DESAIN HALAMAN AKUN
        // =====================================================================
        pnlAkun.removeAll();
        pnlAkun.setLayout(new BorderLayout(20, 25));
        pnlAkun.setBackground(new Color(244, 246, 245));
        pnlAkun.setBorder(BorderFactory.createEmptyBorder(35, 70, 35, 70));

        JPanel cardProfil = new JPanel(new BorderLayout(20, 15));
        cardProfil.setBackground(Color.WHITE);
        cardProfil.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 222), 1, true),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        JPanel gridInfoProfil = new JPanel(new GridLayout(2, 2, 20, 15));
        gridInfoProfil.setOpaque(false);
        lblNamaAkun1.setFont(new Font("Verdana", Font.BOLD, 15));
        lblEmailAkun.setFont(new Font("Verdana", Font.PLAIN, 15));
        lblTeleponAkun.setFont(new Font("Verdana", Font.PLAIN, 15));
        lblSaldoAkun.setFont(new Font("Verdana", Font.BOLD, 16));
        lblSaldoAkun.setForeground(new Color(0, 108, 59));

        gridInfoProfil.add(lblNamaAkun1);
        gridInfoProfil.add(lblEmailAkun);
        gridInfoProfil.add(lblTeleponAkun);
        gridInfoProfil.add(lblSaldoAkun);

        btnLogoutAkun.setText("Keluar / LogOut");
        btnLogoutAkun.setBackground(new Color(198, 40, 40));
        btnLogoutAkun.setForeground(Color.WHITE);
        btnLogoutAkun.setFont(new Font("Verdana", Font.BOLD, 13));
        btnLogoutAkun.setPreferredSize(new Dimension(160, 42));

        cardProfil.add(gridInfoProfil, BorderLayout.CENTER);
        cardProfil.add(btnLogoutAkun, BorderLayout.EAST);

        JPanel pnlTabelTopupWrap = new JPanel(new BorderLayout(0, 10));
        pnlTabelTopupWrap.setOpaque(false);
        JLabel lblJudulTopup = new JLabel("Riwayat Top-Up Axis E-Wallet");
        lblJudulTopup.setFont(new Font("Verdana", Font.BOLD, 18));
        lblJudulTopup.setForeground(new Color(7, 59, 39));
        jScrollPane3.setViewportView(tblRiwayatTopUp);
        pnlTabelTopupWrap.add(lblJudulTopup, BorderLayout.NORTH);
        pnlTabelTopupWrap.add(jScrollPane3, BorderLayout.CENTER);

        pnlAkun.add(cardProfil, BorderLayout.NORTH);
        pnlAkun.add(pnlTabelTopupWrap, BorderLayout.CENTER);

        // =====================================================================
        // 4. DOWNBAR (BOTTOM NAVIGATION BAR)
        // =====================================================================
        pnlMainContent.add(pnlBeranda, "cardBeranda");
        pnlMainContent.add(pnlRiwayatTiket, "cardRiwayat");
        pnlMainContent.add(pnlAkun, "cardAkun");

        pnlDownbar.removeAll();
        pnlDownbar.setBackground(new Color(120, 120, 120));
        pnlDownbar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(100, 100, 100)));
        pnlDownbar.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 12));
        pnlDownbar.setPreferredSize(new Dimension(1200, 70));

        Dimension navSize = new Dimension(180, 44);
        for (JButton btn : new JButton[]{btnNavBeranda, btnNavRiwayat2, btnNavAkun}) {
            btn.setPreferredSize(navSize);
            btn.setFont(new Font("Verdana", Font.BOLD, 13));
            btn.setFocusPainted(false);
            pnlDownbar.add(btn);
        }

        getContentPane().add(pnlMainContent, BorderLayout.CENTER);
        getContentPane().add(pnlDownbar, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private JPanel buatFieldGroup(String judul, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setOpaque(false);
        JLabel lbl = new JLabel(judul);
        lbl.setFont(new Font("Verdana", Font.BOLD, 11));
        lbl.setForeground(new Color(110, 125, 118));
        comp.setPreferredSize(new Dimension(420, 42));
        comp.setFont(new Font("Verdana", Font.PLAIN, 14));
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void styleTable(JTable table, DefaultTableModel model) {
        table.setModel(model);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(0, 108, 59));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(225, 230, 228));
        table.getTableHeader().setForeground(new Color(50, 50, 50));
    }

    private void setupTableModels() {
        modelTabelRiwayat = new DefaultTableModel(new String[]{"ID Pemesanan", "Kode Booking", "Waktu Pesan", "Total Bayar", "Status Pembayaran"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        styleTable(tblRiwayatTiket, modelTabelRiwayat);

        modelTabelKomuter = new DefaultTableModel(new String[]{"ID Perjalanan", "Stasiun Asal", "Stasiun Tujuan", "Waktu Tap-In", "Waktu Tap-Out", "Tarif Potongan", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        styleTable(tblRiwayatKomuter, modelTabelKomuter);

        modelTabelTopup = new DefaultTableModel(new String[]{"ID Top-Up", "Waktu Top-Up", "Nominal", "Metode Pembayaran", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        styleTable(tblRiwayatTopUp, modelTabelTopup);
    }

    private void muatUlangSemuaData() {
        Penumpang p = controller.sinkronkanSessionPenumpang();
        if (p != null) {
            lblSapaanUser.setText("Selamat datang, " + p.getNamaLengkap());
            String saldoFmt = String.format("Rp %,.0f", p.getSaldoEwallet());
            lblSaldoBeranda.setText(saldoFmt);
            lblNamaAkun1.setText("Nama Penumpang : " + p.getNamaLengkap());
            lblEmailAkun.setText("Email          : " + p.getEmail());
            lblTeleponAkun.setText("Nomor Telepon  : " + p.getNomorTelepon());
            lblSaldoAkun.setText("Saldo E-Wallet : " + saldoFmt);
        }

        cmbStasiunAsal.removeAllItems();
        cmbStasiunTujuan.removeAllItems();
        List<Stasiun> stasiunList = controller.dapatkanStasiunAntarkota();
        for (Stasiun s : stasiunList) {
            String label = s.getIdStasiun() + " - " + s.getNamaStasiun() + " (" + s.getKodeKota() + ")";
            cmbStasiunAsal.addItem(label);
            cmbStasiunTujuan.addItem(label);
        }
        if (cmbStasiunTujuan.getItemCount() > 1) {
            cmbStasiunTujuan.setSelectedIndex(1);
        }

        controller.isiTabelRiwayatAntarkota(modelTabelRiwayat);
        controller.isiTabelRiwayatKomuter(modelTabelKomuter);
        controller.isiTabelRiwayatTopup(modelTabelTopup);
    }

    private void pindahHalaman(String namaCard, JButton tombolAktif) {
        cardLayout.show(pnlMainContent, namaCard);
        for (JButton btn : new JButton[]{btnNavBeranda, btnNavRiwayat2, btnNavAkun}) {
            btn.setBackground(new Color(100, 100, 100));
            btn.setForeground(new Color(220, 220, 220));
        }
        tombolAktif.setBackground(new Color(60, 60, 60));
        tombolAktif.setForeground(Color.WHITE);
    }

    private void setupEvents() {
        btnNavBeranda.addActionListener(e -> {
            muatUlangSemuaData();
            pindahHalaman("cardBeranda", btnNavBeranda);
        });
        btnNavRiwayat2.addActionListener(e -> {
            muatUlangSemuaData();
            pindahHalaman("cardRiwayat", btnNavRiwayat2);
        });
        btnNavAkun.addActionListener(e -> {
            muatUlangSemuaData();
            pindahHalaman("cardAkun", btnNavAkun);
        });

        btnCariJadwal.addActionListener(e -> {
            String asal = (String) cmbStasiunAsal.getSelectedItem();
            String tujuan = (String) cmbStasiunTujuan.getSelectedItem();
            Date tgl = (Date) spnTanggalBerangkat.getValue();

            List<JadwalAntarkota> hasil = controller.cariJadwal(asal, tujuan, tgl);
            if (hasil == null) {
                JOptionPane.showMessageDialog(this, "Stasiun asal dan tujuan tidak boleh sama!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (hasil.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada jadwal kereta tersedia pada tanggal tersebut.", "Info Jadwal", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int jmlPenumpang = cmbJumlahPenumpang.getSelectedIndex() + 1;
                new PilihJadwalView(hasil, jmlPenumpang).setVisible(true);
                this.dispose();
            }
        });

        btnTopUpSaldo.addActionListener(e -> {
            JTextField txtNominal = new JTextField();
            JComboBox<String> cmbMetode = new JComboBox<>(new String[]{"Transfer Bank (Virtual Account)", "QRIS", "Minimarket"});
            JTextField txtRekening = new JTextField();
            Object[] message = {
                "Masukkan Nominal Top-Up (Rp):", txtNominal,
                "Pilih Metode Pembayaran (Dummy):", cmbMetode,
                "Masukkan Nomor Rekening / HP Asal:", txtRekening
            };
            int option = JOptionPane.showConfirmDialog(this, message, "Top-Up Saldo Axis E-Wallet", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String res = controller.topUpEwallet(txtNominal.getText().trim(), (String) cmbMetode.getSelectedItem(), txtRekening.getText().trim());
                if (res.equals("SUCCESS")) {
                    JOptionPane.showMessageDialog(this, "Top-Up Berhasil! Saldo Anda telah ditambahkan.");
                    muatUlangSemuaData();
                } else {
                    JOptionPane.showMessageDialog(this, res, "Gagal Top-Up", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLayananKomuter.addActionListener(e -> {
            new KomuterView().setVisible(true);
            this.dispose();
        });    

        btnLogoutAkun.addActionListener(e -> {
            SessionManager.clearSession();
            new LoginView().setVisible(true);
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

        pnlMainContent = new javax.swing.JPanel();
        pnlBeranda = new javax.swing.JPanel();
        lblSapaanUser = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cmbStasiunAsal = new javax.swing.JComboBox<>();
        cmbStasiunTujuan = new javax.swing.JComboBox<>();
        spnTanggalBerangkat = new javax.swing.JSpinner();
        cmbJumlahPenumpang = new javax.swing.JComboBox<>();
        btnCariJadwal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblSaldoBeranda = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnTopUpSaldo = new javax.swing.JButton();
        btnLayananKomuter = new javax.swing.JButton();
        pnlRiwayatTiket = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRiwayatTiket = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRiwayatKomuter = new javax.swing.JTable();
        pnlAkun = new javax.swing.JPanel();
        lblSaldoAkun = new javax.swing.JLabel();
        lblPotoPropil = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        lblEmailAkun = new javax.swing.JLabel();
        lblTeleponAkun = new javax.swing.JLabel();
        lblNamaAkun1 = new javax.swing.JLabel();
        btnLogoutAkun = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRiwayatTopUp = new javax.swing.JTable();
        pnlDownbar = new javax.swing.JPanel();
        btnNavAkun = new javax.swing.JButton();
        btnNavRiwayat2 = new javax.swing.JButton();
        btnNavBeranda = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMainContent.setBackground(new java.awt.Color(204, 204, 204));
        pnlMainContent.setLayout(new java.awt.CardLayout());

        pnlBeranda.setBackground(new java.awt.Color(255, 255, 255));

        lblSapaanUser.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblSapaanUser.setForeground(new java.awt.Color(0, 0, 0));
        lblSapaanUser.setText("Selamat datang, Penumpang");

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));

        cmbStasiunAsal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbStasiunTujuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        spnTanggalBerangkat.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1793602582001L), null, null, java.util.Calendar.DAY_OF_MONTH));

        cmbJumlahPenumpang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 penumpang", "2 penumpang", "3 penumpang", "4 penumpang", "5 penumpang", "6 penumpang", "7 penumpang", "8 penumpang" }));

        btnCariJadwal.setBackground(new java.awt.Color(0, 102, 51));
        btnCariJadwal.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        btnCariJadwal.setText("Cari Jadwal Kereta ->");

        jLabel1.setFont(new java.awt.Font("Verdana", 3, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("🚉Stasiun Awal");

        jLabel2.setFont(new java.awt.Font("Verdana", 3, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Stasiun Akhir");

        jLabel3.setFont(new java.awt.Font("Verdana", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("jadwal Berangkat");

        jLabel4.setFont(new java.awt.Font("Verdana", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Penumpang");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnCariJadwal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(spnTanggalBerangkat, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                                .addComponent(cmbStasiunAsal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmbStasiunTujuan, 0, 310, Short.MAX_VALUE)
                                .addComponent(cmbJumlahPenumpang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(63, 63, 63))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStasiunAsal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbStasiunTujuan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spnTanggalBerangkat, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(cmbJumlahPenumpang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCariJadwal)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\SEMESTER 3\\CAPSTONE\\AXIS\\src\\main\\resources\\image\\asix-upd-wht.png")); // NOI18N

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));

        lblSaldoBeranda.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        lblSaldoBeranda.setForeground(new java.awt.Color(0, 0, 0));
        lblSaldoBeranda.setText("Rp. 0");

        jLabel6.setFont(new java.awt.Font("Verdana", 3, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Saldo AXIS E-Wallet ");

        btnTopUpSaldo.setText("Isi Saldo AXIS E-Wallet");
        btnTopUpSaldo.addActionListener(this::btnTopUpSaldoActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblSaldoBeranda, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTopUpSaldo))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSaldoBeranda, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(btnTopUpSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );

        btnLayananKomuter.setText("Buka Layanan Komuter");
        btnLayananKomuter.addActionListener(this::btnLayananKomuterActionPerformed);

        javax.swing.GroupLayout pnlBerandaLayout = new javax.swing.GroupLayout(pnlBeranda);
        pnlBeranda.setLayout(pnlBerandaLayout);
        pnlBerandaLayout.setHorizontalGroup(
            pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBerandaLayout.createSequentialGroup()
                .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSapaanUser, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnlBerandaLayout.createSequentialGroup()
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(32, 32, 32)
                                    .addComponent(btnLayananKomuter))
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        pnlBerandaLayout.setVerticalGroup(
            pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBerandaLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSapaanUser, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(btnLayananKomuter, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pnlMainContent.add(pnlBeranda, "cardBeranda");

        pnlRiwayatTiket.setBackground(new java.awt.Color(0, 102, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tblRiwayatTiket.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblRiwayatTiket);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("AntarKota", jPanel1);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        tblRiwayatKomuter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tblRiwayatKomuter);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 874, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Komuter", jPanel5);

        javax.swing.GroupLayout pnlRiwayatTiketLayout = new javax.swing.GroupLayout(pnlRiwayatTiket);
        pnlRiwayatTiket.setLayout(pnlRiwayatTiketLayout);
        pnlRiwayatTiketLayout.setHorizontalGroup(
            pnlRiwayatTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRiwayatTiketLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );
        pnlRiwayatTiketLayout.setVerticalGroup(
            pnlRiwayatTiketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pnlMainContent.add(pnlRiwayatTiket, "cardRiwayat");

        pnlAkun.setBackground(new java.awt.Color(255, 255, 255));

        lblSaldoAkun.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblSaldoAkun.setForeground(new java.awt.Color(0, 0, 0));
        lblSaldoAkun.setText("Saldo");

        lblPotoPropil.setText("jLabel7");

        lblLogo.setText("jLabel7");

        lblEmailAkun.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblEmailAkun.setForeground(new java.awt.Color(0, 0, 0));
        lblEmailAkun.setText("Email");

        lblTeleponAkun.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        lblTeleponAkun.setForeground(new java.awt.Color(0, 0, 0));
        lblTeleponAkun.setText("Telepon");

        lblNamaAkun1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblNamaAkun1.setForeground(new java.awt.Color(0, 0, 0));
        lblNamaAkun1.setText("nama akun");

        btnLogoutAkun.setText("Logout");
        btnLogoutAkun.addActionListener(this::btnLogoutAkunActionPerformed);

        tblRiwayatTopUp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblRiwayatTopUp);

        javax.swing.GroupLayout pnlAkunLayout = new javax.swing.GroupLayout(pnlAkun);
        pnlAkun.setLayout(pnlAkunLayout);
        pnlAkunLayout.setHorizontalGroup(
            pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAkunLayout.createSequentialGroup()
                .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAkunLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAkunLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(lblPotoPropil, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAkunLayout.createSequentialGroup()
                        .addComponent(lblNamaAkun1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLogoutAkun)
                        .addGap(160, 160, 160))
                    .addGroup(pnlAkunLayout.createSequentialGroup()
                        .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTeleponAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlAkunLayout.createSequentialGroup()
                                .addComponent(lblEmailAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(175, 175, 175)
                                .addComponent(lblSaldoAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(pnlAkunLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 117, Short.MAX_VALUE))
        );
        pnlAkunLayout.setVerticalGroup(
            pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAkunLayout.createSequentialGroup()
                .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAkunLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNamaAkun1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAkunLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnLogoutAkun)
                        .addGap(2, 2, 2)))
                .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPotoPropil, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlAkunLayout.createSequentialGroup()
                        .addGroup(pnlAkunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmailAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSaldoAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addComponent(lblTeleponAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pnlMainContent.add(pnlAkun, "cardAkun");

        pnlDownbar.setBackground(new java.awt.Color(153, 153, 153));

        btnNavAkun.setText("Akun");

        btnNavRiwayat2.setText("Tiket");
        btnNavRiwayat2.addActionListener(this::btnNavRiwayat2ActionPerformed);

        btnNavBeranda.setText("Beranda");
        btnNavBeranda.addActionListener(this::btnNavBerandaActionPerformed);

        javax.swing.GroupLayout pnlDownbarLayout = new javax.swing.GroupLayout(pnlDownbar);
        pnlDownbar.setLayout(pnlDownbarLayout);
        pnlDownbarLayout.setHorizontalGroup(
            pnlDownbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDownbarLayout.createSequentialGroup()
                .addGap(356, 356, 356)
                .addComponent(btnNavBeranda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNavRiwayat2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNavAkun)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDownbarLayout.setVerticalGroup(
            pnlDownbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDownbarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDownbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNavRiwayat2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDownbarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnNavAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNavBeranda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDownbar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlMainContent, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlDownbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNavBerandaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavBerandaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNavBerandaActionPerformed

    private void btnTopUpSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTopUpSaldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTopUpSaldoActionPerformed

    private void btnLayananKomuterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLayananKomuterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLayananKomuterActionPerformed

    private void btnNavRiwayat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavRiwayat2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNavRiwayat2ActionPerformed

    private void btnLogoutAkunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutAkunActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLogoutAkunActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new MenuUtamaView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariJadwal;
    private javax.swing.JButton btnLayananKomuter;
    private javax.swing.JButton btnLogoutAkun;
    private javax.swing.JButton btnNavAkun;
    private javax.swing.JButton btnNavBeranda;
    private javax.swing.JButton btnNavRiwayat2;
    private javax.swing.JButton btnTopUpSaldo;
    private javax.swing.JComboBox<String> cmbJumlahPenumpang;
    private javax.swing.JComboBox<String> cmbStasiunAsal;
    private javax.swing.JComboBox<String> cmbStasiunTujuan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblEmailAkun;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblNamaAkun1;
    private javax.swing.JLabel lblPotoPropil;
    private javax.swing.JLabel lblSaldoAkun;
    private javax.swing.JLabel lblSaldoBeranda;
    private javax.swing.JLabel lblSapaanUser;
    private javax.swing.JLabel lblTeleponAkun;
    private javax.swing.JPanel pnlAkun;
    private javax.swing.JPanel pnlBeranda;
    private javax.swing.JPanel pnlDownbar;
    private javax.swing.JPanel pnlMainContent;
    private javax.swing.JPanel pnlRiwayatTiket;
    private javax.swing.JSpinner spnTanggalBerangkat;
    private javax.swing.JTable tblRiwayatKomuter;
    private javax.swing.JTable tblRiwayatTiket;
    private javax.swing.JTable tblRiwayatTopUp;
    // End of variables declaration//GEN-END:variables
}
