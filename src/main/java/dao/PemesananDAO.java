package dao;

import java.sql.*;
import java.util.List;
import model.Tiket;
import util.DBConnection;

public class PemesananDAO {
    
    public String prosesPembayaranLengkap(String nikPemesan, int idJadwal, double totalBayar, List<Tiket> listTiket) {
        Connection conn = DBConnection.getConnection();
        String kodeBooking = "BKG-" + System.currentTimeMillis(); // Generate Kode Booking Unik
        
        try {
            conn.setAutoCommit(false); // Mulai Transaksi
            
            // 1. Cek & Potong Saldo Penumpang
            double saldoSaatIni = 0;
            try (PreparedStatement psCek = conn.prepareStatement("SELECT saldo_ewallet FROM penumpang WHERE nik_nomor_passport = ? FOR UPDATE")) {
                psCek.setString(1, nikPemesan);
                ResultSet rs = psCek.executeQuery();
                if (rs.next()) saldoSaatIni = rs.getDouble(1);
            }
            if (saldoSaatIni < totalBayar) {
                conn.rollback();
                return "SALDO_KURANG";
            }
            
            try (PreparedStatement psPotong = conn.prepareStatement("UPDATE penumpang SET saldo_ewallet = saldo_ewallet - ? WHERE nik_nomor_passport = ?")) {
                psPotong.setDouble(1, totalBayar);
                psPotong.setString(2, nikPemesan);
                psPotong.executeUpdate();
            }

            // 2. Insert Pemesanan
            int idPemesanan = 1;
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id_pemesanan), 0) + 1 FROM pemesanan")) {
                if (rs.next()) idPemesanan = rs.getInt(1);
            }
            
            String sqlPesan = "INSERT INTO pemesanan (id_pemesanan, kode_booking, waktu_pesan, total_bayar, penumpang_nik, status_pembayaran) VALUES (?, ?, NOW(), ?, ?, 'Lunas')";
            try (PreparedStatement psPesan = conn.prepareStatement(sqlPesan)) {
                psPesan.setInt(1, idPemesanan);
                psPesan.setString(2, kodeBooking);
                psPesan.setDouble(3, totalBayar);
                psPesan.setString(4, nikPemesan);
                psPesan.executeUpdate();
            }

            // 3. Insert Tiket & Auto-Seat
            String sqlTiket = "INSERT INTO tiket (id_tiket, id_unik_barcode, nama_penumpang_tiket, no_identitas, jadwal_antarkota_id, pemesanan_kode_booking, nomor_gerbong, nomor_kursi) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psTiket = conn.prepareStatement(sqlTiket)) {
                int idTiket = 1;
                try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id_tiket), 0) FROM tiket")) {
                    if (rs.next()) idTiket = rs.getInt(1);
                }
                
                int kursiCounter = 1;
                for (Tiket t : listTiket) {
                    idTiket++;
                    psTiket.setInt(1, idTiket);
                    psTiket.setString(2, "QR-" + kodeBooking + "-" + idTiket);
                    psTiket.setString(3, t.getNamaPenumpangTiket());
                    psTiket.setString(4, t.getNoIdentitas());
                    psTiket.setInt(5, idJadwal);
                    psTiket.setString(6, kodeBooking);
                    psTiket.setString(7, "Gerbong 1"); // Otomatis Pilih Gerbong
                    psTiket.setString(8, "Kursi " + kursiCounter); // Otomatis Pilih Kursi berurutan
                    psTiket.executeUpdate();
                    
                    t.setNomorGerbong("Gerbong 1");
                    t.setNomorKursi("Kursi " + kursiCounter);
                    kursiCounter++;
                }
            }

            conn.commit(); // Simpan Permanen
            return "SUCCESS";
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { }
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { }
        }
    }
}