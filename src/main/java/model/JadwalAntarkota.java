package model;

public class JadwalAntarkota {
    private int idJadwal;
    private String waktuBerangkat;
    private String waktuTiba;
    private double hargaDasar;
    private String namaKereta;
    private int totalGerbong;
    private int kapasitasKursiPerGerbong;
    private int stasiunIdAsal;
    private int stasiunIdTujuan;

    public JadwalAntarkota(int idJadwal, String waktuBerangkat, String waktuTiba, double hargaDasar, String namaKereta, int totalGerbong, int kapasitasKursiPerGerbong, int stasiunIdAsal, int stasiunIdTujuan) {
        this.idJadwal = idJadwal;
        this.waktuBerangkat = waktuBerangkat;
        this.waktuTiba = waktuTiba;
        this.hargaDasar = hargaDasar;
        this.namaKereta = namaKereta;
        this.totalGerbong = totalGerbong;
        this.kapasitasKursiPerGerbong = kapasitasKursiPerGerbong;
        this.stasiunIdAsal = stasiunIdAsal;
        this.stasiunIdTujuan = stasiunIdTujuan;
    }

    public int getIdJadwal() { return idJadwal; }
    public void setIdJadwal(int idJadwal) { this.idJadwal = idJadwal; }
    public String getWaktuBerangkat() { return waktuBerangkat; }
    public void setWaktuBerangkat(String waktuBerangkat) { this.waktuBerangkat = waktuBerangkat; }
    public String getWaktuTiba() { return waktuTiba; }
    public void setWaktuTiba(String waktuTiba) { this.waktuTiba = waktuTiba; }
    public double getHargaDasar() { return hargaDasar; }
    public void setHargaDasar(double hargaDasar) { this.hargaDasar = hargaDasar; }
    public String getNamaKereta() { return namaKereta; }
    public void setNamaKereta(String namaKereta) { this.namaKereta = namaKereta; }
    public int getTotalGerbong() { return totalGerbong; }
    public void setTotalGerbong(int totalGerbong) { this.totalGerbong = totalGerbong; }
    public int getKapasitasKursiPerGerbong() { return kapasitasKursiPerGerbong; }
    public void setKapasitasKursiPerGerbong(int kapasitasKursiPerGerbong) { this.kapasitasKursiPerGerbong = kapasitasKursiPerGerbong; }
    public int getStasiunIdAsal() { return stasiunIdAsal; }
    public void setStasiunIdAsal(int stasiunIdAsal) { this.stasiunIdAsal = stasiunIdAsal; }
    public int getStasiunIdTujuan() { return stasiunIdTujuan; }
    public void setStasiunIdTujuan(int stasiunIdTujuan) { this.stasiunIdTujuan = stasiunIdTujuan; }
}