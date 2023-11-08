package com.example.kontaktverfolgungapp.dbclient;

import java.util.ArrayList;

public interface Communication {
    public static double scanQR(int UID, int PID, String DateTime) {
        return 0;
    }

    public static int newUser(String NewUser) {
        return 0;
    }

    public static void setName(int UID, String NewName) {
    }

    public static ArrayList<Visit> loadVisits(int UID) {
        return null;
    }

    public static String[] loadMetPeople(int PID, String DateTime) {
        return null;
    }
}