// TTOY's Log��
//     2014.9.18 ʵ������ı�ENTER�����Ч��
//         ע�⣺�������Ľṹ�Ż� 
//     2014.10.13 ��ӵ�½����   
//     2014.10.14 ���ô������
//     2014.10.15 �ڵ���������ID��Pssword��ǩ
//     2014.10.16 ȥ�����ֹ�����ʵ���Զ����ǩ�ڴ����е�λ�úͱ�ǩ�������С����
//     2014.10.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

	
public class ChatClient {
	
	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	

	
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.go();
	}
	
	// �������ڲ��趨��������
	private void go() {
		// log interface
		
		new LogInterface().setLogInterface();
		
		if(false){
		JFrame frame = new JFrame("TTOY 1.0");
		JPanel mainPanel = new JPanel();
		
		//�����Ի���
		incoming = new JTextArea(15,50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		
		
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// ��Ϣ�����ı���
		outgoing = new JTextField(40);
		
		//����ı������KEYLISTENER
		outgoing.addKeyListener(new EnterSetUp());
		
		// ���Ͱ�ť����������¼�
		JButton button = new JButton("SEND");
		button.addActionListener(new SendButtonListener());
		
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(button);
		
		// �Socket
		setUpNetworking();
		
		// �����µ��̣߳����ڲ���Ϊ���񣬴������Ƕ�ȡ��������socket������ʾ��incoming�ı�����
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(600,500);
		frame.setVisible(true);
		
	}
	}
	
	//�Socket
	private void setUpNetworking() {
		try{ 
			sock = new Socket("127.0.0.1", 5000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//���outgoing�ı�������ݵ���������
	private void sendMessage() {
		try{ 
				writer.println(outgoing.getText());
				writer.flush();
			}catch (Exception ex){
				ex.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();
	}
	
	
	//�û����°�ťʱ�ͳ��ı���������ݵ���������
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			sendMessage();
		}
		
		
	}
	
	// Thread������
	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					incoming.append(message + "\n");
				}
			}catch (Exception ex) {
					ex.printStackTrace();
			}
		}
		
	}
	
	// �û�����ENTER���ͳ��ı�������� 
	public class EnterSetUp implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				
				sendMessage();
			}
		}
		
		public void keyReleased(KeyEvent e) {}
		
		public void keyTyped(KeyEvent e) {}
		
	}

}

// �û���Ϣ�������ID��
class UserInfo {
	private String password;
	private String ID;
	// ��������
	public void setpassword (String p) {
		password = p;
	}
	// ����ID
	public void setID (String id) {
		ID = id;
	}
	// ����ID��ֵ
	public String getID () {
		return ID;
	}
	// ���������ֵ
	public String getPassword () {
		return password;
	}
}

// ��½����
class LogInterface{
	
	//private JPanel logPanel;
	private JFrame logFrame;
	
	public void setLogInterface() {
   
   
   // logPanel.setLayout(null);
    
   // logPanel = new JPanel();
	  logFrame = new JFrame("TTOY 1.0");
	  logFrame.setLayout(null);
	  
	  // ���ڲ��ɱ༭ 
	  //logFrame.setRootPane();
	  JLabel IDLabel = new JLabel("ID      :");
	  JLabel KeyLabel = new JLabel("PASSWORD:");
	 
	  // �趨��ǩ��λ�ú������С
	  IDLabel.setBounds(100,100,50,50);
	  KeyLabel.setBounds(100,120,100,50);
	  IDLabel.setFont(new Font(Font.SANS_SERIF,15,15));
	  KeyLabel.setFont(new Font(Font.SANS_SERIF,15,15));
	  
	  logFrame.getContentPane().add(IDLabel);
	  logFrame.getContentPane().add(BorderLayout.SOUTH,KeyLabel);
	  
	  logFrame.setResizable(false); // �˷���Ϊ��������󻯡�����ȥ����󻯰�ť
	  logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  logFrame.setSize(400,300);
	  logFrame.setVisible(true);
  }
}

