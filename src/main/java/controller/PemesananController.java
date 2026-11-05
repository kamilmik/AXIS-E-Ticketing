package controller;

import dao.PemesananDAO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.JadwalAntarkota;
import model.Penumpang;
import model.Tiket;

public class PemesananController {
    private final PemesananDAO pemesananDAO = new PemesananDAO();
    
    private List<JadwalAntarkota> listJadwalTersedia;
    private int jumlahPenumpang;
    private JadwalAntarkota jadwalTerpilih;
    private final List<Tiket> listTiketInput = new ArrayList<>();
    private final List<String> listTeleponInput = new ArrayList<>();

    public void inisialisasiPencarian(List<JadwalAntarkota> hasilCari, int jmlPax) {
        this.listJadwalTersedia = hasilCari;
        this.jumlahPenumpang = jmlPax;
        this.listTiketInput.clear();
        this.listTeleponInput.clear();
        for (int i = 0; i < jmlPax; i++) {
            listTiketInput.add(null);
            listTeleponInput.add("");
        }
    }

    public int getJumlahPenumpang() {
        return jumlahPenumpang;
    }

    public Tiket getTiketPenumpang(int index) {
        if (index >= 0 && index < listTiketInput.size()) {
            return listTiketInput.get(index);
        }
        return null;
    }

    public String getTeleponPenumpang(int index) {
        if (index >= 0 && index < listTeleponInput.size()) {
            return listTeleponInput.get(index);
        }
        return "";
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
        if (listJadwalTersedia == null) return false;
        for (JadwalAntarkota j : listJadwalTersedia) {
            if (j.getIdJadwal() == idJadwal) {
                this.jadwalTerpilih = j;
                for (Tiket t : listTiketInput) {
                    if (t != null) t.setJadwalAntarkotaId(idJadwal);
                }
                return true;
            }
        }
        return false;
    }

    public JadwalAntarkota getJadwalTerpilih() {
        return jadwalTerpilih;
    }

    public String simpanDataPenumpang(int index, String nik, String nama, String telp) {
        if (nik.isEmpty() || nama.isEmpty() || telp.isEmpty()) {
            return "Semua kolom (Nama, No. Telepon, NIK/Paspor) wajib diisi!";
        }
        if (!nik.matches("\\d+") || !telp.matches("\\d+")) {
            return "NIK dan Nomor Telepon harus berupa angka!";
        }
        int nomorKursiOtomatis = index + 1;
        Tiket t = new Tiket(
            0, "", nama, nik,
            "Kursi " + nomorKursiOtomatis,
            "Gerbong 1",
            "",
            jadwalTerpilih != null ? jadwalTerpilih.getIdJadwal() : 0
        );
        listTiketInput.set(index, t);
        listTeleponInput.set(index, telp);
        return "SUCCESS";
    }

    public void setSamaDenganPemesan(boolean aktif) {
        if (jumlahPenumpang <= 0) return;
        if (aktif) {
            Penumpang p = SessionManager.getCurrentPenumpang();
            if (p != null) {
                simpanDataPenumpang(0, p.getNikNomorPassport(), p.getNamaLengkap(), p.getNomorTelepon());
            }
        } else {
            listTiketInput.set(0, null);
            listTeleponInput.set(0, "");
        }
    }

    public boolean isSemuaPenumpangLengkap() {
        for (Tiket t : listTiketInput) {
            if (t == null) return false;
        }
        return true;
    }

    public void isiTabelReview(DefaultTableModel model) {
        model.setRowCount(0);
        for (Tiket t : listTiketInput) {
            if (t != null) {
                model.addRow(new Object[]{
                    t.getNoIdentitas(),
                    t.getNamaPenumpangTiket(),
                    jadwalTerpilih != null ? jadwalTerpilih.getNamaKereta() : "-",
                    t.getNomorGerbong(),
                    t.getNomorKursi()
                });
            }
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