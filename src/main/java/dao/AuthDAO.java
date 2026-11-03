package dao;

import java.sql.*;
import model.Admin;
import model.Penumpang;
import util.DBConnection;

public class AuthDAO {

    public Penumpang loginPenumpang(String email, String password) {
        String sql = "SELECT u.*, p.* FROM pengguna u JOIN penumpang p ON u.id_pengguna = p.id_pengguna " +
                     "WHERE u.email = ? AND u.password = ? AND u.tipe_pengguna = 'P'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
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

    public Admin loginAdmin(String kodeKaryawan, String password) {
        String sql = "SELECT u.*, a.* FROM pengguna u JOIN admin a ON u.id_pengguna = a.id_pengguna " +
                     "WHERE a.kode_karyawan = ? AND u.password = ? AND u.tipe_pengguna = 'A'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, kodeKaryawan);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getString("id_pengguna"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("kode_karyawan"),
                    rs.getString("level_akses")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isAkunExist(String email, String nik) {
        String sql = "SELECT 1 FROM pengguna u LEFT JOIN penumpang p ON u.id_pengguna = p.id_pengguna " +
                     "WHERE u.email = ? OR p.nik_nomor_passport = ?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, nik);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String generateIdPenumpang() {
        String sql = "SELECT id_pengguna FROM pengguna WHERE id_pengguna LIKE 'CUST-%' ORDER BY id_pengguna DESC LIMIT 1";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                String lastId = rs.getString("id_pengguna");
                int num = Integer.parseInt(lastId.substring(5)) + 1;
                return String.format("CUST-%03d", num);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "CUST-001";
    }

    public boolean registerPenumpang(Penumpang p) {
        Connection conn = DBConnection.getConnection();
        String sqlUser = "INSERT INTO pengguna (id_pengguna, email, password, tipe_pengguna) VALUES (?, ?, ?, 'P')";
        String sqlPenumpang = "INSERT INTO penumpang (id_pengguna, nik_nomor_passport, nama_lengkap, nomor_telepon, saldo_ewallet) VALUES (?, ?, ?, ?, 0.00)";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlUser);
                 PreparedStatement ps2 = conn.prepareStatement(sqlPenumpang)) {
                
                ps1.setString(1, p.getIdPengguna());
                ps1.setString(2, p.getEmail());
                ps1.setString(3, p.getPassword());
                ps1.executeUpdate();

                ps2.setString(1, p.getIdPengguna());
                ps2.setString(2, p.getNikNomorPassport());
                ps2.setString(3, p.getNamaLengkap());
                ps2.setString(4, p.getNomorTelepon());
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