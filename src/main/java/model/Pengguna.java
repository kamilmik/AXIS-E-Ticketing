package model;

public abstract class Pengguna {
    protected String idPengguna;
    protected String email;
    protected String password;
    protected String tipePengguna; // Diubah jadi String biasa

    public Pengguna(String idPengguna, String email, String password, String tipePengguna) {
        this.idPengguna = idPengguna;
        this.email = email;
        this.password = password;
        this.tipePengguna = tipePengguna;
    }

    public abstract String tampilkanInfoRole();

    public String getIdPengguna() { return idPengguna; }
    public void setIdPengguna(String idPengguna) { this.idPengguna = idPengguna; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTipePengguna() { return tipePengguna; }
    public void setTipePengguna(String tipePengguna) { this.tipePengguna = tipePengguna; }
}