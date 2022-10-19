package _02_Chat_Application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import _00_Click_Chat.gui.ButtonClicker;
import _00_Click_Chat.networking.Client;
import _00_Click_Chat.networking.Server;

/*
 * Using the Click_Chat example, write an application that allows a server computer to chat with a client computer.
 */

public class ChatApp extends JFrame {
	ChatServer server;
	ChatClient client;

	JButton sendMessage = new JButton("SEND MESSAGE");
	JTextArea messages = new JTextArea("Messages:");

	public static void main(String[] args) {
		new ChatApp();
	}

	public ChatApp() {
		messages.setEditable(false);
		int response = JOptionPane.showConfirmDialog(null, "Would you like to host a connection?", "Buttons!",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			String name = JOptionPane.showInputDialog("What would you like your name to be?");
			server = new ChatServer(8080);
			setTitle("SERVER");
			JOptionPane.showMessageDialog(null,
					"Server started at: " + server.getIPAddress() + "\nPort: " + server.getPort());
			System.out.println(server.getIPAddress());
			System.out.println(server.getPort());
			sendMessage.addActionListener((e) -> {
				String message = JOptionPane.showInputDialog("What is your message?");
				messages.setText(messages.getText() + "\n " + name + ": " + message);
				server.sendMessage(name + ": " + message);
			});
			JPanel panel = new JPanel();
			panel.add(sendMessage);
			panel.add(messages);
			add(panel);
			setVisible(true);
			setSize(400, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			server.start(this);

		} else {
			try {
				BufferedReader br = new BufferedReader(new FileReader("src/_02_Chat_Application/log.txt"));
				
				String line = br.readLine();
				line = br.readLine();
				while(line != null){
					messages.setText(messages.getText() + "\n" + line);
					line = br.readLine();
				}
				
				br.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String name = JOptionPane.showInputDialog("What would you like your name to be?");
			setTitle("CLIENT");
			String ipStr = JOptionPane.showInputDialog("Enter the IP Address");
			String prtStr = JOptionPane.showInputDialog("Enter the port number");
			int port = Integer.parseInt(prtStr);
			client = new ChatClient(ipStr, port);
			sendMessage.addActionListener((e) -> {
				String message = JOptionPane.showInputDialog("What is your message?");
				messages.setText(messages.getText() + "\n " + name + ": " + message);
				client.sendMessage(name + ": " + message);
			});
			JPanel panel = new JPanel();
			panel.add(sendMessage);
			panel.add(messages);
			add(panel);
			setVisible(true);
			setSize(400, 300);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			client.start(this);
		}
		
		
	}
	public void receiveMessages(String message) {
		messages.setText(messages.getText() + "\n " + message);
	}
}
