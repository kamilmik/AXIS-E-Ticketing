package controller;

import dao.KomuterDAO;
import java.util.List;
import model.Penumpang;
import model.PerjalananKomuter;
import model.Stasiun;

public class KomuterController {
    private final KomuterDAO komuterDAO = new KomuterDAO();
    private List<Stasiun> listStasiunKomuter;

    public List<Stasiun> muatDaftarStasiun() {
        this.listStasiunKomuter = komuterDAO.getStasiunKomuter();
        return this.listStasiunKomuter;
    }

    public PerjalananKomuter cekPerjalananAktif() {
        Penumpang p = SessionManager.getCurrentPenumpang();
        if (p == null) return null;
        return komuterDAO.getPerjalananAktifByNik(p.getNikNomorPassport());
    }

    public String cariNamaStasiunById(int idStasiun) {
        if (listStasiunKomuter != null) {
            for (Stasiun s : listStasiunKomuter) {
                if (s.getIdStasiun() == idStasiun) {
                    return s.getIdStasiun() + " - " + s.getNamaStasiun() + " (" + s.getKodeKota() + ")";
                }
            }
        }
        return "Stasiun ID: " + idStasiun;
    }

    public String prosesTapIn(String stasiunTerpilihStr) {
        Penumpang p = SessionManager.getCurrentPenumpang();
        if (p == null) return "Session penumpang tidak ditemukan!";

        PerjalananKomuter aktif = cekPerjalananAktif();
        if (aktif != null && aktif.isMasihAktif()) {
            return "Anda masih memiliki perjalanan komuter yang belum diverifikasi Tap-Out oleh Admin!";
        }

        if (stasiunTerpilihStr == null || !stasiunTerpilihStr.contains(" - ")) {
            return "Pilih stasiun awal terlebih dahulu!";
        }

        int idStasiunAwal = Integer.parseInt(stasiunTerpilihStr.split(" - ")[0].trim());
        boolean sukses = komuterDAO.insertTapIn(p.getNikNomorPassport(), idStasiunAwal);
        return sukses ? "SUCCESS" : "Gagal melakukan Tap-In ke database.";
    }
}