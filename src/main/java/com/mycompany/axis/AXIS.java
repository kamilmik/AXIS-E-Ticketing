package com.mycompany.axis;

import util.DBConnection;
import view.LoginView;

public class AXIS {
    public static void main(String[] args) {
        DBConnection.getConnection();

        new LoginView().setVisible(true);

    }
}