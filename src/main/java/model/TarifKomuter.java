package model;

public class TarifKomuter {
    private String idTarif;
    private double hargaRute;
    private int stasiunIdAsal;
    private int stasiunIdTujuan;

    public TarifKomuter(String idTarif, double hargaRute, int stasiunIdAsal, int stasiunIdTujuan) {
        this.idTarif = idTarif;
        this.hargaRute = hargaRute;
        this.stasiunIdAsal = stasiunIdAsal;
        this.stasiunIdTujuan = stasiunIdTujuan;
    }

    public String getIdTarif() { return idTarif; }
    public void setIdTarif(String idTarif) { this.idTarif = idTarif; }
    public double getHargaRute() { return hargaRute; }
    public void setHargaRute(double hargaRute) { this.hargaRute = hargaRute; }
    public int getStasiunIdAsal() { return stasiunIdAsal; }
    public void setStasiunIdAsal(int stasiunIdAsal) { this.stasiunIdAsal = stasiunIdAsal; }
    public int getStasiunIdTujuan() { return stasiunIdTujuan; }
    public void setStasiunIdTujuan(int stasiunIdTujuan) { this.stasiunIdTujuan = stasiunIdTujuan; }
}