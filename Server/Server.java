

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.ArrayList;



class indexServer{
	public ServerSocket serversocket;
	public int port;
	
	public indexServer()throws IOException{
		port = 8010;
		serversocket = new ServerSocket(port);
		System.out.println("Server Start ... ENJOY");
	}
	
	public indexServer(int port)throws IOException{
		this.port = port;
		serversocket = new ServerSocket(port);
		System.out.println("Server Start ... ENJOY");
	}
	
	public PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
		
	}
	
	public BufferedReader getReader(Socket socket)throws IOException{
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
		
	}
}



public class Server {
	private boolean isRunning = true;

	public void service() throws IOException{
		method serverfunction = new method();
		ServerSocket serverSocket;
		indexServer indexserver = new indexServer();
		serverSocket = indexserver.serversocket;
		System.out.println("Command: discover, ping, userlist, quit");
		Thread commandThread = new Thread(() -> {
			try {
				BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
				while (isRunning) {
					String command = localReader.readLine();
					// Handle the command as needed
					if (command.equals("userlist")) {
						// Gracefully shutdown the server
						System.out.println("User Data:");
						for (Map.Entry<String, String> entry : serverfunction.toIP.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
						}
					} 
					else if (command.equals("quit")) {
						// Gracefully shutdown the server
						isRunning = false;
						serverSocket.close();
						System.out.println("Server stopped.");
					} 
					else if (command.equals("discover")) {
						System.out.print("Enter the Peer name: ");
						// Gracefully shutdown the server
						String peerName = localReader.readLine();
						ArrayList<String> fileList = serverfunction.discoverFileList(peerName);
						// Print the fileList to the server console
						System.out.println("File List for Peer " + peerName + ":");
						for (String fileName : fileList) {
							System.out.println(fileName);
						}
					} 
					else if (command.equals("ping")) {
						// Gracefully shutdown the server
						System.out.print("Enter the name of the peer to ping: ");
						String name = localReader.readLine();
						String ipAddress = serverfunction.toIP.get(name);
						int defaultPingport = 8510;
						boolean isOnline = serverfunction.pingPeer(ipAddress,defaultPingport);
						if (isOnline) {
							System.out.println("Peer " + name +  " at IP " + ipAddress + " is online.");
						} else {
							System.out.println("Peer " + name + " at IP " + ipAddress + " is offline.");
						}
					} else {
						System.out.println("Unknown command: " + command);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		commandThread.start();

		try {
			Socket socket = null;
			while (isRunning) {
				socket = serverSocket.accept();
				//System.out.println("New connection accepted!");
				new SThread(socket, serverfunction);
			}
		} catch (Exception e) {
			if (!(e instanceof SocketException && e.getMessage().equalsIgnoreCase("Socket closed"))) {
				e.printStackTrace();
			}
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args)throws IOException{
		new Server().service();
	}
}