package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import util.DBConnection;

public class PenumpangDAO {

    public Penumpang getPenumpangTerbaru(String idPengguna) {
        String sql = "SELECT u.*, p.* FROM pengguna u JOIN penumpang p ON u.id_pengguna = p.id_pengguna WHERE u.id_pengguna = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, idPengguna);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Penumpang(
                    rs.getString("id_pengguna"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("nik_nomor_passport"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nomor_telepon"),
                    rs.getDouble("saldo_ewallet")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Stasiun> getStasiunByKategori(String kategori) {
        List<Stasiun> list = new ArrayList<>();
        String sql = "SELECT * FROM stasiun WHERE kategori_layanan = ? OR kategori_layanan = 'Keduanya' ORDER BY nama_stasiun ASC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, kategori);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Stasiun(
                    rs.getInt("id_stasiun"),
                    rs.getString("nama_stasiun"),
                    rs.getString("kode_kota"),
                    rs.getString("kategori_layanan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<JadwalAntarkota> cariJadwalAntarkota(int idAsal, int idTujuan, String tanggal) {
        List<JadwalAntarkota> list = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_antarkota WHERE stasiun_id_asal = ? AND stasiun_id_tujuan = ? AND DATE(waktu_berangkat) = ? ORDER BY waktu_berangkat ASC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idAsal);
            ps.setInt(2, idTujuan);
            ps.setString(3, tanggal);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new JadwalAntarkota(
                    rs.getInt("id_jadwal"),
                    rs.getString("waktu_berangkat"),
                    rs.getString("waktu_tiba"),
                    rs.getDouble("harga_dasar"),
                    rs.getString("nama_kereta"),
                    rs.getInt("total_gerbong"),
                    rs.getInt("kapasitas_kursi_per_gerbong"),
                    rs.getInt("stasiun_id_asal"),
                    rs.getInt("stasiun_id_tujuan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Pemesanan> getRiwayatAntarkota(String nik) {
        List<Pemesanan> list = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan WHERE penumpang_nik = ? ORDER BY waktu_pesan DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nik);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Pemesanan(
                    rs.getInt("id_pemesanan"),
                    rs.getString("kode_booking"),
                    rs.getString("waktu_pesan"),
                    rs.getDouble("total_bayar"),
                    rs.getString("penumpang_nik"),
                    rs.getString("status_pembayaran")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PerjalananKomuter> getRiwayatKomuter(String nik) {
        List<PerjalananKomuter> list = new ArrayList<>();
        String sql = "SELECT * FROM perjalanan_komuter WHERE penumpang_nik = ? ORDER BY waktu_tap_in DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nik);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PerjalananKomuter(
                    rs.getInt("id_perjalanan"),
                    rs.getString("waktu_tap_in"),
                    rs.getString("waktu_tap_out"),
                    rs.getDouble("tarif_potongan"),
                    rs.getString("status_perjalanan"),
                    rs.getString("penumpang_nik"),
                    rs.getInt("stasiun_id_asal"),
                    rs.getInt("stasiun_id_tujuan"),
                    rs.getString("admin_kode_karyawan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RiwayatTopup> getRiwayatTopup(String nik) {
        List<RiwayatTopup> list = new ArrayList<>();
        String sql = "SELECT * FROM riwayat_topup WHERE penumpang_nik = ? ORDER BY waktu_topup DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nik);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new RiwayatTopup(
                    rs.getInt("id_topup"),
                    rs.getDouble("nominal_topup"),
                    rs.getString("waktu_topup"),
                    rs.getString("metode_pembayaran"),
                    rs.getString("status_topup"),
                    rs.getString("penumpang_nik")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean prosesTopUp(String nik, double nominal, String metode) {
        Connection conn = DBConnection.getConnection();
        int nextId = 1;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id_topup), 0) + 1 FROM riwayat_topup")) {
            if (rs.next()) nextId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlUpdateSaldo = "UPDATE penumpang SET saldo_ewallet = saldo_ewallet + ? WHERE nik_nomor_passport = ?";
        String sqlInsertRiwayat = "INSERT INTO riwayat_topup (id_topup, nominal_topup, waktu_topup, metode_pembayaran, status_topup, penumpang_nik) VALUES (?, ?, NOW(), ?, 'Berhasil', ?)";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateSaldo);
                 PreparedStatement ps2 = conn.prepareStatement(sqlInsertRiwayat)) {
                ps1.setDouble(1, nominal);
                ps1.setString(2, nik);
                ps1.executeUpdate();

                ps2.setInt(1, nextId);
                ps2.setDouble(2, nominal);
                ps2.setString(3, metode);
                ps2.setString(4, nik);
                ps2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}