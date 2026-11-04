package view;

import controller.AdminController;
import controller.SessionManager;
import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DashboardAdminView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardAdminView.class.getName());
    private final AdminController controller = new AdminController();
    private java.awt.CardLayout cardLayout;

    // Model Tabel
    private DefaultTableModel modelJadwal;
    private DefaultTableModel modelStasiun;
    private DefaultTableModel modelTarif;
    private DefaultTableModel modelTapOut;
    private DefaultTableModel modelMonitoring;
    private DefaultTableModel modelDetailTiket;

    // Variabel Simpanan State Sementara
    private int selectedIdJadwal = -1;
    private int selectedIdStasiun = -1;
    private String selectedIdTarif = null;
    private int selectedIdAsalTapOut = -1;
    private double currentEstimasiTarif = 0.0;

    // Komponen Custom untuk Filter & Sorting
    private JComboBox<String> cmbSortJadwalWaktu;
    private JComboBox<String> cmbFilterStasiunKategori;
    private JComboBox<String> cmbSortMonitoringWaktu;

    public DashboardAdminView() {
        initComponents();
        this.setSize(1200, 920);
        this.setLocationRelativeTo(null);

        cardLayout = (java.awt.CardLayout) pnlContent.getLayout();
        
        if (SessionManager.getCurrentAdmin() != null) {
            lblInfoAdmin.setText("Admin: " + SessionManager.getCurrentAdmin().getKodeKaryawan());
        } else {
            lblInfoAdmin.setText("Admin: AXISEMPL_01");
        }

        perbaikiTampilanDanLayout();
        setupTableModels();
        refreshAllData();
        setupEvents();
        
        switchPanel("cardDashboard", btnMenuDashboard);
        this.revalidate();
        this.repaint();
    }

    private void perbaikiTampilanDanLayout() {
        try {
            java.net.URL imgUrl = getClass().getResource("/image/asix-upd-wht.png");
            if(imgUrl == null) imgUrl = getClass().getResource("/image/axis-upd-wht.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage();
                int targetW = 180;
                int targetH = (icon.getIconHeight() * targetW) / icon.getIconWidth();
                jLabel1.setIcon(new ImageIcon(img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH)));
                jLabel1.setText("");
            }
        } catch (Exception e) { }

        jLabel2.setForeground(Color.WHITE);
        lblInfoAdmin.setForeground(new Color(200, 230, 201));
        jLabel17.setText("");

        Date sekarang = new Date();
        spnWaktuBerangkat.setModel(new SpinnerDateModel(sekarang, sekarang, null, java.util.Calendar.MINUTE));
        spnWaktuBerangkat.setEditor(new JSpinner.DateEditor(spnWaktuBerangkat, "yyyy-MM-dd HH:mm:ss"));
        spnWaktuTiba.setModel(new SpinnerDateModel(sekarang, sekarang, null, java.util.Calendar.MINUTE));
        spnWaktuTiba.setEditor(new JSpinner.DateEditor(spnWaktuTiba, "yyyy-MM-dd HH:mm:ss"));

        pnlDashboard.removeAll();
        pnlDashboard.setLayout(new BorderLayout(20, 20));
        pnlDashboard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JPanel gridCards = new JPanel(new GridLayout(2, 2, 25, 25));
        gridCards.setOpaque(false);

        styleCard(jPanel7, lbl3, lblTotalAntarkota);
        styleCard(jPanel6, jLabel4, lblTotalKomuter);
        styleCard(jPanel1, jLabel5, lblTotalGabungan);
        styleCard(jPanel8, jLabel6, lblTotalKeberangkatan);

        gridCards.add(jPanel7); gridCards.add(jPanel6);
        gridCards.add(jPanel1); gridCards.add(jPanel8);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topBar.setOpaque(false);
        btnRefreshDashboard.setPreferredSize(new Dimension(160, 40));
        topBar.add(btnRefreshDashboard);

        pnlDashboard.add(gridCards, BorderLayout.CENTER);
        pnlDashboard.add(topBar, BorderLayout.SOUTH);
        pnlDashboard.revalidate(); pnlDashboard.repaint();
    }

    private void styleCard(JPanel card, JLabel title, JLabel value) {
        card.removeAll(); card.setBackground(new Color(27, 94, 32));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setLayout(new BorderLayout(10, 10));
        title.setForeground(new Color(200, 230, 201)); title.setFont(new Font("Verdana", Font.BOLD, 16));
        value.setForeground(Color.WHITE); value.setFont(new Font("Verdana", Font.BOLD, 44));
        card.add(title, BorderLayout.NORTH); card.add(value, BorderLayout.CENTER);
    }

    private void styleTable(JTable table, DefaultTableModel model) {
        table.setModel(model); 
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true); 
        table.setSelectionBackground(new Color(45, 87, 48)); 
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true); 
        table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));
    }

    // FUNGSI HELPER: Memasang Filter TAPI Menjaga Header Tabel Tetap Muncul
    private void pasangFilterDiAtasTabel(JScrollPane scroll, JTable table, JPanel filterPanel) {
        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.add(filterPanel, BorderLayout.NORTH);
        headerWrap.add(table.getTableHeader(), BorderLayout.SOUTH);
        scroll.setColumnHeaderView(headerWrap);
    }

    private void setupTableModels() {
        modelJadwal = new DefaultTableModel(new String[]{"ID", "Nama Kereta", "Stasiun Asal", "Stasiun Tujuan", "Berangkat", "Tiba", "Harga", "Grbg", "Kursi"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(jTable1, modelJadwal);
        
        modelStasiun = new DefaultTableModel(new String[]{"ID Stasiun", "Nama Stasiun", "Kode Kota", "Kategori"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(tblStasiun, modelStasiun);
        
        modelTarif = new DefaultTableModel(new String[]{"ID Tarif", "Stasiun Awal", "Stasiun Akhir", "Harga Rute"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(tblTarifKomuter, modelTarif);
        
        modelTapOut = new DefaultTableModel(new String[]{"ID Perjalanan", "NIK", "Nama", "Saldo", "Stasiun Asal", "Waktu Tap-In", "Status"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(tblTapInKomuter, modelTapOut);
        
        modelMonitoring = new DefaultTableModel(new String[]{"ID Pesan", "Kode Booking", "NIK", "Nama", "Waktu", "Total Bayar", "Status"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(tblMonitoringPemesanan, modelMonitoring);
        
        modelDetailTiket = new DefaultTableModel(new String[]{"ID Tiket", "Barcode", "Nama", "NIK", "Jadwal", "Gerbong", "Kursi"}, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        styleTable(tblDetailTiketPemesanan, modelDetailTiket);

        pasangKomponenFilterDanSort();
    }

    private void pasangKomponenFilterDanSort() {
        // 1. Sort Waktu di Kelola Jadwal Antarkota
        cmbSortJadwalWaktu = new JComboBox<>(new String[]{"Urut Waktu: Terdekat (ASC)", "Urut Waktu: Terjauh (DESC)"});
        JPanel pnlFilterJadwal = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlFilterJadwal.setBackground(new Color(204, 204, 204));
        pnlFilterJadwal.add(new JLabel("Sorting Jadwal:"));
        pnlFilterJadwal.add(cmbSortJadwalWaktu);
        pasangFilterDiAtasTabel(jScrollPane1, jTable1, pnlFilterJadwal);

        // 2. Filter Kategori di Tabel Stasiun
        cmbFilterStasiunKategori = new JComboBox<>(new String[]{"Semua Kategori", "Antarkota", "Komuter", "Keduanya"});
        JPanel pnlFilterStasiun = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlFilterStasiun.setBackground(new Color(153, 153, 153));
        pnlFilterStasiun.add(new JLabel("Filter Kategori Layanan:"));
        pnlFilterStasiun.add(cmbFilterStasiunKategori);
        pasangFilterDiAtasTabel(jScrollPane2, tblStasiun, pnlFilterStasiun);

        // 3. Sort Waktu Pesan di Monitoring Pemesanan
        cmbSortMonitoringWaktu = new JComboBox<>(new String[]{"Waktu Pesan: Terbaru (DESC)", "Waktu Pesan: Terlama (ASC)"});
        JPanel pnlFilterMon = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlFilterMon.setBackground(new Color(204, 204, 204));
        pnlFilterMon.add(new JLabel("Urutkan:"));
        pnlFilterMon.add(cmbSortMonitoringWaktu);
        pasangFilterDiAtasTabel(jScrollPane5, tblMonitoringPemesanan, pnlFilterMon);
    }

    private void refreshAllData() {
        int[] stats = controller.dapatkanStatistik();
        lblTotalAntarkota.setText(String.valueOf(stats[0]));
        lblTotalKomuter.setText(String.valueOf(stats[1]));
        lblTotalGabungan.setText(String.valueOf(stats[2]));
        lblTotalKeberangkatan.setText(String.valueOf(stats[3]));

        cmbStasiunAsalJadwal.removeAllItems(); cmbStasiunTujuanJadwal.removeAllItems();
        for (String s : controller.dapatkanListStasiun("Antarkota")) {
            cmbStasiunAsalJadwal.addItem(s); cmbStasiunTujuanJadwal.addItem(s);
        }
        cmbTarifStasiunAsal.removeAllItems(); cmbTarifStasiunAkhir.removeAllItems();
        for (String s : controller.dapatkanListStasiun("Komuter")) {
            cmbTarifStasiunAsal.addItem(s); cmbTarifStasiunAkhir.addItem(s);
        }

        cmbStasiunKeluar.removeAllItems();
        cmbStasiunKeluar.addItem("-- Pilih Stasiun Akhir --");

        controller.muatTabelJadwal(modelJadwal);
        controller.muatTabelStasiun(modelStasiun);
        controller.muatTabelTarif(modelTarif);
        controller.muatTabelTapOut(modelTapOut);
        controller.muatMonitoring(modelMonitoring, null);
    }

    private void setupEvents() {
        btnRefreshDashboard.addActionListener(e -> refreshAllData());

        // Event Sorting Waktu Berangkat di Kelola Jadwal
        cmbSortJadwalWaktu.addActionListener(e -> {
            javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelJadwal);
            jTable1.setRowSorter(sorter);
            SortOrder order = cmbSortJadwalWaktu.getSelectedIndex() == 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING;
            sorter.setSortKeys(java.util.Collections.singletonList(new RowSorter.SortKey(4, order)));
            sorter.sort();
        });

        // Event Filter Kategori Layanan di Kelola Stasiun
        cmbFilterStasiunKategori.addActionListener(e -> {
            javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelStasiun);
            tblStasiun.setRowSorter(sorter);
            String pil = (String) cmbFilterStasiunKategori.getSelectedItem();
            if (pil == null || pil.equals("Semua Kategori")) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("^" + pil + "$", 3));
            }
        });

        // Event Sorting Waktu Pesan di Monitoring Pemesanan (Kolom indeks 4 = Waktu)
        cmbSortMonitoringWaktu.addActionListener(e -> {
            javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelMonitoring);
            tblMonitoringPemesanan.setRowSorter(sorter);
            SortOrder order = cmbSortMonitoringWaktu.getSelectedIndex() == 0 ? SortOrder.DESCENDING : SortOrder.ASCENDING;
            sorter.setSortKeys(java.util.Collections.singletonList(new RowSorter.SortKey(4, order)));
            sorter.sort();
        });

        // --- MENU JADWAL ANTARKOTA ---
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            int viewRow = jTable1.getSelectedRow();
            if (viewRow >= 0) {
                int row = jTable1.convertRowIndexToModel(viewRow);
                selectedIdJadwal = Integer.parseInt(modelJadwal.getValueAt(row, 0).toString());
                txtNamaKereta.setText(modelJadwal.getValueAt(row, 1).toString());
                cmbStasiunAsalJadwal.setSelectedItem(modelJadwal.getValueAt(row, 2).toString());
                cmbStasiunTujuanJadwal.setSelectedItem(modelJadwal.getValueAt(row, 3).toString());
                txtHargaDasar.setText(modelJadwal.getValueAt(row, 6).toString());
                txtTotalGerbong.setText(modelJadwal.getValueAt(row, 7).toString());
                txtKapasitasKursi.setText(modelJadwal.getValueAt(row, 8).toString());
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    spnWaktuBerangkat.setValue(sdf.parse(modelJadwal.getValueAt(row, 4).toString()));
                    spnWaktuTiba.setValue(sdf.parse(modelJadwal.getValueAt(row, 5).toString()));
                } catch (Exception ex) { }
            }
        });

        btnTambahJadwal.addActionListener(e -> {
            String res = controller.tambahJadwal(
                txtNamaKereta.getText().trim(),
                (String) cmbStasiunAsalJadwal.getSelectedItem(),
                (String) cmbStasiunTujuanJadwal.getSelectedItem(),
                (Date) spnWaktuBerangkat.getValue(),
                (Date) spnWaktuTiba.getValue(),
                txtHargaDasar.getText().trim(),
                txtTotalGerbong.getText().trim(),
                txtKapasitasKursi.getText().trim()
            );
            if (res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Jadwal berhasil ditambahkan!"); refreshAllData();
            } else { JOptionPane.showMessageDialog(this, res); }
        });

        btnEditJadwal.addActionListener(e -> {
            String res = controller.updateJadwal(
                selectedIdJadwal, 
                txtNamaKereta.getText().trim(), 
                (String)cmbStasiunAsalJadwal.getSelectedItem(), 
                (String)cmbStasiunTujuanJadwal.getSelectedItem(), 
                (Date)spnWaktuBerangkat.getValue(), 
                (Date)spnWaktuTiba.getValue(), 
                txtHargaDasar.getText().trim(), 
                txtTotalGerbong.getText().trim(), 
                txtKapasitasKursi.getText().trim()
            );
            if (res.equals("SUCCESS")) { JOptionPane.showMessageDialog(this, "Jadwal berhasil diupdate!"); refreshAllData(); } 
            else { JOptionPane.showMessageDialog(this, res); }
        });

        btnClearJadwal.addActionListener(e -> {
            selectedIdJadwal = -1; txtNamaKereta.setText(""); txtHargaDasar.setText(""); txtTotalGerbong.setText(""); txtKapasitasKursi.setText("");
            spnWaktuBerangkat.setValue(new Date()); spnWaktuTiba.setValue(new Date()); jTable1.clearSelection();
        });

        // --- MENU STASIUN ---
        tblStasiun.getSelectionModel().addListSelectionListener(e -> {
            int viewRow = tblStasiun.getSelectedRow();
            if (viewRow >= 0) {
                int row = tblStasiun.convertRowIndexToModel(viewRow);
                selectedIdStasiun = Integer.parseInt(modelStasiun.getValueAt(row, 0).toString());
                txtNamaStasiun.setText(modelStasiun.getValueAt(row, 1).toString());
                txtKodeKota.setText(modelStasiun.getValueAt(row, 2).toString());
                cmbKategoriLayanan.setSelectedItem(modelStasiun.getValueAt(row, 3).toString());
            }
        });

        btnTambahStasiun.addActionListener(e -> {
            String res = controller.tambahStasiun(txtNamaStasiun.getText().trim(), txtKodeKota.getText().trim(), (String)cmbKategoriLayanan.getSelectedItem());
            if (res.equals("SUCCESS")) { JOptionPane.showMessageDialog(this, "Stasiun ditambahkan!"); refreshAllData(); } 
            else { JOptionPane.showMessageDialog(this, res); }
        });

        btnEditStasiun.addActionListener(e -> {
            String res = controller.updateStasiun(selectedIdStasiun, txtNamaStasiun.getText().trim(), txtKodeKota.getText().trim(), (String)cmbKategoriLayanan.getSelectedItem());
            if (res.equals("SUCCESS")) { JOptionPane.showMessageDialog(this, "Stasiun diupdate!"); refreshAllData(); } 
            else { JOptionPane.showMessageDialog(this, res); }
        });

        // --- MENU TARIF KOMUTER ---
        tblTarifKomuter.getSelectionModel().addListSelectionListener(e -> {
            int viewRow = tblTarifKomuter.getSelectedRow();
            if (viewRow >= 0) {
                int row = tblTarifKomuter.convertRowIndexToModel(viewRow);
                selectedIdTarif = modelTarif.getValueAt(row, 0).toString();
                cmbTarifStasiunAsal.setSelectedItem(modelTarif.getValueAt(row, 1).toString());
                cmbTarifStasiunAkhir.setSelectedItem(modelTarif.getValueAt(row, 2).toString());
                txtHargaRute.setText(modelTarif.getValueAt(row, 3).toString());
            }
        });

        btnTambahTarif.addActionListener(e -> {
            String res = controller.tambahTarif((String)cmbTarifStasiunAsal.getSelectedItem(), (String)cmbTarifStasiunAkhir.getSelectedItem(), txtHargaRute.getText().trim());
            if (res.equals("SUCCESS")) { JOptionPane.showMessageDialog(this, "Tarif ditambahkan!"); refreshAllData(); } 
            else { JOptionPane.showMessageDialog(this, res); }
        });

        btnEditTarif.addActionListener(e -> {
            String res = controller.updateTarif(selectedIdTarif, (String)cmbTarifStasiunAsal.getSelectedItem(), (String)cmbTarifStasiunAkhir.getSelectedItem(), txtHargaRute.getText().trim());
            if (res.equals("SUCCESS")) { JOptionPane.showMessageDialog(this, "Tarif diupdate!"); refreshAllData(); } 
            else { JOptionPane.showMessageDialog(this, res); }
        });

        // --- MENU TAP-OUT KOMUTER ---
        tblTapInKomuter.getSelectionModel().addListSelectionListener(e -> {
            int viewRow = tblTapInKomuter.getSelectedRow();
            if (viewRow >= 0) {
                int row = tblTapInKomuter.convertRowIndexToModel(viewRow);
                txtIdPerjalananTerpilih.setText(modelTapOut.getValueAt(row, 0).toString());
                txtNikPenumpangTapOut.setText(modelTapOut.getValueAt(row, 1).toString() + " - " + modelTapOut.getValueAt(row, 2).toString());
                String asalStr = modelTapOut.getValueAt(row, 4).toString();
                selectedIdAsalTapOut = Integer.parseInt(asalStr.split(" - ")[0].trim());
                
                cmbStasiunKeluar.removeAllItems();
                cmbStasiunKeluar.addItem("-- Pilih Stasiun Akhir --");
                for (String stTujuan : controller.dapatkanStasiunTujuanTapOut(selectedIdAsalTapOut)) {
                    cmbStasiunKeluar.addItem(stTujuan);
                }
                currentEstimasiTarif = 0.0;
                lblEstimasiTarif.setText("Tarif Potongan: Rp 0");
            }
        });

        cmbStasiunKeluar.addActionListener(e -> {
            currentEstimasiTarif = controller.hitungEstimasiTarif(selectedIdAsalTapOut, (String)cmbStasiunKeluar.getSelectedItem());
            lblEstimasiTarif.setText("Tarif Potongan: Rp " + String.format("%,.0f", currentEstimasiTarif));
        });

        btnProsesTapOut.addActionListener(e -> {
            String adminCode = (SessionManager.getCurrentAdmin() != null) ? SessionManager.getCurrentAdmin().getKodeKaryawan() : "AXISEMPL_01";
            String res = controller.prosesVerifikasiTapOut(txtIdPerjalananTerpilih.getText().trim(), txtNikPenumpangTapOut.getText(), (String)cmbStasiunKeluar.getSelectedItem(), currentEstimasiTarif, adminCode);
            if (res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Tap-Out Berhasil! Saldo potong Rp" + String.format("%,.0f", currentEstimasiTarif));
                txtIdPerjalananTerpilih.setText(""); txtNikPenumpangTapOut.setText(""); lblEstimasiTarif.setText("Tarif Potongan: Rp 0");
                selectedIdAsalTapOut = -1;
                cmbStasiunKeluar.removeAllItems();
                cmbStasiunKeluar.addItem("-- Pilih Stasiun Akhir --");
                refreshAllData();
            } else { JOptionPane.showMessageDialog(this, res, "Gagal Tap-Out", JOptionPane.ERROR_MESSAGE); }
        });

        // --- MENU MONITORING ---
        btnCariPemesanan.addActionListener(e -> {
            controller.muatMonitoring(modelMonitoring, txtCariKodeBooking.getText().trim());
            modelDetailTiket.setRowCount(0);
        });
        btnResetMonitoring.addActionListener(e -> {
            txtCariKodeBooking.setText("");
            cmbSortMonitoringWaktu.setSelectedIndex(0);
            controller.muatMonitoring(modelMonitoring, null);
            modelDetailTiket.setRowCount(0);
        });
        tblMonitoringPemesanan.getSelectionModel().addListSelectionListener(e -> {
            int viewRow = tblMonitoringPemesanan.getSelectedRow();
            if (viewRow >= 0) {
                int row = tblMonitoringPemesanan.convertRowIndexToModel(viewRow);
                controller.muatDetailTiket(modelDetailTiket, modelMonitoring.getValueAt(row, 1).toString());
            }
        });
    }

    private void switchPanel(String cardName, javax.swing.JButton activeButton) {
        cardLayout.show(pnlContent, cardName);
        javax.swing.JButton[] navButtons = { btnMenuDashboard, btnMenuJadwal, btnMenuStasiun, btnMenuTapOut, btnMenuMonitoring };
        for (javax.swing.JButton btn : navButtons) {
            btn.setBackground(new Color(45, 87, 48)); btn.setForeground(Color.WHITE);
        }
        activeButton.setBackground(new Color(232, 245, 233)); activeButton.setForeground(new Color(27, 94, 32));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlSidebar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblInfoAdmin = new javax.swing.JLabel();
        btnMenuDashboard = new javax.swing.JButton();
        btnMenuJadwal = new javax.swing.JButton();
        btnMenuStasiun = new javax.swing.JButton();
        btnMenuTapOut = new javax.swing.JButton();
        btnMenuMonitoring = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        pnlContent = new javax.swing.JPanel();
        pnlDashboard = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblTotalGabungan = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalKomuter = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lbl3 = new javax.swing.JLabel();
        lblTotalAntarkota = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblTotalKeberangkatan = new javax.swing.JLabel();
        btnRefreshDashboard = new javax.swing.JButton();
        pnlKelolaJadwal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtNamaKereta = new javax.swing.JTextField();
        cmbStasiunAsalJadwal = new javax.swing.JComboBox<>();
        cmbStasiunTujuanJadwal = new javax.swing.JComboBox<>();
        txtHargaDasar = new javax.swing.JTextField();
        txtTotalGerbong = new javax.swing.JTextField();
        txtKapasitasKursi = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnTambahJadwal = new javax.swing.JButton();
        btnEditJadwal = new javax.swing.JButton();
        btnClearJadwal = new javax.swing.JButton();
        spnWaktuBerangkat = new javax.swing.JSpinner();
        spnWaktuTiba = new javax.swing.JSpinner();
        pnlKelolaStasiun = new javax.swing.JPanel();
        jTabbedPane = new javax.swing.JTabbedPane();
        pnlSubStasiun = new javax.swing.JPanel();
        txtNamaStasiun = new javax.swing.JTextField();
        txtKodeKota = new javax.swing.JTextField();
        cmbKategoriLayanan = new javax.swing.JComboBox<>();
        btnTambahStasiun = new javax.swing.JButton();
        btnEditStasiun = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblStasiun = new javax.swing.JTable();
        pnlSubTarif = new javax.swing.JPanel();
        cmbTarifStasiunAsal = new javax.swing.JComboBox<>();
        cmbTarifStasiunAkhir = new javax.swing.JComboBox<>();
        txtHargaRute = new javax.swing.JTextField();
        btnTambahTarif = new javax.swing.JButton();
        btnEditTarif = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTarifKomuter = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        pnlVerifikasiTapOut = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblTapInKomuter = new javax.swing.JTable();
        txtIdPerjalananTerpilih = new javax.swing.JTextField();
        txtNikPenumpangTapOut = new javax.swing.JTextField();
        cmbStasiunKeluar = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblEstimasiTarif = new javax.swing.JLabel();
        btnProsesTapOut = new javax.swing.JButton();
        pnlMonitoring = new javax.swing.JPanel();
        txtCariKodeBooking = new javax.swing.JTextField();
        btnCariPemesanan = new javax.swing.JButton();
        btnResetMonitoring = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblMonitoringPemesanan = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblDetailTiketPemesanan = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlSidebar.setBackground(new java.awt.Color(27, 94, 32));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\SEMESTER 3\\CAPSTONE\\AXIS\\src\\main\\resources\\image\\asix-upd-wht.png")); // NOI18N
        jLabel1.setText("jLabel1");

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel2.setText("ADMIN");

        lblInfoAdmin.setText("kode_karyawan");

        btnMenuDashboard.setBackground(new java.awt.Color(204, 204, 204));
        btnMenuDashboard.setForeground(new java.awt.Color(51, 51, 51));
        btnMenuDashboard.setText("Dashboard");
        btnMenuDashboard.addActionListener(this::btnMenuDashboardActionPerformed);

        btnMenuJadwal.setBackground(new java.awt.Color(204, 204, 204));
        btnMenuJadwal.setForeground(new java.awt.Color(51, 51, 51));
        btnMenuJadwal.setText("Kelola Antarkota");
        btnMenuJadwal.addActionListener(this::btnMenuJadwalActionPerformed);

        btnMenuStasiun.setBackground(new java.awt.Color(204, 204, 204));
        btnMenuStasiun.setForeground(new java.awt.Color(51, 51, 51));
        btnMenuStasiun.setText("Kelola Stasiun");
        btnMenuStasiun.addActionListener(this::btnMenuStasiunActionPerformed);

        btnMenuTapOut.setBackground(new java.awt.Color(204, 204, 204));
        btnMenuTapOut.setForeground(new java.awt.Color(51, 51, 51));
        btnMenuTapOut.setText("Verif Tap-Out");
        btnMenuTapOut.addActionListener(this::btnMenuTapOutActionPerformed);

        btnMenuMonitoring.setBackground(new java.awt.Color(204, 204, 204));
        btnMenuMonitoring.setForeground(new java.awt.Color(51, 51, 51));
        btnMenuMonitoring.setText("Monitor Pemesanan");
        btnMenuMonitoring.addActionListener(this::btnMenuMonitoringActionPerformed);

        btnLogout.setBackground(new java.awt.Color(51, 51, 51));
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("LogOut");
        btnLogout.addActionListener(this::btnLogoutActionPerformed);

        javax.swing.GroupLayout pnlSidebarLayout = new javax.swing.GroupLayout(pnlSidebar);
        pnlSidebar.setLayout(pnlSidebarLayout);
        pnlSidebarLayout.setHorizontalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSidebarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMenuTapOut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMenuStasiun, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMenuJadwal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMenuDashboard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMenuMonitoring, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                    .addComponent(lblInfoAdmin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(141, 141, 141))
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSidebarLayout.setVerticalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInfoAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMenuDashboard)
                .addGap(18, 18, 18)
                .addComponent(btnMenuJadwal)
                .addGap(18, 18, 18)
                .addComponent(btnMenuStasiun)
                .addGap(18, 18, 18)
                .addComponent(btnMenuTapOut)
                .addGap(18, 18, 18)
                .addComponent(btnMenuMonitoring)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(40, 40, 40))
        );

        pnlContent.setBackground(new java.awt.Color(204, 204, 204));
        pnlContent.setLayout(new java.awt.CardLayout());

        pnlDashboard.setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(0, 102, 51));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Total Gabungan Penumpang");

        lblTotalGabungan.setFont(new java.awt.Font("Verdana", 1, 36)); // NOI18N
        lblTotalGabungan.setText("jLabel3");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalGabungan, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalGabungan, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 102, 51));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Penumpang Komuter Hari Ini");

        lblTotalKomuter.setFont(new java.awt.Font("Verdana", 1, 36)); // NOI18N
        lblTotalKomuter.setText("jLabel3");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalKomuter, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalKomuter, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(0, 102, 51));

        lbl3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbl3.setText("Penumpang Antarkota Hari Ini");

        lblTotalAntarkota.setFont(new java.awt.Font("Verdana", 1, 36)); // NOI18N
        lblTotalAntarkota.setText("jLabel3");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalAntarkota, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbl3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTotalAntarkota, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 51));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Jumlah Keberangkatan");

        lblTotalKeberangkatan.setFont(new java.awt.Font("Verdana", 1, 36)); // NOI18N
        lblTotalKeberangkatan.setText("jLabel3");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalKeberangkatan, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalKeberangkatan, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        btnRefreshDashboard.setText("Refresh Data");

        javax.swing.GroupLayout pnlDashboardLayout = new javax.swing.GroupLayout(pnlDashboard);
        pnlDashboard.setLayout(pnlDashboardLayout);
        pnlDashboardLayout.setHorizontalGroup(
            pnlDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33)
                .addGroup(pnlDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(pnlDashboardLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(btnRefreshDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDashboardLayout.setVerticalGroup(
            pnlDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDashboardLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(pnlDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(pnlDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addComponent(btnRefreshDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127))
        );

        pnlContent.add(pnlDashboard, "cardDashboard");

        pnlKelolaJadwal.setBackground(new java.awt.Color(204, 204, 204));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        cmbStasiunAsalJadwal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbStasiunTujuanJadwal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Nama Kereta");

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Pilih Stasiun Asal");

        jLabel8.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Pilih Stasiun Tujuan");

        jLabel9.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Waktu Berangkat (YYYY-MM-DD HH:MM:SS)   ");

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Waktu Tiba (YYYY-MM-DD HH:MM:SS)");

        jLabel11.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Harga Dasar (Rp)");

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Total Gerbong");

        jLabel13.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Kapasitas Kursi/Gerbong");

        btnTambahJadwal.setText("Tambah Jadwal");

        btnEditJadwal.setText("Update Jadwal");

        btnClearJadwal.setText("Reset From");

        spnWaktuBerangkat.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1793595789216L), null, null, java.util.Calendar.DAY_OF_MONTH));

        spnWaktuTiba.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1793595660000L), null, null, java.util.Calendar.DAY_OF_MONTH));

        javax.swing.GroupLayout pnlKelolaJadwalLayout = new javax.swing.GroupLayout(pnlKelolaJadwal);
        pnlKelolaJadwal.setLayout(pnlKelolaJadwalLayout);
        pnlKelolaJadwalLayout.setHorizontalGroup(
            pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(pnlKelolaJadwalLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel3)
                    .addGroup(pnlKelolaJadwalLayout.createSequentialGroup()
                        .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(spnWaktuTiba)
                                .addComponent(txtNamaKereta, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbStasiunAsalJadwal, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbStasiunTujuanJadwal, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtHargaDasar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                                .addGroup(pnlKelolaJadwalLayout.createSequentialGroup()
                                    .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTotalGerbong, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtKapasitasKursi)))
                                .addComponent(spnWaktuBerangkat, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel10))
                        .addGap(61, 61, 61)
                        .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnTambahJadwal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEditJadwal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnClearJadwal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(96, Short.MAX_VALUE))
        );
        pnlKelolaJadwalLayout.setVerticalGroup(
            pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlKelolaJadwalLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addComponent(txtNamaKereta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStasiunAsalJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambahJadwal))
                .addGap(1, 1, 1)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStasiunTujuanJadwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditJadwal))
                .addGap(1, 1, 1)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearJadwal)
                    .addComponent(spnWaktuBerangkat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnWaktuTiba, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHargaDasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(4, 4, 4)
                .addGroup(pnlKelolaJadwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKapasitasKursi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotalGerbong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
        );

        pnlContent.add(pnlKelolaJadwal, "cardJadwal");

        pnlKelolaStasiun.setBackground(new java.awt.Color(204, 204, 204));

        pnlSubStasiun.setBackground(new java.awt.Color(153, 153, 153));

        cmbKategoriLayanan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Antarkota", "Komuter", "Keduanya" }));

        btnTambahStasiun.setText("Tambah Stasiun");

        btnEditStasiun.setText("Update Stasiun");

        jLabel14.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Nama Stasiun");

        jLabel15.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("Nama/Kode Kota (contoh: JKT, BDO)");

        jLabel16.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Jenis Layanan");

        tblStasiun.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblStasiun);

        javax.swing.GroupLayout pnlSubStasiunLayout = new javax.swing.GroupLayout(pnlSubStasiun);
        pnlSubStasiun.setLayout(pnlSubStasiunLayout);
        pnlSubStasiunLayout.setHorizontalGroup(
            pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE))
            .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                        .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                                .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(cmbKategoriLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTambahStasiun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEditStasiun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                        .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtNamaStasiun, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                            .addComponent(txtKodeKota, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlSubStasiunLayout.setVerticalGroup(
            pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubStasiunLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNamaStasiun, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKodeKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(btnTambahStasiun))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlSubStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditStasiun)
                    .addComponent(cmbKategoriLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Kelola Stasiun", pnlSubStasiun);

        pnlSubTarif.setBackground(new java.awt.Color(153, 153, 153));

        cmbTarifStasiunAsal.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbTarifStasiunAkhir.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnTambahTarif.setText("Tambah Tarif");

        btnEditTarif.setText("Edit Harga Tarif");

        tblTarifKomuter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblTarifKomuter);

        jLabel17.setText("jLabel17");

        jLabel18.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Stasiun Awal");

        jLabel19.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setText("Stasiun Akhir");

        jLabel20.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Harga");

        javax.swing.GroupLayout pnlSubTarifLayout = new javax.swing.GroupLayout(pnlSubTarif);
        pnlSubTarif.setLayout(pnlSubTarifLayout);
        pnlSubTarifLayout.setHorizontalGroup(
            pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSubTarifLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3))
            .addGroup(pnlSubTarifLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSubTarifLayout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlSubTarifLayout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(208, 208, 208))
                    .addGroup(pnlSubTarifLayout.createSequentialGroup()
                        .addGroup(pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmbTarifStasiunAsal, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbTarifStasiunAkhir, 0, 234, Short.MAX_VALUE)
                                .addComponent(txtHargaRute))
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTambahTarif, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEditTarif, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlSubTarifLayout.setVerticalGroup(
            pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSubTarifLayout.createSequentialGroup()
                .addGroup(pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSubTarifLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel17)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSubTarifLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(pnlSubTarifLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSubTarifLayout.createSequentialGroup()
                        .addComponent(btnTambahTarif)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditTarif))
                    .addGroup(pnlSubTarifLayout.createSequentialGroup()
                        .addComponent(cmbTarifStasiunAsal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19)
                        .addGap(2, 2, 2)
                        .addComponent(cmbTarifStasiunAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel20)
                        .addGap(2, 2, 2)
                        .addComponent(txtHargaRute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane.addTab("Kelola Tarif", pnlSubTarif);

        javax.swing.GroupLayout pnlKelolaStasiunLayout = new javax.swing.GroupLayout(pnlKelolaStasiun);
        pnlKelolaStasiun.setLayout(pnlKelolaStasiunLayout);
        pnlKelolaStasiunLayout.setHorizontalGroup(
            pnlKelolaStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlKelolaStasiunLayout.createSequentialGroup()
                .addComponent(jTabbedPane)
                .addContainerGap())
        );
        pnlKelolaStasiunLayout.setVerticalGroup(
            pnlKelolaStasiunLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlKelolaStasiunLayout.createSequentialGroup()
                .addComponent(jTabbedPane)
                .addContainerGap())
        );

        pnlContent.add(pnlKelolaStasiun, "cardStasiun");

        pnlVerifikasiTapOut.setBackground(new java.awt.Color(204, 204, 204));

        tblTapInKomuter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tblTapInKomuter);

        txtIdPerjalananTerpilih.setEditable(false);

        txtNikPenumpangTapOut.setEditable(false);

        cmbStasiunKeluar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel21.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(51, 51, 51));
        jLabel21.setText("Id Terpilih");

        jLabel22.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(51, 51, 51));
        jLabel22.setText("NIK, Nama");

        jLabel23.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(51, 51, 51));
        jLabel23.setText("VERIFIKASI TAP-OUT");

        lblEstimasiTarif.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        lblEstimasiTarif.setForeground(new java.awt.Color(51, 51, 51));
        lblEstimasiTarif.setText(".");

        btnProsesTapOut.setText("Verifikasi Tap-Out");

        javax.swing.GroupLayout pnlVerifikasiTapOutLayout = new javax.swing.GroupLayout(pnlVerifikasiTapOut);
        pnlVerifikasiTapOut.setLayout(pnlVerifikasiTapOutLayout);
        pnlVerifikasiTapOutLayout.setHorizontalGroup(
            pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVerifikasiTapOutLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVerifikasiTapOutLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlVerifikasiTapOutLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlVerifikasiTapOutLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEstimasiTarif, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlVerifikasiTapOutLayout.createSequentialGroup()
                                .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtIdPerjalananTerpilih)
                                    .addComponent(txtNikPenumpangTapOut)
                                    .addComponent(cmbStasiunKeluar, 0, 327, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22)
                                    .addComponent(btnProsesTapOut))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlVerifikasiTapOutLayout.setVerticalGroup(
            pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVerifikasiTapOutLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdPerjalananTerpilih, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNikPenumpangTapOut, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(29, 29, 29)
                .addGroup(pnlVerifikasiTapOutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbStasiunKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProsesTapOut))
                .addGap(18, 18, 18)
                .addComponent(lblEstimasiTarif)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pnlContent.add(pnlVerifikasiTapOut, "cardTapOut");

        pnlMonitoring.setBackground(new java.awt.Color(204, 204, 204));

        btnCariPemesanan.setText("Cari");

        btnResetMonitoring.setText("Tampilkan Semua");

        jLabel24.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 51, 51));
        jLabel24.setText("cari berdasarkan Kode Booking atau NIK.");

        tblMonitoringPemesanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(tblMonitoringPemesanan);

        tblDetailTiketPemesanan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(tblDetailTiketPemesanan);

        javax.swing.GroupLayout pnlMonitoringLayout = new javax.swing.GroupLayout(pnlMonitoring);
        pnlMonitoring.setLayout(pnlMonitoringLayout);
        pnlMonitoringLayout.setHorizontalGroup(
            pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMonitoringLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                                .addComponent(txtCariKodeBooking, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCariPemesanan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnResetMonitoring)))
                        .addGap(0, 56, Short.MAX_VALUE))
                    .addGroup(pnlMonitoringLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5))
                    .addGroup(pnlMonitoringLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6)))
                .addContainerGap())
        );
        pnlMonitoringLayout.setVerticalGroup(
            pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMonitoringLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMonitoringLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCariKodeBooking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCariPemesanan)
                    .addComponent(btnResetMonitoring))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        pnlContent.add(pnlMonitoring, "cardMonitoring");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMenuDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuDashboardActionPerformed
        switchPanel("cardDashboard", btnMenuDashboard);
    }//GEN-LAST:event_btnMenuDashboardActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        SessionManager.clearSession();
        new LoginView().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnMenuJadwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuJadwalActionPerformed
        switchPanel("cardJadwal", btnMenuJadwal);
    }//GEN-LAST:event_btnMenuJadwalActionPerformed

    private void btnMenuStasiunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuStasiunActionPerformed
        switchPanel("cardStasiun", btnMenuStasiun);
    }//GEN-LAST:event_btnMenuStasiunActionPerformed

    private void btnMenuTapOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuTapOutActionPerformed
        switchPanel("cardTapOut", btnMenuTapOut);
    }//GEN-LAST:event_btnMenuTapOutActionPerformed

    private void btnMenuMonitoringActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuMonitoringActionPerformed
        switchPanel("cardMonitoring", btnMenuMonitoring);
    }//GEN-LAST:event_btnMenuMonitoringActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new DashboardAdminView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCariPemesanan;
    private javax.swing.JButton btnClearJadwal;
    private javax.swing.JButton btnEditJadwal;
    private javax.swing.JButton btnEditStasiun;
    private javax.swing.JButton btnEditTarif;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMenuDashboard;
    private javax.swing.JButton btnMenuJadwal;
    private javax.swing.JButton btnMenuMonitoring;
    private javax.swing.JButton btnMenuStasiun;
    private javax.swing.JButton btnMenuTapOut;
    private javax.swing.JButton btnProsesTapOut;
    private javax.swing.JButton btnRefreshDashboard;
    private javax.swing.JButton btnResetMonitoring;
    private javax.swing.JButton btnTambahJadwal;
    private javax.swing.JButton btnTambahStasiun;
    private javax.swing.JButton btnTambahTarif;
    private javax.swing.JComboBox<String> cmbKategoriLayanan;
    private javax.swing.JComboBox<String> cmbStasiunAsalJadwal;
    private javax.swing.JComboBox<String> cmbStasiunKeluar;
    private javax.swing.JComboBox<String> cmbStasiunTujuanJadwal;
    private javax.swing.JComboBox<String> cmbTarifStasiunAkhir;
    private javax.swing.JComboBox<String> cmbTarifStasiunAsal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lblEstimasiTarif;
    private javax.swing.JLabel lblInfoAdmin;
    private javax.swing.JLabel lblTotalAntarkota;
    private javax.swing.JLabel lblTotalGabungan;
    private javax.swing.JLabel lblTotalKeberangkatan;
    private javax.swing.JLabel lblTotalKomuter;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlDashboard;
    private javax.swing.JPanel pnlKelolaJadwal;
    private javax.swing.JPanel pnlKelolaStasiun;
    private javax.swing.JPanel pnlMonitoring;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlSubStasiun;
    private javax.swing.JPanel pnlSubTarif;
    private javax.swing.JPanel pnlVerifikasiTapOut;
    private javax.swing.JSpinner spnWaktuBerangkat;
    private javax.swing.JSpinner spnWaktuTiba;
    private javax.swing.JTable tblDetailTiketPemesanan;
    private javax.swing.JTable tblMonitoringPemesanan;
    private javax.swing.JTable tblStasiun;
    private javax.swing.JTable tblTapInKomuter;
    private javax.swing.JTable tblTarifKomuter;
    private javax.swing.JTextField txtCariKodeBooking;
    private javax.swing.JTextField txtHargaDasar;
    private javax.swing.JTextField txtHargaRute;
    private javax.swing.JTextField txtIdPerjalananTerpilih;
    private javax.swing.JTextField txtKapasitasKursi;
    private javax.swing.JTextField txtKodeKota;
    private javax.swing.JTextField txtNamaKereta;
    private javax.swing.JTextField txtNamaStasiun;
    private javax.swing.JTextField txtNikPenumpangTapOut;
    private javax.swing.JTextField txtTotalGerbong;
    // End of variables declaration//GEN-END:variables

    

}
