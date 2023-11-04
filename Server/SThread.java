

import java.io.*;
import java.net.*;
import java.util.*;

public class SThread extends Thread{
	private BufferedReader br;
	private PrintWriter pw;
	private Socket connectToClient;
	private method serverfunction;
	private ServerGUI serverGUI;
	
	public SThread(Socket soc,method serverfunction, ServerGUI serverGUI)throws IOException{
		super();
		connectToClient = soc;
		this.serverfunction = serverfunction;
		this.serverGUI = serverGUI;
		br = getReader(connectToClient);
		pw = getWriter(connectToClient);
		start();
	}
	public void addNotification(String msg){
		serverGUI.addNotification(msg);
	}
	
	public PrintWriter getWriter(Socket socket)throws IOException{
		OutputStream socketOut = socket.getOutputStream();
		return new PrintWriter(socketOut, true);
		
	}
	
	public BufferedReader getReader(Socket socket)throws IOException{
		InputStream socketIn = socket.getInputStream();
		return new BufferedReader(new InputStreamReader(socketIn));
		
	}
	
	/*
	 * Deal with messages sent from client
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		try{
			
			String msg = null;
			while((msg = br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(msg);
				//serverGUI.addNotification(msg);
				String command = st.nextToken();
				String name = st.nextToken();
				String ID = st.nextToken();
				String lName = st.nextToken();
				String fName= null;
				
				if( st.hasMoreTokens()) fName = st.nextToken();
				else fName = lName ;

				
				if("signup".equals(command)){
					if (serverfunction.toIP.containsKey(name)) {
						//writer.println("Sign-in Successful");
						serverGUI.addNotification("Sign-in Successful");
						pw.println("Sign-in Successful");
					} else {
						serverfunction.toIP.put(name, ID);
						//writer.println("Sign-up Successful");
						serverGUI.addNotification("Sign-up Successful: " + name);
						pw.println("Sign-up Successful");
					}
				}
				else if("register".equals(command)){
					serverfunction.registery(name, ID, lName, fName);
				}
				else if("unregister".endsWith(command)){
					serverfunction.unregistery(name, ID, lName);
				}else if("search".equals(command)){
					ArrayList<String> peerList = new ArrayList<String>();
					//find the peer list that have that file
					peerList = serverfunction.search(fName);
					
					if(peerList.size() != 0){
						for(int i =0; i<peerList.size();i++){
							pw.println(peerList.get(i));
						}
					}
					
					pw.println("bye");
			
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
