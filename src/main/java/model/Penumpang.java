package model;

public class Penumpang extends Pengguna {
    private String nikNomorPassport;
    private String namaLengkap;
    private String nomorTelepon;
    private double saldoEwallet;

    public Penumpang(String idPengguna, String email, String password, String nik, String nama, String telp, double saldo) {
        super(idPengguna, email, password, "P"); // Langsung tembak String "P"
        this.nikNomorPassport = nik;
        this.namaLengkap = nama;
        this.nomorTelepon = telp;
        this.saldoEwallet = saldo;
    }

    @Override
    public String tampilkanInfoRole() {
        return "Saya adalah Penumpang AXIS bernama: " + this.namaLengkap;
    }

    public String getNikNomorPassport() { return nikNomorPassport; }
    public void setNikNomorPassport(String nikNomorPassport) { this.nikNomorPassport = nikNomorPassport; }
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }
    public double getSaldoEwallet() { return saldoEwallet; }
    public void setSaldoEwallet(double saldoEwallet) { this.saldoEwallet = saldoEwallet; }
}