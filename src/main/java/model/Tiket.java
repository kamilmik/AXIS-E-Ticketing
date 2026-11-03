package model;

public class Tiket {
    private int idTiket;
    private String idUnikBarcode;
    private String namaPenumpangTiket;
    private String noIdentitas;
    private String nomorKursi;
    private String nomorGerbong;
    private String pemesananKodeBooking;
    private int jadwalAntarkotaId;

    public Tiket(int idTiket, String idUnikBarcode, String namaPenumpangTiket, String noIdentitas, String nomorKursi, String nomorGerbong, String pemesananKodeBooking, int jadwalAntarkotaId) {
        this.idTiket = idTiket;
        this.idUnikBarcode = idUnikBarcode;
        this.namaPenumpangTiket = namaPenumpangTiket;
        this.noIdentitas = noIdentitas;
        this.nomorKursi = nomorKursi;
        this.nomorGerbong = nomorGerbong;
        this.pemesananKodeBooking = pemesananKodeBooking;
        this.jadwalAntarkotaId = jadwalAntarkotaId;
    }

    public int getIdTiket() { return idTiket; }
    public void setIdTiket(int idTiket) { this.idTiket = idTiket; }
    public String getIdUnikBarcode() { return idUnikBarcode; }
    public void setIdUnikBarcode(String idUnikBarcode) { this.idUnikBarcode = idUnikBarcode; }
    public String getNamaPenumpangTiket() { return namaPenumpangTiket; }
    public void setNamaPenumpangTiket(String namaPenumpangTiket) { this.namaPenumpangTiket = namaPenumpangTiket; }
    public String getNoIdentitas() { return noIdentitas; }
    public void setNoIdentitas(String noIdentitas) { this.noIdentitas = noIdentitas; }
    public String getNomorKursi() { return nomorKursi; }
    public void setNomorKursi(String nomorKursi) { this.nomorKursi = nomorKursi; }
    public String getNomorGerbong() { return nomorGerbong; }
    public void setNomorGerbong(String nomorGerbong) { this.nomorGerbong = nomorGerbong; }
    public String getPemesananKodeBooking() { return pemesananKodeBooking; }
    public void setPemesananKodeBooking(String pemesananKodeBooking) { this.pemesananKodeBooking = pemesananKodeBooking; }
    public int getJadwalAntarkotaId() { return jadwalAntarkotaId; }
    public void setJadwalAntarkotaId(int jadwalAntarkotaId) { this.jadwalAntarkotaId = jadwalAntarkotaId; }
}