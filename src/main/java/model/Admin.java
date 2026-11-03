package model;

public class Admin extends Pengguna {
    private String kodeKaryawan;
    private String levelAkses;

    public Admin(String idPengguna, String email, String password, String kodeKaryawan, String levelAkses) {
        super(idPengguna, email, password, "A"); // Langsung tembak String "A"
        this.kodeKaryawan = kodeKaryawan;
        this.levelAkses = levelAkses;
    }

    @Override
    public String tampilkanInfoRole() {
        return "Saya adalah Admin dengan level: " + this.levelAkses;
    }

    public String getKodeKaryawan() { return kodeKaryawan; }
    public void setKodeKaryawan(String kodeKaryawan) { this.kodeKaryawan = kodeKaryawan; }
    public String getLevelAkses() { return levelAkses; }
    public void setLevelAkses(String levelAkses) { this.levelAkses = levelAkses; }
}