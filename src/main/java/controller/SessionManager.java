package controller;

import model.Admin;
import model.Penumpang;

public class SessionManager {
    private static Penumpang currentPenumpang;
    private static Admin currentAdmin;

    public static Penumpang getCurrentPenumpang() { return currentPenumpang; }
    public static void setCurrentPenumpang(Penumpang p) { currentPenumpang = p; }
    public static Admin getCurrentAdmin() { return currentAdmin; }
    public static void setCurrentAdmin(Admin a) { currentAdmin = a; }
    
    public static void clearSession() {
        currentPenumpang = null;
        currentAdmin = null;
    }
}