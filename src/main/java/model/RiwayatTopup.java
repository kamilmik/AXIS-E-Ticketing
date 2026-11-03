package model;

public class RiwayatTopup {
    private int idTopup;
    private double nominalTopup;
    private String waktuTopup;
    private String metodePembayaran;
    private String statusTopup;
    private String penumpangNik;

    public RiwayatTopup(int idTopup, double nominalTopup, String waktuTopup, String metodePembayaran, String statusTopup, String penumpangNik) {
        this.idTopup = idTopup;
        this.nominalTopup = nominalTopup;
        this.waktuTopup = waktuTopup;
        this.metodePembayaran = metodePembayaran;
        this.statusTopup = statusTopup;
        this.penumpangNik = penumpangNik;
    }

    public int getIdTopup() { return idTopup; }
    public void setIdTopup(int idTopup) { this.idTopup = idTopup; }
    public double getNominalTopup() { return nominalTopup; }
    public void setNominalTopup(double nominalTopup) { this.nominalTopup = nominalTopup; }
    public String getWaktuTopup() { return waktuTopup; }
    public void setWaktuTopup(String waktuTopup) { this.waktuTopup = waktuTopup; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public String getStatusTopup() { return statusTopup; }
    public void setStatusTopup(String statusTopup) { this.statusTopup = statusTopup; }
    public String getPenumpangNik() { return penumpangNik; }
    public void setPenumpangNik(String penumpangNik) { this.penumpangNik = penumpangNik; }
}