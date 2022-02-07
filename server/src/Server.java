import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	
	static Socket sendingSocket;
	static ServerSocket ss;
	static InputStreamReader isr;
	static BufferedReader br;
	static String message;
	
	static Socket receivingSocket;
	static PrintWriter pw;
	
	public static void sendMessage(String msg, String ip, int port) throws IOException {
		try {
			sendingSocket = new Socket(ip,port);
			pw = new PrintWriter(sendingSocket.getOutputStream());
            pw.write(msg);
            pw.flush();
            pw.close();
            sendingSocket.close();
            System.out.println("SEND MSG: " + msg);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String receiveMessage(int port) {
		try {
			ss = new ServerSocket(port);
			receivingSocket = ss.accept();
			isr = new InputStreamReader(receivingSocket.getInputStream());
			br = new BufferedReader(isr);
			message = br.readLine();
			ss.close();
			receivingSocket.close();
			System.out.println("server received: " + message);
			return message;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "server-error: exception while receiving msg";
		}
	}
	
}
