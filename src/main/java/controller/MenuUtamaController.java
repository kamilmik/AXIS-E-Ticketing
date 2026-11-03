package controller;

import dao.PenumpangDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.*;

public class MenuUtamaController {
    private final PenumpangDAO penumpangDAO = new PenumpangDAO();
    private final SimpleDateFormat sdfTanggal = new SimpleDateFormat("yyyy-MM-dd");

    public Penumpang sinkronkanSessionPenumpang() {
        Penumpang current = SessionManager.getCurrentPenumpang();
        if (current != null) {
            Penumpang updated = penumpangDAO.getPenumpangTerbaru(current.getIdPengguna());
            if (updated != null) {
                SessionManager.setCurrentPenumpang(updated);
                return updated;
            }
        }
        return current;
    }

    public List<Stasiun> dapatkanStasiunAntarkota() {
        return penumpangDAO.getStasiunByKategori("Antarkota");
    }

    private int ekstrakIdStasiun(String itemCombo) {
        if (itemCombo == null || !itemCombo.contains(" - ")) return -1;
        return Integer.parseInt(itemCombo.split(" - ")[0].trim());
    }

    public List<JadwalAntarkota> cariJadwal(String asalStr, String tujuanStr, Date tanggal) {
        int idAsal = ekstrakIdStasiun(asalStr);
        int idTujuan = ekstrakIdStasiun(tujuanStr);
        if (idAsal == -1 || idTujuan == -1 || idAsal == idTujuan) {
            return null;
        }
        String tglStr = sdfTanggal.format(tanggal);
        return penumpangDAO.cariJadwalAntarkota(idAsal, idTujuan, tglStr);
    }

    public String topUpEwallet(String nominalStr, String metode, String rekeningDummy) {
        Penumpang p = SessionManager.getCurrentPenumpang();
        if (p == null) return "Session penumpang tidak ditemukan!";
        if (nominalStr == null || nominalStr.isEmpty() || rekeningDummy == null || rekeningDummy.isEmpty()) {
            return "Nominal dan nomor rekening wajib diisi!";
        }
        try {
            double nominal = Double.parseDouble(nominalStr);
            if (nominal < 10000) return "Minimal Top-Up adalah Rp 10.000!";
            boolean sukses = penumpangDAO.prosesTopUp(p.getNikNomorPassport(), nominal, metode);
            if (sukses) {
                sinkronkanSessionPenumpang();
                return "SUCCESS";
            }
            return "Gagal memproses Top-Up ke database.";
        } catch (NumberFormatException e) {
            return "Nominal harus berupa angka valid!";
        }
    }

    public void isiTabelRiwayatAntarkota(DefaultTableModel model) {
        model.setRowCount(0);
        Penumpang p = SessionManager.getCurrentPenumpang();
        if (p == null) return;

        List<Pemesanan> list = penumpangDAO.getRiwayatAntarkota(p.getNikNomorPassport());
        for (Pemesanan pm : list) {
            model.addRow(new Object[]{
                pm.getIdPemesanan(),
                pm.getKodeBooking(),
                pm.getWaktuPesan(),
                String.format("Rp %,.0f", pm.getTotalBayar()),
                pm.getStatusPembayaran()
            });
        }
    }

    public void isiTabelRiwayatKomuter(DefaultTableModel model) {
        model.setRowCount(0);
        Penumpang p = SessionManager.getCurrentPenumpang();
        if (p == null) return;

        List<PerjalananKomuter> list = penumpangDAO.getRiwayatKomuter(p.getNikNomorPassport());
        for (PerjalananKomuter pk : list) {
            model.addRow(new Object[]{
                pk.getIdPerjalanan(),
                "Stasiun ID: " + pk.getStasiunIdAsal(),
                pk.getStasiunIdTujuan() == 0 ? "-" : "Stasiun ID: " + pk.getStasiunIdTujuan(),
                pk.getWaktuTapIn(),
                pk.getWaktuTapOut() == null ? "Belum Tap-Out" : pk.getWaktuTapOut(),
                String.format("Rp %,.0f", pk.getTarifPotongan()),
                pk.getStatusPerjalanan()
            });
        }
    }

    public void isiTabelRiwayatTopup(DefaultTableModel model) {
        model.setRowCount(0);
        Penumpang p = SessionManager.getCurrentPenumpang();
        if (p == null) return;

        List<RiwayatTopup> list = penumpangDAO.getRiwayatTopup(p.getNikNomorPassport());
        for (RiwayatTopup rt : list) {
            model.addRow(new Object[]{
                rt.getIdTopup(),
                rt.getWaktuTopup(),
                String.format("Rp %,.0f", rt.getNominalTopup()),
                rt.getMetodePembayaran(),
                rt.getStatusTopup()
            });
        }
    }
}