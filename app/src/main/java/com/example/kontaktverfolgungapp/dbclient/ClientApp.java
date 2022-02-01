package com.example.kontaktverfolgungapp.dbclient;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ClientApp {

    final static String serverIP = "192.168.178.21";
    final static int serverPORT = 49500;
    final static int clientPORT = 49501;

    /*
    IMPORTANT:
    Client needs port forwarding on serverPort 49500 when connecting from a local network
     */

    //call this function once before using other functions of the API
    //confirm that it returns true
    public static void initServerConnection() {
        Thread myThread = new Thread(new Client.ReceiveMsgThread(serverPORT));
        myThread.start();
    }

    public static double scanQR(int UID, int PID, String DateTime) {
        String msg = "scanQR;" + UID + ";" + PID + ";" + DateTime;
        try {
            Client.sendMessage(msg, serverIP, clientPORT);
        } catch (IOException e) {
            return 0.0;
        }
        msg = Client.receiveMessage();
        return Double.parseDouble(msg);
    }

    public static int newUser(String NewUser) {
        String msg = "newUser;" + NewUser;
        try {
            Client.sendMessage(msg, serverIP, clientPORT);
        } catch (IOException e) {
            return 0;
        }
        msg = Client.receiveMessage();

        return Integer.parseInt(msg);
    }

    public static void setName(int UID, String NewName) throws IOException {
        String msg = "setName;" + UID + ";" + NewName;
        Client.sendMessage(msg, serverIP, clientPORT);
    }

    public static ArrayList<Visit> loadVisits(int UID) {
        String msg = "loadVisits;" + UID;
        ArrayList<Visit> visits = new ArrayList<Visit>();
        try {
            Client.sendMessage(msg, serverIP, clientPORT);
        } catch (IOException e) {
            return visits;
        }
        msg = Client.receiveMessage();



        if (msg.isEmpty()) {
            return visits;
        }

        String[] visitObjects = msg.split("%");

        for(int i=0; i<visitObjects.length; i++) {
            String[] visitProps = visitObjects[i].split(";");

            int PID = Integer.parseInt(visitProps[0]);
            String PlaceName = visitProps[1];
            String DateTime = visitProps[2];
            double Timeframe = Double.parseDouble(visitProps[3]);

            Visit newVisit = new Visit(PID, PlaceName, DateTime, Timeframe);
            visits.add(newVisit);
        }

        return visits;
    }

    public static String[] loadMetPeople(int PID, String DateTime) {
        String msg = "loadMetPeople;" + PID + ";" + DateTime;

        try {
            Client.sendMessage(msg, serverIP, clientPORT);
        } catch (IOException e) {
            return new String[0];
        }
        msg = Client.receiveMessage();

        String[] metPeople = msg.split(";");

        return metPeople;
    }

    private static String getPublicIP()
    {
        try {

            Document doc = Jsoup.connect("http://www.checkip.org").get();
            return doc.getElementById("yourip").select("h1").first().select("span").text();

        } catch (IOException ex) {
            System.out.println("IOException: " + ex);
            return "error: no ip found";
        }
    }
}
