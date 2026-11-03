package model;

import java.util.UUID;

public class PerjalananKomuter {
    private int idPerjalanan;
    private String waktuTapIn;
    private String waktuTapOut;
    private double tarifPotongan;
    private String statusPerjalanan;
    private String penumpangNik;
    private int stasiunIdAsal;
    private int stasiunIdTujuan;
    private String adminKodeKaryawan;

    public PerjalananKomuter(int idPerjalanan, String waktuTapIn, String waktuTapOut, double tarifPotongan, String statusPerjalanan, String penumpangNik, int stasiunIdAsal, int stasiunIdTujuan, String adminKodeKaryawan) {
        this.idPerjalanan = idPerjalanan;
        this.waktuTapIn = waktuTapIn;
        this.waktuTapOut = waktuTapOut;
        this.tarifPotongan = tarifPotongan;
        this.statusPerjalanan = statusPerjalanan;
        this.penumpangNik = penumpangNik;
        this.stasiunIdAsal = stasiunIdAsal;
        this.stasiunIdTujuan = stasiunIdTujuan;
        this.adminKodeKaryawan = adminKodeKaryawan;
    }

    public boolean isMasihAktif() {
        return statusPerjalanan == null || !statusPerjalanan.equalsIgnoreCase("Selesai");
    }

    public String getKodeUnikGate() {
        String rawSeed = idPerjalanan + "-" + penumpangNik + "-" + waktuTapIn;
        String hash = UUID.nameUUIDFromBytes(rawSeed.getBytes()).toString().substring(0, 8).toUpperCase();
        return String.format("KMT-%s-%03d", hash, idPerjalanan);
    }

    public int getIdPerjalanan() { return idPerjalanan; }
    public void setIdPerjalanan(int idPerjalanan) { this.idPerjalanan = idPerjalanan; }
    public String getWaktuTapIn() { return waktuTapIn; }
    public void setWaktuTapIn(String waktuTapIn) { this.waktuTapIn = waktuTapIn; }
    public String getWaktuTapOut() { return waktuTapOut; }
    public void setWaktuTapOut(String waktuTapOut) { this.waktuTapOut = waktuTapOut; }
    public double getTarifPotongan() { return tarifPotongan; }
    public void setTarifPotongan(double tarifPotongan) { this.tarifPotongan = tarifPotongan; }
    public String getStatusPerjalanan() { return statusPerjalanan; }
    public void setStatusPerjalanan(String statusPerjalanan) { this.statusPerjalanan = statusPerjalanan; }
    public String getPenumpangNik() { return penumpangNik; }
    public void setPenumpangNik(String penumpangNik) { this.penumpangNik = penumpangNik; }
    public int getStasiunIdAsal() { return stasiunIdAsal; }
    public void setStasiunIdAsal(int stasiunIdAsal) { this.stasiunIdAsal = stasiunIdAsal; }
    public int getStasiunIdTujuan() { return stasiunIdTujuan; }
    public void setStasiunIdTujuan(int stasiunIdTujuan) { this.stasiunIdTujuan = stasiunIdTujuan; }
    public String getAdminKodeKaryawan() { return adminKodeKaryawan; }
    public void setAdminKodeKaryawan(String adminKodeKaryawan) { this.adminKodeKaryawan = adminKodeKaryawan; }
}