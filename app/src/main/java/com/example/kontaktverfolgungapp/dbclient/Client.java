package com.example.kontaktverfolgungapp.dbclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    public static void sendMessage(String msg, String ip, int port) throws IOException {
        try {
            Socket s=new Socket(ip,port);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(msg);
            dout.flush();
            dout.close();
            s.close();
        } catch(Exception e) {
            System.out.println(e);
        }

    }

    public static String receiveMessage(int port) {
        try{
            ServerSocket ss=new ServerSocket(port);
            Socket s=ss.accept();//establishes connection

            DataInputStream dis=new DataInputStream(s.getInputStream());
            String str=(String)dis.readUTF();
            System.out.println("client received: "+str);
            ss.close();
            return str;
        } catch(Exception e){
            System.out.println(e);
            return "client-error: exception while receiving msg";
        }
    }
}
