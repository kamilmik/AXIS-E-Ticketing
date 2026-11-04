package controller;

import dao.AdminDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AdminController {
    private final AdminDAO adminDAO = new AdminDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // =========================================================================
    // 1. FUNGSI DASHBOARD & HELPER
    // =========================================================================
    public int[] dapatkanStatistik() {
        return adminDAO.getDashboardStats();
    }

    // Helper: Ekstrak ID dari ComboBox
    private int ekstrakIdCombo(String comboValue) {
        if (comboValue == null || comboValue.isEmpty() || !comboValue.contains(" - ") || comboValue.startsWith("--")) {
            return -1;
        }
        return Integer.parseInt(comboValue.split(" - ")[0].trim());
    }

    public List<String> dapatkanListStasiun(String kategoriLayanan) {
        return adminDAO.getComboStasiun(kategoriLayanan);
    }

    public List<String> dapatkanStasiunTujuanTapOut(int idStasiunAsal) {
        return adminDAO.getComboStasiunTujuanTapOut(idStasiunAsal);
    }

    // =========================================================================
    // 2. FUNGSI MENU JADWAL ANTARKOTA (Validasi Waktu)
    // =========================================================================
    public void muatTabelJadwal(DefaultTableModel model) {
        adminDAO.loadJadwalAntarkota(model);
    }

    public String tambahJadwal(String nama, String asalStr, String tujuanStr, Date brgktDate, Date tibaDate, String hargaStr, String gerbongStr, String kursiStr) {
        try {
            if (nama.isEmpty() || asalStr == null || tujuanStr == null) return "Nama kereta dan stasiun wajib diisi!";
            int asal = ekstrakIdCombo(asalStr);
            int tujuan = ekstrakIdCombo(tujuanStr);
            if (asal == tujuan) return "Stasiun asal dan tujuan tidak boleh sama!";

            // Validasi Waktu
            if (brgktDate.before(new Date(System.currentTimeMillis() - 60000))) {
                return "Waktu berangkat tidak boleh di masa lalu!";
            }
            if (!tibaDate.after(brgktDate)) {
                return "Waktu tiba harus setelah waktu berangkat!";
            }

            String brgkt = sdf.format(brgktDate);
            String tiba = sdf.format(tibaDate);
            double harga = Double.parseDouble(hargaStr);
            int gerbong = Integer.parseInt(gerbongStr);
            int kursi = Integer.parseInt(kursiStr);

            return adminDAO.insertJadwal(nama, asal, tujuan, brgkt, tiba, harga, gerbong, kursi) ? "SUCCESS" : "Gagal menyimpan jadwal.";
        } catch (NumberFormatException e) {
            return "Format angka salah! Pastikan harga, gerbong, dan kursi berisi angka.";
        }
    }

    public String updateJadwal(int idJadwal, String nama, String asalStr, String tujuanStr, Date brgktDate, Date tibaDate, String hargaStr, String gerbongStr, String kursiStr) {
        if (idJadwal == -1) return "Pilih data jadwal di tabel terlebih dahulu!";
        
        // Validasi Waktu
        if (brgktDate.before(new Date(System.currentTimeMillis() - 60000))) {
            return "Waktu berangkat tidak boleh diubah ke masa lalu!";
        }
        if (!tibaDate.after(brgktDate)) {
            return "Waktu tiba harus setelah waktu berangkat!";
        }
        
        try {
            int asal = ekstrakIdCombo(asalStr);
            int tujuan = ekstrakIdCombo(tujuanStr);
            if (asal == tujuan) return "Stasiun asal dan tujuan tidak boleh sama!";
            String brgkt = sdf.format(brgktDate);
            String tiba = sdf.format(tibaDate);
            double harga = Double.parseDouble(hargaStr);
            int gerbong = Integer.parseInt(gerbongStr);
            int kursi = Integer.parseInt(kursiStr);

            return adminDAO.updateJadwal(idJadwal, nama, asal, tujuan, brgkt, tiba, harga, gerbong, kursi) ? "SUCCESS" : "Gagal mengupdate jadwal.";
        } catch (NumberFormatException e) {
            return "Format angka tidak valid!";
        }
    }

    // =========================================================================
    // 3. FUNGSI MENU KELOLA STASIUN & TARIF
    // =========================================================================
    public void muatTabelStasiun(DefaultTableModel model) {
        adminDAO.loadStasiun(model);
    }

    public String tambahStasiun(String nama, String kota, String kategori) {
        if (nama.isEmpty() || kota.isEmpty()) return "Nama stasiun dan kode kota wajib diisi!";
        return adminDAO.insertStasiun(nama, kota, kategori) ? "SUCCESS" : "Gagal menambah stasiun.";
    }

    public String updateStasiun(int idStasiun, String nama, String kota, String kategori) {
        if (idStasiun == -1) return "Pilih stasiun di tabel terlebih dahulu!";
        return adminDAO.updateStasiun(idStasiun, nama, kota, kategori) ? "SUCCESS" : "Gagal mengupdate stasiun.";
    }

    public void muatTabelTarif(DefaultTableModel model) {
        adminDAO.loadTarifKomuter(model);
    }

    public String tambahTarif(String asalStr, String tujuanStr, String hargaStr) {
        try {
            int asal = ekstrakIdCombo(asalStr);
            int akhir = ekstrakIdCombo(tujuanStr);
            if (asal == akhir) return "Stasiun awal dan akhir tidak boleh sama!";
            
            double harga = Double.parseDouble(hargaStr);
            return adminDAO.insertTarif(asal, akhir, harga) ? "SUCCESS" : "Gagal menambah tarif.";
        } catch (NumberFormatException e) {
            return "Format harga harus berupa angka!";
        }
    }

    public String updateTarif(String idTarif, String asalStr, String tujuanStr, String hargaStr) {
        if (idTarif == null) return "Pilih tarif di tabel terlebih dahulu!";
        try {
            int asal = ekstrakIdCombo(asalStr);
            int akhir = ekstrakIdCombo(tujuanStr);
            double harga = Double.parseDouble(hargaStr);
            return adminDAO.updateTarif(idTarif, asal, akhir, harga) ? "SUCCESS" : "Gagal mengupdate tarif.";
        } catch (NumberFormatException e) {
            return "Format harga harus berupa angka!";
        }
    }

    // =========================================================================
    // 4. FUNGSI VERIFIKASI TAP-OUT
    // =========================================================================
    public void muatTabelTapOut(DefaultTableModel model) {
        adminDAO.loadTapInKomuter(model);
    }

    public double hitungEstimasiTarif(int idStasiunAsal, String stasiunTujuanStr) {
        int idTujuan = ekstrakIdCombo(stasiunTujuanStr);
        if (idStasiunAsal == -1 || idTujuan == -1) return 0.0;
        return adminDAO.getHargaTarifKomuter(idStasiunAsal, idTujuan);
    }

    public String prosesVerifikasiTapOut(String idPerjalananStr, String nikFull, String stasiunKeluarStr, double tarif, String kodeAdmin) {
        if (idPerjalananStr.isEmpty()) return "Pilih penumpang dari tabel terlebih dahulu!";
        int idKeluar = ekstrakIdCombo(stasiunKeluarStr);
        if (idKeluar == -1) return "Silakan pilih Stasiun Akhir terlebih dahulu!";
        if (tarif <= 0) return "Rute tarif komuter tidak ditemukan untuk stasiun ini!";

        int idPerjalanan = Integer.parseInt(idPerjalananStr);
        String nik = nikFull.split(" - ")[0].trim();
        return adminDAO.verifikasiTapOut(idPerjalanan, nik, idKeluar, tarif, kodeAdmin);
    }

    // =========================================================================
    // 5. FUNGSI MONITORING PEMESANAN
    // =========================================================================
    public void muatMonitoring(DefaultTableModel model, String keyword) {
        adminDAO.loadMonitoringPemesanan(model, keyword);
    }

    public void muatDetailTiket(DefaultTableModel model, String kodeBooking) {
        adminDAO.loadDetailTiket(model, kodeBooking);
    }
}