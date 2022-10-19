
package _02_Chat_Application;

	import java.io.FileWriter;
import java.io.IOException;
	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
	import java.net.Socket;
	import java.net.UnknownHostException;

	import javax.swing.JOptionPane;

	public class ChatClient {
		private String ip;
		private int port;
		ChatApp ca;
		Socket connection;

		ObjectOutputStream os;
		ObjectInputStream is;

		public ChatClient(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}

		public void start(ChatApp ca){
			this.ca = ca;
			try {

				connection = new Socket(ip, port);

				os = new ObjectOutputStream(connection.getOutputStream());
				is = new ObjectInputStream(connection.getInputStream());

				os.flush();

				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while (connection.isConnected()) {
				try {
					String rObj = is.readObject().toString();
					JOptionPane.showMessageDialog(null, rObj);
					System.out.println(rObj);
					ca.receiveMessages(rObj);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void sendMessage(String message) {
			try {
				if (os != null) {
					os.writeObject(message);
					os.flush();
				}
				try {
					FileWriter fw = new FileWriter("src/_02_Chat_Application/log.txt");
					fw.write(ca.messages.getText());
				System.out.println(ca.messages.getText());
						
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

