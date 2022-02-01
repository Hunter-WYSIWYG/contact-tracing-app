package com.example.kontaktverfolgungapp.dbclient;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    //Code for receiving messages
    static String lastMsg = "no new messages";
    static Boolean messageIsNew = false;

    static class ReceiveMsgThread implements Runnable {
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;

        int port;

        ReceiveMsgThread(int p) {
            port = p;
        }

        @Override
        public void run() {
            try {
                ss = new ServerSocket(port);
                while (true) {
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    lastMsg = br.readLine();
                    messageIsNew = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class listenForNewMessage implements Runnable {
        private static String result = "nothing received";

        @Override
        public void run() {
            while (true) {
                if (messageIsNew) {
                    messageIsNew = false;
                    result = lastMsg;
                    lastMsg = "no new messages";
                    break;
                }
            }
        }

        public static String getMsg() {
            return result;
        }
    }

    public static String receiveMessage() {
        Thread listenThread = new Thread(new listenForNewMessage());
        listenThread.start();
        try {
            listenThread.join();
        } catch (InterruptedException e) {
            return "InterruptedException: " + e;
        }
        return listenForNewMessage.getMsg();
    }


    //Code for sending messages
    public static class MessageSender extends AsyncTask<String,Void,Void> {

        Socket s;
        DataOutputStream dos;
        PrintWriter pw;
        String ip = "";
        int port = 0;

        MessageSender(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        protected Void doInBackground(String... voids) {

            String message = voids[0];

            try {
                s = new Socket(ip, port);
                pw = new PrintWriter(s.getOutputStream());
                pw.write(message);
                pw.flush();
                pw.close();
                s.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static void sendMessage(String msg, String ip, int port) throws IOException {
        MessageSender messageSender = new MessageSender(ip, port);
        messageSender.execute(msg);
    }
}
