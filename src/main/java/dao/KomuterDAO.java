package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.PerjalananKomuter;
import model.Stasiun;
import util.DBConnection;

public class KomuterDAO {

    public List<Stasiun> getStasiunKomuter() {
        List<Stasiun> list = new ArrayList<>();
        String sql = "SELECT * FROM stasiun WHERE kategori_layanan = 'Komuter' OR kategori_layanan = 'Keduanya' ORDER BY id_stasiun ASC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
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

    public PerjalananKomuter getPerjalananAktifByNik(String nik) {
        String sql = "SELECT * FROM perjalanan_komuter WHERE penumpang_nik = ? AND status_perjalanan != 'Selesai' ORDER BY id_perjalanan DESC LIMIT 1";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, nik);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PerjalananKomuter(
                    rs.getInt("id_perjalanan"),
                    rs.getString("waktu_tap_in"),
                    rs.getString("waktu_tap_out"),
                    rs.getDouble("tarif_potongan"),
                    rs.getString("status_perjalanan"),
                    rs.getString("penumpang_nik"),
                    rs.getInt("stasiun_id_asal"),
                    rs.getInt("stasiun_id_tujuan"),
                    rs.getString("admin_kode_karyawan")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String deteksiStatusAwalValid(Connection conn) {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SHOW COLUMNS FROM perjalanan_komuter LIKE 'status_perjalanan'")) {
            if (rs.next()) {
                String type = rs.getString("Type");
                if (type != null && type.startsWith("enum(")) {
                    String[] vals = type.substring(5, type.length() - 1).replace("'", "").split(",");
                    for (String v : vals) {
                        if (!v.equalsIgnoreCase("Selesai")) return v;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Aktif";
    }

    public boolean insertTapIn(String nik, int idStasiunAwal) {
        Connection conn = DBConnection.getConnection();
        int nextId = 1;
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id_perjalanan), 0) + 1 FROM perjalanan_komuter")) {
            if (rs.next()) nextId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String statusAwal = deteksiStatusAwalValid(conn);
        String sql = "INSERT INTO perjalanan_komuter (id_perjalanan, waktu_tap_in, waktu_tap_out, tarif_potongan, status_perjalanan, penumpang_nik, stasiun_id_asal, stasiun_id_tujuan, admin_kode_karyawan) VALUES (?, NOW(), NULL, 0, ?, ?, ?, NULL, NULL)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nextId);
            ps.setString(2, statusAwal);
            ps.setString(3, nik);
            ps.setInt(4, idStasiunAwal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}