import java.net.*;
import java.io.*;
import java.util.*;


public class ChatServer {
	// ArrayList<Socket> sockets;
	ArrayList clientOutputStreams;
	
	// main method
	public static void main(String[] args) {
		new ChatServer().go();
	}
	
	// 
	public void go() {
		clientOutputStreams = new ArrayList();
		try {
			// ÉèÖÃ¼àÌý¶Ë¿Ú
			ServerSocket serverSock = new ServerSocket(5000);
			while(true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread(new SocketHandle(clientSocket));
				t.start();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public class SocketHandle implements Runnable {
		Socket sock;
		BufferedReader reader;
		
    public SocketHandle(Socket clientSocket) {
    	try {
    		sock = clientSocket;
    		InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
    		reader = new BufferedReader(isReader);
    	}catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    public void run() {
    	String message;
    	try {
    		while((message = reader.readLine()) != null){
    			System.out.println("read " + message);
    			tellEveryone(message);
    		}
    		
    	}catch (Exception ex) {
    			ex.printStackTrace();
    	}
    }
	}
	
	public void tellEveryone(String message) {
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext()){
			try{
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
		
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}