package model;

public class Stasiun {
    private int idStasiun;
    private String namaStasiun;
    private String kodeKota;
    private String kategoriLayanan;

    public Stasiun(int idStasiun, String namaStasiun, String kodeKota, String kategoriLayanan) {
        this.idStasiun = idStasiun;
        this.namaStasiun = namaStasiun;
        this.kodeKota = kodeKota;
        this.kategoriLayanan = kategoriLayanan;
    }

    public int getIdStasiun() { return idStasiun; }
    public void setIdStasiun(int idStasiun) { this.idStasiun = idStasiun; }
    public String getNamaStasiun() { return namaStasiun; }
    public void setNamaStasiun(String namaStasiun) { this.namaStasiun = namaStasiun; }
    public String getKodeKota() { return kodeKota; }
    public void setKodeKota(String kodeKota) { this.kodeKota = kodeKota; }
    public String getKategoriLayanan() { return kategoriLayanan; }
    public void setKategoriLayanan(String kategoriLayanan) { this.kategoriLayanan = kategoriLayanan; }
}