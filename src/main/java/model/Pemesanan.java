package model;

public class Pemesanan {
    private int idPemesanan;
    private String kodeBooking;
    private String waktuPesan;
    private double totalBayar;
    private String penumpangNik;
    private String statusPembayaran;

    public Pemesanan(int idPemesanan, String kodeBooking, String waktuPesan, double totalBayar, String penumpangNik, String statusPembayaran) {
        this.idPemesanan = idPemesanan;
        this.kodeBooking = kodeBooking;
        this.waktuPesan = waktuPesan;
        this.totalBayar = totalBayar;
        this.penumpangNik = penumpangNik;
        this.statusPembayaran = statusPembayaran;
    }

    public int getIdPemesanan() { return idPemesanan; }
    public void setIdPemesanan(int idPemesanan) { this.idPemesanan = idPemesanan; }
    public String getKodeBooking() { return kodeBooking; }
    public void setKodeBooking(String kodeBooking) { this.kodeBooking = kodeBooking; }
    public String getWaktuPesan() { return waktuPesan; }
    public void setWaktuPesan(String waktuPesan) { this.waktuPesan = waktuPesan; }
    public double getTotalBayar() { return totalBayar; }
    public void setTotalBayar(double totalBayar) { this.totalBayar = totalBayar; }
    public String getPenumpangNik() { return penumpangNik; }
    public void setPenumpangNik(String penumpangNik) { this.penumpangNik = penumpangNik; }
    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}