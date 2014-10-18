// TTOY's Log：
//     2014.9.18 实现输出文本ENTER输出的效果
//         注意：还需程序的结构优化 
//     2014.10.13 添加登陆窗口   
//     2014.10.14 禁用窗口最大化
//     2014.10.15 在登入界面添加ID和Pssword标签
//     2014.10.16 去除布局管理器实现自定义标签在窗口中的位置和标签的字体大小设置
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
	
	// 建立窗口并设定窗口属性
	private void go() {
		// log interface
		
		new LogInterface().setLogInterface();
		
		if(false){
		JFrame frame = new JFrame("TTOY 1.0");
		JPanel mainPanel = new JPanel();
		
		//构建对话框
		incoming = new JTextArea(15,50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		
		
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		// 消息发送文本框
		outgoing = new JTextField(40);
		
		//输出文本框添加KEYLISTENER
		outgoing.addKeyListener(new EnterSetUp());
		
		// 发送按钮构建并添加事件
		JButton button = new JButton("SEND");
		button.addActionListener(new SendButtonListener());
		
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(button);
		
		// 搭建Socket
		setUpNetworking();
		
		// 启动新的线程，以内部类为任务，此任务是读取服务器的socket串流显示在incoming文本区域
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(600,500);
		frame.setVisible(true);
		
	}
	}
	
	//搭建Socket
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
	
	//输出outgoing文本框的内容到服务器上
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
	
	
	//用户按下按钮时送出文本框里的内容到服务器上
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			sendMessage();
		}
		
		
	}
	
	// Thread的任务
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
	
	// 用户按下ENTER键送出文本框的内容 
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

// 用户信息（密码和ID）
class UserInfo {
	private String password;
	private String ID;
	// 设置密码
	public void setpassword (String p) {
		password = p;
	}
	// 设置ID
	public void setID (String id) {
		ID = id;
	}
	// 返回ID的值
	public String getID () {
		return ID;
	}
	// 返回密码的值
	public String getPassword () {
		return password;
	}
}

// 登陆界面
class LogInterface{
	
	//private JPanel logPanel;
	private JFrame logFrame;
	
	public void setLogInterface() {
   
   
   // logPanel.setLayout(null);
    
   // logPanel = new JPanel();
	  logFrame = new JFrame("TTOY 1.0");
	  logFrame.setLayout(null);
	  
	  // 窗口不可编辑 
	  //logFrame.setRootPane();
	  JLabel IDLabel = new JLabel("ID      :");
	  JLabel KeyLabel = new JLabel("PASSWORD:");
	 
	  // 设定标签的位置和字体大小
	  IDLabel.setBounds(100,100,50,50);
	  KeyLabel.setBounds(100,120,100,50);
	  IDLabel.setFont(new Font(Font.SANS_SERIF,15,15));
	  KeyLabel.setFont(new Font(Font.SANS_SERIF,15,15));
	  
	  logFrame.getContentPane().add(IDLabel);
	  logFrame.getContentPane().add(BorderLayout.SOUTH,KeyLabel);
	  
	  logFrame.setResizable(false); // 此方法为“禁用最大化”，非去除最大化按钮
	  logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  logFrame.setSize(400,300);
	  logFrame.setVisible(true);
  }
}

