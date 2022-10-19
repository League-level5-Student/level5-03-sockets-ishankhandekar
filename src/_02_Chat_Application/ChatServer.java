package _02_Chat_Application;

import java.io.EOFException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ChatServer {
	private int port;

	private ServerSocket server;
	private Socket connection;

	ObjectOutputStream os;
	ObjectInputStream is;
	ChatApp ca;

	public ChatServer(int port) {
		this.port = port;
	}

	public void start(ChatApp ca){
		this.ca = ca;
		try {
			server = new ServerSocket(port, 100);

			connection = server.accept();

			os = new ObjectOutputStream(connection.getOutputStream());
			is = new ObjectInputStream(connection.getInputStream());

			os.flush();

			while (connection.isConnected()) {
				try {
					String rObj = is.readObject().toString();
					JOptionPane.showMessageDialog(null, rObj);
					ca.receiveMessages(rObj);
					
				}catch(EOFException e) {
					JOptionPane.showMessageDialog(null, "Connection Lost");
					System.exit(0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getIPAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "ERROR!!!!!";
		}
	}

	public int getPort() {
		return port;
	}

	public void sendMessage(String message) {
		try {
			if (os != null) {
				os.writeObject(message);
				
				os.flush();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			FileWriter fw = new FileWriter("src/_02_Chat_Application/log.txt");
			fw.write(ca.messages.getText());
		System.out.println(ca.messages.getText());
				
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
