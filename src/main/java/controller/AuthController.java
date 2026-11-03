package controller;

import dao.AuthDAO;
import model.Admin;
import model.Penumpang;

public class AuthController {
    private final AuthDAO authDAO = new AuthDAO();
    private static int failedAttempts = 0;
    private static long lockEndTime = 0;

    public boolean isLocked() {
        if (lockEndTime > System.currentTimeMillis()) {
            return true;
        } else if (lockEndTime != 0) {
            lockEndTime = 0;
            failedAttempts = 0;
        }
        return false;
    }

    public long getRemainingLockSeconds() {
        return Math.max(0, (lockEndTime - System.currentTimeMillis()) / 1000);
    }

    private void recordFailedAttempt() {
        failedAttempts++;
        if (failedAttempts > 3) {
            lockEndTime = System.currentTimeMillis() + (5 * 60 * 1000); // Lock 5 menit
        }
    }

    public String validatePasswordRule(String email, String password) {
        if (password == null || password.length() < 8) {
            return "Password harus minimal 8 karakter!";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password harus memiliki minimal 1 angka!";
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            return "Password harus memiliki minimal 1 karakter unik (simbol)!";
        }
        if (email != null && !email.isEmpty()) {
            String emailPrefix = email.split("@")[0];
            if (password.equalsIgnoreCase(email) || password.equalsIgnoreCase(emailPrefix)) {
                return "Password tidak boleh sama dengan nama email!";
            }
        }
        return "VALID";
    }

    public String loginPenumpang(String email, String password) {
        if (isLocked()) {
            return "TERKUNCI: Tunggu " + getRemainingLockSeconds() + " detik lagi.";
        }
        if (email.isEmpty() || password.isEmpty()) {
            return "Email dan Password wajib diisi!";
        }
        Penumpang p = authDAO.loginPenumpang(email, password);
        if (p != null) {
            failedAttempts = 0;
            SessionManager.setCurrentPenumpang(p);
            return "SUCCESS";
        } else {
            recordFailedAttempt();
            if (isLocked()) {
                return "Gagal > 3 kali! Program dikunci (sleep) selama 5 menit.";
            }
            return "Email atau Password salah! (Percobaan gagal: " + failedAttempts + "/3)";
        }
    }

    public String loginAdmin(String kodeKaryawan, String password) {
        if (isLocked()) {
            return "TERKUNCI: Tunggu " + getRemainingLockSeconds() + " detik lagi.";
        }
        if (kodeKaryawan.isEmpty() || password.isEmpty()) {
            return "Kode Karyawan dan Password wajib diisi!";
        }
        Admin a = authDAO.loginAdmin(kodeKaryawan, password);
        if (a != null) {
            failedAttempts = 0;
            SessionManager.setCurrentAdmin(a);
            return "SUCCESS";
        } else {
            recordFailedAttempt();
            if (isLocked()) {
                return "Gagal > 3 kali! Program dikunci (sleep) selama 5 menit.";
            }
            return "Kode Karyawan atau Password salah! (Percobaan gagal: " + failedAttempts + "/3)";
        }
    }

    public String registerPenumpang(String nama, String email, String password, String telp, String nik) {
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || telp.isEmpty() || nik.isEmpty()) {
            return "Semua kolom wajib diisi!";
        }
        String passCheck = validatePasswordRule(email, password);
        if (!passCheck.equals("VALID")) {
            return passCheck;
        }
        if (authDAO.isAkunExist(email, nik)) {
            return "EXIST";
        }
        String newId = authDAO.generateIdPenumpang();
        Penumpang p = new Penumpang(newId, email, password, nik, nama, telp, 0.0);
        return authDAO.registerPenumpang(p) ? "SUCCESS" : "Gagal menyimpan ke database!";
    }

}