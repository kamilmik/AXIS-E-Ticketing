package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import util.DBConnection;

public class AdminDAO {

    public int[] getDashboardStats() {
        int[] stats = new int[4];
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM tiket");
            if (rs1.next()) stats[0] = rs1.getInt(1);

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM perjalanan_komuter");
            if (rs2.next()) stats[1] = rs2.getInt(1);

            stats[2] = stats[0] + stats[1];

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM jadwal_antarkota");
            if (rs3.next()) stats[3] = rs3.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<String> getComboStasiun(String filterKategori) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT id_stasiun, nama_stasiun FROM stasiun";
        if (filterKategori != null) {
            sql += " WHERE kategori_layanan = '" + filterKategori + "' OR kategori_layanan = 'Keduanya'";
        }
        sql += " ORDER BY id_stasiun ASC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getInt("id_stasiun") + " - " + rs.getString("nama_stasiun"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void loadJadwalAntarkota(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT j.id_jadwal, j.nama_kereta, " +
                     "CONCAT(s1.id_stasiun, ' - ', s1.nama_stasiun) AS asal, " +
                     "CONCAT(s2.id_stasiun, ' - ', s2.nama_stasiun) AS tujuan, " +
                     "j.waktu_berangkat, j.waktu_tiba, j.harga_dasar, " +
                     "j.total_gerbong, j.kapasitas_kursi_per_gerbong " +
                     "FROM jadwal_antarkota j " +
                     "JOIN stasiun s1 ON j.stasiun_id_asal = s1.id_stasiun " +
                     "JOIN stasiun s2 ON j.stasiun_id_tujuan = s2.id_stasiun " +
                     "ORDER BY j.id_jadwal ASC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_jadwal"),
                    rs.getString("nama_kereta"),
                    rs.getString("asal"),
                    rs.getString("tujuan"),
                    rs.getString("waktu_berangkat"),
                    rs.getString("waktu_tiba"),
                    rs.getDouble("harga_dasar"),
                    rs.getInt("total_gerbong"),
                    rs.getInt("kapasitas_kursi_per_gerbong")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertJadwal(String nama, int idAsal, int idTujuan, String berangkat, String tiba, double harga, int gerbong, int kursi) {
        Connection conn = DBConnection.getConnection();
        int nextId = 101;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id_jadwal), 100) + 1 FROM jadwal_antarkota")) {
            if (rs.next()) nextId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO jadwal_antarkota (id_jadwal, nama_kereta, stasiun_id_asal, stasiun_id_tujuan, waktu_berangkat, waktu_tiba, harga_dasar, total_gerbong, kapasitas_kursi_per_gerbong) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setString(2, nama);
            ps.setInt(3, idAsal);
            ps.setInt(4, idTujuan);
            ps.setString(5, berangkat);
            ps.setString(6, tiba);
            ps.setDouble(7, harga);
            ps.setInt(8, gerbong);
            ps.setInt(9, kursi);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateJadwal(int idJadwal, String nama, int idAsal, int idTujuan, String berangkat, String tiba, double harga, int gerbong, int kursi) {
        String sql = "UPDATE jadwal_antarkota SET nama_kereta=?, stasiun_id_asal=?, stasiun_id_tujuan=?, waktu_berangkat=?, waktu_tiba=?, harga_dasar=?, total_gerbong=?, kapasitas_kursi_per_gerbong=? WHERE id_jadwal=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setInt(2, idAsal);
            ps.setInt(3, idTujuan);
            ps.setString(4, berangkat);
            ps.setString(5, tiba);
            ps.setDouble(6, harga);
            ps.setInt(7, gerbong);
            ps.setInt(8, kursi);
            ps.setInt(9, idJadwal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadStasiun(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT * FROM stasiun ORDER BY id_stasiun ASC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_stasiun"),
                    rs.getString("nama_stasiun"),
                    rs.getString("kode_kota"),
                    rs.getString("kategori_layanan")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertStasiun(String nama, String kodeKota, String kategori) {
        Connection conn = DBConnection.getConnection();
        int nextId = 1;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id_stasiun), 0) + 1 FROM stasiun")) {
            if (rs.next()) nextId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO stasiun (id_stasiun, nama_stasiun, kode_kota, kategori_layanan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setString(2, nama);
            ps.setString(3, kodeKota);
            ps.setString(4, kategori);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStasiun(int idStasiun, String nama, String kodeKota, String kategori) {
        String sql = "UPDATE stasiun SET nama_stasiun=?, kode_kota=?, kategori_layanan=? WHERE id_stasiun=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, kodeKota);
            ps.setString(3, kategori);
            ps.setInt(4, idStasiun);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadTarifKomuter(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT t.id_tarif, CONCAT(s1.id_stasiun, ' - ', s1.nama_stasiun) AS asal, " +
                     "CONCAT(s2.id_stasiun, ' - ', s2.nama_stasiun) AS tujuan, t.harga_rute " +
                     "FROM tarif_komuter t " +
                     "JOIN stasiun s1 ON t.stasiun_id_asal = s1.id_stasiun " +
                     "JOIN stasiun s2 ON t.stasiun_id_tujuan = s2.id_stasiun " +
                     "ORDER BY t.id_tarif ASC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_tarif"),
                    rs.getString("asal"),
                    rs.getString("tujuan"),
                    rs.getDouble("harga_rute")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertTarif(int idAsal, int idTujuan, double harga) {
        Connection conn = DBConnection.getConnection();
        String nextId = "TRF-001";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_tarif FROM tarif_komuter ORDER BY id_tarif DESC LIMIT 1")) {
            if (rs.next()) {
                String last = rs.getString(1);
                int num = Integer.parseInt(last.substring(4)) + 1;
                nextId = String.format("TRF-%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO tarif_komuter (id_tarif, stasiun_id_asal, stasiun_id_tujuan, harga_rute) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nextId);
            ps.setInt(2, idAsal);
            ps.setInt(3, idTujuan);
            ps.setDouble(4, harga);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTarif(String idTarif, int idAsal, int idTujuan, double harga) {
        String sql = "UPDATE tarif_komuter SET stasiun_id_asal=?, stasiun_id_tujuan=?, harga_rute=? WHERE id_tarif=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idAsal);
            ps.setInt(2, idTujuan);
            ps.setDouble(3, harga);
            ps.setString(4, idTarif);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void loadTapInKomuter(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT pk.id_perjalanan, pk.penumpang_nik, p.nama_lengkap, p.saldo_ewallet, " +
                     "CONCAT(s1.id_stasiun, ' - ', s1.nama_stasiun) AS asal, pk.waktu_tap_in, pk.status_perjalanan " +
                     "FROM perjalanan_komuter pk " +
                     "JOIN penumpang p ON pk.penumpang_nik = p.nik_nomor_passport " +
                     "JOIN stasiun s1 ON pk.stasiun_id_asal = s1.id_stasiun " +
                     "WHERE pk.status_perjalanan != 'Selesai' " +
                     "ORDER BY pk.id_perjalanan DESC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_perjalanan"),
                    rs.getString("penumpang_nik"),
                    rs.getString("nama_lengkap"),
                    rs.getDouble("saldo_ewallet"),
                    rs.getString("asal"),
                    rs.getString("waktu_tap_in"),
                    rs.getString("status_perjalanan")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getHargaTarifKomuter(int idAsal, int idTujuan) {
        if (idAsal == idTujuan) return 3000.0;
        String sql = "SELECT harga_rute FROM tarif_komuter WHERE stasiun_id_asal = ? AND stasiun_id_tujuan = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idAsal);
            ps.setInt(2, idTujuan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("harga_rute");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 5000.0;
    }

    public String verifikasiTapOut(int idPerjalanan, String nik, int idStasiunKeluar, double tarif, String kodeAdmin) {
        Connection conn = DBConnection.getConnection();
        try {
            double saldo = 0;
            try (PreparedStatement psCek = conn.prepareStatement("SELECT saldo_ewallet FROM penumpang WHERE nik_nomor_passport = ?")) {
                psCek.setString(1, nik);
                ResultSet rs = psCek.executeQuery();
                if (rs.next()) saldo = rs.getDouble(1);
            }

            if (saldo < tarif) {
                return "Saldo Penumpang Tidak Mencukupi! (Saldo: Rp" + saldo + ", Tarif: Rp" + tarif + ")";
            }

            conn.setAutoCommit(false);
            try (PreparedStatement psPotong = conn.prepareStatement("UPDATE penumpang SET saldo_ewallet = saldo_ewallet - ? WHERE nik_nomor_passport = ?");
                 PreparedStatement psUpdate = conn.prepareStatement("UPDATE perjalanan_komuter SET waktu_tap_out = NOW(), stasiun_id_tujuan = ?, tarif_potongan = ?, status_perjalanan = 'Selesai', admin_kode_karyawan = ? WHERE id_perjalanan = ?")) {
                
                psPotong.setDouble(1, tarif);
                psPotong.setString(2, nik);
                psPotong.executeUpdate();

                psUpdate.setInt(1, idStasiunKeluar);
                psUpdate.setDouble(2, tarif);
                psUpdate.setString(3, kodeAdmin);
                psUpdate.setInt(4, idPerjalanan);
                psUpdate.executeUpdate();

                conn.commit();
                return "SUCCESS";
            } catch (SQLException ex) {
                conn.rollback();
                ex.printStackTrace();
                return "Gagal update database: " + ex.getMessage();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public void loadMonitoringPemesanan(DefaultTableModel model, String keyword) {
        model.setRowCount(0);
        String sql = "SELECT pm.id_pemesanan, pm.kode_booking, pm.penumpang_nik, p.nama_lengkap, pm.waktu_pesan, pm.total_bayar, pm.status_pembayaran " +
                     "FROM pemesanan pm JOIN penumpang p ON pm.penumpang_nik = p.nik_nomor_passport ";
        if (keyword != null && !keyword.isEmpty()) {
            sql += "WHERE pm.kode_booking LIKE ? OR pm.penumpang_nik LIKE ? ";
        }
        sql += "ORDER BY pm.id_pemesanan DESC";

        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            if (keyword != null && !keyword.isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
                ps.setString(2, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_pemesanan"),
                    rs.getString("kode_booking"),
                    rs.getString("penumpang_nik"),
                    rs.getString("nama_lengkap"),
                    rs.getString("waktu_pesan"),
                    rs.getDouble("total_bayar"),
                    rs.getString("status_pembayaran")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadDetailTiket(DefaultTableModel model, String kodeBooking) {
        model.setRowCount(0);
        String sql = "SELECT id_tiket, id_unik_barcode, nama_penumpang_tiket, no_identitas, jadwal_antarkota_id, nomor_gerbong, nomor_kursi " +
                     "FROM tiket WHERE pemesanan_kode_booking = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, kodeBooking);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_tiket"),
                    rs.getString("id_unik_barcode"),
                    rs.getString("nama_penumpang_tiket"),
                    rs.getString("no_identitas"),
                    rs.getInt("jadwal_antarkota_id"),
                    rs.getString("nomor_gerbong"),
                    rs.getString("nomor_kursi")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> getComboStasiunTujuanTapOut(int idStasiunAsal) {
        List<String> list = new java.util.ArrayList<>();
        String sql = "SELECT s.id_stasiun, s.nama_stasiun, s.kode_kota "
                   + "FROM tarif_komuter tk "
                   + "JOIN stasiun s ON tk.stasiun_id_tujuan = s.id_stasiun "
                   + "WHERE tk.stasiun_id_asal = ? AND tk.stasiun_id_tujuan != ? "
                   + "ORDER BY s.nama_stasiun ASC";
        try (java.sql.PreparedStatement ps = util.DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idStasiunAsal);
            ps.setInt(2, idStasiunAsal);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getInt("id_stasiun") + " - " + rs.getString("nama_stasiun") + " (" + rs.getString("kode_kota") + ")");
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}