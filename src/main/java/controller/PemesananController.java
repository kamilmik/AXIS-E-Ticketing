package controller;

import dao.PemesananDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.JadwalAntarkota;
import model.Tiket;

public class PemesananController {
    private final PemesananDAO pemesananDAO = new PemesananDAO();
    
    private List<JadwalAntarkota> listJadwalTersedia;
    private int jumlahPenumpang;
    private JadwalAntarkota jadwalTerpilih;
    private final List<Tiket> listTiketInput = new ArrayList<>();

    public void inisialisasiPencarian(List<JadwalAntarkota> hasilCari, int jmlPax) {
        this.listJadwalTersedia = hasilCari;
        this.jumlahPenumpang = jmlPax;
        this.listTiketInput.clear();
    }

    public void isiTabelJadwal(DefaultTableModel model) {
        model.setRowCount(0);
        if (listJadwalTersedia == null) return;
        for (JadwalAntarkota j : listJadwalTersedia) {
            model.addRow(new Object[]{
                j.getIdJadwal(),
                j.getNamaKereta(),
                j.getWaktuBerangkat(),
                j.getWaktuTiba(),
                String.format("Rp %,.0f", j.getHargaDasar())
            });
        }
    }

    public boolean setJadwalTerpilih(int idJadwal) {
        for (JadwalAntarkota j : listJadwalTersedia) {
            if (j.getIdJadwal() == idJadwal) {
                this.jadwalTerpilih = j;
                return true;
            }
        }
        return false;
    }

    public JadwalAntarkota getJadwalTerpilih() {
        return jadwalTerpilih;
    }

    public String tambahDataPenumpang(String nik, String nama, String telp) {
        if (nik.isEmpty() || nama.isEmpty() || telp.isEmpty()) {
            return "Data tidak sesuai validasi sistem, semua kolom wajib diisi!";
        }
        if (!nik.matches("\\d+") || !telp.matches("\\d+")) {
            return "Data tidak sesuai validasi sistem, NIK dan No. Telepon harus angka!";
        }
        int nomorKursiOtomatis = listTiketInput.size() + 1;
        Tiket t = new Tiket(
            0, "", nama, nik, 
            "Kursi " + nomorKursiOtomatis, 
            "Gerbong 1", 
            "", 
            jadwalTerpilih != null ? jadwalTerpilih.getIdJadwal() : 0
        );
        listTiketInput.add(t);
        return "SUCCESS";
    }

    public int getSisaFormPenumpang() {
        return jumlahPenumpang - listTiketInput.size();
    }

    public int getCounterPenumpangAktif() {
        return listTiketInput.size() + 1;
    }

    public void isiTabelReview(DefaultTableModel model) {
        model.setRowCount(0);
        for (Tiket t : listTiketInput) {
            model.addRow(new Object[]{
                t.getNoIdentitas(),
                t.getNamaPenumpangTiket(),
                jadwalTerpilih != null ? jadwalTerpilih.getNamaKereta() : "-",
                t.getNomorGerbong(),
                t.getNomorKursi()
            });
        }
    }

    public double hitungTotalBayar() {
        if (jadwalTerpilih == null) return 0;
        return jadwalTerpilih.getHargaDasar() * jumlahPenumpang;
    }

    public String prosesBayar() {
        if (SessionManager.getCurrentPenumpang() == null) return "Session penumpang tidak ditemukan!";
        String nikPemesan = SessionManager.getCurrentPenumpang().getNikNomorPassport();
        return pemesananDAO.prosesPembayaranLengkap(
            nikPemesan, 
            jadwalTerpilih.getIdJadwal(), 
            hitungTotalBayar(), 
            listTiketInput
        );
    }
}